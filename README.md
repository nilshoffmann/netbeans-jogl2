netbeans-jogl2
==============

This project is a NetBeans RCP project demonstrating how to wrap [JOGL 2](https://jogamp.org/jogl/www/) within a
component and how to set up and display a TopComponent using the NEWT windowing toolkit.

Additionally, it includes a recent version of the [jzy3d](http://www.jzy3d.org/) library based on JOGL 2, using
the NEWT canvas. 

The Module requires at least [NetBeans 7.4](http://www.netbeans.org). It may work with earlier versions.
It adds two additional menu entries under "Window->JOGL 2 Demo" and "Window->Jzy3D Demo".

We currently use JOGL 2.1.3.. Please note that Jzy3D 0.9.1, bundled in this module has been compiled against 
a previous RC version of JOGL. Therefor, please look at the implementation within Jzy3DDemoTopComponent for a 
workaround for a [known issue with that version](https://github.com/jzy3d/jzy3d-api/issues/33).