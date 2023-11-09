package useless.terrainapi.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StructureFeatures extends GeneratorFeatures{
	public List<Function<Parameters, Void>> featureFunctionList = new ArrayList<>();
	public List<Object[]> featureParametersList = new ArrayList<>();
	/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, index 3 with the ChunkDecorator, and index 4 with the oreHeightModifier. Additional parameters can be added in the method.
	 * Range Modifier of -1 indicates that the feature should only generate on the surface
	 *
	 */
	public void addStructure(Function<Parameters, Void> function, Object[] additionalParameters){
		featureFunctionList.add(function);
		featureParametersList.add(additionalParameters);
		assert featureFunctionList.size() == featureParametersList.size(): "Structure Features list sizes do not match!!";
	}
}
