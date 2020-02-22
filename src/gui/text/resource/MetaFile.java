package gui.text.resource;

import gui.text.type.FontCharacter;

import java.util.HashMap;
import java.util.Map;

/**
 * A struct containing info of a font meta file
 *
 */
public class MetaFile
{
    public int[] padding;
    public int paddingWidth;
    public int paddingHeight;

    public double verticalPerPixelSize;
    public double horizontalPerPixelSize;
    public double spaceWidth;

    public Map<Integer, FontCharacter> metaData = new HashMap<Integer, FontCharacter>();
}
