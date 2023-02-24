package com.lexinon.facharbeit;

import org.joml.Vector3f;

public enum Direction {

    UP(new Vector3f(0f, 1f, 0f),
            new Vector3f(0f, 1f, 0f),
            new Vector3f(0f, 1f, 1f),
            new Vector3f(1f, 1f, 1f),
            new Vector3f(1f, 1f, 0f)),
    DOWN(new Vector3f(0f, -1f, 0f),
            new Vector3f(0f, 0f, 0f),
            new Vector3f(1f, 0f, 0f),
            new Vector3f(1f, 0f, 1f),
            new Vector3f(0f, 0f, 1f)),
    NORTH(new Vector3f(0f, 0f, 1f),
            new Vector3f(0f, 0f, 1f),
            new Vector3f(1f, 0f, 1f),
            new Vector3f(1f, 1f, 1f),
            new Vector3f(0f, 1f, 1f)),
    EAST(new Vector3f(-1f, 0f, 0f),
            new Vector3f(0f, 0f, 0f),
            new Vector3f(0f, 0f, 1f),
            new Vector3f(0f, 1f, 1f),
            new Vector3f(0f, 1f, 0f)),
    SOUTH(new Vector3f(0f, 0f, -1f),
            new Vector3f(1f, 0f, 0f),
            new Vector3f(0f, 0f, 0f),
            new Vector3f(0f, 1f, 0f),
            new Vector3f(1f, 1f, 0f)),
    WEST(new Vector3f(1f, 0f, 0f),
            new Vector3f(1f, 0f, 1f),
            new Vector3f(1f, 0f, 0f),
            new Vector3f(1f, 1f, 0f),
            new Vector3f(1f, 1f, 1f));

    public final Vector3f vec;
    public final Vector3f vertex1;
    public final Vector3f vertex2;
    public final Vector3f vertex3;
    public final Vector3f vertex4;

    Direction(Vector3f vec, Vector3f vertex1, Vector3f vertex2, Vector3f vertex3, Vector3f vertex4) {
        this.vec = vec;
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
        this.vertex4 = vertex4;
    }

}
