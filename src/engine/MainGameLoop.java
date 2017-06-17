package engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import org.joml.Matrix4f;
import org.joml.Vector2f;
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
import textures.GUITexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;
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
	List<Entity> entities;
	List<Texture> textures;
	Player player;
	Terrain terrain;
	RawModel skybox;
	
	Light light;
	
	//GUIs
	ArrayList<GUITexture> guiTextures;
	boolean displayMenu = false;
	GUITexture menu;
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
		entities = new ArrayList<Entity>();
		RawModel cylinder = OBJLoader.loadObjModel("cylinder/model", modelLoader);
		cylinder.setSpecularIntensity(10);
		cylinder.setSpecularPower(10);
		TexturedModel texCylinder = new TexturedModel(cylinder, "textures/tex_player.png", false);
		texCylinder.getTexture().setTexParameteri(gl, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		texCylinder.getTexture().setTexParameteri(gl, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		texCylinder.setHasTransparency(true);
		player = new Player(texCylinder, new Vector3f(12, 0, 12), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		backend.addKeyListener(player);
		
		camera = new ThirdPersonCamera(player);
		backend.addMouseListener(camera);
		
		model = OBJLoader.loadObjModel("cube/model", modelLoader);
		model.setSpecularIntensity(1);
		model.setSpecularPower(32);
		staticModel = new TexturedModel(model, "textures/tex_terrain_0.png", false);
		
		RawModel rawBamboo = OBJLoader.loadObjModel("plants/bamboo", modelLoader);
		TexturedModel texturedBamboo = new TexturedModel(rawBamboo, 
				"textures/plants/tex_bamboo.tga", false);
		texturedBamboo.setHasTransparency(true);
		
		RawModel rawBush = OBJLoader.loadObjModel("plants/bush", modelLoader);
		TexturedModel texturedBush = new TexturedModel(rawBush,
				"textures/plants/tex_bush.tga", false);
		texturedBush.setHasTransparency(true);
		
		RawModel rawHemp = OBJLoader.loadObjModel("plants/hemp", modelLoader);
		TexturedModel texturedHemp = new TexturedModel(rawHemp,
				"textures/plants/tex_hemp.tga", false);
		texturedHemp.setHasTransparency(true);
		
		RawModel rawSwirl = OBJLoader.loadObjModel("plants/swirl", modelLoader);
		TexturedModel texturedSwirl = new TexturedModel(rawSwirl,
				"textures/plants/tex_swirl.tga", false);
		texturedSwirl.setHasTransparency(true);
		
		RawModel rawWhiteFlower = OBJLoader.loadObjModel("plants/white_flower", modelLoader);
		TexturedModel texturedWhiteFlower = new TexturedModel(rawWhiteFlower,
				"textures/plants/tex_white_flower.tga", false);
		texturedWhiteFlower.setHasTransparency(true);
		
		for(int i = 0; i < 30; i++)
		{
			float x = (float) (Math.random() * 100);
			float y = 0;
			float z = (float) (Math.random() * 100);
			float rot = (float) (Math.random() * 2 * Maths.PIf);
			
			TexturedModel chosenModel;
			float scale = 1;
			int rand = (int) (Math.random()*5);
			switch(rand)
			{
				case 0: chosenModel = texturedBamboo; scale = 1.0f/25.0f;break;
				case 1: chosenModel = texturedBush; scale = 1.0f/25.0f;break;
				case 2: chosenModel = texturedHemp; scale = 1.0f/25.0f;break;
				case 3: chosenModel = texturedSwirl; scale = 1.0f/25.0f;break;
				case 4: chosenModel = texturedWhiteFlower; scale = 1.0f/25.0f;break;
				default: chosenModel = staticModel; y = 1; break;
			}
			//create some randomly placed entities. *100 because that's the size of the terrain
			
			entities.add(new Entity(chosenModel,
					new Vector3f(x, y, z),
					new Vector3f(0, rot, 0),
					new Vector3f(scale, scale, scale)));
		}
		entity = new Entity(staticModel, new Vector3f(10, 1, 10), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		
		Texture[] textures = new Texture[4];
		Texture blendMap;
		File[] files = new File[4];
		files[0] = new File("textures/terrain/grass_green2y_d.jpg");
		files[1] = new File("textures/terrain/grass_autumn_red_d.jpg");
		files[2] = new File("textures/terrain/grass_rocky_d.jpg");
		files[3] = new File("textures/terrain/ground_crackedo_d.jpg");
		File fileBM = new File("textures/terrain/tex_blendMap.png");
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
			terrain = new Terrain(0, 0, modelLoader, tTP, new TerrainTexture(blendMap), 
					"textures/terrain/tex_heightMap.png");
		} catch (GLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		renderer.setSkybox(shaderLoader, modelLoader);
		
		//GUIs
		guiTextures = new ArrayList<GUITexture>();
		
		try{
			File fileTex = new File("textures/gui/menu.png");
			Texture tex = TextureIO.newTexture(fileTex, false);
			menu = new GUITexture(tex, new Vector2f(0, 0), new Vector2f(1, 1));
		} catch (IOException e)
		{
			System.err.println("Could not load textures/gui/gui_second.png");
			System.err.println(e.getStackTrace());
		}
		
		renderer.setGUI(shaderLoader, modelLoader);
		
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
		
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).cleanUp();
			System.out.println("cleaning up another entity");
		}
		
		for(int i = 0; i < guiTextures.size(); i++)
		{
			guiTextures.get(i).cleanUp();
		}
		if(menu != null)
		{
			menu.cleanUp();
		}
		
		entity.cleanUp();
		terrain.cleanUp();
		player.cleanUp();
				
		System.exit(0);
	}

	@Override
	public void display(GLAutoDrawable drawable) 
	{
		player.move(backend.getFrameTime()/1000);
		camera.move();
		
		for(Entity entity : entities)
		{
			renderer.processEntity(entity);
		}
		renderer.processEntity(entity);
		renderer.processEntity(player);
		renderer.processTerrains(terrain);
		for(GUITexture tex : guiTextures)
		{
			renderer.processGUITextures(tex);
		}
		
		if(displayMenu)
		{
			renderer.processGUITextures(menu);
		}
		
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
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			if(displayMenu)
			{
				displayMenu = false;
			}
			else
			{
				displayMenu = true;
			}
		}
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
		System.out.println("Move around with WASD, Jump with SPACE");
		System.out.println("Click the left mouse button to move the camera around the player");
		System.out.println("Use the mouse wheel to zoom the camera in or out");
		System.out.println("Open the menu with Esc");
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
