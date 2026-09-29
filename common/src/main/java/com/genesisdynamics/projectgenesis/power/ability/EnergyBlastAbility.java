package com.genesisdynamics.projectgenesis.power.ability;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

/** Active: fires a concentrated energy projectile (Genesis Helios). */
public class EnergyBlastAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> RANGE =
            new FloatProperty("range").configurable("Maximum ray range.");
    public static final PalladiumProperty<Float> DAMAGE =
            new FloatProperty("damage").configurable("Damage dealt on impact.");
    public static final PalladiumProperty<Float> HEAT_GAIN =
            new FloatProperty("heat_gain").configurable("Extra heat generated on use.");

    public EnergyBlastAbility() {
        this.withProperty(RANGE, 32f);
        this.withProperty(DAMAGE, 10f);
        this.withProperty(HEAT_GAIN, 20f);
        this.withProperty(ENERGY_COST, 18f);
        this.withProperty(INSTABILITY_GAIN, 10f);
        this.withProperty(COOLDOWN_TICKS, 50);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide || !enabled || !(entity.level() instanceof ServerLevel level)) {
            return;
        }
        if (!tryPayCost(entity, entry)) {
            return;
        }
        // Add to Helios heat pool as well.
        float heat = entity.getPersistentData().getFloat("GenesisHeliosHeat");
        entity.getPersistentData().putFloat("GenesisHeliosHeat", heat + entry.getProperty(HEAT_GAIN));

        Vec3 start = entity.getEyePosition();
        Vec3 look = entity.getLookAngle();
        Vec3 end = start.add(look.scale(entry.getProperty(RANGE)));
        AABB box = entity.getBoundingBox().expandTowards(look.scale(entry.getProperty(RANGE))).inflate(1.0);
        EntityHitResult hit = ProjectileUtil.getEntityHitResult(level, entity, start, end, box, e -> e instanceof LivingEntity && e != entity);

        if (hit != null && hit.getEntity() instanceof LivingEntity target) {
            DamageSource source = level.damageSources().onFire();
            target.hurt(source, entry.getProperty(DAMAGE));
            target.setSecondsOnFire(3);
            level.sendParticles(ParticleTypes.FLAME, target.getX(), target.getY() + 1, target.getZ(), 20, 0.3, 0.5, 0.3, 0.05);
        }
        // Beam particles along the ray.
        Vec3 beamEnd = hit != null ? hit.getLocation() : end;
        int steps = 16;
        for (int i = 0; i <= steps; i++) {
            Vec3 p = start.lerp(beamEnd, i / (double) steps);
            level.sendParticles(ParticleTypes.END_ROD, p.x, p.y, p.z, 1, 0, 0, 0, 0);
        }
        level.playSound(null, entity.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.8f, 1.2f);
    }

    @Override
    public String getDocumentationDescription() {
        return "Fire a searing energy blast that ignites the target.";
    }
}
