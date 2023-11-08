package useless.terrainapi.config;

import com.google.gson.annotations.SerializedName;

public class APIConfig {
	@SerializedName(value = "Override Default Values")
	private boolean configOverride = false;
	public boolean getConfigOverride(){
		return configOverride;
	}
}
