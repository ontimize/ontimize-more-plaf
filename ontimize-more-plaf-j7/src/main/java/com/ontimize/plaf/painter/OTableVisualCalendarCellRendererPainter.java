package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ontimize.gui.calendar.VisualCalendarComponent.DayRenderer;

/**
 * Painter for table header renderer (to configure border and foreground colors,
 * painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OTableVisualCalendarCellRendererPainter extends AbstractRegionPainter {
    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of OTableHeaderRendererPainter to determine which region/state is being
    // painted
    // by that instance.
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_ENABLED = 2;
    public static final int BACKGROUND_ENABLED_FOCUSED = 3;
    public static final int BACKGROUND_MOUSEOVER = 4;
    public static final int BACKGROUND_PRESSED = 5;
    public static final int BACKGROUND_ENABLED_SORTED = 6;
    public static final int BACKGROUND_ENABLED_FOCUSED_SORTED = 7;
    public static final int BACKGROUND_DISABLED_SORTED = 8;

    protected int state; // refers to one of the static ints above
    protected PaintContext ctx;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();
    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of OTableHeaderRendererPainter would be created.
    // For the moment at least,
    // however, all are created for each instance.
    protected Color selectedBgColor;
    protected Color evenColumnBgColor;
    protected Color oddColumnBgColor;
    protected Color selectedDayColor;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OTableVisualCalendarCellRendererPainter(int state, PaintContext ctx) {
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
        case BACKGROUND_ENABLED:
            this.paintBackgroundEnabled(g, c);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    @Override
    protected String getComponentKeyName() {
        return "Table:\"VisualCalendar:Table.cellRenderer\"";
    }

    @Override
    protected void init() {

        Object obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Selected].background");
        if (obj instanceof Color) {
            this.selectedBgColor = (Color) obj;
        } else {
            this.selectedBgColor = new Color(0x5cb1ea);
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Selected].dayColor");
        if (obj instanceof Color) {
            this.selectedDayColor = (Color) obj;
        } else {
            this.selectedDayColor = new Color(0xbcd3e3);
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + ".evenColumnBackground");
        if (obj instanceof Color) {
            this.evenColumnBgColor = (Color) obj;
        } else {
            this.evenColumnBgColor = new Color(0xffffff);
        }

        obj = UIManager.getDefaults().get(this.getComponentKeyName() + ".oddColumnBackground");
        if (obj instanceof Color) {
            this.oddColumnBgColor = (Color) obj;
        } else {
            this.oddColumnBgColor = new Color(0xF3F3F0);
        }
    }

    protected void paintBackgroundEnabled(Graphics2D g, JComponent c) {

        this.rect = this.decodeRect();

        if (c instanceof DayRenderer) {
            if (((DayRenderer) c).isDaySelected()) {
                g.setPaint(this.selectedBgColor);
                g.fill(this.rect);
            } else {
                g.setPaint(this.getColumnBackgroundColor(c));
                g.fill(this.rect);
            }
            if (((DayRenderer) c).isPaintingToday()) {
                Rectangle r = this.decodeTodayCircle(c);
                g.setPaint(this.selectedDayColor);
                g.fillOval(r.x, r.y, r.width, r.height);
            }
        } else {
            g.setPaint(this.getColumnBackgroundColor(c));
            g.fill(this.rect);
        }
    }

    protected Color getColumnBackgroundColor(JComponent c) {
        Color color = null;
        boolean even = this.isEvenColumn(c);
        if (even) {
            color = this.evenColumnBgColor;
        } else {
            color = this.oddColumnBgColor;
        }
        return color;
    }

    protected boolean isEvenColumn(JComponent c) {
        JTable table = this.getTable(c);
        if (table != null) {
            int currentColumn = table.getColumnModel().getColumnIndexAtX(c.getX());
            if (currentColumn % 2 == 0) {
                return true;
            }
        }
        return false;
    }

    protected JTable getTable(JComponent c) {
        JTable table = null;
        try {
            Object obj = SwingUtilities.getAncestorOfClass(JTable.class, c);
            if (obj instanceof JTable) {
                table = (JTable) obj;
            }
        } catch (Exception e) {
        }
        return table;
    }

    protected Rectangle decodeTodayCircle(JComponent c) {
        Rectangle bounds = c.getBounds();
        Insets in = c.getInsets();

        int width = bounds.width - in.left - in.right;
        int height = bounds.height - in.top - in.bottom;

        int radius = height / 2;
        if (width / 2 < radius) {
            radius = width / 2;
        }
        int centerX = width / 2;
        int centerY = height / 2;

        Rectangle r = new Rectangle(centerX + 1 - radius, centerY + 1 - radius, 2 * (radius - 1), 2 * (radius - 1));
        return r;

    }

    protected Rectangle2D decodeRect() {
        this.rect.setRect(this.decodeX(0.0f), // x
                this.decodeY(0.0f), // y
                this.decodeX(3.0f) - this.decodeX(0.0f), // width
                this.decodeY(3.0f) - this.decodeY(0.0f)); // height
        return this.rect;
    }

}
