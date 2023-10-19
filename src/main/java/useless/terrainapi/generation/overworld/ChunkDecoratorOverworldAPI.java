package useless.terrainapi.generation.overworld;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockSand;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.feature.*;
import net.minecraft.core.world.noise.PerlinNoise;
import useless.terrainapi.TerrainMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class ChunkDecoratorOverworldAPI extends ChunkDecoratorAPI {
	public final PerlinNoise treeDensityNoise;
	public final int treeDensityOverride;
	private final int lakeDensityDefault = 4;
	protected ChunkDecoratorOverworldAPI(World world, int treeDensityOverride) {
		super(world);
		this.treeDensityOverride = treeDensityOverride;
		this.treeDensityNoise = new PerlinNoise(world.getRandomSeed(), 8, 74);
	}

	public ChunkDecoratorOverworldAPI(World world) {
		this(world, -1);
	}
	@Override
	public void decorate(Chunk chunk) {
		int chunkX = chunk.xPosition;
		int chunkZ = chunk.zPosition;

		int xCoord = chunkX * 16;
		int zCoord = chunkZ * 16;
		int yCoord = this.world.getHeightValue(xCoord + 16, zCoord + 16);

		Biome biome = this.world.getBlockBiome(xCoord + 16, yCoord, zCoord + 16);

		Random random = new Random(this.world.getRandomSeed());
		long l1 = random.nextLong() / 2L * 2L + 1L;
		long l2 = random.nextLong() / 2L * 2L + 1L;
		random.setSeed((long)chunkX * l1 + (long)chunkZ * l2 ^ this.world.getRandomSeed());
		Random swampRand = new Random((long)chunkX * l1 + (long)chunkZ * l2 ^ this.world.getRandomSeed());
		Random structureRand = new Random((long)chunkX * l1 + (long)chunkZ * l2 ^ this.world.getRandomSeed());

		BlockSand.fallInstantly = true;

		if (biome == Biomes.OVERWORLD_SWAMPLAND){
			swampFeature(xCoord, zCoord, swampRand);
		}
		int lakeChance = getLakeChance(biome);

		generateLakeFeature(lakeChance, xCoord, zCoord, biome, random);

		generateStructures(biome, chunk, structureRand);
		generateOreFeatures(biome, xCoord, zCoord, random, chunk);
		generateBiomeFeature(biome,xCoord, zCoord, random, chunk);
		generateRandomFeatures(biome,xCoord, zCoord, random, chunk);

		generateWithChancesUnderground(new WorldFeatureLiquid(Block.fluidWaterFlowing.id), 50, rangeY, xCoord, zCoord, 8, 8, random);
		generateWithChancesUnderground(new WorldFeatureLiquid(Block.fluidLavaFlowing.id), 20, rangeY, xCoord, zCoord, 8, 8, random);

		freezeSurface(xCoord, zCoord);

		BlockSand.fallInstantly = false;

	}
	public void swampFeature(int x, int z, Random random){
		for (int dx = 0; dx < 16; ++dx) {
			for (int dz = 0; dz < 16; ++dz) {
				boolean shouldPlaceWater;
				int topBlock = this.world.getHeightValue(x + dx, z + dz);
				int id = this.world.getBlockId(x + dx, topBlock - 1, z + dz);
				if (id != Block.grass.id) continue;
				shouldPlaceWater = random.nextFloat() < 0.5f;
				if (!shouldPlaceWater) continue;
				int posXId = this.world.getBlockId(x + dx + 1, topBlock - 1, z + dz);
				int negXId = this.world.getBlockId(x + dx - 1, topBlock - 1, z + dz);
				int posZId = this.world.getBlockId(x + dx, topBlock - 1, z + dz + 1);
				int negZId = this.world.getBlockId(x + dx, topBlock - 1, z + dz - 1);
				int negYId = this.world.getBlockId(x + dx, topBlock - 2, z + dz);
				if (posXId == 0 || !Block.blocksList[posXId].blockMaterial.isSolid() && Block.blocksList[posXId].blockMaterial != Material.water || negXId == 0 || !Block.blocksList[negXId].blockMaterial.isSolid() && Block.blocksList[negXId].blockMaterial != Material.water || posZId == 0 || !Block.blocksList[posZId].blockMaterial.isSolid() && Block.blocksList[posZId].blockMaterial != Material.water || negZId == 0 || !Block.blocksList[negZId].blockMaterial.isSolid() && Block.blocksList[negZId].blockMaterial != Material.water || negYId == 0 || !Block.blocksList[negYId].blockMaterial.isSolid()) continue;
				this.world.setBlock(x + dx, topBlock - 1, z + dz, Block.fluidWaterStill.id);
				this.world.setBlock(x + dx, topBlock, z + dz, 0);
			}
		}
	}
	public int getLakeChance(Biome biome){
		if (biome == Biomes.OVERWORLD_SWAMPLAND) {
			return 2;
		}
		if (biome == Biomes.OVERWORLD_DESERT) {
			return 0;
		}
		return lakeDensityDefault;
	}
	public void generateLakeFeature(int lakeChance, int x, int z, Biome biome, Random random){
		if (lakeChance != 0 && random.nextInt(lakeChance) == 0) {
			int fluid = Block.fluidWaterStill.id;
			if (biome.hasSurfaceSnow()) {
				fluid = Block.ice.id;
			}
			int i1 = x + random.nextInt(16) + 8;
			int l4 = minY + random.nextInt(rangeY);
			int i8 = z + random.nextInt(16) + 8;
			new WorldFeatureLake(fluid).generate(this.world, random, i1, l4, i8);
		}
		if (random.nextInt(8) == 0) {
			int xf = x + random.nextInt(16) + 8;
			int yf = minY + random.nextInt(random.nextInt(rangeY - rangeY / 16) + rangeY / 16);
			int zf = z + random.nextInt(16) + 8;
			if (yf < minY + rangeY / 2 || random.nextInt(10) == 0) {
				new WorldFeatureLake(Block.fluidLavaStill.id).generate(this.world, random, xf, yf, zf);
			}
		}
	}
	public void generateStructures(Biome biome, Chunk chunk, Random random){
		int featureSize = StructureFeature.featureFunctionsList.size();
		for (int i = 0; i < featureSize; i++) {
			StructureFeature.featureFunctionsList.get(i)
				.apply(Parameters.packParameters(biome, random, chunk, this, StructureFeature.featureParametersList.get(i)));
		}
	}
	public void generateOreFeatures(Biome biome, int x, int z, Random random, Chunk chunk){
		int featureSize = OreFeatures.featureFunctionsList.size();
		for (int i = 0; i < featureSize; i++) {
			WorldFeature feature = OreFeatures.featureFunctionsList.get(i)
				.apply(Parameters.packParameters(biome, random, chunk, this, OreFeatures.featureParametersList.get(i)));

			int density = OreFeatures.densityFunctionsList.get(i)
				.apply(Parameters.packParameters(biome, random, chunk, this, OreFeatures.densityParametersList.get(i)));

			float rangeModifier = OreFeatures.rangeModifierList.get(i);
			generateWithChancesUnderground(feature, density, (int) (rangeModifier * rangeY), x, z, random);
		}
	}
	public void generateRandomFeatures(Biome biome, int x, int z, Random random, Chunk chunk){
		int featureSize = RandomFeatures.featureFunctionsList.size();
		for (int i = 0; i < featureSize; i++) {
			if (random.nextInt(RandomFeatures.inverseProbabilityList.get(i)) != 0) {continue;}
			WorldFeature feature = RandomFeatures.featureFunctionsList.get(i)
				.apply(Parameters.packParameters(biome, random, chunk, this, RandomFeatures.featureParametersList.get(i)));

			int density = RandomFeatures.densityFunctionsList.get(i)
				.apply(Parameters.packParameters(biome, random, chunk, this, RandomFeatures.densityParametersList.get(i)));

			float rangeModifier = RandomFeatures.rangeModifierList.get(i);
			if (-1.01 <= rangeModifier && rangeModifier <= -0.99){
				generateWithChancesSurface(feature, density, x, z, 8, 8, random);
			} else {
				generateWithChancesUnderground(feature, density, (int) (rangeModifier * rangeY), x, z, 8, 8, random);
			}
		}
	}
	public void generateBiomeFeature(Biome biome, int x, int z, Random random, Chunk chunk){
		int featureSize = BiomeFeatures.featureFunctionsList.size();
		for (int i = 0; i < featureSize; i++) {
			WorldFeature feature = BiomeFeatures.featureFunctionsList.get(i)
				.apply(Parameters.packParameters(biome, random, chunk, this, BiomeFeatures.featureParametersList.get(i)));

			int density = BiomeFeatures.densityFunctionsList.get(i)
				.apply(Parameters.packParameters(biome, random, chunk, this, BiomeFeatures.densityParametersList.get(i)));

			float rangeModifier = BiomeFeatures.rangeModifierList.get(i);
			if (-1.01 <= rangeModifier && rangeModifier <= -0.99){
				generateWithChancesSurface(feature, density, x, z, 8, 8, random);
			} else {
				generateWithChancesUnderground(feature, density, (int) (rangeModifier * rangeY), x, z, 8, 8, random);
			}
		}
	}


	public static class StructureFeature {
		protected static List<Function<Object[], Boolean>> featureFunctionsList = new ArrayList<>();
		protected static List<Object[]> featureParametersList = new ArrayList<>();
		/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, index 3 with the ChunkDecorator, and index 4 with the oreHeightModifier. Additional parameters can be added in the method.
		 * Range Modifier of -1 indicates that the feature should only generate on the surface
		 *
		 */
		public static void addStructure(Function<Object[], Boolean> function, Object[] functionParameters){
			featureFunctionsList.add(function);
			featureParametersList.add(functionParameters);
			assert featureFunctionsList.size() == featureParametersList.size(): "Structure Features list sizes do not match!!";
		}

	}
	public static class OreFeatures{
		protected static List<Function<Object[], WorldFeature>> featureFunctionsList = new ArrayList<>();
		protected static List<Object[]> featureParametersList = new ArrayList<>();
		protected static List<Function<Object[], Integer>> densityFunctionsList = new ArrayList<>();
		protected static List<Object[]> densityParametersList = new ArrayList<>();
		protected static List<Float> rangeModifierList = new ArrayList<>();
		public static HashMap<Integer, Integer> blockNumberMap = new HashMap<>();
		public static HashMap<Integer, Integer> chancesMap = new HashMap<>();
		public static HashMap<Integer, Float> rangeMap = new HashMap<>();
		public static void addFeature(WorldFeature feature, int chances, float rangeModifier){
			addFeature(feature, chances, rangeModifier, null);
		}
		public static void addFeature(WorldFeature feature, int chances, float rangeModifier, Biome[] biomes){
			addComplexFeature((Object[] x) -> feature, null, VanillaFunctions::getStandardOreBiomesDensity, new Object[]{chances, biomes}, rangeModifier);
		}
		/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, index 3 with the ChunkDecorator, and index 4 with the oreHeightModifier. Additional parameters can be added in the method.
		 * Range Modifier of -1 indicates that the feature should only generate on the surface
		 *
		 */
		public static void addComplexFeature(Function<Object[], WorldFeature> featureFunction, Object[] featureParameters, Function<Object[], Integer> densityFunction, Object[] densityParameters, float rangeModifier){
			assert (rangeModifier >= 0 && rangeModifier <= 1f): "Range Modifier must be bounded to a range of [0f to 1f]";
			featureFunctionsList.add(featureFunction);
			featureParametersList.add(featureParameters);
			densityFunctionsList.add(densityFunction);
			densityParametersList.add(densityParameters);
			rangeModifierList.add(rangeModifier);
			assert (featureFunctionsList.size() == featureParametersList.size()) && (featureFunctionsList.size() == densityFunctionsList.size()) && (featureFunctionsList.size() == densityParametersList.size() && (featureFunctionsList.size() == rangeModifierList.size())): "OreFeatures list sizes do not match!!";
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
	public static class RandomFeatures {
		protected static List<Function<Object[], WorldFeature>> featureFunctionsList = new ArrayList<>();
		protected static List<Object[]> featureParametersList = new ArrayList<>();
		protected static List<Function<Object[], Integer>> densityFunctionsList = new ArrayList<>();
		protected static List<Object[]> densityParametersList = new ArrayList<>();
		protected static List<Float> rangeModifierList = new ArrayList<>();
		protected static List<Integer> inverseProbabilityList = new ArrayList<>();
		public static void addFeatureSurface(WorldFeature feature, int inverseProbability){
			addFeature(feature, inverseProbability, -1f);
		}
		public static void addFeature(WorldFeature feature, int inverseProbability, float rangeModifier){
			addFeature(feature, inverseProbability, rangeModifier,1, null);
		}
		public static void addFeature(WorldFeature feature, int inverseProbability, float rangeModifier, int chances, Biome[] biomes){
			addComplexFeature((Object[] x) -> feature, null, VanillaFunctions::getStandardBiomesDensity, new Object[]{chances, biomes}, inverseProbability, rangeModifier);
		}
		/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, and index 3 with the ChunkDecorator. Additional parameters can be added in the method.
		 * Range Modifier of -1 indicates that the feature should only generate on the surface
		 *
		 */
		public static void addComplexFeature(Function<Object[], WorldFeature> featureFunction, Object[] featureParameters, Function<Object[], Integer> densityFunction, Object[] densityParameters,int inverseProbability, float rangeModifier){
			assert (rangeModifier >= 0 && rangeModifier <= 1f) || (-1.01f <= rangeModifier && rangeModifier <= -0.99f): "Range Modifier must be bounded to a range of [0f to 1f]";
			featureFunctionsList.add(featureFunction);
			featureParametersList.add(featureParameters);
			densityFunctionsList.add(densityFunction);
			densityParametersList.add(densityParameters);
			rangeModifierList.add(rangeModifier);
			inverseProbabilityList.add(inverseProbability);
			assert (featureFunctionsList.size() == featureParametersList.size()) && (featureFunctionsList.size() == densityFunctionsList.size()) && (featureFunctionsList.size() == densityParametersList.size() && (featureFunctionsList.size() == rangeModifierList.size())): "RandomFeatures list sizes do not match!!";
		}
	}
	public static class BiomeFeatures {
		protected static List<Function<Object[], WorldFeature>> featureFunctionsList = new ArrayList<>();
		protected static List<Object[]> featureParametersList = new ArrayList<>();
		protected static List<Function<Object[], Integer>> densityFunctionsList = new ArrayList<>();
		protected static List<Object[]> densityParametersList = new ArrayList<>();
		protected static List<Float> rangeModifierList = new ArrayList<>();
		public static HashMap<Biome, Integer> grassDensityMap = new HashMap<>();
		public static HashMap<Biome, Integer> flowerDensityMap = new HashMap<>();
		public static HashMap<Biome, Integer> yellowFlowerDensityMap = new HashMap<>();
		public static HashMap<Biome, Integer> treeDensityMap = new HashMap<>();
		public static void addFeatureSurface(WorldFeature feature, int chances, Biome[] biomes){
			addFeature(feature, -1f, chances, biomes);
		}
		public static void addFeature(WorldFeature feature, float rangeModifier, int chances, Biome[] biomes){
			addComplexFeature((Object[] x) -> feature, null, VanillaFunctions::getStandardBiomesDensity, new Object[]{chances, biomes}, rangeModifier);
		}

		/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, and index 3 with the ChunkDecorator. Additional parameters can be added in the method.
		 * Range Modifier of -1 indicates that the feature should only generate on the surface
		 *
		 */
		public static void addComplexFeature(Function<Object[], WorldFeature> featureFunction, Object[] featureParameters, Function<Object[], Integer> densityFunction, Object[] densityParameters, float rangeModifier){
			assert (rangeModifier >= 0 && rangeModifier <= 1f) || (-1.01f <= rangeModifier && rangeModifier <= -0.99f): "Range Modifier must be bounded to a range of [0f to 1f]";
			featureFunctionsList.add(featureFunction);
			featureParametersList.add(featureParameters);
			densityFunctionsList.add(densityFunction);
			densityParametersList.add(densityParameters);
			rangeModifierList.add(rangeModifier);
			assert (featureFunctionsList.size() == featureParametersList.size()) && (featureFunctionsList.size() == densityFunctionsList.size()) && (featureFunctionsList.size() == densityParametersList.size() && (featureFunctionsList.size() == rangeModifierList.size())): "BiomeFeatures list sizes do not match!!";
		}
	}
}
