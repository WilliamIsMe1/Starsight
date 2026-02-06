package william.starsight.graphics.shader;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.joml.Matrix4f;
import william.starsight.Starsight;

import org.lwjgl.opengl.GL;
import william.starsight.graphics.GraphicsUtils;

import static org.lwjgl.opengl.GL43.*;

/**
 * The shader class for Starsight
 *
 * @author William
 */
public class ShaderProgram implements AutoCloseable {
	private final String vertexSource, fragmentSource;

	private int id;

	/**
	 * Constructs a new ShaderProgram
	 *
	 * @param vertexSource The vertex shader code
	 * @param fragmentSource The fragment shader code
	 */
	public ShaderProgram(@Language("Glsl") String vertexSource, @Language("Glsl") String fragmentSource) {
		this.vertexSource = vertexSource;
		this.fragmentSource = fragmentSource;
		this.id = 0;
	}

	/**
	 * Compiles and links the shaders
	 *
	 * @throws ShaderCompilationException If the shader cannot compile
	 * @throws ShaderLinkingException If the shader cannot link
	 *
	 * @apiNote The shader will wait to compile both shaders before throwing errors so one error does not mask the other. Obviously it cannot link if there are uncompiled shaders, so no ShaderLinkingException can be thrown
	 *
	 * @apiNote DO NOT RUN BEFORE {@link GL#createCapabilities()} OR THIS WILL CRASH
	 */
	public void compileAndLink() throws ShaderCompilationException, ShaderLinkingException {
		if (!GraphicsUtils.isGLInitialized()) {
			throw new UnsupportedOperationException("This task cannot be performed until OpenGL is initialized.");
		}
		
		if (id != 0) {
			Starsight.LOG.warning("Attempted double compilation");
			return; // Just skip it.
		}
		
		int vert = glCreateShader(GL_VERTEX_SHADER);
		int frag = glCreateShader(GL_FRAGMENT_SHADER);

		glShaderSource(vert, vertexSource);
		glShaderSource(frag, fragmentSource);

		glCompileShader(vert);
		glCompileShader(frag);

		String vertError = null;
		String fragError = null;

		if (glGetShaderi(vert, GL_COMPILE_STATUS) == GL_FALSE) {
			vertError = glGetShaderInfoLog(vert, Starsight.MAX_OPENGL_ERROR_LENGTH);
		}
		
		if (glGetShaderi(frag, GL_COMPILE_STATUS) == GL_FALSE) {
			fragError = glGetShaderInfoLog(frag, Starsight.MAX_OPENGL_ERROR_LENGTH);
		}
		
		if (vertError != null || fragError != null) {
			StringBuilder sb = new StringBuilder("Shader compilation failed:\n");
			if (vertError != null) {
				sb.append("Vertex shader:\n").append(vertError).append("\n");
			}
			if (fragError != null) {
				sb.append("Fragment shader:\n").append(fragError).append("\n");
			}
			throw new ShaderCompilationException(sb.toString());
		}
		
		id = glCreateProgram();
		
		glAttachShader(id, vert);
		glAttachShader(id, frag);
		
		glLinkProgram(id);
		
		if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
			throw new ShaderLinkingException("Shader failed to link: " + glGetProgramInfoLog(id, Starsight.MAX_OPENGL_ERROR_LENGTH));
		}
		
		glDeleteShader(vert);
		glDeleteShader(frag);
	}
	
	/**
	 * Binds the shader for use
	 */
	public void bind() {
		glUseProgram(id);
	}
	
	/**
	 * Unbinds the shader
	 *
	 * @apiNote Kept as instance method for consistency.
	 */
	@SuppressWarnings("MethodMayBeStatic")
	public void unbind() {
		glUseProgram(0);
	}
	
	/**
	 * Cleans up the shader
	 */
	public void cleanup() {
		if (id != 0) {
			glDeleteProgram(id);
			id = 0;
		}
	}
	
	public int getId() {
		if (id == 0) {
			throw new IllegalStateException("Shader program not yet compiled/linked");
		}
		return id;
	}
	
	@Override
	public void close() {
		cleanup();
	}

	/**
	 * Checks for the existence of a uniform in the shader code
	 *
	 * @param uniformName The name of the uniform
	 * @return Whether the uniform is an active variable in the shader code
	 */
	@Contract(pure = true)
	public boolean doesUniformExist(String uniformName) {
		return glGetUniformLocation(id, uniformName) != -1; // Not as clear-cut as it may seem, as there are edge cases
	}
	
	/**
	 * Sets a uniform's value
	 *
	 * @param uniformName The name
	 * @param value The value
	 */
	public void setUniform(String uniformName, int value) {
		if(!doesUniformExist(uniformName)) {
			Starsight.LOG.warning("Uniform " + uniformName + " does not exist. Skipping...");
			return;
		}
		glUniform1i(getUniformLocation(uniformName), value);
	}
	
	/**
	 * Sets a uniform's value
	 *
	 * @param uniformName The name
	 * @param value The value
	 */
	public void setUniform(String uniformName, float value) {
		if(!doesUniformExist(uniformName)) {
			Starsight.LOG.warning("Uniform " + uniformName + " does not exist. Skipping...");
			return;
		}
		glUniform1f(getUniformLocation(uniformName), value);
	}
	
	/**
	 * Sets a uniform's value
	 *
	 * @param uniformName The name
	 * @param value The value
	 */
	public void setUniform(String uniformName, double value) {
		if(!doesUniformExist(uniformName)) {
			Starsight.LOG.warning("Uniform " + uniformName + " does not exist. Skipping...");
			return;
		}
		glUniform1d(getUniformLocation(uniformName), value);
	}
	
	/**
	 * Sets a uniform's value
	 *
	 * @param uniformName The name
	 * @param value The value
	 */
	public void setUniform(String uniformName, Matrix4f value) {
		if (!doesUniformExist(uniformName)) {
			Starsight.LOG.warning("Uniform " + uniformName + " does not exist. Skipping...");
			return;
		}
		glUniformMatrix4fv(getUniformLocation(uniformName), false, value.get(new float[4*4]));
	}
	
	@Contract(pure = true) // Doesn't mutate state I think
	private int getUniformLocation(String uniformName) {
		return glGetUniformLocation(id, uniformName);
	}
}
