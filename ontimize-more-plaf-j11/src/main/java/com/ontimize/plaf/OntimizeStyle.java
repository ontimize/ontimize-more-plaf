package com.ontimize.plaf;

import javax.swing.Painter;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthUI;

public interface OntimizeStyle<T> {

    /* Keys and scales for large/small/mini components, based on Apples sizes */
    public static final String LARGE_KEY = "large";

    public static final String SMALL_KEY = "small";

    public static final String MINI_KEY = "mini";

    public static final double LARGE_SCALE = 1.15;

    public static final double SMALL_SCALE = 0.857;

    public static final double MINI_SCALE = 0.714;

    SynthPainter getPainter(SynthContext ctx);

    Painter<T> getBackgroundPainter(SynthContext ctx);

    Painter<T> getForegroundPainter(SynthContext ctx);

    Painter<T> getBorderPainter(SynthContext ctx);

    void installDefaults(SynthContext context, SynthUI ui);

}
