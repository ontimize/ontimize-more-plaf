package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;



public class OPopupItemPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of MenuItemPainter to determine which region/state is being painted
	//by that instance.
	public static final int BACKGROUND_DISABLED = 1;
	public   static final int BACKGROUND_ENABLED = 2;
	public  static final int BACKGROUND_MOUSEOVER = 3;


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;

	// reused during the painting code of the layers
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of MenuItemPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = decodeColor("nimbusSelection", 0.0f, 0.0f, 0.0f, 0);
	protected final Color color2 = decodeColor("nimbusBlueGrey", 0.0f, 0.0f, 0.0f, 0);

	// painters to fill the component
	protected Paint backgroundColorDisabled;
	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorMouseOver;


	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;



	public OPopupItemPainter(int state, PaintContext ctx) {
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
			case BACKGROUND_MOUSEOVER: paintBackgroundMouseOver(g); break;
			case BACKGROUND_ENABLED: paintBackgroundEnable(g); break;
			case BACKGROUND_DISABLED: paintBackgroundDisable(g); break;
		}
	}



	@Override
	protected PaintContext getPaintContext() {
		return ctx;
	}



	/**
	 * Get configuration properties to be used in this painter (such as: *BorderPainter and *BgPainter).
	 *
	 */
	protected void init (){

		// disable:
		Object obj = UIManager.getLookAndFeelDefaults().get(getComponentKeyName() + "[Disabled].background");
		if(obj instanceof Paint){
			backgroundColorDisabled = (Paint)obj;
		}else{
			backgroundColorDisabled = color2;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Enabled].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}else{
			backgroundColorEnabled = color1;
		}

		// MouseOver:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[MouseOver].background");
		if(obj instanceof Paint){
			backgroundColorMouseOver = (Paint)obj;
		}else{
			backgroundColorMouseOver = color1;
		}

	}

	protected String getComponentKeyName(){
		return "\"PopupItem\"";
	}


	protected void paintBackgroundMouseOver(Graphics2D g) {
		rect = decodeRect1();
		g.setPaint( backgroundColorMouseOver);
		g.fill(rect);

	}


	protected void paintBackgroundEnable(Graphics2D g) {
		rect = decodeRect1();
		g.setPaint( backgroundColorEnabled );
		g.fill(rect);

	}


	protected void paintBackgroundDisable(Graphics2D g) {
		rect = decodeRect1();
		g.setPaint( backgroundColorDisabled );
		g.fill(rect);

	}


	protected Rectangle2D decodeRect1() {
		rect.setRect(decodeX(0.0f), //x
				decodeY(0.0f), //y
				decodeX(3.0f), //width
				decodeY(3.0f) ); //height
		return rect;
	}




}