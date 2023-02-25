package com.lexinon.facharbeit;

import org.joml.Vector3i;

import java.util.Arrays;

public class OctreeNonEmptyLeafNode implements IOctreeNode {

    private final short[] content;                // e.g. [(0, 0, 0), (1, 0, 0), (0, 1, 0), (1, 1, 0), (0, 0, 1), (1, 0, 1), (0, 1, 1), (1, 1, 1)]
    private final int edgeLengthExponent;
    private int nonEmptyVoxels = 0;

    private final Mesh mesh = new Mesh();

    public OctreeNonEmptyLeafNode(Octree octree) {
        System.out.println("dings");
        edgeLengthExponent = octree.getEdgeLengthExponent();
        int edgeLength = 1 << edgeLengthExponent;
        content = new short[edgeLength * edgeLength * edgeLength];
    }

    @Override
    public void render() {
        mesh.draw();
    }

    @Override
    public IOctreeNode addVoxel(Vector3i pos, short material, int remainingDepth, IOctreeParentNode parentNode, Octree octree) {
        Vector3i newPos = new Vector3i(pos.x >> (32 - edgeLengthExponent), pos.y >> (32 - edgeLengthExponent), pos.z >> (32 - edgeLengthExponent));
        //System.out.println("Fools");
        System.out.println(newPos);
        if(content[getIndexByPos(newPos)] == 0)
            nonEmptyVoxels++;
        content[getIndexByPos(newPos)] = material;
        updateMesh(octree.doOcclusionTest());
        return this;
    }

    @Override
    public IOctreeNode removeVoxel(Vector3i pos, IOctreeParentNode parentNode, Octree octree) {
        Vector3i newPos = new Vector3i(pos.x >> (32 - edgeLengthExponent), pos.y >> (32 - edgeLengthExponent), pos.z >> (32 - edgeLengthExponent));
        if(content[getIndexByPos(newPos)] != 0)
            nonEmptyVoxels--;
        content[getIndexByPos(newPos)] = 0;
        if(nonEmptyVoxels > 0) {
            updateMesh(octree.doOcclusionTest());
            return this;
        } else {
            parentNode.decrementNonEmptySubtreesCount();
            mesh.delete();
            return null;
        }
    }

    private void updateMesh(boolean doOcclusionTest) {
        int edgeLength = 1 << edgeLengthExponent;
        System.out.println("dsadd ");
        System.out.println(content[0] + " " + content[1]);
        int edgeLengthSquared = edgeLength * edgeLength;

        MeshBuilder meshBuilder = new MeshBuilder(6 * edgeLength * edgeLength * edgeLength);

        if(doOcclusionTest) {
            for (int i = 0; i < content.length; i++) {
                constructVoxelFacesWithOcclusionTest(i, meshBuilder, edgeLength, edgeLengthSquared);
            }
        }
        else {
            for (int i = 0; i < content.length; i++) {
                constructVoxelFacesWithoutOcclusionTest(i, meshBuilder);
            }
        }

        meshBuilder.build(mesh);
    }

    private void constructVoxelFacesWithOcclusionTest(int i, MeshBuilder meshBuilder, int edgeLength, int edgeLengthSquared) {
        if(content[i] == 0)
            return;

        int topNeighborIndex = i + edgeLength;
        int bottomNeighborIndex = i - edgeLength;
        int northNeighborIndex = i + edgeLengthSquared;
        int eastNeighborIndex = i - 1;
        int southNeighborIndex = i - edgeLengthSquared;
        int westNeighborIndex = i + 1;

        Vector3i pos = getPosByIndex(i);

        if(voxelEmptyOrOutsideOfVolume(topNeighborIndex))
            meshBuilder.addFace(pos, Direction.UP, content[i]);
        if(voxelEmptyOrOutsideOfVolume(bottomNeighborIndex))
            meshBuilder.addFace(pos, Direction.DOWN, content[i]);
        if(voxelEmptyOrOutsideOfVolume(northNeighborIndex))
            meshBuilder.addFace(pos, Direction.NORTH, content[i]);
        if(voxelEmptyOrOutsideOfVolume(eastNeighborIndex))
            meshBuilder.addFace(pos, Direction.EAST, content[i]);
        if(voxelEmptyOrOutsideOfVolume(southNeighborIndex))
            meshBuilder.addFace(pos, Direction.SOUTH, content[i]);
        if(voxelEmptyOrOutsideOfVolume(westNeighborIndex))
            meshBuilder.addFace(pos, Direction.WEST, content[i]);
    }

    private boolean voxelEmptyOrOutsideOfVolume(int i) {
        if(i < 0 || i >= content.length)
            return true;
        return content[i] == 0;
    }

    private void constructVoxelFacesWithoutOcclusionTest(int i, MeshBuilder meshBuilder) {
        if(content[i] == 0)
            return;

        Vector3i pos = getPosByIndex(i); // origin!

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
        int z_ = pos.z << 2 * edgeLengthExponent;
        return x_ + y_ + z_;
    }

    private Vector3i getPosByIndex(int i) {
        int x = i << (32 - edgeLengthExponent) >> (32 - edgeLengthExponent);
        int y = i << (32 - 2 * edgeLengthExponent) >> (32 - edgeLengthExponent);
        int z = i << (32 - 3 * edgeLengthExponent) >> (32 - edgeLengthExponent);
        return new Vector3i(x, y, z);
    }

}
