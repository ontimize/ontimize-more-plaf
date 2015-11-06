package com.ontimize.plaf.painter;

import java.awt.Color;
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
import com.ontimize.plaf.utils.OntimizeLAFParseUtils;


/**
 * Painter for the background of each tab in a TabbedPane (to configure border and background colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OTabbedPaneTabPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of TabbedPaneTabbedPaneTabPainter to determine which region/state is being painted
	//by that instance.
	public static final int BACKGROUND_ENABLED = 1;
	public static final int BACKGROUND_ENABLED_MOUSEOVER = 2;
	public static final int BACKGROUND_ENABLED_PRESSED = 3;
	public static final int BACKGROUND_DISABLED = 4;
	public static final int BACKGROUND_SELECTED_DISABLED = 5;
	public static final int BACKGROUND_SELECTED = 6;
	public static final int BACKGROUND_SELECTED_MOUSEOVER = 7;
	public static final int BACKGROUND_SELECTED_PRESSED = 8;
	public static final int BACKGROUND_SELECTED_FOCUSED = 9;
	public static final int BACKGROUND_SELECTED_MOUSEOVER_FOCUSED = 10;
	public static final int BACKGROUND_SELECTED_PRESSED_FOCUSED = 11;
	
	/**
     * Segment type for each tab.
     */
    enum TabPosition {
        NONE, FIRST, MIDDLE, LAST
    }


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;


	//the following 4 variables are reused during the painting code of the layers
	protected Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);

	// painters points (global definition to be reused for optimization):
	float[] threeFractions = new float[] { 0.0f, 0.5f, 1.0f };
	float[] fiveFractions = new float[] { 0.0f,0.1f,0.2f,0.6f,1.0f };
	float[] sevenFractions = new float[] { 0.0f,0.1f,0.2f,0.42096776f,0.64193547f,0.82096773f,1.0f };
	float[] nineFractions = new float[] { 0.0f,0.12419355f,0.2483871f,0.42580646f,0.6032258f,0.6854839f,0.7677419f,0.88387096f,1.0f };

	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of TabbedPaneTabbedPaneTabPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = decodeColor("nimbusBase", 0.032459438f, -0.55535716f, -0.109803945f, 0);
	protected final Color color2 = decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.4784314f, 0);
	protected final Color color3 = new Color(0xE4E4E4); //Default Enabled BgPainter.
	protected final Color color6 = decodeColor("nimbusBase", 0.032459438f, -0.54616207f, -0.02352941f, 0);
	protected final Color color7 = new Color(0xCCCCCC); //Default EnabledAndMouseOver BgPainter.
	protected final Color color10 = decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.52156866f, 0);
	protected final Color color11 = new Color(0x77ACD0); //Default enabledAndPressed BgPainter.
	protected final Color color15 = decodeColor("nimbusBase", 0.032459438f, -0.59181184f, 0.25490195f, 0);
	protected final Color color16 = decodeColor("nimbusBase", 0.05468172f, -0.58308274f, 0.19607842f, 0);
	protected final Color color17 = new Color(0x969396); //Default disabled BgPainter, selectedAndDisabled BgPainter
	protected final Color color19 = new Color(0x77acd0); //Default selected BgPainter, selectedAndMouseOver BgPainter.
	protected final Color color20 = new Color(0x6c9cbc); //Default focus BgPainter.
	protected final Color color24 = decodeColor("nimbusBase", 5.1498413E-4f, -0.08776909f, -0.2627451f, 0);
	protected final Color color25 = decodeColor("nimbusBase", 0.06332368f, 0.3642857f, -0.4431373f, 0);
	protected final Color color31 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
	protected final Color color37 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);

	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorEnabledMouseOver;
	protected Paint backgroundColorEnabledPressed;
	protected Paint backgroundColorDisabled;
	protected Paint backgroundColorDisabledSelected;
	protected Paint backgroundColorSelected;
	protected Paint backgroundColorMouseOverSelected;
	protected Paint backgroundColorPressedSelected;
	protected Paint backgroundColorFocusedSelected;
	protected Paint backgroundColorFocusedMouseOverSelected;
	protected Paint backgroundColorFocusedPressedSelected;



	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OTabbedPaneTabPainter(int state, PaintContext ctx) {
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
		case BACKGROUND_ENABLED: paintBackgroundEnabled(g,c,width,height); break;
		case BACKGROUND_ENABLED_MOUSEOVER: paintBackgroundEnabledAndMouseOver(g,c,width,height); break;
		case BACKGROUND_ENABLED_PRESSED: paintBackgroundEnabledAndPressed(g,c,width,height); break;
		case BACKGROUND_DISABLED: paintBackgroundDisabled(g,c); break;
		case BACKGROUND_SELECTED_DISABLED: paintBackgroundSelectedAndDisabled(g,c); break;
		case BACKGROUND_SELECTED: paintBackgroundSelected(g,c,width,height); break;
		case BACKGROUND_SELECTED_MOUSEOVER: paintBackgroundSelectedAndMouseOver(g,c); break;
		case BACKGROUND_SELECTED_PRESSED: paintBackgroundSelectedAndPressed(g,c); break;
		case BACKGROUND_SELECTED_FOCUSED: paintBackgroundSelectedAndFocused(g,c); break;
		case BACKGROUND_SELECTED_MOUSEOVER_FOCUSED: paintBackgroundSelectedAndMouseOverAndFocused(g,c); break;
		case BACKGROUND_SELECTED_PRESSED_FOCUSED: paintBackgroundSelectedAndPressedAndFocused(g,c); break;
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

		// enable:
		Object obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane:TabbedPaneTab[Enabled].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}else{
			backgroundColorEnabled = color3;
		}

		// enable+Mouseover
		obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane:TabbedPaneTab[Enabled+MouseOver].background");
		if(obj instanceof Paint){
			backgroundColorEnabledMouseOver = (Paint)obj;
		}else{
			backgroundColorEnabledMouseOver = color7;
		}

		// enable+Pressed
		obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane:TabbedPaneTab[Enabled+Pressed].background");
		if(obj instanceof Paint){
			backgroundColorEnabledPressed = (Paint)obj;
		}else{
			backgroundColorEnabledPressed = color11;
		}

		// disabled
		obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane:TabbedPaneTab[Disabled].background");
		if(obj instanceof Paint){
			backgroundColorDisabled = (Paint)obj;
		}else{
			backgroundColorDisabled = color17;
		}

		// disabled+selected
		obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane:TabbedPaneTab[Disabled+Selected].background");
		if(obj instanceof Paint){
			backgroundColorDisabledSelected = (Paint)obj;
		}else{
			backgroundColorDisabledSelected = color17;
		}

		// selected
		obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane:TabbedPaneTab[Selected].background");
		if(obj instanceof Paint){
			backgroundColorSelected = (Paint)obj;
		}else{
			backgroundColorSelected = color19;
		}

		// mouseover + selected
		obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane:TabbedPaneTab[MouseOver+Selected].background");
		if(obj instanceof Paint){
			backgroundColorMouseOverSelected = (Paint)obj;
		}else{
			backgroundColorMouseOverSelected = color19;
		}

		// pressed + selected
		obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane:TabbedPaneTab[Pressed+Selected].background");
		if(obj instanceof Paint){
			backgroundColorPressedSelected = (Paint)obj;
		}else{
			backgroundColorPressedSelected = color19;
		}

		// focused + selected
		obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane:TabbedPaneTab[Focused+Selected].background");
		if(obj instanceof Paint){
			backgroundColorFocusedSelected = (Paint)obj;
		}else{
			backgroundColorFocusedSelected = color20;
		}

		// focused + mouseover + selected
		obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane:TabbedPaneTab[Focused+MouseOver+Selected].background");
		if(obj instanceof Paint){
			backgroundColorFocusedMouseOverSelected = (Paint)obj;
		}else{
			backgroundColorFocusedMouseOverSelected = color19;
		}

		// focused + pressed + selected
		obj = UIManager.getLookAndFeelDefaults().get( "TabbedPane:TabbedPaneTab[Focused+Pressed+Selected].background");
		if(obj instanceof Paint){
			backgroundColorFocusedPressedSelected = (Paint)obj;
		}else{
			backgroundColorFocusedPressedSelected = color19;
		}

	}


	protected void paintBackgroundEnabled(Graphics2D g, JComponent c, int width, int height) {
		
		paintBackground(g, c, backgroundColorEnabled);
		paintBorder(g, c, false);

	}


	protected void paintBackgroundEnabledAndMouseOver(Graphics2D g, JComponent c, int width, int height) {

		paintBackground(g, c, backgroundColorEnabledMouseOver);
		paintBorder(g, c, false);
	}



	protected void paintBackgroundEnabledAndPressed(Graphics2D g, JComponent c, int width, int height) {
		
		paintBackground(g, c, backgroundColorEnabledPressed);
		paintBorder(g, c, false);
	}


	protected void paintBackgroundSelected(Graphics2D g, JComponent c, int width, int height) {
		
		paintBackground(g, c, backgroundColorSelected);
		paintBorder(g, c, true);
	}


	protected void paintBackgroundDisabled(Graphics2D g, JComponent c) {

		paintBackground(g, c, backgroundColorDisabled);
		paintBorder(g, c, false);

	}

	protected void paintBackgroundSelectedAndDisabled(Graphics2D g, JComponent c) {
		
		paintBackground(g, c, backgroundColorDisabledSelected);
		paintBorder(g, c, false);

	}


	protected void paintBackgroundSelectedAndMouseOver(Graphics2D g, JComponent c) {
		
		paintBackground(g, c, backgroundColorMouseOverSelected);
		paintBorder(g, c, true);

	}

	protected void paintBackgroundSelectedAndPressed(Graphics2D g, JComponent c) {
		
		paintBackground(g, c, backgroundColorPressedSelected);
		paintBorder(g, c, true);

	}

	protected void paintBackgroundSelectedAndFocused(Graphics2D g, JComponent c) {
		
		paintBackground(g, c, backgroundColorFocusedSelected);
		paintBorder(g, c, true);
		
	}

	protected void paintBackgroundSelectedAndMouseOverAndFocused(Graphics2D g, JComponent c) {
		
		paintBackground(g, c, backgroundColorFocusedMouseOverSelected);
		paintBorder(g, c, true);

	}

	protected void paintBackgroundSelectedAndPressedAndFocused(Graphics2D g, JComponent c) {
		
		paintBackground(g, c, backgroundColorFocusedPressedSelected);
		paintBorder(g, c, true);

	}
	
	/**
     * Get the segment type (if any) from the component's client properties.
     *
     * @param  c the component.
     *
     * @return the segment status.
     */
    protected TabPosition getTabPosition(JComponent c) {
        TabPosition segmentType = TabPosition.NONE;

        String position = (String) c.getClientProperty("JTabbedPane.Tab.segmentPosition");

        if ("first".equals(position)) {
            segmentType = TabPosition.FIRST;
        } else if ("middle".equals(position)) {
            segmentType = TabPosition.MIDDLE;
        } else if ("last".equals(position)) {
            segmentType = TabPosition.LAST;
        }

        return segmentType;
    }

    protected int getTabPlacement(JComponent c){
    	if( c instanceof JTabbedPane){
    		int placement = ((JTabbedPane)c).getTabPlacement();
    		return placement;
    	}
    	return -1;
    }

	
	protected void paintBackground(Graphics2D g, JComponent c, Paint bgColor){
		
		double x = 1;//decodeX(1.0f);
		if(TabPosition.MIDDLE == getTabPosition(c) || TabPosition.LAST == getTabPosition(c)){
			x=0;
		}
    	double y = 1;//decodeY(1.0f);
    	double w = decodeX(3.0f)-1;//decodeX(2.0f) - decodeX(1.0f);
    	double h = decodeY(3.0f)-2;//decodeY(2.0f) - decodeY(1.0f);
    	
    	TabPosition position = getTabPosition(c);
    	
    	int placement = getTabPlacement(c);
    	if(JTabbedPane.RIGHT == placement){
    		y--;
    	}else if(JTabbedPane.BOTTOM == placement){
    		y--;
    	}
    	
    	double hUpperShape = decodeY(1.368925f);
    	double yBottomShape = decodeY(3.0f)-decodeY(1.528f)+5;
    	double hBottomShape = decodeY(3.0f)-yBottomShape;
    	

		Shape s = createBackgroundTabShape(x,y,w,h,position);
		g.setPaint( bgColor );
		g.fill(s);
		
		s = createInteriorTabUpperShape(x,y,w,hUpperShape,position);
		g.setPaint(decodeGradientInteriorTabUpperShape(s));
		g.fill(s);
		
		s = createInteriorTabBottomShape(x,yBottomShape,w,hBottomShape,position);
		g.setPaint(decodeGradientInteriorTabBottomShape(s));
		g.fill(s);
	}
	
	protected void paintBorder(Graphics2D g, JComponent c, boolean focused){
		
		int tabCount = ((JTabbedPane)c).getTabCount();
		boolean even = (tabCount % 2)==0;//par
		
		double x = decodeX(0.0f);
    	double y = decodeY(0.0f);
    	double w = decodeX(3.0f)-1;
    	double h = decodeY(3.0f)-1;
		
    	TabPosition position = getTabPosition(c);
		switch (position) {
		case FIRST:
			//Painting superior and bottom lines...
			g.setPaint(getBorderPaint(focused, false));
			decodeExteriorBordersShape(position, true);
			g.draw(path);
			decodeExteriorBordersShape(position, false);
			g.draw(path);
			
			//Painting interior separator lines...
			if (focused || even) {
				g.setPaint(getBorderPaint(focused, true));
				decodeInteriorBordersShape(false);
				g.draw(path);
			}
			
			decodeFirstTabInteriorBorderShape(x, y, w, h);
			g.setPaint(decodeGradientBorderShape(path,focused));
			g.draw(path);
			
			break;
		case MIDDLE:
			//Painting superior and bottom lines...
			g.setPaint(getBorderPaint(focused, false));
			decodeExteriorBordersShape(position, true);
			g.draw(path);
			decodeExteriorBordersShape(position, false);
			g.draw(path);
			
			//Painting interior separator lines...
			g.setPaint(getBorderPaint(focused, true));
			if (focused || !even) {
				decodeInteriorBordersShape(true);
				g.draw(path);
			}
			if (focused || even) {
				decodeInteriorBordersShape(false);
				g.draw(path);
			}	
			
			break;
		case LAST:
			//Painting superior and bottom lines...
			g.setPaint(getBorderPaint(focused, false));
			decodeExteriorBordersShape(position, true);
			g.draw(path);
			decodeExteriorBordersShape(position, false);
			g.draw(path);
			
			//Painting interior separator lines...
			if (focused || !even) {
				g.setPaint(getBorderPaint(focused, true));
				decodeInteriorBordersShape(true);
				g.draw(path);
			}
			
			decodeLastTabInteriorBorderShape(x, y, w, h);
			g.setPaint(decodeGradientBorderShape(path,focused));
			g.draw(path);

			break;
		default:
			break;
		}
		
//		if(s != null){
//		g.setPaint( OntimizeLAFPainterUtils.getPaint(s, borderColor ));
//		g.draw(s);
//		}
		
	}
	
	protected Paint getBorderPaint(boolean focused, boolean interior){
		Paint p = null;
		if(focused){
			if(interior){
				p = OntimizeLAFParseUtils.parseColor("#5195C3", null);
//				p = OntimizeLAFParseUtils.parseColor("#61bee8", null);
			}else{
				p = OntimizeLAFParseUtils.parseColor("#21465E", null);
//				p = OntimizeLAFParseUtils.parseColor("#61bee8", null);
			}
		}else{
			if(interior){
				p = OntimizeLAFParseUtils.parseColor("#8A8A8A", null);
			}else{
				p = OntimizeLAFParseUtils.parseColor("#737373", null);
			}
		}
		return p;
	}
	
	
	protected Shape createBackgroundTabShape(double x, double y, double w, double h, TabPosition position){
		
		switch (position) {
		case FIRST:
			return decodeFirstTabShape(x, y, w, h);
		case MIDDLE:
			return ShapeFactory.getInstace().createRectangle(x, y, w, h); 
		case LAST:
			return decodeLastTabShape(x, y, w, h);
		default:
			return decodeButtonTabShape(x, y, w, h);
		}
	}
	
	
	/**
     * Create the upper shape for the interior of the Tab.
     *
     * @param  position the position of the tab (first, middle, last or none(none when there is only one tab) ).
     *
     * @return the shape of the interior.
     */
    protected Shape createInteriorTabUpperShape(double x, double y, double w, double h, TabPosition position) {
    	path.reset();
    	
        switch (position) {

        case FIRST:
        	return decodeFirstInteriorTabUpperShape(x, y, w);

        case MIDDLE:
            return ShapeFactory.getInstace().createRectangle(x, y , w , h);
            
        case LAST:
        	w=w-1;
        	return decodeLastInteriorTabUpperShape(x, y, w);

        default:
            return decodeButtonInteriorTabUpperShape(x,y,w);
        }
    }


	/**
     * Create the bottom shape for the interior of the Tab.
     *
     * @param  position the position of the tab (first, middle, last or none(none when there is only one tab) ).
     *
     * @return the shape of the interior.
     */
    protected Shape createInteriorTabBottomShape(double x, double y, double w, double h, TabPosition position) {
    	path.reset();
    	
        switch (position) {

        case FIRST:
        	return decodeFirstInteriorTabBottomShape(x, y, w);

        case MIDDLE:
            return ShapeFactory.getInstace().createRectangle(x, y , w , h);

        case LAST:
        	w=w-1;
        	return decodeLastInteriorTabBottomShape(x, y, w);

        default:
            return decodeButtonInteriorTabBottomShape(x, y, w);
        }
    }
    
    protected Shape decodeFirstTabShape(double x, double y, double w, double h){
    	path.reset();
    	path.moveTo(x+3, y);
		path.curveTo(x+1.5, y, x, y+1.5, x, y+3);
		path.lineTo(x, y+h-3);
		path.curveTo(x, y+h-1.5, x+1.5, y+h, x+3, y+h);
		path.lineTo(x+w, y+h);
		path.lineTo(x+w, y);
		path.closePath();
		
    	return path;
    }

    protected Shape decodeLastTabShape(double x, double y, double w, double h){
    	path.reset();
    	path.moveTo(x, y);
    	path.lineTo(x+w-3, y);
		path.curveTo(x+w-1.5, y, x+w, y+1.5, x+w, y+3);
		path.lineTo(x+w, y+h-3);
		path.curveTo(x+w, y+h-1.5, x+w-1.5, y+h, x+w-3, y+h);
		path.lineTo(x, y+h);
		path.closePath();
    	
    	return path;
    }
    
    protected Shape decodeButtonTabShape(double x, double y, double w, double h){
    	path.reset();
    	path.moveTo(x+3, y);
		path.curveTo(x+1.5, y, x, y+1.5, x, y+3);
		path.lineTo(x, y+h-3);
		path.curveTo(x, y+h-1.5, x+1.5, y+h, x+3, y+h);
		path.lineTo(x+w-3, y+h);
		path.curveTo(x+w-1.5, y+h, x+w, y+h-1.5, x+w, y+h-3);
		path.lineTo(x+w, y+3);
		path.curveTo(x+w, y+1.5, x+w-1.5, y, x+w-3, y);
		path.lineTo(x+3, y);
		path.closePath();
		
    	return path;
    }
	
    protected Shape decodeFirstInteriorTabUpperShape(double x, double y, double w){
    	path.reset();
    	path.moveTo(x+1, y);
    	path.lineTo(x+1, decodeY(1.215f)); //y+4
    	path.curveTo(x+1, decodeY(1.32f), x+2.5, decodeY(1.4225f), x+5, decodeY(1.4225f)); //y+6; y+8
    	path.lineTo(x+w, decodeY(1.4225f));
    	path.lineTo(x+w, y);
    	path.closePath();
    	return path;
    }
    
    protected Shape decodeLastInteriorTabUpperShape(double x, double y, double w){
    	path.reset();
    	path.moveTo(x, y);
    	path.lineTo(x+w-1, y);
    	path.lineTo(x+w-1, decodeY(1.215f));//y+4
    	path.curveTo(x+w-1, decodeY(1.32f), x+w-2.5, decodeY(1.4225f), x+w-5, decodeY(1.4225f)); //y+6; y+8
    	path.lineTo(x, decodeY(1.4225f));
    	path.closePath();
        return path;
    }
    
    protected Shape decodeButtonInteriorTabUpperShape(double x, double y, double w){
    	path.reset();
    	path.moveTo(x+1, y);
    	path.lineTo(x+1, decodeY(1.215f)); //y+4
    	path.curveTo(x+1, decodeY(1.32f), x+2.5, decodeY(1.4225f), x+5, decodeY(1.4225f)); //y+6; y+8
    	path.lineTo(x+w-5, decodeY(1.4225f)); //y+8
    	path.curveTo(x+w-2.5, decodeY(1.4225f), x+w-1, decodeY(1.32f), x+w-1, decodeY(1.215f)); //y+8 y+6; y+4
    	path.lineTo(x+w-1, y);
    	
    	path.closePath();
    	return path;
    }
    
    protected Shape decodeFirstInteriorTabBottomShape(double x, double y, double w){
    	path.reset();
    	path.moveTo(x, decodeY(1.7915f)); //y+5
    	path.curveTo(x, decodeY(1.65825f), x+2.5, y, x+5, y); //y+2.5
    	path.lineTo(x+w, y);
    	path.lineTo(x+w, decodeY(3.0f)-1);
    	path.lineTo(x+3, decodeY(3.0f)-1);
    	path.curveTo(x+1.5, decodeY(3.0f)-1, x, decodeY(3.0f)-2.5, x, decodeY(3.0f)-4);
    	path.closePath();
        return path;
    }
    
    protected Shape decodeLastInteriorTabBottomShape(double x, double y, double w){
    	path.reset();
    	path.moveTo(x, y); 
    	path.lineTo(x+w-5, y);
    	path.curveTo(x+w-2.5, y, x+w, decodeY(1.65825f), x+w, decodeY(1.7915f)); //y+2.5 y+5
    	path.lineTo(x+w, decodeY(3.0f)-1-3);
    	path.curveTo(x+w, decodeY(3.0f)-1-1.5, x+w-1.5, decodeY(3.0f)-1, x+w-3, decodeY(3.0f)-1);
    	path.lineTo(x, decodeY(3.0f)-1);
    	path.closePath();
        return path;
    }
    
    protected Shape decodeButtonInteriorTabBottomShape(double x, double y, double w){
    	path.reset();
    	path.moveTo(x, decodeY(1.7915f)); //y+5
    	path.curveTo(x, decodeY(1.65825f), x+2.5, y, x+5, y); //y+2.5
    	path.lineTo(x+w-5, y);
    	path.curveTo(x+w-2.5, y, x+w, decodeY(1.65825f), x+w, decodeY(1.7915f)); //y+2.5 y+5
    	path.lineTo(x+w, decodeY(2.0f)-3);
    	path.curveTo(x+w, decodeY(2.0f)-1.5, x+w-1.5, decodeY(2.0f), x+w-3, decodeY(2.0f));
    	path.lineTo(x+3, decodeY(2.0f));
    	path.curveTo(x+1.5, decodeY(2.0f), x, decodeY(2.0f)-1.5, x, decodeY(2.0f)-3);
    	
    	path.closePath();
    	return path;
    }
    
    
    protected Shape decodeExteriorBordersShape(TabPosition position, boolean top){
    	double x = decodeX(0.0f);
    	double y = decodeY(0.0f);
    	double w = decodeX(3.0f)-1;
    	double h = decodeY(3.0f)-1;
    	
    	switch (position) {
		case FIRST:
			x = x+3;
			w = w-3;
			break;
		case MIDDLE:
			break;
		case LAST:
			w = w-3;
			break;
		default:
			break;
		}
    	
    	path.reset();
    	if(top){
			path.moveTo(x, y);
			path.lineTo(x+w, y);
    	}else{
			path.moveTo(x, y+h);
			path.lineTo(x+w, y+h);
    	}
    	return path;
    }
    
    protected Shape decodeInteriorBordersShape(boolean left){
    	double x = decodeX(0.0f);
    	double y = decodeY(0.0f)+1;
    	double w = decodeX(3.0f)-1;
    	double h = decodeY(3.0f)-3;
    	
    	path.reset();
    	if(left){
			path.moveTo(x, y);
			path.lineTo(x, y+h);
    	}else{
			path.moveTo(x+w, y);
			path.lineTo(x+w, y+h);
    	}
    	return path;
    }
    
    protected Shape decodeFirstTabInteriorBorderShape(double x, double y, double w, double h){
    	path.reset();
    	path.moveTo(x+3, y);
		path.curveTo(x+1.5, y, x, y+1.5, x, y+3);
		path.lineTo(x, y+h-3);
		path.curveTo(x, y+h-1.5, x+1.5, y+h, x+3, y+h);
		
    	return path;
    }
    
    protected Shape decodeLastTabInteriorBorderShape(double x, double y, double w, double h){
    	path.reset();
    	path.moveTo(x+w-3, y);
		path.curveTo(x+w-1.5, y, x+w, y+1.5, x+w, y+3);
		path.lineTo(x+w, y+h-3);
		path.curveTo(x+w, y+h-1.5, x+w-1.5, y+h, x+w-3, y+h);
    	
    	return path;
    }
    
    protected Paint decodeGradientInteriorTabUpperShape(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float h = (float)bounds.getHeight();
        return decodeGradient( x, y, x, y+h,
                new float[] { 0.0f, 1.0f },
                new Color[] { OntimizeLAFColorUtils.setAlpha(Color.white, 0.4),
        		OntimizeLAFColorUtils.setAlpha(Color.white, 0.2)});
    }
    
    protected Paint decodeGradientInteriorTabBottomShape(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float h = (float)bounds.getHeight();
        return decodeGradient( x, y, x, y+h,
                new float[] { 0.0f, 1.0f },
                new Color[] { OntimizeLAFColorUtils.setAlpha(Color.white, 0.05),
        		OntimizeLAFColorUtils.setAlpha(Color.white, 0.5)});
    }
    
    protected Paint decodeGradientBorderShape(Shape s, boolean focused) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float h = (float)bounds.getHeight();
        
        if(focused){
	        return decodeGradient( x, y, x, y+h,
	                new float[] { 0.0f, 0.19f, 0.76f, 0.9f, 1.0f },
	                new Color[] { OntimizeLAFParseUtils.parseColor("#21465E", null),
	        		OntimizeLAFParseUtils.parseColor("#21465E", null),
	        		OntimizeLAFParseUtils.parseColor("#5095c3", null),
	        		OntimizeLAFParseUtils.parseColor("#37749d", null),
	        		OntimizeLAFParseUtils.parseColor("#254f6b", null)});
        }else{
        	return decodeGradient( x, y, x, y+h,
	                new float[] { 0.0f, 0.19f, 0.76f, 0.9f, 1.0f },
	                new Color[] { OntimizeLAFParseUtils.parseColor("#737373", null),
	        		OntimizeLAFParseUtils.parseColor("#6E6E6E", null),
	        		OntimizeLAFParseUtils.parseColor("#cdcdcd", null),
	        		OntimizeLAFParseUtils.parseColor("#7a7a7a", null),
	        		OntimizeLAFParseUtils.parseColor("#535353", null)});
        }
    }

}