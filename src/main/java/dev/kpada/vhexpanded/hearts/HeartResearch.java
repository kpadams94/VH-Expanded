package dev.kpada.vhexpanded.hearts;

import com.google.gson.JsonParser;
import iskallia.vault.config.ModBoxConfig;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.client.gui.helper.SkillFrame;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.research.StageManager;
import iskallia.vault.research.type.CustomResearch;
import iskallia.vault.research.Restrictions;
import iskallia.vault.core.world.data.item.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/** Owns one research and one native loot pool; never replaces a pack configuration. */
public final class HeartResearch {
    public static final String NAME = "Baubley Heart Canisters";
    private HeartResearch() {}

    public static boolean unlocked(Player player) {
        return StageManager.getResearchTree(player).isResearched(NAME);
    }

    public static void merge() {
        HeartVaultRules.merge();
        var qol = Objects.requireNonNull(ModConfigs.RESEARCH_GROUPS.getGroups().get("QoL"), "Missing pinned QoL group");
        // In this pack every incoming QoL modifier is zero. Refuse silent price changes.
        for (var group : ModConfigs.RESEARCH_GROUPS.getGroups().values()) {
            if (group.getGroupIncreasedResearchCost("QoL") != 0)
                throw new IllegalStateException("BHC requires the pinned zero-modifier QoL research cost");
        }
        ModConfigs.RESEARCHES.MOD_RESEARCHES.removeIf(r -> NAME.equals(r.getName()));
        ModConfigs.RESEARCHES.CUSTOM_RESEARCHES.removeIf(r -> NAME.equals(r.getName()));
        var research = new CustomResearch(NAME, 2);
        // Native output gates cover manual and supported network-owner crafting, including god_apple.
        // CRAFTABILITY alone leaves vanilla loot/use and miniature-heart healing untouched.
        for (String item : java.util.List.of("bhc:canister", "bhc:heart_amulet", "bhc:red_heart", "bhc:yellow_heart",
                "bhc:green_heart", "bhc:blue_heart", "bhc:red_heart_canister", "bhc:yellow_heart_canister",
                "bhc:green_heart_canister", "bhc:blue_heart_canister", "bhc:relic_apple", "minecraft:enchanted_golden_apple"))
            research.getItemRestrictions().put(ItemPredicate.of(item, false).orElseThrow(),
                    Restrictions.forMods().set(Restrictions.Type.CRAFTABILITY, true));
        ModConfigs.RESEARCHES.CUSTOM_RESEARCHES.add(research);
        qol.getResearch().removeIf(NAME::equals);
        qol.getResearch().add(NAME);
        ModConfigs.RESEARCHES_GUI.getStyles().put(NAME, new SkillStyle(505, 160,
                new ResourceLocation("the_vault", "gui/researches/vhexpanded_hearts"), SkillFrame.RECTANGULAR));
        try (var reader = new InputStreamReader(Objects.requireNonNull(HeartResearch.class
                .getResourceAsStream("/vhexpanded/hearts/qol-layout.json")), StandardCharsets.UTF_8)) {
            for (var entry : JsonParser.parseReader(reader).getAsJsonObject().entrySet()) {
                var style = Objects.requireNonNull(ModConfigs.RESEARCHES_GUI.getStyles().get(entry.getKey()));
                style.x = entry.getValue().getAsJsonArray().get(0).getAsInt();
                style.y = entry.getValue().getAsJsonArray().get(1).getAsInt();
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot arrange QoL research icons", e);
        }
        try (var reader = new InputStreamReader(Objects.requireNonNull(HeartResearch.class
                .getResourceAsStream("/vhexpanded/hearts/mod-box.json")), StandardCharsets.UTF_8)) {
            var json = JsonParser.parseReader(reader).getAsJsonObject().getAsJsonObject("POOL").getAsJsonObject(NAME);
            var pool = new ModBoxConfig.ModPool();
            pool.weight = json.get("weight").getAsInt();
            for (var element : json.getAsJsonArray("entries")) {
                var entry = element.getAsJsonObject();
                var value = entry.getAsJsonObject("value");
                var product = new ProductEntry();
                product.id = value.get("id").getAsString();
                product.amountMin = value.get("amountMin").getAsInt();
                product.amountMax = value.get("amountMax").getAsInt();
                pool.entries.add(product, entry.get("weight").getAsInt());
            }
            ModConfigs.MOD_BOX.POOL.put(NAME, pool);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot merge Heart Canisters Mod Box pool", e);
        }
    }
}
