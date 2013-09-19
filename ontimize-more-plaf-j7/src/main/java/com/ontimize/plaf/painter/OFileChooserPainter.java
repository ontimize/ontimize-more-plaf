package com.ontimize.plaf.painter;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;


/**
 */
public class OFileChooserPainter extends AbstractRegionPainter {
	public static final int BACKGROUND_ENABLED = 2;


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;

	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
	protected Path2D path = new Path2D.Float();

	// painter to fill the component
	protected Paint backgroundColorEnabled;

	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of TreeCellEditorPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected Color color1 = decodeColor("control", 0.0f, 0.0f, 0.0f, 0);


	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OFileChooserPainter(int state, PaintContext ctx) {
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

		Object obj = UIManager.getLookAndFeelDefaults().get( "FileChooser[Enabled].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}else{
			backgroundColorEnabled = color1;
		}
	}


	protected void paintBackgroundEnabled(Graphics2D g) {
		rect = decodeRect1();
        g.setPaint(backgroundColorEnabled);
        g.fill(rect);
	}
	
	protected Rectangle2D decodeRect1() {
		rect.setRect(decodeX(1.0f), // x
				decodeY(1.0f), // y
				decodeX(2.0f) - decodeX(1.0f), // width
				decodeY(2.0f) - decodeY(1.0f)); // height
		return rect;
	}

}
