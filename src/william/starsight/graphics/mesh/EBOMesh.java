package william.starsight.graphics.mesh;

import org.jetbrains.annotations.NotNull;
import william.starsight.graphics.GraphicsUtils;

import static org.lwjgl.opengl.GL43.*;

/**
 * This is an EBO-based mesh that uses indices to save space when uploading to GPU
 *
 * @author William
 */
public class EBOMesh extends Mesh {
    @SuppressWarnings("FieldMayBeFinal")
    private int[] indices;

    private int VAO, VBO, EBO;

    private final VertexFormat vtx;

    /**
     * Constructs an EBO mesh
     *
     * @param vertexData The raw vertex data in a {@code float[]}
     * @param indices The indices in an {@code int[]}
     * @param vertexFormat The vertex format, as a {@link VertexFormat} object
     */
    public EBOMesh(float @NotNull [] vertexData, int @NotNull [] indices, @NotNull VertexFormat vertexFormat) {
        super(vertexData);
        this.indices = indices.clone();
        this.vtx = vertexFormat;
    }

    @Override
    public void initialize() {
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        vtx.setupAttributes();

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void draw() {
        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0L); // I think this works...
    }

    @Override
    protected void subclassCleanup() {
        glDeleteVertexArrays(VAO);
        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);
    }
}
