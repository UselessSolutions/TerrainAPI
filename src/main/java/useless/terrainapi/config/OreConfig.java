package useless.terrainapi.config;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.core.block.Block;
import org.jetbrains.annotations.ApiStatus;
import useless.terrainapi.TerrainMain;

import java.util.HashMap;

public class OreConfig extends APIConfig {
	/**
	 * Map of Block key and ore vein size
	 */
	@SerializedName(value = "Ore Cluster Size") @Expose
	public HashMap<String, Integer> clusterSize = new HashMap<>();
	/**
	 * Map of Block key and chances to generate per chunk
	 */
	@SerializedName(value = "Chances Per Chunk") @Expose
	public HashMap<String, Integer> chancesPerChunk = new HashMap<>();
	/**
	 * Map of Block key generation range
	 */
	@SerializedName(value = "Vertical Starting Range") @Expose
	public HashMap<String, Float> verticalStartingRange = new HashMap<>();

	/**
	 * Map of Block key generation range
	 */
	@SerializedName(value = "Vertical Ending Range") @Expose
	public HashMap<String, Float> verticalEndingRange = new HashMap<>();

	/**Creates an ore entry, this can be used directly by OreFeatures#addManagedOreFeature or directly by referencing the HashMaps themselves
	 * @param block The block to be generated, the block key is used as the key for the hashmap
	 * @param clusterSize Size in blocks of an ore vein
	 * @param chances Number of chances per chunk to generate an ore patch, this values scales with world height
	 * @param range Value from [0, 1], it's the fraction from the bottom of the world to the surface that the ore can generate
	 */
	public void setOreValues(String modID, Block block, int clusterSize, int chances, float range){
		String key = block.getKey();
		if (this.clusterSize.containsKey(key) && this.getConfigOverride()){
			return;
		}
		if (this.clusterSize.getOrDefault(key, clusterSize) != clusterSize || this.chancesPerChunk.getOrDefault(key, chances) != chances || this.verticalStartingRange.getOrDefault(key, 0f) != 0 || this.verticalStartingRange.getOrDefault(key, range) != range){
			TerrainMain.LOGGER.warn(modID + String.format(" has changed block %s to generate %d blocks with %d chances and a range of %f", block.getKey(), clusterSize, chances, range));
		}
		setOreValues(block, clusterSize, chances, 0, range);
	}
	/**Creates an ore entry, this can be used directly by OreFeatures#addManagedOreFeature or directly by referencing the HashMaps themselves
	 * @param block The block to be generated, the block key is used as the key for the hashmap
	 * @param clusterSize Size in blocks of an ore vein
	 * @param chances Number of chances per chunk to generate an ore patch, this values scales with world height
	 * @param startingRange Value from [0, 1], it's the fraction from the bottom of the world to the surface that the ore can generate
	 * @param endingRange Value from [0, 1], it's the fraction from the bottom of the world to the surface that the ore can generate
	 */
	public void setOreValues(String modID, Block block, int clusterSize, int chances, float startingRange, float endingRange){
		String key = block.getKey();
		if (this.clusterSize.containsKey(key) && this.getConfigOverride()){
			return;
		}
		if (this.clusterSize.getOrDefault(key, clusterSize) != clusterSize || this.chancesPerChunk.getOrDefault(key, chances) != chances || this.verticalStartingRange.getOrDefault(key, startingRange) != startingRange || this.verticalEndingRange.getOrDefault(key, endingRange) != endingRange){
			TerrainMain.LOGGER.warn(modID + String.format(" has changed block %s to generate %d blocks with %d chances and a range of [%f, %f]", block.getKey(), clusterSize, chances, startingRange, endingRange));
		}
		setOreValues(block, clusterSize, chances, startingRange, endingRange);
	}
	/**Creates an ore entry, this can be used directly by OreFeatures#addManagedOreFeature or directly by referencing the HashMaps themselves
	 * @param block The block to be generated, the block key is used as the key for the hashmap
	 * @param clusterSize Size in blocks of an ore vein
	 * @param chances Number of chances per chunk to generate an ore patch, this values scales with world height
	 * @param startingRange Value from [0, 1], it's the fraction from the bottom of the world to the surface that the ore can generate
	 * @param endingRange Value from [0, 1], it's the fraction from the bottom of the world to the surface that the ore can generate
	 */
	@ApiStatus.Internal
	protected void setOreValues(Block block, int clusterSize, int chances, float startingRange, float endingRange){
		this.clusterSize.put(block.getKey(), clusterSize);
		this.chancesPerChunk.put(block.getKey(), chances);
		this.verticalStartingRange.put(block.getKey(), startingRange);
		this.verticalEndingRange.put(block.getKey(), endingRange);
	}
}
