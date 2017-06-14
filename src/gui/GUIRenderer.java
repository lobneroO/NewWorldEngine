package gui;

import java.util.List;

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
		quad = modelLoader.loadToVAO(StandardModels.get2DQuadTriangleStripVertices(), 
				StandardModels.get2DQuadTriangleStripTexCoords(), 2);
		shader = new GUIShader();
		shaderLoader.loadShader(shader);
	}
	
	public void render(List<GUITexture> guiTextures)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		//disable depth testint to render multiple guis at the same positions
		gl.glDisable(GL.GL_DEPTH_TEST);	
		
		//enable blending for transparency in the gui textures
		gl.glEnable(GL.GL_BLEND);	
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		
		shader.start();
		
		gl.glBindVertexArray(quad.getVAO()[0]);
		gl.glEnableVertexAttribArray(0);
		gl.glEnableVertexAttribArray(1);
		
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
		gl.glDisableVertexAttribArray(1);
		gl.glBindVertexArray(0);
		
		gl.glDisable(GL.GL_BLEND);
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		shader.stop();
	}
	
	public void cleanUp()
	{
		shader.cleanUp();
	}
}
