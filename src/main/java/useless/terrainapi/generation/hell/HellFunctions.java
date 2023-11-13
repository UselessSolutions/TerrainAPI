package useless.terrainapi.generation.hell;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;
import net.minecraft.core.world.generate.feature.WorldFeatureLake;
import net.minecraft.core.world.generate.feature.WorldFeatureLiquid;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeFancy;
import org.jetbrains.annotations.Nullable;
import useless.terrainapi.generation.Parameters;
import useless.terrainapi.generation.hell.api.ChunkDecoratorOverworldHellAPI;

import java.util.Random;

public class HellFunctions {
	public static HellConfig hellConfig = ChunkDecoratorOverworldHellAPI.hellConfig;
	/**Vanilla labyrinth generation code
	 * @param parameters Parameters Container
	 * @return null
	 */
	@Nullable
	public static Void generateLabyrinths(Parameters parameters){
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		for (int i = 0; i < 1; ++i) {
			int xPos = x + parameters.random.nextInt(16) + 8;
			int zPos = z + parameters.random.nextInt(16) + 8;
			int yPos = parameters.decorator.world.getHeightValue(xPos, zPos) - (parameters.random.nextInt(2) + 2);
			if (parameters.random.nextInt(10) == 0) {
				yPos -= parameters.random.nextInt(10) + 30;
			}
			if (parameters.random.nextInt(512) != 0) continue;
			Random lRand = parameters.chunk.getChunkRandom(75644760L);
			new WorldFeatureLabyrinth().generate(parameters.decorator.world, lRand, xPos, yPos, zPos);
		}
		return null;
	}

	/**Vanilla random fluid generation code, takes two custom parameters (int)Chances and (int)BlockID
	* @param parameters Parameters Container
	* @return null
	*/
	public static Void generateRandomFluid(Parameters parameters){
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		int chances = (int) parameters.customParameters[0];
		int fluidId = (int) parameters.customParameters[1];
		for (int i = 0; i < chances; ++i) {
			int blockX = x + parameters.random.nextInt(16) + 8;
			int blockY = parameters.decorator.minY + parameters.random.nextInt(parameters.random.nextInt(parameters.decorator.rangeY - 8) + 8);
			int blockZ = z + parameters.random.nextInt(16) + 8;
			new WorldFeatureLiquid(fluidId).generate(parameters.decorator.world, parameters.random, blockX, blockY, blockZ);
		}
		return null;
	}

	/**Vanilla lava lake generation code
	 * @param parameters Parameters Container
	 * @return null
	 */
	@Nullable
	public static Void generateLavaLakeFeature(Parameters parameters){
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		if (parameters.random.nextInt(32) == 0) {
			int xf = x + parameters.random.nextInt(16) + 8;
			int yf = parameters.decorator.minY + parameters.random.nextInt(parameters.decorator.rangeY);
			int zf = z + parameters.random.nextInt(16) + 8;
			if (yf < parameters.decorator.minY + parameters.decorator.rangeY / 2 || parameters.random.nextInt(10) == 0) {
				new WorldFeatureLake(Block.fluidLavaStill.id).generate(parameters.decorator.world, parameters.random, xf, yf, zf);
			}
		}
		return null;
	}
	/**Vanilla obsidian lava lake generation code
	 * @param parameters Parameters Container
	 * @return null
	 */
	@Nullable
	public static Void generateObsidianLakeFeature(Parameters parameters){
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		if (parameters.random.nextInt(16) == 0) {
			int j1 = x + parameters.random.nextInt(16) + 8;
			int i5 = parameters.decorator.minY + parameters.random.nextInt(parameters.decorator.rangeY - 16) + 8;
			int j8 = z + parameters.random.nextInt(16) + 8;
			if (i5 < parameters.decorator.minY + parameters.decorator.rangeY / 2 || parameters.random.nextInt(10) == 0) {
				if (parameters.random.nextInt(4) == 0) {
					new WorldFeatureLake(Block.obsidian.id).generate(parameters.decorator.world, parameters.random, j1, i5, j8);
				} else {
					new WorldFeatureLake(Block.fluidLavaStill.id).generate(parameters.decorator.world, parameters.random, j1, i5, j8);
				}
			}
		}
		return null;
	}

	/**Vanilla tree feature generator
	 * @param parameters Parameters Container
	 * @return Tree feature as specified by Biome#getRandomWorldGenForTrees
	 */
	public static WorldFeature getTreeFeature(Parameters parameters){
		boolean hasLeaves = parameters.random.nextInt(hellConfig.invLeavesProbability) == 0;
		WorldFeature tree = parameters.random.nextInt(10) == 0 ? new WorldFeatureTreeFancy(hasLeaves ? Block.leavesOak.id : 0, Block.logOak.id) : new WorldFeatureTree(hasLeaves ? Block.leavesOak.id : 0, Block.logOak.id, 4);
		tree.func_517_a(1.0, 1.0, 1.0);
		return tree;
	}

	/**Vanilla tree density
	 * @param parameters Parameters Container
	 * @return treeDensityOverride if applicable, otherwise returns the biome's tree density from OverworldConfig's tree density hashmap
	 */
	public static Integer getTreeDensity(Parameters parameters){
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		double d = 0.5;
		ChunkDecoratorOverworldHellAPI decoratorHell = (ChunkDecoratorOverworldHellAPI) parameters.decorator;
		int treeDensity = (int)((decoratorHell.treeDensityNoise.get((double)x * d, (double)z * d) / 8.0 + parameters.random.nextDouble() * 4.0 + 4.0) / 3.0);
		if (parameters.random.nextInt(10) == 0) {
			++treeDensity;
		}
		return treeDensity/2;
	}

	/**Vanilla hell tree generator
	 * @param parameters Parameters Container
	 * @return null
	 */
	@Deprecated
	public static Void generateTrees(Parameters parameters){
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		for (int i = 0; i < getTreeDensity(parameters); i++) {
			int xf = x + parameters.random.nextInt(16) + 8;
			int zf = z + parameters.random.nextInt(16) + 8;
			int yf = parameters.decorator.world.getHeightValue(xf, zf);
			getTreeFeature(parameters).generate(parameters.decorator.world, parameters.random, xf, yf, zf);
		}
		return null;
	}
}
