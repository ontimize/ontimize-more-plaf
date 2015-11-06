package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JToolBar;

import com.ontimize.plaf.state.OToolBarNorthState;
import com.ontimize.plaf.state.OToolBarSouthState;
import com.ontimize.plaf.state.State;


public class ORootPainter extends AbstractRegionPainter {

    protected static final int TITLE_BAR_HEIGHT = 25;

    protected static final State toolBarNorthState = new OToolBarNorthState();
    protected static final State toolBarSouthState = new OToolBarSouthState();

    /**
     * Control state.
     */
    public static final int BACKGROUND_ENABLED = 1;
	public static final int BACKGROUND_ENABLED_WINDOWFOCUSED = 2;
	public static final int BACKGROUND_ENABLED_NOFRAME = 3;

    protected Color frameBorderBase;// = decodeColor("frameBorderBase");

    protected Color frameInnerHighlightInactive;// = decodeColor("frameInnerHighlightInactive");
    protected Color frameInnerHighlightActive;//   = decodeColor("frameInnerHighlightActive");

    protected Color frameBaseActive;//   = decodeColor("frameBaseActive");
    protected Color frameBaseInactive;// = decodeColor("frameBaseInactive");

    protected Color frameBorderActive   = frameBorderBase;
    protected Color frameBorderInactive = frameBorderBase;

    protected Color frameTopActive;//        = deriveColor(frameBaseActive, 0.005208f, -0.080105f, 0.043137f, 0);
    protected Color frameUpperMidActive;//   = frameBaseActive;
    protected Color frameLowerMidActive;//   = frameBaseActive;
    protected Color frameBottomActive ;//    = deriveColor(frameBaseActive, 0f, 0.025723f, -0.015686f, 0);
    protected Color frameTopInactive ;//     = deriveColor(frameBaseInactive, 0f, 0f, 0.050980f, 0);
    protected Color frameUpperMidInactive;// = frameBaseInactive;
    protected Color frameLowerMidInactive;// = frameBaseInactive;
    protected Color frameBottomInactive ;//  = deriveColor(frameBaseInactive, 0f, 0f, -0.050980f, 0);

//    protected FourColors frameActive   = new FourColors(frameTopActive, frameUpperMidActive, frameLowerMidActive,
//                                                      frameBottomActive);
//    protected FourColors frameInactive = new FourColors(frameTopInactive, frameUpperMidInactive, frameLowerMidInactive,
//                                                      frameBottomInactive);

    protected int        state;
    protected PaintContext ctx;

    /**
     * Creates a new FrameAndRootPainter object.
     *
     * @param state the control state to paint.
     */
    public ORootPainter(int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx   = ctx;
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (state == BACKGROUND_ENABLED_NOFRAME) {
            return;
        }

//        Shape s = shapeGenerator.createRoundRectangle(0, 0, (width - 1), (height - 1), CornerSize.FRAME_BORDER,
//                                                      CornerStyle.ROUNDED, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.ROUNDED);
        RoundRectangle2D.Float s = new RoundRectangle2D.Float(0,0,width-1,height-1,12,12);

        g.setPaint(getFrameBorderPaint(s));
        g.draw(s);

        JMenuBar    mb     = null;
        Component[] cArray = null;

        if (c instanceof JInternalFrame) {
            JInternalFrame iframe = (JInternalFrame) c;

            mb     = iframe.getJMenuBar();
            cArray = iframe.getContentPane().getComponents();
        } else if (c instanceof JRootPane) {
            JRootPane root = (JRootPane) c;

            mb     = root.getJMenuBar();
            cArray = root.getContentPane().getComponents();
        }

        int topToolBarHeight    = 0;
        int bottomToolBarHeight = 0;

        if (cArray != null) {

            for (Component comp : cArray) {

                if (comp instanceof JToolBar) {

                    if (toolBarNorthState.isInState((JComponent) comp)) {
                        topToolBarHeight = comp.getHeight();
                    } else if (toolBarSouthState.isInState((JComponent) comp)) {
                        bottomToolBarHeight = comp.getHeight();
                    }
                }
            }
        }

        int titleHeight = TITLE_BAR_HEIGHT;

        if (mb != null && c.getClientProperty("Ontimize.JRootPane.MenuInTitle") == Boolean.TRUE) {
            titleHeight += mb.getHeight();
        }
        
        Rectangle2D titleBar = new Rectangle2D.Float(1, 1, width-2, titleHeight);
        g.setPaint(Color.red);
        g.fill(titleBar);

//        if (c.getClientProperty(OntimizeRootPaneUI.UNIFIED_TOOLBAR_LOOK) == Boolean.TRUE) {
//            // Draw background gradient.
//            s = shapeGenerator.createRoundRectangle(1, 1, width - 2, height - 2, CornerSize.FRAME_INNER_HIGHLIGHT,
//                                                    CornerStyle.ROUNDED, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.ROUNDED);
//            g.setPaint(getFrameInteriorPaint(s, titleHeight, topToolBarHeight, bottomToolBarHeight));
//            g.fill(s);
//        } else {
//            // Paint title bar.
//            s = shapeGenerator.createRoundRectangle(1, 1, width - 2, titleHeight, CornerSize.FRAME_INNER_HIGHLIGHT,
//                                                    CornerStyle.ROUNDED, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.ROUNDED);
//            g.setPaint(getTitleBarInteriorPaint(s, titleHeight));
//            g.fill(s);
//            // Paint contents.
//            s = shapeGenerator.createRoundRectangle(1, titleHeight, width - 2, height - titleHeight - 2, CornerSize.FRAME_INNER_HIGHLIGHT,
//                                                    CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.SQUARE);
//            g.setPaint(c.getBackground());
//            g.fill(s);
//            // Draw separator line.
//            g.setPaint(Color.BLACK);
//            g.drawLine(1, titleHeight, width - 2, titleHeight);
//        }
//
//        s = shapeGenerator.createRoundRectangle(1, 1, width - 3, height - 3, CornerSize.FRAME_INTERIOR);
//        g.setPaint(getFrameInnerHighlightPaint(s));
//        g.draw(s);
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * Get the paint for the border.
     *
     * @param  s the border shape.
     *
     * @return the paint.
     */
    public Paint getFrameBorderPaint(Shape s) {
        switch (state) {

        case BACKGROUND_ENABLED:
            return frameBorderInactive;

        case BACKGROUND_ENABLED_WINDOWFOCUSED:
            return frameBorderActive;
        }

        return null;
    }

    /**
     * Get the color set to paint the interior.
     *
     * @return the color set.
     */
//    protected FourColors getFrameInteriorColors() {
//        switch (state) {
//
//        case BACKGROUND_ENABLED:
//            return frameInactive;
//
//        case BACKGROUND_ENABLED_WINDOWFOCUSED:
//            return frameActive;
//        }
//
//        return null;
//    }

    /**
     * Get the paint for the frame interior.
     *
     * @param  s                   the frame interior shape.
     * @param  titleHeight         the height of the title portion.
     * @param  topToolBarHeight    the height of the top toolbar, or 0 if none.
     * @param  bottomToolBarHeight the height of the bottom toolbar, or 0 if
     *                             none.
     *
     * @return the paint.
     */
//    public Paint getFrameInteriorPaint(Shape s, int titleHeight, int topToolBarHeight, int bottomToolBarHeight) {
//        return createFrameGradient(s, titleHeight, topToolBarHeight, bottomToolBarHeight, getFrameInteriorColors());
//    }

    /**
     * Get the paint for the title bar.
     *
     * @param  s           the frame interior shape.
     * @param  titleHeight the height of the title portion.
     *
     * @return the paint.
     */
//    public Paint getTitleBarInteriorPaint(Shape s, int titleHeight) {
//        return createTitleBarGradient(s, titleHeight, getFrameInteriorColors());
//    }

    /**
     * Get the paint to paint the inner highlight with.
     *
     * @param  s the highlight shape.
     *
     * @return the paint.
     */
//    public Paint getFrameInnerHighlightPaint(Shape s) {
//        switch (state) {
//
//        case BACKGROUND_ENABLED:
//            return frameInnerHighlightInactive;
//
//        case BACKGROUND_ENABLED_WINDOWFOCUSED:
//            return frameInnerHighlightActive;
//        }
//
//        return null;
//    }

    /**
     * Create the gradient to paint the frame interior.
     *
     * @param  s                   the interior shape.
     * @param  titleHeight         the height of the title bar, or 0 if none.
     * @param  topToolBarHeight    the height of the top toolbar, or 0 if none.
     * @param  bottomToolBarHeight the height of the bottom toolbar, or 0 if
     *                             none.
     * @param  defColors           the color set to construct the gradient from.
     *
     * @return the gradient.
     */
//    protected Paint createFrameGradient(Shape s, int titleHeight, int topToolBarHeight, int bottomToolBarHeight, FourColors defColors) {
//        Rectangle2D bounds = s.getBounds2D();
//        float       x      = (float) bounds.getX();
//        float       y      = (float) bounds.getY();
//        float       w      = (float) bounds.getWidth();
//        float       h      = (float) bounds.getHeight();
//
//        float midX        = x + w / 2.0f;
//        float titleBottom = titleHeight / h;
//
//        if (titleBottom >= 1.0f) {
//            titleBottom = 1.0f - 0.00004f;
//        }
//
//        float[] midPoints = null;
//        Color[] colors    = null;
//
//        if (topToolBarHeight > 0 && bottomToolBarHeight > 0) {
//            float topToolBarBottom = (titleHeight + topToolBarHeight) / h;
//
//            if (topToolBarBottom >= 1.0f) {
//                topToolBarBottom = 1.0f - 0.00002f;
//            }
//
//            float bottomToolBarTop = (h - 2 - bottomToolBarHeight) / h;
//
//            if (bottomToolBarTop >= 1.0f) {
//                bottomToolBarTop = 1.0f - 0.00002f;
//            }
//
//            midPoints = new float[] { 0.0f, topToolBarBottom, bottomToolBarTop, 1.0f };
//            colors    = new Color[] { defColors.top, defColors.upperMid, defColors.lowerMid, defColors.bottom };
//        } else if (topToolBarHeight > 0) {
//            float toolBarBottom = (titleHeight + topToolBarHeight) / h;
//
//            if (toolBarBottom >= 1.0f) {
//                toolBarBottom = 1.0f - 0.00002f;
//            }
//
//            midPoints = new float[] { 0.0f, toolBarBottom, 1.0f };
//            colors    = new Color[] { defColors.top, defColors.upperMid, defColors.lowerMid };
//        } else if (bottomToolBarHeight > 0) {
//            float bottomToolBarTop = (h - 2 - bottomToolBarHeight) / h;
//
//            if (bottomToolBarTop >= 1.0f) {
//                bottomToolBarTop = 1.0f - 0.00002f;
//            }
//
//            midPoints = new float[] { 0.0f, titleBottom, bottomToolBarTop, 1.0f };
//            colors    = new Color[] { defColors.top, defColors.upperMid, defColors.lowerMid, defColors.bottom };
//        } else {
//            midPoints = new float[] { 0.0f, titleBottom, 1.0f };
//            colors    = new Color[] { defColors.top, defColors.upperMid, defColors.bottom };
//        }
//
//        return createGradient(midX, y, midX, y + h, midPoints, colors);
//    }

    /**
     * Create the gradient to paint the frame interior.
     *
     * @param  s           the interior shape.
     * @param  titleHeight the height of the title bar, or 0 if none.
     * @param  defColors   the color set to construct the gradient from.
     *
     * @return the gradient.
     */
//    protected Paint createTitleBarGradient(Shape s, int titleHeight, FourColors defColors) {
//        Rectangle2D bounds = s.getBounds2D();
//        float       midX   = (float) bounds.getCenterX();
//        float       y      = (float) bounds.getY();
//        float       h      = (float) bounds.getHeight();
//
//        return createGradient(midX, y, midX, y + h, new float[] { 0.0f, 1.0f },
//                              new Color[] { defColors.top, defColors.upperMid });
//    }
}
