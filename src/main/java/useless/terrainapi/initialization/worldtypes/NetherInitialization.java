package useless.terrainapi.initialization.worldtypes;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.generate.feature.*;
import useless.terrainapi.TerrainMain;
import useless.terrainapi.generation.Parameters;
import useless.terrainapi.generation.nether.NetherFunctions;
import useless.terrainapi.generation.nether.api.ChunkDecoratorNetherAPI;
import useless.terrainapi.initialization.BaseInitialization;

public class NetherInitialization extends BaseInitialization {
	@Override
	protected void initValues() {

	}

	@Override
	protected void initStructure() {

	}

	@Override
	protected void initOre() {
		ChunkDecoratorNetherAPI.oreFeatures.addFeature(new WorldFeatureNetherLava(Block.fluidLavaFlowing.id),  8,120/128f);
		ChunkDecoratorNetherAPI.oreFeatures.addManagedOreFeature(TerrainMain.MOD_ID, Block.oreNethercoalNetherrack, 12, 10, 120/128f, false);
		ChunkDecoratorNetherAPI.oreFeatures.addFeature((Parameters x) -> new WorldFeatureFire(), null, NetherFunctions::netherFireDensity, null, 120/128f);
		ChunkDecoratorNetherAPI.oreFeatures.addFeature((Parameters x) -> new WorldFeatureGlowstoneA(), null, NetherFunctions::netherFireDensity, null, 120/128f);
		ChunkDecoratorNetherAPI.oreFeatures.addFeature(new WorldFeatureGlowstoneB(), 10, 120/128f);
	}

	@Override
	protected void initRandom() {
		ChunkDecoratorNetherAPI.randomFeatures.addFeature(new WorldFeatureLake(Block.fluidLavaStill.id), 8, 120/128f);
	}

	@Override
	protected void initBiome() {

	}
}
