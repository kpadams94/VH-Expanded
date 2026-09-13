package dev.kpada.vhexpanded.repulsor;

import dev.kpada.vhexpanded.Expanded;
import iskallia.vault.core.data.adapter.Adapters;
import iskallia.vault.core.net.ArrayBitBuffer;
import iskallia.vault.entity.entity.*;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.mana.*;
import iskallia.vault.skill.base.*;
import iskallia.vault.skill.tree.AbilityTree;
import iskallia.vault.snapshot.AttributeSnapshotHelper;
import iskallia.vault.util.calc.*;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.util.FakePlayerFactory;
import java.util.*;

/** Opt-in native-runtime checks. Temporary arena is at build ceiling, and restored in finally. */
public final class RepulsorRuntimeChecks {
    private static int count;
    private static void check(boolean good,String label) {
        if(!good) throw new IllegalStateException(label);
        count++; Expanded.LOGGER.info("Repulsor runtime PASS: {}",label);
    }
    public static int run(MinecraftServer server) {
        count=0;
        var config=ModConfigs.ABILITIES.get().orElseThrow();
        var before=config.writeJson().orElseThrow().deepCopy();
        RepulsorConfig.merge(); RepulsorConfig.merge();
        check(before.equals(config.writeJson().orElseThrow()),"config merge twice is idempotent; original family retained");
        AbilityTree tree=config.copy();
        var node=RepulsorCommands.node(tree);
        var family=(SpecializedSkill)node.getParent();
        check(family.indexOf("Mana_Barrier")>=0 && family.indexOf("Mana_Shield_Base")>=0,"both native family choices survive");
        SkillContext context=SkillContext.empty(); context.setLearnPoints(8);
        family.specialize("Repulsor",context);
        for(int i=1;i<=8;i++) { check(node.canLearn(context)&&node.getLearnPointCost()==1,"normal purchase rank "+i); node.learn(context); }
        check(!node.canLearn(context)&&context.getLearnPoints()==0,"eight ranks spend eight points, ninth unavailable");
        for(int level:new int[]{1,3,4,7,8,12,15,16,17,20,31,64}) {
            var a=(RepulsorAbility)node.getChild(level);
            check(a.getUnmodifiedRadius()==RepulsorBalance.barrierRadius(level)&&a.getUnmodifiedRepelRadius()==RepulsorBalance.radius(level)&&a.getDurationTicks()==RepulsorBalance.duration(level)
                    &&a.getManaCost()==RepulsorBalance.mana(level)&&a.getCooldownTicks()==RepulsorBalance.cooldown(level),"native serialized row L"+level);
        }
        var nbt=Adapters.SKILL.readNbt(Adapters.SKILL.writeNbt(tree).orElseThrow()).orElseThrow();
        var bits=ArrayBitBuffer.empty(); Adapters.SKILL.writeBits(tree,bits);bits.setPosition(0);
        var network=Adapters.SKILL.readBits(bits).orElseThrow();
        check(RepulsorCommands.node((AbilityTree)nbt).getUnmodifiedTier()==8,"native NBT keeps owned type and rank");
        check(RepulsorCommands.node((AbilityTree)network).getUnmodifiedTier()==8,"native network keeps owned type and rank");
        var roundTrip=(RepulsorAbility)RepulsorCommands.node((AbilityTree)network).getChild(8);
        check(roundTrip.getUnmodifiedRadius()==2.5f&&roundTrip.getUnmodifiedRepelRadius()==4.5f,"network retains distinct barrier and repel radii");
        var legacyTag=roundTrip.writeNbt().orElseThrow();legacyTag.remove("repelRadius");
        var legacy=new RepulsorAbility();legacy.readNbt(legacyTag);
        check(legacy.getUnmodifiedRepelRadius()==legacy.getUnmodifiedRadius(),"previous-version saved ability remains readable before config merge");
        check(RepulsorCommands.node(tree.copy()).getChild(65) instanceof RepulsorAbility,"copy retains dynamic overlevel type");
        for(int i=0;i<8;i++) node.regret(context);
        check(node.getUnmodifiedTier()==0&&context.getLearnPoints()==8,"native refund removes rank and returns eight points");
        check(ModConfigs.ABILITIES_GUI.getIcon("Repulsor").equals(RepulsorConfig.ICON),"icon lookup registered");
        check(ModConfigs.ABILITIES_GROUPS.getType("Repulsor").equals(ModConfigs.ABILITIES_GROUPS.getType("Mana_Barrier")),"native ability group inherited");

        var center=new Vec3(.5,.5,.5);
        var small=BarrierGeometry.bounds(center,1); var large=BarrierGeometry.bounds(center,1.5);
        check(BarrierGeometry.at(small,new BlockPos(1,0,0)).bounds().maxX==.5
                &&BarrierGeometry.at(large,new BlockPos(1,0,0)).bounds().maxX==1,"half-block rank changes collision geometry");
        for(Vec3 target:List.of(center,new Vec3(1,.5,1),new Vec3(-.2,.5,.5))) {
            Vec3 end=target.add(BarrierGeometry.displacement(center,1,target,.3));
            check(!small.intersects(new AABB(end.x-.3,end.y,end.z-.3,end.x+.3,end.y+1.8,end.z+.3)),"pulse destination clears cube including target width");
        }
        UUID owner=UUID.randomUUID(); BlockPos cell=new BlockPos(1,0,0);
        var tile=new BarrierTile(cell,RepulsorBarrier.BLOCK.get().defaultBlockState());
        tile.add(new BarrierTile.Part(UUID.randomUUID(),owner,10,small));
        tile.add(new BarrierTile.Part(UUID.randomUUID(),owner,20,large));
        var saved=tile.saveWithoutMetadata(); var loaded=new BarrierTile(cell,tile.getBlockState()); loaded.load(saved);
        check(loaded.parts().size()==2,"overlap ownership round trips through block NBT");
        loaded.expire(10);check(loaded.parts().size()==1&&!loaded.shape().isEmpty(),"older expiry preserves newer collision");
        loaded.expire(20);check(loaded.parts().isEmpty()&&loaded.shape().isEmpty(),"final expiry clears collision");
        tile.removeOwner(owner);check(tile.parts().isEmpty(),"owner cleanup removes all owned contributions");

        worldChecks(server);
        return count;
    }
    private static void worldChecks(MinecraftServer server) {
        var world=server.overworld();
        var player=new net.minecraftforge.common.util.FakePlayer(world,new com.mojang.authlib.GameProfile(UUID.randomUUID(),"[RepulsorTest]")) {
            @Override public Vec3 position() { return new Vec3(getX(),getY(),getZ()); }
            @Override public BlockPos blockPosition() { return new BlockPos(getX(),getY(),getZ()); }
        };
        Vec3 previous=player.position(); var oldHelmet=player.getItemBySlot(EquipmentSlot.HEAD);
        float oldMana=Mana.get(player);
        var data=PlayerAbilitiesData.get(world); var oldTree=data.getAbilities(player);
        int y=world.getMaxBuildHeight()-16;
        var center=new Vec3(8.5,y+.5,8.5);
        var region=BlockPos.betweenClosed(new BlockPos(2,y-6,2),new BlockPos(14,y+6,14));
        world.getChunk(0,0);
        if(!world.hasChunkAt(new BlockPos(8,y,8))) throw new IllegalStateException("Load the test chunk first: /forceload add 0 0, wait a moment, then retry. Remove with /forceload remove 0 0 afterward.");
        for(var p:region) if(!world.isEmptyBlock(p)) throw new IllegalStateException("Runtime arena occupied near 8 "+y+" 8; leave this area empty or use another disposable world");
        List<LivingEntity> spawned=new ArrayList<>();
        try {
            player.setPos(8.5,y,8.5); player.setItemSlot(EquipmentSlot.HEAD,net.minecraft.world.item.ItemStack.EMPTY);
            AttributeSnapshotHelper.getInstance().refreshSnapshot(player);
            AbilityTree tree=ModConfigs.ABILITIES.get().orElseThrow().copy();
            data.setAbilities(player,tree); var node=RepulsorCommands.node(tree);
            SkillContext learn=SkillContext.empty();learn.setLearnPoints(8);
            ((SpecializedSkill)node.getParent()).specialize("Repulsor",learn);
            for(int i=0;i<8;i++)node.learn(learn);
            var a=(RepulsorAbility)node.getChild(); var context=SkillContext.of(player);
            for(double offset:new double[]{.75,3.75}) {
                Zombie mob=new Zombie(world); mob.setPos(center.x+offset,y,center.z);mob.setNoAi(true); mob.setNoGravity(true);
                world.addFreshEntity(mob);spawned.add(mob);
            }
            Zombie resistant=new Zombie(world);resistant.setPos(8.5,y,9.5);resistant.setNoAi(true);resistant.setNoGravity(true);
            resistant.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);world.addFreshEntity(resistant);spawned.add(resistant);
            Mana.set(player,ManaAction.SYSTEM,0);
            Vec3 unmoved=spawned.get(0).position(); a.onKeyUp(context);
            check(spawned.get(0).position().equals(unmoved)&&!world.getBlockState(new BlockPos(10,y,8)).is(RepulsorBarrier.BLOCK.get()),"insufficient mana produces no pulse or barrier");
            Mana.set(player,ManaAction.SYSTEM,100);
            float hp=spawned.get(0).getHealth();a.onKeyUp(context);
            var bounds=BarrierGeometry.bounds(center,4.5);
            check(!spawned.get(0).getBoundingBox().intersects(bounds)&&!spawned.get(1).getBoundingBox().intersects(bounds),"real cast clears near and edge mobs in open space");
            check(spawned.get(0).getHealth()==hp,"real pulse adds no damage");
            check(resistant.position().equals(new Vec3(8.5,y,9.5)),"native full knockback resistance preserved");
            check(Math.abs(Mana.get(player)-71)<.001,"successful rank-eight cast pays 29 mana once");
            check(a.getCooldown().orElseThrow().remainingDelayTicks==0,"cooldown begins immediately with no barrier delay");
            float paid=Mana.get(player);a.onKeyUp(context);check(Mana.get(player)==paid,"cooldown rejects a second charge");
            Vec3 from=new Vec3(8.5,y+.25,8.5),to=from.add(7,0,0);
            Arrow arrow=new Arrow(world,player); VaultThrownJavelin javelin=new VaultThrownJavelin(world,player);
            for(Entity e:List.of(arrow,new Snowball(world,player)))
                check(world.clip(new ClipContext(from,to,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,e)).getType()==HitResult.Type.BLOCK,"ordinary player-owned "+e.getType().getRegistryName()+" blocked");
            for(Entity e:List.of(player,javelin,new VaultFireball(world,player)))
                check(world.clip(new ClipContext(from,to,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,e)).getType()==HitResult.Type.MISS,"player/ability "+e.getType().getRegistryName()+" passes");
            var wall=new BlockPos(10,y,8);
            check(!world.getBlockState(wall).getCollisionShape(world,wall,CollisionContext.of(resistant)).isEmpty(),"real barrier cell physically collides with mobs");
            check(!world.getBlockState(new BlockPos(12,y,8)).is(RepulsorBarrier.BLOCK.get()),"rank-eight barrier ends at 2.5 while repel still clears to 4.5");
            var originalCooldown=a.getCooldown().orElseThrow().remainingTicks;
            var originalMana=Mana.get(player);
            var originalPart=((BarrierTile)world.getBlockEntity(wall)).parts().iterator().next();
            try {
                RepulsorBalance.configure(new RepulsorBalance.Tuning(1,.5f,1,1,2,10,10,4,400,20,80,15,1));
                RepulsorConfig.apply(tree);
                check(a.getUnmodifiedRadius()==4&&a.getUnmodifiedRepelRadius()==4.5f&&a.getManaCost()==22&&a.getCooldownTicks()==240,
                        "live retuning changes future cast values on existing ability objects");
                check(node.getUnmodifiedTier()==8&&familySelected(node)&&a.isUnlocked()&&a.getCooldown().orElseThrow().remainingTicks==originalCooldown&&Mana.get(player)==originalMana,
                        "live retuning preserves purchased rank, specialization, running cooldown and mana");
                check(((BarrierTile)world.getBlockEntity(wall)).parts().contains(originalPart),"live retuning leaves existing barrier geometry and expiry intact");
            } finally {
                RepulsorConfig.loadTuning();RepulsorConfig.apply(tree);
            }
            player.setPos(2.5,y,2.5);
            check(world.getBlockState(wall).is(RepulsorBarrier.BLOCK.get()),"moving caster leaves the barrier in place");
            player.setItemSlot(EquipmentSlot.HEAD,RepulsorCommands.helmet(9,true));
            AttributeSnapshotHelper.getInstance().refreshSnapshot(player);SkillBonusTierCache.invalidate(player.getUUID());node.onTick(context);
            check(node.getActualTier()==17,"real native gear grants nine overlevels to purchased rank eight");
            a=(RepulsorAbility)node.getChild();
            check(CooldownHelper.adjustCooldown(player,a,a.getCooldownTicks())==16,"real native 80 percent CDR reduces L17 to 16 ticks");
        } finally {
            for(var entity:spawned)entity.discard();
            for(var p:region) if(world.getBlockState(p).is(RepulsorBarrier.BLOCK.get()))world.removeBlock(p,false);
            player.setItemSlot(EquipmentSlot.HEAD,oldHelmet);player.setPos(previous);
            data.setAbilities(player,oldTree);AttributeSnapshotHelper.getInstance().refreshSnapshot(player);SkillBonusTierCache.invalidate(player.getUUID());
            Mana.set(player,ManaAction.SYSTEM,oldMana);
        }
    }
    private static boolean familySelected(RepulsorTiers node) {
        return ((SpecializedSkill)node.getParent()).getSpecialization()==node;
    }
}
