package engine;

import cameras.FreeMovingCamera;
import cameras.ThirdPersonCamera;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.*;
import entities.*;
import loader.ModelLoader;
import loader.OBJLoader;
import loader.ShaderLoader;
import org.joml.Vector3f;
import util.Program;

import java.util.ArrayList;

public class MaterialEditor extends  Program
{
    MasterRenderer renderer;
    FreeMovingCamera camera;
    ThirdPersonCamera tpCamera;
    Light light;
    MaterialEntity previewModel;
    Player player;

    /**
     * initialize all the techniques, objects, lists and so forth
     * @param gl
     * @return
     */
    public boolean init(GL3 gl)
    {
//        GL3 gl = drawable.getGL().getGL3();
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
        TexturedModel playerModel = new TexturedModel(cylinder, "textures/tex_player.png", false);
        playerModel.getTexture().setTexParameteri(gl, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        playerModel.getTexture().setTexParameteri(gl, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        playerModel.setHasTransparency(true);

        MaterialModel matModel = new MaterialModel(cylinder);
        previewModel = new MaterialEntity(matModel,
                new Vector3f(1,
                        0,
                        -1),
                new Vector3f(0, 0, 0),
                new Vector3f(1, 1, 1));

        renderer = new MasterRenderer();
        renderer.init(getProjectionMatrix(), light, shaderLoader);

//        materialEntities.add(previewModel);

        player = new Player(playerModel,
                new Vector3f(0, 0, 0),
                new Vector3f(0, 0, 0),
                new Vector3f(1, 1, 1));

        camera = new FreeMovingCamera();
        tpCamera = new ThirdPersonCamera(player);

        //-------CONTROLS
        keyListenerList = new ArrayList<KeyListener>();
        mouseListenerList = new ArrayList<MouseListener>();
        keyListenerList.add(player);
        mouseListenerList.add(tpCamera);
        mouseListenerList.add(camera);

        return true;
    }

    /**
     * clean up everything in openGL and further allocated memory.
     * That includes cleaning up with the texture and shader lists!
     * @param drawable
     */
    @Override
    public void dispose(GLAutoDrawable drawable)
    {
        super.dispose(drawable);
        player.cleanUp();
    }
    /**
     * standard display mode, contains the program as it is intended to run
     * @param drawable
     */
    public void display(GLAutoDrawable drawable)
    {
        player.move(frameTime / 1000);
        tpCamera.move();

//        for(TexturedEntity texturedEntity : texturedEntities)
//        {
//            renderer.processTexturedEntity(texturedEntity);
//        }
        renderer.processTexturedEntity(player);

        renderer.processMaterialEntity(previewModel);

        renderer.render(light, tpCamera);
//        renderer.render(light, camera);

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
        System.out.println("Clicked the mouse!");
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

