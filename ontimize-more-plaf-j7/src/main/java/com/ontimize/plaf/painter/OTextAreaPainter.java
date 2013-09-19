package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;


/**
 * This class indicates the component name for the component that must be painted. Paint instructions, indications, ... are encoded in the class that this
 * class extends (named {@link AbstractOTextFieldPainter} )
 *
 * This kind of extensions and final and abstract painters were developed to allow users to have several componentes which are painted in the same way, and
 * configured by the same set of properties, but having only a common code to paint (the class that this one extends).
 *
 * @author Imatia Innovation
 *
 */
public class OTextAreaPainter extends AbstractRegionPainter{
    //package protected integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of TextAreaPainter to determine which region/state is being painted
    //by that instance.
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_ENABLED = 2;
    public static final int BACKGROUND_DISABLED_NOTINSCROLLPANE = 3;
    public static final int BACKGROUND_ENABLED_NOTINSCROLLPANE = 4;
    public static final int BACKGROUND_FOCUSED = 5;
    public static final int BORDER_DISABLED = 6;
    public static final int BORDER_FOCUSED = 7;
    public static final int BORDER_ENABLED = 8;
    public static final int BORDER_DISABLED_NOTINSCROLLPANE = 9;
    public static final int BORDER_FOCUSED_NOTINSCROLLPANE = 10;
    public static final int BORDER_ENABLED_NOTINSCROLLPANE = 11;


    protected int state; //refers to one of the static ints above
    protected PaintContext ctx;
    
    //the following 4 variables are reused during the painting code of the layers
    protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);
    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
    
    //painters to fill the component
	protected Paint backgroundColorDisabled;
	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorFocused;
	
	// arrays to round the component (several rounded borders with degradation):
	protected Paint[] degradatedBorderColorEnabled;
	protected Paint[] degradatedBorderColorDisabled;
	protected Paint[] degradatedBorderColorFocused;

    //All Colors used for painting are stored here. Ideally, only those colors being used
    //by a particular instance of TextAreaPainter would be created. For the moment at least,
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

    public OTextAreaPainter( int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx = ctx;
        
        init();
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        //populate componentColors array with colors calculated in getExtendedCacheKeys call
        componentColors = extendedCacheKeys;
        
        Insets insets = c.getInsets();
        int x = insets.left;
		int y = insets.top;
		int cwidth = width - insets.left - insets.right;
		int cheight = height - insets.top - insets.bottom;
		
//		if(c.isOpaque()){
	        switch(state) {
	            case BACKGROUND_DISABLED: paintBackgroundDisabled(g,c,x,y,cwidth,cheight); break;
	            case BACKGROUND_ENABLED: paintBackgroundEnabled(g,c,x,y,cwidth,cheight); break;
	            case BACKGROUND_DISABLED_NOTINSCROLLPANE: paintBackgroundDisabledAndNotInScrollPane(g,c,x,y,cwidth,cheight); break;
	            case BACKGROUND_ENABLED_NOTINSCROLLPANE: paintBackgroundEnabledAndNotInScrollPane(g,c,x,y,cwidth,cheight); break;
	            case BACKGROUND_FOCUSED: paintBackgroundFocused(g,c,x,y,cwidth,cheight); break;
	        }
//		}
			switch(state) {
			 	case BORDER_DISABLED: paintBorderDisabledInScrollPane(g,c,0,0); break;
	            case BORDER_FOCUSED: paintBorderFocusedInScrollPane(g,c,0,0); break;
	            case BORDER_ENABLED: paintBorderEnabledInScrollPane(g,c,0,0); break;
	            case BORDER_DISABLED_NOTINSCROLLPANE: paintBorderDisabledAndNotInScrollPane(g,c,0,0,width,height); break;
	            case BORDER_FOCUSED_NOTINSCROLLPANE: paintBorderFocusedAndNotInScrollPane(g,c,0,0,width,height); break;
	            case BORDER_ENABLED_NOTINSCROLLPANE: paintBorderEnabledAndNotInScrollPane(g,c,0,0,width,height); break;
			}
    }
        
    protected Object[] getExtendedCacheKeys(JComponent c) {
        Object[] extendedCacheKeys = null;
        switch(state) {
            case BACKGROUND_ENABLED:
                extendedCacheKeys = new Object[] {
                     getComponentColor(c, "background", color2, 0.0f, 0.0f, 0)};
                break;
            case BACKGROUND_ENABLED_NOTINSCROLLPANE:
                extendedCacheKeys = new Object[] {
                     getComponentColor(c, "background", color2, 0.0f, 0.0f, 0)};
                break;
            case BORDER_FOCUSED_NOTINSCROLLPANE:
                extendedCacheKeys = new Object[] {
                     getComponentColor(c, "background", color9, 0.004901961f, -0.19999999f, 0),
                     getComponentColor(c, "background", color2, 0.0f, 0.0f, 0)};
                break;
            case BORDER_ENABLED_NOTINSCROLLPANE:
                extendedCacheKeys = new Object[] {
                     getComponentColor(c, "background", color9, 0.004901961f, -0.19999999f, 0),
                     getComponentColor(c, "background", color2, 0.0f, 0.0f, 0)};
                break;
        }
        return extendedCacheKeys;
    }
    
    protected String getComponentKeyName() {
		return "TextArea";
	}
    
    protected void init (){

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
		// disable:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Focused].border");
		if(obj instanceof Paint){
			degradatedBorderColorFocused = new Paint[]{(Paint)obj};
		}else if(obj instanceof Paint[]){
			degradatedBorderColorFocused = (Paint[])obj;
		}else{
			degradatedBorderColorFocused = new Color[] { color7, decodeColor(color7,color8,0.5f), color8};
		}
		

    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

	protected void paintBackgroundDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		drawBackground(g, c, x, y, width, height, backgroundColorDisabled);
	}

	protected void paintBackgroundEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		drawBackground(g, c, x, y, width, height, backgroundColorEnabled);

	}
    
	protected void paintBackgroundFocused(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		drawBackground(g, c, x, y, width, height, backgroundColorFocused);
	}

	protected void paintBackgroundDisabledAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y,
			int width, int height) {
		drawBackground(g, c, x, y, width, height, backgroundColorDisabled);
	}

	protected void paintBackgroundEnabledAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y,
			int width, int height) {
		drawBackground(g, c, x, y, width, height, backgroundColorEnabled);
	}

	protected void paintBorderDisabledAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width,
			int height) {
		if (degradatedBorderColorDisabled != null && degradatedBorderColorDisabled.length > 0) {
			drawDegradatedBorders(g, c, x, y, width, height, degradatedBorderColorDisabled);
		}
	}
	
	protected void paintBorderDisabledInScrollPane(Graphics2D g, JComponent c, int x, int y) {
		if (degradatedBorderColorDisabled != null && degradatedBorderColorDisabled.length > 0) {
			drawDegradatedBordersInScrollPane(g, c, x, y, degradatedBorderColorDisabled);
		}
	}

	protected void paintBorderFocusedAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width,
			int height) {
		if (degradatedBorderColorFocused != null && degradatedBorderColorFocused.length > 0) {
			drawDegradatedBorders(g, c, x, y, width, height, degradatedBorderColorFocused);
		}
	}
	
	protected void paintBorderFocusedInScrollPane(Graphics2D g, JComponent c, int x, int y) {
		if (degradatedBorderColorFocused != null && degradatedBorderColorFocused.length > 0) {
			drawDegradatedBordersInScrollPane(g, c, x, y, degradatedBorderColorFocused);
		}
	}

	protected void paintBorderEnabledAndNotInScrollPane(Graphics2D g, JComponent c, int x, int y, int width,
			int height) {
		if (degradatedBorderColorEnabled != null && degradatedBorderColorEnabled.length > 0) {
			drawDegradatedBorders(g, c, x, y, width, height, degradatedBorderColorEnabled);
		}
	}
    
	protected void paintBorderEnabledInScrollPane(Graphics2D g, JComponent c, int x, int y) {
		if (degradatedBorderColorEnabled != null && degradatedBorderColorEnabled.length > 0) {
			drawDegradatedBordersInScrollPane(g, c, x, y, degradatedBorderColorEnabled);
		}
	}
    
    
    protected void drawBackground(Graphics2D g, JComponent c, int x, int y, int width, int height, Paint color){
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		Shape s = ShapeFactory.getInstace().createRectangle( x-1, y, width+2, height);
		g.setPaint( getBackgroundColor(c, s, color));
		g.fill(s);
		
		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
	}
    
    protected Paint getBackgroundColor(JComponent c, Shape s, Paint defaultColor){
		if(c!=null){
			if(c.getBackground()!= null){
				return c.getBackground();
			}
		}
		return defaultColor;
	}
    
    
    protected void drawDegradatedBorders(Graphics2D g, JComponent c, int x, int y, int width, int height, Paint[] colors){
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);

//		if(c.isOpaque()) {
			defineBorderFillers(g, c, x, y);
//		}
		
		for (int i = 0; i < colors.length ; i++) {
			double xx = x + 2 - i;
			double yy = y + 2 - i;
			double w = width + (2*i) - 5;
			double h = height + (2*i) - 5;
			Shape s = createRoundRect( xx, yy, w, h,c.getInsets());
			g.setPaint(colors[i]);
			g.draw(s);
		}

		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
		
	}
    
    protected void drawDegradatedBordersInScrollPane(Graphics2D g, JComponent c, int x, int y, Paint[] colors) {

//		if (c.isOpaque()) {
			Paint previousPaint = g.getPaint();
			RenderingHints rh = g.getRenderingHints();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
			
			defineBorderFillersInScrollPane(g, c, x, y);

			g.setPaint(previousPaint);
			g.setRenderingHints(rh);
//		}
	}
    
    
    protected void defineBorderFillers(Graphics2D g, JComponent c, int x, int y){
    	Paint previousPaint = g.getPaint();

    	Rectangle bounds = c.getBounds();
    	Insets insets = c.getInsets();
    	int numBorders = 4;
    	
    	//Drawing rounded corners...
    	Shape s = ShapeFactory.getInstace().createRoundCorner( x+numBorders, y+numBorders, c.getInsets().left-numBorders, c.getInsets().top-numBorders, ShapeFactory.TOP_LEFT_CORNER, false);
		g.setPaint(getPaintForFiller(c, s));
    	g.fill(s);
    	
    	s = ShapeFactory.getInstace().createRoundCorner( c.getBounds().width-numBorders, y+numBorders, c.getInsets().right-numBorders, c.getInsets().top-numBorders, ShapeFactory.TOP_RIGHT_CORNER, false);
    	g.fill(s);
    	
    	s = ShapeFactory.getInstace().createRoundCorner( x+numBorders, c.getBounds().height-numBorders, c.getInsets().left-numBorders, c.getInsets().bottom-numBorders, ShapeFactory.BOTTOM_LEFT_CORNER, false);
    	g.fill(s);
    	
    	s = ShapeFactory.getInstace().createRoundCorner( c.getBounds().width-numBorders, c.getBounds().height-numBorders, c.getInsets().right-numBorders, c.getInsets().bottom-numBorders, ShapeFactory.BOTTOM_RIGHT_CORNER, false);
    	g.fill(s);
    	
    	//Drawing rectangle zones...
    	//top
    	s = ShapeFactory.getInstace().createRectangle( insets.left, y+numBorders, bounds.width-insets.left-insets.right, insets.top-numBorders);
		g.fill(s);
		//bottom
		s = ShapeFactory.getInstace().createRectangle( insets.left, y+bounds.height-insets.bottom, bounds.width-insets.left-insets.right, insets.bottom-numBorders);
		g.fill(s);
		//left
		s = ShapeFactory.getInstace().createRectangle( x+numBorders, insets.top, insets.right-numBorders-1, bounds.height-insets.top-insets.bottom);
		g.fill(s);
		//right
		s = ShapeFactory.getInstace().createRectangle( x+bounds.width-insets.right+1,insets.top, insets.right-numBorders, bounds.height-insets.top-insets.bottom);
		g.fill(s);
		
		g.setPaint(previousPaint);
    }
    
    protected Paint getPaintForFiller(JComponent c, Shape s){
    	Paint filler = null;
		switch (state) {
		case BORDER_DISABLED: filler = getBackgroundColor(c, s, backgroundColorDisabled); break;
		case BORDER_ENABLED: filler = getBackgroundColor(c, s, backgroundColorEnabled); break;
		case BORDER_FOCUSED: filler = getBackgroundColor(c, s, backgroundColorEnabled); break;
		case BORDER_DISABLED_NOTINSCROLLPANE: filler = getBackgroundColor(c, s, backgroundColorDisabled); break;
		case BORDER_ENABLED_NOTINSCROLLPANE: filler = getBackgroundColor(c, s, backgroundColorEnabled); break;
		case BORDER_FOCUSED_NOTINSCROLLPANE: filler = getBackgroundColor(c, s, backgroundColorEnabled); break;
		}
		return filler;
    }
    
    protected void defineBorderFillersInScrollPane(Graphics2D g, JComponent c, int x, int y){
    	Paint previousPaint = g.getPaint();
    	
    	Rectangle bounds = c.getBounds();
    	Insets insets = c.getInsets();
    	int numBorders = 4;
    	
    	//Drawing corners...
    	Shape s = ShapeFactory.getInstace().createRoundCorner( x+numBorders, y+numBorders, c.getInsets().left-numBorders-1, c.getInsets().top-numBorders, ShapeFactory.TOP_LEFT_CORNER, false);
		g.setPaint(getPaintForFiller(c,s));
    	g.fill(s);
    	
    	//Checking scrollBars...
    	JScrollBar vScrollBar = getScrollBar(c, false);
    	JScrollBar hScrollBar = getScrollBar(c, true);
    	
    	//Top right corner...
    	if(vScrollBar!=null && vScrollBar.isVisible()){
			s = ShapeFactory.getInstace().createRectangle( bounds.width-insets.right, y+numBorders, c.getInsets().right, c.getInsets().top-numBorders);
	    	g.fill(s);
    	}else{
    		s = ShapeFactory.getInstace().createRoundCorner( c.getBounds().width-numBorders, y+numBorders, c.getInsets().right-numBorders-1, c.getInsets().top-numBorders, ShapeFactory.TOP_RIGHT_CORNER, false);
        	g.fill(s);
    	}
    	
    	//Bottom right corner...
    	if( (vScrollBar!=null && vScrollBar.isVisible()) &&  (hScrollBar!=null && hScrollBar.isVisible())   ){
	    	s = ShapeFactory.getInstace().createRectangle( bounds.width-insets.right, bounds.height-insets.bottom, insets.right, insets.bottom);
	    	g.fill(s);
    	}else if( !(vScrollBar!=null && vScrollBar.isVisible()) &&  (hScrollBar!=null && hScrollBar.isVisible())){
    		s = ShapeFactory.getInstace().createRectangle( bounds.width-insets.right, bounds.height-insets.bottom, insets.right-numBorders, insets.bottom);
	    	g.fill(s);
    	} else if((vScrollBar!=null && vScrollBar.isVisible()) &&  !(hScrollBar!=null && hScrollBar.isVisible())){
    		s = ShapeFactory.getInstace().createRectangle( bounds.width-insets.right, bounds.height-insets.bottom, insets.right, insets.bottom-numBorders);
	    	g.fill(s);
    	} else{
	    	s = ShapeFactory.getInstace().createRoundCorner( c.getBounds().width-numBorders, c.getBounds().height-numBorders, c.getInsets().right-numBorders-1, c.getInsets().bottom-numBorders, ShapeFactory.BOTTOM_RIGHT_CORNER, false);
	    	g.fill(s);
    	}
    	
    	//Bottom left corner...
    	if(hScrollBar!=null && hScrollBar.isVisible()){
    		s = ShapeFactory.getInstace().createRectangle( x+numBorders, c.getBounds().height-insets.bottom, c.getInsets().left-numBorders, insets.bottom);
	    	g.fill(s);
    	}else{
    		s = ShapeFactory.getInstace().createRoundCorner( x+numBorders, c.getBounds().height-numBorders, c.getInsets().left-numBorders-1, c.getInsets().bottom-numBorders, ShapeFactory.BOTTOM_LEFT_CORNER, false);
        	g.fill(s);
    	}
    	
    	
    	//Drawing rectangle zones...
    	//top
    	s = ShapeFactory.getInstace().createRectangle( insets.left-1, y+numBorders, bounds.width-insets.left-insets.right+2, insets.top-numBorders);
		g.fill(s);
		//bottom
		int bottomMargin = 0;
		if(hScrollBar!=null && hScrollBar.isVisible()){
			bottomMargin = insets.bottom;
		}else{
			bottomMargin = insets.bottom-numBorders;
		}
		s = ShapeFactory.getInstace().createRectangle( insets.left-1, y+bounds.height-insets.bottom, bounds.width-insets.left-insets.right+2, bottomMargin);
		g.fill(s);
		//left
		s = ShapeFactory.getInstace().createRectangle( x+numBorders, insets.top, insets.right-numBorders-1, bounds.height-insets.top-insets.bottom);
		g.fill(s);
		//right
		int rightMargin = 0;
		if(vScrollBar!=null && vScrollBar.isVisible()){
			rightMargin = insets.right-1;
		}else{
			rightMargin = insets.right-numBorders-1;
		}
		s = ShapeFactory.getInstace().createRectangle( x+bounds.width-insets.right+1,insets.top, rightMargin, bounds.height-insets.top-insets.bottom);
		g.fill(s);
		
		
		g.setPaint(previousPaint);
    }
    
    protected JScrollBar getScrollBar(JComponent c, boolean horizontal) {
		Object o = c.getParent();
		if (o instanceof JViewport) {
			JViewport jvp = (JViewport) o;
			Object o2 = jvp.getParent();
			if (o2 instanceof JScrollPane) {
				JScrollPane jsp = (JScrollPane) o2;
				if(horizontal){
					return jsp.getHorizontalScrollBar();
				}else{
					return jsp.getVerticalScrollBar();
				}
			}
		}
		return null;
	}
    
    protected Shape createRoundRect( double x, double y, double w, double h, Insets insets) {
		
		path.reset();
		
		//left top corner.
		path.moveTo(x + insets.left, y);
		path.curveTo(x + insets.left / 2.0, y, x, y + insets.top / 2.0, x, y+ insets.top);

		//left bottom corner
		path.lineTo(x, y+ h-insets.bottom);
		path.curveTo(x,y+ h-insets.bottom/2.0, x+insets.left/2.0, y+h, x+insets.left, y+h);
		path.lineTo(x+w-insets.right, y+h);
		
		//right bottom corner
		path.curveTo(x+w-insets.right/2.0,y+ h, x+w, y+h-insets.bottom/2.0, x+w, y+h-insets.bottom);
		
		//right top corner
		path.lineTo(x+w, y+insets.top);
		path.curveTo(x+w,y+insets.top/2.0, x+w-insets.right/2.0, y, x+w-insets.right, y);
		path.lineTo(x+insets.left, y);
		
		return path;
	}


}
