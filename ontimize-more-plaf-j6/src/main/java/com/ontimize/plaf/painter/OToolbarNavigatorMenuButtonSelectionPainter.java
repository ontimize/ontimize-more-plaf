package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;


public class OToolbarNavigatorMenuButtonSelectionPainter extends AbstractOButtonPainter{
	
	public static  Color defaultFocusBgColor = new Color(0x366581);
	protected Color focusBgColor;
	
	protected Paint foregroundColorDisabled;
	protected Paint foregroundColorEnabled;
	protected Paint foregroundColorMouseOver;
	protected Paint foregroundColorPressed;
	protected Paint foregroundColorFocused;
	
    protected Path2D path = new Path2D.Float();

	public OToolbarNavigatorMenuButtonSelectionPainter(int state, PaintContext ctx) {
		super(state, ctx);
	}
	
	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height,
			Object[] extendedCacheKeys) {
		 componentColors = extendedCacheKeys;
		 super.doPaint(g, c, width, height, extendedCacheKeys);
	}
	
	@Override
	protected String getComponentKeyName() {
		return "\"ToolbarNavigatorMenuButtonSelection\"";
	}
	
	protected void init (){
		super.init();

		Object obj = UIManager.getDefaults().get("\"ToolbarNavigatorMenuButtonSelection\"[Focused].background");
		if(obj instanceof Color){
			this.focusBgColor = (Color)obj;
		}else{
			this.focusBgColor = defaultFocusBgColor;
		}
		
		// disabled:
		obj = UIManager.getLookAndFeelDefaults().get(getComponentKeyName() + "[Disabled].foreground");
		if(obj instanceof Paint){
			foregroundColorDisabled = (Paint)obj;
		}else{
			foregroundColorDisabled = Color.black;
		}

		// enabled:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Enabled].foreground");
		if(obj instanceof Paint){
			foregroundColorEnabled = (Paint)obj;
		}else{
			foregroundColorEnabled = Color.black;
		}

		// selected:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Focused].foreground");
		if(obj instanceof Paint){
			foregroundColorFocused = (Paint)obj;
		}else{
			foregroundColorFocused = Color.black;
		}
		
		// mouseover:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[MouseOver].foreground");
		if(obj instanceof Paint){
			foregroundColorMouseOver = (Paint)obj;
		}else{
			foregroundColorMouseOver = Color.black;
		}

		// pressed:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Pressed].foreground");
		if(obj instanceof Paint){
			foregroundColorPressed = (Paint)obj;
		}else{
			foregroundColorPressed = Color.black;
		}
		
	}
	
	@Override
	protected void paintBackgroundEnabled(Graphics2D g) {
		super.paintBackgroundEnabled(g);
		g.setPaint(foregroundColorEnabled);
		Shape arrow = decodeArrow();
		g.fill(arrow);
	}
	
	@Override
	protected void paintBackgroundDisabled(Graphics2D g) {
		super.paintBackgroundDisabled(g);
		g.setPaint(foregroundColorDisabled);
		Shape arrow = decodeArrow();
		g.fill(arrow);
	}
	
	@Override
	protected void paintBackgroundMouseOver(Graphics2D g) {
		super.paintBackgroundMouseOver(g);
		g.setPaint(foregroundColorMouseOver);
		Shape arrow = decodeArrow();
		g.fill(arrow);
	}
	
	protected void paintBackgroundDefaultAndFocused(Graphics2D g) {
		super.paintBackgroundDefaultAndFocused(g);
		
        g.setPaint(foregroundColorFocused);
        Shape arrow = decodeArrow();
		g.fill(arrow);
    }
	
	protected void paintBackgroundFocused(Graphics2D g) {
		super.paintBackgroundFocused(g);
		
        g.setPaint(foregroundColorFocused);
        Shape arrow = decodeArrow();
		g.fill(arrow);
    }
	
	@Override
	protected void paintBackgroundPressed(Graphics2D g) {
		super.paintBackgroundPressed(g);
		g.setPaint(foregroundColorPressed);
		Shape arrow = decodeArrow();
		g.fill(arrow);
	}
	
	@Override
	protected void paintBackgroundMouseOverAndFocused(Graphics2D g) {
		super.paintBackgroundMouseOverAndFocused(g);
		g.setPaint(foregroundColorFocused);
		Shape arrow = decodeArrow();
		g.fill(arrow);
	}
	
	@Override
	protected void paintBackgroundPressedAndFocused(Graphics2D g) {
		super.paintBackgroundPressedAndFocused(g);
		g.setPaint(foregroundColorFocused);
		Shape arrow = decodeArrow();
		g.fill(arrow);
	}
	
	protected Path2D decodeArrow() {
        path.reset();
        
        double xcenter = (decodeX(3.0f))/2.0;
        double ycenter = (decodeY(3.0f))/2.0;
        
        double arrowHeight = 4;
        double arrowWidth = 6;
        
        path.moveTo(xcenter-arrowWidth/2.0, ycenter-arrowHeight/2.0);
        path.lineTo(xcenter+arrowWidth/2.0, ycenter-arrowHeight/2.0);
        path.lineTo(xcenter, ycenter+arrowHeight/2.0);
        path.lineTo(xcenter-arrowWidth/2.0, ycenter-arrowHeight/2.0);
        
        path.closePath();
        return path;
    }
	
	protected RoundRectangle2D decodeRoundRect1() {
		roundRect.setRoundRect(decodeX(0.2857143f), // x
				decodeY(0.42857143f), // y
				decodeX(2.7142859f) - decodeX(0.2857143f), // width
				decodeY(2.857143f) - decodeY(0.42857143f), // height
				9.0f, 9.0f); // rounding
		return roundRect;
	}

	protected RoundRectangle2D decodeRoundRect2() {
		roundRect.setRoundRect(decodeX(0.2857143f), // x
				decodeY(0.2857143f), // y
				decodeX(2.7142859f) - decodeX(0.2857143f), // width
				decodeY(2.7142859f) - decodeY(0.2857143f), // height
				6.0f, 6.0f); // rounding
		return roundRect;
	}

	protected RoundRectangle2D decodeRoundRect3() {
		roundRect.setRoundRect(decodeX(0.42857143f), // x
				decodeY(0.42857143f), // y
				decodeX(2.5714285f) - decodeX(0.42857143f), // width
				decodeY(2.5714285f) - decodeY(0.42857143f), // height
				6.0f, 6.0f); // rounding
		return roundRect;
	}

	protected RoundRectangle2D decodeRoundRect4() {
		roundRect.setRoundRect(decodeX(0.08571429f), // x
				decodeY(0.08571429f), // y
				decodeX(2.914286f) - decodeX(0.08571429f), // width
				decodeY(2.914286f) - decodeY(0.08571429f), // height
				8.0f, 8.0f); // rounding
		return roundRect;
	}

	protected RoundRectangle2D decodeRoundRect5() {
		roundRect.setRoundRect(decodeX(0.2857143f), // x
				decodeY(0.42857143f), // y
				decodeX(2.7142859f) - decodeX(0.2857143f), // width
				decodeY(2.857143f) - decodeY(0.42857143f), // height
				6.0f, 6.0f); // rounding
		return roundRect;
	}



}
