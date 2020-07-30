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
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

/**
 * Painter for table header renderer (to configure border and foreground colors, painters, images,
 * ...)
 *
 * @author Imatia Innovation
 *
 */
public class OTableHeaderRendererPainter extends AbstractRegionPainter {

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

    // the following 4 variables are reused during the painting code of the
    // layers
    protected Path2D path = new Path2D.Float();

    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

    protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    protected Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    // All Colors used for painting are stored here. Ideally, only those colors
    // being used
    // by a particular instance of OTableHeaderRendererPainter would be created.
    // For the moment at least,
    // however, all are created for each instance.
    protected final Color color1 = this.decodeColor("nimbusBorder", -0.013888836f, 5.823001E-4f, -0.12941176f, 0);

    protected final Color color2 = this.decodeColor("nimbusBlueGrey", -0.01111114f, -0.08625447f, 0.062745094f, 0);

    protected final Color color3 = this.decodeColor("nimbusBlueGrey", -0.013888836f, -0.028334536f, -0.17254901f, 0);

    protected final Color color4 = this.decodeColor("nimbusBlueGrey", -0.013888836f, -0.029445238f, -0.16470587f, 0);

    protected final Color color5 = this.decodeColor("nimbusBlueGrey", -0.02020204f, -0.053531498f, 0.011764705f, 0);

    protected final Color color6 = this.decodeColor("nimbusBlueGrey", 0.055555582f, -0.10655806f, 0.24313724f, 0);

    protected final Color color7 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.08455229f, 0.1607843f, 0);

    protected final Color color8 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);

    protected final Color color9 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.07466974f, 0.23921567f, 0);

    protected final Color color10 = this.decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);

    protected final Color color11 = this.decodeColor("nimbusBlueGrey", 0.055555582f, -0.10658931f, 0.25098038f, 0);

    protected final Color color12 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.08613607f, 0.21960783f, 0);

    protected final Color color13 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.07333623f, 0.20392156f, 0);

    protected final Color color14 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);

    protected final Color color15 = this.decodeColor("nimbusBlueGrey", -0.00505054f, -0.05960039f, 0.10196078f, 0);

    protected final Color color16 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.017742813f, 0.015686274f, 0);

    protected final Color color17 = this.decodeColor("nimbusBlueGrey", -0.0027777553f, -0.0018306673f, -0.02352941f, 0);

    protected final Color color18 = this.decodeColor("nimbusBlueGrey", 0.0055555105f, -0.020436227f, 0.12549019f, 0);

    protected final Color color19 = this.decodeColor("nimbusBase", -0.023096085f, -0.62376213f, 0.4352941f, 0);

    protected final Color color20 = this.decodeColor("nimbusBase", -0.0012707114f, -0.50901747f, 0.31764704f, 0);

    protected final Color color21 = this.decodeColor("nimbusBase", -0.002461195f, -0.47139505f, 0.2862745f, 0);

    protected final Color color22 = this.decodeColor("nimbusBase", -0.0051222444f, -0.49103343f, 0.372549f, 0);

    protected final Color color23 = this.decodeColor("nimbusBase", -8.738637E-4f, -0.49872798f, 0.3098039f, 0);

    protected final Color color24 = this.decodeColor("nimbusBase", -2.2029877E-4f, -0.4916465f, 0.37647057f, 0);

    protected Color color30 = new Color(0xaab7bf);

    protected Color color31 = new Color(0xe5edf0);

    public static Color defaultOutsideRightBorderColor = new Color(0x858d92);

    public static Color defaultInsideRightBorderColor = Color.white;

    public static Color defaultBottomBorderColor = new Color(0x858d92);

    protected Paint outsideRightBorderColor;

    protected Paint insideRightBorderColor;

    protected Paint bottomBorderColor;

    protected Color bgHeaderGradientInit;

    protected Color bgHeaderGradientEnd;

    // Array of current component colors, updated in each paint call
    protected Object[] componentColors;

    public OTableHeaderRendererPainter(int state, PaintContext ctx) {
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
            case BACKGROUND_DISABLED:
                this.paintBackgroundDisabled(g);
                break;
            case BACKGROUND_ENABLED:
                this.paintBackgroundEnabled(g, c);
                break;
            case BACKGROUND_ENABLED_FOCUSED:
                this.paintBackgroundEnabledAndFocused(g);
                break;
            case BACKGROUND_MOUSEOVER:
                this.paintBackgroundMouseOver(g);
                break;
            case BACKGROUND_PRESSED:
                this.paintBackgroundPressed(g);
                break;
            case BACKGROUND_ENABLED_SORTED:
                this.paintBackgroundEnabledAndSorted(g, c);
                break;
            case BACKGROUND_ENABLED_FOCUSED_SORTED:
                this.paintBackgroundEnabledAndFocusedAndSorted(g, c);
                break;
            case BACKGROUND_DISABLED_SORTED:
                this.paintBackgroundDisabledAndSorted(g);
                break;

        }
    }

    @Override
    protected String getComponentKeyName() {
        return "TableHeader:\"TableHeader.renderer\"";
    }

    @Override
    protected void init() {

        // enable:
        Object obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".backgroundDegraded");
        if (obj instanceof Paint[]) {
            Paint[] paints = (Paint[]) obj;
            if (paints[0] instanceof Color) {
                this.bgHeaderGradientInit = (Color) paints[0];
            } else {
                this.bgHeaderGradientInit = new Color(0xaab7bf);
            }

            if ((paints.length > 1) && (paints[1] instanceof Color)) {
                this.bgHeaderGradientEnd = (Color) paints[1];
            } else if (paints.length == 1) {
                this.bgHeaderGradientEnd = this.bgHeaderGradientInit;
            } else {
                this.bgHeaderGradientEnd = new Color(0xe5edf0);
            }

        } else {
            this.bgHeaderGradientInit = new Color(0xaab7bf);
            this.bgHeaderGradientEnd = new Color(0xe5edf0);
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".outsideRightBorder");
        if (obj instanceof Paint) {
            this.outsideRightBorderColor = (Paint) obj;
        } else {
            this.outsideRightBorderColor = OTableHeaderRendererPainter.defaultOutsideRightBorderColor;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".insideRightBorder");
        if (obj instanceof Paint) {
            this.insideRightBorderColor = (Paint) obj;
        } else {
            this.insideRightBorderColor = OTableHeaderRendererPainter.defaultInsideRightBorderColor;
        }

        obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + ".bottomBorder");
        if (obj instanceof Paint) {
            this.bottomBorderColor = (Paint) obj;
        } else {
            this.bottomBorderColor = OTableHeaderRendererPainter.defaultBottomBorderColor;
        }

    }

    @Override
    protected PaintContext getPaintContext() {
        return this.ctx;
    }

    protected void paintBackgroundDisabled(Graphics2D g) {
        this.rect = this.decodeBottomBorder();
        g.setPaint(this.color1);
        g.fill(this.rect);
        this.rect = this.decodeOutsideRightBorder();
        g.setPaint(this.decodeGradient1(this.rect));
        g.fill(this.rect);
        this.rect = this.decodeBackground();
        g.setPaint(this.decodeGradient2(this.rect));
        g.fill(this.rect);

    }

    protected void paintBackgroundEnabled(Graphics2D g, JComponent c) {

        this.rect = this.decodeBackground();
        g.setPaint(this.decodeBgGradient(this.rect));
        g.fill(this.rect);

        // Painting borders...
        this.paintBorders(g, c);
    }

    protected void paintBackgroundEnabledAndFocused(Graphics2D g) {
        this.rect = this.decodeBottomBorder();
        g.setPaint(this.color1);
        g.fill(this.rect);
        this.rect = this.decodeOutsideRightBorder();
        g.setPaint(this.decodeGradient1(this.rect));
        g.fill(this.rect);
        this.rect = this.decodeBackground();
        g.setPaint(this.decodeGradient2(this.rect));
        g.fill(this.rect);
        this.path = this.decodePath1();
        g.setPaint(this.color10);
        g.fill(this.path);

    }

    protected void paintBackgroundMouseOver(Graphics2D g) {
        this.rect = this.decodeBottomBorder();
        g.setPaint(this.color1);
        g.fill(this.rect);
        this.rect = this.decodeOutsideRightBorder();
        g.setPaint(this.decodeGradient1(this.rect));
        g.fill(this.rect);
        this.rect = this.decodeBackground();
        g.setPaint(this.decodeGradient3(this.rect));
        g.fill(this.rect);

    }

    protected void paintBackgroundPressed(Graphics2D g) {
        this.rect = this.decodeBottomBorder();
        g.setPaint(this.color1);
        g.fill(this.rect);
        this.rect = this.decodeOutsideRightBorder();
        g.setPaint(this.decodeGradient1(this.rect));
        g.fill(this.rect);
        this.rect = this.decodeBackground();
        g.setPaint(this.decodeGradient4(this.rect));
        g.fill(this.rect);

    }

    protected void paintBackgroundEnabledAndSorted(Graphics2D g, JComponent c) {
        this.rect = this.decodeBackground();
        g.setPaint(this.decodeBgGradient(this.rect));
        g.fill(this.rect);

        // Painting borders...
        this.paintBorders(g, c);

    }

    protected void paintBackgroundEnabledAndFocusedAndSorted(Graphics2D g, JComponent c) {
        this.rect = this.decodeBackground();
        g.setPaint(this.decodeBgGradient(this.rect));
        g.fill(this.rect);

        // Painting borders...
        this.paintBorders(g, c);

    }

    protected void paintBackgroundDisabledAndSorted(Graphics2D g) {
        this.rect = this.decodeBottomBorder();
        g.setPaint(this.color1);
        g.fill(this.rect);
        this.rect = this.decodeOutsideRightBorder();
        g.setPaint(this.decodeGradient1(this.rect));
        g.fill(this.rect);
        this.rect = this.decodeBackground();
        g.setPaint(this.decodeGradient2(this.rect));
        g.fill(this.rect);

    }

    protected void paintBorders(Graphics2D g, JComponent c) {
        // Painting borders...
        this.rect = this.decodeBottomBorder();
        g.setPaint(this.bottomBorderColor);
        g.fill(this.rect);
        if (!this.isLastColumn(c)) {
            this.rect = this.decodeOutsideRightBorder();
            g.setPaint(this.outsideRightBorderColor);
            g.fill(this.rect);
            this.rect = this.decodeInsideRightBorder();
            g.setPaint(this.insideRightBorderColor);
            g.fill(this.rect);
        }
    }

    protected boolean isLastColumn(JComponent c) {
        JTable table = this.getTable(c);
        if (table != null) {
            int selectedColumn = table.getColumnModel().getColumnIndexAtX(c.getX());
            int lastColumnIndex = this.getLastColumnIndex(c);
            if (selectedColumn == lastColumnIndex) {
                return true;
            }

        }
        return false;
    }

    protected int getLastColumnIndex(JComponent c) {
        int lastColumnIndex = -1;
        JTable table = this.getTable(c);
        if (table != null) {
            int columnCount = table.getModel().getColumnCount();

            for (int i = 0; i < columnCount; i++) {
                int width = table.getColumnModel().getColumn(i).getWidth();
                if (width > 0) {
                    lastColumnIndex = i;
                }
            }
        }
        return lastColumnIndex;
    }

    protected JTable getTable(JComponent c) {
        JTable table = null;
        try {
            Object obj = SwingUtilities.getAncestorOfClass(JTableHeader.class, c);
            if (obj instanceof JTableHeader) {
                table = ((JTableHeader) obj).getTable();
            }
        } catch (Exception e) {
        }
        return table;
    }

    protected Rectangle2D decodeBottomBorder() {
        this.rect.setRect(this.decodeX(0.0f), // x
                this.decodeY(3.0f) - 1, // y
                this.decodeX(3.0f) - this.decodeX(0.0f), // width
                1); // height
        return this.rect;
    }

    protected Rectangle2D decodeTopBorder() {
        this.rect.setRect(this.decodeX(0.0f), // x
                this.decodeY(0.0f), // y
                this.decodeX(3.0f), // width
                1); // height
        return this.rect;
    }

    protected Rectangle2D decodeOutsideRightBorder() {
        this.rect.setRect(this.decodeX(3.0f) - 1, // x
                this.decodeY(0.0f), // y
                1, // width
                this.decodeY(3.0f) - 1); // height
        return this.rect;
    }

    protected Rectangle2D decodeInsideRightBorder() {
        this.rect.setRect(this.decodeX(3.0f) - 2, // x
                this.decodeY(0.0f), // y
                1, // width
                this.decodeY(3.0f) - 1); // height
        return this.rect;
    }

    protected Rectangle2D decodeBackground() {
        this.rect.setRect(this.decodeX(0.0f), // x
                this.decodeY(0.0f), // y
                this.decodeX(3.0f), // width
                this.decodeY(3.0f)); // height
        return this.rect;
    }

    protected Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(0.0f), this.decodeY(3.0f));
        this.path.lineTo(this.decodeX(3.0f), this.decodeY(3.0f));
        this.path.lineTo(this.decodeX(3.0f), this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(0.24000001f), this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(0.24000001f), this.decodeY(0.24000001f));
        this.path.lineTo(this.decodeX(2.7599998f), this.decodeY(0.24000001f));
        this.path.lineTo(this.decodeX(2.7599998f), this.decodeY(2.7599998f));
        this.path.lineTo(this.decodeX(0.24000001f), this.decodeY(2.7599998f));
        this.path.lineTo(this.decodeX(0.24000001f), this.decodeY(0.0f));
        this.path.lineTo(this.decodeX(0.0f), this.decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    protected Paint decodeGradient1(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.14441223f, 0.43703705f, 0.59444445f, 0.75185186f,
                        0.8759259f, 1.0f },
                new Color[] { this.color2, this.decodeColor(this.color2, this.color3, 0.5f), this.color3,
                        this.decodeColor(this.color3, this.color4, 0.5f),
                        this.color4, this.decodeColor(this.color4, this.color5, 0.5f), this.color5 });
    }

    protected Paint decodeGradient2(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.07147767f, 0.2888889f, 0.5490909f, 0.7037037f, 0.8518518f,
                        1.0f },
                new Color[] { this.color6, this.decodeColor(this.color6, this.color7, 0.5f), this.color7,
                        this.decodeColor(this.color7, this.color8, 0.5f), this.color8,
                        this.decodeColor(this.color8, this.color9, 0.5f), this.color9 });
    }

    protected Paint decodeBgGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float h = (float) bounds.getHeight();
        return this.decodeGradient(x, y, x, (1.0f * h) + y, new float[] { 0.0f, 1.0f },
                new Color[] { this.bgHeaderGradientInit, this.bgHeaderGradientEnd });
    }

    protected Paint decodeGradient3(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.07147767f, 0.2888889f, 0.5490909f, 0.7037037f, 0.7919203f,
                        0.88013697f },
                new Color[] { this.color11, this.decodeColor(this.color11, this.color12, 0.5f), this.color12,
                        this.decodeColor(this.color12, this.color13, 0.5f),
                        this.color13, this.decodeColor(this.color13, this.color14, 0.5f), this.color14 });
    }

    protected Paint decodeGradient4(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.07147767f, 0.2888889f, 0.5490909f, 0.7037037f, 0.8518518f,
                        1.0f },
                new Color[] { this.color15, this.decodeColor(this.color15, this.color16, 0.5f), this.color16,
                        this.decodeColor(this.color16, this.color17, 0.5f),
                        this.color17, this.decodeColor(this.color17, this.color18, 0.5f), this.color18 });
    }

    protected Paint decodeGradient5(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.08049711f, 0.32534248f, 0.56267816f, 0.7037037f,
                        0.83986557f, 0.97602737f },
                new Color[] { this.color19, this.decodeColor(this.color19, this.color20, 0.5f), this.color20,
                        this.decodeColor(this.color20, this.color21, 0.5f), this.color21,
                        this.decodeColor(this.color21, this.color22, 0.5f), this.color22 });
    }

    protected Paint decodeGradient6(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return this.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f, 0.07147767f, 0.2888889f, 0.5490909f, 0.7037037f, 0.8518518f,
                        1.0f },
                new Color[] { this.color19, this.decodeColor(this.color19, this.color23, 0.5f), this.color23,
                        this.decodeColor(this.color23, this.color21, 0.5f),
                        this.color21, this.decodeColor(this.color21, this.color24, 0.5f), this.color24 });
    }

}
