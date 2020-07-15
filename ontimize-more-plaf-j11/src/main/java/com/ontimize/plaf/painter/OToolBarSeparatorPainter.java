package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * Painter for the background of the separator in a tool bar
 *
 * @author Imatia Innovation
 *
 */
public class OToolBarSeparatorPainter extends AbstractRegionPainter {

    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of OToolBarSeparatorPainter to determine which region/state is being
    // painted
    // by that instance.
    public static final int BACKGROUND_ENABLED = 1;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of OToolBarSeparatorPainter would be created.
    // For the moment at least,
    // however, all are created for each instance.
    protected final Color color1 = new Color(255, 255, 255, 77);

    protected Paint backgroundColorEnabled;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public static int marginOffset = 5;

    protected int slashWidth = 2;

    protected int slashHeight = 1;

    public OToolBarSeparatorPainter(int state, PaintContext ctx) {
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
        return "ToolBarSeparator";
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

        // enable:
        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].background");
        if (obj instanceof Paint) {
            this.backgroundColorEnabled = (Paint) obj;
        } else {
            this.backgroundColorEnabled = this.color1;
        }
    }

    protected void paintBackgroundEnabled(Graphics2D g, JComponent c) {
        // NOTE:
        // It is necessary to take into consideration that into
        // OntimizeSynthPainterImpl
        // are applied AffineTransformations depending on Toolbar orientation...

        if (c != null) {
            Insets in = c.getInsets();
            Rectangle bounds = c.getBounds();

            int length = -1;
            int x = -1;
            int y = -1;
            if (((JSeparator) c).getOrientation() == SwingConstants.VERTICAL) {
                length = bounds.height - (2 * OToolBarSeparatorPainter.marginOffset);
                x = in.top + OToolBarSeparatorPainter.marginOffset;
                y = bounds.width / 2;
                this.slashWidth = length;
                this.slashHeight = 1;
            } else {
                length = bounds.width - (2 * OToolBarSeparatorPainter.marginOffset);
                x = in.left + OToolBarSeparatorPainter.marginOffset;
                y = bounds.height / 2;
                this.slashWidth = length;
                this.slashHeight = 1;
            }

            g.setPaint(this.backgroundColorEnabled);
            this.rect.setRect(x, y, this.slashWidth, this.slashHeight);
            g.fill(this.rect);

        }

    }

}
