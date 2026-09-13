package dev.kpada.vhexpanded.hearts;

import dev.kpada.vhexpanded.mixin.VaultGeneralAccessor;
import iskallia.vault.init.ModConfigs;
import java.util.List;

/** Native vault interaction blocking and the native "Disabled in the Vaults" tooltip. */
public final class HeartVaultRules {
    public static final List<String> CONSUMABLE_HEARTS = List.of(
            "bhc:red_heart", "bhc:yellow_heart", "bhc:green_heart", "bhc:blue_heart");
    private HeartVaultRules() {}

    public static void merge() {
        var config = (VaultGeneralAccessor) ModConfigs.VAULT_GENERAL;
        for (String id : CONSUMABLE_HEARTS) {
            if (!config.expanded$itemBlacklist().contains(id)) config.expanded$itemBlacklist().add(id);
            config.expanded$itemBlacklistCache().remove(id);
        }
    }
}
