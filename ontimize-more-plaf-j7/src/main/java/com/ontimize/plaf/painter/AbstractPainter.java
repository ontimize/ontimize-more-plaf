package com.ontimize.plaf.painter;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import javax.swing.Painter;


public abstract class AbstractPainter implements Painter<JComponent> {

    @Override
    public void paint(Graphics2D g, JComponent component, int width, int height) {
        g = (Graphics2D) g.create();
        configureGraphics(g);
        doPaint(g, component, width, height, getExtendedCacheKeys(component));
        g.dispose();
    }

    protected void configureGraphics(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }

    protected abstract void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys);

    protected Object[] getExtendedCacheKeys(JComponent c) {
        return null;
    }

}
