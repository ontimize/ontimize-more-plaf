package com.ontimize.plaf.component;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.Popup;
import javax.swing.PopupFactory;

public class OntimizePopupFactory extends PopupFactory {

	public Popup getPopup(Component owner, Component contents, int x, int y) throws IllegalArgumentException {
		if(owner instanceof JComboBox){
			return super.getPopup(owner, contents, x, y);
		}
		// A more complete implementation would cache and reuse popups
//		return new OntimizePopup(owner, contents, x, y);
		return OntimizePopup.getHeavyWeightPopup(owner, contents, x, y);
	}
}
