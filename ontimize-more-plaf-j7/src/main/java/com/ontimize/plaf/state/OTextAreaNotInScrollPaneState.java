package com.ontimize.plaf.state;

import javax.swing.JComponent;

public class OTextAreaNotInScrollPaneState extends State {
    
	public OTextAreaNotInScrollPaneState() {
        super("NotInScrollPane");
    }

    @Override
	public boolean isInState(JComponent c) {

                          return !(c.getParent() instanceof javax.swing.JViewport);
    }
}
