package com.ontimize.plaf.state;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.sun.java.swing.plaf.nimbus.State;

public class OComboBoxEditableState extends State {
	public OComboBoxEditableState() {
		super("Editable");
	}

	@Override
	public boolean isInState(JComponent c) {

		return ((JComboBox) c).isEditable();
	}
}
