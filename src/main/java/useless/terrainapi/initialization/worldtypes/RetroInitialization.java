package useless.terrainapi.initialization.worldtypes;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.generate.feature.WorldFeatureCactus;
import net.minecraft.core.world.generate.feature.WorldFeatureFlowers;
import net.minecraft.core.world.generate.feature.WorldFeatureSugarCane;
import useless.terrainapi.TerrainMain;
import useless.terrainapi.config.OreConfig;
import useless.terrainapi.generation.StructureFeatures;
import useless.terrainapi.generation.overworld.OverworldBiomeFeatures;
import useless.terrainapi.generation.overworld.OverworldFunctions;
import useless.terrainapi.generation.overworld.OverworldOreFeatures;
import useless.terrainapi.generation.overworld.OverworldRandomFeatures;
import useless.terrainapi.generation.retro.RetroFunctions;
import useless.terrainapi.generation.retro.api.ChunkDecoratorRetroAPI;
import useless.terrainapi.initialization.BaseInitialization;

public class RetroInitialization extends BaseInitialization {
	private static final OreConfig retroConfig = ChunkDecoratorRetroAPI.retroConfig;
	public static final StructureFeatures structureFeatures = ChunkDecoratorRetroAPI.structureFeatures;
	public static final OverworldOreFeatures oreFeatures = ChunkDecoratorRetroAPI.oreFeatures;
	public static final OverworldRandomFeatures randomFeatures = ChunkDecoratorRetroAPI.randomFeatures;
	public static final OverworldBiomeFeatures biomeFeatures = ChunkDecoratorRetroAPI.biomeFeatures;
	@Override
	protected void initValues() {
		retroConfig.setOreValues(TerrainMain.MOD_ID, Block.blockClay, 32, 10, 1);
	}

	@Override
	protected void initStructure() {
		structureFeatures.addFeature(RetroFunctions::generateDungeon, null);
		structureFeatures.addFeature(OverworldFunctions::generateRandomFluid, new Object[]{50, Block.fluidWaterFlowing.id});
		structureFeatures.addFeature(OverworldFunctions::generateRandomFluid, new Object[]{20, Block.fluidLavaFlowing.id});
	}

	@Override
	protected void initOre() {
		oreFeatures.addManagedOreFeature(Block.blockClay, false);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.dirt, 32, 20, 1, false);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.gravel, 32, 10, 1, false);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreCoalStone, 16, 20, 1, true);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreIronStone, 8, 20, 1f/2, true);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreGoldStone, 8, 2, 1f/4, true);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreRedstoneStone, 7, 8, 1f/8, true);
		oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreDiamondStone, 7, 1, 1f/8, true);
	}

	@Override
	protected void initRandom() {
		randomFeatures.addFeature(new WorldFeatureFlowers(Block.flowerRed.id), 2, 1);
		randomFeatures.addFeature(new WorldFeatureFlowers(Block.mushroomBrown.id), 4, 1);
		randomFeatures.addFeature(new WorldFeatureFlowers(Block.mushroomRed.id), 8, 1);
	}

	@Override
	protected void initBiome() {
		biomeFeatures.addFeature(RetroFunctions::getTreeFeature, null, RetroFunctions::getTreeDensity, null, -1);
		biomeFeatures.addFeature(new WorldFeatureFlowers(Block.flowerYellow.id), 1, 2, null);
		biomeFeatures.addFeature(new WorldFeatureSugarCane(), 1, 10, null);
		biomeFeatures.addFeature(new WorldFeatureCactus(), 1, 1, null);
	}
}
