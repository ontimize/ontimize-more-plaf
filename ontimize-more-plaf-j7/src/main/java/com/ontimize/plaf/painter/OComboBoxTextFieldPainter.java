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
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;

public class OComboBoxTextFieldPainter extends AbstractRegionPainter {
    //package protected integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of ComboBoxComboBoxTextFieldPainter to determine which region/state is being painted
    //by that instance.
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_ENABLED = 2;
    public static final int BACKGROUND_SELECTED = 3;


    protected int state; //refers to one of the static ints above
    protected PaintContext ctx;
    
    
    // painters to fill the component
	protected Paint backgroundColorDisabled;
	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorSelected;

    //the following 4 variables are reused during the painting code of the layers
    protected Path2D path = new Path2D.Float();
    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
    protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);
    protected Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    //All Colors used for painting are stored here. Ideally, only those colors being used
    //by a particular instance of ComboBoxComboBoxTextFieldPainter would be created. For the moment at least,
    //however, all are created for each instance.
    protected Color color1 = decodeColor("softWhite", 0.0f, 0.0f, 1.0f, 125);
    protected Color color2 = decodeColor("white", 0.0f, 0.0f, 1.0f, 255);


    //Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OComboBoxTextFieldPainter(int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx = ctx;
        
        init();
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        //populate componentColors array with colors calculated in getExtendedCacheKeys call
        componentColors = extendedCacheKeys;
        //generate this entire method. Each state/bg/fg/border combo that has
        //been painted gets its own KEY and paint method.
        switch(state) {
            case BACKGROUND_DISABLED: paintBackgroundDisabled(g); break;
            case BACKGROUND_ENABLED: paintBackgroundEnabled(g); break;
            case BACKGROUND_SELECTED: paintBackgroundSelected(g); break;

        }
        
    }
        


    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }
    
    protected String getComponentKeyName() {
		return "ComboBox:\"ComboBox.textField\"";
	}
    
    protected void init(){
    	
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

		// selected:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Selected].background");
		if(obj instanceof Paint){
			backgroundColorSelected = (Paint)obj;
		}else{
			backgroundColorSelected = color2;
		}
    	
    }

    protected void paintBackgroundDisabled(Graphics2D g) {
    	
    	paintBackground(g,  backgroundColorDisabled);
    }

    protected void paintBackgroundEnabled(Graphics2D g) {
    	
    	paintBackground(g, backgroundColorEnabled);
    }

    protected void paintBackgroundSelected(Graphics2D g) {
    	
    	paintBackground(g, backgroundColorSelected);
    }
    
    protected void paintBackground(Graphics2D g, Paint bg) {
    	
    	double x = decodeX(0.0f);
    	double y = decodeY(0.0f);
    	double w = decodeX(3.0f);
    	double h = decodeY(3.0f);
    	Shape s = ShapeFactory.getInstace().createRectangle(x, y, w, h);
    	g.setPaint( bg );
    	g.fill(s);
    }

}
