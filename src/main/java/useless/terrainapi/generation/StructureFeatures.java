package useless.terrainapi.generation;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StructureFeatures extends GeneratorFeatures{
	@ApiStatus.Internal
	public List<Function<Parameters, Void>> featureFunctionList = new ArrayList<>();
	@ApiStatus.Internal
	public List<Object[]> featureParametersList = new ArrayList<>();

	/**Adds a structure entry
	 * @param function Function that takes a Parameters object and handles the generation
	 * @param additionalParameters Object[] of additional parameters that will be included with the Parameters object passed into the function
	 */
	public void addFeature(Function<Parameters, Void> function, Object[] additionalParameters){
		featureFunctionList.add(function);
		featureParametersList.add(additionalParameters);
		assert featureFunctionList.size() == featureParametersList.size(): "Structure Features list sizes do not match!!";
	}
}
