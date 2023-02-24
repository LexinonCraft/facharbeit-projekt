package com.lexinon.facharbeit;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class MeshBuilder {

    private FloatBuffer buffer;
    private int vertices = 0;

    public MeshBuilder(int maxAmountOfFaces) {
        buffer = BufferUtils.createFloatBuffer(maxAmountOfFaces * Mesh.MAX_AMOUNT_OF_FLOATS_PER_FACE);
    }

    public void addFace(Vector3f voxelPos, Direction direction, int material) {
        Vector3f vertexPos1 = new Vector3f(voxelPos).add(direction.vertex1);
        Vector3f vertexPos2 = new Vector3f(voxelPos).add(direction.vertex2);
        Vector3f vertexPos3 = new Vector3f(voxelPos).add(direction.vertex3);
        Vector3f vertexPos4 = new Vector3f(voxelPos).add(direction.vertex4);

        //

        Vector2f color = switch(material) {
            case 1 -> new Vector2f(1f, 0f);
            case 2 -> new Vector2f(1f, 0.5f);
            case 3 -> new Vector2f(0.5f, 0.5f);
            case 4 -> new Vector2f(0f, 1f);
            default -> new Vector2f(0f, 0f);
        };

        Vector2f vertexTexCoord1 = new Vector2f(0f, 0f);
        Vector2f vertexTexCoord2 = new Vector2f(0f, 1f);
        Vector2f vertexTexCoord3 = new Vector2f(1f, 0f);
        Vector2f vertexTexCoord4 = new Vector2f(1f, 1f);

        //

        buffer.put(vertexPos1.x);
        buffer.put(vertexPos1.y);
        buffer.put(vertexPos1.z);
        buffer.put(vertexTexCoord1.x);
        buffer.put(vertexTexCoord1.y);

        buffer.put(vertexPos2.x);
        buffer.put(vertexPos2.y);
        buffer.put(vertexPos2.z);
        buffer.put(vertexTexCoord2.x);
        buffer.put(vertexTexCoord2.y);

        buffer.put(vertexPos3.x);
        buffer.put(vertexPos3.y);
        buffer.put(vertexPos3.z);
        buffer.put(vertexTexCoord3.x);
        buffer.put(vertexTexCoord3.y);

        buffer.put(vertexPos1.x);
        buffer.put(vertexPos1.y);
        buffer.put(vertexPos1.z);
        buffer.put(vertexTexCoord1.x);
        buffer.put(vertexTexCoord1.y);

        buffer.put(vertexPos3.x);
        buffer.put(vertexPos3.y);
        buffer.put(vertexPos3.z);
        buffer.put(vertexTexCoord3.x);
        buffer.put(vertexTexCoord3.y);

        buffer.put(vertexPos4.x);
        buffer.put(vertexPos4.y);
        buffer.put(vertexPos4.z);
        buffer.put(vertexTexCoord4.x);
        buffer.put(vertexTexCoord4.y);

        vertices += 6;
    }

    public Mesh toMesh() {
        return new Mesh(buffer.flip(), vertices);
    }

}
