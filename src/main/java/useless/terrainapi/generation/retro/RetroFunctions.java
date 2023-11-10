package useless.terrainapi.generation.retro;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureDungeon;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeFancy;
import useless.terrainapi.generation.Parameters;
import useless.terrainapi.generation.retro.api.ChunkDecoratorRetroAPI;

public class RetroFunctions {
	public static Void generateDungeon(Parameters parameters){
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		for (int i1 = 0; i1 < 8; ++i1) {
			int structX = x + parameters.random.nextInt(16) + 8;
			int structY = parameters.decorator.minY + parameters.random.nextInt(parameters.decorator.rangeY);
			int structZ = z + parameters.random.nextInt(16) + 8;
			new WorldFeatureDungeon(Block.cobbleStone.id, Block.cobbleStoneMossy.id, null).generate(parameters.decorator.world, parameters.random, structX, structY, structZ);
		}
		return null;
	}
	public static WorldFeature getTreeFeature(Parameters parameters){
		WorldFeature tree = new WorldFeatureTree(Block.leavesOakRetro.id, Block.logOak.id, 4);
		if (parameters.random.nextInt(10) == 0) {
			tree = new WorldFeatureTreeFancy(Block.leavesOakRetro.id, Block.logOak.id);
		}
		tree.func_517_a(1.0, 1.0, 1.0);
		return tree;
	}
	public static Integer getTreeDensity(Parameters parameters){
		double d = 0.5;
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		ChunkDecoratorRetroAPI decoratorRetro = (ChunkDecoratorRetroAPI) parameters.decorator;
		int density = (int)((decoratorRetro.treeDensityNoise.get((double)x * d, (double)z * d) / 8.0 + parameters.random.nextDouble() * 4.0 + 4.0) / 3.0);
		if (density < 0) {
			density = 0;
		}
		if (parameters.random.nextInt(10) == 0) {
			++density;
		}
		return density;
	}
}
