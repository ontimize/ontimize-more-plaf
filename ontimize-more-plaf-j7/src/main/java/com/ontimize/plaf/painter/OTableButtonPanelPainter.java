package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;


public class OTableButtonPanelPainter extends AbstractRegionPainter {

	public static final int BACKGROUND_ENABLED = 1;

	 protected int state; //refers to one of the static ints above
	 protected PaintContext ctx;
	    
	 protected Color bottomBackgroundColor,topBackgroundColor;
	 
	 
	 public OTableButtonPanelPainter(int state, PaintContext ctx) {
		 super();
		 this.state = state;
		 this.ctx = ctx;	 
		 
		 topBackgroundColor = UIManager.getColor("\"TableButtonPanel\".topBackgroundColor");
		 bottomBackgroundColor = UIManager.getColor("\"TableButtonPanel\".bottomBackgroundColor");
	}
	
	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		paintBackground(g, c, width, height);
	}

	
	protected void paintBackground(Graphics2D g, JComponent c, int width, int height){
		
		Color oldC = g.getColor();
		
		GradientPaint downPaint = new GradientPaint(0,height, bottomBackgroundColor, 0, decodeHeight(height), topBackgroundColor);
		Graphics2D g2D = (Graphics2D)g;
		Paint oldPaint = g2D.getPaint();
		g2D.setPaint(downPaint);
		g2D.fillRect(0,(int)(height-(height*0.2)), width, height);
		g2D.setPaint(oldPaint);
		
		Shape s = ShapeFactory.getInstace().createSemiRoundRect(0,0,width,decodeHeight(height), 5,5,ShapeFactory.TOP_ORIENTATION);
		g.setColor(topBackgroundColor);
		g.fill(s);
		g.setColor(oldC);
	}
	
	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}
	
	protected int decodeHeight(int height){
		return (int)(height-(height*0.2));
	}	
}
