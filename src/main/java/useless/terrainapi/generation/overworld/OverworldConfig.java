package useless.terrainapi.generation.overworld;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import useless.terrainapi.config.OreConfig;
import useless.terrainapi.util.Utilities;

import javax.annotation.Nullable;
import java.util.HashMap;

public class OverworldConfig extends OreConfig {
	@Expose @SerializedName(value = "Biome Random Grass Block")
	public HashMap<String, String> biomeRandomGrassBlock = new HashMap<>();
	@Expose @SerializedName(value = "Grass Density")
	public HashMap<String, Integer> grassDensityMap = new HashMap<>();
	@Expose @SerializedName(value = "Flower Density")
	public HashMap<String, Integer> flowerDensityMap = new HashMap<>();
	@Expose @SerializedName(value = "Yellow Flower Density")
	public HashMap<String, Integer> yellowFlowerDensityMap = new HashMap<>();
	@Expose @SerializedName(value = "Tree Density")
	public HashMap<String, Integer> treeDensityMap = new HashMap<>();
	@Expose @SerializedName(value = "Lake Density")
	public HashMap<String, Integer> lakeDensityMap = new HashMap<>();
	@Expose @SerializedName(value = "World Feature Chance")
	public HashMap<String, Integer> featureChanceMap = new HashMap<>();
	public int defaultLakeDensity = 4;

	/** Specifies the inverse of the chance of a world feature via key
	 */
	public void addFeatureChance(String modId, String key, int chanceINV) {
		if (getConfigOverride() && getFeatureChance(modId, key) != null){
			return;
		}
		featureChanceMap.put(modId + ":" + key, chanceINV);
	}
	/**
	 * @return the inverse of the chance of a world feature via key
	 */
	@Nullable
	public Integer getFeatureChance(String modId, String key){
		return featureChanceMap.get(modId + ":" + key);
	}
	/**
	 * @return the inverse of the chance of a world feature via key
	 */
	public int getFeatureChanceOrDefault(String modId, String key, int defaultValue){
		return featureChanceMap.getOrDefault(modId + ":" + key, defaultValue);
	}

	/**Specifies the block to randomly replace some grass with in the specified biome
	 */
	public void addRandomGrassBlock(Biome biome, Block block) {
		if (getConfigOverride() && getRandomGrassBlock(biome) != null){
			return;
		}
		biomeRandomGrassBlock.put(Registries.BIOMES.getKey(biome), block.getKey());
	}

	/**
	 * @return Biome's random grass block, returns null if there is no entry for the biome
	 */
	@Nullable
	public Block getRandomGrassBlock(Biome biome){
		return Utilities.getBlock(biomeRandomGrassBlock.get(Registries.BIOMES.getKey(biome)));
	}

	/**
	 * @return Biome's random grass block, returns defaultValue if there is no entry for the biome
	 */
	@NotNull
	public Block getRandomGrassBlock(Biome biome, Block defaultValue){
		Block returnBlock = Utilities.getBlock(biomeRandomGrassBlock.get(Registries.BIOMES.getKey(biome)));
		if (returnBlock == null){
			returnBlock = defaultValue;
		}
		return returnBlock;
	}

	/**Specifies the number of chances for grass to spawn for the specified biome
	 */
	public void addGrassDensity(Biome biome, int density){
		if (getConfigOverride() && getGrassDensity(biome) != null){
			return;
		}
		grassDensityMap.put(Registries.BIOMES.getKey(biome), density);
	}

	/**
	 * @return Biome's grass density, returns null if there is no entry for the biome
	 */
	@Nullable
	public Integer getGrassDensity(Biome biome){
		return grassDensityMap.get(Registries.BIOMES.getKey(biome));
	}

	/**
	 * @return Biome's grass density, returns defaultValue if there is no entry for the biome
	 */
	@NotNull
	public Integer getGrassDensity(Biome biome, int defaultValue){
		return grassDensityMap.getOrDefault(Registries.BIOMES.getKey(biome), defaultValue);
	}

	/**Specifies the number of chances for red/yellow flowers patches to spawn for the specified biome
	 */
	public void addFlowerDensity(Biome biome, int density){
		if (getConfigOverride() && getFlowerDensity(biome) != null){
			return;
		}
		flowerDensityMap.put(Registries.BIOMES.getKey(biome), density);
	}

	/**
	 * @return Biome's red/yellow density, returns null if there is no entry for the biome
	 */
	@Nullable
	public Integer getFlowerDensity(Biome biome){
		return flowerDensityMap.get(Registries.BIOMES.getKey(biome));
	}

	/**
	 * @return Biome's red/yellow density, returns defaultValue if there is no entry for the biome
	 */
	@NotNull
	public Integer getFlowerDensity(Biome biome, int defaultValue){
		return flowerDensityMap.getOrDefault(Registries.BIOMES.getKey(biome), defaultValue);
	}

	/**Specifies the number of chances for yellow flowers to spawn for the specified biome
	 */
	public void addYellowFlowerDensity(Biome biome, int density){
		if (getConfigOverride() && getYellowFlowerDensity(biome) != null){
			return;
		}
		yellowFlowerDensityMap.put(Registries.BIOMES.getKey(biome), density);
	}

	/**
	 * @return Biome's yellow flower density, returns null if there is no entry for the biome
	 */
	@Nullable
	public Integer getYellowFlowerDensity(Biome biome){
		return yellowFlowerDensityMap.get(Registries.BIOMES.getKey(biome));
	}

	/**
	 * @return Biome's yellow flower density, returns defaultValue if there is no entry for the biome
	 */
	@NotNull
	public Integer getYellowFlowerDensity(Biome biome, int defaultValue){
		return yellowFlowerDensityMap.getOrDefault(Registries.BIOMES.getKey(biome), defaultValue);
	}

	/**Specifies the number of chances for trees to spawn for the specified biome
	 */
	public void addTreeDensity(Biome biome, int density){
		if (getConfigOverride() && getTreeDensity(biome) != null){
			return;
		}
		treeDensityMap.put(Registries.BIOMES.getKey(biome), density);
	}

	/**
	 * @return Biome's tree density, returns null if there is no entry for the biome
	 */
	@Nullable
	public Integer getTreeDensity(Biome biome){
		return treeDensityMap.get(Registries.BIOMES.getKey(biome));
	}

	/**
	 * @return Biome's tree density, returns defaultValue if there is no entry for the biome
	 */
	@NotNull
	@SuppressWarnings("unused")
	public Integer getTreeDensity(Biome biome, int defaultValue){
		return treeDensityMap.getOrDefault(Registries.BIOMES.getKey(biome), defaultValue);
	}

	/**Specifies the number of chances for lake to spawn for the specified biome
	 */
	@SuppressWarnings("unused")
	public void addLakeDensity(Biome biome, int density){
		if (getConfigOverride() && getTreeDensity(biome) != null){
			return;
		}
		lakeDensityMap.put(Registries.BIOMES.getKey(biome), density);
	}

	/**
	 * @return Biome's lake density, returns null if there is no entry for the biome
	 */
	@Nullable
	@SuppressWarnings("unused")
	public Integer getLakeDensity(Biome biome){
		return lakeDensityMap.get(Registries.BIOMES.getKey(biome));
	}

	/**
	 * @return Biome's lake density, returns defaultValue if there is no entry for the biome
	 */
	@NotNull
	public Integer getLakeDensity(Biome biome, int defaultValue){
		return lakeDensityMap.getOrDefault(Registries.BIOMES.getKey(biome), defaultValue);
	}
}
