package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import entities.RawModel;

/**
 * OBJLoader can load obj files, but it does not yet support any possible variation
 * Currently, it is not possible to use vertices with more than one texCoord or Normal variation.
 * Also it does not yet load a mtl file
 * @author Lobner
 *
 */
public class OBJLoader 
{
	public static RawModel loadObjModel(String filePath, ModelLoader loader)
	{
		FileReader fr = null;
		try {
			fr = new FileReader(new File("models/" + filePath + ".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Could not load model " + filePath + ".obj!");
			e.printStackTrace();
			return null;	//maybe put a dummy file here
		}
		
		BufferedReader br = new BufferedReader(fr);
		
		String line;
		//lists to process the data
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texCoords = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Face> faces = new ArrayList<Face>();
		List<Integer> indices = new ArrayList<Integer>();
		VTNSet dataSet = new VTNSet();
		//float arrays to actually store the data (the ModelLoader needs this)
		float[] verticesArray;
		float[] texCoordsArray = null;
		float[] normalsArray = null;
		int[] indicesArray;
		
		try{
			while((line = br.readLine()) != null)
			{
				//there seem to be models that don't have v, vt, vn and f data ordered
				//to have the individual data types together (i.e. there can be
				//200 vertices, then 200 normals, then 200 texcoords, 
				//then a  face and then more vertices
				//therefore, the file has to be scanned for all vertex and face data first
				//where the face data is stored in a list an processed later on
				
				//replace (multiple) tabs, new lines or spaces with a single space
				line = line.replaceAll("\\s+", " ");	
				String[] currentLine = line.split(" ");
				if(line.startsWith("v "))
				{
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), 
							Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}
				else if(line.startsWith("vt "))
				{
					Vector2f texCoord = new Vector2f(Float.parseFloat(currentLine[1]), 
							Float.parseFloat(currentLine[2]));
					texCoords.add(texCoord);
				}
				else if(line.startsWith("vn "))
				{
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), 
							Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}
				else if(line.startsWith("f "))
				{
					//currentLine[0] is the f
					//therefore, face data starts [1]
					//problem is, if the object is made out of quads rather than triangles
					if(currentLine.length == 4)
					{ 	//"f " + v1 + v2 + v3 -> triangle
						String[] v1 = currentLine[1].split("/");
						String[] v2 = currentLine[2].split("/");
						String[] v3 = currentLine[3].split("/");
						
						Face face = processFaceData(v1, v2, v3);
						
						faces.add(face);
					}
					else
					if(currentLine.length == 5)
					{ 	//"f " + v1 + v2 + v3 + v4 -> quad
						String[] v1 = currentLine[1].split("/");
						String[] v2 = currentLine[2].split("/");
						String[] v3 = currentLine[3].split("/");
						String[] v4 = currentLine[4].split("/");
						
						//going counter clockwise this will always produce two
						//triangle faces t1,t2 from one quad face q1 such that t1+t2=q1
						Face face1 = processFaceData(v1, v2, v3);
						Face face2 = processFaceData(v3, v4, v1);
						
						faces.add(face1);
						faces.add(face2);
					}
				}
			}
			br.close();
			//at this point everything is read in, the faces can now be processed
			
			//the face combinations of v/t/n determine how to setup the vertex,
			//texcoord and normal arrays. 1/1/1 and 1/2/1 and 1/1/2 have to be stored individually
			//thus these three vertices would lead to three different storages of vertex position 1
			//and three indices accordingly
			
			for(Face face : faces)
			{
				addFace(face, dataSet, indices, vertices, texCoords, normals);
			}
		}
		catch(Exception e){
			System.err.println("There was a problem with the file " + filePath + "!");
			e.printStackTrace();
		}

		verticesArray = dataSet.getVertexPositionsArray();
		texCoordsArray = dataSet.getTexCoordsArray();
		normalsArray = dataSet.getNormalsArray();
		indicesArray = new int[indices.size()];
		
		for(int i = 0; i < indices.size(); i++)
		{
			indicesArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(verticesArray, texCoordsArray, normalsArray, indicesArray);
	}
	
	private static void addFace(Face face, VTNSet dataSet, List<Integer> indices,
			List<Vector3f> vertices, List<Vector2f> texCoords, List<Vector3f> normals)
	{
		//takes the individual vertex data from the current face
		//and looks up, whether this particular combination is stored already
		//if so, the index it is stored at is read out and added to the indices array
		//otherwise the combination is added and the new index is stored in the indices array
		indices.add(lookUpIdOrAdd(face.getV1(), dataSet, vertices, texCoords, normals));
		indices.add(lookUpIdOrAdd(face.getV2(), dataSet, vertices, texCoords, normals));
		indices.add(lookUpIdOrAdd(face.getV3(), dataSet, vertices, texCoords, normals));
	}
	
	private static int lookUpIdOrAdd(int[] IDs, VTNSet dataSet,
			List<Vector3f> vertices, List<Vector2f> texCoords, List<Vector3f> normals)
	{ 	//the IDs array uses IDs of the verticesList/texCoordsList/normalsList,
		//which have unique IDs of the individual lists
		//the VTNSet stores its own IDs
		//which are different due to different v/vt/vn combinations
		Vector3f v = vertices.get(IDs[0]);
		Vector2f t = texCoords.get(IDs[1]);
		Vector3f n = normals.get(IDs[2]);
		
		return dataSet.getID(v, t, n);
	}
	
	private static Face processFaceData(String[] strV1, String[] strV2, String[] strV3)
	{
		int[] v1 = {Integer.parseInt(strV1[0]) - 1,
				Integer.parseInt(strV1[1]) - 1,
				Integer.parseInt(strV1[2]) - 1};
		
		int[] v2 = {Integer.parseInt(strV2[0]) - 1,
				Integer.parseInt(strV2[1]) - 1,
				Integer.parseInt(strV2[2]) - 1};
		
		int[] v3 = {Integer.parseInt(strV3[0]) - 1,
				Integer.parseInt(strV3[1]) - 1,
				Integer.parseInt(strV3[2]) - 1};
		
		return new Face(v1, v2, v3);
	}
}
