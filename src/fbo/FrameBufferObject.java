package fbo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

public class FrameBufferObject 
{
	private int[] fbo;
	
	private int width;
	private int height;
	
	private List<int[]> textureAttachments;
	private int[] depthTextureAttachment;
	private int[] depthBufferAttachment;
	
	public FrameBufferObject(int width, int height)
	{
		createFrameBufferObject();
		this.width = width;
		this.height = height;
		
		textureAttachments = new ArrayList<int[]>();
	}
	
	private void createFrameBufferObject()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		//generate the frame buffer object and store it
		gl.glGenFramebuffers(1, fbo, 0);
		
		//bin the new fbo and attach a color attachment
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo[0]);
		gl.glDrawBuffer(GL.GL_COLOR_ATTACHMENT0);
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
	}
	
	public void createTextureAttachment(int width, int height)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		int[] textureID = new int[1];
		gl.glGenTextures(1,  textureID, 0);
		
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, width, height, 0, GL.GL_RGB,
				GL.GL_UNSIGNED_BYTE, (ByteBuffer)null);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		//TODO: probably needs to dynamically use the GL_COLOR_ATTACHMENT number
		gl.glFramebufferTexture(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, textureID[0], 0);
		
		textureAttachments.add(textureID);
	}
	
	public void createDepthTextureAttachment(int width, int height)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		gl.glGenTextures(1, depthTextureAttachment, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_DEPTH_COMPONENT32, width, height, 0, 
				GL3.GL_DEPTH_COMPONENT, GL.GL_FLOAT, (ByteBuffer)null);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		
		gl.glFramebufferTexture(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, 
				depthTextureAttachment[0], 0);
	}
	
	public void createDepthBufferAttachment(int width, int height)
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		gl.glGenRenderbuffers(1, depthBufferAttachment, 0);
		gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, depthBufferAttachment[0]);
		gl.glRenderbufferStorage(GL3.GL_RENDERBUFFER, GL3.GL_DEPTH_COMPONENT, width, height);
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL3.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER,
				depthBufferAttachment[0]);
	}
	
	public int[] getTextureAttachmentID(int i)
	{
		return textureAttachments.get(i);
	}
	
	public void setViewPort(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public void cleanUp()
	{
		GL3 gl = GLContext.getCurrentGL().getGL3();
		
		gl.glDeleteFramebuffers(1, fbo, 0);
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
