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

public class MasterRenderer 
{
	private BasicLightShader shader;
	private EntityRenderer entityRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	public MasterRenderer()
	{
		
	}
	
	public void init(Matrix4f projectionMatrix, Light sun, ShaderLoader shaderLoader)
	{
		shader = new BasicLightShader();
		shaderLoader.loadShader(shader);
		entityRenderer = new EntityRenderer(shader);
		entityRenderer.setProjectionMatrix(projectionMatrix);
		//TODO: add projectionMatrix to all entityRenderers
		shader.start();
		shader.loadLightColor(sun.getColor());
		shader.stop();
		
		setClearColor(new Vector4f(0.0f, 1.0f, 0.0f, 1.0f));
	}
	
	public void render(Light sun, Camera camera)
	{
		entityRenderer.prepare();
		shader.start();
		shader.loadLightPosition(sun.getPosition());
		entityRenderer.render(camera, entities);
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
	
	public void setClearColor(Vector4f color)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glClearColor(color.x, color.y, color.z, color.w);
		gl.glDisable(GL.GL_CULL_FACE);
	}
	
	public void cleanUp()
	{
		//shader.cleanUp();	//is done in the shader loader, don't forget if changing
	}
}
