package william.starsight.util;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

/**
 * The error codes for the GLFW, packed into an enum for unnecessary reasons
 *
 * @author William
 */
public enum GLFWErrorCode {
	/**
	 * No error has occurred
	 */
	GLFW_NO_ERROR(GLFW.GLFW_NO_ERROR),
	/**
	 * This occurs if a GLFW function was called that must not be called unless the library is
	 * initialized through {@link GLFW#glfwInit()}
	 */
	GLFW_NOT_INITIALIZED(GLFW.GLFW_NOT_INITIALIZED),
	/**
	 * This occurs if a GLFW function was called that needs and operates on the current OpenGL
	 * or OpenGL ES context but no context is current on the calling thread. One such function
	 * is {@link GLFW#glfwSwapInterval(int)}.
	 */
	GLFW_NO_CURRENT_CONTEXT(GLFW.GLFW_NO_CURRENT_CONTEXT),
	/**
	 * One of the arguments to the function was an invalid enum value, for example requesting {@link GLFW#GLFW_RED_BITS} with {@link GLFW#glfwGetWindowAttrib(long,int)}
	 */
	GLFW_INVALID_ENUM(GLFW.GLFW_INVALID_ENUM),
	/**
	 * One of the arguments to the function was an invalid value
	 */
	GLFW_INVALID_VALUE(GLFW.GLFW_INVALID_VALUE),
	/**
	 * A memory allocation failed
	 */
	GLFW_OUT_OF_MEMORY(GLFW.GLFW_OUT_OF_MEMORY),
	/**
	 * GLFW could not find support for the requested API on the system
	 */
	GLFW_API_UNAVAILABLE(GLFW.GLFW_API_UNAVAILABLE),
	/**
	 * The requested OpenGL or OpenGL ES version is not available
	 */
	GLFW_VERSION_UNAVAILABLE(GLFW.GLFW_VERSION_UNAVAILABLE),
	/**
	 * A platform-specific error occurred that does not match any of the more specific categories
	 */
	GLFW_PLATFORM_ERROR(GLFW.GLFW_PLATFORM_ERROR),
	/**
	 * The requested format is not supported or available
	 *
	 * @apiNote If emitted during window creation, the requested pixel format is not supported.
	 * If emitted when querying the clipboard, the contents of the clipboard could not be converted to the requested format.
	 */
	GLFW_FORMAT_UNAVAILABLE(GLFW.GLFW_FORMAT_UNAVAILABLE),
	/**
	 * The specified window does not have an OpenGL or OpenGL ES context
	 */
	GLFW_NO_WINDOW_CONTEXT(GLFW.GLFW_NO_WINDOW_CONTEXT);
	
	private final int errorCode;
	
	private static final HashMap<Integer, GLFWErrorCode> ERROR_CODE_TO_INTEGER = new HashMap<>();
	
	static {
		for (GLFWErrorCode g : values()) {
			ERROR_CODE_TO_INTEGER.put(g.errorCode, g);
		}
	}
	
	GLFWErrorCode(int code) {
		this.errorCode = code;
	}
	
	/**
	 * Retrieves the enumerated key from the code returned by GLFW
	 *
	 * @param code The value returned by GLFW
	 * @return The type safe version
	 */
	public static GLFWErrorCode getFromInteger(int code) {
		return ERROR_CODE_TO_INTEGER.get(code);
	}
}
