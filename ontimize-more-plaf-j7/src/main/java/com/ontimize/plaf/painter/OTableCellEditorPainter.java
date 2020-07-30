package com.ontimize.plaf.painter;

import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.JComponent;

public class OTableCellEditorPainter extends AbstractOTextFieldPainter {

    public OTableCellEditorPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected String getComponentKeyName() {
        return "\"Table.editor\"";
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        super.doPaint(g, c, width, height, extendedCacheKeys);
    }

    @Override
    protected void paintBackgroundDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {

        this.drawEditorBackground(g, c);
    }

    @Override
    protected void paintBackgroundEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {

        this.drawEditorBackground(g, c);

    }

    @Override
    protected void paintBackgroundRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {

        this.drawEditorBackground(g, c);

    }

    @Override
    protected void paintBackgroundFocused(Graphics2D g, JComponent c, int x, int y, int width, int height) {

        this.drawEditorBackground(g, c);
    }

    @Override
    protected void drawEditorBackground(Graphics2D g, JComponent c) {
        this.rect = this.decodeRect();
        g.setPaint(this.getBackgroundForState(c));
        g.fill(this.rect);

        Shape s = this.decodeLeftInnerShadowRect();
        g.setPaint(this.decodeLeftInnerShadowGradient(s));
        g.fill(s);

        s = this.decodeRightInnerShadowRect();
        g.setPaint(this.decodeRightInnerShadowGradient(s));
        g.fill(s);

        s = this.decodeTopInnerShadowRect();
        g.setPaint(this.decodeTopInnerShadowGradient(s));
        g.fill(s);

        s = this.decodeBottomInnerShadowRect();
        g.setPaint(this.decodeBottomInnerShadowGradient(s));
        g.fill(s);
    }

}
