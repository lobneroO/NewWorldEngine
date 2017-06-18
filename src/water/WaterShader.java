package water;

import org.joml.Matrix4f;

import shader.ShaderProgram;

public class WaterShader extends ShaderProgram
{
	private int location_modelViewProjectionMatrix;
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
	
}
