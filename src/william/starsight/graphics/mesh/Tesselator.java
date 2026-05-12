package william.starsight.graphics.mesh;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Arrays;

import static org.lwjgl.opengl.GL43.*;

/**
 * This specifically assumes a vertex format of POS, UV, NORMALS, and is not meant to be used for entity models. A different Tesselator class should cover that
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
        for (int i = 0; i < premadeIndices.length - 6;) { // The incrementation logic is better inside the loop code. I love side effects when they can be used properly!
            premadeIndices[i++] = one; // Readability :D
            premadeIndices[i++] = two;
            premadeIndices[i++] = three;
            premadeIndices[i++] = four;
            premadeIndices[i++] = five;
            premadeIndices[i++] /* I learned this secret technique from poorly written C code :D */ = six;
            one += 4;
            two += 4;
            three += 4;
            four += 4;
            five += 4;
            six += 4;
        }
    }

    private final float[] buffer;
    private int bufferPosition;
    private int quadCount;

    public Tesselator() {
        this((int) (32 * 32 * 32 * 24 * 1.25)); // Default capacity
    }

    public Tesselator(int startingCapacity) {
        buffer = new float[startingCapacity];
        bufferPosition = 0;
        quadCount = 0;
    }

    public void setOrientation(Orientation o) { // TODO Is this needed at all? Does this even belong in this class?? what was past self thinking!? FRONT FACE IS A GLOBAL STATE AND THE Tesselator CAN'T TOUCH IT.
        glFrontFace(o == Orientation.CW ? GL_CW : GL_CCW);
    }

    public enum Orientation {
        CW,
        CCW
    }

    @SuppressWarnings("UnnecessaryBreak") // IntelliJ, buddy, you are wrong here.
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
                break;
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
                break;
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
                break;
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
                break;
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
                break;
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
                break;
            }
            case null -> throw new NullPointerException(":| Please don't.");
        }

//        float newQuad[] = { // One new quad :)
//             //  x,  y,  z,    u,    v, nx, ny, nz
//                x1, y1, z1, minU, minV, nX, nY, nZ,
//                x2, y2, z2, minU, maxV, nX, nY, nZ,
//                x3, y3, z3, maxU, maxV, nX, nY, nZ,
//                x4, y4, z4, maxU, minV, nX, nY, nZ // Need to put 32 elements into an array without creating a new object.
//        };

        // Never speak of this again
        buffer[bufferPosition++] = x1;
        buffer[bufferPosition++] = y1;
        buffer[bufferPosition++] = z1;
        buffer[bufferPosition++] = minU;
        buffer[bufferPosition++] = minV;
        buffer[bufferPosition++] = nX;
        buffer[bufferPosition++] = nY;
        buffer[bufferPosition++] = nZ;
        buffer[bufferPosition++] = x2;
        buffer[bufferPosition++] = y2;
        buffer[bufferPosition++] = z2;
        buffer[bufferPosition++] = maxU;
        buffer[bufferPosition++] = minV;
        buffer[bufferPosition++] = nX;
        buffer[bufferPosition++] = nY;
        buffer[bufferPosition++] = nZ;
        buffer[bufferPosition++] = x3;
        buffer[bufferPosition++] = y3;
        buffer[bufferPosition++] = z3;
        buffer[bufferPosition++] = maxU;
        buffer[bufferPosition++] = maxV;
        buffer[bufferPosition++] = nX;
        buffer[bufferPosition++] = nY;
        buffer[bufferPosition++] = nZ;
        buffer[bufferPosition++] = x4;
        buffer[bufferPosition++] = y4;
        buffer[bufferPosition++] = z4;
        buffer[bufferPosition++] = minU;
        buffer[bufferPosition++] = maxV;
        buffer[bufferPosition++] = nX;
        buffer[bufferPosition++] = nY;
        buffer[bufferPosition++] = nZ;

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
