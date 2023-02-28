package com.lexinon.facharbeit;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class TerrainGenerator implements IWorldGenerator {

    private Octree octree;
    private int maxSideLength;
    private PerlinNoiseGenerator heightMapGenerator;
    private float[] heightMap;
    private PerlinNoiseGenerator layerMapGenerator;
    private float[] layerMap;

    private int worldWidthX = 128, worldWidthZ = 128;
    private final int worldHeight = 64;
    private int waterHeight = 64;
    private int seed = 0;
    private boolean placeTrees = true;
    private boolean placeDecorations = true;

    private Camera camera;
    private int spawnX, spawnZ;

    public TerrainGenerator() {}

    @Override
    public Octree generate(int depth, int edgeLengthExponent, Game game) {
        if(octree != null)
            return octree;
        octree = new Octree(depth, edgeLengthExponent, game);

        maxSideLength = Math.max(worldWidthX, worldWidthZ);

        heightMapGenerator = new PerlinNoiseGenerator(seed + Util.hash(0), maxSideLength)
                .noise(2f * maxSideLength / 1024f, 4f)
                .noise(4f * maxSideLength / 1024f, 2f)
                .noise(8f * maxSideLength / 1024f, 1f)
                .noise(16f * maxSideLength / 1024f, 0.5f)
                .noise(32f * maxSideLength / 1024f, 0.25f);

        heightMapGenerator.normalizeTexture();
        heightMap = heightMapGenerator.getTexture();

        for(int i = 0; i < heightMap.length; i++) {
            heightMap[i] = (int) (Math.pow(heightMap[i] * 1.2 + 1.2, 3) * worldHeight);
        }

        layerMapGenerator = new PerlinNoiseGenerator(seed + Util.hash(1), maxSideLength)
                .noise(16f, 1f);

        layerMapGenerator.normalizeTexture();
        layerMap = layerMapGenerator.getTexture();

        for(int i = 0; i < heightMap.length; i++) {
            int terrainHeight = (int) heightMap[i];
            int x = Util.getXPosByIndex(i, maxSideLength);
            int z = Util.getYPosByIndex(i, maxSideLength);

            for(int y = waterHeight; y > terrainHeight; y--) {
                octree.addVoxel(new Vector3i(x - worldWidthX / 2, y - worldHeight / 2, z - worldWidthZ / 2), Material.WATER.getId());
            }
            for(int y = terrainHeight; y >  -worldHeight / 8; y--) {
               if(y >= waterHeight + 10 * layerMap[i]) {
                    if(y == terrainHeight)
                        octree.addVoxel(new Vector3i(x - worldWidthX / 2, y - worldHeight / 2, z - worldWidthZ / 2), Material.GRASS.getId());
                    else if(y > terrainHeight - 2)
                        octree.addVoxel(new Vector3i(x - worldWidthX / 2, y - worldHeight / 2, z - worldWidthZ / 2), Material.DIRT.getId());
                    else
                        octree.addVoxel(new Vector3i(x - worldWidthX / 2, y - worldHeight / 2, z - worldWidthZ / 2), Material.TEST_STONE.getId());
                } else {
                    octree.addVoxel(new Vector3i(x - worldWidthX / 2, y - worldHeight / 2, z - worldWidthZ / 2), Material.SAND.getId());
                }
            }
        }

        if(placeDecorations)
            placeDecorations();
        if(placeTrees)
            placeTrees();

        if(camera != null) {
            int spawnTerrainHeight = (int) heightMap[Util.getIndexByPos(spawnX + maxSideLength / 2, spawnZ + maxSideLength / 2, maxSideLength)] - worldHeight / 2;
            camera.setEye(new Vector3f(spawnX, spawnTerrainHeight + 15, spawnZ));
        }

        return octree;
    }

    private void placeTrees() {
        for(int i = 0; i < maxSideLength * maxSideLength / 32; i++) {
            int x = Math.abs(Util.hash(1 + Util.hash(i))) % maxSideLength;
            int z = Math.abs(Util.hash(2 + Util.hash(i))) % maxSideLength;
            int terrainHeight = (int) heightMap[Util.getIndexByPos(x, z, maxSideLength)];
            if(terrainHeight <= waterHeight)
                continue;
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2, z - maxSideLength / 2), Material.LOG.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2 + 1, z - maxSideLength / 2), Material.LOG.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2 + 2, z - maxSideLength / 2), Material.LOG.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2 + 3, z - maxSideLength / 2), Material.LOG.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2 + 4, z - maxSideLength / 2), Material.LOG.getId());


            octree.addVoxel(new Vector3i(x - maxSideLength / 2 + 1, terrainHeight + 1 - worldHeight / 2 + 3, z - maxSideLength / 2), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2 + 1, terrainHeight + 1 - worldHeight / 2 + 3, z - maxSideLength / 2 + 1), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2 + 3, z - maxSideLength / 2 + 1), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2 - 1, terrainHeight + 1 - worldHeight / 2 + 3, z - maxSideLength / 2 + 1), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2 - 1, terrainHeight + 1 - worldHeight / 2 + 3, z - maxSideLength / 2), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2 - 1, terrainHeight + 1 - worldHeight / 2 + 3, z - maxSideLength / 2 - 1), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2 + 3, z - maxSideLength / 2 - 1), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2 + 1, terrainHeight + 1 - worldHeight / 2 + 3, z - maxSideLength / 2 - 1), Material.LEAVES.getId());

            octree.addVoxel(new Vector3i(x - maxSideLength / 2 + 1, terrainHeight + 1 - worldHeight / 2 + 4, z - maxSideLength / 2), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2 + 1, terrainHeight + 1 - worldHeight / 2 + 4, z - maxSideLength / 2 + 1), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2 + 4, z - maxSideLength / 2 + 1), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2 - 1, terrainHeight + 1 - worldHeight / 2 + 4, z - maxSideLength / 2 + 1), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2 - 1, terrainHeight + 1 - worldHeight / 2 + 4, z - maxSideLength / 2), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2 - 1, terrainHeight + 1 - worldHeight / 2 + 4, z - maxSideLength / 2 - 1), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2 + 4, z - maxSideLength / 2 - 1), Material.LEAVES.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2 + 1, terrainHeight + 1 - worldHeight / 2 + 4, z - maxSideLength / 2 - 1), Material.LEAVES.getId());

            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2 + 5, z - maxSideLength / 2), Material.LEAVES.getId());
        }
    }

    private void placeDecorations() {
        for(int i = 0; i < maxSideLength * maxSideLength / 512; i++) {
            int x = Math.abs(Util.hash(3 + Util.hash(i))) % maxSideLength;
            int z = Math.abs(Util.hash(4 + Util.hash(i))) % maxSideLength;
            int terrainHeight = (int) heightMap[Util.getIndexByPos(x, z, maxSideLength)];
            if(terrainHeight <= waterHeight)
                continue;
            int type = Math.abs(Util.hash(5 + Util.hash(i))) % 8;
            switch(type) {
                case 0 -> {
                    octree.removeVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight - worldHeight / 2, z - maxSideLength / 2));
                    octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight - worldHeight / 2, z - maxSideLength / 2), Material.FARMLAND.getId());
                }
                case 1 -> octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2, z - maxSideLength / 2), Material.HAY.getId());
                case 2 -> {
                    octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2, z - maxSideLength / 2), Material.CACTUS.getId());
                    octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 2 - worldHeight / 2, z - maxSideLength / 2), Material.CACTUS.getId());
                    octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 3 - worldHeight / 2, z - maxSideLength / 2), Material.CACTUS.getId());
                }
                case 3 -> octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2, z - maxSideLength / 2), Material.CRATE.getId());
                case 4 -> octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2, z - maxSideLength / 2), Material.CAKE.getId());
                case 5 -> octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2, z - maxSideLength / 2), Material.PUMPKIN.getId());
                case 6 -> octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2, z - maxSideLength / 2), Material.VINES.getId());
                case 7 -> octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2, z - maxSideLength / 2), Material.TANK.getId());
            }
        }
    }

    @Override
    public TerrainGenerator setCameraToTerrainHeight(int x, int z, Camera camera) {
        this.camera = camera;
        spawnX = x;
        spawnZ = z;
        return this;
    }

    public TerrainGenerator setWorldSize(int worldWidthX, int worldWidthZ) {
        this.worldWidthX = worldWidthX;
        this.worldWidthZ = worldWidthZ;
        return this;
    }

    public TerrainGenerator setWaterHeight(int waterHeight) {
        this.waterHeight = waterHeight;
        return this;
    }

    public TerrainGenerator setSeed(int seed) {
        this.seed = seed;
        return this;
    }

    public void setPlaceTrees(boolean placeTrees) {
        this.placeTrees = placeTrees;
    }

    public void setPlaceDecorations(boolean placeDecorations) {
        this.placeDecorations = placeDecorations;
    }

}
