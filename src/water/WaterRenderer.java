package water;

import java.util.List;

import loader.ModelLoader;

import org.joml.Matrix4f;

import toolbox.StandardModels;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import cameras.Camera;
import entities.RawModel;

public class WaterRenderer 
{
	WaterShader shader;
	
//	Matrix4f modelMatrix;
	Matrix4f projectionMatrix;
	
	RawModel quad;
	
	public WaterRenderer(ModelLoader modelLoader, WaterShader shader, Matrix4f projectionMatrix)
	{
		quad = modelLoader.loadToVAO(StandardModels.getQuadTriangleStripVerticesInXZPlane(1), 3);
		
		this.shader = shader;
		
		this.projectionMatrix = projectionMatrix;
//		modelMatrix = getModelMatrix(xPosition, yPosition, zPosition, rotationAroundYAxis);
	}
	
	public void render(Camera camera, List<WaterTile> waterTiles)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		gl.glDisable(GL.GL_CULL_FACE);
		
		
		shader.start();
		prepareModel(gl);
		for(WaterTile tile : waterTiles)
		{
			Matrix4f modelViewProjectionMatrix = setUpModelViewProjectionMatrix(camera, tile);
			shader.loadModelViewProjectionMatrix(modelViewProjectionMatrix);
		
			gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, quad.getNumVertices());
		}
		shader.stop();
		unbindModel(gl);
		gl.glEnable(GL.GL_CULL_FACE);
	}
	
	private Matrix4f setUpModelViewProjectionMatrix(Camera camera, WaterTile tile)
	{
		Matrix4f modelViewProjectionMatrix = new Matrix4f();
		projectionMatrix.mul(camera.getViewMatrix(), modelViewProjectionMatrix);
		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix.translate(tile.getX(), tile.getY(), tile.getZ()).scale(WaterTile.TILE_SIZE);
		modelViewProjectionMatrix.mul(modelMatrix);
		
		return modelViewProjectionMatrix;
	}
	
	private void prepareModel(GL3 gl)
	{		
		gl.glBindVertexArray(quad.getVAO()[0]);
		gl.glEnableVertexAttribArray(0);
	}
	
	private void unbindModel(GL3 gl)
	{
		gl.glBindVertexArray(0);
		gl.glDisableVertexAttribArray(0);
	}
	
//	private Matrix4f getModelMatrix(float xPosition, float yPosition, float zPosition,
//			float rotationAroundYAxis)
//	{
//		Matrix4f modelMatrix = new Matrix4f();
//		modelMatrix.rotateY(rotationAroundYAxis);
//		modelMatrix.translate(xPosition, yPosition, zPosition);
//		
//		return modelMatrix;
//	}
	
	public void cleanUp()
	{
		shader.cleanUp();
	}
}
