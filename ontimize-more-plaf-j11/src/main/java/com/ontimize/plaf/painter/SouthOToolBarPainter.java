package com.ontimize.plaf.painter;

public class SouthOToolBarPainter extends AbstractOToolBarPainter {

    public SouthOToolBarPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected String getComponentState() {
        return "South";
    }

    @Override
    protected String getComponentKeyName() {
        return null;
    }

}
