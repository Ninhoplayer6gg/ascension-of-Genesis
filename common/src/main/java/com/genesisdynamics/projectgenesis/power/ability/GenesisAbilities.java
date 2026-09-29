package com.genesisdynamics.projectgenesis.power.ability;

import com.genesisdynamics.projectgenesis.ProjectGenesis;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

/**
 * Registers every custom Genesis ability with Palladium's ability registry.
 *
 * <p>Adding a new power later only requires registering another ability here and
 * referencing it from a new JSON file under {@code data/projectgenesis/palladium/powers}.</p>
 */
public final class GenesisAbilities {

    public static final DeferredRegister<Ability> ABILITIES =
            DeferredRegister.create(ProjectGenesis.MOD_ID, Ability.REGISTRY);

    // ---- Shared base (not directly referenced by JSON) ----
    // See GenesisAbility for the reusable base class.

    // ---- Genesis Atlas ----
    public static final RegistrySupplier<Ability> ENHANCED_STRENGTH = ABILITIES.register("enhanced_strength", EnhancedStrengthAbility::new);
    public static final RegistrySupplier<Ability> ENHANCED_DURABILITY = ABILITIES.register("enhanced_durability", EnhancedDurabilityAbility::new);
    public static final RegistrySupplier<Ability> POWER_JUMP = ABILITIES.register("power_jump", PowerJumpAbility::new);
    public static final RegistrySupplier<Ability> GROUND_IMPACT = ABILITIES.register("ground_impact", GroundImpactAbility::new);
    public static final RegistrySupplier<Ability> ATLAS_TITAN_STATE = ABILITIES.register("atlas_titan_state", AtlasTitanStateAbility::new);

    // ---- Genesis Volt ----
    public static final RegistrySupplier<Ability> ENHANCED_SPEED = ABILITIES.register("enhanced_speed", EnhancedSpeedAbility::new);
    public static final RegistrySupplier<Ability> ELECTRIC_DISCHARGE = ABILITIES.register("electric_discharge", ElectricDischargeAbility::new);
    public static final RegistrySupplier<Ability> ELECTRIC_DASH = ABILITIES.register("electric_dash", ElectricDashAbility::new);
    public static final RegistrySupplier<Ability> CHAIN_LIGHTNING = ABILITIES.register("chain_lightning", ChainLightningAbility::new);
    public static final RegistrySupplier<Ability> VOLT_OVERCHARGE = ABILITIES.register("volt_overcharge", VoltOverchargeAbility::new);

    // ---- Genesis Helios ----
    public static final RegistrySupplier<Ability> HELIOS_FLIGHT = ABILITIES.register("helios_flight", HeliosFlightAbility::new);
    public static final RegistrySupplier<Ability> ENERGY_BLAST = ABILITIES.register("energy_blast", EnergyBlastAbility::new);
    public static final RegistrySupplier<Ability> HEAT_BURST = ABILITIES.register("heat_burst", HeatBurstAbility::new);
    public static final RegistrySupplier<Ability> HELIOS_OVERDRIVE = ABILITIES.register("helios_overdrive", HeliosOverdriveAbility::new);

    // ---- Genesis Vector ----
    public static final RegistrySupplier<Ability> TELEKINETIC_PUSH = ABILITIES.register("telekinetic_push", TelekineticPushAbility::new);
    public static final RegistrySupplier<Ability> TELEKINETIC_PULL = ABILITIES.register("telekinetic_pull", TelekineticPullAbility::new);
    public static final RegistrySupplier<Ability> FORCE_FIELD = ABILITIES.register("force_field", ForceFieldAbility::new);
    public static final RegistrySupplier<Ability> VECTOR_BURST = ABILITIES.register("vector_burst", VectorBurstAbility::new);

    private GenesisAbilities() {
    }
}
