package shader;

import java.nio.FloatBuffer;

import javax.media.opengl.GL3;
import javax.media.opengl.GLContext;

import org.joml.Matrix4f;

import toolbox.BufferConversion;
import util.Util;

public abstract class ShaderProgram 
{
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram(String vertexFile,String fragmentFile)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
        vertexShaderID = loadShader(vertexFile, GL3.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL3.GL_FRAGMENT_SHADER);
        programID = gl.glCreateProgram();
        gl.glAttachShader(programID, vertexShaderID);
        gl.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        gl.glLinkProgram(programID);
        gl.glValidateProgram(programID);
        getAllUniformLocations();
    }
	
	public void start()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glUseProgram(programID);
	}
	
	public int getProgramID()
	{
		return programID;
	}
	
	public void stop()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glUseProgram(0);
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String uniformName)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		return gl.glGetUniformLocation(programID, uniformName);
	}
	
	protected void bindAttribute(int attribute, String variableName)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected abstract void bindAttributes();
	
	protected void loadFloat(int location, float value)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glUniform1f(location, value);
	}
	
	protected void loadVec3(int location, float[] vec)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glUniform3fv(location, 1, vec, 0);
	}
	
	/**
	 * Loads a matrix4f into the shader as a 4x4 matrix. The matrix won't be transposed
	 * @param location location of the matrix in the shader program
	 * @param matrix the matrix that is to be uploaded
	 */
	protected void loadMat4(int location, Matrix4f matrix)
	{
		loadMat4(location, matrix, false);
	}
	
	/**
	 * Loads a matrix4f into the shader as a 4x4 matrix.
	 * @param location location of the matrix in the shader program
	 * @param matrix the matrix that is to be uploaded
	 * @param transpose determines, whether OpenGL should transpose the matrix
	 */
	protected void loadMat4(int location, Matrix4f matrix, boolean transpose)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		FloatBuffer fb = BufferConversion.getMatrix4AsFloatBuffer(matrix);
		gl.glUniformMatrix4fv(location, 1, transpose, fb);
	}
	
	protected void loadBoolean(int location, boolean value)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		int v = (value) ? 1 : 0;	//if value is true, assign 1, otherwise 0
		gl.glUniform1i(location, v);
	}
	
	/**
	 * adds the shader specified in the shaderFilePath with the specified shaderType
	 * @param shaderFilePath 	location of the shader file
	 * @param shaderType		shader type
	 * @return the shader object id
	 */
	private static int loadShader(String shaderFilePath, int shaderType)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		String shaderText = Util.readShaderFile(shaderFilePath);
		int[] shaderID = {gl.glCreateShader(shaderType)};
		
		if(shaderID[0] == 0)
		{
			System.err.println("Could not create shader: " + shaderType);
			return -1;
		}
		
		String[] shaderLines = new String[] {shaderText};
		int[] lengths = new int[1];
		lengths[0] = shaderLines[0].length();
		gl.glShaderSource(shaderID[0], 1, shaderLines, lengths, 0);
		gl.glCompileShader(shaderID[0]);
		
		int success[] = new int[1];
		gl.glGetShaderiv(shaderID[0], GL3.GL_COMPILE_STATUS, success, 0);
		if(success[0] == 0)
		{
			int[] logLength = new int[1];
			gl.glGetShaderiv(shaderID[0], GL3.GL_INFO_LOG_LENGTH, logLength, 0);
			
			byte[] log = new byte[logLength[0]];
			gl.glGetShaderInfoLog(shaderID[0], logLength[0], (int[])null, 0, log, 0);
			
			System.err.println("Error compiling shader: " + new String(log));
			return -1;
		}
		
		return shaderID[0];
	}
	
	public void cleanUp()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		stop();
		gl.glDetachShader(programID, vertexShaderID);
		gl.glDetachShader(programID, fragmentShaderID);
		gl.glDeleteShader(vertexShaderID);
		gl.glDeleteShader(fragmentShaderID);
		gl.glDeleteProgram(programID);
	}
}
