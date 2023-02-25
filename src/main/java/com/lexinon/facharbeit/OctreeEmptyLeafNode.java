package com.lexinon.facharbeit;

import org.joml.Vector3i;

public class OctreeEmptyLeafNode implements IOctreeNode {

    @Override
    public void render(Vector3i origin, int volumeEdgeLength, Octree octree) {
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

    @Override
    public void print(int depth) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < depth; i++) {
            stringBuilder.append(".");
        }
        String tabs = stringBuilder.toString();
        System.out.println(tabs + "Empty");
    }

}
