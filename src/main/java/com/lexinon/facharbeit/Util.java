package com.lexinon.facharbeit;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

    public static MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static int hash(int x) {
        byte[] bytes = md.digest(new byte[]{(byte) ((x >> 24) & 0x000000FF), (byte) ((x >> 16) & 0x000000FF), (byte) ((x >> 8) & 0x000000FF), (byte) (x & 0x000000FF)});
        return (bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3];
    }

    public static int getIndexByPos(int x, int y, int length) {
        return x + y * length;
    }

    public static int getXPosByIndex(int i, int length) {
        return i % length;
    }
    public static int getYPosByIndex(int i, int length) {
        return i / length;
    }

    public static void bubbleSort(float[] array) {
        for(int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if(array[j] > array[j + 1]) {
                    float temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

}
