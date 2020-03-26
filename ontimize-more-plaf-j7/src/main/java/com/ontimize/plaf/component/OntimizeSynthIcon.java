package com.ontimize.plaf.component;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.plaf.synth.SynthContext;

/**
 * An icon that delegates to a painter.
 *
 * <p>Based on NimbusIcon by Richard Bair. Reimplemented because too much is package local.</p>
 *
 * @see com.sun.java.swing.plaf.nimbus.NimbusIcon
 */
public class OntimizeSynthIcon {
	protected int		width;
	protected int		height;
	protected String	prefix;
	protected String	key;

	public static Icon newOntimizeIcon(String prefix, String key, int w, int h) {
		return isLegacyVersion() ? new OntimizeSynthIconJ7(prefix, key, w, h) : new OntimizeSynthIconJ9(prefix, key, w, h);
	}

	private static boolean isLegacyVersion() {
		String jvmVersion = System.getProperty("java.specification.version");
		return "1.7".equals(jvmVersion) || "1.8".equals(jvmVersion);
	}

	/**
	 * Returns the icon's width. The {@code getIconWidth(context)} method is called for {@code SynthIcon}.
	 *
	 * @param icon
	 *            the icon
	 * @param context
	 *            {@code SynthContext} requesting the icon, may be null.
	 * @return an int specifying the width of the icon.
	 */
	public static int getIconWidth(Icon icon, SynthContext context) {
		if (icon == null) {
			return 0;
		}
		if (icon instanceof OntimizeSynthIconJ7) {
			return ((OntimizeSynthIconJ7) icon).getIconWidth(context);
		}
		if (icon instanceof OntimizeSynthIconJ9) {
			return ((OntimizeSynthIconJ9) icon).getIconWidth(context);
		}
		return icon.getIconWidth();
	}

	/**
	 * Returns the icon's height. The {@code getIconHeight(context)} method is called for {@code SynthIcon}.
	 *
	 * @param icon
	 *            the icon
	 * @param context
	 *            {@code SynthContext} requesting the icon, may be null.
	 * @return an int specifying the height of the icon.
	 */
	public static int getIconHeight(Icon icon, SynthContext context) {
		if (icon == null) {
			return 0;
		}
		if (icon instanceof OntimizeSynthIconJ7) {
			return ((OntimizeSynthIconJ7) icon).getIconHeight(context);
		}
		if (icon instanceof OntimizeSynthIconJ9) {
			return ((OntimizeSynthIconJ9) icon).getIconHeight(context);
		}
		return icon.getIconHeight();
	}

	/**
	 * Paints the icon. The {@code paintIcon(context, g, x, y, width, height)} method is called for {@code SynthIcon}.
	 *
	 * @param icon
	 *            the icon
	 * @param context
	 *            identifies hosting region, may be null.
	 * @param g
	 *            the graphics context
	 * @param x
	 *            the x location to paint to
	 * @param y
	 *            the y location to paint to
	 * @param width
	 *            the width of the region to paint to, may be 0
	 * @param height
	 *            the height of the region to paint to, may be 0
	 */
	public static void paintIcon(Icon icon, SynthContext context, Graphics g, int x, int y, int width, int height) {
		if (icon instanceof OntimizeSynthIconJ7) {
			((OntimizeSynthIconJ7) icon).paintIcon(context, g, x, y, width, height);
		} else if (icon instanceof OntimizeSynthIconJ9) {
			((OntimizeSynthIconJ9) icon).paintIcon(context, g, x, y, width, height);
		} else if (icon != null) {
			icon.paintIcon(context.getComponent(), g, x, y);
		}
	}
}
