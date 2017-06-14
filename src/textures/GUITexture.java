package textures;

import org.joml.Vector2f;

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
	public GUITexture(Texture texture, Vector2f position, Vector2f scale) 
	{
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
	
	private Texture texture;
	private Vector2f position;
	private Vector2f scale;
	
	public void cleanUp()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		texture.destroy(gl);
	}
	
	public Texture getTexture() 
	{
		return texture;
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
