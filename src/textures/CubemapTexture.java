package textures;

import java.io.File;
import java.io.IOException;

import toolbox.TextureUtils;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * Creates a texture to be used for OpenGL cube maps.
 * JOGL does not automatically support it, thus this class is needed where a Texture class is not
 * @author Lobner
 *
 */
public class CubemapTexture 
{
	private String[] m_fileNames;
	String fileSuffix = ".jpg";
	private int[] m_textureObj;
	private int[] types = {
			GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
			GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
			GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
			GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
			GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
			GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
	};
	private Texture cubemapTexture;
	
	public CubemapTexture(String directoryPath, 
			String posXFilename, String negXFilename,
			String posYFilename, String negYFilename,
			String posZFilename, String negZFilename)
	{
		m_fileNames = new String[6];
		m_fileNames[0] = directoryPath + posXFilename;
		m_fileNames[1] = directoryPath + negXFilename;
		m_fileNames[2] = directoryPath + posYFilename;
		m_fileNames[3] = directoryPath + negYFilename;
		m_fileNames[4] = directoryPath + posZFilename;
		m_fileNames[5] = directoryPath + negZFilename;
		
		m_textureObj = new int[1];
	}
	
	public void bind(GL3 gl, int textureUnit)
	{
		gl.glActiveTexture(textureUnit);	
		cubemapTexture.bind(gl);
	}
	
	/**
	 * Loads in the standard cubemap for the skybox located at "textures/skybox/" with the 
	 * filenames "back.jpg", "bottom.jpg", "front.jpg", "left.jpg", "right.jpg" and "top.jpg".
	 * @param gl
	 * @return Returns whether the cubemap was correctly loaded
	 */
	public boolean load(GL3 gl)
	{
		boolean[] flipped = new boolean[6];
		for(int i = 0; i < 6; i++)
		{
			flipped[i] = false;
		}
		
		load(gl, flipped);
		
		return true;
	}
	
	/**
	 * Loads in the standard cubemap for the skybox located at "textures/skybox/" with the 
	 * filenames "back.jpg", "bottom.jpg", "front.jpg", "left.jpg", "right.jpg" and "top.jpg".
	 * Individual files can be flipped. Which ones are specified in the flipped array.
	 * The indices are as follows:
	 * Positive x (right), negative x (left), 
	 * positive y (top), negative y (bottom), 
	 * positive z (back), negative z (front)
	 * @param gl
	 * @param flipped
	 * @return
	 */
	public boolean load(GL3 gl, boolean[] flipped)
	{
		cubemapTexture = TextureIO.newTexture(GL.GL_TEXTURE_CUBE_MAP);

		try
		{
			for(int i = 0; i < types.length; i++)
			{
				File texFile = new File(m_fileNames[i]);
				TextureData data = TextureIO.newTextureData(GLContext.getCurrentGL().getGLProfile(), 
						texFile, false, fileSuffix);
				
				if(data == null)
				{
					System.err.println("Could not create cubemap!");
					System.err.println("Could not use " + m_fileNames[i]);
					return false;
				}
				
				if(flipped[i])
				{
					TextureData flippedData = TextureUtils.flipTextureData(data);
					cubemapTexture.updateImage(gl, flippedData, types[i]);
				}
				else
				{
					cubemapTexture.updateImage(gl, data, types[i]);
				}
				
			}
		} catch(IOException e)
		{
			System.err.println(e);
			return false;
		}
		
		return true;
	}
	
	public void destroy(GL3 gl)
	{
		gl.glDeleteTextures(1, m_textureObj, 0);
		if(cubemapTexture != null)
		{
			cubemapTexture.destroy(gl);
		}
	}

	public void setFileSuffix(String suffix)
	{
		fileSuffix = suffix;
	}
}
