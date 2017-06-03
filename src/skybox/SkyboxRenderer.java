package skybox;

import loader.ShaderLoader;

import org.joml.Matrix4f;

import textures.CubemapTexture;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import cameras.Camera;
import entities.RawModel;

public class SkyboxRenderer 
{
	private static String TEX_TOP = "top.jpg";
	private static String TEX_BOTTOM = "bottom.jpg";
	private static String TEX_LEFT = "left.jpg";
	private static String TEX_RIGHT = "right.jpg";
	private static String TEX_FRONT = "front.jpg";
	private static String TEX_BACK = "back.jpg";
	
	private RawModel skybox;
	private CubemapTexture cubemap;
	private Matrix4f projectionMatrix;
	private SkyboxShader shader;
	
	public SkyboxRenderer(ShaderLoader loader, RawModel skybox, Matrix4f projectionMatrix)
	{
		init(loader, skybox, projectionMatrix);
		
		loadCubemap();
	}
	
	public SkyboxRenderer(ShaderLoader loader, RawModel skybox, Matrix4f projectionMatrix, String path, String[] textures)
	{
		init(loader, skybox, projectionMatrix);
		
		loadCubemap(path, textures);
	}
	
	
	public void init(ShaderLoader loader, RawModel skybox, Matrix4f projectionMatrix)
	{
		this.skybox = skybox;
		this.projectionMatrix = projectionMatrix;
		shader = new SkyboxShader();
		loader.loadShader(shader);
	}
	
	public void render(Camera camera)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glDisable(GL.GL_CULL_FACE);
		
		shader.start();
		
		gl.glBindVertexArray(skybox.getVAO()[0]);
		gl.glEnableVertexAttribArray(0);	//vertices
		
		Matrix4f viewMatrix = camera.getViewMatrix();
		Matrix4f viewProjectionMatrix = new Matrix4f();
		viewProjectionMatrix.identity();
		viewProjectionMatrix.mul(projectionMatrix);
		viewProjectionMatrix.mul(viewMatrix);
		shader.loadViewProjectionMatrix(viewProjectionMatrix);
		cubemap.bind(gl, GL.GL_TEXTURE0);
		
//		gl.glDrawElements(GL.GL_TRIANGLES, skybox.getNumVertices(), GL.GL_UNSIGNED_INT, 0);
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, skybox.getNumVertices());
		
		gl.glDisableVertexAttribArray(0);
		
		shader.stop();
		gl.glEnable(GL.GL_CULL_FACE);
	}
	
	/**
	 * load the cube map from the default texture location
	 * textures/cubemap/right.jpg
	 * textures/cubemap/left.jpg
	 * textures/cubemap/top.jpg
	 * textures/cubemap/bottom.jpg
	 * textures/cubemap/front.jpg
	 * textures/cubemap/back.jpg
	 */
	public void loadCubemap()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		cubemap = new CubemapTexture("textures/skybox/", TEX_RIGHT, TEX_LEFT, TEX_TOP, 
				TEX_BOTTOM, TEX_FRONT, TEX_BACK);
		
		cubemap.loadWithJOGL(gl);
	}
	
	/**
	 * load the cube map with the given texture paths
	 * @param textures individual textures for the cubemap in the order right, left, top, bottom, front, back
	 */
	public void loadCubemap(String path, String[] textures)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		cubemap = new CubemapTexture("textures/skybox/", textures[0], textures[1], textures[2], 
				textures[3], textures[4], textures[5]);
		
		cubemap.loadWithJOGL(gl);
	}
	
	public void cleanUp()
	{
		shader.cleanUp();
	}
}
