package useless.terrainapi.generation.overworld.api;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.MapGenBase;
import org.jetbrains.annotations.ApiStatus;

import java.util.Random;
@ApiStatus.Experimental
public class MapGenCavesAPI extends MapGenBase {
	private final boolean isAlphaWorldType;

	public MapGenCavesAPI(boolean isAlphaWorldType) {
		this.isAlphaWorldType = isAlphaWorldType;
	}
	@Override
	protected void doGeneration(World world, int chunkX, int chunkZ, int baseChunkX, int baseChunkZ, short[] data) {
		int cavesToGenerate = rand.nextInt(rand.nextInt(rand.nextInt(40) + 1) + 1);
		if (rand.nextInt(15) != 0) {
			cavesToGenerate = 0;
		}
		for (int i = 0; i < cavesToGenerate; ++i) {
			double blockX = chunkX * 16 + rand.nextInt(16);
			double blockY = rand.nextInt(rand.nextInt(this.worldObj.getHeightBlocks() - 8) + 8);
			double blockZ = chunkZ * 16 + rand.nextInt(16);
			int numBranches = 1;
			if (rand.nextInt(4) == 0) {
				this.generateHubRoom(rand.nextLong(), baseChunkX, baseChunkZ, data, blockX, blockY, blockZ);
				numBranches += rand.nextInt(4);
			}
			for (int l1 = 0; l1 < numBranches; ++l1) {
				float f = rand.nextFloat() * 3.141593f * 2.0f;
				float f1 = (rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
				float initialRadius = rand.nextFloat() * 2.0f + rand.nextFloat();
				this.generateCave(rand.nextLong(), baseChunkX, baseChunkZ, data, blockX, blockY, blockZ, initialRadius, f, f1, 0, 0, 1.0);
			}
		}
	}
	protected void generateHubRoom(long seed, int baseChunkX, int baseChunkZ, short[] data, double blockX, double blockY, double blockZ) {
		this.generateCave(seed, baseChunkX, baseChunkZ, data, blockX, blockY, blockZ, 1.0f + rand.nextFloat() * 6.0f, 0.0f, 0.0f, -1, -1, 0.5);
	}
	protected void generateCave(long seed, int baseChunkX, int baseChunkZ, short[] data, double blockX, double blockY, double blockZ, float initialRadius, float rotHor, float rotVer, int startPos, int endPos, double heightMod) {
		boolean sharpRotVer;
		double chunkMiddleX = baseChunkX * 16 + 8;
		double chunkMiddleZ = baseChunkZ * 16 + 8;
		float rotHorOffset = 0.0f;
		float rotVerOffset = 0.0f;
		Random random = new Random(seed);
		if (endPos <= 0) {
			int maxLength = this.radiusChunk * 16 - 16;
			endPos = maxLength - random.nextInt(maxLength / 4);
		}
		boolean noBranches = false;
		if (startPos == -1) {
			startPos = endPos / 2;
			noBranches = true;
		}
		int branchPos = random.nextInt(endPos / 2) + endPos / 4;
		sharpRotVer = random.nextInt(6) == 0;
		while (startPos < endPos) {
			double width = 1.5 + (double)(MathHelper.sin((float)startPos * 3.141593f / (float)endPos) * initialRadius * 1.0f);
			double height = width * heightMod;
			float xzScale = MathHelper.cos(rotVer);
			float yOffset = MathHelper.sin(rotVer);
			blockX += MathHelper.cos(rotHor) * xzScale;
			blockY += yOffset;
			blockZ += MathHelper.sin(rotHor) * xzScale;
			if (sharpRotVer) {
				rotVer *= 0.92f;
			} else {
				rotVer *= 0.7f;
			}
			rotVer += rotVerOffset * 0.1f;
			rotHor += rotHorOffset * 0.1f;
			rotVerOffset *= 0.9f;
			rotHorOffset *= 0.75f;
			rotVerOffset += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0f;
			rotHorOffset += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0f;
			if (!noBranches && startPos == branchPos && initialRadius > 1.0f) {
				this.generateCave(random.nextLong(), baseChunkX, baseChunkZ, data, blockX, blockY, blockZ, random.nextFloat() * 0.5f + 0.5f, rotHor - 1.570796f, rotVer / 3.0f, startPos, endPos, 1.0);
				this.generateCave(random.nextLong(), baseChunkX, baseChunkZ, data, blockX, blockY, blockZ, random.nextFloat() * 0.5f + 0.5f, rotHor + 1.570796f, rotVer / 3.0f, startPos, endPos, 1.0);
				return;
			}
			if (noBranches || random.nextInt(4) != 0) {
				double dxFromMiddle = blockX - chunkMiddleX;
				double dzFromMiddle = blockZ - chunkMiddleZ;
				double length = endPos - startPos;
				double maxRadius = initialRadius + 2.0f + 16.0f;
				if (dxFromMiddle * dxFromMiddle + dzFromMiddle * dzFromMiddle - length * length > maxRadius * maxRadius) {
					return;
				}
				if (!(blockX < chunkMiddleX - 16.0 - width * 2.0 || blockZ < chunkMiddleZ - 16.0 - width * 2.0 || blockX > chunkMiddleX + 16.0 + width * 2.0 || blockZ > chunkMiddleZ + 16.0 + width * 2.0)) {
					int x;
					int minX = MathHelper.floor_double(blockX - width) - baseChunkX * 16 - 1;
					int maxX = MathHelper.floor_double(blockX + width) - baseChunkX * 16 + 1;
					int minY = MathHelper.floor_double(blockY - height) - 1;
					int maxY = MathHelper.floor_double(blockY + height) + 1;
					int minZ = MathHelper.floor_double(blockZ - width) - baseChunkZ * 16 - 1;
					int maxZ = MathHelper.floor_double(blockZ + width) - baseChunkZ * 16 + 1;
					if (minX < 0) {
						minX = 0;
					}
					if (maxX > 16) {
						maxX = 16;
					}
					if (minY < 1) {
						minY = 1;
					}
					if (maxY > this.worldObj.getHeightBlocks() - 8) {
						maxY = this.worldObj.getHeightBlocks() - 8;
					}
					if (minZ < 0) {
						minZ = 0;
					}
					if (maxZ > 16) {
						maxZ = 16;
					}
					boolean hasHitWater = false;
					for (x = minX; !hasHitWater && x < maxX; ++x) {
						for (int z = minZ; !hasHitWater && z < maxZ; ++z) {
							for (int y = maxY + 1; !hasHitWater && y >= minY - 1; --y) {
								int index = (x * 16 + z) * this.worldObj.getHeightBlocks() + y;
								int blockId = data[index] & 0xFFFF;
								if (y >= this.worldObj.getHeightBlocks()) continue;
								if (Block.hasTag(blockId, BlockTags.IS_WATER)) {
									hasHitWater = true;
								}
								if (y == minY - 1 || x == minX || x == maxX - 1 || z == minZ || z == maxZ - 1) continue;
								y = minY;
							}
						}
					}
					if (!hasHitWater) {
						for (x = minX; x < maxX; ++x) {
							double xPercentage = ((double)(x + baseChunkX * 16) + 0.5 - blockX) / width;
							for (int z = minZ; z < maxZ; ++z) {
								double zPercentage = ((double)(z + baseChunkZ * 16) + 0.5 - blockZ) / width;
								int index = (x * 16 + z) * this.worldObj.getHeightBlocks() + maxY;
								boolean replaceTopBlock = false;
								if (xPercentage * xPercentage + zPercentage * zPercentage >= 1.0) continue;
								for (int y = maxY - 1; y >= minY; --y) {
									double yPercentage = ((double)y + 0.5 - blockY) / height;
									if (yPercentage > -0.7 && xPercentage * xPercentage + yPercentage * yPercentage + zPercentage * zPercentage < 1.0) {
										int blockId = data[index] & 0xFFFF;
										if (Block.hasTag(blockId, BlockTags.CAVE_GEN_REPLACES_SURFACE)) {
											replaceTopBlock = true;
										}
										if (Block.hasTag(blockId, BlockTags.CAVES_CUT_THROUGH)) {
											if (y < 10) {
												data[index] = (short)Block.fluidLavaStill.id;
											} else {
												data[index] = 0;
												if (replaceTopBlock) {
													int id = data[index - 1] & 0xFFFF;
													if (id == Block.dirt.id) {
														data[index - 1] = this.isAlphaWorldType ? (short)Block.grassRetro.id : (short)Block.grass.id;
													} else if (id == Block.dirtScorched.id) {
														data[index - 1] = (short)Block.grassScorched.id;
													}
												}
											}
										}
									}
									--index;
								}
							}
						}
						if (noBranches) break;
					}
				}
			}
			++startPos;
		}
	}
}
