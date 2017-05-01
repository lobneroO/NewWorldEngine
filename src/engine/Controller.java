package engine;
import util.Backend;
import util.Program;


public class Controller {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		Backend.initOpenGl();	//initialize openGL
		Program pApp;
		pApp = new MainGameLoop();
		
		
		Backend backend = new Backend();
		backend.createWindow(pApp.getWindowWidth(), pApp.getWindowHeight(), false, "Tutorial", pApp);
	}

}
