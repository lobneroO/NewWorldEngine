package shader;

import shader.ShaderProgram;

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
