package entities;

import org.joml.Vector3f;

public class MaterialEntity extends Entity
{
    private MaterialModel model;

    public MaterialEntity(MaterialModel model, Vector3f position, Vector3f rotation, Vector3f scale)
    {
        this.model = model;
        this.position = position;
        this.rotation = rotation;

        this.scale = scale;
    }

    public MaterialModel getModel()
    {
        return model;
    }

    public void setModel(MaterialModel model)
    {
        this.model = model;
    }

    public void cleanUp()
    {

    }
}
