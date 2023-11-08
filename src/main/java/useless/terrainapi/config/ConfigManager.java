package useless.terrainapi.config;


import net.fabricmc.loader.api.FabricLoader;
import useless.terrainapi.TerrainMain;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ConfigManager {
	private static final HashMap<String, File> fileHashMap = new HashMap<>();
	private static final HashMap<String, APIConfig> configHashMap = new HashMap<>();

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
	private static void load(String id, Class<? extends APIConfig> clazz) {
		prepareBiomeConfigFile(id);

		try {
			if (!fileHashMap.get(id).exists()) {
				save(id);
			}
			if (fileHashMap.get(id).exists()) {
				BufferedReader br = new BufferedReader(new FileReader(fileHashMap.get(id)));
				configHashMap.put(id, TerrainMain.GSON.fromJson(br, clazz));
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
	public static <T extends APIConfig> T getConfig(String id, Class<T> classOfT) {
		if (configHashMap.get(id) == null){
			try {
				configHashMap.put(id, classOfT.getDeclaredConstructor().newInstance());
				load(id, classOfT);
				APIConfig config = configHashMap.get(id);
				if (config.getConfigOverride()){
					return classOfT.cast(configHashMap.getOrDefault(id, classOfT.getDeclaredConstructor().newInstance()));
				} else {
					configHashMap.put(id, classOfT.getDeclaredConstructor().newInstance());
					return classOfT.cast(configHashMap.get(id));
				}
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException |
					 NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
}
