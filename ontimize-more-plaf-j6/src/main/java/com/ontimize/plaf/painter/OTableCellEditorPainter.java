package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;



public class OTableCellEditorPainter extends AbstractRegionPainter {
    //package protected integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of TableEditorPainter to determine which region/state is being painted
    //by that instance.
   public static final int BACKGROUND_DISABLED = 1;
   public static final int BACKGROUND_ENABLED = 2;
   public static final int BACKGROUND_ENABLED_FOCUSED = 3;
   public static final int BACKGROUND_SELECTED = 4;
   

    protected int state; //refers to one of the static ints above
    protected PaintContext ctx;

    //the following 4 variables are reused during the painting code of the layers
    protected Path2D path = new Path2D.Float();
    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
    protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);
    protected Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    //All Colors used for painting are stored here. Ideally, only those colors being used
    //by a particular instance of TableEditorPainter would be created. For the moment at least,
    //however, all are created for each instance.
    protected Color color1 = decodeColor("nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);


    //Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OTableCellEditorPainter(int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx = ctx;
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        //populate componentColors array with colors calculated in getExtendedCacheKeys call
        componentColors = extendedCacheKeys;
        //generate this entire method. Each state/bg/fg/border combo that has
        //been painted gets its own KEY and paint method.
        switch(state) {
            case BACKGROUND_ENABLED: paintBackgroundEnabled(g); break;
            case BACKGROUND_ENABLED_FOCUSED: paintBackgroundEnabledAndFocused(g); break;

        }
    }
        


    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    protected void paintBackgroundEnabled(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color1);
        g.fill(rect);
        paintInnerShadow(g);
    }

    protected void paintBackgroundEnabledAndFocused(Graphics2D g) {
    	rect = decodeRect1();
        g.setPaint(color1);
        g.fill(rect);

        paintInnerShadow(g);
    }
    
    protected void paintInnerShadow(Graphics2D g){
    	
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



    protected Rectangle2D decodeRect1() {
            rect.setRect(decodeX(0.0f), //x
                         decodeY(0.0f), //y
                         decodeX(3.0f) - decodeX(0.0f), //width
                         decodeY(3.0f) - decodeY(0.0f)); //height
        return rect;
    }

    protected Path2D decodePath1() {
        path.reset();
        path.moveTo(decodeX(0.0f), decodeY(0.0f));
        path.lineTo(decodeX(0.0f), decodeY(3.0f));
        path.lineTo(decodeX(3.0f), decodeY(3.0f));
        path.lineTo(decodeX(3.0f), decodeY(0.0f));
        path.lineTo(decodeX(0.24000001f), decodeY(0.0f));
        path.lineTo(decodeX(0.24000001f), decodeY(0.24000001f));
        path.lineTo(decodeX(2.7600007f), decodeY(0.24000001f));
        path.lineTo(decodeX(2.7600007f), decodeY(2.7599998f));
        path.lineTo(decodeX(0.24000001f), decodeY(2.7599998f));
        path.lineTo(decodeX(0.24000001f), decodeY(0.0f));
        path.lineTo(decodeX(0.0f), decodeY(0.0f));
        path.closePath();
        return path;
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
	
	protected Paint decodeTopInnerShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float h = (float)bounds.getHeight();
        return decodeGradient(x, y, x, y+h,
                new float[] { 0.0f, 1.0f },
                new Color[] { new Color(0xBAf1FE),
                               new Color(0xF3FdFF)});
    }
	
	protected Paint decodeBottomInnerShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float h = (float)bounds.getHeight();
        return decodeGradient(x, y, x, y+h,
                new float[] { 0.0f, 1.0f },
                new Color[] {  new Color(0xF3FdFF),
                               new Color(0xBAf1FE)});
    }
	
	protected Paint decodeLeftInnerShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float w = (float)bounds.getWidth();
        return decodeGradient(x, y, x + w, y,
                new float[] { 0.0f, 1.0f },
                new Color[] { new Color(0xBAf1FE),
                              new Color(0xF3FdFF)});
    }
	
	protected Paint decodeRightInnerShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float w = (float)bounds.getWidth();
        return decodeGradient(x, y, x + w, y,
                new float[] { 0.0f, 1.0f },
                new Color[] { new Color(0xF3FdFF),
                              new Color(0xBAf1FE)});
    }




}
