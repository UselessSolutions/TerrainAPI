# TerrainAPI

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
