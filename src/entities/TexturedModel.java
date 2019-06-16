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
public class TexturedModel extends Model
{
	Texture texture;

	public TexturedModel(RawModel model)
	{
		super(model);
	}
	
	public TexturedModel(RawModel model, String texture, boolean mipmap)
	{
		super(model);
		loadTexture(texture, mipmap);
	}
	
	public TexturedModel(RawModel model, Texture texture)
	{
		super(model);
		this.texture = texture;
	}
	

	
	public void bindTexture()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glActiveTexture(GL.GL_TEXTURE0);
		texture.bind(gl);
	}
	
	public Texture getTexture()
	{
		return texture;
	}
	
	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}

	@Override
	public void setHasTransparency(boolean hasTransparency)
	{
		this.hasTransparency = hasTransparency;
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
