package com.ontimize.plaf.component;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.plaf.UIResource;

import com.ontimize.plaf.ui.OArrowButtonUI;

/**
 * JButton object that draws a scaled Arrow in one of the cardinal directions.
 *
 * <p>Based on SynthArrowButton by Scott Violet.</p>
 *
 * @see javax.swing.plaf.synth.SynthArrowButton
 */
public class OntimizeArrowButton extends JButton implements SwingConstants, UIResource {
	protected static final long serialVersionUID = 2673388860074501956L;
	protected int               direction;

    /**
     * Creates a new OntimizeArrowButton object.
     *
     * @param direction the direction to point the arrow. This will be one of
     *                  the SwingConstants {@code NORTH}, {@code WEST},
     *                  {@code SOUTH}, or {@code EAST}.
     */
    public OntimizeArrowButton(int direction) {
        super();
        super.setFocusable(false);
        setDirection(direction);
        setDefaultCapable(false);
    }

    /**
     * @see javax.swing.JButton#getUIClassID()
     */
    public String getUIClassID() {
        return "ArrowButtonUI";
    }

    /**
     * @see javax.swing.JButton#updateUI()
     */
    public void updateUI() {
//    	setUI((ButtonUI)UIManager.getUI(this));
      setUI(new OArrowButtonUI());
    }

    /**
     * Set the arrow's direction.
     *
     * @param dir the direction to point the arrow. This will be one of the
     *            SwingConstants {@code NORTH}, {@code WEST}, {@code SOUTH}, or
     *            {@code EAST}.
     */
    public void setDirection(int dir) {
        direction = dir;
        putClientProperty("__arrow_direction__", new Integer(dir));
        repaint();
    }

    /**
     * Get the direction of the arrow.
     *
     * @return the direction the arrow points. This will be one of the
     *         SwingConstants {@code NORTH}, {@code WEST}, {@code SOUTH}, or
     *         {@code EAST}.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @see java.awt.Component#setFocusable(boolean)
     */
    public void setFocusable(boolean focusable) {
    }
    
}
