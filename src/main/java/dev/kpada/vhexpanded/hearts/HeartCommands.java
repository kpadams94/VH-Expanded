package dev.kpada.vhexpanded.hearts;

import dev.kpada.vhexpanded.Expanded;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;

public final class HeartCommands {
    private HeartCommands() {}
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("vhexpanded").requires(s -> s.hasPermission(2))
                .then(Commands.literal("hearts")
                .then(Commands.literal("selftest").executes(c -> {
                    try {
                        int count = HeartRuntimeChecks.run(c.getSource().getServer());
                        c.getSource().sendSuccess(new TextComponent("Heart Canisters: " + count + " runtime checks passed."), false);
                        return count;
                    } catch (Exception e) {
                        Expanded.LOGGER.error("Heart Canisters runtime checks failed", e);
                        c.getSource().sendFailure(new TextComponent("Heart Canisters check failed: " + e.getMessage()));
                        return 0;
                    }
                }))));
    }
}
