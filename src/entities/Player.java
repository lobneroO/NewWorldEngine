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
		
		long time = System.currentTimeMillis();
		//TODO: if this loop is executed while a keyevent is triggered, a
		//ConcurrentModificationException is thrown, there needs to be some error handling
		List<Short> releasedKeys = new ArrayList<Short>();
		for(short key : pressedKeys)
		{
			if(keyReleased[key])
			{
				if(time - keyTime[key] > releaseEpsilon)
				{
					keyTime[key] = 0;
					keyReleased[key] = false;
					keys[key] = false;
					releasedKeys.add(key);
				}
			}
		}
		for(short key : releasedKeys)
		{
			pressedKeys.remove(key);
		}
		releasedKeys.clear();
	}
	
	/* the key event doesn't trigger anything automatically 
	 * to allow for several key inputs at the same time
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		/*If you press another key while a key is pressed, the press/release events stop for the first one
		 *In that case, the old key needs to keep being pressed*
		 *This solution does not allow to release a key and press another one at the same time
		 *as the released key will still be tracked until it is pressed again, but for the time
		 *being this is the best solution i can achieve without getting a key state without a
		 *key event because the last event triggered upon pushing a new key is always a keyrelease
		 *event for the old key, which makes it impossible to distinguish between just pushing
		 *a new key and holding the old one and actually releasing the old one
		 */
		for(short key : pressedKeys)
		{
			keyReleased[key] = false;
		}
		pressedKeys.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		/*after some time, wrong keyReleased events are triggered, thus stopping
		* the input action of the user. check for the time between the two, to 
		* stop this from happening and enable a normal input*/
		keyTime[e.getKeyCode()] = System.currentTimeMillis();
		keyReleased[e.getKeyCode()] = true;
		
	}

	
}
