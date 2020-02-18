package util;

import java.util.Hashtable;
import java.util.Set;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.opengl.*;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;

import engine.MainGameLoop;
import util.Program;

/**
 * The Backend takes care of the window creation and adjustment.
 * It also tracks the frame time so as to give every class access, that needs the frame time.
 * It manages the key and mouse listeners (although this may be outsourced to extra managing classes
 * later on).
 * @author Lobner
 *
 */
public class Backend implements GLEventListener
{
	GLWindow glWindow;
	Hashtable<Program, GLWindow> subWindows;
	
	Program m_program;
	
	//time management
	private static long lastFrameTime;
	private static float delta;
	
	public Backend()
	{
		subWindows = new Hashtable<Program, GLWindow>();
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

	public void createSubWindow(int windowWidth, int windowHeight, boolean isFullscreen, String windowTitle
			, Program program)
	{

		Display display = NewtFactory.createDisplay("mystring");
		display.addReference();

		Screen screen = NewtFactory.createScreen(display, 0);
		screen.addReference();

		GLProfile profile = GLProfile.get(GLProfile.GL3);
		GLCapabilities caps = new GLCapabilities(profile);
		GLWindow window = GLWindow.create(screen, caps);
		//the following line does not work, context is null
		GLContext context = window.createContext(GLContext.getCurrent());

		window.setSize(windowWidth, windowHeight);
		window.setVisible(true);
		window.setTitle(windowTitle);

		System.out.println(windowTitle + " window is open!");
	}

	public void closeSubwindow(Program program)
	{
		GLWindow w = subWindows.get(program);
//		program.dispose()
	}

	/**
	 * Is called by JOGL when OpenGL is initialized for the program.
	 * On top of the OpenGL functionality check it checks the program object on
	 * whether everything is correctly initialized, then prints the help to the console
	 * and finally startes the frame timer.
	 */
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
        
		if(!m_program.init(gl))
		{
			System.err.println("Could not initialize program!");
			System.exit(1);
		}
		m_program.printHelp();
		
		lastFrameTime = System.currentTimeMillis();
	}

	/**
	 * Disposes of the OpenGL drawable (i.e. the canvas and interface).
	 * All the other clean ups are taken care of elsewhere to not keep track of everything 
	 * the Backend class.
	 */
	@Override
	public void dispose(GLAutoDrawable drawable) 
	{
		m_program.dispose(drawable);
	}

	/**
	 * While JOGl calls this display function, the backend will delegate it to the program.
	 * This will enable several window displays independent of each other.
	 * It also keeps track of the frame times.
	 */
	@Override
	public void display(GLAutoDrawable drawable) 
	{
		long currentFrameTime = System.currentTimeMillis();
		m_program.display(drawable);
		delta = currentFrameTime - lastFrameTime;
		lastFrameTime = System.currentTimeMillis();

		Set<Program> subwindowPrograms = subWindows.keySet();
		for(Program program : subwindowPrograms)
		{
			program.display(drawable);
		}
	}

	/**
	 *	A program has exactly one window and one window has exactly one program. Therefore, if a program queries for whether it has focus, we can look for its window and see if that has focus.
	 * @param program The program for which the focus is queried for.
	 * @return True, if the window of the program has focus, false otherwise.
	 */
	public boolean hasProgramFocus(Program program)
	{
		if(program.getClass() == MainGameLoop.class)
		{
			//is the main game querying for focus
			if(glWindow.hasFocus())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			//is a sub program querying for focus
			GLWindow subWindow = subWindows.get(program);
			return subWindow.hasFocus();
		}
	}
	
	/**
	 * Gives access to the time, the last frame needed for everything.
	 * @return The last frame time.
	 */
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
	
	/**
	 * Manages the KeyListeners by adding them to the window.
	 * @param listener The KeyListener to be added.
	 */
	public void addKeyListener(KeyListener listener)
	{
		glWindow.addKeyListener(listener);
	}
	
	/**
	 * Manages the MouseListeners by adding them to the window.
	 * @param listener The MouseListener to be added.
	 */
	public void addMouseListener(MouseListener listener)
	{
		glWindow.addMouseListener(listener);
	}
}
