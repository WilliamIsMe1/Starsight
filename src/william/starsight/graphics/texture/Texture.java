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
	protected int textureId;
	
	public void bind() {
		glBindTexture(GL_TEXTURE0, textureId);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE0, 0);
	}
}
