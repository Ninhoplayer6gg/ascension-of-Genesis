package com.genesisdynamics.projectgenesis.mixin;

import com.genesisdynamics.projectgenesis.player.GenesisDataHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements GenesisDataHolder {

    @Unique
    private static final String projectgenesis$NBT_KEY = "ProjectGenesisData";

    @Unique
    private CompoundTag projectgenesis$data = new CompoundTag();

    @Override
    public CompoundTag projectgenesis$getData() {
        return this.projectgenesis$data;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void projectgenesis$save(CompoundTag tag, CallbackInfo ci) {
        tag.put(projectgenesis$NBT_KEY, this.projectgenesis$data);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void projectgenesis$load(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains(projectgenesis$NBT_KEY, Tag.TAG_COMPOUND)) {
            this.projectgenesis$data = tag.getCompound(projectgenesis$NBT_KEY);
        }
    }
}
