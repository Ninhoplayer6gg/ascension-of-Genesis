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

/** Passive: permanently increases movement speed (Genesis Volt). */
public class EnhancedSpeedAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> SPEED_MULTIPLIER =
            new FloatProperty("speed_multiplier").configurable("Multiplicative movement-speed bonus while active.");
    private static final UUID MODIFIER_ID = UUID.fromString("3c4d5e6f-7a8b-4c9d-0e1f-2a3b4c5d6e7f");

    public EnhancedSpeedAbility() {
        this.withProperty(SPEED_MULTIPLIER, 0.6f);
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide) {
            return;
        }
        AttributeInstance attr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr == null) {
            return;
        }
        AttributeModifier existing = attr.getModifier(MODIFIER_ID);
        if (enabled && existing == null) {
            attr.addTransientModifier(new AttributeModifier(MODIFIER_ID, "Genesis Volt speed", entry.getProperty(SPEED_MULTIPLIER), AttributeModifier.Operation.MULTIPLY_TOTAL));
        } else if (!enabled && existing != null) {
            attr.removeModifier(MODIFIER_ID);
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        AttributeInstance attr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr != null && attr.getModifier(MODIFIER_ID) != null) {
            attr.removeModifier(MODIFIER_ID);
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Passively increases movement speed.";
    }
}
