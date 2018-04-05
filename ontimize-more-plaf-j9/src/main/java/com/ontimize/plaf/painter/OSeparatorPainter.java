package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

/**
 * Painter for the background of the buttons in a scroll bar (to configure
 * border and foreground colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OSeparatorPainter extends AbstractRegionPainter {
	// package protected integers representing the available states that
	// this painter will paint. These are used when creating a new instance
	// of OSeparatorPainter to determine which region/state is being painted
	// by that instance.
	public static final int BACKGROUND_ENABLED = 1;

	// painter to fill the component:
	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorEnabledShadow;

	// the following 4 variables are reused during the painting code of the
	// layers
	protected Path2D path = new Path2D.Float();
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

	// All Colors used for painting are stored here. Ideally, only those colors
	// being used
	// by a particular instance of OSeparatorPainter would be created. For the
	// moment at least,
	// however, all are created for each instance.
	protected Color color1 = new ColorUIResource(OntimizeLAFColorUtils.setAlpha(Color.black, 0.2));
	protected Color color2 = new ColorUIResource(OntimizeLAFColorUtils.setAlpha(Color.white, 0.2));

	// Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OSeparatorPainter(int state, PaintContext ctx) {
		super(state, ctx);
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		// populate componentColors array with colors calculated in
		// getExtendedCacheKeys call
		this.componentColors = extendedCacheKeys;
		// generate this entire method. Each state/bg/fg/border combo that has
		// been painted gets its own KEY and paint method.
		switch (this.state) {
		case BACKGROUND_ENABLED:
			this.paintBackgroundEnabled(g, width, height);
			break;

		}
	}

	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}

	@Override
	protected String getComponentKeyName() {
		return "Separator";
	}

	/**
	 * Get configuration properties to be used in this painter (such as:
	 * *BorderPainter, *upperCornerPainter, *lowerCornerPainter and *BgPainter).
	 * As usual, it is checked the OLAFCustomConfig.properties, and then in
	 * OLAFDefaultConfig.properties.
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
		Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].background");
		if (obj instanceof Paint) {
			this.backgroundColorEnabled = (Paint) obj;
		} else {
			this.backgroundColorEnabled = this.color1;
		}

		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].backgroundShadow");
		if (obj instanceof Paint) {
			this.backgroundColorEnabledShadow = (Paint) obj;
		} else {
			this.backgroundColorEnabledShadow = this.color2;
		}

	}

	protected void paintBackgroundEnabled(Graphics2D g, int width, int height) {

		// Painting upper line...
		this.path.reset();
		this.path.moveTo(0, (height / 2.0) - 1);
		this.path.lineTo(width, (height / 2.0) - 1);
		g.setPaint(this.backgroundColorEnabled);
		g.draw(this.path);

		// Painting bottom line...
		this.path.reset();
		this.path.moveTo(0, height / 2.0);
		this.path.lineTo(width, height / 2.0);
		g.setPaint(this.backgroundColorEnabledShadow);
		g.draw(this.path);

	}

}