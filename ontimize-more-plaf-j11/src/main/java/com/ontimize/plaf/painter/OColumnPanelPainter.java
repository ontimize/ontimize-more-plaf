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

public class OColumnPanelPainter extends AbstractRegionPainter {

    public static final int BACKGROUND_ENABLED = 1;

    protected Color color1 = OntimizeLAFColorUtils.colorHexToColor("#FFFFFF14");

    protected Paint bgPaint;

    public OColumnPanelPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        this.paintBackground(g, c, width, height);
    }

    @Override
    protected String getComponentKeyName() {
        return "\"Column\"";
    }

    @Override
    protected void init() {
        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".bgpaint");
        if (obj instanceof Paint) {
            this.bgPaint = (Paint) obj;
        } else {
            this.bgPaint = null;
        }
    }

    protected void paintBackground(Graphics2D g, JComponent c, int w, int h) {
        if (ORowPanelPainter.hasTitleBorder(c.getBorder()) || c.isOpaque()) {
            Color old = g.getColor();

            Insets insets = ORowPanelPainter.getTitleBorderInsets(c.getBorder(), c);
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

    protected int decodeHeight(int height) {
        return (int) (height - (height * 0.2));
    }

}
