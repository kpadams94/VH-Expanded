package dev.kpada.vhexpanded.repulsor;

import iskallia.vault.core.world.storage.IZonedWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.*;
import java.util.*;

/** Every cell retains each cast separately. No delayed task can erase somebody else's cast. */
public final class BarrierTile extends BlockEntity {
    public record Part(UUID cast, UUID owner, long expires, AABB bounds) {}
    private final Map<UUID, Part> parts = new LinkedHashMap<>();
    private VoxelShape cached = Shapes.empty();
    public BarrierTile(BlockPos pos, BlockState state) { super(RepulsorBarrier.TILE.get(),pos,state); }
    public Collection<Part> parts() { return Collections.unmodifiableCollection(parts.values()); }
    @Override public void onLoad() { super.onLoad(); BarrierLifecycle.loaded(this); }
    @Override public void setRemoved() { BarrierLifecycle.removed(this); super.setRemoved(); }
    public VoxelShape shape() { return cached; }
    boolean expire(long time) {
        boolean changed=parts.values().removeIf(p->p.expires()<=time);
        if(changed) rebuild();
        return changed;
    }
    public void add(Part part) { parts.put(part.cast(),part); update(); }
    public void removeOwner(UUID owner) { if(parts.values().removeIf(p->p.owner().equals(owner))) update(); }
    private void rebuild() {
        cached=Shapes.empty();
        for(var p:parts.values()) cached=Shapes.or(cached,BarrierGeometry.at(p.bounds(),worldPosition));
    }
    private void update() {
        rebuild(); setChanged();
        if(level!=null) level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
        if(level instanceof ServerLevel server) BarrierLifecycle.changed(server,worldPosition);
    }
    public void tick() {
        if(level==null) return;
        boolean changed=expire(level.getGameTime());
        changed |= parts.values().removeIf(p -> {
            if(level instanceof ServerLevel server) {
                var player=server.getServer().getPlayerList().getPlayer(p.owner());
                return player==null || !player.isAlive() || player.level!=level;
            }
            return false;
        });
        if(parts.isEmpty() && level instanceof ServerLevel server) {
            IZonedWorld.runWithBypass(server,true,()->server.removeBlock(worldPosition,false));
            BarrierLifecycle.changed(server,worldPosition);
        } else if(changed) update();
    }
    @Override protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag list=new ListTag();
        for(var p:parts.values()) {
            CompoundTag n=new CompoundTag(); n.putUUID("cast",p.cast()); n.putUUID("owner",p.owner()); n.putLong("expires",p.expires());
            AABB b=p.bounds(); n.putDouble("x0",b.minX); n.putDouble("y0",b.minY); n.putDouble("z0",b.minZ);
            n.putDouble("x1",b.maxX); n.putDouble("y1",b.maxY); n.putDouble("z1",b.maxZ); list.add(n);
        }
        tag.put("parts",list);
    }
    @Override public void load(CompoundTag tag) {
        super.load(tag); parts.clear();
        for(Tag entry:tag.getList("parts",Tag.TAG_COMPOUND)) {
            CompoundTag n=(CompoundTag)entry;
            Part p=new Part(n.getUUID("cast"),n.getUUID("owner"),n.getLong("expires"),
                    new AABB(n.getDouble("x0"),n.getDouble("y0"),n.getDouble("z0"),n.getDouble("x1"),n.getDouble("y1"),n.getDouble("z1")));
            parts.put(p.cast(),p);
        }
        rebuild();
    }
    @Override public CompoundTag getUpdateTag() { return saveWithoutMetadata(); }
    @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
}
