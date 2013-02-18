netbeans-jogl2
==============

This project is a NetBeans RCP project demonstrating how to wrap [JOGL 2](https://jogamp.org/jogl/www/) within a
component and how to set up and display a TopComponent using the NEWT windowing toolkit.

Additionally, it includes a recent version of the [jzy3d](http://www.jzy3d.org/) library based on JOGL 2, using
the NEWT canvas. 

The Module requires at least [NetBeans 7.2](http://www.netbeans.org). It may work with earlier versions.
It adds two additional menu entries under "Window->JOGL 2 Demo" and "Window->Jzy3D Demo".

Please bear in mind that the jzy3d support for NEWT is still experimental and may lead to 
random lockups due to interactions of the NetBeans docking framework, the event dispatch thread, and 
the way the NEWT canvas and jzy3d interact.