package william.starsight.graphics.mesh;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Arrays;

import static org.lwjgl.opengl.GL43.*;

/**
 * This specifically assumes a vertex format of POS, UV, NORMALS, and is not meant to be used for entity models. MovingObjectTesselator should cover that
 */
public class Tesselator {
    private static final int[] premadeIndices = new int[4194304]; // Filling up the premade indices. I need to document my choice of numbers

    static {
        int one = 0;
        int two = 1;
        int three = 2;
        int four = 2;
        int five = 3;
        int six = 0;
        for (int i = 0; i < premadeIndices.length; i += 6) {
            premadeIndices[i] = one;
            premadeIndices[i + 1] = two;
            premadeIndices[i + 2] = three;
            premadeIndices[i + 3] = four;
            premadeIndices[i + 4] = five;
            premadeIndices[i + 5] = six;
            one += 4;
            two += 4;
            three += 4;
            four += 4;
            five += 4;
            six += 4;
        }
    }

    public static final Tesselator INSTANCE = new Tesselator((int) (32 * 32 * 32 * 24 * 1.25)); // Starting capacity of 983040 floats

    private float[] buffer;
    private int bufferPosition;
    private Orientation orientation = Orientation.CCW;
    private int quadCount;

    private Tesselator(int startingCapacity) {
        buffer = new float[startingCapacity];
        bufferPosition = 0;
        quadCount = 0;
    }

    public void setOrientation(Orientation o) {
        this.orientation = o;
        glFrontFace(o == Orientation.CW ? GL_CW : GL_CCW);
    }

    public enum Orientation {
        CW,
        CCW
    }

    public void addQuad(float x, float y, float z, @NotNull QuadDirection direction, float width, float height, float minU, float minV, float maxU, float maxV) {
        float nX = direction.x();
        float nY = direction.y();
        float nZ = direction.z();

        float x1, x2, x3, x4;
        float y1, y2, y3, y4;
        float z1, z2, z3, z4;
        float widthHalf = width / 2.0f;
        float heightHalf = height / 2.0f;

        switch (direction) {
            case NEG_X -> {
                x1 = x;
                x2 = x;
                x3 = x;
                x4 = x;
                y1 = y - heightHalf;
                y2 = y - heightHalf;
                y3 = y + heightHalf;
                y4 = y + heightHalf;
                z1 = z - widthHalf;
                z2 = z + widthHalf;
                z3 = z + widthHalf;
                z4 = z - widthHalf;
            }
            case NEG_Y -> {
                x1 = x - widthHalf;
                x2 = x - widthHalf;
                x3 = x + widthHalf;
                x4 = x + widthHalf;
                y1 = y;
                y2 = y;
                y3 = y;
                y4 = y;
                z1 = z + heightHalf;
                z2 = z - heightHalf;
                z3 = z - heightHalf;
                z4 = z + heightHalf;
            }
            case NEG_Z -> {
                x1 = x + widthHalf;
                x2 = x - widthHalf;
                x3 = x - widthHalf;
                x4 = x + widthHalf;
                y1 = y - heightHalf;
                y2 = y - heightHalf;
                y3 = y + heightHalf;
                y4 = y + heightHalf;
                z1 = z;
                z2 = z;
                z3 = z;
                z4 = z;
            }
            case POS_X -> {
                x1 = x;
                x2 = x;
                x3 = x;
                x4 = x;
                y1 = y - heightHalf;
                y2 = y - heightHalf;
                y3 = y + heightHalf;
                y4 = y + heightHalf;
                z1 = z + widthHalf;
                z2 = z - widthHalf;
                z3 = z - widthHalf;
                z4 = z + widthHalf;
            }
            case POS_Y -> {
                x1 = x - widthHalf;
                x2 = x - widthHalf;
                x3 = x + widthHalf;
                x4 = x + widthHalf;
                y1 = y;
                y2 = y;
                y3 = y;
                y4 = y;
                z1 = z - heightHalf;
                z2 = z + heightHalf;
                z3 = z + heightHalf;
                z4 = z - heightHalf;
            }
            case POS_Z -> {
                x1 = x - widthHalf;
                x2 = x + widthHalf;
                x3 = x + widthHalf;
                x4 = x - widthHalf;
                y1 = y - heightHalf;
                y2 = y - heightHalf;
                y3 = y + heightHalf;
                y4 = y + heightHalf;
                z1 = z;
                z2 = z;
                z3 = z;
                z4 = z;
            }
            case null -> throw new NullPointerException("I legit don't know how this threw. This is literally impossible.");
        }

        float newQuad[] = { // One new quad :)
             //  x,  y,  z,    u,    v, nx, ny, nz
                x1, y1, z1, minU, minV, nX, nY, nZ,
                x2, y2, z2, minU, maxV, nX, nY, nZ,
                x3, y3, z3, maxU, maxV, nX, nY, nZ,
                x4, y4, z4, maxU, minV, nX, nY, nZ // Need to put 32 elements into an array without creating a new object.
        };

        // Never speak of this again
        buffer[bufferPosition] = x1;
        buffer[bufferPosition + 1] = y1;
        buffer[bufferPosition + 2] = z1;
        buffer[bufferPosition + 3] = minU;
        buffer[bufferPosition + 4] = minV;
        buffer[bufferPosition + 5] = nX;
        buffer[bufferPosition + 6] = nY;
        buffer[bufferPosition + 7] = nZ;
        buffer[bufferPosition + 8] = x2;
        buffer[bufferPosition + 9] = y2;
        buffer[bufferPosition + 10] = z2;
        buffer[bufferPosition + 11] = minU;
        buffer[bufferPosition + 12] = maxV;
        buffer[bufferPosition + 13] = nX;
        buffer[bufferPosition + 14] = nY;
        buffer[bufferPosition + 15] = nZ;
        buffer[bufferPosition + 16] = x3;
        buffer[bufferPosition + 17] = y3;
        buffer[bufferPosition + 18] = z3;
        buffer[bufferPosition + 19] = maxU;
        buffer[bufferPosition + 20] = maxV;
        buffer[bufferPosition + 21] = nX;
        buffer[bufferPosition + 22] = nY;
        buffer[bufferPosition + 23] = nZ;
        buffer[bufferPosition + 24] = x4;
        buffer[bufferPosition + 25] = y4;
        buffer[bufferPosition + 26] = z4;
        buffer[bufferPosition + 27] = maxU;
        buffer[bufferPosition + 28] = minV;
        buffer[bufferPosition + 29] = nX;
        buffer[bufferPosition + 30] = nY;
        buffer[bufferPosition + 31] = nZ;

        quadCount++;
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
        var copyOfBuffer = buffer.clone(); // I want to reset afterward
        Arrays.fill(buffer, 0.0f);
        int oldQuadCount = quadCount;
        int quadCount = 0;
        return new EBOMesh(copyOfBuffer,generateIndices(oldQuadCount * 6),VertexFormat.of(VertexFormatType.VEC3, VertexFormatType.VEC2, VertexFormatType.VEC3)); // Only 2 object allocations (may change)
    }

    private int[] generateIndices(int count) {
        int[] newArrayForIndices = new int[count];
        System.arraycopy(premadeIndices, 0, newArrayForIndices, 0, count);
        return newArrayForIndices;
    }
}
