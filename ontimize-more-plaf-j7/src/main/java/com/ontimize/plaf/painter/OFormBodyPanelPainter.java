package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.geom.Path2D;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.gui.images.ImageManager;
import com.ontimize.plaf.utils.LinearGradient;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

public class OFormBodyPanelPainter extends AbstractRegionPainter {

    public static String BG_IMAGE = "com/ontimize/plaf/images/backgroundDarkBlue.jpg";

    public static final int BACKGROUND_ENABLED = 1;

    protected int state; // refers to one of the static ints above
    protected PaintContext ctx;

    protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);

    protected Color downBackgroundColor, topBackgroundColor;

    protected Paint bgPaint;

    protected Image backgroundImage;

    public OFormBodyPanelPainter(int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx = ctx;
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        this.paintBackground(g, c, width, height);
    }

    @Override
    protected String getComponentKeyName() {
        return "\"FormBodyPanel\"";
    }

    @Override
    protected void init() {
        ImageIcon iIcon = ImageManager.getIcon(OFormBodyPanelPainter.BG_IMAGE);
        if (iIcon != null) {
            this.backgroundImage = iIcon.getImage();
        }

        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".bgpaint");
        if (obj instanceof Paint) {
            this.bgPaint = (Paint) obj;
        }
    }

    protected void paintBackground(Graphics2D g, JComponent c, int w, int h) {
        if (this.bgPaint instanceof LinearGradient) {
            Paint background = OntimizeLAFColorUtils.decodeGradient(c.getBounds(), (LinearGradient) this.bgPaint);
            g.setPaint(background);
            g.fillRect(0, 0, c.getBounds().width, c.getBounds().height);
        } else if (this.backgroundImage != null) {
            int iHeight = this.backgroundImage.getHeight(null);
            int restH = h / iHeight;
            int iWidth = this.backgroundImage.getWidth(null);
            int restW = w / iWidth;

            for (int i = 0; i <= restH; i++) {
                g.drawImage(this.backgroundImage, 0, i * iHeight, w, iHeight + (i * iHeight), 0, 0, w, iHeight, null);
                if (restW > 0) {
                    for (int j = 0; j <= restW; j++) {
                        g.drawImage(this.backgroundImage, j * iWidth, i * iHeight, iWidth + (j * iWidth), iHeight
                                + (i * iHeight), 0, 0, iWidth, iHeight, null);
                    }
                }
            }

        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    protected int decodeHeight(int height) {
        return (int) (height - (height * 0.2));
    }
}
