package entities;

import org.joml.Vector3f;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class Player extends Entity implements KeyListener
{
	private static final float RUN_SPEED = 20;		//units per second
	private static final float TURN_SPEED = 160;	//degrees per second
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	private boolean isInAir = false;
	
	boolean[] keys = new boolean[KeyEvent.EVENT_KEY_PRESSED];
	
	public Player(TexturedModel model, Vector3f position, Vector3f rotation,
			Vector3f scale) {
		super(model, position, rotation, scale);
	}
	
	/**
	 * moves the player entity according to the frame time that has passed
	 * @param frameTime frame time in seconds
	 */
	public void move(float frameTime)
	{
		checkInputs();
		float thetaRad = (float) Math.toRadians(currentTurnSpeed * frameTime);
		//rotation in turn speed is stored in deg, thus conversion is needed
		rotate(new Vector3f(0, thetaRad, 0));
		
		float distance = currentSpeed * frameTime;
		float dx = (float) (distance * Math.sin(Math.toRadians(getYRotation())));
		float dz = (float) (distance * Math.cos(Math.toRadians(getYRotation())));
		translate(new Vector3f(-dx, 0, -dz));
		upwardsSpeed += GRAVITY * frameTime;
		translate(new Vector3f(0, upwardsSpeed*frameTime, 0));
		if(getPosition().y < TERRAIN_HEIGHT)
		{
			upwardsSpeed = 0;
			isInAir = false;
			setPosition(getPosition().x, 0, getPosition().z);
		}
	}
	
	private void jump()
	{
		upwardsSpeed = JUMP_POWER;
	}

	public void checkInputs()
	{
		if(keys[KeyEvent.VK_W])
		{
			currentSpeed = RUN_SPEED;
		}
		else if(keys[KeyEvent.VK_S])
		{
			currentSpeed = -RUN_SPEED;
		}
		else
		{
			currentSpeed = 0;
		}
		
		if(keys[KeyEvent.VK_D])
		{
			currentTurnSpeed = -TURN_SPEED;
		}
		else if(keys[KeyEvent.VK_A])
		{
			currentTurnSpeed = TURN_SPEED;
		}
		else
		{
			currentTurnSpeed = 0;
		}
		
		if(keys[KeyEvent.VK_SPACE])
		{
			if(!isInAir)
			{
				isInAir = true;
				jump();
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		System.out.println(e.getKeyChar() + " is pressed");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		System.out.println(e.getKeyChar() + " is released");
		
	}

	
}
