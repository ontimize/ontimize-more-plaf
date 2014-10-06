package com.ontimize.plaf.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;
import javax.swing.text.JTextComponent;

import com.ontimize.gui.field.DataField;
import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.OntimizeStyle;
import com.ontimize.plaf.utils.ContextUtils;

public class OTextAreaUI extends BasicTextAreaUI implements SynthUI, FocusListener {
    protected SynthStyle style;

    /**
     * Creates a UI for a JTextArea.
     *
     * @param ta a text area
     * @return the UI
     */
    public static ComponentUI createUI(JComponent ta) {
        return new OTextAreaUI();
    }

    public void focusGained(FocusEvent e) {
        getComponent().repaint();
    }

    public void focusLost(FocusEvent e) {
        getComponent().repaint();
    }
    
	protected void installDefaults() {
		// Installs the text cursor on the component
		super.installDefaults();

		if (this.style == null) {
			this.style = OntimizeStyle.NULL_STYLE;
		}
		
		// Ontimize DataField stores foreground color into 'fontColor' variable
		// that is just initialized on init method. On new installations of
		// Look&Feel it is necessary to update this value.
		JTextComponent c = getComponent();
		Object dataField = SwingUtilities.getAncestorOfClass(DataField.class, c);
		if (dataField instanceof DataField) {
			Color fg = ((DataField) dataField).getFontColor();
			if (fg == null || fg instanceof UIResource) {
				((DataField) dataField).setFontColor(c.getForeground());
			}
		}

		updateStyle((JTextComponent) getComponent());
		getComponent().addFocusListener(this);

	}

    protected void uninstallDefaults() {
        SynthContext context = getContext(getComponent(), ENABLED);

        getComponent().putClientProperty("caretAspectRatio", null);
        getComponent().removeFocusListener(this);

        style.uninstallDefaults(context);
        
        style = null;
        super.uninstallDefaults();
        
        if (getComponent().getForeground() instanceof UIResource) {
    		getComponent().setForeground(null);
        }
    	if (getComponent().getBackground() instanceof UIResource) {
    		getComponent().setBackground(null);
        }
    	if (getComponent().getFont() instanceof UIResource) {
    		getComponent().setFont(null);
        }
    }

    public void installUI(JComponent c) {
        super.installUI(c);
    }

    protected void updateStyle(JTextComponent comp) {
    	SynthContext context = getContext(comp, ENABLED);
        SynthStyle oldStyle = style;

        style = OntimizeLookAndFeel.updateStyle(context, this);

        if (style != oldStyle) {
            OTextFieldUI.updateStyle(comp, context, getPropertyPrefix());

            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        
    }

    public SynthContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    protected SynthContext getContext(JComponent c, int state) {
    	return new SynthContext(c, OntimizeLookAndFeel.getRegion(c), this.style, state);
    }

    protected int getComponentState(JComponent c) {
        return OntimizeLookAndFeel.getComponentState(c);
    }

    public void update(Graphics g, JComponent c) {
    	SynthContext context = getContext(c);

        OntimizeLookAndFeel.update(context, g);
        ContextUtils.getPainter(context).paintTextAreaBackground(context,
                          g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        
    }

    protected void paint(SynthContext context, Graphics g) {
        super.paint(g, getComponent());
    }
    

    protected void paintBackground(Graphics g) {
        //Override to do nothing, all our painting is done from update/paint.
    }

    public void paintBorder(SynthContext context, Graphics g, int x,
                            int y, int w, int h) {
        ContextUtils.getPainter(context).paintTextAreaBorder(context, g, x, y, w, h);
    }

    /**
     * This method gets called when a bound property is changed
     * on the associated JTextComponent.  This is a hook
     * which UI implementations may change to reflect how the
     * UI displays bound properties of JTextComponent subclasses.
     * This is implemented to rebuild the View when the
     * <em>WrapLine</em> or the <em>WrapStyleWord</em> property changes.
     *
     * @param evt the property change event
     */
    protected void propertyChange(PropertyChangeEvent evt) {
        if (OntimizeLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle((JTextComponent)evt.getSource());
        }
        super.propertyChange(evt);
    }
}
