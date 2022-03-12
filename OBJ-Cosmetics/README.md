# Obj Cosmetics

### First you need a OBJ loader that was made by Blunderchpis

### https://github.com/Blunderchips/LWJGL-OBJ-Loader

## How to use:

### first create a new instance of the OBJLoader class in your Main

````java 
private OBJLoader objLoader;

public void startup() {
    objLoader = new OBJLoader();
}

public OBJLoader getObjLoader() {
    return objLoader;
}
````

### then you need to register your Cosmetics

````java 
private OBJLoader objLoader;

public void startup() {
    objLoader = new OBJLoader();

    CosmeticsManager.registerCosmetic(new Bandana());
}

public OBJLoader getObjLoader() {
    return objLoader;
}
````
