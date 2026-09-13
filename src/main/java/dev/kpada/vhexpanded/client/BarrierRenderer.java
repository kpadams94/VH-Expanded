package dev.kpada.vhexpanded.client;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kpada.vhexpanded.repulsor.*;
import iskallia.vault.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(modid="vhexpanded",value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
public final class BarrierRenderer implements BlockEntityRenderer<BarrierTile> {
    @SubscribeEvent public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(RepulsorBarrier.TILE.get(),c->new BarrierRenderer());
    }
    @Override public void render(BarrierTile tile,float partial,PoseStack pose,MultiBufferSource buffers,int light,int overlay) {
        for(var box:tile.shape().toAabbs()) {
            pose.pushPose(); pose.translate(box.minX,box.minY,box.minZ);
            pose.scale((float)box.getXsize(),(float)box.getYsize(),(float)box.getZsize());
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(ModBlocks.MANA_BARRIER.defaultBlockState(),pose,buffers,light,overlay);
            pose.popPose();
        }
    }
}
