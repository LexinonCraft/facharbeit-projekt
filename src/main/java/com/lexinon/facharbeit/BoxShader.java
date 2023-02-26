package com.lexinon.facharbeit;

public class BoxShader extends Shader {

    private final int modelViewProjectionMatrixLoc;

    public BoxShader(Camera camera) {
        super("voxel", camera);
        modelViewProjectionMatrixLoc = getLoc("ModelViewProjectionMatrix");
    }

    public int getModelViewProjectionMatrixLoc() {
        return modelViewProjectionMatrixLoc;
    }

}
