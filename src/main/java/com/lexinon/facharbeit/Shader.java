package com.lexinon.facharbeit;

import java.io.InputStream;
import java.util.Scanner;

import static org.lwjgl.opengl.GL20C.*;

public class Shader {

    private final int programId;

    public Shader(String name, Camera camera) {
        InputStream vertexShaderInputStream = Game.class.getResourceAsStream(String.format("/%s.vert", name));
        Scanner vertexShaderScanner = new Scanner(vertexShaderInputStream);
        StringBuilder vertexShaderStringBuilder = new StringBuilder();
        while(vertexShaderScanner.hasNext()) {
            vertexShaderStringBuilder.append(vertexShaderScanner.nextLine());
            vertexShaderStringBuilder.append("\n");
        }
        String vertexShaderCode = vertexShaderStringBuilder.toString();
        vertexShaderCode = vertexShaderCode.replace("_replace_near_", String.valueOf(camera.getZNear()));
        vertexShaderCode = vertexShaderCode.replace("_replace_far_", String.valueOf(camera.getZFar()));
        int vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderId, vertexShaderCode);
        glCompileShader(vertexShaderId);

        InputStream fragmentShaderInputStream = Game.class.getResourceAsStream(String.format("/%s.frag", name));
        Scanner fragmentShaderScanner = new Scanner(fragmentShaderInputStream);
        StringBuilder fragmentShaderStringBuilder = new StringBuilder();
        while(fragmentShaderScanner.hasNext()) {
            fragmentShaderStringBuilder.append(fragmentShaderScanner.nextLine());
            fragmentShaderStringBuilder.append("\n");
        }
        String fragmentShaderCode = fragmentShaderStringBuilder.toString();
        fragmentShaderCode = fragmentShaderCode.replace("_replace_near_", String.valueOf(camera.getZNear()));
        fragmentShaderCode = fragmentShaderCode.replace("_replace_far_", String.valueOf(camera.getZFar()));
        int fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderId, fragmentShaderCode);
        glCompileShader(fragmentShaderId);

        programId = glCreateProgram();
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);
        glLinkProgram(programId);
    }

    public void use() {
        glUseProgram(programId);
    }

    public int getLoc(String name) {
        return glGetUniformLocation(programId, name);
    }

}
