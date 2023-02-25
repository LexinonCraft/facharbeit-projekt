package com.lexinon.facharbeit;

import org.joml.Vector3i;

public class MyTestClass {

    private static int getIndexByPos(Vector3i pos, int edgeLengthExponent) {
        int x_ = pos.x;
        int y_ = pos.y << edgeLengthExponent;
        int z_ = pos.z << 2 * edgeLengthExponent;
        return x_ + y_ + z_;
    }

    private static Vector3i getPosByIndex(int i, int edgeLengthExponent) {
        int x = i << (32 - edgeLengthExponent) >> (32 - edgeLengthExponent);
        int y = i << (32 - 2 * edgeLengthExponent) >> (32 - edgeLengthExponent);
        int z = i << (32 - 3 * edgeLengthExponent) >> (32 - edgeLengthExponent);
        return new Vector3i(x, y, z);
    }

    public static void main(String[] args) {
        System.out.println(getIndexByPos(new Vector3i(1, 0, 0), 6));
        System.out.println(getPosByIndex(1, 6));
    }

}