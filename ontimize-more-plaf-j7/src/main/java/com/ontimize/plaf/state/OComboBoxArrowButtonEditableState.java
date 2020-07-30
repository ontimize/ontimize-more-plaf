package com.ontimize.plaf.state;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.plaf.nimbus.State;

public class OComboBoxArrowButtonEditableState extends State {

    public OComboBoxArrowButtonEditableState() {
        super("Editable");
    }

    @Override
    public boolean isInState(JComponent c) {

        Component parent = c.getParent();
        return parent instanceof JComboBox && ((JComboBox) parent).isEditable();
    }

}
