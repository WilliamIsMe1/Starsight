package william.starsight.graphics.mesh;

import org.jetbrains.annotations.NotNull;
import william.starsight.graphics.GraphicsUtils;

import static org.lwjgl.opengl.GL43.*;

/**
 * A simple mesh class that only uses VBO, no EBO
 *
 * @author William
 */
public class SimpleMesh extends Mesh {
	private int VBO, VAO;
	
	private final VertexFormat vertexFormat;
	private final int vertexCount;
	
	/**
	 * Constructs a mesh with no indices, just using simple triangles
	 *
	 * @param vertexData The starting buffer containing the data
	 * @param vertexFormat The vertex format used by the buffer
	 *
	 * @apiNote {@code startingBuffer} will be cloned into a direct buffer
	 */
	public SimpleMesh(float @NotNull [] vertexData, @NotNull VertexFormat vertexFormat) {
		super(vertexData);
		this.vertexFormat = vertexFormat;
		this.vertexCount = vertexData.length / (vertexFormat.getStrideInFloats());
	}

	@Override
	public void initialize() {
		if (!GraphicsUtils.isGLInitialized()) {
			throw new UnsupportedOperationException("This task cannot be performed until OpenGL is initialized.");
		}
		
		VBO = glGenBuffers();
		
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW); // Put in the direct FloatBuffer
		
		vertexFormat.setupAttributes();
		
		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);   // can be added after glBufferData(...)
	}

	@Override
	public void render() {
		glBindVertexArray(VAO);
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
	}

	@Override
	protected void subclassCleanup() {
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
	}
}
