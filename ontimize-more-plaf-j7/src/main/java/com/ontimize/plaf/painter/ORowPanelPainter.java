package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import com.ontimize.plaf.utils.LinearGradient;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

public class ORowPanelPainter extends AbstractRegionPainter {

    public static final int BACKGROUND_ENABLED = 1;

    protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);

    protected Color color1 = OntimizeLAFColorUtils.colorHexToColor("#FFFFFF14");

    protected Paint bgPaint;

    public ORowPanelPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        this.paintBackground(g, c, width, height);
    }

    @Override
    protected String getComponentKeyName() {
        return "\"Row\"";
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

    public static boolean hasTitleBorder(Border border) {
        if (border instanceof TitledBorder) {
            return true;
        }
        if (border instanceof CompoundBorder) {
            if (ORowPanelPainter.hasTitleBorder(((CompoundBorder) border).getInsideBorder())) {
                return true;
            }

            if (ORowPanelPainter.hasTitleBorder(((CompoundBorder) border).getOutsideBorder())) {
                return true;
            }
        }
        return false;
    }

    public static Insets getTitleBorderInsets(Border border, JComponent c) {
        if (border instanceof TitledBorder) {
            return border.getBorderInsets(c);
        }

        if (border instanceof CompoundBorder) {
            CompoundBorder compound = (CompoundBorder) border;
            if (compound.getOutsideBorder() instanceof TitledBorder) {
                return ORowPanelPainter.getTitleBorderInsets(compound.getOutsideBorder(), c);
            }
        }
        return border.getBorderInsets(c);
    }

    protected void paintBackground(Graphics2D g, JComponent c, int w, int h) {
        if (ORowPanelPainter.hasTitleBorder(c.getBorder()) || c.isOpaque()) {
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

            Color old = g.getColor();
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
