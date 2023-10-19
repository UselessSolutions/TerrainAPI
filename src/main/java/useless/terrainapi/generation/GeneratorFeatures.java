package useless.terrainapi.generation;

import net.minecraft.core.world.generate.feature.WorldFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GeneratorFeatures {
	public List<Function<Object[], WorldFeature>> featureFunctionsList = new ArrayList<>();
	public List<Object[]> featureParametersList = new ArrayList<>();
	public List<Function<Object[], Integer>> densityFunctionsList = new ArrayList<>();
	public List<Object[]> densityParametersList = new ArrayList<>();

	/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, index 3 with the ChunkDecorator, and index 4 with the oreHeightModifier. Additional parameters can be added in the method.
	 * Range Modifier of -1 indicates that the feature should only generate on the surface
	 *
	 */
	public void addComplexFeature(Function<Object[], WorldFeature> featureFunction, Object[] featureParameters, Function<Object[], Integer> densityFunction, Object[] densityParameters){
		featureFunctionsList.add(featureFunction);
		featureParametersList.add(featureParameters);
		densityFunctionsList.add(densityFunction);
		densityParametersList.add(densityParameters);
	}
}
