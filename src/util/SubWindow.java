package util;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;

public class SubWindow implements GLEventListener
{
    GLWindow glWindow;
    Program m_program;

    public void createSubWindow(int windowWidth, int windowHeight, boolean isFullscreen, String windowTitle
            , Program program)
    {
        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL3));
        caps.setBackgroundOpaque(false);
        glWindow = GLWindow.create(caps);

        glWindow.setTitle(windowTitle);
        glWindow.setSize(windowWidth, windowHeight);
        glWindow.setUndecorated(false);
        glWindow.setPointerVisible(true);
        glWindow.setVisible(true);

        m_program = program;
        glWindow.addGLEventListener(this);
        glWindow.addKeyListener(m_program);
        glWindow.addMouseListener(m_program);
//        m_program.setBackend(this);

        Animator animator = new Animator();
        animator.add(glWindow);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable)
    {
        GL3 gl = drawable.getGL().getGL3();

        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

        // Make sure GL3 Core is supported
        if(gl.isGL3core())
        {
            System.out.println("GL3 core detected");
        }
        else
        {
            System.out.println("ERROR: GL3 core not detected");
            System.exit(1);
        }

        if(!m_program.init(gl))
        {
            System.err.println("Could not initialize program!");
            System.exit(1);
        }
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable)
    {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable)
    {
        GL3 gl = glAutoDrawable.getGL().getGL3();
        gl.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3)
    {

    }
}
