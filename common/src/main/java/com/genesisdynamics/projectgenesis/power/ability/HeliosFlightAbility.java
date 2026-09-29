package com.genesisdynamics.projectgenesis.power.ability;

import com.genesisdynamics.projectgenesis.player.GenesisDataHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

/**
 * Movement: creative-style flight that generates HEAT (Genesis Helios).
 *
 * <p>Flight is allowed while active. Heat builds while flying and dissipates when
 * grounded; overheating forces the player to land. Implemented server-side only;
 * vanilla ability syncing handles the client.</p>
 */
public class HeliosFlightAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> HEAT_PER_SECOND =
            new FloatProperty("heat_per_second").configurable("Heat generated per second of flight.");
    public static final PalladiumProperty<Float> MAX_HEAT =
            new FloatProperty("max_heat").configurable("Heat at which flight is forcibly cut.");

    private static final String NBT_HEAT = "GenesisHeliosHeat";

    public HeliosFlightAbility() {
        this.withProperty(HEAT_PER_SECOND, 4f);
        this.withProperty(MAX_HEAT, 100f);
        this.withProperty(ENERGY_COST, 0f); // energy drain is heat, not energy
        this.withProperty(INSTABILITY_GAIN, 0f);
        this.withProperty(COOLDOWN_TICKS, 0);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (!(entity instanceof Player player) || entity.level().isClientSide) {
            return;
        }
        if (enabled) {
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (!(entity instanceof Player player) || entity.level().isClientSide) {
            return;
        }
        float heat = GenesisDataHolder.get(entity).getFloat(NBT_HEAT);
        float maxHeat = entry.getProperty(MAX_HEAT);

        if (enabled && player.getAbilities().flying) {
            heat += entry.getProperty(HEAT_PER_SECOND) / 20f;
            if (heat >= maxHeat) {
                // Overheated: cut flight and start cooling.
                player.getAbilities().flying = false;
                player.getAbilities().mayfly = false;
                player.onUpdateAbilities();
            }
        } else {
            // Cool down when not actively flying.
            heat = Math.max(0f, heat - (entry.getProperty(HEAT_PER_SECOND) * 1.5f) / 20f);
        }
        GenesisDataHolder.get(entity).putFloat(NBT_HEAT, heat);
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity instanceof Player player && !entity.level().isClientSide) {
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Fly, but watch the heat gauge.";
    }
}
