package useless.terrainapi;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TerrainAPI implements ModInitializer {
    public static final String MOD_ID = "terrainapi";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("TerrainAPI initialized.");
    }
}
