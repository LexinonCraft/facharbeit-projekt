package com.lexinon.facharbeit;

public class VoxelShader extends Shader {

    private final int modelViewProjectionMatrixLoc;

    public VoxelShader() {
        super("voxel");
        modelViewProjectionMatrixLoc = getLoc("ModelViewProjectionMatrix");
    }

    public int getModelViewProjectionMatrixLoc() {
        return modelViewProjectionMatrixLoc;
    }

}
