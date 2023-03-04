package com.lexinon.facharbeit;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;

public class ScreenMesh {

    private final int vao;
    private final int vbo;
    private int amountOfTetxures = 0;

    public ScreenMesh() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 16, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 16, 8);
        glEnableVertexAttribArray(1);
    }

    public void display(List<ScreenObject> textures, Window window) {
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        int capacity = textures.size() * 24;
        FloatBuffer buffer = BufferUtils.createFloatBuffer(capacity);

        float a = 2f / window.getFramebufferWidth();
        float b = 2f / window.getFramebufferHeight();

        for(ScreenObject screenObject : textures) {
            buffer.put(screenObject.pos().x * a - 1).put(screenObject.pos().y * b - 1)
                    .put(screenObject.tex().x / 16).put((screenObject.tex().y + 1) / 16)
                    .put((screenObject.pos().x + ScreenShader.CHARACTER_SIZE) * a - 1).put(screenObject.pos().y * b - 1)
                    .put((screenObject.tex().x + 1) / 16).put((screenObject.tex().y + 1) / 16)
                    .put((screenObject.pos().x + ScreenShader.CHARACTER_SIZE) * a - 1).put((screenObject.pos().y + ScreenShader.CHARACTER_SIZE) * b - 1)
                    .put((screenObject.tex().x + 1) / 16).put(screenObject.tex().y / 16)
                    .put(screenObject.pos().x * a - 1).put(screenObject.pos().y * b - 1)
                    .put(screenObject.tex().x / 16).put((screenObject.tex().y + 1) / 16)
                    .put((screenObject.pos().x + ScreenShader.CHARACTER_SIZE) * a - 1).put((screenObject.pos().y + ScreenShader.CHARACTER_SIZE) * b - 1)
                    .put((screenObject.tex().x + 1) / 16).put(screenObject.tex().y / 16)
                    .put(screenObject.pos().x * a - 1).put((screenObject.pos().y + ScreenShader.CHARACTER_SIZE) * b - 1)
                    .put(screenObject.tex().x / 16).put(screenObject.tex().y / 16);
        }
        buffer.flip();

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        amountOfTetxures = textures.size();
    }

    public void draw(int screenWidth, int screenHeight, ScreenShader shader) {
        shader.use();
        glUniform1i(shader.getLoc("ScreenTextureAtlas"), 1);
        glBindVertexArray(vao);

        glDrawArrays(GL_TRIANGLES, 0, 6 * amountOfTetxures);
    }

    public void delete() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }

}
