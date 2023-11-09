package useless.terrainapi.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIConfig {
	@SerializedName(value = "Override Default Values") @Expose
	private boolean configOverride = false;
	public boolean getConfigOverride(){
		return configOverride;
	}
}
