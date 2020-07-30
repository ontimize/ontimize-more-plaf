package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class OMenuItemPainter extends AbstractRegionPainter {

    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of MenuItemPainter to determine which region/state is being painted
    // by that instance.
    public static final int BACKGROUND_DISABLED = 1;

    public static final int BACKGROUND_ENABLED = 2;

    public static final int BACKGROUND_MOUSEOVER = 3;

    // reused during the painting code of the layers
    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of MenuItemPainter would be created. For the
    // moment at least,
    // however, all are created for each instance.
    protected final Color color1 = this.decodeColor("nimbusSelection", 0.0f, 0.0f, 0.0f, 0);

    protected final Color color2 = this.decodeColor("nimbusBlueGrey", 0.0f, 0.0f, 0.0f, 0);

    protected Paint backgroundColorDisabled;

    protected Paint backgroundColorEnabled;

    protected Paint backgroundColorMouseOver;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OMenuItemPainter(int state, PaintContext ctx) {
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
            case BACKGROUND_MOUSEOVER:
                this.paintBackgroundMouseOver(g);
                break;
            case BACKGROUND_ENABLED:
                this.paintBackgroundEnable(g);
                break;
            case BACKGROUND_DISABLED:
                this.paintBackgroundDisable(g);
                break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    /**
     * Get configuration properties to be used in this painter (such as: *BorderPainter and *BgPainter).
     * As usual, it is checked the OLAFCustomConfig.properties, and then in
     * OLAFDefaultConfig.properties.
     *
     * Moreover, if there are not values for that properties in both, the default Nimbus configuration
     * values are set, due to, those properties are needed to paint and used by the Ontimize L&F, so,
     * there are not Nimbus values for that, so, default values are always configured based on Nimbus
     * values.
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
            this.backgroundColorEnabled = this.color1;
        }

        // MouseOver:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[MouseOver].background");
        if (obj instanceof Paint) {
            this.backgroundColorMouseOver = (Paint) obj;
        } else {
            this.backgroundColorMouseOver = this.color1;
        }

    }

    @Override
    protected String getComponentKeyName() {
        return "MenuItem";
    }

    protected void paintBackgroundMouseOver(Graphics2D g) {
        this.rect = this.decodeRect1();
        g.setPaint(this.backgroundColorMouseOver);
        g.fill(this.rect);

    }

    protected void paintBackgroundEnable(Graphics2D g) {
        this.rect = this.decodeRect1();
        g.setPaint(this.backgroundColorEnabled);
        g.fill(this.rect);

    }

    protected void paintBackgroundDisable(Graphics2D g) {
        this.rect = this.decodeRect1();
        g.setPaint(this.backgroundColorDisabled);
        g.fill(this.rect);

    }

    protected Rectangle2D decodeRect1() {
        this.rect.setRect(this.decodeX(0.0f), // x
                this.decodeY(0.0f), // y
                this.decodeX(3.0f), // width
                this.decodeY(3.0f)); // height
        return this.rect;
    }

}
