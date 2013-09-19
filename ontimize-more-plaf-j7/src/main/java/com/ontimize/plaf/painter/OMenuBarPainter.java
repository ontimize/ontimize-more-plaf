package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;



/**
 * Menu bar painter (to configure border and foreground colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OMenuBarPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of MenuBarPainter to determine which region/state is being painted
	//by that instance.
	public static final int BACKGROUND_ENABLED = 1;
	public static final int BORDER_ENABLED = 2;


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;

	//the following 4 variables are reused during the painting code of the layers
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of MenuBarPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);
	protected final Color color2 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.10255819f, 0.23921567f, 0);
	protected final Color color3 = decodeColor("nimbusBlueGrey", -0.111111104f, -0.10654225f, 0.23921567f, -29);
	protected final Color color4 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -255);
	protected final Color color5 = decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);
	
	protected Paint backgroundColorEnabled;
	protected Paint borderColorEnabled;


	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OMenuBarPainter(int state, PaintContext ctx) {
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
		case BACKGROUND_ENABLED: paintBackgroundEnabled(g); break;
		case BORDER_ENABLED: paintBorderEnabled(g); break;

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
		
		
		// Enabled:
		Object obj = UIManager.getLookAndFeelDefaults().get( "MenuBar[Enabled].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		} else {
			backgroundColorEnabled = color1;
		}

		obj = UIManager.getLookAndFeelDefaults().get("MenuBar[Enabled].border");
		if(obj instanceof Paint){
			borderColorEnabled = (Paint)obj;
		}else{
			borderColorEnabled = color2;
		}
	}




	protected void paintBackgroundEnabled(Graphics2D g) {
		rect = decodeRect2();
		g.setPaint( backgroundColorEnabled );
		g.fill(rect);

	}


	protected void paintBorderEnabled(Graphics2D g) {
		rect = decodeRectBottomBorder();
		g.setPaint( borderColorEnabled );
		g.fill(rect);

		rect = decodeRectTopBorder();
		g.setPaint( borderColorEnabled );
		g.fill(rect);

		rect = decodeRectRightBorder();
		g.setPaint( borderColorEnabled );
		g.fill(rect);

		rect = decodeRectLeftBorder();
		g.setPaint( borderColorEnabled );
		g.fill(rect);
	}


	protected Rectangle2D decodeRect2() {
		rect.setRect(decodeX(1.0f), //x
				decodeY(1.0f), //y
				decodeX(2.0f) - decodeX(1.0f), //width
				decodeY(2.0f) - decodeY(1.0f)); //height
		return rect;
	}

	protected Rectangle2D decodeRectBottomBorder() {
		rect.setRect(decodeX(1.0f), //x
				decodeY(2.0f), //y
				decodeX(2.0f) - decodeX(1.0f), //width
				decodeY(3.0f) - decodeY(2.0f)); //height
		return rect;
	}


	protected Rectangle2D decodeRectTopBorder() {
		rect.setRect(decodeX(1.0f), //x
				decodeY(0.0f), //y
				decodeX(2.0f) - decodeX(1.0f), //width
				decodeY(1.0f)); //height
		return rect;
	}



	protected Rectangle2D decodeRectRightBorder() {
		rect.setRect(decodeX(2.0f)-1, //x
				decodeY(0.0f), //y
				decodeX(3.0f) - decodeX(2.0f)+1, //width
				decodeY(3.0f)); //height
		return rect;
	}



	protected Rectangle2D decodeRectLeftBorder() {
		rect.setRect(decodeX(0.0f), //x
				decodeY(0.0f), //y
				decodeX(1.0f), //width
				decodeY(3.0f)); //height
		return rect;
	}



}
