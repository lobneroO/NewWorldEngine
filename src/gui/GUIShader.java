package gui;

import org.joml.Matrix4f;

import shader.ShaderProgram;

public class GUIShader extends ShaderProgram
{
	private int location_modelMatrix;
	
	public GUIShader() 
	{
		super();
	}
	
	@Override
	protected void setShaderPaths() 
	{
		vertexShaderFilePath = "shaders/vertex_shader/guiShader.glsl";
		fragmentShaderFilePath = "shaders/fragment_shader/guiShader.glsl";		
	}

	@Override
	protected void getAllUniformLocations() 
	{
		location_modelMatrix = getUniformLocation("uModelMatrix");
	}

	@Override
	protected void bindAttributes() 
	{
		bindAttribute(0, "position");
		bindAttribute(1, "texCoords");
	}

	public void loadModelMatrix(Matrix4f matrix)
	{
		loadMat4(location_modelMatrix, matrix);
	}
}
