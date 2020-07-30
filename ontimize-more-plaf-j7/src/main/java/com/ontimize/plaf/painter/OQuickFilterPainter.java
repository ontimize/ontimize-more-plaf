package com.ontimize.plaf.painter;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class OQuickFilterPainter extends AbstractOTextFieldPainter {


    protected java.awt.Image queryFilterIcon;

    public static String defaultIconURL = "com/ontimize/plaf/images/queryfilter.png";


    public OQuickFilterPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    @Override
    protected void init() {
        super.init();

        Object obj = UIManager.getDefaults().get(getComponentKeyName() + ".icon");
        if (obj instanceof String) {
            try {
                URL url = getClass().getClassLoader().getResource((String) obj);
                queryFilterIcon = Toolkit.getDefaultToolkit().getImage(url);
            } catch (Exception e) {
                URL url = getClass().getClassLoader().getResource(defaultIconURL);
                queryFilterIcon = Toolkit.getDefaultToolkit().getImage(url);
            }
        } else {
            URL url = getClass().getClassLoader().getResource(defaultIconURL);
            queryFilterIcon = Toolkit.getDefaultToolkit().getImage(url);
        }

    }

    @Override
    protected String getComponentKeyName() {
        return "Table:\"Table.QuickFilter\"";
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height,
            Object[] extendedCacheKeys) {
        super.doPaint(g, c, width, height, extendedCacheKeys);
    }

    @Override
    protected void drawDegradatedBorders(Graphics2D g, JComponent c, int x,
            int y, int width, int height, Paint[] colors) {
        super.drawDegradatedBorders(g, c, x, y, width, height, colors);

        // Painting magnify glass..
        if (queryFilterIcon != null) {
            g.drawImage(queryFilterIcon, (int) decodeX(2.0f), (int) decodeY(1.0f) + 1, 27, 16, null);
        }
    }

    @Override
    protected Shape decodeRightBorderFillerPath(int height) {
        return super.decodeRightBorderFillerPath(height);
    }

}
