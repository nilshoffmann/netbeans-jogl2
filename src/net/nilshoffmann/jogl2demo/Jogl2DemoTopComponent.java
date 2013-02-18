/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.nilshoffmann.jogl2demo;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//net.nilshoffmann.jogl2demo//Jogl2Demo//EN",
autostore = false)
@TopComponent.Description(
    preferredID = "Jogl2DemoTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "net.nilshoffmann.jogl2demo.Jogl2DemoTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_Jogl2DemoAction",
preferredID = "Jogl2DemoTopComponent")
@Messages({
    "CTL_Jogl2DemoAction=Jogl2 Demo",
    "CTL_Jogl2DemoTopComponent=Jogl2 Demo Window",
    "HINT_Jogl2DemoTopComponent=This is a Jogl2 Demo window"
})
public final class Jogl2DemoTopComponent extends TopComponent {
    
    static GLCapabilities glCaps;
    int quad_x = 5;
    int quad_y = 5;
    private final Component canvas;
    
    public Jogl2DemoTopComponent() {
        glCaps = new GLCapabilities(null);
        initComponents();
        setName(Bundle.CTL_Jogl2DemoTopComponent());
        setToolTipText(Bundle.HINT_Jogl2DemoTopComponent());
        canvas = makeWindow("TestWindow", glCaps);
        GLDrawable gld = GLDrawableFactory.getDesktopFactory().createGLPbuffer(null, glCaps, null, 800, 600, null);
        System.out.println(gld.getClass().getName());
        gld = GLDrawableFactory.getDesktopFactory().createOffscreenDrawable(null, glCaps, null, 800, 600);
        System.out.println(gld.getClass().getName());
        add(canvas, BorderLayout.CENTER);
    }
    
    private Component makeWindow(
            final String name, final GLCapabilities caps) {
        final GLWindow window = GLWindow.create(caps);

//        window.setSize(640, 480);
//        window.setVisible(true);
        window.setTitle(name);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(
                    final WindowEvent e) {
                // System.exit(0);
            }
        });
        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
//                quad_x = me.getX();
//                quad_y = window.getHeight()-me.getY();
            }
        });
        window.addGLEventListener(new GLEventListener() {
            public void display(
                    final GLAutoDrawable drawable) {
                System.out.println("thread "
                        + Thread.currentThread().getId()
                        + " display");
                
                quad_x = (quad_x + 1) % window.getWidth();
                quad_y = (quad_y + 1) % window.getHeight();
                
                final GL2 g2 = drawable.getGL().getGL2();
                g2.glClearColor(0.0f, 0.0f, 0.3f, 1.0f);
                g2.glClear(GL.GL_COLOR_BUFFER_BIT);
                
                g2.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
                g2.glLoadIdentity();
                g2.glOrtho(0, window.getWidth(), 0, window.getHeight(), 1, 100);
                g2.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
                g2.glLoadIdentity();
                g2.glTranslated(0, 0, -1);
                
                g2.glBegin(GL2.GL_QUADS);
                {
                    g2.glVertex2d(quad_x, quad_y + 10);
                    g2.glVertex2d(quad_x, quad_y);
                    g2.glVertex2d(quad_x + 10, quad_y);
                    g2.glVertex2d(quad_x + 10, quad_y + 10);
                }
                g2.glEnd();
            }
            
            public void dispose(
                    final GLAutoDrawable arg0) {
                // TODO Auto-generated method stub
            }
            
            public void init(
                    final GLAutoDrawable arg0) {
                // TODO Auto-generated method stub
            }
            
            public void reshape(
                    final GLAutoDrawable arg0,
                    final int arg1,
                    final int arg2,
                    final int arg3,
                    final int arg4) {
                window.getWidth();
                window.getHeight();
            }
        });
        
        final FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start();
        NewtCanvasAWT canvas = new NewtCanvasAWT(window);
        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                quad_x = e.getX();
                quad_y = window.getHeight() - e.getY();
            }
        });
        return canvas;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }
    
    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
