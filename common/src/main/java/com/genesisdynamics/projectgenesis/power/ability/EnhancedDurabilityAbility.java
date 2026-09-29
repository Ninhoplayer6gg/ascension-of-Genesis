package com.genesisdynamics.projectgenesis.power.ability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.UUID;

/** Passive: permanently increases armour and knockback resistance (Genesis Atlas). */
public class EnhancedDurabilityAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> ARMOR_BONUS =
            new FloatProperty("armor_bonus").configurable("Flat armour added while active.");
    public static final PalladiumProperty<Float> KB_RESISTANCE =
            new FloatProperty("knockback_resistance").configurable("Knockback resistance (0-1) added while active.");

    private static final UUID ARMOR_ID = UUID.fromString("1a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d");
    private static final UUID KB_ID = UUID.fromString("2b3c4d5e-6f7a-4b8c-9d0e-1f2a3b4c5d6e");

    public EnhancedDurabilityAbility() {
        this.withProperty(ARMOR_BONUS, 6f);
        this.withProperty(KB_RESISTANCE, 0.4f);
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide) {
            return;
        }
        apply(entity.getAttribute(Attributes.ARMOR), ARMOR_ID, "Genesis Atlas armor", enabled, entry.getProperty(ARMOR_BONUS));
        apply(entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE), KB_ID, "Genesis Atlas stability", enabled, entry.getProperty(KB_RESISTANCE));
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        remove(entity.getAttribute(Attributes.ARMOR), ARMOR_ID);
        remove(entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE), KB_ID);
    }

    private static void apply(AttributeInstance attr, UUID id, String name, boolean enabled, float value) {
        if (attr == null) {
            return;
        }
        AttributeModifier existing = attr.getModifier(id);
        if (enabled && existing == null) {
            attr.addTransientModifier(new AttributeModifier(id, name, value, AttributeModifier.Operation.ADDITION));
        } else if (!enabled && existing != null) {
            attr.removeModifier(id);
        }
    }

    private static void remove(AttributeInstance attr, UUID id) {
        if (attr != null && attr.getModifier(id) != null) {
            attr.removeModifier(id);
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Passively increases armour and knockback resistance.";
    }
}
