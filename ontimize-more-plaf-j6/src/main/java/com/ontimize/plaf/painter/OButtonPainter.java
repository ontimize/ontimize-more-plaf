package com.ontimize.plaf.painter;

import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.UIManager;


public class OButtonPainter extends AbstractOButtonPainter{

	public OButtonPainter(int state, PaintContext ctx) {
		super(state, ctx);
	}
	
	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		super.doPaint(g, c, width, height, extendedCacheKeys);
	}
	
	
	@Override
	protected String getComponentKeyName() {
		return "Button";
	}
	
	@Override
	protected void init (){
		super.init();
		
		Object obj = UIManager.getDefaults().get("Button[Default].alphaTransparency");
		if(obj instanceof Number){
			this.alphaDefaultPercent = ((Number)obj).floatValue();
		}else{
			this.alphaDefaultPercent = defaultAlphaDefaultPercent;
		}

		obj = UIManager.getDefaults().get("Button[Default+Focused].alphaTransparency");
		if(obj instanceof Number){
			this.alphaFocusedDefaultPercent = ((Number)obj).floatValue();
		}else{
			this.alphaFocusedDefaultPercent = defaultAlphaFocusedDefaultPercent;
		}
		
		obj = UIManager.getDefaults().get("Button[Default+MouseOver].alphaTransparency");
		if(obj instanceof Number){
			this.alphaMouseOverDefaultPercent = ((Number)obj).floatValue();
		}else{
			this.alphaMouseOverDefaultPercent = defaultAlphaMouseOverDefaultPercent;
		}
		
		obj = UIManager.getDefaults().get("Button[Default+Focused+MouseOver].alphaTransparency");
		if(obj instanceof Number){
			this.alphaMouseOverDefaultFocusedPercent = ((Number)obj).floatValue();
		}else{
			this.alphaMouseOverDefaultFocusedPercent = defaultAlphaMouseOverDefaultFocusedPercent;
		}
		
		obj = UIManager.getDefaults().get("Button[Default+Pressed].alphaTransparency");
		if(obj instanceof Number){
			this.alphaPressedDefaultPercent = ((Number)obj).floatValue();
		}else{
			this.alphaPressedDefaultPercent = defaultAlphaPressedDefaultPercent;
		}
		
		obj = UIManager.getDefaults().get("Button[Default+Focused+Pressed].alphaTransparency");
		if(obj instanceof Number){
			this.alphaPressedDefaultFocusedPercent = ((Number)obj).floatValue();
		}else{
			this.alphaPressedDefaultFocusedPercent = defaultAlphaPressedDefaultFocusedPercent;
		}
		
	}

}
