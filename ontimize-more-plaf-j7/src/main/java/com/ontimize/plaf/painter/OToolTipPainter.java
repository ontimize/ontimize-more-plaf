package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

/**
 * Painter for the tooltip (to configure borders and background colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OToolTipPainter extends AbstractRegionPainter {

    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of ToolTipPainter to determine which region/state is being painted
    // by that instance.
    public static final int BACKGROUND_ENABLED = 1;

    // painter to fill the component
    protected Paint backgroundColorEnabled;

    protected Paint borderColorEnabled;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of ToolTipPainter would be created. For the
    // moment at least,
    // however, all are created for each instance.
    protected final Color color1 = this.decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);

    protected final Color color2 = this.decodeColor("info", 0.0f, 0.0f, 0.0f, 0);

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OToolTipPainter(int state, PaintContext ctx) {
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
            case BACKGROUND_ENABLED:
                this.paintBackgroundEnabled(g);
                break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    @Override
    protected String getComponentKeyName() {
        return "ToolTip";
    }

    /**
     * Get configuration properties to be used in this painter (such as: *BorderPainter,
     * *upperCornerPainter, *lowerCornerPainter and *BgPainter). As usual, it is checked the
     * OLAFCustomConfig.properties, and then in OLAFDefaultConfig.properties.
     *
     * Moreover, if there are not values for that properties in both, the default Nimbus configuration
     * values are set, due to, those properties are needed to paint and used by the Ontimize L&F, so,
     * there are not Nimbus values for that, so, default values are always configured based on Nimbus
     * values.
     *
     */
    @Override
    protected void init() {

        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".background");
        if (obj instanceof Paint) {
            this.backgroundColorEnabled = (Paint) obj;
        } else {
            this.backgroundColorEnabled = this.color2;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".border");
        if (obj instanceof Paint) {
            this.borderColorEnabled = (Paint) obj;
        } else {
            this.borderColorEnabled = this.color1;
        }

    }

    protected void paintBackgroundEnabled(Graphics2D g) {

        this.paintShadow(g);

        Shape s = this.decodeRoundRect(this.decodeX(0.0f) + OToolTipPainter.defaultShadowWidth,
                this.decodeY(0.0f) + OToolTipPainter.defaultShadowWidth, this.decodeX(3.0f) - 1
                        - (2 * OToolTipPainter.defaultShadowWidth),
                this.decodeY(3.0f) - 1 - (2 * OToolTipPainter.defaultShadowWidth), 5, 5);
        g.setPaint(this.borderColorEnabled);
        g.draw(s);

        s = this.decodeRoundRect(this.decodeX(0.0f) + OToolTipPainter.defaultShadowWidth + 1,
                this.decodeY(0.0f) + OToolTipPainter.defaultShadowWidth + 1, this.decodeX(3.0f) - 2
                        - (2 * OToolTipPainter.defaultShadowWidth),
                this.decodeY(3.0f) - 2 - (2 * OToolTipPainter.defaultShadowWidth), 5, 5);
        g.setPaint(this.backgroundColorEnabled);
        g.fill(s);
    }

    protected Shape decodeRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight) {
        this.path.reset();
        this.path.moveTo(x, (y + h) - arcHeight);
        this.path.lineTo(x, y + arcHeight);
        this.path.curveTo(x, x + (arcHeight / 2.0), x + (arcWidth / 2.0), y, x + arcWidth, y);

        this.path.lineTo((x + w) - arcWidth, y);
        this.path.curveTo((x + w) - (arcWidth / 2.0), y, x + w, y + (arcHeight / 2.0), x + w, y + arcHeight);
        this.path.lineTo(x + w, (y + h) - arcHeight);

        this.path.curveTo(x + w, (y + h) - (arcHeight / 2.0), (x + w) - (arcWidth / 2.0), y + h, (x + w) - arcWidth,
                y + h);
        this.path.lineTo(x + arcWidth, y + h);

        this.path.curveTo(x + (arcWidth / 2.0), y + h, x, (y + h) - (arcHeight / 2.0), x, (y + h) - arcHeight);

        this.path.closePath();
        return this.path;
    }

    public static Color defaultShadowColor = Color.black;

    public static int defaultShadowWidth = 3;

    protected void paintShadow(Graphics2D g) {

        for (int i = 0; i < (OToolTipPainter.defaultShadowWidth + 1); i++) {
            Shape shadow = null;
            shadow = this.decodeShadow(i, i);
            Color shadowColor = OToolTipPainter.defaultShadowColor;
            if (i == 0) {
                shadowColor = OntimizeLAFColorUtils.setAlpha(OToolTipPainter.defaultShadowColor, 0.05);
            } else {
                shadowColor = OntimizeLAFColorUtils.setAlpha(OToolTipPainter.defaultShadowColor, (i / 10.0));
            }
            g.setPaint(shadowColor);
            g.draw(shadow);
        }

    }

    protected Shape decodeShadow(int x, int y) {
        double arcRadius = 5;
        int xOffset = 3;
        int yOffset = 3;

        this.path.reset();
        this.path.moveTo(this.decodeX(0.0f) + xOffset + 1 + arcRadius, this.decodeY(0.0f) + yOffset + y);

        this.path.lineTo(this.decodeX(3.0f) - 2 - arcRadius, this.decodeY(0.0f) + yOffset + y);
        this.path.curveTo(this.decodeX(3.0f) - x - 1 - (arcRadius / 2.0), this.decodeY(0.0f) + yOffset + y,
                this.decodeX(3.0f) - x - 1, ((this.decodeY(0.0f) + yOffset) - 1)
                        + (arcRadius / 2.0),
                this.decodeX(3.0f) - x - 1, ((this.decodeY(0.0f) + yOffset) - 1) + arcRadius);

        this.path.lineTo(this.decodeX(3.0f) - x - 1, this.decodeY(3.0f) - arcRadius - 3);
        // Bottom right corner...
        this.path.curveTo(this.decodeX(3.0f) - x, this.decodeY(3.0f) - y - 1 - (arcRadius / 2.0),
                this.decodeX(3.0f) - 2 - (arcRadius / 2.0), this.decodeY(3.0f) - y - 1,
                this.decodeX(3.0f) - 2 - arcRadius, this.decodeY(3.0f) - y - 1);

        double xPos = this.decodeX(0.0f) + xOffset + 1;
        this.path.lineTo(xPos + arcRadius, this.decodeY(3.0f) - y - 1);
        // Bottom left corner...
        this.path.curveTo(((this.decodeX(0.0f) + xOffset) - 1) + x + (arcRadius / 2.0), this.decodeY(3.0f) - y,
                this.decodeX(0.0f) + xOffset + x, this.decodeY(3.0f) - 3
                        - (arcRadius / 2.0),
                this.decodeX(0.0f) + xOffset + x, this.decodeY(3.0f) - 3 - arcRadius);

        this.path.lineTo(this.decodeX(0.0f) + xOffset + x, ((this.decodeY(0.0f) + yOffset) - 1) + arcRadius);
        // Top left corner...
        this.path.curveTo(this.decodeX(0.0f) + xOffset + x, ((this.decodeY(0.0f) + yOffset) - 1) + (arcRadius / 2.0),
                this.decodeX(0.0f) + xOffset + 1 + (arcRadius / 2.0),
                this.decodeY(0.0f) + yOffset + y, this.decodeX(0.0f) + xOffset + 1 + arcRadius,
                this.decodeY(0.0f) + yOffset + y);

        this.path.closePath();
        return this.path;
    }

}
