package useless.terrainapi.initialization.worldtypes;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.generate.feature.WorldFeatureClay;
import net.minecraft.core.world.generate.feature.WorldFeatureDeadBush;
import useless.terrainapi.TerrainMain;
import useless.terrainapi.generation.StructureFeatures;
import useless.terrainapi.generation.hell.HellConfig;
import useless.terrainapi.generation.hell.HellFunctions;
import useless.terrainapi.generation.hell.api.ChunkDecoratorOverworldHellAPI;
import useless.terrainapi.generation.overworld.OverworldBiomeFeatures;
import useless.terrainapi.generation.overworld.OverworldFunctions;
import useless.terrainapi.generation.overworld.OverworldOreFeatures;
import useless.terrainapi.generation.overworld.OverworldRandomFeatures;
import useless.terrainapi.initialization.BaseInitialization;

public class HellInitialization extends BaseInitialization {
	private static final HellConfig hellConfig = ChunkDecoratorOverworldHellAPI.hellConfig;
	public static final StructureFeatures structureFeatures = ChunkDecoratorOverworldHellAPI.structureFeatures;
	public static final OverworldOreFeatures oreFeatures = ChunkDecoratorOverworldHellAPI.oreFeatures;
	public static final OverworldRandomFeatures randomFeatures = ChunkDecoratorOverworldHellAPI.randomFeatures;
	public static final OverworldBiomeFeatures biomeFeatures = ChunkDecoratorOverworldHellAPI.biomeFeatures;
	@Override
	protected void initValues() {
		hellConfig.setOreValues(TerrainMain.MOD_ID, Block.blockClay, 32, 20, 1);
	}

	@Override
	protected void initStructure() {
		structureFeatures.addFeature(HellFunctions::generateLavaLakeFeature, null);
		structureFeatures.addFeature(HellFunctions::generateObsidianLakeFeature, null);
		structureFeatures.addFeature(HellFunctions::generateRandomFluid, new Object[]{50, Block.fluidWaterFlowing.id});
		structureFeatures.addFeature(OverworldFunctions::generateDungeons, null);
		structureFeatures.addFeature(HellFunctions::generateLabyrinths, null);
		structureFeatures.addFeature(OverworldFunctions::generateRandomFluid, new Object[]{5, Block.fluidWaterFlowing.id});
		structureFeatures.addFeature(HellFunctions::generateRandomFluid, new Object[]{20, Block.fluidLavaFlowing.id});
	}

	@Override
	protected void initOre() {
		String blockKey = Block.blockClay.getKey();
		oreFeatures.addFeature(
			(x) -> new WorldFeatureClay(hellConfig.clusterSize.get(blockKey)), null,
			OverworldFunctions::getStandardOreBiomesDensity, new Object[]{hellConfig.chancesPerChunk.get(blockKey), null},
			hellConfig.verticalStartingRange.get(blockKey), hellConfig.verticalEndingRange.get(blockKey));
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.dirt, 32, 20, 1, false);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.gravel, 32, 10, 1, false);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreCoalStone, 16, 20, 1, true);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreIronStone, 8, 20, 1f/2, true);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreGoldStone, 8, 2, 1f/4, true);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreRedstoneStone, 7, 8, 1f/8, true);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreDiamondStone, 7, 1, 1f/8,true);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreLapisStone, 6, 1, 1f/4, true);
	}

	@Override
	protected void initRandom() {

	}

	@Override
	protected void initBiome() {
		biomeFeatures.addFeature(HellFunctions::getTreeFeature, null, HellFunctions::getTreeDensity, null, -1f);
		biomeFeatures.addFeature(new WorldFeatureDeadBush(Block.deadbush.id), 1, 10, null);
	}
}
