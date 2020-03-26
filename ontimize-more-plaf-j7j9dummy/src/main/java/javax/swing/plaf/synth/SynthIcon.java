package javax.swing.plaf.synth;

import java.awt.Graphics;

import javax.swing.Icon;

public interface SynthIcon extends Icon {

	/**
	 * Paints the icon at the specified location for the given synth context.
	 *
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
	void paintIcon(SynthContext context, Graphics g, int x, int y, int width, int height);

	/**
	 * Returns the icon's width for the given synth context.
	 *
	 * @param context
	 *            {@code SynthContext} requesting the Icon, may be null.
	 * @return an int specifying the width of the icon.
	 */
	int getIconWidth(SynthContext context);

	/**
	 * Returns the icon's height for the given synth context.
	 *
	 * @param context
	 *            {@code SynthContext} requesting the Icon, may be null.
	 * @return an int specifying the height of the icon.
	 */
	int getIconHeight(SynthContext context);

}

