package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;

import com.ontimize.plaf.OntimizeLookAndFeel;


/**
 * <pre>
 * Painter for the background, border and shape of the TextField (to configure border and background colors, painters, images, ...)
 *
 * In this case, we would like to be able to configure some TextFields in a different ways that other TextFields.
 * That is, for TextField which are embedded in Ontimize FormTitle classes and so on, we must be able to configure the same properties in
 * other way.
 *
 * For that reason, this abstract class is included. It indicates how painters must be painted, and also their configuration parameters.
 * However the configuration key is got using the method getComponentKeyName(), which will be defined in the non-abstract classes, that extends this one.
 * For instance, to be able to get a configuration key called something[X].Y:
 *   - OTextFieldPainter will return "TextField" => the readed configuration key will be: TextField[X].Y
 *   - OFormTitlePainter will return "\"FormTitle\"" => the readed configuration key will be: "FormTitle\"[X].Y
 *
 * (Typically, X has values as Enabled, Focused, Pressed, ... and Y becomes values like: backgroundPainter_bgPainter, backgroundPainter_border, ...)
 *
 *
 * @author Imatia Innovation
 *
 * </pre>
 */
public abstract class AbstractOTextFieldPainter extends AbstractRegionPainter {
	
	public  static final int BACKGROUND_DISABLED = 1;
	public  static final int BACKGROUND_ENABLED = 2;
	public  static final int BACKGROUND_FOCUSED = 3;
	public  static final int BORDER_DISABLED = 4;
	public  static final int BORDER_FOCUSED = 5;
	public  static final int BORDER_ENABLED = 6;

	/**
	 * The number of pixels that compounds the border width of the component.
	 */
	public static int numBorders = 4;
	
	protected int state; //refers to one of the static final ints above
	protected PaintContext ctx;

	//the following 4 variables are reused during the painting code of the layers
	protected Path2D path = new Path2D.Float();
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

	// painters to fill the component
	protected Paint backgroundColorDisabled;
	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorFocused;

	// arrays to round the component (several rounded borders with degradation):
	protected Paint[] degradatedBorderColorEnabled;
	protected Paint[] degradatedBorderColorDisabled;
	protected Paint[] degradatedBorderColorFocused;

	protected Paint focusInnerShadow;
	
	protected Color[] degradatedEditorInnerBorderColor;

	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of TextFieldPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected Color color1 = decodeColor("nimbusBlueGrey", -0.015872955f, -0.07995863f, 0.15294117f, 0);
	protected Color color2 = decodeColor("nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
	protected Color color3 = decodeColor("nimbusBlueGrey", -0.006944418f, -0.07187897f, 0.06666666f, 0);
	protected Color color4 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07826825f, 0.10588235f, 0);
	protected Color color7 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.0965403f, -0.18431371f, 0);
	protected Color color8 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.1048766f, -0.05098039f, 0);
	protected Color color9 = decodeColor("nimbusLightBackground", 0.6666667f, 0.004901961f, -0.19999999f, 0);


	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;
	
	protected double radius;
	
	protected java.awt.Image paddLock = null;
	
	public static String imgUrl = "com/ontimize/plaf/images/paddlock.png";

	public AbstractOTextFieldPainter(int state, PaintContext ctx) {
		super();
		this.state = state;
		this.ctx = ctx;

		init();
		
		if (imgUrl != null) {
			URL url = getClass().getClassLoader().getResource(imgUrl);
			paddLock = Toolkit.getDefaultToolkit().getImage(url);
		}
	}


	protected abstract String getComponentKeyName();


	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		//populate componentColors array with colors calculated in getExtendedCacheKeys call
		componentColors = extendedCacheKeys;
		Insets insets = c.getInsets();
		int x = insets.left;
		int y = insets.top;
		int cwidth = width - insets.left - insets.right;
		int cheight = height - insets.top - insets.bottom;
		
		//generate this entire method. Each state/bg/fg/border combo that has
		//been painted gets its own KEY and paint method.
		switch(state) {
		case BACKGROUND_DISABLED: paintBackgroundDisabled(g,c,x,y,cwidth,cheight); break;
		case BACKGROUND_ENABLED: paintBackgroundEnabled(g,c,x,y,cwidth,cheight); break;
		case BACKGROUND_FOCUSED: paintBackgroundFocused(g,c,x,y,cwidth,cheight); break;
		case BORDER_DISABLED: paintBorderDisabled(g,c,0,0,width,height); break;
		case BORDER_FOCUSED: paintBorderFocused(g,c,0,0,width,height); break;
		case BORDER_ENABLED: paintBorderEnabled(g,c,0,0,width,height); break;

		}
	}



	@Override
	protected Object[] getExtendedCacheKeys(JComponent c) {
		Object[] extendedCacheKeys = null;
		switch(state) {
		case BACKGROUND_ENABLED:
			extendedCacheKeys = new Object[] {
					getComponentColor(c, "background", color2, 0.0f, 0.0f, 0)};
			break;
		case BORDER_FOCUSED:
			extendedCacheKeys = new Object[] {
					getComponentColor(c, "background", color9, 0.004901961f, -0.19999999f, 0),
					getComponentColor(c, "background", color2, 0.0f, 0.0f, 0)};
			break;
		case BORDER_ENABLED:
			extendedCacheKeys = new Object[] {
					getComponentColor(c, "background", color9, 0.004901961f, -0.19999999f, 0),
					getComponentColor(c, "background", color2, 0.0f, 0.0f, 0)};
			break;
		}
		return extendedCacheKeys;
	}



	@Override
	protected PaintContext getPaintContext() {
		return ctx;
	}


	/**
	 * Get configuration properties to be used in this painter.
	 *
	 */
	protected void init (){
		
		Object oRadius = UIManager.getLookAndFeelDefaults().get("Application.radius");
		if(oRadius instanceof Double){
			radius = ((Double)oRadius).doubleValue();
		}else{
			radius = OntimizeLookAndFeel.defaultRadius;
		}

		// disable:
		Object obj = UIManager.getLookAndFeelDefaults().get(getComponentKeyName() + "[Disabled].background");
		if(obj instanceof Paint){
			backgroundColorDisabled = (Paint)obj;
		}else{
			backgroundColorDisabled = color1;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Enabled].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}else{
			backgroundColorEnabled = color2;
		}

		// focused:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Focused].background");
		if(obj instanceof Paint){
			backgroundColorFocused = (Paint)obj;
		}else{
			backgroundColorFocused = color2;
		}


		// BORDER COLORS
		// enable:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Enabled].border");
		if(obj instanceof Paint){
			degradatedBorderColorEnabled = new Paint[]{(Paint)obj};
		}else if(obj instanceof Paint[]){
			degradatedBorderColorEnabled = (Paint[])obj;
		}else{
			degradatedBorderColorEnabled = new Color[] { color7, decodeColor(color7,color8,0.5f), color8};
		}
		
		// disable:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Disabled].border");
		if(obj instanceof Paint){
			degradatedBorderColorDisabled = new Paint[]{(Paint)obj};
		}else if(obj instanceof Paint[]){
			degradatedBorderColorDisabled = (Paint[])obj;
		}else{
			degradatedBorderColorDisabled = new Color[] { color3, decodeColor(color3,color4,0.5f), color4};
		}

		//Focused:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Focused].border");
		if(obj instanceof Paint){
			degradatedBorderColorFocused = new Paint[]{(Paint)obj};
		}else if(obj instanceof Paint[]){
			degradatedBorderColorFocused = (Paint[])obj;
		}else{
			degradatedBorderColorFocused = new Color[] { color7, decodeColor(color7,color8,0.5f), color8};
		}
		

		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Focused].innerShadow");
		if(obj instanceof Paint){
			focusInnerShadow = (Paint)obj;
		}else{
			focusInnerShadow = (Paint)new Color(0xCACACA);
		}
		
		//Editor (inner border):
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + ".editorInnerBorder");
		if(obj instanceof Color){
			degradatedEditorInnerBorderColor = new Color[]{(Color)obj, (Color)obj};
		}else if(obj instanceof Color[] && ((Color[])obj).length==2 ){
			degradatedEditorInnerBorderColor = (Color[])obj;
		}else{
			degradatedEditorInnerBorderColor = new Color[] { new Color(0xBAf1FE), new Color(0xF3FdFF)};
		}
		
	}


	protected void paintBackgroundDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {

		drawBackground(g, c, x, y, width, height, backgroundColorDisabled);
	}

	protected void paintBackgroundEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {

		if (c.getParent() instanceof JTable) {
			drawEditorBackground(g, c);
		} else {
			drawBackground(g, c, x, y, width, height, backgroundColorEnabled);
		}

	}

	protected void paintBackgroundFocused(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		
		drawBackground(g, c, x, y, width, height, backgroundColorFocused);
	}
	
	protected Paint getBackgroundForState(JComponent c){
		Paint paint = null;
		switch (state) {
			case BACKGROUND_DISABLED: paint = getBackgroundColor(c, backgroundColorDisabled); break;
			case BACKGROUND_ENABLED: paint = getBackgroundColor(c, backgroundColorEnabled); break;
			case BACKGROUND_FOCUSED: paint = getBackgroundColor(c, backgroundColorFocused); break;
			case BORDER_DISABLED: paint = getBackgroundColor(c, backgroundColorDisabled); break;
			case BORDER_ENABLED: paint = getBackgroundColor(c, backgroundColorEnabled); break;
			case BORDER_FOCUSED: paint = getBackgroundColor(c, backgroundColorFocused); break;
			default: paint = c!=null ? c.getBackground() : null;
		}
		
		return paint;
	}

	protected Paint getBackgroundColor(JComponent c, Paint defaultColor){
		if(c!=null){
			if(c.getBackground()!= null && c.isEnabled()){
				return c.getBackground();
			}
		}
		return defaultColor;
	}
	
	protected Paint getBorderForState(JComponent c){
		Paint paint = null;
		switch (state) {
			case BORDER_DISABLED: paint = degradatedBorderColorDisabled[0]; break;
			case BORDER_ENABLED: paint = degradatedBorderColorEnabled[0]; break;
			case BORDER_FOCUSED: paint = degradatedBorderColorFocused[0]; break;
			default: paint = degradatedBorderColorEnabled[0];
		}
		return paint;
	}



	protected void paintBorderDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		if (degradatedBorderColorDisabled != null && degradatedBorderColorDisabled.length > 0) {
			drawDegradatedBorders(g, c, x, y, width, height, degradatedBorderColorDisabled);
			g.drawImage(paddLock, 1, (int) (decodeY(2.0f) - 9), 10, 10, null);
		}
	}



	protected void paintBorderFocused(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		if (degradatedBorderColorFocused != null && degradatedBorderColorFocused.length > 0) {
			drawDegradatedBorders(g, c, x, y, width, height, degradatedBorderColorFocused);
			drawInnerShadowBorder(g, focusInnerShadow);
		}
	}

	protected void paintBorderEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		if (degradatedBorderColorEnabled != null && degradatedBorderColorEnabled.length > 0) {
			drawDegradatedBorders(g, c, x, y, width, height, degradatedBorderColorEnabled);
		}
	}

	protected Rectangle2D decodeRect() {
		rect.setRect(decodeX(0.0f), //x
				decodeY(0.0f), //y
				decodeX(3.0f) - decodeX(0.0f), //width
				decodeY(3.0f) - decodeY(0.0f)); //height
				return rect;
	}
	
	protected Path2D decodeTopInnerShadowRect() {
		path.reset();
		path.moveTo(decodeX(0.0f), decodeY(0.0f));
		path.lineTo(decodeX(3.0f), decodeY(0.0f));
		path.lineTo(decodeX(3.0f)-4, decodeY(0.0f)+4);
		path.lineTo(decodeX(0.0f)+4, decodeY(0.0f)+4);
		path.closePath();
		return path;
	}
	
	protected Path2D decodeBottomInnerShadowRect() {
		path.reset();
		path.moveTo(decodeX(0.0f), decodeY(3.0f)-1);
		path.lineTo(decodeX(3.0f), decodeY(3.0f)-1);
		path.lineTo(decodeX(3.0f)-4, decodeY(3.0f)-5);
		path.lineTo(decodeX(0.0f)+4, decodeY(3.0f)-5);
		path.closePath();
		return path;
	}
	
	protected Path2D decodeLeftInnerShadowRect() {
		path.reset();
		path.moveTo(decodeX(0.0f), decodeY(0.0f));
		path.lineTo(decodeX(0.0f) + 4, decodeY(0.0f) + 4);
		path.lineTo(decodeX(0.0f) + 4, decodeY(3.0f) - 4);
		path.lineTo(decodeX(0.0f), decodeY(3.0f));
		path.closePath();
		return path;
	}
	
	protected Path2D decodeRightInnerShadowRect() {
		path.reset();
		path.moveTo(decodeX(3.0f) - 1, decodeY(0.0f));
		path.lineTo(decodeX(3.0f) - 1, decodeY(3.0f));
		path.lineTo(decodeX(3.0f) - 5, decodeY(3.0f) - 4);
		path.lineTo(decodeX(3.0f) - 5, decodeY(0.0f) + 4);
		path.closePath();
		return path;
	}
	
	
	
	protected void drawBackground(Graphics2D g, JComponent c, int x, int y, int width, int height, Paint color){
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		double xx = getLeftX();
		double xx2 = getRightX();
		double yy = decodeY(0.0f)+numBorders;
		double w = xx2-xx;
        double h = decodeY(3.0f) - 2*numBorders;
		
		g.setPaint(getBackgroundForState(c));
		g.fillRect(intValue(xx), intValue(yy), intValue(w), intValue(h));
		
		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
	}
	
	protected void drawEditorBackground(Graphics2D g, JComponent c){
		rect = decodeRect();
		g.setPaint(backgroundColorEnabled);
		g.fill(rect);
		
		Shape s = decodeLeftInnerShadowRect();
		g.setPaint( decodeLeftInnerShadowGradient(s) );
		g.fill(s);
		
		s = decodeRightInnerShadowRect();
		g.setPaint( decodeRightInnerShadowGradient(s) );
		g.fill(s);
		
		s = decodeTopInnerShadowRect();
		g.setPaint( decodeTopInnerShadowGradient(s) );
		g.fill(s);
		
		s = decodeBottomInnerShadowRect();
		g.setPaint( decodeBottomInnerShadowGradient(s) );
		g.fill(s);
	}
	
	/**
	 * This method returns, if it is configured by the user, the value for corner radius.
	 * @return
	 */
	protected double getRadius(){
		return radius;
	}
	
	/**
	 * This method returns the maximum radius available for the component. The maximum radius is calculated from the height of the component
	 * and it provokes that the field corners looks like a semicircle. 
	 * @return
	 */
	protected double getMaximumRadius(){
		return  (decodeY(3.0f)-1 - 2*numBorders)/2.0;
	}
	
	/**
	 * Returns the x coordinate where starts the semicircle of the left of the component.
	 * @return
	 */
	protected double getLeftX(){
		double d = decodeX(0.0f) + getMaximumRadius() + numBorders;
		d = Math.round(d);
		return d;
	}
	
	/**
	 * Returns the x coordinate where starts the semicircle of the right of the component.
	 * @return
	 */
	protected double getRightX(){
		double d = decodeX(3.0f) - getMaximumRadius() - numBorders;
		d = Math.round(d);
		return d;
	}
	
	
	protected void drawDegradatedBorders(Graphics2D g, JComponent c, int x, int y, int width, int height, Paint[] colors){
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		//TODO solve setOpaque into TextDataField of Ontimize...
//		if(c.isOpaque()) {
			defineBorderFillers(g, c, x, y,width,height);
//		}
		
		double xx = getLeftX();
		double yy = decodeY(0.0f)+numBorders;
		double h = height - 2*numBorders;
		double w = getRightX();
		
		for (int i = 1; i <= colors.length ; i++) {
			double yyy = yy - i;
			double hh = h + 2*i-1;
			Shape s = decodeBorderPath(xx, yyy, w, hh, i-1);
			g.setPaint(colors[i-1]);
			g.draw(s);
		}

		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
		
	}
	
	protected void drawInnerShadowBorder(Graphics2D g, Paint color){
		
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		double radius = getRadius();
		double x = decodeX(0.0f) + getMaximumRadius() + numBorders-2;
		double x_arc = decodeX(0.0f)+numBorders + radius-2;
		if(x_arc<x){
			x = x_arc;
		}
		double x2 = decodeX(3.0f)-numBorders-getMaximumRadius()+2;
		x_arc = decodeX(3.0f)-numBorders-radius+2;
		if(x_arc>x2){
			x2 = x_arc;
		}
		double y2 = decodeY(0.0f)+numBorders;
		path.reset();
		path.moveTo(x, y2);
		path.lineTo(x2, y2);
		
		g.setPaint(color);
		g.draw(path);
		
		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
	}
	
	protected void defineBorderFillers(Graphics2D g, JComponent c, int x, int y, int width, int height){
		
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		Paint filler = getBackgroundForState(c);
		g.setPaint(filler);
		
		//Left filler...
		Shape s = decodeLeftBorderFillerPath(height);
		g.fill(s);
		
		//Right filler...
		s = decodeRightBorderFillerPath(height);
		g.fill(s);
		
		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
	}
	
	protected Shape decodeLeftBorderFillerPath(int height){
		double x = getLeftX();
		double radius = getRadius();
		double x_arc = decodeX(0.0f)+numBorders + radius;
		if(x_arc>x){
			x_arc = x;
			radius = getMaximumRadius();
		}
		double y = decodeY(0.0f)+numBorders;
		double h = height - 2*numBorders;
		
		path.reset();
		path.moveTo(intValue(x), y);
		path.lineTo(x_arc, y);
		path.curveTo(x_arc - radius/2.0, y, x_arc -radius, y + radius/ 2.0, x_arc -radius, y+ radius);
		path.lineTo(x_arc-radius, y + h-radius);
		path.curveTo(x_arc - radius,y+ h- radius/2.0, x_arc-radius/2.0, y+h, x_arc, y+h);
		path.lineTo(intValue(x), y+h);
		path.closePath();
		
		return path;
	}
	
	
	protected Shape decodeRightBorderFillerPath(int height){
		
		double x = getRightX();
		double radius = getRadius();
		double x_arc = decodeX(3.0f)-numBorders-radius;
		if(x_arc<x){
			x_arc = x;
			radius = getMaximumRadius();
		}
		double y = decodeY(0.0f)+numBorders;
		double h = height - 2*numBorders;
		
		path.reset();
		path.moveTo(intValue(x), y);
		path.lineTo(x_arc, y);
		path.curveTo(x_arc+radius/2.0,y, x_arc+radius, y+radius/2.0, x_arc+radius, y+radius);
		path.lineTo(x_arc+radius, y+h-radius);
		path.curveTo(x_arc+radius,y+h-radius/2.0, x_arc+radius/2.0, y+h, x_arc, y+h);
		path.lineTo(intValue(x), y+h);
		path.closePath();
		
		return path;
	}
	
	/**
	 * Decodes the border component path.
	 * @param x The x coordinate where starts the semicircle of the left
	 * @param y The y coordinate of the top.
	 * @param w The x coordinate where starts the semicircle of the right.
	 * @param h The height of the field.
	 * @param borderIndex the index of the border.
	 * @return
	 */
	protected Shape decodeBorderPath(double x, double y, double w, double h, int borderIndex){
		double radius = getRadius();
		double x_arc = decodeX(0.0f)+ numBorders + radius;
		if(x_arc>x){
			x_arc = x;
			radius = getMaximumRadius();
		}
		
		path.reset();
		path.moveTo(intValue(x), y);
		path.lineTo(x_arc, y);
		path.curveTo(x_arc - radius/2.0, y, x_arc -radius, y + radius/ 2.0, x_arc -radius-borderIndex, y+ radius);
		path.lineTo(x_arc-radius-borderIndex, y + h-radius);
		path.curveTo(x_arc - radius -borderIndex,y+ h- radius/2.0, x_arc-radius/2.0, y+h, x_arc, y+h);
		path.lineTo(intValue(x), y+h);
		
		path.lineTo(intValue(w), y+h);
		
		x_arc = decodeX(3.0f)-1-numBorders-radius;
		if(x_arc<w){
			x_arc = w;
			radius = getMaximumRadius();
		}
		
		path.lineTo(x_arc, y+h);
		path.curveTo(x_arc+radius/2.0,y+h, x_arc+radius, y+h-radius/2.0, x_arc+radius+borderIndex, y+h-radius);
		path.lineTo(x_arc+radius+borderIndex, y+radius);
		path.curveTo(x_arc+radius+borderIndex,y+radius/2.0, x_arc+radius/2.0, y, x_arc, y);
		
		path.lineTo(intValue(x), y);
		path.closePath();
		
		
    	return path;
    }
	
	protected Paint decodeTopInnerShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float h = (float)bounds.getHeight();
        return decodeGradient(x, y, x, y+h,
                new float[] { 0.0f, 1.0f },
                new Color[] { degradatedEditorInnerBorderColor[0],
        						degradatedEditorInnerBorderColor[1]});
    }
	
	protected Paint decodeBottomInnerShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float h = (float)bounds.getHeight();
        return decodeGradient(x, y, x, y+h,
                new float[] { 0.0f, 1.0f },
                new Color[] {  degradatedEditorInnerBorderColor[1],
        						degradatedEditorInnerBorderColor[0]});
    }
	
	protected Paint decodeLeftInnerShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float w = (float)bounds.getWidth();
        return decodeGradient(x, y, x + w, y,
                new float[] { 0.0f, 1.0f },
                new Color[] { degradatedEditorInnerBorderColor[0],
        						degradatedEditorInnerBorderColor[1]});
    }
	
	protected Paint decodeRightInnerShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float w = (float)bounds.getWidth();
        return decodeGradient(x, y, x + w, y,
                new float[] { 0.0f, 1.0f },
                new Color[] { degradatedEditorInnerBorderColor[1],
        						degradatedEditorInnerBorderColor[0]});
    }
	
	
}
