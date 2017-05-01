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
		}
		
		BufferedReader br = new BufferedReader(fr);
		String line;
		//lists to process the data
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texCoords = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		//float arrays to actually store the data (the ModelLoader needs this)
		float[] verticesArray;
		float[] texCoordsArray = null;
		float[] normalsArray = null;
		int[] indicesArray;
		
		try{
			while(true)
			{
				line = br.readLine();
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
					break;
				}
			}
			
			texCoordsArray = new float[vertices.size()*2];
			normalsArray = new float[vertices.size()*3];
			
			while(line != null)
			{
				if(!line.startsWith("f "))
				{
					line = br.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				String[] v1 = currentLine[1].split("/");
				String[] v2 = currentLine[2].split("/");
				String[] v3 = currentLine[3].split("/");
				
				processVertex(v1, indices, texCoords, normals, texCoordsArray, normalsArray);
				processVertex(v2, indices, texCoords, normals, texCoordsArray, normalsArray);
				processVertex(v3, indices, texCoords, normals, texCoordsArray, normalsArray);
				line = br.readLine();
			}
			br.close();
		}catch(Exception e){
			System.err.println("There was a problem with the file " + filePath + "!");
			e.printStackTrace();
		}
		
		verticesArray = new float[vertices.size()*3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for(Vector3f vertex: vertices)
		{
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i = 0; i < indices.size(); i++)
		{
			indicesArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(verticesArray, texCoordsArray, indicesArray);
	}
	
	private static void processVertex(String[] data, List<Integer> indices, List<Vector2f> texCoords,
			List<Vector3f> normals, float[] texCoordsArray, float[] normalsArray)
	{
		int currentVertexPointer = Integer.parseInt(data[0]) - 1;
		indices.add(currentVertexPointer);
		
		Vector2f currentTex = texCoords.get(Integer.parseInt(data[1]) - 1);
		texCoordsArray[currentVertexPointer*2] = currentTex.x;
		texCoordsArray[currentVertexPointer*2+1] = currentTex.y;
		
		Vector3f currentNorm = normals.get(Integer.parseInt(data[2]) - 1);
		normalsArray[currentVertexPointer*3] = currentNorm.x;
		normalsArray[currentVertexPointer*3+1] = currentNorm.y;
		normalsArray[currentVertexPointer*3+2] = currentNorm.z;
	}
}
