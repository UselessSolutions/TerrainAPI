package useless.terrainapi.generation.overworld;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureOre;
import useless.terrainapi.config.OreConfig;
import useless.terrainapi.generation.GeneratorFeatures;
import useless.terrainapi.generation.VanillaFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OverworldOreFeatures extends GeneratorFeatures {
	public List<Float> rangeModifierList = new ArrayList<>();
	public OreConfig config;
	public OverworldOreFeatures(OreConfig config){
		this.config = config;
	}
	public void addFeature(WorldFeature feature, int chances, float rangeModifier){
		addFeature(feature, chances, rangeModifier, null);
	}
	public void addFeature(WorldFeature feature, int chances, float rangeModifier, Biome[] biomes){
		addComplexFeature((Object[] x) -> feature, null, VanillaFunctions::getStandardOreBiomesDensity, new Object[]{chances, biomes}, rangeModifier);
	}
	/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, index 3 with the ChunkDecorator, and index 4 with the oreHeightModifier. Additional parameters can be added in the method.
	 * Range Modifier of -1 indicates that the feature should only generate on the surface
	 *
	 */
	public void addComplexFeature(Function<Object[], WorldFeature> featureFunction, Object[] featureParameters, Function<Object[], Integer> densityFunction, Object[] densityParameters, float rangeModifier){
		super.addComplexFeature(featureFunction, featureParameters, densityFunction, densityParameters);
		rangeModifierList.add(rangeModifier);
	}
	public void addManagedOreFeature(String modID, Block block, int defaultClusterSize, int defaultChances, float defaultRange, boolean hasStoneStates){
		config.setOreValues(modID, block, defaultClusterSize, defaultChances, defaultRange);
		addManagedOreFeature(block, hasStoneStates);
	}
	public void addManagedOreFeature(Block block, boolean hasStoneStates){
		String currentBlock = block.getKey();
		addFeature(new WorldFeatureOre(block.id, config.clusterSize.get(currentBlock), hasStoneStates), config.chancesPerChunk.get(currentBlock), config.verticalRange.get(currentBlock));
	}
}
