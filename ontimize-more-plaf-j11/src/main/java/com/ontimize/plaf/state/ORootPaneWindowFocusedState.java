package com.ontimize.plaf.state;

import java.awt.Component;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.plaf.nimbus.State;

/**
 * Is root pane's window in focused state?
 */
public class ORootPaneWindowFocusedState extends State {

    /**
     * Creates a new RootPaneWindowFocusedState object.
     */
    public ORootPaneWindowFocusedState() {
        super("WindowFocused");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        Component parent = c;

        while (parent.getParent() != null) {

            if (parent instanceof Window) {
                break;
            }

            parent = parent.getParent();
        }

        if (parent instanceof Window) {
            return ((Window) parent).isFocused();
        }

        // Default to true.
        return true;
    }

}
