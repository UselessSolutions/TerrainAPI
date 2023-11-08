package useless.terrainapi.generation.overworld.api;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockSand;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureLake;
import net.minecraft.core.world.generate.feature.WorldFeatureLiquid;
import net.minecraft.core.world.noise.PerlinNoise;
import useless.terrainapi.config.ConfigManager;
import useless.terrainapi.config.OverworldConfig;
import useless.terrainapi.generation.ChunkDecoratorAPI;
import useless.terrainapi.generation.Parameters;
import useless.terrainapi.generation.StructureFeatures;
import useless.terrainapi.generation.overworld.OverworldBiomeFeatures;
import useless.terrainapi.generation.overworld.OverworldOreFeatures;
import useless.terrainapi.generation.overworld.OverworldRandomFeatures;

import java.util.Random;

public class ChunkDecoratorOverworldAPI extends ChunkDecoratorAPI {
	public static OverworldConfig overworldConfig = ConfigManager.getConfig("overworld", OverworldConfig.class);
	public final PerlinNoise treeDensityNoise;
	public final int treeDensityOverride;
	private final int lakeDensityDefault = 4;
	private Parameters parameterBase;
	public static StructureFeatures structureFeatures = new StructureFeatures();
	public static OverworldOreFeatures oreFeatures = new OverworldOreFeatures(overworldConfig);
	public static OverworldRandomFeatures randomFeatures = new OverworldRandomFeatures();
	public static OverworldBiomeFeatures biomeFeatures = new OverworldBiomeFeatures();
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

		BlockSand.fallInstantly = true;

		if (biome == Biomes.OVERWORLD_SWAMPLAND){
			swampFeature(xCoord, zCoord, swampRand);
		}
		int lakeChance = getLakeChance(biome);

		parameterBase = new Parameters(biome, random, chunk, this);

		generateLakeFeature(lakeChance, xCoord, zCoord, biome, random);

		generateStructures(biome, chunk, random);
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
		int featureSize = structureFeatures.featureFunctionsList.size();
		for (int i = 0; i < featureSize; i++) {
			structureFeatures.featureFunctionsList.get(i)
				.apply(new Parameters(parameterBase, structureFeatures.featureParametersList.get(i)));
		}
	}
	public void generateOreFeatures(Biome biome, int x, int z, Random random, Chunk chunk){
		int featureSize = oreFeatures.featureFunctionsList.size();
		for (int i = 0; i < featureSize; i++) {
			WorldFeature feature = oreFeatures.featureFunctionsList.get(i)
				.apply(new Parameters(parameterBase, oreFeatures.featureParametersList.get(i)));

			int density = oreFeatures.densityFunctionsList.get(i)
				.apply(new Parameters(parameterBase, oreFeatures.densityParametersList.get(i)));

			float rangeModifier = oreFeatures.rangeModifierList.get(i);
			generateWithChancesUnderground(feature, density, (int) (rangeModifier * rangeY), x, z, random);
		}
	}
	public void generateRandomFeatures(Biome biome, int x, int z, Random random, Chunk chunk){
		int featureSize = randomFeatures.featureFunctionsList.size();
		for (int i = 0; i < featureSize; i++) {
			if (random.nextInt(randomFeatures.inverseProbabilityList.get(i)) != 0) {continue;}
			WorldFeature feature = randomFeatures.featureFunctionsList.get(i)
				.apply(new Parameters(parameterBase, randomFeatures.featureParametersList.get(i)));

			int density = randomFeatures.densityFunctionsList.get(i)
				.apply(new Parameters(parameterBase, randomFeatures.densityParametersList.get(i)));

			float rangeModifier = randomFeatures.rangeModifierList.get(i);
			if (-1.01 <= rangeModifier && rangeModifier <= -0.99){
				generateWithChancesSurface(feature, density, x, z, 8, 8, random);
			} else {
				generateWithChancesUnderground(feature, density, (int) (rangeModifier * rangeY), x, z, 8, 8, random);
			}
		}
	}
	public void generateBiomeFeature(Biome biome, int x, int z, Random random, Chunk chunk){
		int featureSize = biomeFeatures.featureFunctionsList.size();
		for (int i = 0; i < featureSize; i++) {
			WorldFeature feature = biomeFeatures.featureFunctionsList.get(i)
				.apply(new Parameters(parameterBase, biomeFeatures.featureParametersList.get(i)));

			int density = biomeFeatures.densityFunctionsList.get(i)
				.apply(new Parameters(parameterBase, biomeFeatures.densityParametersList.get(i)));

			float rangeModifier = biomeFeatures.rangeModifierList.get(i);
			if (-1.01 <= rangeModifier && rangeModifier <= -0.99){
				generateWithChancesSurface(feature, density, x, z, 8, 8, random);
			} else {
				generateWithChancesUnderground(feature, density, (int) (rangeModifier * rangeY), x, z, 8, 8, random);
			}
		}
	}
}
