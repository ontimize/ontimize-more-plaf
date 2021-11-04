package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.utils.DerivedColor;

public class OComboBoxPainter extends AbstractRegionPainter {

    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of ComboBoxPainter to determine which region/state is being painted
    // by that instance.
    public static final int BACKGROUND_DISABLED = 1;

    public static final int BACKGROUND_DISABLED_PRESSED = 2;

    public static final int BACKGROUND_DISABLED_REQUIRED = 22;

    public static final int BACKGROUND_ENABLED = 3;

    public static final int BACKGROUND_FOCUSED = 4;

    public static final int BACKGROUND_MOUSEOVER_FOCUSED = 5;

    public static final int BACKGROUND_MOUSEOVER = 6;

    public static final int BACKGROUND_PRESSED_FOCUSED = 7;

    public static final int BACKGROUND_REQUIRED_FOCUSED = 77;

    public static final int BACKGROUND_PRESSED = 8;

    public static final int BACKGROUND_ENABLED_SELECTED = 9;

    public static final int BACKGROUND_DISABLED_EDITABLE = 10;

    public static final int BACKGROUND_ENABLED_EDITABLE = 11;

    public static final int BACKGROUND_FOCUSED_EDITABLE = 12;

    public static final int BACKGROUND_MOUSEOVER_EDITABLE = 13;

    public static final int BACKGROUND_PRESSED_EDITABLE = 14;

    public static final int BACKGROUND_REQUIRED = 222;

    public static final int BORDER_REQUIRED = 666;

    // painters to fill the component
    protected Paint backgroundColorDisabled;

    protected Paint backgroundColorDisabledRequired;

    protected Paint backgroundColorEnabled;

    protected Paint backgroundColorFocused;

    protected Paint backgroundColorFocusedMouseOver;

    protected Paint backgroundColorMouseOver;

    protected Paint backgroundColorFocusedPressed;

    protected Paint backgroundColorFocusedRequired;

    protected Paint backgroundColorPressed;

    protected Paint backgroundColorRequired;

    protected Paint backgroundArrowButtonColorDisabled;

    protected Paint backgroundArrowButtonColorDisabledRequired;

    protected Paint backgroundArrowButtonColorEnabled;

    protected Paint backgroundArrowButtonColorFocused;

    protected Paint backgroundArrowButtonColorRequired;

    protected Paint backgroundArrowButtonColorFocusedMouseOver;

    protected Paint backgroundArrowButtonColorMouseOver;

    protected Paint backgroundArrowButtonColorFocusedPressed;

    protected Paint backgroundArrowButtonColorFocusedRequired;

    protected Paint backgroundArrowButtonColorPressed;

    protected Paint backgroundArrowButtonColorEditableDisabled;

    protected Paint backgroundArrowButtonColorEditableEnabled;

    protected Paint backgroundArrowButtonColorEditableFocused;

    protected Paint backgroundArrowButtonColorEditableRequired;

    protected Paint backgroundArrowButtonColorEditableFocusedMouseOver;

    protected Paint backgroundArrowButtonColorEditableMouseOver;

    protected Paint backgroundArrowButtonColorEditableFocusedPressed;

    protected Paint backgroundArrowButtonColorEditableFocusedRequired;

    protected Paint backgroundArrowButtonColorEditablePressed;

    // arrays to round the component (several rounded borders with degradation):
    protected Paint[] degradatedBorderColorEnabled;

    protected Paint[] degradatedBorderColorDisabled;

    protected Paint[] degradatedBorderColorDisabledRequired;

    protected Paint[] degradatedBorderColorFocused;

    protected Paint[] degradatedBorderColorFocusedRequired;

    protected Paint[] degradatedBorderColorRequired;

    // painters to fill the editor
    protected Paint backgroundEditorColorDisabled;

    protected Paint backgroundEditorColorEnabled;

    protected Paint backgroundEditorColorSelected;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();

    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    protected Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of ComboBoxPainter would be created. For the
    // moment at least,
    // however, all are created for each instance.
    protected Color color1 = this.decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.74509805f, -247);

    protected Color color2 = this.decodeColor("nimbusBase", 0.032459438f, -0.5928571f, 0.2745098f, 0);

    protected Color color3 = this.decodeColor("nimbusBase", 0.032459438f, -0.590029f, 0.2235294f, 0);

    protected Color color4 = this.decodeColor("nimbusBase", 0.032459438f, -0.60996324f, 0.36470586f, 0);

    protected Color color5 = this.decodeColor("nimbusBase", 0.040395975f, -0.60474086f, 0.33725488f, 0);

    protected Color color6 = this.decodeColor("nimbusBase", 0.032459438f, -0.5953556f, 0.32549018f, 0);

    protected Color color7 = this.decodeColor("nimbusBase", 0.032459438f, -0.5957143f, 0.3333333f, 0);

    protected Color color8 = this.decodeColor("nimbusBase", 0.021348298f, -0.56289876f, 0.2588235f, 0);

    protected Color color9 = this.decodeColor("nimbusBase", 0.010237217f, -0.55799407f, 0.20784312f, 0);

    protected Color color10 = this.decodeColor("nimbusBase", 0.021348298f, -0.59223604f, 0.35294116f, 0);

    protected Color color11 = this.decodeColor("nimbusBase", 0.02391243f, -0.5774183f, 0.32549018f, 0);

    protected Color color12 = this.decodeColor("nimbusBase", 0.021348298f, -0.56722116f, 0.3098039f, 0);

    protected Color color13 = this.decodeColor("nimbusBase", 0.021348298f, -0.567841f, 0.31764704f, 0);

    protected Color color14 = this.decodeColor("nimbusBlueGrey", 0.0f, 0.0f, -0.22f, -176);

    protected Color color15 = this.decodeColor("nimbusBase", 0.032459438f, -0.5787523f, 0.07058823f, 0);

    protected Color color16 = this.decodeColor("nimbusBase", 0.032459438f, -0.5399696f, -0.18039218f, 0);

    protected Color color17 = this.decodeColor("nimbusBase", 0.08801502f, -0.63174605f, 0.43921566f, 0);

    protected Color color18 = this.decodeColor("nimbusBase", 0.040395975f, -0.6054113f, 0.35686272f, 0);

    protected Color color19 = this.decodeColor("nimbusBase", 0.032459438f, -0.5998577f, 0.4352941f, 0);

    protected Color color20 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.34585923f, -0.007843137f, 0);

    protected Color color21 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.095173776f, -0.25882354f, 0);

    protected Color color22 = this.decodeColor("nimbusBase", 0.004681647f, -0.6197143f, 0.43137252f, 0);

    protected Color color23 = this.decodeColor("nimbusBase", -0.0028941035f, -0.4800539f, 0.28235292f, 0);

    protected Color color24 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.43866998f, 0.24705881f, 0);

    protected Color color25 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.4625541f, 0.35686272f, 0);

    protected Color color26 = this.decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);

    protected Color color27 = this.decodeColor("nimbusBase", 0.032459438f, -0.54616207f, -0.02352941f, 0);

    protected Color color28 = this.decodeColor("nimbusBase", 0.032459438f, -0.41349208f, -0.33725494f, 0);

    protected Color color29 = this.decodeColor("nimbusBase", 0.08801502f, -0.6317773f, 0.4470588f, 0);

    protected Color color30 = this.decodeColor("nimbusBase", 0.032459438f, -0.6113241f, 0.41568625f, 0);

    protected Color color31 = this.decodeColor("nimbusBase", 0.032459438f, -0.5985242f, 0.39999998f, 0);

    protected Color color32 = this.decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);

    protected Color color33 = this.decodeColor("nimbusBase", 0.0013483167f, -0.1769987f, -0.12156865f, 0);

    protected Color color34 = this.decodeColor("nimbusBase", 0.059279382f, 0.3642857f, -0.43529415f, 0);

    protected Color color35 = this.decodeColor("nimbusBase", 0.004681647f, -0.6198413f, 0.43921566f, 0);

    protected Color color36 = this.decodeColor("nimbusBase", -8.738637E-4f, -0.50527954f, 0.35294116f, 0);

    protected Color color37 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.4555341f, 0.3215686f, 0);

    protected Color color38 = this.decodeColor("nimbusBase", 5.1498413E-4f, -0.4757143f, 0.43137252f, 0);

    protected Color color39 = this.decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.52156866f, 0);

    protected Color color40 = this.decodeColor("nimbusBase", 0.032459438f, -0.5246032f, -0.12549022f, 0);

    protected Color color41 = this.decodeColor("nimbusBase", 0.027408898f, -0.5847884f, 0.2980392f, 0);

    protected Color color42 = this.decodeColor("nimbusBase", 0.026611507f, -0.53623784f, 0.19999999f, 0);

    protected Color color43 = this.decodeColor("nimbusBase", 0.029681683f, -0.52701867f, 0.17254901f, 0);

    protected Color color44 = this.decodeColor("nimbusBase", 0.03801495f, -0.5456242f, 0.3215686f, 0);

    protected Color color45 = this.decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);

    protected Color color46 = this.decodeColor("nimbusBase", -3.528595E-5f, 0.018606722f, -0.23137257f, 0);

    protected Color color47 = this.decodeColor("nimbusBase", -4.2033195E-4f, -0.38050595f, 0.20392156f, 0);

    protected Color color48 = this.decodeColor("nimbusBase", 4.081726E-4f, -0.12922078f, 0.054901958f, 0);

    protected Color color49 = this.decodeColor("nimbusBase", 0.0f, -0.00895375f, 0.007843137f, 0);

    protected Color color50 = this.decodeColor("nimbusBase", -0.0015907288f, -0.1436508f, 0.19215685f, 0);

    protected Color color51 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -83);

    protected Color color52 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -88);

    protected Color color53 = new Color(0x263945);

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    protected java.awt.Image paddLock = null;

    protected double radius;

    public static String imgUrl = "com/ontimize/plaf/images/paddlock.png";

    protected int numBorders;


    public OComboBoxPainter(int state, PaintContext ctx) {
        super(state, ctx);

        if (OComboBoxPainter.imgUrl != null) {
            URL url = this.getClass().getClassLoader().getResource(OComboBoxPainter.imgUrl);
            this.paddLock = Toolkit.getDefaultToolkit().getImage(url);
        }
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent component, int width, int height, Object[] extendedCacheKeys) {
        // populate componentColors array with colors calculated in
        // getExtendedCacheKeys call
        this.componentColors = extendedCacheKeys;
        // generate this entire method. Each state/bg/fg/border combo that has
        // been painted gets its own KEY and paint method.

        switch (this.state) {
            case BACKGROUND_DISABLED:
                this.paintBackgroundDisabled(component, g);
                break;
            case BACKGROUND_DISABLED_PRESSED:
                this.paintBackgroundDisabledAndPressed(component, g);
                break;
            case BACKGROUND_DISABLED_REQUIRED:
                this.paintBackgroundDisabledAndRequired(component, g);
                break;
            case BACKGROUND_ENABLED:
                this.paintBackgroundEnabled(component, g);
                break;
            case BACKGROUND_FOCUSED:
                this.paintBackgroundFocused(component, g);
                break;
            case BACKGROUND_REQUIRED:
                this.paintBackgroundRequired(component, g);
                break;
            case BACKGROUND_MOUSEOVER_FOCUSED:
                this.paintBackgroundMouseOverAndFocused(component, g);
                break;
            case BACKGROUND_MOUSEOVER:
                this.paintBackgroundMouseOver(component, g);
                break;
            case BACKGROUND_PRESSED_FOCUSED:
                this.paintBackgroundPressedAndFocused(component, g);
                break;
            case BACKGROUND_REQUIRED_FOCUSED:
                this.paintBackgroundRequiredAndFocused(component, g);
                break;
            case BACKGROUND_PRESSED:
                this.paintBackgroundPressed(component, g);
                break;
            case BACKGROUND_ENABLED_SELECTED:
                this.paintBackgroundEnabledAndSelected(component, g);
                break;
            case BACKGROUND_DISABLED_EDITABLE:
                this.paintBackgroundDisabledAndEditable(component, g);
                break;
            case BACKGROUND_ENABLED_EDITABLE:
                this.paintBackgroundEnabledAndEditable(component, g);
                break;
            case BACKGROUND_FOCUSED_EDITABLE:
                this.paintBackgroundFocusedAndEditable(component, g);
                break;
            case BACKGROUND_MOUSEOVER_EDITABLE:
                this.paintBackgroundMouseOverAndEditable(component, g);
                break;
            case BACKGROUND_PRESSED_EDITABLE:
                this.paintBackgroundPressedAndEditable(component, g);
                break;
        }
    }

    @Override
    protected Object[] getExtendedCacheKeys(JComponent c) {
        Object[] extendedCacheKeys = null;
        switch (this.state) {
            case BACKGROUND_ENABLED:
                extendedCacheKeys = new Object[] {
                        this.getComponentColor(c, "background", this.color17, -0.63174605f, 0.43921566f, 0),
                        this.getComponentColor(c, "background", this.color18, -0.6054113f, 0.35686272f, 0),
                        this.getComponentColor(c, "background", this.color6, -0.5953556f, 0.32549018f, 0),
                        this.getComponentColor(c, "background", this.color19, -0.5998577f, 0.4352941f, 0),
                        this.getComponentColor(c, "background", this.color22, -0.6197143f, 0.43137252f, 0),
                        this.getComponentColor(c, "background", this.color23, -0.4800539f, 0.28235292f, 0),
                        this.getComponentColor(c, "background", this.color24, -0.43866998f, 0.24705881f, 0),
                        this.getComponentColor(c, "background", this.color25, -0.4625541f, 0.35686272f, 0) };
                break;
            case BACKGROUND_FOCUSED:
                extendedCacheKeys = new Object[] {
                        this.getComponentColor(c, "background", this.color17, -0.63174605f, 0.43921566f, 0),
                        this.getComponentColor(c, "background", this.color18, -0.6054113f, 0.35686272f, 0),
                        this.getComponentColor(c, "background", this.color6, -0.5953556f, 0.32549018f, 0),
                        this.getComponentColor(c, "background", this.color19, -0.5998577f, 0.4352941f, 0),
                        this.getComponentColor(c, "background", this.color22, -0.6197143f, 0.43137252f, 0),
                        this.getComponentColor(c, "background", this.color23, -0.4800539f, 0.28235292f, 0),
                        this.getComponentColor(c, "background", this.color24, -0.43866998f, 0.24705881f, 0),
                        this.getComponentColor(c, "background", this.color25, -0.4625541f, 0.35686272f, 0) };
                break;
            case BACKGROUND_MOUSEOVER_FOCUSED:
                extendedCacheKeys = new Object[] {
                        this.getComponentColor(c, "background", this.color29, -0.6317773f, 0.4470588f, 0),
                        this.getComponentColor(c, "background", this.color30, -0.6113241f, 0.41568625f, 0),
                        this.getComponentColor(c, "background", this.color31, -0.5985242f, 0.39999998f, 0),
                        this.getComponentColor(c, "background", this.color32, -0.6357143f, 0.45098037f, 0),
                        this.getComponentColor(c, "background", this.color35, -0.6198413f, 0.43921566f, 0),
                        this.getComponentColor(c, "background", this.color36, -0.50527954f, 0.35294116f, 0),
                        this.getComponentColor(c, "background", this.color37, -0.4555341f, 0.3215686f, 0),
                        this.getComponentColor(c, "background", this.color25, -0.4625541f, 0.35686272f, 0),
                        this.getComponentColor(c, "background", this.color38, -0.4757143f, 0.43137252f, 0) };
                break;
            case BACKGROUND_MOUSEOVER:
                extendedCacheKeys = new Object[] {
                        this.getComponentColor(c, "background", this.color29, -0.6317773f, 0.4470588f, 0),
                        this.getComponentColor(c, "background", this.color30, -0.6113241f, 0.41568625f, 0),
                        this.getComponentColor(c, "background", this.color31, -0.5985242f, 0.39999998f, 0),
                        this.getComponentColor(c, "background", this.color32, -0.6357143f, 0.45098037f, 0),
                        this.getComponentColor(c, "background", this.color35, -0.6198413f, 0.43921566f, 0),
                        this.getComponentColor(c, "background", this.color36, -0.50527954f, 0.35294116f, 0),
                        this.getComponentColor(c, "background", this.color37, -0.4555341f, 0.3215686f, 0),
                        this.getComponentColor(c, "background", this.color25, -0.4625541f, 0.35686272f, 0),
                        this.getComponentColor(c, "background", this.color38, -0.4757143f, 0.43137252f, 0) };
                break;
            case BACKGROUND_PRESSED_FOCUSED:
                extendedCacheKeys = new Object[] {
                        this.getComponentColor(c, "background", this.color41, -0.5847884f, 0.2980392f, 0),
                        this.getComponentColor(c, "background", this.color42, -0.53623784f, 0.19999999f, 0),
                        this.getComponentColor(c, "background", this.color43, -0.52701867f, 0.17254901f, 0),
                        this.getComponentColor(c, "background", this.color44, -0.5456242f, 0.3215686f, 0),
                        this.getComponentColor(c, "background", this.color47, -0.38050595f, 0.20392156f, 0),
                        this.getComponentColor(c, "background", this.color48, -0.12922078f, 0.054901958f, 0),
                        this.getComponentColor(c, "background", this.color49, -0.00895375f, 0.007843137f, 0),
                        this.getComponentColor(c, "background", this.color50, -0.1436508f, 0.19215685f, 0) };
                break;
            case BACKGROUND_PRESSED:
                extendedCacheKeys = new Object[] {
                        this.getComponentColor(c, "background", this.color41, -0.5847884f, 0.2980392f, 0),
                        this.getComponentColor(c, "background", this.color42, -0.53623784f, 0.19999999f, 0),
                        this.getComponentColor(c, "background", this.color43, -0.52701867f, 0.17254901f, 0),
                        this.getComponentColor(c, "background", this.color44, -0.5456242f, 0.3215686f, 0),
                        this.getComponentColor(c, "background", this.color47, -0.38050595f, 0.20392156f, 0),
                        this.getComponentColor(c, "background", this.color48, -0.12922078f, 0.054901958f, 0),
                        this.getComponentColor(c, "background", this.color49, -0.00895375f, 0.007843137f, 0),
                        this.getComponentColor(c, "background", this.color50, -0.1436508f, 0.19215685f, 0) };
                break;
            case BACKGROUND_ENABLED_SELECTED:
                extendedCacheKeys = new Object[] {
                        this.getComponentColor(c, "background", this.color41, -0.5847884f, 0.2980392f, 0),
                        this.getComponentColor(c, "background", this.color42, -0.53623784f, 0.19999999f, 0),
                        this.getComponentColor(c, "background", this.color43, -0.52701867f, 0.17254901f, 0),
                        this.getComponentColor(c, "background", this.color44, -0.5456242f, 0.3215686f, 0),
                        this.getComponentColor(c, "background", this.color47, -0.38050595f, 0.20392156f, 0),
                        this.getComponentColor(c, "background", this.color48, -0.12922078f, 0.054901958f, 0),
                        this.getComponentColor(c, "background", this.color49, -0.00895375f, 0.007843137f, 0),
                        this.getComponentColor(c, "background", this.color50, -0.1436508f, 0.19215685f, 0) };
                break;
        }
        return extendedCacheKeys;
    }

    @Override
    protected String getComponentKeyName() {
        return "ComboBox";
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

        Object oRadius = UIManager.getLookAndFeelDefaults().get("Application.radius");
        if (oRadius instanceof Double) {
            this.radius = ((Double) oRadius).doubleValue();
        } else {
            this.radius = OntimizeLookAndFeel.defaultRadius;
        }

        Object obj = UIManager.getDefaults().get(this.getComponentKeyName() + ".numBorders");
        if (obj instanceof Number) {
            this.numBorders = ((Number) obj).intValue();
        } else {
            this.numBorders = OntimizeLookAndFeel.defaultNumBorders;
        }

        // disable:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].background");
        if (obj instanceof Paint) {
            this.backgroundColorDisabled = (Paint) obj;
        } else {
            this.backgroundColorDisabled = this.color1;
        }

        // disable + Required:
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

        // focused + mouseover:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused+MouseOver].background");
        if (obj instanceof Paint) {
            this.backgroundColorFocusedMouseOver = (Paint) obj;
        } else {
            this.backgroundColorFocusedMouseOver = this.color2;
        }

        // mouseover:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[MouseOver].background");
        if (obj instanceof Paint) {
            this.backgroundColorMouseOver = (Paint) obj;
        } else {
            this.backgroundColorMouseOver = this.color2;
        }

        // focused + pressed:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused+Pressed].background");
        if (obj instanceof Paint) {
            this.backgroundColorFocusedPressed = (Paint) obj;
        } else {
            this.backgroundColorFocusedPressed = this.color2;
        }

        // focused + required:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused+Required].background");
        if (obj instanceof Paint) {
            this.backgroundColorFocusedRequired = (Paint) obj;
        } else {
            this.backgroundColorFocusedRequired = this.color2;
        }

        // pressed:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Pressed].background");
        if (obj instanceof Paint) {
            this.backgroundColorPressed = (Paint) obj;
        } else {
            this.backgroundColorPressed = this.color2;
        }

        // required:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Required].background");
        if (obj instanceof Paint) {
            this.backgroundColorRequired = (Paint) obj;
        } else {
            this.backgroundColorRequired = this.color2;
        }

        // BORDER COLORS FOR ROUNDED SHAPES (<=> ARCWIDTH>=0 AND ARCHEIGHT>=0):
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

        // disable + Required:
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

        // Focused + Required
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused+Required].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorFocusedRequired = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorFocusedRequired = (Paint[]) obj;
        } else {
            this.degradatedBorderColorFocusedRequired = new Color[] { this.color7,
                    this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
        }

        // Required:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Required].border");
        if (obj instanceof Paint) {
            this.degradatedBorderColorRequired = new Paint[] { (Paint) obj };
        } else if (obj instanceof Paint[]) {
            this.degradatedBorderColorRequired = (Paint[]) obj;
        } else {
            this.degradatedBorderColorRequired = new Color[] { this.color7,
                    this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
        }

        // EDITOR:
        // editor disable:
        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.textField\"[Disabled].background");
        if (obj instanceof Paint) {
            this.backgroundEditorColorDisabled = (Paint) obj;
        } else {
            this.backgroundEditorColorDisabled = this.color1;
        }

        // editor enable:
        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.textField\"[Enabled].background");
        if (obj instanceof Paint) {
            this.backgroundEditorColorEnabled = (Paint) obj;
        } else {
            this.backgroundEditorColorEnabled = this.color2;
        }

        // editor selected:
        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.textField\"[Selected].background");
        if (obj instanceof Paint) {
            this.backgroundEditorColorSelected = (Paint) obj;
        } else {
            this.backgroundEditorColorSelected = this.color2;
        }

        // Arrow Button.
        // disable:
        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Disabled].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorDisabled = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorDisabled = this.color53;
        }

        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Disabled+Required].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorDisabledRequired = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorDisabledRequired = this.color53;
        }

        // enable:
        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Enabled].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorEnabled = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorEnabled = this.color53;
        }

        // mouseover:
        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[MouseOver].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorMouseOver = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorMouseOver = this.color53;
        }

        // pressed:
        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Pressed].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorPressed = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorPressed = this.color53;
        }

        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Focused].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorFocused = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorFocused = this.color53;
        }

        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Focused+Required].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorFocusedRequired = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorFocusedRequired = this.color53;
        }

        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Required].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorRequired = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorRequired = this.color53;
        }

        // Editable
        // disable:
        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Disabled+Editable].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorEditableDisabled = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorEditableDisabled = this.color53;
        }

        // enable:
        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Editable+Enabled].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorEditableEnabled = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorEditableEnabled = this.color53;
        }

        // mouseover:
        obj = UIManager.getLookAndFeelDefaults()
            .get("ComboBox:\"ComboBox.arrowButton\"[Editable+MouseOver].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorEditableMouseOver = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorEditableMouseOver = this.color53;
        }

        // pressed:
        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Editable+Pressed].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorEditablePressed = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorEditablePressed = this.color53;
        }

        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Editable+Focused].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorEditableFocused = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorEditableFocused = this.color53;
        }

        obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Editable+Required].background");
        if (obj instanceof Paint) {
            this.backgroundArrowButtonColorEditableRequired = (Paint) obj;
        } else {
            this.backgroundArrowButtonColorEditableRequired = this.color53;
        }

    }


    public boolean isTableEditor(JComponent c) {
        if (SwingUtilities.getAncestorOfClass(JTable.class, c) != null) {
            return true;
        }
        return false;
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    protected void paintBackgroundDisabled(JComponent component, Graphics2D g) {

        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorDisabled));
        } else {
            this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorDisabled));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorDisabled);
            g.drawImage(this.paddLock, 1, (int) (this.decodeY(2.0f) - 10), 10, 10, null);
        }

    }

    protected void paintBackgroundDisabledAndPressed(JComponent component, Graphics2D g) {

        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorDisabled));
        } else {
            this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorDisabled));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorDisabled);
            g.drawImage(this.paddLock, 1, (int) (this.decodeY(2.0f) - 10), 10, 10, null);
        }
    }

    protected void paintBackgroundDisabledAndRequired(JComponent component, Graphics2D g) {

        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorDisabledRequired));
        } else {
            this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorDisabledRequired));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorDisabled);
            g.drawImage(this.paddLock, 1, (int) (this.decodeY(2.0f) - 10), 10, 10, null);
        }
    }

    protected void paintBackgroundEnabled(JComponent component, Graphics2D g) {

        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorEnabled));
        } else {
            this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorEnabled));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorEnabled);
        }
    }

    protected void paintBackgroundFocused(JComponent component, Graphics2D g) {

        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorFocused));
        } else {
            this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorFocused));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorFocused);
        }

    }

    protected void paintBackgroundRequired(JComponent component, Graphics2D g) {

        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorRequired));
        } else {
            this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorRequired));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorRequired);
        }

    }

    protected void paintBackgroundMouseOverAndFocused(JComponent component, Graphics2D g) {

        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorFocusedMouseOver));
        } else {
            this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorFocusedMouseOver));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorFocused);
        }

    }

    protected void paintBackgroundMouseOver(JComponent component, Graphics2D g) {

        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorMouseOver));
        } else {
            this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorMouseOver));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorEnabled);
        }

    }

    protected void paintBackgroundPressedAndFocused(JComponent component, Graphics2D g) {
        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorFocusedPressed));
        } else {
            this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorFocusedPressed));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorFocused);
        }
    }

    protected void paintBackgroundRequiredAndFocused(JComponent component, Graphics2D g) {
        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorFocusedMouseOver));
        } else {
            this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorFocusedRequired));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorFocused);
        }
    }

    protected void paintBackgroundPressed(JComponent component, Graphics2D g) {

        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorPressed));
        } else {
            this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorPressed));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorEnabled);
        }

    }

    protected void paintBackgroundEnabledAndSelected(JComponent component, Graphics2D g) {

        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorEnabled));
        } else {
            this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorEnabled));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorEnabled);
        }

    }

    protected void paintBackgroundDisabledAndEditable(JComponent component, Graphics2D g) {

        this.drawBackgroundEditable(component, g,
                this.getBackgroundColor(component, this.backgroundEditorColorDisabled));
        this.drawDegradatedBorders(component, g, this.degradatedBorderColorDisabled);
        g.drawImage(this.paddLock, 1, (int) (this.decodeY(2.0f) - 10), 10, 10, null);

    }

    protected void paintBackgroundEnabledAndEditable(JComponent component, Graphics2D g) {

        this.drawBackgroundEditable(component, g,
                this.getBackgroundColor(component, this.backgroundEditorColorEnabled));
        this.drawDegradatedBorders(component, g, this.degradatedBorderColorEnabled);

    }

    protected void paintBackgroundFocusedAndEditable(JComponent component, Graphics2D g) {

        if (this.isTableEditor(component)) {
            this.drawBackgroundTableEditor(component, g,
                    this.getBackgroundColor(component, this.backgroundColorEnabled));
        } else {
            this.drawBackgroundEditable(component, g,
                    this.getBackgroundColor(component, this.backgroundEditorColorEnabled));
            this.drawDegradatedBorders(component, g, this.degradatedBorderColorFocused);
        }
        // TODO At the moment editor textfield does not change to background
        // selected state. When it changes, it is
        // necessary to change the color to backgroundEditorColorSelected.

    }

    protected void paintBackgroundMouseOverAndEditable(JComponent component, Graphics2D g) {

        this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorMouseOver));
        this.drawDegradatedBorders(component, g, this.degradatedBorderColorEnabled);

    }

    protected void paintBackgroundPressedAndEditable(JComponent component, Graphics2D g) {

        this.drawBackground(component, g, this.getBackgroundColor(component, this.backgroundColorPressed));
        this.drawDegradatedBorders(component, g, this.degradatedBorderColorEnabled);

    }

    protected Paint getBackgroundColor(JComponent c, Paint defaultColor) {
        if (c != null) {
            if ((c.getBackground() != null) && c.isEnabled()) {
                return c.getBackground();
            }
        }
        return defaultColor;
    }

    protected com.ontimize.plaf.utils.DerivedColor getDerivedColor(Color parent, float hOffset, float sOffset,
            float bOffset, int aOffset) {
        DerivedColor color = new DerivedColor.UIResource(parent, hOffset, sOffset, bOffset, aOffset);
        color.rederiveColor();
        return color;
    }

    protected Paint decodeGradientButton(Shape s, Color bgBaseColor) {

        Color derived = this.getDerivedColor(bgBaseColor, 0.0036656857f, -0.271856f, 0.21568626f, 0);

        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(x, y, x, y + h, new float[] { 0.0f, 0.495f, 0.505f, 1.0f },
                new Color[] { bgBaseColor, derived, derived, bgBaseColor });
    }

    protected void drawBackground(JComponent component, Graphics2D g, Paint color) {
        Paint previousPaint = g.getPaint();
        RenderingHints rh = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x_arc = this.intValue(this.decodeX(0.0f) + this.getMaximumRadius() + this.numBorders);
        if (x_arc > this.intValue(this.decodeX(1.0f))) {
            x_arc = this.intValue(this.decodeX(1.0f));
        }
        int y = this.intValue(this.decodeY(0.0f) + this.numBorders);
        int w = this.intValue(this.decodeX(2.0f) - this.decodeX(1.0f));
        int h = this.intValue(this.decodeY(3.0f) - (2 * this.numBorders));

        JButton button = this.getArrowButton(component);
        if (button != null) {
            Rectangle bounds = button.getBounds();
            w = bounds.x - x_arc;
        }

        // The rectangle of the component...
        Shape s = ShapeFactory.getInstace().createRectangle(x_arc, y, w, h);
        g.setPaint(color);
        g.fill(s);

        // The left rounded filler
        s = this.decodeLeftBorderFillerPath(h);
        g.fill(s);

        // The button, just the background...
        this.drawButton(component, g);

        g.setPaint(previousPaint);
        g.setRenderingHints(rh);
    }

    protected Shape decodeLeftBorderFillerPath(int h) {

        double x = this.decodeX(0.0f) + this.getMaximumRadius() + this.numBorders;
        double radius = this.getRadius();
        double x_arc = this.decodeX(0.0f) + this.numBorders + radius;
        if (x > this.intValue(this.decodeX(1.0f))) {
            x = this.intValue(this.decodeX(1.0f));
        }
        if (x_arc > x) {
            x_arc = x;
            radius = this.getMaximumRadius();
        }
        double y = this.decodeY(0.0f) + this.numBorders;

        this.path.reset();
        this.path.moveTo(x, y);
        this.path.lineTo(x_arc, y);
        this.path.curveTo(x_arc - (radius / 2.0), y, x_arc - radius, y + (radius / 2.0), x_arc - radius, y + radius);
        this.path.lineTo(x_arc - radius, (y + h) - radius);
        this.path.curveTo(x_arc - radius, (y + h) - (radius / 2.0), x_arc - (radius / 2.0), y + h, x_arc, y + h);
        this.path.lineTo(x, y + h);
        this.path.closePath();

        return this.path;
    }

    protected void drawBackgroundTableEditor(JComponent component, Graphics2D g, Paint color) {
        Paint previousPaint = g.getPaint();
        RenderingHints rh = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = this.intValue(this.decodeX(0.0f));
        int y = this.intValue(this.decodeY(0.0f));
        int w = this.intValue(this.decodeX(2.0f) - this.decodeX(1.0f));
        int h = this.intValue(this.decodeY(3.0f) - 1);

        JButton button = this.getArrowButton(component);
        if (button != null) {
            Rectangle bounds = button.getBounds();
            w = bounds.x;
        }

        Shape s = ShapeFactory.getInstace().createRectangle(x, y, w, h);
        g.setPaint(color);
        g.fill(s);

        this.drawButtonTableEditor(component, g);

        g.setPaint(previousPaint);
        g.setRenderingHints(rh);
    }

    protected void drawBackgroundEditable(JComponent component, Graphics2D g, Paint color) {
        Paint previousPaint = g.getPaint();
        RenderingHints rh = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x_arc = this.intValue(this.decodeX(0.0f) + this.getMaximumRadius() + this.numBorders);
        if (x_arc > this.intValue(this.decodeX(1.0f))) {
            x_arc = this.intValue(this.decodeX(1.0f));
        }
        int y = this.intValue(this.decodeY(0.0f) + this.numBorders);
        int h = this.intValue(this.decodeY(3.0f) - (2 * this.numBorders));

        Shape s = ShapeFactory.getInstace().createSemiCircle(x_arc, y, h, ShapeFactory.ANTICLOCKWISE, false);
        g.setPaint(color);
        g.fill(s);

        this.drawButton(component, g);

        g.setPaint(previousPaint);
        g.setRenderingHints(rh);
    }

    protected void drawButton(JComponent component, Graphics2D g) {

        JButton button = this.getArrowButton(component);
        if (button != null) {
            Rectangle bounds = button.getBounds();
            int xBut = bounds.x;
            int yBut = this.intValue(this.decodeY(0.0f) + this.numBorders);
            int wBut = bounds.width - this.intValue(bounds.width * 0.4);
            int hBut = this.intValue(this.decodeY(3.0f) - (2 * this.numBorders));

            Shape s = ShapeFactory.getInstace().createRectangle(xBut, yBut, wBut, hBut);
            g.setPaint(this.decodeGradientButton(s, this.getArrowButtonBackgroundColor()));
            g.fill(s);

            double x = xBut + wBut;
            double h = this.decodeY(3.0f) - (2 * this.numBorders);
            s = this.decodeRightBorderFillerPath(x, h);
            g.fill(s);

        }

    }

    protected Shape decodeRightBorderFillerPath(double x, double h) {

        double radius = this.getRadius();
        double x_arc = this.decodeX(3.0f) - this.numBorders - radius;
        if (x_arc < x) {
            x_arc = x;
            radius = this.getMaximumRadius();
        }
        double y = this.decodeY(0.0f) + this.numBorders;

        this.path.reset();
        this.path.moveTo(x, y);
        this.path.lineTo(x_arc, y);
        this.path.curveTo(x_arc + (radius / 2.0), y, x_arc + radius, y + (radius / 2.0), x_arc + radius, y + radius);
        this.path.lineTo(x_arc + radius, (y + h) - radius);
        this.path.curveTo(x_arc + radius, (y + h) - (radius / 2.0), x_arc + (radius / 2.0), y + h, x_arc, y + h);
        this.path.lineTo(x, y + h);
        this.path.closePath();

        return this.path;
    }

    protected void drawButtonTableEditor(JComponent component, Graphics2D g) {

        JButton button = this.getArrowButton(component);
        if (button != null) {
            Rectangle bounds = button.getBounds();
            int xBut = bounds.x;
            int yBut = this.intValue(this.decodeY(0.0f));
            int wBut = bounds.width;
            int hBut = this.intValue(this.decodeY(3.0f) - 1);

            Shape s = ShapeFactory.getInstace().createRectangle(xBut, yBut, wBut, hBut);
            g.setPaint(this.decodeGradientButton(s, this.getArrowButtonBackgroundColor()));
            g.fill(s);
        }

    }

    protected JButton getArrowButton(JComponent c) {
        for (int i = 0; i < c.getComponentCount(); i++) {
            if (c.getComponent(i) instanceof JButton) {
                return (JButton) c.getComponent(i);
            }
        }
        return null;
    }

    protected Color getArrowButtonBackgroundColor() {
        Color c = null;
        switch (this.state) {
            case BACKGROUND_DISABLED_PRESSED:
                c = (Color) this.backgroundArrowButtonColorDisabled;
                break;
            case BACKGROUND_DISABLED:
                c = (Color) this.backgroundArrowButtonColorDisabled;
                break;
            case BACKGROUND_DISABLED_REQUIRED:
                c = (Color) this.backgroundArrowButtonColorDisabledRequired;
                break;
            case BACKGROUND_ENABLED:
                c = (Color) this.backgroundArrowButtonColorEnabled;
                break;
            case BACKGROUND_MOUSEOVER:
                c = (Color) this.backgroundArrowButtonColorMouseOver;
                break;
            case BACKGROUND_MOUSEOVER_FOCUSED:
                c = (Color) this.backgroundArrowButtonColorMouseOver;
                break;
            case BACKGROUND_PRESSED:
                c = (Color) this.backgroundArrowButtonColorPressed;
                break;
            case BACKGROUND_PRESSED_FOCUSED:
                c = (Color) this.backgroundArrowButtonColorPressed;
                break;
            case BACKGROUND_REQUIRED_FOCUSED:
                c = (Color) this.backgroundArrowButtonColorFocusedRequired;
                break;
            case BACKGROUND_FOCUSED:
                c = (Color) this.backgroundArrowButtonColorFocused;
                break;
            case BACKGROUND_REQUIRED:
                c = (Color) this.backgroundArrowButtonColorRequired;
                break;

            case BACKGROUND_DISABLED_EDITABLE:
                c = (Color) this.backgroundArrowButtonColorEditableDisabled;
                break;
            case BACKGROUND_ENABLED_EDITABLE:
                c = (Color) this.backgroundArrowButtonColorEditableEnabled;
                break;
            case BACKGROUND_ENABLED_SELECTED:
                c = (Color) this.backgroundArrowButtonColorEditableEnabled;
                break;
            case BACKGROUND_MOUSEOVER_EDITABLE:
                c = (Color) this.backgroundArrowButtonColorEditableMouseOver;
                break;
            case BACKGROUND_PRESSED_EDITABLE:
                c = (Color) this.backgroundArrowButtonColorEditablePressed;
                break;
            case BACKGROUND_FOCUSED_EDITABLE:
                c = (Color) this.backgroundArrowButtonColorEditableFocused;
                break;
        }

        return c;
    }

    protected void drawDegradatedBorders(JComponent component, Graphics2D g, Paint[] colors) {
        Paint previousPaint = g.getPaint();
        RenderingHints rh = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int xx = this.intValue(this.decodeX(0.0f) + this.numBorders + this.getMaximumRadius());
        if (xx > this.intValue(this.decodeX(1.0f))) {
            xx = this.intValue(this.decodeX(1.0f));
        }
        int yy = this.intValue(this.decodeY(0.0f) + this.numBorders);
        int w = this.intValue(this.decodeX(3.0f) - xx - 1);
        int h = this.intValue(this.decodeY(3.0f) - (2 * this.numBorders));

        JButton button = this.getArrowButton(component);
        if (button != null) {
            Rectangle bounds = button.getBounds();
            w = (bounds.x + bounds.width) - this.intValue(bounds.width * 0.4) - 1;
        }

        for (int i = 1; i <= colors.length; i++) {
            int yyy = yy - i;
            int hh = (h + (2 * i)) - 1;
            Shape s = this.decodeBorderPath(xx, yyy, w, hh, i - 1);
            g.setPaint(colors[i - 1]);
            g.draw(s);
        }

        g.setPaint(previousPaint);
        g.setRenderingHints(rh);

    }

    protected Shape decodeBorderPath(double x, double y, double w, double h, int borderIndex) {
        // w is just the width without curved zones.
        double radius = this.getRadius();
        double x_arc = this.decodeX(0.0f) + this.numBorders + radius;
        if (x_arc > x) {
            x_arc = x;
            radius = this.getMaximumRadius();
        }

        this.path.reset();
        this.path.moveTo(x, y);
        this.path.lineTo(x_arc, y);
        this.path.curveTo(x_arc - (radius / 2.0), y, x_arc - radius, y + (radius / 2.0), x_arc - radius - borderIndex,
                y + radius);
        this.path.lineTo(x_arc - radius - borderIndex, (y + h) - radius);
        this.path.curveTo(x_arc - radius - borderIndex, (y + h) - (radius / 2.0), x_arc - (radius / 2.0), y + h, x_arc,
                y + h);
        this.path.lineTo(x, y + h);

        this.path.lineTo(w, y + h);

        x_arc = this.decodeX(3.0f) - 1 - this.numBorders - radius;
        if (x_arc < w) {
            x_arc = w;
            radius = this.getMaximumRadius();
        }

        this.path.lineTo(x_arc, y + h);
        this.path.curveTo(x_arc + (radius / 2.0), y + h, x_arc + radius, (y + h) - (radius / 2.0),
                x_arc + radius + borderIndex, (y + h) - radius);
        this.path.lineTo(x_arc + radius + borderIndex, y + radius);
        this.path.curveTo(x_arc + radius + borderIndex, y + (radius / 2.0), x_arc + (radius / 2.0), y, x_arc, y);

        this.path.lineTo(x, y);
        this.path.closePath();

        return this.path;
    }

    protected double getRadius() {
        return this.radius;
    }

    protected double getMaximumRadius() {
        return (this.decodeY(3.0f) - 1 - (2 * this.numBorders)) / 2.0;
    }

}
