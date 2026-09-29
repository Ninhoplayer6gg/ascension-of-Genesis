package com.genesisdynamics.projectgenesis.power.ability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

/** Active: launches the player upward (Genesis Atlas). */
public class PowerJumpAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> UPWARD_VELOCITY =
            new FloatProperty("upward_velocity").configurable("Upward velocity applied on use.");

    public PowerJumpAbility() {
        this.withProperty(UPWARD_VELOCITY, 1.6f);
        this.withProperty(ENERGY_COST, 15f);
        this.withProperty(INSTABILITY_GAIN, 8f);
        this.withProperty(COOLDOWN_TICKS, 60);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide || !enabled) {
            return;
        }
        if (!tryPayCost(entity, entry)) {
            return;
        }
        Vec3 motion = entity.getDeltaMovement();
        entity.setDeltaMovement(motion.x, entry.getProperty(UPWARD_VELOCITY), motion.z);
        entity.hurtMarked = true; // force velocity sync to clients
    }

    @Override
    public String getDocumentationDescription() {
        return "Launches the user high into the air.";
    }
}
