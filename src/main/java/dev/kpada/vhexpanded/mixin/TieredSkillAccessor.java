package dev.kpada.vhexpanded.mixin;
import iskallia.vault.skill.base.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.List;
@Mixin(value = TieredSkill.class, remap = false)
public interface TieredSkillAccessor {
    @Accessor("tiers") void expanded$tiers(List<LearnableSkill> tiers);
}
