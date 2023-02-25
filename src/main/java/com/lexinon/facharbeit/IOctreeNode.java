package com.lexinon.facharbeit;

import org.joml.Vector3i;

public interface IOctreeNode {

    void render();
    IOctreeNode addVoxel(Vector3i pos, short material, int remainingDepth, IOctreeParentNode parentNode, Octree octree);
    IOctreeNode removeVoxel(Vector3i pos, IOctreeParentNode parentNode, Octree octree);

}
