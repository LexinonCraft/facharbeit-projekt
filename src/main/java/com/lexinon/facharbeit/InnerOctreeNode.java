package com.lexinon.facharbeit;

import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class InnerOctreeNode implements IOctreeNode, IOctreeParentNode {

    private IOctreeNode subtree1 = new OctreeEmptyLeafNode(),
            subtree2 = new OctreeEmptyLeafNode(),
            subtree3 = new OctreeEmptyLeafNode(),
            subtree4 = new OctreeEmptyLeafNode(),
            subtree5 = new OctreeEmptyLeafNode(),
            subtree6 = new OctreeEmptyLeafNode(),
            subtree7 = new OctreeEmptyLeafNode(),
            subtree8 = new OctreeEmptyLeafNode(); // (0,0,0), (0,0,z), (0,y,0), (0,y,z), (x,0,0), (x,0,z), (x,y,0), (x,y,z)
    private int nonEmptySubtrees = 0;

    public InnerOctreeNode() {
        Metrics.incrementNumOctreeNodes();
    }

    @Override
    public void render(int originX, int originY, int originZ, int volumeEdgeLength, Octree octree) {
        //System.out.println(origin);
        int half = volumeEdgeLength / 2;
        subtree1.render(originX, originY, originZ, half, octree);
        subtree2.render(originX, originY, originZ + half, half, octree);
        subtree3.render(originX, originY + half, originZ, half, octree);
        subtree4.render(originX, originY + half, originZ + half, half, octree);
        subtree5.render(originX + half, originY, originZ, half, octree);
        subtree6.render(originX + half, originY, originZ + half, half, octree);
        subtree7.render(originX + half, originY + half, originZ, half, octree);
        subtree8.render(originX + half, originY + half, originZ + half, half, octree);
    }

    @Override
    public IOctreeNode addVoxel(Vector3i pos, short material, int remainingDepth, IOctreeParentNode parentNode, Octree octree) {
        Vector3i newPos = new Vector3i(pos.x << 1, pos.y << 1, pos.z << 1);

        if(pos.z < 0) {
            if(pos.y < 0) {
                if(pos.x < 0) {
                    subtree8 = subtree8.addVoxel(newPos, material, remainingDepth - 1, this, octree);
                } else {
                    subtree4 = subtree4.addVoxel(newPos, material, remainingDepth - 1, this, octree);
                }
            } else {
                if(pos.x < 0) {
                    subtree6 = subtree6.addVoxel(newPos, material, remainingDepth - 1, this, octree);
                } else {
                    subtree2 = subtree2.addVoxel(newPos, material, remainingDepth - 1, this, octree);
                }
            }
        } else {
            if(pos.y < 0) {
                if(pos.x < 0) {
                    subtree7 = subtree7.addVoxel(newPos, material, remainingDepth - 1, this, octree);
                } else {
                    subtree3 = subtree3.addVoxel(newPos, material, remainingDepth - 1, this, octree);
                }
            } else {
                if(pos.x < 0) {
                    subtree5 = subtree5.addVoxel(newPos, material, remainingDepth - 1, this, octree);
                } else {
                    subtree1 = subtree1.addVoxel(newPos, material, remainingDepth - 1, this, octree);
                }
            }
        }

        return this;
    }

    @Override
    public IOctreeNode removeVoxel(Vector3i pos, IOctreeParentNode parentNode, Octree octree) { // TODO
        Vector3i newPos = new Vector3i(pos.x << 1, pos.y << 1, pos.z << 1);

        System.out.println(newPos);

        if(pos.z < 0) {
            if(pos.y < 0) {
                if(pos.x < 0) {
                    System.out.println("ST8");
                    subtree8 = subtree8.removeVoxel(newPos, this, octree);
                } else {
                    System.out.println("ST4");
                    subtree4 = subtree4.removeVoxel(newPos, this, octree);
                }
            } else {
                if(pos.x < 0) {
                    System.out.println("ST6");
                    subtree6 = subtree6.removeVoxel(newPos, this, octree);
                } else {
                    System.out.println("ST2");
                    subtree2 = subtree2.removeVoxel(newPos, this, octree);
                }
            }
        } else {
            if(pos.y < 0) {
                if(pos.x < 0) {
                    System.out.println("ST7");
                    subtree7 = subtree7.removeVoxel(newPos, this, octree);
                } else {
                    System.out.println("ST3");
                    subtree3 = subtree3.removeVoxel(newPos, this, octree);
                }
            } else {
                if(pos.x < 0) {
                    System.out.println("ST5");
                    subtree5 = subtree5.removeVoxel(newPos, this, octree);
                } else {
                    System.out.println("ST1 " /*+ Integer.toBinaryString(pos.x)*/);
                    subtree1 = subtree1.removeVoxel(newPos, this, octree);
                }
            }
        }

        if(nonEmptySubtrees > 0)
            return this;

        parentNode.decrementNonEmptySubtreesCount();
        Metrics.decrementNumOctreeNodes();
        return new OctreeEmptyLeafNode();
    }

    @Override
    public void deleteEverything() {
        subtree1.deleteEverything();
        subtree2.deleteEverything();
        subtree3.deleteEverything();
        subtree4.deleteEverything();
        subtree5.deleteEverything();
        subtree6.deleteEverything();
        subtree7.deleteEverything();
        subtree8.deleteEverything();
    }

    @Override
    public void incrementNonEmptySubtreesCount() {
        nonEmptySubtrees++;
    }

    @Override
    public void decrementNonEmptySubtreesCount() {
        nonEmptySubtrees--;
    }

    @Override
    public void print(int depth) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < depth; i++) {
            stringBuilder.append(".");
        }
        String tabs = stringBuilder.toString();
        System.out.println(tabs + "Inner");
        subtree1.print(depth + 1);
        subtree2.print(depth + 1);
        subtree3.print(depth + 1);
        subtree4.print(depth + 1);
        subtree5.print(depth + 1);
        subtree6.print(depth + 1);
        subtree7.print(depth + 1);
        subtree8.print(depth + 1);
    }

}
