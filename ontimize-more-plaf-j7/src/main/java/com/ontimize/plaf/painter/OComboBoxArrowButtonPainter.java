package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;


public class OComboBoxArrowButtonPainter extends AbstractRegionPainter {
    //package protected integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of ComboBoxComboBoxArrowButtonPainter to determine which region/state is being painted
    //by that instance.
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_ENABLED = 2;
    public static final int BACKGROUND_ENABLED_MOUSEOVER = 3;
    public static final int BACKGROUND_ENABLED_PRESSED = 4;
    public static final int BACKGROUND_DISABLED_EDITABLE = 5;
    public static final int BACKGROUND_ENABLED_EDITABLE = 6;
    public static final int BACKGROUND_MOUSEOVER_EDITABLE = 7;
    public static final int BACKGROUND_PRESSED_EDITABLE = 8;
    public static final int BACKGROUND_SELECTED_EDITABLE = 9;
    public static final int FOREGROUND_ENABLED = 10;
    public static final int FOREGROUND_MOUSEOVER = 11;
    public static final int FOREGROUND_DISABLED = 12;
    public static final int FOREGROUND_PRESSED = 13;
    public static final int FOREGROUND_SELECTED = 14;


    protected int state; //refers to one of the static ints above
    protected PaintContext ctx;

    //the following 4 variables are reused during the painting code of the layers
    protected Path2D path = new Path2D.Float();
    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
    protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);
    protected Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    //All Colors used for painting are stored here. Ideally, only those colors being used
    //by a particular instance of ComboBoxComboBoxArrowButtonPainter would be created. For the moment at least,
    //however, all are created for each instance.
    protected Color color1 = new Color(255,255,255);
    protected Color color2 = new Color(0,0,0);
    protected Color color3 = decodeColor("nimbusBase", 0.010237217f, -0.55799407f, 0.20784312f, 0);
    protected Color color4 = new Color(255, 200, 0, 255);
    protected Color color5 = decodeColor("nimbusBase", 0.021348298f, -0.59223604f, 0.35294116f, 0);
    protected Color color6 = decodeColor("nimbusBase", 0.02391243f, -0.5774183f, 0.32549018f, 0);
    protected Color color7 = decodeColor("nimbusBase", 0.021348298f, -0.56722116f, 0.3098039f, 0);
    protected Color color8 = decodeColor("nimbusBase", 0.021348298f, -0.567841f, 0.31764704f, 0);
    protected Color color9 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.74509805f, -191);
    protected Color color10 = decodeColor("nimbusBase", 5.1498413E-4f, -0.34585923f, -0.007843137f, 0);
    protected Color color11 = decodeColor("nimbusBase", 5.1498413E-4f, -0.095173776f, -0.25882354f, 0);
    protected Color color12 = decodeColor("nimbusBase", 0.004681647f, -0.6197143f, 0.43137252f, 0);
    protected Color color13 = decodeColor("nimbusBase", 0.0023007393f, -0.46825016f, 0.27058822f, 0);
    protected Color color14 = decodeColor("nimbusBase", 5.1498413E-4f, -0.43866998f, 0.24705881f, 0);
    protected Color color15 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4625541f, 0.35686272f, 0);
    protected Color color16 = decodeColor("nimbusBase", 0.0013483167f, -0.1769987f, -0.12156865f, 0);
    protected Color color17 = decodeColor("nimbusBase", 0.059279382f, 0.3642857f, -0.43529415f, 0);
    protected Color color18 = decodeColor("nimbusBase", 0.004681647f, -0.6198413f, 0.43921566f, 0);
    protected Color color19 = decodeColor("nimbusBase", 0.0023007393f, -0.48084703f, 0.33725488f, 0);
    protected Color color20 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4555341f, 0.3215686f, 0);
    protected Color color21 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4757143f, 0.43137252f, 0);
    protected Color color22 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    protected Color color23 = decodeColor("nimbusBase", -3.528595E-5f, 0.018606722f, -0.23137257f, 0);
    protected Color color24 = decodeColor("nimbusBase", -4.2033195E-4f, -0.38050595f, 0.20392156f, 0);
    protected Color color25 = decodeColor("nimbusBase", 7.13408E-4f, -0.064285696f, 0.027450979f, 0);
    protected Color color26 = decodeColor("nimbusBase", 0.0f, -0.00895375f, 0.007843137f, 0);
    protected Color color27 = decodeColor("nimbusBase", 8.9377165E-4f, -0.13853917f, 0.14509803f, 0);
    protected Color color28 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.37254906f, 0);
    protected Color color29 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.5254902f, 0);
    protected Color color30 = decodeColor("nimbusBase", 0.027408898f, -0.57391655f, 0.1490196f, 0);
    protected Color color31 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);
    
    
	protected Paint foregroundColorDisabled;
	protected Paint foregroundColorEnabled;
	protected Paint foregroundColorMouseOver;
	protected Paint foregroundColorPressed;
	protected Paint foregroundColorSelected;


    //Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OComboBoxArrowButtonPainter(int state, PaintContext ctx) {
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
            case FOREGROUND_ENABLED: paintForegroundEnabled(g); break;
            case FOREGROUND_MOUSEOVER: paintForegroundMouseOver(g); break;
            case FOREGROUND_DISABLED: paintForegroundDisabled(g); break;
            case FOREGROUND_PRESSED: paintForegroundPressed(g); break;
            case FOREGROUND_SELECTED: paintForegroundSelected(g); break;

        }
    }

    protected String getComponentKeyName() {
		return "ComboBox:\"ComboBox.arrowButton\"";
	}
    
    protected void init (){
    	
    	// disabled:
		Object obj = UIManager.getLookAndFeelDefaults().get(getComponentKeyName() + "[Disabled].foreground");
		if(obj instanceof Paint){
			foregroundColorDisabled = (Paint)obj;
		}else{
			foregroundColorDisabled = color1;
		}

		// enabled:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Enabled].foreground");
		if(obj instanceof Paint){
			foregroundColorEnabled = (Paint)obj;
		}else{
			foregroundColorEnabled = color1;
		}

		// selected:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Selected].foreground");
		if(obj instanceof Paint){
			foregroundColorSelected = (Paint)obj;
		}else{
			foregroundColorSelected = color2;
		}
		
		// mouseover:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[MouseOver].foreground");
		if(obj instanceof Paint){
			foregroundColorMouseOver = (Paint)obj;
		}else{
			foregroundColorMouseOver = color2;
		}

		// pressed:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Pressed].foreground");
		if(obj instanceof Paint){
			foregroundColorPressed = (Paint)obj;
		}else{
			foregroundColorPressed = color2;
		}
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }


    protected void paintForegroundEnabled(Graphics2D g) {
        path = decodeArrow();
        g.setPaint(foregroundColorEnabled);
        g.fill(path);

    }

    protected void paintForegroundMouseOver(Graphics2D g) {
    	path = decodeArrow();
        g.setPaint(foregroundColorMouseOver);
        g.fill(path);

    }

    protected void paintForegroundDisabled(Graphics2D g) {
    	path = decodeArrow();
        g.setPaint(foregroundColorDisabled);
        g.fill(path);

    }

    protected void paintForegroundPressed(Graphics2D g) {
    	path = decodeArrow();
        g.setPaint(foregroundColorPressed);
        g.fill(path);

    }

    protected void paintForegroundSelected(Graphics2D g) {
    	path = decodeArrow();
        g.setPaint(foregroundColorSelected);
        g.fill(path);

    }


    protected Path2D decodeArrow() {
        path.reset();
        
        double xcenter = (decodeX(2.0f) - decodeX(1.0f))/2.0;
        double ycenter = (decodeY(2.0f) - decodeY(1.0f))/2.0;
        
        double arrowHeight = 6;
        double arrowWidth = 11;
        
        path.moveTo(xcenter-arrowHeight/2.0, ycenter);
        path.lineTo(xcenter+arrowHeight/2.0, ycenter+arrowWidth/2.0);
        path.lineTo(xcenter+arrowHeight/2.0, ycenter-arrowWidth/2.0);
        path.lineTo(xcenter-arrowHeight/2.0, ycenter);
        
        path.closePath();
        return path;
    }


}
