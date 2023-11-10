package useless.terrainapi.generation.overworld;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureOre;
import useless.terrainapi.config.OreConfig;
import useless.terrainapi.generation.GeneratorFeatures;
import useless.terrainapi.generation.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OverworldOreFeatures extends GeneratorFeatures {
	public List<Float> rangeModifierList = new ArrayList<>();
	public OreConfig config;
	public OverworldOreFeatures(OreConfig config){
		this.config = config;
	}

	/**Adds a world feature entry
	 * @param feature WorldFeature to generate
	 * @param chances Number of attempts per chunk
	 * @param rangeModifier Fraction of the world from the bottom to the surface to generate inside, a value of -1 indicates to spawn on the surface only
	 */
	public void addFeature(WorldFeature feature, int chances, float rangeModifier){
		addFeature(feature, chances, rangeModifier, null);
	}
	
	/**Adds a world feature entry
	 * @param feature WorldFeature to generate
	 * @param chances Number of attempts per chunk
	 * @param rangeModifier Fraction of the world from the bottom to the surface to generate inside, a value of -1 indicates to spawn on the surface only
	 * @param biomes List of biomes to generate in, generates in any biome if array is null
	 */
	public void addFeature(WorldFeature feature, int chances, float rangeModifier, Biome[] biomes){
		addComplexFeature((Parameters x) -> feature, null, OverworldFunctions::getStandardOreBiomesDensity, new Object[]{chances, biomes}, rangeModifier);
	}

	/**Adds a world feature entry
	 * @param featureFunction Function that takes a Parameters object and returns a WorldFeature
	 * @param featureParameters Object[] of additional parameters that will be included with the Parameters object passed into the feature function
	 * @param densityFunction Function that takes a Parameters object and returns an Integer representing the number of attempts per chunk
	 * @param densityParameters Object[] of additional parameters that will be included with the Parameters object passed into the density function
	 * @param rangeModifier Fraction of the world from the bottom to the surface to generate inside, a value of -1 indicates to spawn on the surface only
	 */
	public void addComplexFeature(Function<Parameters, WorldFeature> featureFunction, Object[] featureParameters, Function<Parameters, Integer> densityFunction, Object[] densityParameters, float rangeModifier){
		super.addComplexFeature(featureFunction, featureParameters, densityFunction, densityParameters);
		rangeModifierList.add(rangeModifier);
	}

	/**Adds an WorldFeatureOre, which has its generation characteristics managed by OreConfig
	 * @param block Ore to generate
	 * @param defaultClusterSize Default size in blocks of an ore vein
	 * @param defaultChances Default number of chances per chunk to generate an ore patch, this values scales with world height
	 * @param defaultRange Value from [0, 1], it's the default fraction from the bottom of the world to the surface that the ore can generate
	 * @param hasStoneStates Does ore have states for each stone type
	 */
	public void addManagedOreFeature(String modID, Block block, int defaultClusterSize, int defaultChances, float defaultRange, boolean hasStoneStates){
		config.setOreValues(modID, block, defaultClusterSize, defaultChances, defaultRange);
		addManagedOreFeature(block, hasStoneStates);
	}

	/**Adds an WorldFeatureOre, which has its generation characteristics managed by OreConfig
	 * @param block Ore to generate
	 * @param hasStoneStates Does ore have states for each stone type
	 */
	public void addManagedOreFeature(Block block, boolean hasStoneStates){
		String currentBlock = block.getKey();
		addFeature(new WorldFeatureOre(block.id, config.clusterSize.get(currentBlock), hasStoneStates), config.chancesPerChunk.get(currentBlock), config.verticalRange.get(currentBlock));
	}
}
