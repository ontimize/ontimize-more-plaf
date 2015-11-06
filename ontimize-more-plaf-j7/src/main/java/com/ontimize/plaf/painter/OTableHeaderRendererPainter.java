package com.ontimize.plaf.painter;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;



/**
 * Painter for table header renderer (to configure border and foreground colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OTableHeaderRendererPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of OTableHeaderRendererPainter to determine which region/state is being painted
	//by that instance.
	public static final int BACKGROUND_DISABLED = 1;
	public static final int BACKGROUND_ENABLED = 2;
	public static final int BACKGROUND_ENABLED_FOCUSED = 3;
	public static final int BACKGROUND_MOUSEOVER = 4;
	public static final int BACKGROUND_PRESSED = 5;
	public static final int BACKGROUND_ENABLED_SORTED = 6;
	public static final int BACKGROUND_ENABLED_FOCUSED_SORTED = 7;
	public static final int BACKGROUND_DISABLED_SORTED = 8;


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;

	//the following 4 variables are reused during the painting code of the layers
	protected Path2D path = new Path2D.Float();
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
	protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);
	protected Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of OTableHeaderRendererPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = decodeColor("nimbusBorder", -0.013888836f, 5.823001E-4f, -0.12941176f, 0);
	protected final Color color2 = decodeColor("nimbusBlueGrey", -0.01111114f, -0.08625447f, 0.062745094f, 0);
	protected final Color color3 = decodeColor("nimbusBlueGrey", -0.013888836f, -0.028334536f, -0.17254901f, 0);
	protected final Color color4 = decodeColor("nimbusBlueGrey", -0.013888836f, -0.029445238f, -0.16470587f, 0);
	protected final Color color5 = decodeColor("nimbusBlueGrey", -0.02020204f, -0.053531498f, 0.011764705f, 0);
	protected final Color color6 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.10655806f, 0.24313724f, 0);
	protected final Color color7 = decodeColor("nimbusBlueGrey", 0.0f, -0.08455229f, 0.1607843f, 0);
	protected final Color color8 = decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);
	protected final Color color9 = decodeColor("nimbusBlueGrey", 0.0f, -0.07466974f, 0.23921567f, 0);
	protected final Color color10 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
	protected final Color color11 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.10658931f, 0.25098038f, 0);
	protected final Color color12 = decodeColor("nimbusBlueGrey", 0.0f, -0.08613607f, 0.21960783f, 0);
	protected final Color color13 = decodeColor("nimbusBlueGrey", 0.0f, -0.07333623f, 0.20392156f, 0);
	protected final Color color14 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
	protected final Color color15 = decodeColor("nimbusBlueGrey", -0.00505054f, -0.05960039f, 0.10196078f, 0);
	protected final Color color16 = decodeColor("nimbusBlueGrey", 0.0f, -0.017742813f, 0.015686274f, 0);
	protected final Color color17 = decodeColor("nimbusBlueGrey", -0.0027777553f, -0.0018306673f, -0.02352941f, 0);
	protected final Color color18 = decodeColor("nimbusBlueGrey", 0.0055555105f, -0.020436227f, 0.12549019f, 0);
	protected final Color color19 = decodeColor("nimbusBase", -0.023096085f, -0.62376213f, 0.4352941f, 0);
	protected final Color color20 = decodeColor("nimbusBase", -0.0012707114f, -0.50901747f, 0.31764704f, 0);
	protected final Color color21 = decodeColor("nimbusBase", -0.002461195f, -0.47139505f, 0.2862745f, 0);
	protected final Color color22 = decodeColor("nimbusBase", -0.0051222444f, -0.49103343f, 0.372549f, 0);
	protected final Color color23 = decodeColor("nimbusBase", -8.738637E-4f, -0.49872798f, 0.3098039f, 0);
	protected final Color color24 = decodeColor("nimbusBase", -2.2029877E-4f, -0.4916465f, 0.37647057f, 0);
	
	protected final Color color30 =  new Color(0xaab7bf);
	protected final Color color31 =  new Color(0xe5edf0);
	
	public static Color defaultOutsideRightBorderColor = new Color(0x858d92);
	public static Color defaultInsideRightBorderColor = Color.white;
	public static Color defaultBottomBorderColor = new Color(0x858d92);
	
	protected Paint outsideRightBorderColor;
	protected Paint insideRightBorderColor;
	protected Paint bottomBorderColor;
	
	protected Color bgHeaderGradientInit;
	protected Color bgHeaderGradientEnd;


	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OTableHeaderRendererPainter(int state, PaintContext ctx) {
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
		case BACKGROUND_DISABLED: paintBackgroundDisabled(g); break;
		case BACKGROUND_ENABLED: paintBackgroundEnabled(g,c); break;
		case BACKGROUND_ENABLED_FOCUSED: paintBackgroundEnabledAndFocused(g); break;
		case BACKGROUND_MOUSEOVER: paintBackgroundMouseOver(g); break;
		case BACKGROUND_PRESSED: paintBackgroundPressed(g); break;
		case BACKGROUND_ENABLED_SORTED: paintBackgroundEnabledAndSorted(g,c); break;
		case BACKGROUND_ENABLED_FOCUSED_SORTED: paintBackgroundEnabledAndFocusedAndSorted(g,c); break;
		case BACKGROUND_DISABLED_SORTED: paintBackgroundDisabledAndSorted(g); break;

		}
	}

	
	protected void init(){
		
		// enable:
		Object obj = UIManager.getLookAndFeelDefaults().get( "TableHeader:\"TableHeader.renderer\".backgroundDegraded");
		if(obj instanceof Paint[]){
			Paint[] paints = (Paint[])obj;
			if(paints[0] instanceof Color){
				bgHeaderGradientInit = (Color)paints[0];
			}else{
				bgHeaderGradientInit = color30;
			}
			
			if(paints.length>1 && paints[1] instanceof Color){
				bgHeaderGradientEnd = (Color)paints[1];
			} else if(paints.length==1){
				bgHeaderGradientEnd = bgHeaderGradientInit;
			} else{
				bgHeaderGradientEnd = color31;
			}
			
		}else{
			bgHeaderGradientInit = color30;
			bgHeaderGradientEnd = color31;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "TableHeader:\"TableHeader.renderer\".outsideRightBorder");
		if(obj instanceof Paint){
			outsideRightBorderColor = (Paint)obj;
		}else{
			outsideRightBorderColor = defaultOutsideRightBorderColor;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "TableHeader:\"TableHeader.renderer\".insideRightBorder");
		if(obj instanceof Paint){
			insideRightBorderColor = (Paint)obj;
		}else{
			insideRightBorderColor = defaultInsideRightBorderColor;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "TableHeader:\"TableHeader.renderer\".bottomBorder");
		if(obj instanceof Paint){
			bottomBorderColor = (Paint)obj;
		}else{
			bottomBorderColor = defaultBottomBorderColor;
		}
		
	}


	@Override
	protected PaintContext getPaintContext() {
		return ctx;
	}

	protected void paintBackgroundDisabled(Graphics2D g) {
		rect = decodeBottomBorder();
		g.setPaint(color1);
		g.fill(rect);
		rect = decodeOutsideRightBorder();
		g.setPaint(decodeGradient1(rect));
		g.fill(rect);
		rect = decodeBackground();
		g.setPaint(decodeGradient2(rect));
		g.fill(rect);

	}

	protected void paintBackgroundEnabled(Graphics2D g, JComponent c) {
		
		rect = decodeBackground();
		g.setPaint(decodeBgGradient(rect)); 
		g.fill(rect);
		
		//Painting borders...
		paintBorders(g, c);
	}

	protected void paintBackgroundEnabledAndFocused(Graphics2D g) {
		rect = decodeBottomBorder();
		g.setPaint(color1);
		g.fill(rect);
		rect = decodeOutsideRightBorder();
		g.setPaint(decodeGradient1(rect));
		g.fill(rect);
		rect = decodeBackground();
		g.setPaint(decodeGradient2(rect));
		g.fill(rect);
		path = decodePath1();
		g.setPaint(color10);
		g.fill(path);

	}

	protected void paintBackgroundMouseOver(Graphics2D g) {
		rect = decodeBottomBorder();
		g.setPaint(color1);
		g.fill(rect);
		rect = decodeOutsideRightBorder();
		g.setPaint(decodeGradient1(rect));
		g.fill(rect);
		rect = decodeBackground();
		g.setPaint(decodeGradient3(rect));
		g.fill(rect);

	}

	protected void paintBackgroundPressed(Graphics2D g) {
		rect = decodeBottomBorder();
		g.setPaint(color1);
		g.fill(rect);
		rect = decodeOutsideRightBorder();
		g.setPaint(decodeGradient1(rect));
		g.fill(rect);
		rect = decodeBackground();
		g.setPaint(decodeGradient4(rect));
		g.fill(rect);

	}

	protected void paintBackgroundEnabledAndSorted(Graphics2D g, JComponent c) {
		rect = decodeBackground();
		g.setPaint(decodeBgGradient(rect)); 
		g.fill(rect);
		
		//Painting borders...
		paintBorders(g, c);

	}

	protected void paintBackgroundEnabledAndFocusedAndSorted(Graphics2D g, JComponent c) {
		rect = decodeBackground();
		g.setPaint(decodeBgGradient(rect)); 
		g.fill(rect);
		
		//Painting borders...
		paintBorders(g, c);

	}

	protected void paintBackgroundDisabledAndSorted(Graphics2D g) {
		rect = decodeBottomBorder();
		g.setPaint(color1);
		g.fill(rect);
		rect = decodeOutsideRightBorder();
		g.setPaint(decodeGradient1(rect));
		g.fill(rect);
		rect = decodeBackground();
		g.setPaint(decodeGradient2(rect));
		g.fill(rect);

	}
	
	protected void paintBorders(Graphics2D g, JComponent c){
		//Painting borders...
		rect = decodeBottomBorder();
		g.setPaint(bottomBorderColor);
		g.fill(rect);
		if (!isLastColumn(c)) {
			rect = decodeOutsideRightBorder();
			g.setPaint(outsideRightBorderColor);
			g.fill(rect);
			rect = decodeInsideRightBorder();
			g.setPaint(insideRightBorderColor);
			g.fill(rect);
		}
	}
	
	protected boolean isLastColumn(JComponent c){
		JTable table = getTable(c);
		if (table != null ) {
			int selectedColumn = table.getColumnModel().getColumnIndexAtX(c.getX());
			int lastColumnIndex = getLastColumnIndex(c);
			if(selectedColumn==lastColumnIndex){
				return true;
			}
			
		}
		return false;
	}

	
	protected int getLastColumnIndex(JComponent c) {
		int lastColumnIndex = -1;
		JTable table = getTable(c);
		if (table != null) {
			int columnCount = table.getModel().getColumnCount();

			for (int i = 0; i < columnCount; i++) {
				int width = table.getColumnModel().getColumn(i).getWidth();
				if (width > 0) {
					lastColumnIndex = i;
				}
			}
		}
		return lastColumnIndex;
	}
	
	protected JTable getTable(JComponent c){
		JTable table = null;
		try {
			Object obj = SwingUtilities.getAncestorOfClass(JTableHeader.class, c);
			if(obj instanceof JTableHeader){
				table = ((JTableHeader)obj).getTable();
			}
		} catch (Exception e) {
		}
		return table;
	}
	

	protected Rectangle2D decodeBottomBorder() {
		rect.setRect(decodeX(0.0f), //x
				decodeY(3.0f)-1, //y
				decodeX(3.0f) - decodeX(0.0f), //width
				1); //height
				return rect;
	}
	
	protected Rectangle2D decodeTopBorder() {
		rect.setRect(decodeX(0.0f), //x
				decodeY(0.0f), //y
				decodeX(3.0f), //width
				1); //height
				return rect;
	}

	protected Rectangle2D decodeOutsideRightBorder() {
		rect.setRect(decodeX(3.0f)-1, //x
				decodeY(0.0f), //y
				1, //width
				decodeY(3.0f) - 1); //height
				return rect;
	}
	
	protected Rectangle2D decodeInsideRightBorder() {
		rect.setRect(decodeX(3.0f)-2, //x
				decodeY(0.0f), //y
				1, //width
				decodeY(3.0f) -1); //height
				return rect;
	}

	protected Rectangle2D decodeBackground() {
		rect.setRect(decodeX(0.0f), //x
				decodeY(0.0f), //y
				decodeX(3.0f), //width
				decodeY(3.0f)); //height
		return rect;
	}

	protected Path2D decodePath1() {
		path.reset();
		path.moveTo(decodeX(0.0f), decodeY(0.0f));
		path.lineTo(decodeX(0.0f), decodeY(3.0f));
		path.lineTo(decodeX(3.0f), decodeY(3.0f));
		path.lineTo(decodeX(3.0f), decodeY(0.0f));
		path.lineTo(decodeX(0.24000001f), decodeY(0.0f));
		path.lineTo(decodeX(0.24000001f), decodeY(0.24000001f));
		path.lineTo(decodeX(2.7599998f), decodeY(0.24000001f));
		path.lineTo(decodeX(2.7599998f), decodeY(2.7599998f));
		path.lineTo(decodeX(0.24000001f), decodeY(2.7599998f));
		path.lineTo(decodeX(0.24000001f), decodeY(0.0f));
		path.lineTo(decodeX(0.0f), decodeY(0.0f));
		path.closePath();
		return path;
	}



	protected Paint decodeGradient1(Shape s) {
		Rectangle2D bounds = s.getBounds2D();
		float x = (float)bounds.getX();
		float y = (float)bounds.getY();
		float w = (float)bounds.getWidth();
		float h = (float)bounds.getHeight();
		return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
				new float[] { 0.0f,0.14441223f,0.43703705f,0.59444445f,0.75185186f,0.8759259f,1.0f },
				new Color[] { color2,
				decodeColor(color2,color3,0.5f),
				color3,
				decodeColor(color3,color4,0.5f),
				color4,
				decodeColor(color4,color5,0.5f),
				color5});
	}

	protected Paint decodeGradient2(Shape s) {
		Rectangle2D bounds = s.getBounds2D();
		float x = (float)bounds.getX();
		float y = (float)bounds.getY();
		float w = (float)bounds.getWidth();
		float h = (float)bounds.getHeight();
		return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
				new float[] { 0.0f,0.07147767f,0.2888889f,0.5490909f,0.7037037f,0.8518518f,1.0f },
				new Color[] { color6,
				decodeColor(color6,color7,0.5f),
				color7,
				decodeColor(color7,color8,0.5f),
				color8,
				decodeColor(color8,color9,0.5f),
				color9});
	}
	
	protected Paint decodeBgGradient(Shape s) {
		Rectangle2D bounds = s.getBounds2D();
		float x = (float)bounds.getX();
		float y = (float)bounds.getY();
		float w = (float)bounds.getWidth();
		float h = (float)bounds.getHeight();
		return decodeGradient(x,  y,  x, (1.0f * h) + y,
				new float[] { 0.0f,1.0f },
				new Color[] { bgHeaderGradientInit, bgHeaderGradientEnd});
	}
	
	protected Paint decodeGradient3(Shape s) {
		Rectangle2D bounds = s.getBounds2D();
		float x = (float)bounds.getX();
		float y = (float)bounds.getY();
		float w = (float)bounds.getWidth();
		float h = (float)bounds.getHeight();
		return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
				new float[] { 0.0f,0.07147767f,0.2888889f,0.5490909f,0.7037037f,0.7919203f,0.88013697f },
				new Color[] { color11,
				decodeColor(color11,color12,0.5f),
				color12,
				decodeColor(color12,color13,0.5f),
				color13,
				decodeColor(color13,color14,0.5f),
				color14});
	}

	protected Paint decodeGradient4(Shape s) {
		Rectangle2D bounds = s.getBounds2D();
		float x = (float)bounds.getX();
		float y = (float)bounds.getY();
		float w = (float)bounds.getWidth();
		float h = (float)bounds.getHeight();
		return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
				new float[] { 0.0f,0.07147767f,0.2888889f,0.5490909f,0.7037037f,0.8518518f,1.0f },
				new Color[] { color15,
				decodeColor(color15,color16,0.5f),
				color16,
				decodeColor(color16,color17,0.5f),
				color17,
				decodeColor(color17,color18,0.5f),
				color18});
	}

	protected Paint decodeGradient5(Shape s) {
		Rectangle2D bounds = s.getBounds2D();
		float x = (float)bounds.getX();
		float y = (float)bounds.getY();
		float w = (float)bounds.getWidth();
		float h = (float)bounds.getHeight();
		return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
				new float[] { 0.0f,0.08049711f,0.32534248f,0.56267816f,0.7037037f,0.83986557f,0.97602737f },
				new Color[] { color19,
				decodeColor(color19,color20,0.5f),
				color20,
				decodeColor(color20,color21,0.5f),
				color21,
				decodeColor(color21,color22,0.5f),
				color22});
	}

	protected Paint decodeGradient6(Shape s) {
		Rectangle2D bounds = s.getBounds2D();
		float x = (float)bounds.getX();
		float y = (float)bounds.getY();
		float w = (float)bounds.getWidth();
		float h = (float)bounds.getHeight();
		return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
				new float[] { 0.0f,0.07147767f,0.2888889f,0.5490909f,0.7037037f,0.8518518f,1.0f },
				new Color[] { color19,
				decodeColor(color19,color23,0.5f),
				color23,
				decodeColor(color23,color21,0.5f),
				color21,
				decodeColor(color21,color24,0.5f),
				color24});
	}


}
