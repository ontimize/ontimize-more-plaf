package com.ontimize.plaf.component;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JList;

/**
 * The interface which defines the methods required for the implementation of the popup portion of a
 * tabbed pane.
 * <p>
 */
public interface TabbedPaneTabPopup {

    /**
     * Shows the popup
     */
    public void show();

    /**
     * Hides the popup
     */
    public void hide();

    /**
     * Returns true if the popup is visible (currently being displayed).
     * @return <code>true</code> if the component is visible; <code>false</code> otherwise.
     */
    public boolean isVisible();

    /**
     * Returns the list that is being used to draw the tabs in the tabbed pane. This method is highly
     * implementation specific and should not be used for general list manipulation.
     */
    public JList getList();

    /**
     * Returns a mouse listener that will be added to the tabbed pane or null. If this method returns
     * null then it will not be added to the tabbed pane.
     * @return a <code>MouseListener</code> or null
     */
    public MouseListener getMouseListener();

    /**
     * Returns a mouse motion listener that will be added to the tabbed pane or null. If this method
     * returns null then it will not be added to the tabbed pane.
     * @return a <code>MouseMotionListener</code> or null
     */
    public MouseMotionListener getMouseMotionListener();

    /**
     * Called to inform the ComboPopup that the UI is uninstalling. If the ComboPopup added any
     * listeners in the component, it should remove them here.
     */
    public void uninstallingUI();

}
