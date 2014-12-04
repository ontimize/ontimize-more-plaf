package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class OFormButtonPanelPainter extends AbstractRegionPainter {

    public static int BACKGROUND_ENABLED = 1;

    protected int state; // refers to one of the static ints above
    protected PaintContext ctx;

    protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);

    protected Color background, bottomBackgroundColor, topBackgroundColor;

    public OFormButtonPanelPainter(int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx = ctx;
    }

    @Override
    protected String getComponentKeyName() {
        return "\"FormButtonPanel\"";
    }

    @Override
    protected void init() {
        Object obj = UIManager.getDefaults().get(this.getComponentKeyName() + ".background");
        if (obj instanceof Color) {
            this.background = (Color) obj;
        } else {
            this.background = Color.black;
        }

        this.topBackgroundColor = UIManager.getColor(this.getComponentKeyName() + ".topBackgroundColor");
        this.bottomBackgroundColor = UIManager.getColor(this.getComponentKeyName() + ".bottomBackgroundColor");

    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        this.paintBackground(g, c, width, height);
    }

    protected void paintBackground(Graphics2D g, JComponent c, int w, int h) {

        g.setColor(this.background);
        g.fillRect(0, 0, w, h);

        GradientPaint downPaint = new GradientPaint(0, h, this.bottomBackgroundColor, 0, 0, this.topBackgroundColor);
        Graphics2D g2D = g;
        Paint oldPaint = g2D.getPaint();
        g2D.setPaint(downPaint);
        int arc = 6;
        w = w - 1;
        this.path.reset();
        this.path.moveTo(0, h);
        this.path.lineTo(0, 0);
        this.path.lineTo(w - arc, 0);
        this.path.quadTo(w, 0, w, arc);
        this.path.lineTo(w, h);
        this.path.closePath();

        g.fill(this.path);

        g2D.setPaint(oldPaint);

    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    protected int decodeHeight(int height) {
        return (int) (height - (height * 0.2));
    }
}
