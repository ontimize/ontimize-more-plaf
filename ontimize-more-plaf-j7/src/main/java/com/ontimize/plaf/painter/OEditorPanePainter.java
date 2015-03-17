package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;

public class OEditorPanePainter extends OTextAreaPainter {

	// package protected integers representing the available states that
	// this painter will paint. These are used when creating a new instance
	// of EditorPanePainter to determine which region/state is being painted
	// by that instance.
	public static final int BACKGROUND_DISABLED = 1;
	public static final int BACKGROUND_ENABLED = 2;
	public static final int BACKGROUND_SELECTED = 3;

	// the following 4 variables are reused during the painting code of the
	// layers
	protected Path2D path = new Path2D.Float();
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

	// All Colors used for painting are stored here. Ideally, only those colors
	// being used
	// by a particular instance of EditorPanePainter would be created. For the
	// moment at least,
	// however, all are created for each instance.
	protected Color color1 = this.decodeColor("nimbusBlueGrey", -0.015872955f, -0.07995863f, 0.15294117f, 0);
	protected Color color2 = this.decodeColor("nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);

	// Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OEditorPanePainter(int state, PaintContext ctx) {
		super(state, ctx);
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		super.doPaint(g, c, width, height, extendedCacheKeys);
	}

	@Override
	protected String getComponentKeyName() {
		return "EditorPane";
	}

	@Override
	protected void init() {

		// disable:
		Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].background");
		if (obj instanceof Paint) {
			this.backgroundColorDisabled = (Paint) obj;
		} else {
			this.backgroundColorDisabled = this.color1;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].background");
		if (obj instanceof Paint) {
			this.backgroundColorEnabled = (Paint) obj;
		} else {
			this.backgroundColorEnabled = this.color2;
		}

		// focused:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Selected].background");
		if (obj instanceof Paint) {
			this.backgroundColorFocused = (Paint) obj;
		} else {
			this.backgroundColorFocused = this.color2;
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

		// Focused:
		// disable:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused].border");
		if (obj instanceof Paint) {
			this.degradatedBorderColorFocused = new Paint[] { (Paint) obj };
		} else if (obj instanceof Paint[]) {
			this.degradatedBorderColorFocused = (Paint[]) obj;
		} else {
			this.degradatedBorderColorFocused = new Color[] { this.color7, this.decodeColor(this.color7, this.color8, 0.5f), this.color8 };
		}

	}

	@Override
	protected void drawDegradatedBordersInScrollPane(Graphics2D g, JComponent c, int x, int y, Paint[] colors) {
		super.drawDegradatedBordersInScrollPane(g, c, x, y, colors);
		this.paintBorderInScrollPane(g, c, x, y, colors[0]);
	}

	protected void paintBorderInScrollPane(Graphics2D g, JComponent c, int x, int y, Paint color) {
		Paint previousPaint = g.getPaint();

		Rectangle bounds = c.getBounds();
		Insets insets = c.getInsets();
		int numBorders = 0;

		// Drawing corners...
		g.setPaint(color);
		g.drawLine(x + numBorders, y + numBorders, x + numBorders, y + numBorders + c.getInsets().top);
		g.drawLine(x + numBorders, y + numBorders, x + numBorders + c.getInsets().left, y + numBorders);

		// Checking scrollBars...
		JScrollBar vScrollBar = this.getScrollBar(c, false);
		JScrollBar hScrollBar = this.getScrollBar(c, true);

		Shape s = null;
		// Top right corner...
		if ((vScrollBar != null) && vScrollBar.isVisible()) {
			g.setPaint(this.getPaintForFiller(c, s));
			g.drawLine(bounds.width - insets.right, y + numBorders, bounds.width - numBorders, y + numBorders);
			g.drawLine(bounds.width - numBorders, y + numBorders, bounds.width - numBorders, y + numBorders + c.getInsets().top);
		} else {
			g.setPaint(color);
			s = ShapeFactory.getInstace().createRoundCorner(c.getBounds().width - numBorders, y + numBorders, c.getInsets().right - numBorders - 1,
					(c.getInsets().top - numBorders) + 1, ShapeFactory.TOP_RIGHT_CORNER, true);
			g.draw(s);
		}

		// Bottom right corner...
		if (((vScrollBar != null) && vScrollBar.isVisible()) || ((hScrollBar != null) && hScrollBar.isVisible())) {
			g.setPaint(this.getPaintForFiller(c, s));
			g.drawLine(bounds.width - numBorders - c.getInsets().right, c.getBounds().height - numBorders, bounds.width - numBorders, c.getBounds().height - numBorders);
			g.drawLine(bounds.width - numBorders, c.getBounds().height - numBorders, bounds.width - numBorders, c.getBounds().height - numBorders - c.getInsets().bottom);
		} else {
			g.setPaint(color);
			s = ShapeFactory.getInstace().createRoundCorner(c.getBounds().width - numBorders, c.getBounds().height - numBorders, c.getInsets().right - numBorders - 1,
					c.getInsets().bottom - numBorders, ShapeFactory.BOTTOM_RIGHT_CORNER, true);
			g.fill(s);
		}

		// Bottom left corner...
		if ((hScrollBar != null) && hScrollBar.isVisible()) {
			g.setPaint(this.getPaintForFiller(c, s));
			g.drawLine(x + numBorders, c.getBounds().height - numBorders - insets.bottom, x + numBorders, c.getBounds().height - numBorders);
			g.drawLine(x + numBorders, c.getBounds().height - numBorders, x + numBorders + c.getInsets().left, c.getBounds().height - numBorders);
		} else {
			g.setPaint(color);
			s = ShapeFactory.getInstace().createRoundCorner(x + numBorders, c.getBounds().height - numBorders, c.getInsets().left - numBorders, c.getInsets().bottom - numBorders,
					ShapeFactory.BOTTOM_LEFT_CORNER, true);
			g.draw(s);
		}

		// Drawing rectangle zones...
		// top
		g.setPaint(color);
		g.drawLine(x + numBorders + insets.left, y + numBorders, bounds.width - insets.right, y + numBorders);
		// left
		g.drawLine(x + numBorders, y + numBorders + insets.top, x + numBorders, bounds.height - insets.bottom);
		// bottom
		if ((hScrollBar != null) && hScrollBar.isVisible()) {
			g.setPaint(this.getPaintForFiller(c, s));
		} else {
			g.setPaint(color);
		}
		g.drawLine((x + numBorders + insets.left) - 1, bounds.height - numBorders - 1, bounds.width - insets.right, bounds.height - numBorders - 1);

		// right
		if ((vScrollBar != null) && vScrollBar.isVisible()) {
			g.setPaint(this.getPaintForFiller(c, s));
		} else {
			g.setPaint(color);
		}
		g.drawLine(bounds.width - numBorders - 1, (y + numBorders + insets.top) - 1, bounds.width - numBorders - 1, bounds.height - insets.bottom);

		g.setPaint(previousPaint);
	}

	@Override
	protected void defineBorderFillersInScrollPane(Graphics2D g, JComponent c, int x, int y) {
		Paint previousPaint = g.getPaint();

		Rectangle bounds = c.getBounds();
		Insets insets = c.getInsets();
		int numBorders = 1;

		// Drawing corners...
		Shape s = ShapeFactory.getInstace().createRectangle(x + numBorders, y + numBorders, c.getInsets().left - numBorders - 1, c.getInsets().top - numBorders);
		g.setPaint(this.getPaintForFiller(c, s));
		g.fill(s);

		// Checking scrollBars...
		JScrollBar vScrollBar = this.getScrollBar(c, false);
		JScrollBar hScrollBar = this.getScrollBar(c, true);

		// Top right corner...
		if ((vScrollBar != null) && vScrollBar.isVisible()) {
			s = ShapeFactory.getInstace().createRectangle(bounds.width - insets.right, y + numBorders, c.getInsets().right - numBorders, c.getInsets().top - numBorders);
			g.fill(s);
		} else {
			s = ShapeFactory.getInstace().createRoundCorner(c.getBounds().width - numBorders, y + numBorders, c.getInsets().right - numBorders - 1, c.getInsets().top - numBorders,
					ShapeFactory.TOP_RIGHT_CORNER, false);
			g.fill(s);
		}

		// Bottom right corner...
		if (((vScrollBar != null) && vScrollBar.isVisible()) || ((hScrollBar != null) && hScrollBar.isVisible())) {
			s = ShapeFactory.getInstace().createRectangle(bounds.width - insets.right, bounds.height - numBorders - insets.bottom, insets.right - numBorders,
					c.getInsets().bottom - numBorders);
			g.fill(s);
		} else {
			s = ShapeFactory.getInstace().createRoundCorner(c.getBounds().width - numBorders, c.getBounds().height - numBorders, c.getInsets().right - numBorders - 1,
					c.getInsets().bottom - numBorders, ShapeFactory.BOTTOM_RIGHT_CORNER, false);
			g.fill(s);
		}

		// Bottom left corner...
		if ((hScrollBar != null) && hScrollBar.isVisible()) {
			s = ShapeFactory.getInstace().createRectangle(x + numBorders, c.getBounds().height - numBorders - insets.bottom, c.getInsets().left - numBorders,
					c.getInsets().bottom - numBorders);
			g.fill(s);
		} else {
			s = ShapeFactory.getInstace().createRoundCorner(x + numBorders, c.getBounds().height - numBorders, c.getInsets().left - numBorders - 1,
					c.getInsets().bottom - numBorders, ShapeFactory.BOTTOM_LEFT_CORNER, false);
			g.fill(s);
		}

		// Drawing rectangle zones...
		// top
		s = ShapeFactory.getInstace().createRectangle(insets.left - 1, y + numBorders, (bounds.width - insets.left - insets.right) + 2, insets.top - numBorders);
		g.fill(s);
		// bottom
		s = ShapeFactory.getInstace().createRectangle(insets.left - 1, (y + bounds.height) - insets.bottom, (bounds.width - insets.left - insets.right) + 2,
				insets.bottom - numBorders);
		g.fill(s);
		// left
		s = ShapeFactory.getInstace().createRectangle(x + numBorders, insets.top, insets.right - numBorders - 1, bounds.height - insets.top - insets.bottom);
		g.fill(s);
		// right
		s = ShapeFactory.getInstace().createRectangle(((x + bounds.width) - insets.right) + 1, insets.top, insets.right - numBorders - 1,
				bounds.height - insets.top - insets.bottom);
		g.fill(s);

		g.setPaint(previousPaint);
	}

}
