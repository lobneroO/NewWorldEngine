package util;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;

import util.Program;

public class Backend implements GLEventListener
{
	GLWindow glWindow;
	
	Program m_program;
	
	//time management
	private static long lastFrameTime;
	private static float delta;
	
	public Backend()
	{
		
	}
	
	/**
	 * Initializes OpenGL by creating the instance of the GLProfile
	 */
	public static void initOpenGl()
	{
		GLProfile.initSingleton();
	}
	
	/**
	 * creates the window that will be drawn on
	 * No full screen support so far
	 * @param windowWidth 	the width of the window
	 * @param windowHeight	the height of the window
	 * @param isFullscreen	says if the window should be full screen (NOT SUPPORTED YET)
	 * @param windowTitle	sets the window title
	 * @param program		is the interface for the application to set itself (for GLEvent, Key and Mouse listener)
	 */
	public void createWindow(int windowWidth, int windowHeight, boolean isFullscreen, String windowTitle
			, Program program)
	{
		GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL3));
        caps.setBackgroundOpaque(false);
        glWindow = GLWindow.create(caps);
        
		glWindow.setTitle(windowTitle);
		glWindow.setSize(windowWidth, windowHeight);
		glWindow.setUndecorated(false);
		glWindow.setPointerVisible(true);
		glWindow.setVisible(true);
		
		m_program = program;
		glWindow.addGLEventListener(this);
		glWindow.addKeyListener(m_program);
		glWindow.addMouseListener(m_program);
		m_program.setBackend(this);

		Animator animator = new Animator();
        animator.add(glWindow);
        animator.start();
	}


	@Override
	public void init(GLAutoDrawable drawable) 
	{
		GL3 gl = drawable.getGL().getGL3();
	      
        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

        // Make sure GL3 Core is supported
        if(gl.isGL3core())
        {
            System.out.println("GL3 core detected");
        } 
        else 
        {
        	System.out.println("ERROR: GL3 core not detected");
        	System.exit(1);
        }
        
		if(!m_program.init(drawable))
		{
			System.err.println("Could not initialize program!");
			System.exit(1);
		}
		m_program.printHelp();
		
		lastFrameTime = System.currentTimeMillis();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) 
	{
		m_program.dispose(drawable);
	}

	@Override
	public void display(GLAutoDrawable drawable) 
	{
		long currentFrameTime = System.currentTimeMillis();
		m_program.display(drawable);
		delta = currentFrameTime - lastFrameTime;
		lastFrameTime = System.currentTimeMillis();
	}
	
	public float getFrameTime()
	{
		return delta;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) 
	{
		m_program.reshape(drawable, x, y, width, height);
	}
	
	public void addKeyListener(KeyListener listener)
	{
		glWindow.addKeyListener(listener);
	}
	
	public void addMouseListener(MouseListener listener)
	{
		glWindow.addMouseListener(listener);
	}
}
