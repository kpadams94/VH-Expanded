package dev.kpada.vhexpanded.mixin;

import iskallia.vault.config.VaultGeneralConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.List;
import java.util.Map;

@Mixin(value = VaultGeneralConfig.class, remap = false)
public interface VaultGeneralAccessor {
    @Accessor("ITEM_BLACKLIST") List<String> expanded$itemBlacklist();
    @Accessor("itemBlacklistCache") Map<String, Boolean> expanded$itemBlacklistCache();
}
