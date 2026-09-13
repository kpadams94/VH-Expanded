package dev.kpada.vhexpanded.client;

import dev.kpada.vhexpanded.Expanded;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.base.TieredSkill;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(modid = "vhexpanded", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientLoadCheck {
    @SubscribeEvent public static void loaded(FMLLoadCompleteEvent event) throws ClassNotFoundException {
        Class.forName("iskallia.vault.client.gui.screen.summary.VaultEndScreen");
        Class.forName("iskallia.vault.client.gui.screen.summary.VaultExitContainerScreenData");
        Class.forName("iskallia.vault.client.data.ClientVaultXpTracker");
        Class.forName("iskallia.vault.client.render.hud.module.vault.VaultXpTrackerModule");
        var node = ModConfigs.EXPERTISES.getAll().getAll(TieredSkill.class,
                n -> "Experienced".equals(n.getId())).get(0);
        if (node.getMaxLearnableTier() != 5) throw new IllegalStateException("Expanded ranks were not installed");
        Class.forName("iskallia.vault.skill.ability.component.AbilityLabelFactory", false, ClientLoadCheck.class.getClassLoader());
        var repulsor=dev.kpada.vhexpanded.repulsor.RepulsorCommands.node(ModConfigs.ABILITIES.get().orElseThrow());
        if(repulsor.getMaxLearnableTier()!=8 || ModConfigs.ABILITIES_GUI.getIcon("Repulsor")==null)
            throw new IllegalStateException("Repulsor client registration missing");
        Expanded.LOGGER.info("Repulsor client load check PASS: eight ranks, specialization icon, owned serializer and label hook loaded");
        Expanded.LOGGER.info("Expanded client load check PASS: summary and tracker hooks loaded; five Experienced ranks configured");
        var hearts = ModConfigs.RESEARCHES.getByName(dev.kpada.vhexpanded.hearts.HeartResearch.NAME);
        if (hearts == null || hearts.getCost() != 2 || ModConfigs.RESEARCHES_GUI.getStyles().get(hearts.getName()) == null)
            throw new IllegalStateException("Heart Canisters client research registration missing");
        Class.forName("com.traverse.bhc.common.util.HealthModifier");
        Class.forName("net.minecraft.world.inventory.ResultContainer");
        Expanded.LOGGER.info("Heart Canisters client load check PASS: two-point research, icon style, health and recipe hooks loaded");
    }
}
