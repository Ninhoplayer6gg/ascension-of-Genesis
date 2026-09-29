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

/** Active: releases a radial burst of heat, igniting everything nearby (Genesis Helios). */
public class HeatBurstAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> RADIUS =
            new FloatProperty("radius").configurable("Radius of the heat wave.");
    public static final PalladiumProperty<Float> DAMAGE =
            new FloatProperty("damage").configurable("Damage dealt to entities in range.");
    public static final PalladiumProperty<Integer> FIRE_SECONDS =
            new net.threetag.palladium.util.property.IntegerProperty("fire_seconds").configurable("How long victims burn.");

    public HeatBurstAbility() {
        this.withProperty(RADIUS, 7f);
        this.withProperty(DAMAGE, 5f);
        this.withProperty(FIRE_SECONDS, 4);
        this.withProperty(ENERGY_COST, 28f);
        this.withProperty(INSTABILITY_GAIN, 18f);
        this.withProperty(COOLDOWN_TICKS, 140);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide || !enabled || !(entity.level() instanceof ServerLevel level)) {
            return;
        }
        if (!tryPayCost(entity, entry)) {
            return;
        }
        float heat = entity.getPersistentData().getFloat("GenesisHeliosHeat");
        entity.getPersistentData().putFloat("GenesisHeliosHeat", heat + 25f);

        float radius = entry.getProperty(RADIUS);
        AABB box = entity.getBoundingBox().inflate(radius);
        List<LivingEntity> victims = level.getEntitiesOfClass(LivingEntity.class, box, e -> e != entity && e.isAlive());
        DamageSource source = level.damageSources().inFire();
        for (LivingEntity victim : victims) {
            victim.hurt(source, entry.getProperty(DAMAGE));
            victim.setSecondsOnFire(entry.getProperty(FIRE_SECONDS));
        }
        level.sendParticles(ParticleTypes.FLAME, entity.getX(), entity.getY() + 0.5, entity.getZ(), 60, radius / 2, 0.5, radius / 2, 0.08);
        level.playSound(null, entity.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0f, 0.6f);
    }

    @Override
    public String getDocumentationDescription() {
        return "Release a radial burst of heat, igniting everything nearby.";
    }
}
