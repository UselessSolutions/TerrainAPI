package useless.terrainapi;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.generate.feature.*;
import useless.terrainapi.api.TerrainAPI;
import useless.terrainapi.generation.Parameters;
import useless.terrainapi.generation.nether.NetherFunctions;
import useless.terrainapi.generation.nether.api.ChunkDecoratorNetherAPI;
import useless.terrainapi.generation.overworld.OverworldConfig;
import useless.terrainapi.generation.overworld.OverworldFunctions;
import useless.terrainapi.generation.overworld.api.ChunkDecoratorOverworldAPI;

public class TerrainInitialization implements TerrainAPI {
	private static boolean hasInitialized = false;
	private static final OverworldConfig overworldConfig = ChunkDecoratorOverworldAPI.overworldConfig;
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
	public static void initializeDefaultValues(){
		overworldConfig.setOreValues(TerrainMain.MOD_ID, Block.blockClay, 32, 20, 1f);

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

		overworldConfig.addLakeDensity(Biomes.OVERWORLD_SWAMPLAND, 2);
		overworldConfig.addLakeDensity(Biomes.OVERWORLD_DESERT, 0);
	}
	public static void initializeOverworldStructures() {
		ChunkDecoratorOverworldAPI.structureFeatures.addFeature(OverworldFunctions::generateSwamp, null);
		ChunkDecoratorOverworldAPI.structureFeatures.addFeature(OverworldFunctions::generateLakeFeature, null);
		ChunkDecoratorOverworldAPI.structureFeatures.addFeature(OverworldFunctions::generateLavaLakeFeature, null);
		ChunkDecoratorOverworldAPI.structureFeatures.addFeature(OverworldFunctions::generateDungeons, null);
		ChunkDecoratorOverworldAPI.structureFeatures.addFeature(OverworldFunctions::generateLabyrinths, null);
	}
	public static void initializeOverworldOre(){
		String currentBlock = Block.blockClay.getKey();
		ChunkDecoratorOverworldAPI.oreFeatures.addFeature(new WorldFeatureClay(overworldConfig.clusterSize.get(currentBlock)), overworldConfig.chancesPerChunk.get(currentBlock), overworldConfig.verticalRange.get(currentBlock));
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID,Block.dirt, 32, 20, 1f, false);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID,Block.gravel, 32, 10, 1f, false);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID,Block.oreCoalStone, 16, 20, 1f, true);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID,Block.oreIronStone, 8, 20, 1/2f, true);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID,Block.oreGoldStone, 8, 2, 1/4f, true);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID,Block.oreRedstoneStone, 7, 8, 1/8f, true);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID,Block.oreDiamondStone, 7, 1, 1/8f, true);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID,Block.mossStone, 32, 1, 1/2f, true);
		ChunkDecoratorOverworldAPI.oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID,Block.oreLapisStone, 6, 1, 1/8f, true);
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
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature(OverworldFunctions::getTreeFeature, null, OverworldFunctions::getTreeDensity, null, -1f);
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeatureSurface(new WorldFeatureSugarCaneTall(), 1, new Biome[]{Biomes.OVERWORLD_RAINFOREST});
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature(OverworldFunctions::flowerTypeCondition, null, (Parameters x) -> ChunkDecoratorOverworldAPI.overworldConfig.getFlowerDensity(x.biome, 0), null, 1f);
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature((Parameters x) -> new WorldFeatureFlowers(Block.flowerYellow.id), null, (Parameters x) -> ChunkDecoratorOverworldAPI.overworldConfig.getYellowFlowerDensity(x.biome, 0), null, 1);
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature(OverworldFunctions::grassTypeCondition, null, (Parameters x) -> ChunkDecoratorOverworldAPI.overworldConfig.getGrassDensity(x.biome, 0), null, 1);
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature(new WorldFeatureSpinifexPatch(), 1, 4, new Biome[]{Biomes.OVERWORLD_OUTBACK});
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature(new WorldFeatureDeadBush(Block.deadbush.id), 1, 2, new Biome[]{Biomes.OVERWORLD_DESERT});
		ChunkDecoratorOverworldAPI.biomeFeatures.addFeature(new WorldFeatureCactus(), 1, 10, new Biome[]{Biomes.OVERWORLD_DESERT});
	}
	public static void initializeNether(){
		ChunkDecoratorNetherAPI.oreFeatures.addFeature(new WorldFeatureNetherLava(Block.fluidLavaFlowing.id),  8,120/128f);
		ChunkDecoratorNetherAPI.oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreNethercoalNetherrack, 12, 10, 120/128f, false);
		ChunkDecoratorNetherAPI.oreFeatures.addFeature((Parameters x) -> new WorldFeatureFire(), null, NetherFunctions::netherFireDensity, null, 120/128f);
		ChunkDecoratorNetherAPI.oreFeatures.addFeature((Parameters x) -> new WorldFeatureGlowstoneA(), null, NetherFunctions::netherFireDensity, null, 120/128f);
		ChunkDecoratorNetherAPI.oreFeatures.addFeature(new WorldFeatureGlowstoneB(), 10, 120/128f);
		ChunkDecoratorNetherAPI.randomFeatures.addFeature(new WorldFeatureLake(Block.fluidLavaStill.id), 8, 120/128f);
	}
}

