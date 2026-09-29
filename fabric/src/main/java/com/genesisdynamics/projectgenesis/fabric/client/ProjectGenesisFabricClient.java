package com.genesisdynamics.projectgenesis.fabric.client;

import com.genesisdynamics.projectgenesis.client.GenesisHudOverlay;
import com.genesisdynamics.projectgenesis.client.keybind.GenesisKeybinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public final class ProjectGenesisFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (net.minecraft.client.KeyMapping mapping : GenesisKeybinds.all()) {
            KeyBindingHelper.registerKeyBinding(mapping);
        }
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> GenesisHudOverlay.render(graphics, tickDelta));
    }
}
