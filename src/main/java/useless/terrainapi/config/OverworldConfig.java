package useless.terrainapi.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.biome.Biome;

import java.util.HashMap;

public class OverworldConfig extends OreConfig{
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
	public void addRandomGrassBlock(Biome biome, Block block) {
		if (getConfigOverride() && getRandomGrassBlock(biome) != null){
			return;
		}
		biomeRandomGrassBlock.put(Registries.BIOMES.getKey(biome), block.getKey());
	}
	public Block getRandomGrassBlock(Biome biome){
		return Block.getBlockByName(biomeRandomGrassBlock.get(Registries.BIOMES.getKey(biome)));
	}
	public Block getRandomGrassBlock(Biome biome, Block defaultValue){
		Block returnBlock = Block.getBlockByName(biomeRandomGrassBlock.get(Registries.BIOMES.getKey(biome)));
		if (returnBlock == null){
			returnBlock = defaultValue;
		}
		return returnBlock;
	}
	public void addGrassDensity(Biome biome, int density){
		if (getConfigOverride() && getGrassDensity(biome) != null){
			return;
		}
		grassDensityMap.put(Registries.BIOMES.getKey(biome), density);
	}
	public Integer getGrassDensity(Biome biome){
		return grassDensityMap.get(Registries.BIOMES.getKey(biome));
	}
	public Integer getGrassDensity(Biome biome, int defaultValue){
		return grassDensityMap.getOrDefault(Registries.BIOMES.getKey(biome), defaultValue);
	}

	public void addFlowerDensity(Biome biome, int density){
		if (getConfigOverride() && getFlowerDensity(biome) != null){
			return;
		}
		flowerDensityMap.put(Registries.BIOMES.getKey(biome), density);
	}
	public Integer getFlowerDensity(Biome biome){
		return flowerDensityMap.get(Registries.BIOMES.getKey(biome));
	}
	public Integer getFlowerDensity(Biome biome, int defaultValue){
		return flowerDensityMap.getOrDefault(Registries.BIOMES.getKey(biome), defaultValue);
	}

	public void addYellowFlowerDensity(Biome biome, int density){
		if (getConfigOverride() && getYellowFlowerDensity(biome) != null){
			return;
		}
		yellowFlowerDensityMap.put(Registries.BIOMES.getKey(biome), density);
	}
	public Integer getYellowFlowerDensity(Biome biome){
		return yellowFlowerDensityMap.get(Registries.BIOMES.getKey(biome));
	}
	public Integer getYellowFlowerDensity(Biome biome, int defaultValue){
		return yellowFlowerDensityMap.getOrDefault(Registries.BIOMES.getKey(biome), defaultValue);
	}

	public void addTreeDensity(Biome biome, int density){
		if (getConfigOverride() && getTreeDensity(biome) != null){
			return;
		}
		treeDensityMap.put(Registries.BIOMES.getKey(biome), density);
	}
	public Integer getTreeDensity(Biome biome){
		return treeDensityMap.get(Registries.BIOMES.getKey(biome));
	}public Integer getTreeDensity(Biome biome, int defaultValue){
		return treeDensityMap.getOrDefault(Registries.BIOMES.getKey(biome), defaultValue);
	}
}
