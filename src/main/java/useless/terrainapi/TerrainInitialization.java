package useless.terrainapi;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.generate.feature.*;
import useless.terrainapi.api.TerrainAPI;
import useless.terrainapi.generation.Parameters;
import useless.terrainapi.generation.nether.ChunkDecoratorNetherAPI;
import useless.terrainapi.generation.overworld.ChunkDecoratorOverworldAPI;
import useless.terrainapi.generation.VanillaFunctions;
import useless.terrainapi.generation.overworld.OverworldBiomeFeatures;

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

		initializeNether();
	}
	public static void initializeOverworldStructures() {
		ChunkDecoratorOverworldAPI.structureFeatures.addStructure(VanillaFunctions::generateDungeons, null);
		ChunkDecoratorOverworldAPI.structureFeatures.addStructure(VanillaFunctions::generateLabyrinths, null);

	}
	public static void initializeDefaultValues(){
		ChunkDecoratorOverworldAPI.oreFeatures.setOreValues(TerrainMain.MOD_ID, Block.blockClay, 32, 20, 1f);
		ChunkDecoratorOverworldAPI.oreFeatures.setOreValues(TerrainMain.MOD_ID,Block.dirt, 32, 20, 1f);
		ChunkDecoratorOverworldAPI.oreFeatures.setOreValues(TerrainMain.MOD_ID,Block.gravel, 32, 10, 1f);
		ChunkDecoratorOverworldAPI.oreFeatures.setOreValues(TerrainMain.MOD_ID,Block.oreCoalStone, 16, 20, 1f);
		ChunkDecoratorOverworldAPI.oreFeatures.setOreValues(TerrainMain.MOD_ID,Block.oreIronStone, 8, 20, 1/2f);
		ChunkDecoratorOverworldAPI.oreFeatures.setOreValues(TerrainMain.MOD_ID,Block.oreGoldStone, 8, 2, 1/4f);
		ChunkDecoratorOverworldAPI.oreFeatures.setOreValues(TerrainMain.MOD_ID,Block.oreRedstoneStone, 7, 8, 1/8f);
		ChunkDecoratorOverworldAPI.oreFeatures.setOreValues(TerrainMain.MOD_ID,Block.oreDiamondStone, 7, 1, 1/8f);
		ChunkDecoratorOverworldAPI.oreFeatures.setOreValues(TerrainMain.MOD_ID,Block.mossStone, 32, 1, 1/2f);
		ChunkDecoratorOverworldAPI.oreFeatures.setOreValues(TerrainMain.MOD_ID,Block.oreLapisStone, 6, 1, 1/8f);

		OverworldBiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_FOREST, 2);
		OverworldBiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_MEADOW, 2);
		OverworldBiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_RAINFOREST, 10);
		OverworldBiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_DESERT, 5);
		OverworldBiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_SEASONAL_FOREST, 2);
		OverworldBiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_TAIGA, 1);
		OverworldBiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_BOREAL_FOREST, 5);
		OverworldBiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_PLAINS, 10);
		OverworldBiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_SWAMPLAND, 4);
		OverworldBiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_SHRUBLAND, 2);
		OverworldBiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_OUTBACK_GRASSY, 25);
		OverworldBiomeFeatures.grassDensityMap.put(Biomes.OVERWORLD_BIRCH_FOREST, 10);

		OverworldBiomeFeatures.flowerDensityMap.put(Biomes.OVERWORLD_SEASONAL_FOREST, 1);
		OverworldBiomeFeatures.flowerDensityMap.put(Biomes.OVERWORLD_MEADOW, 2);
		OverworldBiomeFeatures.flowerDensityMap.put(Biomes.OVERWORLD_BOREAL_FOREST, 2);
		OverworldBiomeFeatures.flowerDensityMap.put(Biomes.OVERWORLD_SHRUBLAND, 1);

		OverworldBiomeFeatures.yellowFlowerDensityMap.put(Biomes.OVERWORLD_FOREST, 2);
		OverworldBiomeFeatures.yellowFlowerDensityMap.put(Biomes.OVERWORLD_SWAMPLAND, 2);
		OverworldBiomeFeatures.yellowFlowerDensityMap.put(Biomes.OVERWORLD_TAIGA, 2);
		OverworldBiomeFeatures.yellowFlowerDensityMap.put(Biomes.OVERWORLD_PLAINS, 3);
		OverworldBiomeFeatures.yellowFlowerDensityMap.put(Biomes.OVERWORLD_OUTBACK_GRASSY, 2);
		OverworldBiomeFeatures.yellowFlowerDensityMap.put(Biomes.OVERWORLD_OUTBACK, 2);

		OverworldBiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_FOREST, 5);
		OverworldBiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_BIRCH_FOREST, 4);
		OverworldBiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_RAINFOREST, 10);
		OverworldBiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_SEASONAL_FOREST, 2);
		OverworldBiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_TAIGA, 5);
		OverworldBiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_BOREAL_FOREST, 3);
		OverworldBiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_DESERT, -1000);
		OverworldBiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_TUNDRA, -1000);
		OverworldBiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_PLAINS, -1000);
		OverworldBiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_SWAMPLAND, 4);
		OverworldBiomeFeatures.treeDensityMap.put(Biomes.OVERWORLD_OUTBACK_GRASSY, 0);

		VanillaFunctions.biomeRandomGrassType.put(Biomes.OVERWORLD_RAINFOREST, Block.tallgrassFern.id);
		VanillaFunctions.biomeRandomGrassType.put(Biomes.OVERWORLD_SWAMPLAND, Block.tallgrassFern.id);
		VanillaFunctions.biomeRandomGrassType.put(Biomes.OVERWORLD_BOREAL_FOREST, Block.tallgrassFern.id);
		VanillaFunctions.biomeRandomGrassType.put(Biomes.OVERWORLD_TAIGA, Block.tallgrassFern.id);

		ChunkDecoratorNetherAPI.oreFeatures.setOreValues(TerrainMain.MOD_ID, Block.oreNethercoalNetherrack, 12, 10, 120/128f);
	}
	public static void initializeOverworldOre(){
		HashMap<String, Integer> blockNumberMap = ChunkDecoratorOverworldAPI.overworldConfig.clusterSize;
		HashMap<String, Integer> chancesMap = ChunkDecoratorOverworldAPI.overworldConfig.chancesPerChunk;
		HashMap<String, Float> rangeMap = ChunkDecoratorOverworldAPI.overworldConfig.verticalRange;
		String currentBlock;
		currentBlock = Block.blockClay.getKey(); 		ChunkDecoratorOverworldAPI.oreFeatures.addFeature(new WorldFeatureClay(blockNumberMap.get(currentBlock)), chancesMap.get(currentBlock), rangeMap.get(currentBlock));
		currentBlock = Block.dirt.getKey(); 			ChunkDecoratorOverworldAPI.oreFeatures.addFeature(new WorldFeatureOre(Block.getBlockByName(currentBlock).id, blockNumberMap.get(currentBlock), false), chancesMap.get(currentBlock), rangeMap.get(currentBlock));
		currentBlock = Block.gravel.getKey(); 			ChunkDecoratorOverworldAPI.oreFeatures.addFeature(new WorldFeatureOre(Block.getBlockByName(currentBlock).id, blockNumberMap.get(currentBlock), false), chancesMap.get(currentBlock), rangeMap.get(currentBlock));
		currentBlock = Block.oreCoalStone.getKey(); 	ChunkDecoratorOverworldAPI.oreFeatures.addFeature(new WorldFeatureOre(Block.getBlockByName(currentBlock).id, blockNumberMap.get(currentBlock), true), chancesMap.get(currentBlock), rangeMap.get(currentBlock));
		currentBlock = Block.oreIronStone.getKey(); 	ChunkDecoratorOverworldAPI.oreFeatures.addFeature(new WorldFeatureOre(Block.getBlockByName(currentBlock).id, blockNumberMap.get(currentBlock), true), chancesMap.get(currentBlock), rangeMap.get(currentBlock));
		currentBlock = Block.oreGoldStone.getKey(); 	ChunkDecoratorOverworldAPI.oreFeatures.addFeature(new WorldFeatureOre(Block.getBlockByName(currentBlock).id, blockNumberMap.get(currentBlock), true), chancesMap.get(currentBlock), rangeMap.get(currentBlock));
		currentBlock = Block.oreRedstoneStone.getKey(); ChunkDecoratorOverworldAPI.oreFeatures.addFeature(new WorldFeatureOre(Block.getBlockByName(currentBlock).id, blockNumberMap.get(currentBlock), true), chancesMap.get(currentBlock), rangeMap.get(currentBlock));
		currentBlock = Block.oreDiamondStone.getKey();	ChunkDecoratorOverworldAPI.oreFeatures.addFeature(new WorldFeatureOre(Block.getBlockByName(currentBlock).id, blockNumberMap.get(currentBlock), true), chancesMap.get(currentBlock), rangeMap.get(currentBlock));
		currentBlock = Block.mossStone.getKey(); 		ChunkDecoratorOverworldAPI.oreFeatures.addFeature(new WorldFeatureOre(Block.getBlockByName(currentBlock).id, blockNumberMap.get(currentBlock), true), chancesMap.get(currentBlock), rangeMap.get(currentBlock));
		currentBlock = Block.oreLapisStone.getKey();	ChunkDecoratorOverworldAPI.oreFeatures.addFeature(new WorldFeatureOre(Block.getBlockByName(currentBlock).id, blockNumberMap.get(currentBlock), true), chancesMap.get(currentBlock), rangeMap.get(currentBlock));
	}
	public static void initializeOverworldRandom(){
		ChunkDecoratorOverworldAPI.randomFeatures.addFeature(new WorldFeatureFlowers(Block.flowerRed.id), 2, 1);
		ChunkDecoratorOverworldAPI.randomFeatures.addFeature(new WorldFeatureFlowers(Block.mushroomBrown.id), 4, 1);
		ChunkDecoratorOverworldAPI.randomFeatures.addFeature(new WorldFeatureFlowers(Block.mushroomRed.id), 8, 1);
		ChunkDecoratorOverworldAPI.randomFeatures.addFeatureSurface(new WorldFeatureSugarCane(), 5);
		ChunkDecoratorOverworldAPI.randomFeatures.addFeatureSurface(new WorldFeaturePumpkin(), 128);
		ChunkDecoratorOverworldAPI.randomFeatures.addFeatureSurface(new WorldFeatureSponge(), 64);
	}
	public static void initializeOverworldBiome(){
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeatureSurface(new WorldFeatureRichScorchedDirt(10), 1, new Biome[]{Biomes.OVERWORLD_OUTBACK, Biomes.OVERWORLD_OUTBACK_GRASSY});
		ChunkDecoratorOverworldAPI.biomeFeatures.addComplexFeature(VanillaFunctions::getTreeFeature, null, VanillaFunctions::getTreeDensity, null, -1f);
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeatureSurface(new WorldFeatureSugarCaneTall(), 1, new Biome[]{Biomes.OVERWORLD_RAINFOREST});
		ChunkDecoratorOverworldAPI.biomeFeatures.addComplexFeature(VanillaFunctions::flowerTypeCondition, null, (Object[] x) -> OverworldBiomeFeatures.flowerDensityMap.getOrDefault(Parameters.getBiome(x), 0), null, 1f);
		ChunkDecoratorOverworldAPI.biomeFeatures.addComplexFeature((Object[] x) -> new WorldFeatureFlowers(Block.flowerYellow.id), null, (Object[] x) -> OverworldBiomeFeatures.yellowFlowerDensityMap.getOrDefault(Parameters.getBiome(x), 0), null, 1);
		ChunkDecoratorOverworldAPI.biomeFeatures.addComplexFeature(VanillaFunctions::grassTypeCondition, null, (Object[] x) -> OverworldBiomeFeatures.grassDensityMap.getOrDefault(Parameters.getBiome(x), 0), null, 1);
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature(new WorldFeatureSpinifexPatch(), 1, 4, new Biome[]{Biomes.OVERWORLD_OUTBACK});
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature(new WorldFeatureDeadBush(Block.deadbush.id), 1, 2, new Biome[]{Biomes.OVERWORLD_DESERT});
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature(new WorldFeatureCactus(), 1, 10, new Biome[]{Biomes.OVERWORLD_DESERT});
	}
	public static void initializeNether(){
		HashMap<String, Integer> blockNumberMap = ChunkDecoratorNetherAPI.netherConfig.clusterSize;
		HashMap<String, Integer> chancesMap = ChunkDecoratorNetherAPI.netherConfig.chancesPerChunk;
		HashMap<String, Float> rangeMap = ChunkDecoratorNetherAPI.netherConfig.verticalRange;
		String currentBlock;
		ChunkDecoratorNetherAPI.oreFeatures.addFeature(new WorldFeatureNetherLava(Block.fluidLavaFlowing.id),  8,120/128f);
		currentBlock = Block.oreNethercoalNetherrack.getKey(); ChunkDecoratorNetherAPI.oreFeatures.addFeature(new WorldFeatureOre(Block.getBlockByName(currentBlock).id, blockNumberMap.get(currentBlock), false), chancesMap.get(currentBlock), rangeMap.get(currentBlock));
		ChunkDecoratorNetherAPI.oreFeatures.addComplexFeature((Object[] x) -> new WorldFeatureFire(), null, VanillaFunctions::netherFireDensity, null, 120/128f);
		ChunkDecoratorNetherAPI.oreFeatures.addComplexFeature((Object[] x) -> new WorldFeatureGlowstoneA(), null, VanillaFunctions::netherFireDensity, null, 120/128f);
		ChunkDecoratorNetherAPI.oreFeatures.addFeature(new WorldFeatureGlowstoneB(), 10, 120/128f);
		ChunkDecoratorNetherAPI.randomFeatures.addFeature(new WorldFeatureLake(Block.fluidLavaStill.id), 8, 120/128f);
	}
}

