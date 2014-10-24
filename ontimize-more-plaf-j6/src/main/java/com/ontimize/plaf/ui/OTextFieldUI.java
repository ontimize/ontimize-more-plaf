package com.ontimize.plaf.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;

import sun.swing.SwingUtilities2;
import sun.swing.plaf.synth.SynthUI;

import com.ontimize.gui.Form.StatusBar;
import com.ontimize.gui.field.DataField;
import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.utils.ContextUtils;
import com.ontimize.plaf.utils.ReflectionUtils;

public class OTextFieldUI extends BasicTextFieldUI implements SynthUI, FocusListener {
	
	protected String placeholderText;
	protected Color  placeholderColor;

	protected SynthStyle style;
	
	public OTextFieldUI() {
		super();
	}
	
	/**
     * Creates a UI for a JTextField.
     *
     * @param  c the text field
     *
     * @return the UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new OTextFieldUI();
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    protected void updateStyle(JTextComponent c) {
        SynthContext context  = getContext(c, ENABLED);
        SynthStyle      oldStyle = style;

        style = OntimizeLookAndFeel.updateStyle(context, this);

        if (style != oldStyle) {
            updateStyle(c, context, getPropertyPrefix());

            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }

        

    }
    
    /**
     * protected method to update styles.
     *
     * @param c       the JTextField component.
     * @param context the SynthContext.
     * @param prefix  the control prefix, e.g. "TextField",
     *                "FormattedTextField", or "PasswordField".
     */
    static void updateStyle(JTextComponent c, SynthContext context, String prefix) {
    	SynthStyle style = context.getStyle();

    	if (c instanceof StatusBar){
    	    ((StatusBar)c).setLFConfiguration(true);
    	}
    	
        Color color = c.getCaretColor();

        if (color == null || color instanceof UIResource) {
            c.setCaretColor((Color) style.get(context, prefix + ".caretForeground"));
        }

        Color fg = c.getForeground();

        if (fg == null || fg instanceof UIResource) {
//            fg = style.getColorForState(context, ColorType.TEXT_FOREGROUND);
        	fg = (Color)ReflectionUtils.invoke(style, "getColorForState", new Object[]{context, ColorType.TEXT_FOREGROUND});
            if (fg != null) {
                c.setForeground(fg);
            }
        }

        Object ar = style.get(context, prefix + ".caretAspectRatio");

        if (ar instanceof Number) {
            c.putClientProperty("caretAspectRatio", ar);
        }

        ContextUtils.setComponentState(context, SELECTED | FOCUSED);

        Color s = c.getSelectionColor();

        if (s == null || s instanceof UIResource) {
            c.setSelectionColor(style.getColor(context, ColorType.TEXT_BACKGROUND));
        }

        Color sfg = c.getSelectedTextColor();

        if (sfg == null || sfg instanceof UIResource) {
            c.setSelectedTextColor(style.getColor(context, ColorType.TEXT_FOREGROUND));
        }

        ContextUtils.setComponentState(context, DISABLED);

        Color dfg = c.getDisabledTextColor();

        if (dfg == null || dfg instanceof UIResource) {
            c.setDisabledTextColor(style.getColor(context, ColorType.TEXT_FOREGROUND));
        }

        Insets margin = c.getMargin();

        if (margin == null || margin instanceof UIResource) {
            margin = (Insets) style.get(context, prefix + ".margin");
            if (margin == null) {
                // Some places assume margins are non-null.
                margin = OntimizeLookAndFeel.EMPTY_UIRESOURCE_INSETS;
            }

            c.setMargin(margin);
        }

        Caret caret = c.getCaret();

        if (caret instanceof UIResource) {
            Object o = style.get(context, prefix + ".caretBlinkRate");

            if (o != null && o instanceof Integer) {
                Integer rate = (Integer) o;

                caret.setBlinkRate(rate.intValue());
            }
        }
    }

    
    /**
     * @see javax.swing.plaf.basic.BasicTextUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    public void update(Graphics g, JComponent c) {
    	
    	SynthContext context = getContext(c);

    	OntimizeLookAndFeel.update(context, g);
        paintBackground(context, g, c);
        
        //TODO. Revise it!! It should exists one of register this border once
        if("Table.editor".equals(c.getName())){
        	if(UIManager.getDefaults().get("Table.cellEditorBorder") instanceof BorderUIResource){
        		c.setBorder((BorderUIResource) UIManager.getDefaults().get("Table.cellEditorBorder"));
        	}
        }
        
        paint(context, g);
        
    	
    }
    
    
    
    /**
     * Paints the interface. This is routed to the paintSafely method under the
     * guarantee that the model won't change from the view of this thread while
     * it's rendering (if the associated model is derived from
     * AbstractDocument). This enables the model to potentially be updated
     * asynchronously.
     *
     * @param context DOCUMENT ME!
     * @param g       DOCUMENT ME!
     */
    protected void paint(SynthContext context, Graphics g) {
        JTextComponent c = getComponent();
        
        if (c instanceof StatusBar){
        	 Paint previousPaint = ((Graphics2D)g).getPaint();
			 RenderingHints rh = ((Graphics2D)g).getRenderingHints();
			 ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			 
			 paintShadow(g,(StatusBar)c);
			 super.paint(g, c);
			 
			 ((Graphics2D)g).setPaint(previousPaint);
			 ((Graphics2D)g).setRenderingHints(rh);
        }else{
        	super.paint(g, c);
        }
    }
  
    protected void paintShadow(Graphics g, StatusBar c){
    	try{
    		c.setShadowMode(true);
    		Rectangle alloc = getVisibleEditorRect();
    		Rectangle despl = new Rectangle(alloc);
    		despl.x = despl.x - 1;
    		getRootView(getComponent()).paint(g, despl);
    	}finally{
    		c.setShadowMode(false);
    	}
    }
    
    public SynthContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    protected SynthContext getContext(JComponent c, int state) {
    	if(this.style == null){
    		this.style = OntimizeLookAndFeel.getOntimizeStyle(c, OntimizeLookAndFeel.getRegion(c));
    	}
    	return new SynthContext(c, OntimizeLookAndFeel.getRegion(c), this.style, state);
    }

    protected int getComponentState(JComponent c) {
        return OntimizeLookAndFeel.getComponentState(c);
    }

	@Override
	public Dimension getPreferredSize(JComponent c) {
		Dimension d =  super.getPreferredSize(c);
		if(d!=null){
			d.height = d.height + 4;
		}
		return d;
	}
	
	
	@Override
	public Dimension getMinimumSize(JComponent c) {
		Dimension size =  super.getMinimumSize(c);
		if(size!=null){
			size.height = size.height + 4;
		}
		return size;
	}
	
	 /**
     * @see javax.swing.plaf.basic.BasicTextUI#installDefaults()
     */
    protected void installDefaults() {
        // Installs the text cursor on the component
        super.installDefaults();

        JTextComponent c = getComponent();
        
        //Ontimize DataField stores foreground color into 'fontColor' variable that is just
        //initialized on init method. On new installations of Look&Feel it is necessary to
        //update this value.
        if(c.getParent() instanceof DataField){
        	Color fg = ((DataField)c.getParent()).getFontColor();
        	if (fg == null || fg instanceof UIResource) {
        		((DataField)c.getParent()).setFontColor(c.getForeground());
        	}
        }

        updateStyle(c);
        getComponent().addFocusListener(this);
    }
    
    @Override
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
    
    /**
     * @see javax.swing.plaf.basic.BasicTextUI#installUI(javax.swing.JComponent)
     */
    public void installUI(JComponent c) {
        super.installUI(c);
        updateStyle((JTextComponent) c);
    }
	
	
    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     * @param g       DOCUMENT ME!
     * @param c       DOCUMENT ME!
     */
    void paintBackground(SynthContext context, Graphics g, JComponent c) {
        ContextUtils.getPainter(context).paintTextFieldBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        // If necessary, paint the placeholder text.
//        if (placeholderText != null && ((JTextComponent) c).getText().length() == 0 && !c.hasFocus()) {
//            paintPlaceholderText(context, g, c);
//        }
    }

    /**
     * @see sun.swing.plaf.synth.SynthUI#paintBorder(javax.swing.plaf.synth.SynthContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ContextUtils.getPainter(context).paintTextFieldBorder(context, g, x, y, w, h);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTextUI#paintBackground(java.awt.Graphics)
     */
    protected void paintBackground(Graphics g) {
        // Overridden to do nothing, all our painting is done from update/paint.
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     * @param g       DOCUMENT ME!
     * @param c       DOCUMENT ME!
     */
    protected void paintPlaceholderText(SynthContext context, Graphics g, JComponent c) {
        g.setColor(placeholderColor);
        g.setFont(c.getFont());
        Rectangle innerArea    = SwingUtilities.calculateInnerArea(c, null);
        // FIXME Do better baseline calculation than just subtracting 1.
        context.getStyle().getGraphicsUtils(context).paintText(context, g, getPlaceholderText(g, innerArea.width ),
                                                               innerArea.x,
                                                               innerArea.y - 1, -1);
    }

    /**
     * Get the placeholder text, clipped if necessary.
     *
     * @param  g              fm the font metrics to compute size with.
     * @param  availTextWidth the available space to display the title in.
     *
     * @return the text, clipped to fit the available space.
     */
    protected String getPlaceholderText(Graphics g, int availTextWidth) {
        JTextComponent c  = getComponent();
        FontMetrics    fm = SwingUtilities2.getFontMetrics(c, g);

        return SwingUtilities2.clipStringIfNecessary(c, fm, placeholderText, availTextWidth);
    }

    
    /**
     * This method gets called when a bound property is changed on the
     * associated JTextComponent. This is a hook which UI implementations may
     * change to reflect how the UI displays bound properties of JTextComponent
     * subclasses. This is implemented to do nothing (i.e. the response to
     * properties in JTextComponent itself are handled prior to calling this
     * method).
     *
     * @param evt the property change event
     */
    protected void propertyChange(PropertyChangeEvent evt) {
        if (OntimizeLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle((JTextComponent) evt.getSource());
        }

        super.propertyChange(evt);
    }

	@Override
	public void focusGained(FocusEvent e) {
		getComponent().repaint();
	}

	@Override
	public void focusLost(FocusEvent e) {
		getComponent().repaint();
	}
	
}
