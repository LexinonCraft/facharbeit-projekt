package com.lexinon.facharbeit;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextureProcessor {

    public static void main(String[] args) throws IOException {
        String src = args[0];
        String dest = args[1];

        BufferedImage srcImage = ImageIO.read(new File(src));
        int subtextureSideLength = Integer.parseInt(args[2]);
        int horizontalSubtextures = Integer.parseInt(args[3]);
        int verticalSubtextures = Integer.parseInt(args[4]);

        BufferedImage destImage = new BufferedImage(srcImage.getWidth() + 2 * horizontalSubtextures, srcImage.getHeight() + 2 * verticalSubtextures, BufferedImage.TYPE_INT_ARGB);

        for(int h = 0; h < horizontalSubtextures; h++) {
            for(int v = 0; v < verticalSubtextures; v++) {
                for(int x = 0; x < subtextureSideLength; x++) {
                    for(int y = 0; y < subtextureSideLength; y++) {
                        destImage.setRGB((subtextureSideLength + 2) * h + x + 1, (subtextureSideLength + 2) * v + y + 1, srcImage.getRGB(subtextureSideLength * h + x, subtextureSideLength * v + y));
                    }
                }
                for(int x = 0; x < subtextureSideLength; x++) {
                    destImage.setRGB((subtextureSideLength + 2) * h + x + 1, (subtextureSideLength + 2) * v, srcImage.getRGB(subtextureSideLength * h + x, subtextureSideLength * (v + 1) - 1));
                    destImage.setRGB((subtextureSideLength + 2) * h + x + 1, (subtextureSideLength + 2) * (v + 1) - 1, srcImage.getRGB(subtextureSideLength * h + x, subtextureSideLength * v));
                }
                for(int y = 0; y < subtextureSideLength; y++) {
                    destImage.setRGB((subtextureSideLength + 2) * h, (subtextureSideLength + 2) * v + y + 1, srcImage.getRGB(subtextureSideLength * (h + 1) - 1, subtextureSideLength * v + y));
                    destImage.setRGB((subtextureSideLength + 2) * (h + 1) - 1, (subtextureSideLength + 2) * v + y + 1, srcImage.getRGB(subtextureSideLength * h, subtextureSideLength * v + y));
                }
            }
        }

        ImageIO.write(destImage, "png", new File(dest));
    }

}
