import org.joml.Matrix4f;
import org.joml.Vector3f;
import william.starsight.Starsight;
import william.starsight.core.Program;
import william.starsight.core.Window;
import william.starsight.graphics.GraphicsUtils;
import william.starsight.graphics.mesh.Mesh;
import william.starsight.graphics.mesh.SimpleMesh;
import william.starsight.graphics.mesh.VertexFormat;
import william.starsight.graphics.mesh.VertexFormatType;
import william.starsight.graphics.shader.ShaderCompilationException;
import william.starsight.graphics.shader.ShaderLinkingException;
import william.starsight.graphics.shader.ShaderProgram;
import william.starsight.util.Camera;
import william.starsight.util.KeyboardKey;

import static org.lwjgl.glfw.GLFW.*;

@SuppressWarnings("ALL")
public class Main implements Program {
	Mesh mesh = null;
	ShaderProgram shader = null;
	Camera camera = new Camera(new Vector3f(0, 0, 0), 0.0f, 0.0f);
	float aspect = 0.0f;
	
	public static void main(String[] args) {
		Window w = new Window(new Main());
		Thread windowThread = new Thread(w);
		windowThread.start();
		w.setClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	/**
	 * Initializes the program. Meant to be called AFTER openGL is initialized through {@link org.lwjgl.opengl.GL#createCapabilities()}
	 *
	 * @param initWidth  The starting width, necessary for certain perspective shaders
	 * @param initHeight The starting height, necessary for certain perspective shaders
	 * @param startTime  The starting time, necessary for calculating accurate delta time with calls to {@link org.lwjgl.glfw.GLFW#glfwGetTime()}
	 */
	@Override
	public void initialize(int initWidth, int initHeight, double startTime) {
		aspect = (float) initWidth / initHeight;
		float[] vertices = {
			//  x,     y,    z,    r,    g,    b
			-0.5f, -0.5f, -5.0f, 1.0f, 0.0f, 0.0f,
			 0.5f, -0.5f, -5.0f, 0.0f, 1.0f, 0.0f,
			 0.0f,  0.5f, -5.0f, 0.0f, 0.0f, 1.0f
		};
		VertexFormat vtx = VertexFormat.of(VertexFormatType.VEC3, VertexFormatType.VEC3); // Position, color
		mesh = new SimpleMesh(vertices, vtx);
		mesh.initialize();
		GraphicsUtils.checkGLError("After mesh initialization");
		
		shader = new ShaderProgram("""
			#version 430
            
            layout(location = 0) in vec3 pos;
            layout(location = 1) in vec3 color;
            
            out vec3 outColor;
            
            uniform mat4 p;
			uniform mat4 v;
            
            void main() {
                gl_Position = p * v * vec4(pos, 1.0);
                outColor = color;
            }
			""", """
				#version 430
				
				in vec3 outColor;
				
				out vec4 FragColor;
				
				void main() {
					FragColor = vec4(outColor, 1.0);
				}
				""");
		
		try {
			shader.compileAndLink();
			GraphicsUtils.checkGLError("after shader compilation and linking");
		} catch (ShaderCompilationException e) {
			Starsight.LOG.severe("Shaders failed to compile");
			throw new RuntimeException(e);
		} catch (ShaderLinkingException e) {
			Starsight.LOG.severe("Shaders failed to link");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Ticks with the given delta time
	 */
	@Override
	public void tick() {
	
	}
	
	/**
	 * Renders the program
	 */
	@Override
	public void render() {
		shader.bind();
		shader.setUniform("p", Camera.getPerspectiveMatrix(70.0f, aspect));
		shader.setUniform("v", camera.getCameraMatrix());
//		shader.setUniform("m", new Matrix4f().identity());
		mesh.draw();
		GraphicsUtils.checkGLError("After mesh drawing");
		shader.unbind();
	}
	
	/**
	 * This cleans up the entire program to make it ready for shutdown
	 */
	@Override
	public void cleanup() {
		mesh.cleanup();
		shader.cleanup();
	}
	
	/**
	 * This allows the program to want to exit the code
	 *
	 * @return Whether the program should close
	 */
	@Override
	public boolean shouldClose() {
		return shouldClose;
	}
	
	private boolean shouldClose = false;
	
	/**
	 * Called whenever the framebuffer resizes
	 *
	 * @param newWidth  The new width
	 * @param newHeight The new height
	 */
	@Override
	public void resized(int newWidth, int newHeight) {
		aspect = (float) newWidth / newHeight;
	}
	
	/**
	 * Takes a key event from the window as part of the input callback system
	 *
	 * @param key    The key, defined as a {@link org.lwjgl.glfw.GLFW} constant
	 * @param action The action, defined as a {@link org.lwjgl.glfw.GLFW} constant
	 * @param mods
	 */
	@Override
	public void takeKeyEvent(int key, int action, int mods) {
		KeyboardKey keyEnum = KeyboardKey.fromKeyCode(key);
		if (keyEnum == KeyboardKey.KEY_ESCAPE && action == GLFW_RELEASE) {
			shouldClose = true;
		}
		if (keyEnum == KeyboardKey.KEY_UP && action != GLFW_RELEASE) {
			camera.setPitchDeg(camera.getPitchDeg() + 1);
		}
		if (keyEnum == KeyboardKey.KEY_DOWN && action != GLFW_RELEASE) {
			camera.setPitchDeg(camera.getPitchDeg() - 1);
		}
		if (keyEnum == KeyboardKey.KEY_LEFT && action != GLFW_RELEASE) {
			camera.setYawDeg(camera.getYawDeg() - 1);
		}
		if (keyEnum == KeyboardKey.KEY_RIGHT && action != GLFW_RELEASE) {
			camera.setYawDeg(camera.getYawDeg() + 1);
		}
		if (keyEnum == KeyboardKey.KEY_W && action != GLFW_RELEASE) {
			camera.getPosition().add(camera.getForwardVector().normalize(0.1f));
		}
	}
}
