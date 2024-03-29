package com.lexinon.facharbeit;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30C.*;

/**
 * Holds the handles of the VAO and VBO of a single mesh.
 */
public class Mesh {

    public static final int MAX_AMOUNT_OF_FLOATS_PER_FACE = 5 * 3 * 2;
    private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    private int vao;
    private int vbo;
    private int vertices;

    public Mesh() {
        vertices = 0;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * 4, 3 * 4);
        glEnableVertexAttribArray(1);
    }

    public Mesh(FloatBuffer buffer, int vertices) {
        this();
        update(buffer, vertices);
    }

    public void update(FloatBuffer buffer, int vertices) {
        Metrics.decreaseNumTriangles(this.vertices / 3);
        this.vertices = vertices;
        Metrics.increaseNumTriangles(this.vertices / 3);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    public void draw(int originX, int originY, int originZ, Game game) {
        glBindVertexArray(vao);

        Matrix4f modelViewProjectionMatrix = game.getCamera().getModelViewProjectionMatrix().set(game.getCamera().getViewProjectionMatrix())
                        .translate(originX, originY, originZ);

        glUniformMatrix4fv(game.getVoxelShader().getModelViewProjectionMatrixLoc(), false, modelViewProjectionMatrix.get(matrixBuffer));
        glDrawArrays(GL_TRIANGLES, 0, vertices);
    }

    public void delete() {
        Metrics.decreaseNumTriangles(this.vertices / 3);
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }

}
