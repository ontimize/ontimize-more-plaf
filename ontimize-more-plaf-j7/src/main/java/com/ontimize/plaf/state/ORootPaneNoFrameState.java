package com.ontimize.plaf.state;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JRootPane;
import javax.swing.plaf.nimbus.State;

/**
 * Does root pane have a frame?
 */
public class ORootPaneNoFrameState extends State {

    /**
     * Creates a new RootPaneNoFrameState object.
     */
    public ORootPaneNoFrameState() {
        super("Frame");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        Component parent = c.getParent();

        if (true)
            return ((JRootPane) c).getWindowDecorationStyle() == JRootPane.NONE;

        if (parent instanceof JFrame)
            return true;

        if (parent instanceof JInternalFrame)
            return true;

        if (parent instanceof JDialog)
            return true;

        return false;
    }
}
