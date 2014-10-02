package com.ontimize.plaf.utils;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;

/**
 * The Class PainterUtils.
 */
public final class ContextUtils {

	/** The null painter. */
	static SynthPainter	NULL_PAINTER	= new SynthPainter() {};

	/**
	 * The Constructor.
	 */
	private ContextUtils() {
		super();
	}

	/**
	 * Gets the painter.
	 *
	 * @param context
	 *            the context
	 * @return the painter
	 */
	public static SynthPainter getPainter(SynthContext context) {
		SynthPainter painter = context.getStyle().getPainter(context);
		return painter == null ? ContextUtils.NULL_PAINTER : painter;
	}

	public static void setComponentState(SynthContext context, int state) {
		ReflectionUtils.invoke(context, "setComponentState", state);
	}

	public static void setComponentStyle(SynthContext context, SynthStyle newStyle) {
		ReflectionUtils.invoke(context, "setStyle", newStyle);
	}
}