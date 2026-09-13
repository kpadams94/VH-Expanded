package dev.kpada.vhexpanded.repulsor;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import iskallia.vault.gear.VaultGearState;
import iskallia.vault.gear.attribute.VaultGearModifier;
import iskallia.vault.gear.attribute.ability.AbilityLevelAttribute;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.*;
import iskallia.vault.mana.*;
import iskallia.vault.skill.base.*;
import iskallia.vault.skill.tree.AbilityTree;
import iskallia.vault.util.calc.*;
import iskallia.vault.world.data.*;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import java.util.Locale;

public final class RepulsorCommands {
    private RepulsorCommands() {}
    public static RepulsorTiers node(AbilityTree tree) { return tree.getAll(RepulsorTiers.class,n->true).stream().findFirst().orElseThrow(); }
    public static ItemStack helmet(int bonus, boolean sustain) {
        ItemStack stack=new ItemStack(ModItems.HELMET);
        var data=VaultGearData.read(stack);
        data.setState(VaultGearState.IDENTIFIED); data.setItemLevel(0);
        data.addModifier(VaultGearModifier.AffixType.PREFIX,new VaultGearModifier<>(ModGearAttributes.ABILITY_LEVEL,new AbilityLevelAttribute("Mana_Shield",bonus)));
        if(sustain) {
            data.addModifier(VaultGearModifier.AffixType.SUFFIX,new VaultGearModifier<>(ModGearAttributes.COOLDOWN_REDUCTION,.8f));
            data.addModifier(VaultGearModifier.AffixType.SUFFIX,new VaultGearModifier<>(ModGearAttributes.COOLDOWN_REDUCTION_CAP,.8f));
            data.addModifier(VaultGearModifier.AffixType.SUFFIX,new VaultGearModifier<>(ModGearAttributes.MANA_ADDITIVE,10000));
        }
        data.write(stack);
        stack.setHoverName(new TextComponent("Repulsor TEST: +"+bonus+" Mana Shield"+(sustain?", 80% CDR, extra mana":"")));
        return stack;
    }
    public static String status(ServerPlayer p) {
        var n=node(PlayerAbilitiesData.get(p.getLevel()).getAbilities(p));
        var a=(RepulsorAbility)n.getChild();
        if(a==null) return "Repulsor rank 0/8. Select it under Mana Shield and buy a rank.";
        int cd=CooldownHelper.adjustCooldown(p,a,a.getCooldownTicks());
        return String.format(Locale.ROOT,"Repulsor purchased %d/8, effective L%d; barrier radius %.2f; repel radius %.2f; duration %.2fs; cooldown %.2fs (%d ticks); cost %.2f; mana %.2f/%.2f; remaining cooldown %d ticks",
                n.getUnmodifiedTier(),n.getActualTier(),a.getRadius(p),a.getRepelRadius(p),a.getDurationTicks()/20d,cd/20d,cd,
                ManaCostHelper.adjustManaCost(p,a,a.getManaCost()),Mana.get(p),Mana.getMax(p),n.getTreeCooldown().map(c->c.remainingTicks).orElse(0));
    }
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("vhexpanded").requires(s->s.hasPermission(2))
                .then(Commands.literal("repulsor")
                .then(Commands.literal("reload").executes(c->{
                    try {
                        int players=RepulsorConfig.reload(c.getSource().getServer());
                        c.getSource().sendSuccess(new TextComponent("Repulsor tuning reloaded for "+players+" online player(s). Reopen the ability menu. Existing barriers and running cooldowns keep their original timing."),false);
                        return 1;
                    } catch(Exception e) {
                        dev.kpada.vhexpanded.Expanded.LOGGER.error("Repulsor tuning reload failed",e);
                        c.getSource().sendFailure(new TextComponent("Repulsor reload failed; check config/vhexpanded-repulsor.json and latest.log: "+e.getMessage()));
                        return 0;
                    }
                }))
                .then(Commands.literal("selftest").executes(c->{
                    var source=c.getSource(); var world=source.getServer().overworld();
                    boolean alreadyForced=world.getForcedChunks().contains(net.minecraft.world.level.ChunkPos.asLong(0,0));
                    if(!alreadyForced)world.setChunkForced(0,0,true);
                    source.sendSuccess(new TextComponent("Preparing temporary Repulsor runtime checks..."),false);
                    iskallia.vault.util.ServerScheduler.INSTANCE.schedule(20,()-> {
                        try { int count=RepulsorRuntimeChecks.run(source.getServer());
                            source.sendSuccess(new TextComponent("Repulsor runtime checks PASSED: "+count),false);
                        } catch(Exception e) { dev.kpada.vhexpanded.Expanded.LOGGER.error("Repulsor checks FAILED",e);
                            source.sendFailure(new TextComponent("Repulsor checks FAILED: "+e));
                        } finally { if(!alreadyForced)world.setChunkForced(0,0,false); }
                    });
                    return 1;
                }))
                .then(Commands.literal("points").executes(c->{
                    var p=c.getSource().getPlayerOrException(); var d=PlayerVaultStatsData.get(p.getLevel());
                    d.getVaultStats(p).addSkillPoints(16).sync(p.getServer()); d.setDirty();
                    c.getSource().sendSuccess(new TextComponent("Added 16 skill points. Select Repulsor under Mana Shield and buy ranks normally."),false); return 1;
                }))
                .then(Commands.literal("status").executes(c->{c.getSource().sendSuccess(new TextComponent(status(c.getSource().getPlayerOrException())),false);return 1;}))
                .then(Commands.literal("mana").then(Commands.argument("amount",IntegerArgumentType.integer(0,100000)).executes(c->{
                    var p=c.getSource().getPlayerOrException(); Mana.set(p,ManaAction.SYSTEM,IntegerArgumentType.getInteger(c,"amount"));
                    c.getSource().sendSuccess(new TextComponent("Mana: "+Mana.get(p)+"/"+Mana.getMax(p)),false); return 1;
                })))
                .then(Commands.literal("gear").then(Commands.argument("bonusLevels",IntegerArgumentType.integer(0,64)).executes(c->{
                    var p=c.getSource().getPlayerOrException(); p.getInventory().placeItemBackInInventory(helmet(IntegerArgumentType.getInteger(c,"bonusLevels"),false));
                    c.getSource().sendSuccess(new TextComponent("Test helmet added. Equip it to apply native Mana Shield bonus levels. Remove it to undo."),false);return 1;
                })))
                .then(Commands.literal("sustain").executes(c->{
                    var p=c.getSource().getPlayerOrException(); p.getInventory().placeItemBackInInventory(helmet(9,true));
                    c.getSource().sendSuccess(new TextComponent("Sustain test helmet added: +9 family levels, 80% CDR and extra mana. Equip at purchased rank 8, then check status. Remove it to undo."),false);return 1;
                }))));
    }
}
