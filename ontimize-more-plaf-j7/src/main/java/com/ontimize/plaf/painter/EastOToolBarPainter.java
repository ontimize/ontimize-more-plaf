package com.ontimize.plaf.painter;

public class EastOToolBarPainter extends AbstractOToolBarPainter {

    public EastOToolBarPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected String getComponentState() {
        return "East";
    }

    @Override
    protected String getComponentKeyName() {
        return null;
    }

}
