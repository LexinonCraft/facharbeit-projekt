package com.lexinon.facharbeit;

import org.joml.Vector3i;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.lwjgl.opengl.GL20C.*;

public class Octree implements IOctreeParentNode {

    private IOctreeNode rootNode = new OctreeEmptyLeafNode();
    private final int depth;
    private final int leafNodeArrayEdgeLengthExponent;
    private final int worldEdgeLengthHalf;
    private final Game game;
    private Queue<OctreeNonEmptyLeafNode> updateMeshQueue = new LinkedBlockingQueue<>();

    public Octree(int depth, int edgeLengthExponent, Game game) {
        this.depth = depth;
        this.leafNodeArrayEdgeLengthExponent = edgeLengthExponent;
        worldEdgeLengthHalf = 1 << (depth + edgeLengthExponent - 1);
        this.game = game;
        Metrics.incrementNumOctreeNodes();
    }

    public void render() {
        game.voxelShader.use();
        glUniform1i(game.voxelShader.getTextureAtlasLoc(), 0);
        glUniform4f(game.voxelShader.getFogColorLoc(), 0.74609375f, 0.9140625f, 0.95703125f, 1f);
        rootNode.render(-(1 << (depth + leafNodeArrayEdgeLengthExponent - 1)), -(1 << (depth + leafNodeArrayEdgeLengthExponent - 1)), -(1 << (depth + leafNodeArrayEdgeLengthExponent - 1)), 1 << (depth + leafNodeArrayEdgeLengthExponent), this);
    }

    public void addVoxel(Vector3i pos, short material) {
        if(!(pos.x < worldEdgeLengthHalf && pos.x >= -worldEdgeLengthHalf && pos.y < worldEdgeLengthHalf && pos.y >= -worldEdgeLengthHalf && pos.z < worldEdgeLengthHalf && pos.z >= -worldEdgeLengthHalf))
            return;
        int x = (pos.x * (1 << (32 - leafNodeArrayEdgeLengthExponent - depth))) ^ 0x80000000;
        int y = (pos.y * (1 << (32 - leafNodeArrayEdgeLengthExponent - depth))) ^ 0x80000000;
        int z = (pos.z * (1 << (32 - leafNodeArrayEdgeLengthExponent - depth))) ^ 0x80000000;
        rootNode = rootNode.addVoxel(new Vector3i(x, y, z), material, depth, this, this);
    }

    public void removeVoxel(Vector3i pos) {
        if(!(pos.x < worldEdgeLengthHalf && pos.x >= -worldEdgeLengthHalf && pos.y < worldEdgeLengthHalf && pos.y >= -worldEdgeLengthHalf && pos.z < worldEdgeLengthHalf && pos.z >= -worldEdgeLengthHalf))
            return;
        int x = (pos.x * (1 << (32 - leafNodeArrayEdgeLengthExponent - depth))) ^ 0x80000000;
        int y = (pos.y * (1 << (32 - leafNodeArrayEdgeLengthExponent - depth))) ^ 0x80000000;
        int z = (pos.z * (1 << (32 - leafNodeArrayEdgeLengthExponent - depth))) ^ 0x80000000;
        rootNode = rootNode.removeVoxel(new Vector3i(x, y, z), this, this);
    }

    public void deleteEverything() {
        rootNode.deleteEverything();
    }

    public int getLeafNodeArrayEdgeLengthExponent() {
        return leafNodeArrayEdgeLengthExponent;
    }

    public boolean doOcclusionTest() {
        return game.getConfig().doOcclusionTest();
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
        updateMeshQueue.forEach(node -> node.updateMesh(doOcclusionTest(), game.getMeshBuilder()));
        updateMeshQueue.clear();
    }

}
