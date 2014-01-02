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

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays a JOGL2 demo.
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
	private NewtCanvasAWT canvas;

	public Jogl2DemoTopComponent() {
		glCaps = new GLCapabilities(null);
		initComponents();
		setName(Bundle.CTL_Jogl2DemoTopComponent());
		setToolTipText(Bundle.HINT_Jogl2DemoTopComponent());
	}

	@Override
	protected void componentOpened() {
		super.componentOpened();
		canvas = makeWindow("TestWindow", glCaps);
		add(canvas, BorderLayout.CENTER);
	}

	@Override
	protected void componentClosed() {
		super.componentClosed();
		remove(canvas);
		//dispose of canvas and native resources
		canvas.destroy();
	}

	@Override
	protected void componentActivated() {
		super.componentActivated();
		if (canvas != null) {
			canvas.setSize(getSize());
			//need to update complete component tree
			invalidate();
			getTopLevelAncestor().invalidate();
			getTopLevelAncestor().revalidate();
		}
	}

	private NewtCanvasAWT makeWindow(
		final String name, final GLCapabilities caps) {
		final GLWindow window = GLWindow.create(caps);
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
				quad_x = me.getX();
				quad_y = window.getHeight() - me.getY();
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

        setMinimumSize(new java.awt.Dimension(320, 240));
        setPreferredSize(new java.awt.Dimension(640, 480));
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
