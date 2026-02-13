package william.starsight.graphics.mesh;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

/**
 * A set of vertices recognized by OpenGL
 *
 * @author William
 */
public abstract class Mesh implements AutoCloseable {
	protected float[] vertexData;
	
	protected Mesh(float @NotNull [] vertexData) {
		this.vertexData = vertexData;
	}
	
	/**
	 * Uploads the mesh to the GPU
	 */
	public abstract void initialize();
	
	/**
	 * Draws the Mesh to the GPU
	 */
	public abstract void draw();
	
	/**
	 * Cleans up all resources associated with the Mesh, including the vertex data
	 */
	public final void cleanup() {
		vertexData = null;
		subclassCleanup();
	}
	
	/**
	 * This must be implemented by subclasses to clean up their own crap
	 */
	protected abstract void subclassCleanup();
	
	@Override
	public void close() {
		cleanup();
	}
}
