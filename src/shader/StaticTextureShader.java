package shader;

import org.joml.Matrix4f;

import shader.ShaderProgram;

/**
 * Allows for the textureing of models. It does not support any lighting calculations
 * and thus also doesn't upload models.
 * @author Lobner
 *
 */
public class StaticTextureShader extends ShaderProgram 
{	
	//locations
	private int location_modelMatrix;
	private int location_modelViewProjectionMatrix;
	
	public StaticTextureShader() 
	{
		super();
	}
	
	protected void setShaderPaths()
	{
		vertexShaderFilePath = "shaders/vertex_shader/basicTextureShader.glsl";
		fragmentShaderFilePath = "shaders/fragment_shader/basicTextureShader.glsl";
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
