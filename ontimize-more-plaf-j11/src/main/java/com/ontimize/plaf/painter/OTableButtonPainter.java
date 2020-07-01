package com.ontimize.plaf.painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class OTableButtonPainter extends AbstractOButtonPainter {

	public static Color defaultFocusBgColor = new Color(0x366581);
	protected Color focusBgColor;

	public OTableButtonPainter(int state, PaintContext ctx) {
		super(state, ctx);
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		this.componentColors = extendedCacheKeys;
		switch (this.state) {
		case BACKGROUND_DEFAULT_FOCUSED:
			this.paintBackgroundDefaultAndFocused(g);
			break;
		case BACKGROUND_FOCUSED:
			this.paintBackgroundFocused(g);
			break;
		}
	}

	@Override
	protected String getComponentKeyName() {
		return "\"TableButton\"";
	}

	@Override
	protected void init() {
		super.init();
		Object obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Focused].background");
		if (obj instanceof Color) {
			this.focusBgColor = (Color) obj;
		} else {
			this.focusBgColor = OTableButtonPainter.defaultFocusBgColor;
		}

	}

	@Override
	protected void paintBackgroundDefaultAndFocused(Graphics2D g) {
		AlphaComposite old = (AlphaComposite) g.getComposite();
		g.setComposite(this.getDerivedAlphaComposite());
		this.roundRect = this.decodeRoundRect4();
		g.setPaint(this.focusBgColor != null ? this.focusBgColor : OTableButtonPainter.defaultFocusBgColor);
		g.fill(this.roundRect);
		g.setComposite(old);
	}

	@Override
	protected void paintBackgroundFocused(Graphics2D g) {
		AlphaComposite old = (AlphaComposite) g.getComposite();
		g.setComposite(this.getDerivedAlphaComposite());
		this.roundRect = this.decodeRoundRect4();
		g.setPaint(this.focusBgColor != null ? this.focusBgColor : OTableButtonPainter.defaultFocusBgColor);
		g.fill(this.roundRect);
		g.setComposite(old);
	}

}
