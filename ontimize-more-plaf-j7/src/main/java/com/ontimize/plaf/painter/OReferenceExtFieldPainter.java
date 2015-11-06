package com.ontimize.plaf.painter;

import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JComponent;

import com.ontimize.gui.field.ReferenceExtDataField;


/**
 * This class indicates the component name for the component that must be painted. Paint instructions, indications, ... are encoded in the class that this
 * class extends (named {@link AbstractOTextFieldPainter} )
 *
 * This kind of extensions painters were developed to allow users to have several componentes which are painted in the same way, and
 * configured by the same set of properties, but having only a common code to paint (the class that this one extends).
 *
 * @author Imatia S.L.
 *
 */
public class OReferenceExtFieldPainter extends AbstractOTextFieldPainter{

	public OReferenceExtFieldPainter(int state, PaintContext ctx) {
		super(state, ctx);
	}
	
	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		super.doPaint(g, c, width, height, extendedCacheKeys);
	}

	@Override
	protected String getComponentKeyName() {
		return "TextField:\"TextField.ReferenceExt\"";
	}
	
	protected void paintBorderDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		
		if(c.getParent() instanceof ReferenceExtDataField){
			ReferenceExtDataField ref = (ReferenceExtDataField)c.getParent();
			if(ref.isEnabled()==false){
				if (degradatedBorderColorDisabled != null && degradatedBorderColorDisabled.length > 0) {
					drawDegradatedBorders(g, c, x, y, width, height, degradatedBorderColorDisabled);
				}
			}else{
				if (degradatedBorderColorEnabled != null && degradatedBorderColorEnabled.length > 0) {
					drawDegradatedBorders(g, c, x, y, width, height, degradatedBorderColorEnabled);
				}
			}
			
			if(ref.isCodeFieldVisible()==false && ref.isCodeSearchVisible()==false && ref.isEnabled()==false){
				g.drawImage(paddLock, 1, (int) (decodeY(2.0f) - 9), 10, 10, null);
			}
		}else{
			super.paintBorderDisabled(g, c, x, y, width, height);
		}
		
	}
	
	protected void paintBorderEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		super.paintBorderEnabled(g, c, x, y, width, height);
	}
	
	protected void paintBackgroundDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {

		if(c.getParent() instanceof ReferenceExtDataField){
			ReferenceExtDataField ref = (ReferenceExtDataField)c.getParent();
			if(ref.isEnabled()==false){
				super.paintBackgroundDisabled(g, c, x, y, width, height);
			}else{
				drawBackground(g, c, x, y, width, height, backgroundColorEnabled);
			}
		}else{
			super.paintBackgroundDisabled(g, c, x, y, width, height);
		}
	}

	protected void paintBackgroundEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		super.paintBackgroundEnabled(g, c, x, y, width, height);
	}
	
	@Override
	protected Paint getBackgroundForState(JComponent c) {
		if(c.getParent() instanceof ReferenceExtDataField){
			ReferenceExtDataField ref = (ReferenceExtDataField)c.getParent();
			if(ref.isEnabled()==false){
				return super.getBackgroundForState(c);
			}else{
				Paint paint = null;
				switch (state) {
					case BACKGROUND_DISABLED: paint = getBackgroundColor(c, backgroundColorEnabled); break;
					case BORDER_DISABLED: paint = getBackgroundColor(c, backgroundColorEnabled); break;
					default: paint = super.getBackgroundForState(c);
				}
				return paint;
			}
		}else{
			return super.getBackgroundForState(c);
		}
	}
	
	@Override
	protected Paint getBackgroundColor(JComponent c, Paint defaultColor) {
		if(c.getParent() instanceof ReferenceExtDataField){
			ReferenceExtDataField ref = (ReferenceExtDataField)c.getParent();
			if(ref.isEnabled()==false){
				return super.getBackgroundColor(c, defaultColor);
			}else{
				if(c.getBackground()!= null ){
					return c.getBackground();
				}
			}
		}
		return super.getBackgroundColor(c, defaultColor);
	}
	
}
