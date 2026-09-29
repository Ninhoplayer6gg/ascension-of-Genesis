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
 * Ultimate: TITAN STATE (Genesis Atlas Ascension).
 *
 * <p>Temporarily turns the user into a nearly unstoppable juggernaut at a heavy
 * Stability cost. Locked out while instability is critical.</p>
 */
public class AtlasTitanStateAbility extends GenesisAbility {

    public static final PalladiumProperty<Integer> DURATION_TICKS =
            new IntegerProperty("duration_ticks").configurable("How long the titan state lasts.");

    public AtlasTitanStateAbility() {
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
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, entry.getProperty(DURATION_TICKS), 2));
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, entry.getProperty(DURATION_TICKS), 2));
        entity.addEffect(new MobEffectInstance(MobEffects.JUMP, entry.getProperty(DURATION_TICKS), 2));
        if (entity.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, entity.getX(), entity.getY(), entity.getZ(), 1, 0, 0, 0, 0);
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (!entity.level().isClientSide && enabled && entity.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), 2, 0.4, 0.1, 0.4, 0.01);
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Ascension: enter the Titan State, becoming a living siege engine.";
    }
}
