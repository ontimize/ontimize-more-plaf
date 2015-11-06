package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Shape;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;


public class OCollapsibleButtonPanelPainter extends AbstractRegionPainter {

	public static final int BACKGROUND_ENABLED = 1;

	 protected int state; //refers to one of the static ints above
	 protected PaintContext ctx;
	    
	 protected Color backgroundColor,lineBorderColor;
	 protected Color derived1,derived2;
	 
	 public OCollapsibleButtonPanelPainter(int state, PaintContext ctx) {
		 super();
		 this.state = state;
		 this.ctx = ctx;	 
		 
		 lineBorderColor = UIManager.getColor("\"CollapsibleButtonPanel\".lineBorderColor");
		 backgroundColor = UIManager.getColor("\"CollapsibleButtonPanel\".backgroundColor");
		 derived1 = OntimizeLAFColorUtils.setAlpha(lineBorderColor, 0.3);
		 derived2 = OntimizeLAFColorUtils.setAlpha(lineBorderColor, 0.05);
	}
	
	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		paintBackground(g, c, width, height);
	}

	
	protected void paintBackground(Graphics2D g, JComponent c, int width, int height){
		
		
		Color oldC = g.getColor();
		
		Insets insets = c.getInsets();
		int offset = 2;
		width = width - offset;
		
		Shape s = ShapeFactory.getInstace().createSemiRoundRect(0,insets.top,width-1,height-insets.top-insets.bottom, 9,9,ShapeFactory.LEFT_ORIENTATION);
		g.setColor(backgroundColor);
		g.fill(s);
		
		g.setColor(lineBorderColor);
		g.fillRect(width-1, insets.top, 1, height-insets.top-insets.bottom);
		
		GradientPaint downPaint = new GradientPaint(width-1, 0, derived1, width-1-5, 0, derived2);
		
		Graphics2D g2D = (Graphics2D)g;
		Paint oldPaint = g2D.getPaint();
		g2D.setPaint(downPaint);
		
		g2D.fillRect(width-6,insets.top, 5,height-insets.top-insets.bottom);
		
		g2D.setPaint(oldPaint);
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
