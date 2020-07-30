package com.ontimize.plaf.painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

public class OFormHeaderButtonPainter extends AbstractOButtonPainter {

    public static Color defaultFocusBgColor = new Color(0x366581);

    protected Color focusBgColor;

    public static Color defaultMouseOverBgColor = new Color(0xffffff);

    protected Color mouseOverBgColor;

    protected JComponent component;

    public OFormHeaderButtonPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        this.componentColors = extendedCacheKeys;
        this.component = c;
        switch (this.state) {
            case BACKGROUND_DEFAULT_FOCUSED:
                this.paintBackgroundDefaultAndFocused(g);
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

    protected JComponent getComponent() {
        return this.component;
    }

    @Override
    protected String getComponentKeyName() {
        return "\"FormHeaderButton\"";
    }

    @Override
    protected void init() {
        super.init();

        Object obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Focused].background");
        if (obj instanceof Color) {
            this.focusBgColor = (Color) obj;
        } else {
            this.focusBgColor = OFormHeaderButtonPainter.defaultFocusBgColor;
        }

        obj = UIManager.getLookAndFeelDefaults().get("\"PopupItem\"" + "[MouseOver].background");
        if (obj instanceof Paint) {
            this.mouseOverBgColor = (Color) obj;
        } else {
            this.mouseOverBgColor = OFormHeaderButtonPainter.defaultMouseOverBgColor;
        }

    }

    @Override
    protected void paintBackgroundDefaultAndFocused(Graphics2D g) {

        JComponent c = this.getComponent();
        if (c.getParent() instanceof JPopupMenu) {
            // paintBackgroundMouseOver(g);
            return;
        }

        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.focusBgColor != null ? this.focusBgColor : OFormHeaderButtonPainter.defaultFocusBgColor);
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    @Override
    protected void paintBackgroundFocused(Graphics2D g) {

        JComponent c = this.getComponent();
        if (c.getParent() instanceof JPopupMenu) {
            // paintBackgroundMouseOver(g);
            return;
        }

        AlphaComposite old = (AlphaComposite) g.getComposite();
        g.setComposite(this.getDerivedAlphaComposite());
        this.roundRect = this.decodeRoundRect4();
        g.setPaint(this.focusBgColor != null ? this.focusBgColor : OFormHeaderButtonPainter.defaultFocusBgColor);
        g.fill(this.roundRect);
        g.setComposite(old);
    }

    @Override
    protected void paintBackgroundMouseOver(Graphics2D g) {
        JComponent c = this.getComponent();
        if (c.getParent() instanceof JPopupMenu) {
            this.rect = this.decodeRect();
            g.setPaint(this.mouseOverBgColor);
            g.fill(this.rect);
        }
    }

    @Override
    protected void paintBackgroundMouseOverAndFocused(Graphics2D g) {
        JComponent c = this.getComponent();
        if (c.getParent() instanceof JPopupMenu) {
            this.rect = this.decodeRect();
            g.setPaint(this.mouseOverBgColor);
            g.fill(this.rect);
        }
    }

    @Override
    protected void paintBackgroundPressed(Graphics2D g) {
        JComponent c = this.getComponent();
        if (c.getParent() instanceof JPopupMenu) {
            this.rect = this.decodeRect();
            g.setPaint(this.mouseOverBgColor);
            g.fill(this.rect);
        }
    }

    @Override
    protected void paintBackgroundPressedAndFocused(Graphics2D g) {
        JComponent c = this.getComponent();
        if (c.getParent() instanceof JPopupMenu) {
            this.rect = this.decodeRect();
            g.setPaint(this.mouseOverBgColor);
            g.fill(this.rect);
        }
    }

    protected Rectangle2D decodeRect() {
        this.rect.setRect(this.decodeX(0.0f), // x
                this.decodeY(0.0f), // y
                this.decodeX(3.0f), // width
                this.decodeY(3.0f)); // height
        return this.rect;
    }

}
