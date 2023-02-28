package com.lexinon.facharbeit;

import org.joml.Vector2f;

public class Material {

    public static final Material[] MATERIALS = new Material[256];
    private static short initCounter = 1;

    public static final Material
            CRATE = material("Crate", new Vector2f(9, 17), new Vector2f(9, 17), new Vector2f(9, 17)),
            GRASS = material("Grass", new Vector2f(6, 18), new Vector2f(2, 0), new Vector2f(2, 1)),
            DIRT = material("Dirt", new Vector2f(2, 1), new Vector2f(2, 1), new Vector2f(2, 1)),
            TEST_STONE = material("Stone", new Vector2f(2, 4), new Vector2f(2, 4), new Vector2f(2, 4)),
            LOG = material("Log", new Vector2f(14, 16), new Vector2f(13, 16), new Vector2f(14, 16)),
            LEAVES = material("Leaves", new Vector2f(2, 18), new Vector2f(2, 18), new Vector2f(2, 18)),
            WATER = material("Water", new Vector2f(2, 12), new Vector2f(2, 12), new Vector2f(2, 12)),
            SAND = material("Sand", new Vector2f(3, 9), new Vector2f(3, 9), new Vector2f(3, 9)),
            FARMLAND = material("Farmland", new Vector2f(3, 20), new Vector2f(2, 1), new Vector2f(2, 1)),
            HAY = material("Hay", new Vector2f(21, 24), new Vector2f(21, 18), new Vector2f(21, 18)),
            CACTUS = material("Cactus", new Vector2f(4, 19), new Vector2f(23, 18), new Vector2f(4, 19)),
            CAKE = material("Cake", new Vector2f(22, 21), new Vector2f(21, 21), new Vector2f(0, 21)),
            PUMPKIN = material("Pumpkin", new Vector2f(9, 26), new Vector2f(9, 26), new Vector2f(9, 26)),
            VINES = material("Vines", new Vector2f(11, 18), new Vector2f(11, 18), new Vector2f(11, 18)),
            TANK = material("Tank", new Vector2f(2, 19), new Vector2f(2, 20), new Vector2f(2, 19)),
            PLANKS = material("Planks", new Vector2f(0, 17), new Vector2f(0, 17), new Vector2f(0, 17)),
            BRICKS = material("Bricks", new Vector2f(7, 9), new Vector2f(7, 9), new Vector2f(7, 9)),
            RED_BRICKS = material("Red Bricks", new Vector2f(6, 9), new Vector2f(6, 9), new Vector2f(6, 9));

    private String name;
    private Vector2f texCoordsUp, texCoordsSide, texCoordsDown;
    private short id;

    private Material(String name, Vector2f texCoordsUp, Vector2f texCoordsSide, Vector2f texCoordsDown, short id) {
        this.name = name;
        this.texCoordsUp = new Vector2f(texCoordsUp).mul(VoxelTextureAtlas.SIDE_LENGTH_OF_ONE_TEXTURE);
        this.texCoordsSide = new Vector2f(texCoordsSide).mul(VoxelTextureAtlas.SIDE_LENGTH_OF_ONE_TEXTURE);
        this.texCoordsDown = new Vector2f(texCoordsDown).mul(VoxelTextureAtlas.SIDE_LENGTH_OF_ONE_TEXTURE);
        this.id = id;
    }

    private static Material material(String name, Vector2f texCoordsUp, Vector2f texCoordsSide, Vector2f texCoordsDown) {
        MATERIALS[initCounter] = new Material(name, texCoordsUp, texCoordsSide, texCoordsDown, initCounter);
        initCounter++;
        return MATERIALS[initCounter - 1];
    }

    public short getId() {
        return id;
    }

    public static Vector2f getTexCoords(short material, Direction direction) {
        return switch(direction) {
            case UP -> MATERIALS[material].texCoordsUp;
            case NORTH, EAST, SOUTH, WEST -> MATERIALS[material].texCoordsSide;
            case DOWN -> MATERIALS[material].texCoordsDown;
        };
    }

}
