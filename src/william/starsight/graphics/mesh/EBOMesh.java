package william.starsight.graphics.mesh;

import org.jetbrains.annotations.NotNull;
import william.starsight.graphics.GraphicsUtils;

import static org.lwjgl.opengl.GL43.*;

public class EBOMesh extends Mesh {
    private final int[] indices;

    private final VertexFormat vertexFormat;

    private int VAO = 0, VBO = 0, EBO = 0;

    public EBOMesh(float @NotNull [] vertexData, @NotNull VertexFormat vertexFormat, int @NotNull [] indices) {
        super(vertexData);
        this.indices = indices;
        this.vertexFormat = vertexFormat;
    }

    @Override
    public void initialize() {
        if (!GraphicsUtils.isGLInitialized()) {
            throw new UnsupportedOperationException("This task cannot be performed until OpenGL is initialized");
        }

        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        vertexFormat.setupAttributes();

        glBindVertexArray(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void draw() {
        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0L);
    }

    @Override
    protected void subclassCleanup() {
        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);
        glDeleteVertexArrays(VAO);
    }
}
