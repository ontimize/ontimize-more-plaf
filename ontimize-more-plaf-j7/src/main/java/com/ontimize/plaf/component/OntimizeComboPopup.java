package com.ontimize.plaf.component;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;

import com.ontimize.plaf.ui.OComboBoxUI;

public class OntimizeComboPopup  extends BasicComboPopup{
	
	public static Border O_LIST_BORDER = new LineBorder(new Color(0x8ca0ad), 2);
    
    public OntimizeComboPopup( JComboBox combo ) {
        super(combo);
    }

    /**
     * Configures the list which is used to hold the combo box items in the
     * popup. This method is called when the UI class
     * is created.
     *
     * @see #createList
     */
    @Override
    protected void configureList() {
        list.setFont( comboBox.getFont() );
        list.setCellRenderer( comboBox.getRenderer() );
        list.setFocusable( false );
        list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        int selectedIndex = comboBox.getSelectedIndex();
        if ( selectedIndex == -1 ) {
            list.clearSelection();
        }
        else {
            list.setSelectedIndex( selectedIndex );
	    list.ensureIndexIsVisible( selectedIndex );
        }
        installListListeners();
    }
    
    @Override
    protected void configurePopup() {
    	super.configurePopup();
    	setBorder(O_LIST_BORDER);
    }
    
    /**
     * @inheritDoc
     * 
     * Overridden to take into account any popup insets specified in
     * SynthComboBoxUI
     */
    @Override
    protected Rectangle computePopupBounds(int px,int py,int pw,int ph) {
        ComboBoxUI ui = comboBox.getUI();
        if (ui instanceof OComboBoxUI) {
        	OComboBoxUI sui = (OComboBoxUI)ui;
            if (sui.popupInsets != null) {
                Insets i = sui.popupInsets;
                return super.computePopupBounds(
                        px + i.left,
                        py + i.top,
                        pw - i.left - i.right,
                        ph - i.top - i.bottom);
            }
        }
        return super.computePopupBounds(px, py, pw, ph);
    }
}
