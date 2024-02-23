package useless.terrainapi;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import useless.terrainapi.api.TerrainAPI;
import useless.terrainapi.config.ConfigManager;
import useless.terrainapi.initialization.TerrainInitialization;


public class TerrainMain implements ModInitializer, GameStartEntrypoint {
	public static final Gson GSON = (new GsonBuilder()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    public static final String MOD_ID = "terrain-api";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	/**
	 * Prevents accidental use of API Features outside the proper initialization environment
	 */
	public static boolean LOCK_API = true;
    @Override
    public void onInitialize() {
        LOGGER.info("TerrainMain initialized.");
    }
	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {
		LOCK_API = false;
		new TerrainInitialization().onInitialize();
		FabricLoader.getInstance().getEntrypoints("terrain-api", TerrainAPI.class).forEach(api -> {
			// Make sure the method is implemented
			try {
				api.getClass().getDeclaredMethod("onInitialize");
				api.onInitialize();
			} catch (NoSuchMethodException ignored) {
			}
		});
		ConfigManager.saveAll();
		LOCK_API = true;
	}
}
