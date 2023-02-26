package com.lexinon.facharbeit;

import static org.lwjgl.opengl.GL20C.glUniform2f;

public class ScreenShader extends Shader {

    private int screenWidth = 1, screenHeight = 1;
    private final int inverseScreenSizeLoc;

    public ScreenShader() {
        super("screen");
        inverseScreenSizeLoc = getLoc("inverseScreenSize");
    }

    public void screenSize(int width, int height) {
        if(screenWidth == width && screenHeight == height)
            return;
        screenWidth = width;
        screenHeight = height;
        glUniform2f(inverseScreenSizeLoc, 1f / screenWidth, 1f / screenHeight);
    }

}
