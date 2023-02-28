package com.lexinon.facharbeit;

public interface IWorldGenerator {

    Octree generate(int depth, int edgeLengthExponent, Game game);
    IWorldGenerator setCameraToTerrainHeight(int x, int z, Camera camera);

}
