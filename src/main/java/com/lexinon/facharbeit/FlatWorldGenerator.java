package com.lexinon.facharbeit;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class FlatWorldGenerator implements IWorldGenerator {

    private int worldWidthX = 128, worldWidthZ = 128;
    private int worldHeight = 64;
    private boolean placeTrees = true;
    private boolean placeDecorations = true;

    private Camera camera;
    private int spawnX, spawnZ;

    @Override
    public Octree generate(int depth, int edgeLengthExponent, Game game) {
        if(camera != null)
            camera.setEye(new Vector3f(spawnX, 0, spawnZ));

        Octree octree = new Octree(depth, edgeLengthExponent, game);
        for(int x = 0; x < worldWidthX; x++) {
            for(int z = 0; z < worldWidthZ; z++) {
                octree.addVoxel(new Vector3i(x - worldWidthX / 2, worldHeight - 1 - worldHeight / 2, z - worldWidthZ / 2), Material.GRASS.getId());
                for(int y = 0; y < worldHeight - 1; y++) {
                    octree.addVoxel(new Vector3i(x - worldWidthX / 2, y - worldHeight / 2, z - worldWidthZ / 2), Material.DIRT.getId());
                }
            }
        }

        camera.setEye(new Vector3f(spawnX, worldHeight, spawnZ));
        return octree;
    }

    @Override
    public FlatWorldGenerator setCameraToTerrainHeight(int x, int z, Camera camera) {
        this.camera = camera;
        spawnX = x;
        spawnZ = z;
        return this;
    }

    public void setPlaceTrees(boolean placeTrees) {
        this.placeTrees = placeTrees;
    }

    public void setPlaceDecorations(boolean placeDecorations) {
        this.placeDecorations = placeDecorations;
    }

    public FlatWorldGenerator setWorldSize(int worldWidthX, int worldHeight, int worldWidthZ) {
        this.worldWidthX = worldWidthX;
        this.worldHeight = worldHeight;
        this.worldWidthZ = worldWidthZ;
        return this;
    }

}
