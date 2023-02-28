package com.lexinon.facharbeit;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Config {

    private File f;

    private boolean doOcclusionTest = true;
    private int edgeLengthExponent = 4;
    private int depth = 4;
    private WorldType worldType = WorldType.TERRAIN;
    private int seed = 236;
    private int flatWorldHeight = 3;
    private boolean placeTrees = true;
    private boolean placeDecorations = true;

    private Config() {

    }

    public static Config read(File f) {
        Config config = new Config();
        config.f = f;
        config.read();
        return config;
    }

    private void read() {
        try {
            Scanner sc = new Scanner(f);
            while(sc.hasNext()) {
                read(sc.nextLine());
            }
        } catch(IOException e) {
            System.err.println("Warning: couldn't read from config file!");
        }
    }

    private void read(String line) {
        if(line.startsWith("occlusion-test="))
            doOcclusionTest = !line.substring(15).equals("false");
        else if(line.startsWith("edge-length-exponent="))
            edgeLengthExponent = Integer.parseInt(line.substring(21));
        else if(line.startsWith("octree-depth="))
            depth = Integer.parseInt(line.substring(13));
        else if(line.startsWith("world-type=")) {
            if(line.substring(11).equals("terrain"))
                worldType = WorldType.TERRAIN;
            else if(line.substring(11).equals("empty"))
                worldType = WorldType.EMPTY;
            else if(line.substring(11).equals("flat"))
                worldType = WorldType.FLAT;
        } else if(line.startsWith("seed="))
            seed = Integer.parseInt(line.substring(5));
        else if(line.startsWith("flat-world-height="))
            seed = Integer.parseInt(line.substring(18));
        else if(line.startsWith("place-trees="))
            placeTrees = !line.substring(12).equals("false");
        else if(line.startsWith("place-decorations="))
            placeDecorations = !line.substring(18).equals("false");
    }

    public boolean doOcclusionTest() {
        return doOcclusionTest;
    }

    public int getEdgeLengthExponent() {
        return edgeLengthExponent;
    }

    public int getDepth() {
        return depth;
    }

    public WorldType getWorldType() {
        return worldType;
    }

    public int getSeed() {
        return seed;
    }

    public int getFlatWorldHeight() {
        return flatWorldHeight;
    }

    public boolean placeTrees() {
        return placeTrees;
    }

    public boolean placeDecorations() {
        return placeDecorations;
    }

}
