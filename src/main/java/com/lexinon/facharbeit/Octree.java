package com.lexinon.facharbeit;

import org.joml.Vector3i;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Octree implements IOctreeParentNode {

    private IOctreeNode rootNode = new OctreeEmptyLeafNode();
    private final int depth;
    private final int edgeLengthExponent;
    private final Game game;
    private Queue<OctreeNonEmptyLeafNode> updateMeshQueue = new LinkedBlockingQueue<>();

    public Octree(int depth, int edgeLengthExponent, Game game) {
        this.depth = depth;
        this.edgeLengthExponent = edgeLengthExponent;
        this.game = game;
    }

    public void render() {
        rootNode.render(new Vector3i(-(1 << (depth + edgeLengthExponent - 1)), -(1 << (depth + edgeLengthExponent - 1)), -(1 << (depth + edgeLengthExponent - 1))), 1 << (depth + edgeLengthExponent), this);
    }

    public void addVoxel(Vector3i pos, short material) {
        int x = (pos.x * (1 << (32 - edgeLengthExponent - depth))) ^ 0x80000000;
        int y = (pos.y * (1 << (32 - edgeLengthExponent - depth))) ^ 0x80000000;
        int z = (pos.z * (1 << (32 - edgeLengthExponent - depth))) ^ 0x80000000;
        rootNode = rootNode.addVoxel(new Vector3i(x, y, z), material, depth, this, this);
    }

    public void removeVoxel(Vector3i pos) {
        rootNode = rootNode.removeVoxel(pos, this, this);
    }

    public int getEdgeLengthExponent() {
        return edgeLengthExponent;
    }

    public boolean doOcclusionTest() {
        return true;
    }

    @Override
    public void incrementNonEmptySubtreesCount() {
        // Nothing
    }

    @Override
    public void decrementNonEmptySubtreesCount() {
        // Nothing
    }

    public Game getGame() {
        return game;
    }

    public void print() {
        rootNode.print(0);
    }

    public void enqueueModifiedMesh(OctreeNonEmptyLeafNode node) {
        updateMeshQueue.add(node);
    }

    public void updateMeshs() {
        updateMeshQueue.forEach(node -> node.updateMesh(doOcclusionTest()));
        updateMeshQueue.clear();
    }

}
