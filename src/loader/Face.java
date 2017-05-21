package loader;

/**
 * This class is an auxiliary class to process the obj face data
 * @author Lobner
 *
 */
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
	
	/**
	 * Returns the indices of the first vertex of the face
	 * @return An integer array storing vertex position id in [0], 
	 * 			texture coordinate id in [1] and normal id in [2]
	 */
	public int[] getV1()
	{
		return v1;
	}
	
	/**
	 * Returns the indices of the second vertex of the face
	 * @return An integer array storing vertex position id in [0], 
	 * 			texture coordinate id in [1] and normal id in [2]
	 */
	public int[] getV2()
	{
		return v2;
	}
	
	/**
	 * Returns the indices of the third vertex of the face
	 * @return An integer array storing vertex position id in [0], 
	 * 			texture coordinate id in [1] and normal id in [2]
	 */
	public int[] getV3()
	{
		return v3;
	}
}
