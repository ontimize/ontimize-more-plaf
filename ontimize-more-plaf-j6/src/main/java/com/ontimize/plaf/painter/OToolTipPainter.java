package com.ontimize.plaf.painter;



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.utils.OntimizeLAFColorUtils;


/**
 * Painter for the tooltip (to configure borders and background colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OToolTipPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of ToolTipPainter to determine which region/state is being painted
	//by that instance.
	public static final int BACKGROUND_ENABLED = 1;


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;


	// painter to fill the component
	protected Paint backgroundColorEnabled;
	protected Paint borderColorEnabled;


	//the following 4 variables are reused during the painting code of the layers
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
	protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);


	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of ToolTipPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);
	protected final Color color2 = decodeColor("info", 0.0f, 0.0f, 0.0f, 0);


	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OToolTipPainter(int state, PaintContext ctx) {
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

		Object obj = UIManager.getLookAndFeelDefaults().get( "ToolTip.background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}else{
			backgroundColorEnabled = color2;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ToolTip.border");
		if(obj instanceof Paint){
			borderColorEnabled = (Paint)obj;
		}else{
			borderColorEnabled = color1;
		}

	}

	protected void paintBackgroundEnabled(Graphics2D g) {
		
		paintShadow(g);
		
		Shape s = decodeRoundRect(decodeX(0.0f)+defaultShadowWidth, decodeY(0.0f)+defaultShadowWidth, decodeX(3.0f) -1-2*defaultShadowWidth, 
				decodeY(3.0f)- 1-2*defaultShadowWidth, 5, 5);
		g.setPaint(borderColorEnabled);
		g.draw(s);
		
		s = decodeRoundRect(decodeX(0.0f)+defaultShadowWidth+1, decodeY(0.0f)+defaultShadowWidth+1, decodeX(3.0f) - 2 -2*defaultShadowWidth, 
				decodeY(3.0f) - 2-2*defaultShadowWidth, 5, 5);
		g.setPaint( backgroundColorEnabled );
		g.fill(s);
	}
	
	
	protected Shape decodeRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight){
		path.reset();
		path.moveTo(x, y+h-arcHeight);
		path.lineTo(x, y+ arcHeight);
		path.curveTo(x, x+arcHeight/2.0, x+arcWidth/2.0, y, x+arcWidth, y);
		
		path.lineTo(x + w - arcWidth , y);
		path.curveTo(x+w-arcWidth/2.0, y, x+w, y+arcHeight/2.0, x+w, y+arcHeight);
		path.lineTo(x+w, y+h-arcHeight);
		
		path.curveTo(x+w, y+h-arcHeight/2.0, x+w-arcWidth/2.0, y+h, x+w-arcWidth, y+h);
		path.lineTo(x+arcWidth, y+h);
		
		path.curveTo(x+arcWidth/2.0, y+h, x, y+h-arcHeight/2.0, x, y+h-arcHeight);

		path.closePath();
		return path;
	}

	
	
	public static Color defaultShadowColor = Color.black;
	public static int defaultShadowWidth = 3;
	
	protected void paintShadow(Graphics2D g){
		
		for(int i=0;i<defaultShadowWidth+1;i++){
			Shape shadow = null;
			shadow = decodeShadow(i, i);
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
	
	protected Shape decodeShadow(int x, int y){
		double arcRadius = 5;
		int xOffset = 3;
		int yOffset = 3;
		
		path.reset();
		path.moveTo(decodeX(0.0f)+xOffset+1+arcRadius, decodeY(0.0f)+yOffset+y);
		
		path.lineTo(decodeX(3.0f)-2-arcRadius, decodeY(0.0f)+yOffset+y);
		path.curveTo(decodeX(3.0f)-x-1-arcRadius/2.0, 	decodeY(0.0f)+yOffset+y, 
					decodeX(3.0f)-x-1, 					decodeY(0.0f)+yOffset-1+arcRadius/2.0, 
					decodeX(3.0f)-x-1, 					decodeY(0.0f)+yOffset-1+arcRadius);
		
		path.lineTo(decodeX(3.0f)-x-1, decodeY(3.0f)-arcRadius-3);
		//Bottom right corner...
		path.curveTo(decodeX(3.0f)-x, 				decodeY(3.0f)-y-1-arcRadius/2.0, 
					decodeX(3.0f)-2-arcRadius/2.0, 	decodeY(3.0f)-y-1, 
					decodeX(3.0f)-2-arcRadius, 		decodeY(3.0f)-y-1);
		
		double xPos = decodeX(0.0f)+xOffset+1;
		path.lineTo(xPos+arcRadius, decodeY(3.0f)-y-1);
		//Bottom left corner...
		path.curveTo(decodeX(0.0f)+xOffset-1+x+arcRadius/2.0, 					decodeY(3.0f)-y, 
					decodeX(0.0f)+xOffset+x, 					decodeY(3.0f)-3-arcRadius/2.0, 
					decodeX(0.0f)+xOffset+x,  					decodeY(3.0f)-3-arcRadius);
		
		path.lineTo(decodeX(0.0f)+xOffset+x, decodeY(0.0f)+yOffset-1+arcRadius);
		//Top left corner...
		path.curveTo(decodeX(0.0f)+xOffset+x, 							decodeY(0.0f)+yOffset-1+arcRadius/2.0, 
				decodeX(0.0f)+xOffset+1+arcRadius/2.0, 		decodeY(0.0f)+yOffset+y, 
				decodeX(0.0f)+xOffset+1+arcRadius, 			decodeY(0.0f)+yOffset+y);
		
		path.closePath();
		return path;
	}


}
