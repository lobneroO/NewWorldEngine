package loader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import com.jogamp.common.nio.Buffers;

import entities.RawModel;

/**
 * ModelLoader must be instantiated to enable keeping track of loaded models and being able to
 * clean everything up. It doesn't load the data itself, but is given what it needs
 * (thus one can easily create an object, e.g. a terrain within the code).
 * It creates a VAO to store VBOs and the IBO and returns a RawModel object
 * (textures will then be loaded by the TexturedModel objects directly, as the JOGL interface
 * for that is pretty neat and doesn't really need any work additionally than just calling a load
 * and clean up function).
 * @author Lobner
 *
 */
public class ModelLoader 
{
	public final int SIZEOFFLOAT = 4;	//4 bytes for a float
	public final int SIZEOFINT = 4;		//4 bytes for an int
	
	/*
	 * VAOs and VBOs can in theory be stored in the same list, but this makes the code
	 * easier to read and understand
	 */
	private List<int[]> vaoList = new ArrayList<int[]>();
	private List<int[]> vboList = new ArrayList<int[]>();
	private List<int[]> iboList = new ArrayList<int[]>();
	
	public ModelLoader()
	{
		
	}
	
	/**
	 * Takes in information for an object and creates a RawModel object
	 * @param positions	vertex positions with 3 values per position
	 * @param texCoords texture coordinates with 2 values per texture coordinate
	 * @param normals normals with 3 values per normal
	 * @param indices indices for the index buffer with three vertices per face (i.e. triangle)
	 * @return RawModel object created from the data that was input
	 */
	public RawModel loadToVAO(float[] positions, float[] texCoords, float[] normals, int[] indices)
	{
		int[] VAO = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, positions, 3);
		storeDataInAttributeList(1, texCoords, 2);
		storeDataInAttributeList(2, normals, 3);
		unbindVAO();
		
		return new RawModel(VAO.clone(), indices.length);
	}
	
	private int[] createVAO()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		int[] VAO = new int[1];
		gl.glGenVertexArrays(1, VAO, 0);
		gl.glBindVertexArray(VAO[0]);
		
		vaoList.add(VAO);
		
		return VAO;
	}
	
	private int[] storeDataInAttributeList(int attributeNumber, float[] data, int vectorSize)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		int[] VBO = new int[1];
		gl.glGenBuffers(1, VBO, 0);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO[0]);
		
		vboList.add(VBO);
		
		FloatBuffer fb = storeDataInFloatBuffer(data);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, SIZEOFFLOAT*data.length, fb, GL.GL_STATIC_DRAW);
		gl.glVertexAttribPointer(attributeNumber, vectorSize, GL.GL_FLOAT, false, 0, 0);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
		
		return VBO.clone();
	}
	
	private void unbindVAO()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		int[] IBO = new int[1];
		gl.glGenBuffers(1, IBO, 0);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, IBO[0]);
		iboList.add(IBO);
		
		IntBuffer ib = storeDataInIntBuffer(indices);
		gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, SIZEOFINT*indices.length, ib, GL.GL_STATIC_DRAW);
	} 
	
	private IntBuffer storeDataInIntBuffer(int[] data)
	{
		IntBuffer ib = Buffers.newDirectIntBuffer(data);
		return ib;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data)
	{
		FloatBuffer fb = Buffers.newDirectFloatBuffer(data);
		return fb;
	}
	
	public void cleanUp()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		for(int[] vao:vaoList)
		{
			gl.glDeleteVertexArrays(1, vao, 0);
		}
		
		for(int[] vbo:vboList)
		{
			gl.glDeleteBuffers(1, vbo, 0);
		}
		
		for(int[] ibo:iboList)
		{
			gl.glDeleteBuffers(1, ibo, 0);
		}
	}
}
