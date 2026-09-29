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

/** Active: slams the ground, damaging and knocking back nearby entities (Genesis Atlas). */
public class GroundImpactAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> RADIUS =
            new FloatProperty("radius").configurable("Radius of the impact shockwave.");
    public static final PalladiumProperty<Float> DAMAGE =
            new FloatProperty("damage").configurable("Damage dealt to entities in range.");
    public static final PalladiumProperty<Float> KNOCKBACK =
            new FloatProperty("knockback").configurable("Knockback strength applied to victims.");

    public GroundImpactAbility() {
        this.withProperty(RADIUS, 5f);
        this.withProperty(DAMAGE, 8f);
        this.withProperty(KNOCKBACK, 1.2f);
        this.withProperty(ENERGY_COST, 30f);
        this.withProperty(INSTABILITY_GAIN, 20f);
        this.withProperty(COOLDOWN_TICKS, 120);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide || !enabled || !(entity.level() instanceof ServerLevel level)) {
            return;
        }
        if (!tryPayCost(entity, entry)) {
            return;
        }

        float radius = entry.getProperty(RADIUS);
        float damage = entry.getProperty(DAMAGE);
        float knockback = entry.getProperty(KNOCKBACK);

        AABB box = entity.getBoundingBox().inflate(radius);
        List<LivingEntity> victims = level.getEntitiesOfClass(LivingEntity.class, box, e -> e != entity && e.isAlive());
        DamageSource source = level.damageSources().mobAttack(entity instanceof net.minecraft.world.entity.Mob mob ? mob : null);
        for (LivingEntity victim : victims) {
            victim.hurt(source, damage);
            double dx = victim.getX() - entity.getX();
            double dz = victim.getZ() - entity.getZ();
            double dist = Math.max(0.1, Math.sqrt(dx * dx + dz * dz));
            victim.knockback(knockback, -dx / dist, -dz / dist);
        }

        level.sendParticles(ParticleTypes.EXPLOSION, entity.getX(), entity.getY(), entity.getZ(), 1, 0, 0, 0, 0);
        level.sendParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), 20, radius / 2, 0.2, radius / 2, 0.05);
        level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0f, 0.8f);
    }

    @Override
    public String getDocumentationDescription() {
        return "Slam the ground, damaging and launching nearby enemies.";
    }
}
