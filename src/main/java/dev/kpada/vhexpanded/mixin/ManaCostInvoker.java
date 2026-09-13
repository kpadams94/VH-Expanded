package dev.kpada.vhexpanded.mixin;
import iskallia.vault.snapshot.AttributeSnapshot;
import iskallia.vault.util.calc.ManaCostHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
@Mixin(value=ManaCostHelper.class,remap=false)
public interface ManaCostInvoker {
    @Invoker("adjustManaCost") static float expanded$adjust(AttributeSnapshot snapshot,String id,float cost) { throw new AssertionError(); }
}
