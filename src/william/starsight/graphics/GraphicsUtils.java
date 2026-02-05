package william.starsight.graphics;

import org.jetbrains.annotations.Contract;
import org.lwjgl.opengl.GL;
import william.starsight.Starsight;

import static org.lwjgl.opengl.GL43.*;

/**
 * Simple calls that may be useful
 *
 * @author William
 */
public final class GraphicsUtils {
	@Contract(" -> fail")
	private GraphicsUtils() {
	    throw new AssertionError("This class must not be instantiated.");
	}
	
	private static boolean initialized;
	
	/**
	 * Initializes OpenGL if it wasn't already initialized
	 */
	public static void initializeGL() {
		if (!initialized) {
			GL.createCapabilities();
			Starsight.LOG.fine("Initialized OpenGL version: " + glGetString(GL_VERSION));
		}
		initialized = true;
	}
	
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	@Contract(pure = true)
	public static boolean isGLInitialized() {
		return initialized;
	}
	
	/**
	 * Checks for an OpenGL error
	 *
	 * @param location The location at which the error occurs
	 */
	public static void checkGLError(String location) {
		int err = glGetError();
		while (err != GL_NO_ERROR) {
			Starsight.LOG.severe("OpenGL error at " + location + ": 0x" + Integer.toHexString(err));
			err = glGetError();
		}
	}
}
