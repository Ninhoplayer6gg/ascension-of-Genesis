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
import net.threetag.palladium.util.property.IntegerProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Active: lightning that jumps between multiple enemies (Genesis Volt). */
public class ChainLightningAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> RANGE =
            new FloatProperty("range").configurable("Initial targeting range.");
    public static final PalladiumProperty<Float> JUMP_RANGE =
            new FloatProperty("jump_range").configurable("Max distance the bolt can jump to the next target.");
    public static final PalladiumProperty<Integer> MAX_TARGETS =
            new IntegerProperty("max_targets").configurable("Maximum number of chained targets.");
    public static final PalladiumProperty<Float> DAMAGE =
            new FloatProperty("damage").configurable("Damage dealt to each target.");

    public ChainLightningAbility() {
        this.withProperty(RANGE, 14f);
        this.withProperty(JUMP_RANGE, 8f);
        this.withProperty(MAX_TARGETS, 5);
        this.withProperty(DAMAGE, 7f);
        this.withProperty(ENERGY_COST, 25f);
        this.withProperty(INSTABILITY_GAIN, 15f);
        this.withProperty(COOLDOWN_TICKS, 100);
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
        float jumpRange = entry.getProperty(JUMP_RANGE);
        int maxTargets = entry.getProperty(MAX_TARGETS);
        float damage = entry.getProperty(DAMAGE);

        Set<UUID> hit = new HashSet<>();
        LivingEntity current = nearest(entity, level, entity.getBoundingBox().inflate(range), hit);
        List<LivingEntity> chain = new ArrayList<>();
        int count = 0;
        LivingEntity source = entity;
        while (current != null && count < maxTargets) {
            hit.add(current.getUUID());
            chain.add(current);
            DamageSource dmg = level.damageSources().lightningBolt();
            current.hurt(dmg, damage);
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, current.getX(), current.getY() + 1, current.getZ(), 12, 0.3, 0.5, 0.3, 0.02);
            source = current;
            current = nearest(entity, level, source.getBoundingBox().inflate(jumpRange), hit);
            count++;
        }

        if (!chain.isEmpty()) {
            level.playSound(null, entity.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.7f, 1.6f);
        }
    }

    private LivingEntity nearest(LivingEntity self, ServerLevel level, AABB box, Set<UUID> exclude) {
        List<LivingEntity> candidates = level.getEntitiesOfClass(LivingEntity.class, box, e -> e != self && e.isAlive() && !exclude.contains(e.getUUID()));
        LivingEntity nearest = null;
        double best = Double.MAX_VALUE;
        for (LivingEntity candidate : candidates) {
            double dist = candidate.distanceToSqr(self);
            if (dist < best) {
                best = dist;
                nearest = candidate;
            }
        }
        return nearest;
    }

    @Override
    public String getDocumentationDescription() {
        return "Lightning arcs between several nearby enemies.";
    }
}
