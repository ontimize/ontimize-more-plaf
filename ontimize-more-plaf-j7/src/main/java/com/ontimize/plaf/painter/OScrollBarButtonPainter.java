package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.UIManager;



/**
 * Painter for the foreground of the buttons in a scroll bar (to configure border and foreground colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OScrollBarButtonPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of ScrollBarScrollBarButtonPainter to determine which region/state is being painted
	//by that instance.
	public static final int FOREGROUND_ENABLED = 1;
	public static final int FOREGROUND_DISABLED = 2;
	public static final int FOREGROUND_MOUSEOVER = 3;
	public static final int FOREGROUND_PRESSED = 4;


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;

	//the following 4 variables are reused during the painting code of the layers
	protected Path2D path = new Path2D.Float();


	protected final int DEFAULT_BORDERTHICKNESS = 0;

	protected String disablePainter;
	protected String enablePainter;
	protected String outerBorder_enablePainter;
	protected String arrow_enablePainter;
	protected String innerBorder_enablePainter;
	protected String mouseoverPainter;
	protected String outerBorder_mouseoverPainter;
	protected String arrow_mouseoverPainter;
	protected String innerBorder_mouseoverPainter;
	protected String pressedPainter;
	protected String outerBorder_pressedPainter;
	protected String arrow_pressedPainter;
	protected String innerBorder_pressedPainter;
	protected String disableTrackPainter;
	protected String enableTrackPainter;
	
	protected Paint trackColorDisabled;
	protected Paint trackColorEnabled;
	
	protected Paint scrollBarBorderColorEnabled;
	protected Paint scrollBarBorderColorDisabled;

	protected String outerBorder_enablePainter_direction = "(0.0f * w) + x, (0.5f * h) + y, (0.5735294f * w) + x, (0.5f * h) + y";
	protected String arrow_enablePainter_direction = "(0.925f * w) + x, (0.9285714f * h) + y, (0.925f * w) + x, (0.004201681f * h) + y";
	protected String arrow_mouseoverPainter_direction = "(0.925f * w) + x, (0.9285714f * h) + y, (0.925f * w) + x, (0.004201681f * h) + y";
	protected String outerBorder_pressedPainter_direction = "(0.0f * w) + x, (0.5f * h) + y, (0.5735294f * w) + x, (0.5f * h) + y";


	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of ScrollBarScrollBarButtonPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = new Color(189,189,189);
	protected final Color color2 = decodeColor("nimbusBlueGrey", -0.01111114f, -0.07763158f, -0.1490196f, 0);
	protected final Color color3 = decodeColor("nimbusBlueGrey", -0.111111104f, -0.10580933f, 0.086274505f, 0);
	protected final Color color4 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.102261856f, 0.20392156f, 0);
	protected final Color color5 = decodeColor("nimbusBlueGrey", -0.039682567f, -0.079276316f, 0.13333333f, 0);
	protected final Color color6 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.07382907f, 0.109803915f, 0);
	protected final Color color7 = decodeColor("nimbusBlueGrey", -0.039682567f, -0.08241387f, 0.23137254f, 0);
	protected final Color color8 = decodeColor("nimbusBlueGrey", -0.055555522f, -0.08443936f, -0.29411766f, -136);
	protected final Color color9 = decodeColor("nimbusBlueGrey", -0.055555522f, -0.09876161f, 0.25490195f, -178);
	protected final Color color10 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.08878718f, -0.5647059f, 0);
	protected final Color color11 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.080223285f, -0.4862745f, 0);
	protected final Color color12 = decodeColor("nimbusBlueGrey", -0.111111104f, -0.09525914f, -0.23137254f, 0);
	protected final Color color13 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -165);
	protected final Color color14 = decodeColor("nimbusBlueGrey", -0.04444444f, -0.080223285f, -0.09803921f, 0);
	protected final Color color15 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, 0.10588235f, 0);
	protected final Color color16 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
	protected final Color color17 = decodeColor("nimbusBlueGrey", -0.039682567f, -0.081719734f, 0.20784312f, 0);
	protected final Color color18 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.07677104f, 0.18431371f, 0);
	protected final Color color19 = decodeColor("nimbusBlueGrey", -0.04444444f, -0.080223285f, -0.09803921f, -69);
	protected final Color color20 = decodeColor("nimbusBlueGrey", -0.055555522f, -0.09876161f, 0.25490195f, -39);
	protected final Color color21 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.0951417f, -0.49019608f, 0);
	protected final Color color22 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.086996906f, -0.4117647f, 0);
	protected final Color color23 = decodeColor("nimbusBlueGrey", -0.111111104f, -0.09719298f, -0.15686274f, 0);
	protected final Color color24 = decodeColor("nimbusBlueGrey", -0.037037015f, -0.043859646f, -0.21568626f, 0);
	protected final Color color25 = decodeColor("nimbusBlueGrey", -0.06349206f, -0.07309316f, -0.011764705f, 0);
	protected final Color color26 = decodeColor("nimbusBlueGrey", -0.048611104f, -0.07296763f, 0.09019607f, 0);
	protected final Color color27 = decodeColor("nimbusBlueGrey", -0.03535354f, -0.05497076f, 0.031372547f, 0);
	protected final Color color28 = decodeColor("nimbusBlueGrey", -0.034188032f, -0.043168806f, 0.011764705f, 0);
	protected final Color color29 = decodeColor("nimbusBlueGrey", -0.03535354f, -0.0600676f, 0.109803915f, 0);
	protected final Color color30 = decodeColor("nimbusBlueGrey", -0.037037015f, -0.043859646f, -0.21568626f, -44);
	protected final Color color31 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.74509805f, 0);
	
	
	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorDisabled;
	protected Paint backgroundColorMouseOver;
	protected Paint backgroundColorPressed;
	
	protected Paint backgroundArrowColorEnabled;
	protected Paint backgroundArrowColorDisabled;
	protected Paint backgroundArrowColorMouseOver;
	protected Paint backgroundArrowColorPressed;


	//Array of current component colors, updated in each paint call
	protected Object [] componentColors;

	public OScrollBarButtonPainter(int state, PaintContext ctx) {
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
		case FOREGROUND_ENABLED: paintForegroundEnabled(g, c, width, height); break;
		case FOREGROUND_DISABLED: paintForegroundDisabled(g, c, width, height); break;
		case FOREGROUND_MOUSEOVER: paintForegroundMouseOver(g, c, width, height); break;
		case FOREGROUND_PRESSED: paintForegroundPressed(g, c, width, height); break;

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


		// disable:
		Object obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:\"ScrollBar.button\"[Disabled].background");
		if(obj instanceof Paint){
			backgroundColorDisabled = (Paint)obj;
		}else{
			backgroundColorDisabled = color1;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:\"ScrollBar.button\"[Disabled].arrowBackground");
		if(obj instanceof Paint){
			backgroundArrowColorDisabled = (Paint)obj;
		}else{
			backgroundArrowColorDisabled = color11;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:\"ScrollBar.button\"[Enabled].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}else{
			backgroundColorEnabled = color1;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:\"ScrollBar.button\"[Enabled].arrowBackground");
		if(obj instanceof Paint){
			backgroundArrowColorEnabled = (Paint)obj;
		}else{
			backgroundArrowColorEnabled = color11;
		}

		// mouse over:
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:\"ScrollBar.button\"[MouseOver].background");
		if(obj instanceof Paint){
			backgroundColorMouseOver = (Paint)obj;
		}else{
			backgroundColorMouseOver = color14;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:\"ScrollBar.button\"[MouseOver].arrowBackground");
		if(obj instanceof Paint){
			backgroundArrowColorMouseOver = (Paint)obj;
		}else{
			backgroundArrowColorMouseOver = color21;
		}


		// pressed:
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:\"ScrollBar.button\"[Pressed].background");
		if(obj instanceof Paint){
			backgroundColorPressed = (Paint)obj;
		}else{
			backgroundColorPressed = color24;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:\"ScrollBar.button\"[Pressed].arrowBackground");
		if(obj instanceof Paint){
			backgroundArrowColorPressed = (Paint)obj;
		}else{
			backgroundArrowColorPressed = color31;
		}
		
		
		//Track:
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarTrack[Enabled].background");
		if(obj instanceof Paint){
			trackColorEnabled = (Paint)obj;
		}else{
			trackColorEnabled = color1;
		}
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarTrack[Disabled].background");
		if(obj instanceof Paint){
			trackColorDisabled = (Paint)obj;
		}else{
			trackColorDisabled = color5;
		}


		//ScrollBar border:
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar[Enabled].background");
		if(obj instanceof Paint){
			scrollBarBorderColorEnabled = (Paint)obj;
		}else{
			scrollBarBorderColorEnabled = new Color(255,255,255);
		}
		
		// disable:
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar[Disabled].background");
		if(obj instanceof Paint){
			scrollBarBorderColorDisabled = (Paint)obj;
		}else{
			scrollBarBorderColorDisabled = new Color(255,255,255);
		}

	}


	protected void paintForegroundEnabled(Graphics2D g, JComponent c, int width, int height) {
		paintButton(g, c, width, height, backgroundColorEnabled, backgroundArrowColorEnabled);
	}

	protected void paintForegroundDisabled(Graphics2D g, JComponent c, int width, int height) {
		paintButton(g, c, width, height, backgroundColorDisabled, backgroundArrowColorDisabled);
	}

	protected void paintForegroundMouseOver(Graphics2D g, JComponent c, int width, int height) {
		paintButton(g, c, width, height, backgroundColorMouseOver, backgroundArrowColorMouseOver);
	}

	protected void paintForegroundPressed(Graphics2D g, JComponent c, int width, int height) {
		paintButton(g, c, width, height, backgroundColorPressed, backgroundArrowColorPressed);
	}

	
	protected void paintButton(Graphics2D g, JComponent c, int width, int height, Paint background, Paint arrowBackground){
		
		Paint trackColor = null;
		Paint scrollBarBorderColor = null;
		switch (state) {
		case FOREGROUND_ENABLED:
			trackColor = trackColorEnabled;
			scrollBarBorderColor = scrollBarBorderColorEnabled;
			break;
		case FOREGROUND_DISABLED:
			trackColor = trackColorDisabled;
			scrollBarBorderColor = scrollBarBorderColorDisabled;
			break;
		default:
			trackColor = trackColorEnabled;
			scrollBarBorderColor = scrollBarBorderColorEnabled;
		}
		paintTopButtonFiller(g, width, height, scrollBarBorderColor);
		paintBottomButtonFiller(g, width, height, trackColor);
		
		//background
		Shape s = createRoundRect( 0, 0, width, height);
		g.setPaint( background );
		g.fill(s);

		//Arrow
		s = createArrow(0, 0, width, height);
		g.setPaint( arrowBackground );
		g.fill(s);
	}
	
	protected void paintTopButtonFiller(Graphics2D g, int width, int height, Paint backgroundPainter){
		
		double radius = height/2.0;
		int x = 0;
		int y = 0;
		
		path.reset();
		path.moveTo(x, y);
		path.lineTo(x + radius, y);
		path.curveTo(x + radius / 2.0, y, x, y + radius / 2.0, x, y+ radius);
		path.curveTo(x,y+ height-radius/2.0, x+radius/2.0, y+height, x+radius, y+height);
		path.lineTo(x, y+height);
		path.lineTo(x, y);
		
		g.setPaint(backgroundPainter);
		g.fill(path);
		
	}
	
	protected void paintBottomButtonFiller(Graphics2D g, int width, int height, Paint background){
		
		double radius = height/2.0;
		int x = width;
		int y = 0;
		
		path.reset();
		path.moveTo(x, y);
		path.lineTo(x - radius, y);
		path.curveTo(x - radius / 2.0, y, x, y + radius / 2.0, x, y+ radius);
		path.curveTo(x,y+ height-radius/2.0, x-radius/2.0, y+height, x-radius, y+height);
		path.lineTo(x, y+height);
		path.lineTo(x, y);
		
		g.setPaint( background);
		g.fill(path);
		
	}

	
	protected Shape createRoundRect( double x, double y, double w, double h) {
		
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
	
	
	protected Path2D createArrow(double x, double y, double w, double h) {
		path.reset();
		
		double xcenter = x + w/2.0-2;
		double ycenter = y + h/2.0;
		
		path.moveTo(xcenter-2, ycenter);
		path.lineTo(xcenter+4, ycenter+2.5);
		path.lineTo(xcenter+4, ycenter-2.5);
		path.closePath();
		
		return path;
	}



}
