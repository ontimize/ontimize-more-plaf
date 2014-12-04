package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.UIManager;

/**
 * Menu painter for menus IN the MENU BAR (to configure border and foreground
 * colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OMenuBarMenuPainter extends AbstractRegionPainter {
    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of OMenuBarMenuPainter to determine which region/state is being painted
    // by that instance.
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    public static final int BACKGROUND_SELECTED = 3;

    protected int shadowWidth;

    protected int state; // refers to one of the static ints above
    protected PaintContext ctx;

    // painter to fill the component:
    protected Paint backgroundColorEnabled;
    protected Paint borderColorEnabled;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of OMenuBarMenuPainter would be created. For the
    // moment at least,
    // however, all are created for each instance.
    protected final Color color1 = new Color(0x86adbf);
    protected final Color color2 = new Color(0xc6dfe3);

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OMenuBarMenuPainter(int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx = ctx;
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        // populate componentColors array with colors calculated in
        // getExtendedCacheKeys call
        this.componentColors = extendedCacheKeys;
        // generate this entire method. Each state/bg/fg/border combo that has
        // been painted gets its own KEY and paint method.

        switch (this.state) {
        case BACKGROUND_SELECTED:
            this.paintBackgroundSelected(g, c);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    @Override
    protected String getComponentKeyName() {
        return "MenuBar:Menu";
    }

    /**
     * Get configuration properties to be used in this painter.
     */
    @Override
    protected void init() {

        // Selected:
        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Selected].background");
        if (obj instanceof Paint) {
            this.backgroundColorEnabled = (Paint) obj;
        }
        if (this.backgroundColorEnabled == null) {
            obj = UIManager.getLookAndFeelDefaults().get("PopupMenu[Enabled].background");
            if (obj instanceof Paint) {
                this.backgroundColorEnabled = (Paint) obj;
            }
        }
        if (this.backgroundColorEnabled == null) {
            this.backgroundColorEnabled = this.color1;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Selected].border");
        if (obj instanceof Paint) {
            this.borderColorEnabled = (Paint) obj;
        }
        if (this.borderColorEnabled == null) {
            obj = UIManager.getLookAndFeelDefaults().get("PopupMenu[Enabled].border");
            if (obj instanceof Paint) {
                this.borderColorEnabled = (Paint) obj;
            }
        }
        if (this.borderColorEnabled == null) {
            this.borderColorEnabled = this.color2;
        }

        Object oShadowWidth = UIManager.getDefaults().get("PopupMenu.shadowWidth");
        if (oShadowWidth instanceof Number) {
            this.shadowWidth = ((Number) oShadowWidth).intValue();
        } else {
            this.shadowWidth = OPopupMenuPainter.defaultShadowWidth;
        }
    }

    protected void paintBackgroundSelected(Graphics2D g, JComponent c) {

        Shape s = this.decodeBorder(c);
        g.setPaint(this.borderColorEnabled);
        g.fill(s);

        s = this.decodeBackground(c);
        g.setPaint(this.backgroundColorEnabled);
        g.fill(s);
    }

    protected Shape decodeBackground(JComponent c) {

        int popUpWidth = -1;
        if (c instanceof JMenu) {
            if (((JMenu) c).getPopupMenu() != null) {
                popUpWidth = ((JMenu) c).getPopupMenu().getWidth();
            }
        }

        double arcRadius = 7;
        int numBorders = 3;
        this.path.reset();

        if (popUpWidth != -1 && popUpWidth < c.getWidth()) {
            // painting when MenuBar:Menu size is greater than PopupMenu
            // width...
            this.path.moveTo(this.decodeX(0.0f) + numBorders + this.shadowWidth, this.decodeY(1.0f) + arcRadius);
            this.path.curveTo(this.decodeX(0.0f) + numBorders + this.shadowWidth, this.decodeY(1.0f) + arcRadius / 2.0,
                    this.decodeX(0.0f) + numBorders + this.shadowWidth + arcRadius / 2.0, this.decodeY(1.0f),
                    this.decodeX(0.0f) + numBorders + this.shadowWidth + arcRadius, this.decodeY(1.0f));
            this.path.lineTo(this.decodeX(3.0f) - numBorders - this.shadowWidth - arcRadius, this.decodeY(1.0f));
            this.path.curveTo(this.decodeX(3.0f) - numBorders - this.shadowWidth - arcRadius / 2.0, this.decodeY(1.0f),
                    this.decodeX(3.0f) - numBorders - this.shadowWidth, this.decodeY(1.0f) + arcRadius / 2.0,
                    this.decodeX(3.0f) - numBorders - this.shadowWidth, this.decodeY(1.0f) + arcRadius);
            this.path.lineTo(this.decodeX(3.0f) - numBorders - this.shadowWidth, this.decodeY(3.0f) - numBorders
                    - arcRadius);
            this.path.curveTo(this.decodeX(3.0f) - numBorders - this.shadowWidth, this.decodeY(3.0f) - numBorders
                    - arcRadius / 2.0, this.decodeX(3.0f) - 1 - numBorders - this.shadowWidth - arcRadius / 2.0,
                    this.decodeY(3.0f) - numBorders, this.decodeX(3.0f) - 1 - numBorders - this.shadowWidth - arcRadius
                            / 2.0, this.decodeY(3.0f) - numBorders);
            this.path.lineTo(this.decodeX(0.0f) + popUpWidth - numBorders - this.shadowWidth, this.decodeY(3.0f)
                    - numBorders);
            this.path.lineTo(this.decodeX(0.0f) + popUpWidth - numBorders - this.shadowWidth, this.decodeY(3.0f));
            this.path.lineTo(this.decodeX(0.0f) + numBorders + this.shadowWidth, this.decodeY(3.0f));
            this.path.lineTo(this.decodeX(0.0f) + numBorders + this.shadowWidth, this.decodeY(1.0f) + arcRadius);

            this.path.closePath();
        } else {
            // painting when MenuBar:Menu size is lower than PopupMenu width...
            this.path.moveTo(this.decodeX(0.0f) + numBorders + this.shadowWidth, this.decodeY(1.0f) + arcRadius);
            this.path.curveTo(this.decodeX(0.0f) + numBorders + this.shadowWidth, this.decodeY(1.0f) + arcRadius / 2.0,
                    this.decodeX(0.0f) + numBorders + this.shadowWidth + arcRadius / 2.0, this.decodeY(1.0f),
                    this.decodeX(0.0f) + numBorders + this.shadowWidth + arcRadius, this.decodeY(1.0f));
            this.path.lineTo(this.decodeX(3.0f) - numBorders - this.shadowWidth - arcRadius, this.decodeY(1.0f));
            this.path.curveTo(this.decodeX(3.0f) - numBorders - this.shadowWidth - arcRadius / 2.0, this.decodeY(1.0f),
                    this.decodeX(3.0f) - numBorders - this.shadowWidth, this.decodeY(1.0f) + arcRadius / 2.0,
                    this.decodeX(3.0f) - numBorders - this.shadowWidth, this.decodeY(1.0f) + arcRadius);
            this.path.lineTo(this.decodeX(3.0f) - numBorders - this.shadowWidth, this.decodeY(3.0f));
            this.path.lineTo(this.decodeX(0.0f) + numBorders + this.shadowWidth, this.decodeY(3.0f));
            this.path.lineTo(this.decodeX(0.0f) + numBorders + this.shadowWidth, this.decodeY(1.0f) + arcRadius);

            this.path.closePath();
        }
        return this.path;
    }

    protected Shape decodeBorder(JComponent c) {

        int popUpWidth = -1;
        if (c instanceof JMenu) {
            if (((JMenu) c).getPopupMenu() != null) {
                popUpWidth = ((JMenu) c).getPopupMenu().getWidth();
            }
        }

        double arcRadius = 9;
        int numBorders = 3;
        this.path.reset();

        if (popUpWidth != -1 && popUpWidth < c.getWidth()) {
            // painting when MenuBar:Menu size is greater than PopupMenu
            // width...
            this.path.moveTo(this.decodeX(0.0f) + this.shadowWidth, this.decodeY(0.0f) + arcRadius);
            this.path.curveTo(this.decodeX(0.0f) + this.shadowWidth, this.decodeY(0.0f) + arcRadius / 2.0,
                    this.decodeX(0.0f) // top-left corner
                            + this.shadowWidth + arcRadius / 2.0, this.decodeY(0.0f), this.decodeX(0.0f)
                            + this.shadowWidth + arcRadius, this.decodeY(0.0f));
            this.path.lineTo(this.decodeX(3.0f) - this.shadowWidth - numBorders - arcRadius, this.decodeY(0.0f)); // top
                                                                                                                  // line
            this.path.curveTo(this.decodeX(3.0f) - this.shadowWidth - arcRadius / 2.0, this.decodeY(0.0f),
                    this.decodeX(3.0f) // top-right corner
                            - this.shadowWidth, this.decodeY(0.0f) + arcRadius / 2.0, this.decodeX(3.0f)
                            - this.shadowWidth, this.decodeY(0.0f) + arcRadius);
            this.path.lineTo(this.decodeX(3.0f) - this.shadowWidth, this.decodeY(3.0f) - arcRadius); // right
                                                                                                     // line
            this.path.curveTo(this.decodeX(3.0f) - this.shadowWidth, this.decodeY(3.0f) - arcRadius / 2.0, // bottom-right
                                                                                                           // corner
                    this.decodeX(3.0f) - this.shadowWidth - arcRadius / 2.0, this.decodeY(3.0f), this.decodeX(3.0f)
                            - this.shadowWidth - arcRadius, this.decodeY(3.0f));
            this.path.lineTo(this.decodeX(0.0f) + this.shadowWidth, this.decodeY(3.0f)); // bottom
                                                                                         // line
            this.path.lineTo(this.decodeX(0.0f) + this.shadowWidth, this.decodeY(0.0f) + arcRadius); // left
                                                                                                     // line
            this.path.closePath();
        } else {
            // painting when MenuBar:Menu size is lower than PopupMenu width...
            this.path.moveTo(this.decodeX(0.0f) + this.shadowWidth, this.decodeY(0.0f) + arcRadius);
            this.path.curveTo(this.decodeX(0.0f) + this.shadowWidth, this.decodeY(0.0f) + arcRadius / 2.0,
                    this.decodeX(0.0f) // top-left corner
                            + this.shadowWidth + arcRadius / 2.0, this.decodeY(0.0f), this.decodeX(0.0f)
                            + this.shadowWidth + arcRadius, this.decodeY(0.0f));
            this.path.lineTo(this.decodeX(3.0f) - this.shadowWidth - numBorders - arcRadius, this.decodeY(0.0f)); // top
                                                                                                                  // line
            this.path.curveTo(this.decodeX(3.0f) - this.shadowWidth - arcRadius / 2.0, this.decodeY(0.0f),
                    this.decodeX(3.0f) // top-right corner
                            - this.shadowWidth, this.decodeY(0.0f) + arcRadius / 2.0, this.decodeX(3.0f)
                            - this.shadowWidth, this.decodeY(0.0f) + arcRadius);
            this.path.lineTo(this.decodeX(3.0f) - this.shadowWidth, this.decodeY(3.0f)); // right
                                                                                         // line
            this.path.lineTo(this.decodeX(0.0f) + this.shadowWidth, this.decodeY(3.0f)); // bottom
                                                                                         // line
            this.path.lineTo(this.decodeX(0.0f) + this.shadowWidth, this.decodeY(0.0f) + arcRadius); // left
                                                                                                     // line

            this.path.closePath();
        }
        return this.path;
    }

}
