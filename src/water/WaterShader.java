package water;

import org.joml.Matrix4f;

import shader.ShaderProgram;

public class WaterShader extends ShaderProgram
{
	private int location_modelViewProjectionMatrix;
	
	private int location_reflectionTexture;
	private int location_refractionTexture;
	private int location_dudvTexture;
	
	@Override
	protected void setShaderPaths() 
	{
		vertexShaderFilePath = "shaders/vertex_shader/waterShader.glsl";
		fragmentShaderFilePath = "shaders/fragment_shader/waterShader.glsl";
	}

	@Override
	protected void getAllUniformLocations() 
	{
		location_modelViewProjectionMatrix = getUniformLocation("uModelViewProjectionMatrix");
		location_reflectionTexture = getUniformLocation("uReflectionTexture");
		location_refractionTexture = getUniformLocation("uRefractionTexture");
		location_dudvTexture = getUniformLocation("uDudvTexture");
	}

	@Override
	protected void bindAttributes() 
	{
		bindAttribute(1, "position");
	}
	
	public void loadModelViewProjectionMatrix(Matrix4f matrix)
	{
		loadMat4(location_modelViewProjectionMatrix, matrix);
	}
	
	public void setupTextureUnits()
	{
		loadInt(location_reflectionTexture, 0);
		loadInt(location_refractionTexture, 1);
		loadInt(location_dudvTexture, 2);
	}
}
