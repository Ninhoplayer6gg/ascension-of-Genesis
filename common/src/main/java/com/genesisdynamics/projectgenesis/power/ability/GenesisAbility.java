package com.genesisdynamics.projectgenesis.power.ability;

import com.genesisdynamics.projectgenesis.player.EnergyManager;
import com.genesisdynamics.projectgenesis.player.StabilityManager;
import com.genesisdynamics.projectgenesis.progression.PowerProgressionManager;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

/**
 * Reusable base class for every Genesis ability.
 *
 * <p>Adds the shared contract requested by the design doc: each ability declares its
 * energy cost, instability generated, cooldown, duration, required power stage and
 * icon. Concrete subclasses only implement their unique effect logic; resource
 * spending and stage gating are handled here so no code is duplicated.</p>
 */
public abstract class GenesisAbility extends Ability {

    public static final PalladiumProperty<Float> ENERGY_COST =
            new FloatProperty("energy_cost").configurable("Energy consumed when the ability is used.");
    public static final PalladiumProperty<Float> INSTABILITY_GAIN =
            new FloatProperty("instability_gain").configurable("Instability (0-100) added when the ability is used.");
    public static final PalladiumProperty<Integer> COOLDOWN_TICKS =
            new IntegerProperty("cooldown_ticks").configurable("Cooldown in ticks after use.");
    public static final PalladiumProperty<Integer> REQUIRED_STAGE =
            new IntegerProperty("required_stage").configurable("Minimum power stage (1-4) required to unlock this ability.");

    public GenesisAbility() {
        this.withProperty(ENERGY_COST, 0f);
        this.withProperty(INSTABILITY_GAIN, 0f);
        this.withProperty(COOLDOWN_TICKS, 0);
        this.withProperty(REQUIRED_STAGE, PowerProgressionManager.STAGE_MANIFESTATION);
    }

    /** @return true if the entity's progression stage allows using this ability. */
    protected boolean hasRequiredStage(LivingEntity entity, AbilityInstance entry) {
        return PowerProgressionManager.getStage(entity) >= entry.getProperty(REQUIRED_STAGE);
    }

    /**
     * Attempts to pay the energy + instability cost. Instability is always applied
     * (even on success) because exertion always strains the organism.
     *
     * @return true if the ability may fire.
     */
    protected boolean tryPayCost(LivingEntity entity, AbilityInstance entry) {
        if (!hasRequiredStage(entity, entry)) {
            return false;
        }
        float energyCost = entry.getProperty(ENERGY_COST);
        // High instability makes abilities more expensive.
        if (StabilityManager.isUnstable(entity)) {
            energyCost *= 1.5f;
        }
        if (!EnergyManager.consume(entity, energyCost)) {
            return false;
        }
        StabilityManager.addInstability(entity, entry.getProperty(INSTABILITY_GAIN));
        // Award power XP for using the ability, then re-evaluate stage.
        PowerProgressionManager.addPowerXp(entity, 1);
        PowerProgressionManager.recalculateStage(entity);
        return true;
    }
}
