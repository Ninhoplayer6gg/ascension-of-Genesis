package com.genesisdynamics.projectgenesis.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

/**
 * Configurable keybinds for Genesis abilities.
 *
 * <p>No key is hardcoded inside any ability; abilities are triggered through these
 * mappings which the player can rebind in the vanilla Controls screen.</p>
 */
public final class GenesisKeybinds {

    public static final String CATEGORY = "key.categories.projectgenesis";

    public static final KeyMapping ABILITY_1 = new KeyMapping("key.projectgenesis.ability_1", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY);
    public static final KeyMapping ABILITY_2 = new KeyMapping("key.projectgenesis.ability_2", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, CATEGORY);
    public static final KeyMapping ABILITY_3 = new KeyMapping("key.projectgenesis.ability_3", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_N, CATEGORY);
    public static final KeyMapping ABILITY_4 = new KeyMapping("key.projectgenesis.ability_4", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, CATEGORY);
    public static final KeyMapping ULTIMATE = new KeyMapping("key.projectgenesis.ultimate", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, CATEGORY);
    public static final KeyMapping ABILITY_WHEEL = new KeyMapping("key.projectgenesis.ability_wheel", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, CATEGORY);

    private GenesisKeybinds() {
    }

    /** @return every keybind so loaders can register them in one loop. */
    public static KeyMapping[] all() {
        return new KeyMapping[]{ABILITY_1, ABILITY_2, ABILITY_3, ABILITY_4, ULTIMATE, ABILITY_WHEEL};
    }
}
