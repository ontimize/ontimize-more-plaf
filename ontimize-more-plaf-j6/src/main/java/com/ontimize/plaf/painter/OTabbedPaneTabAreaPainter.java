package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;


/**
 * Painter for the background of each tabArea in a TabbedPane (border color, and/or image, texture or gradient painter as background, and so on ...)
 *
 * @author Imatia Innovation
 *
 */
public class OTabbedPaneTabAreaPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of TabbedPaneTabbedPaneTabAreaPainter to determine which region/state is being painted
	//by that instance.
	public static final int BACKGROUND_ENABLED = 1;
	public static final int BACKGROUND_DISABLED = 2;
	public  static final int BACKGROUND_ENABLED_MOUSEOVER = 3;
	public static final int BACKGROUND_ENABLED_PRESSED = 4;


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;

	//the following 4 variables are reused during the painting code of the layers
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
	protected Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);

	float[] fractions = new float[] { 0.08387097f,0.09677419f,0.10967742f,0.43709677f,0.7645161f,0.7758064f,0.7870968f };


	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of TabbedPaneTabbedPaneTabAreaPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = new Color(255, 200, 0, 255);
	protected final Color color2 = new Color(214, 217, 223, 255);


	protected Paint borderColorDisabled;
	protected Paint borderColorEnabled;
	protected Paint backgroundColorDisabled;
	protected Paint backgroundColorEnabled;


	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OTabbedPaneTabAreaPainter(int state, PaintContext ctx) {
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
		case BACKGROUND_ENABLED: paintBackgroundEnabled(g, c); break;
		case BACKGROUND_DISABLED: paintBackgroundDisabled(g, c); break;

		}
	}

	@Override
	protected PaintContext getPaintContext() {
		return ctx;
	}

	/**
	 * Get configuration properties to be used in this painter (such as: *BorderPainter and *BgPainter).
	 * As usual, it is checked the OLAFCustomConfig.properties, and then in OLAFDefaultConfig.properties.
	 *
	 * Moreover, if there are not values for that properties in both, the default Nimbus configuration values are set, due to, those properties are needed to
	 * paint and used by the Ontimize L&F, so, there are not Nimbus values for that, so, default values are always configured based on Nimbus values.
	 *
	 */
	protected void init (){

		// border enable:
		Object obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane[Enabled].border");
		if(obj instanceof Paint){
			borderColorEnabled = (Paint)obj;
		}else{
			borderColorEnabled = Color.black;
		}
		
		// border disable:
		obj = UIManager.getLookAndFeelDefaults().get("TabbedPane[Disabled].border");
		if(obj instanceof Paint){
			borderColorDisabled = (Paint)obj;
		}else{
			borderColorDisabled = color1;
		}
		
		
		// disable:
		obj = UIManager.getLookAndFeelDefaults().get("TabbedPane:TabbedPaneContent[Disabled].background");
		if(obj instanceof Paint){
			backgroundColorDisabled = (Paint)obj;
		}else{
			backgroundColorDisabled = color1;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane:TabbedPaneContent[Enabled].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}else{
			backgroundColorEnabled = color2;
		}

	}


	protected void paintBackgroundEnabled(Graphics2D g, JComponent c) {
		paintBackground(g, c, borderColorEnabled, backgroundColorEnabled);
	}

	protected void paintBackgroundDisabled(Graphics2D g, JComponent c) {
		paintBackground(g, c, borderColorDisabled, backgroundColorDisabled);
	}

	
	protected void paintBackground(Graphics2D g, JComponent c, Paint borderColor, Paint bgColor) {

		if (c instanceof JTabbedPane) {
			JTabbedPane tabPane = (JTabbedPane)c;
			double y = Math.round((decodeY(2.0f) - 1) / 2.0);

			// Painting filler under tabs if it is necessary...
			Color paintColor = getBackgroundColor(c, (Color)bgColor); 
			if ( paintColor != null) {
				double xx = 0.0;
				double yy = 0.0;
				double ww = 0.0;
				double hh = 0.0;
				
				if(JTabbedPane.TOP==tabPane.getTabPlacement()){
					xx = decodeX(0.0f);
					yy = y + OTabbedPanePainter.borderWidth;
					ww = decodeX(3.0f);
					hh = decodeY(3.0f)-yy;
				}else if(JTabbedPane.BOTTOM==tabPane.getTabPlacement()){
					xx = decodeX(0.0f);
					yy = decodeY(0.0f);
					ww = decodeX(3.0f);
					hh = yy + Math.round((decodeY(2.0f) - 1) / 2.0) - OTabbedPanePainter.borderWidth;
				}else if(JTabbedPane.LEFT==tabPane.getTabPlacement()){
					xx = decodeX(0.0f);
					yy = y + OTabbedPanePainter.borderWidth;
					ww = decodeX(3.0f);
					hh = decodeY(3.0f)-yy;
				}else if(JTabbedPane.RIGHT==tabPane.getTabPlacement()){
					xx = decodeX(0.0f);
					yy = y + OTabbedPanePainter.borderWidth;
					ww = decodeX(3.0f);
					hh = decodeY(3.0f)-yy;
				}
				
				
				Shape filler = ShapeFactory.getInstace().createRectangle(xx, yy, ww, hh);
				g.setPaint(paintColor);
				g.fill(filler);
			}

			// Painting degraded line.
			double x = decodeX(1.0f);
			if(JTabbedPane.BOTTOM==tabPane.getTabPlacement()){
				y= y - OTabbedPanePainter.borderWidth;
			}
			Shape degradedLine = createRectangle(x, y,
					decodeX(2.0f) - decodeX(1.0f), OTabbedPanePainter.borderWidth);
			g.setPaint(decodeLineGradient(c, degradedLine, (Color)borderColor));
			g.fill(degradedLine);
		}

	}
	

	protected Color getBackgroundColor(JComponent c, Color defaultLFColor ){
		if(c instanceof JTabbedPane){
			JTabbedPane tabPane = (JTabbedPane)c;
			Component comp = tabPane.getSelectedComponent();
			if(comp != null && comp.isOpaque()){
				 return comp.getBackground();
			}
			if(c.isOpaque()){
				if(c.getBackground()!=null){
					return c.getBackground();
				}
				//By default return LookAndFeel configured color.
				return defaultLFColor;
			}
		}
		return null;
	}

	protected Rectangle2D decodeRect2() {
		rect.setRect(decodeX(0.0f), //x
				decodeY(2.1666667f), //y
				decodeX(3.0f) - decodeX(0.0f), //width
				decodeY(3.0f) - decodeY(2.1666667f)); //height
		return rect;
	}
	
	protected Shape createRectangle(double x, double y, double w, double h) {
    	path.reset();
		
		//left top corner.
		path.moveTo(x , y);
		path.lineTo(x, y+h);

		//left bottom corner
		path.lineTo(x+w, y+h);
		
		//right bottom corner
		path.lineTo(x+w, y);
		
		//right top corner
		path.lineTo(x, y);
		
		return path;
	}


	protected Paint decodeLineGradient(JComponent c, Shape s, Color color) {
		Rectangle2D bounds = s.getBounds2D();
		float x = (float)bounds.getX();
		float y = (float)bounds.getY();
		float h = (float)bounds.getHeight();
		
		Color[] colors = new Color[] { OntimizeLAFColorUtils.setAlpha(color, OTabbedPanePainter.maxAlphaBorderColor),
        		OntimizeLAFColorUtils.setAlpha(color, 0.095)};
		
		float initY = y;
		float endY = y+h-1;
		if(c instanceof JTabbedPane){
			int placement = ((JTabbedPane)c).getTabPlacement();
			if(JTabbedPane.BOTTOM==placement){
				initY = y+h-1;
				endY = y;
			}
		}
		return decodeGradient( x, initY, x, endY, new float[] { 0.0f, 1.0f }, colors);
	}


}