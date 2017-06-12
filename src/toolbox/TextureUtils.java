package toolbox;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.TextureData;

public class TextureUtils 
{
	public static TextureData flipTextureData(TextureData data)
	{
		//the number of bytes per pixel has to be calculated first
		int numBytes = 0;
		switch(data.getPixelFormat())
		{
			case GL.GL_RGB: numBytes = 3; break;
			case GL.GL_BGR: numBytes = 3; break;
			case GL.GL_RGBA: numBytes = 4; break;
			case GL.GL_BGRA: numBytes = 4; break;
			default: System.out.println("default pixel format");break;
		}
		
		switch(data.getPixelType())
		{
			case GL.GL_UNSIGNED_BYTE: break;	//is 1, doesn't need changing
			case GL.GL_UNSIGNED_SHORT: numBytes *= 2; break;	//2 bytes per short
			case GL.GL_UNSIGNED_INT: numBytes *= 4; break;		//4 bytes per int
			case GL.GL_FLOAT: numBytes *= 4; break;
			default: break;
		}
		
		Buffer buffer = data.getBuffer();//assuming no mipmap -> will have be handled
		
		//buffer is created with correct size
		//now the old buffer needs to be read bottom up and put into the new buffer top down 
		//(or vice versa)
		
		//idea: read first into an array, then readout flipped
		byte[] bArray = new byte[buffer.remaining()];
		((ByteBuffer) buffer).get(bArray, 0, bArray.length);
		
		byte[] flippedImageArray = new byte[bArray.length];
		int width = data.getWidth(), height = data.getHeight();
		
		//write into the array for the new image with flipped indices
		for(int i = 0; i < width; i++)
		{	
			int invertedI = width-i-1;
			for(int j = 0; j < height; j++)
			{	
				int invertedJ = height-j-1;
				for(int k = 0; k < numBytes; k++)
				{	//one entry consists of numBytes bytes
					flippedImageArray[i*height*numBytes+j*numBytes+k] = 
							bArray[invertedI*height*numBytes+invertedJ*numBytes+k];
				}
			}
		}
		
		//turn the flipped image array back into a buffer
		ByteBuffer bBuffer = ByteBuffer.wrap(flippedImageArray);	
		TextureData flippedData = new TextureData(GLProfile.get(GLProfile.GL3), 
				data.getInternalFormat(), width, height, data.getBorder(), 
				data.getPixelAttributes(), data.getMipmap(), data.isDataCompressed(), 
				false, bBuffer, null);
		
		data.flush();
		
		return flippedData;
	}
}
