package shader;

import org.joml.Vector3f;

public class BasicMaterialLightShader extends BasicLightShader
{
    //locations
    private int location_diffuseColor;

    public BasicMaterialLightShader()
    {
        super();
    }

    protected void setShaderPaths()
    {
        super.setShaderPaths();

        fragmentShaderDefines = "#define MATERIAL\n";
    }

    @Override
    protected void getAllUniformLocations()
    {
        super.getAllUniformLocations();
        location_diffuseColor = getUniformLocation("uDiffuseColor");
    }

    public void loadDiffuseColor(Vector3f color)
    {
        loadVec3(location_diffuseColor, color);
    }
}
