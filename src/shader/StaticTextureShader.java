package shader;

import org.joml.Matrix4f;

import shader.ShaderProgram;

public class StaticTextureShader extends ShaderProgram 
{
	private static final String VERTEX_FILE = "shaders/vertex_shader/basicTextureShader.glsl";
	private static final String FRAGMENT_FILE = "shaders/fragment_shader/basicTextureShader.glsl";
	
	//locations
	private int location_modelMatrix;
	private int location_modelViewProjectionMatrix;
	
	public StaticTextureShader() 
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() 
	{
		bindAttribute(0, "position");
		bindAttribute(1, "texCoords");
	}

	@Override
	protected void getAllUniformLocations() 
	{
		location_modelMatrix = getUniformLocation("uModelMatrix");
		location_modelViewProjectionMatrix = getUniformLocation("uModelViewProjectionMatrix");
	}
	
	public void loadModelMatrix(Matrix4f matrix)
	{
		loadMat4(location_modelMatrix, matrix);
	}
	
	public void loadModelViewProjectionMatrix(Matrix4f matrix)
	{
		loadMat4(location_modelViewProjectionMatrix, matrix);
	}
	
}
