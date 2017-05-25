package skybox;

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
	
	public SkyboxRenderer(RawModel skybox, Matrix4f projectionMatrix)
	{
		this.skybox = skybox;
		this.projectionMatrix = projectionMatrix;
		
		loadCubemap();
	}
	
	public SkyboxRenderer(RawModel skybox, Matrix4f projectionMatrix, String path, String[] textures)
	{
		this.skybox = skybox;
		this.projectionMatrix = projectionMatrix;
		
		loadCubemap(path, textures);
	}
	
	public void render(Camera camera)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		shader.start();
		
		Matrix4f viewMatrix = camera.getViewMatrix();
		Matrix4f viewProjectionMatrix = projectionMatrix.mul(viewMatrix);
		shader.loadViewProjectionMatrix(viewProjectionMatrix);
		cubemap.bind(gl, 0);
		
		gl.glDrawElements(GL.GL_TRIANGLES, skybox.getNumVertices(), GL.GL_UNSIGNED_INT, 0);
		
		shader.stop();
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
		
		cubemap.load(gl);
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
		
		cubemap.load(gl);
	}
	
	public void cleanUp()
	{
		shader.cleanUp();
	}
}
