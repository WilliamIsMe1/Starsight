package william.starsight.core;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.*;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryUtil;
import william.starsight.Starsight;
import william.starsight.graphics.GraphicsUtils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * The window class for Starsight
 *
 * @author William
 */
public class Window implements Runnable {
	
	private static final int DEFAULT_DIMENSIONS = 600;
	
	private final Program program;
	
	private long windowHandle;
	
	private int width;
	private int height;
	private String title;
	
	@SuppressWarnings({"MagicNumber", "RedundantSuppression"}) // Suppress one, it complains about redundant suppression on others. Suppress that :)
	private float r = 0.6f, g = 0.9f, b = 0.9f, a = 1.0f;
	
	private GLFWErrorCallback errorCallback = null;
	private GLFWFramebufferSizeCallback framebufferSizeCallback = null;
	private GLFWWindowCloseCallback windowCloseCallback = null;
	private GLFWKeyCallback keyCallback = null;
	private GLFWMouseButtonCallback mouseButtonCallback = null;
	private GLFWWindowPosCallback repositionCallback = null;
	private GLFWCursorPosCallback mouseMoveCallback = null;
	
	/**
	 * Constructs a window with the given program
	 *
	 * @param program An object whose class conforms to the expectations set by the {@link Program} interface
	 */
	public Window(Program program) {
		this(DEFAULT_DIMENSIONS, DEFAULT_DIMENSIONS, "Starsight Application", program);
	}
	
	/**
	 * Constructs a window with the given parameters
	 *
	 * @param width The width of the window
	 * @param height The height of the window
	 * @param title The title of the window
	 * @param program An object whose class conforms to the expectations set by the {@link Program} interface
	 */
	public Window(int width, int height, @NotNull String title, @NotNull Program program) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.program = program;
	}
	
	@Override
	public void run() {
		try {
			init();
			loop();
		} catch (Exception e) {
			Starsight.LOG.severe("Something bad happened: " + e.getClass().getSimpleName() + " said " + e.getMessage()); // This is a clear log of everything that went wrong
			Starsight.LOG.severe("At: " + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber());
			Starsight.LOG.severe("At: " + e.getStackTrace()[1].getFileName() + ":" + e.getStackTrace()[1].getLineNumber());
			Starsight.LOG.severe("At: " + e.getStackTrace()[2].getFileName() + ":" + e.getStackTrace()[2].getLineNumber());
			Starsight.LOG.severe("At: " + e.getStackTrace()[3].getFileName() + ":" + e.getStackTrace()[3].getLineNumber());
			Starsight.LOG.severe("At: " + e.getStackTrace()[4].getFileName() + ":" + e.getStackTrace()[4].getLineNumber());
		} finally {
			cleanup(); // Make SURE to do this. If this isn't done, there could be leaks
		}
	}
	
	/**
	 * Sets the dimensions of the window, calling the linked callback if the size changes.
	 *
	 * @param width The new width
	 * @param height The new height
	 */
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
		glfwSetWindowSize(windowHandle, width, height);
	}
	
	/**
	 * Sets the title of the window
	 *
	 * @param title The title of the program
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Sets the background color of the window
	 *
	 * @param r Red component
	 * @param g Green component
	 * @param b Blue component
	 * @param a Alpha component
	 */
	public void setClearColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public void setRawMode(boolean rawMode) {
		glfwSetInputMode(windowHandle, GLFW_RAW_MOUSE_MOTION, rawMode ? GLFW_TRUE : GLFW_FALSE);
	}
	
	private void init() {
		if (!glfwInit()) {
			int errorCode = nglfwGetError(MemoryUtil.NULL);
			throw new IllegalStateException("GLFW is uninitialized: Error code " + errorCode);
		}
		Starsight.LOG.fine("Initialized GLFW version " + glfwGetVersionString()); // Retrieves the version
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		
		windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
		
		glfwMakeContextCurrent(windowHandle);
		
		errorCallback = glfwSetErrorCallback(this::takeError);
		keyCallback = glfwSetKeyCallback(windowHandle, this::takeKeyEvent);
		framebufferSizeCallback = glfwSetFramebufferSizeCallback(windowHandle, this::takeResizeEvent);
		mouseMoveCallback = glfwSetCursorPosCallback(windowHandle, this::takeMouseMovementEvent);
		mouseButtonCallback = glfwSetMouseButtonCallback(windowHandle, this::takeMouseButtonEvent);
		windowCloseCallback = glfwSetWindowCloseCallback(windowHandle, this::closeWindow);
		
		GraphicsUtils.initializeGL();
		
		glViewport(0, 0, width, height);
		glfwSwapInterval(0);
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		
		
		program.initialize(this, width, height, glfwGetTime());
	}

	private void closeWindow(long windowId) { // Let's see if this works.
		glfwSetWindowShouldClose(windowId, true);
	}

	@SuppressWarnings("MethodMayBeStatic")
	private void takeError(int errorCode, long descriptionPointer) {
		String description = MemoryUtil.memUTF8(descriptionPointer);
		Starsight.LOG.severe("[LWJGL] Error(" + errorCode + ") " + description);
	}
	
	private void takeResizeEvent(long windowId, int newWidth, int newHeight) {
		this.width = newWidth;
		this.height = newHeight;
		program.resized(newWidth, newHeight);
		glViewport(0, 0, newWidth, newHeight);
	}
	
	private void takeKeyEvent(long windowId, int key, int scancode, int action, int mods) {
		program.takeKeyEvent(key, action, mods);
	}

	private void takeMouseMovementEvent(long windowId, double xPos, double yPos) {
		program.takeMouseMovementEvent(xPos, yPos);
	}

	private void takeMouseButtonEvent(long windowId, int button, int action, int mods) {
		program.takeMouseButtonEvent(button, action, mods);
	}
	
	/// Simply loops and calls the program tick and render functions
	private void loop() {
		while (!glfwWindowShouldClose(windowHandle) && !program.shouldClose()) {
			glClearColor(r, g, b, a);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			program.tick();
			program.render();
			
			glfwSwapBuffers(windowHandle);
			glfwPollEvents();
		}
	}
	
	/// Cleans up callbacks and program resources
	private void cleanup() {
		freeIfNotNull(errorCallback);
		freeIfNotNull(framebufferSizeCallback);
		freeIfNotNull(windowCloseCallback);
		freeIfNotNull(keyCallback);
		freeIfNotNull(mouseButtonCallback);
		freeIfNotNull(repositionCallback);
		freeIfNotNull(mouseMoveCallback);
		program.cleanup();
	}
	
	/// Small utility method to clear non-null callbacks
	private static void freeIfNotNull(Callback callback) {
		if (callback != null) {
			callback.free();
		}
	}
}
