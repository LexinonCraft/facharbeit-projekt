package com.lexinon.facharbeit;

/**
 * Places voxels in the world with a specific pattern.
 */
public interface IWorldGenerator {

    Octree generate(int depth, int edgeLengthExponent, Game game);
    IWorldGenerator setCameraToTerrainHeight(int x, int z, Camera camera);

}
