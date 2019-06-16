package shader;

public class BasicTextureLightShader extends BasicLightShader
{
    public BasicTextureLightShader()
    {
        super();
    }

    protected void setShaderPaths()
    {
        super.setShaderPaths();

        fragmentShaderDefines = "#define TEXTURE\n";
    }
}
