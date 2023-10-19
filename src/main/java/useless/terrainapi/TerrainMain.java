package useless.terrainapi;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import useless.terrainapi.api.TerrainAPI;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;


public class TerrainMain implements ModInitializer {
    public static final String MOD_ID = "terrain-api";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
		LOGGER.info("TerrainMain loading modules.");
		loadModules();
        LOGGER.info("TerrainMain initialized.");
    }
	public void loadModules(){
		new TerrainInitialization().onInitialize();
		FabricLoader.getInstance().getEntrypoints("terrain-api", TerrainAPI.class).forEach(api -> {
			try {
				api.getClass().getDeclaredMethod("onInitialize"); // Make sure the method is implemented
				api.onInitialize();
			} catch (NoSuchMethodException ignored) {
			}
		});
	}
}
