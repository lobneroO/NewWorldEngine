package entities;

import org.joml.Vector3f;

/**
 * The entity class encapsulates a TexturedModel object, a position and rotation as well as a scale.
 * The scale is not fully supported and may be taken out in later versions in order to be able to use
 * the ModelMatrix for normals, too (saves some matrix load ups and calculations and thus decreases
 * the bandwidth need towards the graphics card and increases performance)
 * @author Lobner
 *
 */
public class Entity 
{
	private TexturedModel model;
	private Vector3f position;
	private Vector3f rotation;	//around the axes, in radians
	private Vector3f scale;
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale)
	{
		this.model = model;
		this.position = position;
		this.rotation = rotation;	//rotation is about the x,y,z axes respectively
									//thus a vec3 to store it
		this.scale = scale;
	}
	
	public void translate(Vector3f vec)
	{
		position.add(vec);
	}
	
	/**
	 * Changes the current rotation by the angles specified in the vector.
	 * @param vec The changes in rotation around the axes. Every angle must be in radians
	 */
	public void rotate(Vector3f vec)
	{
		rotation.add(vec);
	}
	
	public TexturedModel getModel() {
		return model;
	}
	public void setModel(TexturedModel model) {
		this.model = model;
	}
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public void setPosition(float x, float y, float z)
	{
		position = new Vector3f(x, y, z);
	}
	public Vector3f getRotation() {
		return rotation;	//every rotation is in radians
	}
	/**
	 * Returns the rotation around the x-axis in radians.
	 * @return Rotation around the x-axis in radians.
	 */
	public float getXRotation()
	{
		return rotation.x;
	}
	/**
	 * Returns the rotation around the y-axis in radians.
	 * @return Rotation around the y-axis in radians.
	 */
	public float getYRotation()
	{
		return rotation.y;
	}
	/**
	 * Returns the rotation around the z-axis in radians.
	 * @return Rotation around the z-axis in radians.
	 */
	public float getZRotation()
	{
		return rotation.z;
	}
	
	/**
	 * Sets the rotation angles around the axes. The rotations are in radians.
	 * @param rotation The rotations as a Vector3f, every angle in radians.
	 */
	public void setRotation(Vector3f rotation) 
	{
		this.rotation = rotation;
	}
	
	public Vector3f getScale() {
		return scale;
	}
	
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public void cleanUp()
	{
		model.cleanUp();
	}
}
