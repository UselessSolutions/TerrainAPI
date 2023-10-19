package useless.terrainapi.generation.overworld;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.generate.feature.WorldFeature;
import useless.terrainapi.TerrainMain;
import useless.terrainapi.generation.GeneratorFeatures;
import useless.terrainapi.generation.VanillaFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class OverworldOreFeatures extends GeneratorFeatures {
	protected  List<Float> rangeModifierList = new ArrayList<>();
	public static HashMap<Integer, Integer> blockNumberMap = new HashMap<>();
	public static HashMap<Integer, Integer> chancesMap = new HashMap<>();
	public static HashMap<Integer, Float> rangeMap = new HashMap<>();
	public  void addFeature(WorldFeature feature, int chances, float rangeModifier){
		addFeature(feature, chances, rangeModifier, null);
	}
	public  void addFeature(WorldFeature feature, int chances, float rangeModifier, Biome[] biomes){
		addComplexFeature((Object[] x) -> feature, null, VanillaFunctions::getStandardOreBiomesDensity, new Object[]{chances, biomes}, rangeModifier);
	}
	/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, index 3 with the ChunkDecorator, and index 4 with the oreHeightModifier. Additional parameters can be added in the method.
	 * Range Modifier of -1 indicates that the feature should only generate on the surface
	 *
	 */
	public void addComplexFeature(Function<Object[], WorldFeature> featureFunction, Object[] featureParameters, Function<Object[], Integer> densityFunction, Object[] densityParameters, float rangeModifier){
		super.addComplexFeature(featureFunction, featureParameters, densityFunction, densityParameters);
		rangeModifierList.add(rangeModifier);
	}
	public static void setOreValues(String modID, int blockID, int blockNumbers, int chances, float range){
		if (blockNumberMap.get(blockID) != null){
			TerrainMain.LOGGER.warn(modID + String.format(" has changed block %s to generate %d blocks with %d chances and a range of %f", Block.getBlock(blockID).getKey(), blockNumbers, chances, range));
		}
		setOreValues(blockID, blockNumbers, chances, range);
	}
	protected static void setOreValues(int blockID, int blockNumbers, int chances, float range){
		blockNumberMap.put(blockID, blockNumbers);
		chancesMap.put(blockID, chances);
		rangeMap.put(blockID, range);
	}
}
