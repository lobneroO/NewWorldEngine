package loader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import shader.ShaderProgram;

public class ShaderLoader 
{
	//keep track of loaded shaders and clean up the tracked ones
	private List<ShaderProgram> shaderList = new ArrayList<ShaderProgram>();
	
	public ShaderLoader()
	{
		
	}
	
	public String readShaderFile(String filePath)
	{
		String shader = "";
		
		BufferedReader br;
		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			
			String  line = null;
	        while((line = br.readLine()) != null) 
	        {
	        	shader += line;
	        	shader += "\n";
	        }
		} catch (FileNotFoundException e) {
			System.err.println("Shader " + filePath + " not found!");
			return "";
		} catch (IOException e) {
			System.err.println("IOException while reading file " + filePath + "!");
			return shader;
		}
		
		try 
		{
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return shader;
	}
	
	public void cleanUp()
	{
		for(ShaderProgram shader : shaderList)
		{
			shader.cleanUp();
		}
	}
}
