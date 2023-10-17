package useless.terrainapi.generation.overworld;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockSand;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.BiomeOutback;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.feature.*;
import net.minecraft.core.world.noise.PerlinNoise;
import net.minecraft.core.world.type.WorldTypes;

import java.util.ArrayList;
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

		generateStructures(biome, chunk,xCoord, zCoord, structureRand);
		generateOreFeatures(biome, xCoord, zCoord, random);
		generateBiomeFeature(biome,xCoord, zCoord, random);
		generateRandomFeatures(biome,xCoord, zCoord, random);

		generateWithChancesUnderground(new WorldFeatureLiquid(Block.fluidWaterFlowing.id), 50, rangeY, xCoord, zCoord, 8, 8, random);
		generateWithChancesUnderground(new WorldFeatureLiquid(Block.fluidLavaFlowing.id), 20, rangeY, xCoord, zCoord, 8, 8, random);

		freezeSurface(xCoord, zCoord);

		BlockSand.fallInstantly = false;

	}
	public WorldFeature grassTypeCondition(Object[] parameters){
		Biome biome = (Biome)parameters[0];
		Random random = (Random)parameters[1];

		int blockId = Block.tallgrass.id;
		if ((biome == Biomes.OVERWORLD_RAINFOREST || biome == Biomes.OVERWORLD_SWAMPLAND || biome == Biomes.OVERWORLD_BOREAL_FOREST || biome == Biomes.OVERWORLD_TAIGA) && random.nextInt(3) != 0) {
			blockId = Block.tallgrassFern.id;
		}
		return new WorldFeatureTallGrass(blockId);
	}
	public WorldFeature flowerTypeCondition(Object[] parameters){
		Random random = (Random) parameters[0];
		int blockId = Block.flowerYellow.id;
		if (random.nextInt(3) != 0) {
			blockId = Block.flowerRed.id;
		}
		return new WorldFeatureTallGrass(blockId);
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
	public void generateDungeons(int x, int z, Random random){
		for (int i = 0; i < 8.0f * oreHeightModifier; i++) {
			int xPos = x + random.nextInt(16) + 8;
			int yPos = minY + random.nextInt(rangeY);
			int zPos = z + random.nextInt(16) + 8;
			if (random.nextInt(2) == 0){
				new WorldFeatureDungeon(Block.brickClay.id, Block.brickClay.id, null).generate(world, random, xPos, yPos, zPos);
			} else {
				new WorldFeatureDungeon(Block.cobbleStone.id, Block.cobbleStoneMossy.id, null).generate(world, random, xPos, yPos, zPos);
			}
		}
	}
	public void generateLabyrinths(int x, int z, Chunk chunk, Random random){
		for (int i = 0; i < 1; ++i) {
			int xPos = x + random.nextInt(16) + 8;
			int zPos = z + random.nextInt(16) + 8;
			int yPos = world.getHeightValue(xPos, zPos) - (random.nextInt(2) + 2);
			if (random.nextInt(5) == 0) {
				yPos -= random.nextInt(10) + 30;
			}
			if (random.nextInt(700) != 0) continue;
			Random lRand = chunk.getChunkRandom(75644760L);
			new WorldFeatureLabyrinth().generate(world, lRand, xPos, yPos, zPos);
		}
	}
	public void generateStructures(Biome biome, Chunk chunk, int x, int z, Random random){
		generateDungeons(x, z, random);
		generateLabyrinths(x, z, chunk, random);
	}
	public void generateOreFeatures(Biome biome, int x, int z, Random random){
		int featureSize = OreFeatures.featureList.size();
		for (int i = 0; i < featureSize; i++) {
			if (OreFeatures.biomesList.get(i) == null){
				generateWithChancesUnderground(OreFeatures.featureList.get(i),oreHeightModifier * OreFeatures.chancesList.get(i), (int) (rangeY * OreFeatures.rangeModifierList.get(i)), x, z, random);
			} else {
				for (Biome checkBiome: OreFeatures.biomesList.get(i)) {
					if (biome.equals(checkBiome)){
						generateWithChancesUnderground(OreFeatures.featureList.get(i),oreHeightModifier * OreFeatures.chancesList.get(i), (int) (rangeY * OreFeatures.rangeModifierList.get(i)), x, z, random);
						break;
					}
				}
			}
		}
	}
	public void generateRandomFeatures(Biome biome, int x, int z, Random random){
		if (random.nextInt(2) == 0) {
			generateWithChancesUnderground(new WorldFeatureFlowers(Block.flowerRed.id), 1, rangeY, x, z, 8, 8, random);
		}
		if (random.nextInt(4) == 0) {
			generateWithChancesUnderground(new WorldFeatureFlowers(Block.mushroomBrown.id), 1, rangeY, x, z, 8, 8, random);
		}
		if (random.nextInt(8) == 0) {
			generateWithChancesUnderground(new WorldFeatureFlowers(Block.mushroomRed.id), 1, rangeY, x, z, 8, 8, random);
		}
		if (random.nextInt(5) == 0) {
			generateWithChancesSurface(new WorldFeatureSugarCane(), 1, x, z, 8, 8, random);
		}
		if (random.nextInt(128) == 0) {
			generateWithChancesSurface(new WorldFeaturePumpkin(), 1, x, z, 8, 8, random);
		}
		if (random.nextInt(64) == 0) {
			generateWithChancesSurface(new WorldFeatureSponge(), 1, x, z, 8, 8, random);
		}
	}
	public void generateBiomeFeature(Biome biome, int x, int z, Random random){
		if (biome instanceof BiomeOutback){
			generateWithChancesSurface(new WorldFeatureRichScorchedDirt(10), 1,x, z, random);
		}

		int treeDensity = getTreeDensity(biome, x,z, random);
		WorldFeature treeFeature = biome.getRandomWorldGenForTrees(random);
		treeFeature.func_517_a(1.0, 1.0, 1.0);
		generateWithChancesSurface(treeFeature, treeDensity, x, z, 8, 8, random);

		if (biome == Biomes.OVERWORLD_RAINFOREST){
			generateWithChancesSurface(new WorldFeatureSugarCaneTall(), 1, x, z, 8, 8, random);
		}
		int flowerDensity = getFlowerDensity(biome);
		generateWithChancesAndConditionallyUnderground(this::flowerTypeCondition, new Object[]{random}, flowerDensity, x, z, 8, 8, random);

		int yellowFlowerDensity = getYellowFlowerDensity(biome);
		generateWithChancesUnderground(new WorldFeatureFlowers(Block.flowerYellow.id), yellowFlowerDensity, rangeY, x, z, 8, 8, random);

		int grassDensity = getGrassDensity(biome);
		generateWithChancesAndConditionallyUnderground(this::grassTypeCondition, new Object[]{biome, random}, grassDensity, x, z, 8, 8, random);
		if (biome == Biomes.OVERWORLD_OUTBACK) {
			generateWithChancesUnderground(new WorldFeatureSpinifexPatch(), 4, rangeY, x, z, 8, 8, random);
		}
		if (biome == Biomes.OVERWORLD_DESERT) {
			generateWithChancesUnderground(new WorldFeatureDeadBush(Block.deadbush.id),2, rangeY, x, z, 8, 8, random);
		}
		if (biome == Biomes.OVERWORLD_DESERT) {
			generateWithChancesUnderground(new WorldFeatureCactus(), 10, rangeY, x, z, 8, 8, random);
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
	public void generateWithChancesAndConditionallyUnderground(Function<Object[], WorldFeature> condition, Object[] parameters, float chances, int x, int z, int xOff, int zOff, Random random){
		for (int i = 0; i < chances; i++) {
			int posX = x + random.nextInt(16) + xOff;
			int posY = minY + random.nextInt(rangeY);
			int posZ = z + random.nextInt(16) + zOff;
			condition.apply(parameters).generate(world, random, posX, posY, posZ);
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
	public int getTreeDensity(Biome biome, int x, int z, Random random){
		if (treeDensityOverride != -1){
			return treeDensityOverride;
		}
		double d = 0.5;
		int noiseValue = (int)((this.treeDensityNoise.get((double)x * d, (double)z * d) / 8.0 + random.nextDouble() * 4.0 + 4.0) / 3.0);
		int treeDensity = 0;
		if (random.nextInt(10) == 0) {
			++treeDensity;
		}
		if (biome == Biomes.OVERWORLD_FOREST) {
			treeDensity += noiseValue + 5;
		}
		if (biome == Biomes.OVERWORLD_BIRCH_FOREST) {
			treeDensity += noiseValue + 4;
		}
		if (biome == Biomes.OVERWORLD_RAINFOREST) {
			treeDensity += noiseValue + 10;
		}
		if (biome == Biomes.OVERWORLD_SEASONAL_FOREST) {
			treeDensity += noiseValue + 2;
		}
		if (biome == Biomes.OVERWORLD_TAIGA) {
			treeDensity += noiseValue + 5;
		}
		if (biome == Biomes.OVERWORLD_BOREAL_FOREST) {
			treeDensity += noiseValue + 3;
		}
		if (biome == Biomes.OVERWORLD_DESERT) {
			treeDensity = 0;
		}
		if (biome == Biomes.OVERWORLD_TUNDRA) {
			treeDensity -= 20;
		}
		if (biome == Biomes.OVERWORLD_PLAINS) {
			treeDensity -= 20;
		}
		if (biome == Biomes.OVERWORLD_SWAMPLAND) {
			treeDensity += noiseValue + 4;
		}
		if (biome == Biomes.OVERWORLD_OUTBACK_GRASSY) {
			treeDensity += noiseValue;
		}
		return treeDensity;
	}
	public int getFlowerDensity(Biome biome){
		if (biome == Biomes.OVERWORLD_SEASONAL_FOREST) {
			return 1;
		}
		if (biome == Biomes.OVERWORLD_MEADOW) {
			return 2;
		}
		if (biome == Biomes.OVERWORLD_BOREAL_FOREST) {
			return 2;
		}
		if (biome == Biomes.OVERWORLD_SHRUBLAND) {
			return 1;
		}
		return 0;
	}
	public int getYellowFlowerDensity(Biome biome){
		if (biome == Biomes.OVERWORLD_FOREST) {
			return 2;
		}
		if (biome == Biomes.OVERWORLD_SWAMPLAND) {
			return 2;
		}
		if (biome == Biomes.OVERWORLD_TAIGA) {
			return 2;
		}
		if (biome == Biomes.OVERWORLD_PLAINS) {
			return 3;
		}
		if (biome == Biomes.OVERWORLD_OUTBACK_GRASSY || biome == Biomes.OVERWORLD_OUTBACK) {
			return 2;
		}
		return 0;
	}
	public int getGrassDensity(Biome biome){
		if (biome == Biomes.OVERWORLD_FOREST) {
			return 2;
		}
		if (biome == Biomes.OVERWORLD_MEADOW) {
			return 2;
		}
		if (biome == Biomes.OVERWORLD_RAINFOREST) {
			return 10;
		}
		if (biome == Biomes.OVERWORLD_DESERT) {
			return 5;
		}
		if (biome == Biomes.OVERWORLD_SEASONAL_FOREST) {
			return 2;
		}
		if (biome == Biomes.OVERWORLD_TAIGA) {
			return 1;
		}
		if (biome == Biomes.OVERWORLD_BOREAL_FOREST) {
			return 5;
		}
		if (biome == Biomes.OVERWORLD_PLAINS) {
			return 10;
		}
		if (biome == Biomes.OVERWORLD_SWAMPLAND) {
			return 4;
		}
		if (biome == Biomes.OVERWORLD_SHRUBLAND) {
			return 2;
		}
		if (biome == Biomes.OVERWORLD_OUTBACK_GRASSY) {
			return 25;
		}
		if (biome == Biomes.OVERWORLD_BIRCH_FOREST) {
			return 10;
		}
		return 0;
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

	static {
		OreFeatures.initialize();
	}
	public static class OreFeatures {
		public static List<WorldFeature> featureList = new ArrayList<>();
		public static List<Integer> chancesList = new ArrayList<>();
		public static List<Float> rangeModifierList = new ArrayList<>();
		public static List<Biome[]> biomesList = new ArrayList<>();
		private static final boolean hasInitialized = false;
		public static void addOreFeature(WorldFeature feature, int chances, float rangeModifier){
			addOreFeature(feature, chances, rangeModifier, null);
		}
		public static void addOreFeature(WorldFeature feature, int chances, float rangeModifier, Biome[] biomes){
			assert rangeModifier >= 0 && rangeModifier <= 1f: "Range Modifier must be bounded to a range of [0f to 1f]";
			featureList.add(feature);
			chancesList.add(chances);
			rangeModifierList.add(rangeModifier);
			biomesList.add(biomes);
			assert (featureList.size() == chancesList.size()) && (featureList.size() == rangeModifierList.size()) && (featureList.size() == biomesList.size()): "OreFeatures list sizes do not match!!";
		}
		private static void initialize(){
			if (hasInitialized) {return;}
			addOreFeature(new WorldFeatureClay(32), 20, 1);
			addOreFeature(new WorldFeatureOre(Block.dirt.id, 32, false), 20, 1);
			addOreFeature(new WorldFeatureOre(Block.gravel.id, 32, false), 10, 1);
			addOreFeature(new WorldFeatureOre(Block.oreCoalStone.id, 16, true), 20, 1);
			addOreFeature(new WorldFeatureOre(Block.oreIronStone.id, 8, true), 20, 1/2f);
			addOreFeature(new WorldFeatureOre(Block.oreGoldStone.id, 8, true), 2, 1/4f);
			addOreFeature(new WorldFeatureOre(Block.oreRedstoneStone.id, 7, true), 8, 1/8f);
			addOreFeature(new WorldFeatureOre(Block.oreDiamondStone.id, 7, true), 1, 1/8f);
			addOreFeature(new WorldFeatureOre(Block.mossStone.id, 32, true), 1, 1/2f);
			addOreFeature(new WorldFeatureOre(Block.oreLapisStone.id, 6, true), 1, 1/8f);
		}
	}
	public static class RandomFeatures {
		
	}
}
