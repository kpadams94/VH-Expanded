package dev.kpada.vhexpanded.repulsor;

import dev.kpada.vhexpanded.mixin.TieredSkillAccessor;
import iskallia.vault.core.data.adapter.Adapters;
import iskallia.vault.core.net.ArrayBitBuffer;
import iskallia.vault.skill.base.*;
import net.minecraft.server.level.ServerPlayer;
import java.util.ArrayList;

/** Keeps the native bonus-tier pipeline while extending its finite config table when necessary. */
public final class RepulsorTiers extends TieredSkill {
    private void ensure(int level) {
        if (level <= getTiers().size()) return;
        var tiers = new ArrayList<>(getTiers());
        var baseline = tiers.get(tiers.size() - 1).writeJson().orElseThrow();
        while (tiers.size() < level) {
            var ability = new RepulsorAbility();
            ability.readJson(RepulsorConfig.rank(baseline, tiers.size() + 1));
            ability.setParent(this);
            tiers.add(ability);
        }
        ((TieredSkillAccessor)(Object)this).expanded$tiers(tiers);
    }
    @Override public LearnableSkill getChild(int tier) { ensure(tier); return super.getChild(tier); }
    @Override public void onTick(SkillContext context) {
        context.getSource().as(ServerPlayer.class).ifPresent(p -> ensure(getUnmodifiedTier() + SkillBonusTierCache.getBonuses(p).getBonus(this)));
        super.onTick(context);
    }
    @Override public <T extends Skill> T copy() {
        var buffer = ArrayBitBuffer.empty();
        Adapters.SKILL.writeBits(this, buffer); buffer.setPosition(0);
        return (T) Adapters.SKILL.readBits(buffer).orElseThrow();
    }
}
