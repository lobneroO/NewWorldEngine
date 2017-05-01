package engine;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLContext;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import cameras.ThirdPersonCamera;
import shader.StaticTextureShader;
import toolbox.Maths;
import entities.Entity;
import entities.RawModel;
import entities.TexturedModel;

public class Renderer 
{
	//controls
	ThirdPersonCamera camera;
	//tmp
	boolean printed = false;
	//preferences
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.01f;
	private static final float FAR_PLANE = 100;
	private int windowWidth = 1024;
	private int windowHeight = 768;
	//
	private Matrix4f projectionMatrix;
	
	public Renderer(int windowWidth, int windowHeight, ThirdPersonCamera camera)
	{
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		
		this.camera = camera;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.identity();
		createProjectionMatrix();
	}
	
	public void prepare()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
	}
	
	public void render(Entity entity, StaticTextureShader shader)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getRawModel();
		
		gl.glBindVertexArray(rawModel.getVAO()[0]);
		gl.glEnableVertexAttribArray(0);
		gl.glEnableVertexAttribArray(1);
		
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
		gl.glBindVertexArray(0);
	}
	
	public void setClearColor(Vector4f color)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glClearColor(color.x, color.y, color.z, color.w);
		gl.glDisable(GL.GL_CULL_FACE);
	}
	
	private void createProjectionMatrix()
	{
		float aspect = (float)windowWidth / (float)windowHeight;
		projectionMatrix.setPerspective((float)Math.toRadians(FOV), aspect, NEAR_PLANE, FAR_PLANE);
	}
}
