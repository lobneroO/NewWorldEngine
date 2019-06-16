package entities;

import entities.materials.Material;
import entities.materials.BrdfMaterial;
import entities.materials.PhongMaterial;

public class MaterialModel extends Model
{
    Material material;

    public MaterialModel(RawModel model)
    {
        super(model);
        material = new PhongMaterial();
    }

    public MaterialModel(RawModel model, PhongMaterial phongMaterial)
    {
        super(model);
        this.material = phongMaterial;
    }

    public MaterialModel(RawModel model, BrdfMaterial brdfMaterial)
    {
        super(model);
        this.material = brdfMaterial;
    }

    public void setMaterial(Material material)
    {
        this.material = material;
    }

    public Material getMaterial()
    {
        return material;
    }

    @Override
    public void setHasTransparency(boolean hasTransparency)
    {
        //TODO: support tansparency
        System.err.println("Transparency has not yet been implemented for Material Models!");
    }

    public void cleanUp()
    {
        //nothing to be done here
    }
}
