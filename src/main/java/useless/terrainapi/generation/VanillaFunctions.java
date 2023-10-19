package useless.terrainapi.generation;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureDungeon;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import useless.terrainapi.generation.overworld.ChunkDecoratorOverworldAPI;
import useless.terrainapi.generation.overworld.OverworldBiomeFeatures;

import java.util.Random;

public class VanillaFunctions {
	public static WorldFeature getTreeFeature(Object[] parameters){
		Biome biome = Parameters.getBiome(parameters);
		Random random = Parameters.getRandom(parameters);
		WorldFeature treeFeature = biome.getRandomWorldGenForTrees(random);
		treeFeature.func_517_a(1.0, 1.0, 1.0);
		return treeFeature;
	}
	public static int getTreeDensity(Object[] parameters){
		Biome biome = Parameters.getBiome(parameters);
		ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) Parameters.getDecorator(parameters);

		Integer treeDensity = OverworldBiomeFeatures.treeDensityMap.get(biome);

		if (decorator.treeDensityOverride != -1){
			return decorator.treeDensityOverride;
		}

		if (treeDensity != null && treeDensity == -1000){
			return 0;
		} else {
			Random random = Parameters.getRandom(parameters);
			Chunk chunk = Parameters.getChunk(parameters);

			int x = chunk.xPosition * 16;
			int z = chunk.zPosition * 16;
			double d = 0.5;

			int noiseValue = (int)((decorator.treeDensityNoise.get((double)x * d, (double)z * d) / 8.0 + random.nextDouble() * 4.0 + 4.0) / 3.0);
			int treeDensityOffset = 0;
			if (random.nextInt(10) == 0) {
				++treeDensityOffset;
			}
			if (treeDensity == null){
				return treeDensityOffset;
			}

			return treeDensity + noiseValue + treeDensityOffset;
		}
	}
	public static WorldFeature grassTypeCondition(Object[] parameters){
		Biome biome = Parameters.getBiome(parameters);
		Random random = Parameters.getRandom(parameters);

		int blockId = Block.tallgrass.id;
		if ((biome == Biomes.OVERWORLD_RAINFOREST || biome == Biomes.OVERWORLD_SWAMPLAND || biome == Biomes.OVERWORLD_BOREAL_FOREST || biome == Biomes.OVERWORLD_TAIGA) && random.nextInt(3) != 0) {
			blockId = Block.tallgrassFern.id;
		}
		return new WorldFeatureTallGrass(blockId);
	}
	public static WorldFeature flowerTypeCondition(Object[] parameters){
		Random random = Parameters.getRandom(parameters);
		int blockId = Block.flowerYellow.id;
		if (random.nextInt(3) != 0) {
			blockId = Block.flowerRed.id;
		}
		return new WorldFeatureTallGrass(blockId);
	}
	public static int getStandardBiomesDensity(Object[] parameters){
		Biome biome = Parameters.getBiome(parameters);
		int chance = (int) Parameters.getCustomParameter(parameters, 1);
		Biome[] biomes = (Biome[]) Parameters.getCustomParameter(parameters, 2);
		if (biomes == null) {return chance;}
		if (checkForBiomeInBiomes(biome, biomes)){
			return chance;
		}
		return 0;
	}
	public static int getStandardOreBiomesDensity(Object[] parameters){
		Biome biome = Parameters.getBiome(parameters);
		float oreHeightModifier = ((ChunkDecoratorOverworldAPI) Parameters.getDecorator(parameters)).oreHeightModifier;
		int chance = (int) Parameters.getCustomParameter(parameters, 1);
		Biome[] biomes = (Biome[]) Parameters.getCustomParameter(parameters, 2);
		if (biomes == null) {return chance;}
		if (checkForBiomeInBiomes(biome, biomes)){
			return (int) (chance * oreHeightModifier);
		}
		return 0;
	}
	public static Boolean generateDungeons(Object[] parameters){
		Random random = Parameters.getRandom(parameters);
		Chunk chunk = Parameters.getChunk(parameters);
		ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) Parameters.getDecorator(parameters);
		int x = chunk.xPosition * 16;
		int z = chunk.zPosition * 16;
		for (int i = 0; i < 8.0f * decorator.oreHeightModifier; i++) {
			int xPos = x + random.nextInt(16) + 8;
			int yPos = decorator.minY + random.nextInt(decorator.rangeY);
			int zPos = z + random.nextInt(16) + 8;
			if (random.nextInt(2) == 0){
				new WorldFeatureDungeon(Block.brickClay.id, Block.brickClay.id, null).generate(decorator.world, random, xPos, yPos, zPos);
			} else {
				new WorldFeatureDungeon(Block.cobbleStone.id, Block.cobbleStoneMossy.id, null).generate(decorator.world, random, xPos, yPos, zPos);
			}
		}
		return true;
	}
	public static Boolean generateLabyrinths(Object[] parameters){
		Random random = Parameters.getRandom(parameters);
		Chunk chunk = Parameters.getChunk(parameters);
		ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) Parameters.getDecorator(parameters);
		int x = chunk.xPosition * 16;
		int z = chunk.zPosition * 16;
		for (int i = 0; i < 1; ++i) {
			int xPos = x + random.nextInt(16) + 8;
			int zPos = z + random.nextInt(16) + 8;
			int yPos = decorator.world.getHeightValue(xPos, zPos) - (random.nextInt(2) + 2);
			if (random.nextInt(5) == 0) {
				yPos -= random.nextInt(10) + 30;
			}
			if (random.nextInt(700) != 0) continue;
			Random lRand = chunk.getChunkRandom(75644760L);
			new WorldFeatureLabyrinth().generate(decorator.world, lRand, xPos, yPos, zPos);
		}
		return true;
	}
	public static int netherFireDensity(Object[] parameters){
		Random random = Parameters.getRandom(parameters);
		return random.nextInt(random.nextInt(10) + 1);
	}
	public static boolean checkForBiomeInBiomes(Biome biome, Biome[] biomes){
		for (Biome checkBiome: biomes) {
			if (biome.equals(checkBiome)){
				return true;
			}
		}
		return false;
	}
}
