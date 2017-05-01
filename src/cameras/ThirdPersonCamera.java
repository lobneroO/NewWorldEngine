package cameras;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

import entities.Player;
import toolbox.Maths;

public class ThirdPersonCamera extends Camera implements MouseListener
{
	private float mouseXPos = 0;
	private float mouseYPos = 0;
	private float distanceFromPlayer = 10;
	private float angleAroundPlayer = 180;
	
	private Player player;
	
	boolean init = false;
	
	private boolean[] buttons = new boolean[MouseEvent.EVENT_MOUSE_PRESSED];
	
	public ThirdPersonCamera(Player player)
	{
		this.player = player;
		pitch = 70f;
		viewMatrix = Maths.createViewMatrix(position, pitch, yaw);
	}
	
	public void move()
	{
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
	}
	
	private float calculateHorizontalDistance()
	{
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance()
	{
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance)
	{
		float theta = player.getYRotation() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticalDistance;
		yaw = 180 - (player.getYRotation() + angleAroundPlayer);
	}

	private void calculateZoom(float currentScroll)
	{
		distanceFromPlayer -= currentScroll;
	}
	
	private void calculatePitch(float dy)
	{
		dy *= 0.01f;
		if(buttons[MouseEvent.BUTTON3])
		{
			pitch += dy;
		}
	}
	
	private void calculateAngleAroundPlayer(float dx)
	{
		dx *= 0.01f;
		if(buttons[MouseEvent.BUTTON1])
		{
			angleAroundPlayer += dx;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		init = false;
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
		buttons[e.getButton()] = true;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
		
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{		
		mouseXPos = e.getX();
		mouseYPos = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		float dx = mouseXPos - e.getX();
		float dy = mouseYPos - e.getY();
		calculateAngleAroundPlayer(dx);
		calculatePitch(dy);
		mouseXPos = e.getX();
		mouseYPos = e.getY();
	}

	@Override
	public void mouseWheelMoved(MouseEvent e) 
	{
		calculateZoom(e.getRotation()[1]);
	}
}
