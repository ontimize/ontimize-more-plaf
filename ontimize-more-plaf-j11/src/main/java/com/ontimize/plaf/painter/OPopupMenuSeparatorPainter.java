package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

/**
 * Pop up menu separator painter (to configure border, background, foreground colors, painters,
 * images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OPopupMenuSeparatorPainter extends AbstractRegionPainter {

    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of OPopupMenuSeparatorPainter to determine which region/state is being
    // painted
    // by that instance.
    public static final int BACKGROUND_ENABLED = 1;

    public static Color defaultUpperShadowColor = OntimizeLAFColorUtils.setAlpha(Color.black, 0.2);

    public static Color defaultBottomShadowColor = OntimizeLAFColorUtils.setAlpha(Color.white, 0.2);

    ;
    protected Paint upperShadowColor;

    protected Paint bottomShadowColor;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OPopupMenuSeparatorPainter(int state, PaintContext ctx) {
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
                this.paintBackgroundEnabled(g, c);
                break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    @Override
    protected String getComponentKeyName() {
        return "PopupMenuSeparator";
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

        Object oUpperShadowColor = UIManager.getDefaults().get(this.getComponentKeyName() + ".uppershadowcolor");
        if (oUpperShadowColor instanceof Paint) {
            this.upperShadowColor = (Paint) oUpperShadowColor;
        } else {
            this.upperShadowColor = OPopupMenuSeparatorPainter.defaultUpperShadowColor;
        }

        Object oBottomShadowColor = UIManager.getDefaults().get(this.getComponentKeyName() + ".bottomshadowcolor");
        if (oBottomShadowColor instanceof Paint) {
            this.bottomShadowColor = (Paint) oBottomShadowColor;
        } else {
            this.bottomShadowColor = OPopupMenuSeparatorPainter.defaultBottomShadowColor;
        }
    }

    protected void paintBackgroundEnabled(Graphics2D g, JComponent c) {
        // Painting upper line...
        g.setPaint(this.upperShadowColor);
        g.drawLine(0, c.getHeight() / 2, c.getWidth(), c.getHeight() / 2);

        // Painting bottom line...
        g.setPaint(this.bottomShadowColor);
        g.drawLine(0, (c.getHeight() / 2) + 1, c.getWidth(), (c.getHeight() / 2) + 1);
    }

}
