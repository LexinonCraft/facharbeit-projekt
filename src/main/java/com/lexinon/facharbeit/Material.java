package com.lexinon.facharbeit;

import org.joml.Vector2f;

public class Material {

    public static final Material[] MATERIALS = new Material[256];
    private static short initCounter = 1;

    public static final Material
            CRATE = material("Crate", new Vector2f(9, 17), new Vector2f(9, 17), new Vector2f(9, 17)),
            GRASS = material("Grass", new Vector2f(6, 18), new Vector2f(2, 0), new Vector2f(2, 1));

    private String name;
    private Vector2f texCoordsUp, texCoordsSide, texCoordsDown;
    private short id;

    private Material(String name, Vector2f texCoordsUp, Vector2f texCoordsSide, Vector2f texCoordsDown, short id) {
        this.name = name;
        this.texCoordsUp = new Vector2f(texCoordsUp).mul(TextureAtlas.SIDE_LENGTH_OF_ONE_TEXTURE);
        this.texCoordsSide = new Vector2f(texCoordsSide).mul(TextureAtlas.SIDE_LENGTH_OF_ONE_TEXTURE);
        this.texCoordsDown = new Vector2f(texCoordsDown).mul(TextureAtlas.SIDE_LENGTH_OF_ONE_TEXTURE);
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