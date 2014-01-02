/*
 * Copyright 2014 Nils Hoffmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.nilshoffmann.jogl2demo;

import com.jogamp.newt.event.MouseEvent;
import java.awt.BorderLayout;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingUtilities;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.camera.NewtCameraMouseController;
import org.jzy3d.chart.controllers.thread.camera.CameraThreadController;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory.Toolkit;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.CanvasNewtAwt;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.openide.windows.TopComponent;

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
	private CanvasNewtAwt canvas;
	private CameraThreadController ctc;
	private AtomicBoolean chartCreated = new AtomicBoolean(false);

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
		if (chartCreated.get() && chart != null) {
			try {
				//dispose chart resources
				chart.dispose();
			} catch (Exception e) {
				Exceptions.printStackTrace(e);
			} finally {
				//cleanup
				chart = null;
				ctc = null;
				remove(canvas);
				//dispose of canvas and native resources
				canvas.dispose();
				chartCreated.set(false);
			}
		}
	}

	@Override
	protected void componentShowing() {
		super.componentShowing();
		System.out.println("Showing");
		if (chartCreated.get()) {
			if (ctc != null) {
				if (toggleAnimation.isSelected()) {
					//reenable animation if animation is toggled and component is set to visible
					ctc.start();
				}
			}
		}
	}

	@Override
	protected void componentHidden() {
		super.componentHidden();
		System.out.println("Hidden");
		if (chartCreated.get()) {
			if (ctc != null) {
				//stop animation if top component is not visible
				ctc.stop();
			}
		}
	}

	@Override
	protected void componentActivated() {
		super.componentActivated();
		System.out.println("Activated");
		if (chartCreated.get()) {
			if (chart != null) {
				CanvasNewtAwt cna = (CanvasNewtAwt) chart.getCanvas();
				cna.setSize(getSize());
				//need to update complete component tree
				invalidate();
				getTopLevelAncestor().invalidate();
				getTopLevelAncestor().revalidate();
			}
		}
	}

	private void createChart() {
		if (chartCreated.compareAndSet(false, true)) {
			System.out.println("Creating chart!");
			Runnable worker = new Runnable() {

				@Override
				public void run() {
					ProgressHandle ph = ProgressHandleFactory.createHandle("Opening Jzy3D View!");
					ph.start();
					ph.switchToIndeterminate();
					try {
						AWTChartComponentFactory accf = new AWTChartComponentFactory();
						final Chart chart = accf.newChart(Quality.Advanced, Toolkit.newt.name());
						chart.getView().setMaximized(true);
						canvas = (CanvasNewtAwt) chart.getCanvas();
						final CameraThreadController ctc = new CameraThreadController(chart);
						//signature change in latest upstream jogl causes java.lang.NoSuchMethodError: com.jogamp.newt.event.MouseEvent.getClickCount()I
//						NewtCameraMouseController camMouse = new NewtCameraMouseController(chart);
						WorkaroundNewtCameraMouseController camMouse = new WorkaroundNewtCameraMouseController(chart);
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
						Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
						surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
						surface.setFaceDisplayed(true);
						surface.setWireframeDisplayed(false);
						surface.setWireframeColor(Color.BLACK);
						chart.getScene().getGraph().add(surface, true);
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								setChart(chart);
								canvas.setMinimumSize(getMinimumSize());
								canvas.setMaximumSize(getMaximumSize());
								canvas.setPreferredSize(getPreferredSize());
								add(canvas, BorderLayout.CENTER);
								setCameraThreadController(ctc);
								if (toggleAnimation.isSelected()) {
									ctc.start();
								}
								//need to update complete component tree
								invalidate();
								getTopLevelAncestor().invalidate();
								getTopLevelAncestor().revalidate();
							}
						});
					} finally {
						ph.finish();
					}
				}

			};
			Task t = RequestProcessor.getDefault().post(worker);
			t.addTaskListener(new TaskListener() {

				@Override
				public void taskFinished(Task task) {
					if (task.isFinished()) {
						requestAttention(true);
					}
				}
			});
		} else {
			System.out.println("Chart already created!");
		}
	}

	private void setChart(Chart chart) {
		this.chart = chart;
	}

	private void setCameraThreadController(CameraThreadController controller) {
		this.ctc = controller;
	}

	static class WorkaroundNewtCameraMouseController extends NewtCameraMouseController {

		public WorkaroundNewtCameraMouseController(Chart chart) {
			super(chart);
		}

		/**
		 * Compute zoom
		 */
		@Override
		public void mouseWheelMoved(MouseEvent e) {
			stopThreadController();
			zoomZ(1 + (e.getRotation()[1] * e.getRotationScale() / 10.0f));
		}

		@Override
		public boolean handleSlaveThread(MouseEvent e) {
			if (e.getClickCount() == 2) {
				if (threadController != null) {
					threadController.start();
					return true;
				}
			}
			if (threadController != null) {
				threadController.stop();
			}
			return false;
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
        toggleAnimation = new javax.swing.JToggleButton();

        setMinimumSize(new java.awt.Dimension(320, 240));
        setPreferredSize(new java.awt.Dimension(640, 480));
        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        org.openide.awt.Mnemonics.setLocalizedText(toggleAnimation, org.openide.util.NbBundle.getMessage(Jzy3DDemoTopComponent.class, "Jzy3DDemoTopComponent.toggleAnimation.text")); // NOI18N
        toggleAnimation.setFocusable(false);
        toggleAnimation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleAnimation.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toggleAnimation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleAnimationActionPerformed(evt);
            }
        });
        jToolBar1.add(toggleAnimation);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void toggleAnimationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleAnimationActionPerformed
		if (ctc != null) {
			if (toggleAnimation.isSelected()) {
				ctc.start();
			} else {
				ctc.stop();
			}
		}
    }//GEN-LAST:event_toggleAnimationActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToggleButton toggleAnimation;
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
