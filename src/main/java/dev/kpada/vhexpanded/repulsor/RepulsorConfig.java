package dev.kpada.vhexpanded.repulsor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import iskallia.vault.config.AbilitiesGUIConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.base.SpecializedSkill;
import iskallia.vault.skill.base.TieredSkill;
import dev.kpada.vhexpanded.mixin.AbilityStylesAccessor;
import net.minecraft.resources.ResourceLocation;

public final class RepulsorConfig {
    public static final String ID = "Repulsor";
    // Native ability atlas filters to the_vault's namespace; this filename is addon-owned.
    public static final ResourceLocation ICON = new ResourceLocation("the_vault", "gui/abilities/vhexpanded_repulsor");
    private RepulsorConfig() {}
    public static JsonObject rank(JsonObject baseline, int level) {
        JsonObject j = baseline.deepCopy();
        j.addProperty("type", "vhexpanded:repulsor");
        j.addProperty("id", "vhexpanded_repulsor_" + level);
        tune(j,level);
        j.addProperty("present", false);
        return j;
    }
    private static void tune(JsonObject j,int level) {
        j.addProperty("radius", RepulsorBalance.barrierRadius(level));
        j.addProperty("repelRadius", RepulsorBalance.radius(level));
        j.addProperty("durationTicks", RepulsorBalance.duration(level));
        j.addProperty("cooldownTicks", RepulsorBalance.cooldown(level));
        j.addProperty("manaCost", RepulsorBalance.mana(level));
    }
    public static void loadTuning() {
        var path=net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR.get().resolve("vhexpanded-repulsor.json");
        try(var reader=java.nio.file.Files.exists(path)?java.nio.file.Files.newBufferedReader(path):
                new java.io.InputStreamReader(java.util.Objects.requireNonNull(RepulsorConfig.class.getResourceAsStream("/vhexpanded/repulsor.json")),java.nio.charset.StandardCharsets.UTF_8)) {
            JsonObject j=com.google.gson.JsonParser.parseReader(reader).getAsJsonObject();
            RepulsorBalance.configure(new RepulsorBalance.Tuning(j.get("radiusBase").getAsFloat(),j.get("radiusPerLevel").getAsFloat(),
                    j.has("barrierRadiusBase")?j.get("barrierRadiusBase").getAsFloat():1,
                    j.has("barrierRadiusStep")?j.get("barrierRadiusStep").getAsFloat():.5f,
                    j.has("barrierEveryLevels")?j.get("barrierEveryLevels").getAsInt():2,
                    j.get("durationBaseTicks").getAsInt(),j.get("durationStepTicks").getAsInt(),j.get("durationEveryLevels").getAsInt(),
                    j.get("cooldownInterceptTicks").getAsInt(),j.get("cooldownPerLevelTicks").getAsInt(),j.get("cooldownFloorTicks").getAsInt(),
                    j.get("manaBase").getAsFloat(),j.get("manaPerLevel").getAsFloat()));
        } catch(Exception e) { throw new IllegalStateException("Cannot load Repulsor tuning",e); }
    }
    /** Changes only tuning on existing objects, preserving ranks, selection, and running cooldowns. */
    public static void apply(iskallia.vault.skill.tree.AbilityTree tree) {
        for(var node:tree.getAll(RepulsorTiers.class,n->true)) {
            for(int i=0;i<node.getTiers().size();i++) {
                var ability=node.getTiers().get(i);
                var values=ability.writeJson().orElseThrow();
                tune(values,i+1);
                ability.readJson(values);
            }
        }
    }
    public static int reload(net.minecraft.server.MinecraftServer server) {
        loadTuning(); // Validate the entire file before touching a live tree.
        ModConfigs.ABILITIES.get().ifPresent(RepulsorConfig::apply);
        var data=iskallia.vault.world.data.PlayerAbilitiesData.get(server);
        for(var player:server.getPlayerList().getPlayers()) {
            var tree=data.getAbilities(player);
            apply(tree);
            tree.sync(iskallia.vault.skill.base.SkillContext.of(player));
        }
        data.setDirty();
        return server.getPlayerList().getPlayerCount();
    }
    public static void merge() {
        loadTuning();
        SpecializedSkill family = ModConfigs.ABILITIES.get().orElseThrow().getAll(SpecializedSkill.class,
                s -> "Mana_Shield".equals(s.getId())).stream().findFirst().orElseThrow();
        TieredSkill parent = (TieredSkill)family.getSpecialization(family.indexOf("Mana_Barrier"));
        JsonObject node = parent.writeJson().orElseThrow();
        node.addProperty("type", "vhexpanded:repulsor_tiers");
        node.addProperty("id", ID);
        node.addProperty("name", "Repulsor");
        node.addProperty("maxLearnableTier", 8);
        JsonArray ranks = new JsonArray();
        for (int i = 0; i < parent.getTiers().size(); i++) ranks.add(rank(parent.getTiers().get(i).writeJson().orElseThrow(), i + 1));
        node.add("tiers", ranks);
        JsonObject group = family.writeJson().orElseThrow();
        JsonArray choices = group.getAsJsonArray("specializations");
        boolean replaced = false;
        for (int i = 0; i < choices.size(); i++) if (ID.equals(choices.get(i).getAsJsonObject().get("id").getAsString())) {
            choices.set(i, node); replaced = true; break;
        }
        if (!replaced) choices.add(node);
        family.readJson(group);
        var style = new AbilitiesGUIConfig.SpecializationStyle(ICON);
        ModConfigs.ABILITIES_GUI.getStyles().get("Mana_Shield").getSpecializationStyles().put(ID, style);
        ((AbilityStylesAccessor)ModConfigs.ABILITIES_GUI).expanded$lookup().put(ID, style);
        ModConfigs.ABILITIES_GROUPS.getType("Mana_Barrier").ifPresent(type -> {
            var ids = ModConfigs.ABILITIES_GROUPS.getAbilitiesMatching(type);
            if (!ids.contains(ID)) ids.add(ID);
        });
    }
}
