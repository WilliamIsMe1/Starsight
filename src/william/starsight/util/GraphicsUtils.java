package william.starsight.util;

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
	 * Checks for an OpenGL error and then repeats doing so.
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

    private static boolean doesDepthTesting = false;
    private static CullFaceMode cullFaceMode = CullFaceMode.CCW;

    /**
     * Sets depth testing
     */
    public static void setDepthTest(boolean depthTest) {
        doesDepthTesting = depthTest;
        if (depthTest) {
            glEnable(GL_DEPTH_TEST);
        } else {
            glDisable(GL_DEPTH_TEST);
        }
    }

    /**
     * Gets the status of the depth test feature
     */
    public static boolean getDepthTest() {
        return doesDepthTesting;
    }

    public enum CullFaceMode {
        NO_CULL,
        CCW,
        CW
    }
}
