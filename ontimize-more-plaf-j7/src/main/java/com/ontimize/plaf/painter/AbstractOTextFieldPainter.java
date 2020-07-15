package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;

import com.ontimize.plaf.OntimizeLookAndFeel;

/**
 * <pre>
 * Painter for the background, border and shape of the TextField (to configure border and background colors, painters, images, ...)
 *
 * In this case, we would like to be able to configure some TextFields in a different ways that other TextFields.
 * That is, for TextField which are embedded in Ontimize FormTitle classes and so on, we must be able to configure the same properties in
 * other way.
 *
 * For that reason, this abstract class is included. It indicates how painters must be painted, and also their configuration parameters.
 * However the configuration key is got using the method getComponentKeyName(), which will be defined in the non-abstract classes, that extends this one.
 * For instance, to be able to get a configuration key called something[X].Y:
 *   - OTextFieldPainter will return "TextField" => the readed configuration key will be: TextField[X].Y
 *   - OFormTitlePainter will return "\"FormTitle\"" => the readed configuration key will be: "FormTitle\"[X].Y
 *
 * (Typically, X has values as Enabled, Focused, Pressed, ... and Y becomes values like: backgroundPainter_bgPainter, backgroundPainter_border, ...)
 *
 *

 * &#64;author Imatia Innovation
 *
 * </pre>
 */
public abstract class AbstractOTextFieldPainter extends AbstractRegionPainter {

    public static final int BACKGROUND_DISABLED = 1;

    public static final int BACKGROUND_ENABLED = 2;

    public static final int BACKGROUND_FOCUSED = 3;

    public static final int BACKGROUND_REQUIRED = 222;

    public static final int BACKGROUND_DISABLED_REQUIRED = 223;

    public static final int BACKGROUND_FOCUSED_REQUIRED = 224;

    public static final int BORDER_DISABLED = 4;

    public static final int BORDER_FOCUSED = 5;

    public static final int BORDER_ENABLED = 6;

    public static final int BORDER_REQUIRED = 666;

    public static final int BORDER_FOCUSED_REQUIRED = 667;

    public static final int BORDER_DISABLED_REQUIRED = 668;

    /**
     * The number of pixels that compounds the border width of the component.
     */
    public static int numBorders = 4;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();

    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    // painters to fill the component
    protected Paint backgroundColorDisabled;

    protected Paint backgroundColorEnabled;

    protected Paint backgroundColorRequired;

    protected Paint backgroundColorFocused;

    protected Paint backgroundColorFocusedRequired;

    protected Paint backgroundColorDisabledRequired;

    // arrays to round the component (several rounded borders with degradation):
    protected Paint[] degradatedBorderColorEnabled;

    protected Paint[] degradatedBorderColorDisabled;

    protected Paint[] degradatedBorderColorDisabledRequired;

    protected Paint[] degradatedBorderColorFocused;

    protected Paint[] degradatedBorderColorFocusedRequired;

    protected Paint[] degradatedBorderColorRequired;

    protected Paint focusInnerShadow;

    protected Color[] degradatedEditorInnerBorderColor;

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of TextFieldPainter would be created. For the
    // moment at least,
    // however, all are created for each instance.
    protected Color color1;

    protected Color color2;

    protected Color color3;

    protected Color color4;

    protected Color color7;

    protected Color color8;

    protected Color color9;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    protected double radius;

    protected java.awt.Image paddLock = null;

    public static String imgUrl = "com/ontimize/plaf/images/paddlock.png";

    public AbstractOTextFieldPainter(int state, PaintContext ctx) {
        super(state, ctx);

        if (AbstractOTextFieldPainter.imgUrl != null) {
            URL url = this.getClass().getClassLoader().getResource(AbstractOTextFieldPainter.imgUrl);
            this.paddLock = Toolkit.getDefaultToolkit().getImage(url);
        }
    }

    @Override
    protected abstract String getComponentKeyName();

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        // populate componentColors array with colors calculated in
        // getExtendedCacheKeys call
        this.componentColors = extendedCacheKeys;

        Insets insets = c.getInsets();
        int x = insets.left;
        int y = insets.top;
        int cwidth = width - insets.left - insets.right;
        int cheight = height - insets.top - insets.bottom;

        // generate this entire method. Each state/bg/fg/border combo that has
        // been painted gets its own KEY and paint method.
        switch (this.state) {
            case BACKGROUND_DISABLED:
                this.paintBackgroundDisabled(g, c, x, y, cwidth, cheight);
                break;
            case BACKGROUND_DISABLED_REQUIRED:
                this.paintBackgroundDisabledRequired(g, c, x, y, cwidth, cheight);
                break;
            case BACKGROUND_ENABLED:
                this.paintBackgroundEnabled(g, c, x, y, cwidth, cheight);
                break;
            case BACKGROUND_FOCUSED:
                this.paintBackgroundFocused(g, c, x, y, cwidth, cheight);
                break;
            case BACKGROUND_FOCUSED_REQUIRED:
                this.paintBackgroundFocusedRequired(g, c, x, y, cwidth, cheight);
                break;
            case BACKGROUND_REQUIRED:
                this.paintBackgroundRequired(g, c, x, y, cwidth, cheight);
                break;
            case BORDER_DISABLED:
                this.paintBorderDisabled(g, c, 0, 0, width, height);
                break;
            case BORDER_DISABLED_REQUIRED:
                this.paintBorderDisabledRequired(g, c, 0, 0, width, height);
                break;
            case BORDER_FOCUSED:
                this.paintBorderFocused(g, c, 0, 0, width, height);
                break;
            case BORDER_ENABLED:
                this.paintBorderEnabled(g, c, 0, 0, width, height);
                break;
            case BORDER_REQUIRED:
                this.paintBorderRequired(g, c, 0, 0, width, height);
                break;
            case BORDER_FOCUSED_REQUIRED:
                this.paintBorderFocusedRequired(g, c, 0, 0, width, height);
                break;

        }
    }

    @Override
    protected Object[] getExtendedCacheKeys(JComponent c) {
        Object[] extendedCacheKeys = null;
        switch (this.state) {
            case BACKGROUND_ENABLED:
                extendedCacheKeys = new Object[] {
                        this.getComponentColor(c, "background", this.color2, 0.0f, 0.0f, 0) };
                break;
            case BORDER_FOCUSED:
                extendedCacheKeys = new Object[] {
                        this.getComponentColor(c, "background", this.color9, 0.004901961f, -0.19999999f, 0),
                        this.getComponentColor(c, "background", this.color2, 0.0f, 0.0f, 0) };
                break;
            case BORDER_ENABLED:
                extendedCacheKeys = new Object[] {
                        this.getComponentColor(c, "background", this.color9, 0.004901961f, -0.19999999f, 0),
                        this.getComponentColor(c, "background", this.color2, 0.0f, 0.0f, 0) };
                break;
        }
        return extendedCacheKeys;

    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    @Override
    protected void initializeDefaultColors() {
        this.color1 = this.decodeColor("nimbusBlueGrey", -0.015872955f, -0.07995863f, 0.15294117f, 0);
        this.color2 = this.decodeColor("nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        this.color3 = this.decodeColor("nimbusBlueGrey", -0.006944418f, -0.07187897f, 0.06666666f, 0);
        this.color4 = this.decodeColor("nimbusBlueGrey", 0.007936537f, -0.07826825f, 0.10588235f, 0);
        this.color7 = this.decodeColor("nimbusBlueGrey", -0.027777791f, -0.0965403f, -0.18431371f, 0);
        this.color8 = this.decodeColor("nimbusBlueGrey", 0.055555582f, -0.1048766f, -0.05098039f, 0);
        this.color9 = this.decodeColor("nimbusLightBackground", 0.6666667f, 0.004901961f, -0.19999999f, 0);
    }

    /**
     * Get configuration properties to be used in this painter.
     *
     */
    @Override
    protected void init() {

        Object oRadius = UIManager.getLookAndFeelDefaults().get("Application.radius");
        if (oRadius instanceof Double) {
            this.radius = ((Double) oRadius).doubleValue();
        } else {
            this.radius = OntimizeLookAndFeel.defaultRadius;
        }

        // disable:
        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].background");
        if (obj instanceof Paint) {
            this.backgroundColorDisabled = (Paint) obj;
        } else {
            this.backgroundColorDisabled = this.color1;
        }

        // disable + required:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled+Required].background");
        if (obj instanceof Paint) {
            this.backgroundColorDisabledRequired = (Paint) obj;
        } else {
            this.backgroundColorDisabledRequired = this.color1;
        }

        // enable:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].background");
        if (obj instanceof Paint) {
            this.backgroundColorEnabled = (Paint) obj;
        } else {
            this.backgroundColorEnabled = this.color2;
        }

        // focused:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused].background");
        if (obj instanceof Paint) {
            this.backgroundColorFocused = (Paint) obj;
        } else {
            this.backgroundColorFocused = this.color2;
        }

        // required:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Required].background");
        if (obj instanceof Paint) {
            this.backgroundColorRequired = (Paint) obj;
        } else {
            this.backgroundColorRequired = this.color9;
        }

        // focused + required:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused+Required].background");
        if (obj instanceof Paint) {
            this.backgroundColorFocusedRequired = (Paint) obj;
        } else {
            this.backgroundColorFocusedRequired = this.color9;
        }

        // BORDER COLORS
        // enable:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorEnabled = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorEnabled = (Paint[]) obj;
        } else {
            this.degradatedBorderColorEnabled = new Color[] { this.color7,
                    this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
        }

        // required:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Required].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorRequired = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorRequired = (Paint[]) obj;
        } else {
            this.degradatedBorderColorRequired = new Color[] { this.color7,
                    this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
        }

        // disable:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorDisabled = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorDisabled = (Paint[]) obj;
        } else {
            this.degradatedBorderColorDisabled = new Color[] { this.color3,
                    this.decodeColor(this.color3, this.color4, 0.5f), this.color4 };
        }

        // disable + required
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled+Required].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorDisabledRequired = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorDisabledRequired = (Paint[]) obj;
        } else {
            this.degradatedBorderColorDisabledRequired = new Color[] { this.color7,
                    this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
        }

        // Focused:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorFocused = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorFocused = (Paint[]) obj;
        } else {
            this.degradatedBorderColorFocused = new Color[] { this.color7,
                    this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
        }

        // Focused + required
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused+Required].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorFocusedRequired = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorFocusedRequired = (Paint[]) obj;
        } else {
            this.degradatedBorderColorFocusedRequired = new Color[] { this.color7,
                    this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused].innerShadow");
        if (obj instanceof Paint) {
            this.focusInnerShadow = (Paint) obj;
        } else {
            this.focusInnerShadow = new Color(0xCACACA);
        }

        // Editor (inner border):
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".editorInnerBorder");
        if (obj instanceof Color) {
            this.degradatedEditorInnerBorderColor = new Color[] { (Color) obj, (Color) obj };
        } else if ((obj instanceof Color[]) && (((Color[]) obj).length == 2)) {
            this.degradatedEditorInnerBorderColor = (Color[]) obj;
        } else {
            this.degradatedEditorInnerBorderColor = new Color[] { new Color(0xBAf1FE), new Color(0xF3FdFF) };
        }

    }

    protected void paintBackgroundDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {

        this.drawBackground(g, c, x, y, width, height, this.backgroundColorDisabled);
    }

    protected void paintBackgroundDisabledRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {

        this.drawBackground(g, c, x, y, width, height, this.backgroundColorDisabledRequired);
    }

    protected void paintBackgroundEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {

        if (c.getParent() instanceof JTable) {
            this.drawEditorBackground(g, c);
        } else {
            this.drawBackground(g, c, x, y, width, height, this.backgroundColorEnabled);
        }

    }

    protected void paintBackgroundRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {

        this.drawBackground(g, c, x, y, width, height, this.backgroundColorRequired);

    }

    protected void paintBackgroundFocused(Graphics2D g, JComponent c, int x, int y, int width, int height) {

        this.drawBackground(g, c, x, y, width, height, this.backgroundColorFocused);
    }

    protected void paintBackgroundFocusedRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {

        this.drawBackground(g, c, x, y, width, height, this.backgroundColorFocusedRequired);
    }

    protected Paint getBackgroundForState(JComponent c) {
        Paint paint = null;
        switch (this.state) {
            case BACKGROUND_DISABLED:
                paint = this.getBackgroundColor(c, this.backgroundColorDisabled);
                break;
            case BACKGROUND_DISABLED_REQUIRED:
                paint = this.getBackgroundColor(c, this.backgroundColorDisabledRequired);
                break;
            case BACKGROUND_ENABLED:
                paint = this.getBackgroundColor(c, this.backgroundColorEnabled);
                break;
            case BACKGROUND_REQUIRED:
                paint = this.getBackgroundColor(c, this.backgroundColorRequired);
                break;
            case BACKGROUND_FOCUSED:
                paint = this.getBackgroundColor(c, this.backgroundColorFocused);
                break;
            case BACKGROUND_FOCUSED_REQUIRED:
                paint = this.getBackgroundColor(c, this.backgroundColorFocusedRequired);
                break;
            case BORDER_DISABLED:
                paint = this.getBackgroundColor(c, this.backgroundColorDisabled);
                break;
            case BORDER_DISABLED_REQUIRED:
                paint = this.backgroundColorDisabledRequired != null ? this.backgroundColorDisabledRequired
                        : c.getBackground();
                break;
            case BORDER_ENABLED:
                paint = this.getBackgroundColor(c, this.backgroundColorEnabled);
                break;
            case BORDER_REQUIRED:
                paint = this.getBackgroundColor(c, this.backgroundColorRequired);
                break;
            case BORDER_FOCUSED:
                paint = this.getBackgroundColor(c, this.backgroundColorFocused);
                break;
            case BORDER_FOCUSED_REQUIRED:
                paint = this.backgroundColorFocusedRequired != null ? this.backgroundColorFocusedRequired
                        : c.getBackground();
                break;
            default:
                paint = c != null ? c.getBackground() : null;
        }

        return paint;
    }

    protected Paint getBackgroundColor(JComponent c, Paint defaultColor) {
        if (c != null) {
            if ((c.getBackground() != null) && c.isEnabled()) {
                return c.getBackground();
            }
        }
        return defaultColor;
    }

    protected Paint getBorderForState(JComponent c) {
        Paint paint = null;
        switch (this.state) {
            case BORDER_DISABLED:
                paint = this.degradatedBorderColorDisabled[0];
                break;
            case BORDER_DISABLED_REQUIRED:
                paint = this.degradatedBorderColorDisabledRequired[0];
                break;
            case BORDER_ENABLED:
                paint = this.degradatedBorderColorEnabled[0];
                break;
            case BORDER_FOCUSED:
                paint = this.degradatedBorderColorFocused[0];
                break;
            case BORDER_FOCUSED_REQUIRED:
                paint = this.degradatedBorderColorFocusedRequired[0];
                break;
            default:
                paint = this.degradatedBorderColorEnabled[0];
        }
        return paint;
    }

    protected void paintBorderDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        if ((this.degradatedBorderColorDisabled != null) && (this.degradatedBorderColorDisabled.length > 0)) {
            this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorDisabled);
            g.drawImage(this.paddLock, 1, (int) (this.decodeY(2.0f) - 9), 10, 10, null);
        }
    }

    protected void paintBorderDisabledRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        if ((this.degradatedBorderColorDisabledRequired != null)
                && (this.degradatedBorderColorDisabledRequired.length > 0)) {
            this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorDisabledRequired);
            g.drawImage(this.paddLock, 1, (int) (this.decodeY(2.0f) - 9), 10, 10, null);
        }
    }

    protected void paintBorderFocused(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        if ((this.degradatedBorderColorFocused != null) && (this.degradatedBorderColorFocused.length > 0)) {
            this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorFocused);
            this.drawInnerShadowBorder(g, this.focusInnerShadow);
        }
    }

    protected void paintBorderFocusedRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        if ((this.degradatedBorderColorFocusedRequired != null)
                && (this.degradatedBorderColorFocusedRequired.length > 0)) {
            this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorFocusedRequired);
        }
    }

    protected void paintBorderEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        if ((this.degradatedBorderColorEnabled != null) && (this.degradatedBorderColorEnabled.length > 0)) {
            this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorEnabled);
        }
    }

    protected void paintBorderRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        if ((this.degradatedBorderColorRequired != null) && (this.degradatedBorderColorRequired.length > 0)) {
            this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorRequired);
        }
    }

    protected Rectangle2D decodeRect() {
        this.rect.setRect(this.decodeX(0.0f), // x
                this.decodeY(0.0f), // y
                this.decodeX(3.0f) - this.decodeX(0.0f), // width
                this.decodeY(3.0f) - this.decodeY(0.0f)); // height
        return this.rect;
    }

    protected Path2D decodeTopInnerShadowRect() {
        this.path.reset();
        this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(3.0f), this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(3.0f) - 4, this.decodeY(0.0f) + 4);
        this.path.lineTo(this.decodeX(0.0f) + 4, this.decodeY(0.0f) + 4);
        this.path.closePath();
        return this.path;
    }

    protected Path2D decodeBottomInnerShadowRect() {
        this.path.reset();
        this.path.moveTo(this.decodeX(0.0f), this.decodeY(3.0f) - 1);
        this.path.lineTo(this.decodeX(3.0f), this.decodeY(3.0f) - 1);
        this.path.lineTo(this.decodeX(3.0f) - 4, this.decodeY(3.0f) - 5);
        this.path.lineTo(this.decodeX(0.0f) + 4, this.decodeY(3.0f) - 5);
        this.path.closePath();
        return this.path;
    }

    protected Path2D decodeLeftInnerShadowRect() {
        this.path.reset();
        this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(0.0f) + 4, this.decodeY(0.0f) + 4);
        this.path.lineTo(this.decodeX(0.0f) + 4, this.decodeY(3.0f) - 4);
        this.path.lineTo(this.decodeX(0.0f), this.decodeY(3.0f));
        this.path.closePath();
        return this.path;
    }

    protected Path2D decodeRightInnerShadowRect() {
        this.path.reset();
        this.path.moveTo(this.decodeX(3.0f) - 1, this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(3.0f) - 1, this.decodeY(3.0f));
        this.path.lineTo(this.decodeX(3.0f) - 5, this.decodeY(3.0f) - 4);
        this.path.lineTo(this.decodeX(3.0f) - 5, this.decodeY(0.0f) + 4);
        this.path.closePath();
        return this.path;
    }

    protected void drawBackground(Graphics2D g, JComponent c, int x, int y, int width, int height, Paint color) {
        Paint previousPaint = g.getPaint();
        RenderingHints rh = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xx = this.getLeftX();
        double xx2 = this.getRightX();
        double yy = this.decodeY(0.0f) + AbstractOTextFieldPainter.numBorders;
        double w = xx2 - xx;
        double h = this.decodeY(3.0f) - (2 * AbstractOTextFieldPainter.numBorders);

        g.setPaint(this.getBackgroundForState(c));
        g.fillRect(this.intValue(xx), this.intValue(yy), this.intValue(w), this.intValue(h));

        this.defineBorderFillers(g, c, 0, 0, c.getWidth(), c.getHeight());

        g.setPaint(previousPaint);
        g.setRenderingHints(rh);
    }

    protected void drawEditorBackground(Graphics2D g, JComponent c) {
        this.rect = this.decodeRect();
        g.setPaint(this.backgroundColorEnabled);
        g.fill(this.rect);

        Shape s = this.decodeLeftInnerShadowRect();
        g.setPaint(this.decodeLeftInnerShadowGradient(s));
        g.fill(s);

        s = this.decodeRightInnerShadowRect();
        g.setPaint(this.decodeRightInnerShadowGradient(s));
        g.fill(s);

        s = this.decodeTopInnerShadowRect();
        g.setPaint(this.decodeTopInnerShadowGradient(s));
        g.fill(s);

        s = this.decodeBottomInnerShadowRect();
        g.setPaint(this.decodeBottomInnerShadowGradient(s));
        g.fill(s);
    }

    /**
     * This method returns, if it is configured by the user, the value for corner radius.
     * @return
     */
    protected double getRadius() {
        return this.radius;
    }

    /**
     * This method returns the maximum radius available for the component. The maximum radius is
     * calculated from the height of the component and it provokes that the field corners looks like a
     * semicircle.
     * @return
     */
    protected double getMaximumRadius() {
        return (this.decodeY(3.0f) - 1 - (2 * AbstractOTextFieldPainter.numBorders)) / 2.0;
    }

    /**
     * Returns the x coordinate where starts the semicircle of the left of the component.
     * @return
     */
    protected double getLeftX() {
        double d = this.decodeX(0.0f) + this.getMaximumRadius() + AbstractOTextFieldPainter.numBorders;
        d = Math.round(d);
        return d;
    }

    /**
     * Returns the x coordinate where starts the semicircle of the right of the component.
     * @return
     */
    protected double getRightX() {
        double d = this.decodeX(3.0f) - this.getMaximumRadius() - AbstractOTextFieldPainter.numBorders;
        d = Math.round(d);
        return d;
    }

    protected void drawDegradatedBorders(Graphics2D g, JComponent c, int x, int y, int width, int height,
            Paint[] colors) {
        Paint previousPaint = g.getPaint();
        RenderingHints rh = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xx = this.getLeftX();
        double yy = this.decodeY(0.0f) + AbstractOTextFieldPainter.numBorders;
        double h = height - (2 * AbstractOTextFieldPainter.numBorders);
        double w = this.getRightX();

        for (int i = 1; i <= colors.length; i++) {
            double yyy = yy - i;
            double hh = (h + (2 * i)) - 1;
            Shape s = this.decodeBorderPath(xx, yyy, w, hh, i - 1);
            g.setPaint(colors[i - 1]);
            g.draw(s);
        }

        g.setPaint(previousPaint);
        g.setRenderingHints(rh);

    }

    protected void drawInnerShadowBorder(Graphics2D g, Paint color) {

        Paint previousPaint = g.getPaint();
        RenderingHints rh = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double radius = this.getRadius();
        double x = (this.decodeX(0.0f) + this.getMaximumRadius() + AbstractOTextFieldPainter.numBorders) - 2;
        double x_arc = (this.decodeX(0.0f) + AbstractOTextFieldPainter.numBorders + radius) - 2;
        if (x_arc < x) {
            x = x_arc;
        }
        double x2 = (this.decodeX(3.0f) - AbstractOTextFieldPainter.numBorders - this.getMaximumRadius()) + 2;
        x_arc = (this.decodeX(3.0f) - AbstractOTextFieldPainter.numBorders - radius) + 2;
        if (x_arc > x2) {
            x2 = x_arc;
        }
        double y2 = this.decodeY(0.0f) + AbstractOTextFieldPainter.numBorders;
        this.path.reset();
        this.path.moveTo(x, y2);
        this.path.lineTo(x2, y2);

        g.setPaint(color);
        g.draw(this.path);

        g.setPaint(previousPaint);
        g.setRenderingHints(rh);
    }

    protected void defineBorderFillers(Graphics2D g, JComponent c, int x, int y, int width, int height) {

        Paint previousPaint = g.getPaint();
        RenderingHints rh = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Paint filler = this.getBackgroundForState(c);
        g.setPaint(filler);

        // Left filler...
        Shape s = this.decodeLeftBorderFillerPath(height);
        g.fill(s);

        // Right filler...
        s = this.decodeRightBorderFillerPath(height);
        g.fill(s);

        g.setPaint(previousPaint);
        g.setRenderingHints(rh);
    }

    protected Shape decodeLeftBorderFillerPath(int height) {
        double x = this.getLeftX();
        double radius = this.getRadius();
        double x_arc = this.decodeX(0.0f) + AbstractOTextFieldPainter.numBorders + radius;
        if (x_arc > x) {
            x_arc = x;
            radius = this.getMaximumRadius();
        }
        double y = this.decodeY(0.0f) + AbstractOTextFieldPainter.numBorders;
        double h = height - (2 * AbstractOTextFieldPainter.numBorders);

        this.path.reset();
        this.path.moveTo(this.intValue(x), y);
        this.path.lineTo(x_arc, y);
        this.path.curveTo(x_arc - (radius / 2.0), y, x_arc - radius, y + (radius / 2.0), x_arc - radius, y + radius);
        this.path.lineTo(x_arc - radius, (y + h) - radius);
        this.path.curveTo(x_arc - radius, (y + h) - (radius / 2.0), x_arc - (radius / 2.0), y + h, x_arc, y + h);
        this.path.lineTo(this.intValue(x), y + h);
        this.path.closePath();

        return this.path;
    }

    protected Shape decodeRightBorderFillerPath(int height) {

        double x = this.getRightX();
        double radius = this.getRadius();
        double x_arc = this.decodeX(3.0f) - AbstractOTextFieldPainter.numBorders - radius;
        if (x_arc < x) {
            x_arc = x;
            radius = this.getMaximumRadius();
        }
        double y = this.decodeY(0.0f) + AbstractOTextFieldPainter.numBorders;
        double h = height - (2 * AbstractOTextFieldPainter.numBorders);

        this.path.reset();
        this.path.moveTo(this.intValue(x), y);
        this.path.lineTo(x_arc, y);
        this.path.curveTo(x_arc + (radius / 2.0), y, x_arc + radius, y + (radius / 2.0), x_arc + radius, y + radius);
        this.path.lineTo(x_arc + radius, (y + h) - radius);
        this.path.curveTo(x_arc + radius, (y + h) - (radius / 2.0), x_arc + (radius / 2.0), y + h, x_arc, y + h);
        this.path.lineTo(this.intValue(x), y + h);
        this.path.closePath();

        return this.path;
    }

    /**
     * Decodes the border component path.
     * @param x The x coordinate where starts the semicircle of the left
     * @param y The y coordinate of the top.
     * @param w The x coordinate where starts the semicircle of the right.
     * @param h The height of the field.
     * @param borderIndex the index of the border.
     * @return
     */
    protected Shape decodeBorderPath(double x, double y, double w, double h, int borderIndex) {
        double radius = this.getRadius();
        double x_arc = this.decodeX(0.0f) + AbstractOTextFieldPainter.numBorders + radius;
        if (x_arc > x) {
            x_arc = x;
            radius = this.getMaximumRadius();
        }

        this.path.reset();
        this.path.moveTo(this.intValue(x), y);
        this.path.lineTo(x_arc, y);
        this.path.curveTo(x_arc - (radius / 2.0), y, x_arc - radius, y + (radius / 2.0), x_arc - radius - borderIndex,
                y + radius);
        this.path.lineTo(x_arc - radius - borderIndex, (y + h) - radius);
        this.path.curveTo(x_arc - radius - borderIndex, (y + h) - (radius / 2.0), x_arc - (radius / 2.0), y + h, x_arc,
                y + h);
        this.path.lineTo(this.intValue(x), y + h);

        this.path.lineTo(this.intValue(w), y + h);

        x_arc = this.decodeX(3.0f) - 1 - AbstractOTextFieldPainter.numBorders - radius;
        if (x_arc < w) {
            x_arc = w;
            radius = this.getMaximumRadius();
        }

        this.path.lineTo(x_arc, y + h);
        this.path.curveTo(x_arc + (radius / 2.0), y + h, x_arc + radius, (y + h) - (radius / 2.0),
                x_arc + radius + borderIndex, (y + h) - radius);
        this.path.lineTo(x_arc + radius + borderIndex, y + radius);
        this.path.curveTo(x_arc + radius + borderIndex, y + (radius / 2.0), x_arc + (radius / 2.0), y, x_arc, y);

        this.path.lineTo(this.intValue(x), y);
        this.path.closePath();

        return this.path;
    }

    protected Paint decodeTopInnerShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(x, y, x, y + h, new float[] { 0.0f, 1.0f },
                new Color[] { this.degradatedEditorInnerBorderColor[0], this.degradatedEditorInnerBorderColor[1] });
    }

    protected Paint decodeBottomInnerShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(x, y, x, y + h, new float[] { 0.0f, 1.0f },
                new Color[] { this.degradatedEditorInnerBorderColor[1], this.degradatedEditorInnerBorderColor[0] });
    }

    protected Paint decodeLeftInnerShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        return this.decodeGradient(x, y, x + w, y, new float[] { 0.0f, 1.0f },
                new Color[] { this.degradatedEditorInnerBorderColor[0], this.degradatedEditorInnerBorderColor[1] });
    }

    protected Paint decodeRightInnerShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        return this.decodeGradient(x, y, x + w, y, new float[] { 0.0f, 1.0f },
                new Color[] { this.degradatedEditorInnerBorderColor[1], this.degradatedEditorInnerBorderColor[0] });
    }

}
