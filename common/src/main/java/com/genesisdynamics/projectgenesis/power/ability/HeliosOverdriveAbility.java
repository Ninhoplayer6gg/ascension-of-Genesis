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
 * Ultimate: SOLAR OVERDRIVE (Genesis Helios Ascension).
 *
 * <p>For a limited time the user gains strength, speed and fire immunity while
 * burning through Stability. Unavailable when instability is already critical.</p>
 */
public class HeliosOverdriveAbility extends GenesisAbility {

    public static final PalladiumProperty<Integer> DURATION_TICKS =
            new IntegerProperty("duration_ticks").configurable("How long the overdrive lasts.");

    public HeliosOverdriveAbility() {
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
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, entry.getProperty(DURATION_TICKS), 1));
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, entry.getProperty(DURATION_TICKS), 1));
        entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, entry.getProperty(DURATION_TICKS), 0));
        if (entity.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.FLASH, entity.getX(), entity.getY() + 1, entity.getZ(), 1, 0, 0, 0, 0);
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (!entity.level().isClientSide && enabled && entity.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.FLAME, entity.getX(), entity.getY() + 1, entity.getZ(), 3, 0.3, 0.5, 0.3, 0.02);
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Ascension: temporarily overcharge all solar systems.";
    }
}
