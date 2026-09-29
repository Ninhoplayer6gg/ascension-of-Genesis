package com.genesisdynamics.projectgenesis.power.ability;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

/**
 * Defense: personal force field that grants resistance and absorption while held
 * (Genesis Vector). Drains energy each second while active.
 */
public class ForceFieldAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> ENERGY_PER_SECOND =
            new FloatProperty("energy_per_second").configurable("Energy drained per second while the field is up.");
    public static final PalladiumProperty<Integer> REFRESH_TICKS =
            new IntegerProperty("refresh_ticks").configurable("How often the protective effect is refreshed.");

    public ForceFieldAbility() {
        this.withProperty(ENERGY_PER_SECOND, 8f);
        this.withProperty(REFRESH_TICKS, 40);
        this.withProperty(INSTABILITY_GAIN, 0f);
        this.withProperty(COOLDOWN_TICKS, 0);
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide || !enabled) {
            return;
        }
        // Drain energy every second.
        if (entity.tickCount % 20 == 0) {
            if (!com.genesisdynamics.projectgenesis.player.EnergyManager.consume(entity, entry.getProperty(ENERGY_PER_SECOND))) {
                // Out of energy: drop the field by disabling via effects expiring.
                return;
            }
            com.genesisdynamics.projectgenesis.player.StabilityManager.addInstability(entity, 1f);
        }
        if (entity.tickCount % entry.getProperty(REFRESH_TICKS) == 0) {
            int duration = entry.getProperty(REFRESH_TICKS) + 10;
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, 1));
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Project a personal force field that absorbs incoming damage.";
    }
}
