package com.lexinon.facharbeit;

import static org.lwjgl.opengl.GL20C.*;

public class ScreenShader extends Shader {

    public static final int CHARACTER_SIZE = 25;

    public ScreenShader(Camera camera) {
        super("screen", camera);
    }

}
