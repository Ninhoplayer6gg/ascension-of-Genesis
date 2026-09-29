package com.genesisdynamics.projectgenesis.fabric;

import com.genesisdynamics.projectgenesis.ProjectGenesis;
import net.fabricmc.api.ModInitializer;

public final class ProjectGenesisFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        // All registration is centralised in the common init.
        ProjectGenesis.init();
    }
}
