package useless.terrainapi;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import useless.terrainapi.api.TerrainAPI;
import useless.terrainapi.config.ConfigManager;
import useless.terrainapi.initialization.TerrainInitialization;


public class TerrainMain implements ModInitializer {
	public static final Gson GSON = (new GsonBuilder()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    public static final String MOD_ID = "terrain-api";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("TerrainMain initialized.");
    }
	@ApiStatus.Internal
	public static void loadModules(){
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
	}
}
