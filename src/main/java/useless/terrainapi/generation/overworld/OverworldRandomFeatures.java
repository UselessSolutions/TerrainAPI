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
	public List<Float> rangeModifierList = new ArrayList<>();
	@ApiStatus.Internal
	public List<Integer> inverseProbabilityList = new ArrayList<>();

	/**Adds a world feature entry, will only generate on the surface
	 * @param feature WorldFeature to generate
	 * @param inverseProbability Inverse of the probability, example inverseProbability of 2 means a 50% chance
	 */
	public void addFeatureSurface(WorldFeature feature, int inverseProbability){
		addFeature(feature, inverseProbability, -1f);
	}
	public void addFeature(WorldFeature feature, int inverseProbability, float rangeModifier){
		addFeature(feature, inverseProbability, rangeModifier,1, null);
	}
	public void addFeature(WorldFeature feature, int inverseProbability, float rangeModifier, int chances, Biome[] biomes){
		addComplexFeature((Parameters x) -> feature, null, OverworldFunctions::getStandardBiomesDensity, new Object[]{chances, biomes}, inverseProbability, rangeModifier);
	}
	/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, and index 3 with the ChunkDecorator. Additional parameters can be added in the method.
	 * Range Modifier of -1 indicates that the feature should only generate on the surface
	 *
	 */
	public void addComplexFeature(Function<Parameters, WorldFeature> featureFunction, Object[] featureParameters, Function<Parameters, Integer> densityFunction, Object[] densityParameters, int inverseProbability, float rangeModifier){
		super.addComplexFeature(featureFunction, featureParameters, densityFunction, densityParameters);
		rangeModifierList.add(rangeModifier);
		inverseProbabilityList.add(inverseProbability);
	}
}
