package com.genesisdynamics.projectgenesis.progression;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

/**
 * Tracks which Genesis serum a player carries and how far their power has progressed.
 *
 * <p>Progression stages: 1 = MANIFESTATION, 2 = ADAPTATION, 3 = MASTERY,
 * 4 = ASCENSION. Persisted in the player's NBT so it survives relog and death.</p>
 */
public final class PowerProgressionManager {

    public static final int STAGE_MANIFESTATION = 1;
    public static final int STAGE_ADAPTATION = 2;
    public static final int STAGE_MASTERY = 3;
    public static final int STAGE_ASCENSION = 4;

    private static final String NBT_KEY = "GenesisProgression";
    private static final String NBT_SERUM = "serum_type";
    private static final String NBT_STAGE = "power_stage";
    private static final String NBT_XP = "power_xp";

    private PowerProgressionManager() {
    }

    public static String getSerumType(LivingEntity entity) {
        return getTag(entity).getString(NBT_SERUM);
    }

    public static boolean hasPower(LivingEntity entity) {
        return !getSerumType(entity).isEmpty();
    }

    public static void setSerumType(LivingEntity entity, String serumType) {
        getTag(entity).putString(NBT_SERUM, serumType);
    }

    public static int getStage(LivingEntity entity) {
        int stage = getTag(entity).getInt(NBT_STAGE);
        return stage < STAGE_MANIFESTATION ? STAGE_MANIFESTATION : stage;
    }

    public static void setStage(LivingEntity entity, int stage) {
        getTag(entity).putInt(NBT_STAGE, Math.max(STAGE_MANIFESTATION, Math.min(STAGE_ASCENSION, stage)));
    }

    public static int getPowerXp(LivingEntity entity) {
        return getTag(entity).getInt(NBT_XP);
    }

    public static void addPowerXp(LivingEntity entity, int amount) {
        getTag(entity).putInt(NBT_XP, Math.max(0, getPowerXp(entity) + amount));
    }

    /** Simple V1 rule: XP thresholds drive stage-ups. Server-side only. */
    public static void recalculateStage(LivingEntity entity) {
        int xp = getPowerXp(entity);
        int stage = STAGE_MANIFESTATION;
        if (xp >= 300) {
            stage = STAGE_ASCENSION;
        } else if (xp >= 120) {
            stage = STAGE_MASTERY;
        } else if (xp >= 40) {
            stage = STAGE_ADAPTATION;
        }
        setStage(entity, stage);
    }

    public static void reset(LivingEntity entity) {
        entity.getPersistentData().remove(NBT_KEY);
    }

    private static CompoundTag getTag(LivingEntity entity) {
        CompoundTag root = entity.getPersistentData();
        if (!root.contains(NBT_KEY)) {
            root.put(NBT_KEY, new CompoundTag());
        }
        return root.getCompound(NBT_KEY);
    }
}
