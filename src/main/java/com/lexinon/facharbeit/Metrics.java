package com.lexinon.facharbeit;

import org.openjdk.jol.info.ClassLayout;

public class Metrics {

    public static final long INNER_OCTREE_NODE_INSTANCE_SIZE = ClassLayout.parseClass(InnerOctreeNode.class).instanceSize();
    public static final long EMPTY_OCTREE_LEAF_NODE_INSTANCE_SIZE = ClassLayout.parseClass(OctreeNonEmptyLeafNode.class).instanceSize();
    public static final long NON_EMPTY_OCTREE_LEAF_NODE_INSTANCE_SIZE = ClassLayout.parseClass(OctreeNonEmptyLeafNode.class).instanceSize();
    public static final long MESH_INSTANCE_SIZE = ClassLayout.parseClass(Mesh.class).instanceSize();

    private static int numInnerOctreeNodes = 0;
    private static int numEmptyOctreeLeafNotes = 0;
    private static int numVoxelArrays = 0;
    private static long numNonEmptyVoxels = 0;
    private static long numTriangles = 0;

    private static float fps;
    private static int frameCounter;
    private static long lastFrameCounterReset;

    public static void incrementNumInnerOctreeNodes() {
        numInnerOctreeNodes++;
    }

    public static void decrementNumInnerOctreeNodes() {
        numInnerOctreeNodes--;
    }

    public static void incrementNumEmptyOctreeLeafNodes() {
        numEmptyOctreeLeafNotes++;
    }

    public static void decrementNumEmptyOctreeLeafNodes() {
        numEmptyOctreeLeafNotes--;
    }

    public static void incrementNumVoxelArrays() {
        numVoxelArrays++;
    }

    public static void decrementNumVoxelArrays() {
        numVoxelArrays--;
    }

    public static void incrementNumNonEmptyVoxels() {
        numNonEmptyVoxels++;
    }

    public static void decrementNumNonEmptyVoxels() {
        numNonEmptyVoxels--;
    }

    public static void decreaseNumNonEmptyVoxels(int num) {
        numNonEmptyVoxels -= num;
    }

    public static void increaseNumTriangles(int num) {
        numTriangles += num;
    }

    public static void decreaseNumTriangles(int num) {
        numTriangles -= num;
    }

    public static int getNumInnerOctreeNodes() {
        return numInnerOctreeNodes;
    }

    public static int getNumEmptyOctreeLeafNotes() {
        return numEmptyOctreeLeafNotes;
    }

    public static int getNumVoxelArrays() {
        return numVoxelArrays;
    }

    public static long getNumNonEmptyVoxels() {
        return numNonEmptyVoxels;
    }

    public static long getNumTriangles() {
        return numTriangles;
    }

    public static float getFps() {
        return fps;
    }

    public static void tick() {
        if(System.nanoTime() - lastFrameCounterReset > 1_000_000_000f) {
            fps = frameCounter;
            frameCounter = 0;
            lastFrameCounterReset = System.nanoTime();
        }
        frameCounter++;
    }

}
