package com.lexinon.facharbeit;

import org.joml.Vector3i;

public interface IOctreeNode {

    public static final int ONLY_MOST_SIGNIFICANT_BIT_MASK = 0x80000000;

    void render();
    IOctreeNode addVoxel(Vector3i pos, short material, int remainingDepth, IOctreeParentNode parentNode, Octree octree);
    IOctreeNode removeVoxel(Vector3i pos, IOctreeParentNode parentNode, Octree octree);

}
