package engine;

import com.jogamp.opengl.GLAutoDrawable;

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
import shader.TerrainShader;
import terrains.Terrain;
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
	TerrainShader terrainShader;
	@Override
	public boolean init(GLAutoDrawable drawable) 
	{
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
		TexturedModel texCylinder = new TexturedModel(cylinder, "models/cylinder/texture.png", false);
		player = new Player(texCylinder, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		backend.addKeyListener(player);
		
		camera = new ThirdPersonCamera(player);
		backend.addMouseListener(camera);
		
		terrainShader = new TerrainShader();
		shaderLoader.loadShader(terrainShader);
		terrainRenderer = new TerrainRenderer(camera, terrainShader, projectionMatrix);
		
		model = OBJLoader.loadObjModel("cube/model", modelLoader);
		model.setSpecularIntensity(1);
		model.setSpecularPower(32);
		staticModel = new TexturedModel(model, "textures/texObject.png", false);
		entity = new Entity(staticModel, new Vector3f(3, 0, -3), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		
		terrain = new Terrain(0, 0, modelLoader, "models/quad/texture.png");
		
		terrainShader.start();
		terrainShader.loadLightColor(light.getColor());
		terrainShader.stop();
		
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
		renderer.render(light, camera);
		
		terrainShader.start();
		terrainShader.loadLightPosition(light.getPosition());
		terrainRenderer.render(terrain);
		terrainShader.stop();
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
