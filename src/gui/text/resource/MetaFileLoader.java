package gui.text.resource;

import gui.text.type.FontCharacter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;



public class MetaFileLoader
{
    //make the reading of char lines as easy as possible
    public static class CharMetaInfo
    {
        int id;
        int x, y;
        int width, height;
        int xOffset, yOffset;
        int xAdvance;
    }

    //constants that won't change
    private static final int PAD_TOP = 0;
    private static final int PAD_LEFT = 1;
    private static final int PAD_BOTTOM = 2;
    private static final int PAD_RIGHT = 3;

    private static final int DESIRE_PADDING = 3;

    //the info to read and return
    private static MetaFile metaFile;
    private static Map<String, String> metaInfo;
    private static Map<Integer, CharMetaInfo> charInfo;

    public static MetaFile readFontMetaFile(String filePath)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));

            metaFile = null;
            metaInfo = new HashMap<String, String>();

            String line = reader.readLine();
            while(line != null && !line.startsWith("chars count"))
            {
                //the line needs to be split according to its formation:
                //individual information is separated by spaces
                for(String info : line.split(" "))
                {
                    //info is stored as "type=value", thus we need to split at the "=" as well
                    String[] pair = info.split("=");
                    if(pair.length == 2)
                    {
                        //we have a correct type -> value pair, thus we can store it
                        metaInfo.put(pair[0], pair[1]);

                    }
                }

                line = reader.readLine();
            }

            //line now starts with the chars count, but we don't need that.
            //however wie can now read out the individual char info
            line = reader.readLine();
            while(line != null)
            {
                //the line needs to be split according to its formation:
                //individual information is separated by spaces
                CharMetaInfo c = new CharMetaInfo();
                for(String info : line.split(" "))
                {
                    //info is stored as "type=value", thus we need to split at the "=" as well
                    String[] pair = info.split("=");

                    //the information in char is
                    //id=
                    //x, y=
                    //width, height=
                    //x- yoffset=
                    //xadvance=
                    //page=
                    //chnl=

                    if(pair[0].equals("id"))
                    {
                        c.id = Integer.parseInt(pair[1]);
                    }
                    else if(pair[0].equals("x"))
                    {
                        c.x = Integer.parseInt(pair[1]);
                    }
                    else if(pair[0].equals("y"))
                    {
                        c.y = Integer.parseInt(pair[1]);
                    }
                    else if(pair[0].equals("width"))
                    {
                        c.width = Integer.parseInt(pair[1]);
                    }
                    else if(pair[0].equals("height"))
                    {
                        c.height = Integer.parseInt(pair[1]);
                    }
                    else if(pair[0].equals("xoffset"))
                    {
                        c.xOffset = Integer.parseInt(pair[1]);
                    }
                    else if(pair[0].equals("yoffset"))
                    {
                        c.yOffset = Integer.parseInt(pair[1]);
                    }
                    else if(pair[0].equals("xadvance"))
                    {
                        c.xAdvance = Integer.parseInt(pair[1]);
                    }

                    //we can ignore page and channel, since they are always 0 (the font should be created that way!)
                    //so we can map the values to the id
                }
                charInfo.put(c.id, c);
            }

            //the first info in the meta file is the padding data


            reader.close();
            return metaFile;
        }
        catch(java.io.FileNotFoundException e)
        {
            System.err.println("Font Meta File not found at " + filePath + "!");
            e.printStackTrace();
        }
        catch(Exception e)
        {
            System.err.println("Could not read Font Meta File at " + filePath + "!");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Assigns the padding data from the values read to the MetaFile object.
     */
    private static void assignPaddingData()
    {
        metaFile.padding = getValues("padding");
        metaFile.paddingWidth = metaFile.padding[PAD_LEFT] + metaFile.padding[PAD_RIGHT];
        metaFile.paddingHeight = metaFile.padding[PAD_TOP] + metaFile.padding[PAD_BOTTOM];
    }

    /**
     * Assigns the line sizes.
     * This method assumes that the padding data has been assigned already, as that will be read and used
     */
    private static void assignLineSizes()
    {
        int lineHeightPixels = getValue("lineHeight") - metaFile.paddingHeight;
        metaFile.verticalPerPixelSize = 0.03 / (double)lineHeightPixels;//TODO: replace constant by a final int in textmeshcreator class
        double aspectRatio = 1024 / 768;   //TODO: read out windows aspect ratio!
        metaFile.horizontalPerPixelSize = metaFile.verticalPerPixelSize / aspectRatio;
    }

    /**
     * Assigns the character data for all characters found in the meta file to the MetaFile object.
     * This method assumes that line sizes and padding data have been assigned already, as they are read and used
     * for some calculations
     * @param imageWidth is the width (and also height, since quadratic) of the texture atlas
     */
    private static void assignCharacterData(int imageWidth)
    {
        for(CharMetaInfo cmi : charInfo.values())
        {
            //special case to handle is the space character!
            if(cmi.id == 32)
            {
               metaFile.spaceWidth = (cmi.xAdvance - metaFile.paddingWidth) * metaFile.horizontalPerPixelSize;
               //don't actually add the space character. there is no more information needed
                continue;
            }

            double xTex = ((double)cmi.x + (metaFile.padding[PAD_LEFT] - DESIRE_PADDING)) / imageWidth;
            double yTex = ((double)cmi.y + (metaFile.padding[PAD_TOP] - DESIRE_PADDING)) / imageWidth;

            //width of a character is the width reserved in the atlas, minus the padding
            //the padding is the overall used padding in the atlas minus the sired padding for usage (on either side)
            int width = cmi.width - (metaFile.paddingWidth - (2 * DESIRE_PADDING));
            int height = cmi.height - (metaFile.paddingHeight - (2 * DESIRE_PADDING));

            double quadWidth = width * metaFile.horizontalPerPixelSize;
            double quadHeight = height * metaFile.verticalPerPixelSize;

            double xTexSize = (double)width / imageWidth;
            double yTexSize = (double)height / imageWidth;

            double xOffset = (cmi.xOffset + metaFile.padding[PAD_LEFT] - DESIRE_PADDING)
                    * metaFile.horizontalPerPixelSize;
            double yOffset = (cmi.yOffset + metaFile.padding[PAD_TOP] - DESIRE_PADDING)
                    * metaFile.verticalPerPixelSize;

            double xAdvance = (cmi.xAdvance - metaFile.paddingWidth) * metaFile.horizontalPerPixelSize;

            FontCharacter c = new FontCharacter(cmi.id, xTex, yTex, xTexSize, yTexSize, xOffset, yOffset,
                    quadWidth, quadHeight, xAdvance);
            metaFile.metaData.put(cmi.id, c);
        }
    }

    private static int[] getValues(String variableName)
    {
        //individual values are comma separated
        String[] strValues = metaInfo.get(variableName).split(",");
        int[] values = new int[strValues.length];
        for(int i = 0; i < strValues.length; i++)
        {
            values[i] = Integer.parseInt(strValues[i]);
        }

        return values;
    }

    private static int getValue(String variableName)
    {
        String strValue = metaInfo.get(variableName);
        return Integer.parseInt(strValue);
    }

}
