package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class OTableHeaderVisualCalendarRendererPainter extends OTableHeaderRendererPainter {

    protected final Color color1 = new Color(0x959ea3);

    protected final Color color2 = new Color(0xb6bdbf);

    protected Paint topBorderColor;

    protected Paint bottomBorderColor;

    public OTableHeaderVisualCalendarRendererPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        super.doPaint(g, c, width, height, extendedCacheKeys);
    }

    @Override
    protected String getComponentKeyName() {
        return "TableHeader:\"VisualCalendar:TableHeader.renderer\"";
    }

    @Override
    protected void init() {
        super.init();

        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".topBorder");
        if (obj instanceof Paint) {
            this.topBorderColor = (Paint) obj;
        } else {
            this.topBorderColor = this.color1;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".bottomBorder");
        if (obj instanceof Paint) {
            this.bottomBorderColor = (Paint) obj;
        } else {
            this.bottomBorderColor = this.color2;
        }
    }

    @Override
    protected void paintBorders(Graphics2D g, JComponent c) {
        // Painting borders...
        this.rect = this.decodeTopBorder();
        g.setPaint(this.topBorderColor);
        g.fill(this.rect);

        this.rect = this.decodeBottomBorder();
        g.setPaint(this.bottomBorderColor);
        g.fill(this.rect);
    }

}
