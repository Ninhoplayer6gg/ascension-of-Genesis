package com.genesisdynamics.projectgenesis.forge;

import com.genesisdynamics.projectgenesis.ProjectGenesis;
import net.minecraftforge.fml.common.Mod;

@Mod(ProjectGenesis.MOD_ID)
public final class ProjectGenesisForge {

    public ProjectGenesisForge() {
        // All registration is centralised in the common init.
        ProjectGenesis.init();
    }
}
