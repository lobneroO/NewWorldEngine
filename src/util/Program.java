package util;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GLAutoDrawable;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.util.texture.Texture;
import com.sun.prism.ps.Shader;

/**
 * Provide common functionality for the render programs being used.
 * The backend needs an object that is a program to start and manage everything.
 * Currently, it keeps track of textures and shaders in a list, but that may be changed 
 * (to an extra manager, somewhat like the ModelLoader class).
 * @author Lobner
 *
 */
public abstract class Program implements KeyListener, MouseListener
{
	//
	protected Backend backend;
	//constants for convenience
	public static int SIZEOFFLOAT = 4;
	public static int SIZEOFINT = 4;
	//lists for better controls
	protected List<Texture> textureList;
	protected List<Shader> shaderList;
	//the variables that are always needed
	Shader shadert;
	float m_scale = 0.05f;
	
	//The standard OpenGL functions, called by the Backend but with the same parameters
	
	/**
	 * initialize all the techniques, objects, lists and so forth
	 * @param drawable
	 * @return
	 */
	public abstract boolean init(GLAutoDrawable drawable);

	/**
	 * clean up everything in openGL and further allocated memory.
	 * That includes cleaning up with the texture and shader lists!
	 * @param drawable
	 */
	public abstract void dispose(GLAutoDrawable drawable);
	/**
	 * standard display mode, contains the program as it is intended to run
	 * @param drawable
	 */
	public abstract void display(GLAutoDrawable drawable);
	
	public abstract void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height);
	
	public void initLists()
	{
		textureList = new ArrayList<Texture>();
		shaderList = new ArrayList<Shader>();
	}
	/**
	 * describe how to control the program
	 */
	public abstract void printHelp();
	
	//maybe unncecessary
	public abstract int getWindowWidth();
	public abstract int getWindowHeight();
	
	public void setBackend(Backend backend)
	{
		this.backend = backend;
	}
}
