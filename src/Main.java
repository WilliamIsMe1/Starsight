
import william.starsight.core.Program;
import william.starsight.core.Window;
import william.starsight.graphics.mesh.Mesh;
import william.starsight.graphics.mesh.Tesselator;
import william.starsight.util.KeyboardKey;

/**
 * This demo will be that of several cubes in space and us moving around
 */
@SuppressWarnings("ALL")
public class Main implements Program {
    Window parent = null;

    float aspect = 0.0f;

    double currentTime = 0.0;

    Tesselator tess = Tesselator.INSTANCE;

    Mesh cubes = null;

    @Override
    public void initialize(Window parent, int initWidth, int initHeight, double startTime) {
        this.parent = null;
        this.aspect = (float) initWidth / initHeight;

        currentTime = startTime;

        parent.captureMouse();

        // Create a cube
        tess.addQuad(0.5f, 0.0f, 0.0f, Tesselator.QuadDirection.POS_X, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
        tess.addQuad(-0.5f, 0.0f, 0.0f, Tesselator.QuadDirection.NEG_X, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
        tess.addQuad(0.0f, 0.5f, 0.0f, Tesselator.QuadDirection.POS_Y, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
        tess.addQuad(0.0f, -0.5f, 0.0f, Tesselator.QuadDirection.NEG_Y, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
        tess.addQuad(0.0f, 0.0f, 0.5f, Tesselator.QuadDirection.POS_Z, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
        tess.addQuad(0.0f, 0.0f, -0.5f, Tesselator.QuadDirection.NEG_Z, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);

        cubes = tess.flushMesh();
        cubes.initialize(); // Upload to GPU :)
    }

    @Override
    public void tick() {

    }

    @Override
    public void render() {

    }

    @Override
    public void cleanup() {

    }

    @Override
    public boolean shouldClose() {
        return false;
    }

    @Override
    public void resized(int newWidth, int newHeight) {

    }

    @Override
    public void takeKeyEvent(int key, int action, int mods) {
        var keyAsEnum = KeyboardKey.fromKeyCode(key);
        switch (keyAsEnum) {
            case KeyboardKey.KEY_W -> {
                /* cameraPos += cameraSpeed * cameraFront; */
            }
            case KeyboardKey.KEY_A -> {
                /* cameraPos -= normalize(cross(cameraFront, cameraUp)) * cameraSpeed; */
            }
            case KeyboardKey.KEY_S -> {
                /* cameraPos -= cameraSpeed * cameraFront; */
            }
            case KeyboardKey.KEY_D -> {
                /* cameraPos += normalize(cross(cameraFront, cameraUp)) * cameraSpeed; */
            }
        };
    }

    @Override
    public void takeMouseMovementEvent(double xPos, double yPos) {

    }

    @Override
    public void takeMouseButtonEvent(int button, int action, int mods) {

    }
}
