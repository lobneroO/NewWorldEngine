package entities.materials;

import org.joml.Vector3f;

public class PhongMaterial extends Material
{
    public Vector3f specularColor;
    public float specularExponent;

    public PhongMaterial()
    {
        initColors(true, true, true);
    }

    public PhongMaterial(Vector3f diffuseColor)
    {
        this.diffuseColor = diffuseColor;
        initColors(false, true, true);
    }

    public PhongMaterial(Vector3f diffuseColor, Vector3f specularColor)
    {
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        initColors(false, false, true);
    }

    public PhongMaterial(Vector3f diffuseColor, Vector3f specularColor, float specularExponent)
    {
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.specularExponent = specularExponent;
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
            specularExponent = 0.5f;
        }
    }
}
