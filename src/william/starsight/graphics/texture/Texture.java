package william.starsight.graphics.texture;

import static org.lwjgl.opengl.GL43.*;

/**
 * The texture class for Starsight, extendable to make texture atlases
 *
 * @author William
 *
 * @implNote DO NOT USE ANYTHING EXCEPT FOR DIRECT BUFFERS
 */
public abstract class Texture {
    protected boolean isBound = false;
	protected int textureId = 0;
	
	public void bind() {
        if (isBound)
            return;
        if (textureId != 0) {
            glBindTexture(GL_TEXTURE_2D, textureId);
            isBound = true;
        }
	}

	public void unbind() {
        if (!isBound)
            return;
        glBindTexture(GL_TEXTURE_2D, 0);
        isBound = false;
	}

	public abstract void initialize();

	public void cleanup() {
		textureSubclassCleanup();
		if (textureId != 0)
			glDeleteTextures(textureId);
	}

	protected abstract void textureSubclassCleanup();
}
