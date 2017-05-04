package engine;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLContext;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import cameras.Camera;
import entities.RawModel;
import shader.TerrainShader;
import terrains.Terrain;
import toolbox.Maths;

public class TerrainRenderer 
{
	private TerrainShader shader;
	
	private Matrix4f projectionMatrix;
	
	private Camera camera;
	
	public TerrainRenderer(Camera camera, TerrainShader shader, Matrix4f projectionMatrix)
	{
		this.camera = camera;
		this.shader = shader;
		this.projectionMatrix = projectionMatrix;
	}
	
	public void render(Terrain terrain)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		RawModel rawModel = terrain.getModel();
		shader.loadMaterialSpecularIntensity(rawModel.getSpecularIntensity());
		shader.loadMaterialSpecularPower(rawModel.getSpecularPower());
		
		gl.glBindVertexArray(rawModel.getVAO()[0]);
		gl.glEnableVertexAttribArray(0);	//vertices
		gl.glEnableVertexAttribArray(1);	//texCoords
		gl.glEnableVertexAttribArray(2);	//normals
		
		Matrix4f modelMatrix = Maths.createModelMatrix(new Vector3f(terrain.getX(),0,terrain.getZ()), 
				new Vector3f(0,0,0), new Vector3f(1,1,1));
		Matrix4f modelViewProjectionMatrix = new Matrix4f();
		projectionMatrix.mul(camera.getViewMatrix(), modelViewProjectionMatrix);//MVP = P * V
		modelViewProjectionMatrix.mul(modelMatrix);								//MVP = PV * M
		
		shader.loadModelMatrix(modelMatrix);
		shader.loadModelViewProjectionMatrix(modelViewProjectionMatrix);
		
		gl.glActiveTexture(GL.GL_TEXTURE0);
		terrain.bindTexture();
		
		gl.glDrawElements(GL.GL_TRIANGLES, rawModel.getNumVertices(), GL.GL_UNSIGNED_INT, 0);
		gl.glDisableVertexAttribArray(0);
		gl.glDisableVertexAttribArray(1);
		gl.glDisableVertexAttribArray(2);
		gl.glBindVertexArray(0);
	}
}
