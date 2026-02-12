package william.starsight.graphics.mesh;

import static org.lwjgl.opengl.GL43.*;

/**
 * Vertex format type class
 *
 * @author William
 */
public enum VertexFormatType {
	/**
	 * Represents one byte
	 */
	BYTE(1, Byte.BYTES, GL_BYTE),
	/**
	 * Represents one short integer
	 */
	SHORT(1, Short.BYTES, GL_SHORT),
	/**
	 * Represents one integer
	 */
	INT(1, Integer.BYTES, GL_INT),
	/**
	 * Represents one unsigned integer
	 */
	UINT(1, Integer.BYTES, GL_UNSIGNED_INT),
	/**
	 * Represents one floating point number
	 */
	FLOAT(1, Float.BYTES, GL_FLOAT),
	/**
	 * Represents two floating point numbers
	 */
	VEC2(2, Float.BYTES * 2, GL_FLOAT),
	/**
	 * Represents 3 floating point numbers
	 */
	VEC3(3, Float.BYTES * 3, GL_FLOAT),
	/**
	 * Represents 4 floating point numbers
	 */
	VEC4(4, Float.BYTES * 4, GL_FLOAT),
	;
	
	final int components;  // Number of components (1-4)
	final int size;        // Total size in bytes
	final int glType;      // Base type
	
	VertexFormatType(int components, int size, int glType) {
		this.components = components;
		this.size = size;
		this.glType = glType;
	}
	
	void setup(int index, int bytesSoFar, int stride) {
		glEnableVertexAttribArray(index);
		glVertexAttribPointer(index, components, glType, false, stride, bytesSoFar);
	}
}