package com.ontimize.plaf.painter;

public class WestOToolBarPainter extends AbstractOToolBarPainter {

    public WestOToolBarPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected String getComponentState() {
        return "West";
    }

    @Override
    protected String getComponentKeyName() {
        return null;
    }

}
