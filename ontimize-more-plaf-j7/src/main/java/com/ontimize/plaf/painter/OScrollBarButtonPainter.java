package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * Painter for the foreground of the buttons in a scroll bar (to configure
 * border and foreground colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OScrollBarButtonPainter extends AbstractRegionPainter {
    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of ScrollBarScrollBarButtonPainter to determine which region/state is
    // being painted
    // by that instance.
    public static final int FOREGROUND_ENABLED = 1;
    public static final int FOREGROUND_DISABLED = 2;
    public static final int FOREGROUND_MOUSEOVER = 3;
    public static final int FOREGROUND_PRESSED = 4;

    protected int state; // refers to one of the static ints above
    protected PaintContext ctx;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();

    protected final int DEFAULT_BORDERTHICKNESS = 0;

    protected String disablePainter;
    protected String enablePainter;
    protected String outerBorder_enablePainter;
    protected String arrow_enablePainter;
    protected String innerBorder_enablePainter;
    protected String mouseoverPainter;
    protected String outerBorder_mouseoverPainter;
    protected String arrow_mouseoverPainter;
    protected String innerBorder_mouseoverPainter;
    protected String pressedPainter;
    protected String outerBorder_pressedPainter;
    protected String arrow_pressedPainter;
    protected String innerBorder_pressedPainter;
    protected String disableTrackPainter;
    protected String enableTrackPainter;

    protected Paint trackColorDisabled;
    protected Paint trackColorEnabled;

    protected Paint scrollBarBorderColorEnabled;
    protected Paint scrollBarBorderColorDisabled;

    protected String outerBorder_enablePainter_direction = "(0.0f * w) + x, (0.5f * h) + y, (0.5735294f * w) + x, (0.5f * h) + y";
    protected String arrow_enablePainter_direction = "(0.925f * w) + x, (0.9285714f * h) + y, (0.925f * w) + x, (0.004201681f * h) + y";
    protected String arrow_mouseoverPainter_direction = "(0.925f * w) + x, (0.9285714f * h) + y, (0.925f * w) + x, (0.004201681f * h) + y";
    protected String outerBorder_pressedPainter_direction = "(0.0f * w) + x, (0.5f * h) + y, (0.5735294f * w) + x, (0.5f * h) + y";

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of ScrollBarScrollBarButtonPainter would be
    // created. For the moment at least,
    // however, all are created for each instance.
    protected final Color color1 = new Color(189, 189, 189);
    protected final Color color2 = this.decodeColor("nimbusBlueGrey", -0.01111114f, -0.07763158f, -0.1490196f, 0);
    protected final Color color3 = this.decodeColor("nimbusBlueGrey", -0.111111104f, -0.10580933f, 0.086274505f, 0);
    protected final Color color4 = this.decodeColor("nimbusBlueGrey", -0.027777791f, -0.102261856f, 0.20392156f, 0);
    protected final Color color5 = this.decodeColor("nimbusBlueGrey", -0.039682567f, -0.079276316f, 0.13333333f, 0);
    protected final Color color6 = this.decodeColor("nimbusBlueGrey", -0.027777791f, -0.07382907f, 0.109803915f, 0);
    protected final Color color7 = this.decodeColor("nimbusBlueGrey", -0.039682567f, -0.08241387f, 0.23137254f, 0);
    protected final Color color8 = this.decodeColor("nimbusBlueGrey", -0.055555522f, -0.08443936f, -0.29411766f, -136);
    protected final Color color9 = this.decodeColor("nimbusBlueGrey", -0.055555522f, -0.09876161f, 0.25490195f, -178);
    protected final Color color10 = this.decodeColor("nimbusBlueGrey", 0.055555582f, -0.08878718f, -0.5647059f, 0);
    protected final Color color11 = this.decodeColor("nimbusBlueGrey", -0.027777791f, -0.080223285f, -0.4862745f, 0);
    protected final Color color12 = this.decodeColor("nimbusBlueGrey", -0.111111104f, -0.09525914f, -0.23137254f, 0);
    protected final Color color13 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -165);
    protected final Color color14 = this.decodeColor("nimbusBlueGrey", -0.04444444f, -0.080223285f, -0.09803921f, 0);
    protected final Color color15 = this.decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, 0.10588235f, 0);
    protected final Color color16 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    protected final Color color17 = this.decodeColor("nimbusBlueGrey", -0.039682567f, -0.081719734f, 0.20784312f, 0);
    protected final Color color18 = this.decodeColor("nimbusBlueGrey", -0.027777791f, -0.07677104f, 0.18431371f, 0);
    protected final Color color19 = this.decodeColor("nimbusBlueGrey", -0.04444444f, -0.080223285f, -0.09803921f, -69);
    protected final Color color20 = this.decodeColor("nimbusBlueGrey", -0.055555522f, -0.09876161f, 0.25490195f, -39);
    protected final Color color21 = this.decodeColor("nimbusBlueGrey", 0.055555582f, -0.0951417f, -0.49019608f, 0);
    protected final Color color22 = this.decodeColor("nimbusBlueGrey", -0.027777791f, -0.086996906f, -0.4117647f, 0);
    protected final Color color23 = this.decodeColor("nimbusBlueGrey", -0.111111104f, -0.09719298f, -0.15686274f, 0);
    protected final Color color24 = this.decodeColor("nimbusBlueGrey", -0.037037015f, -0.043859646f, -0.21568626f, 0);
    protected final Color color25 = this.decodeColor("nimbusBlueGrey", -0.06349206f, -0.07309316f, -0.011764705f, 0);
    protected final Color color26 = this.decodeColor("nimbusBlueGrey", -0.048611104f, -0.07296763f, 0.09019607f, 0);
    protected final Color color27 = this.decodeColor("nimbusBlueGrey", -0.03535354f, -0.05497076f, 0.031372547f, 0);
    protected final Color color28 = this.decodeColor("nimbusBlueGrey", -0.034188032f, -0.043168806f, 0.011764705f, 0);
    protected final Color color29 = this.decodeColor("nimbusBlueGrey", -0.03535354f, -0.0600676f, 0.109803915f, 0);
    protected final Color color30 = this.decodeColor("nimbusBlueGrey", -0.037037015f, -0.043859646f, -0.21568626f, -44);
    protected final Color color31 = this.decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.74509805f, 0);

    protected Paint backgroundColorEnabled;
    protected Paint backgroundColorDisabled;
    protected Paint backgroundColorMouseOver;
    protected Paint backgroundColorPressed;

    protected Paint backgroundArrowColorEnabled;
    protected Paint backgroundArrowColorDisabled;
    protected Paint backgroundArrowColorMouseOver;
    protected Paint backgroundArrowColorPressed;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OScrollBarButtonPainter(int state, PaintContext ctx) {
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
        case FOREGROUND_ENABLED:
            this.paintForegroundEnabled(g, c, width, height);
            break;
        case FOREGROUND_DISABLED:
            this.paintForegroundDisabled(g, c, width, height);
            break;
        case FOREGROUND_MOUSEOVER:
            this.paintForegroundMouseOver(g, c, width, height);
            break;
        case FOREGROUND_PRESSED:
            this.paintForegroundPressed(g, c, width, height);
            break;

        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    @Override
    protected String getComponentKeyName() {
        return "ScrollBar:\"ScrollBar.button\"";
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

        // disable:
        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].background");
        if (obj instanceof Paint) {
            this.backgroundColorDisabled = (Paint) obj;
        } else {
            this.backgroundColorDisabled = this.color1;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].arrowBackground");
        if (obj instanceof Paint) {
            this.backgroundArrowColorDisabled = (Paint) obj;
        } else {
            this.backgroundArrowColorDisabled = this.color11;
        }

        // enable:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].background");
        if (obj instanceof Paint) {
            this.backgroundColorEnabled = (Paint) obj;
        } else {
            this.backgroundColorEnabled = this.color1;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].arrowBackground");
        if (obj instanceof Paint) {
            this.backgroundArrowColorEnabled = (Paint) obj;
        } else {
            this.backgroundArrowColorEnabled = this.color11;
        }

        // mouse over:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[MouseOver].background");
        if (obj instanceof Paint) {
            this.backgroundColorMouseOver = (Paint) obj;
        } else {
            this.backgroundColorMouseOver = this.color14;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[MouseOver].arrowBackground");
        if (obj instanceof Paint) {
            this.backgroundArrowColorMouseOver = (Paint) obj;
        } else {
            this.backgroundArrowColorMouseOver = this.color21;
        }

        // pressed:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Pressed].background");
        if (obj instanceof Paint) {
            this.backgroundColorPressed = (Paint) obj;
        } else {
            this.backgroundColorPressed = this.color24;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Pressed].arrowBackground");
        if (obj instanceof Paint) {
            this.backgroundArrowColorPressed = (Paint) obj;
        } else {
            this.backgroundArrowColorPressed = this.color31;
        }

        // Track:
        obj = UIManager.getLookAndFeelDefaults().get("ScrollBar:ScrollBarTrack[Enabled].background");
        if (obj instanceof Paint) {
            this.trackColorEnabled = (Paint) obj;
        } else {
            this.trackColorEnabled = this.color1;
        }
        obj = UIManager.getLookAndFeelDefaults().get("ScrollBar:ScrollBarTrack[Disabled].background");
        if (obj instanceof Paint) {
            this.trackColorDisabled = (Paint) obj;
        } else {
            this.trackColorDisabled = this.color5;
        }

        // ScrollBar border:
        obj = UIManager.getLookAndFeelDefaults().get("ScrollBar[Enabled].background");
        if (obj instanceof Paint) {
            this.scrollBarBorderColorEnabled = (Paint) obj;
        } else {
            this.scrollBarBorderColorEnabled = new Color(255, 255, 255);
        }

        // disable:
        obj = UIManager.getLookAndFeelDefaults().get("ScrollBar[Disabled].background");
        if (obj instanceof Paint) {
            this.scrollBarBorderColorDisabled = (Paint) obj;
        } else {
            this.scrollBarBorderColorDisabled = new Color(255, 255, 255);
        }

    }

    protected void paintForegroundEnabled(Graphics2D g, JComponent c, int width, int height) {
        this.paintButton(g, c, width, height, this.backgroundColorEnabled, this.backgroundArrowColorEnabled);
    }

    protected void paintForegroundDisabled(Graphics2D g, JComponent c, int width, int height) {
        this.paintButton(g, c, width, height, this.backgroundColorDisabled, this.backgroundArrowColorDisabled);
    }

    protected void paintForegroundMouseOver(Graphics2D g, JComponent c, int width, int height) {
        this.paintButton(g, c, width, height, this.backgroundColorMouseOver, this.backgroundArrowColorMouseOver);
    }

    protected void paintForegroundPressed(Graphics2D g, JComponent c, int width, int height) {
        this.paintButton(g, c, width, height, this.backgroundColorPressed, this.backgroundArrowColorPressed);
    }

    protected void paintButton(Graphics2D g, JComponent c, int width, int height, Paint background,
            Paint arrowBackground) {

        Paint trackColor = null;
        Paint scrollBarBorderColor = null;
        switch (this.state) {
        case FOREGROUND_ENABLED:
            trackColor = this.trackColorEnabled;
            scrollBarBorderColor = this.scrollBarBorderColorEnabled;
            break;
        case FOREGROUND_DISABLED:
            trackColor = this.trackColorDisabled;
            scrollBarBorderColor = this.scrollBarBorderColorDisabled;
            break;
        default:
            trackColor = this.trackColorEnabled;
            scrollBarBorderColor = this.scrollBarBorderColorEnabled;
        }
        this.paintTopButtonFiller(g, width, height, scrollBarBorderColor);
        this.paintBottomButtonFiller(g, width, height, trackColor);

        // background
        Shape s = this.createRoundRect(0, 0, width, height);
        g.setPaint(background);
        g.fill(s);

        // Arrow
        s = this.createArrow(0, 0, width, height);
        g.setPaint(arrowBackground);
        g.fill(s);
    }

    protected void paintTopButtonFiller(Graphics2D g, int width, int height, Paint backgroundPainter) {

        double radius = height / 2.0;
        int x = 0;
        int y = 0;

        this.path.reset();
        this.path.moveTo(x, y);
        this.path.lineTo(x + radius, y);
        this.path.curveTo(x + radius / 2.0, y, x, y + radius / 2.0, x, y + radius);
        this.path.curveTo(x, y + height - radius / 2.0, x + radius / 2.0, y + height, x + radius, y + height);
        this.path.lineTo(x, y + height);
        this.path.lineTo(x, y);

        g.setPaint(backgroundPainter);
        g.fill(this.path);

    }

    protected void paintBottomButtonFiller(Graphics2D g, int width, int height, Paint background) {

        double radius = height / 2.0;
        int x = width;
        int y = 0;

        this.path.reset();
        this.path.moveTo(x, y);
        this.path.lineTo(x - radius, y);
        this.path.curveTo(x - radius / 2.0, y, x, y + radius / 2.0, x, y + radius);
        this.path.curveTo(x, y + height - radius / 2.0, x - radius / 2.0, y + height, x - radius, y + height);
        this.path.lineTo(x, y + height);
        this.path.lineTo(x, y);

        g.setPaint(background);
        g.fill(this.path);

    }

    protected Shape createRoundRect(double x, double y, double w, double h) {

        double radius = h / 2.0;
        this.path.reset();

        // left top corner.
        this.path.moveTo(x + radius, y);
        this.path.curveTo(x + radius / 2.0, y, x, y + radius / 2.0, x, y + radius);

        // left bottom corner
        this.path.curveTo(x, y + h - radius / 2.0, x + radius / 2.0, y + h, x + radius, y + h);
        this.path.lineTo(x + w - radius, y + h);

        // right bottom corner
        this.path.curveTo(x + w - radius / 2.0, y + h, x + w, y + h - radius / 2.0, x + w, y + h - radius);

        // right top corner
        this.path.curveTo(x + w, y + radius / 2.0, x + w - radius / 2.0, y, x + w - radius, y);
        this.path.lineTo(x + radius, y);

        return this.path;
    }

    protected Path2D createArrow(double x, double y, double w, double h) {
        this.path.reset();

        double xcenter = x + w / 2.0 - 2;
        double ycenter = y + h / 2.0;

        this.path.moveTo(xcenter - 2, ycenter);
        this.path.lineTo(xcenter + 4, ycenter + 2.5);
        this.path.lineTo(xcenter + 4, ycenter - 2.5);
        this.path.closePath();

        return this.path;
    }

}
