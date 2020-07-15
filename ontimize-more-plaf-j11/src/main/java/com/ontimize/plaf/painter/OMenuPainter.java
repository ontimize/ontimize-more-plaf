package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

public class OMenuPainter extends AbstractRegionPainter {

    // package integers representing the available states that this painter will
    // paint. These are used when creating a new instance
    // of MenuPainter to determine which region/state is being painted by that
    // instance.
    public static final int BACKGROUND_DISABLED = 1;

    public static final int BACKGROUND_ENABLED = 2;

    public static final int BACKGROUND_ENABLED_SELECTED = 3;

    public static final int ARROWICON_DISABLED = 4;

    public static final int ARROWICON_ENABLED = 5;

    public static final int ARROWICON_ENABLED_SELECTED = 6;

    // reused during the painting code of the layers
    protected Path2D path = new Path2D.Float();

    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    protected Paint arrowIconDisabledColor;

    protected Paint arrowIconEnabledColor;

    protected Paint arrowIconEnabledAndSelectedColor;

    protected Paint backgroundColorEnabledSelected;

    protected Paint borderColorEnabledSelected;

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of MenuPainter would be created. For the moment
    // at least,
    // however, all are created for each instance.
    protected final Color color1 = new Color(0x86adbf);

    protected final Color color2 = new Color(0xc6dfe3);

    protected final Color color3 = this.decodeColor("nimbusBlueGrey", 0.055555582f, -0.09663743f, -0.4627451f, 0);

    protected final Color color4 = new Color(255, 255, 255, 255);

    protected final Color color5 = OntimizeLAFColorUtils.setAlpha(Color.black, 0.2);

    protected final Color color6 = OntimizeLAFColorUtils.setAlpha(Color.white, 0.5);

    protected Paint arrowIconUpperShadowColor;

    protected Paint arrowIconBottomShadowColor;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OMenuPainter(int state, PaintContext ctx) {
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
            case BACKGROUND_ENABLED_SELECTED:
                this.paintBackgroundEnabledAndSelected(g, c);
                break;
            case ARROWICON_DISABLED:
                this.paintarrowIconDisabled(g);
                break;
            case ARROWICON_ENABLED:
                this.paintarrowIconEnabled(g);
                break;
            case ARROWICON_ENABLED_SELECTED:
                this.paintarrowIconEnabledAndSelected(g);
                break;

        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return this.ctx;
    }

    @Override
    protected String getComponentKeyName() {
        return "Menu";
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

        // Selected:
        Object obj = UIManager.getLookAndFeelDefaults()
            .get(this.getComponentKeyName() + "[Enabled+Selected].background");
        if (obj instanceof Paint) {
            this.backgroundColorEnabledSelected = (Paint) obj;
        } else {
            this.backgroundColorEnabledSelected = this.color1;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled+Selected].border");
        if (obj instanceof Paint) {
            this.borderColorEnabledSelected = (Paint) obj;
        }
        if (this.borderColorEnabledSelected == null) {
            obj = UIManager.getLookAndFeelDefaults().get("PopupMenu[Enabled].border");
            if (obj instanceof Paint) {
                this.borderColorEnabledSelected = (Paint) obj;
            }
        }
        if (this.borderColorEnabledSelected == null) {
            this.borderColorEnabledSelected = this.color2;
        }

        // Arrow:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].arrowBackground");
        if (obj instanceof Paint) {
            this.arrowIconEnabledColor = (Paint) obj;
        } else {
            this.arrowIconEnabledColor = this.color3;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].arrowBackground");
        if (obj instanceof Paint) {
            this.arrowIconDisabledColor = (Paint) obj;
        } else {
            this.arrowIconDisabledColor = this.color2;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled+Selected].arrowBackground");
        if (obj instanceof Paint) {
            this.arrowIconEnabledAndSelectedColor = (Paint) obj;
        } else {
            this.arrowIconEnabledAndSelectedColor = this.color4;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".arrowBackground_upperShadow");
        if (obj instanceof Paint) {
            this.arrowIconUpperShadowColor = (Paint) obj;
        } else {
            this.arrowIconUpperShadowColor = this.color5;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".arrowBackground_bottomShadow");
        if (obj instanceof Paint) {
            this.arrowIconBottomShadowColor = (Paint) obj;
        } else {
            this.arrowIconBottomShadowColor = this.color6;
        }
    }

    protected void paintBackgroundEnabledAndSelected(Graphics2D g, JComponent c) {
        boolean right = true;
        if (c instanceof JMenu) {
            Point p = this.getPopupMenuOrigin((JMenu) c, ((JMenu) c).getPopupMenu());
            if (p != null) {
                if (p.getX() < 0) {
                    right = false;
                }
            }
        }

        if (right) {
            // painting borders...
            Shape s = ShapeFactory.getInstace()
                .createRectangle(this.decodeX(1.0f), this.decodeY(0.0f), this.decodeX(2.0f), this.decodeY(3.0f));
            g.setPaint(this.borderColorEnabledSelected);
            g.fill(s);

            s = ShapeFactory.getInstace()
                .createSemiCircle(this.decodeX(1.0f) + 1, this.decodeY(0.0f), this.decodeY(3.0f),
                        ShapeFactory.ANTICLOCKWISE, false);
            g.fill(s);

            // Painting background...
            this.rect = this.decodeRect1();
            g.setPaint(this.backgroundColorEnabledSelected);
            g.fill(this.rect);

            s = ShapeFactory.getInstace()
                .createSemiCircle(this.decodeX(1.0f) + 2, this.decodeY(1.0f), this.decodeY(2.0f) - this.decodeY(1.0f),
                        ShapeFactory.ANTICLOCKWISE, false);
            g.fill(s);
        } else {
            // painting borders...
            Shape s = ShapeFactory.getInstace()
                .createRectangle(this.decodeX(0.0f), this.decodeY(0.0f), this.decodeX(2.0f), this.decodeY(3.0f));
            g.setPaint(this.borderColorEnabledSelected);
            g.fill(s);

            s = ShapeFactory.getInstace()
                .createSemiCircle(this.decodeX(2.0f), this.decodeY(0.0f), this.decodeY(3.0f), ShapeFactory.CLOCKWISE,
                        false);
            g.fill(s);

            // Painting background...
            this.rect = this.decodeRect2();
            g.setPaint(this.backgroundColorEnabledSelected);
            g.fill(this.rect);

            s = ShapeFactory.getInstace()
                .createSemiCircle(this.decodeX(2.0f), this.decodeY(1.0f), this.decodeY(2.0f) - this.decodeY(1.0f),
                        ShapeFactory.CLOCKWISE, false);
            g.fill(s);

        }
    }

    protected void paintarrowIconDisabled(Graphics2D g) {
        this.path = this.decodeArrowPath();
        g.setPaint(this.arrowIconDisabledColor);
        g.fill(this.path);

    }

    protected void paintarrowIconEnabled(Graphics2D g) {
        this.path = this.decodeUpperShadowPath();
        g.setPaint(this.arrowIconUpperShadowColor);
        g.draw(this.path);

        this.path = this.decodeArrowPath();
        g.setPaint(this.arrowIconEnabledColor);
        g.fill(this.path);
    }

    protected void paintarrowIconEnabledAndSelected(Graphics2D g) {

        this.path = this.decodeArrowPath2();
        g.setPaint(this.arrowIconBottomShadowColor);
        g.fill(this.path);

        this.path = this.decodeArrowPath();
        g.setPaint(this.arrowIconEnabledAndSelectedColor);
        g.fill(this.path);

    }

    protected Rectangle2D decodeRect1() {
        this.rect.setRect(this.decodeX(1.0f) + 2, // x
                this.decodeY(1.0f), // y
                this.decodeX(3.0f), // width
                this.decodeY(2.0f) - this.decodeY(1.0f)); // height
        return this.rect;
    }

    protected Rectangle2D decodeRect2() {
        this.rect.setRect(this.decodeX(0.0f), // x
                this.decodeY(1.0f), // y
                this.decodeX(2.0f), // width
                this.decodeY(2.0f) - this.decodeY(1.0f)); // height
        return this.rect;
    }

    protected Path2D decodeUpperShadowPath() {
        this.path.reset();
        this.path.moveTo(this.decodeX(1.0f), this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(2.0f), this.decodeY(1.44f));
        return this.path;
    }

    protected Path2D decodeArrowPath() {
        this.path.reset();
        this.path.moveTo(this.decodeX(1.0f), this.decodeY(1.0f));
        this.path.lineTo(this.decodeX(2.0f), this.decodeY(1.5625f));
        this.path.lineTo(this.decodeX(1.0f), this.decodeY(2.0f));
        this.path.lineTo(this.decodeX(1.0f), this.decodeY(1.0f));
        this.path.closePath();
        return this.path;
    }

    protected Path2D decodeArrowPath2() {
        this.path.reset();
        this.path.moveTo(this.decodeX(1.0f), this.decodeY(1.0f) + 1);
        this.path.lineTo(this.decodeX(2.0f), this.decodeY(1.5625f) + 1);
        this.path.lineTo(this.decodeX(1.0f), this.decodeY(3.0f));
        this.path.lineTo(this.decodeX(1.0f), this.decodeY(1.0f) + 1);
        this.path.closePath();
        return this.path;
    }

    protected Point getPopupMenuOrigin(JMenu jMenu, JPopupMenu pm) {
        int x = 0;
        int y = 0;
        // JPopupMenu pm = getPopupMenu();
        // Figure out the sizes needed to caclulate the menu position
        Dimension s = jMenu.getSize();
        Dimension pmSize = pm.getSize();
        // For the first time the menu is popped up,
        // the size has not yet been initiated
        if (pmSize.width == 0) {
            pmSize = pm.getPreferredSize();
        }
        Point position = jMenu.getLocationOnScreen();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        GraphicsConfiguration gc = jMenu.getGraphicsConfiguration();
        Rectangle screenBounds = new Rectangle(toolkit.getScreenSize());
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        for (int i = 0; i < gd.length; i++) {
            if (gd[i].getType() == GraphicsDevice.TYPE_RASTER_SCREEN) {
                GraphicsConfiguration dgc = gd[i].getDefaultConfiguration();
                if (dgc.getBounds().contains(position)) {
                    gc = dgc;
                    break;
                }
            }
        }

        if (gc != null) {
            screenBounds = gc.getBounds();
            // take screen insets (e.g. taskbar) into account
            Insets screenInsets = toolkit.getScreenInsets(gc);

            screenBounds.width -= Math.abs(screenInsets.left + screenInsets.right);
            screenBounds.height -= Math.abs(screenInsets.top + screenInsets.bottom);
            position.x -= Math.abs(screenInsets.left);
            position.y -= Math.abs(screenInsets.top);
        }

        Container parent = jMenu.getParent();
        if (parent instanceof JPopupMenu) {
            // We are a submenu (pull-right)
            int xOffset = UIManager.getInt(this.getComponentKeyName() + ".submenuPopupOffsetX");
            int yOffset = UIManager.getInt(this.getComponentKeyName() + ".submenuPopupOffsetY");

            // if (SwingUtilities.isLeftToRight(jMenu)) {
            if (jMenu.getComponentOrientation().isLeftToRight()) {
                // First determine x:
                x = s.width + xOffset; // Prefer placement to the right
                if (((position.x + x + pmSize.width) >= (screenBounds.width + screenBounds.x)) && (// popup
                                                                                                   // doesn't
                                                                                                   // fit
                                                                                                   // -
                                                                                                   // place
                                                                                                   // it
                                                                                                   // wherever
                                                                                                   // there's
                                                                                                   // more
                                                                                                   // room
                (screenBounds.width - s.width) < (2 * (position.x - screenBounds.x)))) {

                    x = 0 - xOffset - pmSize.width;
                }
            } else {
                // First determine x:
                x = 0 - xOffset - pmSize.width; // Prefer placement to the left
                if (((position.x + x) < screenBounds.x) && (// popup doesn't fit
                                                            // - place it
                                                            // wherever there's
                                                            // more room
                (screenBounds.width - s.width) > (2 * (position.x - screenBounds.x)))) {

                    x = s.width + xOffset;
                }
            }
            // Then the y:
            y = yOffset; // Prefer dropping down
            if (((position.y + y + pmSize.height) >= (screenBounds.height + screenBounds.y)) && (// popup
                                                                                                 // doesn't
                                                                                                 // fit
                                                                                                 // -
                                                                                                 // place
                                                                                                 // it
                                                                                                 // wherever
                                                                                                 // there's
                                                                                                 // more
                                                                                                 // room
            (screenBounds.height - s.height) < (2 * (position.y - screenBounds.y)))) {

                y = s.height - yOffset - pmSize.height;
            }
        } else {
            // We are a toplevel menu (pull-down)
            int xOffset = UIManager.getInt(this.getComponentKeyName() + ".menuPopupOffsetX");
            int yOffset = UIManager.getInt(this.getComponentKeyName() + ".menuPopupOffsetY");

            if (jMenu.getComponentOrientation().isLeftToRight()) {
                // First determine the x:
                x = xOffset; // Extend to the right
                if (((position.x + x + pmSize.width) >= (screenBounds.width + screenBounds.x)) && (// popup
                                                                                                   // doesn't
                                                                                                   // fit
                                                                                                   // -
                                                                                                   // place
                                                                                                   // it
                                                                                                   // wherever
                                                                                                   // there's
                                                                                                   // more
                                                                                                   // room
                (screenBounds.width - s.width) < (2 * (position.x - screenBounds.x)))) {

                    x = s.width - xOffset - pmSize.width;
                }
            } else {
                // First determine the x:
                x = s.width - xOffset - pmSize.width; // Extend to the left
                if (((position.x + x) < screenBounds.x) && (// popup doesn't fit
                                                            // - place it
                                                            // wherever there's
                                                            // more room
                (screenBounds.width - s.width) > (2 * (position.x - screenBounds.x)))) {

                    x = xOffset;
                }
            }
            // Then the y:
            y = s.height + yOffset; // Prefer dropping down
            if (((position.y + y + pmSize.height) >= screenBounds.height) && (// popup
                                                                              // doesn't
                                                                              // fit
                                                                              // -
                                                                              // place
                                                                              // it
                                                                              // wherever
                                                                              // there's
                                                                              // more
                                                                              // room
            (screenBounds.height - s.height) < (2 * (position.y - screenBounds.y)))) {

                y = 0 - yOffset - pmSize.height; // Otherwise drop 'up'
            }
        }
        return new Point(x, y);
    }

}
