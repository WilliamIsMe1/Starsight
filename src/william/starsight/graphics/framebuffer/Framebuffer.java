package william.starsight.graphics.framebuffer;

import william.starsight.Starsight;
import william.starsight.graphics.GraphicsUtils;
import william.starsight.graphics.texture.Texture;

import static org.lwjgl.opengl.GL43.*;

public class Framebuffer {
    private int FBO;
    private boolean isRecording;

    public Framebuffer() {
        FBO = 0;
        isRecording = false;
    }

    public void initialize() {
        if (!GraphicsUtils.isGLInitialized()) {
            throw new UnsupportedOperationException("OpenGL is not initialized.");
        }

        if (FBO != 0) {
            Starsight.LOG.warning("Attempted double compilation");
            return; // Skip
        }

        // TODO Finish setting up framebuffer
        FBO = glGenFramebuffers();


    }

    public void startRecord() {
        if (isRecording)
            return;
        glBindFramebuffer(GL_FRAMEBUFFER, FBO);
        isRecording = true;
    }

    public void stopRecord() {
        if (!isRecording)
            return;
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        isRecording = false;
    }

    public Texture flushToTexture() {
        // TODO Method stub
        return null;
    }

    public void cleanup() {
        glDeleteFramebuffers(FBO);
    }
}
