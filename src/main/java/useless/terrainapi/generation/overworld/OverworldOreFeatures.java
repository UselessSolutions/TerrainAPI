package useless.terrainapi.generation.overworld;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.generate.feature.WorldFeature;
import useless.terrainapi.TerrainMain;
import useless.terrainapi.config.TerrainAPIConfig;
import useless.terrainapi.generation.GeneratorFeatures;
import useless.terrainapi.generation.VanillaFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OverworldOreFeatures extends GeneratorFeatures {
	public List<Float> rangeModifierList = new ArrayList<>();
	public TerrainAPIConfig config;
	public OverworldOreFeatures(TerrainAPIConfig config){
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
	@Deprecated
	public void setOreValues(String modID, int blockID, int clusterSize, int chances, float range){
		setOreValues(modID, Block.getBlock(blockID), clusterSize, chances, range);
	}
	public void setOreValues(String modID, Block block, int clusterSize, int chances, float range){
		if (config.clusterSize.get(block.getKey()) != null){
			TerrainMain.LOGGER.warn(modID + String.format(" has changed block %s to generate %d blocks with %d chances and a range of %f", block.getKey(), clusterSize, chances, range));
		}
		setOreValues(block, clusterSize, chances, range);
	}
	protected void setOreValues(Block block, int clusterSize, int chances, float range){
		if (config.clusterSize.containsKey(block.getKey()) && config.getConfigOverride()){
			return;
		}
		config.clusterSize.put(block.getKey(), clusterSize);
		config.chancesPerChunk.put(block.getKey(), chances);
		config.verticalRange.put(block.getKey(), range);
	}
}
