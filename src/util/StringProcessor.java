package util;

/**
 * This class' functionality is to process read in obj strings in order to create
 * vertex and index buffer objects.
 * @author Lobner
 *
 */
public class StringProcessor 
{
	/**
	 * Split the read in string from the obj file into its parts:
	 * 0 - vertices (v)
	 * 1 - texture coordinates (vt)
	 * 2 - vertex normals (vn)
	 * 3 - face descriptions (f)
	 * leaves the string in the array free, if there is no specified data
	 * @param objString
	 * @return
	 */
	public static String[] splitVertexDataString(String objString)
	{
		String[] objSplitString = {"", "", ""};
		String str[] = objString.split("\n");
		for(int i = 0; i < str.length; i++)
		{
			if(str[i].length() == 0)
			{
				continue;
			}
			String dataType = str[i].substring(0, 2);
			switch(dataType)
			{
			case "v ": objSplitString[0] += str[i] + "\n"; break;
			case "vt": objSplitString[1] += str[i] + "\n"; break;
			case "vn": objSplitString[2] += str[i] + "\n"; break;
//			case "f ": objSplitString[3] += str[i] + "\n"; break;
			default: 
				System.out.println("default problem - dataType is " + dataType);
				/*if something else is read, there is a problem with the data*/ break;
			}
		}
		
		return objSplitString;
	}
	
	/**
	 * Split the given string  into its parts:
	 * 0 - vertex indices
	 * 1 - texture indices
	 * 2 - vertex normal indices
	 * leaves the string in the array free, if there is no specified data
	 * @param objString
	 * @return
	 */
	public static int[][][] splitFaceDataString(String objString)
	{
		
		//the information is stored as f v0/vt0/vn0 v1/vt1/vn1 v2/vt2/vn2 (e.g. f 1/1/1 2/2/2 3/3/3)
		//thus it has to be divided 
		//	-in individual faces (line split "\n")
		//	-in individual data chunks (space split " ")
		//	-in individual data (slash split "/")
		String line[] = objString.split("\n");
		int[][][] indices = new int[line.length][3][3];
		for(int i = 0; i < line.length; i++)
		{
			if(line[i].length() == 0)
			{
				continue;
			}
			while(!(line[i].substring(0, 1).matches("[0-9]+") 
					|| line[i].substring(0, 1).equals("/")))
			{ 	//while the first char is not a slash or a digit: delete the first char
				line[i]  = line[i].substring(1);
			}
			
			String faceDataChunk[] = line[i].split(" ");
			
			for(int j = 0; j < faceDataChunk.length; j++)
			{
				String[] faceData = faceDataChunk[j].split("/");
				for(int k = 0; k < faceData.length; k++)
				{
					indices[i][j][k] = Integer.parseInt(faceData[k]);
				}
			}
			
		}
		
		return indices;
	}
	
	/**
	 * splits read in obj data into a one dimensional string array of 2 elements
	 * index 0 contains the vertex, tex coords and normal data
	 * index 1 contains the face data (unprocessed, e.g "f 1/1/1 2/2/2 3/3/3\n")
	 * @param objData the read in obj file containing only f, v, vt and vn data
	 * @return the split string array
	 */
	public static String[] splitObjString(String objData)
	{
		String[] splittedString = {"", ""};//new String[2];
		String[] objLines = objData.split("\n");
		
		for(int i = 0; i < objLines.length; i++)
		{
			if(objLines[i].length() == 0)
			{
				continue;
			}
			if(objLines[i].substring(0, 1).equals("f"))
			{
				splittedString[1] += objLines[i] + "\n";
			}
			else
			{
				splittedString[0] += objLines[i] +"\n";
			}
		}
		
		return splittedString;
	}
	
	/**
	 * takes the given string and returns a float of the data provided
	 * @param data
	 * @return
	 */
 	public static float[][] stringToFloatArray(String data)
	{
		int secondDimension = 0;
		if(data.length() < 2)
		{
			System.out.println("data = " + data);
			return null;
		}
		switch(data.substring(0, 2))
		{
		case "v ": case "vn":  secondDimension = 3; break;
		case "vt": secondDimension = 2; break;
		default: 
			System.out.print("default case, data.substring = ");
			System.out.println(data.substring(0, 2));
		/*if something else is read, there is a problem with the data*/ break;
		}
		
		String[] splitData = data.split("\n");
		float[][] objData = new float[splitData.length][secondDimension];
		
		for(int i = 0; i < splitData.length; i++)
		{
			while(!(splitData[i].substring(0, 1).matches("[0-9]+") 
					|| splitData[i].substring(0, 1).equals("-")))
			{ 	//while the first char is not a minus or a digit: delete the first char
				splitData[i]  = splitData[i].substring(1);
			}
			//at this point the string contains only the values, seperated by spaces
			String[] tmp = splitData[i].split(" ");
			for(int j = 0; j < secondDimension; j++)
			{
				objData[i][j] = Float.parseFloat(tmp[j]);
			}
		}
		
		return objData;
	}
 	
 	/**
	 * Reads in the specified obj file and returns a two dimensional float array with
	 * first dimension:
	 * 0 - vertices coordinates (v)
	 * 1 - texture coordinates (vt)
	 * 2 - vertex normals (vn)
	 * second dimension:
	 * Index of the data (i.e. the order in which they were read in)
	 * third dimension:
	 * the coordinates:
	 * 0 - x coordinate / u coordinate
	 * 1 - y coordinate / v coordinate
	 * 2 - z coordinate / NOTHING
	 * @param str the data from an obj file as a str (no comments, no empty lines)
	 * @return
	 */
	public static float[][][] stringToFloatTupel(String str)
	{
		//split the given data into vertex coordinates, vertex texture coordinates and vertex normal "coordinates"
		//all of the data inside the strings has to be separated by a "\n"
		String[] dataSplit = splitVertexDataString(str);
//		int maxLength = maxOfStringArray(dataSplit);
		
		float obj[][][] = new float[3][][];
		
		for(int i = 0; i < obj.length; i++)
		{
			obj[i] = stringToFloatArray(dataSplit[i]);
		}
		
		return obj;
	}
}
