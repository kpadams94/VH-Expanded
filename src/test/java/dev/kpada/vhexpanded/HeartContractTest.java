package dev.kpada.vhexpanded;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.*;
import java.util.jar.JarFile;
import static org.junit.jupiter.api.Assertions.*;

class HeartContractTest {
    @Test void qolIconsFormTwoAlignedRowsOfThree() throws Exception {
        var layout = JsonParser.parseString(Files.readString(Path.of("src/main/resources/vhexpanded/hearts/qol-layout.json"))).getAsJsonObject();
        Map<Integer, Set<Integer>> rows = new TreeMap<>();
        layout.entrySet().forEach(e -> {
            var xy = e.getValue().getAsJsonArray();
            rows.computeIfAbsent(xy.get(1).getAsInt(), y -> new TreeSet<>()).add(xy.get(0).getAsInt());
        });
        assertEquals(6, layout.size());
        assertEquals(2, rows.size());
        assertEquals(rows.values().iterator().next(), new ArrayList<>(rows.values()).get(1), "QoL rows must align vertically");
        rows.values().forEach(xs -> assertEquals(Set.of(390, 440, 490), xs));
    }
    private static String hash(Path path) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(path)));
    }
    @Test void actualBuildDependenciesMatchInspectedArtifacts() throws Exception {
        assertEquals("fada1a53f18a3fdc5199f759e634b364837559be987b5a8536d1da74aad5f988", hash(Path.of(System.getProperty("bhcJar"))));
        assertEquals("327b6a611cdd10be9d789e082cb7c91ef8afa822dcb3daf112aee71012fdfd13", hash(Path.of(System.getProperty("curiosJar"))));
        try (var jar = new JarFile(System.getProperty("bhcJar"))) {
            var node = new ClassNode();
            new ClassReader(jar.getInputStream(jar.getJarEntry("com/traverse/bhc/common/util/HealthModifier.class"))).accept(node, 0);
            assertEquals(1, node.methods.stream().filter(m -> m.name.equals("updatePlayerHealth")
                    && m.desc.equals("(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Z)V")).count());
        }
    }
    @Test void installedRecipesExactlyMatchUserExportAndReplaceEveryNativeRecipe() throws Exception {
        Path source = Path.of("docs/recipes/heart-canisters/source-export.json");
        assertEquals("8acdaca7d8e9e1a0e0f84b147e86d987558693176bb51370da00c8cd2210aec7", hash(source));
        var recipes = JsonParser.parseString(Files.readString(source)).getAsJsonObject().getAsJsonArray("recipes");
        assertEquals(12, recipes.size());
        Set<String> replacementPaths = new HashSet<>();
        for (var entry : recipes) {
            var json = entry.getAsJsonObject();
            Path path = Path.of("src/main/resources", json.get("path").getAsString());
            assertEquals(json.get("recipe"), JsonParser.parseString(Files.readString(path)), path.toString());
            if (json.get("action").getAsString().equals("replace")) replacementPaths.add(json.get("path").getAsString());
        }
        try (var archive = new JarFile(System.getProperty("bhcJar"))) {
            Set<String> nativePaths = new HashSet<>();
            archive.stream().filter(e -> e.getName().startsWith("data/bhc/recipes/") && e.getName().endsWith(".json"))
                    .forEach(e -> nativePaths.add(e.getName()));
            assertEquals(nativePaths, replacementPaths);
            assertEquals(8, nativePaths.size());
        }
        try (var files = Files.list(Path.of("src/main/resources/data/bhc/recipes"))) { assertEquals(12, files.count()); }
    }
    @Test void dropOverlayRetainsRequiredNestedStructure() throws Exception {
        var entries = JsonParser.parseString(Files.readString(Path.of("pack-overlay/config/bhc/drops.json"))).getAsJsonObject().getAsJsonObject("heartEntries");
        assertEquals(Set.of("red", "yellow", "green", "blue"), entries.keySet());
        for (var color : entries.entrySet()) {
            assertFalse(color.getValue().getAsJsonObject().entrySet().isEmpty());
            color.getValue().getAsJsonObject().entrySet().forEach(e -> assertEquals(0, e.getValue().getAsDouble()));
        }
    }
    @Test void lootFragmentIsTheApprovedPayload() throws Exception {
        var approved = JsonParser.parseString(Files.readString(Path.of("docs/recipes/heart-canisters/mod-box-fragment.json")));
        var installed = JsonParser.parseString(Files.readString(Path.of("src/main/resources/vhexpanded/hearts/mod-box.json")));
        assertEquals(approved, installed);
    }
}
