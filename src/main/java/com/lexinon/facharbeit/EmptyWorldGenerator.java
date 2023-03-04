package com.lexinon.facharbeit;

import org.joml.Vector3f;

public class EmptyWorldGenerator implements IWorldGenerator {

    private Camera camera;
    private int spawnX, spawnZ;

    @Override
    public Octree generate(int depth, int edgeLengthExponent, Game game) {
        if(camera != null)
            camera.setEye(new Vector3f(spawnX, 0, spawnZ));
        return new Octree(depth, edgeLengthExponent, game);
    }

    @Override
    public EmptyWorldGenerator setCameraToTerrainHeight(int x, int z, Camera camera) {
        this.camera = camera;
        spawnX = x;
        spawnZ = z;
        return this;
    }
}
