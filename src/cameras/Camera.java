package cameras;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import toolbox.Maths;

public class Camera
{
	protected Vector3f position = new Vector3f(0, 0, 0);
	//pitch: how high/low a camera is aimed
	//yaw: how far to the left / right a camera is aimed
	//roll: rotation about the looking direction
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


	public float getPitch() 
	{
		return pitch;
	}


	public float getYaw() 
	{
		return yaw;
	}


	public float getRoll() 
	{
		return roll;
	}	
}