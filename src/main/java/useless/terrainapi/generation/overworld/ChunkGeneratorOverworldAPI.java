package useless.terrainapi.generation.overworld;

import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.MapGenCaves;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.chunk.perlin.overworld.SurfaceGeneratorOverworld;
import net.minecraft.core.world.generate.chunk.perlin.overworld.TerrainGeneratorOverworld;

public class ChunkGeneratorOverworldAPI extends ChunkGeneratorPerlin {
	public ChunkGeneratorOverworldAPI(World world) {
		super(world, new ChunkDecoratorOverworldAPI(world), new TerrainGeneratorOverworld(world), new SurfaceGeneratorOverworld(world), new MapGenCaves(false));
	}
}
