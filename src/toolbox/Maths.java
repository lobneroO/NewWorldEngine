package toolbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;


/**
 * The Maths class gives access to common mathematical instructions beyond JOML.
 * It works mostly with float (rather than double) variables.
 * @author Lobner
 *
 */
public class Maths 
{
	public static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
	public static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
	public static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);
	
	public static final float PIf = (float)Math.PI;
	
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
		rotationMatrix.rotateXYZ(new Vector3f (pitch, yaw, 0));	//works with angles in radians
		
		viewMatrix = rotationMatrix.mul(translationMatrix);
		return viewMatrix;
	}
	
	/**
	 * Converts every value of the vector from radians to degree
	 * @param vec The input rotation vector with three angles in radians
	 * @return The output rotation vector with three angles in degrees
	 */
	public static Vector3f radToDeg(Vector3f vec)
	{
		Vector3f convertedVec = new Vector3f((float)Math.toDegrees(vec.x), 
				(float)Math.toDegrees(vec.y), (float)Math.toDegrees(vec.z));
		return convertedVec;
	}
	
	/**
	 * Converts every value of the vector from degree to radians
	 * @param vec The input rotation vector with three angles in degress
	 * @return The output rotation vector with three angles in radians
	 */
	public static Vector3f degToRad(Vector3f vec)
	{
		Vector3f convertedVec = new Vector3f((float)Math.toRadians(vec.x), 
				(float)Math.toRadians(vec.y), (float)Math.toRadians(vec.z));
		return convertedVec;
	}
}
