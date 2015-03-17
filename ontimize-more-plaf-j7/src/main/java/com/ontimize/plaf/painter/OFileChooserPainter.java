package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 */
public class OFileChooserPainter extends AbstractRegionPainter {
	public static final int BACKGROUND_ENABLED = 2;

	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
	protected Path2D path = new Path2D.Float();

	// painter to fill the component
	protected Paint backgroundColorEnabled;

	// All Colors used for painting are stored here. Ideally, only those colors
	// being used
	// by a particular instance of TreeCellEditorPainter would be created. For
	// the moment at least,
	// however, all are created for each instance.
	protected Color color1 = this.decodeColor("control", 0.0f, 0.0f, 0.0f, 0);

	// Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OFileChooserPainter(int state, PaintContext ctx) {
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
			this.paintBackgroundEnabled(g);
			break;

		}
	}

	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}

	@Override
	protected String getComponentKeyName() {
		return "FileChooser";
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

		Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].background");
		if (obj instanceof Paint) {
			this.backgroundColorEnabled = (Paint) obj;
		} else {
			this.backgroundColorEnabled = this.color1;
		}
	}

	protected void paintBackgroundEnabled(Graphics2D g) {
		this.rect = this.decodeRect1();
		g.setPaint(this.backgroundColorEnabled);
		g.fill(this.rect);
	}

	protected Rectangle2D decodeRect1() {
		this.rect.setRect(this.decodeX(1.0f), // x
				this.decodeY(1.0f), // y
				this.decodeX(2.0f) - this.decodeX(1.0f), // width
				this.decodeY(2.0f) - this.decodeY(1.0f)); // height
		return this.rect;
	}

}
