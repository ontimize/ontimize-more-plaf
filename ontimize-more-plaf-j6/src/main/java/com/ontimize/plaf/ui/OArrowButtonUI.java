package com.ontimize.plaf.ui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.ontimize.plaf.OntimizeContext;
import com.ontimize.plaf.component.OntimizeArrowButton;

public class OArrowButtonUI extends OButtonUI{

    /**
     * @see com.ontimize.plaf.ui.OButtonUI#installDefaults(javax.swing.AbstractButton)
     */
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        updateStyle(b);
    }

    /**
     * @see com.ontimize.plaf.ui.OButtonUI#paint(com.ontimize.plaf.OntimizeContext,
     *      java.awt.Graphics)
     */
    protected void paint(OntimizeContext context, Graphics g) {
        OntimizeArrowButton button = (OntimizeArrowButton) context.getComponent();

        context.getPainter().paintArrowButtonForeground(context, g, 0, 0, button.getWidth(), button.getHeight(),
                                                        button.getDirection());
    }

    /**
     * Paint the arrow background.
     *
     * @param context the SynthContext.
     * @param g       the Graphics context.
     * @param c       the arrow component.
     */
    @SuppressWarnings("all")
    void paintBackground(OntimizeContext context, Graphics g, JComponent c) {
        context.getPainter().paintArrowButtonBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
    }

    /**
     * Paint the arrow border.
     *
     * @param context the SynthContext.
     * @param g       the Graphics context.
     * @param x       the x coordinate corresponding to the upper-left
     *                corner to paint.
     * @param y       the y coordinate corresponding to the upper-left
     *                corner to paint.
     * @param w       the width to paint.
     * @param h       the height to paint.
     */
    @SuppressWarnings("unused")
    public void paintBorder(OntimizeContext context, Graphics g, int x, int y, int w, int h) {
        context.getPainter().paintArrowButtonBorder(context, g, x, y, w, h);
    }

    /**
     * Get the minimum size for the arrow.
     *
     * @return the minimum size.
     */
    @SuppressWarnings("unused")
    public Dimension getMinimumSize() {
        return new Dimension(5, 5);
    }

    /**
     * Get the maximum size for the arrow.
     *
     * @return the maximum size.
     */
    @SuppressWarnings("unused")
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * @see com.ontimize.plaf.ui.OButtonUI#getPreferredSize(javax.swing.JComponent)
     */
    public Dimension getPreferredSize(JComponent c) {
    	OntimizeContext context = getContext(c);
        Dimension       dim     = null;

        if (context.getComponent().getName() == "ScrollBar.button") {

            // ScrollBar arrow buttons can be non-square when
            // the ScrollBar.squareButtons property is set to FALSE
            // and the ScrollBar.buttonSize property is non-null
            dim = (Dimension) context.getStyle().get(context, "ScrollBar.buttonSize");
        }

        if (dim == null) {

            // For all other cases (including Spinner, ComboBox), we will
            // fall back on the single ArrowButton.size value to create
            // a square return value
            int size = context.getStyle().getInt(context, "ArrowButton.size", 16);

            dim = new Dimension(size, size);
        }

        // handle scaling for sizeVarients for special case components. The
        // key "JComponent.sizeVariant" scales for large/small/mini
        // components are based on Apples LAF
        JComponent parent = (JComponent) context.getComponent().getParent();

        if (parent != null && !(parent instanceof JComboBox)) {
            String scaleKey = (String) parent.getClientProperty("JComponent.sizeVariant");

            if (scaleKey != null) {

                if ("large".equals(scaleKey)) {
                    dim = new Dimension((int) (dim.width * 1.15), (int) (dim.height * 1.15));
                } else if ("small".equals(scaleKey)) {
                    dim = new Dimension((int) (dim.width * 0.857), (int) (dim.height * 0.857));
                } else if ("mini".equals(scaleKey)) {
                    dim = new Dimension((int) (dim.width * 0.714), (int) (dim.height * 0.714));
                }
            }
        }

        context.dispose();

        return dim;
    }


}
