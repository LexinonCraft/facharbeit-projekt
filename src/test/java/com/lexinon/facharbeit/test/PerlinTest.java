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

    public static void main(String[] args) throws IOException {
        int textureSideLength = 512;

        float[] texture = new float[textureSideLength * textureSideLength];
        float weight = 0;
        weight += noise(textureSideLength, 8, 0, texture, 2f);
        weight += noise(textureSideLength, 32, 0, texture, 1f);

        for(int x = 0; x < textureSideLength; x++) {
            for(int y = 0; y < textureSideLength; y++) {
                texture[getIndexByPos(x, y, textureSideLength)] = texture[getIndexByPos(x, y, textureSideLength)] / weight;
            }
        }

        BufferedImage image = new BufferedImage(textureSideLength, textureSideLength, BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < textureSideLength; x++) {
            for(int y = 0; y < textureSideLength; y++) {
                float value = texture[getIndexByPos(x, y, textureSideLength)];
                image.setRGB(x, y, new Color(value / 2 + 0.5f, value / 2 + 0.5f, value / 2 + 0.5f).getRGB());
            }
        }
        ImageIO.write(image, "png", new File("perlin.png"));
    }

    public static float noise(int textureSideLength, float frequency, int seed, float[] texture, float weight) {
        float gridPointDistance = 1f / frequency * textureSideLength;
        int amountGridPoints = ceilDiv(textureSideLength, gridPointDistance) + 1;

        int gridPoints = amountGridPoints * amountGridPoints;
        Vector2f[] gradients = new Vector2f[gridPoints];

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < gridPoints; i++) {
            float angle = hash(i, seed, md) % 36_000f / 18_000f * (float) Math.PI;
            gradients[i] = new Vector2f((float) Math.sin(angle), (float) Math.cos(angle));
        }

        for(int x = 0; x < textureSideLength; x++) {
            for(int y = 0; y < textureSideLength; y++) {
                int gridPointX = x / (int) gridPointDistance;
                int gridPointY = y / (int) gridPointDistance;
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

                texture[getIndexByPos(x, y, textureSideLength)] += fRes * weight;
                //image.setRGB(x, y, new Color(fRes / 2 + 0.5f, fRes / 2 + 0.5f, fRes / 2 + 0.5f).getRGB());
            }
        }
        return weight;
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
