package com.lexinon.facharbeit;

public class VoxelShader extends Shader {

    private final int modelViewProjectionMatrixLoc;
    private final int textureAtlasLoc;
    private final int fogColorLoc;

    public VoxelShader(Camera camera) {
        super("voxel", camera);
        modelViewProjectionMatrixLoc = getLoc("ModelViewProjectionMatrix");
        textureAtlasLoc = getLoc("TextureAtlas");
        fogColorLoc = getLoc("fogColor");
    }

    public int getModelViewProjectionMatrixLoc() {
        return modelViewProjectionMatrixLoc;
    }

    public int getTextureAtlasLoc() {
        return textureAtlasLoc;
    }

    public int getFogColorLoc() {
        return fogColorLoc;
    }
}
