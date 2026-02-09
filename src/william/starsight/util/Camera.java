package william.starsight.util;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import william.coreutils.MathUtils;

/**
 * Defines a camera, as a simple way of approaching the look-at matrix
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
	
	
	private float pitchDeg, yawDeg;
	
	private Vector3f position;
	private Vector3f forward;
	
	/**
	 * Sets the position, pitch, and yaw
	 *
	 * @param position The position the camera is at
	 * @param pitchDeg The pitch of the camera
	 * @param yawDeg The yaw of the camera
	 */
	public Camera(Vector3f position, float pitchDeg, float yawDeg) {
		this.position = position;
		setPitchDeg(pitchDeg);
		this.yawDeg = yawDeg;
	}
	
	public void setPitchDeg(float newPitch) {
		//noinspection MagicNumber // as we don't need to use this in many areas
		this.pitchDeg = MathUtils.clamp(newPitch, -89.9f, 89.9f);
		recalculateForward();
	}
	
	public void setYawDeg(float newYaw) {
		this.yawDeg = newYaw;
		recalculateForward();
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
		recalculateForward();
	}
	
	public float getPitchDeg() {
		return pitchDeg;
	}
	
	public float getYawDeg() {
		return yawDeg;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getForwardVector() {
		return new Vector3f(forward);
	}
	
	/**
	 * Recalculates the forward vector using the angles
	 */
	public void recalculateForward() {
		float yawRad = (float) Math.toRadians(yawDeg);
		float pitchRad = (float) Math.toRadians(pitchDeg);
		
		forward = new Vector3f(
			(float) (Math.cos(pitchRad) * Math.cos(yawRad)),
			(float) Math.sin(pitchRad),
			(float) (Math.cos(pitchRad) * Math.sin(yawRad))
		).normalize();
	}
	
	/**
	 * Calculates and returns the view matrix for this camera.
	 *
	 * @return The view (camera) matrix
	 */
	public Matrix4f getCameraMatrix() {
		Vector3f target = new Vector3f(position).add(forward);
		
		return new Matrix4f().identity().lookAt(
			position,
			target,
			new Vector3f(0, 1, 0)
		);
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
		
		return new Matrix4f().identity().perspective(fov, aspect, 1.0f, FAR_CLIP_PLANE);
	}
}
