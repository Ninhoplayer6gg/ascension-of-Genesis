package com.genesisdynamics.projectgenesis.power.ability;

import com.genesisdynamics.projectgenesis.player.StabilityManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

/**
 * Ultimate: OVERCHARGE (Genesis Volt Ascension).
 *
 * <p>Massively boosts speed and grants regeneration for a short time at a heavy
 * Stability cost. Locked out while instability is critical.</p>
 */
public class VoltOverchargeAbility extends GenesisAbility {

    public static final PalladiumProperty<Integer> DURATION_TICKS =
            new IntegerProperty("duration_ticks").configurable("How long overcharge lasts.");

    public VoltOverchargeAbility() {
        this.withProperty(DURATION_TICKS, 200);
        this.withProperty(ENERGY_COST, 40f);
        this.withProperty(INSTABILITY_GAIN, 45f);
        this.withProperty(COOLDOWN_TICKS, 1200);
        this.withProperty(REQUIRED_STAGE, com.genesisdynamics.projectgenesis.progression.PowerProgressionManager.STAGE_ASCENSION);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide || !enabled) {
            return;
        }
        if (StabilityManager.isAscensionLocked(entity)) {
            return;
        }
        if (!tryPayCost(entity, entry)) {
            return;
        }
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, entry.getProperty(DURATION_TICKS), 2));
        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, entry.getProperty(DURATION_TICKS), 1));
        if (entity.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, entity.getX(), entity.getY() + 1, entity.getZ(), 40, 0.5, 1, 0.5, 0.1);
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (!entity.level().isClientSide && enabled && entity.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, entity.getX(), entity.getY() + 1, entity.getZ(), 2, 0.3, 0.5, 0.3, 0.02);
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Ascension: overcharge your nervous system for extreme speed.";
    }
}
