package com.genesisdynamics.projectgenesis.forge.client;

import com.genesisdynamics.projectgenesis.ProjectGenesis;
import com.genesisdynamics.projectgenesis.client.GenesisHudOverlay;
import com.genesisdynamics.projectgenesis.client.keybind.GenesisKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectGenesis.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ProjectGenesisForgeClient {

    private ProjectGenesisForgeClient() {
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        for (net.minecraft.client.KeyMapping mapping : GenesisKeybinds.all()) {
            event.register(mapping);
        }
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "genesis_hud", (gui, graphics, partialTick, width, height) -> {
            GenesisHudOverlay.render(graphics, partialTick);
        });
    }
}
