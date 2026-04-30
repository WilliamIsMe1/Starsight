package william.starsight.graphics.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL43.*;

public class BufferedImageBasedTexture extends Texture {
    private ByteBuffer imgData;
    private final int width;
    private final int height;

    public BufferedImageBasedTexture(BufferedImage img, boolean usesAlpha) {
        int width = img.getWidth();
        int height = img.getHeight();
        imgData = MemoryUtil.memAlloc(width * height * 4);

        this.width = width;
        this.height = height;


        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                int pixel = img.getRGB(x, y);
                byte alpha = (byte) ((pixel >> 24) & 0xFF);
                byte red = (byte) ((pixel >> 16) & 0xFF);
                byte green = (byte) ((pixel >> 8) & 0xFF);
                byte blue = (byte) (pixel & 0xFF);
                imgData.put(red);
                imgData.put(green);
                imgData.put(blue);
                imgData.put(usesAlpha ? alpha : (byte) 255);
            }
        }
        imgData.flip();
    }

    @Override
    public void initialize() {
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imgData);
        glGenerateMipmap(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    protected void textureSubclassCleanup() {
        imgData.clear();
        MemoryUtil.memFree(imgData); // Clear it entirely :D
    }
}
