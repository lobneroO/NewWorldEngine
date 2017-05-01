package textures;

import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLContext;

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
		gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, m_textureObj[0]);
	}
	
	public boolean load(GL3 gl)
	{
		cubemapTexture = TextureIO.newTexture(GL.GL_TEXTURE_CUBE_MAP);

		try
		{
			for(int i = 0; i < types.length; i++)
			{
				File texFile = new File(m_fileNames[i]);
				TextureData data = TextureIO.newTextureData(GLContext.getCurrentGL().getGLProfile(), texFile, 
						GL.GL_RGB, GL.GL_RGBA, false, fileSuffix);
				
				if(data == null)
				{
					System.err.println("Could not create cubemap!");
					System.err.println("Could not use " + m_fileNames[i]);
					return false;
				}
				
				cubemapTexture.updateImage(gl, data, types[i]);
			}
		} catch(IOException e)
		{
			System.err.println(e);
			return false;
		}
		
		return true;
	}
	
	public boolean loadWithJOGL(GL3 gl)
	{
		gl.glGenTextures(1, m_textureObj, 0);
		gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, m_textureObj[0]);
		
		try
		{
			for(int i = 0; i < types.length; i++)
			{
				File texFile = new File(m_fileNames[i]);
				gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
				TextureData data = TextureIO.newTextureData(GLContext.getCurrentGL().getGLProfile(), texFile, 
						GL.GL_RGB, GL.GL_RGBA, false, fileSuffix);
				
//				gl.glTexImage2D(int target, int level, int internalformat, 
//						int width, int height, int border, 
//						int format, int type, Buffer pixels);
				gl.glTexImage2D(types[i], 0, data.getInternalFormat(), data.getWidth(), data.getHeight(), 0, 
						data.getPixelFormat(), GL.GL_UNSIGNED_BYTE, data.getBuffer());
				gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
				gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
				gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
				gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
				gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_WRAP_R, GL.GL_CLAMP_TO_EDGE);
				
				//texFile doesn't need to be closed, since java.io.File represents
				//only a file path, but the file is never technically opened
				//however, the texture data can be destroyed once OpenGL has stored it in a texture
				data.destroy();
			}
		}catch(IOException e)
		{
			System.err.println(e);
			return false;
		}
		
		return true;
	}
	
	public void destroy(GL3 gl)
	{
		gl.glDeleteTextures(1, m_textureObj, 0);
	}

	public void setFileSuffix(String suffix)
	{
		fileSuffix = suffix;
	}
}
