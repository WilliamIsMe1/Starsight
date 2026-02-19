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
	protected int textureId = 0;
	
	public void bind() {
		if (textureId != 0)
			glBindTexture(GL_TEXTURE0, textureId);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE0, 0);
	}

	public abstract void initialize();

	public void cleanup() {
		subclassCleanup();
		if (textureId != 0)
			glDeleteTextures(textureId);
	}

	protected abstract void subclassCleanup();
}
