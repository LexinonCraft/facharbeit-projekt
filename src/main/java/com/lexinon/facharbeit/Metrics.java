package com.lexinon.facharbeit;

import org.openjdk.jol.info.ClassLayout;

/**
 * The {@code Metrics} is - among other things - responsible for keeping track of the amount of living objects in memory
 * and holding a reference on a {@link Benchmark} object.
 */
public class Metrics {

    public static final long INNER_OCTREE_NODE_INSTANCE_SIZE = ClassLayout.parseClass(InnerOctreeNode.class).instanceSize();
    public static final long EMPTY_OCTREE_LEAF_NODE_INSTANCE_SIZE = ClassLayout.parseClass(OctreeNonEmptyLeafNode.class).instanceSize();
    public static final long NON_EMPTY_OCTREE_LEAF_NODE_INSTANCE_SIZE = ClassLayout.parseClass(OctreeNonEmptyLeafNode.class).instanceSize();
    public static final long MESH_INSTANCE_SIZE = ClassLayout.parseClass(Mesh.class).instanceSize();

    private static long numInnerOctreeNodes = 0;
    private static long numEmptyOctreeLeafNodes = 0;
    private static long numVoxelArrays = 0;
    private static long numNonEmptyVoxels = 0;
    private static long numTriangles = 0;

    private static float fps;
    private static int frameCounter;
    private static long lastFrameCounterReset;

    private static Benchmark benchmark;

    public static void incrementNumInnerOctreeNodes() {
        numInnerOctreeNodes++;
    }

    public static void decrementNumInnerOctreeNodes() {
        numInnerOctreeNodes--;
    }

    public static void incrementNumEmptyOctreeLeafNodes() {
        numEmptyOctreeLeafNodes++;
    }

    public static void decrementNumEmptyOctreeLeafNodes() {
        numEmptyOctreeLeafNodes--;
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

    public static long getNumInnerOctreeNodes() {
        return numInnerOctreeNodes;
    }

    public static long getNumEmptyOctreeLeafNodes() {
        return numEmptyOctreeLeafNodes;
    }

    public static long getNumVoxelArrays() {
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

    public static void startBenchmark(Window window, Config config, float duration) {
        benchmark = new Benchmark(window, config, numInnerOctreeNodes, numEmptyOctreeLeafNodes, numVoxelArrays, numNonEmptyVoxels, numTriangles, (long) (duration * 1_000_000_000L));
    }

    public static void startBenchmark(Window window, Config config) {
        startBenchmark(window, config, -1);
    }

    public static void frameTime(long frameTime) {
        if(benchmark == null)
            return;
        if(benchmark.frameTime(frameTime))
            endBenchmark();
    }

    public static void endBenchmark() {
        benchmark.end(numInnerOctreeNodes, numEmptyOctreeLeafNodes, numVoxelArrays, numNonEmptyVoxels, numTriangles);
        benchmark = null;
    }

    public static void switchBenchmarkProfile(BenchmarkProfile profile) {
        if(benchmark != null)
            benchmark.switchProfile(profile);
    }

    public static boolean isBenchmarkRunning() {
        return benchmark != null;
    }

}
