package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 */
public class OTreeCellEditorPainter extends AbstractRegionPainter {
	// package protected integers representing the available states that
	// this painter will paint. These are used when creating a new instance
	// of TreeCellEditorPainter to determine which region/state is being painted
	// by that instance.
	public static final int BACKGROUND_DISABLED = 1;
	public static final int BACKGROUND_ENABLED = 2;
	public static final int BACKGROUND_ENABLED_FOCUSED = 3;
	public static final int BACKGROUND_SELECTED = 4;

	// the following 4 variables are reused during the painting code of the
	// layers
	protected Path2D path = new Path2D.Float();

	// painter to fill the component
	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorFocused;

	// All Colors used for painting are stored here. Ideally, only those colors
	// being used
	// by a particular instance of TreeCellEditorPainter would be created. For
	// the moment at least,
	// however, all are created for each instance.
	protected final Color color1 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.017358616f, -0.11372548f, 0);
	protected final Color color2 = this.decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);

	// Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OTreeCellEditorPainter(int state, PaintContext ctx) {
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
		case BACKGROUND_ENABLED_FOCUSED:
			this.paintBackgroundEnabledAndFocused(g);
			break;

		}
	}

	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}

	@Override
	protected String getComponentKeyName() {
		return "\"Tree.cellEditor\"";
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

		// focused:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused].background");
		if (obj instanceof Paint) {
			this.backgroundColorFocused = (Paint) obj;
		} else {
			this.backgroundColorFocused = this.color2;
		}

	}

	protected void paintBackgroundEnabled(Graphics2D g) {
		this.path = this.decodePath1();
		g.setPaint(this.backgroundColorEnabled);
		g.fill(this.path);

	}

	protected void paintBackgroundEnabledAndFocused(Graphics2D g) {
		this.path = this.decodePath2();
		g.setPaint(this.backgroundColorFocused);
		g.fill(this.path);

	}

	protected Path2D decodePath1() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.0f));
		this.path.lineTo(this.decodeX(0.0f), this.decodeY(3.0f));
		this.path.lineTo(this.decodeX(3.0f), this.decodeY(3.0f));
		this.path.lineTo(this.decodeX(3.0f), this.decodeY(0.0f));
		this.path.lineTo(this.decodeX(0.2f), this.decodeY(0.0f));
		this.path.lineTo(this.decodeX(0.2f), this.decodeY(0.2f));
		this.path.lineTo(this.decodeX(2.8f), this.decodeY(0.2f));
		this.path.lineTo(this.decodeX(2.8f), this.decodeY(2.8f));
		this.path.lineTo(this.decodeX(0.2f), this.decodeY(2.8f));
		this.path.lineTo(this.decodeX(0.2f), this.decodeY(0.0f));
		this.path.lineTo(this.decodeX(0.0f), this.decodeY(0.0f));
		this.path.closePath();
		return this.path;
	}

	protected Path2D decodePath2() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.0f));
		this.path.lineTo(this.decodeX(0.0f), this.decodeY(3.0f));
		this.path.lineTo(this.decodeX(3.0f), this.decodeY(3.0f));
		this.path.lineTo(this.decodeX(3.0f), this.decodeY(0.0f));
		this.path.lineTo(this.decodeX(0.24000001f), this.decodeY(0.0f));
		this.path.lineTo(this.decodeX(0.24000001f), this.decodeY(0.24000001f));
		this.path.lineTo(this.decodeX(2.7600007f), this.decodeY(0.24000001f));
		this.path.lineTo(this.decodeX(2.7600007f), this.decodeY(2.7599998f));
		this.path.lineTo(this.decodeX(0.24000001f), this.decodeY(2.7599998f));
		this.path.lineTo(this.decodeX(0.24000001f), this.decodeY(0.0f));
		this.path.lineTo(this.decodeX(0.0f), this.decodeY(0.0f));
		this.path.closePath();
		return this.path;
	}

}
