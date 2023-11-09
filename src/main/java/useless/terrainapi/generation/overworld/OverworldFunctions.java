package useless.terrainapi.generation.overworld;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureDungeon;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import useless.terrainapi.generation.Parameters;
import useless.terrainapi.generation.overworld.api.ChunkDecoratorOverworldAPI;
import useless.terrainapi.util.Utilities;

import java.util.Random;

public class OverworldFunctions {
	public static OverworldConfig overworldConfig = ChunkDecoratorOverworldAPI.overworldConfig;
	public static WorldFeature getTreeFeature(Parameters parameters){
		WorldFeature treeFeature = parameters.biome.getRandomWorldGenForTrees(parameters.random);
		treeFeature.func_517_a(1.0, 1.0, 1.0);
		return treeFeature;
	}
	public static int getTreeDensity(Parameters parameters){
		ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) parameters.decorator;

		Integer treeDensity = overworldConfig.getTreeDensity(parameters.biome);

		if (decorator.treeDensityOverride != -1){
			return decorator.treeDensityOverride;
		}

		if (treeDensity != null && treeDensity == -1000){
			return 0;
		} else {
			int x = parameters.chunk.xPosition * 16;
			int z = parameters.chunk.zPosition * 16;
			double d = 0.5;

			int noiseValue = (int)((decorator.treeDensityNoise.get((double)x * d, (double)z * d) / 8.0 + parameters.random.nextDouble() * 4.0 + 4.0) / 3.0);
			int treeDensityOffset = 0;
			if (parameters.random.nextInt(10) == 0) {
				++treeDensityOffset;
			}
			if (treeDensity == null){
				return treeDensityOffset;
			}

			return treeDensity + noiseValue + treeDensityOffset;
		}
	}
	public static WorldFeature grassTypeCondition(Parameters parameters){
		Block block = Block.tallgrass;
		if (Utilities.checkForBiomeInBiomes(parameters.biome, overworldConfig.biomeRandomGrassBlock.keySet().toArray(new String[0])) && parameters.random.nextInt(3) != 0) {
			block = overworldConfig.getRandomGrassBlock(parameters.biome);
		}
		return new WorldFeatureTallGrass(block.id);
	}
	public static WorldFeature flowerTypeCondition(Parameters parameters){
		int blockId = Block.flowerYellow.id;
		if (parameters.random.nextInt(3) != 0) {
			blockId = Block.flowerRed.id;
		}
		return new WorldFeatureTallGrass(blockId);
	}
	public static int getStandardBiomesDensity(Parameters parameters){
		int chance = (int) parameters.customParameters[0];
		Biome[] biomes = (Biome[]) parameters.customParameters[1];
		if (biomes == null) {return chance;}
		if (Utilities.checkForBiomeInBiomes(parameters.biome, biomes)){
			return chance;
		}
		return 0;
	}
	public static int getStandardOreBiomesDensity(Parameters parameters){
		float oreHeightModifier = ((ChunkDecoratorOverworldAPI) parameters.decorator).oreHeightModifier;
		int chance = (int) parameters.customParameters[0];
		Biome[] biomes = (Biome[]) parameters.customParameters[1];
		if (biomes == null) {return chance;}
		if (Utilities.checkForBiomeInBiomes(parameters.biome, biomes)){
			return (int) (chance * oreHeightModifier);
		}
		return 0;
	}
	public static Void generateDungeons(Parameters parameters){
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		ChunkDecoratorOverworldAPI decoratorOverworldAPI = (ChunkDecoratorOverworldAPI) parameters.decorator;
		for (int i = 0; i < 8.0f * decoratorOverworldAPI.oreHeightModifier; i++) {
			int xPos = x + parameters.random.nextInt(16) + 8;
			int yPos = decoratorOverworldAPI.minY + parameters.random.nextInt(decoratorOverworldAPI.rangeY);
			int zPos = z + parameters.random.nextInt(16) + 8;
			if (parameters.random.nextInt(2) == 0){
				new WorldFeatureDungeon(Block.brickClay.id, Block.brickClay.id, null).generate(decoratorOverworldAPI.world, parameters.random, xPos, yPos, zPos);
			} else {
				new WorldFeatureDungeon(Block.cobbleStone.id, Block.cobbleStoneMossy.id, null).generate(decoratorOverworldAPI.world, parameters.random, xPos, yPos, zPos);
			}
		}
		return null;
	}
	public static Void generateLabyrinths(Parameters parameters){
		ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) parameters.decorator;
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		for (int i = 0; i < 1; ++i) {
			int xPos = x + parameters.random.nextInt(16) + 8;
			int zPos = z + parameters.random.nextInt(16) + 8;
			int yPos = decorator.world.getHeightValue(xPos, zPos) - (parameters.random.nextInt(2) + 2);
			if (parameters.random.nextInt(5) == 0) {
				yPos -= parameters.random.nextInt(10) + 30;
			}
			if (parameters.random.nextInt(700) != 0) continue;
			Random lRand = parameters.chunk.getChunkRandom(75644760L);
			new WorldFeatureLabyrinth().generate(decorator.world, lRand, xPos, yPos, zPos);
		}
		return null;
	}
}
