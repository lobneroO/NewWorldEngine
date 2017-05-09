package terrains;

import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLException;

import loader.ModelLoader;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

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
	private static final float SIZE = 800;
	private static final int NUM_VERTICES = 128;
	
	private float x, z;
	private RawModel model;
	private Texture texture;
	
	public Terrain(int gridX, int gridZ, ModelLoader loader, Texture texture)
	{
		this. texture = texture;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		
		this.model = generateTerrain(loader);
	}
	
	public Terrain(int gridX, int gridZ, ModelLoader loader, String texturePath)
	{
		loadTexture(texturePath, false);
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		
		this.model = generateTerrain(loader);
	}
	public void loadTexture(String filePath, boolean mipmap)
	{
		File file = new File(filePath);
		try {
			texture = TextureIO.newTexture(file, mipmap);
		} catch (GLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public void bindTexture()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glActiveTexture(GL.GL_TEXTURE0);
		texture.bind(gl);
	}
	
	public Texture getTexture() {
		return texture;
	}
}
