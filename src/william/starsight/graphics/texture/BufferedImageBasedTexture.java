package william.starsight.graphics.texture;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL43.*;

public class BufferedImageBasedTexture extends Texture { // TODO Finish for use in TextureAtlas
    ByteBuffer imgData;

    public BufferedImageBasedTexture(BufferedImage img) { // TODO Create a way to get images outside of ImageIO to avoid ABGR
        imgData = ByteBuffer.allocateDirect(0);

    }

    @Override
    public void initialize() {
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);


    }

    @Override
    protected void textureSubclassCleanup() {
        // TODO Method stub
    }
}
