package william.starsight.graphics.mesh;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * This represents the vertex format used by mesh vertices
 *
 * @author William
 */
public class VertexFormat {
	private final VertexFormatType[] vaoFormat;
	
	@Contract(pure = true)
	private VertexFormat(VertexFormatType... vft) {
		this.vaoFormat = vft;
	}
	
	@NotNull
	private static final HashMap<List<VertexFormatType>, VertexFormat> CACHE = new HashMap<>();
	
	/**
	 * Sets up the VAO based on the format list
	 */
	public void setupAttributes() {
		int stride = 0;
		for (VertexFormatType v : vaoFormat) {
			stride += v.size;
		}
		
		int offset = 0;
		for (int i = 0; i < vaoFormat.length; i++) {
			VertexFormatType type = vaoFormat[i];
			type.setup(i, offset, stride);
			offset += type.size;
		}
	}
	
	/**
	 * Fetches the format from the format types
	 *
	 * @param vft The list of types
	 * @return The respective vertex format
	 */
	public static @NotNull VertexFormat of(VertexFormatType... vft) {
		List<VertexFormatType> key = List.of(vft);
		return CACHE.computeIfAbsent(key, k -> new VertexFormat(vft.clone())); // IntelliJ, CACHE CANNOT BE NULL. SO SHUT UP.
	}
	
	// In VertexFormat.java — add this method
	public int getStrideInBytes() {
		int stride = 0;
		for (VertexFormatType v : vaoFormat) stride += v.size;
		return stride;
	}
	
	public int getStrideInFloats() {
		return getStrideInBytes() / Float.BYTES;   // assuming everything is float-based
	}
}