package useless.terrainapi.generation.overworld;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockSand;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.feature.*;
import net.minecraft.core.world.noise.PerlinNoise;
import net.minecraft.core.world.type.WorldTypes;
import useless.terrainapi.TerrainMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class ChunkDecoratorOverworldAPI implements ChunkDecorator {
	public final World world;
	public final PerlinNoise treeDensityNoise;
	public final int treeDensityOverride;
	private final int lakeDensityDefault = 4;
	public final int minY;
	public final int maxY;
	public final int rangeY;
	public final float oreHeightModifier;
	protected ChunkDecoratorOverworldAPI(World world, int treeDensityOverride) {
		this.world = world;
		this.treeDensityOverride = treeDensityOverride;
		this.treeDensityNoise = new PerlinNoise(world.getRandomSeed(), 8, 74);

		this.minY = this.world.getWorldType().getMinY();
		this.maxY = this.world.getWorldType().getMaxY();
		this.rangeY = maxY + 1 - minY;
		this.oreHeightModifier = (float)rangeY / 128.0f;
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
	public void generateWithChancesUnderground(WorldFeature worldFeature, float chances, int rangeY, int x, int z, Random random){
		generateWithChancesUnderground(worldFeature, chances, rangeY, x, z, 0, 0, random);
	}
	public void generateWithChancesUnderground(WorldFeature worldFeature, float chances, int rangeY, int x, int z, int xOff, int zOff, Random random){
		for (int i = 0; i < chances; i++) {
			int posX = x + random.nextInt(16) + xOff;
			int posY = minY + random.nextInt(rangeY);
			int posZ = z + random.nextInt(16) + zOff;
			worldFeature.generate(world, random, posX, posY, posZ);
		}
	}
	public void generateWithChancesSurface(WorldFeature worldFeature, float chances, int x, int z, Random random){
		generateWithChancesSurface(worldFeature, chances, x, z, 0, 0, random);
	}
	public void generateWithChancesSurface(WorldFeature worldFeature, float chances, int x, int z, int xOff, int zOff, Random random){
		for (int i = 0; i < chances; i++) {
			int posX = x + random.nextInt(16) + xOff;
			int posZ = z + random.nextInt(16) + zOff;
			int posY = this.world.getHeightValue(posX, posZ);
			worldFeature.generate(world, random, posX, posY, posZ);
		}
	}

	public void freezeSurface(int x, int z){
		int oceanY = this.world.getWorldType().getOceanY();
		for (int dx = x + 8; dx < x + 8 + 16; ++dx) {
			for (int dz = z + 8; dz < z + 8 + 16; ++dz) {
				int dy = this.world.getHeightValue(dx, dz);
				Biome localBiome = this.world.getBlockBiome(dx, dy, dz);
				if ((localBiome.hasSurfaceSnow() || this.world.worldType == WorldTypes.OVERWORLD_WINTER) && dy > 0 && dy < this.world.getHeightBlocks() && this.world.isAirBlock(dx, dy, dz) && this.world.getBlockMaterial(dx, dy - 1, dz).blocksMotion()) {
					this.world.setBlockWithNotify(dx, dy, dz, Block.layerSnow.id);
				}
				if (!localBiome.hasSurfaceSnow() && this.world.worldType != WorldTypes.OVERWORLD_WINTER || this.world.getBlockId(dx, oceanY - 1, dz) != Block.fluidWaterStill.id && this.world.getBlockId(dx, oceanY - 1, dz) != Block.fluidWaterFlowing.id) continue;
				this.world.setBlockWithNotify(dx, oceanY - 1, dz, Block.ice.id);
			}
		}
	}
	public static boolean checkForBiomeInBiomes(Biome biome, Biome[] biomes){
		for (Biome checkBiome: biomes) {
			if (biome.equals(checkBiome)){
				return true;
			}
		}
		return false;
	}

	static {
		StructureFeature.initialize();
		OreFeatures.initialize();
		RandomFeatures.initialize();
		BiomeFeatures.initialize();
	}
	public static class StructureFeature {
		protected static List<Function<Object[], Boolean>> featureFunctionsList = new ArrayList<>();
		protected static List<Object[]> featureParametersList = new ArrayList<>();
		private static boolean hasInitialized = false;
		/** The Object[] are the parameters passed into the provided function, index 0 will always be populated by Biome, index 1 with Random, index 2 with Chunk, index 3 with the ChunkDecorator, and index 4 with the oreHeightModifier. Additional parameters can be added in the method.
		 * Range Modifier of -1 indicates that the feature should only generate on the surface
		 *
		 */
		public static void addStructure(Function<Object[], Boolean> function, Object[] functionParameters){
			featureFunctionsList.add(function);
			featureParametersList.add(functionParameters);
			assert featureFunctionsList.size() == featureParametersList.size(): "Structure Features list sizes do not match!!";
		}
		private static void initialize() {
			if (hasInitialized) {
				return;
			}
			hasInitialized = true;
			addStructure(ComplexFunctions::generateDungeons, null);
			addStructure(ComplexFunctions::generateLabyrinths, null);

		}

	}
	public static class OreFeatures {
		protected static List<Function<Object[], WorldFeature>> featureFunctionsList = new ArrayList<>();
		protected static List<Object[]> featureParametersList = new ArrayList<>();
		protected static List<Function<Object[], Integer>> densityFunctionsList = new ArrayList<>();
		protected static List<Object[]> densityParametersList = new ArrayList<>();
		protected static List<Float> rangeModifierList = new ArrayList<>();
		private static boolean hasInitialized = false;
		protected static HashMap<Integer, Integer> blockNumberMap = new HashMap<>();
		protected static HashMap<Integer, Integer> chancesMap = new HashMap<>();
		protected static HashMap<Integer, Float> rangeMap = new HashMap<>();
		static {
			setOreValues(Block.blockClay.id, 32, 20, 1f);
			setOreValues(Block.dirt.id, 32, 20, 1f);
			setOreValues(Block.gravel.id, 32, 10, 1f);
			setOreValues(Block.oreCoalStone.id, 16, 20, 1f);
			setOreValues(Block.oreIronStone.id, 8, 20, 1/2f);
			setOreValues(Block.oreGoldStone.id, 8, 2, 1/4f);
			setOreValues(Block.oreRedstoneStone.id, 7, 8, 1/8f);
			setOreValues(Block.oreDiamondStone.id, 7, 1, 1/8f);
			setOreValues(Block.mossStone.id, 32, 1, 1/2f);
			setOreValues(Block.oreLapisStone.id, 6, 1, 1/8f);
		}
		public static void addFeature(WorldFeature feature, int chances, float rangeModifier){
			addFeature(feature, chances, rangeModifier, null);
		}
		public static void addFeature(WorldFeature feature, int chances, float rangeModifier, Biome[] biomes){
			addComplexFeature((Object[] x) -> feature, null, ComplexFunctions::getStandardOreBiomesDensity, new Object[]{chances, biomes}, rangeModifier);
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
		private static void initialize(){
			if (hasInitialized) {return;}
			hasInitialized = true;
			int currentBlockID = Block.blockClay.id;
			addFeature(new WorldFeatureClay(blockNumberMap.get(currentBlockID)), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
			addFeature(new WorldFeatureOre(currentBlockID = Block.dirt.id, blockNumberMap.get(currentBlockID), false), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
			addFeature(new WorldFeatureOre(currentBlockID = Block.gravel.id, blockNumberMap.get(currentBlockID), false), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
			addFeature(new WorldFeatureOre(currentBlockID = Block.oreCoalStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
			addFeature(new WorldFeatureOre(currentBlockID = Block.oreIronStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
			addFeature(new WorldFeatureOre(currentBlockID = Block.oreGoldStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
			addFeature(new WorldFeatureOre(currentBlockID = Block.oreRedstoneStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
			addFeature(new WorldFeatureOre(currentBlockID = Block.oreDiamondStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
			addFeature(new WorldFeatureOre(currentBlockID = Block.mossStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
			addFeature(new WorldFeatureOre(currentBlockID = Block.oreLapisStone.id, blockNumberMap.get(currentBlockID), true), chancesMap.get(currentBlockID), rangeMap.get(currentBlockID));
		}
	}
	public static class RandomFeatures {
		protected static List<Function<Object[], WorldFeature>> featureFunctionsList = new ArrayList<>();
		protected static List<Object[]> featureParametersList = new ArrayList<>();
		protected static List<Function<Object[], Integer>> densityFunctionsList = new ArrayList<>();
		protected static List<Object[]> densityParametersList = new ArrayList<>();
		protected static List<Float> rangeModifierList = new ArrayList<>();
		protected static List<Integer> inverseProbabilityList = new ArrayList<>();
		private static boolean hasInitialized = false;
		public static void addFeatureSurface(WorldFeature feature, int inverseProbability){
			addFeature(feature, inverseProbability, -1f);
		}
		public static void addFeature(WorldFeature feature, int inverseProbability, float rangeModifier){
			addFeature(feature, inverseProbability, rangeModifier,1, null);
		}
		public static void addFeature(WorldFeature feature, int inverseProbability, float rangeModifier, int chances, Biome[] biomes){
			addComplexFeature((Object[] x) -> feature, null, ComplexFunctions::getStandardBiomesDensity, new Object[]{chances, biomes}, inverseProbability, rangeModifier);
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
		private static void initialize(){
			if (hasInitialized) {return;}
			hasInitialized = true;
			addFeature(new WorldFeatureFlowers(Block.flowerRed.id), 2, 1);
			addFeature(new WorldFeatureFlowers(Block.mushroomBrown.id), 4, 1);
			addFeature(new WorldFeatureFlowers(Block.mushroomRed.id), 8, 1);
			addFeatureSurface(new WorldFeatureSugarCane(), 5);
			addFeatureSurface(new WorldFeaturePumpkin(), 128);
			addFeatureSurface(new WorldFeatureSponge(), 64);
		}
	}
	public static class BiomeFeatures {
		protected static List<Function<Object[], WorldFeature>> featureFunctionsList = new ArrayList<>();
		protected static List<Object[]> featureParametersList = new ArrayList<>();
		protected static List<Function<Object[], Integer>> densityFunctionsList = new ArrayList<>();
		protected static List<Object[]> densityParametersList = new ArrayList<>();
		protected static List<Float> rangeModifierList = new ArrayList<>();
		private static boolean hasInitialized = false;
		public static HashMap<Biome, Integer> grassDensityMap = new HashMap<>();
		public static HashMap<Biome, Integer> flowerDensityMap = new HashMap<>();
		public static HashMap<Biome, Integer> yellowFlowerDensityMap = new HashMap<>();
		public static HashMap<Biome, Integer> treeDensityMap = new HashMap<>();
		static {
			grassDensityMap.put(Biomes.OVERWORLD_FOREST, 2);
			grassDensityMap.put(Biomes.OVERWORLD_MEADOW, 2);
			grassDensityMap.put(Biomes.OVERWORLD_RAINFOREST, 10);
			grassDensityMap.put(Biomes.OVERWORLD_DESERT, 5);
			grassDensityMap.put(Biomes.OVERWORLD_SEASONAL_FOREST, 2);
			grassDensityMap.put(Biomes.OVERWORLD_TAIGA, 1);
			grassDensityMap.put(Biomes.OVERWORLD_BOREAL_FOREST, 5);
			grassDensityMap.put(Biomes.OVERWORLD_PLAINS, 10);
			grassDensityMap.put(Biomes.OVERWORLD_SWAMPLAND, 4);
			grassDensityMap.put(Biomes.OVERWORLD_SHRUBLAND, 2);
			grassDensityMap.put(Biomes.OVERWORLD_OUTBACK_GRASSY, 25);
			grassDensityMap.put(Biomes.OVERWORLD_BIRCH_FOREST, 10);

			flowerDensityMap.put(Biomes.OVERWORLD_SEASONAL_FOREST, 1);
			flowerDensityMap.put(Biomes.OVERWORLD_MEADOW, 2);
			flowerDensityMap.put(Biomes.OVERWORLD_BOREAL_FOREST, 2);
			flowerDensityMap.put(Biomes.OVERWORLD_SHRUBLAND, 1);

			yellowFlowerDensityMap.put(Biomes.OVERWORLD_FOREST, 2);
			yellowFlowerDensityMap.put(Biomes.OVERWORLD_SWAMPLAND, 2);
			yellowFlowerDensityMap.put(Biomes.OVERWORLD_TAIGA, 2);
			yellowFlowerDensityMap.put(Biomes.OVERWORLD_PLAINS, 3);
			yellowFlowerDensityMap.put(Biomes.OVERWORLD_OUTBACK_GRASSY, 2);
			yellowFlowerDensityMap.put(Biomes.OVERWORLD_OUTBACK, 2);

			treeDensityMap.put(Biomes.OVERWORLD_FOREST, 5);
			treeDensityMap.put(Biomes.OVERWORLD_BIRCH_FOREST, 4);
			treeDensityMap.put(Biomes.OVERWORLD_RAINFOREST, 10);
			treeDensityMap.put(Biomes.OVERWORLD_SEASONAL_FOREST, 2);
			treeDensityMap.put(Biomes.OVERWORLD_TAIGA, 5);
			treeDensityMap.put(Biomes.OVERWORLD_BOREAL_FOREST, 3);
			treeDensityMap.put(Biomes.OVERWORLD_DESERT, -1000);
			treeDensityMap.put(Biomes.OVERWORLD_TUNDRA, -1000);
			treeDensityMap.put(Biomes.OVERWORLD_PLAINS, -1000);
			treeDensityMap.put(Biomes.OVERWORLD_SWAMPLAND, 4);
			treeDensityMap.put(Biomes.OVERWORLD_OUTBACK_GRASSY, 0);

		}
		public static void addFeatureSurface(WorldFeature feature, int chances, Biome[] biomes){
			addFeature(feature, -1f, chances, biomes);
		}
		public static void addFeature(WorldFeature feature, float rangeModifier, int chances, Biome[] biomes){
			addComplexFeature((Object[] x) -> feature, null, ComplexFunctions::getStandardBiomesDensity, new Object[]{chances, biomes}, rangeModifier);
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
		public static void initialize(){
			if (hasInitialized) {return;}
			hasInitialized = true;
			addFeatureSurface(new WorldFeatureRichScorchedDirt(10), 1, new Biome[]{Biomes.OVERWORLD_OUTBACK, Biomes.OVERWORLD_OUTBACK_GRASSY});
			addComplexFeature(ComplexFunctions::getTreeFeature, null, ComplexFunctions::getTreeDensity, null, -1f);
			addFeatureSurface(new WorldFeatureSugarCaneTall(), 1, new Biome[]{Biomes.OVERWORLD_RAINFOREST});
			addComplexFeature(ComplexFunctions::flowerTypeCondition, null, (Object[] x) -> flowerDensityMap.getOrDefault((Biome)x[0], 0), null, 1f);
			addComplexFeature((Object[] x) -> new WorldFeatureFlowers(Block.flowerYellow.id), null, (Object[] x) -> yellowFlowerDensityMap.getOrDefault((Biome)x[0], 0), null, 1);
			addComplexFeature(ComplexFunctions::grassTypeCondition, null, (Object[] x) -> grassDensityMap.getOrDefault((Biome)x[0], 0), null, 1);
			addFeature(new WorldFeatureSpinifexPatch(), 1, 4, new Biome[]{Biomes.OVERWORLD_OUTBACK});
			addFeature(new WorldFeatureDeadBush(Block.deadbush.id), 1, 2, new Biome[]{Biomes.OVERWORLD_DESERT});
			addFeature(new WorldFeatureCactus(), 1, 10, new Biome[]{Biomes.OVERWORLD_DESERT});
		}
	}
	public static class ComplexFunctions {
		public static WorldFeature getTreeFeature(Object[] parameters){
			Biome biome = Parameters.getBiome(parameters);
			Random random = Parameters.getRandom(parameters);
			WorldFeature treeFeature = biome.getRandomWorldGenForTrees(random);
			treeFeature.func_517_a(1.0, 1.0, 1.0);
			return treeFeature;
		}
		public static int getTreeDensity(Object[] parameters){
			Biome biome = Parameters.getBiome(parameters);
			ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) Parameters.getDecorator(parameters);

			Integer treeDensity = BiomeFeatures.treeDensityMap.get(biome);

			if (decorator.treeDensityOverride != -1){
				return decorator.treeDensityOverride;
			}

			if (treeDensity != null && treeDensity == -1000){
				return 0;
			} else {
				Random random = Parameters.getRandom(parameters);
				Chunk chunk = Parameters.getChunk(parameters);

				int x = chunk.xPosition * 16;
				int z = chunk.zPosition * 16;
				double d = 0.5;

				int noiseValue = (int)((decorator.treeDensityNoise.get((double)x * d, (double)z * d) / 8.0 + random.nextDouble() * 4.0 + 4.0) / 3.0);
				int treeDensityOffset = 0;
				if (random.nextInt(10) == 0) {
					++treeDensityOffset;
				}
				if (treeDensity == null){
					return treeDensityOffset;
				}

				return treeDensity + noiseValue + treeDensityOffset;
			}
		}
		public static WorldFeature grassTypeCondition(Object[] parameters){
			Biome biome = Parameters.getBiome(parameters);
			Random random = Parameters.getRandom(parameters);

			int blockId = Block.tallgrass.id;
			if ((biome == Biomes.OVERWORLD_RAINFOREST || biome == Biomes.OVERWORLD_SWAMPLAND || biome == Biomes.OVERWORLD_BOREAL_FOREST || biome == Biomes.OVERWORLD_TAIGA) && random.nextInt(3) != 0) {
				blockId = Block.tallgrassFern.id;
			}
			return new WorldFeatureTallGrass(blockId);
		}
		public static WorldFeature flowerTypeCondition(Object[] parameters){
			Random random = Parameters.getRandom(parameters);
			int blockId = Block.flowerYellow.id;
			if (random.nextInt(3) != 0) {
				blockId = Block.flowerRed.id;
			}
			return new WorldFeatureTallGrass(blockId);
		}
		public static int getStandardBiomesDensity(Object[] parameters){
			Biome biome = Parameters.getBiome(parameters);
			int chance = (int) Parameters.getCustomParameter(parameters, 1);
			Biome[] biomes = (Biome[]) Parameters.getCustomParameter(parameters, 2);
			if (biomes == null) {return chance;}
			if (ChunkDecoratorOverworldAPI.checkForBiomeInBiomes(biome, biomes)){
				return chance;
			}
			return 0;
		}
		public static int getStandardOreBiomesDensity(Object[] parameters){
			Biome biome = Parameters.getBiome(parameters);
			float oreHeightModifier = ((ChunkDecoratorOverworldAPI) Parameters.getDecorator(parameters)).oreHeightModifier;
			int chance = (int) Parameters.getCustomParameter(parameters, 1);
			Biome[] biomes = (Biome[]) Parameters.getCustomParameter(parameters, 2);
			if (biomes == null) {return chance;}
			if (ChunkDecoratorOverworldAPI.checkForBiomeInBiomes(biome, biomes)){
				return (int) (chance * oreHeightModifier);
			}
			return 0;
		}
		public static Boolean generateDungeons(Object[] parameters){
			Random random = Parameters.getRandom(parameters);
			Chunk chunk = Parameters.getChunk(parameters);
			ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) Parameters.getDecorator(parameters);
			int x = chunk.xPosition * 16;
			int z = chunk.zPosition * 16;
			for (int i = 0; i < 8.0f * decorator.oreHeightModifier; i++) {
				int xPos = x + random.nextInt(16) + 8;
				int yPos = decorator.minY + random.nextInt(decorator.rangeY);
				int zPos = z + random.nextInt(16) + 8;
				if (random.nextInt(2) == 0){
					new WorldFeatureDungeon(Block.brickClay.id, Block.brickClay.id, null).generate(decorator.world, random, xPos, yPos, zPos);
				} else {
					new WorldFeatureDungeon(Block.cobbleStone.id, Block.cobbleStoneMossy.id, null).generate(decorator.world, random, xPos, yPos, zPos);
				}
			}
			return true;
		}
		public static Boolean generateLabyrinths(Object[] parameters){
			Random random = Parameters.getRandom(parameters);
			Chunk chunk = Parameters.getChunk(parameters);
			ChunkDecoratorOverworldAPI decorator = (ChunkDecoratorOverworldAPI) Parameters.getDecorator(parameters);
			int x = chunk.xPosition * 16;
			int z = chunk.zPosition * 16;
			for (int i = 0; i < 1; ++i) {
				int xPos = x + random.nextInt(16) + 8;
				int zPos = z + random.nextInt(16) + 8;
				int yPos = decorator.world.getHeightValue(xPos, zPos) - (random.nextInt(2) + 2);
				if (random.nextInt(5) == 0) {
					yPos -= random.nextInt(10) + 30;
				}
				if (random.nextInt(700) != 0) continue;
				Random lRand = chunk.getChunkRandom(75644760L);
				new WorldFeatureLabyrinth().generate(decorator.world, lRand, xPos, yPos, zPos);
			}
			return true;
		}
	}
}
