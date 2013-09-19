package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.UIManager;



public class OFormButtonPanelPainter extends AbstractRegionPainter {

	public static int BACKGROUND_ENABLED = 1;

	 protected int state; //refers to one of the static ints above
	 protected PaintContext ctx;
	    
	 protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);
	 
	 protected Color bottomBackgroundColor,topBackgroundColor;
	 
	 
	 public OFormButtonPanelPainter(int state, PaintContext ctx) {
		 super();
		 this.state = state;
		 this.ctx = ctx;	 
		 
		 topBackgroundColor = UIManager.getColor("\"FormButtonPanel\".topBackgroundColor");
		 bottomBackgroundColor = UIManager.getColor("\"FormButtonPanel\".bottomBackgroundColor");
		 
		
		 
	}
	
	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		paintBackground(g, c, width, height);
	}

	
	protected void paintBackground(Graphics2D g, JComponent c, int w, int h){
		
		Color oldC = g.getColor();
		
		g.setColor(Color.black);
		g.fillRect(0,0,w,h);
		
		GradientPaint downPaint = new GradientPaint(0,h, bottomBackgroundColor, 0, 0, topBackgroundColor);
		Graphics2D g2D = (Graphics2D)g;
		Paint oldPaint = g2D.getPaint();
		g2D.setPaint(downPaint);
		int arc = 6;
		w = w-1;
		path.reset();
		path.moveTo(0, h);
		path.lineTo(0, 0);
		path.lineTo(w - arc , 0);
		path.quadTo(w, 0, w, arc);
		path.lineTo(w, h);
		path.closePath();
		
		g.fill(path);
		
		g2D.setPaint(oldPaint);
		
		
	}
	
	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}
	
	protected int decodeHeight(int height){
		return (int)(height-(height*0.2));
	}	
}
