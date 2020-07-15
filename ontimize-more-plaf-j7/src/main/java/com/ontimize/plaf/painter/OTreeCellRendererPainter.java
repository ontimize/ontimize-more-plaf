package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;

import com.ontimize.gui.tree.BasicTreeCellRenderer;
import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.utils.OntimizeLAFParseUtils;

public class OTreeCellRendererPainter extends AbstractRegionPainter {

    public static final int BACKGROUND_ENABLED = 1;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();

    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    protected Paint background;

    protected Paint backgroundSelectionParent;

    protected Paint backgroundSelection;

    protected Paint topBackgroundSelection;

    protected Paint bottomBackgroundSelection;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OTreeCellRendererPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        // populate componentColors array with colors calculated in
        // getExtendedCacheKeys call
        this.componentColors = extendedCacheKeys;
        // generate this entire method. Each state/bg/fg/border combo that has
        // been painted gets its own KEY and paint method.
        switch (this.state) {
            case BACKGROUND_ENABLED:
                this.paintBackgroundEnabled(g, c, width, height);
                break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    @Override
    protected String getComponentKeyName() {
        return "Tree:\"Tree.cellRenderer\"";
    }

    @Override
    protected void init() {

        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".background");
        if (obj instanceof Paint) {
            this.background = (Paint) obj;
        } else {
            this.background = OntimizeLAFParseUtils.parseColor("#E4EDF0", Color.white);
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".backgroundSelectionParent");
        if (obj instanceof Paint) {
            this.backgroundSelectionParent = (Paint) obj;
        } else {
            this.backgroundSelectionParent = OntimizeLAFParseUtils.parseColor("#ABC7D8", Color.white);
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".backgroundSelection");
        if (obj instanceof Paint) {
            this.backgroundSelection = (Paint) obj;
        } else {
            this.backgroundSelection = OntimizeLAFParseUtils.parseColor("#517286", Color.blue);
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".topBackgroundSelection");
        if (obj instanceof Paint) {
            this.topBackgroundSelection = (Paint) obj;
        } else {
            this.topBackgroundSelection = OntimizeLAFParseUtils.parseColor("#638092", Color.blue);
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".bottomBackgroundSelection");
        if (obj instanceof Paint) {
            this.bottomBackgroundSelection = (Paint) obj;
        } else {
            this.bottomBackgroundSelection = OntimizeLAFParseUtils.parseColor("#496678", Color.blue);
        }

    }

    protected void paintBackgroundEnabled(Graphics2D g, JComponent c, int width, int height) {
        Paint previousPaint = g.getPaint();
        RenderingHints rh = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color ant = g.getColor();
        g.setPaint(this.background);

        boolean selected = false;
        boolean leaf = true;
        int childCount = -1;

        boolean isRoot = false;

        if (c instanceof BasicTreeCellRenderer) {
            BasicTreeCellRenderer basic = (BasicTreeCellRenderer) c;

            if (basic.isParentSelected()) {
                g.setPaint(this.backgroundSelectionParent);
            }

            if (basic.isSelected()) {
                g.setColor(basic.getBackgroundSelectionColor());
                selected = true;
            }

            if (!basic.isLeaf()) {
                leaf = false;
            }

            childCount = basic.getChildCount();

            isRoot = basic.isRoot();
        }

        if (!isRoot) {
            this.paintBackground(g, c, width, height, selected, leaf, childCount);
        }

        g.setColor(ant);
        g.setPaint(previousPaint);
        g.setRenderingHints(rh);
    }

    protected int getLabelStart(JLabel label) {
        Icon currentI = label.getIcon();
        if (currentI != null) {
            return currentI.getIconWidth() + 2;
        }
        return 0;
    }

    protected void paintBackground(Graphics2D g, JComponent c, int width, int height, boolean selected, boolean leaf,
            int childCount) {
        int offset = (height / 2) - 1;
        int labelstart = this.getLabelStart((JLabel) c);
        if (width < (labelstart + offset + offset)) {
            return;
        }
        // left
        Shape s = ShapeFactory.getInstace()
            .createSemiCircle(this.getLabelStart((JLabel) c) + offset, 1, height - 2, ShapeFactory.ANTICLOCKWISE,
                    false);
        g.fill(s);

        // center
        g.fillRect(this.getLabelStart((JLabel) c) + offset, 1,
                width - ((offset * 2) + this.getLabelStart((JLabel) c)) - 1, height - 2);

        // right
        s = ShapeFactory.getInstace()
            .createSemiCircle(
                    (this.getLabelStart((JLabel) c) + offset
                            + (width - ((offset * 2) + this.getLabelStart((JLabel) c)))) - 1,
                    1, height - 2,
                    ShapeFactory.CLOCKWISE, false);
        g.fill(s);

        if (selected) {
            // TOP
            int x1 = this.getLabelStart((JLabel) c) + 3;
            int y1 = (height / 2) - 2;
            int h = (height / 2) - 4;
            int w = width - 7 - this.getLabelStart((JLabel) c);

            this.path.reset();
            this.path.moveTo(x1, y1);
            this.path.curveTo(x1, y1 - (h / 2.0), x1 + (h / 2.0), y1 - h, x1 + h, y1 - h);

            this.path.lineTo((x1 + w) - h, y1 - h);

            this.path.curveTo((x1 + w) - (h / 2.0), y1 - h, x1 + w, y1 - (h / 2.0), x1 + w, y1);
            this.path.closePath();

            g.setPaint(this.topBackgroundSelection);
            g.fill(this.path);

            this.path.reset();

            g.setPaint(this.bottomBackgroundSelection);

            // BOTTOM
            x1 = this.getLabelStart((JLabel) c) + 3;
            y1 = (height / 2) + 3;
            h = (height / 2) - 4;

            this.path.moveTo(x1, y1);
            this.path.curveTo(x1, y1 + (h / 2.0), x1 + (h / 2.0), y1 + h, x1 + h, y1 + h);

            this.path.lineTo((x1 + w) - h, y1 + h);
            this.path.curveTo((x1 + w) - (h / 2.0), y1 + h, x1 + w, y1 + (h / 2.0), x1 + w, y1);
            this.path.closePath();
            g.fill(this.path);
        }

    }

}
