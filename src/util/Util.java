package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Enables some old functionality (e.g. reading shader files), but also some debugging functions,
 * i.e. to print arrays very easily.
 * @author Lobner
 *
 */
@Deprecated
public class Util 
{
	public static String readShaderFile(String filePath)
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
	
	public static void printQuaternion(float[] q)
	{
		System.out.println("("  + q[0] + ", " + q[1] + ", " + q[2] + ", " + q[3] + ")");
	}
	
	public static void printVector(float[] v)
	{
		System.out.println("(" + v[0] + ", " + v[1] + ", " + v[2] + ")");
	}
	
	public static void printMatrix(float[] m)
	{
		for(int j = 0; j < 4; j++)
		{
			for(int i = 0; i < 4; i++)
			{
				System.out.print(m[j*4 + i]);
				if(i != 3)
				{
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	
	public static void printMatrix(float[] m, String name)
	{
		System.out.println(name + " = ");
		for(int j = 0; j < 4; j++)
		{
			for(int i = 0; i < 4; i++)
			{
				System.out.print(m[j*4 + i]);
				if(i != 3)
				{
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	
	public static void printFloatArray(float[] arr)
	{
		if(arr == null || arr.length == 0)
		{
			System.out.println("Array is empty!");
			return;
		}
		System.out.print("arr = (");
		for(int i = 0; i < arr.length-1; i++)
		{
			System.out.print(arr[i] + ", ");
		}
		System.out.print(arr[arr.length-1] + ")\n");
	}
	
	public static void printFloatArray(float[] arr, String name)
	{
		if(arr == null || arr.length == 0)
		{
			System.out.println(name + " is empty!");
			return;
		}
		System.out.print(name + " = (");
		for(int i = 0; i < arr.length-1; i++)
		{
			System.out.print(arr[i] + ", ");
		}
		System.out.print(arr[arr.length-1] + ")\n");
	}
	
	public static void printIntArray(int[] arr)
	{
		System.out.print("arr = (");
		for(int i = 0; i < arr.length-1; i++)
		{
			System.out.print(arr[i] + ", ");
		}
		System.out.print(arr[arr.length-1] + ")\n");
	}
	
	public static void printIntArray(int[] arr, String name)
	{
		System.out.print("name = (");
		for(int i = 0; i < arr.length-1; i++)
		{
			System.out.print(arr[i] + ", ");
		}
		System.out.print(arr[arr.length-1] + ")\n");
	}

	public static void printFloatArrayList(ArrayList<float[]> list)
	{
		System.out.println("list.size() = " + list.size());
		if(list.size() > 0)
		{
			for(int i = 0; i < list.size(); i++)
			{
				float[] tmp = list.get(i);
				if(tmp.length > 0)
				{
					for(int j = 0; j < tmp.length-1; j++)
					{
						System.out.print(tmp[j] + " ");
					}
					System.out.print(tmp[tmp.length-1] + "\n");
				}
			}
		}
	}
	
	public static void printIntArrayList(ArrayList<int[]> list)
	{
		System.out.println("list.size() = " + list.size());
		if(list.size() > 0)
		{
			for(int i = 0; i < list.size(); i++)
			{
				int[] tmp = list.get(i);
				if(tmp.length > 0)
				{
					for(int j = 0; j < tmp.length-1; j++)
					{
						System.out.print(tmp[j] + " ");
					}
					System.out.print(tmp[tmp.length-1] + "\n");
				}
			}
		}
	}
}
