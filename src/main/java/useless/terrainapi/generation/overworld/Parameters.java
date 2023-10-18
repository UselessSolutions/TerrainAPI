package useless.terrainapi.generation.overworld;

import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;

import java.lang.reflect.Array;
import java.util.Random;

public enum Parameters {
	BIOME(0),
	RANDOM(1),
	CHUNK(2),
	DECORATOR(3);
	private static final int biggestID = 3;
	public final int id;
	private Parameters(int id){
		this.id = id;
	}
	public static Biome getBiome(Object[] parameters){
		return (Biome) parameters[BIOME.id];
	}
	public static Random getRandom(Object[] parameters){
		return (Random) parameters[RANDOM.id];
	}
	public static Chunk getChunk(Object[] parameters){
		return (Chunk) parameters[CHUNK.id];
	}
	public static ChunkDecorator getDecorator(Object[] parameters){
		return (ChunkDecorator) parameters[DECORATOR.id];
	}

	/**
	 * @param parameters Object[] with preloaded parameters
	 * @param customIndex index into additional parameters, starts at index 1
	 * @return Returns the selected custom parameter
	 */
	public static Object getCustomParameter(Object[] parameters, int customIndex){
		if (customIndex < 1) {
			throw new NullPointerException("Custom Index must start from index 1 not 0!");
		}
		return parameters[biggestID + customIndex];
	}
	public static Object[] packParameters(Biome biome, Random random, Chunk chunk, ChunkDecorator decorator, Object[] customParameters){
		Object[] parameters = new Object[]{biome, random, chunk, decorator};
		if (customParameters != null){
			parameters = concatenate(parameters, customParameters);
		}
		return parameters;
	}
	public static <T> T[] concatenate(T[] a, T[] b) {
		int aLen = a.length;
		int bLen = b.length;

		@SuppressWarnings("unchecked")
		T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}
}
