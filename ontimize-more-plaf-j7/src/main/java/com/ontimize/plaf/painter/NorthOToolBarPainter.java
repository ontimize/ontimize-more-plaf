package com.ontimize.plaf.painter;

public class NorthOToolBarPainter extends AbstractOToolBarPainter {

    public NorthOToolBarPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected String getComponentState() {
        return "North";
    }

    @Override
    protected String getComponentKeyName() {
        return null;
    }

}
