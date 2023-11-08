package useless.terrainapi.generation.nether;

import useless.terrainapi.generation.Parameters;

public class NetherFunctions {
	public static int netherFireDensity(Parameters parameters){
		return parameters.random.nextInt(parameters.random.nextInt(10) + 1);
	}
}
