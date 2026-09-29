package com.genesisdynamics.projectgenesis.condition;

import com.genesisdynamics.projectgenesis.player.StabilityManager;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.util.context.DataContext;

/**
 * Condition that is met while the entity's instability is at or below a threshold.
 *
 * <p>Used in power JSON to gate Ascension-tier abilities and to raise costs when the
 * organism is destabilised. Example JSON:
 * <pre>{ "type": "projectgenesis:has_stability", "max_instability": 79.0 }</pre></p>
 */
public class HasStabilityCondition extends Condition {

    private final float maxInstability;

    public HasStabilityCondition(float maxInstability) {
        this.maxInstability = maxInstability;
    }

    @Override
    public boolean active(DataContext context) {
        if (!(context.getEntity() instanceof LivingEntity entity)) {
            return false;
        }
        return StabilityManager.getInstability(entity) <= maxInstability;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.HAS_STABILITY.get();
    }

    public static class Serializer extends ConditionSerializer {
        @Override
        public Condition make(JsonObject json) {
            return new HasStabilityCondition(GsonHelper.getAsFloat(json, "max_instability", 100f));
        }

        @Override
        public String getDocumentationDescription() {
            return "True while the entity's instability is at or below 'max_instability'.";
        }
    }
}
