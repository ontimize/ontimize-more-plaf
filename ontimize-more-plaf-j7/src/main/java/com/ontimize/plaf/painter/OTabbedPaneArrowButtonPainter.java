package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

public class OTabbedPaneArrowButtonPainter extends AbstractRegionPainter {

	public static final int FOREGROUND_ENABLED = 0;
	public static final int FOREGROUND_DISABLED = 1;
	public static final int FOREGROUND_PRESSED = 2;
	public static final int FOREGROUND_MOUSEOVER = 3;

	public static int arrowWidth = 4;
	public static int arrowHeight = 8;

	// the following 4 variables are reused during the painting code of the
	// layers
	protected Path2D path = new Path2D.Float();

	// All Colors used for painting are stored here. Ideally, only those colors
	// being used
	// by a particular instance of TabbedPaneTabbedPaneTabAreaPainter would be
	// created. For the moment at least,
	// however, all are created for each instance.
	protected final Color color1 = new Color(0, 0, 0, 255);
	protected final Color color2 = OntimizeLAFColorUtils.setAlpha(new Color(0, 0, 0), 0.2);
	protected final Color color3 = OntimizeLAFColorUtils.setAlpha(new Color(255, 255, 255), 0.5);
	protected final Color color4 = OntimizeLAFColorUtils.setAlpha(new Color(255, 255, 255), 0.5);
	protected final Color color5 = new Color(0, 0, 0, 125);

	protected Paint foregroundColorDisabled;
	protected Paint foregroundColorEnabled;
	protected Paint foregroundColorPressed;
	protected Paint foregroundColorMouseOver;

	protected Color foregroundShadowColor = this.color5;

	// Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	/**
	 * Creates a new ArrowButtonPainter object.
	 *
	 * @param state
	 *            the control state.
	 */
	public OTabbedPaneArrowButtonPainter(int state, PaintContext ctx) {
		super(state, ctx);
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		// populate componentColors array with colors calculated in
		// getExtendedCacheKeys call
		this.componentColors = extendedCacheKeys;

		switch (this.state) {

		case FOREGROUND_DISABLED:
			this.paintForegroundDisabled(g, width, height);
			break;

		case FOREGROUND_ENABLED:
			this.paintForegroundEnabled(g, width, height);
			break;

		case FOREGROUND_PRESSED:
			this.paintForegroundPressed(g, width, height);
			break;

		case FOREGROUND_MOUSEOVER:
			this.paintForegroundMouseOver(g, width, height);
			break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}

	@Override
	protected String getComponentKeyName() {
		return "TabbedPane:TabbedPaneTabArea:\"TabbedPaneTabArea.button\"";
	}

	/**
	 * Get configuration properties to be used in this painter (such as:
	 * *BorderPainter and *BgPainter). As usual, it is checked the
	 * OLAFCustomConfig.properties, and then in OLAFDefaultConfig.properties.
	 *
	 * Moreover, if there are not values for that properties in both, the
	 * default Nimbus configuration values are set, due to, those properties are
	 * needed to paint and used by the Ontimize L&F, so, there are not Nimbus
	 * values for that, so, default values are always configured based on Nimbus
	 * values.
	 *
	 */
	@Override
	protected void init() {

		// enable:
		Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].foreground");
		if (obj instanceof Paint) {
			this.foregroundColorEnabled = (Paint) obj;
		} else {
			this.foregroundColorEnabled = this.color2;
		}

		// disable:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].foreground");
		if (obj instanceof Paint) {
			this.foregroundColorDisabled = (Paint) obj;
		} else {
			this.foregroundColorDisabled = this.color1;
		}

		// pressed:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Pressed].foreground");
		if (obj instanceof Paint) {
			this.foregroundColorPressed = (Paint) obj;
		} else {
			this.foregroundColorPressed = this.color3;
		}

		// mouseover:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[MouseOver].foreground");
		if (obj instanceof Paint) {
			this.foregroundColorMouseOver = (Paint) obj;
		} else {
			this.foregroundColorMouseOver = this.color4;
		}

	}

	/**
	 * Paint the arrow in disabled state.
	 *
	 * @param g
	 *            the Graphics2D context to paint with.
	 * @param width
	 *            the width.
	 * @param height
	 *            the height.
	 */
	protected void paintForegroundDisabled(Graphics2D g, int width, int height) {
		Shape s = this.decodeArrowPath(width, height);

		g.setPaint(this.foregroundColorDisabled);
		g.fill(s);

	}

	/**
	 * Paint the arrow in enabled state.
	 *
	 * @param g
	 *            the Graphics2D context to paint with.
	 * @param width
	 *            the width.
	 * @param height
	 *            the height.
	 */
	protected void paintForegroundEnabled(Graphics2D g, int width, int height) {
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		Shape s = this.decodeArrowPath(width, height);
		g.setPaint(this.foregroundColorEnabled);
		g.fill(s);

		s = this.decodeArrowShadowPath(width, height);
		g.setPaint(this.foregroundShadowColor);
		g.draw(s);

		g.setRenderingHints(rh);

	}

	/**
	 * Paint the arrow in pressed state.
	 *
	 * @param g
	 *            the Graphics2D context to paint with.
	 * @param width
	 *            the width.
	 * @param height
	 *            the height.
	 */
	protected void paintForegroundPressed(Graphics2D g, int width, int height) {
		Shape s = this.decodeArrowPath(width, height);
		g.setPaint(this.foregroundColorPressed);
		g.fill(s);

		s = this.decodeArrowShadowPath(width, height);
		g.setPaint(this.foregroundShadowColor);
		g.draw(s);

	}

	/**
	 * Paint the arrow in mouseover state.
	 *
	 * @param g
	 *            the Graphics2D context to paint with.
	 * @param width
	 *            the width.
	 * @param height
	 *            the height.
	 */
	protected void paintForegroundMouseOver(Graphics2D g, int width, int height) {

		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		Shape s = this.decodeArrowPath(width, height);
		g.setPaint(this.foregroundColorMouseOver);
		g.fill(s);

		s = this.decodeArrowShadowPath(width, height);
		g.setPaint(this.foregroundShadowColor);
		g.draw(s);

		g.setRenderingHints(rh);

	}

	/**
	 * Create the arrow path.
	 *
	 * @param width
	 *            the width.
	 * @param height
	 *            the height.
	 *
	 * @return the arrow path.
	 */
	protected Shape decodeArrowPath(int width, int height) {
		double w = OTabbedPaneArrowButtonPainter.arrowWidth;
		double h = OTabbedPaneArrowButtonPainter.arrowHeight - 1;

		double x = (((this.decodeX(2.0f) - this.decodeX(1.0f)) / 2.0) - (w / 2.0)) + 0.5;
		double y = (((this.decodeY(2.0f) - this.decodeY(1.0f)) / 2.0) - (h / 2.0)) + 1.5;

		this.path.reset();
		this.path.moveTo(x, y + (h / 2.0));
		this.path.lineTo(x + w, y);
		this.path.lineTo(x + w, y + h);
		this.path.closePath();

		return this.path;
	}

	/**
	 * Create the arrow path.
	 *
	 * @param width
	 *            the width.
	 * @param height
	 *            the height.
	 *
	 * @return the arrow path.
	 */
	protected Shape decodeArrowShadowPath(int width, int height) {
		double w = OTabbedPaneArrowButtonPainter.arrowWidth;
		double h = OTabbedPaneArrowButtonPainter.arrowHeight;

		double x = ((this.decodeX(2.0f) - this.decodeX(1.0f)) / 2.0) - (w / 2.0);
		double y = (((this.decodeY(2.0f) - this.decodeY(1.0f)) / 2.0) - (h / 2.0)) + 2;

		this.path.reset();
		this.path.moveTo(x + 1, (y - 1) + (h / 2.0));
		this.path.lineTo(x + w, y);

		return this.path;
	}
}
