package water;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import fbo.FramebufferObject;

public class WaterFramebufferObject extends FramebufferObject 
{
	protected static final int REFLECTION_WIDTH = 320;
	protected static final int REFLECTION_HEIGHT = 180;
	
	protected static final int REFRACTION_WIDTH = 1280;
	protected static final int REFRACTION_HEIGHT = 720;
	
	private int[] reflectionFBO;
	private int[] reflectionTexture;
	private int[] reflectionDepthBuffer;
	
	private int[] refractionFBO;
	private int[] refractionTexture;
	private int[] refractionDepthTexture;
	
	public WaterFramebufferObject(int width, int height) 
	{
		super();
		
		this.width = width;
		this.height = height;
		
		initReflectionFBO();
		initRefractionFBO();
	}
	
	private void initReflectionFBO()
	{
		reflectionFBO = createFrameBufferObject();
		reflectionTexture = createTextureAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
		reflectionDepthBuffer = createDepthBufferAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
		unbind();
	}
	
	private void initRefractionFBO()
	{
		refractionFBO = createFrameBufferObject();
		refractionTexture = createTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
		refractionDepthTexture = createDepthTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
		unbind();
	}
	
	public void bindReflectionFBO()
	{
		bind(reflectionFBO, REFLECTION_WIDTH, REFLECTION_HEIGHT);
	}
	
	public void bindRefractionFBO()
	{
		bind(refractionFBO, REFRACTION_WIDTH, REFRACTION_HEIGHT);
	}

	public void cleanUp()
	{
		super.cleanUp();
		
		GL3 gl = GLContext.getCurrentGL().getGL3();
		gl.glDeleteFramebuffers(1, reflectionFBO, 0);
		gl.glDeleteFramebuffers(1, refractionFBO, 0);
		gl.glDeleteRenderbuffers(1, reflectionDepthBuffer, 0);
		gl.glDeleteTextures(1, reflectionTexture, 0);
		gl.glDeleteTextures(1, refractionTexture, 0);
		gl.glDeleteTextures(1, refractionDepthTexture, 0);
	}
}
