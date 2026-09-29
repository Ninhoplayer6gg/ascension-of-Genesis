package com.genesisdynamics.projectgenesis.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

/**
 * Loader-agnostic replacement for Forge's {@code getPersistentData()}.
 *
 * <p>Implemented on every {@link LivingEntity} by
 * {@link com.genesisdynamics.projectgenesis.mixin.LivingEntityMixin}, which saves the tag
 * with the entity.</p>
 */
public interface GenesisDataHolder {

    CompoundTag projectgenesis$getData();

    static CompoundTag get(LivingEntity entity) {
        return ((GenesisDataHolder) entity).projectgenesis$getData();
    }
}
