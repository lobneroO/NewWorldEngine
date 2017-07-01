package water;

public class WaterTile 
{
	public static final float TILE_SIZE = 10;
	
	private float x, y, z;
	
	public WaterTile(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getY()
	{
		return y;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getZ()
	{
		return z;
	}
}
