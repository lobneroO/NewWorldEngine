package entities.materials;

import org.joml.Vector3f;

public class BrdfMaterial extends Material
{
    public Vector3f specularColor;
    public float roughness;

    public BrdfMaterial()
    {
        initColors(true, true, true);
    }

    public BrdfMaterial(Vector3f diffuseColor)
    {
        this.diffuseColor = diffuseColor;
        initColors(false, true, true);
    }

    public BrdfMaterial(Vector3f diffuseColor, Vector3f specularColor)
    {
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        initColors(false, false, true);
    }

    public BrdfMaterial(Vector3f diffuseColor, Vector3f specularColor, float roughness)
    {
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.roughness = roughness;
    }

    public void initColors(boolean dC, boolean sC, boolean r)
    {
        //if dC is set to true, set diffuse Color to default
        //the same for sC and specular Color and r and roughness
        if(dC)
        {
            diffuseColor = new Vector3f(0.5f, 0.5f, 0.5f);
        }

        if(sC)
        {
            specularColor = new Vector3f(0.05f, 0.05f, 0.05f);
        }

        if(r)
        {
            roughness = 0.5f;
        }
    }
}
