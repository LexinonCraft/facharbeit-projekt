package com.lexinon.facharbeit;

public class VoxelShader extends Shader {

    private final int modelViewProjectionMatrixLoc;

    public VoxelShader(Camera camera) {
        super("voxel", camera);
        modelViewProjectionMatrixLoc = getLoc("ModelViewProjectionMatrix");
    }

    public int getModelViewProjectionMatrixLoc() {
        return modelViewProjectionMatrixLoc;
    }

}
