package shader;

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import toolbox.BufferConversion;

/**
 * ShaderProgram takes care of loading (glsl) shaders for OpenGL.
 * Currently, only vertex and fragment shader are supported and they need to be loaded
 * together.
 * @author Lobner
 *
 */
public abstract class ShaderProgram 
{
	protected String vertexShaderFilePath;
	protected String fragmentShaderFilePath;
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	protected String vertexShaderDefines = "";
	protected String fragmentShaderDefines = "";

	protected static String VERSION = "#version 330\n";
	
	public ShaderProgram()
	{
		setShaderPaths();
	}
	
	/**
	 * Ensure that every shader program object has its shader file paths correctly set
	 */
	protected abstract void setShaderPaths();
	
	public String getVertexShaderFilePath()
	{
		return vertexShaderFilePath;
	}
	
	public String getFragmentShaderFilePath()
	{
		return fragmentShaderFilePath;
	}
	
	public boolean initShader(String vertexShaderString, String fragmentShaderString)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		vertexShaderID = loadShader(vertexShaderFilePath, vertexShaderString, GL3.GL_VERTEX_SHADER, vertexShaderDefines);
        fragmentShaderID = loadShader(fragmentShaderFilePath, fragmentShaderString, GL3.GL_FRAGMENT_SHADER, fragmentShaderDefines);
        programID = gl.glCreateProgram();
        gl.glAttachShader(programID, vertexShaderID);
        gl.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        gl.glLinkProgram(programID);
        gl.glValidateProgram(programID);
        getAllUniformLocations();
		
		return true;
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
	
	/**
	 * At the initialization of a shader, all the uniform loactions need to be looked up
	 * and stored so as to upload the values correctly. They differ from shader to shader,
	 * thus this is abstract.
	 */
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
	
	protected void loadInt(int location, int value)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glUniform1i(location, value);
	}
	
	protected void loadVec3(int location, float[] vec)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glUniform3fv(location, 1, vec, 0);
	}
	
	protected void loadVec3(int location, Vector3f vec)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		FloatBuffer fb = BufferConversion.getVector3AsFloatBuffer(vec);
		gl.glUniform3fv(location, 1, fb);
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
	
	/**
	 * Loads an integer value to the shader, as glsl doesn't support booleans.
	 * It can be accessed only via a boolean in the engine though, to assure 
	 * correct values to be uploaded.
	 * @param location The uniform location of the variable.
	 * @param value The boolean value to uploaded to the shader.
	 */
	protected void loadBoolean(int location, boolean value)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		int v = (value) ? 1 : 0;	//if value is true, assign 1, otherwise 0
		gl.glUniform1i(location, v);
	}

	/**
	 * adds the shader specified in the shaderFilePath with the specified shaderType
	 * @param shaderText 	The actual shader source code
	 * @param shaderType		shader type
	 * @return the shader object id
	 */
	private static int loadShader(String shaderPath, String shaderText, int shaderType)
	{
		return loadShader(shaderPath, shaderText, shaderType, "");
	}
	
	/**
	 * adds the shader specified in the shaderFilePath with the specified shaderType
	 * @param shaderText 	The actual shader source code
	 * @param shaderType		shader type
	 * @param shaderDefines	Preprocessor defines for the shader
	 * @return the shader object id
	 */
	private static int loadShader(String shaderPath, String shaderText, int shaderType, String shaderDefines)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
//		String shaderText = Util.readShaderFile(shaderFilePath);
		int[] shaderID = {gl.glCreateShader(shaderType)};
		
		if(shaderID[0] == 0)
		{
			System.err.println("Could not create " + shaderType + "shader: " + shaderPath);
			return -1;
		}
		
		String[] shaderLines;
		if(shaderDefines != "")
		{
			shaderLines = new String[] {VERSION, shaderDefines, shaderText};
		}
		else
		{
			shaderLines = new String[] {VERSION, shaderText};
		}
		int[] lengths = new int[shaderLines.length];
		for(int i = 0; i < shaderLines.length; i++)
		{
			lengths[i] = shaderLines[i].length();
		}
//		lengths[0] = shaderLines[0].length();
		gl.glShaderSource(shaderID[0], shaderLines.length, shaderLines, lengths, 0);
		gl.glCompileShader(shaderID[0]);
		
		int success[] = new int[1];
		gl.glGetShaderiv(shaderID[0], GL3.GL_COMPILE_STATUS, success, 0);
		if(success[0] == 0)
		{
			int[] logLength = new int[1];
			gl.glGetShaderiv(shaderID[0], GL3.GL_INFO_LOG_LENGTH, logLength, 0);
			
			byte[] log = new byte[logLength[0]];
			gl.glGetShaderInfoLog(shaderID[0], logLength[0], (int[])null, 0, log, 0);

			System.err.println(shaderPath);
			System.err.println("Error compiling shader: " + new String(log));
			return -1;
		}
		
		return shaderID[0];
	}
	
	/**
	 * Takes care of the all the references and objects to free the graphics card.
	 */
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
