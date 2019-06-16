package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.TexturedEntity;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import loader.ModelLoader;
import loader.ShaderLoader;
import cameras.Camera;
import entities.Light;
import entities.RawModel;
import entities.TexturedModel;
import gui.GUIRenderer;
import shader.BasicLightShader;
import shader.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import textures.GUITexture;
import toolbox.StandardModels;

/**
 * Manages all the individual renderers, their shaders and OpenGL set ups
 * @author Lobner
 *
 */
public class MasterRenderer 
{
	private Matrix4f projectionMatrix;
	
	private BasicLightShader basicLightShader;
	private EntityRenderer entityRenderer;
	private TerrainShader terrainShader;
	private TerrainRenderer terrainRenderer;
	private SkyboxRenderer skyboxRenderer;
	private GUIRenderer guiRenderer;
	boolean skyboxIsSet = false;
	boolean guiIsSet = false;
	
	private Map<TexturedModel, List<TexturedEntity>> texturedEntities = new HashMap<TexturedModel, List<TexturedEntity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<GUITexture> guiTextures = new ArrayList<GUITexture>();
	
	public MasterRenderer()
	{
		
	}
	
	/**
	 * Sets up the individual shaders and corresponding renderers
	 * @param projectionMatrix The projection matrix that is shared among all renderers
	 * @param sun The light for the scene
	 * @param shaderLoader The shader loader that takes care of cleaning up afterwards
	 */
	public void init(Matrix4f projectionMatrix, Light sun, ShaderLoader shaderLoader)
	{
		this.projectionMatrix = projectionMatrix;
		
		basicLightShader = new BasicLightShader();
		shaderLoader.loadShader(basicLightShader);
		entityRenderer = new EntityRenderer(basicLightShader);
		entityRenderer.setProjectionMatrix(projectionMatrix);
		basicLightShader.start();
		basicLightShader.loadLightColor(sun.getColor());
		basicLightShader.stop();
		
		terrainShader = new TerrainShader();
		shaderLoader.loadShader(terrainShader);
		terrainShader.start();
		terrainShader.loadLightColor(sun.getColor());
		terrainRenderer = new TerrainRenderer(terrainShader);
		terrainShader.stop();
		terrainRenderer.setProjectionMatrix(projectionMatrix);
		
		setClearColor(new Vector4f(0.0f, 1.0f, 0.0f, 1.0f));
	}
	
	/**
	 * Render the texturedEntity batches, i.e. if two entities have the same RawModel and Texture,
	 * they don't need individual OpenGL set ups (other than the matrices), so they are rendered
	 * as efficiently as possible
	 * @param sun
	 * @param camera
	 */
	public void render(Light sun, Camera camera)
	{
		prepare();
		
		basicLightShader.start();
		basicLightShader.loadLightPosition(sun.getPosition());
		entityRenderer.render(camera, texturedEntities);
		basicLightShader.stop();
		
		terrainShader.start();
		terrainShader.loadLightPosition(sun.getPosition());
		terrainRenderer.render(camera, terrains);
		terrainShader.stop();

		texturedEntities.clear();
		terrains.clear();
		
		if(skyboxIsSet)
		{
			skyboxRenderer.render(camera);
		}
		
		if(guiIsSet)
		{
			guiRenderer.render(guiTextures);
		}
		
		guiTextures.clear();
	}
	
	/**
	 * Puts the new texturedEntity into the batch of equal texturedEntities.
	 * If there is no batch for this texturedEntity yet, a new one will be created.
	 * @param texturedEntity
	 */
	public void processTexturedEntity(TexturedEntity texturedEntity)
	{
		TexturedModel entityModel = texturedEntity.getModel();
		List<TexturedEntity> batch = texturedEntities.get(entityModel);
		
		if(batch != null)
		{
			batch.add(texturedEntity);
		}
		else
		{
			List<TexturedEntity> newBatch = new ArrayList<TexturedEntity>();
			newBatch.add(texturedEntity);
			texturedEntities.put(entityModel, newBatch);	//this entityModel parameter is just a lookup key,
													//the rendered one (which is the same reference) is in the 
													//list called newBatch which will be taken out of the hashmap
		}
	}
	
	/**
	 * Adds the terrain to the terrains that ought to be rendered
	 * @param terrain
	 */
	public void processTerrains(Terrain terrain)
	{
		terrains.add(terrain);
	}
	
	public void processGUITextures(GUITexture guiTexture)
	{
		guiTextures.add(guiTexture);
	}
	
	public void setGUI(ShaderLoader shaderLoader, ModelLoader modelLoader)
	{
		guiRenderer = new GUIRenderer(modelLoader, shaderLoader);
		
		guiIsSet = true;
	}
	
	public void setSkybox(ShaderLoader shaderLoader, ModelLoader modelLoader)
	{
		float skyboxSize = 100f;
		RawModel skybox = modelLoader.loadToVAO(StandardModels.getCubeVertices(skyboxSize), 3);
		skyboxRenderer = new SkyboxRenderer(shaderLoader, skybox, projectionMatrix);
		float t = skyboxSize/2; //translate it to match the terrain
		skyboxRenderer.translateSkybox(new Vector3f(t, 0, t));
		skyboxIsSet = true;
	}
	
	public void setSkybox(ShaderLoader shaderLoader, ModelLoader modelLoader, String path, 
			String[] textures)
	{
		float skyboxSize = 100f;
		RawModel skybox = modelLoader.loadToVAO(StandardModels.getCubeVertices(skyboxSize), 3);
		skyboxRenderer = new SkyboxRenderer(shaderLoader,skybox, projectionMatrix, path, textures);
		float t = skyboxSize/2; //translate it to match the terrain
		skyboxRenderer.translateSkybox(new Vector3f(t, 0, t));
		skyboxIsSet = true;
	}
	
	public void prepare()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void enableCulling()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glCullFace(GL.GL_BACK);
	}
	
	public static void disableCulling()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glDisable(GL.GL_CULL_FACE);
	}
	
	public void setClearColor(Vector4f color)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glClearColor(color.x, color.y, color.z, color.w);
		gl.glDisable(GL.GL_CULL_FACE);
	}
	
	public void cleanUp()
	{
		skyboxRenderer.cleanUp();
		guiRenderer.cleanUp();
	}
}
