package useless.terrainapi.generation.retro.api;

import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.MapGenCaves;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.chunk.perlin.overworld.retro.SurfaceGeneratorOverworldRetro;
import net.minecraft.core.world.generate.chunk.perlin.overworld.retro.TerrainGeneratorOverworldRetro;

public class ChunkGeneratorRetroAPI extends ChunkGeneratorPerlin {
	public ChunkGeneratorRetroAPI(World world) {
		super(world, new ChunkDecoratorRetroAPI(world), new TerrainGeneratorOverworldRetro(world), new SurfaceGeneratorOverworldRetro(world), new MapGenCaves(true));
	}
}
