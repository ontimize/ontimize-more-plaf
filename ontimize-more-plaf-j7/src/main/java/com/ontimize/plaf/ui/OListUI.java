package com.ontimize.plaf.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicListUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthUI;

import com.ontimize.plaf.OntimizeContext;
import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.OntimizeStyle;
import com.ontimize.plaf.border.OntimizeBorder;

public class OListUI  extends BasicListUI implements PropertyChangeListener, SynthUI{
    protected OntimizeStyle style;
    protected boolean useListColors;
    protected boolean useUIBorder;

    public static ComponentUI createUI(JComponent list) {
        return new OListUI();
    }

    public void update(Graphics g, JComponent c) {
    	
        OntimizeContext context = getContext(c);

        OntimizeLookAndFeel.update(context, g);
        context.getPainter().paintListBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        context.dispose();
        paint(g, c);
    }
    
    @Override
    public void paint(Graphics g, JComponent c) {
    	super.paint(g, c);
    }

    public void paintBorder(SynthContext context, Graphics g, int x,
                            int y, int w, int h) {
        ((OntimizeContext)context).getPainter().paintListBorder(context, g, x, y, w, h);
    }
    

    protected void installListeners() {
        super.installListeners();
        list.addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (OntimizeLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle((JList)e.getSource());
        }
    }

    protected void uninstallListeners() {
        super.uninstallListeners();
        list.removePropertyChangeListener(this);
    }

    protected void installDefaults() {
        if (list.getCellRenderer() == null ||
                 (list.getCellRenderer() instanceof UIResource)) {
            list.setCellRenderer(new OListCellRenderer());
        }
        updateStyle(list);
    }

    protected void updateStyle(JComponent c) {
    	OntimizeContext context = getContext(list, ENABLED);
        OntimizeStyle oldStyle = style;

        style = (OntimizeStyle) OntimizeLookAndFeel.updateStyle(context, this);

        if (style != oldStyle) {
            context.setComponentState(SELECTED);
            Color sbg = list.getSelectionBackground();
            if (sbg == null || sbg instanceof UIResource) {
                list.setSelectionBackground(style.getColor(
                                 context, ColorType.TEXT_BACKGROUND));
            }

            Color sfg = list.getSelectionForeground();
            if (sfg == null || sfg instanceof UIResource) {
                list.setSelectionForeground(style.getColor(
                                 context, ColorType.TEXT_FOREGROUND));
            }

            useListColors = style.getBoolean(context,
                                  "List.rendererUseListColors", true);
            useUIBorder = style.getBoolean(context,
                                  "List.rendererUseUIBorder", true);

            int height = style.getInt(context, "List.cellHeight", -1);
            if (height != -1) {
                list.setFixedCellHeight(height);
            }
            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    protected void uninstallDefaults() {
        super.uninstallDefaults();

        OntimizeContext context = getContext(list, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
    }

    public OntimizeContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    protected OntimizeContext getContext(JComponent c, int state) {
        return OntimizeContext.getContext(OntimizeContext.class, c,
                    OntimizeLookAndFeel.getRegion(c), style, state);
    }

    protected Region getRegion(JComponent c) {
        return OntimizeLookAndFeel.getRegion(c);
    }

    protected int getComponentState(JComponent c) {
        return OntimizeLookAndFeel.getComponentState(c);
    }


    protected class OListCellRenderer extends DefaultListCellRenderer.UIResource {
        public String getName() {
            return "List.cellRenderer";
        }

        public void setBorder(Border b) {
            if (useUIBorder || b instanceof OntimizeBorder) {
                super.setBorder(b);
            }
        }

        public Component getListCellRendererComponent(JList list, Object value,
                  int index, boolean isSelected, boolean cellHasFocus) {
            if (!useListColors && (isSelected || cellHasFocus)) {
                OntimizeLookAndFeel.setSelectedUI((OLabelUI)OntimizeLookAndFeel.
                             getUIOfType(getUI(), OLabelUI.class),
                                   isSelected, cellHasFocus, list.isEnabled(), false);
            }
            else {
                OntimizeLookAndFeel.resetSelectedUI();
            }
            
            super.getListCellRendererComponent(list, value, index,
                                               isSelected, cellHasFocus);
            return this;
        }

        public void paint(Graphics g) {
            super.paint(g);
            OntimizeLookAndFeel.resetSelectedUI();
        }
    }

}
