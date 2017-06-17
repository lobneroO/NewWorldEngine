package terrains;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Vector3f;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import textures.TerrainTexture;
import textures.TerrainTexturePack;
import loader.ModelLoader;
import entities.RawModel;

/**
 * Encapsulates a RawModel object and a texture (as this is different from a normal TexturedModel).
 * It creates a flat model consisting of a specified size of patches of quads(/triangles), thus working
 * as a ground for a level.
 * @author Lobner
 *
 */
public class Terrain 
{
	private static final float SIZE = 100;
	private static final int NUM_VERTICES = 128;
	
	private static final float DEFAULT_MAX_HEIGHT = 40f;
	private static final float DEFAULT_MIN_HEIGHT = -40f;
	private static final int DEFAULT_NUM_VERTICES = 128;
	
	private float x, z;
	private RawModel model;
	//support several textures and a blend map via the TerrainTexture class
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	public Terrain(int gridX, int gridZ, ModelLoader loader, TerrainTexturePack texturePack, TerrainTexture blendMap)
	{
		this. texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		
		this.model = generateTerrain(loader);
	}
	
	public RawModel generateTerrainWithHeightMap(ModelLoader loader, String heightMapPath)
	{
		BufferedImage heightMap;
		try {
			heightMap = ImageIO.read(new File(heightMapPath));
			return generateTerrainWithHeightMap(loader, heightMap);
		} catch (IOException e) {
			System.err.println("Could not load height map for terrain generation!");
			e.printStackTrace();
			return null;
		}
	}
	
	public RawModel generateTerrainWithHeightMap(ModelLoader loader, BufferedImage heightMap)
	{
		int numVertices = DEFAULT_NUM_VERTICES;
		if(heightMap != null)
		{
			//let each pixel be one vertex
			numVertices = heightMap.getHeight();
		}
		
		int count = numVertices * numVertices;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(numVertices-1)*(numVertices-1)];
		int vertexPointer = 0;
		for(int i=0;i<numVertices;i++)
		{
			for(int j=0;j<numVertices;j++)
			{
				vertices[vertexPointer*3] = (float)j/((float)numVertices - 1) * SIZE;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = (float)i/((float)numVertices - 1) * SIZE;
				
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				
				textureCoords[vertexPointer*2] = (float)j/((float)numVertices - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)numVertices - 1);
				
				vertexPointer++;
			}
		}
		
		int pointer = 0;
		for(int gz=0;gz<numVertices-1;gz++)
		{
			for(int gx=0;gx<numVertices-1;gx++)
			{
				int topLeft = (gz*numVertices)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*numVertices)+gx;
				int bottomRight = bottomLeft + 1;
				
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	/**
	 * Returns the height at the specified pixel in the height map
	 * @param x
	 * @param y
	 * @return
	 */
	private float getHeightAt(BufferedImage heightMap, int x, int z)
	{
		//check if the specified pixels are correct values
		if(x < 0 || x >= heightMap.getWidth() || z < 0 || z >= heightMap.getHeight())
		{
			return 0;
		}
		
		float height = heightMap.getRGB(x, z);
	}
	
	private RawModel generateTerrain(ModelLoader loader)
	{
		int count = NUM_VERTICES * NUM_VERTICES;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(NUM_VERTICES-1)*(NUM_VERTICES-1)];
		int vertexPointer = 0;
		for(int i=0;i<NUM_VERTICES;i++)
		{
			for(int j=0;j<NUM_VERTICES;j++)
			{
				vertices[vertexPointer*3] = (float)j/((float)NUM_VERTICES - 1) * SIZE;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = (float)i/((float)NUM_VERTICES - 1) * SIZE;
				
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				
				textureCoords[vertexPointer*2] = (float)j/((float)NUM_VERTICES - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)NUM_VERTICES - 1);
				
				vertexPointer++;
			}
		}
		
		int pointer = 0;
		for(int gz=0;gz<NUM_VERTICES-1;gz++)
		{
			for(int gx=0;gx<NUM_VERTICES-1;gx++)
			{
				int topLeft = (gz*NUM_VERTICES)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*NUM_VERTICES)+gx;
				int bottomRight = bottomLeft + 1;
				
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	public float getX() 
	{
		return x;
	}

	public float getZ() 
	{
		return z;
	}
	
	public float getSize()
	{
		return SIZE;
	}
	
	public Vector3f getPosition()
	{
		return new Vector3f(x, 0, z);
	}

	public RawModel getRawModel() 
	{
		return model;
	}
	
	public TerrainTexturePack getTexturePack() 
	{
		return texturePack;
	}
	
	public TerrainTexture getBlendMap()
	{
		return blendMap;
	}
	
	public void cleanUp()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		texturePack.cleanUp();
		blendMap.getTexture().destroy(gl);
	}
}
