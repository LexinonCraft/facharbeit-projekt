package com.lexinon.facharbeit;

import it.unimi.dsi.fastutil.Pair;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;
import static com.lexinon.facharbeit.ScreenTextureAtlas.SIDE_LENGTH_OF_ONE_TEXTURE;

public class ScreenMesh {

    private int vao;
    private int vbo;
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

    public void display(List<ScreenObject> textures) {
        int capacity = textures.size() * 24;
        FloatBuffer buffer = BufferUtils.createFloatBuffer(capacity);

        for(ScreenObject pair : textures) {
            buffer.put(pair.pos().x).put(pair.pos().y)
                    .put(pair.tex().x).put(pair.tex().y)
                    .put(pair.pos().x + 1).put(pair.pos().y)
                    .put(pair.tex().x + SIDE_LENGTH_OF_ONE_TEXTURE).put(pair.tex().y)
                    .put(pair.pos().x + 1).put(pair.pos().y + 1)
                    .put(pair.tex().x + SIDE_LENGTH_OF_ONE_TEXTURE).put(pair.tex().y + SIDE_LENGTH_OF_ONE_TEXTURE)
                    .put(pair.pos().x).put(pair.pos().y)
                    .put(pair.tex().x).put(pair.tex().y)
                    .put(pair.pos().x + 1).put(pair.pos().y + 1)
                    .put(pair.tex().x + SIDE_LENGTH_OF_ONE_TEXTURE).put(pair.tex().y + SIDE_LENGTH_OF_ONE_TEXTURE)
                    .put(pair.pos().x).put(pair.pos().y + 1)
                    .put(pair.tex().x).put(pair.tex().y + SIDE_LENGTH_OF_ONE_TEXTURE);
        }

        buffer.flip();

        for(int i = 0; i < buffer.limit(); i++) {
            System.out.println(buffer.get(i));
        }

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        amountOfTetxures = textures.size();
    }

    public void draw(int screenWidth, int screenHeight, Game game) {
        game.screenShader.use();
        glBindVertexArray(vao);

        game.screenShader.screenSize(screenWidth, screenHeight);
        glDrawArrays(GL_LINE_STRIP, 0, 2 * amountOfTetxures);
    }

    public void delete() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }

}
