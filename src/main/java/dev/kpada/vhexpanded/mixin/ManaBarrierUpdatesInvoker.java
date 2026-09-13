package dev.kpada.vhexpanded.mixin;
import iskallia.vault.skill.ability.effect.ManaBarrierAbility;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
@Mixin(value=ManaBarrierAbility.class,remap=false)
public interface ManaBarrierUpdatesInvoker {
    @Invoker("sendBlockUpdatesToClient") static void expanded$refresh(ServerLevel level,BlockPos pos) { throw new AssertionError(); }
}
