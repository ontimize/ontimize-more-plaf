package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.utils.LinearGradient;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

public class OGridPanelPainter extends AbstractRegionPainter {

    public static final int BACKGROUND_ENABLED = 1;

    protected int state; // refers to one of the static ints above
    protected PaintContext ctx;

    protected Paint bgPaint;

    public OGridPanelPainter(int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx = ctx;

        this.init();
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        this.paintBackground(g, c, width, height);
    }

    @Override
    protected String getComponentKeyName() {
        return "\"Grid\"";
    }

    @Override
    protected void init() {

        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".bgpaint");
        if (obj instanceof Paint) {
            this.bgPaint = (Paint) obj;
        }
    }

    protected void paintBackground(Graphics2D g, JComponent c, int w, int h) {
        if (c.isOpaque()) {
            Color old = g.getColor();
            Insets insets = c.getInsets();

            Paint background = c.getBackground();
            if (this.bgPaint instanceof LinearGradient) {
                Rectangle bounds = new Rectangle();
                bounds.x = insets.left;
                bounds.y = insets.top;
                bounds.width = w - insets.left - insets.right;
                bounds.height = h - insets.top - insets.bottom;
                background = OntimizeLAFColorUtils.decodeGradient(bounds, (LinearGradient) this.bgPaint);
            }

            g.setPaint(background);
            g.fillRect(insets.left, insets.top, w - insets.left - insets.right, h - insets.top - insets.bottom);
            g.setColor(old);
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

}
