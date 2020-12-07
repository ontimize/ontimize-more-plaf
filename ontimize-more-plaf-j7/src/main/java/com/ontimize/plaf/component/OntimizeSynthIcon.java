package com.ontimize.plaf.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.Painter;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.SynthContext;


import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.OntimizeStyle;
import sun.swing.plaf.synth.SynthIcon;

/**
 * An icon that delegates to a painter.
 *
 * <p>
 * Based on NimbusIcon by Richard Bair. Reimplemented because too much is package local.
 * </p>
 *
 * @see com.sun.java.swing.plaf.nimbus.NimbusIcon
 */
public class OntimizeSynthIcon extends SynthIcon {

    protected int width;

    protected int height;

    protected String prefix;

    protected String key;

    /**
     * Creates a new OntimizeSynthIcon object.
     * @param prefix the prefix to use to get the painter.
     * @param key the key to use to get the painter.
     * @param w the icon width.
     * @param h the icon height.
     */
    public OntimizeSynthIcon(String prefix, String key, int w, int h) {
        this.width = w;
        this.height = h;
        this.prefix = prefix;
        this.key = key;
    }

    /**
     * Paints the icon at the specified location.
     * @param context Identifies hosting region, may be null.
     * @param g the Graphics context to paint with.
     * @param x x location to paint to.
     * @param y y location to paint to.
     * @param w Width of the region to paint to, may be 0.
     * @param h Height of the region to paint to, may be 0.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void paintIcon(SynthContext context, Graphics g, int x, int y, int w, int h) {
        Painter painter = null;

        if (context != null) {
            painter = (Painter) context.getStyle().get(context, key);
        }

        if (painter == null) {
            painter = (Painter) UIManager.get(prefix + "[Enabled]." + key);
        }

        if (painter == null) {
            painter = (Painter) UIManager.get(prefix + "." + key);
        }

        if (painter != null && context != null) {
            JComponent c = context.getComponent();
            boolean rotate = false;
            boolean flip = false;

            // translatex and translatey are additional translations that
            // must occur on the graphics context when rendering a toolbar
            // icon
            int translatex = 0;
            int translatey = 0;

            if (c instanceof JToolBar) {
                JToolBar toolbar = (JToolBar) c;

                rotate = toolbar.getOrientation() == JToolBar.VERTICAL;
                flip = !toolbar.getComponentOrientation().isLeftToRight();
                Object o = OntimizeLookAndFeel.resolveToolbarConstraint(toolbar);

                // we only do the +1 hack for UIResource borders, assuming
                // that the border is probably going to be our border
                if (toolbar.getBorder() instanceof UIResource) {

                    if (o == BorderLayout.SOUTH) {
                        translatey = 1;
                    } else if (o == BorderLayout.EAST) {
                        translatex = 1;
                    }
                }
            }

            if (g instanceof Graphics2D) {
                Graphics2D gfx = (Graphics2D) g;

                gfx.translate(x, y);
                gfx.translate(translatex, translatey);

                if (rotate) {
                    gfx.rotate(Math.toRadians(90));
                    gfx.translate(0, -w);
                    painter.paint(gfx, context.getComponent(), h, w);
                    gfx.translate(0, w);
                    gfx.rotate(Math.toRadians(-90));
                } else if (flip) {
                    gfx.scale(-1, 1);
                    gfx.translate(-w, 0);
                    painter.paint(gfx, context.getComponent(), w, h);
                    gfx.translate(w, 0);
                    gfx.scale(-1, 1);
                } else {
                    painter.paint(gfx, context.getComponent(), w, h);
                }

                gfx.translate(-translatex, -translatey);
                gfx.translate(-x, -y);
            } else {

                // use image if we are printing to a Java 1.1 PrintGraphics as
                // it is not a instance of Graphics2D
                BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D gfx = img.createGraphics();

                if (rotate) {
                    gfx.rotate(Math.toRadians(90));
                    gfx.translate(0, -w);
                    painter.paint(gfx, context.getComponent(), h, w);
                } else if (flip) {
                    gfx.scale(-1, 1);
                    gfx.translate(-w, 0);
                    painter.paint(gfx, context.getComponent(), w, h);
                } else {
                    painter.paint(gfx, context.getComponent(), w, h);
                }

                gfx.dispose();
                g.drawImage(img, x, y, null);
                img = null;
            }
        }
    }

    /**
     * Implements the standard Icon interface's paintIcon method as the standard synth stub passes null
     * for the context and this will cause us to not paint any thing, so we override here so that we can
     * paint the enabled state if no synth context is available
     * @param c the component to paint.
     * @param g the Graphics context to paint with.
     * @param x the x coordinate of the upper-left corner of the icon.
     * @param y the y coordinate of the upper-left corner of the icon.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Painter painter = (Painter) UIManager.get(prefix + "[Enabled]." + key);

        if (painter != null) {
            JComponent jc = (c instanceof JComponent) ? (JComponent) c : null;
            Graphics2D gfx = (Graphics2D) g;

            gfx.translate(x, y);
            painter.paint(gfx, jc, width, height);
            gfx.translate(-x, -y);
        }
    }

    /**
     * Returns the icon's width. This is a cover methods for <code>
     * getIconWidth(null)</code>.
     * @param context the SynthContext describing the component/region, the style, and the state.
     * @return an int specifying the fixed width of the icon.
     */
    @Override
    public int getIconWidth(SynthContext context) {
        if (context == null) {
            return width;
        }

        JComponent c = context.getComponent();

        if (c instanceof JToolBar && ((JToolBar) c).getOrientation() == JToolBar.VERTICAL) {

            // we only do the -1 hack for UIResource borders, assuming
            // that the border is probably going to be our border
            if (c.getBorder() instanceof UIResource) {
                return c.getWidth() - 1;
            } else {
                return c.getWidth();
            }
        } else {
            return scale(context, width);
        }
    }

    /**
     * Returns the icon's height. This is a cover method for <code>
     * getIconHeight(null)</code>.
     * @param context the SynthContext describing the component/region, the style, and the state.
     * @return an int specifying the fixed height of the icon.
     */
    @Override
    public int getIconHeight(SynthContext context) {
        if (context == null) {
            return height;
        }

        JComponent c = context.getComponent();

        if (c instanceof JToolBar) {
            JToolBar toolbar = (JToolBar) c;

            if (toolbar.getOrientation() == JToolBar.HORIZONTAL) {

                // we only do the -1 hack for UIResource borders, assuming
                // that the border is probably going to be our border
                if (toolbar.getBorder() instanceof UIResource) {
                    return c.getHeight() - 1;
                } else {
                    return c.getHeight();
                }
            } else {
                return scale(context, width);
            }
        } else {
            return scale(context, height);
        }
    }

    /**
     * Scale a size based on the "JComponent.sizeVariant" client property of the component that is using
     * this icon
     * @param context The synthContext to get the component from
     * @param size The size to scale
     * @return The scaled size or original if "JComponent.sizeVariant" is not set
     */
    protected int scale(SynthContext context, int size) {
        if (context == null || context.getComponent() == null) {
            return size;
        }

        // The key "JComponent.sizeVariant" is used to match Apple's LAF
        String scaleKey = (String) context.getComponent().getClientProperty("JComponent.sizeVariant");

        if (scaleKey != null) {

            if (OntimizeStyle.LARGE_KEY.equals(scaleKey)) {
                size *= OntimizeStyle.LARGE_SCALE;
            } else if (OntimizeStyle.SMALL_KEY.equals(scaleKey)) {
                size *= OntimizeStyle.SMALL_SCALE;
            } else if (OntimizeStyle.MINI_KEY.equals(scaleKey)) {

                // mini is not quite as small for icons as full mini is
                // just too tiny
                size *= OntimizeStyle.MINI_SCALE + 0.07;
            }
        }

        return size;
    }

}
