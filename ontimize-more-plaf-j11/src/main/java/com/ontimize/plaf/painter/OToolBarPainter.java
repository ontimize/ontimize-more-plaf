package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

public class OToolBarPainter extends AbstractRegionPainter {

    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of ToolBarPainter to determine which region/state is being painted
    // by that instance.
    public static final int BORDER_NORTH = 1;

    public static final int BORDER_SOUTH = 2;

    public static final int BORDER_EAST = 3;

    public static final int BORDER_WEST = 4;

    public static final int HANDLEICON_ENABLED = 5;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();

    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    protected Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of ToolBarPainter would be created. For the
    // moment at least,
    // however, all are created for each instance.
    protected Color color1 = this.decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);

    protected Color color2 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);

    protected Color color3 = this.decodeColor("nimbusBlueGrey", -0.006944418f, -0.07399663f, 0.11372548f, 0);

    protected Color color4 = this.decodeColor("nimbusBorder", 0.0f, -0.029675633f, 0.109803915f, 0);

    protected Color color5 = this.decodeColor("nimbusBlueGrey", -0.008547008f, -0.03494492f, -0.07058823f, 0);

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OToolBarPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        // populate componentColors array with colors calculated in
        // getExtendedCacheKeys call
        this.componentColors = extendedCacheKeys;
        // generate this entire method. Each state/bg/fg/border combo that has
        // been painted gets its own KEY and paint method.
        switch (this.state) {
            case BORDER_NORTH:
                this.paintBorderNorth(g);
                break;
            case BORDER_SOUTH:
                this.paintBorderSouth(g);
                break;
            case BORDER_EAST:
                this.paintBorderEast(g);
                break;
            case BORDER_WEST:
                this.paintBorderWest(g);
                break;
            case HANDLEICON_ENABLED:
                this.painthandleIconEnabled(g);
                break;

        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    @Override
    protected String getComponentKeyName() {
        return "ToolBar";
    }

    protected void paintBorderNorth(Graphics2D g) {
        this.rect = this.decodeRect1();
        g.setPaint(this.color1);
        g.fill(this.rect);

    }

    protected void paintBorderSouth(Graphics2D g) {
        this.rect = this.decodeRect2();
        g.setPaint(this.color1);
        g.fill(this.rect);

    }

    protected void paintBorderEast(Graphics2D g) {
        this.rect = this.decodeRect2();
        g.setPaint(this.color1);
        g.fill(this.rect);

    }

    protected void paintBorderWest(Graphics2D g) {
        this.rect = this.decodeRect1();
        g.setPaint(this.color1);
        g.fill(this.rect);

    }

    protected void painthandleIconEnabled(Graphics2D g) {
        this.rect = this.decodeRect3();
        g.setPaint(this.decodeGradient1(this.rect));
        g.fill(this.rect);
        this.rect = this.decodeRect4();
        g.setPaint(this.color4);
        g.fill(this.rect);
        this.path = this.decodePath1();
        g.setPaint(this.color5);
        g.fill(this.path);
        this.path = this.decodePath2();
        g.setPaint(this.color5);
        g.fill(this.path);

    }

    protected Rectangle2D decodeRect1() {
        this.rect.setRect(this.decodeX(1.0f), // x
                this.decodeY(2.0f), // y
                this.decodeX(2.0f) - this.decodeX(1.0f), // width
                this.decodeY(3.0f) - this.decodeY(2.0f)); // height
        return this.rect;
    }

    protected Rectangle2D decodeRect2() {
        this.rect.setRect(this.decodeX(1.0f), // x
                this.decodeY(0.0f), // y
                this.decodeX(2.0f) - this.decodeX(1.0f), // width
                this.decodeY(1.0f) - this.decodeY(0.0f)); // height
        return this.rect;
    }

    protected Rectangle2D decodeRect3() {
        this.rect.setRect(this.decodeX(0.0f), // x
                this.decodeY(0.0f), // y
                this.decodeX(2.8f) - this.decodeX(0.0f), // width
                this.decodeY(3.0f) - this.decodeY(0.0f)); // height
        return this.rect;
    }

    protected Rectangle2D decodeRect4() {
        this.rect.setRect(this.decodeX(2.8f), // x
                this.decodeY(0.0f), // y
                this.decodeX(3.0f) - this.decodeX(2.8f), // width
                this.decodeY(3.0f) - this.decodeY(0.0f)); // height
        return this.rect;
    }

    protected Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(0.0f), this.decodeY(0.4f));
        this.path.lineTo(this.decodeX(0.4f), this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(0.0f), this.decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    protected Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(this.decodeX(0.0f), this.decodeY(3.0f));
        this.path.lineTo(this.decodeX(0.0f), this.decodeY(2.6f));
        this.path.lineTo(this.decodeX(0.4f), this.decodeY(3.0f));
        this.path.lineTo(this.decodeX(0.0f), this.decodeY(3.0f));
        this.path.closePath();
        return this.path;
    }

    protected Paint decodeGradient1(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.0f * w) + x, (0.5f * h) + y, (1.0f * w) + x, (0.5f * h) + y,
                new float[] { 0.0f, 0.5f, 1.0f },
                new Color[] { this.color2, this.decodeColor(this.color2, this.color3, 0.5f), this.color3 });
    }

}
