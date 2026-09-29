package com.genesisdynamics.projectgenesis.power.ability;

import com.genesisdynamics.projectgenesis.player.StabilityManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.List;

/**
 * Ultimate: VECTOR ASCENDANCY / Vector Burst (Genesis Vector Ascension).
 *
 * <p>Unleashes a massive telekinetic shockwave. Locked out while instability is
 * critical.</p>
 */
public class VectorBurstAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> RADIUS =
            new FloatProperty("radius").configurable("Radius of the burst.");
    public static final PalladiumProperty<Float> DAMAGE =
            new FloatProperty("damage").configurable("Damage dealt to entities in range.");
    public static final PalladiumProperty<Float> LAUNCH =
            new FloatProperty("launch").configurable("Upward/away launch force.");

    public VectorBurstAbility() {
        this.withProperty(RADIUS, 10f);
        this.withProperty(DAMAGE, 12f);
        this.withProperty(LAUNCH, 2.0f);
        this.withProperty(ENERGY_COST, 50f);
        this.withProperty(INSTABILITY_GAIN, 50f);
        this.withProperty(COOLDOWN_TICKS, 1200);
        this.withProperty(REQUIRED_STAGE, com.genesisdynamics.projectgenesis.progression.PowerProgressionManager.STAGE_ASCENSION);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide || !enabled || !(entity.level() instanceof ServerLevel level)) {
            return;
        }
        if (StabilityManager.isAscensionLocked(entity)) {
            return;
        }
        if (!tryPayCost(entity, entry)) {
            return;
        }

        float radius = entry.getProperty(RADIUS);
        float launch = entry.getProperty(LAUNCH);
        AABB box = entity.getBoundingBox().inflate(radius);
        List<LivingEntity> victims = level.getEntitiesOfClass(LivingEntity.class, box, e -> e != entity && e.isAlive());
        DamageSource source = level.damageSources().magic();
        for (LivingEntity victim : victims) {
            victim.hurt(source, entry.getProperty(DAMAGE));
            Vec3 away = victim.position().subtract(entity.position()).normalize();
            victim.push(away.x * launch, launch * 0.6, away.z * launch);
            victim.hurtMarked = true;
        }
        level.sendParticles(ParticleTypes.SONIC_BOOM, entity.getX(), entity.getY() + 1, entity.getZ(), 1, 0, 0, 0, 0);
        level.sendParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY() + 1, entity.getZ(), 40, radius / 2, 0.5, radius / 2, 0.05);
        level.playSound(null, entity.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    @Override
    public String getDocumentationDescription() {
        return "Ascension: unleash a devastating telekinetic shockwave.";
    }
}
