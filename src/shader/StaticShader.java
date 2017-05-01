package shader;

import shader.ShaderProgram;

/**
 * Static shader is just a shader program that can be used for quick debugging and testing.
 * It supports only a position input for the shader and doesn't do any lighting calculations.
 * @author Lobner
 *
 */
public class StaticShader extends ShaderProgram 
{
	
	public StaticShader() 
	{
		super();
	}

	@Override
	protected void bindAttributes() 
	{
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() 
	{
		//no uniforms used
	}

	@Override
	protected void setShaderPaths() 
	{
		vertexShaderFilePath = "shaders/vertex_shader/basicShader.glsl";
		fragmentShaderFilePath = "shaders/fragment_shader/basicShader.glsl";
	}

}
