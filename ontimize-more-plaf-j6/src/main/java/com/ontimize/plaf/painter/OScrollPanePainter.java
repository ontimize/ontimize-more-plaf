package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import com.ontimize.plaf.painter.util.ShapeFactory;

public class OScrollPanePainter extends AbstractRegionPainter {
    //package protected integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of ScrollPanePainter to determine which region/state is being painted
    //by that instance.
    public static final int BACKGROUND_ENABLED = 1;
    public static final int BORDER_ENABLED_FOCUSED = 2;
    public static final int BORDER_ENABLED = 3;
    public static final int BORDER_DISABLED = 4;
    
    
    public static final int CORNER_ENABLED = 5;


    protected int state; //refers to one of the static ints above
    protected PaintContext ctx;
    
	protected int numBorders=4;

    //the following 4 variables are reused during the painting code of the layers
    protected Path2D path = new Path2D.Float();
    
    // arrays to round the component (several rounded borders with degradation):
	protected Paint[] degradatedBorderColorEnabled;
	protected Paint[] degradatedBorderColorDisabled;
	protected Paint[] degradatedBorderColorFocused;
	protected Color scrollBarCornerColor;

	//All Colors used for painting are stored here. Ideally, only those colors being used
    //by a particular instance of TextAreaPainter would be created. For the moment at least,
    //however, all are created for each instance.
    protected Color color3 = decodeColor("nimbusBlueGrey", -0.006944418f, -0.07187897f, 0.06666666f, 0);
    protected Color color4 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07826825f, 0.10588235f, 0);
    protected Color color7 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.0965403f, -0.18431371f, 0);
    protected Color color8 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.1048766f, -0.05098039f, 0);


    //Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OScrollPanePainter(int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx = ctx;
        
        init();
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        //populate componentColors array with colors calculated in getExtendedCacheKeys call
        componentColors = extendedCacheKeys;

        int x =0;
        int y =0;
        
        switch(state) {
            case BORDER_ENABLED_FOCUSED: paintBorderEnabledAndFocused(g,c,x,y,width,height); break;
            case BORDER_ENABLED: paintBorderEnabled(g,c,x,y,width,height); break;
            case BORDER_DISABLED: paintBorderDisabled(g,c,x,y,width,height); break;
            case BACKGROUND_ENABLED: break;
            case CORNER_ENABLED: paintCorner(g, c);
        }
    }
    
    protected String getComponentKeyName() {
		return "ScrollPane";
	}
        

    protected void init (){

		// BORDER COLORS
		// enable:
		Object obj = UIManager.getLookAndFeelDefaults().get("TextArea[Enabled].border");
		if(obj instanceof Paint){
			degradatedBorderColorEnabled = new Paint[]{(Paint)obj};
		}else if(obj instanceof Paint[]){
			degradatedBorderColorEnabled = (Paint[])obj;
		}else{
			degradatedBorderColorEnabled = new Color[] { color7, decodeColor(color7,color8,0.5f), color8};
		}
		
		// disable:
		obj = UIManager.getLookAndFeelDefaults().get("TextArea[Disabled].border");
		if(obj instanceof Paint){
			degradatedBorderColorDisabled = new Paint[]{(Paint)obj};
		}else if(obj instanceof Paint[]){
			degradatedBorderColorDisabled = (Paint[])obj;
		}else{
			degradatedBorderColorDisabled = new Color[] { color3, decodeColor(color3,color4,0.5f), color4};
		}

		//Focused:
		obj = UIManager.getLookAndFeelDefaults().get("TextArea[Focused].border");
		if(obj instanceof Paint){
			degradatedBorderColorFocused = new Paint[]{(Paint)obj};
		}else if(obj instanceof Paint[]){
			degradatedBorderColorFocused = (Paint[])obj;
		}else{
			degradatedBorderColorFocused = new Color[] { color7, decodeColor(color7,color8,0.5f), color8};
		}
		
		scrollBarCornerColor = (Color)UIManager.getLookAndFeelDefaults().get( "ScrollBar[Enabled].border");
		if (scrollBarCornerColor==null ) scrollBarCornerColor =  new Color(255,255,255);
		
		numBorders = Math.max(degradatedBorderColorFocused.length, Math.max(degradatedBorderColorEnabled.length, degradatedBorderColorDisabled.length))-1;
		
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }
    
    protected boolean containsTextComponent(JComponent c){
    	Component[] comps = c.getComponents();
    	if(comps!=null && comps.length>0){
    		for(int i=0;i<comps.length;i++){
    			Component current = comps[i];
    			if(current instanceof JComponent && current.getClass().isAssignableFrom(JViewport.class)){
    				if(((JComponent)current).getComponents()[0] instanceof JTextComponent){
    					return true;
    				}
    			}
    		}
    	}
    	
    	return false;
    }
    
    protected JTextComponent getTextComponent(JComponent c){
    	Component[] comps = c.getComponents();
    	if(comps!=null && comps.length>0){
    		for(int i=0;i<comps.length;i++){
    			Component current = comps[i];
    			if(current instanceof JComponent && current.getClass().isAssignableFrom(JViewport.class)){
    				if(((JComponent)current).getComponents()[0] instanceof JTextComponent){
    					return (JTextComponent)((JComponent)current).getComponents()[0];
    				}
    			}
    		}
    	}
    	
    	return null;
    }

	protected void paintBorderEnabledAndFocused(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		if (!containsTextComponent(c))
			return;
		if (degradatedBorderColorFocused != null && degradatedBorderColorFocused.length > 0) {
			drawDegradatedBorders(g, c, x, y, width, height, degradatedBorderColorFocused);
		}

	}

	protected void paintBorderEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		if (!containsTextComponent(c)) {
			return;
		}
		if (degradatedBorderColorEnabled != null && degradatedBorderColorEnabled.length > 0) {
			drawDegradatedBorders(g, c, x, y, width, height, degradatedBorderColorEnabled);
		}

	}
	
	protected void paintBorderDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
		if (!containsTextComponent(c)) {
			return;
		}
		if (degradatedBorderColorDisabled != null && degradatedBorderColorDisabled.length > 0) {
			drawDegradatedBorders(g, c, x, y, width, height, degradatedBorderColorDisabled);
		}
	}
	
	protected void paintCorner(Graphics2D g, JComponent c){
    	if(c instanceof JScrollPane){
    		JScrollBar hScrollBar = ((JScrollPane)c).getHorizontalScrollBar();
    		JScrollBar vScrollBar = ((JScrollPane)c).getVerticalScrollBar();
    		if(vScrollBar!=null && vScrollBar.isVisible() && hScrollBar!=null && hScrollBar.isVisible()){
    			int width = vScrollBar.getWidth();
    			int height = hScrollBar.getHeight();
    			Shape corner = ShapeFactory.getInstace().createRectangle(0, 0, width, height);
    			g.setPaint(scrollBarCornerColor);
    			g.fill(corner);
    		}
    	}
    }

    public void drawDegradatedBorders(Graphics2D g, JComponent c, int x, int y, int width, int height, Paint[] colors){
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if(c instanceof JScrollPane){
    		JScrollBar hScrollBar = ((JScrollPane)c).getHorizontalScrollBar();
    		JScrollBar vScrollBar = ((JScrollPane)c).getVerticalScrollBar();
    		
    		if(vScrollBar!=null && vScrollBar.isVisible() && hScrollBar!=null && !hScrollBar.isVisible()){
    			//Painting border just with vertical scrollbar...
    			for (int i = colors.length-1; i >=0 ; i--) {
        			Shape s = decodeBorderPathWithVerticalScrollBar(c, numBorders-i, numBorders-i);
        			g.setPaint(colors[i]);
        			g.draw(s);
        		}
    		}else if(vScrollBar!=null && !vScrollBar.isVisible() && hScrollBar!=null && hScrollBar.isVisible()){
    			//Painting border just with horizontal scrollbar...
    			for (int i = colors.length-1; i >=0 ; i--) {
        			Shape s = decodeBorderPathWithHorizontalScrollBar(c, numBorders-i, numBorders-i);
        			g.setPaint(colors[i]);
        			g.draw(s);
        		}
    		}else if(vScrollBar!=null && vScrollBar.isVisible() && hScrollBar!=null && hScrollBar.isVisible()){
    			//Painting border with both scrollbar...
    			for (int i = colors.length-1; i >=0 ; i--) {
        			Shape s = decodeBorderPathWithBothScrollBar(c, numBorders-i, numBorders-i);
        			g.setPaint(colors[i]);
        			g.draw(s);
        		}
    		}else{
	    		for (int i = colors.length-1; i >=0  ; i--) {
	    			Shape s = decodeBorderPath(c, numBorders-i, numBorders-i);
	    			g.setPaint(colors[i]);
	    			g.draw(s);
	    		}
    		}
    		
		}
		
		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
	}
    
    protected Shape decodeBorderPath(JComponent c, int x, int y){
		path.reset();
		
		JTextComponent textComp = getTextComponent(c);
		if(textComp !=null){
			Insets insets = textComp.getInsets();
			Insets tInsets = (Insets) insets.clone();
			tInsets.top = (int) (decodeY(1.0f)+insets.top);
			tInsets.bottom = (int) (decodeY(3.0f)-decodeY(2.0f))+insets.bottom;
			tInsets.right = (int) (decodeX(3.0f)-decodeX(2.0f))+insets.right;
			tInsets.left = (int) decodeX(1.0f)+insets.left;
			
			path.moveTo(decodeX(0.0f)+tInsets.left-1, decodeY(0.0f)+y);
			
			path.lineTo(decodeX(3.0f)-tInsets.right, decodeY(0.0f)+y);
			//Top right corner...
			path.curveTo(decodeX(3.0f)-x-tInsets.right+tInsets.right/2.0, 	decodeY(0.0f)+y, 
						decodeX(3.0f)-x-1, 					decodeY(0.0f)+tInsets.top/2.0, 
						decodeX(3.0f)-x-1, 					decodeY(0.0f)+tInsets.top);
			
			path.lineTo(decodeX(3.0f)-x-1, decodeY(3.0f)-tInsets.bottom-1);
			//Bottom right corner...
			path.curveTo(decodeX(3.0f)-x-1, 			decodeY(3.0f)-y-tInsets.bottom+tInsets.bottom/2.0, 
						decodeX(3.0f)-1-tInsets.right/2.0, 	decodeY(3.0f)-y-1, 
						decodeX(3.0f)-1-tInsets.right, 		decodeY(3.0f)-y-1);
			
			path.lineTo(decodeX(0.0f)-1+tInsets.left, decodeY(3.0f)-y-1);
			//Bottom left corner...
			path.curveTo(decodeX(0.0f)+tInsets.left-tInsets.left/2.0, 					decodeY(3.0f)-y-1, 
						decodeX(0.0f)+x, 					decodeY(3.0f)-2-tInsets.bottom+tInsets.bottom/2.0, 
						decodeX(0.0f)+x,  					decodeY(3.0f)-2-tInsets.bottom);
			
			path.lineTo(decodeX(0.0f)+x, decodeY(0.0f)+tInsets.top);
			//Top left corner...
			path.curveTo(decodeX(0.0f)+x,	 			decodeY(0.0f)+y+tInsets.bottom-tInsets.bottom/2.0, 
					decodeX(0.0f)+tInsets.right/2.0, 	decodeY(0.0f)+y, 
					decodeX(0.0f)+tInsets.right, 		decodeY(0.0f)+y);
		
		}
		return path;
	}
    
    
    protected Shape decodeBorderPathWithVerticalScrollBar(JComponent c, int x, int y){
		path.reset();
		
		JTextComponent textComp = getTextComponent(c);
		if(textComp !=null){
			Insets insets = textComp.getInsets();
			Insets tInsets = (Insets) insets.clone();
			tInsets.top = (int) (decodeY(1.0f)+insets.top);
			tInsets.bottom = (int) (decodeY(3.0f)-decodeY(2.0f))+insets.bottom;
			tInsets.right = (int) (decodeX(3.0f)-decodeX(2.0f))+insets.right;
			tInsets.left = (int) decodeX(1.0f)+insets.left;
			
			path.moveTo(decodeX(0.0f)+tInsets.left, decodeY(0.0f)+y);
			
			path.lineTo(decodeX(3.0f)-x-1, decodeY(0.0f)+y);
			//Top right corner...
			path.lineTo(decodeX(3.0f)-x-1, decodeY(3.0f)-y-1);
			//Bottom right corner...
			path.lineTo(decodeX(0.0f)+tInsets.left, decodeY(3.0f)-y-1);
			//Bottom left corner...
			path.curveTo(decodeX(0.0f)+tInsets.left-tInsets.left/2.0, 					decodeY(3.0f)-y-1, 
						decodeX(0.0f)+x, 					decodeY(3.0f)-2-tInsets.bottom+tInsets.bottom/2.0, 
						decodeX(0.0f)+x,  					decodeY(3.0f)-2-tInsets.bottom);
			
			path.lineTo(decodeX(0.0f)+x, decodeY(0.0f)+tInsets.top);
			//Top left corner...
			path.curveTo(decodeX(0.0f)+x,	 			decodeY(0.0f)+y+tInsets.bottom-tInsets.bottom/2.0, 
					decodeX(0.0f)+tInsets.right/2.0, 	decodeY(0.0f)+y, 
					decodeX(0.0f)+tInsets.right, 		decodeY(0.0f)+y);
		
		}
		return path;
	}
    
    protected Shape decodeBorderPathWithHorizontalScrollBar(JComponent c, int x, int y){
		path.reset();
		
		JTextComponent textComp = getTextComponent(c);
		if(textComp !=null){
			Insets insets = textComp.getInsets();
			Insets tInsets = (Insets) insets.clone();
			tInsets.top = (int) (decodeY(1.0f)+insets.top);
			tInsets.bottom = (int) (decodeY(3.0f)-decodeY(2.0f))+insets.bottom;
			tInsets.right = (int) (decodeX(3.0f)-decodeX(2.0f))+insets.right;
			tInsets.left = (int) decodeX(1.0f)+insets.left;
			
			path.moveTo(decodeX(0.0f)+tInsets.left, decodeY(0.0f)+y);
			
			path.lineTo(decodeX(3.0f)-tInsets.right, decodeY(0.0f)+y);
			//Top right corner...
			path.curveTo(decodeX(3.0f)-tInsets.right+tInsets.right/2.0, 	decodeY(0.0f)+y, 
						decodeX(3.0f)-x-1, 					decodeY(0.0f)+tInsets.top/2.0, 
						decodeX(3.0f)-x-1, 					decodeY(0.0f)+tInsets.top);
			
			path.lineTo(decodeX(3.0f)-x-1, decodeY(3.0f)-y-1);
			//Bottom right corner...
			path.lineTo(decodeX(0.0f)+x, decodeY(3.0f)-y-1);
			//Bottom left corner...
			
			path.lineTo(decodeX(0.0f)+x, decodeY(0.0f)+tInsets.top);
			//Top left corner...
			path.curveTo(decodeX(0.0f)+x,	 			decodeY(0.0f)+y+tInsets.bottom-tInsets.bottom/2.0, 
					decodeX(0.0f)+tInsets.right/2.0, 	decodeY(0.0f)+y, 
					decodeX(0.0f)+tInsets.right, 		decodeY(0.0f)+y);
		
		}
		return path;
	}
    
    protected Shape decodeBorderPathWithBothScrollBar(JComponent c, int x, int y){
		path.reset();
		
		JTextComponent textComp = getTextComponent(c);
		if(textComp !=null){
			Insets insets = textComp.getInsets();
			Insets tInsets = (Insets) insets.clone();
			tInsets.top = (int) (decodeY(1.0f)+insets.top);
			tInsets.bottom = (int) (decodeY(3.0f)-decodeY(2.0f))+insets.bottom;
			tInsets.right = (int) (decodeX(3.0f)-decodeX(2.0f))+insets.right;
			tInsets.left = (int) decodeX(1.0f)+insets.left;
			
			path.moveTo(decodeX(0.0f)+tInsets.left, decodeY(0.0f)+y);
			
			path.lineTo(decodeX(3.0f)-x-1, decodeY(0.0f)+y);
			//Top right corner...
			
			path.lineTo(decodeX(3.0f)-x-1, decodeY(3.0f)-y-1);
			//Bottom right corner...
			
			path.lineTo(decodeX(0.0f)+x, decodeY(3.0f)-y-1);
			//Bottom left corner...
			
			path.lineTo(decodeX(0.0f)+x, decodeY(0.0f)+tInsets.top);
			//Top left corner...
			path.curveTo(decodeX(0.0f)+x,	 			decodeY(0.0f)+y+tInsets.bottom-tInsets.bottom/2.0, 
					decodeX(0.0f)+tInsets.right/2.0, 	decodeY(0.0f)+y, 
					decodeX(0.0f)+tInsets.right, 		decodeY(0.0f)+y);
		
		}
		return path;
	}
    
    

}
