package com.ontimize.plaf.state;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

public class OSplitPaneDividerVerticalState extends State {
	public OSplitPaneDividerVerticalState() {
        super("Vertical");
    }

    @Override
	public boolean isInState(JComponent c) {

                        return c instanceof JSplitPane && (((JSplitPane)c).getOrientation() == 1);
    }
}