package william.starsight.core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

/**
 * This interface defines a program that can be run by a window
 *
 * @author William
 */
public interface Program extends AutoCloseable {
	/**
	 * Initializes the program. Meant to be called AFTER openGL is initialized through {@link GL#createCapabilities()}
	 *
	 * @param initWidth  The starting width, necessary for certain perspective shaders
	 * @param initHeight The starting height, necessary for certain perspective shaders
	 * @param startTime  The starting time, necessary for calculating accurate delta time with calls to {@link GLFW#glfwGetTime()}
	 */
	void initialize(int initWidth, int initHeight, double startTime);
	
	/**
	 * Ticks with the given delta time
	 */
	void tick();
	
	/**
	 * Renders the program
	 */
	void render();
	
	/**
	 * This cleans up the entire program to make it ready for shutdown
	 */
	void cleanup();
	
	/**
	 * I override it so that I can simply call cleanup at the end of a try block
	 * <br>
	 * <pre>
	 * try (Program p = new SimpleProgram()) {
	 *     p.initialize(800, 600, glfwGetTime());
	 *     while (!p.shouldClose()) {
	 *         p.tick();
	 *         p.render();
	 *     }
	 * }</pre>
	 *
	 * @apiNote Ideally this should not be overridden, as all relevant functionality should be located in the
	 * {@link #cleanup()} method, in the likely case that this method isn't called at all, such as most places
	 * outside a try-with-resources block.
	 */
	@Override
	default void close() {
		cleanup();
	}
	
	/**
	 * This allows the program to want to exit the code
	 *
	 * @return Whether the program should close
	 */
	boolean shouldClose();
	
	/**
	 * Called whenever the framebuffer resizes
	 *
	 * @param newWidth  The new width
	 * @param newHeight The new height
	 */
	void resized(int newWidth, int newHeight);
	
	/**
	 * Takes a key event from the window as part of the input callback system
	 *
	 * @param key    The key, defined as a {@link GLFW} constant
	 * @param action The action, defined as a {@link GLFW} constant
	 * @param mods
	 */
	void takeKeyEvent(int key, int action, int mods);
}