package dev.kpada.vhexpanded.repulsor;

import dev.kpada.vhexpanded.Expanded;
import iskallia.vault.core.world.storage.IZonedWorld;
import iskallia.vault.util.EntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;
import java.util.*;

public final class RepulsorBarrier {
    private static final DeferredRegister<Block> BLOCKS=DeferredRegister.create(ForgeRegistries.BLOCKS,"vhexpanded");
    private static final DeferredRegister<BlockEntityType<?>> TILES=DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES,"vhexpanded");
    public static final RegistryObject<Block> BLOCK=BLOCKS.register("repulsor_barrier",BarrierBlock::new);
    public static final RegistryObject<BlockEntityType<BarrierTile>> TILE=TILES.register("repulsor_barrier",
            ()->BlockEntityType.Builder.of(BarrierTile::new,BLOCK.get()).build(null));
    private RepulsorBarrier() {}
    public static void register(IEventBus bus) { BLOCKS.register(bus); TILES.register(bus); }
    public static void cast(ServerPlayer player, float radius, float repelRadius, int ticks) {
        if(radius<=0 || repelRadius<=0 || ticks<=0) return;
        var level=player.getLevel();
        Vec3 center=Vec3.atCenterOf(player.blockPosition());
        var bounds=BarrierGeometry.bounds(center,radius);
        var repelBounds=BarrierGeometry.bounds(center,repelRadius);
        int moved=0, obstructed=0;
        for(LivingEntity mob:level.getEntitiesOfClass(LivingEntity.class,repelBounds,EntityHelper.VAULT_TARGET_SELECTOR)) {
            if(BarrierPassage.passes(mob)) continue;
            Vec3 push=BarrierGeometry.displacement(center,repelRadius,mob.position(),mob.getBbWidth()/2d);
            Vec3 before=mob.getDeltaMovement();
            // Invoke the native resistance/Forge cancellation path. Consume its horizontal impulse
            // with collision-resolved movement once, instead of letting excess velocity fling mobs.
            mob.knockback(push.length(),-push.x,-push.z);
            Vec3 after=mob.getDeltaMovement();
            if(!after.equals(before)) {
                mob.move(MoverType.SELF,new Vec3(after.x-before.x/2,0,after.z-before.z/2));
                mob.setDeltaMovement(before.x/2,after.y,before.z/2);
                mob.hasImpulse=true;
            }
            if(mob.getBoundingBox().intersects(repelBounds)) obstructed++; else moved++;
        }
        UUID cast=UUID.randomUUID();
        var part=new BarrierTile.Part(cast,player.getUUID(),level.getGameTime()+ticks,bounds);
        Set<BlockPos> cells=new HashSet<>();
        // Enumerate only the six surfaces, not the cube's volume.
        for(var face:BarrierGeometry.faces(bounds)) for(BlockPos p:BlockPos.betweenClosed(
                new BlockPos(face.minX,face.minY,face.minZ),new BlockPos(Math.nextDown(face.maxX),Math.nextDown(face.maxY),Math.nextDown(face.maxZ)))) cells.add(p.immutable());
        int placed=0;
        for(BlockPos pos:cells) {
            if(!level.hasChunkAt(pos) || level.isOutsideBuildHeight(pos)) continue;
            var old=level.getBlockState(pos);
            if(!old.is(BLOCK.get()) && !old.isAir() && !old.getMaterial().isReplaceable()) continue;
            IZonedWorld.runWithBypass(level,true,()-> {
                if(!old.is(BLOCK.get())) level.setBlockAndUpdate(pos,BLOCK.get().defaultBlockState());
                if(level.getBlockEntity(pos) instanceof BarrierTile tile) tile.add(part);
            });
            placed++;
        }
        Expanded.LOGGER.info("Repulsor cast {}: barrier radius {}, repel radius {}, duration {} ticks, cleared {}, resistant/obstructed {}, cells {}",cast,radius,repelRadius,ticks,moved,obstructed,placed);
    }
}
