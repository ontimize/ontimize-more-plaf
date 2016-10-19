package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.painter.util.ShapeFactory;

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
public class OTextAreaPainter extends AbstractRegionPainter {
	// package protected integers representing the available states that
	// this painter will paint. These are used when creating a new instance
	// of TextAreaPainter to determine which region/state is being painted
	// by that instance.
	public static final int BACKGROUND_DISABLED = 1;
	public static final int BACKGROUND_ENABLED = 2;
	public static final int BACKGROUND_DISABLED_NOTINSCROLLPANE = 3;
	public static final int BACKGROUND_ENABLED_NOTINSCROLLPANE = 4;
	public static final int BACKGROUND_FOCUSED = 5;
	public static final int BACKGROUND_REQUIRED = 222;
	public static final int BACKGROUND_DISABLED_REQUIRED = 223;
	public static final int BACKGROUND_FOCUSED_REQUIRED = 224;
	public static final int BACKGROUND_REQUIRED_NOTINSCROLLPANE = 333;
	public static final int BACKGROUND_FOCUSED_NOTINSCROLLPANE = 336;
	public static final int BACKGROUND_DISABLED_REQUIRED_NOTINSCROLLPANE = 334;
	public static final int BACKGROUND_FOCUSED_REQUIRED_NOTINSCROLLPANE = 335;
	public static final int BORDER_DISABLED = 6;
	public static final int BORDER_FOCUSED = 7;
	public static final int BORDER_ENABLED = 8;
	public static final int BORDER_REQUIRED = 666;
	public static final int BORDER_FOCUSED_REQUIRED = 667;
	public static final int BORDER_DISABLED_REQUIRED = 668;
	public static final int BORDER_DISABLED_NOTINSCROLLPANE = 9;
	public static final int BORDER_FOCUSED_NOTINSCROLLPANE = 10;
	public static final int BORDER_ENABLED_NOTINSCROLLPANE = 11;
	public static final int BORDER_REQUIRED_NOTINSCROLLPANE = 777;
	public static final int BORDER_FOCUSED_REQUIRED_NOTINSCROLLPANE = 778;
	public static final int BORDER_DISABLED_REQUIRED_NOTINSCROLLPANE = 779;

	/**
	 * The number of pixels that compounds the border width of the component.
	 */
	public static int numBorders = 4;

	// the following 4 variables are reused during the painting code of the
	// layers
	protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

	// painters to fill the component
	protected Paint backgroundColorDisabled;
	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorFocused;
	protected Paint backgroundColorRequired;
	protected Paint backgroundColorFocusedRequired;
	protected Paint backgroundColorDisabledRequired;

	// arrays to round the component (several rounded borders with degradation):
	protected Paint[] degradatedBorderColorEnabled;
	protected Paint[] degradatedBorderColorDisabled;
	protected Paint[] degradatedBorderColorDisabledRequired;
	protected Paint[] degradatedBorderColorFocused;
	protected Paint[] degradatedBorderColorFocusedRequired;
	protected Paint[] degradatedBorderColorRequired;

	// All Colors used for painting are stored here. Ideally, only those colors
	// being used
	// by a particular instance of TextAreaPainter would be created. For the
	// moment at least,
	// however, all are created for each instance.
	protected Color color1 = this.decodeColor("nimbusBlueGrey", -0.015872955f, -0.07995863f, 0.15294117f, 0);
	protected Color color2 = this.decodeColor("nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
	protected Color color3 = this.decodeColor("nimbusBlueGrey", -0.006944418f, -0.07187897f, 0.06666666f, 0);
	protected Color color4 = this.decodeColor("nimbusBlueGrey", 0.007936537f, -0.07826825f, 0.10588235f, 0);
	protected Color color7 = this.decodeColor("nimbusBlueGrey", -0.027777791f, -0.0965403f, -0.18431371f, 0);
	protected Color color8 = this.decodeColor("nimbusBlueGrey", 0.055555582f, -0.1048766f, -0.05098039f, 0);
	protected Color color9 = this.decodeColor("nimbusLightBackground", 0.6666667f, 0.004901961f, -0.19999999f, 0);

	// Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	protected double radius;

	protected java.awt.Image paddLock = null;

	public OTextAreaPainter(int state, PaintContext ctx) {
		super(state, ctx);

		if (AbstractOTextFieldPainter.imgUrl != null) {
			URL url = this.getClass().getClassLoader().getResource(AbstractOTextFieldPainter.imgUrl);
			this.paddLock = Toolkit.getDefaultToolkit().getImage(url);
		}
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		// populate componentColors array with colors calculated in
		// getExtendedCacheKeys call
		this.componentColors = extendedCacheKeys;

		Insets insets = c.getInsets();
		int x = insets.left;
		int y = insets.top;
		int cwidth = width - insets.left - insets.right;
		int cheight = height - insets.top - insets.bottom;

		// if(c.isOpaque()){
		switch (this.state) {
		case BACKGROUND_DISABLED:
			this.paintBackgroundDisabled(g, c, x, y, cwidth, cheight);
			break;
		case BACKGROUND_DISABLED_REQUIRED:
			this.paintBackgroundDisabledRequired(g, c, x, y, cwidth, cheight);
			break;
		case BACKGROUND_ENABLED:
			this.paintBackgroundEnabled(g, c, x, y, cwidth, cheight);
			break;
		case BACKGROUND_DISABLED_NOTINSCROLLPANE:
			this.paintBackgroundDisabledAndNotInScrollPane(g, c, x, y, cwidth, cheight);
			break;
		case BACKGROUND_DISABLED_REQUIRED_NOTINSCROLLPANE:
			this.paintBackgroundDisabledRequiredAndNotInScrollPane(g, c, x, y, cwidth, cheight);
			break;
		case BACKGROUND_ENABLED_NOTINSCROLLPANE:
			this.paintBackgroundEnabledAndNotInScrollPane(g, c, x, y, cwidth, cheight);
			break;
		case BACKGROUND_FOCUSED:
			this.paintBackgroundFocused(g, c, x, y, cwidth, cheight);
			break;
		case BACKGROUND_FOCUSED_REQUIRED:
			this.paintBackgroundFocusedRequired(g, c, x, y, cwidth, cheight);
			break;
		case BACKGROUND_REQUIRED:
			this.paintBackgroundRequired(g, c, x, y, cwidth, cheight);
			break;
		case BACKGROUND_REQUIRED_NOTINSCROLLPANE:
			this.paintBackgroundRequiredAndNotInScrollPane(g, c, x, y, cwidth, cheight);
			break;
		case BACKGROUND_FOCUSED_NOTINSCROLLPANE:
			this.paintBackgroundFocusedAndNotInScrollPane(g, c, x, y, cwidth, cheight);
			break;
		case BACKGROUND_FOCUSED_REQUIRED_NOTINSCROLLPANE:
			this.paintBackgroundFocusedRequiredAndNotInScrollPane(g, c, x, y, cwidth, cheight);
			break;
		}
		// }
		switch (this.state) {
		case BORDER_DISABLED:
			this.paintBorderDisabledInScrollPane(g, c, 0, 0);
			break;
		case BORDER_DISABLED_REQUIRED:
			this.paintBorderDisabledRequiredInScrollPane(g, c, 0, 0);
			break;
		case BORDER_FOCUSED:
			this.paintBorderFocusedInScrollPane(g, c, 0, 0);
			break;
		case BORDER_ENABLED:
			this.paintBorderEnabledInScrollPane(g, c, 0, 0);
			break;
		case BORDER_REQUIRED:
			this.paintBorderRequiredInScrollPane(g, c, 0, 0);
			break;
		case BORDER_FOCUSED_REQUIRED:
			this.paintBorderFocusedRequiredInScrollPane(g, c, 0, 0);
			break;
		case BORDER_DISABLED_NOTINSCROLLPANE:
			this.paintBorderDisabledAndNotInScrollPane(g, c, 0, 0, width, height);
			break;
		case BORDER_DISABLED_REQUIRED_NOTINSCROLLPANE:
			this.paintBorderDisabledRequiredAndNotInScrollPane(g, c, 0, 0, width, height);
			break;
		case BORDER_FOCUSED_NOTINSCROLLPANE:
			this.paintBorderFocusedAndNotInScrollPane(g, c, 0, 0, width, height);
			break;
		case BORDER_ENABLED_NOTINSCROLLPANE:
			this.paintBorderEnabledAndNotInScrollPane(g, c, 0, 0, width, height);
			break;
		case BORDER_REQUIRED_NOTINSCROLLPANE:
			this.paintBorderRequiredAndNotInScrollPane(g, c, 0, 0, width, height);
			break;
		case BORDER_FOCUSED_REQUIRED_NOTINSCROLLPANE:
			this.paintBorderFocusedRequiredAndNotInScrollPane(g, c, 0, 0, width, height);
			break;
		}
	}

	@Override
	protected Object[] getExtendedCacheKeys(JComponent c) {
		Object[] extendedCacheKeys = null;
		switch (this.state) {
		case BACKGROUND_ENABLED:
			extendedCacheKeys = new Object[] { this.getComponentColor(c, "background", this.color2, 0.0f, 0.0f, 0) };
			break;
		case BACKGROUND_ENABLED_NOTINSCROLLPANE:
			extendedCacheKeys = new Object[] { this.getComponentColor(c, "background", this.color2, 0.0f, 0.0f, 0) };
			break;
		case BORDER_FOCUSED_NOTINSCROLLPANE:
			extendedCacheKeys = new Object[] { this.getComponentColor(c, "background", this.color9, 0.004901961f, -0.19999999f, 0),
					this.getComponentColor(c, "background", this.color2, 0.0f, 0.0f, 0) };
			break;
		case BORDER_ENABLED_NOTINSCROLLPANE:
			extendedCacheKeys = new Object[] { this.getComponentColor(c, "background", this.color9, 0.004901961f, -0.19999999f, 0),
					this.getComponentColor(c, "background", this.color2, 0.0f, 0.0f, 0) };
			break;
		}
		return extendedCacheKeys;
	}

	@Override
	protected String getComponentKeyName() {
		return "TextArea";
	}

	@Override
	protected void init() {

		Object oRadius = UIManager.getLookAndFeelDefaults().get("Application.radius");
		if (oRadius instanceof Double) {
			this.radius = ((Double) oRadius).doubleValue();
		} else {
			this.radius = OntimizeLookAndFeel.defaultRadius;
		}

		// disable:
		Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].background");
		if (obj instanceof Paint) {
			this.backgroundColorDisabled = (Paint) obj;
		} else {
			this.backgroundColorDisabled = this.color1;
		}

		// disable + required:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled+Required].background");
		if (obj instanceof Paint) {
			this.backgroundColorDisabledRequired = (Paint) obj;
		} else {
			this.backgroundColorDisabledRequired = this.color1;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].background");
		if (obj instanceof Paint) {
			this.backgroundColorEnabled = (Paint) obj;
		} else {
			this.backgroundColorEnabled = this.color2;
		}

		// focused:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused].background");
		if (obj instanceof Paint) {
			this.backgroundColorFocused = (Paint) obj;
		} else {
			this.backgroundColorFocused = this.color2;
		}

		// focused + required:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused+Required].background");
		if (obj instanceof Paint) {
			this.backgroundColorFocusedRequired = (Paint) obj;
		} else {
			this.backgroundColorFocusedRequired = this.color9;
		}

		// required:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Required].background");
		if (obj instanceof Paint) {
			this.backgroundColorRequired = (Paint) obj;
		} else {
			this.backgroundColorRequired = this.color9;
		}

		// BORDER COLORS
		// enable:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].border");
		if (obj instanceof Paint) {
			this.degradatedBorderColorEnabled = new Paint[] { (Paint) obj };
		} else if (obj instanceof Paint[]) {
			this.degradatedBorderColorEnabled = (Paint[]) obj;
		} else {
			this.degradatedBorderColorEnabled = new Color[] { this.color7, this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
		}

		// disable:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].border");
		if (obj instanceof Paint) {
			this.degradatedBorderColorDisabled = new Paint[] { (Paint) obj };
		} else if (obj instanceof Paint[]) {
			this.degradatedBorderColorDisabled = (Paint[]) obj;
		} else {
			this.degradatedBorderColorDisabled = new Color[] { this.color3, this.decodeColor(this.color3, this.color4, 0.5f), this.color4 };
		}

		// disable + required
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled+Required].border");
		if (obj instanceof Paint) {
			this.degradatedBorderColorDisabledRequired = new Paint[] { (Paint) obj };
		} else if (obj instanceof Paint[]) {
			this.degradatedBorderColorDisabledRequired = (Paint[]) obj;
		} else {
			this.degradatedBorderColorDisabledRequired = new Color[] { this.color7, this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
		}

		// Focused:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused].border");
		if (obj instanceof Paint) {
			this.degradatedBorderColorFocused = new Paint[] { (Paint) obj };
		} else if (obj instanceof Paint[]) {
			this.degradatedBorderColorFocused = (Paint[]) obj;
		} else {
			this.degradatedBorderColorFocused = new Color[] { this.color7, this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
		}

		// Focused + required
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused+Required].border");
		if (obj instanceof Paint) {
			this.degradatedBorderColorFocusedRequired = new Paint[] { (Paint) obj };
		} else if (obj instanceof Paint[]) {
			this.degradatedBorderColorFocusedRequired = (Paint[]) obj;
		} else {
			this.degradatedBorderColorFocusedRequired = new Color[] { this.color7, this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
		}

		// required:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Required].border");
		if (obj instanceof Paint) {
			this.degradatedBorderColorRequired = new Paint[] { (Paint) obj };
		} else if (obj instanceof Paint[]) {
			this.degradatedBorderColorRequired = (Paint[]) obj;
		} else {
			this.degradatedBorderColorRequired = new Color[] { this.color7, this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
		}

	}

	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}

	protected void paintBackgroundDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		this.drawBackground(g, c, x, y, width, height, this.backgroundColorDisabled);
	}

	protected void paintBackgroundDisabledRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		this.drawBackground(g, c, x, y, width, height, this.backgroundColorDisabledRequired);
	}

	protected void paintBackgroundEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		this.drawBackground(g, c, x, y, width, height, this.backgroundColorEnabled);
	}

	protected void paintBackgroundRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		this.drawBackground(g, c, x, y, width, height, this.backgroundColorRequired);
	}

	protected void paintBackgroundFocused(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		this.drawBackground(g, c, x, y, width, height, this.backgroundColorFocused);
	}

	protected void paintBackgroundFocusedRequired(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		this.drawBackground(g, c, x, y, width, height, this.backgroundColorFocusedRequired);
	}

	protected void paintBackgroundDisabledAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		this.drawBackground(g, c, x, y, width, height, this.backgroundColorDisabled);
	}

	protected void paintBackgroundDisabledRequiredAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		this.drawBackground(g, c, x, y, width, height, this.backgroundColorDisabledRequired);
	}

	protected void paintBackgroundEnabledAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		this.drawBackground(g, c, x, y, width, height, this.backgroundColorEnabled);
	}

	protected void paintBackgroundRequiredAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		this.drawBackground(g, c, x, y, width, height, this.backgroundColorRequired);
	}

	protected void paintBackgroundFocusedAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		this.drawBackground(g, c, x, y, width, height, this.backgroundColorFocused);
	}

	protected void paintBackgroundFocusedRequiredAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		this.drawBackground(g, c, x, y, width, height, this.backgroundColorFocusedRequired);
	}

	protected void paintBorderDisabledAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		if ((this.degradatedBorderColorDisabled != null) && (this.degradatedBorderColorDisabled.length > 0)) {
			this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorDisabled);
			g.drawImage(this.paddLock, -2, (int) (this.decodeY(2.0f) - 4), 10, 10, null);
		}
	}

	protected void paintBorderDisabledRequiredAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		if ((this.degradatedBorderColorDisabledRequired != null) && (this.degradatedBorderColorDisabledRequired.length > 0)) {
			this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorDisabledRequired);
			g.drawImage(this.paddLock, -2, (int) (this.decodeY(2.0f) - 4), 10, 10, null);
		}
	}

	protected void paintBorderDisabledInScrollPane(Graphics2D g, JComponent c, int x, int y) {
		if ((this.degradatedBorderColorDisabled != null) && (this.degradatedBorderColorDisabled.length > 0)) {
			this.drawDegradatedBordersInScrollPane(g, c, x, y, this.degradatedBorderColorDisabled);
			g.drawImage(this.paddLock, -2, (int) (this.decodeY(2.0f) - 4), 10, 10, null);
		}
	}

	protected void paintBorderDisabledRequiredInScrollPane(Graphics2D g, JComponent c, int x, int y) {
		if ((this.degradatedBorderColorDisabledRequired != null) && (this.degradatedBorderColorDisabledRequired.length > 0)) {
			this.drawDegradatedBordersInScrollPane(g, c, x, y, this.degradatedBorderColorDisabledRequired);
			g.drawImage(this.paddLock, -2, (int) (this.decodeY(2.0f) - 4), 10, 10, null);
		}
	}

	protected void paintBorderFocusedAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		if ((this.degradatedBorderColorFocused != null) && (this.degradatedBorderColorFocused.length > 0)) {
			this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorFocused);
		}
	}

	protected void paintBorderFocusedRequiredAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		if ((this.degradatedBorderColorFocused != null) && (this.degradatedBorderColorFocused.length > 0)) {
			this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorFocused);
		}
	}

	protected void paintBorderFocusedInScrollPane(Graphics2D g, JComponent c, int x, int y) {
		if ((this.degradatedBorderColorFocused != null) && (this.degradatedBorderColorFocused.length > 0)) {
			this.drawDegradatedBordersInScrollPane(g, c, x, y, this.degradatedBorderColorFocused);
		}
	}

	protected void paintBorderFocusedRequiredInScrollPane(Graphics2D g, JComponent c, int x, int y) {
		if ((this.degradatedBorderColorFocused != null) && (this.degradatedBorderColorFocused.length > 0)) {
			this.drawDegradatedBordersInScrollPane(g, c, x, y, this.degradatedBorderColorFocused);
		}
	}

	protected void paintBorderEnabledAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		if ((this.degradatedBorderColorEnabled != null) && (this.degradatedBorderColorEnabled.length > 0)) {
			this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorEnabled);
		}
	}

	protected void paintBorderRequiredAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		if ((this.degradatedBorderColorRequired != null) && (this.degradatedBorderColorRequired.length > 0)) {
			this.drawDegradatedBorders(g, c, x, y, width, height, this.degradatedBorderColorRequired);
		}
	}

	protected void paintBorderEnabledInScrollPane(Graphics2D g, JComponent c, int x, int y) {
		if ((this.degradatedBorderColorEnabled != null) && (this.degradatedBorderColorEnabled.length > 0)) {
			this.drawDegradatedBordersInScrollPane(g, c, x, y, this.degradatedBorderColorEnabled);
		}
	}

	protected void paintBorderRequiredInScrollPane(Graphics2D g, JComponent c, int x, int y) {
		if ((this.degradatedBorderColorRequired != null) && (this.degradatedBorderColorRequired.length > 0)) {
			this.drawDegradatedBordersInScrollPane(g, c, x, y, this.degradatedBorderColorRequired);
		}
	}

	protected void drawBackground(Graphics2D g, JComponent c, int x, int y, int width, int height, Paint color) {
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Shape s = ShapeFactory.getInstace().createRectangle(x, y, width, height);
		g.setPaint(this.getBackgroundColor(c, s, color));
		g.fill(s);

		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
	}

	protected Paint getBackgroundColor(JComponent c, Shape s, Paint defaultColor) {
		if (c != null) {
			if ((c.getBackground() instanceof UIResource) == false) {
				return c.getBackground();
			}
		}
		return defaultColor;
	}

	protected void drawDegradatedBorders(Graphics2D g, JComponent c, int x, int y, int width, int height, Paint[] colors) {
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// if(c.isOpaque()) {
		this.defineBorderFillers(g, c, x, y);
		// }

		double xx = this.getLeftX();
		double yy = this.decodeY(0.0f) + OTextAreaPainter.numBorders;
		double h = height - (2 * OTextAreaPainter.numBorders);
		double w = this.getRightX();

		for (int i = 1; i <= colors.length; i++) {
			double yyy = yy - i;
			double hh = (h + (2 * i)) - 1;
			Shape s = this.decodeBorderPath(xx, yyy, w, hh, i - 1);
			g.setPaint(colors[i - 1]);
			g.draw(s);
		}

		g.setPaint(previousPaint);
		g.setRenderingHints(rh);

	}

	protected void drawDegradatedBordersInScrollPane(Graphics2D g, JComponent c, int x, int y, Paint[] colors) {

		// if (c.isOpaque()) {
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		this.defineBorderFillersInScrollPane(g, c, x, y);

		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
		// }
	}

	protected void defineBorderFillers(Graphics2D g, JComponent c, int x, int y) {
		Paint previousPaint = g.getPaint();

		Rectangle bounds = c.getBounds();
		Insets insets = c.getInsets();

		// Drawing rounded corners...
		Shape s = ShapeFactory.getInstace().createRoundCorner(x + OTextAreaPainter.numBorders, y + OTextAreaPainter.numBorders, c.getInsets().left - OTextAreaPainter.numBorders,
				c.getInsets().top - OTextAreaPainter.numBorders, ShapeFactory.TOP_LEFT_CORNER, false);
		g.setPaint(this.getPaintForFiller(c, s));
		g.fill(s);

		s = ShapeFactory.getInstace().createRoundCorner(c.getBounds().width - OTextAreaPainter.numBorders, y + OTextAreaPainter.numBorders,
				c.getInsets().right - OTextAreaPainter.numBorders, c.getInsets().top - OTextAreaPainter.numBorders, ShapeFactory.TOP_RIGHT_CORNER, false);
		g.fill(s);

		s = ShapeFactory.getInstace().createRoundCorner(x + OTextAreaPainter.numBorders, c.getBounds().height - OTextAreaPainter.numBorders,
				c.getInsets().left - OTextAreaPainter.numBorders, c.getInsets().bottom - OTextAreaPainter.numBorders, ShapeFactory.BOTTOM_LEFT_CORNER, false);
		g.fill(s);

		s = ShapeFactory.getInstace().createRoundCorner(c.getBounds().width - OTextAreaPainter.numBorders, c.getBounds().height - OTextAreaPainter.numBorders,
				c.getInsets().right - OTextAreaPainter.numBorders, c.getInsets().bottom - OTextAreaPainter.numBorders, ShapeFactory.BOTTOM_RIGHT_CORNER, false);
		g.fill(s);

		// Drawing rectangle zones...
		// top
		s = ShapeFactory.getInstace().createRectangle(insets.left, y + OTextAreaPainter.numBorders, bounds.width - insets.left - insets.right,
				insets.top - OTextAreaPainter.numBorders);
		g.fill(s);
		// bottom
		s = ShapeFactory.getInstace().createRectangle(insets.left, (y + bounds.height) - insets.bottom, bounds.width - insets.left - insets.right,
				insets.bottom - OTextAreaPainter.numBorders);
		g.fill(s);
		// left
		s = ShapeFactory.getInstace().createRectangle(x + OTextAreaPainter.numBorders, insets.top, insets.right - OTextAreaPainter.numBorders - 1,
				bounds.height - insets.top - insets.bottom);
		g.fill(s);
		// right
		s = ShapeFactory.getInstace().createRectangle(((x + bounds.width) - insets.right) + 1, insets.top, insets.right - OTextAreaPainter.numBorders,
				bounds.height - insets.top - insets.bottom);
		g.fill(s);

		g.setPaint(previousPaint);
	}

	protected Paint getPaintForFiller(JComponent c, Shape s) {
		Paint filler = null;
		switch (this.state) {
		case BORDER_DISABLED:
			filler = this.getBackgroundColor(c, s, this.backgroundColorDisabled);
			break;
		case BORDER_DISABLED_REQUIRED:
			filler = this.getBackgroundColor(c, s, this.backgroundColorDisabledRequired);
			break;
		case BORDER_ENABLED:
			filler = this.getBackgroundColor(c, s, this.backgroundColorEnabled);
			break;
		case BORDER_FOCUSED:
			filler = this.getBackgroundColor(c, s, this.backgroundColorEnabled);
			break;
		case BORDER_REQUIRED:
			filler = this.getBackgroundColor(c, s, this.backgroundColorRequired);
			break;
		case BORDER_FOCUSED_REQUIRED:
			filler = this.getBackgroundColor(c, s, this.backgroundColorRequired);
			break;
		case BORDER_DISABLED_NOTINSCROLLPANE:
			filler = this.getBackgroundColor(c, s, this.backgroundColorDisabled);
			break;
		case BORDER_DISABLED_REQUIRED_NOTINSCROLLPANE:
			filler = this.getBackgroundColor(c, s, this.backgroundColorDisabledRequired);
			break;
		case BORDER_ENABLED_NOTINSCROLLPANE:
			filler = this.getBackgroundColor(c, s, this.backgroundColorEnabled);
			break;
		case BORDER_FOCUSED_NOTINSCROLLPANE:
			filler = this.getBackgroundColor(c, s, this.backgroundColorEnabled);
			break;
		case BORDER_REQUIRED_NOTINSCROLLPANE:
			filler = this.getBackgroundColor(c, s, this.backgroundColorRequired);
			break;
		case BORDER_FOCUSED_REQUIRED_NOTINSCROLLPANE:
			filler = this.getBackgroundColor(c, s, this.backgroundColorRequired);
			break;
		}
		return filler;
	}

	protected void defineBorderFillersInScrollPane(Graphics2D g, JComponent c, int x, int y) {
		Paint previousPaint = g.getPaint();

		Rectangle bounds = c.getBounds();
		Insets insets = c.getInsets();

		// Drawing corners...
		Shape s = this.createTopLeftCorner(x + c.getInsets().left, y, c.getInsets().left, c.getInsets().top);
		g.setPaint(this.getPaintForFiller(c, s));
		g.fill(s);

		// Checking scrollBars...
		JScrollBar vScrollBar = this.getScrollBar(c, false);
		JScrollBar hScrollBar = this.getScrollBar(c, true);

		// Top right corner...
		if ((vScrollBar != null) && vScrollBar.isVisible()) {
			s = ShapeFactory.getInstace().createRectangle(bounds.width - insets.right, y, insets.right, insets.top);
			g.fill(s);
		} else {
			s = this.createTopRightCorner(bounds.width - insets.right, y, insets.right, insets.top);
			g.fill(s);
		}

		// Bottom right corner...
		if (((vScrollBar != null) && vScrollBar.isVisible()) && ((hScrollBar != null) && hScrollBar.isVisible())) {
			s = ShapeFactory.getInstace().createRectangle(bounds.width - insets.right, bounds.height - insets.bottom, insets.right, insets.bottom);
			g.fill(s);
		} else if (!((vScrollBar != null) && vScrollBar.isVisible()) && ((hScrollBar != null) && hScrollBar.isVisible())) {
			s = ShapeFactory.getInstace().createRectangle(bounds.width - insets.right, bounds.height - insets.bottom, insets.right, insets.bottom);
			g.fill(s);
		} else if (((vScrollBar != null) && vScrollBar.isVisible()) && !((hScrollBar != null) && hScrollBar.isVisible())) {
			s = ShapeFactory.getInstace().createRectangle(bounds.width - insets.right, bounds.height - insets.bottom, insets.right, insets.bottom);
			g.fill(s);
		} else {
			s = this.createBottomRightCorner(bounds.width - insets.right, c.getBounds().height, c.getInsets().right, c.getInsets().bottom);
			g.fill(s);
		}

		// Bottom left corner...
		if ((hScrollBar != null) && hScrollBar.isVisible()) {
			s = ShapeFactory.getInstace().createRectangle(x, bounds.height - insets.bottom, insets.left, insets.bottom);
			g.fill(s);
		} else {
			s = this.createBottomLeftCorner(x + insets.left, bounds.height, insets.left, insets.bottom);
			g.fill(s);
		}

		// Drawing rectangle zones...
		// top
		s = ShapeFactory.getInstace().createRectangle(insets.left, y, bounds.width - insets.left - insets.right, insets.top);
		g.fill(s);
		// bottom
		if ((hScrollBar != null) && hScrollBar.isVisible()) {
			s = ShapeFactory.getInstace().createRectangle(insets.left, (y + bounds.height) - insets.bottom, bounds.width - insets.left - insets.right, insets.bottom);
		} else {
			s = ShapeFactory.getInstace().createRectangle(insets.left, (y + bounds.height) - insets.bottom, bounds.width - insets.left - insets.right, insets.bottom);
		}
		g.fill(s);
		// left
		s = ShapeFactory.getInstace().createRectangle(x, insets.top, insets.left, bounds.height - insets.top - insets.bottom);
		g.fill(s);
		// right
		s = ShapeFactory.getInstace().createRectangle((x + bounds.width) - insets.right, insets.top, insets.right, bounds.height - insets.top - insets.bottom);
		g.fill(s);

		g.setPaint(previousPaint);
	}

	protected JScrollBar getScrollBar(JComponent c, boolean horizontal) {
		Object o = c.getParent();
		if (o instanceof JViewport) {
			JViewport jvp = (JViewport) o;
			Object o2 = jvp.getParent();
			if (o2 instanceof JScrollPane) {
				JScrollPane jsp = (JScrollPane) o2;
				if (horizontal) {
					return jsp.getHorizontalScrollBar();
				} else {
					return jsp.getVerticalScrollBar();
				}
			}
		}
		return null;
	}

	protected Shape createTopLeftCorner(double x, double y, double w, double h) {
		double radius = this.getRadius();
		double x_arc = (x - w) + radius;
		if (x_arc > x) {
			x_arc = x;
			radius = w;
		}

		this.path.reset();
		this.path.moveTo(this.intValue(x), y);
		this.path.lineTo(x_arc, y);
		this.path.curveTo(x_arc - (radius / 2.0), y, x_arc - radius, y + (radius / 2.0), x_arc - radius, y + radius);
		this.path.lineTo(x_arc - radius, y + h);
		this.path.lineTo(x_arc, y + h);
		this.path.lineTo(x, y + h);
		this.path.closePath();
		return this.path;
	}

	protected Shape createTopRightCorner(double x, double y, double w, double h) {
		double radius = this.getRadius();
		double x_arc = this.decodeX(3.0f) - radius;
		if (x_arc < x) {
			x_arc = x;
			radius = this.decodeX(3.0f) - this.decodeX(2.0f);
		}

		this.path.reset();
		this.path.moveTo(this.intValue(x), y);
		this.path.lineTo(x_arc, y);
		this.path.curveTo(x_arc + (radius / 2.0), y, x_arc + radius, y + (radius / 2.0), x_arc + radius, y + radius);
		this.path.lineTo(x_arc + radius, y + h);
		this.path.lineTo(x_arc, y + h);
		this.path.lineTo(x, y + h);
		this.path.closePath();
		return this.path;
	}

	protected Shape createBottomRightCorner(double x, double y, double w, double h) {
		double radius = this.getRadius();
		double x_arc = this.decodeX(3.0f) - radius;
		if (x_arc < x) {
			x_arc = x;
			radius = this.decodeX(3.0f) - this.decodeX(2.0f);
		}

		this.path.reset();
		this.path.moveTo(this.intValue(x), y);
		this.path.lineTo(x_arc, y);
		this.path.curveTo(x_arc + (radius / 2.0), y, x_arc + radius, y - (radius / 2.0), x_arc + radius, y - radius);
		this.path.lineTo(x_arc + radius, y - h);
		this.path.lineTo(x_arc, y - h);
		this.path.lineTo(x, y - h);
		this.path.closePath();
		return this.path;
	}

	protected Shape createBottomLeftCorner(double x, double y, double w, double h) {
		double radius = this.getRadius();
		double x_arc = (x - w) + radius;
		if (x_arc > x) {
			x_arc = x;
			radius = w;
		}

		this.path.reset();
		this.path.moveTo(this.intValue(x), y);
		this.path.lineTo(x_arc, y);
		this.path.curveTo(x_arc - (radius / 2.0), y, x_arc - radius, y - (radius / 2.0), x_arc - radius, y - radius);
		this.path.lineTo(x_arc - radius, y - h);
		this.path.lineTo(x_arc, y - h);
		this.path.lineTo(x, y - h);
		this.path.closePath();
		return this.path;
	}

	protected Shape createRoundRect(double x, double y, double w, double h, Insets insets) {

		this.path.reset();

		// left top corner.
		this.path.moveTo(x + insets.left, y);
		this.path.curveTo(x + (insets.left / 2.0), y, x, y + (insets.top / 2.0), x, y + insets.top);

		// left bottom corner
		this.path.lineTo(x, (y + h) - insets.bottom);
		this.path.curveTo(x, (y + h) - (insets.bottom / 2.0), x + (insets.left / 2.0), y + h, x + insets.left, y + h);
		this.path.lineTo((x + w) - insets.right, y + h);

		// right bottom corner
		this.path.curveTo((x + w) - (insets.right / 2.0), y + h, x + w, (y + h) - (insets.bottom / 2.0), x + w, (y + h) - insets.bottom);

		// right top corner
		this.path.lineTo(x + w, y + insets.top);
		this.path.curveTo(x + w, y + (insets.top / 2.0), (x + w) - (insets.right / 2.0), y, (x + w) - insets.right, y);
		this.path.lineTo(x + insets.left, y);

		return this.path;
	}

	/**
	 * Decodes the border component path.
	 *
	 * @param x
	 *            The x coordinate where starts the semicircle of the left
	 * @param y
	 *            The y coordinate of the top.
	 * @param w
	 *            The x coordinate where starts the semicircle of the right.
	 * @param h
	 *            The height of the field.
	 * @param borderIndex
	 *            the index of the border.
	 * @return
	 */
	protected Shape decodeBorderPath(double x, double y, double w, double h, int borderIndex) {
		double radius = this.getRadius();
		double x_arc = this.decodeX(0.0f) + OTextAreaPainter.numBorders + radius;
		if (x_arc > x) {
			x_arc = x;
			radius = this.getMaximumRadius();
		}

		this.path.reset();
		this.path.moveTo(this.intValue(x), y);
		this.path.lineTo(x_arc, y);
		this.path.curveTo(x_arc - (radius / 2.0), y, x_arc - radius, y + (radius / 2.0), x_arc - radius - borderIndex, y + radius);
		this.path.lineTo(x_arc - radius - borderIndex, (y + h) - radius);
		this.path.curveTo(x_arc - radius - borderIndex, (y + h) - (radius / 2.0), x_arc - (radius / 2.0), y + h, x_arc, y + h);
		this.path.lineTo(this.intValue(x), y + h);

		this.path.lineTo(this.intValue(w), y + h);

		x_arc = this.decodeX(3.0f) - 1 - OTextAreaPainter.numBorders - radius;
		if (x_arc < w) {
			x_arc = w;
			radius = this.getMaximumRadius();
		}

		this.path.lineTo(x_arc, y + h);
		this.path.curveTo(x_arc + (radius / 2.0), y + h, x_arc + radius, (y + h) - (radius / 2.0), x_arc + radius + borderIndex, (y + h) - radius);
		this.path.lineTo(x_arc + radius + borderIndex, y + radius);
		this.path.curveTo(x_arc + radius + borderIndex, y + (radius / 2.0), x_arc + (radius / 2.0), y, x_arc, y);

		this.path.lineTo(this.intValue(x), y);
		this.path.closePath();

		return this.path;
	}

	/**
	 * This method returns, if it is configured by the user, the value for
	 * corner radius.
	 *
	 * @return
	 */
	protected double getRadius() {
		return this.radius;
	}

	/**
	 * This method returns the maximum radius available for the component. The
	 * maximum radius is calculated from the height of the component and it
	 * provokes that the field corners looks like a semicircle.
	 *
	 * @return
	 */
	protected double getMaximumRadius() {
		return (this.decodeY(3.0f) - 1 - (2 * OTextAreaPainter.numBorders)) / 2.0;
	}

	/**
	 * Returns the x coordinate where starts the semicircle of the left of the
	 * component.
	 *
	 * @return
	 */
	protected double getLeftX() {
		double d = this.decodeX(0.0f) + this.getMaximumRadius() + OTextAreaPainter.numBorders;
		d = Math.round(d);
		return d;
	}

	/**
	 * Returns the x coordinate where starts the semicircle of the right of the
	 * component.
	 *
	 * @return
	 */
	protected double getRightX() {
		double d = this.decodeX(3.0f) - this.getMaximumRadius() - OTextAreaPainter.numBorders;
		d = Math.round(d);
		return d;
	}

}
