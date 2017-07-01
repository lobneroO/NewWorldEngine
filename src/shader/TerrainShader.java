package shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TerrainShader extends StaticShader 
{
	//locations
		private int location_modelMatrix;
		private int location_modelViewProjectionMatrix;
		private int location_lightPosition;
		private int location_lightColor;
		private int location_materialSpecularIntensity;
		private int location_materialSpecularPower;
		private int location_cameraPosition;
		
		private int location_blackMap;
		private int location_redMap;
		private int location_greenMap;
		private int location_blueMap;
		private int location_blendMap;
		
		private int location_clippingPlane;
		
		public TerrainShader() 
		{
			super();
		}
		
		protected void setShaderPaths()
		{
			vertexShaderFilePath = "shaders/vertex_shader/terrainShader.glsl";
			fragmentShaderFilePath = "shaders/fragment_shader/terrainShader.glsl";
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
			
			location_blackMap = getUniformLocation("blackMap");
			location_redMap = getUniformLocation("redMap");
			location_greenMap = getUniformLocation("greenMap");
			location_blueMap = getUniformLocation("blueMap");
			location_blendMap = getUniformLocation("blendMap");
			
			location_clippingPlane = getUniformLocation("uClippingPlane");
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
		
		public void loadClippingPlane(Vector4f clippingPlane)
		{
			loadVec4(location_clippingPlane, clippingPlane);
		}
		
		public void loadTextures()
		{
			loadInt(location_blackMap, 0);
			loadInt(location_redMap, 1);
			loadInt(location_greenMap, 2);
			loadInt(location_blueMap, 3);
			loadInt(location_blendMap, 4);
		}
}
