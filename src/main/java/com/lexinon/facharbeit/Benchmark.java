package com.lexinon.facharbeit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Measure the application's performance over a certain time span and saves it into a file.
 */
public class Benchmark {

    private static final int FRAME_TIME_VALUES_PER_LIST_ENTRY = 200;

    private final Window window;
    private final Config config;
    private final long duration;
    private long timeElapsed = 0;
    private final long beforeInnerOctreeNodes, beforeEmptyOctreeLeafNodes, beforeVoxelArrays, beforeNonEmptyVoxels, beforeTriangles;
    private final List<float[]> frameTimes = new ArrayList<>();
    private float[] currentFrameTimesArray = new float[FRAME_TIME_VALUES_PER_LIST_ENTRY];
    private int currentFrameTimesArrayIndex = 0;

    private BenchmarkProfile benchmarkProfile = BenchmarkProfile.NONE;
    private long lastTime;
    private long
            timeTraversingTree,
            timeDrawCall,
            timeWaitingForGpu,
            timeGeneratingMesh,
            timeOther;

    public Benchmark(Window window, Config config, long innerOctreeNodes, long emptyOctreeLeafNodes, long voxelArrays, long nonEmptyVoxels, long triangles, long duration) {
        this.window = window;
        this.config = config;
        this.beforeInnerOctreeNodes = innerOctreeNodes;
        this.beforeEmptyOctreeLeafNodes = emptyOctreeLeafNodes;
        this.beforeVoxelArrays = voxelArrays;
        this.beforeNonEmptyVoxels = nonEmptyVoxels;
        this.beforeTriangles = triangles;
        this.duration = duration;
    }

    public boolean frameTime(long frameTime) {
        currentFrameTimesArray[currentFrameTimesArrayIndex] = frameTime / 1_000_000_000f;
        currentFrameTimesArrayIndex++;

        if(currentFrameTimesArrayIndex == FRAME_TIME_VALUES_PER_LIST_ENTRY) {
            frameTimes.add(currentFrameTimesArray);
            currentFrameTimesArray = new float[FRAME_TIME_VALUES_PER_LIST_ENTRY];
            currentFrameTimesArrayIndex = 0;
        }

        timeElapsed += frameTime;
        return duration >= 0 && timeElapsed > duration;
    }

    public void switchProfile(BenchmarkProfile newProfile) {
        long currentTime = System.nanoTime();
        long delta = currentTime - lastTime;

        switch(benchmarkProfile) {
            case TRAVERSING_TREE -> timeTraversingTree += delta;
            case DRAW_CALL -> timeDrawCall += delta;
            case WAITING_FOR_GPU -> timeWaitingForGpu += delta;
            case GENERATING_MESH -> timeGeneratingMesh += delta;
            case OTHER -> timeOther += delta;
        }

        benchmarkProfile = newProfile;
        lastTime = currentTime;
    }

    public void end(long innerOctreeNodes, long emptyOctreeLeafNodes, long voxelArrays, long nonEmptyVoxels, long triangles) {
        switchProfile(BenchmarkProfile.NONE);
        if(currentFrameTimesArrayIndex != 0)
            frameTimes.add(currentFrameTimesArray);

        float[] fullFrameTimesArray = generateFullFrameTimesArray();
        Util.bubbleSort(fullFrameTimesArray);

        float sumFrameTimes = 0;
        for(int i = 0; i < fullFrameTimesArray.length; i++) {
            sumFrameTimes += fullFrameTimesArray[i];
        }
        float averageFrameTime = sumFrameTimes / fullFrameTimesArray.length;

        float firstPercentileFrameTime = fullFrameTimesArray[fullFrameTimesArray.length / 100 * 99];

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        Date now = new Date();
        try {
            new File("benchmarks").mkdir();
            File f = new File(String.format("benchmarks/Benchmark-%s.txt", formatter.format(now)));
            f.createNewFile();
            FileWriter fw = new FileWriter(f);

            DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
            DecimalFormat df = new DecimalFormat("#", dfs);
            df.setMaximumFractionDigits(8);
            df.setMinimumIntegerDigits(1);
            df.setDecimalSeparatorAlwaysShown(false);

            fw.write(df.format(window.getFramebufferWidth()) + ",");
            fw.write(df.format(window.getFramebufferHeight()) + ",");
            fw.write(df.format(timeElapsed / 1_000_000_000f) + ",");

            fw.write(df.format(config.getDepth()) + ",");
            fw.write(df.format(config.getEdgeLengthExponent()) + ",");
            fw.write(config.getWorldType().toString() + ",");
            fw.write(df.format(config.getSeed()) + ",");

            fw.write(df.format(fullFrameTimesArray.length) + ",");
            fw.write(df.format(averageFrameTime) + ",");
            fw.write(df.format(firstPercentileFrameTime) + ",");

            fw.write(df.format(beforeInnerOctreeNodes) + ",");
            fw.write(df.format(beforeEmptyOctreeLeafNodes) + ",");
            fw.write(df.format(beforeVoxelArrays) + ",");
            fw.write(df.format(beforeNonEmptyVoxels) + ",");
            fw.write(df.format(beforeTriangles) + ",");

            fw.write(df.format(innerOctreeNodes) + ",");
            fw.write(df.format(emptyOctreeLeafNodes) + ",");
            fw.write(df.format(voxelArrays) + ",");
            fw.write(df.format(nonEmptyVoxels) + ",");
            fw.write(df.format(triangles) + ",");

            fw.write(df.format(Metrics.INNER_OCTREE_NODE_INSTANCE_SIZE) + ",");
            fw.write(df.format(Metrics.EMPTY_OCTREE_LEAF_NODE_INSTANCE_SIZE) + ",");
            fw.write(df.format(Metrics.NON_EMPTY_OCTREE_LEAF_NODE_INSTANCE_SIZE) + ",");
            fw.write(df.format(Metrics.MESH_INSTANCE_SIZE) + ",");
            fw.write(df.format(((1L << config.getEdgeLengthExponent()) * (1L << config.getEdgeLengthExponent()) * (1L << config.getEdgeLengthExponent())) + 4) + ",");

            fw.write(df.format(timeTraversingTree / 1_000_000_000f / fullFrameTimesArray.length) + ",");
            fw.write(df.format(timeDrawCall / 1_000_000_000f / fullFrameTimesArray.length) + ",");
            fw.write(df.format(timeWaitingForGpu / 1_000_000_000f / fullFrameTimesArray.length) + ",");
            fw.write(df.format(timeGeneratingMesh / 1_000_000_000f / fullFrameTimesArray.length) + ",");
            fw.write(df.format(timeOther / 1_000_000_000f / fullFrameTimesArray.length));

            fw.flush();
        } catch(IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private float[] generateFullFrameTimesArray() {
        int measuredFrameTimes = (frameTimes.size() - 1) * FRAME_TIME_VALUES_PER_LIST_ENTRY + currentFrameTimesArrayIndex;
        float[] fullFrameTimesArray = new float[measuredFrameTimes];
        int i = 0;
        for(float[] frameTimesArray : frameTimes) {
            for(int j = 0; j < FRAME_TIME_VALUES_PER_LIST_ENTRY; j++) {
                if(i < measuredFrameTimes) {
                    fullFrameTimesArray[i] = frameTimesArray[j];
                    i++;
                } else
                    break;
            }
        }
        return fullFrameTimesArray;
    }

}
