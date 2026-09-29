package com.genesisdynamics.projectgenesis.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/**
 * Server-authoritative ENERGY pool.
 *
 * <p>Energy is the short-term resource spent by most active abilities. It regenerates
 * relatively quickly and is capped by {@link #getMaxEnergy}. The full state is stored
 * inside the player's persistent NBT so it survives relog and (optionally) death.</p>
 */
public final class EnergyManager {

    private static final String NBT_KEY = "GenesisEnergy";
    private static final String NBT_ENERGY = "energy";

    /** Base maximum energy. Suit modifiers may raise this in later versions. */
    public static final float BASE_MAX_ENERGY = 100f;
    /** Energy regenerated per second while not overloading. */
    public static final float REGEN_PER_SECOND = 6f;

    private EnergyManager() {
    }

    public static float getEnergy(LivingEntity entity) {
        CompoundTag tag = getTag(entity);
        return tag.getFloat(NBT_ENERGY);
    }

    public static float getMaxEnergy(LivingEntity entity) {
        return BASE_MAX_ENERGY;
    }

    public static void setEnergy(LivingEntity entity, float value) {
        float clamped = Math.max(0f, Math.min(getMaxEnergy(entity), value));
        getTag(entity).putFloat(NBT_ENERGY, clamped);
    }

    public static void addEnergy(LivingEntity entity, float delta) {
        setEnergy(entity, getEnergy(entity) + delta);
    }

    /** @return true if the entity had enough energy and it was consumed. */
    public static boolean consume(LivingEntity entity, float amount) {
        if (amount <= 0f) {
            return true;
        }
        float current = getEnergy(entity);
        if (current < amount) {
            return false;
        }
        setEnergy(entity, current - amount);
        return true;
    }

    /** Called every server tick for players. */
    public static void serverTick(ServerPlayer player) {
        addEnergy(player, REGEN_PER_SECOND / 20f);
    }

    private static CompoundTag getTag(LivingEntity entity) {
        CompoundTag root = entity.getPersistentData();
        if (!root.contains(NBT_KEY)) {
            root.put(NBT_KEY, new CompoundTag());
        }
        return root.getCompound(NBT_KEY);
    }
}
