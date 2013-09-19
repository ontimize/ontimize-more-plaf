package com.ontimize.plaf.painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;



public class OFormHeaderButtonPainter extends AbstractOButtonPainter{
	
	public static  Color defaultFocusBgColor = new Color(0x366581);
	protected Color focusBgColor;
	
	public static  Color defaultMouseOverBgColor = new Color(0xffffff);
	protected Color mouseOverBgColor;

	protected PaintContext ctx;
	protected int state;
	
	protected JComponent component;
	
	public OFormHeaderButtonPainter(int state, PaintContext ctx) {
		super(state, ctx);
		this.state = state;
		this.ctx = ctx;
		
		init();
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height,
			Object[] extendedCacheKeys) {
		componentColors = extendedCacheKeys;
		this.component = c;
		 switch(state) {
        case BACKGROUND_DEFAULT_FOCUSED: paintBackgroundDefaultAndFocused(g); break;
        case BACKGROUND_FOCUSED: paintBackgroundFocused(g); break;
        case BACKGROUND_MOUSEOVER: paintBackgroundMouseOver(g); break;
        case BACKGROUND_MOUSEOVER_FOCUSED: paintBackgroundMouseOverAndFocused(g); break;
        case BACKGROUND_PRESSED: paintBackgroundPressed(g); break;
        case BACKGROUND_PRESSED_FOCUSED: paintBackgroundPressedAndFocused(g); break;
		 }
		
	}
	
	protected JComponent getComponent(){
		return this.component;
	}
	
	@Override
	protected String getComponentKeyName() {
		return "\"FormHeaderButton\"";
	}
	
	protected void init (){
		super.init();

		Object obj = UIManager.getDefaults().get("\"FormHeaderButton\"[Focused].background");
		if(obj instanceof Color){
			this.focusBgColor = (Color)obj;
		}else{
			this.focusBgColor = defaultFocusBgColor;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "\"PopupItem\"" + "[MouseOver].background");
		if(obj instanceof Paint){
			mouseOverBgColor = (Color)obj;
		}else{
			mouseOverBgColor = defaultMouseOverBgColor;
		}
		
	}

	protected void paintBackgroundDefaultAndFocused(Graphics2D g) {
		
		JComponent c = getComponent();
		if(c.getParent() instanceof JPopupMenu){
//			paintBackgroundMouseOver(g);
			return;
		}
		
    	AlphaComposite old = (AlphaComposite) g.getComposite();
    	g.setComposite(getDerivedAlphaComposite());
        roundRect = decodeRoundRect4();
        g.setPaint(focusBgColor!=null ? focusBgColor : defaultFocusBgColor);
        g.fill(roundRect);
        g.setComposite(old);
    }
	
	protected void paintBackgroundFocused(Graphics2D g) {
		
		JComponent c = getComponent();
		if(c.getParent() instanceof JPopupMenu){
//			paintBackgroundMouseOver(g);
			return;
		}
		
    	AlphaComposite old = (AlphaComposite) g.getComposite();
    	g.setComposite(getDerivedAlphaComposite());
        roundRect = decodeRoundRect4();
        g.setPaint(focusBgColor!=null ? focusBgColor : defaultFocusBgColor);
        g.fill(roundRect);
        g.setComposite(old);
    }
	
	@Override
	protected void paintBackgroundMouseOver(Graphics2D g) {
		JComponent c = getComponent();
		if(c.getParent() instanceof JPopupMenu){
			rect = decodeRect();
			g.setPaint(mouseOverBgColor);
			g.fill(rect);
		}
	}
	
	@Override
	protected void paintBackgroundMouseOverAndFocused(Graphics2D g) {
		JComponent c = getComponent();
		if(c.getParent() instanceof JPopupMenu){
			rect = decodeRect();
			g.setPaint(mouseOverBgColor);
			g.fill(rect);
		}
	}
	
	@Override
	protected void paintBackgroundPressed(Graphics2D g) {
		JComponent c = getComponent();
		if(c.getParent() instanceof JPopupMenu){
			rect = decodeRect();
			g.setPaint(mouseOverBgColor);
			g.fill(rect);
		}
	}
	
	@Override
	protected void paintBackgroundPressedAndFocused(Graphics2D g) {
		JComponent c = getComponent();
		if(c.getParent() instanceof JPopupMenu){
			rect = decodeRect();
			g.setPaint(mouseOverBgColor);
			g.fill(rect);
		}
	}
	
	protected Rectangle2D decodeRect() {
		rect.setRect(decodeX(0.0f), //x
				decodeY(0.0f), //y
				decodeX(3.0f), //width
				decodeY(3.0f) ); //height
		return rect;
	}

}
