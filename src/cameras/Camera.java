package cameras;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import toolbox.Maths;

/**
 * The Camera class encapsulates the basic functionality for cameras to come.
 * It uses euler angles for the viewing direction and calculates the viewMatrix via the Maths class
 * @author Lobner
 *
 */
public class Camera
{
	protected Vector3f position = new Vector3f(0, 0, 0);
	//pitch: how high/low a camera is aimed
	//yaw: how far to the left / right a camera is aimed
	//roll: rotation about the looking direction
	//all angles in radians
	protected float pitch = 0, yaw = 0, roll = 0;
	
	Matrix4f viewMatrix = new Matrix4f();
	
	
	public Camera()
	{
		viewMatrix = Maths.createViewMatrix(position, pitch, yaw);
	}

	public Matrix4f getViewMatrix()
	{
		viewMatrix = Maths.createViewMatrix(position, pitch, yaw);
		return viewMatrix;
	}

	public Vector3f getPosition() 
	{
		return position;
	}

	/**
	 * Euler Angle pitch (similar to a persons looking up- or downwards)
	 * @return The pitch angle in radians
	 */
	public float getPitch() 
	{
		return pitch;
	}

	/**
	 * Euler Angle yaw (similar to a persons looing right or left)
	 * @return The yaw angle in radians
	 */
	public float getYaw() 
	{
		return yaw;
	}

	/**
	 * Euler Anlge roll (similar to a person tilting his head to the side)
	 * @return The roll angle in radians
	 */
	public float getRoll() 
	{
		return roll;
	}	
}