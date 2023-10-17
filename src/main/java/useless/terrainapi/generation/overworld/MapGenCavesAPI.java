package useless.terrainapi.generation.overworld;

import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.MapGenBase;

public class MapGenCavesAPI extends MapGenBase {
	private boolean isAlphaWorldType;

	public MapGenCavesAPI(boolean isAlphaWorldType) {
		this.isAlphaWorldType = isAlphaWorldType;
	}
	@Override
	protected void doGeneration(World world, int chunkX, int chunkZ, int baseChunkX, int baseChunkZ, short[] data) {

	}
}
