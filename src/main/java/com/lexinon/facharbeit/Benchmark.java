package com.lexinon.facharbeit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private BenchmarkMode benchmarkMode = BenchmarkMode.OTHER;
    private long lastTime;
    private long
            timeTraversingTree,
            timeDrawCall;

    public Benchmark(Window window, Config config, long innerOctreeNodes, long emptyOctreeLeafNodes, long voxelArrays, long nonEmptyVoxels, long triangles, long duration) {
        this.window = window;
        this.config = config;
        this.beforeInnerOctreeNodes = innerOctreeNodes;
        this.beforeEmptyOctreeLeafNodes = emptyOctreeLeafNodes;
        this.beforeVoxelArrays = voxelArrays;
        this.beforeNonEmptyVoxels = nonEmptyVoxels;
        this.beforeTriangles = triangles;
        this.duration = duration;

        window.lockSize();
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

    public void switchMode(BenchmarkMode newMode) {
        long currentTime = System.nanoTime();
        long delta = currentTime - lastTime;

        switch(benchmarkMode) {
            case TRAVERSING_TREE -> timeTraversingTree += delta;
            case DRAW_CALL -> timeDrawCall += delta;
        }

        benchmarkMode = newMode;
        lastTime = currentTime;
    }

    public void end(long innerOctreeNodes, long emptyOctreeLeafNodes, long voxelArrays, long nonEmptyVoxels, long triangles) {
        switchMode(BenchmarkMode.OTHER);
        if(currentFrameTimesArrayIndex != 0)
            frameTimes.add(currentFrameTimesArray);

        float[] fullFrameTimesArray = generateFullFrameTimesArray();
        Util.bubbleSort(fullFrameTimesArray);

        float sum = 0;
        for(int i = 0; i < fullFrameTimesArray.length; i++) {
            sum += fullFrameTimesArray[i];
        }
        float average = sum / fullFrameTimesArray.length;

        float firstPercentile = fullFrameTimesArray[fullFrameTimesArray.length / 100 * 99];

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        Date now = new Date();
        try {
            new File("benchmarks").mkdir();
            File f = new File(String.format("benchmarks/Benchmark-%s.txt", formatter.format(now)));
            f.createNewFile();
            FileWriter fw = new FileWriter(f);

            fw.write(window.getFramebufferWidth() + "\n");
            fw.write(window.getFramebufferHeight() + "\n");
            fw.write(timeElapsed / 1_000_000_000f + "\n");

            fw.write(config.getDepth() + "\n");
            fw.write(config.getEdgeLengthExponent() + "\n");
            fw.write(config.getWorldType().toString() + "\n");
            fw.write(config.getSeed() + "\n");

            fw.write(fullFrameTimesArray.length + "\n");
            fw.write(average + "\n");
            fw.write(firstPercentile + "\n");

            fw.write(beforeInnerOctreeNodes + "\n");
            fw.write(beforeEmptyOctreeLeafNodes + "\n");
            fw.write(beforeVoxelArrays + "\n");
            fw.write(beforeNonEmptyVoxels + "\n");
            fw.write(beforeTriangles + "\n");

            fw.write(innerOctreeNodes + "\n");
            fw.write(emptyOctreeLeafNodes + "\n");
            fw.write(voxelArrays + "\n");
            fw.write(nonEmptyVoxels + "\n");
            fw.write(triangles + "\n");

            fw.write(timeTraversingTree / 1_000_000_000f / fullFrameTimesArray.length + "\n");
            fw.write(timeDrawCall / 1_000_000_000f / fullFrameTimesArray.length + "\n");

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
