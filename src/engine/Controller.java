package engine;
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
	}

}
