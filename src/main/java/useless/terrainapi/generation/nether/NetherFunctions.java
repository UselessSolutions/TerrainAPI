package useless.terrainapi.generation.nether;

import useless.terrainapi.generation.Parameters;

public class NetherFunctions {
	/**Vanilla Nether fire density
	 * @param parameters Parameters Container
	 * @return Amount of fire per chunk
	 */
	public static int netherFireDensity(Parameters parameters){
		return parameters.random.nextInt(parameters.random.nextInt(10) + 1);
	}
}
