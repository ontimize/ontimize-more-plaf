package com.ontimize.plaf.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

import sun.swing.plaf.synth.SynthUI;

import com.ontimize.gui.container.Tab;
import com.ontimize.plaf.OntimizeContext;
import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.OntimizeStyle;

public class OPanelUI extends BasicPanelUI implements PropertyChangeListener, SynthUI {
	protected SynthStyle style;

	public static ComponentUI createUI(JComponent c) {
		return new OPanelUI();
	}

	public void installUI(JComponent c) {
		JPanel p = (JPanel) c;

		super.installUI(c);
		installListeners(p);
	}

	public void uninstallUI(JComponent c) {
		JPanel p = (JPanel) c;

		uninstallListeners(p);
		super.uninstallUI(c);
	}

	protected void installListeners(JPanel p) {
		p.addPropertyChangeListener(this);
	}

	protected void uninstallListeners(JPanel p) {
		p.removePropertyChangeListener(this);
	}

	protected void installDefaults(JPanel p) {
		updateStyle(p);
	}

	protected void uninstallDefaults(JPanel p) {
		OntimizeContext context = getContext(p, ENABLED);

		style.uninstallDefaults(context);
		context.dispose();
		style = null;
	}

	protected void updateStyle(JPanel c) {
		OntimizeContext context = getContext(c, ENABLED);
		SynthStyle      oldStyle = style;
		
//		style = OntimizeLookAndFeel.updateStyle(context, this);
//		if(c instanceof Tab){
//			Object obj = UIManager.getDefaults().get("TabbedPane:TabbedPaneContent.background");
//			if(obj instanceof Color){
//				c.setBackground((Color)obj);
//			}
//		}
//		
//		
//		
//		context.dispose();
		
        style = OntimizeLookAndFeel.updateStyle(context, this);

        if (style != oldStyle) {
            updateStyle(c, context, getPropertyPrefix(c));
        }
        context.dispose();
	}
	
	/**
     * Fetches the name used as a key to lookup properties through the
     * UIManager.  This is used as a prefix to all the standard
     * text properties.
     *
     * @return the name ("TextField")
     */
    protected String getPropertyPrefix(JComponent c) {
    	if(c instanceof Tab){
    		return "TabbedPane:TabbedPaneContent";
    	}
    	return "Panel";
    }
	
	protected void updateStyle(JComponent c, OntimizeContext context, String prefix){
		OntimizeStyle style = (OntimizeStyle) context.getStyle();
		
		 Color bg = c.getBackground();
        if ((bg == null) || (bg instanceof UIResource)) {
            c.setBackground((Color)UIManager.get(prefix + ".background"));
        }
	}

	public OntimizeContext getContext(JComponent c) {
		return getContext(c, getComponentState(c));
	}

	protected OntimizeContext getContext(JComponent c, int state) {
		return OntimizeContext.getContext(OntimizeContext.class, c, OntimizeLookAndFeel.getRegion(c), style, state);
	}

	protected Region getRegion(JComponent c) {
		return OntimizeLookAndFeel.getRegion(c);
	}

	protected int getComponentState(JComponent c) {
		return OntimizeLookAndFeel.getComponentState(c);
	}

	public void update(Graphics g, JComponent c) {
		OntimizeContext context = getContext(c);

		OntimizeLookAndFeel.update(context, g);
		context.getPainter().paintPanelBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
		paint(context, g);
		context.dispose();
	}

	public void paint(Graphics g, JComponent c) {
		OntimizeContext context = getContext(c);

		paint(context, g);
		context.dispose();
	}

	protected void paint(OntimizeContext context, Graphics g) {
		// do actual painting
	}

	public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
		((OntimizeContext)context).getPainter().paintPanelBorder(context, g, x, y, w, h);
	}

	public void propertyChange(PropertyChangeEvent pce) {
		if (OntimizeLookAndFeel.shouldUpdateStyle(pce)) {
			updateStyle((JPanel) pce.getSource());
		}
	}

}
