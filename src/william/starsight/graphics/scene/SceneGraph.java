package william.starsight.graphics.scene;

import java.io.Serializable;

public class SceneGraph implements Serializable { /*
                                                   Wait do we need this? I guess it conveys it is a scene
                                                   graph, but it acts as a wrapper.
                                                  */

    private SceneNode root;

    public SceneGraph(SceneNode root) {
        this.root = root;
    }

    public void initialize() {
        root.initialize(); // Recursively initializes all the scene node meshes
    }

    public void render() {
        root.render(); // Also recursive
    }

    public void cleanup() {
        root.cleanup();
    }
}
