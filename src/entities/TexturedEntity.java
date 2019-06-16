package entities;

import org.joml.Vector3f;

/**
 * The TexturedEntity class encapsulates a TexturedModel object. It is first and foremost a distinction from
 * the MaterialEntity class.
 *
 * @author Lobner
 *
 */
public class TexturedEntity extends Entity
{
	private TexturedModel model;
	
	public TexturedEntity(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale)
	{
		this.model = model;
		this.position = position;
		this.rotation = rotation;

		this.scale = scale;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public void cleanUp()
	{
		model.cleanUp();
	}
}
