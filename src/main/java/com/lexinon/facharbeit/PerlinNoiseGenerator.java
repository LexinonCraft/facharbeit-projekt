package com.lexinon.facharbeit;

import org.joml.Vector2f;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;

public class PerlinNoiseGenerator {

    private final float[] texture;
    private final int textureSideLength;
    private float totalWeight;
    private final int seed;
    private final MessageDigest md;

    public PerlinNoiseGenerator(int seed, int textureSideLength) {
        this.seed = seed;
        this.textureSideLength = textureSideLength;

        texture = new float[textureSideLength * textureSideLength];
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Couldn't initialize hashing algorithm!");
        }
    }

    public PerlinNoiseGenerator noise(float frequency, float weight) {
        float gridPointDistance = textureSideLength / frequency;
        int amountGridPoints = (int) (frequency + 2);

        int gridPoints = amountGridPoints * amountGridPoints;
        Vector2f[] gradients = new Vector2f[gridPoints];

        for(int i = 0; i < gridPoints; i++) {
            float angle = hash(i) % 36_000f / 18_000f * (float) Math.PI;
            gradients[i] = new Vector2f((float) Math.sin(angle), (float) Math.cos(angle));
        }

        for(int x = 0; x < textureSideLength; x++) {
            for(int y = 0; y < textureSideLength; y++) {
                int gridPointX = (int) (x / gridPointDistance);
                int gridPointY = (int) (y / gridPointDistance);
                Vector2f gradient1 = gradients[getIndexByPos(gridPointX, gridPointY, amountGridPoints)];
                Vector2f gradient2 = gradients[getIndexByPos(gridPointX + 1, gridPointY, amountGridPoints)];
                Vector2f gradient3 = gradients[getIndexByPos(gridPointX + 1, gridPointY + 1, amountGridPoints)];
                Vector2f gradient4 = gradients[getIndexByPos(gridPointX, gridPointY + 1, amountGridPoints)];
                float relX = x / gridPointDistance - gridPointX;
                float relY = y / gridPointDistance - gridPointY;

                float s1 = gradient1.dot(new Vector2f(relX, relY));
                float s2 = gradient2.dot(new Vector2f(relX - 1, relY));
                float s3 = gradient3.dot(new Vector2f(relX - 1, relY - 1));
                float s4 = gradient4.dot(new Vector2f(relX, relY - 1));

                float f0 = s1 * interpolationFunction(1 - relX) + s2 * interpolationFunction(relX);
                float f1 = s4 * interpolationFunction(1 - relX) + s3 * interpolationFunction(relX);
                float fRes = f0 * interpolationFunction(1 - relY) + f1 * interpolationFunction(relY);

                texture[getIndexByPos(x, y, textureSideLength)] += fRes * weight;
            }
        }
        totalWeight += weight;
        return this;
    }

    private int hashBackbone(int x) {
        byte[] bytes = md.digest(new byte[]{(byte) ((x >> 24) & 0x000000FF), (byte) ((x >> 16) & 0x000000FF), (byte) ((x >> 8) & 0x000000FF), (byte) (x & 0x000000FF)});
        return (bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3];
    }

    private int hash(int x) {
        return hashBackbone(x + hashBackbone(seed));
    }

    private float interpolationFunction(float a) {
        return a * a * a * (6 * a * a - 15 * a + 10);
    }

    public int getIndexByPos(int x, int y, int length) {
        return x + y * length;
    }

    public int getXPosByIndex(int i, int length) {
        return i % length;
    }
    public int getYPosByIndex(int i, int length) {
        return i / length;
    }

    private static int ceilDiv(float a, float b) {
        return (int) Math.floor(a / b) + 1;
    }

    public void normalizeTexture() {
        for(int i = 0; i < texture.length; i++) {
            texture[i] /= totalWeight;
        }
        totalWeight = 1;
    }

    public float[] getTexture() {
        return texture;
    }

}
