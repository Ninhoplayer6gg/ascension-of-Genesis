package com.genesisdynamics.projectgenesis.player;

import com.genesisdynamics.projectgenesis.progression.PowerProgressionManager;
import com.genesisdynamics.projectgenesis.suit.SuitModifierManager;
import net.minecraft.server.level.ServerPlayer;
import net.threetag.palladiumcore.event.LivingEntityEvents;

/**
 * Server-side tick handler for the Genesis resource systems.
 *
 * <p>Hooked to PalladiumCore's {@link LivingEntityEvents#TICK}, filtered to
 * server-side players only. Energy and Stability are never computed on the client,
 * guaranteeing correct multiplayer behaviour.</p>
 */
public final class GenesisPlayerTicker {

    private GenesisPlayerTicker() {
    }

    /** Registers the tick listener. Call once during common init. */
    public static void register() {
        LivingEntityEvents.TICK.register(entity -> {
            if (entity instanceof ServerPlayer player) {
                tick(player);
            }
        });
    }

    private static void tick(ServerPlayer player) {
        // Only spend CPU on players who actually have a Genesis power.
        if (!PowerProgressionManager.hasPower(player)) {
            return;
        }
        EnergyManager.serverTick(player);
        StabilityManager.serverTick(player);
        SuitModifierManager.serverTick(player);
    }
}
