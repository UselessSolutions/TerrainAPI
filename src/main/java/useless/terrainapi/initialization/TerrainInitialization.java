package useless.terrainapi.initialization;

import useless.terrainapi.TerrainMain;
import useless.terrainapi.api.TerrainAPI;
import useless.terrainapi.initialization.worldtypes.NetherInitialization;
import useless.terrainapi.initialization.worldtypes.OverworldInitialization;
import useless.terrainapi.initialization.worldtypes.RetroInitialization;

public class TerrainInitialization implements TerrainAPI {
	private static boolean hasInitialized = false;
	@Override
	public String getModID() {
		return TerrainMain.MOD_ID;
	}
	@Override
	public void onInitialize() {
		if (hasInitialized) {return;}
		hasInitialized = true;
		new OverworldInitialization().init();
		new NetherInitialization().init();
		new RetroInitialization().init();
	}
}

