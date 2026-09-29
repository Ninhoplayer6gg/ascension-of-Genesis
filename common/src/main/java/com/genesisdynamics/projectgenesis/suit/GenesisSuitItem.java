package com.genesisdynamics.projectgenesis.suit;

import net.minecraft.world.item.Item;

/**
 * A single piece of a Genesis suit (helmet / chestplate / leggings / boots).
 *
 * <p>Suits are pure equipment: they never grant the underlying power, only improve
 * it. This class mainly carries the suit's archetype so modifiers can be matched
 * to the player's serum type.</p>
 */
public class GenesisSuitItem extends Item {

    private final String archetype;
    private final Slot slot;

    public enum Slot {
        HELMET, CHESTPLATE, LEGGINGS, BOOTS
    }

    public GenesisSuitItem(Properties properties, String archetype, Slot slot) {
        super(properties.stacksTo(1));
        this.archetype = archetype;
        this.slot = slot;
    }

    public String getArchetype() {
        return archetype;
    }

    public Slot getSlot() {
        return slot;
    }
}
