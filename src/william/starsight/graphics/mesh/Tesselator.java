package william.starsight.graphics.mesh;

import org.joml.Vector3f;

import java.util.Vector;

import static org.lwjgl.opengl.GL43.*;

/**
 * This specifically assumes a vertex format of POS, UV, NORMALS, and is not meant to be used for entity models. MovingObjectTesselator should cover that
 */
public class Tesselator {
    private static int[] indexBuffer = new int[4194304];

    public static final Tesselator INSTANCE = new Tesselator((int) (32 * 32 * 32 * 24 * 1.25)); // Starting capacity of 983040 floats

    private float[] buffer;
    private int bufferSize;
    private Orientation orientation = Orientation.CCW;
    private int indexCount;

    private Tesselator(int startingCapacity) {
        buffer = new float[startingCapacity];
        bufferSize = 0;
        indexCount = 0;
    }

    public void setOrientation(Orientation o) {
        this.orientation = o;
        glFrontFace(o == Orientation.CW ? GL_CW : GL_CCW);
    }

    public enum Orientation {
        CW,
        CCW
    }

    public void addQuad(float x, float y, float z, QuadDirection direction, float width, float height, float minU, float minV, float maxU, float maxV) {
        float nX = direction.x();
        float nY = direction.y();
        float nZ = direction.z();

        float x1 = x, x2;
        float y1 = y, y2;
        float z1 = z, z2;

        switch (direction) {
            case NEG_X -> {

            }
            case NEG_Y -> {

            }
            case NEG_Z -> {

            }
            case POS_X -> {

            }
            case POS_Y -> {

            }
            case POS_Z -> {

            }
        }

        float newQuad[] = {
             // x,  y,  z,    u,    v, nx, ny, nz
                x,  y,  z, minU, minV, nX, nY, nZ
        };
        indexCount++;
    }

    public enum QuadDirection {
        NEG_X(new Vector3f(-1, 0, 0)),
        NEG_Y(new Vector3f(0, -1, 0)),
        NEG_Z(new Vector3f(0, 0, -1)),
        POS_X(new Vector3f(1, 0, 0)),
        POS_Y(new Vector3f(0, 1, 0)),
        POS_Z(new Vector3f(0, 0, 1));
        private final Vector3f normal;
        QuadDirection(Vector3f normal) {
            this.normal = normal;
        }
        public float x() {
            return normal.x;
        }
        public float y() {
            return normal.y;
        }
        public float z() {
            return normal.z;
        }
    }

    public Mesh flushMesh() {
        return new EBOMesh(,generateIndices(indexCount),VertexFormat.of(VertexFormatType.VEC3, VertexFormatType.VEC2, VertexFormatType.VEC3));
    }

    private int[] generateIndices(int count) {
        int[] newArrayForIndices = new int[count];
        System.arraycopy(indexBuffer, 0, newArrayForIndices, 0, count);
        return newArrayForIndices;
    }
}
