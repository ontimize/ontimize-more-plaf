package com.ontimize.plaf.painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.utils.OntimizeLAFColorUtils;


public class OToggleButtonPainter extends AbstractOButtonPainter {


    public static final int BACKGROUND_SELECTED = 20;

    public static final int BACKGROUND_SELECTED_FOCUSED = 21;

    public static final int BACKGROUND_PRESSED_SELECTED = 22;

    public static final int BACKGROUND_PRESSED_SELECTED_FOCUSED = 23;

    public static final int BACKGROUND_MOUSEOVER_SELECTED = 24;

    public static final int BACKGROUND_MOUSEOVER_SELECTED_FOCUSED = 25;

    public static final int BACKGROUND_DISABLED_SELECTED = 26;

    protected Color color30 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -86);

    protected Color color31 = decodeColor("nimbusBlueGrey", 0.0f, -0.06472479f, -0.23137254f, 0);

    protected Color color32 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.06959064f, -0.0745098f, 0);

    protected Color color33 = decodeColor("nimbusBlueGrey", 0.0138888955f, -0.06401469f, -0.07058823f, 0);

    protected Color color34 = decodeColor("nimbusBlueGrey", 0.0f, -0.06530018f, 0.035294116f, 0);

    protected Color color35 = decodeColor("nimbusBlueGrey", 0.0f, -0.06507177f, 0.031372547f, 0);

    protected Color color36 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.05338346f, -0.47058824f, 0);

    protected Color color37 = decodeColor("nimbusBlueGrey", 0.0f, -0.049301825f, -0.36078432f, 0);

    protected Color color38 = decodeColor("nimbusBlueGrey", -0.018518567f, -0.03909774f, -0.2509804f, 0);

    protected Color color39 = decodeColor("nimbusBlueGrey", -0.00505054f, -0.040013492f, -0.13333333f, 0);

    protected Color color40 = decodeColor("nimbusBlueGrey", 0.01010108f, -0.039558575f, -0.1372549f, 0);

    protected Color color41 = decodeColor("nimbusBlueGrey", -0.01111114f, -0.060526315f, -0.3529412f, 0);

    protected Color color42 = decodeColor("nimbusBlueGrey", 0.0f, -0.064372465f, -0.2352941f, 0);

    protected Color color43 = decodeColor("nimbusBlueGrey", -0.006944418f, -0.0595709f, -0.12941176f, 0);

    protected Color color44 = decodeColor("nimbusBlueGrey", 0.0f, -0.061075766f, -0.031372547f, 0);

    protected Color color45 = decodeColor("nimbusBlueGrey", 0.0f, -0.06080256f, -0.035294116f, 0);

    protected Color color46 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -220);

    protected Color color47 = decodeColor("nimbusBlueGrey", 0.0f, -0.066408664f, 0.054901958f, 0);

    protected Color color48 = decodeColor("nimbusBlueGrey", 0.0f, -0.06807348f, 0.086274505f, 0);

    protected Color color49 = decodeColor("nimbusBlueGrey", 0.0f, -0.06924191f, 0.109803915f, 0);


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

    public OToggleButtonPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        super.doPaint(g, c, width, height, extendedCacheKeys);

        switch (state) {
            case BACKGROUND_SELECTED:
                paintBackgroundSelected(g);
                break;
            case BACKGROUND_SELECTED_FOCUSED:
                paintBackgroundSelectedAndFocused(g);
                break;
            case BACKGROUND_PRESSED_SELECTED:
                paintBackgroundPressedAndSelected(g);
                break;
            case BACKGROUND_PRESSED_SELECTED_FOCUSED:
                paintBackgroundPressedAndSelectedAndFocused(g);
                break;
            case BACKGROUND_MOUSEOVER_SELECTED:
                paintBackgroundMouseOverAndSelected(g);
                break;
            case BACKGROUND_MOUSEOVER_SELECTED_FOCUSED:
                paintBackgroundMouseOverAndSelectedAndFocused(g);
                break;
            case BACKGROUND_DISABLED_SELECTED:
                paintBackgroundDisabledAndSelected(g);
                break;
        }
    }


    protected Object[] getExtendedCacheKeys(JComponent c) {
        Object[] extendedCacheKeys = null;
        switch (state) {
            case BACKGROUND_SELECTED:
                extendedCacheKeys = new Object[] {
                        getComponentColor(c, "background", color33, -0.06401469f, -0.07058823f, 0),
                        getComponentColor(c, "background", color34, -0.06530018f, 0.035294116f, 0),
                        getComponentColor(c, "background", color35, -0.06507177f, 0.031372547f, 0) };
                break;
            case BACKGROUND_SELECTED_FOCUSED:
                extendedCacheKeys = new Object[] {
                        getComponentColor(c, "background", color33, -0.06401469f, -0.07058823f, 0),
                        getComponentColor(c, "background", color34, -0.06530018f, 0.035294116f, 0),
                        getComponentColor(c, "background", color35, -0.06507177f, 0.031372547f, 0) };
                break;
            case BACKGROUND_PRESSED_SELECTED:
                extendedCacheKeys = new Object[] {
                        getComponentColor(c, "background", color38, -0.03909774f, -0.2509804f, 0),
                        getComponentColor(c, "background", color39, -0.040013492f, -0.13333333f, 0),
                        getComponentColor(c, "background", color40, -0.039558575f, -0.1372549f, 0) };
                break;
            case BACKGROUND_PRESSED_SELECTED_FOCUSED:
                extendedCacheKeys = new Object[] {
                        getComponentColor(c, "background", color38, -0.03909774f, -0.2509804f, 0),
                        getComponentColor(c, "background", color39, -0.040013492f, -0.13333333f, 0),
                        getComponentColor(c, "background", color40, -0.039558575f, -0.1372549f, 0) };
                break;
            case BACKGROUND_MOUSEOVER_SELECTED:
                extendedCacheKeys = new Object[] {
                        getComponentColor(c, "background", color43, -0.0595709f, -0.12941176f, 0),
                        getComponentColor(c, "background", color44, -0.061075766f, -0.031372547f, 0),
                        getComponentColor(c, "background", color45, -0.06080256f, -0.035294116f, 0) };
                break;
            case BACKGROUND_MOUSEOVER_SELECTED_FOCUSED:
                extendedCacheKeys = new Object[] {
                        getComponentColor(c, "background", color43, -0.0595709f, -0.12941176f, 0),
                        getComponentColor(c, "background", color44, -0.061075766f, -0.031372547f, 0),
                        getComponentColor(c, "background", color45, -0.06080256f, -0.035294116f, 0) };
                break;
            default:
                extendedCacheKeys = super.getExtendedCacheKeys(c);
                break;
        }
        return extendedCacheKeys;
    }

    @Override
    protected String getComponentKeyName() {
        return "ToggleButton";
    }

    @Override
    protected void init() {
        super.init();

        Object obj = UIManager.getDefaults().get(getComponentKeyName() + "[Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaSelectedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaSelectedPercent = defaultAlphaSelectedPercent;
        }

        obj = UIManager.getDefaults().get(getComponentKeyName() + "[Disabled+Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaDisabledSelectedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaDisabledSelectedPercent = defaultAlphaDisabledSelectedPercent;
        }

        obj = UIManager.getDefaults().get(getComponentKeyName() + "[Focused+Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaFocusedSelectedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaFocusedSelectedPercent = defaultAlphaFocusedSelectedPercent;
        }

        obj = UIManager.getDefaults().get(getComponentKeyName() + "[MouseOver+Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaMouseOverSelectedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaMouseOverSelectedPercent = defaultAlphaMouseOverSelectedPercent;
        }

        obj = UIManager.getDefaults().get(getComponentKeyName() + "[Focused+MouseOver+Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaMouseOverSelectedFocusedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaMouseOverSelectedFocusedPercent = defaultAlphaMouseOverSelectedFocusedPercent;
        }

        obj = UIManager.getDefaults().get(getComponentKeyName() + "[Pressed+Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaPressedSelectedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaPressedSelectedPercent = defaultAlphaPressedSelectedPercent;
        }

        obj = UIManager.getDefaults().get(getComponentKeyName() + "[Focused+Pressed+Selected].alphaTransparency");
        if (obj instanceof Number) {
            this.alphaPressedSelectedFocusedPercent = ((Number) obj).floatValue();
        } else {
            this.alphaPressedSelectedFocusedPercent = defaultAlphaPressedSelectedFocusedPercent;
        }

    }

    protected AlphaComposite getDerivedAlphaComposite() {
        AlphaComposite alpha = null;
        switch (state) {
            case BACKGROUND_SELECTED:
                alpha = AlphaComposite.SrcOver.derive((alphaSelectedPercent >= 0 && alphaSelectedPercent <= 1)
                        ? alphaSelectedPercent : defaultAlphaSelectedPercent);
                break;
            case BACKGROUND_SELECTED_FOCUSED:
                alpha = AlphaComposite.SrcOver
                    .derive((alphaFocusedSelectedPercent >= 0 && alphaFocusedSelectedPercent <= 1)
                            ? alphaFocusedSelectedPercent : defaultAlphaFocusedSelectedPercent);
                break;
            case BACKGROUND_PRESSED_SELECTED:
                alpha = AlphaComposite.SrcOver
                    .derive((alphaPressedSelectedPercent >= 0 && alphaPressedSelectedPercent <= 1)
                            ? alphaPressedSelectedPercent : defaultAlphaPressedSelectedPercent);
                break;
            case BACKGROUND_PRESSED_SELECTED_FOCUSED:
                alpha = AlphaComposite.SrcOver
                    .derive((alphaPressedSelectedFocusedPercent >= 0 && alphaPressedSelectedFocusedPercent <= 1)
                            ? alphaPressedSelectedFocusedPercent : defaultAlphaPressedSelectedFocusedPercent);
                break;
            case BACKGROUND_MOUSEOVER_SELECTED:
                alpha = AlphaComposite.SrcOver
                    .derive((alphaMouseOverSelectedPercent >= 0 && alphaMouseOverSelectedPercent <= 1)
                            ? alphaMouseOverSelectedPercent : defaultAlphaMouseOverSelectedPercent);
                break;
            case BACKGROUND_MOUSEOVER_SELECTED_FOCUSED:
                alpha = AlphaComposite.SrcOver
                    .derive((alphaMouseOverSelectedFocusedPercent >= 0 && alphaMouseOverSelectedFocusedPercent <= 1)
                            ? alphaMouseOverSelectedFocusedPercent : defaultAlphaMouseOverSelectedFocusedPercent);
                break;
            case BACKGROUND_DISABLED_SELECTED:
                alpha = AlphaComposite.SrcOver
                    .derive((alphaDisabledSelectedPercent >= 0 && alphaDisabledSelectedPercent <= 1)
                            ? alphaDisabledSelectedPercent : defaultAlphaDisabledSelectedPercent);
                break;
            default:
                alpha = super.getDerivedAlphaComposite();
        }
        return alpha;
    }


    protected void paintBackgroundSelected(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(getDerivedAlphaComposite());
        roundRect = decodeRoundRect5();
        g.setPaint(color30);
        g.fill(roundRect);
        roundRect = decodeRoundRect2();
        g.setPaint(decodeGradient9(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect3();
        g.setPaint(decodeGradient10(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect7();
        g.setPaint(decodeGradient15(roundRect));
        g.fill(roundRect);
        g.setPaint(decodeGradient16(roundRect));
        g.fill(roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundSelectedAndFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(getDerivedAlphaComposite());
        roundRect = decodeRoundRect6();
        g.setPaint(color16);
        g.fill(roundRect);
        roundRect = decodeRoundRect2();
        g.setPaint(decodeGradient9(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect3();
        g.setPaint(decodeGradient10(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect7();
        g.setPaint(decodeGradient15(roundRect));
        g.fill(roundRect);
        g.setPaint(decodeGradient16(roundRect));
        g.fill(roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundPressedAndSelected(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(getDerivedAlphaComposite());
        roundRect = decodeRoundRect5();
        g.setPaint(color30);
        g.fill(roundRect);
        roundRect = decodeRoundRect2();
        g.setPaint(decodeGradient11(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect3();
        g.setPaint(decodeGradient10(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect7();
        g.setPaint(decodeGradient15(roundRect));
        g.fill(roundRect);
        g.setPaint(decodeGradient16(roundRect));
        g.fill(roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundPressedAndSelectedAndFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(getDerivedAlphaComposite());
        roundRect = decodeRoundRect6();
        g.setPaint(color16);
        g.fill(roundRect);
        roundRect = decodeRoundRect2();
        g.setPaint(decodeGradient11(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect3();
        g.setPaint(decodeGradient10(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect7();
        g.setPaint(decodeGradient15(roundRect));
        g.fill(roundRect);
        g.setPaint(decodeGradient16(roundRect));
        g.fill(roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundMouseOverAndSelected(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(getDerivedAlphaComposite());
        roundRect = decodeRoundRect5();
        g.setPaint(color30);
        g.fill(roundRect);
        roundRect = decodeRoundRect2();
        g.setPaint(decodeGradient12(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect3();
        g.setPaint(decodeGradient10(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect7();
        g.setPaint(decodeGradient15(roundRect));
        g.fill(roundRect);
        g.setPaint(decodeGradient16(roundRect));
        g.fill(roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundMouseOverAndSelectedAndFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(getDerivedAlphaComposite());
        roundRect = decodeRoundRect6();
        g.setPaint(color16);
        g.fill(roundRect);
        roundRect = decodeRoundRect2();
        g.setPaint(decodeGradient12(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect3();
        g.setPaint(decodeGradient10(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect7();
        g.setPaint(decodeGradient15(roundRect));
        g.fill(roundRect);
        g.setPaint(decodeGradient16(roundRect));
        g.fill(roundRect);
        g.setComposite(old);
    }

    protected void paintBackgroundDisabledAndSelected(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(getDerivedAlphaComposite());
        roundRect = decodeRoundRect5();
        g.setPaint(color46);
        g.fill(roundRect);
        roundRect = decodeRoundRect2();
        g.setPaint(decodeGradient13(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect3();
        g.setPaint(decodeGradient14(roundRect));
        g.fill(roundRect);
        roundRect = decodeRoundRect7();
        g.setPaint(decodeGradient15(roundRect));
        g.fill(roundRect);
        g.setPaint(decodeGradient16(roundRect));
        g.fill(roundRect);
        g.setComposite(old);
    }

    protected RoundRectangle2D decodeRoundRect6() {
        roundRect.setRoundRect(decodeX(0.08571429f), // x
                decodeY(0.08571429f), // y
                decodeX(2.914286f) - decodeX(0.08571429f), // width
                decodeY(2.9142857f) - decodeY(0.08571429f), // height
                11.0f, 11.0f); // rounding
        return roundRect;
    }

    protected RoundRectangle2D decodeRoundRect7() {
        roundRect.setRoundRect(decodeX(0.42857143f), // x
                decodeY(0.42857143f), // y
                decodeX(2.5714285f) - decodeX(0.42857143f), // width
                decodeY(2.5714285f) - decodeY(0.42857143f), // height
                7.0f, 7.0f); // rounding
        return roundRect;
    }

    protected Paint decodeGradient9(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.5f, 1.0f },
                new Color[] { color31,
                        decodeColor(color31, color32, 0.5f),
                        color32 });
    }

    protected Paint decodeGradient10(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.06684492f, 0.13368984f, 0.56684494f, 1.0f },
                new Color[] { (Color) componentColors[0],
                        decodeColor((Color) componentColors[0], (Color) componentColors[1], 0.5f),
                        (Color) componentColors[1],
                        decodeColor((Color) componentColors[1], (Color) componentColors[2], 0.5f),
                        (Color) componentColors[2] });
    }

    protected Paint decodeGradient11(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.5f, 1.0f },
                new Color[] { color36,
                        decodeColor(color36, color37, 0.5f),
                        color37 });
    }

    protected Paint decodeGradient12(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.5f, 1.0f },
                new Color[] { color41,
                        decodeColor(color41, color42, 0.5f),
                        color42 });
    }

    protected Paint decodeGradient13(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.5f, 1.0f },
                new Color[] { color47,
                        decodeColor(color47, color48, 0.5f),
                        color48 });
    }

    protected Paint decodeGradient14(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.06684492f, 0.13368984f, 0.56684494f, 1.0f },
                new Color[] { color48,
                        decodeColor(color48, color49, 0.5f),
                        color49,
                        decodeColor(color49, color49, 0.5f),
                        color49 });
    }

    protected Paint decodeGradient15(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.1f, 1.0f },
                new Color[] { OntimizeLAFColorUtils.setAlpha(Color.black, 0.6),
                        OntimizeLAFColorUtils.setAlpha(Color.gray, 0.12),
                        OntimizeLAFColorUtils.setAlpha(Color.white, 0.0) });
    }

    protected Paint decodeGradient16(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.0f * w) + x, (0.5f * h) + y, (1.0f * w) + x, (0.5f * h) + y,
                new float[] { 0.0f, 0.05f, 1.0f },
                new Color[] { OntimizeLAFColorUtils.setAlpha(Color.black, 0.6),
                        OntimizeLAFColorUtils.setAlpha(Color.gray, 0.12),
                        OntimizeLAFColorUtils.setAlpha(Color.white, 0.0) });
    }

}
