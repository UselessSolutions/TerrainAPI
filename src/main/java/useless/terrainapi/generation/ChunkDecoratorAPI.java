package useless.terrainapi.generation;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockSand;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.type.WorldTypes;
import org.jetbrains.annotations.ApiStatus;

import java.util.Random;

public abstract class ChunkDecoratorAPI implements ChunkDecorator {
	public final World world;
	public final int minY;
	public final int maxY;
	public final int rangeY;
	public final float oreHeightModifier;
	public long chunkSeed;
	protected Parameters parameterBase;
	protected ChunkDecoratorAPI(World world) {
		this.world = world;

		this.minY = this.world.getWorldType().getMinY();
		this.maxY = this.world.getWorldType().getMaxY();
		this.rangeY = maxY + 1 - minY;
		this.oreHeightModifier = (float)rangeY / 128.0f;
	}
	@Override
	public void decorate(Chunk chunk){
		int chunkX = chunk.xPosition;
		int chunkZ = chunk.zPosition;

		int xCoord = chunkX * 16;
		int zCoord = chunkZ * 16;
		int yCoord = this.world.getHeightValue(xCoord + 16, zCoord + 16);

		Biome biome = this.world.getBlockBiome(xCoord + 16, yCoord, zCoord + 16);

		Random random = new Random(this.world.getRandomSeed());
		long l1 = random.nextLong() / 2L * 2L + 1L;
		long l2 = random.nextLong() / 2L * 2L + 1L;
		chunkSeed = (long)chunkX * l1 + (long)chunkZ * l2 ^ this.world.getRandomSeed();
		random.setSeed(chunkSeed);
		parameterBase = new Parameters(biome, random, chunk, this);

		BlockSand.fallInstantly = true;

		decorateAPI();

		BlockSand.fallInstantly = false;
	}
	@ApiStatus.Internal
	public abstract void decorateAPI();
	@ApiStatus.Internal
	public abstract void generateStructures(Biome biome, Chunk chunk, Random random);
	@ApiStatus.Internal
	public abstract void generateOreFeatures(Biome biome, int x, int z, Random random, Chunk chunk);
	@ApiStatus.Internal
	public abstract void generateRandomFeatures(Biome biome, int x, int z, Random random, Chunk chunk);
	@ApiStatus.Internal
	public abstract void generateBiomeFeature(Biome biome, int x, int z, Random random, Chunk chunk);
	@ApiStatus.Internal
	public void generateWithChancesUnderground(WorldFeature worldFeature, float chances, int rangeY, int x, int z, Random random){
		generateWithChancesUnderground(worldFeature, chances, rangeY, x, z, 0, 0, random);
	}
	@ApiStatus.Internal
	public void generateWithChancesUnderground(WorldFeature worldFeature, float chances, int rangeY, int x, int z, int xOff, int zOff, Random random){
		for (int i = 0; i < chances; i++) {
			int posX = x + random.nextInt(16) + xOff;
			int posY = minY + random.nextInt(rangeY);
			int posZ = z + random.nextInt(16) + zOff;
			worldFeature.generate(world, random, posX, posY, posZ);
		}
	}
	@ApiStatus.Internal
	public void generateWithChancesSurface(WorldFeature worldFeature, float chances, int x, int z, Random random){
		generateWithChancesSurface(worldFeature, chances, x, z, 0, 0, random);
	}
	@ApiStatus.Internal
	public void generateWithChancesSurface(WorldFeature worldFeature, float chances, int x, int z, int xOff, int zOff, Random random){
		for (int i = 0; i < chances; i++) {
			int posX = x + random.nextInt(16) + xOff;
			int posZ = z + random.nextInt(16) + zOff;
			int posY = this.world.getHeightValue(posX, posZ);
			worldFeature.generate(world, random, posX, posY, posZ);
		}
	}
	@ApiStatus.Internal
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

}
