package useless.terrainapi.generation.hell.api;

import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.MapGenCaves;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.chunk.perlin.overworld.SurfaceGeneratorOverworld;
import net.minecraft.core.world.generate.chunk.perlin.overworld.TerrainGeneratorOverworld;

public class ChunkGeneratorOverworldHellAPI extends ChunkGeneratorPerlin {
	public ChunkGeneratorOverworldHellAPI(World world) {
		super(world, new ChunkDecoratorOverworldHellAPI(world), new TerrainGeneratorOverworld(world), new SurfaceGeneratorOverworld(world), new MapGenCaves(false));
	}
}
