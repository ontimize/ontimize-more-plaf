package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
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

import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.painter.util.ShapeFactory;

public class OScrollPanePainter extends AbstractRegionPainter {

    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of ScrollPanePainter to determine which region/state is being painted
    // by that instance.
    public static final int BACKGROUND_ENABLED = 1;

    public static final int BORDER_ENABLED_FOCUSED = 2;

    public static final int BORDER_ENABLED = 3;

    public static final int BORDER_DISABLED = 4;

    public static final int BORDER_REQUIRED = 666;

    public static final int BORDER_FOCUSED_REQUIRED = 667;

    public static final int BORDER_DISABLED_REQUIRED = 668;

    public static final int CORNER_ENABLED = 5;

    /**
     * The number of pixels that compounds the border width of the component.
     */
    public static int numBorders = 4;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();

    // arrays to round the component (several rounded borders with degradation):
    protected Paint[] degradatedBorderColorEnabled;

    protected Paint[] degradatedBorderColorDisabled;

    protected Paint[] degradatedBorderColorDisabledRequired;

    protected Paint[] degradatedBorderColorFocused;

    protected Paint[] degradatedBorderColorFocusedRequired;

    protected Paint[] degradatedBorderColorRequired;

    protected Color scrollBarCornerColor;

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of TextAreaPainter would be created. For the
    // moment at least,
    // however, all are created for each instance.
    protected Color color3 = this.decodeColor("nimbusBlueGrey", -0.006944418f, -0.07187897f, 0.06666666f, 0);

    protected Color color4 = this.decodeColor("nimbusBlueGrey", 0.007936537f, -0.07826825f, 0.10588235f, 0);

    protected Color color7 = this.decodeColor("nimbusBlueGrey", -0.027777791f, -0.0965403f, -0.18431371f, 0);

    protected Color color8 = this.decodeColor("nimbusBlueGrey", 0.055555582f, -0.1048766f, -0.05098039f, 0);

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    protected double radius;

    public OScrollPanePainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        // populate componentColors array with colors calculated in
        // getExtendedCacheKeys call
        this.componentColors = extendedCacheKeys;

        int x = 0;
        int y = 0;

        switch (this.state) {
            case BORDER_ENABLED_FOCUSED:
                this.paintBorderEnabledAndFocused(g, c, x, y, width, height);
                break;
            case BORDER_ENABLED:
                this.paintBorderEnabled(g, c, x, y, width, height);
                break;
            case BORDER_DISABLED:
                this.paintBorderDisabled(g, c, x, y, width, height);
                break;
            case BORDER_DISABLED_REQUIRED:
                this.paintBorderDisabledRequired(g, c, x, y, width, height);
                break;
            case BORDER_REQUIRED:
                this.paintBorderRequired(g, c, x, y, width, height);
                break;
            case BORDER_FOCUSED_REQUIRED:
                this.paintBorderFocuseddRequired(g, c, x, y, width, height);
                break;
            case BACKGROUND_ENABLED:
                break;
            case CORNER_ENABLED:
                this.paintCorner(g, c);
        }
    }

    @Override
    protected String getComponentKeyName() {
        return "ScrollPane";
    }

    @Override
    protected void init() {

        Object oRadius = UIManager.getLookAndFeelDefaults().get("Application.radius");
        if (oRadius instanceof Double) {
            this.radius = ((Double) oRadius).doubleValue();
        } else {
            this.radius = OntimizeLookAndFeel.defaultRadius;
        }

        // BORDER COLORS
        // enable:
        Object obj = UIManager.getLookAndFeelDefaults().get("TextArea[Enabled].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorEnabled = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorEnabled = (Paint[]) obj;
        } else {
            this.degradatedBorderColorEnabled = new Color[] { this.color7,
                    this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
        }

        // disable:
        obj = UIManager.getLookAndFeelDefaults().get("TextArea[Disabled].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorDisabled = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorDisabled = (Paint[]) obj;
        } else {
            this.degradatedBorderColorDisabled = new Color[] { this.color3,
                    this.decodeColor(this.color3, this.color4, 0.5f), this.color4 };
        }

        // disable + required
        obj = UIManager.getLookAndFeelDefaults().get("TextArea[Disabled+Required].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorDisabledRequired = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorDisabledRequired = (Paint[]) obj;
        } else {
            this.degradatedBorderColorDisabledRequired = new Color[] { this.color7,
                    this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
        }

        // Focused:
        obj = UIManager.getLookAndFeelDefaults().get("TextArea[Focused].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorFocused = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorFocused = (Paint[]) obj;
        } else {
            this.degradatedBorderColorFocused = new Color[] { this.color7,
                    this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
        }

        // Focused + required
        obj = UIManager.getLookAndFeelDefaults().get("TextArea[Focused+Required].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorFocusedRequired = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorFocusedRequired = (Paint[]) obj;
        } else {
            this.degradatedBorderColorFocusedRequired = new Color[] { this.color7,
                    this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
        }

        // required:
        obj = UIManager.getLookAndFeelDefaults().get("TextArea[Required].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorRequired = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorRequired = (Paint[]) obj;
        } else {
            this.degradatedBorderColorRequired = new Color[] { this.color7,
                    this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
        }

        this.scrollBarCornerColor = (Color) UIManager.getLookAndFeelDefaults()
            .get(this.getComponentKeyName() + "[Enabled].border");
        if (this.scrollBarCornerColor == null) {
            this.scrollBarCornerColor = new Color(255, 255, 255);
        }

        OScrollPanePainter.numBorders = Math.max(this.degradatedBorderColorFocused.length,
                Math.max(this.degradatedBorderColorEnabled.length, this.degradatedBorderColorDisabled.length));// -1;

    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    protected JTextComponent getTextComponent(JComponent c) {
        Component[] comps = c.getComponents();
        if ((comps != null) && (comps.length > 0)) {
            for (int i = 0; i < comps.length; i++) {
                Component current = comps[i];
                if ((current instanceof JComponent) && current.getClass().isAssignableFrom(JViewport.class)) {
                    JComponent jComp = (JComponent) current;
                    if ((jComp.getComponentCount() > 0) && (jComp.getComponent(0) instanceof JTextComponent)) {
                        return (JTextComponent) jComp.getComponent(0);
                    }
                }
            }
        }

        return null;
    }

    protected void paintBorderEnabledAndFocused(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        if ((this.degradatedBorderColorFocused != null) && (this.degradatedBorderColorFocused.length > 0)) {
            this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorFocused);
        }

    }

    protected void paintBorderEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        if ((this.degradatedBorderColorEnabled != null) && (this.degradatedBorderColorEnabled.length > 0)) {
            this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorEnabled);
        }

    }

    protected void paintBorderDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        if ((this.degradatedBorderColorDisabled != null) && (this.degradatedBorderColorDisabled.length > 0)) {
            this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorDisabled);
        }
    }

    protected void paintBorderDisabledRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        if ((this.degradatedBorderColorDisabledRequired != null)
                && (this.degradatedBorderColorDisabledRequired.length > 0)) {
            this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorDisabledRequired);
        }
    }

    protected void paintBorderRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        if ((this.degradatedBorderColorRequired != null) && (this.degradatedBorderColorRequired.length > 0)) {
            this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorRequired);
        }
    }

    protected void paintBorderFocuseddRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        if ((this.degradatedBorderColorFocusedRequired != null)
                && (this.degradatedBorderColorFocusedRequired.length > 0)) {
            this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorFocusedRequired);
        }
    }

    protected void paintCorner(Graphics2D g, JComponent c) {
        if (c instanceof JScrollPane) {
            JScrollBar hScrollBar = ((JScrollPane) c).getHorizontalScrollBar();
            JScrollBar vScrollBar = ((JScrollPane) c).getVerticalScrollBar();
            if ((vScrollBar != null) && vScrollBar.isVisible() && (hScrollBar != null) && hScrollBar.isVisible()) {
                int width = vScrollBar.getWidth();
                int height = hScrollBar.getHeight();
                Shape corner = ShapeFactory.getInstace().createRectangle(0, 0, width, height);
                g.setPaint(this.scrollBarCornerColor);
                g.fill(corner);
            }
        }
    }

    public void drawDegradatedBorders(Graphics2D g, JComponent c, int x, int y, int width, int height, Paint[] colors) {
        Paint previousPaint = g.getPaint();
        RenderingHints rh = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (c instanceof JScrollPane) {
            JScrollBar hScrollBar = ((JScrollPane) c).getHorizontalScrollBar();
            JScrollBar vScrollBar = ((JScrollPane) c).getVerticalScrollBar();

            double xx = this.decodeX(1.0f) - 1;
            double yy = this.decodeY(0.0f) + OScrollPanePainter.numBorders;
            double h = height - (2 * OScrollPanePainter.numBorders);
            double w = this.decodeX(2.0f);

            if ((vScrollBar != null) && vScrollBar.isVisible() && (hScrollBar != null) && !hScrollBar.isVisible()) {
                // Painting border just with vertical scrollbar...
                for (int i = 1; i <= colors.length; i++) {
                    double yyy = yy - i;
                    double hh = (h + (2 * i)) - 1;
                    Shape s = this.decodeBorderPathWithVerticalScrollBar(c, xx, yyy, w, hh, i - 1);
                    g.setPaint(colors[i - 1]);
                    g.draw(s);
                }
            } else if ((vScrollBar != null) && !vScrollBar.isVisible() && (hScrollBar != null)
                    && hScrollBar.isVisible()) {
                // Painting border just with horizontal scrollbar...
                for (int i = 1; i <= colors.length; i++) {
                    double yyy = yy - i;
                    double hh = (h + (2 * i)) - 1;
                    Shape s = this.decodeBorderPathWithHorizontalScrollBar(c, xx, yyy, w, hh, i - 1);
                    g.setPaint(colors[i - 1]);
                    g.draw(s);
                }
            } else if ((vScrollBar != null) && vScrollBar.isVisible() && (hScrollBar != null)
                    && hScrollBar.isVisible()) {
                // Painting border with both scrollbar...
                for (int i = 1; i <= colors.length; i++) {
                    double yyy = yy - i;
                    double hh = (h + (2 * i)) - 1;
                    Shape s = this.decodeBorderPathWithBothScrollBar(c, xx, yyy, w, hh, i - 1);
                    g.setPaint(colors[i - 1]);
                    g.draw(s);
                }
            } else {
                for (int i = 1; i <= colors.length; i++) {
                    double yyy = yy - i;
                    double hh = (h + (2 * i)) - 1;
                    Shape s = this.decodeBorderPath(c, xx, yyy, w, hh, i - 1);
                    g.setPaint(colors[i - 1]);
                    g.draw(s);
                }
            }

        }

        g.setPaint(previousPaint);
        g.setRenderingHints(rh);
    }

    protected Shape decodeBorderPath(JComponent c, double x, double y, double w, double h, int borderIndex) {
        this.path.reset();
        JTextComponent textComp = this.getTextComponent(c);
        if (textComp != null) {
            double radius = this.getRadius();
            double x_arc = x + radius;
            if (x_arc > (x + textComp.getInsets().left)) {
                x_arc = x + textComp.getInsets().left;
                radius = textComp.getInsets().left;
            }

            this.path.moveTo(this.intValue(x_arc), y);
            this.path.lineTo(x_arc, y);
            this.path.curveTo(x_arc - (radius / 2.0), y, x_arc - radius, y + (radius / 2.0),
                    x_arc - radius - borderIndex, y + radius);
            this.path.lineTo(x_arc - radius - borderIndex, (y + h) - radius);
            this.path.curveTo(x_arc - radius - borderIndex, (y + h) - (radius / 2.0), x_arc - (radius / 2.0), y + h,
                    x_arc, y + h);
            this.path.lineTo(this.intValue(x_arc), y + h);

            x_arc = w - radius;
            if (x_arc < (w - textComp.getInsets().right)) {
                x_arc = w - textComp.getInsets().right;
                radius = textComp.getInsets().right;
            }

            this.path.lineTo(x_arc, y + h);
            this.path.curveTo(x_arc + (radius / 2.0), y + h, x_arc + radius, (y + h) - (radius / 2.0),
                    x_arc + radius + borderIndex, (y + h) - radius);
            this.path.lineTo(x_arc + radius + borderIndex, y + radius);
            this.path.curveTo(x_arc + radius + borderIndex, y + (radius / 2.0), x_arc + (radius / 2.0), y, x_arc, y);

            this.path.lineTo(this.intValue(x_arc), y);
            this.path.closePath();

        }
        return this.path;
    }

    protected Shape decodeBorderPathWithVerticalScrollBar(JComponent c, double x, double y, double w, double h,
            int borderIndex) {
        this.path.reset();
        JTextComponent textComp = this.getTextComponent(c);
        if (textComp != null) {
            double radius = this.getRadius();
            double x_arc = x + radius;
            if (x_arc > (x + textComp.getInsets().left)) {
                x_arc = x + textComp.getInsets().left;
                radius = textComp.getInsets().left;
            }

            this.path.moveTo(this.intValue(x_arc), y);
            this.path.lineTo(x_arc, y);
            this.path.curveTo(x_arc - (radius / 2.0), y, x_arc - radius, y + (radius / 2.0),
                    x_arc - radius - borderIndex, y + radius);
            this.path.lineTo(x_arc - radius - borderIndex, (y + h) - radius);
            this.path.curveTo(x_arc - radius - borderIndex, (y + h) - (radius / 2.0), x_arc - (radius / 2.0), y + h,
                    x_arc, y + h);
            this.path.lineTo(this.intValue(x_arc), y + h);

            this.path.lineTo(this.intValue(w), y + h);

            x_arc = this.decodeX(3.0f) - OScrollPanePainter.numBorders;

            this.path.lineTo(x_arc + borderIndex, y + h);
            this.path.lineTo(x_arc + borderIndex, y);

            this.path.lineTo(this.intValue(x_arc), y);
            this.path.closePath();

        }
        return this.path;
    }

    protected Shape decodeBorderPathWithHorizontalScrollBar(JComponent c, double x, double y, double w, double h,
            int borderIndex) {
        this.path.reset();
        JTextComponent textComp = this.getTextComponent(c);
        if (textComp != null) {
            double radius = this.getRadius();
            double x_arc = x + radius;
            if (x_arc > (x + textComp.getInsets().left)) {
                x_arc = x + textComp.getInsets().left;
                radius = textComp.getInsets().left;
            }

            this.path.moveTo(this.intValue(x_arc), y);
            this.path.lineTo(x_arc, y);
            this.path.curveTo(x_arc - (radius / 2.0), y, x_arc - radius, y + (radius / 2.0),
                    x_arc - radius - borderIndex, y + radius);
            this.path.lineTo(x_arc - radius - borderIndex, y + h);

            this.path.lineTo(this.intValue(w), y + h);

            x_arc = w - radius;
            if (x_arc < (w - textComp.getInsets().right)) {
                x_arc = w - textComp.getInsets().right;
                radius = textComp.getInsets().right;
            }

            this.path.lineTo(x_arc + radius + borderIndex, y + h);
            this.path.lineTo(x_arc + radius + borderIndex, y + radius);
            this.path.curveTo(x_arc + radius + borderIndex, y + (radius / 2.0), x_arc + (radius / 2.0), y, x_arc, y);

            this.path.lineTo(this.intValue(x_arc), y);
            this.path.closePath();

        }
        return this.path;
    }

    protected Shape decodeBorderPathWithBothScrollBar(JComponent c, double x, double y, double w, double h,
            int borderIndex) {
        this.path.reset();
        JTextComponent textComp = this.getTextComponent(c);
        if (textComp != null) {
            double radius = this.getRadius();
            double x_arc = x + radius;
            if (x_arc > (x + textComp.getInsets().left)) {
                x_arc = x + textComp.getInsets().left;
                radius = textComp.getInsets().left;
            }

            this.path.moveTo(this.intValue(x_arc), y);
            this.path.lineTo(x_arc, y);
            this.path.curveTo(x_arc - (radius / 2.0), y, x_arc - radius, y + (radius / 2.0),
                    x_arc - radius - borderIndex, y + radius);
            this.path.lineTo(x_arc - radius - borderIndex, y + h);

            this.path.lineTo(this.intValue(w), y + h);

            x_arc = this.decodeX(3.0f) - OScrollPanePainter.numBorders;
            this.path.lineTo(x_arc + borderIndex, y + h);
            this.path.lineTo(x_arc + borderIndex, y);
            this.path.lineTo(this.intValue(x_arc), y);
            this.path.closePath();

        }
        return this.path;
    }

    /**
     * This method returns, if it is configured by the user, the value for corner radius.
     * @return
     */
    protected double getRadius() {
        return this.radius;
    }

}
