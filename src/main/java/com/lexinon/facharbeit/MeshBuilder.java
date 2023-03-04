package com.lexinon.facharbeit;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;

import static com.lexinon.facharbeit.VoxelTextureAtlas.TEXTURE_MARGIN;
import static com.lexinon.facharbeit.VoxelTextureAtlas.SIDE_LENGTH_OF_ONE_TEXTURE;

import java.nio.FloatBuffer;

public class MeshBuilder {

    private FloatBuffer buffer;
    private int vertices = 0;

    public MeshBuilder(int maxAmountOfFaces) {
        buffer = BufferUtils.createFloatBuffer(maxAmountOfFaces * Mesh.MAX_AMOUNT_OF_FLOATS_PER_FACE);
    }

    public MeshBuilder(MeshBuilder meshBuilder) {
        buffer = meshBuilder.buffer.flip().limit(meshBuilder.buffer.capacity());
    }

    public void addFace(Vector3i voxelPos, Direction direction, short material) {
        addFace(voxelPos, direction, Material.getTexCoords(material, direction));
    }

    public void addFace(Vector3i voxelPos, Direction direction, Vector2f texCoords) {
        Vector3f vertexPos1 = new Vector3f(voxelPos).add(direction.vertex1);
        Vector3f vertexPos2 = new Vector3f(voxelPos).add(direction.vertex2);
        Vector3f vertexPos3 = new Vector3f(voxelPos).add(direction.vertex3);
        Vector3f vertexPos4 = new Vector3f(voxelPos).add(direction.vertex4);

        Vector2f vertexTexCoords1 = new Vector2f(TEXTURE_MARGIN, SIDE_LENGTH_OF_ONE_TEXTURE - TEXTURE_MARGIN).add(texCoords);
        Vector2f vertexTexCoords2 = new Vector2f(SIDE_LENGTH_OF_ONE_TEXTURE - TEXTURE_MARGIN, SIDE_LENGTH_OF_ONE_TEXTURE - TEXTURE_MARGIN).add(texCoords);
        Vector2f vertexTexCoords3 = new Vector2f(SIDE_LENGTH_OF_ONE_TEXTURE - TEXTURE_MARGIN, TEXTURE_MARGIN).add(texCoords);
        Vector2f vertexTexCoords4 = new Vector2f(TEXTURE_MARGIN, TEXTURE_MARGIN).add(texCoords);

        buffer.put(vertexPos1.x);
        buffer.put(vertexPos1.y);
        buffer.put(vertexPos1.z);
        buffer.put(vertexTexCoords1.x);
        buffer.put(vertexTexCoords1.y);

        buffer.put(vertexPos2.x);
        buffer.put(vertexPos2.y);
        buffer.put(vertexPos2.z);
        buffer.put(vertexTexCoords2.x);
        buffer.put(vertexTexCoords2.y);

        buffer.put(vertexPos3.x);
        buffer.put(vertexPos3.y);
        buffer.put(vertexPos3.z);
        buffer.put(vertexTexCoords3.x);
        buffer.put(vertexTexCoords3.y);

        buffer.put(vertexPos1.x);
        buffer.put(vertexPos1.y);
        buffer.put(vertexPos1.z);
        buffer.put(vertexTexCoords1.x);
        buffer.put(vertexTexCoords1.y);

        buffer.put(vertexPos3.x);
        buffer.put(vertexPos3.y);
        buffer.put(vertexPos3.z);
        buffer.put(vertexTexCoords3.x);
        buffer.put(vertexTexCoords3.y);

        buffer.put(vertexPos4.x);
        buffer.put(vertexPos4.y);
        buffer.put(vertexPos4.z);
        buffer.put(vertexTexCoords4.x);
        buffer.put(vertexTexCoords4.y);

        vertices += 6;
    }

    public Mesh build() {
        return new Mesh(buffer.flip(), vertices);
    }

    public void build(Mesh mesh) {
        mesh.update(buffer.flip(), vertices);
    }

}
