package com.ontimize.plaf.painter;


/**
 * This class indicates the component name for the component that must be painted. Paint
 * instructions, indications, ... are encoded in the class that this class extends (named
 * {@link AbstractOTextFieldPainter} )
 *
 * This kind of extensions abstract painters were developed to allow users to have several
 * componentes which are painted in the same way, and configured by the same set of properties, but
 * having only a common code to paint (the class that this one extends).
 *
 * @author Imatia S.L.
 *
 */
public class OTextFieldPainter extends AbstractOTextFieldPainter {

    public OTextFieldPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected String getComponentKeyName() {
        return "TextField";
    }

}
