package com.ontimize.plaf.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ViewportUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;

import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.OntimizeStyle;
import com.ontimize.plaf.painter.ViewportPainter;
import com.ontimize.plaf.utils.ContextUtils;

/**
 * OViewportUI implementation.
 * 
 * Based on SynthViewPortUI, which is package local.
 * 
 * @see javax.swing.plaf.synth.SynthViewportUI
 */
public class OViewportUI extends ViewportUI implements PropertyChangeListener, SynthUI {
    protected SynthStyle style;

    public static ComponentUI createUI(JComponent c) {
        return new OViewportUI();
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        installDefaults(c);
        installListeners(c);
    }

    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        uninstallListeners(c);
        uninstallDefaults(c);
    }

    protected void installDefaults(JComponent c) {
    	if (this.style == null) {
			this.style = OntimizeStyle.NULL_STYLE;
		}
        updateStyle(c);
    }

    protected void updateStyle(JComponent c) {
        SynthContext context = getContext(c, ENABLED);
        
		if(c.getComponents().length>0){
			int length = c.getComponents().length;
			for(int i=0;i<length;i++){
				if(c.getComponent(i) instanceof JTextArea){
					LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
					c.setOpaque(false);
				}
			}
		}

        // Note: JViewport is special cased as it does not allow for
        // a border to be set. JViewport.setBorder is overriden to throw
        // an IllegalArgumentException. Refer to SynthScrollPaneUI for
        // details of this.
        SynthStyle newStyle = SynthLookAndFeel.getStyle(context.getComponent(), context.getRegion());
        SynthStyle oldStyle = context.getStyle();

        if (newStyle != oldStyle) {
            if (oldStyle != null) {
                oldStyle.uninstallDefaults(context);
            }
            ContextUtils.setComponentStyle(context, newStyle);
			OntimizeLookAndFeel.updateStyle(context, this);
        }
        this.style = newStyle;
        
    }

    protected void installListeners(JComponent c) {
        c.addPropertyChangeListener(this);
    }

    protected void uninstallListeners(JComponent c) {
        c.removePropertyChangeListener(this);
    }

    protected void uninstallDefaults(JComponent c) {
    	SynthContext context = getContext(c, ENABLED);
        style.uninstallDefaults(context);
        
        style = null;
    }

    public SynthContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    protected SynthContext getContext(JComponent c, int state) {
    	return new SynthContext( c, this.getRegion(c), this.style, state);
    }

    protected Region getRegion(JComponent c) {
        return SynthLookAndFeel.getRegion(c);
    }

    protected int getComponentState(JComponent c) {
        return OntimizeLookAndFeel.getComponentState(c);
    }

    public void update(Graphics g, JComponent c) {
    	SynthContext context = getContext(c);

    	OntimizeLookAndFeel.update(context, g);
        ContextUtils.getPainter(context).paintViewportBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        // This does nothing on purpose, JViewport doesn't allow a border
        // and therefore this will NEVER be called.
    }

    public void paint(Graphics g, JComponent c) {
    	SynthContext context = getContext(c);

        paint(context, g);
        
    }

    protected void paint(SynthContext context, Graphics g) {
        JComponent c = context.getComponent();
        JViewport viewport = (JViewport) c;
        if (c.isOpaque()) {
            Component view = viewport.getView();
            Object ui = (view == null) ? null : invokeGetter(view, "getUI", null);
            if (ui instanceof ViewportPainter) {
                ((ViewportPainter) ui).paintViewport(context, g, viewport);
            } 
            else {
                if (viewport.getView() != null) {
                    g.setColor(viewport.getView().getBackground());
                    g.fillRect(0, 0, c.getWidth(), c.getHeight());
                }
            }
        }
        
    }

    /**
     * Invokes the specified getter method if it exists.
     * 
     * @param obj
     *            The object on which to invoke the method.
     * @param methodName
     *            The name of the method.
     * @param defaultValue
     *            This value is returned, if the method does not exist.
     * @return The value returned by the getter method or the default value.
     */
    protected static Object invokeGetter(Object obj, String methodName, Object defaultValue) {
        try {
            Method method = obj.getClass().getMethod(methodName, new Class[0]);
            Object result = method.invoke(obj, new Object[0]);
            return result;
        } catch (NoSuchMethodException e) {
            return defaultValue;
        } catch (IllegalAccessException e) {
            return defaultValue;
        } catch (InvocationTargetException e) {
            return defaultValue;
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (OntimizeLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle((JComponent) e.getSource());
        }
    }
}
