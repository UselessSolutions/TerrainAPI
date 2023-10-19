package useless.terrainapi.generation.nether;

import net.minecraft.core.block.Block;
import useless.terrainapi.TerrainMain;
import useless.terrainapi.generation.overworld.OverworldOreFeatures;

import java.util.HashMap;

public class NetherOreFeatures extends OverworldOreFeatures {
	public HashMap<Integer, Integer> blockNumberMap = new HashMap<>();
	public HashMap<Integer, Integer> chancesMap = new HashMap<>();
	public HashMap<Integer, Float> rangeMap = new HashMap<>();

	public void setOreValues(String modID, int blockID, int blockNumbers, int chances, float range){
		if (blockNumberMap.get(blockID) != null){
			TerrainMain.LOGGER.warn(modID + String.format(" has changed block %s to generate %d blocks with %d chances and a range of %f", Block.getBlock(blockID).getKey(), blockNumbers, chances, range));
		}
		setOreValues(blockID, blockNumbers, chances, range);
	}
	protected void setOreValues(int blockID, int blockNumbers, int chances, float range){
		blockNumberMap.put(blockID, blockNumbers);
		chancesMap.put(blockID, chances);
		rangeMap.put(blockID, range);
	}
}
