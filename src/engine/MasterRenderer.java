package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import loader.ShaderLoader;
import cameras.Camera;
import entities.Entity;
import entities.Light;
import entities.TexturedModel;
import shader.BasicLightShader;
import shader.TerrainShader;
import terrains.Terrain;

public class MasterRenderer 
{
	private BasicLightShader basicLightShader;
	private EntityRenderer entityRenderer;
	private TerrainShader terrainShader;
	private TerrainRenderer terrainRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public MasterRenderer()
	{
		
	}
	
	public void init(Matrix4f projectionMatrix, Light sun, ShaderLoader shaderLoader)
	{
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
	
	public void render(Light sun, Camera camera)
	{
		prepare();
		
		basicLightShader.start();
		basicLightShader.loadLightPosition(sun.getPosition());
		entityRenderer.render(camera, entities);
		basicLightShader.stop();
		
		terrainShader.start();
		terrainShader.loadLightPosition(sun.getPosition());
		terrainRenderer.render(camera, terrains);
		terrainShader.stop();
		
		entities.clear();
		terrains.clear();
	}
	
	public void processEntity(Entity entity)
	{
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		
		if(batch != null)
		{
			batch.add(entity);
		}
		else
		{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);	//this entityModel parameter is just a lookup key, 
													//the rendered one (which is the same reference) is in the 
													//list called newBatch which will be taken out of the hashmap
		}
	}
	
	public void processTerrains(Terrain terrain)
	{
		terrains.add(terrain);
	}
	
	public void prepare()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
	}
	
	public void setClearColor(Vector4f color)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glClearColor(color.x, color.y, color.z, color.w);
		gl.glDisable(GL.GL_CULL_FACE);
	}
	
	public void cleanUp()
	{
		//basicLightShader.cleanUp();	//is done in the shader loader, don't forget if changing
	}
}
