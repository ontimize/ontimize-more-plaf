package com.ontimize.plaf.painter;


import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.utils.DerivedColor;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;


/**
 * Painter for the background of the pop up (to configure border and foreground colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OPopupMenuPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of OPopupMenuPainter to determine which region/state is being painted
	//by that instance.
	public static final int BACKGROUND_DISABLED = 1;
	public static final int BACKGROUND_ENABLED = 2;


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;
	

	// painter to fill the component:
	protected Paint backgroundColorDisabled;
	protected Paint backgroundColorEnabled;
	protected Paint borderColorDisabled;
	protected Paint borderColorEnabled;


	float[] fractions = new float[] { 0.0f,0.0030f,0.02f,0.5f,0.98f,0.996f,1.0f };


	//the following 4 variables are reused during the painting code of the layers
	protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);
	protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of OPopupMenuPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.39607844f, 0);
	protected final Color color2 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);
	protected final Color color3 = decodeColor("nimbusBase", 0.021348298f, -0.6150531f, 0.39999998f, 0);
	protected final Color color4 = new Color(0xc6dfe3);

	public static Color defaultShadowColor = Color.black;
	public static int defaultShadowWidth = 6;
	protected int shadowWidth;

	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OPopupMenuPainter(int state, PaintContext ctx) {
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
		case BACKGROUND_DISABLED: paintBackground(g,c); break;
		case BACKGROUND_ENABLED: paintBackground(g,c); break;
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

		// enable:
		Object obj = UIManager.getLookAndFeelDefaults().get(getComponentKeyName() + "[Enabled].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}else{
			backgroundColorEnabled = color2;
		}
		obj = UIManager.getLookAndFeelDefaults().get(getComponentKeyName() + "[Enabled].border");
		if(obj instanceof Paint){
			borderColorEnabled = (Paint)obj;
		}else{
			borderColorEnabled = color4;
		}
		
		// disable:
		obj = UIManager.getLookAndFeelDefaults().get(getComponentKeyName() + "[Disabled].background");
		if(obj instanceof Paint){
			backgroundColorDisabled = (Paint)obj;
		}else{
			backgroundColorDisabled = color2;
		}
		obj = UIManager.getLookAndFeelDefaults().get(getComponentKeyName() + "[Disabled].border");
		if(obj instanceof Paint){
			borderColorDisabled = (Paint)obj;
		}else{
			borderColorDisabled = color4;
		}
		
		
		Object oShadowWidth = UIManager.getDefaults().get("PopupMenu.shadowWidth");
		if(oShadowWidth instanceof Number){
			shadowWidth = ((Number)oShadowWidth).intValue();
		}else{
			shadowWidth = defaultShadowWidth;
		}
	}

	protected String getComponentKeyName(){
		return "PopupMenu";
	}

	protected Paint getBorderColorForState(Shape s){
		switch(state) {
			case BACKGROUND_DISABLED: return borderColorDisabled;
			case BACKGROUND_ENABLED: return borderColorEnabled;
		}
		return Color.white;
	}

	
	protected Paint getBackgroundColorForState(Shape s){
		Color color = null;
		switch(state) {
			case BACKGROUND_DISABLED: color = (Color) backgroundColorDisabled;
			case BACKGROUND_ENABLED: color = (Color) backgroundColorEnabled;
		}
		if(color!=null){
			return decodeGradient(s, color);
		}
		return Color.white;
	}



	/**
	 * Note that menu and menu item elements are overpainted in the pop up, so the popup menu background painter can be hidden by menus painters
	 *
	 * @param g
	 */
	protected void paintBackground(Graphics2D g, JComponent c) {
		
		if(c instanceof JPopupMenu){
			Component invoker = ((JPopupMenu)c).getInvoker();
			if(invoker instanceof JComponent){
				
				if(invoker.getParent() instanceof JPopupMenu){
					boolean rightPos = isChildOnRightPosition((JComponent)invoker, c);
					
					paintShadow(g, (JComponent)invoker, c);
					
					Shape s = decodeBorderPopupMenu((JComponent)invoker, rightPos);
					g.setPaint( getBorderColorForState(s));
					g.fill(s);
					
					s = ShapeFactory.getInstace().createRectangle(decodeX(1.0f), decodeY(1.0f), decodeX(2.0f)-decodeX(1.0f), decodeY(2.0f)-decodeY(1.0f));
					g.setPaint( getBackgroundColorForState(s));
					g.fill(s);
					
					s = decodeUpperFillerPopupMenu((JComponent)invoker, rightPos);
					g.fill(s);
					
					s = decodeBottomFiller();
					g.fill(s);
				}else if (invoker.getParent() instanceof JMenuBar){
					
					paintShadow(g, (JComponent)invoker, c);
					//Painting border
					//TODO now is an opaque shape painted before background. To performance it could be good to paint just the lines of the border.
					Shape s = decodeBorderMenuBar(c, (JComponent)invoker);
					g.setPaint( getBorderColorForState(s));
					g.fill(s);
		
					s = ShapeFactory.getInstace().createRectangle(decodeX(1.0f), decodeY(1.0f), decodeX(2.0f)-decodeX(1.0f), decodeY(2.0f)-decodeY(1.0f));
					g.setPaint( getBackgroundColorForState(s));
					g.fill(s);
					
					s = decodeUpperFillerMenuBar(c, (JComponent)invoker);
					g.fill(s);
					
					s = decodeBottomFiller();
					g.fill(s);
				} else{
					
					paintShadow(g, (JComponent)invoker, null);
					
					Shape s = decodeBorder((JComponent)invoker);
					g.setPaint( getBorderColorForState(s));
					g.fill(s);
					
					s = ShapeFactory.getInstace().createRectangle(decodeX(1.0f), decodeY(1.0f), decodeX(2.0f)-decodeX(1.0f), decodeY(2.0f)-decodeY(1.0f));
					g.setPaint( getBackgroundColorForState(s));
					g.fill(s);
					
					s = decodeUpperFiller((JComponent)invoker);
					g.fill(s);
					
					s = decodeBottomFiller();
					g.fill(s);
				}
				
			}
			
		}
	}
	
	/**
	 * Method to know whether child PopupMenu is opened on the right or on the left of the invoker component. The invoker component
	 * is an JMenu placed into a JPopupMenu.
	 * @param invoker Component that provokes that a new PopupMenu will be displayed.
	 * @param component the PopupMenu.
	 * @return true if child PopupMenu is placed on the right of invoker component.
	 */
	protected boolean isChildOnRightPosition(JComponent invoker, JComponent component){
		if(invoker!=null && component!=null	){
			Point parentLocation=null;
			if(invoker.getParent()!=null ){
				parentLocation = invoker.getParent().getLocationOnScreen();
			}
			Point location = component.getLocationOnScreen();
			if(parentLocation!=null && location!=null){
				if(parentLocation.getX()<=location.getX())
					return true;
				if(parentLocation.getX()>location.getX())
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Method to know whether invoker component is going to open the child PopupMenu on the right or on the left. The invoker component
	 * is an JMenu placed into a JMenuBar.
	 * @param invoker Component that provokes that a new PopupMenu will be displayed.
	 * @param component the PopupMenu.
	 * @return true if invoker component is placed on the left of child PopupMenu.
	 */
	protected boolean isInvokerOnLeftPosition(JComponent invoker, JComponent component){
		if(invoker!=null && component!=null	){
			Point invokerLocation=invoker.getLocationOnScreen();
			Point location = component.getLocationOnScreen();
			if(invokerLocation!=null && location!=null){
				if(invokerLocation.getX()<=location.getX())
					return true;
				if(invokerLocation.getX()>location.getX())
					return false;
			}
		}
		
		return true;
	}
	
	protected void paintShadow(Graphics2D g, JComponent invoker, JComponent c){
		int numShadowBorders = 6;
		
		Container parentContainer = invoker.getParent();
		for(int i=0;i<numShadowBorders;i++){
			Shape shadow = null;
			if(parentContainer instanceof JMenuBar){
				shadow = decodeShadowMenuBar(i, i, invoker, c);
			} else if(parentContainer instanceof JPopupMenu){
				boolean rightPosition = isChildOnRightPosition(invoker, c);
				shadow = decodeShadowPopupMenu(i, i, rightPosition);
			}else{
				shadow = decodeShadow(i, i);
			}
			Color shadowColor = defaultShadowColor;
			if(i==0){
				shadowColor = OntimizeLAFColorUtils.setAlpha(defaultShadowColor, 0.05);
			}else{
				shadowColor = OntimizeLAFColorUtils.setAlpha(defaultShadowColor, (i/10.0));
			}
			g.setPaint(shadowColor);
			g.draw(shadow);
		}
		
	}
	
	protected Shape decodeShadowMenuBar(int x, int y, JComponent invoker, JComponent c){
		double arcRadius = 9;
		double numBorders = 3;
		int xOffset = 3;
		int yOffset = 3;
		
		int width = invoker.getBounds().width;
		width = width - invoker.getInsets().left - invoker.getInsets().right;
		
		int compWidth = c.getBounds().width;
		compWidth = compWidth - c.getInsets().left - c.getInsets().right;
		
		boolean leftPos = isInvokerOnLeftPosition((JComponent)invoker, c);
		
		path.reset();
		if(leftPos){
			if(width<compWidth){
				path.moveTo(decodeX(1.0f), decodeY(0.0f)+yOffset+y-numBorders);
				path.lineTo(decodeX(3.0f)-2-numBorders-arcRadius, decodeY(0.0f)+yOffset+y-numBorders);
				path.curveTo(decodeX(3.0f)-x-1-arcRadius/2.0, 	decodeY(0.0f)+yOffset+y-numBorders, 
							decodeX(3.0f)-x-1, 					decodeY(0.0f)+yOffset+numBorders-1+arcRadius/2.0, 
							decodeX(3.0f)-x-1, 					decodeY(0.0f)+yOffset+numBorders-1+arcRadius);
			}else{
				path.moveTo(decodeX(3.0f)-x-1, decodeY(0.0f)+yOffset-numBorders);
			}
			
			path.lineTo(decodeX(3.0f)-x-1, decodeY(3.0f)-arcRadius-numBorders-3);
			//Bottom right corner...
			path.curveTo(decodeX(3.0f)-numBorders-x, 				decodeY(3.0f)-y-1-arcRadius/2.0, 
						decodeX(3.0f)-2-numBorders-arcRadius/2.0, 	decodeY(3.0f)-y-1, 
						decodeX(3.0f)-2-numBorders-arcRadius, 		decodeY(3.0f)-y-1);
			
			double xPos = decodeX(0.0f)+xOffset+numBorders+1;
			path.lineTo(xPos+arcRadius, decodeY(3.0f)-y-1);
			//Bottom left corner...
			path.curveTo(decodeX(0.0f)+xOffset-1+x+arcRadius/2.0, 					decodeY(3.0f)-numBorders-y, 
						decodeX(0.0f)+xOffset+x, 					decodeY(3.0f)-3-numBorders-arcRadius/2.0, 
						decodeX(0.0f)+xOffset+x,  					decodeY(3.0f)-3-numBorders-arcRadius);
			
			path.lineTo(decodeX(0.0f)+xOffset+x, decodeY(0.0f));
		}else{
			path.moveTo(decodeX(3.0f)-x-1, decodeY(0.0f)+yOffset-numBorders);
			path.lineTo(decodeX(3.0f)-x-1, decodeY(3.0f)-arcRadius-numBorders-3);
			//Bottom right corner...
			path.curveTo(decodeX(3.0f)-numBorders-x, 				decodeY(3.0f)-y-1-arcRadius/2.0, 
						decodeX(3.0f)-2-numBorders-arcRadius/2.0, 	decodeY(3.0f)-y-1, 
						decodeX(3.0f)-2-numBorders-arcRadius, 		decodeY(3.0f)-y-1);
			
			double xPos = decodeX(0.0f)+xOffset+numBorders+1;
			path.lineTo(xPos+arcRadius, decodeY(3.0f)-y-1);
			//Bottom left corner...
			path.curveTo(decodeX(0.0f)+xOffset-1+x+arcRadius/2.0, 					decodeY(3.0f)-numBorders-y, 
						decodeX(0.0f)+xOffset+x, 					decodeY(3.0f)-3-numBorders-arcRadius/2.0, 
						decodeX(0.0f)+xOffset+x,  					decodeY(3.0f)-3-numBorders-arcRadius);
			
			if(width<compWidth){
				path.lineTo(decodeX(0.0f)+xOffset+x,					decodeY(0.0f)+yOffset+numBorders-1+arcRadius);
				path.curveTo(decodeX(0.0f)+xOffset+x, 					decodeY(0.0f)+yOffset+y-numBorders+arcRadius/2.0, 
							decodeX(0.0f)+numBorders+xOffset+arcRadius/2.0, 		decodeY(0.0f)+yOffset+y-numBorders, 
							decodeX(0.0f)+numBorders+xOffset+arcRadius, 			decodeY(0.0f)+yOffset+y-numBorders);
			}else{
				path.lineTo(decodeX(0.0f), decodeY(0.0f)+yOffset+y-numBorders);
			}
			path.lineTo(decodeX(2.0f), decodeY(0.0f)+yOffset+y-numBorders);
		}
		return path;
	}
	
	protected Shape decodeShadowPopupMenu(int x, int y, boolean rightPosition){
		double arcRadius = 9;
		double numBorders = 3;
		int xOffset = 3;
		int yOffset = 3;
		
		path.reset();
		
		if(rightPosition){
			path.moveTo(decodeX(1.0f), decodeY(0.0f)+yOffset+y-numBorders);
			path.lineTo(decodeX(3.0f)-2-numBorders-arcRadius, decodeY(0.0f)+yOffset+y-numBorders);
			path.curveTo(decodeX(3.0f)-x-1-arcRadius/2.0, 	decodeY(0.0f)+yOffset+y-numBorders, 
						decodeX(3.0f)-x-1, 					decodeY(0.0f)+yOffset+numBorders-1+arcRadius/2.0, 
						decodeX(3.0f)-x-1, 					decodeY(0.0f)+yOffset+numBorders-1+arcRadius);
			path.lineTo(decodeX(3.0f)-x-1, decodeY(3.0f)-arcRadius-numBorders-3);
			//Bottom right corner...
			path.curveTo(decodeX(3.0f)-numBorders-x, 				decodeY(3.0f)-y-1-arcRadius/2.0, 
						decodeX(3.0f)-2-numBorders-arcRadius/2.0, 	decodeY(3.0f)-y-1, 
						decodeX(3.0f)-2-numBorders-arcRadius, 		decodeY(3.0f)-y-1);
			double xPos = decodeX(0.0f)+xOffset+numBorders+1;
			path.lineTo(xPos+arcRadius, decodeY(3.0f)-y-1);
			//Bottom left corner...
			path.curveTo(decodeX(0.0f)+xOffset-1+x+arcRadius/2.0, 					decodeY(3.0f)-numBorders-y, 
						decodeX(0.0f)+xOffset+x, 					decodeY(3.0f)-3-numBorders-arcRadius/2.0, 
						decodeX(0.0f)+xOffset+x,  					decodeY(3.0f)-3-numBorders-arcRadius);
			path.lineTo(decodeX(0.0f)+xOffset+x, decodeY(0.0f));
		}else{
			path.moveTo(decodeX(0.0f)+xOffset+x+arcRadius, decodeY(0.0f)+yOffset+y-numBorders);
			path.lineTo(decodeX(3.0f)-x-1, decodeY(0.0f)+yOffset+y-numBorders);
			path.lineTo(decodeX(3.0f)-x-1, decodeY(3.0f)-arcRadius-numBorders-3);
			//Bottom right corner...
			path.curveTo(decodeX(3.0f)-numBorders-x, 				decodeY(3.0f)-y-1-arcRadius/2.0, 
						decodeX(3.0f)-2-numBorders-arcRadius/2.0, 	decodeY(3.0f)-y-1, 
						decodeX(3.0f)-2-numBorders-arcRadius, 		decodeY(3.0f)-y-1);
			double xPos = decodeX(0.0f)+xOffset+numBorders+1;
			path.lineTo(xPos+arcRadius, decodeY(3.0f)-y-1);
			//Bottom left corner...
			path.curveTo(decodeX(0.0f)+xOffset-1+x+arcRadius/2.0, 					decodeY(3.0f)-numBorders-y, 
						decodeX(0.0f)+xOffset+x, 					decodeY(3.0f)-3-numBorders-arcRadius/2.0, 
						decodeX(0.0f)+xOffset+x,  					decodeY(3.0f)-3-numBorders-arcRadius);
			
			path.lineTo(decodeX(0.0f)+xOffset+x, decodeY(0.0f)+yOffset+numBorders-1+arcRadius);
			path.curveTo(decodeX(0.0f)+xOffset+x, 			decodeY(0.0f)+yOffset-numBorders+arcRadius/2.0, 
					decodeX(0.0f)+xOffset+x+arcRadius/2.0, 	decodeY(0.0f)+yOffset+y-numBorders, 
					decodeX(0.0f)+xOffset+x+arcRadius, 		decodeY(0.0f)+yOffset+y-numBorders);
		}
		
		return path;
	}
	
	protected Shape decodeShadow(int x, int y){
		double arcRadius = 9;
		double numBorders = 3;
		int xOffset = 3;
		int yOffset = 3;
		
		path.reset();
		path.moveTo(decodeX(0.0f)+xOffset+numBorders+1+arcRadius, decodeY(0.0f)+yOffset+y-numBorders);
		
		path.lineTo(decodeX(3.0f)-2-numBorders-arcRadius, decodeY(0.0f)+yOffset+y-numBorders);
		path.curveTo(decodeX(3.0f)-x-1-arcRadius/2.0, 	decodeY(0.0f)+yOffset+y-numBorders, 
					decodeX(3.0f)-x-1, 					decodeY(0.0f)+yOffset+numBorders-1+arcRadius/2.0, 
					decodeX(3.0f)-x-1, 					decodeY(0.0f)+yOffset+numBorders-1+arcRadius);
		
		path.lineTo(decodeX(3.0f)-x-1, decodeY(3.0f)-arcRadius-numBorders-3);
		//Bottom right corner...
		path.curveTo(decodeX(3.0f)-numBorders-x, 				decodeY(3.0f)-y-1-arcRadius/2.0, 
					decodeX(3.0f)-2-numBorders-arcRadius/2.0, 	decodeY(3.0f)-y-1, 
					decodeX(3.0f)-2-numBorders-arcRadius, 		decodeY(3.0f)-y-1);
		
		double xPos = decodeX(0.0f)+xOffset+numBorders+1;
		path.lineTo(xPos+arcRadius, decodeY(3.0f)-y-1);
		//Bottom left corner...
		path.curveTo(decodeX(0.0f)+xOffset-1+x+arcRadius/2.0, 					decodeY(3.0f)-numBorders-y, 
					decodeX(0.0f)+xOffset+x, 					decodeY(3.0f)-3-numBorders-arcRadius/2.0, 
					decodeX(0.0f)+xOffset+x,  					decodeY(3.0f)-3-numBorders-arcRadius);
		
		path.lineTo(decodeX(0.0f)+xOffset+x, decodeY(0.0f)+yOffset+numBorders-1+arcRadius);
		//Top left corner...
		path.curveTo(decodeX(0.0f)+xOffset+x, 							decodeY(0.0f)+yOffset+numBorders-1+arcRadius/2.0, 
				decodeX(0.0f)+xOffset+numBorders+1+arcRadius/2.0, 		decodeY(0.0f)+yOffset+y-numBorders, 
				decodeX(0.0f)+xOffset+numBorders+1+arcRadius, 			decodeY(0.0f)+yOffset+y-numBorders);
		
		path.closePath();
		return path;
	}
	
	protected Shape decodeBorder(JComponent invoker){
		double arcRadius = 9;
		double numBorders = 3;
		
		path.reset();
		path.moveTo(decodeX(1.0f)-numBorders+arcRadius, decodeY(0.0f)+numBorders);
		path.lineTo(decodeX(2.0f)+numBorders - arcRadius, decodeY(0.0f)+numBorders);
		path.curveTo(decodeX(2.0f)+numBorders-arcRadius/2.0, decodeY(0.0f)+numBorders, 
					decodeX(2.0f)+numBorders, decodeY(0.0f)+numBorders+arcRadius/2.0, 
					decodeX(2.0f)+numBorders, decodeY(0.0f)+numBorders+arcRadius);
		path.lineTo(decodeX(2.0f)+numBorders, decodeY(3.0f)-numBorders-arcRadius);
		path.curveTo(decodeX(2.0f)+numBorders, decodeY(3.0f)-2*numBorders-arcRadius/2.0, 
					decodeX(2.0f)+numBorders-arcRadius/2.0, decodeY(3.0f)-2*numBorders, 
					decodeX(2.0f)+numBorders-arcRadius, decodeY(3.0f)-2*numBorders);
		path.lineTo(decodeX(1.0f)-numBorders+arcRadius, decodeY(3.0f)-2*numBorders);
		path.curveTo(decodeX(1.0f)-numBorders+arcRadius/2.0, decodeY(3.0f)-2*numBorders, 
					decodeX(1.0f)-numBorders, decodeY(3.0f)-2*numBorders-arcRadius/2.0, 
					decodeX(1.0f)-numBorders, decodeY(3.0f)-2*numBorders-arcRadius);
		path.lineTo(decodeX(1.0f)-numBorders, decodeY(0.0f)+numBorders+arcRadius);
		path.curveTo(decodeX(1.0f)-numBorders, decodeY(0.0f)+numBorders+arcRadius/2.0, 
					decodeX(1.0f)-numBorders+arcRadius/2.0, decodeY(0.0f)+numBorders, 
					decodeX(1.0f)-numBorders+arcRadius, decodeY(0.0f)+numBorders);
		
		path.closePath();
		return path;
	}

	protected Shape decodeBorderMenuBar(JComponent c, JComponent invoker){
		double arcRadius = 9;
		double numBorders = 3;
		
		int width = invoker.getBounds().width;
		int compWidth = c.getBounds().width;
		
		boolean leftPos = isInvokerOnLeftPosition((JComponent)invoker, c);
		
		path.reset();
		if(leftPos){
			path.moveTo(decodeX(1.0f)-numBorders, decodeY(0.0f));
			if(width<compWidth){
				double middlepoint = width-2*shadowWidth-numBorders;
				path.lineTo(decodeX(1.0f)+middlepoint, decodeY(0.0f));
				path.lineTo(decodeX(1.0f)+middlepoint, decodeY(0.0f)+numBorders);
				path.lineTo(decodeX(2.0f)+numBorders - arcRadius, decodeY(0.0f)+numBorders);
				path.curveTo(decodeX(2.0f)+numBorders-arcRadius/2.0, decodeY(0.0f)+numBorders, 
							decodeX(2.0f)+numBorders, decodeY(0.0f)+numBorders+arcRadius/2.0, 
							decodeX(2.0f)+numBorders, decodeY(0.0f)+numBorders+arcRadius);
			}else{
				path.lineTo(decodeX(2.0f)+numBorders, decodeY(0.0f)-numBorders);
			}
			
			path.lineTo(decodeX(2.0f)+numBorders, decodeY(3.0f)-numBorders-arcRadius);
			path.curveTo(decodeX(2.0f)+numBorders, decodeY(3.0f)-2*numBorders-arcRadius/2.0, decodeX(2.0f)+numBorders-arcRadius/2.0, decodeY(3.0f)-2*numBorders, decodeX(2.0f)+numBorders-arcRadius, decodeY(3.0f)-2*numBorders);
			path.lineTo(decodeX(1.0f)-numBorders+arcRadius, decodeY(3.0f)-2*numBorders);
			path.curveTo(decodeX(1.0f)-numBorders+arcRadius/2.0, decodeY(3.0f)-2*numBorders, decodeX(1.0f)-numBorders, decodeY(3.0f)-2*numBorders-arcRadius/2.0, decodeX(1.0f)-numBorders, decodeY(3.0f)-2*numBorders-arcRadius);
			path.lineTo(decodeX(1.0f)-numBorders, decodeY(0.0f));
			
			path.closePath();
		}else{
			if(width<compWidth){
				path.moveTo(decodeX(1.0f)-numBorders, decodeY(0.0f)+numBorders+arcRadius);
				path.curveTo(decodeX(1.0f)-numBorders, decodeY(0.0f)+numBorders+arcRadius/2.0, 
						decodeX(1.0f)-numBorders+arcRadius/2.0, decodeY(0.0f)+numBorders, 
						decodeX(1.0f)-numBorders+arcRadius, decodeY(0.0f)+numBorders);
				double middlepoint = width-2*shadowWidth-1*numBorders;
				path.lineTo(decodeX(2.0f)-middlepoint, decodeY(0.0f)+numBorders);
				path.lineTo(decodeX(2.0f)-middlepoint, decodeY(0.0f));
				path.lineTo(decodeX(2.0f)+numBorders, decodeY(0.0f));
			}else{
				path.moveTo(decodeX(1.0f)-numBorders, decodeY(0.0f));
				path.lineTo(decodeX(2.0f)+numBorders, decodeY(0.0f)-numBorders);
			}
			
			path.lineTo(decodeX(2.0f)+numBorders, decodeY(3.0f)-numBorders-arcRadius);
			path.curveTo(decodeX(2.0f)+numBorders, decodeY(3.0f)-2*numBorders-arcRadius/2.0, decodeX(2.0f)+numBorders-arcRadius/2.0, decodeY(3.0f)-2*numBorders, decodeX(2.0f)+numBorders-arcRadius, decodeY(3.0f)-2*numBorders);
			path.lineTo(decodeX(1.0f)-numBorders+arcRadius, decodeY(3.0f)-2*numBorders);
			path.curveTo(decodeX(1.0f)-numBorders+arcRadius/2.0, decodeY(3.0f)-2*numBorders, decodeX(1.0f)-numBorders, decodeY(3.0f)-2*numBorders-arcRadius/2.0, decodeX(1.0f)-numBorders, decodeY(3.0f)-2*numBorders-arcRadius);
			
			if(width<compWidth){
				path.lineTo(decodeX(1.0f)-numBorders, decodeY(0.0f)+numBorders+arcRadius);
			}else{
				path.lineTo(decodeX(1.0f)-numBorders, decodeY(0.0f));
			}
			
			path.closePath();
		}
		return path;
	}
	
	protected Shape decodeBorderPopupMenu(JComponent invoker, boolean rightPosition){
		double arcRadius = 9;
		double numBorders = 3;
		
		int height = invoker.getBounds().height;
		path.reset();
		
		if(rightPosition){
			path.moveTo(decodeX(0.0f), decodeY(0.0f));
			path.lineTo(decodeX(2.0f)+numBorders - arcRadius, decodeY(0.0f));
			path.curveTo(decodeX(2.0f)+numBorders-arcRadius/2.0, decodeY(0.0f), decodeX(2.0f)+numBorders, decodeY(0.0f)+arcRadius/2.0, decodeX(2.0f)+numBorders, decodeY(0.0f)+arcRadius);
			path.lineTo(decodeX(2.0f)+numBorders, decodeY(3.0f)-2*numBorders-arcRadius);
			path.curveTo(decodeX(2.0f)+numBorders, decodeY(3.0f)-2*numBorders-arcRadius/2.0, decodeX(2.0f)+numBorders-arcRadius/2.0, decodeY(3.0f)-2*numBorders, decodeX(2.0f)+numBorders-arcRadius, decodeY(3.0f)-2*numBorders);
			path.lineTo(decodeX(1.0f)-numBorders+arcRadius, decodeY(3.0f)-2*numBorders);
			path.curveTo(decodeX(1.0f)-numBorders+arcRadius/2.0, decodeY(3.0f)-2*numBorders, decodeX(1.0f)-numBorders, decodeY(3.0f)-2*numBorders-arcRadius/2.0, decodeX(1.0f)-numBorders, decodeY(3.0f)-2*numBorders-arcRadius);
			path.lineTo(decodeX(1.0f)-numBorders,  decodeY(0.0f) + height);
			path.lineTo(decodeX(0.0f),  decodeY(0.0f) + height);
			path.lineTo(decodeX(0.0f),  decodeY(0.0f));
		}else{
			path.moveTo(decodeX(1.0f)+numBorders + arcRadius, decodeY(0.0f));
			path.lineTo(decodeX(3.0f), decodeY(0.0f));
			path.lineTo(decodeX(3.0f),  decodeY(0.0f) + height);
			path.lineTo(decodeX(2.0f)+numBorders,  decodeY(0.0f)+height);
			
			path.lineTo(decodeX(2.0f)+numBorders, decodeY(3.0f)-2*numBorders-arcRadius);
			path.curveTo(decodeX(2.0f)+numBorders, decodeY(3.0f)-2*numBorders-arcRadius/2.0, decodeX(2.0f)+numBorders-arcRadius/2.0, decodeY(3.0f)-2*numBorders, decodeX(2.0f)+numBorders-arcRadius, decodeY(3.0f)-2*numBorders);
			path.lineTo(decodeX(1.0f)-numBorders+arcRadius, decodeY(3.0f)-2*numBorders);
			path.curveTo(decodeX(1.0f)-numBorders+arcRadius/2.0, decodeY(3.0f)-2*numBorders, decodeX(1.0f)-numBorders, decodeY(3.0f)-2*numBorders-arcRadius/2.0, decodeX(1.0f)-numBorders, decodeY(3.0f)-2*numBorders-arcRadius);
			path.lineTo(decodeX(1.0f)-numBorders, decodeY(0.0f)+numBorders+arcRadius);
			path.curveTo(decodeX(1.0f)-numBorders, decodeY(0.0f)+arcRadius/2.0, decodeX(1.0f)-numBorders+arcRadius/2.0, decodeY(0.0f), decodeX(1.0f)-numBorders+arcRadius, decodeY(0.0f));
		}
		
		path.closePath();
		return path;
	}
	
	protected Shape decodeUpperFiller(JComponent invoker){
		
		int numBorders = 3;
		double arcRadius = 7;
		
		path.reset();
		
		path.moveTo(decodeX(1.0f) + arcRadius, decodeY(0.0f)+2*numBorders);
		path.lineTo(decodeX(2.0f) - arcRadius-numBorders, decodeY(0.0f)+2*numBorders);
		path.curveTo(decodeX(2.0f)-arcRadius/2.0, decodeY(0.0f)+2*numBorders, decodeX(2.0f), decodeY(0.0f)+2*numBorders+arcRadius/2.0, decodeX(2.0f), decodeY(0.0f)+2*numBorders+arcRadius);
		path.lineTo(decodeX(2.0f), decodeY(1.0f));
		path.lineTo(decodeX(1.0f), decodeY(1.0f));
		path.lineTo(decodeX(1.0f), decodeY(0.0f)+2*numBorders+arcRadius);
		path.curveTo(decodeX(1.0f), decodeY(0.0f)+2*numBorders+arcRadius/2.0, decodeX(1.0f)+arcRadius/2.0, decodeY(0.0f)+2*numBorders, decodeX(1.0f)+arcRadius, decodeY(0.0f)+2*numBorders);
		
		path.closePath();
		return path;
	}
	
	protected Shape decodeUpperFillerMenuBar(JComponent c, JComponent invoker){
		
		int numBorders = 3;
		double arcRadius = 7;
		
		int width = invoker.getBounds().width;
		int compWidth = c.getBounds().width;
		
		boolean leftPos = isInvokerOnLeftPosition((JComponent)invoker, c);
		
		path.reset();
		if(leftPos){
			path.moveTo(decodeX(1.0f), decodeY(0.0f));
			if(width<compWidth){
				double middlepoint = width-2*shadowWidth-2*numBorders;
				path.lineTo(decodeX(1.0f)+middlepoint, decodeY(0.0f));
				path.lineTo(decodeX(1.0f)+middlepoint, decodeY(0.0f)+2*numBorders);
				path.lineTo(decodeX(2.0f) - arcRadius-numBorders, decodeY(0.0f)+2*numBorders);
				path.curveTo(decodeX(2.0f)-arcRadius/2.0, decodeY(0.0f)+2*numBorders, decodeX(2.0f), decodeY(0.0f)+2*numBorders+arcRadius/2.0, decodeX(2.0f), decodeY(0.0f)+2*numBorders+arcRadius);
			}else{
				path.lineTo(decodeX(2.0f), decodeY(0.0f));
			}
			path.lineTo(decodeX(2.0f), decodeY(1.0f));
			path.lineTo(decodeX(1.0f), decodeY(1.0f));
			path.lineTo(decodeX(1.0f), decodeY(0.0f));
			
			path.closePath();
		}else{
			if(width<compWidth){
				path.moveTo(decodeX(1.0f), decodeY(0.0f)+2*numBorders+arcRadius);
				path.curveTo(decodeX(1.0f), decodeY(0.0f)+2*numBorders+arcRadius/2.0, decodeX(1.0f)+arcRadius/2.0, decodeY(0.0f)+2*numBorders, decodeX(1.0f)+arcRadius, decodeY(0.0f)+2*numBorders);
				double middlepoint = width-2*shadowWidth-2*numBorders;
				path.lineTo(decodeX(2.0f)-middlepoint, decodeY(0.0f)+2*numBorders);
				path.lineTo(decodeX(2.0f)-middlepoint, decodeY(0.0f));
				path.lineTo(decodeX(2.0f), decodeY(0.0f));
			}else{
				path.moveTo(decodeX(1.0f), decodeY(0.0f));
				path.lineTo(decodeX(2.0f), decodeY(0.0f));
			}
			
			path.lineTo(decodeX(2.0f), decodeY(1.0f));
			path.lineTo(decodeX(1.0f), decodeY(1.0f));
			if(width<compWidth){
				path.lineTo(decodeX(1.0f), decodeY(0.0f)+2*numBorders+arcRadius);
			}else{
				path.lineTo(decodeX(1.0f), decodeY(0.0f));
			}
			path.closePath();
		}
		return path;
	}
	
	protected Shape decodeUpperFillerPopupMenu(JComponent invoker, boolean rightPosition){
		
		int numBorders = 3;
		double arcRadius = 7;
		
		int height = invoker.getBounds().height;
		path.reset();
		
		if(rightPosition){
			path.moveTo(decodeX(0.0f), decodeY(0.0f)+numBorders);
			path.lineTo(decodeX(2.0f) - arcRadius-numBorders, decodeY(0.0f)+numBorders);
			path.curveTo(decodeX(2.0f)-arcRadius/2.0, decodeY(0.0f)+numBorders, decodeX(2.0f), decodeY(0.0f)+numBorders+arcRadius/2.0, decodeX(2.0f), decodeY(0.0f)+numBorders+arcRadius);
			path.lineTo(decodeX(2.0f), decodeY(0.0f) + height);
			path.lineTo(decodeX(1.0f), decodeY(0.0f) + height);
			path.lineTo(decodeX(1.0f), decodeY(0.0f) + height-numBorders);
			path.lineTo(decodeX(0.0f), decodeY(0.0f)+height-numBorders);
			path.lineTo(decodeX(0.0f), decodeY(0.0f)+numBorders);
		}else{
			path.moveTo(decodeX(1.0f)+ arcRadius+numBorders, decodeY(0.0f)+numBorders);
			path.lineTo(decodeX(3.0f), decodeY(0.0f) + numBorders);
			path.lineTo(decodeX(3.0f), decodeY(0.0f) + height-numBorders);
			path.lineTo(decodeX(2.0f), decodeY(0.0f) + height-numBorders);
			path.lineTo(decodeX(2.0f), decodeY(0.0f) + height);
			path.lineTo(decodeX(1.0f), decodeY(0.0f) + height);
			path.lineTo(decodeX(1.0f), decodeY(0.0f)+numBorders+arcRadius);
			path.curveTo(decodeX(1.0f), decodeY(0.0f)+numBorders+arcRadius/2.0, decodeX(1.0f)+arcRadius/2.0, decodeY(0.0f)+numBorders, decodeX(1.0f)+arcRadius, decodeY(0.0f)+numBorders);
		}
		
		path.closePath();
		return path;
	}
	
	protected Shape decodeBottomFiller(){
		
		int numBorders = 9;
		double arcRadius = 7;
		path.reset();
		
		path.moveTo(decodeX(1.0f), decodeY(2.0f));
		path.lineTo(decodeX(2.0f), decodeY(2.0f));
		path.lineTo(decodeX(2.0f), decodeY(3.0f)-numBorders-arcRadius);
		path.curveTo(decodeX(2.0f), decodeY(3.0f)-numBorders-arcRadius/2.0, decodeX(2.0f)-arcRadius/2.0, decodeY(3.0f)-numBorders, decodeX(2.0f)-arcRadius, decodeY(3.0f)-numBorders);
		path.lineTo(decodeX(1.0f)+ arcRadius, decodeY(3.0f)-numBorders);
		path.curveTo(decodeX(1.0f)+arcRadius/2.0, decodeY(3.0f)-numBorders, decodeX(1.0f), decodeY(3.0f)-numBorders-arcRadius/2.0, decodeX(1.0f), decodeY(3.0f)-numBorders-arcRadius);
		path.lineTo(decodeX(1.0f), decodeY(2.0f));
		
		path.closePath();
		return path;
	}
	
	protected com.ontimize.plaf.utils.DerivedColor getDerivedColor(Color parent,
            float hOffset, float sOffset,
            float bOffset, int aOffset) {
    	DerivedColor color = new DerivedColor.UIResource(parent, hOffset, sOffset, bOffset, aOffset);
    	color.rederiveColor();
    	return color;
    }

    protected Paint decodeGradient(Shape s, Color bgBaseColor) {
    	
    	Color derived = getDerivedColor(bgBaseColor, -0.0010442734f, -0.0010442734f, -0.18823528f, 0);
    	
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float h = (float)bounds.getHeight();
        return decodeGradient( x, y, x, y+h,
                new float[] { 0.0f,1.0f },
                new Color[] { bgBaseColor,
        					  derived});
    }


}
