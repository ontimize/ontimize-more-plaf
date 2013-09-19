package com.ontimize.plaf.painter;

import java.awt.Graphics2D;

import javax.swing.JComponent;


/**
 * This class indicates the component name for the component that must be painted. Paint instructions, indications, ... are encoded in the class that this
 * class extends (named {@link AbstractOTextFieldPainter} )
 *
 * This kind of extensions abstract painters were developed to allow users to have several componentes which are painted in the same way, and
 * configured by the same set of properties, but having only a common code to paint (the class that this one extends).
 *
 * @author Imatia S.L.
 *
 */
public class OReferenceExtCodeFieldPainter extends AbstractOTextFieldPainter{

	public OReferenceExtCodeFieldPainter(int state, PaintContext ctx) {
		super(state, ctx);
	}
	
	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		super.doPaint(g, c, width, height, extendedCacheKeys);
	}

	@Override
	protected String getComponentKeyName() {
		return "TextField:\"TextField.ReferenceExtCode\"";
	}
	
}
