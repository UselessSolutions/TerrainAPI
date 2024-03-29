package useless.terrainapi.generation.overworld;

import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.ApiStatus;
import useless.terrainapi.generation.GeneratorFeatures;
import useless.terrainapi.generation.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OverworldRandomFeatures extends GeneratorFeatures {
	@ApiStatus.Internal
	public List<Float> startingRangeList = new ArrayList<>();
	@ApiStatus.Internal
	public List<Float> endingRangeList = new ArrayList<>();
	@ApiStatus.Internal
	public List<Integer> inverseProbabilityList = new ArrayList<>();
	/**Adds a world feature entry, will only generate on the surface
	 * @param feature WorldFeature to generate
	 * @param inverseProbability Inverse of the probability, example inverseProbability of 2 means a 50% chance
	 */
	public void addFeatureSurface(WorldFeature feature, int inverseProbability){
		addFeature(feature, inverseProbability, -1f);
	}

	/**Adds a world feature entry
	 * @param feature WorldFeature to generate
	 * @param inverseProbability Inverse of the probability, example inverseProbability of 2 means a 50% chance
	 * @param rangeModifier Fraction of the world from the bottom to the surface to generate inside, a value of -1 indicates to spawn on the surface only
	 */
	public void addFeature(WorldFeature feature, int inverseProbability, float rangeModifier){
		addFeature(feature, inverseProbability, rangeModifier,1, null);
	}

	/**Adds a world feature entry
	 * @param feature WorldFeature to generate
	 * @param inverseProbability Inverse of the probability, example inverseProbability of 2 means a 50% chance
	 * @param rangeModifier Fraction of the world from the bottom to the surface to generate inside, a value of -1 indicates to spawn on the surface only
	 * @param chances Number of attempts per chunk
	 * @param biomes List of biomes to generate in, generates in any biome if array is null
	 */
	public void addFeature(WorldFeature feature, int inverseProbability, float rangeModifier, int chances, Biome[] biomes){
		addFeature((Parameters x) -> feature, null, OverworldFunctions::getStandardBiomesDensity, new Object[]{chances, biomes}, inverseProbability, rangeModifier);
	}

	/**Adds a world feature entry
	 * @param featureFunction Function that takes a Parameters object and returns a WorldFeature
	 * @param featureParameters Object[] of additional parameters that will be included with the Parameters object passed into the feature function
	 * @param densityFunction Function that takes a Parameters object and returns an Integer representing the number of attempts per chunk
	 * @param densityParameters Object[] of additional parameters that will be included with the Parameters object passed into the density function
	 * @param inverseProbability Inverse of the probability, example inverseProbability of 2 means a 50% chance
	 * @param rangeModifier Fraction of the world from the bottom to the surface to generate inside, a value of -1 indicates to spawn on the surface only
	 */
	public void addFeature(Function<Parameters, WorldFeature> featureFunction, Object[] featureParameters, Function<Parameters, Integer> densityFunction, Object[] densityParameters, int inverseProbability, float rangeModifier){
		addFeature(featureFunction, featureParameters, densityFunction, densityParameters, inverseProbability, 0, rangeModifier);
	}
	/**Adds a world feature entry
	 * @param featureFunction Function that takes a Parameters object and returns a WorldFeature
	 * @param featureParameters Object[] of additional parameters that will be included with the Parameters object passed into the feature function
	 * @param densityFunction Function that takes a Parameters object and returns an Integer representing the number of attempts per chunk
	 * @param densityParameters Object[] of additional parameters that will be included with the Parameters object passed into the density function
	 * @param inverseProbability Inverse of the probability, example inverseProbability of 2 means a 50% chance
	 * @param startingRange Fraction of the world from the bottom to the surface to generate inside, a value of -1 indicates to spawn on the surface only
	 * @param endingRange Fraction of the world from the bottom to the surface to generate inside, a value of -1 indicates to spawn on the surface only
	 */
	public void addFeature(Function<Parameters, WorldFeature> featureFunction, Object[] featureParameters, Function<Parameters, Integer> densityFunction, Object[] densityParameters, int inverseProbability, float startingRange, float endingRange){
		super.addFeature(featureFunction, featureParameters, densityFunction, densityParameters);
		endingRangeList.add(endingRange);
		startingRangeList.add(startingRange);
		inverseProbabilityList.add(inverseProbability);
	}
}
