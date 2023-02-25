package com.lexinon.facharbeit;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30C.*;

public class Mesh {

    public static final int MAX_AMOUNT_OF_FLOATS_PER_FACE = 5 * 3 * 2;

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
        this.vertices = vertices;

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    public void draw() {
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, vertices);
    }

    public void delete() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }

}
