package com.ontimize.plaf.painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.gui.ApToolBarToggleButton;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

public class OToolBarToggleButtonPainter extends AbstractOButtonPainter {

    public static final int BACKGROUND_SELECTED = 20;

    public static final int BACKGROUND_SELECTED_FOCUSED = 21;

    public static final int BACKGROUND_PRESSED_SELECTED = 22;

    public static final int BACKGROUND_PRESSED_SELECTED_FOCUSED = 23;

    public static final int BACKGROUND_MOUSEOVER_SELECTED = 24;

    public static final int BACKGROUND_MOUSEOVER_SELECTED_FOCUSED = 25;

    public static final int BACKGROUND_DISABLED_SELECTED = 26;

    /**
     * NOTE: The buttons are painted with three layers: 1) The first layer contains a degraded
     * background and a border. 2) Second layer contains an interior border. 3) Third layer paints a
     * centered square to highlight the icon.
     */

    public static Color squareColor = OntimizeLAFColorUtils.setAlpha(Color.white, 0.1);

    // First layer colors...
    protected Color color100 = OntimizeLAFColorUtils.setAlpha(new Color(0x817F7F), 0.50);

    protected Color color101 = OntimizeLAFColorUtils.setAlpha(new Color(0x474746), 0.50);

    protected Color color102 = OntimizeLAFColorUtils.setAlpha(new Color(0xCCCCCC), 0.35);

    protected Color color103 = OntimizeLAFColorUtils.setAlpha(new Color(0xCCCCCC), 0.35);

    protected Color color104 = OntimizeLAFColorUtils.setAlpha(new Color(0x817F7F), 0.70);

    protected Color color105 = OntimizeLAFColorUtils.setAlpha(new Color(0x474746), 0.70);

    protected Color color106 = OntimizeLAFColorUtils.setAlpha(new Color(0x817F7F), 0.70);

    protected Color color107 = OntimizeLAFColorUtils.setAlpha(new Color(0x474746), 0.70);

    protected Color color108 = OntimizeLAFColorUtils.setAlpha(new Color(0x817F7F), 0.80);

    protected Color color109 = OntimizeLAFColorUtils.setAlpha(new Color(0x5A5A5A), 0.60);

    // Borders..
    protected Color color110 = OntimizeLAFColorUtils.setAlpha(new Color(0x8C8A8A), 0.5);

    protected Color color111 = OntimizeLAFColorUtils.setAlpha(new Color(0x5A5A5A), 0.6);

    protected Color color112 = OntimizeLAFColorUtils.setAlpha(new Color(0xCCCCCC), 0.35);

    protected Color color113 = OntimizeLAFColorUtils.setAlpha(new Color(0xCCCCCC), 0.35);

    // Second layer colors...
    protected Color color114 = OntimizeLAFColorUtils.setAlpha(Color.white, 0.2);

    protected Color color115 = OntimizeLAFColorUtils.setAlpha(Color.black, 0.2);

    // Selected State...
    // First layer...
    protected Color color200 = OntimizeLAFColorUtils.setAlpha(this.color108, 0.25);

    protected Color color201 = OntimizeLAFColorUtils.setAlpha(this.color108, 0.25);

    // Borders...
    protected Color color210 = new Color(0x252525);

    protected Color color211 = OntimizeLAFColorUtils.setAlpha(Color.white, 0.75);

    // Second layer colors...
    protected Color color214 = OntimizeLAFColorUtils.setAlpha(Color.white, 0.75);

    protected Color color215 = new Color(0x252525);

    protected Color bgBaseColor;

    protected float defaultAlphaSelectedPercent = 0.5f;

    protected float alphaSelectedPercent;

    protected float defaultAlphaFocusedSelectedPercent = 0.5f;

    protected float alphaFocusedSelectedPercent;

    protected float defaultAlphaPressedSelectedPercent = 0.5f;

    protected float alphaPressedSelectedPercent;

    protected float defaultAlphaPressedSelectedFocusedPercent = 0.5f;

    protected float alphaPressedSelectedFocusedPercent;

    protected float defaultAlphaMouseOverSelectedPercent = 0.5f;

    protected float alphaMouseOverSelectedPercent;

    protected float defaultAlphaMouseOverSelectedFocusedPercent = 0.5f;

    protected float alphaMouseOverSelectedFocusedPercent;

    protected float defaultAlphaDisabledSelectedPercent = 0.5f;

    protected float alphaDisabledSelectedPercent;

    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    protected float arcwidth = 10.0f;

    protected float archeight = 10.0f;

    protected JComponent component;

    public OToolBarToggleButtonPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        this.component = c;

        this.componentColors = extendedCacheKeys;
        if ("TableButton".equals(this.component.getName())) {
            switch (this.state) {
                case BACKGROUND_DEFAULT_FOCUSED:
                    this.paintBackgroundDefaultAndFocused(g);
                    break;
                case BACKGROUND_FOCUSED:
                    this.paintBackgroundFocused(g);
                    break;
            }
        } else {
            super.doPaint(g, c, width, height, extendedCacheKeys);
        }

        switch (this.state) {
            case BACKGROUND_SELECTED:
                this.paintBackgroundSelected(g);
                break;
            case BACKGROUND_SELECTED_FOCUSED:
                this.paintBackgroundPressedAndDefaultAndFocused(g);
                break;
            case BACKGROUND_PRESSED_SELECTED:
                this.paintBackgroundPressedAndDefaultAndFocused(g);
                break;
            case BACKGROUND_PRESSED_SELECTED_FOCUSED:
                this.paintBackgroundPressedAndDefaultAndFocused(g);
                break;
            case BACKGROUND_MOUSEOVER_SELECTED:
                this.paintBackgroundMouseOverAndDefault(g);
                break;
            case BACKGROUND_MOUSEOVER_SELECTED_FOCUSED:
                this.paintBackgroundMouseOverAndDefaultAndFocused(g);
                break;
            case BACKGROUND_DISABLED_SELECTED:
                this.paintBackgroundDisabled(g);
                break;
        }

    }

    protected JComponent getComponent() {
        return this.component;
    }

    @Override
    protected String getComponentKeyName() {
        return "ToolBar:ToggleButton";
    }

    @Override
    protected void init() {
        super.init();
        Object obj = UIManager.getDefaults().get(this.getComponentKeyName() + ".background");
        if (obj instanceof Color) {
            this.bgBaseColor = (Color) obj;
        } else {
            this.bgBaseColor = null;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaSelectedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaSelectedPercent = this.defaultAlphaSelectedPercent;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Disabled+Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaDisabledSelectedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaDisabledSelectedPercent = this.defaultAlphaDisabledSelectedPercent;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Focused+Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaFocusedSelectedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaFocusedSelectedPercent = this.defaultAlphaFocusedSelectedPercent;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[MouseOver+Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaMouseOverSelectedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaMouseOverSelectedPercent = this.defaultAlphaMouseOverSelectedPercent;
        }

        obj = UIManager.getDefaults()
            .get(this.getComponentKeyName() + "[Focused+MouseOver+Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaMouseOverSelectedFocusedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaMouseOverSelectedFocusedPercent = this.defaultAlphaMouseOverSelectedFocusedPercent;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Pressed+Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaPressedSelectedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaPressedSelectedPercent = this.defaultAlphaPressedSelectedPercent;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Focused+Pressed+Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaPressedSelectedFocusedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaPressedSelectedFocusedPercent = this.defaultAlphaPressedSelectedFocusedPercent;
        }

    }

    @Override
    protected Object[] getExtendedCacheKeys(JComponent c) {

        if (!this.initColors && c != null) {
            this.initColors = true;
            Color background = this.bgBaseColor != null ? this.bgBaseColor : c.getBackground();
            this.color100 = background; /* init enabled */
            this.color101 = this.getDerivedColor(background, 0.16666667f, -0.0014193691f, -0.227451f, 0); /*
                                                                                                           * end enabled
                                                                                                           */
            this.color102 = this.getDerivedColor(background, 0.0f, -0.015503876f, 0.29411763f, -39); /*
                                                                                                      * init disabled
                                                                                                      */
            this.color103 = this.getDerivedColor(background, 0.0f, -0.015503876f, 0.29411763f, -39); /*
                                                                                                      * end disabled
                                                                                                      */
            this.color104 = this.getDerivedColor(background, 0.0f, 0.0f, 0.0f, 51); /*
                                                                                     * init focused
                                                                                     */
            this.color105 = this.getDerivedColor(background, 0.16666667f, -0.0014193691f, -0.227451f, 51); /*
                                                                                                            * end
                                                                                                            * focused
                                                                                                            */
            this.color106 = this.getDerivedColor(background, 0.0f, 0.0f, 0.0f, 51); /*
                                                                                     * init mouseover
                                                                                     */
            this.color107 = this.getDerivedColor(background, 0.16666667f, -0.0014193691f, -0.227451f, 51); /*
                                                                                                            * end
                                                                                                            * mouseover
                                                                                                            */
            this.color108 = this.getDerivedColor(background, 0.0f, 0.0f, 0.0f, 76); /*
                                                                                     * init pressed
                                                                                     */
            this.color109 = this.getDerivedColor(background, 0.0f, -0.015503876f, -0.1529412f, 25); /*
                                                                                                     * end pressed
                                                                                                     */

            this.color110 = this.getDerivedColor(background, 0.0f, -0.0012181615f, 0.043137252f, 0); /*
                                                                                                      * init border
                                                                                                      */
            this.color111 = this.getDerivedColor(background, 0.0f, -0.015503876f, -0.1529412f, 25); /*
                                                                                                     * end border
                                                                                                     */
            this.color112 = this.getDerivedColor(background, 0.0f, -0.015503876f, 0.29411763f, -39); /*
                                                                                                      * init border
                                                                                                      * Disabled
                                                                                                      */
            this.color113 = this.getDerivedColor(background, 0.0f, -0.015503876f, 0.29411763f, -39); /*
                                                                                                      * end border
                                                                                                      * Disabled
                                                                                                      */

            this.color114 = this.getDerivedColor(background, 0.0f, -0.015503876f, 0.49411762f, -51); /*
                                                                                                      * init middle
                                                                                                      */
            this.color115 = this.getDerivedColor(background, 0.0f, -0.015503876f, -0.5058824f, -77); /*
                                                                                                      * end middle
                                                                                                      */

            this.color200 = this.getDerivedColor(background, 0.0f, 0.0f, 0.0f, -64); /*
                                                                                      * init selected enabled
                                                                                      */
            this.color201 = this.getDerivedColor(background, 0.0f, 0.0f, 0.0f, -64); /*
                                                                                      * end selected enabled
                                                                                      */

            this.color210 = this.getDerivedColor(background, 0.0f, -0.015503876f, 0.1607843f, 127); /*
                                                                                                     * init selected
                                                                                                     * border
                                                                                                     */
            this.color211 = this.getDerivedColor(background, 0.0f, -0.015503876f, 0.1607843f, 127); /*
                                                                                                     * end selected
                                                                                                     * border
                                                                                                     */

            this.color214 = this.getDerivedColor(background, 0.0f, -0.015503876f, 0.22745097f, -13); /*
                                                                                                      * init middle
                                                                                                      * selected
                                                                                                      */
            this.color215 = this.getDerivedColor(background, 0.0f, -0.015503876f, -0.36078435f, 127); /*
                                                                                                       * end middle
                                                                                                       * selected
                                                                                                       */
        }

        Object[] extendedCacheKeys = null;
        switch (this.state) {
            case BACKGROUND_ENABLED:
                extendedCacheKeys = new Object[] { this.color100, this.color101, this.color110, this.color111,
                        this.color114, this.color115 };
                break;
            case BACKGROUND_DISABLED:
                extendedCacheKeys = new Object[] { this.color102, this.color103, this.color112, this.color113,
                        this.color114, this.color115 };
                break;
            case BACKGROUND_FOCUSED:
                extendedCacheKeys = new Object[] { this.color104, this.color105, this.color110, this.color111,
                        this.color114, this.color115 };
                break;
            case BACKGROUND_MOUSEOVER:
                extendedCacheKeys = new Object[] { this.color106, this.color107, this.color110, this.color111,
                        this.color114, this.color115 };
                break;
            case BACKGROUND_MOUSEOVER_FOCUSED:
                extendedCacheKeys = new Object[] { this.color106, this.color107, this.color110, this.color111,
                        this.color114, this.color115 };
                break;
            case BACKGROUND_PRESSED:
                extendedCacheKeys = new Object[] { this.color108, this.color109, this.color110, this.color111,
                        this.color114, this.color115 };
                break;
            case BACKGROUND_PRESSED_FOCUSED:
                extendedCacheKeys = new Object[] { this.color108, this.color109, this.color110, this.color111,
                        this.color114, this.color115 };
                break;
            case BACKGROUND_SELECTED:
                extendedCacheKeys = new Object[] { this.color200, this.color201, this.color210, this.color211,
                        this.color214, this.color215 };
                break;
            case BACKGROUND_SELECTED_FOCUSED:
                extendedCacheKeys = new Object[] { this.color200, this.color201, this.color210, this.color211,
                        this.color214, this.color215 };
                break;
            case BACKGROUND_PRESSED_SELECTED:
                extendedCacheKeys = new Object[] { this.color200, this.color201, this.color210, this.color211,
                        this.color214, this.color215 };
                break;
            case BACKGROUND_PRESSED_SELECTED_FOCUSED:
                extendedCacheKeys = new Object[] { this.color200, this.color201, this.color210, this.color211,
                        this.color214, this.color215 };
                break;
            case BACKGROUND_MOUSEOVER_SELECTED:
                extendedCacheKeys = new Object[] { this.color200, this.color201, this.color210, this.color211,
                        this.color214, this.color215 };
                break;
            case BACKGROUND_MOUSEOVER_SELECTED_FOCUSED:
                extendedCacheKeys = new Object[] { this.color200, this.color201, this.color210, this.color211,
                        this.color214, this.color215 };
                break;
            case BACKGROUND_DISABLED_SELECTED:
                extendedCacheKeys = new Object[] { this.color200, this.color201, this.color110, this.color111,
                        this.color214, this.color215 };
                break;
            default:
                super.getExtendedCacheKeys(c);
                break;
        }

        return extendedCacheKeys;
    }

    @Override
    protected AlphaComposite getDerivedAlphaComposite() {
        AlphaComposite alpha = null;
        switch (this.state) {
            case BACKGROUND_SELECTED:
                alpha = AlphaComposite.SrcOver
                    .derive((this.alphaSelectedPercent >= 0 && this.alphaSelectedPercent <= 1)
                            ? this.alphaSelectedPercent
                            : this.defaultAlphaSelectedPercent);
                break;
            case BACKGROUND_SELECTED_FOCUSED:
                alpha = AlphaComposite.SrcOver
                    .derive((this.alphaFocusedSelectedPercent >= 0 && this.alphaFocusedSelectedPercent <= 1)
                            ? this.alphaFocusedSelectedPercent
                            : this.defaultAlphaFocusedSelectedPercent);
                break;
            case BACKGROUND_PRESSED_SELECTED:
                alpha = AlphaComposite.SrcOver
                    .derive((this.alphaPressedSelectedPercent >= 0 && this.alphaPressedSelectedPercent <= 1)
                            ? this.alphaPressedSelectedPercent
                            : this.defaultAlphaPressedSelectedPercent);
                break;
            case BACKGROUND_PRESSED_SELECTED_FOCUSED:
                alpha = AlphaComposite.SrcOver
                    .derive((this.alphaPressedSelectedFocusedPercent >= 0
                            && this.alphaPressedSelectedFocusedPercent <= 1) ? this.alphaPressedSelectedFocusedPercent
                                    : this.defaultAlphaPressedSelectedFocusedPercent);
                break;
            case BACKGROUND_MOUSEOVER_SELECTED:
                alpha = AlphaComposite.SrcOver
                    .derive((this.alphaMouseOverSelectedPercent >= 0 && this.alphaMouseOverSelectedPercent <= 1)
                            ? this.alphaMouseOverSelectedPercent
                            : this.defaultAlphaMouseOverSelectedPercent);
                break;
            case BACKGROUND_MOUSEOVER_SELECTED_FOCUSED:
                alpha = AlphaComposite.SrcOver
                    .derive((this.alphaMouseOverSelectedFocusedPercent >= 0
                            && this.alphaMouseOverSelectedFocusedPercent <= 1)
                                    ? this.alphaMouseOverSelectedFocusedPercent
                                    : this.defaultAlphaMouseOverSelectedFocusedPercent);
                break;
            case BACKGROUND_DISABLED_SELECTED:
                alpha = AlphaComposite.SrcOver
                    .derive((this.alphaDisabledSelectedPercent >= 0 && this.alphaDisabledSelectedPercent <= 1)
                            ? this.alphaDisabledSelectedPercent
                            : this.defaultAlphaDisabledSelectedPercent);
                break;
            default:
                alpha = super.getDerivedAlphaComposite();
        }
        return alpha;
    }

    protected boolean isSquareButton() {
        JComponent c = this.getComponent();
        Rectangle bounds = c.getBounds();
        Insets in = c.getInsets();

        int width = bounds.width - in.left - in.right;
        int height = bounds.height - in.top - in.bottom;
        if (width == height) {
            return true;
        }
        return false;
    }

    protected int getRestrictiveMeasure() {
        JComponent c = this.getComponent();
        Rectangle bounds = c.getBounds();
        Insets in = c.getInsets();

        int width = bounds.width - in.left - in.right;
        int height = bounds.height - in.top - in.bottom;
        if (width <= height) {
            return width;
        }
        return height;
    }

    protected boolean hasText() {
        JComponent c = this.getComponent();
        if (c instanceof AbstractButton) {
            String text = ((AbstractButton) c).getText();
            if (text != null && text.length() > 0) {
                return true;
            }
        }
        return false;
    }

    protected int getSquareOffset() {
        JComponent c = this.getComponent();
        if (c != null && !this.isSquareButton() && !this.hasText()) {
            Insets in = c.getInsets();
            Rectangle bounds = c.getBounds();

            int width = bounds.width - in.left - in.right;
            int height = bounds.height - in.top - in.bottom;
            int offset = (height - width) / 2;
            return offset;
        } else if (c != null && this.hasText()) {
            return 4; // margin for separate the text
        }
        return 0; // By default
    }

    public static Color defaultFocusBgColor = new Color(0x366581);

    protected Color focusBgColor;

    @Override
    protected void paintBackgroundDefaultAndFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.focusBgColor != null ? this.focusBgColor : OToolBarToggleButtonPainter.defaultFocusBgColor);
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    @Override
    protected void paintBackgroundFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.focusBgColor != null ? this.focusBgColor : OToolBarToggleButtonPainter.defaultFocusBgColor);
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    @Override
    protected void paintBackgroundDefault(Graphics2D g) {
        g.setComposite(this.getDerivedAlphaComposite());
        // *********************************************
        // First layer...
        this.roundRect = this.decodeRoundRect1();
        g.setPaint(this.decodeGradientFirstLayer(this.roundRect));
        g.fill(this.roundRect);
        // border
        this.roundRect = this.decodeRoundRect1Border();
        g.setPaint(this.decodeGradientFirstLayerBorder(this.roundRect));
        g.draw(this.roundRect);

        // *********************************************
        // Second layer...
        // border
        if (this.state != AbstractOButtonPainter.BACKGROUND_DISABLED) {
            this.roundRect = this.decodeRoundRect2Border();
            g.setPaint(this.decodeGradientMiddleLayerBorder(this.roundRect));
            g.draw(this.roundRect);
        }

        // *********************************************
        // Third layer...
        if (!this.hasText()) {
            this.rect = this.decodeCenterSquare();
            g.setPaint(OToolBarToggleButtonPainter.squareColor);
            g.fill(this.rect);
        }
    }

    @Override
    protected void paintBackgroundEnabled(Graphics2D g) {
        this.paintBackgroundDefault(g);
    }

    @Override
    protected void paintBackgroundDisabled(Graphics2D g) {
        this.paintBackgroundDefault(g);
    }

    @Override
    protected void paintBackgroundMouseOver(Graphics2D g) {
        this.paintBackgroundDefault(g);
    }

    @Override
    protected void paintBackgroundMouseOverAndFocused(Graphics2D g) {
        this.paintBackgroundDefault(g);
    }

    @Override
    protected void paintBackgroundPressed(Graphics2D g) {
        this.paintBackgroundDefault(g);
    }

    @Override
    protected void paintBackgroundPressedAndFocused(Graphics2D g) {
        this.paintBackgroundDefault(g);
    }

    @Override
    protected void paintBackgroundPressedAndDefaultAndFocused(Graphics2D g) {
        this.paintBackgroundSelectedDefault(g);
    }

    @Override
    protected void paintBackgroundMouseOverAndDefault(Graphics2D g) {
        this.paintBackgroundSelectedDefault(g);
    }

    @Override
    protected void paintBackgroundMouseOverAndDefaultAndFocused(Graphics2D g) {
        this.paintBackgroundSelectedDefault(g);
    }

    protected void paintBackgroundSelected(Graphics2D g) {
        this.paintBackgroundSelectedDefault(g);
    }

    protected void paintBackgroundSelectedDefault(Graphics2D g) {
        g.setComposite(this.getDerivedAlphaComposite());
        // *********************************************
        // First layer...
        this.roundRect = this.decodeRoundRect1();
        g.setPaint(this.decodeGradientFirstLayer(this.roundRect));
        g.fill(this.roundRect);
        // border
        this.roundRect = this.decodeRoundRect1Border();
        g.setPaint(this.decodeGradientSelectedFirstLayerBorder(this.roundRect));
        g.draw(this.roundRect);

        // *********************************************
        // Second layer...
        // border
        if (this.state != AbstractOButtonPainter.BACKGROUND_DISABLED) {
            this.roundRect = this.decodeRoundRect2Border();
            g.setPaint(this.decodeGradientSelectedMiddleLayerBorder(this.roundRect));
            g.fill(this.roundRect);
        }

        // *********************************************
        // Third layer...
        if (!this.hasText()) {
            this.rect = this.decodeCenterSquare();
            g.setPaint(OToolBarToggleButtonPainter.squareColor);
            g.fill(this.rect);
        }
    }

    @Override
    protected RoundRectangle2D decodeRoundRect1() {
        JComponent c = this.getComponent();
        if (c != null) {
            Insets in = c.getInsets();
            Rectangle bounds = c.getBounds();
            int x = in.left + 1 - this.getSquareOffset();
            int width = bounds.width - in.left - in.right - 1 + 2 * this.getSquareOffset();
            this.roundRect.setRoundRect(x < 0 ? 0 : x, // x
                    in.top + 1, // y
                    width >= bounds.width ? bounds.width - 1 : width, // width
                    bounds.height - in.top - in.bottom - 1, // height
                    this.arcwidth, this.arcwidth); // rounding
        } else {
            this.roundRect.setRoundRect(this.decodeX(1.0f), // x
                    this.decodeY(1.0f), // y
                    this.decodeX(2.0f) - this.decodeX(1.0f), // width
                    this.decodeY(2.0f) - this.decodeY(1.0f), // height
                    this.arcwidth, this.arcwidth); // rounding
        }
        return this.roundRect;
    }

    protected RoundRectangle2D decodeRoundRect1Border() {
        JComponent c = this.getComponent();
        if (c != null) {
            Insets in = c.getInsets();
            Rectangle bounds = c.getBounds();
            int x = in.left - this.getSquareOffset();
            int width = bounds.width - in.left - in.right + 2 * this.getSquareOffset();
            this.roundRect.setRoundRect(x < 0 ? 0 : x, // x
                    in.top, // y
                    width >= bounds.width ? bounds.width - 1 : width, // width
                    bounds.height - in.top - in.bottom - 1, // height
                    this.arcwidth, this.arcwidth); // rounding
        } else {
            this.roundRect.setRoundRect(this.decodeX(0.0f), // x
                    this.decodeY(0.0f), // y
                    this.decodeX(3.0f) - 1, // width
                    this.decodeY(3.0f) - 1, // height
                    this.arcwidth, this.arcwidth); // rounding
        }
        return this.roundRect;
    }

    protected RoundRectangle2D decodeRoundRect2Border() {
        JComponent c = this.getComponent();
        if (c != null) {
            Insets in = c.getInsets();
            Rectangle bounds = c.getBounds();
            int x = in.left - this.getSquareOffset();
            int width = bounds.width - in.left - in.right + 2 * this.getSquareOffset();
            this.roundRect.setRoundRect(x < 0 ? 0 : x, // x
                    in.top, // y
                    width >= bounds.width ? bounds.width - 1 : width, // width
                    bounds.height - in.top - in.bottom - 2, // height
                    this.arcwidth, this.arcwidth); // rounding
        } else {
            this.roundRect.setRoundRect(this.decodeX(0.0f), // x
                    this.decodeY(0.0f), // y
                    this.decodeX(3.0f) - 1, this.decodeY(2.0f) - this.decodeY(1.0f), this.arcwidth, this.arcwidth); // rounding
        }
        return this.roundRect;
    }

    protected Rectangle2D decodeCenterSquare() {
        JComponent c = this.getComponent();
        if (c instanceof ApToolBarToggleButton) {
            Icon icon = ((ApToolBarToggleButton) c).getIcon();
            Rectangle bounds = c.getBounds();
            Insets in = c.getInsets();
            int width = bounds.width - in.left - in.right;
            int height = bounds.height - in.top - in.bottom;
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();

            int pWidth = iconWidth >= width ? this.getRestrictiveMeasure() : iconWidth;
            int pHeight = iconHeight >= height ? this.getRestrictiveMeasure() : iconHeight;

            this.rect.setRect(bounds.width / 2.0 - pWidth / 2.0, // x
                    bounds.height / 2.0 - pHeight / 2.0, // y
                    pWidth, // width
                    pHeight); // height
        } else {
            this.rect.setRect(this.decodeX(1.13f), // x
                    this.decodeY(1.13f), // y
                    this.decodeX(1.7f), // width
                    this.decodeY(1.7f)); // height
        }
        return this.rect;
    }

    protected Paint decodeGradientFirstLayer(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(x, y, x, y + h, new float[] { 0.0f, 1.0f }, new Color[] {
                (Color) this.componentColors[0], (Color) this.componentColors[1] });
    }

    protected Paint decodeGradientFirstLayerBorder(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(x, y, x, y + h - 1, new float[] { 0.0f, 1.0f }, new Color[] {
                (Color) this.componentColors[2], (Color) this.componentColors[3] });
    }

    /**
     * This method recovers the gradient for the border of the middle layer.
     * @param s
     * @return
     */
    protected Paint decodeGradientMiddleLayerBorder(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(x, y, x, y + h - 1, new float[] { 0.0f, 1.0f }, new Color[] {
                (Color) this.componentColors[4], (Color) this.componentColors[5] });
    }

    protected Paint decodeGradientSelectedFirstLayerBorder(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(x, y, x, y + h - 1, new float[] { 0.0f, 1.0f }, new Color[] {
                (Color) this.componentColors[2], (Color) this.componentColors[3] });
    }

    protected Paint decodeGradientSelectedMiddleLayerBorder(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(x, y, x, y + h - 1, new float[] { 0.0f, 1.0f }, new Color[] {
                (Color) this.componentColors[4], (Color) this.componentColors[5] });
    }

}
