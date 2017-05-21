package loader;

public class Face 
{
	private int[] v1, v2, v3;	//each one represents one triangle vertex
						//i.e. one vertex pos id, one tex coord id, one normal id
	public Face(int[] v1, int[] v2, int[] v3)
	{
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}
	
	public int[] getV1()
	{
		return v1;
	}
	
	public int[] getV2()
	{
		return v2;
	}
	
	public int[] getV3()
	{
		return v3;
	}
}
