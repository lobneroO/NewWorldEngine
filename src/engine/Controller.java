package engine;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import util.Backend;
import util.Program;

/**
 * Controller is the main class. It creates a Backend and the wished for rendering Program object,
 * which it then puts into the Backend object to use.
 * @author Lobner
 *
 */
public class Controller {

	public static void main(String[] args) 
	{
		Backend.initOpenGl();	//initialize openGL
		Program pApp;
		pApp = new MainGameLoop();
		
		Backend backend = new Backend();
		backend.createWindow(pApp.getWindowWidth(), pApp.getWindowHeight(), false,
				"New World Laboratories", pApp);

//		GLWindow sndWindow = openSecondWindow();
//		sndWindow.setVisible(true);

	}

	public static GLWindow openSecondWindow()
	{
		Display display = NewtFactory.createDisplay("mystring");
		display.addReference();

		Screen screen = NewtFactory.createScreen(display, 0);
		screen.addReference();

		GLProfile profile = GLProfile.get(GLProfile.GL3);
		GLCapabilities caps = new GLCapabilities(profile);
		GLWindow window = GLWindow.create(screen, caps);

		window.setPosition(10, 10);
		window.setSize(800, 600);

		System.out.println("Second window is open!");
		return window;
	}

}
