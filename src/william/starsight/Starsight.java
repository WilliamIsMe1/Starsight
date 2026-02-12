package william.starsight;

import org.jetbrains.annotations.Contract;

import java.util.logging.Logger;

/**
 * The library behind my 3d rendering code
 *
 * @author William
 */
public final class Starsight {
	/**
	 * The max allowed length of OpenGL error logs
	 */
	public static final int MAX_OPENGL_ERROR_LENGTH = 1024;

	/**
	 * Whether Starsight is in debug mode
	 */
	public static final boolean DEBUG = false;

	@Contract(" -> fail")
	private Starsight() {
	    throw new AssertionError("This class must not be instantiated.");
	}

	/**
	 * The version of Starsight
	 */
	public static final String VERSION = "1.0.0-alpha";

	/**
	 * The logger for the class
	 */
	public static final Logger LOG = Logger.getLogger("starsight");

}
