package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIManager;



/**
 * Painter for the background of the buttons in a scroll bar (to configure border and foreground colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OScrollBarThumbPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of ScrollBarScrollBarThumbPainter to determine which region/state is being painted
	//by that instance.
	public static final int BACKGROUND_DISABLED = 1;
	public static final int BACKGROUND_ENABLED = 2;
	public static final int BACKGROUND_FOCUSED = 3;
	public static final int BACKGROUND_MOUSEOVER = 4;
	public static final int BACKGROUND_PRESSED = 5;


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;

	//the following 4 variables are reused during the painting code of the layers
	protected Path2D path = new Path2D.Float();

	//Array of current component colors, updated in each paint call
	protected Object [] componentColors;

	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of ScrollBarScrollBarThumbPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = decodeColor("nimbusBase", 5.1498413E-4f, 0.18061227f, -0.35686278f, 0);
	protected final Color color11 = decodeColor("nimbusBase", -0.0017285943f, -0.362987f, 0.011764705f, -255);


	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorMouseOver;
	protected Paint backgroundColorPressed;
	protected Paint backgroundShadowColorEnabled;
	protected Paint backgroundShadowColorMouseOver;
	protected Paint backgroundShadowColorPressed;
	
	protected Paint borderColorEnabled;
	protected Paint borderColorMouseOver;
	protected Paint borderColorPressed;

	//Array of current component colors, updated in each paint call

	public OScrollBarThumbPainter(int state, PaintContext ctx) {
		super();
		this.state = state;
		this.ctx = ctx;

		init();
	}


	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object [] extendedCacheKeys) {
		//populate componentColors array with colors calculated in getExtendedCacheKeys call
		componentColors = extendedCacheKeys;

		//generate this entire method. Each state/bg/fg/border combo that has
		//been painted gets its own KEY and paint method.
		switch(state) {
		case BACKGROUND_ENABLED: paintBackgroundEnabled(g,c,width,height); break;
		case BACKGROUND_MOUSEOVER: paintBackgroundMouseOver(g,c,width,height); break;
		case BACKGROUND_PRESSED: paintBackgroundPressed(g,c,width,height); break;
		}
	}

	@Override
	protected PaintContext getPaintContext() {
		return ctx;
	}

	/**
	 * Get configuration properties to be used in this painter (such as: *BorderPainter,  *upperCornerPainter,  *lowerCornerPainter and *BgPainter).
	 * As usual, it is checked the OLAFCustomConfig.properties, and then in OLAFDefaultConfig.properties.
	 *
	 * Moreover, if there are not values for that properties in both, the default Nimbus configuration values are set, due to, those properties are needed to
	 * paint and used by the Ontimize L&F, so, there are not Nimbus values for that, so, default values are always configured based on Nimbus values.
	 *
	 */
	protected void init (){

		// enable:
		Object obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarThumb[Enabled].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}else{
			backgroundColorEnabled = color1;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarThumb[Enabled].backgroundShadow");
		if(obj instanceof Paint){
			backgroundShadowColorEnabled = (Paint)obj;
		}else{
			backgroundShadowColorEnabled = color11;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarThumb[Enabled].border");
		if(obj instanceof Paint){
			borderColorEnabled = (Paint)obj;
		}else{
			borderColorEnabled = color1;
		}

		// mouse Over:
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarThumb[MouseOver].background");
		if(obj instanceof Paint){
			backgroundColorMouseOver = (Paint)obj;
		}else{
			backgroundColorMouseOver = color1;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarThumb[MouseOver].backgroundShadow");
		if(obj instanceof Paint){
			backgroundShadowColorMouseOver = (Paint)obj;
		}else{
			backgroundShadowColorMouseOver = color11;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarThumb[MouseOver].border");
		if(obj instanceof Paint){
			borderColorMouseOver = (Paint)obj;
		}else{
			borderColorMouseOver = color1;
		}

		// Pressed:
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarThumb[Pressed].background");
		if(obj instanceof Paint){
			backgroundColorPressed = (Paint)obj;
		}else{
			backgroundColorPressed = color1;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarThumb[Pressed].backgroundShadow");
		if(obj instanceof Paint){
			backgroundShadowColorPressed = (Paint)obj;
		}else{
			backgroundShadowColorPressed = color11;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarThumb[Pressed].border");
		if(obj instanceof Paint){
			borderColorPressed = (Paint)obj;
		}else{
			borderColorPressed = color1;
		}

	}

	protected void paintBackgroundEnabled(Graphics2D g, JComponent c, int width, int height) {

		paintRoundThumb(g, width, height, borderColorEnabled, backgroundColorEnabled);
		paintInteriorRoundThumb(g, c, width, height, backgroundShadowColorEnabled);

	}
	

	protected void paintBackgroundMouseOver(Graphics2D g, JComponent c, int width, int height) {

		paintRoundThumb(g, width, height, borderColorMouseOver, backgroundColorMouseOver);
		paintInteriorRoundThumb(g, c, width, height, backgroundShadowColorMouseOver);
	}


	protected void paintBackgroundPressed(Graphics2D g, JComponent c, int width, int height) {

		paintRoundThumb(g, width, height, borderColorPressed, backgroundColorPressed);
		paintInteriorRoundThumb(g, c, width, height, backgroundShadowColorPressed);
	}


	protected void paintRoundThumb(Graphics2D g, int width, int height, Paint borderColor, Paint backgroundColor){
		Shape s = createRoundRect(g, 0, 0, width, height);
		g.setPaint(borderColor);
		g.fill(s);
		
		s = createRoundRect(g, 1, 1, width-2, height-2);
		g.setPaint( backgroundColor);
		g.fill(s);
	}
	
	protected void paintInteriorRoundThumb(Graphics2D g, JComponent c, int width, int height, Paint backgroundColor){
		Shape s = createInsideThumbShape(g, c, 4, height/2.0 , width-8, (height-2)/2.0 -1);
		g.setPaint(backgroundColor);
		g.fill(s);
	}


	protected Shape createRoundRect(Graphics2D g, double x, double y, double w, double h) {
		
		double radius = h / 2.0;
		path.reset();
		
		//left top corner.
		path.moveTo(x + radius, y);
		path.curveTo(x + radius / 2.0, y, x, y + radius / 2.0, x, y+ radius);

		//left bottom corner
		path.curveTo(x,y+ h-radius/2.0, x+radius/2.0, y+h, x+radius, y+h);
		path.lineTo(x+w-radius, y+h);
		
		//right bottom corner
		path.curveTo(x+w-radius/2.0,y+ h, x+w, y+h-radius/2.0, x+w, y+h-radius);
		
		//right top corner
		path.curveTo(x+w,y+radius/2.0, x+w-radius/2.0, y, x+w-radius, y);
		path.lineTo(x+radius, y);
		
		return path;
	}
	
	protected Shape createInsideThumbShape(Graphics2D g, JComponent c, double x, double y, double w, double h) {
		
		path.reset();
		double rad = h ;
		if(c instanceof JScrollBar){
			int orientation = ((JScrollBar)c).getOrientation();
			
			if(JScrollBar.VERTICAL == orientation){
				
				//left top corner.
				path.moveTo(x , y);
				path.curveTo(x , y-rad/2.0, x+rad/2.0, y-h , x+rad, y- h);

				//left bottom corner
				path.lineTo(x+w-rad, y- h);
				path.curveTo(x+w-rad/2.0, y- h, x+w, y-rad/2.0, x+w, y);
				path.closePath();
			}else{
				//left top corner.
				path.moveTo(x , y);
				//left bottom corner.
				path.curveTo(x , y+rad/2.0, x+rad/2.0, y+h , x+rad, y+ h);

				// bottom line
				path.lineTo(x+w-rad, y+ h );
				// right bottom corner.
				path.curveTo(x+w-rad/2.0,y+ h, x+w, y+rad/2.0, x+w, y);
				path.closePath();
			}
		}
		
		return path;
	}

}
