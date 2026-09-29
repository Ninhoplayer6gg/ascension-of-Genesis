package com.genesisdynamics.projectgenesis.suit;

import com.genesisdynamics.projectgenesis.player.EnergyManager;
import com.genesisdynamics.projectgenesis.player.StabilityManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Applies the passive modifiers granted by wearing a matching Genesis suit.
 *
 * <p>Called once per server tick per player. Modifiers are deliberately small and
 * multiplicative so that mixing suit pieces with the "wrong" power still works but
 * is suboptimal.</p>
 */
public final class SuitModifierManager {

    private SuitModifierManager() {
    }

    public static void serverTick(Player player) {
        String serum = com.genesisdynamics.projectgenesis.progression.PowerProgressionManager.getSerumType(player);
        if (serum.isEmpty()) {
            return;
        }

        int matchingPieces = 0;
        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.getItem() instanceof GenesisSuitItem suit && suit.getArchetype().equals(serum)) {
                matchingPieces++;
            }
        }

        if (matchingPieces == 0) {
            return;
        }

        float bonus = matchingPieces * 0.02f; // 2% per matching piece
        switch (serum) {
            case "atlas" -> {
                // Stability decays faster.
                StabilityManager.addInstability(player, -bonus * 0.5f);
            }
            case "volt" -> {
                // Energy regenerates faster.
                EnergyManager.addEnergy(player, bonus * 0.4f);
            }
            case "helios" -> {
                // Heat (stored as extra instability for now) dissipates faster.
                StabilityManager.addInstability(player, -bonus * 0.5f);
            }
            case "vector" -> {
                // Energy regenerates faster.
                EnergyManager.addEnergy(player, bonus * 0.4f);
            }
            default -> {
            }
        }
    }
}
