package com.lexinon.facharbeit.test;

import org.joml.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PerlinTest {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        File f = new File(args[0]);

        int textureSideLength = Integer.parseInt(args[1]);
        int gridPointDistance = Integer.parseInt(args[2]);
        int amountGridPoints = ceilDiv(textureSideLength + 1, gridPointDistance);
        int seed = Integer.parseInt(args[3]);

        int gridPoints = amountGridPoints * amountGridPoints;
        Vector2f[] gradients = new Vector2f[gridPoints];

        MessageDigest md = MessageDigest.getInstance("SHA256");

        for(int i = 0; i < gridPoints; i++) {
            float angle = hash(i, seed, md) % 36_000f / 18_000f * (float) Math.PI;
            gradients[i] = new Vector2f((float) Math.sin(angle), (float) Math.cos(angle));
        }

        BufferedImage image = new BufferedImage(textureSideLength, textureSideLength, BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < textureSideLength; x++) {
            for(int y = 0; y < textureSideLength; y++) {
                int gridPointX = x / gridPointDistance;
                int gridPointY = y / gridPointDistance;
                Vector2f gradient1 = gradients[getIndexByPos(gridPointX, gridPointY, amountGridPoints)];
                Vector2f gradient2 = gradients[getIndexByPos(gridPointX + 1, gridPointY, amountGridPoints)];
                Vector2f gradient3 = gradients[getIndexByPos(gridPointX + 1, gridPointY + 1, amountGridPoints)];
                Vector2f gradient4 = gradients[getIndexByPos(gridPointX, gridPointY + 1, amountGridPoints)];
                float relX = x % gridPointDistance / (float) gridPointDistance;
                float relY = y % gridPointDistance / (float) gridPointDistance;

                float s1 = gradient1.dot(new Vector2f(relX, relY));
                float s2 = gradient2.dot(new Vector2f(relX - 1, relY));
                float s3 = gradient3.dot(new Vector2f(relX - 1, relY - 1));
                float s4 = gradient4.dot(new Vector2f(relX, relY - 1));

                float f0 = s1 * interpolationFunction(1 - relX) + s2 * interpolationFunction(relX);
                float f1 = s4 * interpolationFunction(1 - relX) + s3 * interpolationFunction(relX);
                float fRes = f0 * interpolationFunction(1 - relY) + f1 * interpolationFunction(relY);

                image.setRGB(x, y, new Color(fRes / 2 + 0.5f, fRes / 2 + 0.5f, fRes / 2 + 0.5f).getRGB());
            }
        }

        ImageIO.write(image, "png", f);
    }

    private static int getIndexByPos(int x, int y, int length) {
        return x + y * length;
    }

    private static int getXPosByIndex(int i, int length) {
        return i % length;
    }
    private static int getYPosByIndex(int i, int length) {
        return i / length;
    }

    private static int hash(int x, MessageDigest md) {
        byte[] bytes = md.digest(new byte[]{(byte) ((x >> 24) & 0x000000FF), (byte) ((x >> 16) & 0x000000FF), (byte) ((x >> 8) & 0x000000FF), (byte) (x & 0x000000FF)});
        return (bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3];
    }

    private static int hash(int x, int seed, MessageDigest md) {
        return hash(x + hash(seed, md), md);
    }

    private static float interpolationFunction(float a) {
        return a * a * a * (6 * a * a - 15 * a + 10);
        //return 3 * a * a - 2 * a * a * a;
        //return  a;
    }

    private static int ceilDiv(float a, float b) {
        return (int) Math.floor(a / b) + 1;
    }

}
