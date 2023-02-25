package com.lexinon.facharbeit;

public class BoxShader extends Shader {

    private final int modelViewProjectionMatrixLoc;

    public BoxShader() {
        super("voxel");
        modelViewProjectionMatrixLoc = getLoc("ModelViewProjectionMatrix");
    }

    public int getModelViewProjectionMatrixLoc() {
        return modelViewProjectionMatrixLoc;
    }

}
