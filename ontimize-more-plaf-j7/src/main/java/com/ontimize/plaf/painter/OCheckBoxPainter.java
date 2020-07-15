package com.ontimize.plaf.painter;

import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class OCheckBoxPainter extends AbstractRegionPainter {

    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of CheckBoxPainter to determine which region/state is being painted
    // by that instance.
    public static final int BACKGROUND_DISABLED = 1;

    public static final int BACKGROUND_ENABLED = 2;

    public static final int ICON_DISABLED = 3;

    public static final int ICON_ENABLED = 4;

    public static final int ICON_FOCUSED = 5;

    public static final int ICON_MOUSEOVER = 6;

    public static final int ICON_MOUSEOVER_FOCUSED = 7;

    public static final int ICON_PRESSED = 8;

    public static final int ICON_PRESSED_FOCUSED = 9;

    public static final int ICON_SELECTED = 10;

    public static final int ICON_SELECTED_FOCUSED = 11;

    public static final int ICON_PRESSED_SELECTED = 12;

    public static final int ICON_PRESSED_SELECTED_FOCUSED = 13;

    public static final int ICON_MOUSEOVER_SELECTED = 14;

    public static final int ICON_MOUSEOVER_SELECTED_FOCUSED = 15;

    public static final int ICON_DISABLED_SELECTED = 16;

    protected URL url;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    protected Image image;

    public OCheckBoxPainter(int state, PaintContext ctx) {
        super(state, ctx);
    }

    public OCheckBoxPainter(int state, PaintContext ctx, URL url) {
        super(state, ctx);
        this.url = url;
    }

    public Image getImage() {
        if (this.image == null) {
            if (this.url != null) {
                this.image = new ImageIcon(this.url).getImage();
            }
        }
        return this.image;
    }

    @Override
    protected String getComponentKeyName() {
        return "CheckBox";
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        this.componentColors = extendedCacheKeys;
        switch (this.state) {
            case ICON_DISABLED:
            case ICON_ENABLED:
            case ICON_FOCUSED:
            case ICON_MOUSEOVER:
            case ICON_MOUSEOVER_FOCUSED:
            case ICON_PRESSED:
            case ICON_PRESSED_FOCUSED:
            case ICON_SELECTED:
            case ICON_SELECTED_FOCUSED:
            case ICON_PRESSED_SELECTED:
            case ICON_PRESSED_SELECTED_FOCUSED:
            case ICON_MOUSEOVER_SELECTED:
            case ICON_MOUSEOVER_SELECTED_FOCUSED:
            case ICON_DISABLED_SELECTED:
                this.paintImage(g);
                break;
        }
    }

    protected void paintImage(Graphics2D g) {
        g.drawImage(this.getImage(), (int) this.decodeX(0f), // x
                (int) this.decodeY(0f), // y
                (int) (this.decodeX(3f) - this.decodeX(0f)), // width
                (int) (this.decodeY(3f) - this.decodeY(0f)), null);
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

}
