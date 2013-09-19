package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;

import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.synth.SynthContext;

import com.ontimize.plaf.OntimizeLookAndFeel;

public class RootPanePainter {


	protected RootPanePainter(){
	}

	public static RootPanePainter getInstance(){
		return getInstance(null);
	}

	public static RootPanePainter getInstance(SynthContext synthcontext){
		return new RootPanePainter();
	}

	public void paintRootPaneBorder(JRootPane rootPane, int state, Graphics graphics, int x, int y, int width, int height){
		
		Graphics2D g2d = (Graphics2D)graphics;
		Object uiColor = OntimizeLookAndFeel.lookup("RootPane.border.background");
		
		Color selectedColor = Color.white;
		if (uiColor instanceof Color){
			selectedColor = (Color)uiColor;
		}
		
		Object arc = OntimizeLookAndFeel.lookup("RootPane.border.arc");
		int borderArc = 13;
		
		if (arc instanceof Integer){
				borderArc = ((Integer)arc).intValue();
		}
		
		g2d.setColor(selectedColor);
		g2d.fillRoundRect(x, y, width, height, borderArc, borderArc);
		
	}

	public void paintRootPaneBorder(SynthContext synthcontext, Graphics g, int x, int y, int width, int height){
		JRootPane jrootpane = (JRootPane)synthcontext.getComponent();
		paintRootPaneBorder(jrootpane,  synthcontext.getComponentState(), g, x, y, width, height);
	}
	
	protected Window getWindow(JRootPane jrootpane){
       java.awt.Container container = jrootpane.getParent();
       return (container instanceof Window) ? (Window)container : SwingUtilities.getWindowAncestor(container);
   }
	 
	protected boolean isMaximized(Window window){
		return (window instanceof Frame) && (((Frame)window).getExtendedState() & 6) == 6;
	}
}
