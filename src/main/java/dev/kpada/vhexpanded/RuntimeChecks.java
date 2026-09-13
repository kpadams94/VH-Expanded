package dev.kpada.vhexpanded;

import iskallia.vault.core.net.ArrayBitBuffer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.skill.base.TieredSkill;
import iskallia.vault.skill.expertise.type.ExperiencedExpertise;
import iskallia.vault.skill.tree.ExpertiseTree;
import iskallia.vault.world.data.PlayerExpertisesData;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.player.PlayerXpEvent;

/** Opt-in checks using detached test objects, never the operator's character or an actual run. */
public final class RuntimeChecks {
    private RuntimeChecks() {}
    private static void require(boolean ok, String name) {
        if (!ok) throw new IllegalStateException(name);
        Expanded.LOGGER.info("Expanded runtime PASS: {}", name);
    }
    public static int run(MinecraftServer server) throws Exception {
        // Force validation of the run boundary mixin even without an online player.
        Class.forName("iskallia.vault.world.data.VaultPlayerStats");
        var before = ModConfigs.EXPERTISES.getAll().writeJson().orElseThrow().deepCopy();
        ExperiencedConfig.merge();
        require(before.equals(ModConfigs.EXPERTISES.getAll().writeJson().orElseThrow()), "config merge is idempotent");
        ExpertiseTree tree = (ExpertiseTree) ModConfigs.EXPERTISES.getAll().copy();
        var node = tree.getAll(TieredSkill.class, n -> "Experienced".equals(n.getId())).get(0);
        require(node.getTiers().size() == 5 && node.getMaxLearnableTier() == 5, "five native ranks");
        SkillContext context = SkillContext.empty();
        context.setLearnPoints(5);
        for (int rank = 1; rank <= 5; rank++) {
            require(node.getLearnPointCost() == 1 && node.canLearn(context), "rank " + rank + " purchasable for one point");
            node.learn(context);
        }
        require(context.getLearnPoints() == 0 && !node.canLearn(context), "five points spent; sixth rank unavailable");
        ExpertiseTree nbtCopy = new ExpertiseTree();
        nbtCopy.readNbt(tree.writeNbt().orElseThrow());
        require(ExperiencedConfig.rank(nbtCopy) == 5, "rank survives native NBT serialization");
        var buffer = ArrayBitBuffer.empty();
        tree.writeBits(buffer);
        buffer.setPosition(0);
        ExpertiseTree networkCopy = new ExpertiseTree();
        networkCopy.readBits(buffer);
        require(ExperiencedConfig.rank(networkCopy) == 5, "rank survives native network serialization");
        for (int i = 0; i < 5; i++) node.regret(context);
        require(ExperiencedConfig.rank(tree) == 0 && context.getLearnPoints() == 5, "native regret resets rank and refunds five points");

        var multiplier = ModConfigs.LEVELS_META.getClass().getDeclaredField("expMultiplier");
        multiplier.setAccessible(true);
        float original = multiplier.getFloat(ModConfigs.LEVELS_META);
        UUID id = UUID.randomUUID();
        PlayerVaultStats stats = new PlayerVaultStats(id);
        try {
            multiplier.setFloat(ModConfigs.LEVELS_META, 1.0f);
            for (int rank = 0; rank <= 5; rank++) {
                stats.setLevelAndExp(server, 10, 0);
                try (var scope = new RunAwardScope(id, rank)) { stats.addVaultExp(server, 1000); }
                require(stats.getExp() == 1000 + 100 * rank, "native award rank " + rank);
            }
            stats.setLevelAndExp(server, 10, 0);
            multiplier.setFloat(ModConfigs.LEVELS_META, 2.0f);
            try (var scope = new RunAwardScope(id, 5)) { stats.addVaultExp(server, 1000); }
            require(stats.getExp() == 3000, "native global +100% then Experienced +50% gives 3000");
            multiplier.setFloat(ModConfigs.LEVELS_META, 1.0f);
            stats.setLevelAndExp(server, 10, 0);
            try (var scope = new RunAwardScope(id, 5)) { stats.addVaultExp(server, 101); }
            require(stats.getExp() == 151, "native fractional final bonus floors to 151");
            stats.setLevelAndExp(server, 10, 0);
            try (var scope = new RunAwardScope(id, 5)) { stats.addVaultExp(server, 0); }
            require(stats.getExp() == 0, "native zero stays zero");
            stats.addVaultExp(server, 1000);
            require(stats.getExp() == 1000, "ordinary helper outside run receives no bonus");
            var saved = stats.serializeNBT();
            PlayerVaultStats restored = new PlayerVaultStats(id);
            restored.deserializeNBT(saved);
            require(restored.getVaultLevel() == 10 && restored.getExp() == 1000, "native player XP NBT agrees with awarded XP");
            stats.setLevelAndExp(server, 100, 0);
            try (var scope = new RunAwardScope(id, 5)) { stats.addVaultExp(server, 1000); }
            require(stats.getVaultLevel() == 100 && stats.getExp() == 0, "native cap discards award");
        } finally { multiplier.setFloat(ModConfigs.LEVELS_META, original); }
        var player = FakePlayerFactory.getMinecraft(server.overworld());
        var orb = new ExperienceOrb(server.overworld(), 0, 0, 0, 7);
        var fakeTree = PlayerExpertisesData.get(server.overworld()).getExpertises(player);
        var originalTree = fakeTree.writeNbt().orElseThrow();
        try {
            var fakeNode = fakeTree.getAll(TieredSkill.class, n -> "Experienced".equals(n.getId())).get(0);
            SkillContext fakeContext = SkillContext.empty();
            fakeContext.setLearnPoints(5);
            while (fakeNode.getUnmodifiedTier() < 5) fakeNode.learn(fakeContext);
            require(ExperiencedConfig.rank(fakeTree) == 5, "orb test character has Experienced rank five");
            ExperiencedExpertise.onOrbPickup(new PlayerXpEvent.PickupXp(player, orb));
            require(orb.getValue() == 7, "rank-five orb handler leaves seven XP unchanged");
        } finally { fakeTree.readNbt(originalTree); }
        RunReceipt receipt = new RunReceipt(id, UUID.randomUUID(), 5, 1000, 1500, true);
        require(receipt.equals(RunReceipt.load(receipt.save())), "historical receipt NBT round trip");
        return 26;
    }
}
