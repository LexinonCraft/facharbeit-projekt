package com.lexinon.facharbeit;

import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OctreeNonEmptyLeafNode implements IOctreeNode {

    private final short[] content;                // e.g. [(0, 0, 0), (1, 0, 0), (0, 1, 0), (1, 1, 0), (0, 0, 1), (1, 0, 1), (0, 1, 1), (1, 1, 1)]
    private final int edgeLengthExponent;
    private final int edgeLength;
    private final int edgeLengthSquared;
    private int nonEmptyVoxels = 0;
    private boolean enqueuedForUpdatingMesh = false;

    private final Mesh mesh = new Mesh();

    public OctreeNonEmptyLeafNode(Octree octree) {
        edgeLengthExponent = octree.getEdgeLengthExponent();
        edgeLength = 1 << edgeLengthExponent;
        edgeLengthSquared = edgeLength * edgeLength;
        content = new short[edgeLength * edgeLength * edgeLength];
    }

    @Override
    public void render(Vector3i origin, int volumeEdgeLength, Octree octree) {
        mesh.draw(origin, octree.getGame());
    }

    @Override
    public IOctreeNode addVoxel(Vector3i pos, short material, int remainingDepth, IOctreeParentNode parentNode, Octree octree) {
        Vector3i newPos = new Vector3i((pos.x >> (32 - edgeLengthExponent)) & (1 << edgeLengthExponent) - 1,
                (pos.y >> (32 - edgeLengthExponent)) & (1 << edgeLengthExponent) - 1,
                (pos.z >> (32 - edgeLengthExponent)) & (1 << edgeLengthExponent) - 1);
        if(content[getIndexByPos(newPos)] == 0)
            nonEmptyVoxels++;
        content[getIndexByPos(newPos)] = material;
        if(!enqueuedForUpdatingMesh) {
            octree.enqueueModifiedMesh(this);
            enqueuedForUpdatingMesh = true;
        }
        return this;
    }

    @Override
    public IOctreeNode removeVoxel(Vector3i pos, IOctreeParentNode parentNode, Octree octree) {
        Vector3i newPos = new Vector3i(pos.x >> (32 - edgeLengthExponent), pos.y >> (32 - edgeLengthExponent), pos.z >> (32 - edgeLengthExponent));
        if(content[getIndexByPos(newPos)] != 0)
            nonEmptyVoxels--;
        content[getIndexByPos(newPos)] = 0;
        if(nonEmptyVoxels > 0) {
            if(!enqueuedForUpdatingMesh) {
                octree.enqueueModifiedMesh(this);
                enqueuedForUpdatingMesh = true;
            }
            return this;
        } else {
            parentNode.decrementNonEmptySubtreesCount();
            mesh.delete();
            return null;
        }
    }

    public void updateMesh(boolean doOcclusionTest) {
        MeshBuilder meshBuilder = new MeshBuilder(6 * edgeLength * edgeLength * edgeLength);

        if(doOcclusionTest) {
            for (int i = 0; i < content.length; i++) {
                constructVoxelFacesWithOcclusionTest(i, meshBuilder);
            }
        }
        else {
            for (int i = 0; i < content.length; i++) {
                constructVoxelFacesWithoutOcclusionTest(i, meshBuilder);
            }
        }

        meshBuilder.build(mesh);
        enqueuedForUpdatingMesh = false;
    }

    private void constructVoxelFacesWithOcclusionTest(int i, MeshBuilder meshBuilder) {
        if(content[i] == 0)
            return;

        Vector3i pos = getPosByIndex(i);

        if(pos.y == edgeLength - 1 || content[i + edgeLength] == 0)
            meshBuilder.addFace(pos, Direction.UP, content[i]);
        if(pos.y == 0 || content[i - edgeLength] == 0)
            meshBuilder.addFace(pos, Direction.DOWN, content[i]);
        if(pos.z == edgeLength - 1 || content[i + edgeLengthSquared] == 0)
            meshBuilder.addFace(pos, Direction.NORTH, content[i]);
        if(pos.x == 0 || content[i - 1] == 0)
            meshBuilder.addFace(pos, Direction.EAST, content[i]);
        if(pos.z == 0 || content[i - edgeLengthSquared] == 0)
            meshBuilder.addFace(pos, Direction.SOUTH, content[i]);
        if(pos.x == edgeLength - 1 || content[i + 1] == 0)
            meshBuilder.addFace(pos, Direction.WEST, content[i]);
    }

    private void constructVoxelFacesWithoutOcclusionTest(int i, MeshBuilder meshBuilder) {
        if(content[i] == 0)
            return;

        Vector3i pos = getPosByIndex(i);

        meshBuilder.addFace(pos, Direction.UP, content[i]);
        meshBuilder.addFace(pos, Direction.DOWN, content[i]);
        meshBuilder.addFace(pos, Direction.NORTH, content[i]);
        meshBuilder.addFace(pos, Direction.EAST, content[i]);
        meshBuilder.addFace(pos, Direction.SOUTH, content[i]);
        meshBuilder.addFace(pos, Direction.WEST, content[i]);
    }

    private int getIndexByPos(Vector3i pos) {
        int x_ = pos.x;
        int y_ = pos.y << edgeLengthExponent;
        int z_ = pos.z << (2 * edgeLengthExponent);
        return x_ + y_ + z_;
    }

    private Vector3i getPosByIndex(int i) {
        int x = i & ((1 << edgeLengthExponent) - 1);
        int y = (i >> edgeLengthExponent) & (((1 << edgeLengthExponent) - 1));
        int z = (i >> 2 * edgeLengthExponent) & (((1 << edgeLengthExponent) - 1));
        return new Vector3i(x, y, z);
    }

    @Override
    public void print(int depth) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < depth; i++) {
            stringBuilder.append(".");
        }
        String tabs = stringBuilder.toString();
        System.out.println(tabs + "Array");
    }

}
