package useless.terrainapi.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StructureFeatures extends GeneratorFeatures{
	public List<Function<Object[], Boolean>> featureFunctionsList = new ArrayList<>();
	public List<Object[]> featureParametersList = new ArrayList<>();
	/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, index 3 with the ChunkDecorator, and index 4 with the oreHeightModifier. Additional parameters can be added in the method.
	 * Range Modifier of -1 indicates that the feature should only generate on the surface
	 *
	 */
	public void addStructure(Function<Object[], Boolean> function, Object[] functionParameters){
		featureFunctionsList.add(function);
		featureParametersList.add(functionParameters);
		assert featureFunctionsList.size() == featureParametersList.size(): "Structure Features list sizes do not match!!";
	}
}
