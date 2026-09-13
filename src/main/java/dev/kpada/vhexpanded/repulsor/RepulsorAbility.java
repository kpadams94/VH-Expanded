package dev.kpada.vhexpanded.repulsor;

import com.google.gson.JsonObject;
import iskallia.vault.core.data.adapter.Adapters;
import iskallia.vault.core.net.BitBuffer;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.effect.spi.core.InstantManaAbility;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.util.calc.AreaOfEffectHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;

public final class RepulsorAbility extends InstantManaAbility {
    private float radius;
    private float repelRadius;
    private int durationTicks;
    public RepulsorAbility() {}
    public float getUnmodifiedRadius() { return radius; }
    public float getRadius(LivingEntity player) { return AreaOfEffectHelper.adjustAreaOfEffect(player, this, radius); }
    public float getUnmodifiedRepelRadius() { return repelRadius; }
    public float getRepelRadius(LivingEntity player) { return AreaOfEffectHelper.adjustAreaOfEffect(player, this, repelRadius); }
    public int getDurationTicks() { return durationTicks; }

    @Override protected ActionResult doAction(SkillContext context) {
        return context.getSource().as(ServerPlayer.class).map(player -> {
            RepulsorBarrier.cast(player, getRadius(player), getRepelRadius(player), durationTicks);
            player.getLevel().playSound(null, player.blockPosition(), ModSounds.MANA_SHIELD, SoundSource.PLAYERS, .2f, 1f);
            return ActionResult.successCooldownImmediate();
        }).orElse(ActionResult.fail());
    }
    @Override public void writeBits(BitBuffer buffer) {
        super.writeBits(buffer);
        Adapters.FLOAT.writeBits(radius, buffer);
        Adapters.FLOAT.writeBits(repelRadius, buffer);
        Adapters.INT.writeBits(durationTicks, buffer);
    }
    @Override public void readBits(BitBuffer buffer) {
        super.readBits(buffer);
        radius = Adapters.FLOAT.readBits(buffer).orElseThrow();
        repelRadius = Adapters.FLOAT.readBits(buffer).orElseThrow();
        durationTicks = Adapters.INT.readBits(buffer).orElseThrow();
    }
    @Override public Optional<CompoundTag> writeNbt() {
        return super.writeNbt().map(n -> { n.putFloat("radius", radius); n.putFloat("repelRadius", repelRadius); n.putInt("durationTicks", durationTicks); return n; });
    }
    @Override public void readNbt(CompoundTag n) { super.readNbt(n); radius = n.getFloat("radius"); repelRadius = n.contains("repelRadius") ? n.getFloat("repelRadius") : radius; durationTicks = n.getInt("durationTicks"); }
    @Override public Optional<JsonObject> writeJson() {
        return super.writeJson().map(j -> { j.addProperty("radius", radius); j.addProperty("repelRadius", repelRadius); j.addProperty("durationTicks", durationTicks); return j; });
    }
    @Override public void readJson(JsonObject j) {
        super.readJson(j); radius = j.get("radius").getAsFloat(); repelRadius = j.has("repelRadius") ? j.get("repelRadius").getAsFloat() : radius; durationTicks = j.get("durationTicks").getAsInt();
    }
}
