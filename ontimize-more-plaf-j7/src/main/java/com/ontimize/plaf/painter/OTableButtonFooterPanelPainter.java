package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;

public class OTableButtonFooterPanelPainter extends AbstractRegionPainter {

	public static final int BACKGROUND_ENABLED = 1;

	protected Color bottomBackgroundColor, topBackgroundColor;

	public OTableButtonFooterPanelPainter(int state, PaintContext ctx) {
		super(state, ctx);
	}

	@Override
	protected String getComponentKeyName() {
		return "\"TableButtonFooterPanel\"";
	}

	@Override
	protected void init() {
		super.init();

		this.topBackgroundColor = UIManager.getColor(this.getComponentKeyName() + ".topBackgroundColor");
		this.bottomBackgroundColor = UIManager.getColor(this.getComponentKeyName() + ".bottomBackgroundColor");
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		this.paintBackground(g, c, width, height);
	}

	protected void paintBackground(Graphics2D g, JComponent c, int width, int height) {

		Color oldC = g.getColor();

		GradientPaint downPaint = new GradientPaint(0, 0, this.topBackgroundColor, 0, this.decodeHeight(height), this.bottomBackgroundColor);
		Graphics2D g2D = g;
		Paint oldPaint = g2D.getPaint();
		g2D.setPaint(downPaint);
		g2D.fillRect(0, 0, width, this.decodeHeight(height));
		g2D.setPaint(oldPaint);

		Shape s = ShapeFactory.getInstace().createSemiRoundRect(0, this.decodeHeight(height), width, height - this.decodeHeight(height), 5, 5, ShapeFactory.BOTTOM_ORIENTATION);
		g.setColor(this.bottomBackgroundColor);
		g.fill(s);
		g.setColor(oldC);
	}

	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}

	protected int decodeHeight(int height) {
		return (int) (height * 0.2);
	}
}
