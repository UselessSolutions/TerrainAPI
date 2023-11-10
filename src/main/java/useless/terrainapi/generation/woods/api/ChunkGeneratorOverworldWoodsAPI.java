package useless.terrainapi.generation.woods.api;

import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.MapGenCaves;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.chunk.perlin.overworld.SurfaceGeneratorOverworld;
import net.minecraft.core.world.generate.chunk.perlin.overworld.TerrainGeneratorOverworld;
import useless.terrainapi.generation.overworld.api.ChunkDecoratorOverworldAPI;

public class ChunkGeneratorOverworldWoodsAPI extends ChunkGeneratorPerlin {
	public ChunkGeneratorOverworldWoodsAPI(World world) {
		super(world, new ChunkDecoratorOverworldAPI(world, 50), new TerrainGeneratorOverworld(world), new SurfaceGeneratorOverworld(world), new MapGenCaves(false));
	}
}
