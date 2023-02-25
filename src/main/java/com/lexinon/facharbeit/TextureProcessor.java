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
        int margin = Integer.parseInt(args[5]);

        BufferedImage destImage = new BufferedImage(srcImage.getWidth() + (2 * margin) * horizontalSubtextures, srcImage.getHeight() + (2 * margin) * verticalSubtextures, BufferedImage.TYPE_INT_ARGB);

        /*for(int h = 0; h < horizontalSubtextures; h++) {
            for(int v = 0; v < verticalSubtextures; v++) {
                for(int x = 0; x < subtextureSideLength; x++) {
                    for(int y = 0; y < subtextureSideLength; y++) {
                        destImage.setRGB((subtextureSideLength + 2 * margin) * h + x + 1, (subtextureSideLength + 2 * margin) * v + y + 1, srcImage.getRGB(subtextureSideLength * h + x, subtextureSideLength * v + y));
                    }
                }
                for(int x = 0; x < subtextureSideLength; x++) {
                    int x_ = (subtextureSideLength + 2 * margin) * h + x + 1;
                    destImage.setRGB(x_, (subtextureSideLength + 2 * margin) * v, srcImage.getRGB(subtextureSideLength * h + x, subtextureSideLength * (v + 1) - 1));
                    destImage.setRGB(x_, (subtextureSideLength + 2 * margin) * (v + 1) - 1 - additionalSpace, srcImage.getRGB(subtextureSideLength * h + x, subtextureSideLength * v));
                }
                for(int y = 0; y < subtextureSideLength; y++) {
                    int y_ = (subtextureSideLength + 2 * margin) * v + y + 1;
                    destImage.setRGB((subtextureSideLength + 2 * margin) * h, y_, srcImage.getRGB(subtextureSideLength * (h + 1) - 1, subtextureSideLength * v + y));
                    destImage.setRGB((subtextureSideLength + 2 * margin) * (h + 1) - 1 - additionalSpace, y_, srcImage.getRGB(subtextureSideLength * h, subtextureSideLength * v + y));
                }
            }
        }*/

        for(int h = 0; h < horizontalSubtextures; h++) {
            for(int v = 0; v < verticalSubtextures; v++) {
                for(int x = 0; x < subtextureSideLength + 2 * margin; x++) {
                    for(int y = 0; y < subtextureSideLength + 2 * margin; y++) {
                        //System.out.println("===");
                        //System.out.println(h * subtextureSideLength);
                        //System.out.println(Math.floorMod((x - margin), subtextureSideLength));
                        //System.out.println(v * subtextureSideLength);
                        //System.out.println(Math.floorMod((y - margin), subtextureSideLength));
                        destImage.setRGB(h * (subtextureSideLength + 2 * margin) + x, v * (subtextureSideLength + 2 * margin) + y, srcImage.getRGB(h * subtextureSideLength + Math.floorMod((x - margin), subtextureSideLength),
                                v * subtextureSideLength + Math.floorMod((y - margin), subtextureSideLength)));
                    }
                }
            }
        }

        ImageIO.write(destImage, "png", new File(dest));
    }

}
