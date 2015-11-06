package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;




/**
 * Painter for the background of the buttons in a scroll bar (to configure border and foreground colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public  class OScrollBarTrackPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of ScrollBarScrollBarTrackPainter to determine which region/state is being painted
	//by that instance.
	public static final int BACKGROUND_DISABLED = 1;
	public static final int BACKGROUND_ENABLED = 2;


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;

	//the following 4 variables are reused during the painting code of the layers
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);


	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of ScrollBarScrollBarTrackPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.10016362f, 0.011764705f, 0);
	protected final Color color5 = decodeColor("nimbusBlueGrey", 0.02222228f, -0.06465475f, -0.31764707f, 0);
	
	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorDisabled;


	//Array of current component colors, updated in each paint call
	protected Object [] componentColors;

	public OScrollBarTrackPainter(int state, PaintContext ctx) {
		super();
		this.state = state;
		this.ctx = ctx;

		init();
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object [] extendedCacheKeys) {
		//populate componentColors array with colors calculated in getExtendedCacheKeys call
		componentColors = extendedCacheKeys;
		//generate this entire method. Each state/bg/fg/border combo that has
		//been painted gets its own KEY and paint method.
		switch(state) {
		case BACKGROUND_DISABLED: paintBackgroundDisabled(g); break;
		case BACKGROUND_ENABLED: paintBackgroundEnabled(g); break;

		}
	}

	@Override
	protected PaintContext getPaintContext() {
		return ctx;
	}

	/**
	 * Get configuration properties to be used in this painter.
	 * As usual, it is checked the OLAFCustomConfig.properties, and then in OLAFDefaultConfig.properties.
	 *
	 * Moreover, if there are not values for that properties in both, the default Nimbus configuration values are set, due to, those properties are needed to
	 * paint and used by the Ontimize L&F, so, there are not Nimbus values for that, so, default values are always configured based on Nimbus values.
	 *
	 */
	protected void init (){

		// enable:
		Object obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarTrack[Enabled].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}else{
			backgroundColorEnabled = color1;
		}
		
		// disable:
		obj = UIManager.getLookAndFeelDefaults().get( "ScrollBar:ScrollBarTrack[Disabled].background");
		if(obj instanceof Paint){
			backgroundColorDisabled = (Paint)obj;
		}else{
			backgroundColorDisabled = color5;
		}

	}


	protected void paintBackgroundDisabled(Graphics2D g) {
		rect = decodeRect1();
		g.setPaint( backgroundColorDisabled);
		g.fill(rect);
	}

	protected void paintBackgroundEnabled(Graphics2D g) {
		rect = decodeRect1();
		g.setPaint( backgroundColorEnabled);
		g.fill(rect);
	}

	protected Rectangle2D decodeRect1() {
		rect.setRect(decodeX(0.0f), //x
				decodeY(0.0f), //y
				decodeX(3.0f), //width
				decodeY(3.0f)); //height
				return rect;
	}

}
