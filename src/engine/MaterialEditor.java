package engine;

import cameras.ThirdPersonCamera;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.*;
import entities.*;
import loader.ModelLoader;
import loader.OBJLoader;
import loader.ShaderLoader;
import org.joml.Vector3f;
import util.Program;

public class MaterialEditor extends  Program
{
    MasterRenderer renderer;
    ThirdPersonCamera camera;
    Light light;
    MaterialEntity previewModel;

    /**
     * initialize all the techniques, objects, lists and so forth
     * @param drawable
     * @return
     */
    public boolean init(GLAutoDrawable drawable)
    {
        GL3 gl = drawable.getGL().getGL3();
        createProjectionMatrix();

        //------LOADERS
        modelLoader = new ModelLoader();
        shaderLoader = new ShaderLoader();

        //------LIGHTS
        light = new Light(new Vector3f(-5f, 5, -5f), new Vector3f(1.0f, 1.0f, 1.0f));

        //------MODELS
        RawModel cylinder = OBJLoader.loadObjModel("cylinder/model", modelLoader);
        cylinder.setSpecularIntensity(10);
        cylinder.setSpecularPower(10);
        MaterialModel matModel = new MaterialModel(cylinder);
        previewModel = new MaterialEntity(matModel,
                new Vector3f(0,
                        0,
                        0),
                new Vector3f(0, 0, 0),
                new Vector3f(1, 1, 1));

        renderer = new MasterRenderer();
        renderer.init(getProjectionMatrix(), light, shaderLoader);
        return false;
    }

    /**
     * clean up everything in openGL and further allocated memory.
     * That includes cleaning up with the texture and shader lists!
     * @param drawable
     */
    public void dispose(GLAutoDrawable drawable)
    {

    }
    /**
     * standard display mode, contains the program as it is intended to run
     * @param drawable
     */
    public void display(GLAutoDrawable drawable)
    {
        renderer.render(light, camera);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                                 int height)
    {

    }

    /**
     * describe how to control the program
     */
    public  void printHelp()
    {

    }


    //implements KeyListener, MouseListener
    //implement the abstract methods here
    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            if(hasFocus())
            {
                //TODO: close the material editor
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
