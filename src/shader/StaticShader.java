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
	private static final String VERTEX_FILE = "shaders/vertex_shader/basicShader.glsl";
	private static final String FRAGMENT_FILE = "shaders/fragment_shader/basicShader.glsl";
	
	public StaticShader() 
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
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

}
