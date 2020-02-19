package util;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.util.texture.Texture;
import entities.MaterialEntity;
import entities.TexturedEntity;
import loader.ModelLoader;
import loader.ShaderLoader;
import org.joml.Matrix4f;
//import com.sun.prism.ps.Shader;

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
	//preferences
	protected int windowWidth = 1024;
	protected int windowHeight = 1024;
	Matrix4f projectionMatrix;
	//
	protected Backend backend;
	//constants for convenience
	public static int SIZEOFFLOAT = 4;
	public static int SIZEOFINT = 4;

	//info
	protected float frameTime = 0.0f;

	//scene
	protected ModelLoader modelLoader;
	protected ShaderLoader shaderLoader;

	//control input lists such that this program doesn't need to know it's GLEventListener directly but can be queried
	protected List<KeyListener> keyListenerList;
	protected List<MouseListener> mouseListenerList;

	//lists for better controls
	protected List<Texture> textureList;
	//protected List<Shader> shaderList;
	protected List<MaterialEntity> materialEntities;
	protected List<TexturedEntity> texturedEntities;
	//the variables that are always needed
	//Shader shadert;
	float m_scale = 0.05f;
	
	//The standard OpenGL functions, called by the Backend but with the same parameters
	
	/**
	 * initialize all the techniques, objects, lists and so forth
	 * @return
	 */
	public abstract boolean init(GL3 gl);

	/**
	 * clean up everything in openGL and further allocated memory.
	 * That includes cleaning up with the texture and shader lists!
	 * @param drawable
	 */
	public void dispose(GLAutoDrawable drawable)
	{
		for(int i = 0; i < texturedEntities.size(); i++)
		{
			texturedEntities.get(i).cleanUp();
			System.out.println("cleaning up another textured entity");
		}
		for(int i = 0; i < materialEntities.size(); i++)
		{
			materialEntities.get(i).cleanUp();
			System.out.println("cleaning up another textured entity");
		}
	}

	/**
	 * standard display mode, contains the program as it is intended to run
	 * @param drawable
	 */
	public abstract void display(GLAutoDrawable drawable);
	
	public abstract void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height);

	public void setFrameTime(float time)
	{
		frameTime = time;
	}

	public boolean hasFocus()
	{
		return backend.hasProgramFocus(this);
	}

	public List<KeyListener> getKeyListenerList()
	{
		return keyListenerList;
	}

	public List<MouseListener> getMouseListenerList()
	{
		return mouseListenerList;
	}

	/**
	 * Creates the shared projection matrix. This function will be moved when the rendering is restructured
	 */
	protected void createProjectionMatrix()
	{
		//create a default projection matrix
		float FOV = 70;
		float NEAR_PLANE = 0.01f;
		float FAR_PLANE = 100;

		float aspect = (float)windowWidth / (float)windowHeight;
		projectionMatrix = new Matrix4f();
		projectionMatrix.setPerspective((float)Math.toRadians(FOV), aspect, NEAR_PLANE, FAR_PLANE);
	}
	
	public void initLists()
	{
		textureList = new ArrayList<Texture>();
		//shaderList = new ArrayList<Shader>();
	}
	/**
	 * describe how to control the program
	 */
	public abstract void printHelp();
	
	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}

	public int getWindowWidth()
	{
		return windowWidth;
	}
	public int getWindowHeight()
	{
		return windowHeight;
	}
	
	public void setBackend(Backend backend)
	{
		this.backend = backend;
	}
}
