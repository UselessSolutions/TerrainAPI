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
	public void addFeatureSurface(WorldFeature feature, int chances, Biome[] biomes){
		addFeature(feature, -1f, chances, biomes);
	}
	public void addFeature(WorldFeature feature, float rangeModifier, int chances, Biome[] biomes){
		addComplexFeature((Parameters x) -> feature, null, OverworldFunctions::getStandardBiomesDensity, new Object[]{chances, biomes}, rangeModifier);
	}

	/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, and index 3 with the ChunkDecorator. Additional parameters can be added in the method.
	 * Range Modifier of -1 indicates that the feature should only generate on the surface
	 *
	 */
	public void addComplexFeature(Function<Parameters, WorldFeature> featureFunction, Object[] featureParameters, Function<Parameters, Integer> densityFunction, Object[] densityParameters, float rangeModifier){
		super.addComplexFeature(featureFunction, featureParameters, densityFunction, densityParameters);
		rangeModifierList.add(rangeModifier);
	}
}
