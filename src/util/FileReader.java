package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReader 
{
	/**
	 * Reads in the specified obj file and returns it as a string
	 * already excludes all the comments
	 * returns an empty string, if the file is not an obj file or is not found
	 * @param filePath
	 * @return
	 */
	public static String OBJFileToString(String filePath)
	{
		if(!filePath.endsWith(".obj"))
		{
			System.err.println("The file " + filePath + " is not a valid obj file!");
			return "";
		}
		String str = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			
			String  line = null;
			while((line = br.readLine()) != null) 
			{
				if (line.startsWith("#")) 
				{ 
                    //comments are irrelevant
                } 
				else if (line.equals("")) 
                {
                    //ignore whitespace data ...
                }
				else if(!(line.startsWith("v") || line.startsWith("f")))
				{
					System.err.println("The file " + filePath + " is not a valid obj file!");
					br.close();
					return "";
				}
				else
				{
					str += line;
					str += "\n";
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return str;
	}
}
