package com.ontimize.plaf.painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.utils.DerivedColor;

/**
 * <pre>
 *
 * Painter for the background of the buttons (to configure border, painters, images, ...)
 *
 * Beziér functions are needed in order to be able to paint small buttons (as table buttons or field buttons) in the same way that others.
 *
 * In this case, we would like to be able to configure some panels in a different ways that other panels.
 * That is, for panel which are embedded in Ontimize Form classes or in Ontimize FormExt classes, we must be able to configure the same properties in
 * other way.
 *
 * For that reason, this abstract class is included. It indicates how painters must be painted, and also their configuration parameters.
 * However the configuration key is got using the method getComponentKeyName(), which will be defined in the non-abstract classes, that extends this one.
 * For instance, to be able to get a configuration key called something[X].Y:
 *   - OPanelPainter will return "Panel" => the readed configuration key will be: Panel[X].Y
 *   - OFormPainter will return "\"Form\"" => the readed configuration key will be: "Form\"[X].Y
 *   - OFormExtlPainter will return "\"FormExt\"" => the readed configuration key will be: "FormExt\"[X].Y
 *
 * (Typically, X has values as Enabled, Focused, Pressed, ... and Y becomes values like: backgroundPainter_bgPainter, backgroundPainter_border, ...)
 *
 *
 * @author Imatia Innovation
 *
 * </pre>
 */
public abstract class AbstractOButtonPainter extends AbstractRegionPainter {

    public static final int BACKGROUND_DEFAULT = 1;
    public static final int BACKGROUND_DEFAULT_FOCUSED = 2;
    public static final int BACKGROUND_MOUSEOVER_DEFAULT = 3;
    public static final int BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED = 4;
    public static final int BACKGROUND_PRESSED_DEFAULT = 5;
    public static final int BACKGROUND_PRESSED_DEFAULT_FOCUSED = 6;
    public static final int BACKGROUND_DISABLED = 7;
    public static final int BACKGROUND_ENABLED = 8;
    public static final int BACKGROUND_FOCUSED = 9;
    public static final int BACKGROUND_MOUSEOVER = 10;
    public static final int BACKGROUND_MOUSEOVER_FOCUSED = 11;
    public static final int BACKGROUND_PRESSED = 12;
    public static final int BACKGROUND_PRESSED_FOCUSED = 13;

    protected int state; // refers to one of the static final ints above
    protected PaintContext ctx;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();
    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
    protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);
    protected Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of ButtonPainter would be created. For the
    // moment at least,
    // however, all are created for each instance.
    protected boolean initColors = false;

    protected Color color1 = this.decodeColor("nimbusBlueGrey", -0.027777791f, -0.06885965f, -0.36862746f, -190);
    protected Color color2 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.34585923f, -0.007843137f, 0);
    protected Color color3 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.095173776f, -0.25882354f, 0);
    protected Color color4 = this.decodeColor("nimbusBase", 0.004681647f, -0.6197143f, 0.43137252f, 0);
    protected Color color5 = this.decodeColor("nimbusBase", 0.004681647f, -0.5766426f, 0.38039213f, 0);
    protected Color color6 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.43866998f, 0.24705881f, 0);
    protected Color color7 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.46404046f, 0.36470586f, 0);
    protected Color color8 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.47761154f, 0.44313723f, 0);
    protected Color color9 = this.decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    protected Color color10 = this.decodeColor("nimbusBase", 0.0013483167f, -0.1769987f, -0.12156865f, 0);
    protected Color color11 = this.decodeColor("nimbusBase", 0.059279382f, 0.3642857f, -0.43529415f, 0);
    protected Color color12 = this.decodeColor("nimbusBase", 0.004681647f, -0.6198413f, 0.43921566f, 0);
    protected Color color13 = this.decodeColor("nimbusBase", -0.0017285943f, -0.5822163f, 0.40392154f, 0);
    protected Color color14 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.4555341f, 0.3215686f, 0);
    protected Color color15 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.47698414f, 0.43921566f, 0);
    protected Color color16 = this.decodeColor("nimbusBase", -0.06415892f, -0.5455182f, 0.45098037f, 0);
    protected Color color17 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -95);
    protected Color color18 = this.decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    protected Color color19 = this.decodeColor("nimbusBase", -3.528595E-5f, 0.018606722f, -0.23137257f, 0);
    protected Color color20 = this.decodeColor("nimbusBase", -4.2033195E-4f, -0.38050595f, 0.20392156f, 0);
    protected Color color21 = this.decodeColor("nimbusBase", 0.001903832f, -0.29863563f, 0.1490196f, 0);
    protected Color color22 = this.decodeColor("nimbusBase", 0.0f, 0.0f, 0.0f, 0);
    protected Color color23 = this.decodeColor("nimbusBase", 0.0018727183f, -0.14126986f, 0.15686274f, 0);
    protected Color color24 = this.decodeColor("nimbusBase", 8.9377165E-4f, -0.20852983f, 0.2588235f, 0);
    protected Color color25 = this.decodeColor("nimbusBlueGrey", -0.027777791f, -0.06885965f, -0.36862746f, -232);
    protected Color color26 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.06766917f, 0.07843137f, 0);
    protected Color color27 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.06484103f, 0.027450979f, 0);
    protected Color color28 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.08477524f, 0.16862744f, 0);
    protected Color color29 = this.decodeColor("nimbusBlueGrey", -0.015872955f, -0.080091536f, 0.15686274f, 0);
    protected Color color30 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);
    protected Color color31 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.07052632f, 0.1372549f, 0);
    protected Color color32 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.070878744f, 0.14509803f, 0);
    protected Color color33 = this.decodeColor("nimbusBlueGrey", -0.055555522f, -0.05356429f, -0.12549019f, 0);
    protected Color color34 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.0147816315f, -0.3764706f, 0);
    protected Color color35 = this.decodeColor("nimbusBlueGrey", 0.055555582f, -0.10655806f, 0.24313724f, 0);
    protected Color color36 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.09823123f, 0.2117647f, 0);
    protected Color color37 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.0749532f, 0.24705881f, 0);
    protected Color color38 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    protected Color color39 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.020974077f, -0.21960783f, 0);
    protected Color color40 = this.decodeColor("nimbusBlueGrey", 0.0f, 0.11169591f, -0.53333336f, 0);
    protected Color color41 = this.decodeColor("nimbusBlueGrey", 0.055555582f, -0.10658931f, 0.25098038f, 0);
    protected Color color42 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.098526314f, 0.2352941f, 0);
    protected Color color43 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.07333623f, 0.20392156f, 0);
    protected Color color44 = new Color(245, 250, 255, 160);
    protected Color color45 = this.decodeColor("nimbusBlueGrey", 0.055555582f, 0.8894737f, -0.7176471f, 0);
    protected Color color46 = this.decodeColor("nimbusBlueGrey", 0.0f, 5.847961E-4f, -0.32156864f, 0);
    protected Color color47 = this.decodeColor("nimbusBlueGrey", -0.00505054f, -0.05960039f, 0.10196078f, 0);
    protected Color color48 = this.decodeColor("nimbusBlueGrey", -0.008547008f, -0.04772438f, 0.06666666f, 0);
    protected Color color49 = this.decodeColor("nimbusBlueGrey", -0.0027777553f, -0.0018306673f, -0.02352941f, 0);
    protected Color color50 = this.decodeColor("nimbusBlueGrey", -0.0027777553f, -0.0212406f, 0.13333333f, 0);
    protected Color color51 = this.decodeColor("nimbusBlueGrey", 0.0055555105f, -0.030845039f, 0.23921567f, 0);

    protected float defaultAlphaDefaultPercent = 0.5f;
    protected float alphaDefaultPercent;
    protected float defaultAlphaFocusedDefaultPercent = 0.5f;
    protected float alphaFocusedDefaultPercent;
    protected float defaultAlphaMouseOverDefaultPercent = 0.5f;
    protected float alphaMouseOverDefaultPercent;
    protected float defaultAlphaMouseOverDefaultFocusedPercent = 0.5f;
    protected float alphaMouseOverDefaultFocusedPercent;
    protected float defaultAlphaPressedDefaultPercent = 0.5f;
    protected float alphaPressedDefaultPercent;
    protected float defaultAlphaPressedDefaultFocusedPercent = 0.5f;
    protected float alphaPressedDefaultFocusedPercent;
    protected float defaultAlphaDisabledPercent = 0.5f;
    protected float alphaDisabledPercent;
    protected float defaultAlphaEnabledPercent = 0.3f;
    protected float alphaEnabledPercent;
    protected float defaultAlphaFocusedPercent = 0.3f;
    protected float alphaFocusedPercent;
    protected float defaultAlphaMouseOverPercent = 0.5f;
    protected float alphaMouseOverPercent;
    protected float defaultAlphaMouseOverFocusedPercent = 0.5f;
    protected float alphaMouseOverFocusedPercent;
    protected float defaultAlphaPressedPercent = 0.5f;
    protected float alphaPressedPercent;
    protected float defaultAlphaPressedFocusedPercent = 0.5f;
    protected float alphaPressedFocusedPercent;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public AbstractOButtonPainter(int state, PaintContext ctx) {
        this.state = state;
        this.ctx = ctx;
    }

    @Override
    protected void init() {

        Object obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Disabled].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaDisabledPercent = ((Number) obj).floatValue();
        } else {
            this.alphaDisabledPercent = this.defaultAlphaDisabledPercent;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Enabled].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaEnabledPercent = ((Number) obj).floatValue();
        } else {
            this.alphaEnabledPercent = this.defaultAlphaEnabledPercent;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Focused].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaFocusedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaFocusedPercent = this.defaultAlphaFocusedPercent;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[MouseOver].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaMouseOverPercent = ((Number) obj).floatValue();
        } else {
            this.alphaMouseOverPercent = this.defaultAlphaMouseOverPercent;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Focused+MouseOver].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaMouseOverFocusedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaMouseOverFocusedPercent = this.defaultAlphaMouseOverFocusedPercent;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Pressed].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaPressedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaPressedPercent = this.defaultAlphaPressedPercent;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Focused+Pressed].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaPressedFocusedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaPressedFocusedPercent = this.defaultAlphaPressedFocusedPercent;
        }

    };

    @Override
    protected abstract String getComponentKeyName();

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        // populate componentColors array with colors calculated in
        // getExtendedCacheKeys call
        this.componentColors = extendedCacheKeys;
        // generate this entire method. Each state/bg/fg/border combo that has
        // been painted gets its own KEY and paint method.
        switch (this.state) {
        case BACKGROUND_DEFAULT:
            this.paintBackgroundDefault(g);
            break;
        case BACKGROUND_DEFAULT_FOCUSED:
            this.paintBackgroundDefaultAndFocused(g);
            break;
        case BACKGROUND_MOUSEOVER_DEFAULT:
            this.paintBackgroundMouseOverAndDefault(g);
            break;
        case BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED:
            this.paintBackgroundMouseOverAndDefaultAndFocused(g);
            break;
        case BACKGROUND_PRESSED_DEFAULT:
            this.paintBackgroundPressedAndDefault(g);
            break;
        case BACKGROUND_PRESSED_DEFAULT_FOCUSED:
            this.paintBackgroundPressedAndDefaultAndFocused(g);
            break;
        case BACKGROUND_DISABLED:
            this.paintBackgroundDisabled(g);
            break;
        case BACKGROUND_ENABLED:
            this.paintBackgroundEnabled(g);
            break;
        case BACKGROUND_FOCUSED:
            this.paintBackgroundFocused(g);
            break;
        case BACKGROUND_MOUSEOVER:
            this.paintBackgroundMouseOver(g);
            break;
        case BACKGROUND_MOUSEOVER_FOCUSED:
            this.paintBackgroundMouseOverAndFocused(g);
            break;
        case BACKGROUND_PRESSED:
            this.paintBackgroundPressed(g);
            break;
        case BACKGROUND_PRESSED_FOCUSED:
            this.paintBackgroundPressedAndFocused(g);
            break;
        }
    }

    protected com.ontimize.plaf.utils.DerivedColor getDerivedColor(Color parent, float hOffset, float sOffset,
            float bOffset, int aOffset) {
        DerivedColor color = new DerivedColor.UIResource(parent, hOffset, sOffset, bOffset, aOffset);
        color.rederiveColor();
        return color;
    }

    @Override
    protected Object[] getExtendedCacheKeys(JComponent c) {
        // FIXME Find out the way to initialize colors and to make that L&F
        // reinstallation functions.
        // if (!initColors && c!=null){
        this.initColors = true;
        Color background = c.getBackground();
        this.color1 = this.getDerivedColor(background, -0.027777791f, -0.06885965f, -0.36862746f, -190);
        this.color17 = this.getDerivedColor(background, 0.0f, -0.110526316f, 0.25490195f, -95);
        this.color25 = this.getDerivedColor(background, -0.027777791f, -0.06885965f, -0.36862746f, -232);
        this.color26 = this.getDerivedColor(background, 0.0f, -0.06766917f, 0.07843137f, 0);
        this.color27 = this.getDerivedColor(background, 0.0f, -0.06484103f, 0.027450979f, 0);
        this.color28 = this.getDerivedColor(background, 0.0f, -0.08477524f, 0.16862744f, 0);
        this.color29 = this.getDerivedColor(background, -0.015872955f, -0.080091536f, 0.15686274f, 0);
        this.color30 = this.getDerivedColor(background, 0.0f, -0.07016757f, 0.12941176f, 0);
        this.color31 = this.getDerivedColor(background, 0.0f, -0.07052632f, 0.1372549f, 0);
        this.color32 = this.getDerivedColor(background, 0.0f, -0.070878744f, 0.14509803f, 0);
        this.color33 = this.getDerivedColor(background, -0.055555522f, -0.05356429f, -0.12549019f, 0);
        this.color34 = this.getDerivedColor(background, 0.0f, -0.0147816315f, -0.3764706f, 0);
        this.color35 = this.getDerivedColor(background, 0.055555582f, -0.10655806f, 0.24313724f, 0);
        this.color36 = this.getDerivedColor(background, 0.0f, -0.09823123f, 0.2117647f, 0);
        this.color37 = this.getDerivedColor(background, 0.0f, -0.0749532f, 0.24705881f, 0);
        this.color38 = this.getDerivedColor(background, 0.0f, -0.110526316f, 0.25490195f, 0);
        this.color39 = this.getDerivedColor(background, 0.0f, -0.020974077f, -0.21960783f, 0);
        this.color40 = this.getDerivedColor(background, 0.0f, 0.11169591f, -0.53333336f, 0);
        this.color41 = this.getDerivedColor(background, 0.055555582f, -0.10658931f, 0.25098038f, 0);
        this.color42 = this.getDerivedColor(background, 0.0f, -0.098526314f, 0.2352941f, 0);
        this.color43 = this.getDerivedColor(background, 0.0f, -0.07333623f, 0.20392156f, 0);
        this.color45 = this.getDerivedColor(background, 0.055555582f, 0.8894737f, -0.7176471f, 0);
        this.color46 = this.getDerivedColor(background, 0.0f, 5.847961E-4f, -0.32156864f, 0);
        this.color47 = this.getDerivedColor(background, -0.00505054f, -0.05960039f, 0.10196078f, 0);
        this.color48 = this.getDerivedColor(background, -0.008547008f, -0.04772438f, 0.06666666f, 0);
        this.color49 = this.getDerivedColor(background, -0.0027777553f, -0.0018306673f, -0.02352941f, 0);
        this.color50 = this.getDerivedColor(background, -0.0027777553f, -0.0212406f, 0.13333333f, 0);
        this.color51 = this.getDerivedColor(background, 0.0055555105f, -0.030845039f, 0.23921567f, 0);
        // }

        Object[] extendedCacheKeys = null;
        switch (this.state) {
        case BACKGROUND_DEFAULT:
            extendedCacheKeys = new Object[] {
                    this.getComponentColor(c, "background", this.color4, -0.6197143f, 0.43137252f, 0),
                    this.getComponentColor(c, "background", this.color5, -0.5766426f, 0.38039213f, 0),
                    this.getComponentColor(c, "background", this.color6, -0.43866998f, 0.24705881f, 0),
                    this.getComponentColor(c, "background", this.color7, -0.46404046f, 0.36470586f, 0),
                    this.getComponentColor(c, "background", this.color8, -0.47761154f, 0.44313723f, 0) };
            break;
        case BACKGROUND_DEFAULT_FOCUSED:
            extendedCacheKeys = new Object[] {
                    this.getComponentColor(c, "background", this.color4, -0.6197143f, 0.43137252f, 0),
                    this.getComponentColor(c, "background", this.color5, -0.5766426f, 0.38039213f, 0),
                    this.getComponentColor(c, "background", this.color6, -0.43866998f, 0.24705881f, 0),
                    this.getComponentColor(c, "background", this.color7, -0.46404046f, 0.36470586f, 0),
                    this.getComponentColor(c, "background", this.color8, -0.47761154f, 0.44313723f, 0) };
            break;
        case BACKGROUND_MOUSEOVER_DEFAULT:
            extendedCacheKeys = new Object[] {
                    this.getComponentColor(c, "background", this.color12, -0.6198413f, 0.43921566f, 0),
                    this.getComponentColor(c, "background", this.color13, -0.5822163f, 0.40392154f, 0),
                    this.getComponentColor(c, "background", this.color14, -0.4555341f, 0.3215686f, 0),
                    this.getComponentColor(c, "background", this.color15, -0.47698414f, 0.43921566f, 0),
                    this.getComponentColor(c, "background", this.color16, -0.5455182f, 0.45098037f, 0) };
            break;
        case BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED:
            extendedCacheKeys = new Object[] {
                    this.getComponentColor(c, "background", this.color12, -0.6198413f, 0.43921566f, 0),
                    this.getComponentColor(c, "background", this.color13, -0.5822163f, 0.40392154f, 0),
                    this.getComponentColor(c, "background", this.color14, -0.4555341f, 0.3215686f, 0),
                    this.getComponentColor(c, "background", this.color15, -0.47698414f, 0.43921566f, 0),
                    this.getComponentColor(c, "background", this.color16, -0.5455182f, 0.45098037f, 0) };
            break;
        case BACKGROUND_PRESSED_DEFAULT:
            extendedCacheKeys = new Object[] {
                    this.getComponentColor(c, "background", this.color20, -0.38050595f, 0.20392156f, 0),
                    this.getComponentColor(c, "background", this.color21, -0.29863563f, 0.1490196f, 0),
                    this.getComponentColor(c, "background", this.color22, 0.0f, 0.0f, 0),
                    this.getComponentColor(c, "background", this.color23, -0.14126986f, 0.15686274f, 0),
                    this.getComponentColor(c, "background", this.color24, -0.20852983f, 0.2588235f, 0) };
            break;
        case BACKGROUND_PRESSED_DEFAULT_FOCUSED:
            extendedCacheKeys = new Object[] {
                    this.getComponentColor(c, "background", this.color20, -0.38050595f, 0.20392156f, 0),
                    this.getComponentColor(c, "background", this.color21, -0.29863563f, 0.1490196f, 0),
                    this.getComponentColor(c, "background", this.color22, 0.0f, 0.0f, 0),
                    this.getComponentColor(c, "background", this.color23, -0.14126986f, 0.15686274f, 0),
                    this.getComponentColor(c, "background", this.color24, -0.20852983f, 0.2588235f, 0) };
            break;
        case BACKGROUND_ENABLED:
            extendedCacheKeys = new Object[] {
                    this.getComponentColor(c, "background", this.color35, -0.10655806f, 0.24313724f, 0),
                    this.getComponentColor(c, "background", this.color36, -0.09823123f, 0.2117647f, 0),
                    this.getComponentColor(c, "background", this.color30, -0.07016757f, 0.12941176f, 0),
                    this.getComponentColor(c, "background", this.color37, -0.0749532f, 0.24705881f, 0),
                    this.getComponentColor(c, "background", this.color38, -0.110526316f, 0.25490195f, 0) };
            break;
        case BACKGROUND_FOCUSED:
            extendedCacheKeys = new Object[] {
                    this.getComponentColor(c, "background", this.color35, -0.10655806f, 0.24313724f, 0),
                    this.getComponentColor(c, "background", this.color36, -0.09823123f, 0.2117647f, 0),
                    this.getComponentColor(c, "background", this.color30, -0.07016757f, 0.12941176f, 0),
                    this.getComponentColor(c, "background", this.color37, -0.0749532f, 0.24705881f, 0),
                    this.getComponentColor(c, "background", this.color38, -0.110526316f, 0.25490195f, 0) };
            break;
        case BACKGROUND_MOUSEOVER:
            extendedCacheKeys = new Object[] {
                    this.getComponentColor(c, "background", this.color41, -0.10658931f, 0.25098038f, 0),
                    this.getComponentColor(c, "background", this.color42, -0.098526314f, 0.2352941f, 0),
                    this.getComponentColor(c, "background", this.color43, -0.07333623f, 0.20392156f, 0),
                    this.getComponentColor(c, "background", this.color38, -0.110526316f, 0.25490195f, 0) };
            break;
        case BACKGROUND_MOUSEOVER_FOCUSED:
            extendedCacheKeys = new Object[] {
                    this.getComponentColor(c, "background", this.color41, -0.10658931f, 0.25098038f, 0),
                    this.getComponentColor(c, "background", this.color42, -0.098526314f, 0.2352941f, 0),
                    this.getComponentColor(c, "background", this.color43, -0.07333623f, 0.20392156f, 0),
                    this.getComponentColor(c, "background", this.color38, -0.110526316f, 0.25490195f, 0) };
            break;
        case BACKGROUND_PRESSED:
            extendedCacheKeys = new Object[] {
                    this.getComponentColor(c, "background", this.color47, -0.05960039f, 0.10196078f, 0),
                    this.getComponentColor(c, "background", this.color48, -0.04772438f, 0.06666666f, 0),
                    this.getComponentColor(c, "background", this.color49, -0.0018306673f, -0.02352941f, 0),
                    this.getComponentColor(c, "background", this.color50, -0.0212406f, 0.13333333f, 0),
                    this.getComponentColor(c, "background", this.color51, -0.030845039f, 0.23921567f, 0) };
            break;
        case BACKGROUND_PRESSED_FOCUSED:
            extendedCacheKeys = new Object[] {
                    this.getComponentColor(c, "background", this.color47, -0.05960039f, 0.10196078f, 0),
                    this.getComponentColor(c, "background", this.color48, -0.04772438f, 0.06666666f, 0),
                    this.getComponentColor(c, "background", this.color49, -0.0018306673f, -0.02352941f, 0),
                    this.getComponentColor(c, "background", this.color50, -0.0212406f, 0.13333333f, 0),
                    this.getComponentColor(c, "background", this.color51, -0.030845039f, 0.23921567f, 0) };
            break;
        }
        return extendedCacheKeys;
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    protected AlphaComposite getDerivedAlphaComposite() {
        AlphaComposite alpha = null;

        switch (this.state) {
        case BACKGROUND_DEFAULT:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaDefaultPercent >= 0 && this.alphaDefaultPercent <= 1) ? this.alphaDefaultPercent
                            : this.defaultAlphaDefaultPercent);
            break;
        case BACKGROUND_DEFAULT_FOCUSED:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaFocusedDefaultPercent >= 0 && this.alphaFocusedDefaultPercent <= 1) ? this.alphaFocusedDefaultPercent
                            : this.defaultAlphaFocusedDefaultPercent);
            break;
        case BACKGROUND_MOUSEOVER_DEFAULT:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaMouseOverDefaultPercent >= 0 && this.alphaMouseOverDefaultPercent <= 1) ? this.alphaMouseOverDefaultPercent
                            : this.defaultAlphaMouseOverDefaultPercent);
            break;
        case BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaMouseOverDefaultFocusedPercent >= 0 && this.alphaMouseOverDefaultFocusedPercent <= 1) ? this.alphaMouseOverDefaultFocusedPercent
                            : this.defaultAlphaMouseOverDefaultFocusedPercent);
            break;
        case BACKGROUND_PRESSED_DEFAULT:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaPressedDefaultPercent >= 0 && this.alphaPressedDefaultPercent <= 1) ? this.alphaPressedDefaultPercent
                            : this.defaultAlphaPressedDefaultPercent);
            break;
        case BACKGROUND_PRESSED_DEFAULT_FOCUSED:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaPressedDefaultFocusedPercent >= 0 && this.alphaPressedDefaultFocusedPercent <= 1) ? this.alphaPressedDefaultFocusedPercent
                            : this.defaultAlphaPressedDefaultFocusedPercent);
            break;
        case BACKGROUND_DISABLED:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaDisabledPercent >= 0 && this.alphaDisabledPercent <= 1) ? this.alphaDisabledPercent
                            : this.defaultAlphaDisabledPercent);
            break;
        case BACKGROUND_ENABLED:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaEnabledPercent >= 0 && this.alphaEnabledPercent <= 1) ? this.alphaEnabledPercent
                            : this.defaultAlphaEnabledPercent);
            break;
        case BACKGROUND_FOCUSED:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaFocusedPercent >= 0 && this.alphaFocusedPercent <= 1) ? this.alphaFocusedPercent
                            : this.defaultAlphaFocusedPercent);
            break;
        case BACKGROUND_MOUSEOVER:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaMouseOverPercent >= 0 && this.alphaMouseOverPercent <= 1) ? this.alphaMouseOverPercent
                            : this.defaultAlphaMouseOverPercent);
            break;
        case BACKGROUND_MOUSEOVER_FOCUSED:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaMouseOverFocusedPercent >= 0 && this.alphaMouseOverFocusedPercent <= 1) ? this.alphaMouseOverFocusedPercent
                            : this.defaultAlphaMouseOverFocusedPercent);
            break;
        case BACKGROUND_PRESSED:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaPressedPercent >= 0 && this.alphaPressedPercent <= 1) ? this.alphaPressedPercent
                            : this.defaultAlphaPressedPercent);
            break;
        case BACKGROUND_PRESSED_FOCUSED:
            alpha = AlphaComposite.SrcOver
                    .derive((this.alphaPressedFocusedPercent >= 0 && this.alphaPressedFocusedPercent <= 1) ? this.alphaPressedFocusedPercent
                            : this.defaultAlphaPressedFocusedPercent);
            break;
        default:
            alpha = AlphaComposite.SrcOver.derive(1.0f);
        }
        return alpha;
    }

    protected void paintBackgroundDefault(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());

        this.roundRect = this.decodeRoundRect1();
        g.setPaint(this.color1);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient1(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient2(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);

    }

    protected void paintBackgroundDefaultAndFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.color9);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient1(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient2(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundMouseOverAndDefault(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect5();
        g.setPaint(this.color1);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient3(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient2(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundMouseOverAndDefaultAndFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.color9);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient3(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient2(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundPressedAndDefault(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect1();
        g.setPaint(this.color17);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient4(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient2(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundPressedAndDefaultAndFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.color9);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient4(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient2(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundDisabled(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect1();
        g.setPaint(this.color25);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient5(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient6(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundEnabled(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect1();
        g.setPaint(this.color1);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient7(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient2(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.color9);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient7(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient8(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundMouseOver(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect1();
        g.setPaint(this.color1);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient9(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient10(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundMouseOverAndFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.color9);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient9(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient10(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundPressed(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect1();
        g.setPaint(this.color44);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient11(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient2(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundPressedAndFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.color9);
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect2();
        g.setPaint(this.decodeGradient11(this.roundRect));
        g.fill(this.roundRect);
        this.roundRect = this.decodeRoundRect3();
        g.setPaint(this.decodeGradient2(this.roundRect));
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    protected RoundRectangle2D decodeRoundRect1() {
        this.roundRect.setRoundRect(this.decodeX(0.2857143f), // x
                this.decodeY(0.42857143f), // y
                this.decodeX(2.7142859f) - this.decodeX(0.2857143f), // width
                this.decodeY(2.857143f) - this.decodeY(0.42857143f), // height
                12.0f, 12.0f); // rounding
        return this.roundRect;
    }

    protected RoundRectangle2D decodeRoundRect2() {
        this.roundRect.setRoundRect(this.decodeX(0.2857143f), // x
                this.decodeY(0.2857143f), // y
                this.decodeX(2.7142859f) - this.decodeX(0.2857143f), // width
                this.decodeY(2.7142859f) - this.decodeY(0.2857143f), // height
                9.0f, 9.0f); // rounding
        return this.roundRect;
    }

    protected RoundRectangle2D decodeRoundRect3() {
        this.roundRect.setRoundRect(this.decodeX(0.42857143f), // x
                this.decodeY(0.42857143f), // y
                this.decodeX(2.5714285f) - this.decodeX(0.42857143f), // width
                this.decodeY(2.5714285f) - this.decodeY(0.42857143f), // height
                7.0f, 7.0f); // rounding
        return this.roundRect;
    }

    protected RoundRectangle2D decodeRoundRect4() {
        this.roundRect.setRoundRect(this.decodeX(0.08571429f), // x
                this.decodeY(0.08571429f), // y
                this.decodeX(2.914286f) - this.decodeX(0.08571429f), // width
                this.decodeY(2.914286f) - this.decodeY(0.08571429f), // height
                11.0f, 11.0f); // rounding
        return this.roundRect;
    }

    protected RoundRectangle2D decodeRoundRect5() {
        this.roundRect.setRoundRect(this.decodeX(0.2857143f), // x
                this.decodeY(0.42857143f), // y
                this.decodeX(2.7142859f) - this.decodeX(0.2857143f), // width
                this.decodeY(2.857143f) - this.decodeY(0.42857143f), // height
                9.0f, 9.0f); // rounding
        return this.roundRect;
    }

    protected Paint decodeGradient1(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.05f,
                0.5f, 0.95f },
                new Color[] { this.color2, this.decodeColor(this.color2, this.color3, 0.5f), this.color3 });
    }

    protected Paint decodeGradient2(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        Color c = (Color) this.componentColors[0];
        return this.decodeGradient(
                (0.5f * w) + x,
                (0.0f * h) + y,
                (0.5f * w) + x,
                (1.0f * h) + y,
                new float[] { 0.0f, 0.024f, 0.06f, 0.276f, 0.6f, 0.65f, 0.7f, 0.856f, 0.96f, 0.98399997f, 1.0f },
                new Color[] { (Color) this.componentColors[0],
                        this.decodeColor((Color) this.componentColors[0], (Color) this.componentColors[1], 0.5f),
                        (Color) this.componentColors[1],
                        this.decodeColor((Color) this.componentColors[1], (Color) this.componentColors[2], 0.5f),
                        (Color) this.componentColors[2],
                        this.decodeColor((Color) this.componentColors[2], (Color) this.componentColors[2], 0.5f),
                        (Color) this.componentColors[2],
                        this.decodeColor((Color) this.componentColors[2], (Color) this.componentColors[3], 0.5f),
                        (Color) this.componentColors[3],
                        this.decodeColor((Color) this.componentColors[3], (Color) this.componentColors[4], 0.5f),
                        (Color) this.componentColors[4] });
    }

    protected Paint decodeGradient3(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.05f,
                0.5f, 0.95f }, new Color[] { this.color10, this.decodeColor(this.color10, this.color11, 0.5f),
                this.color11 });
    }

    protected Paint decodeGradient4(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.05f,
                0.5f, 0.95f }, new Color[] { this.color18, this.decodeColor(this.color18, this.color19, 0.5f),
                this.color19 });
    }

    protected Paint decodeGradient5(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.09f,
                0.52f, 0.95f }, new Color[] { this.color26, this.decodeColor(this.color26, this.color27, 0.5f),
                this.color27 });
    }

    protected Paint decodeGradient6(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(
                (0.5f * w) + x,
                (0.0f * h) + y,
                (0.5f * w) + x,
                (1.0f * h) + y,
                new float[] { 0.0f, 0.03f, 0.06f, 0.33f, 0.6f, 0.65f, 0.7f, 0.825f, 0.95f, 0.975f, 1.0f },
                new Color[] { this.color28, this.decodeColor(this.color28, this.color29, 0.5f), this.color29,
                        this.decodeColor(this.color29, this.color30, 0.5f), this.color30,
                        this.decodeColor(this.color30, this.color30, 0.5f), this.color30,
                        this.decodeColor(this.color30, this.color31, 0.5f), this.color31,
                        this.decodeColor(this.color31, this.color32, 0.5f), this.color32 });
    }

    protected Paint decodeGradient7(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.09f,
                0.52f, 0.95f }, new Color[] { this.color33, this.decodeColor(this.color33, this.color34, 0.5f),
                this.color34 });
    }

    protected Paint decodeGradient8(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(
                (0.5f * w) + x,
                (0.0f * h) + y,
                (0.5f * w) + x,
                (1.0f * h) + y,
                new float[] { 0.0f, 0.03f, 0.06f, 0.33f, 0.6f, 0.65f, 0.7f, 0.825f, 0.95f, 0.975f, 1.0f },
                new Color[] { (Color) this.componentColors[0],
                        this.decodeColor((Color) this.componentColors[0], (Color) this.componentColors[1], 0.5f),
                        (Color) this.componentColors[1],
                        this.decodeColor((Color) this.componentColors[1], (Color) this.componentColors[2], 0.5f),
                        (Color) this.componentColors[2],
                        this.decodeColor((Color) this.componentColors[2], (Color) this.componentColors[2], 0.5f),
                        (Color) this.componentColors[2],
                        this.decodeColor((Color) this.componentColors[2], (Color) this.componentColors[3], 0.5f),
                        (Color) this.componentColors[3],
                        this.decodeColor((Color) this.componentColors[3], (Color) this.componentColors[4], 0.5f),
                        (Color) this.componentColors[4] });
    }

    protected Paint decodeGradient9(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.09f,
                0.52f, 0.95f }, new Color[] { this.color39, this.decodeColor(this.color39, this.color40, 0.5f),
                this.color40 });
    }

    protected Paint decodeGradient10(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(
                (0.5f * w) + x,
                (0.0f * h) + y,
                (0.5f * w) + x,
                (1.0f * h) + y,
                new float[] { 0.0f, 0.024f, 0.06f, 0.276f, 0.6f, 0.65f, 0.7f, 0.856f, 0.96f, 0.98f, 1.0f },
                new Color[] { (Color) this.componentColors[0],
                        this.decodeColor((Color) this.componentColors[0], (Color) this.componentColors[1], 0.5f),
                        (Color) this.componentColors[1],
                        this.decodeColor((Color) this.componentColors[1], (Color) this.componentColors[2], 0.5f),
                        (Color) this.componentColors[2],
                        this.decodeColor((Color) this.componentColors[2], (Color) this.componentColors[2], 0.5f),
                        (Color) this.componentColors[2],
                        this.decodeColor((Color) this.componentColors[2], (Color) this.componentColors[3], 0.5f),
                        (Color) this.componentColors[3],
                        this.decodeColor((Color) this.componentColors[3], (Color) this.componentColors[3], 0.5f),
                        (Color) this.componentColors[3] });
    }

    protected Paint decodeGradient11(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.05f,
                0.5f, 0.95f }, new Color[] { this.color45, this.decodeColor(this.color45, this.color46, 0.5f),
                this.color46 });
    }
}