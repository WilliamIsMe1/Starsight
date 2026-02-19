package william.starsight.graphics.texture;

import org.lwjgl.stb.STBImage;

import static org.lwjgl.opengl.GL43.*;

public class SimpleTexture extends Texture {
    private String sourcePath;

    public SimpleTexture(String path) {
        this.sourcePath = path;
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
        var imageData = STBImage.stbi_load(sourcePath, width, height, nrChannels, 0);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width[0], height[0], 0, GL_RGB, GL_UNSIGNED_BYTE, imageData);
        glGenerateMipmap(GL_TEXTURE_2D);
        STBImage.stbi_image_free(imageData);

    }

    @Override
    protected void subclassCleanup() {
        // Nothing needed here
    }
}
