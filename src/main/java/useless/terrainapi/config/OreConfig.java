package useless.terrainapi.config;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class OreConfig extends APIConfig {
	@SerializedName(value = "Ore Cluster Size")
	public HashMap<String, Integer> clusterSize = new HashMap<>();
	@SerializedName(value = "Chances Per Chunk")
	public HashMap<String, Integer> chancesPerChunk = new HashMap<>();
	@SerializedName(value = "Vertical Range")
	public HashMap<String, Float> verticalRange = new HashMap<>();
}
