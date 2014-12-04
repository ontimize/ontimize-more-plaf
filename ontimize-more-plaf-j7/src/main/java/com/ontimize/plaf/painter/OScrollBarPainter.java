package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

public class OScrollBarPainter extends AbstractRegionPainter {
    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of ScrollBarPainter to determine which region/state is being painted
    // by that instance.

    public static final int BORDER_DISABLED = 1;
    public static final int BORDER_ENABLED = 2;

    protected int state; // refers to one of the static ints above
    protected PaintContext ctx;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of TextAreaPainter would be created. For the
    // moment at least,
    // however, all are created for each instance.
    protected Color color4 = this.decodeColor("nimbusBlueGrey", 0.007936537f, -0.07826825f, 0.10588235f, 0);
    protected Color color7 = this.decodeColor("nimbusBlueGrey", -0.027777791f, -0.0965403f, -0.18431371f, 0);

    protected Paint borderColorEnabled;
    protected Paint borderColorDisabled;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OScrollBarPainter(int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx = ctx;
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        // populate componentColors array with colors calculated in
        // getExtendedCacheKeys call
        this.componentColors = extendedCacheKeys;

        switch (this.state) {
        case BORDER_ENABLED:
            this.paintBorderEnabled(g, c, width, height);
            break;
        case BORDER_DISABLED:
            this.paintBorderDisabled(g, c, width, height);
            break;
        }
    }

    @Override
    protected String getComponentKeyName() {
        return "ScrollBar";
    }

    @Override
    protected void init() {

        // enable:
        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].border");
        if (obj instanceof Paint) {
            this.borderColorEnabled = (Paint) obj;
        } else {
            this.borderColorEnabled = this.color7;
        }

        // disable:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].border");
        if (obj instanceof Paint) {
            this.borderColorDisabled = (Paint) obj;
        } else {
            this.borderColorDisabled = this.color4;
        }

    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    protected void paintBorderEnabled(Graphics2D g, JComponent c, int width, int height) {
        this.drawDegradatedBorders(g, c, width, height, this.borderColorEnabled);
    }

    protected void paintBorderDisabled(Graphics2D g, JComponent c, int width, int height) {
        this.drawDegradatedBorders(g, c, width, height, this.borderColorDisabled);
    }

    protected void drawDegradatedBorders(Graphics2D g, JComponent c, int width, int height, Paint color) {
        Paint previousPaint = g.getPaint();
        RenderingHints rh = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = 0;
        int y = 0;
        Insets insets = c.getInsets();

        g.setPaint(color);

        // Drawing rectangle zones...
        // top
        Shape s = this.createRect(insets.left, y, width - insets.left - insets.right, insets.top);
        g.fill(s);
        // bottom
        s = this.createRect(insets.left, y + height - insets.bottom, width - insets.left - insets.right, insets.bottom);
        g.fill(s);
        // left
        s = this.createRect(x, y, insets.right, height);
        g.fill(s);
        // right
        s = this.createRect(x + width - insets.right, y, insets.right, height);
        g.fill(s);

        g.setPaint(previousPaint);
        g.setRenderingHints(rh);

    }

    protected Shape createRect(double x, double y, double w, double h) {

        this.path.reset();

        // left top corner.
        this.path.moveTo(x, y);
        this.path.lineTo(x, y + h);

        // left bottom corner
        this.path.lineTo(x + w, y + h);

        // right bottom corner
        this.path.lineTo(x + w, y);

        // right top corner
        this.path.lineTo(x, y);

        return this.path;
    }

    protected boolean containsTextComponent(JComponent c) {

        if (c.getParent() instanceof JScrollPane) {
            Component[] comps = c.getParent().getComponents();
            if (comps != null && comps.length > 0) {
                for (int i = 0; i < comps.length; i++) {
                    Component current = comps[i];
                    if (current instanceof JComponent && current.getClass().isAssignableFrom(JViewport.class)) {
                        JComponent jC = (JComponent) current;
                        if (jC.getComponents().length > 0 && jC.getComponents()[0] instanceof JTextComponent) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    protected boolean areBothScrollBarsVisibles(JComponent c) {

        if (c.getParent() instanceof JScrollPane) {
            JScrollBar sbv = ((JScrollPane) c.getParent()).getVerticalScrollBar();
            JScrollBar sbh = ((JScrollPane) c.getParent()).getHorizontalScrollBar();
            if (sbv != null && sbv.isVisible() && sbh != null && sbh.isVisible()) {
                return true;
            }
        }

        return false;
    }

}
