package dev.kpada.vhexpanded;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.jar.JarFile;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/** Checks actual shipped bytecode, not a stubbed model of Vault internals. */
class PinnedHooksTest {
    @Test void repulsorNativeIntegrationContract() throws Exception {
        assertNotNull(method("skill/base/Skill$Adapter", "<init>"));
        assertNotNull(method("config/AbilitiesDescriptionsConfig", "getCurrent"));
        assertNotNull(method("config/AbilitiesDescriptionsConfig", "getNext"));
        assertNotNull(method("skill/ability/component/AbilityLabelFactory", "create"));
        assertNotNull(method("skill/ability/effect/ManaBarrierAbility", "sendBlockUpdatesToClient"));
        var action=method("skill/ability/effect/spi/core/InstantAbility", "onAction");
        assertTrue(action.instructions.size()>0);
        assertEquals(1,calls(method("skill/ability/effect/spi/core/InstantManaAbility", "lambda$doActionPost$1"),
                "util/calc/ManaCostHelper","adjustManaCostForPayment"));
    }
    private final Path jar = Path.of(System.getProperty("vaultJar"));
    private ClassNode read(String name) throws Exception {
        try (var archive = new JarFile(jar.toFile())) {
            ClassNode node = new ClassNode();
            new ClassReader(archive.getInputStream(archive.getJarEntry("iskallia/vault/" + name + ".class"))).accept(node, 0);
            return node;
        }
    }
    private MethodNode method(String cls, String method) throws Exception {
        return read(cls).methods.stream().filter(m -> m.name.equals(method)).findFirst().orElseThrow();
    }
    private long calls(MethodNode method, String owner, String name) {
        long count = 0;
        for (var insn : method.instructions) if (insn instanceof MethodInsnNode call
                && call.owner.equals("iskallia/vault/" + owner) && call.name.equals(name)) count++;
        return count;
    }
    @Test void dependencyIsExactlyPinnedArtifact() throws Exception {
        assertEquals("fc6adfeb76071d61e633027334fc95e8efb7faf8a4cb57d646f8176b4f75390b",
                HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(jar))));
    }
    @Test void isolatedRunCallAndSeparateDollCompanionCallsExist() throws Exception {
        var consume = method("world/data/VaultPlayerStats", "consume");
        assertEquals(1, calls(consume, "world/data/PlayerVaultStatsData", "addVaultExp"));
        assertEquals(1, calls(consume, "item/VaultDollItem", "onVaultCompletion"));
        assertEquals(1, calls(consume, "item/CompanionItem", "grantVaultCompletionXP"));
        assertEquals(1, calls(method("world/data/VaultPlayerStats", "prompt"), "world/data/VaultPlayerStats", "getSnapshot"));
    }
    @Test void nativeGlobalMultiplierIsFollowedByExactlyOneAccumulationWrite() throws Exception {
        var method = method("skill/PlayerVaultStats", "addVaultExp");
        boolean inSlice = false;
        int writes = 0;
        for (var insn : method.instructions) {
            if (insn instanceof MethodInsnNode call && call.name.equals("getExpMultiplier")) inSlice = true;
            if (insn instanceof MethodInsnNode call && call.name.equals("getGreedTierXpMultiplier")) inSlice = false;
            if (inSlice && insn instanceof FieldInsnNode field && field.getOpcode() == Opcodes.PUTFIELD) {
                assertEquals("exp", field.name);
                writes++;
            }
        }
        assertEquals(1, writes);
    }
    @Test void pinnedClientSummaryAndTrackingHooksExist() throws Exception {
        String summary = "client/gui/screen/summary/VaultEndScreen";
        long constructorCalls = 0;
        for (var m : read(summary).methods) if (m.name.equals("<init>"))
            constructorCalls += calls(m, "client/gui/screen/summary/VaultExitContainerScreenData", "applyGreedXpBonus");
        assertEquals(1, constructorCalls);
        var fullConstructor = read(summary).methods.stream().filter(m -> m.name.equals("<init>")
                && m.desc.equals("(Liskallia/vault/core/vault/stat/VaultSnapshot;Lnet/minecraft/network/chat/Component;Ljava/util/UUID;ZZ)V"))
                .findFirst().orElseThrow();
        assertEquals(1, calls(fullConstructor, "client/gui/screen/summary/VaultExitContainerScreenData", "applyGreedXpBonus"));
        assertEquals(1, calls(method(summary, "lambda$new$26"),
                "client/gui/screen/summary/VaultExitContainerScreenData", "applyGreedXpBonus"));
        assertEquals("(Liskallia/vault/client/gui/screen/summary/VaultExitContainerScreenData;Liskallia/vault/core/vault/Vault;)Ljava/util/List;",
                method(summary, "lambda$new$23").desc);
        assertEquals("(Liskallia/vault/core/vault/Vault;Liskallia/vault/core/vault/stat/StatCollector;)V",
                method("client/data/ClientVaultXpTracker", "updateBreakdown").desc);
        boolean labelFound = false;
        for (var insn : method("client/render/hud/module/vault/VaultXpTrackerModule", "buildLines").instructions)
            if (insn instanceof LdcInsnNode constant && "Vault XP: %s".equals(constant.cst)) labelFound = true;
        assertTrue(labelFound, "live tracker heading is the pinned format string");
    }
}
