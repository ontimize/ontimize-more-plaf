package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.utils.LinearGradient;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;
import com.ontimize.plaf.utils.OntimizeLAFPainterUtils;
import com.ontimize.plaf.utils.OntimizeLAFParseUtils;

/**
 * ToolBar Painter (to configure border and foreground colors, painters, images, ...). Several
 * states are allowed such us north, west, east or south.
 *
 * @author Imatia Innovation
 *
 */
public abstract class AbstractOToolBarPainter extends AbstractRegionPainter {

    // extends AbstractRegionPainter{
    // package protected integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of OToolBarPainter to determine which region/state is being painted
    // by that instance.
    public static final int BORDER_ENABLED = 1;
    static final int HANDLEICON_ENABLED = 2; // not public due to we do not
                                             // allow this item

    public static final int BACKGROUND_ENABLED = 3;

    // painter to fill the component:
    public static boolean defaultUseTextureImage = true;

    protected boolean useTextureImage;

    protected String borderPainter;

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();

    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of ToolBarPainter would be created. For the
    // moment at least,
    // however, all are created for each instance.
    protected final Color color1 = this.decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);

    protected final Color color2 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);

    protected final Color color3 = this.decodeColor("nimbusBlueGrey", -0.006944418f, -0.07399663f, 0.11372548f, 0);

    protected final Color color4 = this.decodeColor("nimbusBorder", 0.0f, -0.029675633f, 0.109803915f, 0);

    protected final Color color5 = this.decodeColor("nimbusBlueGrey", -0.008547008f, -0.03494492f, -0.07058823f, 0);

    protected final Color defaultBgColor = this.decodeColor("background", 0.0f, 0.0f, 0.0f, 0);

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    protected java.awt.Image bgImage;

    protected TexturePaint texturePaint;

    public static String imgUrl = "com/ontimize/plaf/images/toolbarbackground.png";

    protected Paint bgPaint;

    public AbstractOToolBarPainter(int state, PaintContext ctx) {
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
            case BORDER_ENABLED:
                this.paintBorderEnabled(g);
                break;
            // case HANDLEICON_ENABLED: painthandleIconEnabled(g); break;
            case BACKGROUND_ENABLED:
                this.paintBackground(g, c, width, height);
                break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    protected abstract String getComponentState();

    /**
     * Get configuration properties to be used in this painter (such as: *BorderPainter,
     * *upperCornerPainter, *lowerCornerPainter and *BgPainter). As usual, it is checked the
     * OLAFCustomConfig.properties, and then in OLAFDefaultConfig.properties.
     *
     * Moreover, if there are not values for that properties in both, the default Nimbus configuration
     * values are set, due to, those properties are needed to paint and used by the Ontimize L&F, so,
     * there are not Nimbus values for that, so, default values are always configured based on Nimbus
     * values.
     *
     */
    @Override
    protected void init() {
        if (UIManager.getDefaults().get("ToolBar.useTextureImage") instanceof Boolean) {
            this.useTextureImage = (Boolean) UIManager.getLookAndFeel().getDefaults().get("ToolBar.useTextureImage");
        } else {
            this.useTextureImage = AbstractOToolBarPainter.defaultUseTextureImage;
        }

        AbstractOToolBarPainter.imgUrl = UIManager.getDefaults().get("ToolBar.backgroundImage") != null
                ? (String) UIManager.getDefaults().get("ToolBar.backgroundImage")
                : AbstractOToolBarPainter.imgUrl;
        if (this.useTextureImage) {
            if (AbstractOToolBarPainter.imgUrl != null) {
                URL url = this.getClass().getClassLoader().getResource(AbstractOToolBarPainter.imgUrl);
                this.bgImage = new ImageIcon(url).getImage();
                if (this.bgImage != null) {
                    BufferedImage bi = new BufferedImage(this.bgImage.getWidth(null), this.bgImage.getHeight(null),
                            BufferedImage.TYPE_INT_ARGB);
                    bi.getGraphics().drawImage(this.bgImage, 0, 0, null);
                    this.texturePaint = new TexturePaint(bi, new Rectangle(bi.getWidth(null), bi.getHeight(null)));
                }
            }
        }

        Object obj = UIManager.getLookAndFeelDefaults().get("ToolBar.bgpaint");
        if (obj instanceof Paint) {
            this.bgPaint = (Paint) obj;
        } else {
            this.bgPaint = null;
        }

    }

    /**
     * Created to paint toolbar background
     * @param g
     * @param c
     * @param width
     * @param height
     */
    protected void paintBackground(Graphics2D g, JComponent c, int width, int height) {
        this.rect = this.decodeRect2();

        if (this.useTextureImage && (this.texturePaint != null)) {
            g.setPaint(this.texturePaint);
        } else {
            Paint tp = null;
            if (this.bgPaint instanceof LinearGradient) {
                Rectangle bounds = new Rectangle();
                bounds.x = 0;
                bounds.y = 0;
                bounds.width = width;
                bounds.height = height;
                tp = OntimizeLAFColorUtils.decodeGradient(bounds, (LinearGradient) this.bgPaint);
            } else if ((c != null) && (c.getBackground() != null)) {
                tp = c.getBackground();
            }
            g.setPaint(tp);
        }
        g.fillRect(0, 0, width, height);
    }

    protected void paintDiagramBackground(Graphics2D g, JComponent c, int width, int height) {

        Color oldC = g.getColor();

        Color topBackgroundColor = OntimizeLAFParseUtils.parseColor("#DAE7ED", Color.black);
        Color downBackgroundColor = OntimizeLAFParseUtils.parseColor("#A2ABB0", Color.black);

        GradientPaint downPaint = new GradientPaint(0, height, downBackgroundColor, 0, this.decodeHeight(height),
                topBackgroundColor);
        Graphics2D g2D = g;
        Paint oldPaint = g2D.getPaint();
        g2D.setPaint(downPaint);
        g2D.fillRect(0, (int) (height - (height * 0.2)), width, height);
        g2D.setPaint(oldPaint);

        Shape s = ShapeFactory.getInstace()
            .createSemiRoundRect(0, 0, width, this.decodeHeight(height), 5, 5, ShapeFactory.TOP_ORIENTATION);
        g.setColor(topBackgroundColor);
        g.fill(s);
        g.setColor(oldC);
    }

    protected int decodeHeight(int height) {
        return (int) (height - (height * 0.2));
    }

    protected void paintBorderEnabled(Graphics2D g) {
        this.rect = this.decodeRect1();
        g.setPaint(OntimizeLAFPainterUtils.getPaint(this.rect, this.borderPainter));
        g.fill(this.rect);

    }

    protected Rectangle2D decodeRect1() {
        this.rect.setRect(this.decodeX(1.0f), // x
                this.decodeY(2.0f), // y
                this.decodeX(2.0f) - this.decodeX(1.0f), // width
                this.decodeY(3.0f) - this.decodeY(2.0f)); // height
        return this.rect;
    }

    protected Rectangle2D decodeRect2() {
        this.rect.setRect(this.decodeX(0.0f), // x
                this.decodeY(0.0f), // y
                this.decodeX(2.8f) - this.decodeX(0.0f), // width
                this.decodeY(3.0f) - this.decodeY(0.0f)); // height
        return this.rect;
    }

    protected Rectangle2D decodeRect3() {
        this.rect.setRect(this.decodeX(2.8f), // x
                this.decodeY(0.0f), // y
                this.decodeX(3.0f) - this.decodeX(2.8f), // width
                this.decodeY(3.0f) - this.decodeY(0.0f)); // height
        return this.rect;
    }

    protected Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(0.0f), this.decodeY(0.4f));
        this.path.lineTo(this.decodeX(0.4f), this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(0.0f), this.decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    protected Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(this.decodeX(0.0f), this.decodeY(3.0f));
        this.path.lineTo(this.decodeX(0.0f), this.decodeY(2.6f));
        this.path.lineTo(this.decodeX(0.4f), this.decodeY(3.0f));
        this.path.lineTo(this.decodeX(0.0f), this.decodeY(3.0f));
        this.path.closePath();
        return this.path;
    }

}
