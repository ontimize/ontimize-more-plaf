package com.ontimize.plaf.state;

import javax.swing.JComponent;

/**
 * Do we use the arrow shape for a slider? We do if snap-to-ticks is set for the
 * slider. Otherwise we use the round shape.
 */
public class OSliderArrowShapeState extends State {
	public OSliderArrowShapeState() {
        super("ArrowShape");
    }

    @Override
	public boolean isInState(JComponent c) {
 return c.getClientProperty("Slider.paintThumbArrowShape") == Boolean.TRUE; 
    }
}
