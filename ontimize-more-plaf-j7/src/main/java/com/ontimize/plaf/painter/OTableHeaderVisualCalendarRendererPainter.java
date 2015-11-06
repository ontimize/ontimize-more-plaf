package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class OTableHeaderVisualCalendarRendererPainter extends
		OTableHeaderRendererPainter {
	
	protected final Color color1 = new Color(0x959ea3);
	protected final Color color2 = new Color(0xb6bdbf);
	
	protected Paint topBorderColor;
	protected Paint bottomBorderColor;

	public OTableHeaderVisualCalendarRendererPainter(int state, PaintContext ctx) {
		super(state, ctx);
	}
	
	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height,
			Object[] extendedCacheKeys) {
		super.doPaint(g, c, width, height, extendedCacheKeys);
	}
	
	@Override
	protected void init() {
		super.init();
		
		Object obj = UIManager.getLookAndFeelDefaults().get( "TableHeader:\"VisualCalendar:TableHeader.renderer\".topBorder");
		if(obj instanceof Paint){
			topBorderColor = (Paint)obj;
		}else{
			topBorderColor = color1;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "TableHeader:\"VisualCalendar:TableHeader.renderer\".bottomBorder");
		if(obj instanceof Paint){
			bottomBorderColor = (Paint)obj;
		}else{
			bottomBorderColor = color2;
		}
	}
	
	protected void paintBorders(Graphics2D g, JComponent c){
		//Painting borders...
		rect = decodeTopBorder();
		g.setPaint(topBorderColor);
		g.fill(rect);
		
		rect = decodeBottomBorder();
		g.setPaint(bottomBorderColor);
		g.fill(rect);
	}
	
//	@Override
//	protected Rectangle2D decodeBackground() {
//		rect.setRect(decodeX(0.0f), //x
//				decodeY(0.0f)+5, //y
//				decodeX(3.0f), //width
//				decodeY(3.0f)-1); //height
//		return rect;
//	}
//	
//	@Override
//	protected Paint decodeBgGradient(Shape s) {
//		Rectangle2D bounds = s.getBounds2D();
//		float x = (float)bounds.getX();
//		float y = (float)bounds.getY();
//		float w = (float)bounds.getWidth();
//		float h = (float)bounds.getHeight();
//		return decodeGradient(x,  y+5,  x, (1.0f * h) + y,
//				new float[] { 0.0f,1.0f },
//				new Color[] { bgHeaderGradientInit, bgHeaderGradientEnd});
//	}

}
