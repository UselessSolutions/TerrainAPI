package useless.terrainapi.generation;

import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GeneratorFeatures {
	@ApiStatus.Internal
	public List<Function<Parameters, WorldFeature>> featureFunctionsList = new ArrayList<>();
	@ApiStatus.Internal
	public List<Object[]> featureParametersList = new ArrayList<>();
	@ApiStatus.Internal
	public List<Function<Parameters, Integer>> densityFunctionsList = new ArrayList<>();
	@ApiStatus.Internal
	public List<Object[]> densityParametersList = new ArrayList<>();

	/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, index 3 with the ChunkDecorator, and index 4 with the oreHeightModifier. Additional parameters can be added in the method.
	 * Range Modifier of -1 indicates that the feature should only generate on the surface
	 *
	 */
	public void addComplexFeature(Function<Parameters, WorldFeature> featureFunction, Object[] featureParameters, Function<Parameters, Integer> densityFunction, Object[] densityParameters){
		featureFunctionsList.add(featureFunction);
		featureParametersList.add(featureParameters);
		densityFunctionsList.add(densityFunction);
		densityParametersList.add(densityParameters);
	}
}
