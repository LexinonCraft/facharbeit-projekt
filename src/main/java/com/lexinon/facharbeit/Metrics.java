package com.lexinon.facharbeit;

public class Metrics {

    private static int numOctreeNodes = 0;
    private static int numVoxelArrays = 0;
    private static long numTriangles = 0;

    private static float fps;
    private static int frameCounter;
    private static long lastFrameCounterReset;

    public static void incrementNumOctreeNodes() {
        numOctreeNodes++;
    }

    public static void decrementNumOctreeNodes() {
        numOctreeNodes--;
    }

    public static void incrementNumVoxelArrays() {
        numVoxelArrays++;
    }

    public static void decrementNumVoxelArrays() {
        numVoxelArrays--;
    }

    public static void increaseNumTriangles(long num) {
        numTriangles += num;
    }

    public static void decreaseNumTriangles(long num) {
        numTriangles -= num;
    }

    public static int getNumOctreeNodes() {
        return numOctreeNodes;
    }

    public static int getNumVoxelArrays() {
        return numVoxelArrays;
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
