package com.ontimize.plaf.painter;

import java.awt.Graphics;

import javax.swing.JViewport;
import javax.swing.plaf.synth.SynthContext;

/**
 * Interface implemented by UI delegates that paint onto a JViewport.
 */
public interface ViewportPainter {

    public void paintViewport(SynthContext context, Graphics g, JViewport c);

}
