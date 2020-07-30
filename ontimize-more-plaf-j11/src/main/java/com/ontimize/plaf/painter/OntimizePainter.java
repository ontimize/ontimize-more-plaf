package com.ontimize.plaf.painter;

import java.awt.Graphics;

import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

public class OntimizePainter extends SynthPainter {

    protected static SynthPainter instance;

    public OntimizePainter() {
        if (instance == null) {
            instance = this;
        }
    }

    public static SynthPainter getInstance() {
        if (instance == null) {
            instance = new OntimizePainter();
        }
        return instance;
    }

    public void paintRootPaneBorder(SynthContext synthcontext, Graphics g, int x, int y, int width, int height) {
        RootPanePainter.getInstance(synthcontext).paintRootPaneBorder(synthcontext, g, x, y, width, height);
    }

}
