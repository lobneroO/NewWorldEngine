package engine;

import javax.media.opengl.GLAutoDrawable;

import org.joml.Vector3f;
import org.joml.Vector4f;

import loader.ModelLoader;
import loader.OBJLoader;
import cameras.ThirdPersonCamera;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;

import entities.Entity;
import entities.Player;
import entities.RawModel;
import entities.TexturedModel;
import shader.StaticTextureShader;
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
	
	//scene
	ModelLoader modelLoader;
	float[] vertices = {-0.5f, 0.5f, 0f,
			-0.5f, -0.5f, 0f,
			0.5f, -0.5f, 0f,
			0.5f, 0.5f, 0f};
	float[] texCoords = {0.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f};
	int[] indices = {0, 1, 3,
		3, 1, 2};

	ThirdPersonCamera camera;
	Renderer renderer;
	RawModel model;
	TexturedModel staticModel;
	Entity entity;
	Player player;
	Entity terrain;
	
	StaticTextureShader shader;
	@Override
	public boolean init(GLAutoDrawable drawable) 
	{
		modelLoader = new ModelLoader();
		
		RawModel cylinder = OBJLoader.loadObjModel("cylinder/model", modelLoader);
		TexturedModel texCylinder = new TexturedModel(cylinder, "models/cylinder/texture.png", false);
		player = new Player(texCylinder, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		backend.addKeyListener(player);
		
		camera = new ThirdPersonCamera(player);
		backend.addMouseListener(camera);
		
		renderer = new Renderer(windowWidth, windowHeight, camera);
		renderer.setClearColor(new Vector4f(0.0f, 1.0f, 0.0f, 1.0f));
		
//		model = loader.loadToVAO(vertices, texCoords, indices);
		model = OBJLoader.loadObjModel("cube/model", modelLoader);
		staticModel = new TexturedModel(model, "textures/texObject.png", false);
		entity = new Entity(staticModel, new Vector3f(3, 0, -3), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		
		RawModel ter = OBJLoader.loadObjModel("quad/model", modelLoader);
		TexturedModel texTer = new TexturedModel(ter, "models/quad/texture.png", false);
		terrain = new Entity(texTer, new Vector3f(0, 0, 0), new Vector3f((float) Math.toRadians(90), 0, 0), new Vector3f(10, 10, 10));
		
		shader = new StaticTextureShader();
		
		return true;
	}

	@Override
	public void dispose(GLAutoDrawable drawable) 
	{
		//this standard dispose (or the one in backend) is not called, when System.exit(0)
		//is called. so if i want to close on a button, i need to be able to do from an own
		//dispose method
		System.out.println("Exiting program, cleaning up");
		modelLoader.cleanUp();
		System.out.println("loader - done");
		shader.cleanUp();
		System.out.println("shader - done");
				
		System.exit(0);
	}

	@Override
	public void display(GLAutoDrawable drawable) 
	{
		player.move(backend.getFrameTime()/1000);
		camera.move();
		shader.start();
		renderer.prepare();
		renderer.render(player, shader);
		renderer.render(entity, shader);
		entity.setPosition(3, 0, 3);
		renderer.render(entity, shader);
		entity.setPosition(-3, 0, 3);
		renderer.render(entity, shader);
		entity.setPosition(-3, 0, -3);
		renderer.render(entity, shader);
		renderer.render(terrain, shader);
		shader.stop();
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
