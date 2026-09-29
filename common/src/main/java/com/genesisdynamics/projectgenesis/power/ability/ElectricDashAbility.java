package com.genesisdynamics.projectgenesis.power.ability;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

/** Movement: dashes the player forward at high speed (Genesis Volt). */
public class ElectricDashAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> DASH_VELOCITY =
            new FloatProperty("dash_velocity").configurable("Horizontal velocity applied on use.");

    public ElectricDashAbility() {
        this.withProperty(DASH_VELOCITY, 3.0f);
        this.withProperty(ENERGY_COST, 10f);
        this.withProperty(INSTABILITY_GAIN, 5f);
        this.withProperty(COOLDOWN_TICKS, 30);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide || !enabled) {
            return;
        }
        if (!tryPayCost(entity, entry)) {
            return;
        }
        Vec3 look = entity.getLookAngle().normalize();
        double speed = entry.getProperty(DASH_VELOCITY);
        entity.setDeltaMovement(look.x * speed, Math.max(0.2, look.y * speed), look.z * speed);
        entity.hurtMarked = true;
        if (entity.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, entity.getX(), entity.getY() + 1, entity.getZ(), 12, 0.2, 0.2, 0.2, 0.05);
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Dash forward at electric speed.";
    }
}
