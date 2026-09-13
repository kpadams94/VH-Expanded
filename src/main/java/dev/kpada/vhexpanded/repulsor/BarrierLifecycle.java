package dev.kpada.vhexpanded.repulsor;
import iskallia.vault.core.world.storage.IZonedWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;
@Mod.EventBusSubscriber(modid="vhexpanded")
public final class BarrierLifecycle {
    private static final Map<ServerLevel,Set<BlockPos>> LOADED=new WeakHashMap<>();
    private static final Map<ServerLevel,Map<net.minecraft.world.level.ChunkPos,BlockPos>> PENDING=new WeakHashMap<>();
    private BarrierLifecycle() {}
    static void changed(ServerLevel level,BlockPos pos) {
        PENDING.computeIfAbsent(level,k->new HashMap<>()).put(new net.minecraft.world.level.ChunkPos(pos),pos);
    }
    @SubscribeEvent public static void tick(net.minecraftforge.event.TickEvent.WorldTickEvent e) {
        if(e.phase!=net.minecraftforge.event.TickEvent.Phase.END || !(e.world instanceof ServerLevel level))return;
        var pending=PENDING.remove(level);
        if(pending!=null) for(var pos:pending.values())
            dev.kpada.vhexpanded.mixin.ManaBarrierUpdatesInvoker.expanded$refresh(level,pos);
    }
    static void loaded(BarrierTile tile) {
        if(tile.getLevel() instanceof ServerLevel level) LOADED.computeIfAbsent(level,k->new HashSet<>()).add(tile.getBlockPos());
    }
    static void removed(BarrierTile tile) {
        if(tile.getLevel() instanceof ServerLevel level && LOADED.containsKey(level)) LOADED.get(level).remove(tile.getBlockPos());
    }
    private static void clearOwner(UUID owner) {
        for(var entry:LOADED.entrySet()) for(var pos:List.copyOf(entry.getValue())) {
            if(entry.getKey().getBlockEntity(pos) instanceof BarrierTile tile) { tile.removeOwner(owner); tile.tick(); }
        }
    }
    private static void clear(ServerLevel level) {
        for(var pos:List.copyOf(LOADED.getOrDefault(level,Set.of()))) if(level.getBlockState(pos).is(RepulsorBarrier.BLOCK.get()))
            IZonedWorld.runWithBypass(level,true,()->level.removeBlock(pos,false));
        LOADED.remove(level);
        PENDING.remove(level);
    }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent e) { clearOwner(e.getPlayer().getUUID()); }
    @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent e) { clearOwner(e.getPlayer().getUUID()); }
    @SubscribeEvent public static void death(LivingDeathEvent e) { if(e.getEntityLiving() instanceof Player p) clearOwner(p.getUUID()); }
    @SubscribeEvent public static void stop(ServerStoppingEvent e) { for(var level:List.copyOf(LOADED.keySet())) clear(level); }
    @SubscribeEvent public static void unload(WorldEvent.Unload e) { if(e.getWorld() instanceof ServerLevel level) clear(level); }
}
