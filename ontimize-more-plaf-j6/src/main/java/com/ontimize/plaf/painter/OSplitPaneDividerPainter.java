package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;

/**
 */
public class OSplitPaneDividerPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of OSplitPaneDividerPainter to determine which region/state is being painted
	//by that instance.
	public static final int BACKGROUND_ENABLED = 1;
	public static final int BACKGROUND_FOCUSED = 2;
	public static final int FOREGROUND_ENABLED = 3;
	public static final int FOREGROUND_ENABLED_VERTICAL = 4;
	
	public static int buttonWidth = 90;
	public static int buttonHeight = 5;


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;


	//the following 4 variables are reused during the painting code of the layers
	protected Path2D path = new Path2D.Float();
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of OSplitPaneDividerPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = decodeColor("nimbusBlueGrey", 0.0f, -0.017358616f, -0.11372548f, 0);
	protected final Color color3 = decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);
	protected final Color color5 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
	protected final Color color7 = decodeColor("nimbusBlueGrey", 0.0055555105f, -0.06970999f, 0.21568626f, 0);

	
	protected Paint backgroundColorFocused;
	protected Paint backgroundColorEnabled;
	
	protected Paint foregroundColorEnabled;
	protected Paint foregroundColorEnabledVertical;
	protected Paint foregroundBorderColorEnabled;
	protected Paint foregroundBorderColorEnabledVertical;
	

	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OSplitPaneDividerPainter(int state, PaintContext ctx) {
		super();
		this.state = state;
		this.ctx = ctx;

		init();
	}



	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		//populate componentColors array with colors calculated in getExtendedCacheKeys call
		componentColors = extendedCacheKeys;

		switch(state) {
		case BACKGROUND_ENABLED: paintBackgroundEnabled(g); break;
		case BACKGROUND_FOCUSED: paintBackgroundFocused(g); break;
		case FOREGROUND_ENABLED: paintForegroundEnabled(g,width, height); break;
		case FOREGROUND_ENABLED_VERTICAL: paintForegroundEnabledAndVertical(g,width, height); break;

		}
	}



	@Override
	protected PaintContext getPaintContext() {
		return ctx;
	}

	/**
	 * Get configuration properties to be used in this painter.
	 * As usual, it is checked the OLAFCustomConfig.properties, and then in OLAFDefaultConfig.properties.
	 *
	 * Moreover, if there are not values for that properties in both, the default Nimbus configuration values are set, due to, those properties are needed to
	 * paint and used by the Ontimize L&F, so, there are not Nimbus values for that, so, default values are always configured based on Nimbus values.
	 *
	 */
	protected void init (){

		// focused:
		Object obj = UIManager.getLookAndFeelDefaults().get("SplitPane:SplitPaneDivider[Focused].background");
		if(obj instanceof Paint){
			backgroundColorFocused = (Paint)obj;
		}else{
			backgroundColorFocused = color3;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get("SplitPane:SplitPaneDivider[Enabled].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}else{
			backgroundColorEnabled = color1;
		}
		
		
		obj = UIManager.getLookAndFeelDefaults().get("SplitPane:SplitPaneDivider[Enabled].foreground");
		if(obj instanceof Paint){
			foregroundColorEnabled = (Paint)obj;
		}else{
			foregroundColorEnabled = color5;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get("SplitPane:SplitPaneDivider[Enabled+Vertical].foreground");
		if(obj instanceof Paint){
			foregroundColorEnabledVertical = (Paint)obj;
		}else{
			foregroundColorEnabledVertical = color5;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get("SplitPane:SplitPaneDivider[Enabled].foregroundBorder");
		if(obj instanceof Paint){
			foregroundBorderColorEnabled = (Paint)obj;
		}else{
			foregroundBorderColorEnabled = color7;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get("SplitPane:SplitPaneDivider[Enabled+Vertical].foregroundBorder");
		if(obj instanceof Paint){
			foregroundBorderColorEnabledVertical = (Paint)obj;
		}else{
			foregroundBorderColorEnabledVertical = color7;
		}
		
	}


	protected void paintBackgroundEnabled(Graphics2D g) {
		rect = decodeRect1();
		g.setPaint( backgroundColorEnabled );
		g.fill(rect);

	}

	protected void paintBackgroundFocused(Graphics2D g) {
		rect = decodeRect1();
		g.setPaint( backgroundColorFocused );
		g.fill(rect);

	}

	protected void paintForegroundEnabled(Graphics2D g, int width, int height) {

		double x = decodeX(1.05f);
		double y = Math.round((height/2.0) - buttonHeight/2.0) - 1;
		
		Shape s = decodeForeground(x+1, y+1, buttonWidth, buttonHeight);
		g.setPaint( foregroundBorderColorEnabled );
		g.fill(s);
		
		s = decodeForeground(x, y, buttonWidth, buttonHeight);
		g.setPaint( foregroundColorEnabled );
		g.fill(s);
		
	}

	protected void paintForegroundEnabledAndVertical(Graphics2D g,int width, int height) {
		
		double y = decodeY(1.05f);
		double x = Math.round((width/2.0) - buttonHeight/2.0) -1;
		
		Shape s = decodeForegroundVertical(x+1, y+1, buttonHeight, buttonWidth);
		g.setPaint( foregroundBorderColorEnabledVertical );
		g.fill(s);
		
		s = decodeForegroundVertical(x, y, buttonHeight, buttonWidth);
		g.setPaint(foregroundColorEnabledVertical);
		g.fill(s);

	}



	protected Rectangle2D decodeRect1() {
		rect.setRect(decodeX(1.0f), //x
				decodeY(0.0f), //y
				decodeX(2.0f) - decodeX(1.0f), //width
				decodeY(3.0f) - decodeY(0.0f)); //height
				return rect;
	}


	protected Shape decodeForeground(double x, double y, double w, double h){
		path.reset();
		
		path.moveTo(x, y);
    	Shape s = ShapeFactory.getInstace().createSemiCircle(x, y, h, ShapeFactory.ANTICLOCKWISE, true);
    	path.append(s, true);
    	path.lineTo(x+w, y+h);
    	s = ShapeFactory.getInstace().createSemiCircle(x+w, y+h, -h, ShapeFactory.ANTICLOCKWISE, true);
    	path.append(s, true);
    	path.lineTo(x, y);
    	path.closePath();
		
		return path;
	}

	protected Shape decodeForegroundVertical(double x, double y, double w, double h){
		path.reset();
		
		path.moveTo(x, y);
    	Shape s = ShapeFactory.getInstace().createSemiCircleVertical(x, y, w, ShapeFactory.CLOCKWISE, true);
    	path.append(s, true);
    	path.lineTo(x+w, y+h);
    	s = ShapeFactory.getInstace().createSemiCircleVertical(x+w, y+h, -w, ShapeFactory.CLOCKWISE, true);
    	path.append(s, true);
    	path.lineTo(x, y);
    	path.closePath();
		
		return path;
	}



}
