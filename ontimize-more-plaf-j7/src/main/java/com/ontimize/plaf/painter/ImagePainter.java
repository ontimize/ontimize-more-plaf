package com.ontimize.plaf.painter;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import sun.awt.AppContext;
import sun.swing.plaf.synth.Paint9Painter;

import com.ontimize.plaf.utils.OntimizeLAFParseUtils;

public abstract class ImagePainter extends AbstractPainter {


    protected static final StringBuffer CACHE_KEY = new StringBuffer("OntimizeCacheKey");

    protected Image image;

    protected Insets sInsets;

    protected Insets dInsets;

    protected URL path;

    protected boolean tiles;

    protected boolean paintCenter;

    protected Paint9Painter imageCache;

    protected boolean center;

    protected Object[] componentColors;

    protected int state;

    protected static Paint9Painter getPaint9Painter() {
        // A SynthPainter is created per <imagePainter>. We want the
        // cache to be shared by all, and we don't use a static because we
        // don't want it to persist between look and feels. For that reason
        // we use a AppContext specific Paint9Painter. It's backed via
        // a WeakRef so that it can go away if the look and feel changes.
        synchronized (ImagePainter.CACHE_KEY) {
            WeakReference<Paint9Painter> cacheRef = (WeakReference<Paint9Painter>) AppContext.getAppContext()
                .get(ImagePainter.CACHE_KEY);
            Paint9Painter painter;
            if ((cacheRef == null) || ((painter = cacheRef.get()) == null)) {
                painter = new Paint9Painter(30);
                cacheRef = new WeakReference(painter);
                AppContext.getAppContext().put(ImagePainter.CACHE_KEY, cacheRef);
            }
            return painter;
        }
    }

    public ImagePainter(int which, boolean tiles, boolean paintCenter, Insets sourceInsets, Insets destinationInsets,
            URL path, boolean center) {
        if (sourceInsets != null) {
            this.sInsets = (Insets) sourceInsets.clone();
        }
        if (destinationInsets == null) {
            this.dInsets = this.sInsets;
        } else {
            this.dInsets = (Insets) destinationInsets.clone();
        }
        this.tiles = tiles;
        this.paintCenter = paintCenter;
        this.imageCache = ImagePainter.getPaint9Painter();
        this.path = path;
        this.center = center;

        this.state = which;
    }

    public boolean getTiles() {
        return this.tiles;
    }

    public boolean getPaintsCenter() {
        return this.paintCenter;
    }

    public boolean getCenter() {
        return this.center;
    }

    public Insets getInsets(Insets insets) {
        if (insets == null) {
            return (Insets) this.dInsets.clone();
        }
        insets.left = this.dInsets.left;
        insets.right = this.dInsets.right;
        insets.top = this.dInsets.top;
        insets.bottom = this.dInsets.bottom;
        return insets;
    }

    public Image getImage() {
        if (this.image == null) {
            this.image = new ImageIcon(this.path, null).getImage();
        }
        return this.image;
    }


    /**
     * Testing: Añadido para probar si podemos mantener esta forma (necesario distintos tamaños de
     * botones, pq si se redimensiona uno sólo, pixela y deforma)
     * @param component
     * @return
     */
    public Image getImage(JComponent component) {

        Image i = new ImageIcon(this.path, null).getImage();

        double w = component.getSize().getWidth();
        double h = component.getSize().getHeight();
        if ((w < 25) && (h < 25)) {
            // Parsing the received path String to get an ImageIcon Object and caching and returning it:
            URL url = OntimizeLAFParseUtils.class.getClassLoader()
                .getResource("com/ontimize/plaf/images/20x20_button.png"); // -> not valid for tomcat deployments. It
                                                                           // must be getResourceAsStream
            if (url != null) {
                i = new ImageIcon(url, null).getImage();
            }
        } else if ((w > 100) && (h > 40)) {
            // Parsing the received path String to get an ImageIcon Object and caching and returning it:
            URL url = OntimizeLAFParseUtils.class.getClassLoader()
                .getResource("com/ontimize/plaf/images/135x48_button.png"); // -> not valid for tomcat deployments. It
                                                                            // must be getResourceAsStream
            if (url != null) {
                i = new ImageIcon(url, null).getImage();
            }
        } else if ((2 * w) < h) {
            // Parsing the received path String to get an ImageIcon Object and caching and returning it:
            URL url = OntimizeLAFParseUtils.class.getClassLoader()
                .getResource("com/ontimize/plaf/images/34x20_button.png"); // -> not valid for tomcat deployments. It
                                                                           // must be getResourceAsStream
            if (url != null) {
                i = new ImageIcon(url, null).getImage();
            }
        } else {
            // Parsing the received path String to get an ImageIcon Object and caching and returning it:
            URL url = OntimizeLAFParseUtils.class.getClassLoader().getResource("com/ontimize/plaf/images/bottom.png"); // ->
                                                                                                                       // not
                                                                                                                       // valid
                                                                                                                       // for
                                                                                                                       // tomcat
                                                                                                                       // deployments.
                                                                                                                       // It
                                                                                                                       // must
                                                                                                                       // be
                                                                                                                       // getResourceAsStream
            if (url != null) {
                i = new ImageIcon(url, null).getImage();
            }
        }

        return i;
    }


    protected void paint(JComponent component, Graphics g, int x, int y, int w, int h) {
        Image image = this.getImage();
        // Image image = getImage(component);
        if (Paint9Painter.validImage(image)) {
            Paint9Painter.PaintType type;
            if (this.getCenter()) {
                type = Paint9Painter.PaintType.CENTER;
            } else if (!this.getTiles()) {
                type = Paint9Painter.PaintType.PAINT9_STRETCH;
            } else {
                type = Paint9Painter.PaintType.PAINT9_TILE;
            }
            int mask = Paint9Painter.PAINT_ALL;
            if (!this.getCenter() && !this.getPaintsCenter()) {
                mask |= Paint9Painter.PAINT_CENTER;
            }
            this.imageCache.paint(component, g, x, y, w, h, image, this.sInsets, this.dInsets, type, mask);
        }
    }

}
