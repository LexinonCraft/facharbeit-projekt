package com.lexinon.facharbeit;

import org.joml.Vector3f;
import org.joml.Vector3i;

public interface IOctreeNode {

    void render(Vector3i origin, int volumeEdgeLength, Octree octree);
    IOctreeNode addVoxel(Vector3i pos, short material, int remainingDepth, IOctreeParentNode parentNode, Octree octree);
    IOctreeNode removeVoxel(Vector3i pos, IOctreeParentNode parentNode, Octree octree);

    void print(int depth);

}
