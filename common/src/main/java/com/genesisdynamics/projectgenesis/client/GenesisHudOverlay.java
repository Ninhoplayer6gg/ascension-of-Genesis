package com.genesisdynamics.projectgenesis.client;

import com.genesisdynamics.projectgenesis.player.EnergyManager;
import com.genesisdynamics.projectgenesis.player.StabilityManager;
import com.genesisdynamics.projectgenesis.progression.PowerProgressionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Modern, unobtrusive HUD for PROJECT GENESIS.
 *
 * <p>Draws Energy and Stability bars plus the selected ability / cooldown readout.
 * Automatically fades to nothing when the local player has no Genesis power, keeping
 * the vanilla UI clean. Architected so alternate skins/themes can be plugged in by
 * subclassing or by replacing {@link #render}.</p>
 */
public class GenesisHudOverlay {

    private static final int BAR_WIDTH = 90;
    private static final int BAR_HEIGHT = 6;
    private static final int MARGIN = 8;

    private static final int COLOR_ENERGY = 0xFF3EC6FF;   // cyan
    private static final int COLOR_STABILITY = 0xFFFFB347; // amber
    private static final int COLOR_BACKGROUND = 0x80000000; // translucent black

    /** Called from the loader-specific HUD render event every frame. */
    public static void render(GuiGraphics graphics, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }
        // Stay hidden when the player has no Genesis power.
        if (!PowerProgressionManager.hasPower(mc.player)) {
            return;
        }

        float energy = EnergyManager.getEnergy(mc.player);
        float maxEnergy = EnergyManager.getMaxEnergy(mc.player);
        float instability = StabilityManager.getInstability(mc.player);

        int x = MARGIN;
        int y = graphics.guiHeight() - MARGIN - (BAR_HEIGHT * 2) - 18;

        // Energy bar
        drawBar(graphics, x, y, energy / maxEnergy, COLOR_ENERGY, "ENERGY");
        // Stability bar (full bar = fully stable; drains as instability rises)
        drawBar(graphics, x, y + BAR_HEIGHT + 4, 1.0f - (instability / 100f), COLOR_STABILITY, "STABILITY");

        // Selected ability + cooldown readout (populated by Palladium's keybind layer)
        String selected = ClientAbilityTracker.getSelectedAbilityName();
        if (selected != null && !selected.isEmpty()) {
            graphics.drawString(mc.font, selected, x, y + (BAR_HEIGHT + 4) * 2, 0xFFFFFFFF, false);
        }
        float cooldown = ClientAbilityTracker.getSelectedCooldownSeconds();
        if (cooldown > 0f) {
            graphics.drawString(mc.font, String.format("%.1fs", cooldown), x, y + (BAR_HEIGHT + 4) * 2 + 10, 0xFFFF5555, false);
        }
    }

    private static void drawBar(GuiGraphics graphics, int x, int y, float fraction, int color, String label) {
        fraction = Math.max(0f, Math.min(1f, fraction));
        Minecraft mc = Minecraft.getInstance();
        graphics.drawString(mc.font, label, x, y - 9, 0xFFFFFFFF, false);
        graphics.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, COLOR_BACKGROUND);
        graphics.fill(x, y, x + (int) (BAR_WIDTH * fraction), y + BAR_HEIGHT, color);
    }
}
