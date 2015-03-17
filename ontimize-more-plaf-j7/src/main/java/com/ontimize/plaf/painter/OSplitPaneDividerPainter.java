package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;

/**
 */
public class OSplitPaneDividerPainter extends AbstractRegionPainter {
	// package protected integers representing the available states that
	// this painter will paint. These are used when creating a new instance
	// of OSplitPaneDividerPainter to determine which region/state is being
	// painted
	// by that instance.
	public static final int BACKGROUND_ENABLED = 1;
	public static final int BACKGROUND_FOCUSED = 2;
	public static final int FOREGROUND_ENABLED = 3;
	public static final int FOREGROUND_ENABLED_VERTICAL = 4;

	public static int buttonWidth = 90;
	public static int buttonHeight = 5;

	// the following 4 variables are reused during the painting code of the
	// layers
	protected Path2D path = new Path2D.Float();
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

	// All Colors used for painting are stored here. Ideally, only those colors
	// being used
	// by a particular instance of OSplitPaneDividerPainter would be created.
	// For the moment at least,
	// however, all are created for each instance.
	protected final Color color1 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.017358616f, -0.11372548f, 0);
	protected final Color color3 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);
	protected final Color color5 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
	protected final Color color7 = this.decodeColor("nimbusBlueGrey", 0.0055555105f, -0.06970999f, 0.21568626f, 0);

	protected Paint backgroundColorFocused;
	protected Paint backgroundColorEnabled;

	protected Paint foregroundColorEnabled;
	protected Paint foregroundColorEnabledVertical;
	protected Paint foregroundBorderColorEnabled;
	protected Paint foregroundBorderColorEnabledVertical;

	// Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OSplitPaneDividerPainter(int state, PaintContext ctx) {
		super(state, ctx);
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		// populate componentColors array with colors calculated in
		// getExtendedCacheKeys call
		this.componentColors = extendedCacheKeys;

		switch (this.state) {
		case BACKGROUND_ENABLED:
			this.paintBackgroundEnabled(g);
			break;
		case BACKGROUND_FOCUSED:
			this.paintBackgroundFocused(g);
			break;
		case FOREGROUND_ENABLED:
			this.paintForegroundEnabled(g, width, height);
			break;
		case FOREGROUND_ENABLED_VERTICAL:
			this.paintForegroundEnabledAndVertical(g, width, height);
			break;

		}
	}

	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}

	@Override
	protected String getComponentKeyName() {
		return "SplitPane:SplitPaneDivider";
	}

	/**
	 * Get configuration properties to be used in this painter. As usual, it is
	 * checked the OLAFCustomConfig.properties, and then in
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

		// focused:
		Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused].background");
		if (obj instanceof Paint) {
			this.backgroundColorFocused = (Paint) obj;
		} else {
			this.backgroundColorFocused = this.color3;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].background");
		if (obj instanceof Paint) {
			this.backgroundColorEnabled = (Paint) obj;
		} else {
			this.backgroundColorEnabled = this.color1;
		}

		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].foreground");
		if (obj instanceof Paint) {
			this.foregroundColorEnabled = (Paint) obj;
		} else {
			this.foregroundColorEnabled = this.color5;
		}

		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled+Vertical].foreground");
		if (obj instanceof Paint) {
			this.foregroundColorEnabledVertical = (Paint) obj;
		} else {
			this.foregroundColorEnabledVertical = this.color5;
		}

		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].foregroundBorder");
		if (obj instanceof Paint) {
			this.foregroundBorderColorEnabled = (Paint) obj;
		} else {
			this.foregroundBorderColorEnabled = this.color7;
		}

		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled+Vertical].foregroundBorder");
		if (obj instanceof Paint) {
			this.foregroundBorderColorEnabledVertical = (Paint) obj;
		} else {
			this.foregroundBorderColorEnabledVertical = this.color7;
		}

	}

	protected void paintBackgroundEnabled(Graphics2D g) {
		this.rect = this.decodeRect1();
		g.setPaint(this.backgroundColorEnabled);
		g.fill(this.rect);

	}

	protected void paintBackgroundFocused(Graphics2D g) {
		this.rect = this.decodeRect1();
		g.setPaint(this.backgroundColorFocused);
		g.fill(this.rect);

	}

	protected void paintForegroundEnabled(Graphics2D g, int width, int height) {

		double x = this.decodeX(1.05f);
		double y = Math.round((height / 2.0) - (OSplitPaneDividerPainter.buttonHeight / 2.0)) - 1;

		Shape s = this.decodeForeground(x + 1, y + 1, OSplitPaneDividerPainter.buttonWidth, OSplitPaneDividerPainter.buttonHeight);
		g.setPaint(this.foregroundBorderColorEnabled);
		g.fill(s);

		s = this.decodeForeground(x, y, OSplitPaneDividerPainter.buttonWidth, OSplitPaneDividerPainter.buttonHeight);
		g.setPaint(this.foregroundColorEnabled);
		g.fill(s);

	}

	protected void paintForegroundEnabledAndVertical(Graphics2D g, int width, int height) {

		double y = this.decodeY(1.05f);
		double x = Math.round((width / 2.0) - (OSplitPaneDividerPainter.buttonHeight / 2.0)) - 1;

		Shape s = this.decodeForegroundVertical(x + 1, y + 1, OSplitPaneDividerPainter.buttonHeight, OSplitPaneDividerPainter.buttonWidth);
		g.setPaint(this.foregroundBorderColorEnabledVertical);
		g.fill(s);

		s = this.decodeForegroundVertical(x, y, OSplitPaneDividerPainter.buttonHeight, OSplitPaneDividerPainter.buttonWidth);
		g.setPaint(this.foregroundColorEnabledVertical);
		g.fill(s);

	}

	protected Rectangle2D decodeRect1() {
		this.rect.setRect(this.decodeX(1.0f), // x
				this.decodeY(0.0f), // y
				this.decodeX(2.0f) - this.decodeX(1.0f), // width
				this.decodeY(3.0f) - this.decodeY(0.0f)); // height
		return this.rect;
	}

	protected Shape decodeForeground(double x, double y, double w, double h) {
		this.path.reset();

		this.path.moveTo(x, y);
		Shape s = ShapeFactory.getInstace().createSemiCircle(x, y, h, ShapeFactory.ANTICLOCKWISE, true);
		this.path.append(s, true);
		this.path.lineTo(x + w, y + h);
		s = ShapeFactory.getInstace().createSemiCircle(x + w, y + h, -h, ShapeFactory.ANTICLOCKWISE, true);
		this.path.append(s, true);
		this.path.lineTo(x, y);
		this.path.closePath();

		return this.path;
	}

	protected Shape decodeForegroundVertical(double x, double y, double w, double h) {
		this.path.reset();

		this.path.moveTo(x, y);
		Shape s = ShapeFactory.getInstace().createSemiCircleVertical(x, y, w, ShapeFactory.CLOCKWISE, true);
		this.path.append(s, true);
		this.path.lineTo(x + w, y + h);
		s = ShapeFactory.getInstace().createSemiCircleVertical(x + w, y + h, -w, ShapeFactory.CLOCKWISE, true);
		this.path.append(s, true);
		this.path.lineTo(x, y);
		this.path.closePath();

		return this.path;
	}

}
