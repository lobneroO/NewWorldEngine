package entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.joml.Vector3f;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

/**
 * Player is the player controlled entity in the world.
 * It sets its own speed and gravity but uses the given frame time.
 * As long as the terrain has uniform height, it is set in the player to 0.
 * It has to register itself with the backend as a key listener.
 * @author Lobner
 *
 */
public class Player extends Entity implements KeyListener
{
	private static final float RUN_SPEED = 20;		//units per second
	private static final float TURN_SPEED = 90;	//degrees per second
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	private boolean isInAir = false;
	
	boolean[] keys = new boolean[KeyEvent.EVENT_KEY_PRESSED];
	boolean[] keyReleased = new boolean[KeyEvent.EVENT_KEY_PRESSED];
	HashSet<Short> pressedKeys = new HashSet<Short>();
	long[] keyTime = new long[KeyEvent.EVENT_KEY_PRESSED];
	float releaseEpsilon = 10f;	//time in milliseconds to check against
	
	public Player(TexturedModel model, Vector3f position, Vector3f rotation,
			Vector3f scale) {
		super(model, position, rotation, scale);
		initKeyArrays();
	}
	
	private void initKeyArrays()
	{
		for(int i = 0; i < keys.length; i++)
		{
			keys[i] = false;
			keyReleased[i] = false;
			keyTime[i] = 0;
		}
	}
	
	/**
	 * moves the player entity according to the frame time that has passed
	 * @param frameTime frame time in seconds
	 */
	public void move(float frameTime)
	{
		checkInputs();	//checks, which keys are currently down 
		
		float thetaRad = (float) Math.toRadians(currentTurnSpeed * frameTime);
		//rotation in turn speed is stored in deg, thus conversion is needed
		rotate(new Vector3f(0, thetaRad, 0));
		
		/* distance calculation can be understood with a top-down look onto the scene
		 * it is basically just trigonometry
		 * it is divided into turn (above), forwards/sidewards movement and later on the jumping
		 */
		float distance = currentSpeed * frameTime;
		float dx = (float) (distance * Math.sin(getYRotation()));	//getYRotation returns the angle in radians
		float dz = (float) (distance * Math.cos(getYRotation()));
		translate(new Vector3f(-dx, 0, -dz));
		//following is for jumping, with the set gravity and speed variables
		upwardsSpeed += GRAVITY * frameTime;
		translate(new Vector3f(0, upwardsSpeed*frameTime, 0));
		/* For the time being, the player constantly moves downwards according to gravity
		 * this must of course be checked to not go below the terrain
		 */
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
		if(!isInAir)
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
				isInAir = true;
				jump();
			}
		}
//		
//		if(keys[KeyEvent.VK_SPACE])
//		{
//			if(!isInAir)
//			{
//				isInAir = true;
//				jump();
//			}
//		}
	}
	
	/* the key event doesn't trigger anything automatically 
	 * to allow for several key inputs at the same time
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		/*after some time, wrong keyReleased events (followed by wrong keyPressed events)
		 * are triggered, thus stopping the input action of the user. 
		 * this is an autoRepeatEvent, and can be queried as thus -
		 * just omit the event if it is*/
		if(!e.isAutoRepeat())
		{
			keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		/*after some time, wrong keyReleased events are triggered, thus stopping
		* the input action of the user. this is an autoRepeatEvent, and can be 
		* queried as thus - just omit the event if it is*/
		if(!e.isAutoRepeat())
		{
			keys[e.getKeyCode()] = false;
		}
	}

	
}
