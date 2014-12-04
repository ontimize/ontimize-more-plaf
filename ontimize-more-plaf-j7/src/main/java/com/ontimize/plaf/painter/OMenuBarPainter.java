package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * Menu bar painter (to configure border and foreground colors, painters,
 * images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OMenuBarPainter extends AbstractRegionPainter {
    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of MenuBarPainter to determine which region/state is being painted
    // by that instance.
    public static final int BACKGROUND_ENABLED = 1;
    public static final int BORDER_ENABLED = 2;

    protected int state; // refers to one of the static ints above
    protected PaintContext ctx;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of MenuBarPainter would be created. For the
    // moment at least,
    // however, all are created for each instance.
    protected final Color color1 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);
    protected final Color color2 = this.decodeColor("nimbusBlueGrey", -0.027777791f, -0.10255819f, 0.23921567f, 0);
    protected final Color color3 = this.decodeColor("nimbusBlueGrey", -0.111111104f, -0.10654225f, 0.23921567f, -29);
    protected final Color color4 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -255);
    protected final Color color5 = this.decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);

    protected Paint backgroundColorEnabled;
    protected Paint borderColorEnabled;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OMenuBarPainter(int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx = ctx;

        this.init();
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
        case BORDER_ENABLED:
            this.paintBorderEnabled(g);
            break;

        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    @Override
    protected String getComponentKeyName() {
        return "MenuBar";
    }

    /**
     * Get configuration properties to be used in this painter (such as:
     * *BorderPainter, *upperCornerPainter, *lowerCornerPainter and *BgPainter).
     * As usual, it is checked the OLAFCustomConfig.properties, and then in
     * OLAFDefaultConfig.properties.
     *
     * Moreover, if there are not values for that properties in both, the
     * default Nimbus configuration values are set, due to, those properties are
     * needed to paint and used by the Ontimize L&F, so, there are not Nimbus
     * values for that, so, default values are always configured based on Nimbus
     * values.
     *
     */
    @Override
    protected void init() {

        // Enabled:
        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].background");
        if (obj instanceof Paint) {
            this.backgroundColorEnabled = (Paint) obj;
        } else {
            this.backgroundColorEnabled = this.color1;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].border");
        if (obj instanceof Paint) {
            this.borderColorEnabled = (Paint) obj;
        } else {
            this.borderColorEnabled = this.color2;
        }
    }

    protected void paintBackgroundEnabled(Graphics2D g) {
        this.rect = this.decodeRect2();
        g.setPaint(this.backgroundColorEnabled);
        g.fill(this.rect);

    }

    protected void paintBorderEnabled(Graphics2D g) {
        this.rect = this.decodeRectBottomBorder();
        g.setPaint(this.borderColorEnabled);
        g.fill(this.rect);

        this.rect = this.decodeRectTopBorder();
        g.setPaint(this.borderColorEnabled);
        g.fill(this.rect);

        this.rect = this.decodeRectRightBorder();
        g.setPaint(this.borderColorEnabled);
        g.fill(this.rect);

        this.rect = this.decodeRectLeftBorder();
        g.setPaint(this.borderColorEnabled);
        g.fill(this.rect);
    }

    protected Rectangle2D decodeRect2() {
        this.rect.setRect(this.decodeX(1.0f), // x
                this.decodeY(1.0f), // y
                this.decodeX(2.0f) - this.decodeX(1.0f), // width
                this.decodeY(2.0f) - this.decodeY(1.0f)); // height
        return this.rect;
    }

    protected Rectangle2D decodeRectBottomBorder() {
        this.rect.setRect(this.decodeX(1.0f), // x
                this.decodeY(2.0f), // y
                this.decodeX(2.0f) - this.decodeX(1.0f), // width
                this.decodeY(3.0f) - this.decodeY(2.0f)); // height
        return this.rect;
    }

    protected Rectangle2D decodeRectTopBorder() {
        this.rect.setRect(this.decodeX(1.0f), // x
                this.decodeY(0.0f), // y
                this.decodeX(2.0f) - this.decodeX(1.0f), // width
                this.decodeY(1.0f)); // height
        return this.rect;
    }

    protected Rectangle2D decodeRectRightBorder() {
        this.rect.setRect(this.decodeX(2.0f) - 1, // x
                this.decodeY(0.0f), // y
                this.decodeX(3.0f) - this.decodeX(2.0f) + 1, // width
                this.decodeY(3.0f)); // height
        return this.rect;
    }

    protected Rectangle2D decodeRectLeftBorder() {
        this.rect.setRect(this.decodeX(0.0f), // x
                this.decodeY(0.0f), // y
                this.decodeX(1.0f), // width
                this.decodeY(3.0f)); // height
        return this.rect;
    }

}
