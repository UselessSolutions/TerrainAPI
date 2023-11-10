package useless.terrainapi.generation.floating.api;

import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.MapGenCaves;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.chunk.perlin.overworld.SurfaceGeneratorOverworld;
import net.minecraft.core.world.generate.chunk.perlin.paradise.TerrainGeneratorParadise;
import useless.terrainapi.generation.overworld.api.ChunkDecoratorOverworldAPI;

public class ChunkGeneratorFloatingAPI extends ChunkGeneratorPerlin {
	public ChunkGeneratorFloatingAPI(World world) {
		super(world, new ChunkDecoratorOverworldAPI(world), new TerrainGeneratorParadise(world), new SurfaceGeneratorOverworld(world), new MapGenCaves(false));
	}
}
