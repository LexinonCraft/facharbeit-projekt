package com.lexinon.facharbeit;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL12C.*;
import static org.lwjgl.opengl.GL30C.*;

public class TextureAtlas {

    public static final float SIDE_LENGTH_OF_ONE_TEXTURE = 1f / 32;
    public static final float TEXTURE_MARGIN = 1f / 3 * SIDE_LENGTH_OF_ONE_TEXTURE;

    private final int handle;

    public TextureAtlas() {
        InputStream inputStream = TextureAtlas.class.getResourceAsStream("/texture_atlas.png");
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load textures!");
        }
        int[] byteArray = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), byteArray, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for(int h = 0; h < image.getHeight(); h++) {
            for(int w = 0; w < image.getWidth(); w++) {
                int pixel = byteArray[h * image.getWidth() + w];

                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        handle = glGenTextures();
        if(handle == 0)
            throw new IllegalStateException("Could not generate texture atlas!");
        glBindTexture(GL_TEXTURE_2D, handle);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 4);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    public void activate(Game game) {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, handle);
        // TODO
        glUniform1i(game.voxelShader.getLoc("TextureAtlas"), 0);
    }

    public static Vector2f getTexCoords(int x, int y) {
        return new Vector2f(x, y).div(32);
    }

}
