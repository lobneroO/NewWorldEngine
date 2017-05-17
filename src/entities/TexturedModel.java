package entities;

import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLException;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import entities.RawModel;

/**
 * TexturedModel encapsulates the RawModel class plus a texture object.
 * CleanUp should be called before exiting the program, which will take care of the texture.
 * @author Lobner
 *
 */
public class TexturedModel 
{
	RawModel model;
	Texture texture;
	
	boolean hasTransparencey = false;
	
	public TexturedModel(RawModel model)
	{
		this.model = model;
	}
	
	public TexturedModel(RawModel model, String texture, boolean mipmap)
	{
		this.model = model;
		loadTexture(texture, mipmap);
	}
	
	public TexturedModel(RawModel model, Texture texture)
	{
		this.model = model;
		this.texture = texture;
	}
	
	public RawModel getRawModel()
	{
		return model;
	}
	
	public void bindTexture()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glActiveTexture(GL.GL_TEXTURE0);
		texture.bind(gl);
	}
	
	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}
	
	public void setHasTransparency(boolean hasTransparency)
	{
		this.hasTransparencey = hasTransparency;
	}
	
	public boolean getHasTransparency()
	{
		return hasTransparencey;
	}
	
	public void loadTexture(String filePath, boolean mipmap)
	{
		File file = new File(filePath);
		try {
			texture = TextureIO.newTexture(file, mipmap);
		} catch (GLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cleanUp()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		texture.destroy(gl);
	}
}
