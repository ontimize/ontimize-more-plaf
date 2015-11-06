package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;



public class OCardPanelPainter extends AbstractRegionPainter {

	public static final int BACKGROUND_ENABLED = 1;

	 protected int state; //refers to one of the static ints above
	 protected PaintContext ctx;
	 
	 public OCardPanelPainter(int state, PaintContext ctx) {
		 super();
		 this.state = state;
		 this.ctx = ctx;	 
	}
	
	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		paintBackground(g, c, width, height);
	}

	
	protected void paintBackground(Graphics2D g, JComponent c, int w, int h){
		if (c.isOpaque()){
			Color background = c.getBackground();
			Insets insets = c.getInsets();
			Color old = g.getColor();
			g.setColor(background);
			g.fillRect(insets.left , insets.top , w-insets.left-insets.right , h-insets.top-insets.bottom);
			g.setColor(old);
		}
	}
	
	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}
	
	protected int decodeHeight(int height){
		return (int)(height-(height*0.2));
	}	
}
