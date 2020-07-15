package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;

public class OTabbedPaneContentPainter extends AbstractRegionPainter {

    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of TabbedPanePainter to determine which region/state is being painted
    // by that instance.
    public static final int BACKGROUND_DISABLED = 1;

    public static final int BACKGROUND_ENABLED = 2;

    public static final int BACKGROUND_FOCUSED = 3;

    public static final int BACKGROUND_MOUSEOVER = 4;

    public static final int BACKGROUND_PRESSED = 5;

    public static final int BORDER_ENABLED = 6;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of TabbedPaneTabbedPaneTabAreaPainter would be
    // created. For the moment at least,
    // however, all are created for each instance.
    protected final Color color1 = new Color(0, 0, 0, 255);

    protected final Color color2 = new Color(214, 217, 223, 255);

    protected Paint backgroundColorDisabled;

    protected Paint backgroundColorEnabled;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OTabbedPaneContentPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        // populate componentColors array with colors calculated in
        // getExtendedCacheKeys call
        this.componentColors = extendedCacheKeys;

        switch (this.state) {
            case BACKGROUND_ENABLED:
                this.paintBackgroundEnabled(g, c, width, height);
                break;
            case BACKGROUND_DISABLED:
                this.paintBackgroundDisabled(g, c, width, height);
                break;
        }
    }

    @Override
    protected String getComponentKeyName() {
        return "TabbedPane:TabbedPaneContent";
    }

    /**
     * Get configuration properties to be used in this painter.
     *
     */
    @Override
    protected void init() {

        // disable:
        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].background");
        if (obj instanceof Paint) {
            this.backgroundColorDisabled = (Paint) obj;
        } else {
            this.backgroundColorDisabled = this.color2;
        }

        // enable:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].background");
        if (obj instanceof Paint) {
            this.backgroundColorEnabled = (Paint) obj;
        } else {
            this.backgroundColorEnabled = this.color2;
        }

    }

    @Override
    protected final PaintContext getPaintContext() {
        return this.ctx;
    }

    protected void paintBackgroundEnabled(Graphics2D g, JComponent c, int width, int height) {

        this.paintBackground(g, c, this.backgroundColorEnabled);
    }

    protected void paintBackgroundDisabled(Graphics2D g, JComponent c, int width, int height) {

        this.paintBackground(g, c, this.backgroundColorDisabled);
    }

    protected void paintBackground(Graphics2D g, JComponent c, Paint color) {
        Paint previousPaint = g.getPaint();
        RenderingHints rh = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Paint paint = this.getBackgroundColor(c);
        if (paint != null) {
            double x = this.decodeX(0.0f);
            double y = this.decodeY(0.0f);
            double w = this.decodeX(3.0f);
            double h = this.decodeY(3.0f);
            Shape s = ShapeFactory.getInstace().createRectangle(x, y, w, h);

            g.setPaint(paint);
            g.fill(s);
        }

        g.setPaint(previousPaint);
        g.setRenderingHints(rh);
    }

    protected Color getBackgroundColor(JComponent c) {
        if (c instanceof JTabbedPane) {
            JTabbedPane tabPane = (JTabbedPane) c;
            Component comp = tabPane.getSelectedComponent();
            if ((comp != null) && comp.isOpaque()) {
                return comp.getBackground();
            }
            if (c.isOpaque()) {
                if (c.getBackground() != null) {
                    return c.getBackground();
                }
            }
        }
        return null;
    }

    protected Shape decodeBackgroundPath(double x, double y, double w, double h) {

        double arcRadius = 6.0;
        this.path.reset();

        this.path.moveTo(x, y);
        this.path.lineTo(x, (y + h) - arcRadius);
        Shape s = ShapeFactory.getInstace()
            .createRoundCorner(x, y + h, arcRadius, arcRadius, ShapeFactory.BOTTOM_LEFT_CORNER, true);
        this.path.append(s, true);
        this.path.lineTo((x + w) - arcRadius, y + h);
        s = ShapeFactory.getInstace()
            .createRoundCorner(x + w, y + h, arcRadius, arcRadius, ShapeFactory.BOTTOM_RIGHT_CORNER, true);
        this.path.append(s, true);
        this.path.lineTo(x + w, y);
        this.path.closePath();

        return this.path;
    }

}
