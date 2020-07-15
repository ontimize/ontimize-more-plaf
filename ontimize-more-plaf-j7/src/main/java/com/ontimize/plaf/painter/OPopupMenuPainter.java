package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.utils.DerivedColor;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

/**
 * Painter for the background of the pop up (to configure border and foreground colors, painters,
 * images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OPopupMenuPainter extends AbstractRegionPainter {

    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of OPopupMenuPainter to determine which region/state is being painted
    // by that instance.
    public static final int BACKGROUND_DISABLED = 1;

    public static final int BACKGROUND_ENABLED = 2;

    // painter to fill the component:
    protected Paint backgroundColorDisabled;

    protected Paint backgroundColorEnabled;

    protected Paint borderColorDisabled;

    protected Paint borderColorEnabled;

    float[] fractions = new float[] { 0.0f, 0.0030f, 0.02f, 0.5f, 0.98f, 0.996f, 1.0f };

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);

    protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of OPopupMenuPainter would be created. For the
    // moment at least,
    // however, all are created for each instance.
    protected final Color color1 = this.decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.39607844f, 0);

    protected final Color color2 = this.decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);

    protected final Color color3 = this.decodeColor("nimbusBase", 0.021348298f, -0.6150531f, 0.39999998f, 0);

    protected final Color color4 = new Color(0xc6dfe3);

    public static Color defaultShadowColor = Color.black;

    public static int defaultShadowWidth = 6;

    protected int shadowWidth;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OPopupMenuPainter(int state, PaintContext ctx) {
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
            case BACKGROUND_DISABLED:
                this.paintBackground(g, c);
                break;
            case BACKGROUND_ENABLED:
                this.paintBackground(g, c);
                break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
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
            this.backgroundColorEnabled = this.color2;
        }
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].border");
        if (obj instanceof Paint) {
            this.borderColorEnabled = (Paint) obj;
        } else {
            this.borderColorEnabled = this.color4;
        }

        // disable:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].background");
        if (obj instanceof Paint) {
            this.backgroundColorDisabled = (Paint) obj;
        } else {
            this.backgroundColorDisabled = this.color2;
        }
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].border");
        if (obj instanceof Paint) {
            this.borderColorDisabled = (Paint) obj;
        } else {
            this.borderColorDisabled = this.color4;
        }

        Object oShadowWidth = UIManager.getDefaults().get(this.getComponentKeyName() + ".shadowWidth");
        if (oShadowWidth instanceof Number) {
            this.shadowWidth = ((Number) oShadowWidth).intValue();
        } else {
            this.shadowWidth = OPopupMenuPainter.defaultShadowWidth;
        }
    }

    @Override
    protected String getComponentKeyName() {
        return "PopupMenu";
    }

    protected Paint getBorderColorForState(Shape s) {
        switch (this.state) {
            case BACKGROUND_DISABLED:
                return this.borderColorDisabled;
            case BACKGROUND_ENABLED:
                return this.borderColorEnabled;
        }
        return Color.white;
    }

    protected Paint getBackgroundColorForState(Shape s) {
        Color color = null;
        switch (this.state) {
            case BACKGROUND_DISABLED:
                color = (Color) this.backgroundColorDisabled;
            case BACKGROUND_ENABLED:
                color = (Color) this.backgroundColorEnabled;
        }
        if (color != null) {
            return this.decodeGradient(s, color);
        }
        return Color.white;
    }

    /**
     * Note that menu and menu item elements are overpainted in the pop up, so the popup menu background
     * painter can be hidden by menus painters
     * @param g
     */
    protected void paintBackground(Graphics2D g, JComponent c) {

        if (c instanceof JPopupMenu) {
            Component invoker = ((JPopupMenu) c).getInvoker();
            if (invoker instanceof JComponent) {

                if (invoker.getParent() instanceof JPopupMenu) {
                    boolean rightPos = this.isChildOnRightPosition((JComponent) invoker, c);

                    this.paintShadow(g, (JComponent) invoker, c);

                    Shape s = this.decodeBorderPopupMenu((JComponent) invoker, rightPos);
                    g.setPaint(this.getBorderColorForState(s));
                    g.fill(s);

                    s = ShapeFactory.getInstace()
                        .createRectangle(this.decodeX(1.0f), this.decodeY(1.0f),
                                this.decodeX(2.0f) - this.decodeX(1.0f),
                                this.decodeY(2.0f) - this.decodeY(1.0f));
                    g.setPaint(this.getBackgroundColorForState(s));
                    g.fill(s);

                    s = this.decodeUpperFillerPopupMenu((JComponent) invoker, rightPos);
                    g.fill(s);

                    s = this.decodeBottomFiller();
                    g.fill(s);
                } else if (invoker.getParent() instanceof JMenuBar) {

                    this.paintShadow(g, (JComponent) invoker, c);
                    // Painting border
                    // TODO now is an opaque shape painted before background. To
                    // performance it could be good to paint just the lines of
                    // the border.
                    Shape s = this.decodeBorderMenuBar(c, (JComponent) invoker);
                    g.setPaint(this.getBorderColorForState(s));
                    g.fill(s);

                    s = ShapeFactory.getInstace()
                        .createRectangle(this.decodeX(1.0f), this.decodeY(1.0f),
                                this.decodeX(2.0f) - this.decodeX(1.0f),
                                this.decodeY(2.0f) - this.decodeY(1.0f));
                    g.setPaint(this.getBackgroundColorForState(s));
                    g.fill(s);

                    s = this.decodeUpperFillerMenuBar(c, (JComponent) invoker);
                    g.fill(s);

                    s = this.decodeBottomFiller();
                    g.fill(s);
                } else {

                    this.paintShadow(g, (JComponent) invoker, null);

                    Shape s = this.decodeBorder((JComponent) invoker);
                    g.setPaint(this.getBorderColorForState(s));
                    g.fill(s);

                    s = ShapeFactory.getInstace()
                        .createRectangle(this.decodeX(1.0f), this.decodeY(1.0f),
                                this.decodeX(2.0f) - this.decodeX(1.0f),
                                this.decodeY(2.0f) - this.decodeY(1.0f));
                    g.setPaint(this.getBackgroundColorForState(s));
                    g.fill(s);

                    s = this.decodeUpperFiller((JComponent) invoker);
                    g.fill(s);

                    s = this.decodeBottomFiller();
                    g.fill(s);
                }

            }

        }
    }

    /**
     * Method to know whether child PopupMenu is opened on the right or on the left of the invoker
     * component. The invoker component is an JMenu placed into a JPopupMenu.
     * @param invoker Component that provokes that a new PopupMenu will be displayed.
     * @param component the PopupMenu.
     * @return true if child PopupMenu is placed on the right of invoker component.
     */
    protected boolean isChildOnRightPosition(JComponent invoker, JComponent component) {
        if ((invoker != null) && (component != null)) {
            Point parentLocation = null;
            if (invoker.getParent() != null) {
                parentLocation = invoker.getParent().getLocationOnScreen();
            }
            Point location = component.getLocationOnScreen();
            if ((parentLocation != null) && (location != null)) {
                if (parentLocation.getX() <= location.getX()) {
                    return true;
                }
                if (parentLocation.getX() > location.getX()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Method to know whether invoker component is going to open the child PopupMenu on the right or on
     * the left. The invoker component is an JMenu placed into a JMenuBar.
     * @param invoker Component that provokes that a new PopupMenu will be displayed.
     * @param component the PopupMenu.
     * @return true if invoker component is placed on the left of child PopupMenu.
     */
    protected boolean isInvokerOnLeftPosition(JComponent invoker, JComponent component) {
        if ((invoker != null) && (component != null)) {
            Point invokerLocation = invoker.getLocationOnScreen();
            Point location = component.getLocationOnScreen();
            if ((invokerLocation != null) && (location != null)) {
                if (invokerLocation.getX() <= location.getX()) {
                    return true;
                }
                if (invokerLocation.getX() > location.getX()) {
                    return false;
                }
            }
        }

        return true;
    }

    protected void paintShadow(Graphics2D g, JComponent invoker, JComponent c) {
        int numShadowBorders = 6;

        Container parentContainer = invoker.getParent();
        for (int i = 0; i < numShadowBorders; i++) {
            Shape shadow = null;
            if (parentContainer instanceof JMenuBar) {
                shadow = this.decodeShadowMenuBar(i, i, invoker, c);
            } else if (parentContainer instanceof JPopupMenu) {
                boolean rightPosition = this.isChildOnRightPosition(invoker, c);
                shadow = this.decodeShadowPopupMenu(i, i, rightPosition);
            } else {
                shadow = this.decodeShadow(i, i);
            }
            Color shadowColor = OPopupMenuPainter.defaultShadowColor;
            if (i == 0) {
                shadowColor = OntimizeLAFColorUtils.setAlpha(OPopupMenuPainter.defaultShadowColor, 0.05);
            } else {
                shadowColor = OntimizeLAFColorUtils.setAlpha(OPopupMenuPainter.defaultShadowColor, (i / 10.0));
            }
            g.setPaint(shadowColor);
            g.draw(shadow);
        }

    }

    protected Shape decodeShadowMenuBar(int x, int y, JComponent invoker, JComponent c) {
        double arcRadius = 9;
        double numBorders = 3;
        int xOffset = 3;
        int yOffset = 3;

        int width = invoker.getBounds().width;
        width = width - invoker.getInsets().left - invoker.getInsets().right;

        int compWidth = c.getBounds().width;
        compWidth = compWidth - c.getInsets().left - c.getInsets().right;

        boolean leftPos = this.isInvokerOnLeftPosition(invoker, c);

        this.path.reset();
        if (leftPos) {
            if (width < compWidth) {
                this.path.moveTo(this.decodeX(1.0f), (this.decodeY(0.0f) + yOffset + y) - numBorders);
                this.path.lineTo(this.decodeX(3.0f) - 2 - numBorders - arcRadius,
                        (this.decodeY(0.0f) + yOffset + y) - numBorders);
                this.path.curveTo(this.decodeX(3.0f) - x - 1 - (arcRadius / 2.0),
                        (this.decodeY(0.0f) + yOffset + y) - numBorders, this.decodeX(3.0f) - x - 1,
                        ((this.decodeY(0.0f)
                                + yOffset + numBorders) - 1)
                                + (arcRadius / 2.0),
                        this.decodeX(3.0f) - x - 1, ((this.decodeY(0.0f) + yOffset + numBorders) - 1) + arcRadius);
            } else {
                this.path.moveTo(this.decodeX(3.0f) - x - 1, (this.decodeY(0.0f) + yOffset) - numBorders);
            }

            this.path.lineTo(this.decodeX(3.0f) - x - 1, this.decodeY(3.0f) - arcRadius - numBorders - 3);
            // Bottom right corner...
            this.path.curveTo(this.decodeX(3.0f) - numBorders - x, this.decodeY(3.0f) - y - 1 - (arcRadius / 2.0),
                    this.decodeX(3.0f) - 2 - numBorders - (arcRadius / 2.0),
                    this.decodeY(3.0f) - y - 1, this.decodeX(3.0f) - 2 - numBorders - arcRadius,
                    this.decodeY(3.0f) - y - 1);

            double xPos = this.decodeX(0.0f) + xOffset + numBorders + 1;
            this.path.lineTo(xPos + arcRadius, this.decodeY(3.0f) - y - 1);
            // Bottom left corner...
            this.path.curveTo(((this.decodeX(0.0f) + xOffset) - 1) + x + (arcRadius / 2.0),
                    this.decodeY(3.0f) - numBorders - y, this.decodeX(0.0f) + xOffset + x,
                    this.decodeY(3.0f) - 3 - numBorders - (arcRadius / 2.0), this.decodeX(0.0f) + xOffset + x,
                    this.decodeY(3.0f) - 3 - numBorders - arcRadius);

            this.path.lineTo(this.decodeX(0.0f) + xOffset + x, this.decodeY(0.0f));
        } else {
            this.path.moveTo(this.decodeX(3.0f) - x - 1, (this.decodeY(0.0f) + yOffset) - numBorders);
            this.path.lineTo(this.decodeX(3.0f) - x - 1, this.decodeY(3.0f) - arcRadius - numBorders - 3);
            // Bottom right corner...
            this.path.curveTo(this.decodeX(3.0f) - numBorders - x, this.decodeY(3.0f) - y - 1 - (arcRadius / 2.0),
                    this.decodeX(3.0f) - 2 - numBorders - (arcRadius / 2.0),
                    this.decodeY(3.0f) - y - 1, this.decodeX(3.0f) - 2 - numBorders - arcRadius,
                    this.decodeY(3.0f) - y - 1);

            double xPos = this.decodeX(0.0f) + xOffset + numBorders + 1;
            this.path.lineTo(xPos + arcRadius, this.decodeY(3.0f) - y - 1);
            // Bottom left corner...
            this.path.curveTo(((this.decodeX(0.0f) + xOffset) - 1) + x + (arcRadius / 2.0),
                    this.decodeY(3.0f) - numBorders - y, this.decodeX(0.0f) + xOffset + x,
                    this.decodeY(3.0f) - 3 - numBorders - (arcRadius / 2.0), this.decodeX(0.0f) + xOffset + x,
                    this.decodeY(3.0f) - 3 - numBorders - arcRadius);

            if (width < compWidth) {
                this.path.lineTo(this.decodeX(0.0f) + xOffset + x,
                        ((this.decodeY(0.0f) + yOffset + numBorders) - 1) + arcRadius);
                this.path.curveTo(this.decodeX(0.0f) + xOffset + x,
                        ((this.decodeY(0.0f) + yOffset + y) - numBorders) + (arcRadius / 2.0),
                        this.decodeX(0.0f) + numBorders
                                + xOffset + (arcRadius / 2.0),
                        (this.decodeY(0.0f) + yOffset + y) - numBorders,
                        this.decodeX(0.0f) + numBorders + xOffset + arcRadius, (this.decodeY(0.0f)
                                + yOffset + y)
                                - numBorders);
            } else {
                this.path.lineTo(this.decodeX(0.0f), (this.decodeY(0.0f) + yOffset + y) - numBorders);
            }
            this.path.lineTo(this.decodeX(2.0f), (this.decodeY(0.0f) + yOffset + y) - numBorders);
        }
        return this.path;
    }

    protected Shape decodeShadowPopupMenu(int x, int y, boolean rightPosition) {
        double arcRadius = 9;
        double numBorders = 3;
        int xOffset = 3;
        int yOffset = 3;

        this.path.reset();

        if (rightPosition) {
            this.path.moveTo(this.decodeX(1.0f), (this.decodeY(0.0f) + yOffset + y) - numBorders);
            this.path.lineTo(this.decodeX(3.0f) - 2 - numBorders - arcRadius,
                    (this.decodeY(0.0f) + yOffset + y) - numBorders);
            this.path.curveTo(this.decodeX(3.0f) - x - 1 - (arcRadius / 2.0),
                    (this.decodeY(0.0f) + yOffset + y) - numBorders, this.decodeX(3.0f) - x - 1, ((this.decodeY(0.0f)
                            + yOffset + numBorders) - 1)
                            + (arcRadius / 2.0),
                    this.decodeX(3.0f) - x - 1, ((this.decodeY(0.0f) + yOffset + numBorders) - 1) + arcRadius);
            this.path.lineTo(this.decodeX(3.0f) - x - 1, this.decodeY(3.0f) - arcRadius - numBorders - 3);
            // Bottom right corner...
            this.path.curveTo(this.decodeX(3.0f) - numBorders - x, this.decodeY(3.0f) - y - 1 - (arcRadius / 2.0),
                    this.decodeX(3.0f) - 2 - numBorders - (arcRadius / 2.0),
                    this.decodeY(3.0f) - y - 1, this.decodeX(3.0f) - 2 - numBorders - arcRadius,
                    this.decodeY(3.0f) - y - 1);
            double xPos = this.decodeX(0.0f) + xOffset + numBorders + 1;
            this.path.lineTo(xPos + arcRadius, this.decodeY(3.0f) - y - 1);
            // Bottom left corner...
            this.path.curveTo(((this.decodeX(0.0f) + xOffset) - 1) + x + (arcRadius / 2.0),
                    this.decodeY(3.0f) - numBorders - y, this.decodeX(0.0f) + xOffset + x,
                    this.decodeY(3.0f) - 3 - numBorders - (arcRadius / 2.0), this.decodeX(0.0f) + xOffset + x,
                    this.decodeY(3.0f) - 3 - numBorders - arcRadius);
            this.path.lineTo(this.decodeX(0.0f) + xOffset + x, this.decodeY(0.0f));
        } else {
            this.path.moveTo(this.decodeX(0.0f) + xOffset + x + arcRadius,
                    (this.decodeY(0.0f) + yOffset + y) - numBorders);
            this.path.lineTo(this.decodeX(3.0f) - x - 1, (this.decodeY(0.0f) + yOffset + y) - numBorders);
            this.path.lineTo(this.decodeX(3.0f) - x - 1, this.decodeY(3.0f) - arcRadius - numBorders - 3);
            // Bottom right corner...
            this.path.curveTo(this.decodeX(3.0f) - numBorders - x, this.decodeY(3.0f) - y - 1 - (arcRadius / 2.0),
                    this.decodeX(3.0f) - 2 - numBorders - (arcRadius / 2.0),
                    this.decodeY(3.0f) - y - 1, this.decodeX(3.0f) - 2 - numBorders - arcRadius,
                    this.decodeY(3.0f) - y - 1);
            double xPos = this.decodeX(0.0f) + xOffset + numBorders + 1;
            this.path.lineTo(xPos + arcRadius, this.decodeY(3.0f) - y - 1);
            // Bottom left corner...
            this.path.curveTo(((this.decodeX(0.0f) + xOffset) - 1) + x + (arcRadius / 2.0),
                    this.decodeY(3.0f) - numBorders - y, this.decodeX(0.0f) + xOffset + x,
                    this.decodeY(3.0f) - 3 - numBorders - (arcRadius / 2.0), this.decodeX(0.0f) + xOffset + x,
                    this.decodeY(3.0f) - 3 - numBorders - arcRadius);

            this.path.lineTo(this.decodeX(0.0f) + xOffset + x,
                    ((this.decodeY(0.0f) + yOffset + numBorders) - 1) + arcRadius);
            this.path.curveTo(this.decodeX(0.0f) + xOffset + x,
                    ((this.decodeY(0.0f) + yOffset) - numBorders) + (arcRadius / 2.0), this.decodeX(0.0f) + xOffset + x
                            + (arcRadius / 2.0),
                    (this.decodeY(0.0f) + yOffset + y) - numBorders, this.decodeX(0.0f) + xOffset + x + arcRadius,
                    (this.decodeY(0.0f) + yOffset + y)
                            - numBorders);
        }

        return this.path;
    }

    protected Shape decodeShadow(int x, int y) {
        double arcRadius = 9;
        double numBorders = 3;
        int xOffset = 3;
        int yOffset = 3;

        this.path.reset();
        this.path.moveTo(this.decodeX(0.0f) + xOffset + numBorders + 1 + arcRadius,
                (this.decodeY(0.0f) + yOffset + y) - numBorders);

        this.path.lineTo(this.decodeX(3.0f) - 2 - numBorders - arcRadius,
                (this.decodeY(0.0f) + yOffset + y) - numBorders);
        this.path.curveTo(this.decodeX(3.0f) - x - 1 - (arcRadius / 2.0),
                (this.decodeY(0.0f) + yOffset + y) - numBorders, this.decodeX(3.0f) - x - 1, ((this.decodeY(0.0f)
                        + yOffset + numBorders) - 1)
                        + (arcRadius / 2.0),
                this.decodeX(3.0f) - x - 1, ((this.decodeY(0.0f) + yOffset + numBorders) - 1) + arcRadius);

        this.path.lineTo(this.decodeX(3.0f) - x - 1, this.decodeY(3.0f) - arcRadius - numBorders - 3);
        // Bottom right corner...
        this.path.curveTo(this.decodeX(3.0f) - numBorders - x, this.decodeY(3.0f) - y - 1 - (arcRadius / 2.0),
                this.decodeX(3.0f) - 2 - numBorders - (arcRadius / 2.0),
                this.decodeY(3.0f) - y - 1, this.decodeX(3.0f) - 2 - numBorders - arcRadius,
                this.decodeY(3.0f) - y - 1);

        double xPos = this.decodeX(0.0f) + xOffset + numBorders + 1;
        this.path.lineTo(xPos + arcRadius, this.decodeY(3.0f) - y - 1);
        // Bottom left corner...
        this.path.curveTo(((this.decodeX(0.0f) + xOffset) - 1) + x + (arcRadius / 2.0),
                this.decodeY(3.0f) - numBorders - y, this.decodeX(0.0f) + xOffset + x, this.decodeY(3.0f)
                        - 3 - numBorders - (arcRadius / 2.0),
                this.decodeX(0.0f) + xOffset + x, this.decodeY(3.0f) - 3 - numBorders - arcRadius);

        this.path.lineTo(this.decodeX(0.0f) + xOffset + x,
                ((this.decodeY(0.0f) + yOffset + numBorders) - 1) + arcRadius);
        // Top left corner...
        this.path.curveTo(this.decodeX(0.0f) + xOffset + x,
                ((this.decodeY(0.0f) + yOffset + numBorders) - 1) + (arcRadius / 2.0),
                this.decodeX(0.0f) + xOffset + numBorders + 1
                        + (arcRadius / 2.0),
                (this.decodeY(0.0f) + yOffset + y) - numBorders,
                this.decodeX(0.0f) + xOffset + numBorders + 1 + arcRadius, (this.decodeY(0.0f) + yOffset + y)
                        - numBorders);

        this.path.closePath();
        return this.path;
    }

    protected Shape decodeBorder(JComponent invoker) {
        double arcRadius = 9;
        double numBorders = 3;

        this.path.reset();
        this.path.moveTo((this.decodeX(1.0f) - numBorders) + arcRadius, this.decodeY(0.0f) + numBorders);
        this.path.lineTo((this.decodeX(2.0f) + numBorders) - arcRadius, this.decodeY(0.0f) + numBorders);
        this.path.curveTo((this.decodeX(2.0f) + numBorders) - (arcRadius / 2.0), this.decodeY(0.0f) + numBorders,
                this.decodeX(2.0f) + numBorders, this.decodeY(0.0f) + numBorders
                        + (arcRadius / 2.0),
                this.decodeX(2.0f) + numBorders, this.decodeY(0.0f) + numBorders + arcRadius);
        this.path.lineTo(this.decodeX(2.0f) + numBorders, this.decodeY(3.0f) - numBorders - arcRadius);
        this.path.curveTo(this.decodeX(2.0f) + numBorders, this.decodeY(3.0f) - (2 * numBorders) - (arcRadius / 2.0),
                (this.decodeX(2.0f) + numBorders) - (arcRadius / 2.0),
                this.decodeY(3.0f) - (2 * numBorders), (this.decodeX(2.0f) + numBorders) - arcRadius,
                this.decodeY(3.0f) - (2 * numBorders));
        this.path.lineTo((this.decodeX(1.0f) - numBorders) + arcRadius, this.decodeY(3.0f) - (2 * numBorders));
        this.path.curveTo((this.decodeX(1.0f) - numBorders) + (arcRadius / 2.0), this.decodeY(3.0f) - (2 * numBorders),
                this.decodeX(1.0f) - numBorders, this.decodeY(3.0f)
                        - (2 * numBorders) - (arcRadius / 2.0),
                this.decodeX(1.0f) - numBorders, this.decodeY(3.0f) - (2 * numBorders) - arcRadius);
        this.path.lineTo(this.decodeX(1.0f) - numBorders, this.decodeY(0.0f) + numBorders + arcRadius);
        this.path.curveTo(this.decodeX(1.0f) - numBorders, this.decodeY(0.0f) + numBorders + (arcRadius / 2.0),
                (this.decodeX(1.0f) - numBorders) + (arcRadius / 2.0),
                this.decodeY(0.0f) + numBorders, (this.decodeX(1.0f) - numBorders) + arcRadius,
                this.decodeY(0.0f) + numBorders);

        this.path.closePath();
        return this.path;
    }

    protected Shape decodeBorderMenuBar(JComponent c, JComponent invoker) {
        double arcRadius = 9;
        double numBorders = 3;

        int width = invoker.getBounds().width;
        int compWidth = c.getBounds().width;

        boolean leftPos = this.isInvokerOnLeftPosition(invoker, c);

        this.path.reset();
        if (leftPos) {
            this.path.moveTo(this.decodeX(1.0f) - numBorders, this.decodeY(0.0f));
            if (width < compWidth) {
                double middlepoint = width - (2 * this.shadowWidth) - numBorders;
                this.path.lineTo(this.decodeX(1.0f) + middlepoint, this.decodeY(0.0f));
                this.path.lineTo(this.decodeX(1.0f) + middlepoint, this.decodeY(0.0f) + numBorders);
                this.path.lineTo((this.decodeX(2.0f) + numBorders) - arcRadius, this.decodeY(0.0f) + numBorders);
                this.path.curveTo((this.decodeX(2.0f) + numBorders) - (arcRadius / 2.0),
                        this.decodeY(0.0f) + numBorders, this.decodeX(2.0f) + numBorders, this.decodeY(0.0f)
                                + numBorders + (arcRadius / 2.0),
                        this.decodeX(2.0f) + numBorders, this.decodeY(0.0f) + numBorders + arcRadius);
            } else {
                this.path.lineTo(this.decodeX(2.0f) + numBorders, this.decodeY(0.0f) - numBorders);
            }

            this.path.lineTo(this.decodeX(2.0f) + numBorders, this.decodeY(3.0f) - numBorders - arcRadius);
            this.path.curveTo(this.decodeX(2.0f) + numBorders,
                    this.decodeY(3.0f) - (2 * numBorders) - (arcRadius / 2.0),
                    (this.decodeX(2.0f) + numBorders) - (arcRadius / 2.0),
                    this.decodeY(3.0f) - (2 * numBorders), (this.decodeX(2.0f) + numBorders) - arcRadius,
                    this.decodeY(3.0f) - (2 * numBorders));
            this.path.lineTo((this.decodeX(1.0f) - numBorders) + arcRadius, this.decodeY(3.0f) - (2 * numBorders));
            this.path.curveTo((this.decodeX(1.0f) - numBorders) + (arcRadius / 2.0),
                    this.decodeY(3.0f) - (2 * numBorders), this.decodeX(1.0f) - numBorders, this.decodeY(3.0f)
                            - (2 * numBorders) - (arcRadius / 2.0),
                    this.decodeX(1.0f) - numBorders, this.decodeY(3.0f) - (2 * numBorders) - arcRadius);
            this.path.lineTo(this.decodeX(1.0f) - numBorders, this.decodeY(0.0f));

            this.path.closePath();
        } else {
            if (width < compWidth) {
                this.path.moveTo(this.decodeX(1.0f) - numBorders, this.decodeY(0.0f) + numBorders + arcRadius);
                this.path.curveTo(this.decodeX(1.0f) - numBorders, this.decodeY(0.0f) + numBorders + (arcRadius / 2.0),
                        (this.decodeX(1.0f) - numBorders) + (arcRadius / 2.0),
                        this.decodeY(0.0f) + numBorders, (this.decodeX(1.0f) - numBorders) + arcRadius,
                        this.decodeY(0.0f) + numBorders);
                double middlepoint = width - (2 * this.shadowWidth) - (1 * numBorders);
                this.path.lineTo(this.decodeX(2.0f) - middlepoint, this.decodeY(0.0f) + numBorders);
                this.path.lineTo(this.decodeX(2.0f) - middlepoint, this.decodeY(0.0f));
                this.path.lineTo(this.decodeX(2.0f) + numBorders, this.decodeY(0.0f));
            } else {
                this.path.moveTo(this.decodeX(1.0f) - numBorders, this.decodeY(0.0f));
                this.path.lineTo(this.decodeX(2.0f) + numBorders, this.decodeY(0.0f) - numBorders);
            }

            this.path.lineTo(this.decodeX(2.0f) + numBorders, this.decodeY(3.0f) - numBorders - arcRadius);
            this.path.curveTo(this.decodeX(2.0f) + numBorders,
                    this.decodeY(3.0f) - (2 * numBorders) - (arcRadius / 2.0),
                    (this.decodeX(2.0f) + numBorders) - (arcRadius / 2.0),
                    this.decodeY(3.0f) - (2 * numBorders), (this.decodeX(2.0f) + numBorders) - arcRadius,
                    this.decodeY(3.0f) - (2 * numBorders));
            this.path.lineTo((this.decodeX(1.0f) - numBorders) + arcRadius, this.decodeY(3.0f) - (2 * numBorders));
            this.path.curveTo((this.decodeX(1.0f) - numBorders) + (arcRadius / 2.0),
                    this.decodeY(3.0f) - (2 * numBorders), this.decodeX(1.0f) - numBorders, this.decodeY(3.0f)
                            - (2 * numBorders) - (arcRadius / 2.0),
                    this.decodeX(1.0f) - numBorders, this.decodeY(3.0f) - (2 * numBorders) - arcRadius);

            if (width < compWidth) {
                this.path.lineTo(this.decodeX(1.0f) - numBorders, this.decodeY(0.0f) + numBorders + arcRadius);
            } else {
                this.path.lineTo(this.decodeX(1.0f) - numBorders, this.decodeY(0.0f));
            }

            this.path.closePath();
        }
        return this.path;
    }

    protected Shape decodeBorderPopupMenu(JComponent invoker, boolean rightPosition) {
        double arcRadius = 9;
        double numBorders = 3;

        int height = invoker.getBounds().height;
        this.path.reset();

        if (rightPosition) {
            this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.0f));
            this.path.lineTo((this.decodeX(2.0f) + numBorders) - arcRadius, this.decodeY(0.0f));
            this.path.curveTo((this.decodeX(2.0f) + numBorders) - (arcRadius / 2.0), this.decodeY(0.0f),
                    this.decodeX(2.0f) + numBorders, this.decodeY(0.0f) + (arcRadius / 2.0),
                    this.decodeX(2.0f) + numBorders, this.decodeY(0.0f) + arcRadius);
            this.path.lineTo(this.decodeX(2.0f) + numBorders, this.decodeY(3.0f) - (2 * numBorders) - arcRadius);
            this.path.curveTo(this.decodeX(2.0f) + numBorders,
                    this.decodeY(3.0f) - (2 * numBorders) - (arcRadius / 2.0),
                    (this.decodeX(2.0f) + numBorders) - (arcRadius / 2.0),
                    this.decodeY(3.0f) - (2 * numBorders), (this.decodeX(2.0f) + numBorders) - arcRadius,
                    this.decodeY(3.0f) - (2 * numBorders));
            this.path.lineTo((this.decodeX(1.0f) - numBorders) + arcRadius, this.decodeY(3.0f) - (2 * numBorders));
            this.path.curveTo((this.decodeX(1.0f) - numBorders) + (arcRadius / 2.0),
                    this.decodeY(3.0f) - (2 * numBorders), this.decodeX(1.0f) - numBorders, this.decodeY(3.0f)
                            - (2 * numBorders) - (arcRadius / 2.0),
                    this.decodeX(1.0f) - numBorders, this.decodeY(3.0f) - (2 * numBorders) - arcRadius);
            this.path.lineTo(this.decodeX(1.0f) - numBorders, this.decodeY(0.0f) + height);
            this.path.lineTo(this.decodeX(0.0f), this.decodeY(0.0f) + height);
            this.path.lineTo(this.decodeX(0.0f), this.decodeY(0.0f));
        } else {
            this.path.moveTo(this.decodeX(1.0f) + numBorders + arcRadius, this.decodeY(0.0f));
            this.path.lineTo(this.decodeX(3.0f), this.decodeY(0.0f));
            this.path.lineTo(this.decodeX(3.0f), this.decodeY(0.0f) + height);
            this.path.lineTo(this.decodeX(2.0f) + numBorders, this.decodeY(0.0f) + height);

            this.path.lineTo(this.decodeX(2.0f) + numBorders, this.decodeY(3.0f) - (2 * numBorders) - arcRadius);
            this.path.curveTo(this.decodeX(2.0f) + numBorders,
                    this.decodeY(3.0f) - (2 * numBorders) - (arcRadius / 2.0),
                    (this.decodeX(2.0f) + numBorders) - (arcRadius / 2.0),
                    this.decodeY(3.0f) - (2 * numBorders), (this.decodeX(2.0f) + numBorders) - arcRadius,
                    this.decodeY(3.0f) - (2 * numBorders));
            this.path.lineTo((this.decodeX(1.0f) - numBorders) + arcRadius, this.decodeY(3.0f) - (2 * numBorders));
            this.path.curveTo((this.decodeX(1.0f) - numBorders) + (arcRadius / 2.0),
                    this.decodeY(3.0f) - (2 * numBorders), this.decodeX(1.0f) - numBorders, this.decodeY(3.0f)
                            - (2 * numBorders) - (arcRadius / 2.0),
                    this.decodeX(1.0f) - numBorders, this.decodeY(3.0f) - (2 * numBorders) - arcRadius);
            this.path.lineTo(this.decodeX(1.0f) - numBorders, this.decodeY(0.0f) + numBorders + arcRadius);
            this.path.curveTo(this.decodeX(1.0f) - numBorders, this.decodeY(0.0f) + (arcRadius / 2.0),
                    (this.decodeX(1.0f) - numBorders) + (arcRadius / 2.0), this.decodeY(0.0f),
                    (this.decodeX(1.0f) - numBorders) + arcRadius, this.decodeY(0.0f));
        }

        this.path.closePath();
        return this.path;
    }

    protected Shape decodeUpperFiller(JComponent invoker) {

        int numBorders = 3;
        double arcRadius = 7;

        this.path.reset();

        this.path.moveTo(this.decodeX(1.0f) + arcRadius, this.decodeY(0.0f) + (2 * numBorders));
        this.path.lineTo(this.decodeX(2.0f) - arcRadius - numBorders, this.decodeY(0.0f) + (2 * numBorders));
        this.path.curveTo(this.decodeX(2.0f) - (arcRadius / 2.0), this.decodeY(0.0f) + (2 * numBorders),
                this.decodeX(2.0f), this.decodeY(0.0f) + (2 * numBorders)
                        + (arcRadius / 2.0),
                this.decodeX(2.0f), this.decodeY(0.0f) + (2 * numBorders) + arcRadius);
        this.path.lineTo(this.decodeX(2.0f), this.decodeY(1.0f));
        this.path.lineTo(this.decodeX(1.0f), this.decodeY(1.0f));
        this.path.lineTo(this.decodeX(1.0f), this.decodeY(0.0f) + (2 * numBorders) + arcRadius);
        this.path.curveTo(this.decodeX(1.0f), this.decodeY(0.0f) + (2 * numBorders) + (arcRadius / 2.0),
                this.decodeX(1.0f) + (arcRadius / 2.0), this.decodeY(0.0f)
                        + (2 * numBorders),
                this.decodeX(1.0f) + arcRadius, this.decodeY(0.0f) + (2 * numBorders));

        this.path.closePath();
        return this.path;
    }

    protected Shape decodeUpperFillerMenuBar(JComponent c, JComponent invoker) {

        int numBorders = 3;
        double arcRadius = 7;

        int width = invoker.getBounds().width;
        int compWidth = c.getBounds().width;

        boolean leftPos = this.isInvokerOnLeftPosition(invoker, c);

        this.path.reset();
        if (leftPos) {
            this.path.moveTo(this.decodeX(1.0f), this.decodeY(0.0f));
            if (width < compWidth) {
                double middlepoint = width - (2 * this.shadowWidth) - (2 * numBorders);
                this.path.lineTo(this.decodeX(1.0f) + middlepoint, this.decodeY(0.0f));
                this.path.lineTo(this.decodeX(1.0f) + middlepoint, this.decodeY(0.0f) + (2 * numBorders));
                this.path.lineTo(this.decodeX(2.0f) - arcRadius - numBorders, this.decodeY(0.0f) + (2 * numBorders));
                this.path.curveTo(this.decodeX(2.0f) - (arcRadius / 2.0), this.decodeY(0.0f) + (2 * numBorders),
                        this.decodeX(2.0f), this.decodeY(0.0f) + (2 * numBorders)
                                + (arcRadius / 2.0),
                        this.decodeX(2.0f), this.decodeY(0.0f) + (2 * numBorders) + arcRadius);
            } else {
                this.path.lineTo(this.decodeX(2.0f), this.decodeY(0.0f));
            }
            this.path.lineTo(this.decodeX(2.0f), this.decodeY(1.0f));
            this.path.lineTo(this.decodeX(1.0f), this.decodeY(1.0f));
            this.path.lineTo(this.decodeX(1.0f), this.decodeY(0.0f));

            this.path.closePath();
        } else {
            if (width < compWidth) {
                this.path.moveTo(this.decodeX(1.0f), this.decodeY(0.0f) + (2 * numBorders) + arcRadius);
                this.path.curveTo(this.decodeX(1.0f), this.decodeY(0.0f) + (2 * numBorders) + (arcRadius / 2.0),
                        this.decodeX(1.0f) + (arcRadius / 2.0), this.decodeY(0.0f)
                                + (2 * numBorders),
                        this.decodeX(1.0f) + arcRadius, this.decodeY(0.0f) + (2 * numBorders));
                double middlepoint = width - (2 * this.shadowWidth) - (2 * numBorders);
                this.path.lineTo(this.decodeX(2.0f) - middlepoint, this.decodeY(0.0f) + (2 * numBorders));
                this.path.lineTo(this.decodeX(2.0f) - middlepoint, this.decodeY(0.0f));
                this.path.lineTo(this.decodeX(2.0f), this.decodeY(0.0f));
            } else {
                this.path.moveTo(this.decodeX(1.0f), this.decodeY(0.0f));
                this.path.lineTo(this.decodeX(2.0f), this.decodeY(0.0f));
            }

            this.path.lineTo(this.decodeX(2.0f), this.decodeY(1.0f));
            this.path.lineTo(this.decodeX(1.0f), this.decodeY(1.0f));
            if (width < compWidth) {
                this.path.lineTo(this.decodeX(1.0f), this.decodeY(0.0f) + (2 * numBorders) + arcRadius);
            } else {
                this.path.lineTo(this.decodeX(1.0f), this.decodeY(0.0f));
            }
            this.path.closePath();
        }
        return this.path;
    }

    protected Shape decodeUpperFillerPopupMenu(JComponent invoker, boolean rightPosition) {

        int numBorders = 3;
        double arcRadius = 7;

        int height = invoker.getBounds().height;
        this.path.reset();

        if (rightPosition) {
            this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.0f) + numBorders);
            this.path.lineTo(this.decodeX(2.0f) - arcRadius - numBorders, this.decodeY(0.0f) + numBorders);
            this.path.curveTo(this.decodeX(2.0f) - (arcRadius / 2.0), this.decodeY(0.0f) + numBorders,
                    this.decodeX(2.0f), this.decodeY(0.0f) + numBorders + (arcRadius / 2.0),
                    this.decodeX(2.0f), this.decodeY(0.0f) + numBorders + arcRadius);
            this.path.lineTo(this.decodeX(2.0f), this.decodeY(0.0f) + height);
            this.path.lineTo(this.decodeX(1.0f), this.decodeY(0.0f) + height);
            this.path.lineTo(this.decodeX(1.0f), (this.decodeY(0.0f) + height) - numBorders);
            this.path.lineTo(this.decodeX(0.0f), (this.decodeY(0.0f) + height) - numBorders);
            this.path.lineTo(this.decodeX(0.0f), this.decodeY(0.0f) + numBorders);
        } else {
            this.path.moveTo(this.decodeX(1.0f) + arcRadius + numBorders, this.decodeY(0.0f) + numBorders);
            this.path.lineTo(this.decodeX(3.0f), this.decodeY(0.0f) + numBorders);
            this.path.lineTo(this.decodeX(3.0f), (this.decodeY(0.0f) + height) - numBorders);
            this.path.lineTo(this.decodeX(2.0f), (this.decodeY(0.0f) + height) - numBorders);
            this.path.lineTo(this.decodeX(2.0f), this.decodeY(0.0f) + height);
            this.path.lineTo(this.decodeX(1.0f), this.decodeY(0.0f) + height);
            this.path.lineTo(this.decodeX(1.0f), this.decodeY(0.0f) + numBorders + arcRadius);
            this.path.curveTo(this.decodeX(1.0f), this.decodeY(0.0f) + numBorders + (arcRadius / 2.0),
                    this.decodeX(1.0f) + (arcRadius / 2.0), this.decodeY(0.0f) + numBorders,
                    this.decodeX(1.0f) + arcRadius, this.decodeY(0.0f) + numBorders);
        }

        this.path.closePath();
        return this.path;
    }

    protected Shape decodeBottomFiller() {

        int numBorders = 9;
        double arcRadius = 7;
        this.path.reset();

        this.path.moveTo(this.decodeX(1.0f), this.decodeY(2.0f));
        this.path.lineTo(this.decodeX(2.0f), this.decodeY(2.0f));
        this.path.lineTo(this.decodeX(2.0f), this.decodeY(3.0f) - numBorders - arcRadius);
        this.path.curveTo(this.decodeX(2.0f), this.decodeY(3.0f) - numBorders - (arcRadius / 2.0),
                this.decodeX(2.0f) - (arcRadius / 2.0), this.decodeY(3.0f) - numBorders,
                this.decodeX(2.0f) - arcRadius, this.decodeY(3.0f) - numBorders);
        this.path.lineTo(this.decodeX(1.0f) + arcRadius, this.decodeY(3.0f) - numBorders);
        this.path.curveTo(this.decodeX(1.0f) + (arcRadius / 2.0), this.decodeY(3.0f) - numBorders, this.decodeX(1.0f),
                this.decodeY(3.0f) - numBorders - (arcRadius / 2.0),
                this.decodeX(1.0f), this.decodeY(3.0f) - numBorders - arcRadius);
        this.path.lineTo(this.decodeX(1.0f), this.decodeY(2.0f));

        this.path.closePath();
        return this.path;
    }

    protected com.ontimize.plaf.utils.DerivedColor getDerivedColor(Color parent, float hOffset, float sOffset,
            float bOffset, int aOffset) {
        DerivedColor color = new DerivedColor.UIResource(parent, hOffset, sOffset, bOffset, aOffset);
        color.rederiveColor();
        return color;
    }

    protected Paint decodeGradient(Shape s, Color bgBaseColor) {

        Color derived = this.getDerivedColor(bgBaseColor, -0.0010442734f, -0.0010442734f, -0.18823528f, 0);

        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(x, y, x, y + h, new float[] { 0.0f, 1.0f }, new Color[] { bgBaseColor, derived });
    }

}
