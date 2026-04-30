package william.starsight.graphics.framebuffer;

import william.starsight.Starsight;
import william.starsight.graphics.GraphicsUtils;
import william.starsight.graphics.texture.Texture;

import java.awt.*;

import static org.lwjgl.opengl.GL43.*;

public class Framebuffer {
    private int FBO;
    private boolean isRecording;
    private FramebufferTexture tex;
    private final Color clearColor;

    public Framebuffer(Color clearColor) {
        FBO = 0;
        isRecording = false;
        tex = null;
        this.clearColor = clearColor;
    }

    public void initialize(int initWidth, int initHeight) {
        if (!GraphicsUtils.isGLInitialized()) {
            throw new UnsupportedOperationException("OpenGL is not initialized.");
        }

        if (FBO != 0) {
            Starsight.LOG.warning("Attempted double compilation");
            return; // Skip
        }

        FBO = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, FBO);

        int colorTexAttachment = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, colorTexAttachment);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, initWidth, initHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0L);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorTexAttachment, 0);

        tex = new FramebufferTexture(colorTexAttachment);

        int RBO = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, RBO);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, initWidth, initHeight);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);

        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, RBO);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new IllegalStateException("The framebuffer is not complete!");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

    }

    public void startRecord() {
        if (isRecording)
            return;
        glBindFramebuffer(GL_FRAMEBUFFER, FBO);
        isRecording = true;

        glClearColor(
                clearColor.getRed() / 255.0f,
                clearColor.getGreen() / 255.0f,
                clearColor.getBlue() / 255.0f,
                clearColor.getAlpha() / 255.0f
                );
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
    }

    public void stopRecord() {
        if (!isRecording)
            return;
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        isRecording = false;
    }

    public Texture getAsTexture() {
        return tex;
    }

    public void cleanup() {
        tex.cleanup();
        glDeleteFramebuffers(FBO);
        FBO = 0; // One is meant to be able to delete and resize the Framebuffer at will. this will be kinda glitchy
    }

    private static class FramebufferTexture extends Texture {
        FramebufferTexture(int texId) {
            textureId = texId;
        }

        @Override
        public void initialize() {

        }

        @Override
        protected void textureSubclassCleanup() {

        }
    }
}
