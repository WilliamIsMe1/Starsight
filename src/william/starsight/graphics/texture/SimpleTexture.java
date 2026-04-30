package william.starsight.graphics.texture;

import org.lwjgl.stb.STBImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL43.*;

public final class SimpleTexture extends Texture {
    ByteBuffer imageData;
    private final String sourcePath;
    private final int format;

    public SimpleTexture(String path) throws FileNotFoundException {
        this.sourcePath = path;
        File verification = new File(path);
        if (!verification.exists()) throw new FileNotFoundException("This file does not exist.");
        if (path.trim().endsWith(".jpg") || path.trim().endsWith(".jpeg")) {
            format = GL_RGB;
        } else {
            format = GL_RGBA;
        }
    }

    @Override
    public void initialize() {
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        int[] width = new int[1], height = new int[1], nrChannels = new int[1];
        imageData = STBImage.stbi_load(sourcePath, width, height, nrChannels, 0);

        glTexImage2D(GL_TEXTURE_2D, 0, format, width[0], height[0], 0, format, GL_UNSIGNED_BYTE, imageData);
        glGenerateMipmap(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    protected void textureSubclassCleanup() {
        STBImage.stbi_image_free(imageData);
    }
}
