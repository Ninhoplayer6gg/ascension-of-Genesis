package com.genesisdynamics.projectgenesis.registry;

import com.genesisdynamics.projectgenesis.ProjectGenesis;
import com.genesisdynamics.projectgenesis.item.GenesisSerumItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

/** Registers all PROJECT GENESIS items. */
public final class GenesisItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ProjectGenesis.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> GENESIS_ATLAS_SERUM =
            ITEMS.register("genesis_atlas_serum", () -> new GenesisSerumItem(new Item.Properties(), "atlas", "genesis_atlas"));
    public static final RegistrySupplier<Item> GENESIS_VOLT_SERUM =
            ITEMS.register("genesis_volt_serum", () -> new GenesisSerumItem(new Item.Properties(), "volt", "genesis_volt"));
    public static final RegistrySupplier<Item> GENESIS_HELIOS_SERUM =
            ITEMS.register("genesis_helios_serum", () -> new GenesisSerumItem(new Item.Properties(), "helios", "genesis_helios"));
    public static final RegistrySupplier<Item> GENESIS_VECTOR_SERUM =
            ITEMS.register("genesis_vector_serum", () -> new GenesisSerumItem(new Item.Properties(), "vector", "genesis_vector"));

    private GenesisItems() {
    }
}
