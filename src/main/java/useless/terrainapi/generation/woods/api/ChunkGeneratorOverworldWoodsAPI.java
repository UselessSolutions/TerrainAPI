package useless.terrainapi.generation.woods.api;

import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.MapGenCaves;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.chunk.perlin.overworld.SurfaceGeneratorOverworld;
import net.minecraft.core.world.generate.chunk.perlin.overworld.TerrainGeneratorOverworld;

public class ChunkGeneratorOverworldWoodsAPI extends ChunkGeneratorPerlin {
	public ChunkGeneratorOverworldWoodsAPI(World world) {
		super(world, new ChunkDecoratorOverworldWoodsAPI(world), new TerrainGeneratorOverworld(world), new SurfaceGeneratorOverworld(world), new MapGenCaves(false));
	}
}
