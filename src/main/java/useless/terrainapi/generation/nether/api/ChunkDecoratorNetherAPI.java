package useless.terrainapi.generation.nether.api;

import net.minecraft.core.block.BlockSand;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.ApiStatus;
import useless.terrainapi.config.ConfigManager;
import useless.terrainapi.generation.nether.NetherConfig;
import useless.terrainapi.generation.ChunkDecoratorAPI;
import useless.terrainapi.generation.Parameters;
import useless.terrainapi.generation.StructureFeatures;
import useless.terrainapi.generation.nether.NetherBiomeFeatures;
import useless.terrainapi.generation.nether.NetherOreFeatures;
import useless.terrainapi.generation.nether.NetherRandomFeatures;

import java.util.Random;

public class ChunkDecoratorNetherAPI extends ChunkDecoratorAPI {
	public static NetherConfig netherConfig = ConfigManager.getConfig("nether", NetherConfig.class);
	public static StructureFeatures structureFeatures = new StructureFeatures();
	public static NetherOreFeatures oreFeatures = new NetherOreFeatures(netherConfig);
	public static NetherRandomFeatures randomFeatures = new NetherRandomFeatures();
	public static NetherBiomeFeatures biomeFeatures = new NetherBiomeFeatures();
	private Parameters parameterBase;
	protected ChunkDecoratorNetherAPI(World world) {
		super(world);
	}

	@Override
	@ApiStatus.Internal
	public void decorate(Chunk chunk) {
		int chunkX = chunk.xPosition;
		int chunkZ = chunk.zPosition;
		int xCoord = chunkX * 16;
		int zCoord = chunkZ * 16;
		Random random = new Random((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
		Random structureRand = new Random((long)chunkX * 341873128712L + (long)chunkZ * 341873128712L);

		Biome biome = this.world.getBlockBiome(xCoord + 16, maxY-1, zCoord + 16);

		BlockSand.fallInstantly = true;

		parameterBase = new Parameters(biome, random, chunk, this);

		generateStructures(biome, chunk, structureRand);
		generateOreFeatures(biome, xCoord, zCoord, random, chunk);
		generateBiomeFeature(biome,xCoord, zCoord, random, chunk);
		generateRandomFeatures(biome,xCoord, zCoord, random, chunk);

		BlockSand.fallInstantly = false;
	}

	@ApiStatus.Internal
	public void generateStructures(Biome biome, Chunk chunk, Random random){
		int featureSize = structureFeatures.featureFunctionList.size();
		for (int i = 0; i < featureSize; i++) {
			structureFeatures.featureFunctionList.get(i)
				.apply(new Parameters(parameterBase, structureFeatures.featureParametersList.get(i)));
		}
	}
	@ApiStatus.Internal
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
	@ApiStatus.Internal
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
	@ApiStatus.Internal
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
