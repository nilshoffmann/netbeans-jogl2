/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.nilshoffmann.jogl2demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.concurrent.locks.ReentrantLock;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.DualModeMouseSelector;
import org.jzy3d.chart.controllers.mouse.camera.CameraMouseController;
import org.jzy3d.chart.controllers.thread.camera.CameraThreadController;
import org.jzy3d.chart.factories.ChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.scene.Scene;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays a JZY3D demo.
 */
@ConvertAsProperties(
    dtd = "-//net.nilshoffmann.jogl2demo//Jzy3DDemo//EN",
autostore = false)
@TopComponent.Description(
    preferredID = "Jzy3DDemoTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "net.nilshoffmann.jogl2demo.Jzy3DDemoTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_Jzy3DDemoAction",
preferredID = "Jzy3DDemoTopComponent")
@Messages({
    "CTL_Jzy3DDemoAction=Jzy3DDemo",
    "CTL_Jzy3DDemoTopComponent=Jzy3DDemo Window",
    "HINT_Jzy3DDemoTopComponent=This is a Jzy3DDemo window"
})
public final class Jzy3DDemoTopComponent extends TopComponent {
    
    private Chart chart;
    private Component canvas;
    private Shape surface;
    private CameraThreadController ctc;
    
    public Jzy3DDemoTopComponent() {
        setOpaque(true);
        initComponents();
        setName(Bundle.CTL_Jzy3DDemoTopComponent());
        setToolTipText(Bundle.HINT_Jzy3DDemoTopComponent());
    }
    
    @Override
    protected void componentOpened() {
		super.componentOpened();
		System.out.println("Opened");
        createChart();
    }
    
    @Override
    protected void componentClosed() {
		super.componentClosed();
		System.out.println("Closed");
        if (chart != null) {
            try {
                chart.dispose();
                chart = null;
                canvas = null;
                surface = null;
                ctc = null;
            } catch (Exception e) {
                Exceptions.printStackTrace(e);
            }
        }
    }
    
    @Override
    protected void componentShowing() {
        super.componentShowing();
		System.out.println("Showing");
    }

    @Override
    protected void componentHidden() {
        super.componentHidden();
		System.out.println("Hidden");
    }
    
    @Override
    protected void componentActivated() {
        super.componentActivated();
		System.out.println("Activated");
		RepaintManager.currentManager(this).addInvalidComponent(this);
    }

	@Override
	protected void componentDeactivated() {
		super.componentDeactivated();
		System.out.println("Deactivated");
		RepaintManager.currentManager(this).addInvalidComponent(this);
	}
    
    private void createChart() {
        if (chart == null) {
            chart = new Chart(Quality.Advanced, "newt");
            chart.getView().setMaximized(true);
            canvas = (Component) chart.getCanvas();
            ctc = new CameraThreadController(chart);
            ctc.start();
            jToggleButton1.setSelected(true);
            CameraMouseController camMouse = new CameraMouseController(chart);
            add(canvas, BorderLayout.CENTER);
            // Create a surface drawing that function
            // Define a function to plot
            Mapper mapper = new Mapper() {
                public double f(double x, double y) {
                    return 10 * Math.sin(x / 10) * Math.cos(y / 20) * x;
                }
            };
            // Define range and precision for the function to plot
            Range range = new Range(-150, 150);
            int steps = 50;
            surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
            surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
            surface.setFaceDisplayed(true);
            surface.setWireframeDisplayed(false);
            surface.setWireframeColor(Color.BLACK);
            chart.getScene().getGraph().add(surface, true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jToggleButton1 = new javax.swing.JToggleButton();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        org.openide.awt.Mnemonics.setLocalizedText(jToggleButton1, org.openide.util.NbBundle.getMessage(Jzy3DDemoTopComponent.class, "Jzy3DDemoTopComponent.jToggleButton1.text")); // NOI18N
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        if(ctc!=null) {
            if(jToggleButton1.isSelected()) {
                ctc.start();
            }else{
                ctc.stop();
            }
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
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
