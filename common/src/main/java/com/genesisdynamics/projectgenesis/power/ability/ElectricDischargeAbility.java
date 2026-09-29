package com.genesisdynamics.projectgenesis.power.ability;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.List;

/** Active: zaps a single nearby target with an electric discharge (Genesis Volt). */
public class ElectricDischargeAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> RANGE =
            new FloatProperty("range").configurable("Maximum targeting range.");
    public static final PalladiumProperty<Float> DAMAGE =
            new FloatProperty("damage").configurable("Damage dealt to the target.");

    public ElectricDischargeAbility() {
        this.withProperty(RANGE, 12f);
        this.withProperty(DAMAGE, 6f);
        this.withProperty(ENERGY_COST, 12f);
        this.withProperty(INSTABILITY_GAIN, 6f);
        this.withProperty(COOLDOWN_TICKS, 40);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide || !enabled || !(entity.level() instanceof ServerLevel level)) {
            return;
        }
        if (!tryPayCost(entity, entry)) {
            return;
        }

        float range = entry.getProperty(RANGE);
        LivingEntity target = findNearestTarget(entity, level, range);
        if (target == null) {
            return;
        }
        DamageSource source = level.damageSources().lightningBolt();
        target.hurt(source, entry.getProperty(DAMAGE));
        level.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY() + 1, target.getZ(), 15, 0.3, 0.5, 0.3, 0.02);
        level.playSound(null, target.blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.5f, 1.5f);
    }

    protected LivingEntity findNearestTarget(LivingEntity self, ServerLevel level, float range) {
        AABB box = self.getBoundingBox().inflate(range);
        List<LivingEntity> candidates = level.getEntitiesOfClass(LivingEntity.class, box, e -> e != self && e.isAlive());
        LivingEntity nearest = null;
        double best = Double.MAX_VALUE;
        for (LivingEntity candidate : candidates) {
            double dist = candidate.distanceToSqr(self);
            if (dist < best && dist <= range * range) {
                best = dist;
                nearest = candidate;
            }
        }
        return nearest;
    }

    @Override
    public String getDocumentationDescription() {
        return "Strike the nearest enemy with an electric discharge.";
    }
}
