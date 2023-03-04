package com.lexinon.facharbeit;

import org.joml.Vector3i;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.lwjgl.opengl.GL20C.*;

/**
 * The {@code Octree} class represents the whole octree and furthermore contains some important configurations for the
 * construction of the octree. (See {@link IOctreeNode} for a more generalized explanation for the most important
 * methods of this class)
 */
public class Octree implements IOctreeParentNode {

    private IOctreeNode rootNode = new OctreeEmptyLeafNode();
    private final int depth;
    private final int leafNodeArrayEdgeLengthExponent;
    private final int worldEdgeLengthHalf;
    private final Game game;
    private Queue<OctreeNonEmptyLeafNode> updateMeshQueue = new LinkedBlockingQueue<>();

    public Octree(int depth, int edgeLengthExponent, Game game) {
        Metrics.switchBenchmarkProfile(BenchmarkProfile.TRAVERSING_TREE);
        this.depth = depth;
        this.leafNodeArrayEdgeLengthExponent = edgeLengthExponent;
        worldEdgeLengthHalf = 1 << (depth + edgeLengthExponent - 1);
        this.game = game;
        Metrics.incrementNumInnerOctreeNodes();
        Metrics.switchBenchmarkProfile(BenchmarkProfile.OTHER);
    }

    public void render() {
        game.getVoxelShader().use();
        glUniform1i(game.getVoxelShader().getTextureAtlasLoc(), 0);
        glUniform4f(game.getVoxelShader().getFogColorLoc(), 0.74609375f, 0.9140625f, 0.95703125f, 1f);
        Metrics.switchBenchmarkProfile(BenchmarkProfile.TRAVERSING_TREE);
        rootNode.render(-(1 << (depth + leafNodeArrayEdgeLengthExponent - 1)), -(1 << (depth + leafNodeArrayEdgeLengthExponent - 1)), -(1 << (depth + leafNodeArrayEdgeLengthExponent - 1)), 1 << (depth + leafNodeArrayEdgeLengthExponent), this);
        Metrics.switchBenchmarkProfile(BenchmarkProfile.OTHER);
    }

    public void addVoxel(Vector3i pos, short material) {
        Metrics.switchBenchmarkProfile(BenchmarkProfile.TRAVERSING_TREE);
        if(!(pos.x < worldEdgeLengthHalf && pos.x >= -worldEdgeLengthHalf && pos.y < worldEdgeLengthHalf && pos.y >= -worldEdgeLengthHalf && pos.z < worldEdgeLengthHalf && pos.z >= -worldEdgeLengthHalf))
            return;
        int x = (pos.x * (1 << (32 - leafNodeArrayEdgeLengthExponent - depth))) ^ 0x80000000;
        int y = (pos.y * (1 << (32 - leafNodeArrayEdgeLengthExponent - depth))) ^ 0x80000000;
        int z = (pos.z * (1 << (32 - leafNodeArrayEdgeLengthExponent - depth))) ^ 0x80000000;
        rootNode = rootNode.addVoxel(new Vector3i(x, y, z), material, depth, this, this);
        Metrics.switchBenchmarkProfile(BenchmarkProfile.OTHER);
    }

    public void removeVoxel(Vector3i pos) {
        Metrics.switchBenchmarkProfile(BenchmarkProfile.TRAVERSING_TREE);
        if(!(pos.x < worldEdgeLengthHalf && pos.x >= -worldEdgeLengthHalf && pos.y < worldEdgeLengthHalf && pos.y >= -worldEdgeLengthHalf && pos.z < worldEdgeLengthHalf && pos.z >= -worldEdgeLengthHalf))
            return;
        int x = (pos.x * (1 << (32 - leafNodeArrayEdgeLengthExponent - depth))) ^ 0x80000000;
        int y = (pos.y * (1 << (32 - leafNodeArrayEdgeLengthExponent - depth))) ^ 0x80000000;
        int z = (pos.z * (1 << (32 - leafNodeArrayEdgeLengthExponent - depth))) ^ 0x80000000;
        rootNode = rootNode.removeVoxel(new Vector3i(x, y, z), this, this);
        Metrics.switchBenchmarkProfile(BenchmarkProfile.OTHER);
    }

    public void deleteEverything() {
        Metrics.switchBenchmarkProfile(BenchmarkProfile.TRAVERSING_TREE);
        rootNode.deleteEverything();
        Metrics.switchBenchmarkProfile(BenchmarkProfile.OTHER);
    }

    public int getDepth() {
        return depth;
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
        Metrics.switchBenchmarkProfile(BenchmarkProfile.GENERATING_MESH);
        updateMeshQueue.forEach(node -> node.updateMesh(doOcclusionTest(), game.getMeshBuilder()));
        updateMeshQueue.clear();
        Metrics.switchBenchmarkProfile(BenchmarkProfile.OTHER);
    }

}
