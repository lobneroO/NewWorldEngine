package engine;

import java.util.List;
import java.util.Map;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import org.joml.Matrix4f;

import cameras.Camera;
import shader.BasicLightShader;
import toolbox.Maths;
import entities.Entity;
import entities.RawModel;
import entities.TexturedModel;

/**
 * The Renderer class takes care of the rendering tasks themselves.
 * That is, it takes care of loading up the data to the grapics card, clear the frame buffer
 * and setting up the projection matrix.
 * @author Lobner
 *
 */
public class EntityRenderer 
{
	BasicLightShader shader;
	//tmp
	boolean printed = false;
	//preferences
	//
	private Matrix4f projectionMatrix;
	
	public EntityRenderer(BasicLightShader shader)
	{
		this.shader = shader;
	}
	
	public void render(Camera camera, Map<TexturedModel, List<Entity>> entities)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		for(TexturedModel model : entities.keySet())
		{
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity : batch)
			{
				prepareEntity(camera, entity);
				
				gl.glDrawElements(GL.GL_TRIANGLES, model.getRawModel().getNumVertices(), GL.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	public void prepareTexturedModel(TexturedModel model)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		RawModel rawModel = model.getRawModel();
		shader.loadMaterialSpecularIntensity(rawModel.getSpecularIntensity());
		shader.loadMaterialSpecularPower(rawModel.getSpecularPower());
		
		gl.glBindVertexArray(rawModel.getVAO()[0]);
		gl.glEnableVertexAttribArray(0);	//vertices
		gl.glEnableVertexAttribArray(1);	//texCoords
		gl.glEnableVertexAttribArray(2);	//normals
		
		if(model.getHasTransparency())
		{
			MasterRenderer.disableCulling();
		}
		gl.glActiveTexture(GL.GL_TEXTURE0);
		model.bindTexture();
	}
	
	public void unbindTexturedModel()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		gl.glDisableVertexAttribArray(0);
		gl.glDisableVertexAttribArray(1);
		gl.glDisableVertexAttribArray(2);
		gl.glBindVertexArray(0);
		
		MasterRenderer.enableCulling();
	}
	
	public void prepareEntity(Camera camera, Entity entity)
	{
		Matrix4f modelMatrix = Maths.createModelMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
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
