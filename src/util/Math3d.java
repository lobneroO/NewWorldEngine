package util;
import com.jogamp.opengl.math.FloatUtil;

import java.lang.Math;

import util.Math3d;

/**
 * Gives quick access to commonly used mathematical functions.
 * Encapsulates some of the Java.lang.Math functionality for float (rather than double)
 * values.
 * It also enables some 3d mathematics. That is obsolete since the use of JOML though.
 * @author Lobner
 *
 */
@Deprecated
public class Math3d 
{
	public static float sinf(float angle)
	{
		return (float)Math.sin(angle);
	}
	
	public static float cosf(float angle)
	{
		return (float)Math.cos(angle);
	}
	
	public static float tanf(float angle)
	{
		return (float)Math.tan(angle);
	}
	
	public static float dotProduct(float[] v1, float[] v2)
	{
		return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
	}
	
	public static float[] crossProduct(float[] v1, float[] v2)
	{
		float[] result = {0, 0, 0};
		
		result[0] = v1[1] * v2[2] - v1[2] * v2[1];
		result[1] = v1[2] * v2[0] - v1[0] * v2[2];
		result[2] = v1[0] * v2[1] - v1[1] * v2[0];
		
		return result;
	}
	
	public static float[] normalize(float[] v)
	{ 	//can be used for vec2, vec3, vec4 and quaternions
		float tmp = 0;
		for(int i = 0; i < v.length; i++)
		{
			tmp += v[i]*v[i];
		}
		float length = (float)Math.sqrt(tmp);
		
		float[] result = new float[v.length];
		
		for(int i = 0; i < v.length; i++)
		{
			result[i] = v[i]/length;
		}
		
		return result;
	}
	
	public static float[] multiplyQuaternion(float[] q1, float[] q2)
	{
		float[] result = new float[4];
		
		result[0] = q1[0] * q2[3] + q1[3] * q2[0] + q1[1] * q2[2] - q1[2] * q2[1];
		result[1] = q1[1] * q2[3] + q1[3] * q2[1] + q1[2] * q2[0] - q1[0] * q2[2];
		result[2] = q1[2] * q2[3] + q1[3] * q2[2] + q1[0] * q2[1] - q1[1] * q2[0];
		result[3] = q1[3] * q2[3] - q1[0] * q2[0] - q1[1] * q2[1] - q1[2] * q2[2];
		
		return result;
	}
	
	public static float[] multiplyQuaternionWithVector(float[] q, float[] v)
	{
		float[] result = new float[4];
		
		result[0] = q[3] * v[0] + q[1] * v[2] - q[2] * v[1];
		result[1] = q[3] * v[1] + q[2] * v[0] - q[0] * v[2];
		result[2] = q[3] * v[2] + q[0] * v[1] - q[1] * v[0];
		result[3] = -q[0] * v[0] - q[1] * v[1] - q[2] * v[2];
		
		return result;
	}
	
	public static float[] rotateVectorWithQuaternion(float[] v, float angle, float[] axis)
	{
		//angle is in degrees
		float sinHalfAngle = (float) Math.sin(Math.toRadians(angle)/2.0);
		float cosHalfAngle = (float) Math.cos(Math.toRadians(angle)/2.0);
		
		float[] rotationQ = new float[4];
		rotationQ[0] = axis[0] * sinHalfAngle;
		rotationQ[1] = axis[1] * sinHalfAngle;
		rotationQ[2] = axis[2] * sinHalfAngle;
		rotationQ[3] = cosHalfAngle;
		
		float[] conjugateQ = Math3d.conjugate(rotationQ);
		
		float[] w = Math3d.multiplyQuaternion(Math3d.multiplyQuaternionWithVector(rotationQ, v), conjugateQ);
		
		return new float[]{w[0], w[1], w[2]};
	}
	
	public static float[] matrixTranslate(float[] m, float[] v)
	{
		float[] result = m.clone();
		
		result[3] = m[3] + v[0];
		result[3] = m[7] + v[1];
		result[3] = m[11] + v[2];
		
		return result.clone();
	}
	
	public static float[] matrixRotateX(float[] m, float angle)//, float rotateY, float rotateZ)
	{
		float[] rotXMatrix = {	1.0f, 0.0f, 0.0f, 0.0f,
								0.0f, Math3d.cosf(angle), -Math3d.sinf(angle), 0.0f,
								0.0f, Math3d.sinf(angle), Math3d.cosf(angle), 0.0f,
								0.0f, 0.0f, 0.0f, 1.0f
		};
		
		return multiplyMatrix4(m, rotXMatrix);
	}
		
	public static float[] matrixRotateY(float[] m, float angle)
	{
		float[] rotYMatrix = {	Math3d.cosf(angle), 0.0f, -Math3d.sinf(angle), 0.0f,
								0.0f, 1.0f, 0.0f, 0.0f,
								Math3d.sinf(angle), 0.0f, Math3d.cosf(angle), 0.0f,
								0.0f, 0.0f, 0.0f, 1.0f
		};
		
		return multiplyMatrix4(m, rotYMatrix);
	}
	
	public static float[] matrixRotateZ(float[] m, float angle)
	{
		float[] rotZMatrix = {	Math3d.cosf(angle), -Math3d.sinf(angle), 0.0f, 0.0f,
								Math3d.sinf(angle), Math3d.cosf(angle), 0.0f, 0.0f,
								0.0f, 0.0f, 1.0f, 0.0f,
								0.0f, 0.0f, 0.0f, 1.0f
		};
		
		return multiplyMatrix4(m, rotZMatrix);
	}
	
	public static float[] conjugate(float[] q)
	{
		return new float[]{-q[0], -q[1], -q[2], q[3]};
	}
	
	/**
	 * Returns the product of two matrices m1 and m2, in the order m1*m2
	 * @param m1 left hand side matrix
	 * @param m2 right hand side matrix
	 * @return matrix product
	 */
	public static float[] multiplyMatrix4(float[] m1, float[] m2)
	{
		float[] result = m1.clone();
		
		FloatUtil.multMatrix(result, 0, m2, 0);
		
		return result;
	}
	
	/**
     * Returns a transposed matrix. 
     * @param matrix
     * @return transposed matrix. 
     */
    public static final float[] transposeMatrix(float[] matrix){
    	float[] transpose = new float[16];
    	for (int i = 0; i < 4; i++) 
    	{
    		for (int j = 0; j < 4; j++) 
    		{
				transpose[4*i + j] = matrix[4*j + i];
			}
		}
    	return transpose;
    }
    
    public static final float[] getIdentity()
    {
    	float[] i = {	1.0f, 0.0f, 0.0f, 0.0f,
    					0.0f, 1.0f, 0.0f, 0.0f,
    					0.0f, 0.0f, 1.0f, 0.0f,
    					0.0f, 0.0f, 0.0f, 1.0f
    	};
    	
    	return i;
    }
    
    public static final float[] EulerAngleToVector(float[] angle)
    {
    	//euler angle is a three-tuple (pitch, yaw, roll)
    	float[] v = new float[3];
    	v[0] = (float) (Math.cos(angle[1]) * Math.sin(angle[0])); 	// cos of yaw * cos pitch
    	v[1] = (float) (Math.sin(angle[0]));						// sin of pitch
    	v[2] = (float) (Math.sin(angle[1]) * Math. sin(angle[2]));	// sin of yaw * cos pitch
    	
    	return v;
    }
}
