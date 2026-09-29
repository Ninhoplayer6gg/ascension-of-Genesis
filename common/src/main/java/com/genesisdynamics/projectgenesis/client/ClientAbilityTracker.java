package com.genesisdynamics.projectgenesis.client;

/**
 * Client-side cache of the currently selected Genesis ability and its cooldown.
 *
 * <p>Kept tiny and allocation-free. Values are written by the keybind/selection layer
 * and read by {@link GenesisHudOverlay} each frame.</p>
 */
public final class ClientAbilityTracker {

    private static volatile String selectedAbilityName = "";
    private static volatile float selectedCooldownSeconds = 0f;

    private ClientAbilityTracker() {
    }

    public static String getSelectedAbilityName() {
        return selectedAbilityName;
    }

    public static void setSelectedAbilityName(String name) {
        selectedAbilityName = name == null ? "" : name;
    }

    public static float getSelectedCooldownSeconds() {
        return selectedCooldownSeconds;
    }

    public static void setSelectedCooldownSeconds(float seconds) {
        selectedCooldownSeconds = Math.max(0f, seconds);
    }
}
