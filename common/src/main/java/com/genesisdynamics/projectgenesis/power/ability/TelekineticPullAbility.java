package com.genesisdynamics.projectgenesis.power.ability;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

/** Active: telekinetically yanks a target toward the user (Genesis Vector). */
public class TelekineticPullAbility extends TelekineticPushAbility {

    public TelekineticPullAbility() {
        this.withProperty(FORCE, 2.0f);
        this.withProperty(ENERGY_COST, 14f);
        this.withProperty(INSTABILITY_GAIN, 7f);
        this.withProperty(COOLDOWN_TICKS, 45);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity.level().isClientSide || !enabled || !(entity.level() instanceof ServerLevel level)) {
            return;
        }
        if (!tryPayCost(entity, entry)) {
            return;
        }
        LivingEntity target = raycastTarget(entity, level, entry.getProperty(RANGE));
        if (target == null) {
            return;
        }
        Vec3 toward = entity.position().subtract(target.position()).normalize();
        target.push(toward.x * entry.getProperty(FORCE), 0.3, toward.z * entry.getProperty(FORCE));
        target.hurtMarked = true;
        level.sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY() + 1, target.getZ(), 10, 0.3, 0.5, 0.3, 0.03);
        level.playSound(null, target.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5f, 1.4f);
    }

    @Override
    public String getDocumentationDescription() {
        return "Telekinetically yank the targeted enemy toward you.";
    }
}
