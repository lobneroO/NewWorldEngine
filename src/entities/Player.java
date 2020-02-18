package entities;

import org.joml.Vector3f;

import terrains.Terrain;

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

//TODO: player needs to be able to be a MaterialEntity (or any arbitrary entity!)
public class Player extends TexturedEntity implements KeyListener
{
	private static final float RUN_SPEED = 20;		//units per second
	private static final float TURN_SPEED = 90;	//degrees per second
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
//	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	private boolean isInAir = false;
	
	boolean[] keys = new boolean[KeyEvent.EVENT_KEY_PRESSED];
	
	public Player(TexturedModel model, Vector3f position, Vector3f rotation,
			Vector3f scale)
	{
		super(model, position, rotation, scale);
		initKeyArrays();
	}

	private void initKeyArrays()
	{
		for(int i = 0; i < keys.length; i++)
		{
			keys[i] = false;
		}
	}
	
	/**
	 * moves the player entity according to the frame time that has passed and to the height at the current terrain position
	 * @param frameTime frame time in seconds
	 * @param terrain The terrain on which to move (will look up the height at the new position)
	 */
	public void move(float frameTime, Terrain terrain)
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
//		translate(new Vector3f(-dx, 0, -dz));
		//following is for jumping, with the set gravity and speed variables
		upwardsSpeed += GRAVITY * frameTime;
		translate(new Vector3f(-dx, upwardsSpeed*frameTime, -dz));
		/* For the time being, the player constantly moves downwards according to gravity
		 * this must of course be checked to not go below the terrain
		 */
		float terrainHeight = terrain.getTerrainModelHeightAt(getPosition().x, getPosition().z);
		if(getPosition().y < terrainHeight)
		{
			upwardsSpeed = 0;
			isInAir = false;
			setPosition(getPosition().x, terrainHeight, getPosition().z);
		}
	}

	/**
	 * moves the player entity according to the frame time that has passed and to a height of 0
	 * @param frameTime frame time in seconds
	 */
	public void move(float frameTime)
	{
		move(frameTime, 0);
	}

	/**
	 * moves the player entity according to the frame time that has passed and uses the given height
	 * @param frameTime frame time in seconds
	 * @param height The height to use
	 */
	private void move(float frameTime, float height)
	{
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
//		translate(new Vector3f(-dx, 0, -dz));
		//following is for jumping, with the set gravity and speed variables
		upwardsSpeed += GRAVITY * frameTime;
		translate(new Vector3f(-dx, upwardsSpeed*frameTime, -dz));
		/* For the time being, the player constantly moves downwards according to gravity
		 * this must of course be checked to not go below the terrain
		 */
		if(getPosition().y < height)
		{
			upwardsSpeed = 0;
			isInAir = false;
			setPosition(getPosition().x, height, getPosition().z);
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
