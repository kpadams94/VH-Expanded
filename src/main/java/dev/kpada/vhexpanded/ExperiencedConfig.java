package dev.kpada.vhexpanded;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.base.TieredSkill;
import iskallia.vault.skill.tree.ExpertiseTree;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class ExperiencedConfig {
    private ExperiencedConfig() {}

    public static void merge() {
        try (var reader = new InputStreamReader(Objects.requireNonNull(
                ExperiencedConfig.class.getResourceAsStream("/vhexpanded/experienced.json")), StandardCharsets.UTF_8)) {
            JsonObject owned = JsonParser.parseReader(reader).getAsJsonObject();
            var nodes = ModConfigs.EXPERTISES.getAll().getAll(TieredSkill.class,
                    node -> "Experienced".equals(node.getId()));
            if (nodes.size() != 1) throw new IllegalStateException("Expected exactly one Experienced expertise");
            TieredSkill node = nodes.get(0);
            JsonObject json = node.writeJson().orElseThrow();
            json.add("tiers", owned.get("tiers").deepCopy());
            json.add("maxLearnableTier", owned.get("maxLearnableTier"));
            node.readJson(json);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot install Expanded Experienced ranks", e);
        }
    }

    public static int rank(ExpertiseTree tree) {
        return tree.getAll(TieredSkill.class, node -> "Experienced".equals(node.getId())).stream()
                .mapToInt(TieredSkill::getUnmodifiedTier).map(rank -> Math.max(0, Math.min(5, rank)))
                .findFirst().orElse(0);
    }
}
