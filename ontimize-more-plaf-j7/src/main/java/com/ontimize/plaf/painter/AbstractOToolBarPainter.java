package com.ontimize.plaf.painter;


import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.utils.LinearGradient;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;
import com.ontimize.plaf.utils.OntimizeLAFPainterUtils;
import com.ontimize.plaf.utils.OntimizeLAFParseUtils;


/**
 * ToolBar Painter (to configure border and foreground colors, painters, images, ...).
 * Several states are allowed such us north, west, east or south.
 *
 * @author Imatia Innovation
 *
 */
public abstract class AbstractOToolBarPainter extends AbstractRegionPainter{
//extends AbstractRegionPainter{
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of OToolBarPainter to determine which region/state is being painted
	//by that instance.
	public static final int BORDER_ENABLED = 1;
	static final int HANDLEICON_ENABLED = 2; // not public due to we do not allow this item

	public static final int BACKGROUND_ENABLED = 3;


	// painter to fill the component:
	public static boolean defaultUseTextureImage = true;
	protected boolean useTextureImage;
	protected String borderPainter;


	protected final int state; //refers to one of the static ints above
	protected PaintContext ctx;

	//the following 4 variables are reused during the painting code of the layers
	protected Path2D path = new Path2D.Float();
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);


	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of ToolBarPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);
	protected final Color color2 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
	protected final Color color3 = decodeColor("nimbusBlueGrey", -0.006944418f, -0.07399663f, 0.11372548f, 0);
	protected final Color color4 = decodeColor("nimbusBorder", 0.0f, -0.029675633f, 0.109803915f, 0);
	protected final Color color5 = decodeColor("nimbusBlueGrey", -0.008547008f, -0.03494492f, -0.07058823f, 0);

	protected final Color defaultBgColor = decodeColor("background", 0.0f, 0.0f, 0.0f, 0);

	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;
	
	protected java.awt.Image bgImage;
	
	protected TexturePaint texturePaint;
	
	public static String imgUrl = "com/ontimize/plaf/images/toolbarbackground.png";
	
	protected Paint bgPaint;

	public AbstractOToolBarPainter(int state, PaintContext ctx) {
		super();
		this.state = state;
		this.ctx = ctx;

		init();
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		//populate componentColors array with colors calculated in getExtendedCacheKeys call
		componentColors = extendedCacheKeys;
		// generate this entire method. Each state/bg/fg/border combo that has been painted gets its own KEY and paint method.
		switch(state) {
		case BORDER_ENABLED: paintBorderEnabled(g); break;
		// case HANDLEICON_ENABLED: painthandleIconEnabled(g); break;
		case BACKGROUND_ENABLED: paintBackground(g,c,width,height); break;
		}
	}



	@Override
	protected PaintContext getPaintContext() {
		return ctx;
	}


	protected abstract String getComponentState();



	/**
	 * Get configuration properties to be used in this painter (such as: *BorderPainter,  *upperCornerPainter,  *lowerCornerPainter and *BgPainter).
	 * As usual, it is checked the OLAFCustomConfig.properties, and then in OLAFDefaultConfig.properties.
	 *
	 * Moreover, if there are not values for that properties in both, the default Nimbus configuration values are set, due to, those properties are needed to
	 * paint and used by the Ontimize L&F, so, there are not Nimbus values for that, so, default values are always configured based on Nimbus values.
	 *
	 */
	protected void init (){
		if (UIManager.getDefaults().get("ToolBar.useTextureImage") instanceof Boolean){
			useTextureImage = (Boolean)UIManager.getLookAndFeel().getDefaults().get("ToolBar.useTextureImage");
		}else
			useTextureImage = defaultUseTextureImage;
		
		imgUrl = UIManager.getDefaults().get("ToolBar.backgroundImage")!=null?(String)UIManager.getDefaults().get("ToolBar.backgroundImage"):imgUrl;
		if(useTextureImage){
			if (imgUrl != null) {
				URL url = getClass().getClassLoader().getResource(imgUrl);
				bgImage = new ImageIcon(url).getImage();
				if(bgImage!=null){
					BufferedImage bi = new BufferedImage( bgImage.getWidth(null),bgImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
					bi.getGraphics().drawImage(bgImage, 0, 0, null);
					texturePaint = new TexturePaint(bi, new Rectangle(bi.getWidth(null), bi.getHeight(null)));
				}
			}
		}
		
		Object obj = UIManager.getLookAndFeelDefaults().get("ToolBar.bgpaint");
		if (obj instanceof Paint) {
			bgPaint = (Paint) obj;
		} else {
			bgPaint = null;
		}

	}

	/**
	 * Created to paint toolbar background
	 * @param g
	 * @param c
	 * @param width
	 * @param height
	 */
	protected void paintBackground(Graphics2D g, JComponent c, int width, int height) {
		rect = decodeRect2();
		
		if(useTextureImage && texturePaint!=null){
			g.setPaint(texturePaint);
		}
		else{
			Paint tp = null;
			if(bgPaint instanceof LinearGradient){
				Rectangle bounds = new Rectangle();
				bounds.x = 0;
				bounds.y = 0;
				bounds.width = width;
				bounds.height = height;
				tp = OntimizeLAFColorUtils.decodeGradient(bounds, (LinearGradient)bgPaint);
			}else if(c!=null && c.getBackground()!=null){
				tp = c.getBackground();
			}
			g.setPaint(tp);
		}
		g.fillRect(0, 0, width, height);
	}

	
	protected void paintDiagramBackground(Graphics2D g, JComponent c, int width, int height){
		
		Color oldC = g.getColor();
		
		Color topBackgroundColor = OntimizeLAFParseUtils.parseColor("#DAE7ED", Color.black);
		Color downBackgroundColor = OntimizeLAFParseUtils.parseColor("#A2ABB0", Color.black);
		
		GradientPaint downPaint = new GradientPaint(0,height, downBackgroundColor, 0, decodeHeight(height), topBackgroundColor);
		Graphics2D g2D = (Graphics2D)g;
		Paint oldPaint = g2D.getPaint();
		g2D.setPaint(downPaint);
		g2D.fillRect(0,(int)(height-(height*0.2)), width, height);
		g2D.setPaint(oldPaint);
		
		Shape s = ShapeFactory.getInstace().createSemiRoundRect(0,0,width,decodeHeight(height), 5,5,ShapeFactory.TOP_ORIENTATION);
		g.setColor(topBackgroundColor);
		g.fill(s);
		g.setColor(oldC);
	}
	
	protected int decodeHeight(int height){
		return (int)(height-(height*0.2));
	}




	protected void paintBorderEnabled(Graphics2D g) {
		rect = decodeRect1();
		g.setPaint( OntimizeLAFPainterUtils.getPaint(rect, borderPainter ));
		g.fill(rect);

	}


	protected Rectangle2D decodeRect1() {
		rect.setRect(decodeX(1.0f), //x
				decodeY(2.0f), //y
				decodeX(2.0f) - decodeX(1.0f), //width
				decodeY(3.0f) - decodeY(2.0f)); //height
		return rect;
	}

	protected Rectangle2D decodeRect2() {
		rect.setRect(decodeX(0.0f), //x
				decodeY(0.0f), //y
				decodeX(2.8f) - decodeX(0.0f), //width
				decodeY(3.0f) - decodeY(0.0f)); //height
		return rect;
	}

	protected Rectangle2D decodeRect3() {
		rect.setRect(decodeX(2.8f), //x
				decodeY(0.0f), //y
				decodeX(3.0f) - decodeX(2.8f), //width
				decodeY(3.0f) - decodeY(0.0f)); //height
		return rect;
	}

	protected Path2D decodePath1() {
		path.reset();
		path.moveTo(decodeX(0.0f), decodeY(0.0f));
		path.lineTo(decodeX(0.0f), decodeY(0.4f));
		path.lineTo(decodeX(0.4f), decodeY(0.0f));
		path.lineTo(decodeX(0.0f), decodeY(0.0f));
		path.closePath();
		return path;
	}

	protected Path2D decodePath2() {
		path.reset();
		path.moveTo(decodeX(0.0f), decodeY(3.0f));
		path.lineTo(decodeX(0.0f), decodeY(2.6f));
		path.lineTo(decodeX(0.4f), decodeY(3.0f));
		path.lineTo(decodeX(0.0f), decodeY(3.0f));
		path.closePath();
		return path;
	}







}
