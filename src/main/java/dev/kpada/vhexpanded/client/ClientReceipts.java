package dev.kpada.vhexpanded.client;

import dev.kpada.vhexpanded.RunReceipt;
import iskallia.vault.client.data.ClientExpertiseData;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.stat.VaultSnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "vhexpanded", value = Dist.CLIENT)
public final class ClientReceipts {
    private static final Map<String, RunReceipt> RECEIPTS = new HashMap<>();
    private ClientReceipts() {}
    public static void accept(RunReceipt receipt) { RECEIPTS.put(receipt.player() + ":" + receipt.vault(), receipt); }
    public static RunReceipt find(VaultSnapshot snapshot, UUID player) {
        return RECEIPTS.get(player + ":" + snapshot.getEnd().get(Vault.ID));
    }
    public static int award(VaultSnapshot snapshot, UUID player, int fallback) {
        RunReceipt r = find(snapshot, player);
        return r == null ? fallback : r.awarded();
    }
    public static int rank() {
        var node = ClientExpertiseData.getLearnedTalentNode("Experienced");
        return node == null ? 0 : Math.max(0, Math.min(5, node.getUnmodifiedTier()));
    }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggedOutEvent event) { RECEIPTS.clear(); }
}
