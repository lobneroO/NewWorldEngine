package engine;

import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import loader.ModelLoader;
import loader.OBJLoader;
import loader.ShaderLoader;
import cameras.ThirdPersonCamera;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;

import entities.Entity;
import entities.Light;
import entities.Player;
import entities.RawModel;
import entities.TexturedModel;
import terrains.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import util.Program;

/**
 * Manages the game with its game loop, thus this concerns gaming functions 
 * (rather than the backend setup)
 * @author Lobner
 *
 */
public class MainGameLoop extends Program 
{
	//preferences
	int windowWidth = 1024;
	int windowHeight = 768;
	Matrix4f projectionMatrix;
	
	MasterRenderer renderer;
	
	//scene
	ModelLoader modelLoader;
	ShaderLoader shaderLoader;

	ThirdPersonCamera camera;
	TerrainRenderer terrainRenderer;
	RawModel model;
	TexturedModel staticModel;
	Entity entity;
	Player player;
	Terrain terrain;
	
	Light light;
	@Override
	public boolean init(GLAutoDrawable drawable) 
	{
		GL3 gl = drawable.getGL().getGL3();
		createProjectionMatrix();
		
		//------LOADERS
		modelLoader = new ModelLoader();
		shaderLoader = new ShaderLoader();
				
		//------LIGHTS
		light = new Light(new Vector3f(-5f, 5, -5f), new Vector3f(1.0f, 1.0f, 1.0f));
				
		renderer = new MasterRenderer();
		renderer.init(projectionMatrix, light, shaderLoader);
		
		
		//------MDOELS, PLAYER and CAMERA
		RawModel cylinder = OBJLoader.loadObjModel("cylinder/model", modelLoader);
		cylinder.setSpecularIntensity(10);
		cylinder.setSpecularPower(10);
		TexturedModel texCylinder = new TexturedModel(cylinder, "textures/tex_player.png", false);
		player = new Player(texCylinder, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		backend.addKeyListener(player);
		
		camera = new ThirdPersonCamera(player);
		backend.addMouseListener(camera);
		
		model = OBJLoader.loadObjModel("cube/model", modelLoader);
		model.setSpecularIntensity(1);
		model.setSpecularPower(32);
		staticModel = new TexturedModel(model, "textures/tex_terrain_0.png", false);
		entity = new Entity(staticModel, new Vector3f(3, 0, -3), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		
		Texture[] textures = new Texture[4];
		Texture blendMap;
		File[] files = new File[4];
		files[0] = new File("textures/tex_terrain_0.png");
		files[1] = new File("textures/tex_terrain_1.png");
		files[2] = new File("textures/tex_terrain_2.png");
		files[3] = new File("textures/tex_terrain_3.png");
		File fileBM = new File("textures/tex_blendMap.png");
		try {
			for(int i = 0; i < files.length; i++)
			{
				textures[i] = TextureIO.newTexture(files[i], false);
				textures[i].enable(gl);
				textures[i].setTexParameteri(gl, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
				textures[i].setTexParameteri(gl, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
			}
			blendMap = TextureIO.newTexture(fileBM, false);
			TerrainTexturePack tTP = new TerrainTexturePack(new TerrainTexture(textures[0]), 
					new TerrainTexture(textures[1]), new TerrainTexture(textures[2]),
					new TerrainTexture(textures[3]));
			terrain = new Terrain(0, 0, modelLoader, tTP, new TerrainTexture(blendMap));
		} catch (GLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public void dispose(GLAutoDrawable drawable) 
	{
		//this standard dispose (or the one in backend) is not called, when System.exit(0)
		//is called. so if i want to close on a button, i need to be able to do from an own
		//dispose method
		renderer.cleanUp();
		System.out.println("Exiting program, cleaning up");
		modelLoader.cleanUp();
		System.out.println("loader - done");
		shaderLoader.cleanUp();
		System.out.println("shader - done");
				
		System.exit(0);
	}

	@Override
	public void display(GLAutoDrawable drawable) 
	{
		player.move(backend.getFrameTime()/1000);
		camera.move();
		
		renderer.processEntity(entity);
		renderer.processEntity(player);
		renderer.processTerrains(terrain);
		renderer.render(light, camera);
	}
	
	/**
	 * Creates the shared projection matrix. This function will be moved when the rendering is restructured
	 */
	private void createProjectionMatrix()
	{
		float FOV = 70;
		float NEAR_PLANE = 0.01f;
		float FAR_PLANE = 100;
		
		float aspect = (float)windowWidth / (float)windowHeight;
		projectionMatrix = new Matrix4f();
		projectionMatrix.setPerspective((float)Math.toRadians(FOV), aspect, NEAR_PLANE, FAR_PLANE);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) 
	{
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
//		camera.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printHelp() 
	{
		System.out.println("Move around with WASD");		
	}

	@Override
	public int getWindowWidth() 
	{
		return windowWidth;
	}

	@Override
	public int getWindowHeight() 
	{
		return windowHeight;
	}
}
