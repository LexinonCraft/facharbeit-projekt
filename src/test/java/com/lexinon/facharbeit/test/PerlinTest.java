package com.lexinon.facharbeit.test;

import com.lexinon.facharbeit.PerlinNoiseGenerator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PerlinTest {

    public static void main(String[] args) throws IOException {
        int textureSideLength = 512;

        PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator(0, textureSideLength);
        perlinNoiseGenerator.noise(8f, 1f)
                        .noise(32f, 1f)
                        .noise(14f, 2f);

        perlinNoiseGenerator.normalizeTexture();
        float[] texture = perlinNoiseGenerator.getTexture();
        for(int x = 0; x < textureSideLength; x++) {
            for(int y = 0; y < textureSideLength; y++) {
                texture[perlinNoiseGenerator.getIndexByPos(x, y, textureSideLength)] = texture[perlinNoiseGenerator.getIndexByPos(x, y, textureSideLength)];
            }
        }

        BufferedImage image = new BufferedImage(textureSideLength, textureSideLength, BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < textureSideLength; x++) {
            for(int y = 0; y < textureSideLength; y++) {
                float value = texture[perlinNoiseGenerator.getIndexByPos(x, y, textureSideLength)];
                image.setRGB(x, y, new Color(value / 2 + 0.5f, value / 2 + 0.5f, value / 2 + 0.5f).getRGB());
            }
        }
        ImageIO.write(image, "png", new File("perlin.png"));
    }

}
