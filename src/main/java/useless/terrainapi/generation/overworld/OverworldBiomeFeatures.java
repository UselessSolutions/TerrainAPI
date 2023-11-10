package useless.terrainapi.generation.overworld;

import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.generate.feature.WorldFeature;
import useless.terrainapi.generation.GeneratorFeatures;
import useless.terrainapi.generation.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OverworldBiomeFeatures extends GeneratorFeatures {
	public List<Float> rangeModifierList = new ArrayList<>();

	/**Adds a world feature entry
	 * @param feature WorldFeature to generate
	 * @param chances Number of attempts per chunk
	 * @param biomes List of biomes to generate in, generates in any biome if array is null
	 */
	public void addFeatureSurface(WorldFeature feature, int chances, Biome[] biomes){
		addFeature(feature, -1f, chances, biomes);
	}

	/**Adds a world feature entry
	 * @param feature WorldFeature to generate
	 * @param chances Number of attempts per chunk
	 * @param rangeModifier Fraction of the world from the bottom to the surface to generate inside, a value of -1 indicates to spawn on the surface only
	 * @param biomes List of biomes to generate in, generates in any biome if array is null
	 */
	public void addFeature(WorldFeature feature, float rangeModifier, int chances, Biome[] biomes){
		addFeature((Parameters x) -> feature, null, OverworldFunctions::getStandardBiomesDensity, new Object[]{chances, biomes}, rangeModifier);
	}

	/**Adds a world feature entry
	 * @param featureFunction Function that takes a Parameters object and returns a WorldFeature
	 * @param featureParameters Object[] of additional parameters that will be included with the Parameters object passed into the feature function
	 * @param densityFunction Function that takes a Parameters object and returns an Integer representing the number of attempts per chunk
	 * @param densityParameters Object[] of additional parameters that will be included with the Parameters object passed into the density function
	 * @param rangeModifier Fraction of the world from the bottom to the surface to generate inside, a value of -1 indicates to spawn on the surface only
	 */
	public void addFeature(Function<Parameters, WorldFeature> featureFunction, Object[] featureParameters, Function<Parameters, Integer> densityFunction, Object[] densityParameters, float rangeModifier){
		super.addFeature(featureFunction, featureParameters, densityFunction, densityParameters);
		rangeModifierList.add(rangeModifier);
	}
}
