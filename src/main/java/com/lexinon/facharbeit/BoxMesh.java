package com.lexinon.facharbeit;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;

public class BoxMesh {

    private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    private int vao;
    private int vbo;

    private static final float OFFSET = 0.005f;

    public BoxMesh() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(48);
        buffer.put(-OFFSET).put(-OFFSET).put(-OFFSET)
                .put(1 + OFFSET).put(-OFFSET).put(-OFFSET)
                .put(1 + OFFSET).put(-OFFSET).put(1 + OFFSET)
                .put(-OFFSET).put(-OFFSET).put(1 + OFFSET)
                .put(-OFFSET).put(-OFFSET).put(-OFFSET)
                .put(-OFFSET).put(1 + OFFSET).put(-OFFSET)
                .put(1 + OFFSET).put(1 + OFFSET).put(-OFFSET)
                .put(1 + OFFSET).put(-OFFSET).put(-OFFSET)
                .put(1 + OFFSET).put(1 + OFFSET).put(-OFFSET)
                .put(1 + OFFSET).put(1 + OFFSET).put(1 + OFFSET)
                .put(1 + OFFSET).put(-OFFSET).put(1 + OFFSET)
                .put(1 + OFFSET).put(1 + OFFSET).put(1 + OFFSET)
                .put(-OFFSET).put(1 + OFFSET).put(1 + OFFSET)
                .put(-OFFSET).put(-OFFSET).put(1 + OFFSET)
                .put(-OFFSET).put(1 + OFFSET).put(1 + OFFSET)
                .put(-OFFSET).put(1 + OFFSET).put(-OFFSET).flip();
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
        glEnableVertexAttribArray(0);
    }

    public void draw(Vector3i pos, Game game) {
        game.boxShader.use();
        glLineWidth(5);
        glBindVertexArray(vao);

        Matrix4f modelViewProjectionMatrix = game.camera.getModelViewProjectionMatrix().set(game.camera.getViewProjectionMatrix())
                .translate(pos.x, pos.y, pos.z);

        glUniformMatrix4fv(game.boxShader.getModelViewProjectionMatrixLoc(), false, modelViewProjectionMatrix.get(matrixBuffer));
        glDrawArrays(GL_LINE_STRIP, 0, 16);
    }

    public void delete() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }

}
