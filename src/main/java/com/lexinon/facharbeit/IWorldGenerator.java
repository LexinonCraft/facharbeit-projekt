package com.lexinon.facharbeit;

public interface IWorldGenerator {

    Octree generate(int depth, int edgeLengthExponent, Game game);

}
