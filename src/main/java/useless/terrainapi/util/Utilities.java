package useless.terrainapi.util;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.biome.Biome;

public class Utilities {
	public static boolean checkForBiomeInBiomes(Biome biome, String[] biomesKeys){
		for (String key: biomesKeys) {
			if (biome.equals(Registries.BIOMES.getItem(key))){
				return true;
			}
		}
		return false;
	}
	public static boolean checkForBiomeInBiomes(Biome biome, Biome[] biomes){
		for (Biome checkBiome: biomes) {
			if (biome.equals(checkBiome)){
				return true;
			}
		}
		return false;
	}
}
