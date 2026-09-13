package dev.kpada.vhexpanded;

import dev.kpada.vhexpanded.client.ClientReceipts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("vhexpanded")
public final class Expanded {
    public static final Logger LOGGER = LogManager.getLogger("VH Expanded");
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("vhexpanded", "receipts"), () -> "2", "2"::equals, "2"::equals);
    public Expanded() {
        net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus()
                .addListener(dev.kpada.vhexpanded.hearts.HeartRecipePack::find);
        dev.kpada.vhexpanded.repulsor.RepulsorBarrier.register(net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus());
        CHANNEL.messageBuilder(RunReceipt.class, 0, NetworkDirection.PLAY_TO_CLIENT)
                .encoder((r, b) -> b.writeNbt(r.save()))
                .decoder(b -> RunReceipt.load(b.readNbt()))
                .consumer((r, context) -> {
                    context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientReceipts.accept(r)));
                    context.get().setPacketHandled(true);
                }).add();
        MinecraftForge.EVENT_BUS.addListener(this::login);
        MinecraftForge.EVENT_BUS.addListener(dev.kpada.vhexpanded.hearts.HeartEquipment::tick);
        MinecraftForge.EVENT_BUS.addListener(dev.kpada.vhexpanded.hearts.HeartCommands::register);
        MinecraftForge.EVENT_BUS.addListener(PlaytestCommands::register);
        MinecraftForge.EVENT_BUS.addListener(dev.kpada.vhexpanded.repulsor.RepulsorCommands::register);
    }
    private void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            RunReceipts.get(player.getServer()).sync(player);
            var tree=iskallia.vault.world.data.PlayerAbilitiesData.get(player.getLevel()).getAbilities(player);
            dev.kpada.vhexpanded.repulsor.RepulsorConfig.apply(tree);
            tree.sync(iskallia.vault.skill.base.SkillContext.of(player));
        }
    }
    public static void send(ServerPlayer player, RunReceipt receipt) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), receipt);
    }
}
