package skybox;

import org.joml.Matrix4f;

import shader.ShaderProgram;

/**
 * Allows for the textureing of models. It does not support any lighting calculations
 * and thus also doesn't upload models.
 * @author Lobner
 *
 */
public class SkyboxShader extends ShaderProgram 
{	
	//locations
	private int location_cubemap;
	private int location_modelViewProjectionMatrix;
	
	public SkyboxShader() 
	{
		super();
	}
	
	protected void setShaderPaths()
	{
		vertexShaderFilePath = "shaders/vertex_shader/skybox.glsl";
		fragmentShaderFilePath = "shaders/fragment_shader/skybox.glsl";
	}

	@Override
	protected void bindAttributes() 
	{
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() 
	{
		location_modelViewProjectionMatrix = getUniformLocation("uModelViewProjectionMatrix");
		location_cubemap = getUniformLocation("cubemapTexture");
	}
	
	public void loadModelViewProjectionMatrix(Matrix4f matrix)
	{
		loadMat4(location_modelViewProjectionMatrix, matrix);
	}
	
	public void loadCubemap()
	{
		loadInt(location_cubemap, 0);
	}
	
}
