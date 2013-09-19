package com.ontimize.plaf.state;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

public class OSplitPaneVerticalState extends State {
	public OSplitPaneVerticalState() {
        super("Vertical");
    }

    @Override public boolean isInState(JComponent c) {

                        return c instanceof JSplitPane && (((JSplitPane)c).getOrientation() == 1);
    }
}
