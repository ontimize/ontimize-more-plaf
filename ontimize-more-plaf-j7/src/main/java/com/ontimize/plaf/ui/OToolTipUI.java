package com.ontimize.plaf.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicToolTipUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;
import javax.swing.text.View;

import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.OntimizeStyle;
import com.ontimize.plaf.utils.ContextUtils;

public class OToolTipUI extends BasicToolTipUI implements PropertyChangeListener, SynthUI {
	protected SynthStyle style;

	public static ComponentUI createUI(JComponent c) {
		return new OToolTipUI();
	}

	protected void installDefaults(JComponent c) {
		if (this.style == null) {
			this.style = OntimizeStyle.NULL_STYLE;
		}
		updateStyle(c);
	}

	protected void updateStyle(JComponent c) {
		SynthContext context = getContext(c, ENABLED);
		style = OntimizeLookAndFeel.updateStyle(context, this);
		
	}

	protected void uninstallDefaults(JComponent c) {
		SynthContext context = getContext(c, ENABLED);
		style.uninstallDefaults(context);
		
		style = null;
	}

	protected void installListeners(JComponent c) {
		c.addPropertyChangeListener(this);
	}

	protected void uninstallListeners(JComponent c) {
		c.removePropertyChangeListener(this);
	}

	public SynthContext getContext(JComponent c) {
		return getContext(c, getComponentState(c));
	}

	protected SynthContext getContext(JComponent c, int state) {
		return new SynthContext( c, OntimizeLookAndFeel.getRegion(c), this.style,
				state);
	}

	protected Region getRegion(JComponent c) {
		return OntimizeLookAndFeel.getRegion(c);
	}

	protected int getComponentState(JComponent c) {
		JComponent comp = ((JToolTip) c).getComponent();

//		 if (comp != null && !comp.isEnabled()) {
//		 return DISABLED;
//		 }
		return OntimizeLookAndFeel.getComponentState(c);
	}

	public void update(Graphics g, JComponent c) {
		SynthContext context = getContext(c);

		
		OntimizeLookAndFeel.update(context, g);
		if(isEmpty(c)==false)
			ContextUtils.getPainter(context).paintToolTipBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
		paint(context, g);
		
	}
	
	protected boolean isEmpty(JComponent c){
		if(c instanceof JToolTip){
			String text = ((JToolTip)c).getTipText();
			if(text==null || text.length()==0){
				return true;
			}
		}
		return false;
	}

	public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
		ContextUtils.getPainter(context).paintToolTipBorder(context, g, x, y, w, h);
	}

	public void paint(Graphics g, JComponent c) {
		SynthContext context = getContext(c);

		paint(context, g);
		
	}

	protected void paint(SynthContext context, Graphics g) {
		JToolTip tip = (JToolTip) context.getComponent();
		String tipText = tip.getToolTipText();

		Insets insets = tip.getInsets();
		View v = (View) tip.getClientProperty(BasicHTML.propertyKey);
		if (v != null) {
			Rectangle paintTextR = new Rectangle(insets.left, insets.top, tip.getWidth()
					- (insets.left + insets.right), tip.getHeight() - (insets.top + insets.bottom));
			v.paint(g, paintTextR);
		} else {
			g.setColor(context.getStyle().getColor(context, ColorType.TEXT_FOREGROUND));
			g.setFont(style.getFont(context));
			context.getStyle().getGraphicsUtils(context)
					.paintText(context, g, tip.getTipText(), insets.left, insets.top, -1);
		}
	}

	public Dimension getPreferredSize(JComponent c) {
		SynthContext context = getContext(c);
		Insets insets = c.getInsets();
		Dimension prefSize = new Dimension(insets.left + insets.right, insets.top + insets.bottom);
		String text = ((JToolTip) c).getTipText();

		if (text != null) {
			View v = (c != null) ? (View) c.getClientProperty("html") : null;
			if (v != null) {
				prefSize.width += (int) v.getPreferredSpan(View.X_AXIS);
				prefSize.height += (int) v.getPreferredSpan(View.Y_AXIS);
			} else {
				Font font = context.getStyle().getFont(context);
				FontMetrics fm = c.getFontMetrics(font);
				prefSize.width += context.getStyle().getGraphicsUtils(context)
						.computeStringWidth(context, font, fm, text);
				prefSize.height += fm.getHeight();
			}
		}
		
		return prefSize;
	}

	public void propertyChange(PropertyChangeEvent e) {
		if (OntimizeLookAndFeel.shouldUpdateStyle(e)) {
			updateStyle((JToolTip) e.getSource());
		}
		String name = e.getPropertyName();
		if (name.equals("tiptext") || "font".equals(name) || "foreground".equals(name)) {
			// remove the old html view client property if one
			// existed, and install a new one if the text installed
			// into the JLabel is html source.
			JToolTip tip = ((JToolTip) e.getSource());
			String text = tip.getTipText();
			BasicHTML.updateRenderer(tip, text);
		}
	}
}
