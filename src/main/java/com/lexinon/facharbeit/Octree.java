package com.lexinon.facharbeit;

import org.joml.Vector3i;

public class Octree implements IOctreeParentNode {

    private IOctreeNode rootNode = new OctreeEmptyLeafNode();
    private final int depth;
    private final int edgeLengthExponent;

    public Octree(int depth, int edgeLengthExponent) {
        this.depth = depth;
        this.edgeLengthExponent = edgeLengthExponent;
    }

    public void render() {
        rootNode.render();
    }

    public void addVoxel(Vector3i pos, short material) {
        rootNode = rootNode.addVoxel(pos.mul(1 << 32 - edgeLengthExponent), material, depth, this, this);
    }

    public void removeVoxel(Vector3i pos, InnerOctreeNode parentNode, Octree octree) {
        rootNode = rootNode.removeVoxel(pos, this, this);
    }

    public int getEdgeLengthExponent() {
        return edgeLengthExponent;
    }

    public boolean doOcclusionTest() {
        return false;
    }

    @Override
    public void incrementNonEmptySubtreesCount() {
        // Nothing
    }

    @Override
    public void decrementNonEmptySubtreesCount() {
        // Nothing
    }
}
