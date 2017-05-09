package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cameras.Camera;
import entities.Entity;
import entities.Light;
import entities.TexturedModel;
import shader.BasicLightShader;

public class MasterRenderer 
{
	private BasicLightShader shader = new BasicLightShader();
	private EntityRenderer renderer = new EntityRenderer(shader);
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	public MasterRenderer()
	{
		
	}
	
	public void init(Light sun)
	{
		//TODO: add projectionMatrix to all renderers
		shader.start();
		shader.loadLightColor(sun.getColor());
		shader.stop();
	}
	
	public void render(Light sun, Camera camera)
	{
		renderer.prepare();
		shader.start();
		shader.loadLightPosition(sun.getPosition());
		renderer.render(camera, entities);
		shader.stop();
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
	
	public void cleanUp()
	{
		//shader.cleanUp();	//is done in the shader loader, don't forget if changing
	}
}
