package entities;

import org.joml.Vector3f;

public class MaterialEntity extends Entity
{
    private MaterialModel model;

    public MaterialEntity(MaterialModel model, Vector3f position, Vector3f rotation, Vector3f sale)
    {
        this.model = model;
        this.position = position;
        this.rotation = rotation;

        this.scale = scale;
    }

    public void cleanUp()
    {

    }
}
