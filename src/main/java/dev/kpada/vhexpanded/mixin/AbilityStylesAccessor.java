package dev.kpada.vhexpanded.mixin;
import iskallia.vault.config.AbilitiesGUIConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.Map;
@Mixin(value = AbilitiesGUIConfig.class, remap = false)
public interface AbilityStylesAccessor {
    @Accessor("specializationStyleLookup") Map<String, AbilitiesGUIConfig.SpecializationStyle> expanded$lookup();
}
