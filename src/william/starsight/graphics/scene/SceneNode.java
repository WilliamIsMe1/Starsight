package william.starsight.graphics.scene;

import org.joml.Matrix4f;
import william.starsight.graphics.mesh.Mesh;

import java.io.Serializable;
import java.util.List;

public class SceneNode implements Serializable, AutoCloseable {
    private Matrix4f transform;
    private Mesh content;
    private List<SceneNode> children;

    private Runnable beforeRenderCallback;
    private Runnable afterRenderCallback;

    public SceneNode(Mesh content, Runnable beforeRenderCallback, Runnable afterRenderCallback) {
        this.content = content;
        this.beforeRenderCallback = beforeRenderCallback;
        this.afterRenderCallback = afterRenderCallback;
    }

    public Matrix4f getTransform(Matrix4f parentTransform) {
        if (parentTransform == null) {
            return transform;
        }
        return parentTransform.mul(transform);
    }

    public void render() {
        beforeRenderCallback.run();
        content.render();
        afterRenderCallback.run();
        children.forEach(SceneNode::render);
    }

    public void setTransform(Matrix4f transform) {
        this.transform = new Matrix4f(transform);
    }

    public List<SceneNode> getChildren() {
        return List.copyOf(children); // Immutable shallow copy of all items in the list
    }

    public void addChild(SceneNode child) {
        children.add(child);
    }

    public boolean removeChild(SceneNode child) {
        return children.remove(child); // Returns true if node was successfully removed
    }

    public void initialize() {
        content.initialize();
        children.forEach(SceneNode::initialize);
    }

    public void setContent(Mesh mesh) {
        this.content = mesh;
    }

    public Mesh getContent() {
        return content;
    }

    public void cleanup() {
        content.cleanup();
        children.forEach(SceneNode::cleanup);
    }

    @Override
    public void close() {
        cleanup();
    }
}
