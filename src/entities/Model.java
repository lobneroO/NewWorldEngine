package entities;

/**
 * This encapsulates some functionality for how a model is handeled in the engine
 * Don't confuse it with the RawModel, which only stores vertex information
 */
public abstract class Model
{
    protected RawModel model;
    protected boolean hasTransparency = false;

    public Model(RawModel model)
    {
        this.model = model;
    }

    /**
     *
     * @return The Raw Model that just contains vertex information
     */
    public RawModel getRawModel()
    {
        return model;
    }

    /**
     * Transparency may not be wanted or implemented in subclasses, therefore let subclasses
     * handle how they need the setter
     * @param hasTransparency
     */
    public abstract void setHasTransparency(boolean hasTransparency);

    public boolean getHasTransparency()
    {
        return hasTransparency;
    }

    public abstract void cleanUp();
}
