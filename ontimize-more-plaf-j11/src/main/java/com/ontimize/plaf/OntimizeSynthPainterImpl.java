package com.ontimize.plaf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;

import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.Painter;
import javax.swing.SwingConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OntimizeSynthPainterImpl.
 *
 * <p>
 * Based on Nimbus's SynthPainterImpl class by Richard Bair.
 * </p>
 * This class is very similar to SynthPainterImpl. It is override due to there are a lot of
 * variables and methods protected, that we need to be public.
 *
 * @see com.sun.java.swing.plaf.nimbus.SynthPainterImpl
 */
@SuppressWarnings("unchecked")
public class OntimizeSynthPainterImpl extends SynthPainter {

    private static final Logger logger = LoggerFactory.getLogger(OntimizeSynthPainterImpl.class);

    protected OntimizeStyle style;

    /**
     * Creates a new OntimizeSynthPainterImpl object.
     * @param style the SynthStyle object.
     */
    public OntimizeSynthPainterImpl(OntimizeStyle style) {
        this.style = style;
    }

    public void setStyle(OntimizeStyle style) {
        this.style = style;
    }

    /**
     * Paint the provided painter using the provided transform at the specified position and size.
     * Handles if g is a non 2D Graphics by painting via a BufferedImage.
     * @param p the painter to use.
     * @param ctx the SynthContext describing the component/region, the style, and the state.
     * @param g the Graphics context to paint with.
     * @param x the left-most portion of the object to paint.
     * @param y the upper-most portion of the object to paint.
     * @param w the width to paint.
     * @param h the height to paint.
     * @param transform the affine transform to apply, or {@code null} if none is to be applied.
     */
    protected void paint(Painter p, SynthContext ctx, Graphics g, int x, int y, int w, int h,
            AffineTransform transform) {
        if (p != null) {

            if (g instanceof Graphics2D) {
                Graphics2D gfx = (Graphics2D) g;

                if (transform != null) {
                    gfx.transform(transform);
                }

                gfx.translate(x, y);
                p.paint(gfx, ctx.getComponent(), w, h);
                gfx.translate(-x, -y);

                if (transform != null) {

                    try {
                        gfx.transform(transform.createInverse());
                    } catch (NoninvertibleTransformException e) {

                        // this should never happen as we are in control of all
                        // calls into this method and only ever pass in simple
                        // transforms of rotate, flip and translates
                        OntimizeSynthPainterImpl.logger.error("", e);
                    }
                }
            } else {

                // use image if we are printing to a Java 1.1 PrintGraphics as
                // it is not a instance of Graphics2D
                BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D gfx = img.createGraphics();

                if (transform != null) {
                    gfx.transform(transform);
                }

                p.paint(gfx, ctx.getComponent(), w, h);
                gfx.dispose();
                g.drawImage(img, x, y, null);
                img = null;
            }
        }
    }

    /**
     * Paint the object's background.
     * @param ctx the SynthContext.
     * @param g the Graphics context.
     * @param x the x location corresponding to the upper-left coordinate to paint.
     * @param y the y location corresponding to the upper left coordinate to paint.
     * @param w the width to paint.
     * @param h the height to paint.
     * @param transform the affine transform to apply, or {@code null} if none is to be applied.
     */
    protected void paintBackground(SynthContext ctx, Graphics g, int x, int y, int w, int h,
            AffineTransform transform) {
        // if the background color of the component is 100% transparent
        // then we should not paint any background graphics. This is a solution
        // for there being no way of turning off Nimbus background painting as
        // basic components are all non-opaque by default.
        Component c = ctx.getComponent();
        Color bg = (c != null) ? c.getBackground() : null;

        if ((bg == null) || (bg.getAlpha() > 0)) {
            Painter backgroundPainter = this.style.getBackgroundPainter(ctx);

            if (backgroundPainter != null) {
                this.paint(backgroundPainter, ctx, g, x, y, w, h, transform);
            }
        }
    }

    /**
     * Paint the object's foreground.
     * @param ctx the SynthContext.
     * @param g the Graphics context.
     * @param x the x location corresponding to the upper-left coordinate to paint.
     * @param y the y location corresponding to the upper left coordinate to paint.
     * @param w the width to paint.
     * @param h the height to paint.
     * @param transform the affine transform to apply, or {@code null} if none is to be applied.
     */
    protected void paintForeground(SynthContext ctx, Graphics g, int x, int y, int w, int h,
            AffineTransform transform) {
        Painter foregroundPainter = this.style.getForegroundPainter(ctx);

        if (foregroundPainter != null) {
            this.paint(foregroundPainter, ctx, g, x, y, w, h, transform);
        }
    }

    /**
     * Paint the object's border.
     * @param ctx the SynthContext.
     * @param g the Graphics context.
     * @param x the x location corresponding to the upper-left coordinate to paint.
     * @param y the y location corresponding to the upper left coordinate to paint.
     * @param w the width to paint.
     * @param h the height to paint.
     * @param transform the affine transform to apply, or {@code null} if none is to be applied.
     */
    protected void paintBorder(SynthContext ctx, Graphics g, int x, int y, int w, int h, AffineTransform transform) {
        Painter borderPainter = this.style.getBorderPainter(ctx);

        if (borderPainter != null) {
            this.paint(borderPainter, ctx, g, x, y, w, h, transform);
        }
    }

    /**
     * Paint the object's background.
     * @param ctx the SynthContext.
     * @param g the Graphics context.
     * @param x the x location corresponding to the upper-left coordinate to paint.
     * @param y the y location corresponding to the upper left coordinate to paint.
     * @param w the width to paint.
     * @param h the height to paint.
     * @param orientation the component's orientation, used to construct an affine transform.
     */
    protected void paintBackground(SynthContext ctx, Graphics g, int x, int y, int w, int h, int orientation) {
        Component c = ctx.getComponent();
        boolean ltr = c.getComponentOrientation().isLeftToRight();

        // Don't RTL flip JSpliders as they handle it internaly
        if (ctx.getComponent() instanceof JSlider) {
            ltr = true;
        }

        if ((orientation == SwingConstants.VERTICAL) && ltr) {
            AffineTransform transform = new AffineTransform();

            transform.scale(-1, 1);
            transform.rotate(Math.toRadians(90));
            this.paintBackground(ctx, g, y, x, h, w, transform);
        } else if (orientation == SwingConstants.VERTICAL) {
            AffineTransform transform = new AffineTransform();

            transform.rotate(Math.toRadians(90));
            transform.translate(0, -(x + w));
            this.paintBackground(ctx, g, y, x, h, w, transform);
        } else if ((orientation == SwingConstants.HORIZONTAL) && ltr) {
            this.paintBackground(ctx, g, x, y, w, h, null);
        } else {

            // horizontal and right-to-left orientation
            AffineTransform transform = new AffineTransform();

            transform.translate(x, y);
            transform.scale(-1, 1);
            transform.translate(-w, 0);
            this.paintBackground(ctx, g, 0, 0, w, h, transform);
        }
    }

    /**
     * Paint the object's border.
     * @param ctx the SynthContext.
     * @param g the Graphics context.
     * @param x the x location corresponding to the upper-left coordinate to paint.
     * @param y the y location corresponding to the upper left coordinate to paint.
     * @param w the width to paint.
     * @param h the height to paint.
     * @param orientation the component's orientation, used to construct an affine transform.
     */
    protected void paintBorder(SynthContext ctx, Graphics g, int x, int y, int w, int h, int orientation) {
        Component c = ctx.getComponent();
        boolean ltr = c.getComponentOrientation().isLeftToRight();

        if ((orientation == SwingConstants.VERTICAL) && ltr) {
            AffineTransform transform = new AffineTransform();

            transform.scale(-1, 1);
            transform.rotate(Math.toRadians(90));
            this.paintBorder(ctx, g, y, x, h, w, transform);
        } else if (orientation == SwingConstants.VERTICAL) {
            AffineTransform transform = new AffineTransform();

            transform.rotate(Math.toRadians(90));
            transform.translate(0, -(x + w));
            this.paintBorder(ctx, g, y, 0, h, w, transform);
        } else if ((orientation == SwingConstants.HORIZONTAL) && ltr) {
            this.paintBorder(ctx, g, x, y, w, h, null);
        } else {

            // horizontal and right-to-left orientation
            this.paintBorder(ctx, g, x, y, w, h, null);
        }
    }

    /**
     * Paint the object's foreground.
     * @param ctx the SynthContext.
     * @param g the Graphics context.
     * @param x the x location corresponding to the upper-left coordinate to paint.
     * @param y the y location corresponding to the upper left coordinate to paint.
     * @param w the width to paint.
     * @param h the height to paint.
     * @param orientation the component's orientation, used to construct an affine transform.
     */
    protected void paintForeground(SynthContext ctx, Graphics g, int x, int y, int w, int h, int orientation) {
        Component c = ctx.getComponent();
        boolean ltr = c.getComponentOrientation().isLeftToRight();

        if ((orientation == SwingConstants.VERTICAL) && ltr) {
            AffineTransform transform = new AffineTransform();

            transform.scale(-1, 1);
            transform.rotate(Math.toRadians(90));
            this.paintForeground(ctx, g, y, x, h, w, transform);
        } else if (orientation == SwingConstants.VERTICAL) {
            AffineTransform transform = new AffineTransform();

            transform.rotate(Math.toRadians(90));
            transform.translate(0, -(x + w));
            this.paintForeground(ctx, g, y, 0, h, w, transform);
        } else if ((orientation == SwingConstants.HORIZONTAL) && ltr) {
            this.paintForeground(ctx, g, x, y, w, h, null);
        } else {

            // horizontal and right-to-left orientation
            this.paintForeground(ctx, g, x, y, w, h, null);
        }
    }

    /**
     * Paints the background of an arrow button. Arrow buttons are created by some components, such as
     * <code>JScrollBar</code>.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintArrowButtonBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        if (context.getComponent().getComponentOrientation().isLeftToRight()) {
            this.paintBackground(context, g, x, y, w, h, null);
        } else {
            AffineTransform transform = new AffineTransform();

            transform.translate(x, y);
            transform.scale(-1, 1);
            transform.translate(-w, 0);
            this.paintBackground(context, g, 0, 0, w, h, transform);
        }
    }

    /**
     * Paints the border of an arrow button. Arrow buttons are created by some components, such as
     * <code>JScrollBar</code>.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintArrowButtonBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the foreground of an arrow button. This method is responsible for drawing a graphical
     * representation of a direction, typically an arrow. Arrow buttons are created by some components,
     * such as <code>
     * JScrollBar</code>
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param direction One of SwingConstants.NORTH, SwingConstants.SOUTH SwingConstants.EAST or
     *        SwingConstants.WEST
     */
    @Override
    public void paintArrowButtonForeground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int direction) {
        // assume that the painter is arranged with the arrow pointing... LEFT?
        String compName = context.getComponent().getName();
        boolean ltr = context.getComponent().getComponentOrientation().isLeftToRight();

        // The hard coding for spinners here needs to be replaced by a more
        // general method for disabling rotation
        if ("Spinner.nextButton".equals(compName) || "Spinner.previousButton".equals(compName)) {

            if (ltr) {
                this.paintForeground(context, g, x, y, w, h, null);
            } else {
                AffineTransform transform = new AffineTransform();

                transform.translate(w, 0);
                transform.scale(-1, 1);
                this.paintForeground(context, g, x, y, w, h, transform);
            }
        } else if (direction == SwingConstants.WEST) {
            this.paintForeground(context, g, x, y, w, h, null);
        } else if (direction == SwingConstants.NORTH) {

            if (ltr) {
                AffineTransform transform = new AffineTransform();

                transform.scale(-1, 1);
                transform.rotate(Math.toRadians(90));
                this.paintForeground(context, g, y, 0, h, w, transform);
            } else {
                AffineTransform transform = new AffineTransform();

                transform.rotate(Math.toRadians(90));
                transform.translate(0, -(x + w));
                this.paintForeground(context, g, y, 0, h, w, transform);
            }
        } else if (direction == SwingConstants.EAST) {
            AffineTransform transform = new AffineTransform();

            transform.translate(w, 0);
            transform.scale(-1, 1);
            this.paintForeground(context, g, x, y, w, h, transform);
        } else if (direction == SwingConstants.SOUTH) {

            if (ltr) {
                AffineTransform transform = new AffineTransform();

                transform.rotate(Math.toRadians(-90));
                transform.translate(-h, 0);
                this.paintForeground(context, g, y, x, h, w, transform);
            } else {
                AffineTransform transform = new AffineTransform();

                transform.scale(-1, 1);
                transform.rotate(Math.toRadians(-90));
                transform.translate(-(h + y), -(w + x));
                this.paintForeground(context, g, y, x, h, w, transform);
            }
        }
    }

    /**
     * Paints the foreground of a search field button. This method is responsible for drawing a
     * graphical representation of a find or cancel button.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    public void paintSearchButtonForeground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintForeground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a button.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintButtonBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a button.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintButtonBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a check box menu item.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintCheckBoxMenuItemBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a check box menu item.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintCheckBoxMenuItemBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a check box.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintCheckBoxBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a check box.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintCheckBoxBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a color chooser.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintColorChooserBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a color chooser.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintColorChooserBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a combo box.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintComboBoxBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        if (context.getComponent().getComponentOrientation().isLeftToRight()) {
            this.paintBackground(context, g, x, y, w, h, null);
        } else {
            AffineTransform transform = new AffineTransform();

            transform.translate(x, y);
            transform.scale(-1, 1);
            transform.translate(-w, 0);
            this.paintBackground(context, g, 0, 0, w, h, transform);
        }
    }

    /**
     * Paints the border of a combo box.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintComboBoxBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a desktop icon.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintDesktopIconBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a desktop icon.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintDesktopIconBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a desktop pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintDesktopPaneBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a desktop pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintDesktopPaneBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of an editor pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintEditorPaneBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of an editor pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintEditorPaneBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a file chooser.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintFileChooserBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a file chooser.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintFileChooserBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a formatted text field.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintFormattedTextFieldBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        if (context.getComponent().getComponentOrientation().isLeftToRight()) {
            this.paintBackground(context, g, x, y, w, h, null);
        } else {
            AffineTransform transform = new AffineTransform();

            transform.translate(x, y);
            transform.scale(-1, 1);
            transform.translate(-w, 0);
            this.paintBackground(context, g, 0, 0, w, h, transform);
        }
    }

    /**
     * Paints the border of a formatted text field.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintFormattedTextFieldBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        if (context.getComponent().getComponentOrientation().isLeftToRight()) {
            this.paintBorder(context, g, x, y, w, h, null);
        } else {
            AffineTransform transform = new AffineTransform();

            transform.translate(x, y);
            transform.scale(-1, 1);
            transform.translate(-w, 0);
            this.paintBorder(context, g, 0, 0, w, h, transform);
        }
    }

    /**
     * Paints the background of an internal frame title pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintInternalFrameTitlePaneBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of an internal frame title pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintInternalFrameTitlePaneBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of an internal frame.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintInternalFrameBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of an internal frame.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintInternalFrameBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a label.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintLabelBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a label.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintLabelBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a list.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintListBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a list.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintListBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a menu bar.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintMenuBarBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a menu bar.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintMenuBarBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a menu item.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintMenuItemBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a menu item.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintMenuItemBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a menu.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintMenuBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a menu.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintMenuBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of an option pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintOptionPaneBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of an option pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintOptionPaneBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a panel.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintPanelBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a panel.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintPanelBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a password field.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintPasswordFieldBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a password field.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintPasswordFieldBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a popup menu.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintPopupMenuBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a popup menu.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintPopupMenuBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a progress bar.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintProgressBarBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a progress bar. This implementation invokes the method of the same name
     * without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation one of <code>JProgressBar.HORIZONTAL</code> or <code>
     *                    JProgressBar.VERTICAL</code>
     */
    @Override
    public void paintProgressBarBackground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBackground(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the border of a progress bar.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintProgressBarBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a progress bar. This implementation invokes the method of the same name
     * without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation one of <code>JProgressBar.HORIZONTAL</code> or <code>
     *                    JProgressBar.VERTICAL</code>
     */
    @Override
    public void paintProgressBarBorder(SynthContext context, Graphics g, int x, int y, int w, int h, int orientation) {
        this.paintBorder(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the foreground of a progress bar. is responsible for providing an indication of the
     * progress of the progress bar.
     * @param ctx SynthContext identifying the <code>JComponent</code> and <code>Region</code> to paint
     *        to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation one of <code>JProgressBar.HORIZONTAL</code> or <code>
     *                    JProgressBar.VERTICAL</code>
     */
    @Override
    public void paintProgressBarForeground(SynthContext ctx, Graphics g, int x, int y, int w, int h, int orientation) {
        Component c = ctx.getComponent();
        boolean ltr = c.getComponentOrientation().isLeftToRight();

        if (orientation == SwingConstants.VERTICAL) {

            // We always draw from bottom to top, regardless of the
            // left-to-right orientation.
            AffineTransform transform = new AffineTransform();

            transform.translate(x, y);
            transform.rotate(Math.toRadians(-90));
            this.paintForeground(ctx, g, 0, 0, h, w, transform);
        } else if ((orientation == SwingConstants.HORIZONTAL) && ltr) {
            this.paintForeground(ctx, g, x, y, w, h, null);
        } else {

            // Horizontal and right-to-left orientation.
            // Flip the drawing so that any border on the progress bar is
            // painted at the left end.
            AffineTransform transform = new AffineTransform();

            transform.translate(x + w, 0);
            transform.scale(-1, 1);
            this.paintForeground(ctx, g, 0, y, w, h, transform);
        }
    }

    /**
     * Paints the background of a radio button menu item.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintRadioButtonMenuItemBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a radio button menu item.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintRadioButtonMenuItemBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a radio button.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintRadioButtonBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a radio button.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintRadioButtonBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a root pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintRootPaneBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a root pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintRootPaneBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a scrollbar.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintScrollBarBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a scrollbar. This implementation invokes the method of the same name
     * without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation Orientation of the JScrollBar, one of <code>
     *                    JScrollBar.HORIZONTAL</code> or <code>
     *                    JScrollBar.VERTICAL</code>
     */
    @Override
    public void paintScrollBarBackground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBackground(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the border of a scrollbar.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintScrollBarBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a scrollbar. This implementation invokes the method of the same name without
     * the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation Orientation of the JScrollBar, one of <code>
     *                    JScrollBar.HORIZONTAL</code> or <code>
     *                    JScrollBar.VERTICAL</code>
     */
    @Override
    public void paintScrollBarBorder(SynthContext context, Graphics g, int x, int y, int w, int h, int orientation) {
        this.paintBorder(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the background of the thumb of a scrollbar. The thumb provides a graphical indication as
     * to how much of the Component is visible in a <code>JScrollPane</code>.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation Orientation of the JScrollBar, one of <code>
     *                    JScrollBar.HORIZONTAL</code> or <code>
     *                    JScrollBar.VERTICAL</code>
     */
    @Override
    public void paintScrollBarThumbBackground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBackground(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the border of the thumb of a scrollbar. The thumb provides a graphical indication as to
     * how much of the Component is visible in a <code>JScrollPane</code>.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation Orientation of the JScrollBar, one of <code>
     *                    JScrollBar.HORIZONTAL</code> or <code>
     *                    JScrollBar.VERTICAL</code>
     */
    @Override
    public void paintScrollBarThumbBorder(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBorder(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the background of the track of a scrollbar. The track contains the thumb.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintScrollBarTrackBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of the track of a scrollbar. The track contains the thumb. This
     * implementation invokes the method of the same name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation Orientation of the JScrollBar, one of <code>
     *                    JScrollBar.HORIZONTAL</code> or <code>
     *                    JScrollBar.VERTICAL</code>
     */
    @Override
    public void paintScrollBarTrackBackground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBackground(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the border of the track of a scrollbar. The track contains the thumb.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintScrollBarTrackBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of the track of a scrollbar. The track contains the thumb. This implementation
     * invokes the method of the same name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation Orientation of the JScrollBar, one of <code>
     *                    JScrollBar.HORIZONTAL</code> or <code>
     *                    JScrollBar.VERTICAL</code>
     */
    @Override
    public void paintScrollBarTrackBorder(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBorder(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the background of a scroll pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintScrollPaneBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a scroll pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintScrollPaneBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a separator.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintSeparatorBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a separator. This implementation invokes the method of the same name
     * without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JSeparator.HORIZONTAL</code> or <code>
     *                    JSeparator.VERTICAL</code>
     */
    @Override
    public void paintSeparatorBackground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBackground(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the border of a separator.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintSeparatorBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a separator. This implementation invokes the method of the same name without
     * the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JSeparator.HORIZONTAL</code> or <code>
     *                    JSeparator.VERTICAL</code>
     */
    @Override
    public void paintSeparatorBorder(SynthContext context, Graphics g, int x, int y, int w, int h, int orientation) {
        this.paintBorder(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the foreground of a separator.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JSeparator.HORIZONTAL</code> or <code>
     *                    JSeparator.VERTICAL</code>
     */
    @Override
    public void paintSeparatorForeground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintForeground(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the background of a slider.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintSliderBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a slider. This implementation invokes the method of the same name
     * without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JSlider.HORIZONTAL</code> or <code>
     *                    JSlider.VERTICAL</code>
     */
    @Override
    public void paintSliderBackground(SynthContext context, Graphics g, int x, int y, int w, int h, int orientation) {
        this.paintBackground(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the border of a slider.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintSliderBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a slider. This implementation invokes the method of the same name without
     * the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JSlider.HORIZONTAL</code> or <code>
     *                    JSlider.VERTICAL</code>
     */
    @Override
    public void paintSliderBorder(SynthContext context, Graphics g, int x, int y, int w, int h, int orientation) {
        this.paintBorder(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the background of the thumb of a slider.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JSlider.HORIZONTAL</code> or <code>
     *                    JSlider.VERTICAL</code>
     */
    @Override
    public void paintSliderThumbBackground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        if (context.getComponent().getClientProperty("Slider.paintThumbArrowShape") == Boolean.TRUE) {

            if (orientation == SwingConstants.HORIZONTAL) {
                orientation = SwingConstants.VERTICAL;
            } else {
                orientation = SwingConstants.HORIZONTAL;
            }

            this.paintBackground(context, g, x, y, w, h, orientation);
        } else {
            this.paintBackground(context, g, x, y, w, h, orientation);
        }
    }

    /**
     * Paints the border of the thumb of a slider.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JSlider.HORIZONTAL</code> or <code>
     *                    JSlider.VERTICAL</code>
     */
    @Override
    public void paintSliderThumbBorder(SynthContext context, Graphics g, int x, int y, int w, int h, int orientation) {
        this.paintBorder(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the background of the track of a slider.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintSliderTrackBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of the track of a slider. This implementation invokes the method of the
     * same name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JSlider.HORIZONTAL</code> or <code>
     *                    JSlider.VERTICAL</code>
     */
    @Override
    public void paintSliderTrackBackground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBackground(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the border of the track of a slider.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintSliderTrackBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of the track of a slider. This implementation invokes the method of the same
     * name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JSlider.HORIZONTAL</code> or <code>
     *                    JSlider.VERTICAL</code>
     */
    @Override
    public void paintSliderTrackBorder(SynthContext context, Graphics g, int x, int y, int w, int h, int orientation) {
        this.paintBorder(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the background of a spinner.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintSpinnerBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a spinner.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintSpinnerBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of the divider of a split pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintSplitPaneDividerBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of the divider of a split pane. This implementation invokes the method of
     * the same name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JSplitPane.HORIZONTAL_SPLIT</code> or
     *        <code>JSplitPane.VERTICAL_SPLIT</code>
     */
    @Override
    public void paintSplitPaneDividerBackground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
            AffineTransform transform = new AffineTransform();

            transform.scale(-1, 1);
            transform.rotate(Math.toRadians(90));
            this.paintBackground(context, g, y, x, h, w, transform);
        } else {
            this.paintBackground(context, g, x, y, w, h, null);
        }
    }

    /**
     * Paints the foreground of the divider of a split pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JSplitPane.HORIZONTAL_SPLIT</code> or
     *        <code>JSplitPane.VERTICAL_SPLIT</code>
     */
    @Override
    public void paintSplitPaneDividerForeground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintForeground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the divider, when the user is dragging the divider, of a split pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JSplitPane.HORIZONTAL_SPLIT</code> or
     *        <code>JSplitPane.VERTICAL_SPLIT</code>
     */
    @Override
    public void paintSplitPaneDragDivider(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a split pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintSplitPaneBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a split pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintSplitPaneBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a tabbed pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTabbedPaneBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a tabbed pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JTabbedPane.TOP</code>, <code>
     *                    JTabbedPane.LEFT</code>, <code>
     *                    JTabbedPane.BOTTOM</code> , or <code>
     *                    JTabbedPane.RIGHT</code>
     */
    public void paintTabbedPaneBorder(SynthContext context, Graphics g, int x, int y, int w, int h, int orientation) {

        if (orientation == SwingConstants.LEFT) {
            AffineTransform transform = new AffineTransform();

            transform.scale(-1, 1);
            transform.rotate(Math.toRadians(90));
            this.paintBorder(context, g, x, y, w, h, transform);
        } else if (orientation == SwingConstants.RIGHT) {
            AffineTransform transform = new AffineTransform();

            transform.rotate(Math.toRadians(90));
            transform.translate(0, -(x + w));
            this.paintBorder(context, g, x, y, w, h, transform);
        } else if (orientation == SwingConstants.BOTTOM) {
            AffineTransform transform = new AffineTransform();

            transform.translate(x, y);
            transform.scale(1, -1);
            transform.translate(0, -h);
            this.paintBorder(context, g, x, y, w, h, transform);
        } else {
            this.paintBorder(context, g, x, y, w, h, null);
        }

    }

    /**
     * Paints the background of the area behind the tabs of a tabbed pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTabbedPaneTabAreaBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of the area behind the tabs of a tabbed pane. This implementation invokes
     * the method of the same name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JTabbedPane.TOP</code>, <code>
     *                    JTabbedPane.LEFT</code>, <code>
     *                    JTabbedPane.BOTTOM</code> , or <code>
     *                    JTabbedPane.RIGHT</code>
     */
    @Override
    public void paintTabbedPaneTabAreaBackground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        if (orientation == SwingConstants.LEFT) {
            AffineTransform transform = new AffineTransform();

            transform.scale(-1, 1);
            transform.rotate(Math.toRadians(90));
            this.paintBackground(context, g, y, x, h, w, transform);
        } else if (orientation == SwingConstants.RIGHT) {
            AffineTransform transform = new AffineTransform();

            transform.rotate(Math.toRadians(90));
            transform.translate(0, -(x + w));
            this.paintBackground(context, g, y, 0, h, w, transform);
        } else if (orientation == SwingConstants.BOTTOM) {
            AffineTransform transform = new AffineTransform();

            transform.translate(x, y);
            this.paintBackground(context, g, 0, 0, w, h, transform);
        } else {
            this.paintBackground(context, g, x, y, w, h, null);
        }
    }

    /**
     * Paints the border of the area behind the tabs of a tabbed pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTabbedPaneTabAreaBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of the area behind the tabs of a tabbed pane. This implementation invokes the
     * method of the same name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JTabbedPane.TOP</code>, <code>
     *                    JTabbedPane.LEFT</code>, <code>
     *                    JTabbedPane.BOTTOM</code> , or <code>
     *                    JTabbedPane.RIGHT</code>
     */
    @Override
    public void paintTabbedPaneTabAreaBorder(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a tab of a tabbed pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param tabIndex Index of tab being painted.
     */
    @Override
    public void paintTabbedPaneTabBackground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int tabIndex) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a tab of a tabbed pane. This implementation invokes the method of the
     * same name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param tabIndex Index of tab being painted.
     * @param orientation One of <code>JTabbedPane.TOP</code>, <code>
     *                    JTabbedPane.LEFT</code>, <code>
     *                    JTabbedPane.BOTTOM</code> , or <code>
     *                    JTabbedPane.RIGHT</code>
     */
    @Override
    public void paintTabbedPaneTabBackground(SynthContext context, Graphics g, int x, int y, int w, int h, int tabIndex,
            int orientation) {
        if (orientation == SwingConstants.LEFT) {
            AffineTransform transform = new AffineTransform();

            transform.scale(-1, 1);
            transform.rotate(Math.toRadians(90));
            this.paintBackground(context, g, y, x, h, w, transform);
        } else if (orientation == SwingConstants.RIGHT) {
            AffineTransform transform = new AffineTransform();

            transform.rotate(Math.toRadians(90));
            transform.translate(0, -(x + w));
            this.paintBackground(context, g, y, 0, h, w, transform);
        } else if (orientation == SwingConstants.BOTTOM) {
            AffineTransform transform = new AffineTransform();

            transform.translate(x, y);
            transform.scale(1, -1);
            transform.translate(0, -h);
            this.paintBackground(context, g, 0, 0, w, h, transform);
        } else {
            this.paintBackground(context, g, x, y, w, h, null);
        }
    }

    /**
     * Paints the border of a tab of a tabbed pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param tabIndex Index of tab being painted.
     */
    @Override
    public void paintTabbedPaneTabBorder(SynthContext context, Graphics g, int x, int y, int w, int h, int tabIndex) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a tab of a tabbed pane. This implementation invokes the method of the same
     * name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param tabIndex Index of tab being painted.
     * @param orientation One of <code>JTabbedPane.TOP</code>, <code>
     *                    JTabbedPane.LEFT</code>, <code>
     *                    JTabbedPane.BOTTOM</code> , or <code>
     *                    JTabbedPane.RIGHT</code>
     */
    @Override
    public void paintTabbedPaneTabBorder(SynthContext context, Graphics g, int x, int y, int w, int h, int tabIndex,
            int orientation) {

        // if (orientation == JTabbedPane.LEFT) {
        // AffineTransform transform = new AffineTransform();
        //
        // transform.scale(-1, 1);
        // transform.rotate(Math.toRadians(90));
        // // paintBackground(context, g, y, x, h, w, transform);
        // paintBorder(context, g, y, x, h, w, transform);
        // } else if (orientation == JTabbedPane.RIGHT) {
        // AffineTransform transform = new AffineTransform();
        //
        // transform.rotate(Math.toRadians(90));
        // transform.translate(0, -(x + w));
        // // paintBackground(context, g, y, 0, h, w, transform);
        // paintBorder(context, g, y, 0, h, w, transform);
        // } else if (orientation == JTabbedPane.BOTTOM) {
        // AffineTransform transform = new AffineTransform();
        //
        // transform.translate(x, y);
        // transform.scale(1, -1);
        // transform.translate(0, -h);
        // // paintBackground(context, g, 0, 0, w, h, transform);
        // paintBorder(context, g, 0, 0, w, h, transform);
        // } else {
        // paintBorder(context, g, x, y, w, h, null);
        // }

        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of the area that contains the content of the selected tab of a tabbed pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTabbedPaneContentBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of the area that contains the content of the selected tab of a tabbed pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTabbedPaneContentBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of the header of a table.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTableHeaderBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of the header of a table.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTableHeaderBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a table.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTableBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a table.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTableBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a text area.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTextAreaBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a text area.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTextAreaBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a text pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTextPaneBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a text pane.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTextPaneBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a text field.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTextFieldBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        if (context.getComponent().getComponentOrientation().isLeftToRight()) {
            this.paintBackground(context, g, x, y, w, h, null);
        } else {
            AffineTransform transform = new AffineTransform();

            transform.translate(x, y);
            transform.scale(-1, 1);
            transform.translate(-w, 0);
            this.paintBackground(context, g, 0, 0, w, h, transform);
        }
    }

    /**
     * Paints the border of a text field.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTextFieldBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        if (context.getComponent().getComponentOrientation().isLeftToRight()) {
            this.paintBorder(context, g, x, y, w, h, null);
        } else {
            AffineTransform transform = new AffineTransform();

            transform.translate(x, y);
            transform.scale(-1, 1);
            transform.translate(-w, 0);
            this.paintBorder(context, g, 0, 0, w, h, transform);
        }
    }

    /**
     * Paints the background of a toggle button.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintToggleButtonBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a toggle button.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintToggleButtonBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a tool bar.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintToolBarBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a tool bar. This implementation invokes the method of the same name
     * without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JToolBar.HORIZONTAL</code> or <code>
     *                    JToolBar.VERTICAL</code>
     */
    @Override
    public void paintToolBarBackground(SynthContext context, Graphics g, int x, int y, int w, int h, int orientation) {
        this.paintBackground(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the border of a tool bar.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintToolBarBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a tool bar. This implementation invokes the method of the same name without
     * the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JToolBar.HORIZONTAL</code> or <code>
     *                    JToolBar.VERTICAL</code>
     */
    @Override
    public void paintToolBarBorder(SynthContext context, Graphics g, int x, int y, int w, int h, int orientation) {
        this.paintBorder(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the background of the tool bar's content area.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintToolBarContentBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of the tool bar's content area. This implementation invokes the method of
     * the same name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JToolBar.HORIZONTAL</code> or <code>
     *                    JToolBar.VERTICAL</code>
     */
    @Override
    public void paintToolBarContentBackground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBackground(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the border of the content area of a tool bar.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintToolBarContentBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of the content area of a tool bar. This implementation invokes the method of
     * the same name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JToolBar.HORIZONTAL</code> or <code>
     *                    JToolBar.VERTICAL</code>
     */
    @Override
    public void paintToolBarContentBorder(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBorder(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the background of the window containing the tool bar when it has been detached from its
     * primary frame.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintToolBarDragWindowBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of the window containing the tool bar when it has been detached from its
     * primary frame. This implementation invokes the method of the same name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JToolBar.HORIZONTAL</code> or <code>
     *                    JToolBar.VERTICAL</code>
     */
    @Override
    public void paintToolBarDragWindowBackground(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBackground(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the border of the window containing the tool bar when it has been detached from it's
     * primary frame.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintToolBarDragWindowBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of the window containing the tool bar when it has been detached from it's
     * primary frame. This implementation invokes the method of the same name without the orientation.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     * @param orientation One of <code>JToolBar.HORIZONTAL</code> or <code>
     *                    JToolBar.VERTICAL</code>
     */
    @Override
    public void paintToolBarDragWindowBorder(SynthContext context, Graphics g, int x, int y, int w, int h,
            int orientation) {
        this.paintBorder(context, g, x, y, w, h, orientation);
    }

    /**
     * Paints the background of a tool tip.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintToolTipBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a tool tip.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintToolTipBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of a tree.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTreeBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a tree.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTreeBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the background of the row containing a cell in a tree.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTreeCellBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of the row containing a cell in a tree.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTreeCellBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

    /**
     * Paints the focus indicator for a cell in a tree when it has focus.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintTreeCellFocus(SynthContext context, Graphics g, int x, int y, int w, int h) {
        // TODO Paint tree cell focus.
    }

    /**
     * Paints the background of the viewport.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintViewportBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBackground(context, g, x, y, w, h, null);
    }

    /**
     * Paints the border of a viewport.
     * @param context SynthContext identifying the <code>JComponent</code> and <code>Region</code> to
     *        paint to
     * @param g <code>Graphics</code> to paint to
     * @param x X coordinate of the area to paint to
     * @param y Y coordinate of the area to paint to
     * @param w Width of the area to paint to
     * @param h Height of the area to paint to
     */
    @Override
    public void paintViewportBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        this.paintBorder(context, g, x, y, w, h, null);
    }

}
