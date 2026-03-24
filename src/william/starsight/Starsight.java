package william.starsight;

import org.jetbrains.annotations.Contract;

import william.coreutils.starchart.StarchartLogger;


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
	public static final StarchartLogger LOG = StarchartLogger.ROOT.createChild("starsight", StarchartLogger.Level.INFO);

    /**
     * The logger for LWJGL
     */
    public static final StarchartLogger LWJGL = LOG.createChild("lwjgl", StarchartLogger.Level.FINE);

}
