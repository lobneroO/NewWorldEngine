package engine;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import cameras.ThirdPersonCamera;
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
	//controls
	ThirdPersonCamera camera;
	//tmp
	boolean printed = false;
	//preferences
	//
	private Matrix4f projectionMatrix;
	
	public EntityRenderer(ThirdPersonCamera camera, Matrix4f projectionMatrix)
	{
		
		this.camera = camera;
		
		this.projectionMatrix = projectionMatrix;
		
	}
	
	public void prepare()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
	}
	
	public void render(Entity entity, BasicLightShader shader)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getRawModel();
		shader.loadMaterialSpecularIntensity(rawModel.getSpecularIntensity());
		shader.loadMaterialSpecularPower(rawModel.getSpecularPower());
		
		gl.glBindVertexArray(rawModel.getVAO()[0]);
		gl.glEnableVertexAttribArray(0);	//vertices
		gl.glEnableVertexAttribArray(1);	//texCoords
		gl.glEnableVertexAttribArray(2);	//normals
		
		Matrix4f modelMatrix = Maths.createModelMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		Matrix4f modelViewProjectionMatrix = new Matrix4f();
		projectionMatrix.mul(camera.getViewMatrix(), modelViewProjectionMatrix);//MVP = P * V
		modelViewProjectionMatrix.mul(modelMatrix);								//MVP = PV * M
		
		shader.loadModelMatrix(modelMatrix);
		shader.loadModelViewProjectionMatrix(modelViewProjectionMatrix);
		
		gl.glActiveTexture(GL.GL_TEXTURE0);
		model.bindTexture();
		
		gl.glDrawElements(GL.GL_TRIANGLES, rawModel.getNumVertices(), GL.GL_UNSIGNED_INT, 0);
		gl.glDisableVertexAttribArray(0);
		gl.glDisableVertexAttribArray(1);
		gl.glDisableVertexAttribArray(2);
		gl.glBindVertexArray(0);
	}
	
	public void setClearColor(Vector4f color)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glClearColor(color.x, color.y, color.z, color.w);
		gl.glDisable(GL.GL_CULL_FACE);
	}
}
