package useless.terrainapi.initialization;

public abstract class BaseInitialization {
	private boolean hasInitialized = false;
	public void init() {
		if (hasInitialized) return;
		hasInitialized = true;
		initValues();
		initStructure();
		initOre();
		initRandom();
		initBiome();
	}
	protected abstract void initValues();
	protected abstract void initStructure();
	protected abstract void initOre();
	protected abstract void initRandom();
	protected abstract void initBiome();
}
