package engine;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;

import loader.ModelLoader;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;

import entities.RawModel;
import entities.TexturedModel;
import shader.StaticTextureShader;
import util.Program;

public class RawModelProgram extends Program 
{
	static int windowWidth = 1024;
	static int windowHeight = 768;
	
	List<TexturedModel> modelList = new ArrayList<TexturedModel>();
	RawModel rawModel;
	ModelLoader loader;
	StaticTextureShader shader;
	@Override
	public boolean init(GLAutoDrawable drawable) 
	{
		GL3 gl = drawable.getGL().getGL3();
		gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
		loader = new ModelLoader();
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
		rawModel = loader.loadToVAO(vertices, texCoords, indices);
		TexturedModel texModel = new TexturedModel(rawModel, "textures/texObject.png", false);
		modelList.add(texModel);
		
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
		loader.cleanUp();
		System.out.println("loader - done");
		shader.cleanUp();
		System.out.println("shader - done");
		
		System.exit(0);
	}

	@Override
	public void display(GLAutoDrawable drawable) 
	{
		GL3 gl = drawable.getGL().getGL3();
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		shader.start();
		for(TexturedModel model : modelList)
		{
			RawModel rawModel = model.getRawModel();
			gl.glBindVertexArray(rawModel.getVAO()[0]);
			gl.glEnableVertexAttribArray(0);
			gl.glEnableVertexAttribArray(1);
			model.bindTexture();
			gl.glDrawElements(GL.GL_TRIANGLES, rawModel.getNumVertices(), GL.GL_UNSIGNED_INT, 0);
			gl.glDisableVertexAttribArray(0);
			gl.glDisableVertexAttribArray(1);
			gl.glBindVertexArray(0);
		}
		shader.stop();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
	{
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode())
		{
//		case KeyEvent.VK_ESCAPE: System.exit(0); break;
		default: break;
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
	public void printHelp() {
		// TODO Auto-generated method stub
		
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
