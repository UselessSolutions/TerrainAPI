package useless.terrainapi.api;

public interface TerrainAPI {
	/**
	 * @return Mod ID
	 */
	String getModID();

	/**
	 * This method is run by TerrainAPI at startup
	 */
	void onInitialize();
}
