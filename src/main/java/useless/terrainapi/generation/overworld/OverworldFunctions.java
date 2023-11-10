package useless.terrainapi.generation.overworld;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.generate.feature.*;
import org.jetbrains.annotations.Nullable;
import useless.terrainapi.generation.Parameters;
import useless.terrainapi.generation.overworld.api.ChunkDecoratorOverworldAPI;
import useless.terrainapi.util.Utilities;

import java.util.Random;

public class OverworldFunctions {
	public static OverworldConfig overworldConfig = ChunkDecoratorOverworldAPI.overworldConfig;

	/**Vanilla tree feature generator
	 * @param parameters Parameters Container
	 * @return Tree feature as specified by Biome#getRandomWorldGenForTrees
	 */
	public static WorldFeature getTreeFeature(Parameters parameters){
		WorldFeature treeFeature = parameters.biome.getRandomWorldGenForTrees(parameters.random);
		treeFeature.func_517_a(1.0, 1.0, 1.0);
		return treeFeature;
	}

	/**Vanilla tree density
	 * @param parameters Parameters Container
	 * @return treeDensityOverride if applicable, otherwise returns the biome's tree density from OverworldConfig's tree density hashmap
	 */
	public static int getTreeDensity(Parameters parameters){
		ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) parameters.decorator;

		Integer treeDensity = overworldConfig.getTreeDensity(parameters.biome);

		if (decorator.treeDensityOverride != -1){
			return decorator.treeDensityOverride;
		}

		if (treeDensity != null && treeDensity == -1000){
			return 0;
		} else {
			int x = parameters.chunk.xPosition * 16;
			int z = parameters.chunk.zPosition * 16;
			double d = 0.5;

			int noiseValue = (int)((decorator.treeDensityNoise.get((double)x * d, (double)z * d) / 8.0 + parameters.random.nextDouble() * 4.0 + 4.0) / 3.0);
			int treeDensityOffset = 0;
			if (parameters.random.nextInt(10) == 0) {
				++treeDensityOffset;
			}
			if (treeDensity == null){
				return treeDensityOffset;
			}

			return treeDensity + noiseValue + treeDensityOffset;
		}
	}
	/**Vanilla grass feature generator
	 * @param parameters Parameters Container
	 * @return Randomly returns tall grass or the random grass for the biome as specified in the OverworldConfig biomeRandomGrassBlock hashmap
	 */
	public static WorldFeature grassTypeCondition(Parameters parameters){
		Block block = Block.tallgrass;
		if (Utilities.checkForBiomeInBiomes(parameters.biome, overworldConfig.biomeRandomGrassBlock.keySet().toArray(new String[0])) && parameters.random.nextInt(3) != 0) {
			block = overworldConfig.getRandomGrassBlock(parameters.biome, block);
		}
		return new WorldFeatureTallGrass(block.id);
	}
	/**Vanilla flower feature generator
	 * @param parameters Parameters Container
	 * @return Randomly returns yellow or red flower features
	 */
	public static WorldFeature flowerTypeCondition(Parameters parameters){
		int blockId = Block.flowerYellow.id;
		if (parameters.random.nextInt(3) != 0) {
			blockId = Block.flowerRed.id;
		}
		return new WorldFeatureTallGrass(blockId);
	}

	/**Vanilla biome feature density
	 * @param parameters Parameters Container
	 * @return number of chances per chunk if in valid biome
	 */
	public static int getStandardBiomesDensity(Parameters parameters){
		int chance = (int) parameters.customParameters[0];
		Biome[] biomes = (Biome[]) parameters.customParameters[1];
		if (biomes == null) {return chance;}
		if (Utilities.checkForBiomeInBiomes(parameters.biome, biomes)){
			return chance;
		}
		return 0;
	}

	/**Vanilla ore density
	 * @param parameters Parameters Container
	 * @return number of chances per chunk scaled by the oreHeightModifier if in valid biome
	 */
	public static int getStandardOreBiomesDensity(Parameters parameters){
		float oreHeightModifier = ((ChunkDecoratorOverworldAPI) parameters.decorator).oreHeightModifier;
		int chance = (int) parameters.customParameters[0];
		Biome[] biomes = (Biome[]) parameters.customParameters[1];
		if (biomes == null) {return chance;}
		if (Utilities.checkForBiomeInBiomes(parameters.biome, biomes)){
			return (int) (chance * oreHeightModifier);
		}
		return 0;
	}
	/**Vanilla dungeon generation code
	 * @param parameters Parameters Container
	 * @return null
	 */
	@Nullable
	public static Void generateDungeons(Parameters parameters){
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		ChunkDecoratorOverworldAPI decoratorOverworldAPI = (ChunkDecoratorOverworldAPI) parameters.decorator;
		for (int i = 0; i < 8.0f * decoratorOverworldAPI.oreHeightModifier; i++) {
			int xPos = x + parameters.random.nextInt(16) + 8;
			int yPos = decoratorOverworldAPI.minY + parameters.random.nextInt(decoratorOverworldAPI.rangeY);
			int zPos = z + parameters.random.nextInt(16) + 8;
			if (parameters.random.nextInt(2) == 0){
				new WorldFeatureDungeon(Block.brickClay.id, Block.brickClay.id, null).generate(decoratorOverworldAPI.world, parameters.random, xPos, yPos, zPos);
			} else {
				new WorldFeatureDungeon(Block.cobbleStone.id, Block.cobbleStoneMossy.id, null).generate(decoratorOverworldAPI.world, parameters.random, xPos, yPos, zPos);
			}
		}
		return null;
	}

	/**Vanilla labyrinth generation code
	 * @param parameters Parameters Container
	 * @return null
	 */
	@Nullable
	public static Void generateLabyrinths(Parameters parameters){
		ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) parameters.decorator;
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		for (int i = 0; i < 1; ++i) {
			int xPos = x + parameters.random.nextInt(16) + 8;
			int zPos = z + parameters.random.nextInt(16) + 8;
			int yPos = decorator.world.getHeightValue(xPos, zPos) - (parameters.random.nextInt(2) + 2);
			if (parameters.random.nextInt(5) == 0) {
				yPos -= parameters.random.nextInt(10) + 30;
			}
			if (parameters.random.nextInt(700) != 0) continue;
			Random lRand = parameters.chunk.getChunkRandom(75644760L);
			new WorldFeatureLabyrinth().generate(decorator.world, lRand, xPos, yPos, zPos);
		}
		return null;
	}
	/**Vanilla swamp generation code
	 * @param parameters Parameters Container
	 * @return null
	 */
	@Nullable
	public static Void generateSwamp(Parameters parameters){
		if (parameters.biome != Biomes.OVERWORLD_SWAMPLAND) return null;
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) parameters.decorator;

		Random swampRand = new Random(decorator.chunkSeed);

		for (int dx = 0; dx < 16; ++dx) {
			for (int dz = 0; dz < 16; ++dz) {
				if (!(swampRand.nextFloat() < 0.5f)) continue;

				int topBlock = decorator.world.getHeightValue(x + dx, z + dz);
				int id = decorator.world.getBlockId(x + dx, topBlock - 1, z + dz);
				if (id != Block.grass.id) continue;

				int posXId = decorator.world.getBlockId(x + dx + 1, topBlock - 1, z + dz);
				if (posXId == 0) continue;
				int negXId = decorator.world.getBlockId(x + dx - 1, topBlock - 1, z + dz);
				if (negXId == 0) continue;
				int posZId = decorator.world.getBlockId(x + dx, topBlock - 1, z + dz + 1);
				if (posZId == 0) continue;
				int negZId = decorator.world.getBlockId(x + dx, topBlock - 1, z + dz - 1);
				if (negZId == 0) continue;
				int negYId = decorator.world.getBlockId(x + dx, topBlock - 2, z + dz);
				if (negYId == 0) continue;

				if ((!Block.blocksList[posXId].blockMaterial.isSolid() && Block.blocksList[posXId].blockMaterial != Material.water)
					|| (!Block.blocksList[negXId].blockMaterial.isSolid() && Block.blocksList[negXId].blockMaterial != Material.water)
					|| (!Block.blocksList[posZId].blockMaterial.isSolid() && Block.blocksList[posZId].blockMaterial != Material.water)
					|| (!Block.blocksList[negZId].blockMaterial.isSolid() && Block.blocksList[negZId].blockMaterial != Material.water)
					|| !Block.blocksList[negYId].blockMaterial.isSolid()) continue;
				decorator.world.setBlock(x + dx, topBlock - 1, z + dz, Block.fluidWaterStill.id);
				decorator.world.setBlock(x + dx, topBlock, z + dz, 0);
			}
		}
		return null;
	}
	/**Vanilla lake generation code
	 * @param parameters Parameters Container
	 * @return null
	 */
	@Nullable
	public static Void generateLakeFeature(Parameters parameters){
		int lakeChance = overworldConfig.getLakeDensity(parameters.biome, overworldConfig.defaultLakeDensity);
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;

		ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) parameters.decorator;

		if (lakeChance != 0 && parameters.random.nextInt(lakeChance) == 0) {
			int fluid = Block.fluidWaterStill.id;
			if (parameters.biome.hasSurfaceSnow()) {
				fluid = Block.ice.id;
			}
			int xf = x + parameters.random.nextInt(16) + 8;
			int yf = decorator.minY + parameters.random.nextInt(decorator.rangeY);
			int zf = z + parameters.random.nextInt(16) + 8;
			new WorldFeatureLake(fluid).generate(decorator.world, parameters.random, xf, yf, zf);
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
		ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) parameters.decorator;
		if (parameters.random.nextInt(8) == 0) {
			int xf = x + parameters.random.nextInt(16) + 8;
			int yf = decorator.minY + parameters.random.nextInt(parameters.random.nextInt(decorator.rangeY - decorator.rangeY / 16) + decorator.rangeY / 16);
			int zf = z + parameters.random.nextInt(16) + 8;
			if (yf < decorator.minY + decorator.rangeY / 2 || parameters.random.nextInt(10) == 0) {
				new WorldFeatureLake(Block.fluidLavaStill.id).generate(decorator.world, parameters.random, xf, yf, zf);
			}
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
}
