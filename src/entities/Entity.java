package entities;

import org.joml.Vector3f;

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
		this.rotation = rotation;
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
