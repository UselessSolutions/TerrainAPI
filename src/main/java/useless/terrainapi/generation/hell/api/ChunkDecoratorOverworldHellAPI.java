package useless.terrainapi.generation.hell.api;

import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.noise.PerlinNoise;
import org.jetbrains.annotations.ApiStatus;
import useless.terrainapi.config.ConfigManager;
import useless.terrainapi.generation.ChunkDecoratorAPI;
import useless.terrainapi.generation.Parameters;
import useless.terrainapi.generation.StructureFeatures;
import useless.terrainapi.generation.hell.HellConfig;
import useless.terrainapi.generation.overworld.OverworldBiomeFeatures;
import useless.terrainapi.generation.overworld.OverworldOreFeatures;
import useless.terrainapi.generation.overworld.OverworldRandomFeatures;

import java.util.Random;

public class ChunkDecoratorOverworldHellAPI extends ChunkDecoratorAPI {
	public final PerlinNoise treeDensityNoise;
	public static HellConfig hellConfig = ConfigManager.getConfig("hell", HellConfig.class);
	public static StructureFeatures structureFeatures = new StructureFeatures();
	public static OverworldOreFeatures oreFeatures = new OverworldOreFeatures(hellConfig);
	public static OverworldRandomFeatures randomFeatures = new OverworldRandomFeatures();
	public static OverworldBiomeFeatures biomeFeatures = new OverworldBiomeFeatures();
	protected ChunkDecoratorOverworldHellAPI(World world) {
		super(world);
		this.treeDensityNoise = new PerlinNoise(world.getRandomSeed(), 8, 74);
	}

	@Override
	@ApiStatus.Internal
	public void decorateAPI() {
		int xCoord = parameterBase.chunk.xPosition * 16;
		int zCoord = parameterBase.chunk.zPosition * 16;

		generateStructures(parameterBase.biome, parameterBase.chunk, parameterBase.random);
		generateOreFeatures(parameterBase.biome, xCoord, zCoord, parameterBase.random, parameterBase.chunk);
		generateBiomeFeature(parameterBase.biome,xCoord, zCoord, parameterBase.random, parameterBase.chunk);
		generateRandomFeatures(parameterBase.biome,xCoord, zCoord, parameterBase.random, parameterBase.chunk);

		if (this.world.getBlockId(xCoord, minY + this.world.getWorldType().getOceanY() - 1, zCoord) == this.world.getWorldType().getOceanBlock()) {
			this.world.setBlockWithNotify(xCoord, minY + this.world.getWorldType().getOceanY() - 1, zCoord, this.world.getWorldType().getOceanBlock());
		}
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
