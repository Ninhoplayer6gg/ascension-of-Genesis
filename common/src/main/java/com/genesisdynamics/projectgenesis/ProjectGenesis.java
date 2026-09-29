package com.genesisdynamics.projectgenesis;

import com.genesisdynamics.projectgenesis.command.GenesisCommand;
import com.genesisdynamics.projectgenesis.condition.ConditionSerializers;
import com.genesisdynamics.projectgenesis.player.GenesisPlayerTicker;
import com.genesisdynamics.projectgenesis.power.GenesisPowerProvider;
import com.genesisdynamics.projectgenesis.power.ability.GenesisAbilities;
import com.genesisdynamics.projectgenesis.registry.GenesisCreativeTab;
import com.genesisdynamics.projectgenesis.registry.GenesisItems;
import com.genesisdynamics.projectgenesis.registry.GenesisSuitItems;
import net.threetag.palladiumcore.event.CommandEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PROJECT GENESIS — original-superhero powers mod.
 *
 * <p>Entry point shared by every loader. Registers custom Palladium abilities and
 * conditions; the actual powers (Atlas / Volt / Helios / Vector) are data-driven and
 * live under {@code data/projectgenesis/palladium/powers}.</p>
 */
public final class ProjectGenesis {

    public static final String MOD_ID = "projectgenesis";
    public static final String MOD_NAME = "PROJECT GENESIS";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    private ProjectGenesis() {
    }

    /** Called once from every loader-specific entry point during init. */
    public static void init() {
        GenesisAbilities.ABILITIES.register();
        ConditionSerializers.CONDITION_SERIALIZERS.register();
        GenesisItems.ITEMS.register();
        GenesisSuitItems.ITEMS.register();
        GenesisCreativeTab.TABS.register();
        GenesisCreativeTab.init();
        GenesisPlayerTicker.register();
        GenesisPowerProvider.init();
        CommandEvents.REGISTER.register((dispatcher, selection) -> GenesisCommand.register(dispatcher));
        LOGGER.info("[{}] Initialized. Genesis Event systems online.", MOD_NAME);
    }
}
