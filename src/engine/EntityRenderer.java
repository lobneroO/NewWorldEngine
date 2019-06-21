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
import shader.BasicTextureLightShader;
import toolbox.Maths;

/**
 * The renderer for TexturedEntity objects
 * @author Lobner
 *
 */
public class EntityRenderer 
{
	BasicTextureLightShader textureShader;
	BasicMaterialLightShader materialShader;
	//tmp
	boolean printed = false;
	//preferences
	//
	private Matrix4f projectionMatrix;
	
	public EntityRenderer(BasicTextureLightShader textureShader, BasicMaterialLightShader materialShader)
	{
		this.textureShader = textureShader;
		this.materialShader = materialShader;
	}

	public void setTextureShader(BasicTextureLightShader shader)
	{
		this.textureShader = shader;
	}

	public void setMaterialShader(BasicMaterialLightShader shader)
	{
		this.materialShader = shader;
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
		GL3 gl = GLContext.getCurrentGL().getGL3();

		for(MaterialModel model : entities.keySet())
		{
			prepareMaterialModel(model);
			List<MaterialEntity> batch = entities.get(model);
			for(MaterialEntity materialEntity : batch)
			{
				prepareMaterialEntity(camera, materialEntity);

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
				prepareTexturedEntity(camera, texturedEntity);
				
				gl.glDrawElements(GL.GL_TRIANGLES, model.getRawModel().getNumVertices(), GL.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	public void prepareMaterialModel(MaterialModel model)
	{
		Material material = model.getMaterial();
//		BasicMaterialLightShader mShader = (BasicMaterialLightShader)shader;
		materialShader.loadDiffuseColor(material.diffuseColor);

		RawModel rawModel = model.getRawModel();
		textureShader.loadMaterialSpecularIntensity(rawModel.getSpecularIntensity());
		textureShader.loadMaterialSpecularPower(rawModel.getSpecularPower());

		prepareModel(model);
	}

	public void prepareTexturedModel(TexturedModel model)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();

		prepareModel(model);
		RawModel rawModel = model.getRawModel();
		textureShader.loadMaterialSpecularIntensity(rawModel.getSpecularIntensity());
		textureShader.loadMaterialSpecularPower(rawModel.getSpecularPower());

		gl.glActiveTexture(GL.GL_TEXTURE0);
		model.bindTexture();
	}

	public void prepareModel(Model model)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();

		RawModel rawModel = model.getRawModel();
//		shader.loadMaterialSpecularIntensity(rawModel.getSpecularIntensity());
//		shader.loadMaterialSpecularPower(rawModel.getSpecularPower());

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

	public MatrixContainer prepareMatrices(Camera camera, Entity entity)
	{
		Matrix4f modelMatrix = Maths.createModelMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		Matrix4f modelViewProjectionMatrix = new Matrix4f();
		projectionMatrix.mul(camera.getViewMatrix(), modelViewProjectionMatrix);//MVP = P * V
		modelViewProjectionMatrix.mul(modelMatrix);								//MVP = PV * M

		return new MatrixContainer(modelMatrix, modelViewProjectionMatrix);
	}

	public void prepareTexturedEntity(Camera camera, TexturedEntity entity)
	{
		MatrixContainer mc = prepareMatrices(camera, entity);

		textureShader.loadModelMatrix(mc.modelMatrix);
		textureShader.loadModelViewProjectionMatrix(mc.modelViewProjectionMatrix);
	}

	public void prepareMaterialEntity(Camera camera, MaterialEntity entity)
	{
		MatrixContainer mc = prepareMatrices(camera, entity);

		materialShader.loadModelMatrix(mc.modelMatrix);
		materialShader.loadModelViewProjectionMatrix(mc.modelViewProjectionMatrix);
	}
	
	public void setProjectionMatrix(Matrix4f projectionMatrix)
	{
		this.projectionMatrix = projectionMatrix;
	}
}

//convenience "struct" to return two values from a single method
class MatrixContainer
{
	public Matrix4f modelMatrix;
	public Matrix4f modelViewProjectionMatrix;

	public MatrixContainer(Matrix4f modelMatrix, Matrix4f modelViewProjectionMatrix)
	{
		this.modelMatrix = modelMatrix;
		this.modelViewProjectionMatrix = modelViewProjectionMatrix;
	}
}