package com.genesisdynamics.projectgenesis.registry;

import com.genesisdynamics.projectgenesis.ProjectGenesis;
import com.genesisdynamics.projectgenesis.suit.GenesisSuitItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

/** Registers every Genesis suit piece (4 archetypes x 4 slots). */
public final class GenesisSuitItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ProjectGenesis.MOD_ID, Registries.ITEM);

    // ---- Atlas ----
    public static final RegistrySupplier<Item> ATLAS_HELMET = suit("atlas_suit_helmet", "atlas", GenesisSuitItem.Slot.HELMET);
    public static final RegistrySupplier<Item> ATLAS_CHESTPLATE = suit("atlas_suit_chestplate", "atlas", GenesisSuitItem.Slot.CHESTPLATE);
    public static final RegistrySupplier<Item> ATLAS_LEGGINGS = suit("atlas_suit_leggings", "atlas", GenesisSuitItem.Slot.LEGGINGS);
    public static final RegistrySupplier<Item> ATLAS_BOOTS = suit("atlas_suit_boots", "atlas", GenesisSuitItem.Slot.BOOTS);

    // ---- Volt ----
    public static final RegistrySupplier<Item> VOLT_HELMET = suit("volt_suit_helmet", "volt", GenesisSuitItem.Slot.HELMET);
    public static final RegistrySupplier<Item> VOLT_CHESTPLATE = suit("volt_suit_chestplate", "volt", GenesisSuitItem.Slot.CHESTPLATE);
    public static final RegistrySupplier<Item> VOLT_LEGGINGS = suit("volt_suit_leggings", "volt", GenesisSuitItem.Slot.LEGGINGS);
    public static final RegistrySupplier<Item> VOLT_BOOTS = suit("volt_suit_boots", "volt", GenesisSuitItem.Slot.BOOTS);

    // ---- Helios ----
    public static final RegistrySupplier<Item> HELIOS_HELMET = suit("helios_suit_helmet", "helios", GenesisSuitItem.Slot.HELMET);
    public static final RegistrySupplier<Item> HELIOS_CHESTPLATE = suit("helios_suit_chestplate", "helios", GenesisSuitItem.Slot.CHESTPLATE);
    public static final RegistrySupplier<Item> HELIOS_LEGGINGS = suit("helios_suit_leggings", "helios", GenesisSuitItem.Slot.LEGGINGS);
    public static final RegistrySupplier<Item> HELIOS_BOOTS = suit("helios_suit_boots", "helios", GenesisSuitItem.Slot.BOOTS);

    // ---- Vector ----
    public static final RegistrySupplier<Item> VECTOR_HELMET = suit("vector_suit_helmet", "vector", GenesisSuitItem.Slot.HELMET);
    public static final RegistrySupplier<Item> VECTOR_CHESTPLATE = suit("vector_suit_chestplate", "vector", GenesisSuitItem.Slot.CHESTPLATE);
    public static final RegistrySupplier<Item> VECTOR_LEGGINGS = suit("vector_suit_leggings", "vector", GenesisSuitItem.Slot.LEGGINGS);
    public static final RegistrySupplier<Item> VECTOR_BOOTS = suit("vector_suit_boots", "vector", GenesisSuitItem.Slot.BOOTS);

    private static RegistrySupplier<Item> suit(String name, String archetype, GenesisSuitItem.Slot slot) {
        return ITEMS.register(name, () -> new GenesisSuitItem(new Item.Properties(), archetype, slot));
    }

    private GenesisSuitItems() {
    }
}
