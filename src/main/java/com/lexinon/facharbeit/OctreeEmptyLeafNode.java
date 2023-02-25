package com.lexinon.facharbeit;

import org.joml.Vector3i;

public class OctreeEmptyLeafNode implements IOctreeNode {

    @Override
    public void render() {
        // Nothing...
    }

    @Override
    public IOctreeNode addVoxel(Vector3i pos, short material, int remainingDepth, IOctreeParentNode parentNode, Octree octree) {
        parentNode.incrementNonEmptySubtreesCount();
        if(remainingDepth > 0)
            return new InnerOctreeNode().addVoxel(pos, material, remainingDepth, parentNode, octree);
        else
            return new OctreeNonEmptyLeafNode(octree).addVoxel(pos, material, remainingDepth, parentNode, octree);
    }

    @Override
    public IOctreeNode removeVoxel(Vector3i pos, IOctreeParentNode parentNode, Octree octree) {
        return this;
    }
}
