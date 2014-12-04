package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;

public class OComboBoxTextFieldPainter extends AbstractRegionPainter {
    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of ComboBoxComboBoxTextFieldPainter to determine which region/state is
    // being painted
    // by that instance.
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_ENABLED = 2;
    public static final int BACKGROUND_SELECTED = 3;

    protected int state; // refers to one of the static ints above
    protected PaintContext ctx;

    // painters to fill the component
    protected Paint backgroundColorDisabled;
    protected Paint backgroundColorEnabled;
    protected Paint backgroundColorSelected;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();
    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
    protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);
    protected Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of ComboBoxComboBoxTextFieldPainter would be
    // created. For the moment at least,
    // however, all are created for each instance.
    protected Color color1 = this.decodeColor("softWhite", 0.0f, 0.0f, 1.0f, 125);
    protected Color color2 = this.decodeColor("white", 0.0f, 0.0f, 1.0f, 255);

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OComboBoxTextFieldPainter(int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx = ctx;
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        // populate componentColors array with colors calculated in
        // getExtendedCacheKeys call
        this.componentColors = extendedCacheKeys;
        // generate this entire method. Each state/bg/fg/border combo that has
        // been painted gets its own KEY and paint method.
        switch (this.state) {
        case BACKGROUND_DISABLED:
            this.paintBackgroundDisabled(g);
            break;
        case BACKGROUND_ENABLED:
            this.paintBackgroundEnabled(g);
            break;
        case BACKGROUND_SELECTED:
            this.paintBackgroundSelected(g);
            break;

        }

    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    @Override
    protected String getComponentKeyName() {
        return "ComboBox:\"ComboBox.textField\"";
    }

    @Override
    protected void init() {

        // disable:
        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].background");
        if (obj instanceof Paint) {
            this.backgroundColorDisabled = (Paint) obj;
        } else {
            this.backgroundColorDisabled = this.color1;
        }

        // enable:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].background");
        if (obj instanceof Paint) {
            this.backgroundColorEnabled = (Paint) obj;
        } else {
            this.backgroundColorEnabled = this.color2;
        }

        // selected:
        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Selected].background");
        if (obj instanceof Paint) {
            this.backgroundColorSelected = (Paint) obj;
        } else {
            this.backgroundColorSelected = this.color2;
        }

    }

    protected void paintBackgroundDisabled(Graphics2D g) {

        this.paintBackground(g, this.backgroundColorDisabled);
    }

    protected void paintBackgroundEnabled(Graphics2D g) {

        this.paintBackground(g, this.backgroundColorEnabled);
    }

    protected void paintBackgroundSelected(Graphics2D g) {

        this.paintBackground(g, this.backgroundColorSelected);
    }

    protected void paintBackground(Graphics2D g, Paint bg) {

        double x = this.decodeX(0.0f);
        double y = this.decodeY(0.0f);
        double w = this.decodeX(3.0f);
        double h = this.decodeY(3.0f);
        Shape s = ShapeFactory.getInstace().createRectangle(x, y, w, h);
        g.setPaint(bg);
        g.fill(s);
    }

}
