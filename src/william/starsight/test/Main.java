package william.starsight.test;

import org.joml.Vector3f;
import william.starsight.core.Program;
import william.starsight.core.Window;
import william.starsight.graphics.mesh.Mesh;
import william.starsight.graphics.mesh.Tesselator;
import william.starsight.graphics.shader.ShaderCompilationException;
import william.starsight.graphics.shader.ShaderLinkingException;
import william.starsight.graphics.shader.ShaderProgram;
import william.starsight.graphics.texture.SimpleTexture;
import william.starsight.graphics.texture.Texture;
import william.starsight.util.Camera;
import william.starsight.util.KeyboardKey;

import java.io.FileNotFoundException;

import static org.lwjgl.glfw.GLFW.*;

/**
 * This demo will be that of a cube in space and us moving around
 */
@SuppressWarnings("ALL")
public class Main implements Program {

    public static void main(String[] args) {
        Window w = new Window(800, 600, "Starsight Test", new Main());
        w.run();
    }

    Window parent = null;

    float aspect = 0.0f;

    double currentTime = 0.0;

    Tesselator tess = new Tesselator();

    Mesh cubes = null;

    Texture tex;

    {
        try {
            tex = new SimpleTexture("res/container.jpg");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    final Camera camera = new Camera(new Vector3f(0,0,3), 0, 180);

    final boolean testingNormals = false;

    boolean w = false, a = false, s = false, d = false;

    boolean hasPressedQ = false;

    boolean focused = true;
    boolean lastFocused = true;

    ShaderProgram testShader = new ShaderProgram("""
            #version 430
            
            layout(location = 0) in vec3 pos;
            layout(location = 1) in vec2 uv;
            layout(location = 2) in vec3 normals;
            
            uniform mat4 perspective;
            uniform mat4 view;
            
            out vec2 fragUV;
            out vec3 fragNormals;
            
            void main() {
                fragUV = uv;
                fragNormals = normals;
                
                gl_Position = perspective * view * vec4(pos, 1.0);
            }
            """, testingNormals ? """
            #version 430
            
            in vec2 fragUV;
            in vec3 fragNormals;
            
            out vec4 outColor;
            
            void main() {
                outColor = vec4(fragNormals, 1.0);
            }
            """ : """
            #version 430
            
            in vec2 fragUV;
            in vec3 fragNormals;
            
            uniform sampler2D texSampler;

            out vec4 outColor;
            
            void main() {
                outColor = texture(texSampler, fragUV); 
            }
            """);

    double lastX = 0;
    double lastY = 0;

    @Override
    public void initialize(Window parent, int initWidth, int initHeight, double startTime) {
        this.parent = parent;
        this.aspect = (float) initWidth / initHeight;

        this.lastX = initWidth / 2;
        this.lastY = initWidth / 2;

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

        tex.initialize();

        try {
            testShader.compileAndLink();
        } catch (ShaderLinkingException e) {
            throw new RuntimeException(e);
        } catch (ShaderCompilationException e) {
            throw new RuntimeException(e);
        }
    }

    private final Vector3f tickVector = new Vector3f();

    @Override
    public void tick(double newTime) {
        double delta = newTime - currentTime;
        if (w) {
            camera.transformPosition(camera.getFront().mul(0.01f /* Camera speed */, tickVector));
        }
        if (a) {
            camera.transformPosition(camera.getFront().cross(camera.getUp(), tickVector).mul(-0.01f, tickVector));
        }
        if (s) {
            camera.transformPosition(camera.getFront().mul(-0.01f /* Camera speed */, tickVector));
        }
        if (d) {
            camera.transformPosition(camera.getFront().cross(camera.getUp(), tickVector).mul(0.01f, tickVector));
        }
        if (!focused && lastFocused) {
            parent.letGoOfMouse();
        }
        if (!lastFocused && focused) {
            parent.captureMouse();
        }
        lastFocused = focused;
    }

    @Override
    public void render(double newTime) {
        testShader.bind();
        testShader.setUniform("perspective", Camera.getPerspectiveMatrix(/*(float) Math.toRadians(70.0)*/70, aspect, 0.01f, 10000f)); // Whyy does JOML use degrees?
        testShader.setUniform("view", camera.getViewMatrix());
        tex.bind();

        cubes.render();

        testShader.unbind();
        tex.unbind();
    }

    @Override
    public void cleanup() {
        testShader.cleanup();
        tex.cleanup();
        cubes.cleanup();
    }

    @Override
    public boolean shouldClose() {
        return hasPressedQ;
    }

    @Override
    public void resized(int newWidth, int newHeight) {
        this.aspect = (float) newWidth / newHeight;
    }

    @Override
    public void takeKeyEvent(int key, int action, int mods) {
        var keyAsEnum = KeyboardKey.fromKeyCode(key);
        switch (keyAsEnum) {
            case KeyboardKey.KEY_W -> {
                w = action == GLFW_PRESS || action == GLFW_REPEAT;
                break;
            }
            case KeyboardKey.KEY_A -> {
                a = action == GLFW_PRESS || action == GLFW_REPEAT;
                break;
            }
            case KeyboardKey.KEY_S -> {
                s = action == GLFW_PRESS || action == GLFW_REPEAT;
                break;
            }
            case KeyboardKey.KEY_D -> {
                d = action == GLFW_PRESS || action == GLFW_REPEAT;
                break;
            }
            case KeyboardKey.KEY_ESCAPE -> {
                if (action == GLFW_PRESS) {
                    focused = !focused;
                }
                break;
            }
            case KeyboardKey.KEY_Q -> {
                hasPressedQ = true;
                break;
            }
        };
    }

    @Override
    public void takeMouseMovementEvent(double xPos, double yPos) {
        double xOff = lastX - xPos;
        double yOff = yPos - lastY; // Reversed cause OpenGL

        lastX = xPos;
        lastY = yPos;
        if (!focused) return;
        camera.letMouseChangeAngle((float) -xOff, (float) -yOff); // There is an odd reversal for some reason
    }

    @Override
    public void takeMouseButtonEvent(int button, int action, int mods) {

    }
}
