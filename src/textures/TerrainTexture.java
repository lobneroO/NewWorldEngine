package textures;

import com.jogamp.opengl.util.texture.Texture;

public class TerrainTexture 
{
	private Texture texture;
	private int textureID;
	
	public TerrainTexture(Texture texture) 
	{
		this.texture = texture;
	}

	public Texture getTexture() 
	{
		return texture;
	}
	
	public int getTextureID() 
	{
		return textureID;
	}
}
