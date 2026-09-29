package com.genesisdynamics.projectgenesis.power;

import com.genesisdynamics.projectgenesis.ProjectGenesis;
import com.genesisdynamics.projectgenesis.progression.PowerProgressionManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerValidator;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerCollector;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.provider.PowerProvider;
import net.threetag.palladium.power.provider.PowerProviders;
import net.threetag.palladiumcore.registry.RegistrySupplier;

/**
 * Grants the data-driven Genesis power matching the player's stored serum type.
 *
 * <p>This is Palladium's native extension point for "the player always has this
 * power while a condition holds". Reading the serum type from the player's
 * persistent NBT keeps the whole system server-authoritative and data-driven.</p>
 */
public class GenesisPowerProvider extends PowerProvider {

    public static final RegistrySupplier<PowerProvider> GENESIS =
            PowerProviders.PROVIDERS.register(ProjectGenesis.MOD_ID + "/serum", GenesisPowerProvider::new);

    @Override
    public void providePowers(LivingEntity entity, IPowerHandler handler, PowerCollector collector) {
        String serum = PowerProgressionManager.getSerumType(entity);
        if (serum.isEmpty()) {
            return;
        }
        PowerManager manager = PowerManager.getInstance(entity.level());
        Power power = manager.getPower(new ResourceLocation(ProjectGenesis.MOD_ID, "genesis_" + serum));
        if (power != null) {
            collector.addPower(power, () -> new Validator(serum));
        }
    }

    /** Keeps the power only while the player still carries the matching serum. */
    public record Validator(String serum) implements IPowerValidator {
        @Override
        public boolean stillValid(LivingEntity entity, Power power) {
            return serum.equals(PowerProgressionManager.getSerumType(entity));
        }
    }

    public static void init() {
        // Class-loading this class registers the provider above.
    }
}
