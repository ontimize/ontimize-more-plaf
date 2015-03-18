package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.utils.LinearGradient;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

/**
 * This class indicates the component name for the component that must be
 * painted. Paint instructions, indications, ... are encoded in the class that
 * this class extends (named {@link AbstractOTextFieldPainter} )
 *
 * This kind of extensions and final and abstract painters were developed to
 * allow users to have several componentes which are painted in the same way,
 * and configured by the same set of properties, but having only a common code
 * to paint (the class that this one extends).
 *
 * @author Imatia Innovation
 *
 */
public class OFormTitlePainter extends AbstractRegionPainter {

	public static final int BACKGROUND_ENABLED = 0;
	public static final int BORDER_ENABLED = 1;

	protected static Color defaultShadowColor = new Color(0.0f, 0.0f, 0.0f, 0.2f);

	protected Color color1 = this.decodeColor("nimbusBlueGrey", -0.015872955f, -0.07995863f, 0.15294117f, 0);

	// TODO: The border is overriden by Ontimize (SoftBevelBorder2). So some
	// border params will not be able to be configured until Ontimize was
	// modified. Change Ontimzie!!

	protected Paint backgroundColor;
	protected Paint shadowColor;
	protected Paint bgPaint;

	public OFormTitlePainter(int state, PaintContext ctx) {
		super(state, ctx);
	}

	@Override
	protected String getComponentKeyName() {
		return "\"FormTitle\"";
	}

	@Override
	protected void init() {

		Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".background");
		if (obj instanceof Paint) {
			this.backgroundColor = (Paint) obj;
		} else {
			this.backgroundColor = this.color1;
		}

		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".bgpaint");
		if (obj instanceof Paint) {
			this.bgPaint = (Paint) obj;
		} else {
			this.bgPaint = null;
		}

		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".shadow");
		if (obj instanceof Paint) {
			this.shadowColor = (Paint) obj;
		} else {
			this.shadowColor = OFormTitlePainter.defaultShadowColor;
		}
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		// componentColors = extendedCacheKeys;
		Insets insets = c.getInsets();
		int x = insets.left;
		int y = insets.top;
		int cwidth = width - insets.left - insets.right;
		int cheight = height - insets.top - insets.bottom;

		switch (this.state) {
		case BACKGROUND_ENABLED:
			this.paintBackgroundEnabled(g, c, x, y, cwidth, cheight);
			break;
		case BORDER_ENABLED:
			this.paintBorderEnabled(g, c, 0, 0, width, height);
			break;
		}
	}

	protected void paintBackgroundEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		Color oldColor = g.getColor();
		Paint background = c.getBackground();
		if (this.bgPaint != null) {
			Rectangle bounds = new Rectangle();
			bounds.x = x;
			bounds.y = y;
			bounds.width = width;
			bounds.height = height;
			background = OntimizeLAFColorUtils.decodeGradient(bounds, (LinearGradient) this.bgPaint);
		}
		g.setPaint(background);

		Shape shape = this.decodeFormTitleShape(c);
		g.fill(shape);
		g.setColor(oldColor);
	}

	protected void paintBorderEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		Insets insets = c.getInsets();
		int arc = (int) ((height - insets.top - insets.bottom) * 0.4);

		Color oldColor = g.getColor();

		Path2D shadow = new Path2D.Double(Path2D.WIND_EVEN_ODD);
		shadow.moveTo(x + insets.left, y + insets.top);
		shadow.quadTo((x + insets.left) - arc, y + insets.top, (x + insets.left) - arc, y + insets.top + arc);
		shadow.lineTo((x + insets.left) - arc, (y + height) - insets.bottom - arc);
		shadow.quadTo((x + insets.left) - arc, (y + height) - insets.bottom, x + insets.left, (y + height) - insets.bottom - 1);

		shadow.quadTo(((x + insets.left) - arc) + 1, (y + height) - insets.bottom - 1, ((x + insets.left) - arc) + 1, (y + height) - insets.bottom - arc - 1);
		shadow.lineTo(((x + insets.left) - arc) + 1, y + insets.top + arc);
		shadow.quadTo(((x + insets.left) - arc) + 1, y + insets.top + 1, x + insets.left, y + insets.top);

		g.setPaint(this.shadowColor);
		g.draw(shadow);

		shadow.reset();
		shadow.moveTo((x + width) - insets.right - 1, y + insets.top);
		shadow.quadTo(((x + width) - insets.right - 1) + arc, y + insets.top, ((x + width) - insets.right - 1) + arc, y + insets.top + arc);
		shadow.lineTo(((x + width) - insets.right - 1) + arc, (y + height) - insets.bottom - arc);
		shadow.quadTo(((x + width) - insets.right - 1) + arc, (y + height) - insets.bottom, (x + width) - insets.right - 1, (y + height) - insets.bottom - 1);

		shadow.quadTo((((x + width) - insets.right) + arc) - 2, (y + height) - insets.bottom - 1, (((x + width) - insets.right) + arc) - 2, (y + height) - insets.bottom - arc - 1);
		shadow.lineTo((((x + width) - insets.right) + arc) - 2, y + insets.top + arc);
		shadow.quadTo((((x + width) - insets.right) + arc) - 2, y + insets.top + 1, (x + width) - insets.right - 1, y + insets.top);

		g.setPaint(this.shadowColor);
		g.draw(shadow);

		g.setColor(oldColor);
	}

	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}

	protected Shape decodeFormTitleShape(JComponent c) {
		Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);
		Insets insets = c.getInsets();
		Rectangle bounds = c.getBounds();

		double w = bounds.width - insets.right - insets.left;
		double h = bounds.height - insets.top - insets.bottom;

		double arc = h * 0.4;

		double x = insets.left;
		double y = insets.top;

		path.moveTo(x, y);
		path.quadTo(x - arc, y, x - arc, y + arc); /* top-left corner */
		path.lineTo(x - arc, (y + h) - arc);
		path.quadTo(x - arc, y + h, x, y + h); /* bottom-left corner */
		path.lineTo(x + w, y + h);
		path.quadTo(x + w + arc, y + h, x + w + arc, (y + h) - arc); /*
		 * bottom-right
		 * corner
		 */
		path.lineTo(x + w + arc, y + arc);
		path.quadTo(x + w + arc, y, x + w, y); /* top-right corner */
		path.lineTo(x, y);

		return path;
	}

}
