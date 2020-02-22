package gui.text.type;

public class FontCharacter
{
    //FontCharacter is in theory just a struct, but we want to calculate a small portion
    //and we don't want a character to change post creation, therefor use everything as private
    //and access values via getters instead

    private int id;
    private double xTextureCoord;
    private double yTextureCoord;
    private double xMaxTextureCoord;
    private double yMaxTextureCoord;
    private double xOffset;
    private double yOffset;
    private double sizeX;
    private double sizeY;
    private double xAdvance;

    public FontCharacter(int id, double xTextureCoord, double yTextureCoord, double xTexSize,
                            double yTexSize, double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance)
    {
        this.id = id;
        this.xTextureCoord = xTextureCoord;
        this.yTextureCoord = yTextureCoord;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.xMaxTextureCoord = xTexSize + xTextureCoord;
        this.yMaxTextureCoord = yTexSize + yTextureCoord;
        this.xAdvance = xAdvance;
    }

    protected int getId()
    {
        return id;
    }
}
