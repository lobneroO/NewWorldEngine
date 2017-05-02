package entities;

/**
 * stores object data in a VAO and keeps track of the number of vertices of the model.
 * @author Lobner
 *
 */
public class RawModel 
{
	//model structure properties
	private int[] VAO;
	private int numVertices;
	//shader properties
	private float specularIntensity = 0;
	private float specularPower = 0; 
	
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

	public float getSpecularIntensity() {
		return specularIntensity;
	}

	public void setSpecularIntensity(float specularIntensity) {
		this.specularIntensity = specularIntensity;
	}

	public float getSpecularPower() {
		return specularPower;
	}

	public void setSpecularPower(float specularPower) {
		this.specularPower = specularPower;
	}
}
