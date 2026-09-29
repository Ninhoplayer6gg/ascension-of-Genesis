package com.genesisdynamics.projectgenesis.condition;

import com.genesisdynamics.projectgenesis.ProjectGenesis;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

/** Registers custom Palladium conditions used by the Genesis power data. */
public final class ConditionSerializers {

    public static final DeferredRegister<ConditionSerializer> CONDITION_SERIALIZERS =
            DeferredRegister.create(ProjectGenesis.MOD_ID, ConditionSerializer.REGISTRY);

    public static final RegistrySupplier<ConditionSerializer> HAS_STABILITY =
            CONDITION_SERIALIZERS.register("has_stability", HasStabilityCondition.Serializer::new);

    private ConditionSerializers() {
    }
}
