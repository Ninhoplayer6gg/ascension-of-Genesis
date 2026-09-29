package com.genesisdynamics.projectgenesis.registry;

import com.genesisdynamics.projectgenesis.ProjectGenesis;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

/** The PROJECT GENESIS creative tab. */
public final class GenesisCreativeTab {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(ProjectGenesis.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> PROJECT_GENESIS = TABS.register("project_genesis",
            () -> CreativeModeTabRegistry.create(
                    Component.translatable("itemGroup.projectgenesis"),
                    () -> new ItemStack(GenesisItems.GENESIS_VOLT_SERUM.get())));

    /** Populates the tab. Call after all item registries are ready. */
    public static void init() {
        CreativeModeTabRegistry.addToTab(PROJECT_GENESIS, entries -> {
            // Serums
            entries.add(GenesisItems.GENESIS_ATLAS_SERUM.get());
            entries.add(GenesisItems.GENESIS_VOLT_SERUM.get());
            entries.add(GenesisItems.GENESIS_HELIOS_SERUM.get());
            entries.add(GenesisItems.GENESIS_VECTOR_SERUM.get());
            // Suits
            GenesisSuitItems.ITEMS.getEntries().forEach(entry -> entries.add(entry.get()));
        });
    }

    private GenesisCreativeTab() {
    }
}
