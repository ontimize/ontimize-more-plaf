package com.ontimize.plaf.state;

import javax.swing.JComponent;
import com.sun.java.swing.plaf.nimbus.State;

public class OTableHeaderTableHeaderRendererSortedState extends State {
	public OTableHeaderTableHeaderRendererSortedState() {
        super("Sorted");
    }

    @Override
	public boolean isInState(JComponent c) {

                    String sortOrder = (String)c.getClientProperty("Table.sortOrder");
                    return  sortOrder != null && ("ASCENDING".equals(sortOrder) || "DESCENDING".equals(sortOrder)); 
    }
}