package dev.kpada.vhexpanded;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;

public final class RunReceipts extends SavedData {
    private final Map<String, RunReceipt> receipts = new HashMap<>();
    public static RunReceipts get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(RunReceipts::load,
                RunReceipts::new, "vhexpanded_run_receipts");
    }
    public RunReceipt find(UUID player, UUID vault) { return receipts.get(player + ":" + vault); }
    public void put(RunReceipt receipt) {
        receipts.put(receipt.player() + ":" + receipt.vault(), receipt);
        setDirty();
    }
    public void sync(ServerPlayer player) {
        receipts.values().stream().filter(r -> r.player().equals(player.getUUID()))
                .forEach(r -> Expanded.send(player, r));
    }
    @Override public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        receipts.values().forEach(r -> list.add(r.save()));
        tag.put("receipts", list);
        return tag;
    }
    private static RunReceipts load(CompoundTag tag) {
        RunReceipts data = new RunReceipts();
        ListTag list = tag.getList("receipts", 10);
        for (int i = 0; i < list.size(); i++) {
            RunReceipt receipt = RunReceipt.load(list.getCompound(i));
            data.receipts.put(receipt.player() + ":" + receipt.vault(), receipt);
        }
        return data;
    }
}
