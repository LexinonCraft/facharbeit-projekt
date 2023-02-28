package com.lexinon.facharbeit;

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

    public TerrainGenerator() {}

    @Override
    public Octree generate(int depth, int edgeLengthExponent, Game game) {
        if(octree != null)
            return octree;
        octree = new Octree(depth, edgeLengthExponent, game);

        maxSideLength = Math.max(worldWidthX, worldWidthZ);

        heightMapGenerator = new PerlinNoiseGenerator(seed + Util.hash(0), maxSideLength)
                .noise(2f, 4f)
                .noise(4f, 2f)
                .noise(8f, 1f)
                .noise(16f, 0.5f)
                .noise(32f, 0.25f);

        heightMapGenerator.normalizeTexture();
        heightMap = heightMapGenerator.getTexture();

        for(int i = 0; i < heightMap.length; i++) {
            heightMap[i] = (int) (Math.pow(heightMap[i] * 1.2 + 1.2, 3) * worldHeight);
        }

        layerMapGenerator = new PerlinNoiseGenerator(seed + Util.hash(1), maxSideLength)
                .noise(32f, 1f);

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
                    else if(y > terrainHeight - 3)
                        octree.addVoxel(new Vector3i(x - worldWidthX / 2, y - worldHeight / 2, z - worldWidthZ / 2), Material.DIRT.getId());
                    else
                        octree.addVoxel(new Vector3i(x - worldWidthX / 2, y - worldHeight / 2, z - worldWidthZ / 2), Material.TEST_STONE.getId());
                } else {
                    octree.addVoxel(new Vector3i(x - worldWidthX / 2, y - worldHeight / 2, z - worldWidthZ / 2), Material.SAND.getId());
                }
            }
        }

        for(int i = 0; i < maxSideLength * maxSideLength / 32; i++) {
            int x = Math.abs(Util.hash(1 + Util.hash(i))) % maxSideLength;
            int z = Math.abs(Util.hash(2 + Util.hash(i))) % maxSideLength;
            int terrainHeight = (int) heightMap[Util.getIndexByPos(x, z, maxSideLength)];
            if(terrainHeight <= worldHeight)
                continue;
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2, z - maxSideLength / 2), Material.LOG.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2 + 1, z - maxSideLength / 2), Material.LOG.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2 + 2, z - maxSideLength / 2), Material.LOG.getId());
            octree.addVoxel(new Vector3i(x - maxSideLength / 2, terrainHeight + 1 - worldHeight / 2 + 3, z - maxSideLength / 2), Material.LOG.getId());


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

        return octree;
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
}
