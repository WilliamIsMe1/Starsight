package william.starsight.util;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import william.coreutils.MathUtils;

/**
 * Defines a camera, as a simple way of approaching the look-at matrix
 * <p>
 * Will be replaced with an abstract class with multiple implementations in a later update
 *
 * @author William
 */
public class Camera {
	/**
	 * A constant that deals with perspective matrices
	 */
	public static final float MINIMUM_FOV = 60.0f;
	
	/**
	 * A constant that deals with perspective matrices
	 */
	public static final float MAXIMUM_FOV = 120.0f;
	
	/**
	 * A constant that deals with perspective matrices
	 */
	public static final float FAR_CLIP_PLANE = 1000.0f;
	public static final float DEFAULT_MOUSE_SENSITIVITY = 0.1f;
	public static final float DEFAULT_ZOOM = 45.0f;

	private Vector3f position;
	private Vector3f front;
	private Vector3f up;
	private Vector3f right;
	private final Vector3f worldUp = new Vector3f(0.0f, 1.0f, 0.0f);

	private float yaw;
	private float pitch;

	private float mouseSensitivity;
	private float zoom;

	public Camera(Vector3f position, float pitch, float yaw, float mouseSensitivity, float zoom) {
		this.position = position;
		this.pitch = pitch;
		this.yaw = yaw;
		this.mouseSensitivity = mouseSensitivity;
		this.zoom = zoom;
		this.up = new Vector3f(0.0f, 1.0f, 0.0f);
		this.front = new Vector3f(0.0f, 0.0f, -1.0f);
		this.right = front.cross(worldUp, new Vector3f()).normalize(); // Allocates new vector
		this.mouseSensitivity = mouseSensitivity;
	}

	public Camera(Vector3f position, float pitch, float yaw) {
		this(position, pitch, yaw, DEFAULT_MOUSE_SENSITIVITY, DEFAULT_ZOOM);
	}

	/**
	 * Calculates and returns the view matrix for this camera.
	 *
	 * @return The view (camera) matrix
	 */
	public Matrix4f getViewMatrix() {
		return new Matrix4f().identity().lookAt(
				position,
				position.add(front, new Vector3f()),
				up
		);
	}

	public void setYaw(float newYaw) {
		this.yaw = newYaw;
	}

	public void setPitch(float newPitch) {
		this.pitch = pitch;
	}

	public void letMouseChangeAngle(float xOffset, float yOffset) {
		xOffset *= mouseSensitivity;
		yOffset *= mouseSensitivity;

		yaw += xOffset;
		pitch += yOffset;

		pitch = MathUtils.clamp(pitch, -89.0f, 89.0f);
		yaw = (yaw + 360.0f) % 360.0f; // Stop it from spinning off to infinity. The +360.0f is so that just in case, negative numbers act properly.

		updateCameraVectors();
	}

	public void transformPosition(Vector3f transformation) {
		this.position.add(transformation);
	}

	public void setZoom(float newZoom) {
		this.zoom = newZoom;
	}
	
	/**
	 * Simple utility method that ought to be in here but can be moved if there is a better place
	 *
	 * @param fov The FOV of the matrix
	 * @param aspect The aspect ratio of the viewport
	 * @return A perspective matrix
	 */
	public static Matrix4f getPerspectiveMatrix(float fov, float aspect) {
		fov = MathUtils.clamp(fov, MINIMUM_FOV, MAXIMUM_FOV); // Yes, I know reassigning parameter variables is bad practice, but we only use this value as clamped.
		
		return new Matrix4f().identity().perspective(fov, aspect, 0.1f, FAR_CLIP_PLANE);
	}

	private void updateCameraVectors() {
		front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
		front.y = (float) Math.sin(Math.toRadians(pitch));
		front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
		front.normalize();
		this.right = front.cross(worldUp, right).normalize(); // No new allocations since "right" is the destination
		this.up = right.cross(front, up).normalize();
	}
}
