package shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import shader.ShaderProgram;

/**
 * Allows for the texturing and lighting of models.
 * The lighting is a basic phong shading, long term PBR is going to be implemented
 * @author Lobner
 *
 */
public class BasicLightShader extends ShaderProgram 
{	
	//locations
	private int location_modelMatrix;
	private int location_modelViewProjectionMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_materialSpecularIntensity;
	private int location_materialSpecularPower;
	private int location_cameraPosition;
	
	public BasicLightShader() 
	{
		super();
	}
	
	protected void setShaderPaths()
	{
		vertexShaderFilePath = "shaders/vertex_shader/basicLightingShader.glsl";
		fragmentShaderFilePath = "shaders/fragment_shader/basicLightingShader.glsl";
	}

	@Override
	protected void bindAttributes() 
	{
		bindAttribute(0, "position");
		bindAttribute(1, "texCoords");
		bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() 
	{
		location_modelMatrix = getUniformLocation("uModelMatrix");
		location_modelViewProjectionMatrix = getUniformLocation("uModelViewProjectionMatrix");
		location_lightPosition = getUniformLocation("uLightPosition");
		location_lightColor = getUniformLocation("uLightColor");
		location_materialSpecularIntensity = getUniformLocation("uIntensity");
		location_materialSpecularPower = getUniformLocation("uPower");
		location_cameraPosition = getUniformLocation("uCamPos");
	}
	
	public void loadModelMatrix(Matrix4f matrix)
	{
		loadMat4(location_modelMatrix, matrix);
	}
	
	public void loadModelViewProjectionMatrix(Matrix4f matrix)
	{
		loadMat4(location_modelViewProjectionMatrix, matrix);
	}
	
	public void loadLightPosition(Vector3f position)
	{
		loadVec3(location_lightPosition, position);
	}
	
	public void loadLightColor(Vector3f color)
	{
		loadVec3(location_lightColor, color);
	}
	
	public void loadMaterialSpecularIntensity(float intensity)
	{
		loadFloat(location_materialSpecularIntensity, intensity);
	}
	
	public void loadMaterialSpecularPower(float power)
	{
		loadFloat(location_materialSpecularPower, power);
	}
	
	public void loadCameraPosition(Vector3f position)
	{
		loadVec3(location_cameraPosition, position);
	}
	
}
