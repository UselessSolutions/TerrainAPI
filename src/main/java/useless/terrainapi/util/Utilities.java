package useless.terrainapi.util;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.biome.Biome;

import java.lang.reflect.Array;

public class Utilities {

	/**Check if biome is present in the array of biomes
	 * @param biome Biome to check
	 * @param biomesKeys Array of biomes keys
	 * @return True if biome is in array
	 */
	public static boolean checkForBiomeInBiomes(Biome biome, String[] biomesKeys){
		for (String key: biomesKeys) {
			if (biome.equals(Registries.BIOMES.getItem(key))){
				return true;
			}
		}
		return false;
	}

	/**Check if biome is present in the array of biomes
	 * @param biome Biome to check
	 * @param biomes Array of biomes
	 * @return True if biome is in array
	 */
	public static boolean checkForBiomeInBiomes(Biome biome, Biome[] biomes){
		for (Biome checkBiome: biomes) {
			if (biome.equals(checkBiome)){
				return true;
			}
		}
		return false;
	}

	/**Combines two same type arrays
	 * @param a Array 1
	 * @param b Array 2
	 * @param <T> Type of both arrays
	 * @return Array A + Array B
	 */
	public static <T> T[] concatenate(T[] a, T[] b) {
		int aLen = a.length;
		int bLen = b.length;

		@SuppressWarnings("unchecked")
		T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}
}
