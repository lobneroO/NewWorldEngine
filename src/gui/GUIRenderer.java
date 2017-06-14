package gui;

import java.util.List;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import textures.GUITexture;
import toolbox.StandardModels;
import loader.ModelLoader;
import loader.ShaderLoader;
import entities.RawModel;

public class GUIRenderer
{
	public final RawModel quad;
	
	private GUIShader shader;
	
	public GUIRenderer(ModelLoader modelLoader, ShaderLoader shaderLoader)
	{
		quad = modelLoader.loadToVAO(StandardModels.get2DQuadTriangleStripVertices(), 2);
		shader = new GUIShader();
		shaderLoader.loadShader(shader);
	}
	
	public void render(List<GUITexture> guiTextures)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		shader.start();
		
		gl.glBindVertexArray(quad.getVAO()[0]);
		gl.glEnableVertexAttribArray(0);
		
		for(GUITexture guiTex : guiTextures)
		{
			Matrix4f modelMatrix = new Matrix4f();
			modelMatrix.identity();
			modelMatrix.translate(guiTex.getPosition().x, guiTex.getPosition().y, 0.0f);
			modelMatrix.scale(guiTex.getScale().x, guiTex.getScale().y, 0.0f);
			shader.loadModelMatrix(modelMatrix);
			
			gl.glActiveTexture(GL.GL_TEXTURE0);
			guiTex.bind(gl);
			gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, quad.getNumVertices());
		}
		
		gl.glDisableVertexAttribArray(0);
		gl.glBindVertexArray(0);
		
		shader.stop();
	}
	
	public void cleanUp()
	{
		shader.cleanUp();
	}
}
