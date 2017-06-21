package textures;

import org.joml.Vector2f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Textures specifically meant for rendering GUIs on a quad on screen
 * (i.e. on top of the rendered scene)
 * @author Lobner
 *
 */
public class GUITexture 
{
	boolean joglTexture = false;
	public GUITexture(Texture texture, Vector2f position, Vector2f scale) 
	{
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		
		joglTexture = true;
	}
	
	public GUITexture(int[] textureID, Vector2f position, Vector2f scale) 
	{
		this.textureID = textureID;
		this.position = position;
		this.scale = scale;
	}
	
	private Texture texture;
	private int[] textureID;
	private Vector2f position;
	private Vector2f scale;
	
	public void cleanUp()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		if(joglTexture)
		{
			texture.destroy(gl);
		}
		else
		{
			gl.glDeleteTextures(1, textureID, 0);
		}
	}
	
	public void bind(GL3 gl)
	{
		if(joglTexture)
		{
			texture.bind(gl);
		}
		else
		{
			gl.glActiveTexture(GL.GL_TEXTURE0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, textureID[0]);
		}
	}
	
	public boolean isJoglTexture()
	{
		return joglTexture;
	}
	
	public Texture getJoglTexture() 
	{
		return texture;
	}
	
	public int[] getTextureID()
	{
		return textureID;
	}
	
	public void setTexture(Texture texture) 
	{
		this.texture = texture;
	}
	
	public Vector2f getPosition() 
	{
		return position;
	}
	
	public void setPosition(Vector2f position) 
	{
		this.position = position;
	}
	
	public Vector2f getScale() 
	{
		return scale;
	}
	
	public void setScale(Vector2f scale) 
	{
		this.scale = scale;
	}
}
