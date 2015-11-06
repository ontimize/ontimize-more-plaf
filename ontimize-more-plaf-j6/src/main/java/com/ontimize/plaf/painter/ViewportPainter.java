package com.ontimize.plaf.painter;

import java.awt.Graphics;

import javax.swing.JViewport;

import com.ontimize.plaf.OntimizeContext;

/**
 * Interface implemented by UI delegates that paint onto a JViewport.
 */
public interface ViewportPainter {
    public void paintViewport(OntimizeContext context, Graphics g, JViewport c);
}
