package com.genesisdynamics.projectgenesis.power.ability;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

/** Active: telekinetically shoves a target away (Genesis Vector). */
public class TelekineticPushAbility extends GenesisAbility {

    public static final PalladiumProperty<Float> RANGE =
            new FloatProperty("range").configurable("Maximum targeting range.");
    public static final PalladiumProperty<Float> FORCE =
            new FloatProperty("force").configurable("Knockback force applied to the target.");

    public TelekineticPushAbility() {
        this.withProperty(RANGE, 16f);
        this.withProperty(FORCE, 2.5f);
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
        Vec3 look = entity.getLookAngle().normalize();
        target.push(look.x * entry.getProperty(FORCE), 0.4, look.z * entry.getProperty(FORCE));
        target.hurtMarked = true;
        level.sendParticles(ParticleTypes.CLOUD, target.getX(), target.getY() + 1, target.getZ(), 10, 0.3, 0.5, 0.3, 0.03);
        level.playSound(null, target.blockPosition(), SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 0.5f, 1.8f);
    }

    protected LivingEntity raycastTarget(LivingEntity self, ServerLevel level, float range) {
        Vec3 start = self.getEyePosition();
        Vec3 end = start.add(self.getLookAngle().scale(range));
        AABB box = self.getBoundingBox().expandTowards(self.getLookAngle().scale(range)).inflate(1.0);
        EntityHitResult hit = ProjectileUtil.getEntityHitResult(level, self, start, end, box, e -> e instanceof LivingEntity && e != self);
        return hit != null && hit.getEntity() instanceof LivingEntity living ? living : null;
    }

    @Override
    public String getDocumentationDescription() {
        return "Telekinetically shove the targeted enemy away.";
    }
}
