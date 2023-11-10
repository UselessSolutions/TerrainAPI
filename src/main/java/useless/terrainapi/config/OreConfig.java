package useless.terrainapi.config;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.core.block.Block;
import org.jetbrains.annotations.ApiStatus;
import useless.terrainapi.TerrainMain;

import java.util.HashMap;

public class OreConfig extends APIConfig {
	@SerializedName(value = "Ore Cluster Size") @Expose
	public HashMap<String, Integer> clusterSize = new HashMap<>();
	@SerializedName(value = "Chances Per Chunk") @Expose
	public HashMap<String, Integer> chancesPerChunk = new HashMap<>();
	@SerializedName(value = "Vertical Range") @Expose
	public HashMap<String, Float> verticalRange = new HashMap<>();

	/**Creates an ore entry, this can be used directly by OreFeatures#addManagedOreFeature or directly by referencing the HashMaps themselves
	 * @param block The block to be generated, the block key is used as the key for the hashmap
	 * @param clusterSize Size in blocks of an ore vein
	 * @param chances Number of chances per chunk to generate an ore patch, this values scales with world height
	 * @param range Value from [0, 1], it's the fraction from the bottom of the world to the surface that the ore can generate
	 */
	public void setOreValues(String modID, Block block, int clusterSize, int chances, float range){
		if (this.clusterSize.get(block.getKey()) != null){
			TerrainMain.LOGGER.warn(modID + String.format(" has changed block %s to generate %d blocks with %d chances and a range of %f", block.getKey(), clusterSize, chances, range));
		}
		setOreValues(block, clusterSize, chances, range);
	}
	/**Creates an ore entry, this can be used directly by OreFeatures#addManagedOreFeature or directly by referencing the HashMaps themselves
	 * @param block The block to be generated, the block key is used as the key for the hashmap
	 * @param clusterSize Size in blocks of an ore vein
	 * @param chances Number of chances per chunk to generate an ore patch, this values scales with world height
	 * @param range Value from [0, 1], it's the fraction from the bottom of the world to the surface that the ore can generate
	 */
	@ApiStatus.Internal
	protected void setOreValues(Block block, int clusterSize, int chances, float range){
		if (this.clusterSize.containsKey(block.getKey()) && this.getConfigOverride()){
			return;
		}
		this.clusterSize.put(block.getKey(), clusterSize);
		this.chancesPerChunk.put(block.getKey(), chances);
		this.verticalRange.put(block.getKey(), range);
	}
}
