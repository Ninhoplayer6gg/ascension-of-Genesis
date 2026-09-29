package com.genesisdynamics.projectgenesis.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

/**
 * Server-authoritative STABILITY system.
 *
 * <p>Stability is <b>not</b> mana. It models how much physiological strain the
 * organism can endure. 0 % = completely stable; 100 % = critical instability.
 * Powerful abilities generate instability; it decays gradually over time. High
 * instability makes abilities cost more and locks Ascension-tier powers, but never
 * kills the player.</p>
 */
public final class StabilityManager {

    private static final String NBT_KEY = "GenesisStability";
    private static final String NBT_INSTABILITY = "instability";

    /** Instability decays by this many points per second. */
    public static final float DECAY_PER_SECOND = 1.5f;
    /** Above this threshold abilities cost extra energy. */
    public static final float HIGH_INSTABILITY = 60f;
    /** Above this threshold Ascension abilities are unavailable. */
    public static final float ASCENSION_LOCKOUT = 80f;

    private StabilityManager() {
    }

    public static float getInstability(LivingEntity entity) {
        return getTag(entity).getFloat(NBT_INSTABILITY);
    }

    public static void setInstability(LivingEntity entity, float value) {
        getTag(entity).putFloat(NBT_INSTABILITY, Math.max(0f, Math.min(100f, value)));
    }

    public static void addInstability(LivingEntity entity, float delta) {
        setInstability(entity, getInstability(entity) + delta);
    }

    /** @return true when instability is high enough to raise ability costs. */
    public static boolean isUnstable(LivingEntity entity) {
        return getInstability(entity) >= HIGH_INSTABILITY;
    }

    /** @return true when Ascension-tier abilities are locked out. */
    public static boolean isAscensionLocked(LivingEntity entity) {
        return getInstability(entity) >= ASCENSION_LOCKOUT;
    }

    /** Called every server tick for players. */
    public static void serverTick(ServerPlayer player) {
        addInstability(player, -DECAY_PER_SECOND / 20f);
    }

    private static CompoundTag getTag(LivingEntity entity) {
        CompoundTag root = GenesisDataHolder.get(entity);
        if (!root.contains(NBT_KEY)) {
            root.put(NBT_KEY, new CompoundTag());
        }
        return root.getCompound(NBT_KEY);
    }
}
