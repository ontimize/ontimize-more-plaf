package com.ontimize.plaf.painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class OFieldButtonPainter extends AbstractOButtonPainter {

    public static Color defaultFocusBgColor = new Color(0x366581);

    public static Color defaultMouseOverBgColor = new Color(0x366581);

    public static Color defaultFocusedMouseOverBgColor = new Color(0x366581);

    protected Color focusBgColor;

    protected Color mouseOverBgColor;

    protected Color focusedMouseOverBgColor;

    public OFieldButtonPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        this.componentColors = extendedCacheKeys;
        switch (this.state) {
            case BACKGROUND_DEFAULT_FOCUSED:
                this.paintBackgroundDefaultAndFocused(g);
                break;
            case BACKGROUND_FOCUSED:
                this.paintBackgroundFocused(g);
                break;

            case BACKGROUND_MOUSEOVER_DEFAULT:
                this.paintBackgroundMouseOverAndDefault(g);
                break;
            case BACKGROUND_MOUSEOVER:
                this.paintBackgroundMouseOver(g);
                break;
            case BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED:
                this.paintBackgroundMouseOverAndDefaultAndFocused(g);
                break;
            case BACKGROUND_MOUSEOVER_FOCUSED:
                this.paintBackgroundMouseOverAndFocused(g);
        }
    }

    @Override
    protected String getComponentKeyName() {
        return "\"FieldButton\"";
    }

    @Override
    protected void init() {
        super.init();

        Object obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Focused].background");
        if (obj instanceof Color) {
            this.focusBgColor = (Color) obj;
        } else {
            this.focusBgColor = OFieldButtonPainter.defaultFocusBgColor;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[MouseOver].background");
        if (obj instanceof Color) {
            this.mouseOverBgColor = (Color) obj;
        } else {
            this.mouseOverBgColor = OMenuButtonSelectionPainter.defaultMouseOverBgColor;
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Focused+MouseOver].background");
        if (obj instanceof Color) {
            this.focusedMouseOverBgColor = (Color) obj;
        } else {
            this.focusedMouseOverBgColor = OMenuButtonSelectionPainter.defaultFocusedMouseOverBgColor;
        }

    }

    @Override
    protected void paintBackgroundDefaultAndFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.focusBgColor != null ? this.focusBgColor : OFieldButtonPainter.defaultFocusBgColor);
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    @Override
    protected void paintBackgroundFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.focusBgColor != null ? this.focusBgColor : OFieldButtonPainter.defaultFocusBgColor);
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    @Override
    protected void paintBackgroundMouseOverAndDefault(Graphics2D g) {

        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.mouseOverBgColor != null ? this.mouseOverBgColor
                : OMenuButtonSelectionPainter.defaultMouseOverBgColor);
        g.fill(this.roundRect);
        g.setComposite(old);

    }

    @Override
    protected void paintBackgroundMouseOver(Graphics2D g) {

        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.mouseOverBgColor != null ? this.mouseOverBgColor
                : OMenuButtonSelectionPainter.defaultMouseOverBgColor);
        g.fill(this.roundRect);
        g.setComposite(old);

    }

    @Override
    protected void paintBackgroundMouseOverAndFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.focusedMouseOverBgColor != null ? this.focusedMouseOverBgColor
                : OMenuButtonSelectionPainter.defaultFocusedMouseOverBgColor);
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    @Override
    protected void paintBackgroundMouseOverAndDefaultAndFocused(Graphics2D g) {
        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.focusedMouseOverBgColor != null ? this.focusedMouseOverBgColor
                : OMenuButtonSelectionPainter.defaultFocusedMouseOverBgColor);
        g.fill(this.roundRect);
        g.setComposite(old);

    }

}
