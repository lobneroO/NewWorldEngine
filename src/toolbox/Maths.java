package toolbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;



public class Maths 
{
	public static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
	public static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
	public static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);
	
	public static Matrix4f createModelMatrix(Vector3f translation, Vector3f rotation, Vector3f scale)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		
		matrix.translate(translation);
		matrix.rotateXYZ(rotation);
		matrix.scale(scale);
		
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Vector3f position, float pitch, float yaw)
	{
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.identity();
		
		Matrix4f translationMatrix = new Matrix4f();
		translationMatrix.identity();
		translationMatrix.translate(new Vector3f(-position.x, -position.y, -position.z));
		
		Matrix4f rotationMatrix = new Matrix4f();
		rotationMatrix.identity();
		rotationMatrix.rotateXYZ(new Vector3f (pitch, yaw, 0));
		
		viewMatrix = rotationMatrix.mul(translationMatrix);
		return viewMatrix;
	}
}
