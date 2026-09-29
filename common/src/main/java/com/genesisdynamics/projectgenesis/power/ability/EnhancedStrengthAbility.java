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

/** Passive: permanently increases attack damage (Genesis Atlas). */
public class EnhancedStrengthAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> DAMAGE_BONUS =
            new FloatProperty("damage_bonus").configurable("Flat attack damage added while active.");
    private static final UUID MODIFIER_ID = UUID.fromString("7d3f2a1e-9b4c-4a5d-8e6f-1a2b3c4d5e6f");

    public EnhancedStrengthAbility() {
        this.withProperty(DAMAGE_BONUS, 4f);
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide) {
            return;
        }
        AttributeInstance attr = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attr == null) {
            return;
        }
        AttributeModifier existing = attr.getModifier(MODIFIER_ID);
        if (enabled && existing == null) {
            attr.addTransientModifier(new AttributeModifier(MODIFIER_ID, "Genesis Atlas strength", entry.getProperty(DAMAGE_BONUS), AttributeModifier.Operation.ADDITION));
        } else if (!enabled && existing != null) {
            attr.removeModifier(MODIFIER_ID);
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        AttributeInstance attr = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attr != null && attr.getModifier(MODIFIER_ID) != null) {
            attr.removeModifier(MODIFIER_ID);
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Passively increases attack damage.";
    }
}
