package loader;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * An auxiliary class to process an obj file
 * @author Lobner
 *
 */
public class VTNSet 
{
	//the lists contain the actual vertices(/tex coords/normals), not the indices
	List<Vector3f> vList = new ArrayList<Vector3f>();
	List<Vector2f> tList = new ArrayList<Vector2f>();
	List<Vector3f> nList = new ArrayList<Vector3f>();
	
	/**
	 * Returns the id of the new vertex data. If it was not present before, it is added at the
	 * end of the list
	 * @param v	Vertex position (v data in obj file)
	 * @param t Texture coordinate (vt data in obj file)
	 * @param n Normal data (vn data in obj file)
	 * @return ID of the new vertex data set in the to-be-vertex-buffer-object
	 */
	public int getID(Vector3f v, Vector2f t, Vector3f n)
	{
		int id = 0;
		boolean isInList = false;
		//the lists store the positions the way they will be stored in the buffer,
		//thus they all have the same length at any given time
		for(int i = 0; i < vList.size(); i++)
		{
			Vector3f currentV = vList.get(i);
			Vector2f currentT = tList.get(i);
			Vector3f currentN = nList.get(i);
			if(currentV.equals(v) && currentT.equals(t) && currentN.equals(n))
			{
				isInList = true;
				id = i;
				break;
			}
		}
		
		if(isInList)
		{
			//no need to add to set, give out index
			return id;
		}
		else
		{
			vList.add(v);
			tList.add(t);
			nList.add(n);
			//the new set is at the last position in the lists, thus the index is the size-1 (indices starting at 0)
			return vList.size()-1;
		}
	}
	
	/**
	 * Returns an array with all vertices as they are to be stored in the VBO
	 * @return float array that contains all the positions of the vertices for a VBO
	 */
	public float[] getVertexPositionsArray()
	{
		return convertVector3fListToFloatArray(vList);
	}
	
	/**
	 * Returns an array with all texture coordinates as they are to be stored in the VBO
	 * @return float array that contains all the texcoords of the vertices for a VBO
	 */
	public float[] getTexCoordsArray()
	{
		return convertVector2fListToFloatArray(tList);
	}
	
	/**
	 * Returns an array with all normals as they are to be stored in the VBO
	 * @return float array that contains all the normals of the vertices for a VBO
	 */
	public float[] getNormalsArray()
	{
		return convertVector3fListToFloatArray(nList);
	}
	
	private float[] convertVector3fListToFloatArray(List<Vector3f> list)
	{
		float[] arr = new float[list.size()*3];
		
		for(int i = 0; i < list.size(); i++)
		{
			Vector3f current = list.get(i);
			arr[i*3] = current.x;
			arr[i*3+1] = current.y;
			arr[i*3+2] = current.z;
		}
		
		return arr;
	}
	
	private float[] convertVector2fListToFloatArray(List<Vector2f> list)
	{
		float[] arr = new float[list.size()*2];
		
		for(int i = 0; i < list.size(); i++)
		{
			Vector2f current = list.get(i);
			arr[i*2] = current.x;
			arr[i*2+1] = current.y;
		}
		
		return arr;
	}
}
