package com.ontimize.plaf.painter;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.UIManager;



/**
 * Menu painter for menus IN the MENU BAR (to configure border and foreground colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OMenuBarMenuPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of OMenuBarMenuPainter to determine which region/state is being painted
	//by that instance.
	static final int BACKGROUND_DISABLED = 1;
	static final int BACKGROUND_ENABLED = 2;
	public static final int BACKGROUND_SELECTED = 3;
	
	protected int shadowWidth;


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;

	// painter to fill the component:
	protected Paint backgroundColorEnabled;
	protected Paint borderColorEnabled;

	//the following 4 variables are reused during the painting code of the layers
	protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);

	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of OMenuBarMenuPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = new Color(0x86adbf);
	protected final Color color2 = new Color(0xc6dfe3);


	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OMenuBarMenuPainter(int state, PaintContext ctx) {
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
			case BACKGROUND_SELECTED: paintBackgroundSelected(g, c); break;
		}
	}


	@Override
	protected PaintContext getPaintContext() {
		return ctx;
	}


	/**
	 * Get configuration properties to be used in this painter.
	 */
	protected void init (){

		// Selected:
		Object obj = UIManager.getLookAndFeelDefaults().get( "MenuBar:Menu[Selected].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}
		if (backgroundColorEnabled == null) {
			obj = UIManager.getLookAndFeelDefaults().get("PopupMenu[Enabled].background");
			if (obj instanceof Paint) {
				backgroundColorEnabled = (Paint) obj;
			}
		}
		if(backgroundColorEnabled==null){
			backgroundColorEnabled = color1;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get("MenuBar:Menu[Selected].border");
		if(obj instanceof Paint){
			borderColorEnabled = (Paint)obj;
		}
		if (borderColorEnabled == null) {
			obj = UIManager.getLookAndFeelDefaults().get("PopupMenu[Enabled].border");
			if (obj instanceof Paint) {
				borderColorEnabled = (Paint) obj;
			}
		}
		if(borderColorEnabled==null){
			borderColorEnabled = color2;
		}
		
		
		Object oShadowWidth = UIManager.getDefaults().get("PopupMenu.shadowWidth");
		if(oShadowWidth instanceof Number){
			shadowWidth = ((Number)oShadowWidth).intValue();
		}else{
			shadowWidth = OPopupMenuPainter.defaultShadowWidth;
		}
	}

	
	protected void paintBackgroundSelected(Graphics2D g, JComponent c) {

		Shape s = decodeBorder(c);
		g.setPaint(borderColorEnabled);
		g.fill(s);

		s = decodeBackground(c);
		g.setPaint(backgroundColorEnabled);
		g.fill(s);
	}
	
	protected Shape decodeBackground( JComponent c){
		
		int popUpWidth = -1;
		if(c instanceof JMenu){
			if(((JMenu)c).getPopupMenu() != null){
				popUpWidth = ((JMenu)c).getPopupMenu().getWidth();
			}
		}
		
		double arcRadius = 7;
		int numBorders = 3;
		path.reset();
		
		if(popUpWidth!=-1 && popUpWidth<c.getWidth()){
			//painting when MenuBar:Menu size is greater than PopupMenu width...
			path.moveTo(decodeX(0.0f) + numBorders + shadowWidth, decodeY(1.0f) + arcRadius);
			path.curveTo(decodeX(0.0f) + numBorders + shadowWidth, decodeY(1.0f) + arcRadius / 2.0,
					decodeX(0.0f) + numBorders + shadowWidth + arcRadius / 2.0, decodeY(1.0f), decodeX(0.0f)
							+ numBorders + shadowWidth + arcRadius, decodeY(1.0f));
			path.lineTo(decodeX(3.0f) - numBorders - shadowWidth - arcRadius, decodeY(1.0f));
			path.curveTo(decodeX(3.0f) - numBorders - shadowWidth - arcRadius / 2.0, decodeY(1.0f),
					decodeX(3.0f) - numBorders - shadowWidth, decodeY(1.0f) + arcRadius / 2.0, decodeX(3.0f)
							- numBorders - shadowWidth, decodeY(1.0f) + arcRadius);
			path.lineTo(decodeX(3.0f) - numBorders - shadowWidth, decodeY(3.0f)- numBorders - arcRadius);
			path.curveTo(decodeX(3.0f) - numBorders - shadowWidth, decodeY(3.0f) - numBorders  - arcRadius / 2.0,
					decodeX(3.0f)-1 - numBorders - shadowWidth - arcRadius / 2.0, decodeY(3.0f)- numBorders,
					decodeX(3.0f)-1 - numBorders - shadowWidth - arcRadius / 2.0, decodeY(3.0f)- numBorders);
			path.lineTo(decodeX(0.0f) + popUpWidth - numBorders - shadowWidth, decodeY(3.0f) - numBorders);
			path.lineTo(decodeX(0.0f) + popUpWidth - numBorders - shadowWidth, decodeY(3.0f));
			path.lineTo(decodeX(0.0f) + numBorders + shadowWidth, decodeY(3.0f));
			path.lineTo(decodeX(0.0f) + numBorders + shadowWidth, decodeY(1.0f) + arcRadius);

			path.closePath();
		} else {
			//painting when MenuBar:Menu size is lower than PopupMenu width...
			path.moveTo(decodeX(0.0f) + numBorders + shadowWidth, decodeY(1.0f) + arcRadius);
			path.curveTo(decodeX(0.0f) + numBorders + shadowWidth, decodeY(1.0f) + arcRadius / 2.0,
					decodeX(0.0f) + numBorders + shadowWidth + arcRadius / 2.0, decodeY(1.0f), decodeX(0.0f)
							+ numBorders + shadowWidth + arcRadius, decodeY(1.0f));
			path.lineTo(decodeX(3.0f) - numBorders - shadowWidth - arcRadius, decodeY(1.0f));
			path.curveTo(decodeX(3.0f) - numBorders - shadowWidth - arcRadius / 2.0, decodeY(1.0f),
					decodeX(3.0f) - numBorders - shadowWidth, decodeY(1.0f) + arcRadius / 2.0, decodeX(3.0f)
							- numBorders - shadowWidth, decodeY(1.0f) + arcRadius);
			path.lineTo(decodeX(3.0f) - numBorders - shadowWidth, decodeY(3.0f));
			path.lineTo(decodeX(0.0f) + numBorders + shadowWidth, decodeY(3.0f));
			path.lineTo(decodeX(0.0f) + numBorders + shadowWidth, decodeY(1.0f) + arcRadius);

			path.closePath();
		}
		return path;
	}

	protected Shape decodeBorder( JComponent c){
		
		int popUpWidth = -1;
		if(c instanceof JMenu){
			if(((JMenu)c).getPopupMenu() != null){
				popUpWidth = ((JMenu)c).getPopupMenu().getWidth();
			}
		}
		
		double arcRadius = 9;
		int numBorders = 3;
		path.reset();
		
		
		if(popUpWidth!=-1 && popUpWidth<c.getWidth()){
			//painting when MenuBar:Menu size is greater than PopupMenu width...
			path.moveTo(decodeX(0.0f) + shadowWidth, decodeY(0.0f) + arcRadius);
			path.curveTo(decodeX(0.0f) + shadowWidth, decodeY(0.0f) + arcRadius / 2.0, decodeX(0.0f)			//top-left corner
					+ shadowWidth + arcRadius / 2.0, decodeY(0.0f), decodeX(0.0f) + shadowWidth + arcRadius,
					decodeY(0.0f));
			path.lineTo(decodeX(3.0f) - shadowWidth - numBorders - arcRadius, decodeY(0.0f));					//top line
			path.curveTo(decodeX(3.0f) - shadowWidth - arcRadius / 2.0, decodeY(0.0f), decodeX(3.0f)			//top-right corner
					- shadowWidth, decodeY(0.0f) + arcRadius / 2.0, decodeX(3.0f) - shadowWidth,
					decodeY(0.0f) + arcRadius);
			path.lineTo(decodeX(3.0f) - shadowWidth, decodeY(3.0f)- arcRadius);									//right line
			path.curveTo(decodeX(3.0f) - shadowWidth , decodeY(3.0f) - arcRadius / 2.0, 						//bottom-right corner
					decodeX(3.0f) - shadowWidth - arcRadius / 2.0, decodeY(3.0f),
					decodeX(3.0f) - shadowWidth - arcRadius, decodeY(3.0f));
			path.lineTo(decodeX(0.0f) + shadowWidth, decodeY(3.0f));											//bottom line
			path.lineTo(decodeX(0.0f) + shadowWidth, decodeY(0.0f) + arcRadius);								//left line
			path.closePath();
		} else {
			//painting when MenuBar:Menu size is lower than PopupMenu width...
			path.moveTo(decodeX(0.0f) + shadowWidth, decodeY(0.0f) + arcRadius);								
			path.curveTo(decodeX(0.0f) + shadowWidth, decodeY(0.0f) + arcRadius / 2.0, decodeX(0.0f)			//top-left corner
					+ shadowWidth + arcRadius / 2.0, decodeY(0.0f), decodeX(0.0f) + shadowWidth + arcRadius,
					decodeY(0.0f));
			path.lineTo(decodeX(3.0f) - shadowWidth - numBorders - arcRadius, decodeY(0.0f));					//top line
			path.curveTo(decodeX(3.0f) - shadowWidth - arcRadius / 2.0, decodeY(0.0f), decodeX(3.0f)			//top-right corner
					- shadowWidth, decodeY(0.0f) + arcRadius / 2.0, decodeX(3.0f) - shadowWidth,
					decodeY(0.0f) + arcRadius);
			path.lineTo(decodeX(3.0f) - shadowWidth, decodeY(3.0f));											//right line
			path.lineTo(decodeX(0.0f) + shadowWidth, decodeY(3.0f));											//bottom line
			path.lineTo(decodeX(0.0f) + shadowWidth, decodeY(0.0f) + arcRadius);								//left line

			path.closePath();
		}
		return path;
	}

}
