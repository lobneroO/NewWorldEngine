package cameras;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

/**
 * A camera that is not bound to an entity, allowing for free moving in the scene.
 * @author Lobner
 *
 */
public class FreeMovingCamera extends Camera  implements KeyListener, MouseListener
{
	boolean moved = false;
	
	public FreeMovingCamera()
	{
		
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
	public void keyPressed(KeyEvent e) 
	{
		moved = true;
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_W: position.z -= 0.02f; break;
		case KeyEvent.VK_S: position.z += 0.02f; break;
		case KeyEvent.VK_A: position.x -= 0.02f; break;
		case KeyEvent.VK_D: position.x += 0.02f; break;
		default: moved = false; break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
