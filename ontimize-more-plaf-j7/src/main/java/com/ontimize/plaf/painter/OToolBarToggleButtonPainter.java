package com.ontimize.plaf.painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.gui.ApToolBarToggleButton;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;


public class OToolBarToggleButtonPainter extends AbstractOButtonPainter{
	
	public static final int BACKGROUND_SELECTED=20;
	public static final int BACKGROUND_SELECTED_FOCUSED=21;
	public static final int BACKGROUND_PRESSED_SELECTED=22;
	public static final int BACKGROUND_PRESSED_SELECTED_FOCUSED=23;
	public static final int BACKGROUND_MOUSEOVER_SELECTED=24;
	public static final int BACKGROUND_MOUSEOVER_SELECTED_FOCUSED=25;
	public static final int BACKGROUND_DISABLED_SELECTED=26;
	/**
	 * NOTE:
	 * The buttons are painted with three layers:
	 * 1) The first layer contains a degraded background and a border.
	 * 2) Second layer contains an interior border.
	 * 3) Third layer paints a centered square to highlight the icon.
	 */
	
	public static Color squareColor = OntimizeLAFColorUtils.setAlpha(Color.white, 0.1);
	
	
	// First layer colors...
	protected Color color100 = OntimizeLAFColorUtils.setAlpha(new Color(0x817F7F), 0.50);
	protected Color color101 = OntimizeLAFColorUtils.setAlpha(new Color(0x474746), 0.50);
	protected Color color102 = OntimizeLAFColorUtils.setAlpha(new Color(0xCCCCCC), 0.35);
	protected Color color103 = OntimizeLAFColorUtils.setAlpha(new Color(0xCCCCCC), 0.35);
	protected Color color104 = OntimizeLAFColorUtils.setAlpha(new Color(0x817F7F), 0.70);
	protected Color color105 = OntimizeLAFColorUtils.setAlpha(new Color(0x474746), 0.70);
	protected Color color106 = OntimizeLAFColorUtils.setAlpha(new Color(0x817F7F), 0.70);
	protected Color color107 = OntimizeLAFColorUtils.setAlpha(new Color(0x474746), 0.70);
	protected Color color108 = OntimizeLAFColorUtils.setAlpha(new Color(0x817F7F), 0.80);
	protected Color color109 = OntimizeLAFColorUtils.setAlpha(new Color(0x5A5A5A), 0.60);

	// Borders..
	protected Color color110 = OntimizeLAFColorUtils.setAlpha(new Color(0x8C8A8A), 0.5);
	protected Color color111 = OntimizeLAFColorUtils.setAlpha(new Color(0x5A5A5A), 0.6);
	protected Color color112 = OntimizeLAFColorUtils.setAlpha(new Color(0xCCCCCC), 0.35);
	protected Color color113 = OntimizeLAFColorUtils.setAlpha(new Color(0xCCCCCC), 0.35);

	// Second layer colors...
	protected Color color114 = OntimizeLAFColorUtils.setAlpha(Color.white, 0.2);
	protected Color color115 = OntimizeLAFColorUtils.setAlpha(Color.black, 0.2);

	// Selected State...
	// First layer...
	protected Color color200 = OntimizeLAFColorUtils.setAlpha(color108, 0.25);
	protected Color color201 = OntimizeLAFColorUtils.setAlpha(color108, 0.25);

	// Borders...
	protected Color color210 = new Color(0x252525);
	protected Color color211 = OntimizeLAFColorUtils.setAlpha(Color.white, 0.75);

	// Second layer colors...
	protected Color color214 = OntimizeLAFColorUtils.setAlpha(Color.white, 0.75);
	protected Color color215 = new Color(0x252525);

	protected Color bgBaseColor;

	protected float defaultAlphaSelectedPercent = 0.5f;
	protected float alphaSelectedPercent;
	protected float defaultAlphaFocusedSelectedPercent = 0.5f;
	protected float alphaFocusedSelectedPercent;
	protected float defaultAlphaPressedSelectedPercent = 0.5f;
	protected float alphaPressedSelectedPercent;
	protected float defaultAlphaPressedSelectedFocusedPercent = 0.5f;
	protected float alphaPressedSelectedFocusedPercent;
	protected float defaultAlphaMouseOverSelectedPercent = 0.5f;
	protected float alphaMouseOverSelectedPercent;
	protected float defaultAlphaMouseOverSelectedFocusedPercent = 0.5f;
	protected float alphaMouseOverSelectedFocusedPercent;
	protected float defaultAlphaDisabledSelectedPercent = 0.5f;
	protected float alphaDisabledSelectedPercent;
	
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
	protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);
	
	protected float arcwidth = 10.0f;
	protected float archeight = 10.0f;
	
	protected JComponent component;

	public OToolBarToggleButtonPainter(int state, PaintContext ctx) {
		super(state, ctx);
		init();
	}

	
	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height,
			Object[] extendedCacheKeys) {
		this.component = c;
		
		 componentColors = extendedCacheKeys;
		 if("TableButton".equals(this.component.getName())){
		    switch(state) {
	         case BACKGROUND_DEFAULT_FOCUSED: paintBackgroundDefaultAndFocused(g); break;
	         case BACKGROUND_FOCUSED: paintBackgroundFocused(g); break;
	       }
		 }else{
		    super.doPaint(g, c, width, height, extendedCacheKeys);
		 }
		
		switch(state) {
		case BACKGROUND_SELECTED: paintBackgroundSelected(g); break;
		case BACKGROUND_SELECTED_FOCUSED: paintBackgroundPressedAndDefaultAndFocused(g); break;
		case BACKGROUND_PRESSED_SELECTED: paintBackgroundPressedAndDefaultAndFocused(g); break;
		case BACKGROUND_PRESSED_SELECTED_FOCUSED: paintBackgroundPressedAndDefaultAndFocused(g); break;
		case BACKGROUND_MOUSEOVER_SELECTED: paintBackgroundMouseOverAndDefault(g);break;
		case BACKGROUND_MOUSEOVER_SELECTED_FOCUSED: paintBackgroundMouseOverAndDefaultAndFocused(g); break;
		case BACKGROUND_DISABLED_SELECTED: paintBackgroundDisabled(g); break;
		}
		
	}
	
	protected JComponent getComponent()	{
		return this.component;
	}
	
	@Override
	protected String getComponentKeyName() {
		return "ToolBar:ToggleButton";
	}
	
	protected void init(){
		super.init();
		Object obj = UIManager.getDefaults().get(getComponentKeyName() + ".background");
		if(obj instanceof Color){
			this.bgBaseColor = (Color)obj;
		}else{
			this.bgBaseColor = null;
		}
		
		obj = UIManager.getDefaults().get(getComponentKeyName() + "[Selected].alphaTransparency");
		if(obj instanceof Number){
			this.alphaSelectedPercent = ((Number)obj).floatValue();
		}else{
			this.alphaSelectedPercent = defaultAlphaSelectedPercent;
		}

		obj = UIManager.getDefaults().get(getComponentKeyName() + "[Disabled+Selected].alphaTransparency");
		if(obj instanceof Number){
			this.alphaDisabledSelectedPercent = ((Number)obj).floatValue();
		}else{
			this.alphaDisabledSelectedPercent = defaultAlphaDisabledSelectedPercent;
		}
		
		obj = UIManager.getDefaults().get(getComponentKeyName() + "[Focused+Selected].alphaTransparency");
		if(obj instanceof Number){
			this.alphaFocusedSelectedPercent = ((Number)obj).floatValue();
		}else{
			this.alphaFocusedSelectedPercent = defaultAlphaFocusedSelectedPercent;
		}
		
		obj = UIManager.getDefaults().get(getComponentKeyName() + "[MouseOver+Selected].alphaTransparency");
		if(obj instanceof Number){
			this.alphaMouseOverSelectedPercent = ((Number)obj).floatValue();
		}else{
			this.alphaMouseOverSelectedPercent = defaultAlphaMouseOverSelectedPercent;
		}
		
		obj = UIManager.getDefaults().get(getComponentKeyName() + "[Focused+MouseOver+Selected].alphaTransparency");
		if(obj instanceof Number){
			this.alphaMouseOverSelectedFocusedPercent = ((Number)obj).floatValue();
		}else{
			this.alphaMouseOverSelectedFocusedPercent = defaultAlphaMouseOverSelectedFocusedPercent;
		}
		
		obj = UIManager.getDefaults().get(getComponentKeyName() + "[Pressed+Selected].alphaTransparency");
		if(obj instanceof Number){
			this.alphaPressedSelectedPercent = ((Number)obj).floatValue();
		}else{
			this.alphaPressedSelectedPercent = defaultAlphaPressedSelectedPercent;
		}
		
		obj = UIManager.getDefaults().get(getComponentKeyName() + "[Focused+Pressed+Selected].alphaTransparency");
		if(obj instanceof Number){
			this.alphaPressedSelectedFocusedPercent = ((Number)obj).floatValue();
		}else{
			this.alphaPressedSelectedFocusedPercent = defaultAlphaPressedSelectedFocusedPercent;
		}
		
	}
	
	@Override
	protected Object[] getExtendedCacheKeys(JComponent c) {
		
		if (!initColors && c!=null){
    		initColors = true;
    		Color background = this.bgBaseColor!=null ? this.bgBaseColor : c.getBackground();
    		color100 = background;																	/*init enabled*/
    		color101 = getDerivedColor(background, 0.16666667f, -0.0014193691f, -0.227451f, 0);		/*end enabled*/
    		color102 = getDerivedColor(background, 0.0f, -0.015503876f, 0.29411763f, -39);			/*init disabled*/
    		color103 = getDerivedColor(background, 0.0f, -0.015503876f, 0.29411763f, -39);			/*end disabled*/
    		color104 = getDerivedColor(background, 0.0f, 0.0f, 0.0f, 51);							/*init focused*/
    		color105 = getDerivedColor(background, 0.16666667f, -0.0014193691f, -0.227451f, 51);	/*end focused*/
    		color106 = getDerivedColor(background, 0.0f, 0.0f, 0.0f, 51);							/*init mouseover*/
    		color107 = getDerivedColor(background, 0.16666667f, -0.0014193691f, -0.227451f, 51);	/*end mouseover*/
    		color108 = getDerivedColor(background, 0.0f, 0.0f, 0.0f, 76);							/*init pressed*/
    		color109 = getDerivedColor(background, 0.0f, -0.015503876f, -0.1529412f, 25);			/*end pressed*/
    		
    		color110 = getDerivedColor(background, 0.0f, -0.0012181615f, 0.043137252f, 0);			/*init border*/
    		color111 = getDerivedColor(background, 0.0f, -0.015503876f, -0.1529412f, 25);			/*end border*/
    		color112 = getDerivedColor(background, 0.0f, -0.015503876f, 0.29411763f, -39);			/*init border Disabled*/
    		color113 = getDerivedColor(background, 0.0f, -0.015503876f, 0.29411763f, -39);			/*end border Disabled*/
    		
    		color114 = getDerivedColor(background, 0.0f, -0.015503876f, 0.49411762f, -51);			/*init middle*/
    		color115 = getDerivedColor(background, 0.0f, -0.015503876f, -0.5058824f, -77);			/*end middle*/
    		
    		color200 = getDerivedColor(background, 0.0f, 0.0f, 0.0f, -64);						/*init selected enabled*/
    		color201 = getDerivedColor(background, 0.0f, 0.0f, 0.0f, -64);						/*end selected enabled*/
    		
    		color210 = getDerivedColor(background, 0.0f, -0.015503876f, 0.1607843f, 127);			/*init selected border*/
    		color211 = getDerivedColor(background, 0.0f, -0.015503876f, 0.1607843f, 127);			/*end selected border*/
    		
    		color214 = getDerivedColor(background, 0.0f, -0.015503876f, 0.22745097f, -13);			/*init middle selected*/
    		color215 = getDerivedColor(background, 0.0f, -0.015503876f, -0.36078435f, 127);			/*end middle selected*/
		}
		
		Object[] extendedCacheKeys = null;
		switch (state) {
		case BACKGROUND_ENABLED:
			extendedCacheKeys = new Object[] { color100, color101, color110, color111, color114, color115 };
			break;
		case BACKGROUND_DISABLED:
			extendedCacheKeys = new Object[] { color102, color103, color112, color113, color114, color115 };
			break;
		case BACKGROUND_FOCUSED:
			extendedCacheKeys = new Object[] { color104, color105, color110, color111, color114, color115 };
			break;
		case BACKGROUND_MOUSEOVER:
			extendedCacheKeys = new Object[] { color106, color107, color110, color111, color114, color115 };
			break;
		case BACKGROUND_MOUSEOVER_FOCUSED:
			extendedCacheKeys = new Object[] { color106, color107, color110, color111, color114, color115 };
			break;
		case BACKGROUND_PRESSED:
			extendedCacheKeys = new Object[] { color108, color109, color110, color111, color114, color115 };
			break;
		case BACKGROUND_PRESSED_FOCUSED:
			extendedCacheKeys = new Object[] { color108, color109, color110, color111, color114, color115 };
			break;
		case BACKGROUND_SELECTED:
			extendedCacheKeys = new Object[] { color200, color201, color210, color211, color214, color215 };
			break;
		case BACKGROUND_SELECTED_FOCUSED:
			extendedCacheKeys = new Object[] { color200, color201, color210, color211, color214, color215 };
			break;
		case BACKGROUND_PRESSED_SELECTED:
			extendedCacheKeys = new Object[] { color200, color201, color210, color211, color214, color215 };
			break;
		case BACKGROUND_PRESSED_SELECTED_FOCUSED:
			extendedCacheKeys = new Object[] { color200, color201, color210, color211, color214, color215 };
			break;
		case BACKGROUND_MOUSEOVER_SELECTED:
			extendedCacheKeys = new Object[] { color200, color201, color210, color211, color214, color215 };
			break;
		case BACKGROUND_MOUSEOVER_SELECTED_FOCUSED:
			extendedCacheKeys = new Object[] { color200, color201, color210, color211, color214, color215 };
			break;
		case BACKGROUND_DISABLED_SELECTED:
			extendedCacheKeys = new Object[] { color200, color201, color110, color111, color214, color215 };
			break;
		default:
			super.getExtendedCacheKeys(c);
			break;
		}

		return extendedCacheKeys;
	}
	
	 protected AlphaComposite getDerivedAlphaComposite(){
	    	AlphaComposite alpha = null;
	    	 switch(state) {
		         case BACKGROUND_SELECTED: alpha = AlphaComposite.SrcOver.derive(  (alphaSelectedPercent>=0 && alphaSelectedPercent<=1) ? alphaSelectedPercent : defaultAlphaSelectedPercent  ); break;
		         case BACKGROUND_SELECTED_FOCUSED: alpha = AlphaComposite.SrcOver.derive(  (alphaFocusedSelectedPercent>=0 && alphaFocusedSelectedPercent<=1) ? alphaFocusedSelectedPercent : defaultAlphaFocusedSelectedPercent  ); break;
		         case BACKGROUND_PRESSED_SELECTED: alpha = AlphaComposite.SrcOver.derive(  (alphaPressedSelectedPercent>=0 && alphaPressedSelectedPercent<=1) ? alphaPressedSelectedPercent : defaultAlphaPressedSelectedPercent  ); break;
		         case BACKGROUND_PRESSED_SELECTED_FOCUSED: alpha = AlphaComposite.SrcOver.derive(  (alphaPressedSelectedFocusedPercent>=0 && alphaPressedSelectedFocusedPercent<=1) ? alphaPressedSelectedFocusedPercent : defaultAlphaPressedSelectedFocusedPercent  ); break;
		         case BACKGROUND_MOUSEOVER_SELECTED: alpha = AlphaComposite.SrcOver.derive(  (alphaMouseOverSelectedPercent>=0 && alphaMouseOverSelectedPercent<=1) ? alphaMouseOverSelectedPercent : defaultAlphaMouseOverSelectedPercent  ); break;
		         case BACKGROUND_MOUSEOVER_SELECTED_FOCUSED: alpha = AlphaComposite.SrcOver.derive(  (alphaMouseOverSelectedFocusedPercent>=0 && alphaMouseOverSelectedFocusedPercent<=1) ? alphaMouseOverSelectedFocusedPercent : defaultAlphaMouseOverSelectedFocusedPercent ); break;
		         case BACKGROUND_DISABLED_SELECTED: alpha = AlphaComposite.SrcOver.derive(  (alphaDisabledSelectedPercent>=0 && alphaDisabledSelectedPercent<=1) ? alphaDisabledSelectedPercent : defaultAlphaDisabledSelectedPercent  ); break;
		         default: alpha = super.getDerivedAlphaComposite();
	    	 }
	    	return alpha;
	    }
	
	protected boolean isSquareButton(){
		JComponent c = getComponent();
		Rectangle bounds = c.getBounds();
		Insets in = c.getInsets();
		
		int width = bounds.width - in.left - in.right;
		int height = bounds.height - in.top - in.bottom;
		if(width==height){
			return true;
		}
		return false;
	}
	
	protected int getRestrictiveMeasure(){
		JComponent c = getComponent();
		Rectangle bounds = c.getBounds();
		Insets in = c.getInsets();
		
		int width = bounds.width - in.left - in.right;
		int height = bounds.height - in.top - in.bottom;
		if(width<=height){
			return width;
		}
		return height;
	}
	
	protected boolean hasText(){
		JComponent c = getComponent();
		if(c instanceof AbstractButton){
			String text = ((AbstractButton)c).getText();
			if(text!=null && text.length()>0){
				return true;
			}
		}
		return false;
	}
	
	protected int getSquareOffset(){
		JComponent c = getComponent();
		if (c != null && !isSquareButton() && !hasText()) {
			Insets in = c.getInsets();
			Rectangle bounds = c.getBounds();
			
			int width = bounds.width - in.left - in.right;
			int height = bounds.height - in.top - in.bottom;
			int offset = (height-width)/2;
			return offset;
		}else if(c != null && hasText()){
			return 4; //margin for separate the text
		}
		return 0; //By default
	}
	
	public static  Color defaultFocusBgColor = new Color(0x366581);
	protected Color focusBgColor;
	
	protected void paintBackgroundDefaultAndFocused(Graphics2D g) {
    	AlphaComposite old = (AlphaComposite) g.getComposite();
    	g.setComposite(getDerivedAlphaComposite());
        roundRect = decodeRoundRect4();
        g.setPaint(focusBgColor!=null ? focusBgColor : defaultFocusBgColor);
        g.fill(roundRect);
        g.setComposite(old);
    }
	
	protected void paintBackgroundFocused(Graphics2D g) {
    	AlphaComposite old = (AlphaComposite) g.getComposite();
    	g.setComposite(getDerivedAlphaComposite());
        roundRect = decodeRoundRect4();
        g.setPaint(focusBgColor!=null ? focusBgColor : defaultFocusBgColor);
        g.fill(roundRect);
        g.setComposite(old);
    }
	
	@Override
	protected void paintBackgroundDefault(Graphics2D g) {
		g.setComposite(getDerivedAlphaComposite());
		//*********************************************
		//First layer...
		roundRect = decodeRoundRect1();
		g.setPaint(decodeGradientFirstLayer(roundRect));
		g.fill(roundRect);
		//border
		roundRect = decodeRoundRect1Border();
		g.setPaint(decodeGradientFirstLayerBorder(roundRect));
		g.draw(roundRect);

		//*********************************************
		//Second layer...
		//border
		if (this.state != BACKGROUND_DISABLED) {
			roundRect = decodeRoundRect2Border();
			g.setPaint(decodeGradientMiddleLayerBorder(roundRect));
			g.draw(roundRect);
		}
		
		//*********************************************
		//Third layer...
		if (!hasText()) {
			rect = decodeCenterSquare();
			g.setPaint(squareColor);
			g.fill(rect);
		}
	}
	
	@Override
	protected void paintBackgroundEnabled(Graphics2D g) {
		paintBackgroundDefault(g);
	}
	
	@Override
	protected void paintBackgroundDisabled(Graphics2D g) {
		paintBackgroundDefault(g);
	}
	
	@Override
	protected void paintBackgroundMouseOver(Graphics2D g) {
		paintBackgroundDefault(g);
	}
	
	 @Override
	protected void paintBackgroundMouseOverAndFocused(Graphics2D g) {
		paintBackgroundDefault(g);
	}
	
	 @Override
   protected void paintBackgroundPressed(Graphics2D g) {
      paintBackgroundDefault(g);
   }
	 
	 @Override
	protected void paintBackgroundPressedAndFocused(Graphics2D g) {
		paintBackgroundDefault(g);
	}
	 
	 @Override
   protected void paintBackgroundPressedAndDefaultAndFocused(Graphics2D g) {
      paintBackgroundSelectedDefault(g);
   }

   @Override
   protected void paintBackgroundMouseOverAndDefault(Graphics2D g) {
      paintBackgroundSelectedDefault(g);
   }

   @Override
   protected void paintBackgroundMouseOverAndDefaultAndFocused(Graphics2D g) {
      paintBackgroundSelectedDefault(g);
   }
   
   protected void paintBackgroundSelected(Graphics2D g) {
      paintBackgroundSelectedDefault(g);
   }
   
   protected void paintBackgroundSelectedDefault(Graphics2D g) {
	   g.setComposite(getDerivedAlphaComposite());
      //*********************************************
      //First layer...
      roundRect = decodeRoundRect1();
      g.setPaint(decodeGradientFirstLayer(roundRect));
      g.fill(roundRect);
      //border
      roundRect = decodeRoundRect1Border();
      g.setPaint(decodeGradientSelectedFirstLayerBorder(roundRect));
      g.draw(roundRect);

      //*********************************************
      //Second layer...
      //border
      if (this.state != BACKGROUND_DISABLED) {
         roundRect = decodeRoundRect2Border();
         g.setPaint(decodeGradientSelectedMiddleLayerBorder(roundRect));
         g.fill(roundRect);
      }

      //*********************************************
      //Third layer...
      if (!hasText()) {
         rect = decodeCenterSquare();
         g.setPaint(squareColor);
         g.fill(rect);
      }
   }
	
	protected RoundRectangle2D decodeRoundRect1() {
		JComponent c = getComponent();
		if (c != null) {
			Insets in = c.getInsets();
			Rectangle bounds = c.getBounds();
			int x = in.left + 1 -getSquareOffset();
			int width = bounds.width - in.left - in.right - 1 + 2*getSquareOffset();
			roundRect.setRoundRect(x<0 ? 0 : x, // x
					in.top+1, // y
					width >= bounds.width ? bounds.width-1 : width, // width
					bounds.height - in.top - in.bottom -1, // height
					arcwidth, arcwidth); // rounding
		} else {
			roundRect.setRoundRect(decodeX(1.0f), // x
					decodeY(1.0f), // y
					decodeX(2.0f) - decodeX(1.0f), // width
					decodeY(2.0f) - decodeY(1.0f), // height
					arcwidth, arcwidth); // rounding
		}
		return roundRect;
	}
	
	protected RoundRectangle2D decodeRoundRect1Border() {
		JComponent c = getComponent();
		if (c != null) {
			Insets in = c.getInsets();
			Rectangle bounds = c.getBounds();
			int x = in.left -getSquareOffset();
			int width = bounds.width - in.left - in.right +2*getSquareOffset();
			roundRect.setRoundRect(x<0 ? 0 : x, // x
					in.top, // y
					width >= bounds.width ? bounds.width -1 : width, // width
					bounds.height - in.top - in.bottom-1, // height
					arcwidth, arcwidth); // rounding
		} else {
			roundRect.setRoundRect(decodeX(0.0f), // x
					decodeY(0.0f), // y
					decodeX(3.0f) - 1, // width
					decodeY(3.0f) - 1, // height
					arcwidth, arcwidth); // rounding
		}
		return roundRect;
	}
	
	
	protected RoundRectangle2D decodeRoundRect2Border() {
		JComponent c = getComponent();
		if (c != null) {
			Insets in = c.getInsets();
			Rectangle bounds = c.getBounds();
			int x = in.left -getSquareOffset();
			int width = bounds.width - in.left - in.right +2*getSquareOffset();
			roundRect.setRoundRect(x<0 ? 0 : x, // x
					in.top, // y
					width >= bounds.width ? bounds.width-1 : width, // width
					bounds.height - in.top - in.bottom-2, // height
					arcwidth, arcwidth); // rounding
		} else {
			roundRect.setRoundRect(
					decodeX(0.0f), // x
					decodeY(0.0f), // y
					decodeX(3.0f)-1, 
					decodeY(2.0f) - decodeY(1.0f), 
					arcwidth, arcwidth); // rounding
		}
		return roundRect;
	}
	
	
	protected Rectangle2D decodeCenterSquare() {
		JComponent c = getComponent();
		if (c instanceof ApToolBarToggleButton) {
			Icon icon = ((ApToolBarToggleButton)c).getIcon();
			Rectangle bounds = c.getBounds();
			Insets in = c.getInsets();
			int width = bounds.width - in.left - in.right;
			int height = bounds.height - in.top -in.bottom;
			int iconWidth = icon.getIconWidth();
			int iconHeight = icon.getIconHeight();
			
			int pWidth = iconWidth>=width ? getRestrictiveMeasure() : iconWidth;
			int pHeight = iconHeight>=height ? getRestrictiveMeasure() : iconHeight;
			
			rect.setRect(bounds.width/2.0 - pWidth/2.0, // x
					bounds.height/2.0 - pHeight/2.0, // y
					pWidth, // width
					pHeight); // height
		} else {
			rect.setRect(decodeX(1.13f), // x
					decodeY(1.13f), // y
					decodeX(1.7f), // width
					decodeY(1.7f)); // height
		}
		return rect;
	}
	
	
	
	protected Paint decodeGradientFirstLayer(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float h = (float)bounds.getHeight();
        return decodeGradient( x,  y,  x, y+h,
                new float[] { 0.0f, 1.0f },
        new Color[] { (Color) componentColors[0], 
        		(Color) componentColors[1]});
    }
	
	
	protected Paint decodeGradientFirstLayerBorder(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float h = (float)bounds.getHeight();
        return decodeGradient( x,  y,  x,  y+h-1,
                new float[] { 0.0f, 1.0f },
                new Color[] { (Color) componentColors[2], 
        		(Color) componentColors[3]});
    }
	
	/**
	 * This method recovers the gradient for the border of the middle layer.
	 * @param s
	 * @return
	 */
	protected Paint decodeGradientMiddleLayerBorder(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float h = (float)bounds.getHeight();
        return decodeGradient( x,  y,  x, y+h-1,
                new float[] { 0.0f, 1.0f },
                new Color[] { (Color) componentColors[4], 
        		(Color) componentColors[5]});
    }
	
	
	protected Paint decodeGradientSelectedFirstLayerBorder(Shape s) {
      Rectangle2D bounds = s.getBounds2D();
      float x = (float)bounds.getX();
      float y = (float)bounds.getY();
      float h = (float)bounds.getHeight();
      return decodeGradient( x,  y,  x,  y+h-1,
              new float[] { 0.0f, 1.0f },
              new Color[] { (Color) componentColors[2], 
    		  (Color) componentColors[3]});
  }
	
	protected Paint decodeGradientSelectedMiddleLayerBorder(Shape s) {
      Rectangle2D bounds = s.getBounds2D();
      float x = (float)bounds.getX();
      float y = (float)bounds.getY();
      float h = (float)bounds.getHeight();
      return decodeGradient( x,  y,  x, y+h-1,
              new float[] { 0.0f, 1.0f },
              new Color[] { (Color) componentColors[4],  
    		  (Color) componentColors[5]});
  }
}
