package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.ui.OTabbedPaneUI;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

public class OTabbedPanePainter extends AbstractRegionPainter {
	// package protected integers representing the available states that
	// this painter will paint. These are used when creating a new instance
	// of TabbedPanePainter to determine which region/state is being painted
	// by that instance.
	static final int BACKGROUND_DISABLED = 1;
	static final int BACKGROUND_ENABLED = 2;
	static final int BACKGROUND_FOCUSED = 3;
	static final int BACKGROUND_MOUSEOVER = 4;
	static final int BACKGROUND_PRESSED = 5;

	public static final int BORDER_ENABLED = 6;
	public static final int BORDER_DISABLED = 7;

	// the following 4 variables are reused during the painting code of the
	// layers
	protected Path2D path = new Path2D.Float();

	// All Colors used for painting are stored here. Ideally, only those colors
	// being used
	// by a particular instance of TabbedPaneTabbedPaneTabAreaPainter would be
	// created. For the moment at least,
	// however, all are created for each instance.
	protected final Color color1 = new Color(0, 0, 0, 255);
	protected final Color color4 = new Color(214, 217, 223, 255);

	protected Paint borderColorDisabled;
	protected Paint borderColorEnabled;

	public static int borderWidth = 4;
	public static int borderPadding = 3;
	public static double arcRadius = 6.0;
	public static double maxAlphaBorderColor = 0.2;
	public static double minAlphaBorderColor = 0.06;

	// Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OTabbedPanePainter(int state, PaintContext ctx) {
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
		case BORDER_ENABLED:
			this.paintBorderEnabled(g, c, width, height);
			break;
		case BORDER_DISABLED:
			this.paintBorderDisabled(g, c, width, height);
			break;
		}
	}

	@Override
	protected String getComponentKeyName() {
		return "TabbedPane";
	}

	/**
	 * Get configuration properties to be used in this painter
	 *
	 */
	@Override
	protected void init() {

		// disable:
		Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].border");
		if (obj instanceof Paint) {
			this.borderColorDisabled = (Paint) obj;
		} else {
			this.borderColorDisabled = this.color4;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].border");
		if (obj instanceof Paint) {
			this.borderColorEnabled = (Paint) obj;
		} else {
			this.borderColorEnabled = this.color1;
		}

	}

	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}

	protected void paintBorderEnabled(Graphics2D g, JComponent c, int width, int height) {
		this.paintBorder(g, c, width, height, this.borderColorEnabled);
	}

	protected void paintBorderDisabled(Graphics2D g, JComponent c, int width, int height) {
		this.paintBorder(g, c, width, height, this.borderColorDisabled);
	}

	protected void paintBorder(Graphics2D g, JComponent c, int width, int height, Paint color) {
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Shape oldClip = g.getClip();

		JTabbedPane tabPane = (JTabbedPane) c;
		if ((tabPane.getTabCount() > 0) && (tabPane.getUI() instanceof OTabbedPaneUI)) {
			int numBorders = tabPane.getInsets().left;
			if (numBorders < OTabbedPanePainter.borderWidth) {
				numBorders = OTabbedPanePainter.borderWidth + OTabbedPanePainter.borderPadding;
			}

			int placement = ((JTabbedPane) c).getTabPlacement();
			if (((OTabbedPaneUI) tabPane.getUI()).getMaxTabBounds(tabPane) == null) {
				return;
			}
			int tabHeight = ((OTabbedPaneUI) tabPane.getUI()).getMaxTabBounds(tabPane).height;
			int tabWidth = ((OTabbedPaneUI) tabPane.getUI()).getMaxTabBounds(tabPane).width;
			Insets tabPinsets = tabPane.getInsets();

			double xx = 0.0;
			double yy = 0.0;
			double ww = 0.0;
			double hh = 0.0;

			double totalBorderWidth = OTabbedPanePainter.borderPadding + OTabbedPanePainter.borderWidth;

			if (SwingConstants.TOP == placement) {
				xx = this.decodeX(1.0f) - totalBorderWidth;
				yy = this.decodeY(1.0f) + (tabHeight / 2.0);
				ww = this.decodeX(3.0f) - 1 - (tabPinsets.left - totalBorderWidth) - (tabPinsets.right - totalBorderWidth);
				hh = this.decodeY(3.0f) - yy - 1 - (tabPinsets.bottom - totalBorderWidth);
			} else if (SwingConstants.LEFT == placement) {
				xx = this.decodeY(1.0f) - totalBorderWidth;
				yy = this.decodeX(1.0f) + (tabWidth / 2.0);
				hh = (this.decodeX(3.0f) - 1 - yy - c.getInsets().right) + totalBorderWidth;
				ww = this.decodeY(3.0f) - 1 - (tabPinsets.top - totalBorderWidth) - (tabPinsets.bottom - totalBorderWidth);
			} else if (SwingConstants.RIGHT == placement) {
				xx = this.decodeY(1.0f) - totalBorderWidth;
				yy = tabPinsets.right + (tabWidth / 2.0);
				hh = (this.decodeX(3.0f) - 1 - yy - c.getInsets().left) + totalBorderWidth;
				ww = this.decodeY(3.0f) - 1 - (tabPinsets.top - totalBorderWidth) - (tabPinsets.bottom - totalBorderWidth);
			} else if (SwingConstants.BOTTOM == placement) {
				xx = this.decodeX(1.0f) - totalBorderWidth;
				yy = tabPinsets.bottom + 1 + (tabHeight / 2.0);
				ww = this.decodeX(3.0f) - 1 - (tabPinsets.left - totalBorderWidth) - (tabPinsets.right - totalBorderWidth);
				hh = this.decodeY(3.0f) - 1 - yy - (tabPinsets.top - totalBorderWidth);
			}

			// Note:
			// The border width is 4 px. If insets is greater than 4, outsider
			// pixels are not painted.
			int indexColor = 0;
			double arcRadius_copy = OTabbedPanePainter.arcRadius;
			for (int i = 0; i < OTabbedPanePainter.borderWidth; i++) {
				double xxx = xx + i;
				double yyy = yy + i;
				double www = ww - (2 * i);
				double hhh = hh - (2 * i);
				Shape s = this.decodeBorderPath(xxx, yyy, www, hhh, arcRadius_copy--);
				g.setPaint(OntimizeLAFColorUtils.setAlpha((Color) color, OTabbedPanePainter.maxAlphaBorderColor
						- (((OTabbedPanePainter.maxAlphaBorderColor - OTabbedPanePainter.minAlphaBorderColor) / OTabbedPanePainter.borderWidth) * indexColor)));
				g.draw(s);
				indexColor++;
			}

			if (this.getBackgroundColor(c) != null) {
				this.paintInteriorBorderFillers(g, xx, yy, ww, hh, placement, this.getBackgroundColor(c));
			}

		}

		g.setClip(oldClip);
		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
	}

	protected void paintInteriorBorderFillers(Graphics2D g, double x, double y, double w, double h, int placement, Color color) {
		g.setPaint(color);
		double totalBorder = OTabbedPanePainter.borderWidth + OTabbedPanePainter.borderPadding;
		double arcRadius = OTabbedPanePainter.arcRadius - 2;

		if (SwingConstants.BOTTOM == placement) {
			y--;
		} else if (SwingConstants.RIGHT == placement) {
			y--;
		} else if (SwingConstants.LEFT == placement) {
			y--;
			h++;
		}

		Shape leftFiller = ShapeFactory.getInstace().createSemiRoundRect(x + OTabbedPanePainter.borderWidth, y + OTabbedPanePainter.borderWidth, OTabbedPanePainter.borderPadding,
				h - totalBorder, arcRadius, arcRadius, ShapeFactory.LEFT_ORIENTATION);
		g.fill(leftFiller);

		Shape rightFiller = ShapeFactory.getInstace().createSemiRoundRect(((w + x) - OTabbedPanePainter.borderPadding - OTabbedPanePainter.borderWidth) + 1,
				y + OTabbedPanePainter.borderWidth, OTabbedPanePainter.borderPadding, h - totalBorder, arcRadius, arcRadius, ShapeFactory.RIGHT_ORIENTATION);
		g.fill(rightFiller);

		Shape bottomFiller = ShapeFactory.getInstace().createRectangle(x + totalBorder, ((h + y) - totalBorder) + 1,
				(w + 1) - (2 * OTabbedPanePainter.borderWidth) - (2 * OTabbedPanePainter.borderPadding), OTabbedPanePainter.borderPadding);
		g.fill(bottomFiller);
	}

	protected Color getBackgroundColor(JComponent c) {
		if (c instanceof JTabbedPane) {
			JTabbedPane tabPane = (JTabbedPane) c;
			Component comp = tabPane.getSelectedComponent();
			if ((comp != null) && comp.isOpaque()) {
				return comp.getBackground();
			}
			if (c.isOpaque()) {
				if (c.getBackground() != null) {
					return c.getBackground();
				}
				// By default return LookAndFeel configured color.
				return this.color4;
			}
		}
		return null;
	}

	protected Shape decodeBorderPath(double x, double y, double w, double h, double arcRadius) {

		this.path.reset();

		Shape s = ShapeFactory.getInstace().createRoundCorner(x, y, arcRadius, arcRadius, ShapeFactory.TOP_LEFT_CORNER, true);
		this.path.append(s, true);
		this.path.lineTo(x, (y + h) - arcRadius);
		s = ShapeFactory.getInstace().createRoundCorner(x, y + h, arcRadius, arcRadius, ShapeFactory.BOTTOM_LEFT_CORNER, true);
		this.path.append(s, true);
		this.path.lineTo((x + w) - arcRadius, y + h);
		s = ShapeFactory.getInstace().createRoundCorner(x + w, y + h, arcRadius, arcRadius, ShapeFactory.BOTTOM_RIGHT_CORNER, true);
		this.path.append(s, true);
		this.path.lineTo(x + w, y + arcRadius);
		s = ShapeFactory.getInstace().createRoundCorner(x + w, y, arcRadius, arcRadius, ShapeFactory.TOP_RIGHT_CORNER, true);
		this.path.append(s, false);

		return this.path;
	}

}
