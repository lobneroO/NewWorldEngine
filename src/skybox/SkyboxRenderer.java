package skybox;

import loader.ShaderLoader;

import org.joml.Matrix4f;
import org.joml.Vector3f;

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
	private Matrix4f modelMatrix;	//the cube is centered around (0,0,0) 
									//and needs to be moved accordingly
	private Matrix4f projectionMatrix;
	private SkyboxShader shader;
	
	public SkyboxRenderer(ShaderLoader loader, RawModel skybox, float skyboxLength, Matrix4f projectionMatrix)
	{
		init(loader, skybox, skyboxLength, projectionMatrix);
		
		loadCubemap();
	}
	
	public SkyboxRenderer(ShaderLoader loader, RawModel skybox, float skyboxLength, Matrix4f projectionMatrix, String path, String[] textures)
	{
		init(loader, skybox, skyboxLength, projectionMatrix);
		
		loadCubemap(path, textures);
	}
	
	
	public void init(ShaderLoader loader, RawModel skybox, float skyboxLength, Matrix4f projectionMatrix)
	{
		this.skybox = skybox;
		modelMatrix = new Matrix4f();
		float t = skyboxLength/2; //translate it to match the terrain
		modelMatrix.translate(t, 0, t);
		this.projectionMatrix = projectionMatrix;
		shader = new SkyboxShader();
		loader.loadShader(shader);
		shader.start();
		shader.loadCubemap();
		shader.stop();
	}
	
	public void render(Camera camera)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();

		shader.start();
		
		prepareSkybox();
		prepareMatrices(camera);
		prepareCubemap();
		
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, skybox.getNumVertices());
		
		unbindSkybox();
		
		shader.stop();
		gl.glEnable(GL.GL_CULL_FACE);
	}
	
	private void prepareSkybox()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glDisable(GL.GL_CULL_FACE);
		gl.glDepthFunc(GL.GL_LEQUAL);	//the skybox is always on the farplane, but the standard test
										//fails if something is ON the farplane (as the test is Less Than),
										//thus the skybox would always fail the depth test. 
										//this way it will never fail the depth test
		
		gl.glBindVertexArray(skybox.getVAO()[0]);
		gl.glEnableVertexAttribArray(0);	//vertices
	}
	
	private void prepareMatrices(Camera camera)
	{
		Matrix4f modelViewProjectionMatrix = new Matrix4f();
		//MVPMatrix = VMatrix * MMatrix
		camera.getViewMatrix().mul(modelMatrix, modelViewProjectionMatrix);
		//MVPMatrix = PMatrix * MVMatrix
		projectionMatrix.mul(modelViewProjectionMatrix, modelViewProjectionMatrix);
		shader.loadModelViewProjectionMatrix(modelViewProjectionMatrix);
	}
	
	private void prepareCubemap()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		cubemap.bind(gl, GL.GL_TEXTURE0);
	}
	
	private void unbindSkybox()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glDisableVertexAttribArray(0);
		gl.glBindVertexArray(0);
	}
	
	/**
	 * load the cube map from the default texture location
	 * textures/cubemap/right.jpg
	 * textures/cubemap/left.jpg
	 * textures/cubemap/top.jpg
	 * textures/cubemap/bottom.jpg
	 * textures/cubemap/back.jpg
	 * textures/cubemap/front.jpg
	 */
	public void loadCubemap()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		cubemap = new CubemapTexture("textures/skybox/", TEX_RIGHT, TEX_LEFT, TEX_TOP, 
				TEX_BOTTOM, TEX_BACK, TEX_FRONT);
		
		cubemap.load(gl, new boolean[]{true, true, false, false, true, true});
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
	
	public void translateSkybox(Vector3f t)
	{
		modelMatrix.identity();
		modelMatrix.translate(t);
	}
	
	public void translateSkybox(Matrix4f m)
	{
		modelMatrix = m;
	}
}
