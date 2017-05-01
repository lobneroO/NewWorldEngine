package toolbox;

import java.nio.FloatBuffer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.common.nio.Buffers;

public class BufferConversion 
{
	public static FloatBuffer getVector2AsFloatBuffer(Vector2f vector)
	{
		FloatBuffer fb = Buffers.newDirectFloatBuffer(2);
		vector.get(fb);
		return fb;
	}
	
	public static FloatBuffer getVector3AsFloatBuffer(Vector3f vector)
	{
		FloatBuffer fb = Buffers.newDirectFloatBuffer(3);
		vector.get(fb);
		return fb;
	}
	
	public static FloatBuffer getVector4AsFloatBuffer(Vector4f vector)
	{
		FloatBuffer fb = Buffers.newDirectFloatBuffer(4);
		vector.get(fb);
		return fb;
	}
	
	public static FloatBuffer getMatrix3AsFloatBuffer(Matrix3f matrix)
	{
		FloatBuffer fb = Buffers.newDirectFloatBuffer(9);
		matrix.get(fb);
		return fb;
	}
	
	public static FloatBuffer getMatrix4AsFloatBuffer(Matrix4f matrix)
	{
		FloatBuffer fb = Buffers.newDirectFloatBuffer(16);
		matrix.get(fb);
		return fb;
	}
}
