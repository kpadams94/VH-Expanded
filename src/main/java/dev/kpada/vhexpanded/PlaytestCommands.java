package dev.kpada.vhexpanded;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;

/** Explicit operator-only helpers for disposable-world acceptance testing. */
public final class PlaytestCommands {
    private PlaytestCommands() {}
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("vhexpanded").requires(s -> s.hasPermission(2))
                .then(Commands.literal("selftest").executes(c -> {
                    try {
                        int count = RuntimeChecks.run(c.getSource().getServer());
                        c.getSource().sendSuccess(new TextComponent("Expanded runtime checks PASSED: " + count), false);
                        return count;
                    } catch (Exception e) {
                        Expanded.LOGGER.error("Expanded runtime checks FAILED", e);
                        c.getSource().sendFailure(new TextComponent("Expanded runtime checks FAILED: " + e));
                        return 0;
                    }
                }))
                .then(Commands.literal("status").executes(c -> {
                    var p = c.getSource().getPlayerOrException();
                    var s = PlayerVaultStatsData.get(p.getLevel()).getVaultStats(p);
                    c.getSource().sendSuccess(new TextComponent("Experienced rank " + RunAwards.rank(p)
                            + "/5; vault level " + s.getVaultUncappedLevel() + "; XP " + s.getExp()
                            + "; next level " + s.getExpNeededToNextLevel()
                            + "; unspent expertise points " + s.getUnspentExpertisePoints()), false);
                    return 1;
                }))
                .then(Commands.literal("points").executes(c -> {
                    var p = c.getSource().getPlayerOrException();
                    var data = PlayerVaultStatsData.get(p.getLevel());
                    var s = data.getVaultStats(p);
                    s.addExpertisePoints(5);
                    s.sync(p.getServer());
                    data.setDirty();
                    c.getSource().sendSuccess(new TextComponent("Added 5 expertise points for testing. Buy Experienced in the normal expertise screen."), false);
                    return 1;
                }))
                .then(Commands.literal("testaward").then(Commands.argument("xpBeforeGlobalMultiplier", IntegerArgumentType.integer(0, 1000000))
                        .executes(c -> {
                            var p = c.getSource().getPlayerOrException();
                            int input = IntegerArgumentType.getInteger(c, "xpBeforeGlobalMultiplier");
                            var data = PlayerVaultStatsData.get(p.getLevel());
                            int rank = RunAwards.rank(p);
                            int level = data.getVaultStats(p).getVaultUncappedLevel();
                            int xp = data.getVaultStats(p).getExp();
                            try (RunAwardScope scope = new RunAwardScope(p.getUUID(), rank)) {
                                data.addVaultExp(p, input);
                                c.getSource().sendSuccess(new TextComponent("TEST personal award: rank " + rank
                                        + ", before Experienced " + scope.beforeBonus() + ", awarded " + scope.awarded()
                                        + "; level/XP " + level + "/" + xp + " -> "
                                        + data.getVaultStats(p).getVaultUncappedLevel() + "/" + data.getVaultStats(p).getExp()
                                        + (scope.used() ? "" : " (native cap or shared-level path; no personal accumulation)")), false);
                            }
                            return 1;
                        })))
                .then(Commands.literal("testordinary").then(Commands.argument("xpBeforeGlobalMultiplier", IntegerArgumentType.integer(0, 1000000))
                        .executes(c -> {
                            var p = c.getSource().getPlayerOrException();
                            int input = IntegerArgumentType.getInteger(c, "xpBeforeGlobalMultiplier");
                            PlayerVaultStatsData.get(p.getLevel()).addVaultExp(p, input);
                            c.getSource().sendSuccess(new TextComponent("TEST ordinary award: " + RunAwards.nativeAward(input)
                                    + " XP before the native level cap. Experienced was not applied."), false);
                            return 1;
                        }))));
    }
}
