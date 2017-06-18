package cameras;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

import entities.Player;
import toolbox.Maths;

/**
 * A special camera to follow a Player object around. It can be controlled via mouse inputs.
 * @author Lobner
 *
 */
public class ThirdPersonCamera extends Camera implements MouseListener
{
	private float mouseXPos = 0;
	private float mouseYPos = 0;
	private float distanceFromPlayer = 10;
	private float yOffset = 0;
	private float yOffsetFactor = 1;
	private float angleAroundPlayer = Maths.PIf;	//angles in radians
	
	private Player player;
	
	private boolean[] buttons = new boolean[MouseEvent.EVENT_MOUSE_PRESSED];
	
	public ThirdPersonCamera(Player player)
	{
		this.player = player;
		pitch = (float) Math.toRadians(45);
		viewMatrix = Maths.createViewMatrix(position, pitch, yaw);
		
		if(player.getMirrorModelFacing())
		{
			angleAroundPlayer += Maths.PIf;
		}
		
		updateYOffsetFactor();
	}
	
	/**
	 * Updates the camera position and view direction according to the processed mouse input
	 */
	public void move()
	{
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
	}
	
	private float calculateHorizontalDistance()
	{
		return (float) (distanceFromPlayer * Math.cos(pitch));
	}
	
	private float calculateVerticalDistance()
	{
		return (float) (distanceFromPlayer * Math.sin(pitch));
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance)
	{
		float theta = player.getYRotation() + angleAroundPlayer;	//angle in radians
		
		float offsetX = (float) (horizontalDistance * Math.sin(theta));	//theta is in radians at this point
		float offsetZ = (float) (horizontalDistance * Math.cos(theta));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		float offset = yOffset * yOffsetFactor;
		System.out.println("yOffset = " + yOffset + "; yOffsetFactor = " + yOffsetFactor + "; total offset = " + offset);
		position.y = player.getPosition().y + verticalDistance + yOffset * yOffsetFactor;
		yaw = Maths.PIf - theta;	//angle in radians
	}

	private void calculateZoom(float currentScroll)
	{
		float tmp = distanceFromPlayer;
		distanceFromPlayer -= currentScroll;
		if(distanceFromPlayer < 1 || distanceFromPlayer > 50)
		{
			distanceFromPlayer = tmp;
		}
		
		updateYOffsetFactor();
	}
	
	private void calculatePitch(float dy)
	{
		float tmp = pitch;
		dy *= 0.01f;
		if(buttons[MouseEvent.BUTTON1])
		{
			pitch -= dy;
			if(pitch < 0 || pitch > Maths.PIf/2)
			{
				pitch = tmp;
			}
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
	
	public void setYOffset(float yOffset)
	{
		this.yOffset = yOffset;
		updateYOffsetFactor();
	}
	
	private void updateYOffsetFactor()
	{
		yOffsetFactor = (float) (-Math.pow(1/-distanceFromPlayer, 3) + yOffset);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//This should not be used in-game as the buttons are checked on being down
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* Mouse buttons do not directly trigger an action but will be checked on whether 
	 * they are down. Supports multiple buttons at the same time and prevents the break in the
	 * beginning after they are pressed
	 */
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
		//update the mouse position, otherwise the mouseDragged calculation will use
		//wrong initial mouse positions
		mouseXPos = e.getX();
		mouseYPos = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		//get the difference of the old and new position
		float dx = mouseXPos - e.getX();
		float dy = mouseYPos - e.getY();
		//see, if they should actually be used (check for buttons in the respective functions)
		calculateAngleAroundPlayer(dx);
		calculatePitch(dy);
		//update the current mouse position
		mouseXPos = e.getX();
		mouseYPos = e.getY();
	}

	@Override
	public void mouseWheelMoved(MouseEvent e) 
	{
		calculateZoom(e.getRotation()[1]);
	}
}
