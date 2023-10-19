package useless.terrainapi;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.generate.feature.*;
import useless.terrainapi.api.TerrainAPI;
import useless.terrainapi.generation.overworld.ChunkDecoratorOverworldAPI;
import useless.terrainapi.generation.overworld.VanillaFunctions;

import java.util.HashMap;

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
		initializeDefaultValues();

		initializeOverworldStructures();
		initializeOverworldOre();
		initializeOverworldRandom();
		initializeOverworldBiome();
	}
	public static void initializeOverworldStructures() {
		ChunkDecoratorOverworldAPI.StructureFeature.addStructure(VanillaFunctions::generateDungeons, null);
		ChunkDecoratorOverworldAPI.StructureFeature.addStructure(VanillaFunctions::generateLabyrinths, null);

	}
	public static void initializeDefaultValues(){
		ChunkDecoratorOverworldAPI.OreFeatures.setOreValues(TerrainMain.MOD_ID,Block.blockClay.id, 32, 20, 1f);
		ChunkDecoratorOverworldAPI.OreFeatures.setOreValues(TerrainMain.MOD_ID,Block.dirt.id, 32, 20, 1f);
		ChunkDecoratorOverworldAPI.OreFeatures.setOreValues(TerrainMain.MOD_ID,Block.gravel.id, 32, 10, 1f);
		ChunkDecoratorOverworldAPI.OreFeatures.setOreValues(TerrainMain.MOD_ID,Block.oreCoalStone.id, 16, 20, 1f);
		ChunkDecoratorOverworldAPI.OreFeatures.setOreValues(TerrainMain.MOD_ID,Block.oreIronStone.id, 8, 20, 1/2f);
		ChunkDecoratorOverworldAPI.OreFeatures.setOreValues(TerrainMain.MOD_ID,Block.oreGoldStone.id, 8, 2, 1/4f);
		ChunkDecoratorOverworldAPI.OreFeatures.setOreValues(TerrainMain.MOD_ID,Block.oreRedstoneStone.id, 7, 8, 1/8f);
		ChunkDecoratorOverworldAPI.OreFeatures.setOreValues(TerrainMain.MOD_ID,Block.oreDiamondStone.id, 7, 1, 1/8f);
		ChunkDecoratorOverworldAPI.OreFeatures.setOreValues(TerrainMain.MOD_ID,Block.mossStone.id, 32, 1, 1/2f);
		ChunkDecoratorOverworldAPI.OreFeatures.setOreValues(TerrainMain.MOD_ID,Block.oreLapisStone.id, 6, 1, 1/8f);

		ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_FOREST, 2);
		ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_MEADOW, 2);
		ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_RAINFOREST, 10);
		ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_DESERT, 5);
		ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_SEASONAL_FOREST, 2);
		ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_TAIGA, 1);
		ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_BOREAL_FOREST, 5);
		ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_PLAINS, 10);
		ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_SWAMPLAND, 4);
		ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_SHRUBLAND, 2);
		ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_OUTBACK_GRASSY, 25);
		ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_BIRCH_FOREST, 10);

		ChunkDecoratorOverworldAPI.BiomeFeatures.flowerDensityMap.put(Biomes.OVERWORLD_SEASONAL_FOREST, 1);
		ChunkDecoratorOverworldAPI.BiomeFeatures.flowerDensityMap.put(Biomes.OVERWORLD_MEADOW, 2);
		ChunkDecoratorOverworldAPI.BiomeFeatures.flowerDensityMap.put(Biomes.OVERWORLD_BOREAL_FOREST, 2);
		ChunkDecoratorOverworldAPI.BiomeFeatures.flowerDensityMap.put(Biomes.OVERWORLD_SHRUBLAND, 1);

		ChunkDecoratorOverworldAPI.BiomeFeatures.yellowFlowerDensityMap.put(Biomes.OVERWORLD_FOREST, 2);
		ChunkDecoratorOverworldAPI.BiomeFeatures.yellowFlowerDensityMap.put(Biomes.OVERWORLD_SWAMPLAND, 2);
		ChunkDecoratorOverworldAPI.BiomeFeatures.yellowFlowerDensityMap.put(Biomes.OVERWORLD_TAIGA, 2);
		ChunkDecoratorOverworldAPI.BiomeFeatures.yellowFlowerDensityMap.put(Biomes.OVERWORLD_PLAINS, 3);
		ChunkDecoratorOverworldAPI.BiomeFeatures.yellowFlowerDensityMap.put(Biomes.OVERWORLD_OUTBACK_GRASSY, 2);
		ChunkDecoratorOverworldAPI.BiomeFeatures.yellowFlowerDensityMap.put(Biomes.OVERWORLD_OUTBACK, 2);

		ChunkDecoratorOverworldAPI.BiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_FOREST, 5);
		ChunkDecoratorOverworldAPI.BiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_BIRCH_FOREST, 4);
		ChunkDecoratorOverworldAPI.BiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_RAINFOREST, 10);
		ChunkDecoratorOverworldAPI.BiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_SEASONAL_FOREST, 2);
		ChunkDecoratorOverworldAPI.BiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_TAIGA, 5);
		ChunkDecoratorOverworldAPI.BiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_BOREAL_FOREST, 3);
		ChunkDecoratorOverworldAPI.BiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_DESERT, -1000);
		ChunkDecoratorOverworldAPI.BiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_TUNDRA, -1000);
		ChunkDecoratorOverworldAPI.BiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_PLAINS, -1000);
		ChunkDecoratorOverworldAPI.BiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_SWAMPLAND, 4);
		ChunkDecoratorOverworldAPI.BiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_OUTBACK_GRASSY, 0);
	}
	public static void initializeOverworldOre(){
		HashMap<Integer, Integer> blockNumberMap = ChunkDecoratorOverworldAPI.OreFeatures.blockNumberMap;
		HashMap<Integer, Integer> chancesMap = ChunkDecoratorOverworldAPI.OreFeatures.chancesMap;
		HashMap<Integer, Float> rangeMap = ChunkDecoratorOverworldAPI.OreFeatures.rangeMap;
		int currentBlockID = Block.blockClay.id;
		ChunkDecoratorOverworldAPI.OreFeatures.addFeature(new WorldFeatureClay(blockNumberMap.get(currentBlockID)), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
		ChunkDecoratorOverworldAPI.OreFeatures.addFeature(new WorldFeatureOre(currentBlockID = Block.dirt.id, blockNumberMap.get(currentBlockID), false), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
		ChunkDecoratorOverworldAPI.OreFeatures.addFeature(new WorldFeatureOre(currentBlockID = Block.gravel.id, blockNumberMap.get(currentBlockID), false), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
		ChunkDecoratorOverworldAPI.OreFeatures.addFeature(new WorldFeatureOre(currentBlockID = Block.oreCoalStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
		ChunkDecoratorOverworldAPI.OreFeatures.addFeature(new WorldFeatureOre(currentBlockID = Block.oreIronStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
		ChunkDecoratorOverworldAPI.OreFeatures.addFeature(new WorldFeatureOre(currentBlockID = Block.oreGoldStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
		ChunkDecoratorOverworldAPI.OreFeatures.addFeature(new WorldFeatureOre(currentBlockID = Block.oreRedstoneStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
		ChunkDecoratorOverworldAPI.OreFeatures.addFeature(new WorldFeatureOre(currentBlockID = Block.oreDiamondStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
		ChunkDecoratorOverworldAPI.OreFeatures.addFeature(new WorldFeatureOre(currentBlockID = Block.mossStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
		ChunkDecoratorOverworldAPI.OreFeatures.addFeature(new WorldFeatureOre(currentBlockID = Block.oreLapisStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
	}
	public static void initializeOverworldRandom(){
		ChunkDecoratorOverworldAPI.RandomFeatures.addFeature(new WorldFeatureFlowers(Block.flowerRed.id), 2, 1);
		ChunkDecoratorOverworldAPI.RandomFeatures.addFeature(new WorldFeatureFlowers(Block.mushroomBrown.id), 4, 1);
		ChunkDecoratorOverworldAPI.RandomFeatures.addFeature(new WorldFeatureFlowers(Block.mushroomRed.id), 8, 1);
		ChunkDecoratorOverworldAPI.RandomFeatures.addFeatureSurface(new WorldFeatureSugarCane(), 5);
		ChunkDecoratorOverworldAPI.RandomFeatures.addFeatureSurface(new WorldFeaturePumpkin(), 128);
		ChunkDecoratorOverworldAPI.RandomFeatures.addFeatureSurface(new WorldFeatureSponge(), 64);
	}
	public static void initializeOverworldBiome(){
		ChunkDecoratorOverworldAPI.BiomeFeatures.addFeatureSurface(new WorldFeatureRichScorchedDirt(10), 1, new Biome[]{Biomes.OVERWORLD_OUTBACK, Biomes.OVERWORLD_OUTBACK_GRASSY});
		ChunkDecoratorOverworldAPI.BiomeFeatures.addComplexFeature(VanillaFunctions::getTreeFeature, null, VanillaFunctions::getTreeDensity, null, -1f);
		ChunkDecoratorOverworldAPI.BiomeFeatures.addFeatureSurface(new WorldFeatureSugarCaneTall(), 1, new Biome[]{Biomes.OVERWORLD_RAINFOREST});
		ChunkDecoratorOverworldAPI.BiomeFeatures.addComplexFeature(VanillaFunctions::flowerTypeCondition, null, (Object[] x) -> ChunkDecoratorOverworldAPI.BiomeFeatures.flowerDensityMap.getOrDefault((Biome)x[0], 0), null, 1f);
		ChunkDecoratorOverworldAPI.BiomeFeatures.addComplexFeature((Object[] x) -> new WorldFeatureFlowers(Block.flowerYellow.id), null, (Object[] x) -> ChunkDecoratorOverworldAPI.BiomeFeatures.yellowFlowerDensityMap.getOrDefault((Biome)x[0], 0), null, 1);
		ChunkDecoratorOverworldAPI.BiomeFeatures.addComplexFeature(VanillaFunctions::grassTypeCondition, null, (Object[] x) -> ChunkDecoratorOverworldAPI.BiomeFeatures.grassDensityMap.getOrDefault((Biome)x[0], 0), null, 1);
		ChunkDecoratorOverworldAPI.BiomeFeatures.addFeature(new WorldFeatureSpinifexPatch(), 1, 4, new Biome[]{Biomes.OVERWORLD_OUTBACK});
		ChunkDecoratorOverworldAPI.BiomeFeatures.addFeature(new WorldFeatureDeadBush(Block.deadbush.id), 1, 2, new Biome[]{Biomes.OVERWORLD_DESERT});
		ChunkDecoratorOverworldAPI.BiomeFeatures.addFeature(new WorldFeatureCactus(), 1, 10, new Biome[]{Biomes.OVERWORLD_DESERT});
	}
}

