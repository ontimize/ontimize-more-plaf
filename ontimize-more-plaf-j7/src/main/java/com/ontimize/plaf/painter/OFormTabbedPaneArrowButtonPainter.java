package com.ontimize.plaf.painter;

import java.awt.Graphics2D;

import javax.swing.JComponent;

public class OFormTabbedPaneArrowButtonPainter extends OTabbedPaneArrowButtonPainter {

	/**
	 * Creates a new ArrowButtonPainter object.
	 *
	 * @param state
	 *            the control state.
	 */
	public OFormTabbedPaneArrowButtonPainter(int state, PaintContext ctx) {
		super(state, ctx);
		this.state = state;
		this.ctx = ctx;
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		super.doPaint(g, c, width, height, extendedCacheKeys);
	}

	@Override
	protected String getComponentKeyName() {
		return "FormTabbedPane:TabbedPaneTabArea:\"FormTabbedPaneTabArea.button\"";
	}

}
