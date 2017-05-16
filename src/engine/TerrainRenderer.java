package engine;

import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import cameras.Camera;
import entities.RawModel;
import shader.TerrainShader;
import terrains.Terrain;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class TerrainRenderer 
{
	private TerrainShader shader;
	
	private Matrix4f projectionMatrix;
	
	public TerrainRenderer(TerrainShader shader)
	{
		this.shader = shader;
	}
	
	public void init()
	{
		shader.loadTextures();
	}
	
	public void render(Camera camera, List<Terrain> terrains)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		for(Terrain terrain : terrains)
		{
			prepareTerrain(terrain);
			prepareMatrices(camera, terrain);
			gl.glDrawElements(GL.GL_TRIANGLES, terrain.getRawModel().getNumVertices(), 
					GL.GL_UNSIGNED_INT, 0);
			unbindTerrain();
			unbindTextures(terrain);
		}
	}
	
	public void prepareTerrain(Terrain terrain)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		RawModel rawModel = terrain.getRawModel();
		shader.loadMaterialSpecularIntensity(rawModel.getSpecularIntensity());
		shader.loadMaterialSpecularPower(rawModel.getSpecularPower());
		
		gl.glBindVertexArray(rawModel.getVAO()[0]);
		gl.glEnableVertexAttribArray(0);	//vertices
		gl.glEnableVertexAttribArray(1);	//texCoords
		gl.glEnableVertexAttribArray(2);	//normals
		
		bindTextures(terrain);
	}
	
	private void bindTextures(Terrain terrain)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		TerrainTexturePack texturePack = terrain.getTexturePack();
		
		gl.glActiveTexture(GL.GL_TEXTURE0);
//		texturePack.getBlackTexture().getTexture().enable(gl);
		texturePack.getBlackTexture().getTexture().bind(gl);
		
		gl.glActiveTexture(GL.GL_TEXTURE1);
//		texturePack.getRedTexture().getTexture().enable(gl);
		texturePack.getRedTexture().getTexture().bind(gl);
		
		gl.glActiveTexture(GL.GL_TEXTURE2);
//		texturePack.getGreenTexture().getTexture().enable(gl);
		texturePack.getGreenTexture().getTexture().bind(gl);
		
		gl.glActiveTexture(GL.GL_TEXTURE3);
//		texturePack.getBlueTexture().getTexture().enable(gl);
		texturePack.getBlueTexture().getTexture().bind(gl);
		
		gl.glActiveTexture(GL.GL_TEXTURE4);
//		terrain.getBlendMap().getTexture().enable(gl);
		terrain.getBlendMap().getTexture().bind(gl);
	}
	
	private void unbindTextures(Terrain terrain)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		TerrainTexturePack texturePack = terrain.getTexturePack();
		
		texturePack.getBlackTexture().getTexture().disable(gl);
		texturePack.getRedTexture().getTexture().disable(gl);
		texturePack.getGreenTexture().getTexture().disable(gl);
		texturePack.getBlueTexture().getTexture().disable(gl);
		terrain.getBlendMap().getTexture().disable(gl);
	}
	
	public void unbindTerrain()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		gl.glDisableVertexAttribArray(0);
		gl.glDisableVertexAttribArray(1);
		gl.glDisableVertexAttribArray(2);
		gl.glBindVertexArray(0);
	}
	
	public void prepareMatrices(Camera camera, Terrain terrain)
	{
		Matrix4f modelMatrix = Maths.createModelMatrix(terrain.getPosition(), 
				new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		Matrix4f modelViewProjectionMatrix = new Matrix4f();
		projectionMatrix.mul(camera.getViewMatrix(), modelViewProjectionMatrix);//MVP = P * V
		modelViewProjectionMatrix.mul(modelMatrix);								//MVP = PV * M
		
		shader.loadModelMatrix(modelMatrix);
		shader.loadModelViewProjectionMatrix(modelViewProjectionMatrix);
	}
	
	public void setProjectionMatrix(Matrix4f projectionMatrix)
	{
		this.projectionMatrix = projectionMatrix;
	}
}
