package useless.terrainapi.config;


import net.fabricmc.loader.api.FabricLoader;
import useless.terrainapi.TerrainMain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class TerrainAPIConfigManager {
	private static final HashMap<String, File> fileHashMap = new HashMap<>();
	private static final HashMap<String, TerrainAPIConfig> configHashMap = new HashMap<>();

	private static void prepareBiomeConfigFile(String id) {
		if (fileHashMap.get(id) != null) {
			return;
		}
		Path filePath = Paths.get(FabricLoader.getInstance().getConfigDirectory() + "/" + TerrainMain.MOD_ID);
		try {
			Files.createDirectories(filePath);
		} catch (IOException e) {
			System.err.println("Failed to create directory!" + e.getMessage());
		}
		fileHashMap.put(id, new File(filePath.toFile(), id + ".json"));
	}
	private static void load(String id) {
		prepareBiomeConfigFile(id);

		try {
			if (!fileHashMap.get(id).exists()) {
				save(id);
			}
			if (fileHashMap.get(id).exists()) {
				BufferedReader br = new BufferedReader(new FileReader(fileHashMap.get(id)));

				configHashMap.put(id, TerrainMain.GSON.fromJson(br, TerrainAPIConfig.class));
				save(id);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load TerrainAPI configuration file; reverting to defaults");
			e.printStackTrace();
		}
	}
	public static void save(String id) {
		prepareBiomeConfigFile(id);

		String jsonString = TerrainMain.GSON.toJson(configHashMap.get(id));
		TerrainMain.LOGGER.info(jsonString);

		try (FileWriter fileWriter = new FileWriter(fileHashMap.get(id))) {
			fileWriter.write(jsonString);
		} catch (IOException e) {
			System.err.println("Couldn't save TerrainAPI configuration file");
			e.printStackTrace();
		}
	}
	public static void saveAll(){
		for (String id: configHashMap.keySet()) {
			save(id);
		}
	}
	public static TerrainAPIConfig getConfig(String id) {
		if (configHashMap.get(id) == null){
			configHashMap.put(id, new TerrainAPIConfig());
		}
		load(id);
		TerrainAPIConfig config = configHashMap.get(id);
		if (config.getConfigOverride()){
			return configHashMap.getOrDefault(id, new TerrainAPIConfig());
		} else {
			configHashMap.put(id, new TerrainAPIConfig());
			return configHashMap.get(id);
		}

	}
}
