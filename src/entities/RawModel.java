package entities;

/**
 * stores object data in a VAO and keeps track of the number of vertices of the model.
 * @author Lobner
 *
 */
public class RawModel 
{
	private int[] VAO;
	private int numVertices;
	
	public RawModel()
	{
		VAO = new int[1];
		numVertices = 0;
	}
	
	public RawModel(int[] VAO, int numVertices)
	{
		this.VAO = VAO;
		this.numVertices = numVertices;
	}
	
	public void setVAO(int[] VAO)
	{
		this.VAO = VAO;
	}
	
	public void setNumVertices(int numVertices)
	{
		this.numVertices = numVertices;
	}
	
	public int[] getVAO()
	{
		return VAO.clone();
	}
	
	public int getNumVertices()
	{
		return numVertices;
	}
}
