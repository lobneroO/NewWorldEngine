package engine;

import java.util.List;
import java.util.Map;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import entities.*;
import entities.materials.Material;
import org.joml.Matrix4f;

import cameras.Camera;
import org.joml.Vector3f;
import shader.BasicLightShader;
import shader.BasicMaterialLightShader;
import toolbox.Maths;

/**
 * The renderer for TexturedEntity objects
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

	public void setShader(BasicLightShader shader)
	{
		this.shader = shader;
	}

	/**
	 * Renders a list of textured model entities in batches of equivalent RawModel and Texture combinations
	 * i.e. Entities that share the RawModel and Texture object are rendered in a series
	 * to not set up everything anew
	 * @param camera The camera responsible for the view matrix
	 * @param entities The list of textured model entities that share the same RawModel object and Texture
	 */
	public void renderMaterialEntities(Camera camera, Map<MaterialModel, List<MaterialEntity>> entities)
	{
//		if (true)
//			return;

		//TODO: there is a memory leak somewhere. it seems to disappear with prepareEntity being commented out
		//TODO: this holds true for material model. textured model is not a problem

		GL3 gl = GLContext.getCurrentGL().getGL3();

		for(MaterialModel model : entities.keySet())
		{
			prepareMaterialModel(model);
			List<MaterialEntity> batch = entities.get(model);
			for(MaterialEntity materialEntity : batch)
			{
				prepareEntity(camera, materialEntity);//materialEntity.getPosition(), materialEntity.getRotation(), materialEntity.getScale());

				gl.glDrawElements(GL.GL_TRIANGLES, model.getRawModel().getNumVertices(), GL.GL_UNSIGNED_INT, 0);
			}
			unbindMaterialModel();
		}
	}
	
	/**
	 * Renders a list of textured model entities in batches of equivalent RawModel and Texture combinations
	 * i.e. Entities that share the RawModel and Texture object are rendered in a series
	 * to not set up everything anew
	 * @param camera The camera responsible for the view matrix
	 * @param entities The list of textured model entities that share the same RawModel object and Texture
	 */
	public void renderTexturedEntities(Camera camera, Map<TexturedModel, List<TexturedEntity>> entities)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		for(TexturedModel model : entities.keySet())
		{
			prepareTexturedModel(model);
			List<TexturedEntity> batch = entities.get(model);
			for(TexturedEntity texturedEntity : batch)
			{
				prepareEntity(camera, texturedEntity);
				
				gl.glDrawElements(GL.GL_TRIANGLES, model.getRawModel().getNumVertices(), GL.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	public void prepareMaterialModel(MaterialModel model)
	{
		Material material = model.getMaterial();
		BasicMaterialLightShader mShader = (BasicMaterialLightShader)shader;
		mShader.loadDiffuseColor(material.diffuseColor);

		prepareModel(model);
	}

	public void prepareTexturedModel(TexturedModel model)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();

		prepareModel(model);

		gl.glActiveTexture(GL.GL_TEXTURE0);
		model.bindTexture();
	}

	public void prepareModel(Model model)
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
	}
	
	public void unbindTexturedModel()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		gl.glDisableVertexAttribArray(0);
		gl.glDisableVertexAttribArray(1);
		gl.glDisableVertexAttribArray(2);
		gl.glBindVertexArray(0);
		
		//instead of checking whether back face culling was disabled just enable by default
		MasterRenderer.enableCulling();
	}

	public void unbindMaterialModel()
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
//		Matrix4f[] modelMatrix = { Maths.createModelMatrix(entity.getPosition(), entity.getRotation(), entity.getScale())};
		Matrix4f modelMatrix = Maths.createModelMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		Matrix4f modelViewProjectionMatrix = new Matrix4f();
		projectionMatrix.mul(camera.getViewMatrix(), modelViewProjectionMatrix);//MVP = P * V
		modelViewProjectionMatrix.mul(modelMatrix);								//MVP = PV * M

		shader.loadModelMatrix(modelMatrix);
		shader.loadModelViewProjectionMatrix(modelViewProjectionMatrix);
	}

	public void prepareEntity(Camera camera, Vector3f position, Vector3f rotation, Vector3f scale)
	{
//		Matrix4f[] modelMatrix = { Maths.createModelMatrix(entity.getPosition(), entity.getRotation(), entity.getScale())};
		Matrix4f modelMatrix = Maths.createModelMatrix(position, rotation, scale);
		Matrix4f modelViewProjectionMatrix = new Matrix4f();
		projectionMatrix.mul(camera.getViewMatrix(), modelViewProjectionMatrix);//MVP = P * V
		modelViewProjectionMatrix.mul(modelMatrix);								//MVP = PV * M
//
		shader.loadModelMatrix(modelMatrix);
		shader.loadModelViewProjectionMatrix(modelViewProjectionMatrix);
	}
	
	public void setProjectionMatrix(Matrix4f projectionMatrix)
	{
		this.projectionMatrix = projectionMatrix;
	}
}
