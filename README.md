# TerrainAPI

# About

TerrainAPI is a complete rewrite of the Chunk Decorator Classes in BTA, it allows for mods to easily add and modify world features and structures in BTA. TerrainAPI is also setup to allow lots of configurability to the user, allowing them to modify the world generation themselves.

Some user configurable things being:
- Tree densities
- Ore densities
- Ore spawn heights
- Lake densities
- Plant densities

## How to include TerrainAPI in a project
Add this in your `build.gradle`:
```groovy
repositories {
    ivy {
		url = "https://github.com/UselessSolutions"
		patternLayout {
			artifact "[organisation]/releases/download/v[revision]/[module]-[revision].jar"
			m2compatible = true
		}
		metadataSources { artifact() }
	}
}

dependencies {

    modImplementation "TerrainAPI:terrainapi:${project.terrain_api_version}"
   
}
```

Documentation Eventually! :)
