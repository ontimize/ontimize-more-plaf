package com.ontimize.plaf.state;

import javax.swing.JComponent;
import javax.swing.plaf.nimbus.State;

public class OSliderThumbArrowShapeState extends State {

    public OSliderThumbArrowShapeState() {
        super("ArrowShape");
    }

    @Override
    public boolean isInState(JComponent c) {
        return c.getClientProperty("Slider.paintThumbArrowShape") == Boolean.TRUE;
    }

}
