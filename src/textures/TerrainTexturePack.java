package textures;

/**
 * Contains several Textures that can be used by a Terrain model.
 * The Terrain model reads a blend map that determines which Texture to use 
 * for the fragment it currently processes in the fragment shader.
 * The blend map itself is not part of the TerrainTexturePack
 * @author Lobner
 *
 */
public class TerrainTexturePack 
{
	public TerrainTexturePack(TerrainTexture blackTexture,
			TerrainTexture redTexture, TerrainTexture greenTexture,
			TerrainTexture blueTexture) 
	{
		this.blackTexture = blackTexture;
		this.redTexture = redTexture;
		this.greenTexture = greenTexture;
		this.blueTexture = blueTexture;
	}
	
	private TerrainTexture blackTexture;	//corresponds to the black color of the blend map
	private TerrainTexture redTexture;		//corresponds to the red color channel of the blend map
	private TerrainTexture greenTexture;
	private TerrainTexture blueTexture;
	
	/**
	 * Returns the Texture that corresponds to the black color of the blend map
	 * @return
	 */
	public TerrainTexture getBlackTexture() 
	{
		return blackTexture;
	}
	
	/**
	 * Returns the Texture that corresponds to the red color channel of the blend map
	 * @return
	 */
	public TerrainTexture getRedTexture() 
	{
		return redTexture;
	}
	
	/**
	 * Returns the Texture that corresponds to the green color channel of the blend map
	 * @return
	 */
	public TerrainTexture getGreenTexture() 
	{
		return greenTexture;
	}
	
	/**
	 * Returns the Texture that corresponds to the blue color channel of the blend map
	 * @return
	 */
	public TerrainTexture getBlueTexture() 
	{
		return blueTexture;
	}
	
	
}
