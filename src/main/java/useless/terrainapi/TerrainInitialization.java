package useless.terrainapi;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.generate.feature.*;
import useless.terrainapi.api.TerrainAPI;
import useless.terrainapi.config.OverworldConfig;
import useless.terrainapi.generation.Parameters;
import useless.terrainapi.generation.VanillaFunctions;
import useless.terrainapi.generation.nether.ChunkDecoratorNetherAPI;
import useless.terrainapi.generation.overworld.ChunkDecoratorOverworldAPI;

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
		OverworldConfig overworldConfig = ChunkDecoratorOverworldAPI.overworldConfig;
		overworldConfig.setOreValues(TerrainMain.MOD_ID, Block.blockClay, 32, 20, 1f);
		overworldConfig.setOreValues(TerrainMain.MOD_ID,Block.dirt, 32, 20, 1f);
		overworldConfig.setOreValues(TerrainMain.MOD_ID,Block.gravel, 32, 10, 1f);
		overworldConfig.setOreValues(TerrainMain.MOD_ID,Block.oreCoalStone, 16, 20, 1f);
		overworldConfig.setOreValues(TerrainMain.MOD_ID,Block.oreIronStone, 8, 20, 1/2f);
		overworldConfig.setOreValues(TerrainMain.MOD_ID,Block.oreGoldStone, 8, 2, 1/4f);
		overworldConfig.setOreValues(TerrainMain.MOD_ID,Block.oreRedstoneStone, 7, 8, 1/8f);
		overworldConfig.setOreValues(TerrainMain.MOD_ID,Block.oreDiamondStone, 7, 1, 1/8f);
		overworldConfig.setOreValues(TerrainMain.MOD_ID,Block.mossStone, 32, 1, 1/2f);
		overworldConfig.setOreValues(TerrainMain.MOD_ID,Block.oreLapisStone, 6, 1, 1/8f);

		overworldConfig.addGrassDensity(Biomes.OVERWORLD_FOREST, 2);
		overworldConfig.addGrassDensity(Biomes.OVERWORLD_MEADOW, 2);
		overworldConfig.addGrassDensity(Biomes.OVERWORLD_RAINFOREST, 10);
		overworldConfig.addGrassDensity(Biomes.OVERWORLD_DESERT, 5);
		overworldConfig.addGrassDensity(Biomes.OVERWORLD_SEASONAL_FOREST, 2);
		overworldConfig.addGrassDensity(Biomes.OVERWORLD_TAIGA, 1);
		overworldConfig.addGrassDensity(Biomes.OVERWORLD_BOREAL_FOREST, 5);
		overworldConfig.addGrassDensity(Biomes.OVERWORLD_PLAINS, 10);
		overworldConfig.addGrassDensity(Biomes.OVERWORLD_SWAMPLAND, 4);
		overworldConfig.addGrassDensity(Biomes.OVERWORLD_SHRUBLAND, 2);
		overworldConfig.addGrassDensity(Biomes.OVERWORLD_OUTBACK_GRASSY, 25);
		overworldConfig.addGrassDensity(Biomes.OVERWORLD_BIRCH_FOREST, 10);

		overworldConfig.addFlowerDensity(Biomes.OVERWORLD_SEASONAL_FOREST, 1);
		overworldConfig.addFlowerDensity(Biomes.OVERWORLD_MEADOW, 2);
		overworldConfig.addFlowerDensity(Biomes.OVERWORLD_BOREAL_FOREST, 2);
		overworldConfig.addFlowerDensity(Biomes.OVERWORLD_SHRUBLAND, 1);

		overworldConfig.addYellowFlowerDensity(Biomes.OVERWORLD_FOREST, 2);
		overworldConfig.addYellowFlowerDensity(Biomes.OVERWORLD_SWAMPLAND, 2);
		overworldConfig.addYellowFlowerDensity(Biomes.OVERWORLD_TAIGA, 2);
		overworldConfig.addYellowFlowerDensity(Biomes.OVERWORLD_PLAINS, 3);
		overworldConfig.addYellowFlowerDensity(Biomes.OVERWORLD_OUTBACK_GRASSY, 2);
		overworldConfig.addYellowFlowerDensity(Biomes.OVERWORLD_OUTBACK, 2);

		overworldConfig.addTreeDensity(Biomes.OVERWORLD_FOREST, 5);
		overworldConfig.addTreeDensity(Biomes.OVERWORLD_BIRCH_FOREST, 4);
		overworldConfig.addTreeDensity(Biomes.OVERWORLD_RAINFOREST, 10);
		overworldConfig.addTreeDensity(Biomes.OVERWORLD_SEASONAL_FOREST, 2);
		overworldConfig.addTreeDensity(Biomes.OVERWORLD_TAIGA, 5);
		overworldConfig.addTreeDensity(Biomes.OVERWORLD_BOREAL_FOREST, 3);
		overworldConfig.addTreeDensity(Biomes.OVERWORLD_DESERT, -1000);
		overworldConfig.addTreeDensity(Biomes.OVERWORLD_TUNDRA, -1000);
		overworldConfig.addTreeDensity(Biomes.OVERWORLD_PLAINS, -1000);
		overworldConfig.addTreeDensity(Biomes.OVERWORLD_SWAMPLAND, 4);
		overworldConfig.addTreeDensity(Biomes.OVERWORLD_OUTBACK_GRASSY, 0);

		overworldConfig.addRandomGrassBlock(Biomes.OVERWORLD_RAINFOREST, Block.tallgrassFern);
		overworldConfig.addRandomGrassBlock(Biomes.OVERWORLD_SWAMPLAND, Block.tallgrassFern);
		overworldConfig.addRandomGrassBlock(Biomes.OVERWORLD_BOREAL_FOREST, Block.tallgrassFern);
		overworldConfig.addRandomGrassBlock(Biomes.OVERWORLD_TAIGA, Block.tallgrassFern);

		ChunkDecoratorNetherAPI.netherConfig.setOreValues(TerrainMain.MOD_ID, Block.oreNethercoalNetherrack, 12, 10, 120/128f);
	}
	public static void initializeOverworldOre(){
		HashMap<String, Integer> blockNumberMap = ChunkDecoratorOverworldAPI.overworldConfig.clusterSize;
		HashMap<String, Integer> chancesMap = ChunkDecoratorOverworldAPI.overworldConfig.chancesPerChunk;
		HashMap<String, Float> rangeMap = ChunkDecoratorOverworldAPI.overworldConfig.verticalRange;
		String currentBlock;
		currentBlock = Block.blockClay.getKey();
		ChunkDecoratorOverworldAPI.oreFeatures.addFeature(new WorldFeatureClay(blockNumberMap.get(currentBlock)), chancesMap.get(currentBlock), rangeMap.get(currentBlock));
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(Block.dirt, false);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(Block.gravel, false);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(Block.oreCoalStone, true);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(Block.oreIronStone, true);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(Block.oreGoldStone, true);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(Block.oreRedstoneStone, true);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(Block.oreDiamondStone, true);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(Block.mossStone, true);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(Block.oreLapisStone, true);
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
		ChunkDecoratorOverworldAPI.biomeFeatures.addComplexFeature(VanillaFunctions::flowerTypeCondition, null, (Object[] x) -> ChunkDecoratorOverworldAPI.overworldConfig.getFlowerDensity(Parameters.getBiome(x), 0), null, 1f);
		ChunkDecoratorOverworldAPI.biomeFeatures.addComplexFeature((Object[] x) -> new WorldFeatureFlowers(Block.flowerYellow.id), null, (Object[] x) -> ChunkDecoratorOverworldAPI.overworldConfig.getYellowFlowerDensity(Parameters.getBiome(x), 0), null, 1);
		ChunkDecoratorOverworldAPI.biomeFeatures.addComplexFeature(VanillaFunctions::grassTypeCondition, null, (Object[] x) -> ChunkDecoratorOverworldAPI.overworldConfig.getGrassDensity(Parameters.getBiome(x), 0), null, 1);
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature(new WorldFeatureSpinifexPatch(), 1, 4, new Biome[]{Biomes.OVERWORLD_OUTBACK});
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature(new WorldFeatureDeadBush(Block.deadbush.id), 1, 2, new Biome[]{Biomes.OVERWORLD_DESERT});
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature(new WorldFeatureCactus(), 1, 10, new Biome[]{Biomes.OVERWORLD_DESERT});
	}
	public static void initializeNether(){
		ChunkDecoratorNetherAPI.oreFeatures.addFeature(new WorldFeatureNetherLava(Block.fluidLavaFlowing.id),  8,120/128f);
		ChunkDecoratorNetherAPI.oreFeatures.addManagedOreFeature(Block.oreNethercoalNetherrack, false);
		ChunkDecoratorNetherAPI.oreFeatures.addComplexFeature((Object[] x) -> new WorldFeatureFire(), null, VanillaFunctions::netherFireDensity, null, 120/128f);
		ChunkDecoratorNetherAPI.oreFeatures.addComplexFeature((Object[] x) -> new WorldFeatureGlowstoneA(), null, VanillaFunctions::netherFireDensity, null, 120/128f);
		ChunkDecoratorNetherAPI.oreFeatures.addFeature(new WorldFeatureGlowstoneB(), 10, 120/128f);
		ChunkDecoratorNetherAPI.randomFeatures.addFeature(new WorldFeatureLake(Block.fluidLavaStill.id), 8, 120/128f);
	}
}

