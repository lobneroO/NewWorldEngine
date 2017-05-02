package entities;

import org.joml.Vector3f;

public class Light 
{
	private Vector3f position;
	private Vector3f color;
	private float specularIntensity = 0;
	private float specularPower = 0;
	
	public Light(Vector3f position, Vector3f color)
	{
		this.position = position;
		this.color = color;
	}
	
	public Light(Vector3f position, Vector3f color, float specularIntensity, float specularPower)
	{
		this.position = position;
		this.color = color;
		this.specularIntensity = specularIntensity;
		this.specularPower = specularPower;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getSpecularIntensity() {
		return specularIntensity;
	}

	public void setSpecularIntensity(float specularIntensity) {
		this.specularIntensity = specularIntensity;
	}

	public float getSpecularPower() {
		return specularPower;
	}

	public void setSpecularPower(float specularPower) {
		this.specularPower = specularPower;
	}
	
	
}
