package useless.terrainapi.generation.hell;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import useless.terrainapi.config.OreConfig;

public class HellConfig extends OreConfig {
	@SerializedName("Inverse Leaves Probability") @Expose
	public int invLeavesProbability = 1234;
}
