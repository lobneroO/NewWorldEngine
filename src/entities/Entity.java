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
	private Vector3f rotation;
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
		return rotation;
	}
	public float getXRotation()
	{
		return rotation.x;
	}
	public float getYRotation()
	{
		return rotation.y;
	}
	public float getZRotation()
	{
		return rotation.z;
	}
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	public Vector3f getScale() {
		return scale;
	}
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
}
