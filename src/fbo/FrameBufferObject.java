package fbo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

public class FramebufferObject 
{
	private int[] fbo;
	
	protected int width;
	protected int height;
	
	protected int displayWidth = 0;
	protected int displayHeight = 0;
	
	private List<int[]> textureAttachments;
	private int[] depthTextureAttachment;
	private int[] depthBufferAttachment;
	
	public FramebufferObject(int width, int height)
	{
		createFrameBufferObject();
		this.width = width;
		this.height = height;
		
		init();
	}
	
	protected FramebufferObject()
	{
		init();
	}
	
	/**
	 * Function for initializations shared by all constructors
	 */
	private void init()
	{
		textureAttachments = new ArrayList<int[]>();
	}
	
	protected int[] createFrameBufferObject()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		int[] fbo = new int[1];
		
		//generate the frame buffer object and store it
		gl.glGenFramebuffers(1, fbo, 0);
		
		//bin the new fbo and attach a color attachment
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo[0]);
		gl.glDrawBuffer(GL.GL_COLOR_ATTACHMENT0);
		
		return fbo;
	}
	
	public void bind(int[] fbo, int width, int height)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo[0]);
		gl.glViewport(0, 0, width, height);
	}
	
	public void bind()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		//first make sure no texture is bound
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		//now bind the frame buffer
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo[0]);
		gl.glViewport(0, 0, width, height);
	}
	
	public void unbind()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		if(displayWidth != 0 && displayHeight != 0)
		{
			gl.glViewport(0, 0, displayWidth, displayHeight);
		}
	}
	
	public int[] createTextureAttachment(int width, int height)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		int[] textureID = new int[1];
		gl.glGenTextures(1,  textureID, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureID[0]);
		
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, width, height, 0, GL.GL_RGB,
				GL.GL_UNSIGNED_BYTE, (ByteBuffer)null);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		//TODO: probably needs to dynamically use the GL_COLOR_ATTACHMENT number
		gl.glFramebufferTexture(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, textureID[0], 0);
		
//		textureAttachments.add(textureID);
		return textureID;
	}
	
	public int[] createDepthTextureAttachment(int width, int height)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		int[] depthTextureAttachment = new int[1];
		
		gl.glGenTextures(1, depthTextureAttachment, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, depthTextureAttachment[0]);
		
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_DEPTH_COMPONENT32, width, height, 0, 
				GL3.GL_DEPTH_COMPONENT, GL.GL_FLOAT, (ByteBuffer)null);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		
		gl.glFramebufferTexture(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, 
				depthTextureAttachment[0], 0);
		
		return depthTextureAttachment;
	}
	
	public int[] createDepthBufferAttachment(int width, int height)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		int[] depthBufferAttachment = new int[1];
		
		gl.glGenRenderbuffers(1, depthBufferAttachment, 0);
		gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, depthBufferAttachment[0]);
		gl.glRenderbufferStorage(GL3.GL_RENDERBUFFER, GL3.GL_DEPTH_COMPONENT, width, height);
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL3.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER,
				depthBufferAttachment[0]);
		
		return depthBufferAttachment;
	}
	
	public int[] getTextureAttachmentID(int i)
	{
		return textureAttachments.get(i);
	}
	
	public void setFramebufferViewPort(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public void setDisplayViewport(int width, int height)
	{
		displayWidth = width;
		displayHeight = height;
	}
	
	public void cleanUp()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		if(fbo != null)
		{
			gl.glDeleteFramebuffers(1, fbo, 0);
		}
		for(int[] id : textureAttachments)
		{
			gl.glDeleteTextures(1, id, 0);
		}
		if(depthTextureAttachment != null)
		{
			gl.glDeleteTextures(1, depthTextureAttachment, 0);
		}
		if(depthBufferAttachment != null)
		{
			gl.glDeleteBuffers(1, depthBufferAttachment, 0);
		}
	}
}
