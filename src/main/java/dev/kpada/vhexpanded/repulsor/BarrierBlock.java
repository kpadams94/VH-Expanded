package dev.kpada.vhexpanded.repulsor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;
public final class BarrierBlock extends Block implements EntityBlock {
    public BarrierBlock() {
        super(Properties.copy(Blocks.GLASS).strength(-1,3600000).noOcclusion().dynamicShape().noDrops()
                .isSuffocating((s,l,p)->false).isViewBlocking((s,l,p)->false));
    }
    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }
    @Override public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Native ability projectiles also do a context-free embedding check at their current position.
        // Actual movement and raycasts supply their entity and receive the precise shape below.
        if (!(context instanceof EntityCollisionContext e) || e.getEntity()==null || BarrierPassage.passes(e.getEntity())) return Shapes.empty();
        return level.getBlockEntity(pos) instanceof BarrierTile tile ? tile.shape() : Shapes.empty();
    }
    @Override public VoxelShape getShape(BlockState s, BlockGetter l, BlockPos p, CollisionContext c) { return getCollisionShape(s,l,p,c); }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new BarrierTile(pos,state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == RepulsorBarrier.TILE.get() ? (l,p,s,t) -> ((BarrierTile)t).tick() : null;
    }
}
