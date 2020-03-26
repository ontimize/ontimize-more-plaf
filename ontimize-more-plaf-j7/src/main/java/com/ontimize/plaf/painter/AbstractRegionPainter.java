package com.ontimize.plaf.painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Path2D;
import java.awt.image.VolatileImage;
import java.awt.print.PrinterGraphics;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.Painter;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.ontimize.plaf.utils.ImageCache;
import com.ontimize.plaf.utils.ImageScalingHelper;

public abstract class AbstractRegionPainter implements Painter<JComponent> {

	/**
	 * Added to be able to get the whole area that must be painted, in order to
	 * create Ontimize Painters for the whole area. Sometimes, only a part of
	 * the whole area will be painted (depending on the specifications in the
	 * classes that extends this one).
	 *
	 * @param path
	 * @return
	 */
	protected Path2D getWholePath(Path2D path) {
		path.reset();
		path.moveTo(this.decodeX(0.0f), this.decodeY(0.0f));
		path.lineTo(this.decodeX(3.0f), this.decodeY(0.0f));
		path.lineTo(this.decodeX(3.0f), this.decodeY(3.0f));
		path.lineTo(this.decodeX(0.0f), this.decodeY(3.0f));
		path.lineTo(this.decodeX(0.0f), this.decodeY(0.0f));
		path.closePath();
		return path;
	}

	/**
	 * PaintContext, which holds a lot of the state needed for cache hinting and
	 * x/y value decoding The data contained within the context is typically
	 * only computed once and reused over multiple paint calls, whereas the
	 * other values (w, h, f, leftWidth, etc) are recomputed for each call to
	 * paint.
	 *
	 * This field is retrieved from subclasses on each paint operation. It is up
	 * to the subclass to compute and cache the PaintContext over multiple
	 * calls.
	 */
	protected PaintContext ctx;

	protected int state;

	/**
	 * The scaling factor. Recomputed on each call to paint.
	 */
	protected float f;
	/*
	 * Various metrics used for decoding x/y values based on the canvas size and
	 * stretching insets.
	 * 
	 * On each call to paint, we first ask the subclass for the PaintContext.
	 * From the context we get the canvas size and stretching insets, and
	 * whether the algorithm should be "inverted", meaning the center section
	 * remains a fixed size and the other sections scale.
	 * 
	 * We then use these values to compute a series of metrics (listed below)
	 * which are used to decode points in a specific axis (x or y).
	 * 
	 * The leftWidth represents the distance from the left edge of the region to
	 * the first stretching inset, after accounting for any scaling factor (such
	 * as DPI scaling). The centerWidth is the distance between the leftWidth
	 * and the rightWidth. The rightWidth is the distance from the right edge,
	 * to the right inset (after scaling has been applied).
	 * 
	 * The same logic goes for topHeight, centerHeight, and bottomHeight.
	 * 
	 * The leftScale represents the proportion of the width taken by the left
	 * section. The same logic is applied to the other scales.
	 * 
	 * The various widths/heights are used to decode control points. The various
	 * scales are used to decode bezier handles (or anchors).
	 */
	/**
	 * The width of the left section. Recomputed on each call to paint.
	 */
	protected float leftWidth;
	/**
	 * The height of the top section. Recomputed on each call to paint.
	 */
	protected float topHeight;
	/**
	 * The width of the center section. Recomputed on each call to paint.
	 */
	protected float centerWidth;
	/**
	 * The height of the center section. Recomputed on each call to paint.
	 */
	protected float centerHeight;
	/**
	 * The width of the right section. Recomputed on each call to paint.
	 */
	protected float rightWidth;
	/**
	 * The height of the bottom section. Recomputed on each call to paint.
	 */
	protected float bottomHeight;
	/**
	 * The scaling factor to use for the left section. Recomputed on each call
	 * to paint.
	 */
	protected float leftScale;
	/**
	 * The scaling factor to use for the top section. Recomputed on each call to
	 * paint.
	 */
	protected float topScale;
	/**
	 * The scaling factor to use for the center section, in the horizontal
	 * direction. Recomputed on each call to paint.
	 */
	protected float centerHScale;
	/**
	 * The scaling factor to use for the center section, in the vertical
	 * direction. Recomputed on each call to paint.
	 */
	protected float centerVScale;
	/**
	 * The scaling factor to use for the right section. Recomputed on each call
	 * to paint.
	 */
	protected float rightScale;
	/**
	 * The scaling factor to use for the bottom section. Recomputed on each call
	 * to paint.
	 */
	protected float bottomScale;

	/**
	 * Create a new AbstractRegionPainter
	 */
	public AbstractRegionPainter(int state, PaintContext ctx) {
		this.state = state;
		this.ctx = ctx;
		this.initializeDefaultColors();
		this.init();
	}

	protected void init() {}

	protected void initializeDefaultColors() {};

	protected abstract String getComponentKeyName();

	/**
	 * @inheritDoc
	 */
	@Override
	public void paint(Graphics2D g, JComponent c, int w, int h) {
		// don't render if the width/height are too small
		if ((w <= 0) || (h <= 0)) {
			return;
		}

		Object[] extendedCacheKeys = this.getExtendedCacheKeys(c);
		this.ctx = this.getPaintContext();
		PaintContext.CacheMode cacheMode = this.ctx == null ? PaintContext.CacheMode.NO_CACHING : this.ctx.cacheMode;
		if ((cacheMode == PaintContext.CacheMode.NO_CACHING) || !ImageCache.getInstance().isImageCachable(w, h) || (g instanceof PrinterGraphics)) {
			// no caching so paint directly
			this.paint0(g, c, w, h, extendedCacheKeys);
		} else if (cacheMode == PaintContext.CacheMode.FIXED_SIZES) {
			this.paintWithFixedSizeCaching(g, c, w, h, extendedCacheKeys);
		} else {
			// 9 Square caching
			this.paintWith9SquareCaching(g, this.ctx, c, w, h, extendedCacheKeys);
		}
	}

	/**
	 * Get any extra attributes which the painter implementation would like to
	 * include in the image cache lookups. This is checked for every call of the
	 * paint(g, c, w, h) method.
	 *
	 * @param c
	 *            The component on the current paint call
	 * @return Array of extra objects to be included in the cache key
	 */
	protected Object[] getExtendedCacheKeys(JComponent c) {
		return null;
	}

	/**
	 * <p>
	 * Gets the PaintContext for this painting operation. This method is called
	 * on every paint, and so should be fast and produce no garbage. The
	 * PaintContext contains information such as cache hints. It also contains
	 * data necessary for decoding points at runtime, such as the stretching
	 * insets, the canvas size at which the encoded points were defined, and
	 * whether the stretching insets are inverted.
	 * </p>
	 *
	 * <p>
	 * This method allows for subclasses to package the painting of different
	 * states with possibly different canvas sizes, etc, into one
	 * AbstractRegionPainter implementation.
	 * </p>
	 *
	 * @return a PaintContext associated with this paint operation.
	 */
	protected abstract PaintContext getPaintContext();

	/**
	 * <p>
	 * Configures the given Graphics2D. Often, rendering hints or compositiing
	 * rules are applied to a Graphics2D object prior to painting, which should
	 * affect all of the subsequent painting operations. This method provides a
	 * convenient hook for configuring the Graphics object prior to rendering,
	 * regardless of whether the render operation is performed to an
	 * intermediate buffer or directly to the display.
	 * </p>
	 *
	 * @param g
	 *            The Graphics2D object to configure. Will not be null.
	 */
	protected void configureGraphics(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	/**
	 * Actually performs the painting operation. Subclasses must implement this
	 * method. The graphics object passed may represent the actual surface being
	 * rendererd to, or it may be an intermediate buffer. It has also been
	 * pre-translated. Simply render the component as if it were located at 0, 0
	 * and had a width of <code>width</code> and a height of <code>height</code>
	 * . For performance reasons, you may want to read the clip from the
	 * Graphics2D object and only render within that space.
	 *
	 * @param g
	 *            The Graphics2D surface to paint to
	 * @param c
	 *            The JComponent related to the drawing event. For example, if
	 *            the region being rendered is Button, then <code>c</code> will
	 *            be a JButton. If the region being drawn is ScrollBarSlider,
	 *            then the component will be JScrollBar. This value may be null.
	 * @param width
	 *            The width of the region to paint. Note that in the case of
	 *            painting the foreground, this value may differ from
	 *            c.getWidth().
	 * @param height
	 *            The height of the region to paint. Note that in the case of
	 *            painting the foreground, this value may differ from
	 *            c.getHeight().
	 * @param extendedCacheKeys
	 *            The result of the call to getExtendedCacheKeys()
	 */
	protected abstract void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys);

	/**
	 * Decodes and returns a float value representing the actual pixel location
	 * for the given encoded X value.
	 *
	 * @param x
	 *            an encoded x value (0...1, or 1...2, or 2...3)
	 * @return the decoded x value
	 */
	protected float decodeX(float x) {
		if (this.ctx.canvasSize == null) {
			return x;
		}

		if ((x >= 0) && (x <= 1)) {
			return x * this.leftWidth;
		} else if ((x > 1) && (x < 2)) {
			return ((x - 1) * this.centerWidth) + this.leftWidth;
		} else if ((x >= 2) && (x <= 3)) {
			return ((x - 2) * this.rightWidth) + this.leftWidth + this.centerWidth;
		} else {
			throw new AssertionError("Invalid x");
		}
	}

	/**
	 * Decodes and returns a float value representing the actual pixel location
	 * for the given encoded y value.
	 *
	 * @param y
	 *            an encoded y value (0...1, or 1...2, or 2...3)
	 * @return the decoded y value
	 */
	protected float decodeY(float y) {
		if (this.ctx.canvasSize == null) {
			return y;
		}

		if ((y >= 0) && (y <= 1)) {
			return y * this.topHeight;
		} else if ((y > 1) && (y < 2)) {
			return ((y - 1) * this.centerHeight) + this.topHeight;
		} else if ((y >= 2) && (y <= 3)) {
			return ((y - 2) * this.bottomHeight) + this.topHeight + this.centerHeight;
		} else {
			throw new AssertionError("Invalid y");
		}
	}

	/**
	 * Decodes and returns a float value representing the actual pixel location
	 * for the anchor point given the encoded X value of the control point, and
	 * the offset distance to the anchor from that control point.
	 *
	 * @param x
	 *            an encoded x value of the bezier control point (0...1, or
	 *            1...2, or 2...3)
	 * @param dx
	 *            the offset distance to the anchor from the control point x
	 * @return the decoded x location of the control point
	 */
	protected float decodeAnchorX(float x, float dx) {
		if (this.ctx.canvasSize == null) {
			return x + dx;
		}

		if ((x >= 0) && (x <= 1)) {
			return this.decodeX(x) + (dx * this.leftScale);
		} else if ((x > 1) && (x < 2)) {
			return this.decodeX(x) + (dx * this.centerHScale);
		} else if ((x >= 2) && (x <= 3)) {
			return this.decodeX(x) + (dx * this.rightScale);
		} else {
			throw new AssertionError("Invalid x");
		}
	}

	/**
	 * Decodes and returns a float value representing the actual pixel location
	 * for the anchor point given the encoded Y value of the control point, and
	 * the offset distance to the anchor from that control point.
	 *
	 * @param y
	 *            an encoded y value of the bezier control point (0...1, or
	 *            1...2, or 2...3)
	 * @param dy
	 *            the offset distance to the anchor from the control point y
	 * @return the decoded y position of the control point
	 */
	protected float decodeAnchorY(float y, float dy) {
		if (this.ctx.canvasSize == null) {
			return y + dy;
		}

		if ((y >= 0) && (y <= 1)) {
			return this.decodeY(y) + (dy * this.topScale);
		} else if ((y > 1) && (y < 2)) {
			return this.decodeY(y) + (dy * this.centerVScale);
		} else if ((y >= 2) && (y <= 3)) {
			return this.decodeY(y) + (dy * this.bottomScale);
		} else {
			throw new AssertionError("Invalid y");
		}
	}

	/**
	 * Decodes and returns a color, which is derived from a base color in UI
	 * defaults.
	 *
	 * @param key
	 *            A key corrosponding to the value in the UI Defaults table of
	 *            UIManager where the base color is defined
	 * @param hOffset
	 *            The hue offset used for derivation.
	 * @param sOffset
	 *            The saturation offset used for derivation.
	 * @param bOffset
	 *            The brightness offset used for derivation.
	 * @param aOffset
	 *            The alpha offset used for derivation. Between 0...255
	 * @return The derived color, whos color value will change if the parent
	 *         uiDefault color changes.
	 */
	protected Color decodeColor(String key, float hOffset, float sOffset, float bOffset, int aOffset) {
		if (UIManager.getLookAndFeel() instanceof NimbusLookAndFeel) {
			NimbusLookAndFeel laf = (NimbusLookAndFeel) UIManager.getLookAndFeel();
			return laf.getDerivedColor(key, hOffset, sOffset, bOffset, aOffset, true);
		} else {
			// can not give a right answer as painter sould not be used outside
			// of nimbus laf but do the best we can
			return Color.getHSBColor(hOffset, sOffset, bOffset);
		}
	}

	/**
	 * Decodes and returns a color, which is derived from a offset between two
	 * other colors.
	 *
	 * @param color1
	 *            The first color
	 * @param color2
	 *            The second color
	 * @param midPoint
	 *            The offset between color 1 and color 2, a value of 0.0 is
	 *            color 1 and 1.0 is color 2;
	 * @return The derived color, whos color value will change if either of the
	 *         colors change, this will only work if they are color fetched from
	 *         the other decodeColor(s,f,f,f,i) method as they fire property
	 *         change events for then they change.
	 */
	protected Color decodeColor(Color color1, Color color2, float midPoint) {
		if (UIManager.getLookAndFeel() instanceof NimbusLookAndFeel) {
			NimbusLookAndFeel laf = (NimbusLookAndFeel) UIManager.getLookAndFeel();
			// return laf.getDerivedColor(color1, color2, midPoint);
			return this.getDerivedColor(color1, color2, midPoint);
		} else {
			// can not give a right answer as painter should not be used outside
			// of nimbus laf but do the best we can
			return new Color((color1.getRed() + color2.getRed()) / 2, (color1.getGreen() + color2.getGreen()) / 2, (color1.getBlue() + color2.getBlue()) / 2);
		}
	}

	/**
	 * Given parameters for creating a LinearGradientPaint, this method will
	 * create and return a linear gradient paint. One primary purpose for this
	 * method is to avoid creating a LinearGradientPaint where the start and end
	 * points are equal. In such a case, the end y point is slightly increased
	 * to avoid the overlap.
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param midpoints
	 * @param colors
	 * @return a valid LinearGradientPaint. This method never returns null.
	 */
	protected LinearGradientPaint decodeGradient(float x1, float y1, float x2, float y2, float[] midpoints, Color[] colors) {
		if ((x1 == x2) && (y1 == y2)) {
			y2 += .00001f;
		}
		return new LinearGradientPaint(x1, y1, x2, y2, midpoints, colors);
	}

	/**
	 * Given parameters for creating a RadialGradientPaint, this method will
	 * create and return a radial gradient paint. One primary purpose for this
	 * method is to avoid creating a RadialGradientPaint where the radius is
	 * non-positive. In such a case, the radius is just slightly increased to
	 * avoid 0.
	 *
	 * @param x
	 * @param y
	 * @param r
	 * @param midpoints
	 * @param colors
	 * @return a valid RadialGradientPaint. This method never returns null.
	 */
	protected RadialGradientPaint decodeRadialGradient(float x, float y, float r, float[] midpoints, Color[] colors) {
		if (r == 0f) {
			r = .00001f;
		}
		return new RadialGradientPaint(x, y, r, midpoints, colors);
	}

	/**
	 * Get a color property from the given JComponent. First checks for a
	 * <code>getXXX()</code> method and if that fails checks for a client
	 * property with key <code>property</code>. If that still fails to return a
	 * Color then <code>defaultColor</code> is returned.
	 *
	 * @param c
	 *            The component to get the color property from
	 * @param property
	 *            The name of a bean style property or client property
	 * @param defaultColor
	 *            The color to return if no color was obtained from the
	 *            component.
	 * @return The color that was obtained from the component or defaultColor
	 */
	protected Color getComponentColor(JComponent c, String property, Color defaultColor, float saturationOffset, float brightnessOffset, int alphaOffset) {
		Color color = null;
		if (c != null) {
			// handle some special cases for performance
			if ("background".equals(property)) {
				color = c.getBackground();
			} else if ("foreground".equals(property)) {
				color = c.getForeground();
			} else if ((c instanceof JList) && "selectionForeground".equals(property)) {
				color = ((JList) c).getSelectionForeground();
			} else if ((c instanceof JList) && "selectionBackground".equals(property)) {
				color = ((JList) c).getSelectionBackground();
			} else if ((c instanceof JTable) && "selectionForeground".equals(property)) {
				color = ((JTable) c).getSelectionForeground();
			} else if ((c instanceof JTable) && "selectionBackground".equals(property)) {
				color = ((JTable) c).getSelectionBackground();
			} else {
				String s = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
				try {
					Method method = c.getClass().getMethod(s);
					color = (Color) method.invoke(c);
				} catch (Exception e) {
					// don't do anything, it just didn't work, that's all.
					// This could be a normal occurance if you use a property
					// name referring to a key in clientProperties instead of
					// a real property
				}
				if (color == null) {
					Object value = c.getClientProperty(property);
					if (value instanceof Color) {
						color = (Color) value;
					}
				}
			}
		}
		// we return the defaultColor if the color found is null, or if
		// it is a UIResource. This is done because the color for the
		// ENABLED state is set on the component, but you don't want to use
		// that color for the over state. So we only respect the color
		// specified for the property if it was set by the user, as opposed
		// to set by us.
		if ((color == null) || (color instanceof UIResource)) {
			return defaultColor;
		} else if ((saturationOffset != 0) || (brightnessOffset != 0) || (alphaOffset != 0)) {
			float[] tmp = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
			tmp[1] = this.clamp(tmp[1] + saturationOffset);
			tmp[2] = this.clamp(tmp[2] + brightnessOffset);
			int alpha = this.clamp(color.getAlpha() + alphaOffset);
			return new Color((Color.HSBtoRGB(tmp[0], tmp[1], tmp[2]) & 0xFFFFFF) | (alpha << 24));
		} else {
			return color;
		}
	}

	/**
	 * A class encapsulating state useful when painting. Generally, instances of
	 * this class are created once, and reused for each paint request without
	 * modification. This class contains values useful when hinting the cache
	 * engine, and when decoding control points and bezier curve anchors.
	 */
	public static class PaintContext {
		public static enum CacheMode {
			NO_CACHING, FIXED_SIZES, NINE_SQUARE_SCALE
		}

		protected static Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

		protected Insets stretchingInsets;
		protected Dimension canvasSize;
		protected boolean inverted;
		protected CacheMode cacheMode;
		protected double maxHorizontalScaleFactor;
		protected double maxVerticalScaleFactor;

		protected float a; // insets.left
		protected float b; // canvasSize.width - insets.right
		protected float c; // insets.top
		protected float d; // canvasSize.height - insets.bottom;
		protected float aPercent; // only used if inverted == true
		protected float bPercent; // only used if inverted == true
		protected float cPercent; // only used if inverted == true
		protected float dPercent; // only used if inverted == true

		/**
		 * Creates a new PaintContext which does not attempt to cache or scale
		 * any cached images.
		 *
		 * @param insets
		 *            The stretching insets. May be null. If null, then assumed
		 *            to be 0, 0, 0, 0.
		 * @param canvasSize
		 *            The size of the canvas used when encoding the various x/y
		 *            values. May be null. If null, then it is assumed that
		 *            there are no encoded values, and any calls to one of the
		 *            "decode" methods will return the passed in value.
		 * @param inverted
		 *            Whether to "invert" the meaning of the 9-square grid and
		 *            stretching insets
		 */
		public PaintContext(Insets insets, Dimension canvasSize, boolean inverted) {
			this(insets, canvasSize, inverted, null, 1, 1);
		}

		/**
		 * Creates a new PaintContext.
		 *
		 * @param insets
		 *            The stretching insets. May be null. If null, then assumed
		 *            to be 0, 0, 0, 0.
		 * @param canvasSize
		 *            The size of the canvas used when encoding the various x/y
		 *            values. May be null. If null, then it is assumed that
		 *            there are no encoded values, and any calls to one of the
		 *            "decode" methods will return the passed in value.
		 * @param inverted
		 *            Whether to "invert" the meaning of the 9-square grid and
		 *            stretching insets
		 * @param cacheMode
		 *            A hint as to which caching mode to use. If null, then set
		 *            to no caching.
		 * @param maxH
		 *            The maximium scale in the horizontal direction to use
		 *            before punting and redrawing from scratch. For example, if
		 *            maxH is 2, then we will attempt to scale any cached images
		 *            up to 2x the canvas width before redrawing from scratch.
		 *            Reasonable maxH values may improve painting performance.
		 *            If set too high, then you may get poor looking graphics at
		 *            higher zoom levels. Must be >= 1.
		 * @param maxV
		 *            The maximium scale in the vertical direction to use before
		 *            punting and redrawing from scratch. For example, if maxV
		 *            is 2, then we will attempt to scale any cached images up
		 *            to 2x the canvas height before redrawing from scratch.
		 *            Reasonable maxV values may improve painting performance.
		 *            If set too high, then you may get poor looking graphics at
		 *            higher zoom levels. Must be >= 1.
		 */
		public PaintContext(Insets insets, Dimension canvasSize, boolean inverted, CacheMode cacheMode, double maxH, double maxV) {
			if ((maxH < 1) || (maxH < 1)) {
				throw new IllegalArgumentException("Both maxH and maxV must be >= 1");
			}

			this.stretchingInsets = insets == null ? PaintContext.EMPTY_INSETS : insets;
			this.canvasSize = canvasSize;
			this.inverted = inverted;
			this.cacheMode = cacheMode == null ? CacheMode.NO_CACHING : cacheMode;
			this.maxHorizontalScaleFactor = maxH;
			this.maxVerticalScaleFactor = maxV;

			if (canvasSize != null) {
				this.a = insets.left;
				this.b = canvasSize.width - insets.right;
				this.c = insets.top;
				this.d = canvasSize.height - insets.bottom;
				this.canvasSize = canvasSize;
				this.inverted = inverted;
				if (inverted) {
					float available = canvasSize.width - (this.b - this.a);
					this.aPercent = available > 0f ? this.a / available : 0f;
					this.bPercent = available > 0f ? this.b / available : 0f;
					available = canvasSize.height - (this.d - this.c);
					this.cPercent = available > 0f ? this.c / available : 0f;
					this.dPercent = available > 0f ? this.d / available : 0f;
				}
			}
		}
	}

	// ---------------------- protected methods

	// initializes the class to prepare it for being able to decode points
	protected void prepare(float w, float h) {
		// if no PaintContext has been specified, reset the values and bail
		// also bail if the canvasSize was not set (since decoding will not
		// work)
		if ((this.ctx == null) || (this.ctx.canvasSize == null)) {
			this.f = 1f;
			this.leftWidth = this.centerWidth = this.rightWidth = 0f;
			this.topHeight = this.centerHeight = this.bottomHeight = 0f;
			this.leftScale = this.centerHScale = this.rightScale = 0f;
			this.topScale = this.centerVScale = this.bottomScale = 0f;
			return;
		}

		// calculate the scaling factor, and the sizes for the various 9-square
		// sections
		Number scale = (Number) UIManager.get("scale");
		this.f = scale == null ? 1f : scale.floatValue();

		if (this.ctx.inverted) {
			this.centerWidth = (this.ctx.b - this.ctx.a) * this.f;
			float availableSpace = w - this.centerWidth;
			this.leftWidth = availableSpace * this.ctx.aPercent;
			this.rightWidth = availableSpace * this.ctx.bPercent;
			this.centerHeight = (this.ctx.d - this.ctx.c) * this.f;
			availableSpace = h - this.centerHeight;
			this.topHeight = availableSpace * this.ctx.cPercent;
			this.bottomHeight = availableSpace * this.ctx.dPercent;
		} else {
			this.leftWidth = this.ctx.a * this.f;
			this.rightWidth = (float) (this.ctx.canvasSize.getWidth() - this.ctx.b) * this.f;
			this.centerWidth = w - this.leftWidth - this.rightWidth;
			this.topHeight = this.ctx.c * this.f;
			this.bottomHeight = (float) (this.ctx.canvasSize.getHeight() - this.ctx.d) * this.f;
			this.centerHeight = h - this.topHeight - this.bottomHeight;
		}

		this.leftScale = this.ctx.a == 0f ? 0f : this.leftWidth / this.ctx.a;
		this.centerHScale = (this.ctx.b - this.ctx.a) == 0f ? 0f : this.centerWidth / (this.ctx.b - this.ctx.a);
		this.rightScale = (this.ctx.canvasSize.width - this.ctx.b) == 0f ? 0f : this.rightWidth / (this.ctx.canvasSize.width - this.ctx.b);
		this.topScale = this.ctx.c == 0f ? 0f : this.topHeight / this.ctx.c;
		this.centerVScale = (this.ctx.d - this.ctx.c) == 0f ? 0f : this.centerHeight / (this.ctx.d - this.ctx.c);
		this.bottomScale = (this.ctx.canvasSize.height - this.ctx.d) == 0f ? 0f : this.bottomHeight / (this.ctx.canvasSize.height - this.ctx.d);
	}

	protected void paintWith9SquareCaching(Graphics2D g, PaintContext ctx, JComponent c, int w, int h, Object[] extendedCacheKeys) {
		// check if we can scale to the requested size
		Dimension canvas = ctx.canvasSize;
		Insets insets = ctx.stretchingInsets;

		if ((w <= (canvas.width * ctx.maxHorizontalScaleFactor)) && (h <= (canvas.height * ctx.maxVerticalScaleFactor))) {
			// get image at canvas size
			VolatileImage img = this.getImage(g.getDeviceConfiguration(), c, canvas.width, canvas.height, extendedCacheKeys);
			if (img != null) {
				// calculate dst inserts
				// todo: destination inserts need to take into acount scale
				// factor for high dpi. Note: You can use f for this, I think
				Insets dstInsets;
				if (ctx.inverted) {
					int leftRight = (w - (canvas.width - (insets.left + insets.right))) / 2;
					int topBottom = (h - (canvas.height - (insets.top + insets.bottom))) / 2;
					dstInsets = new Insets(topBottom, leftRight, topBottom, leftRight);
				} else {
					dstInsets = insets;
				}
				// paint 9 square scaled
				Object oldScaleingHints = g.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				ImageScalingHelper.paint(g, 0, 0, w, h, img, insets, dstInsets, ImageScalingHelper.PaintType.PAINT9_STRETCH, ImageScalingHelper.PAINT_ALL);
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldScaleingHints != null ? oldScaleingHints : RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			} else {
				// render directly
				this.paint0(g, c, w, h, extendedCacheKeys);
			}
		} else {
			// paint directly
			this.paint0(g, c, w, h, extendedCacheKeys);
		}
	}

	protected void paintWithFixedSizeCaching(Graphics2D g, JComponent c, int w, int h, Object[] extendedCacheKeys) {
		VolatileImage img = this.getImage(g.getDeviceConfiguration(), c, w, h, extendedCacheKeys);
		if (img != null) {
			// render cached image
			g.drawImage(img, 0, 0, null);
		} else {
			// render directly
			this.paint0(g, c, w, h, extendedCacheKeys);
		}
	}

	/**
	 * Gets the rendered image for this painter at the requested size, either
	 * from cache or create a new one
	 */
	protected VolatileImage getImage(GraphicsConfiguration config, JComponent c, int w, int h, Object[] extendedCacheKeys) {
		ImageCache imageCache = ImageCache.getInstance();
		// get the buffer for this component
		VolatileImage buffer = (VolatileImage) imageCache.getImage(config, w, h, this, extendedCacheKeys);

		int renderCounter = 0; // to avoid any potential, though unlikely,
		// infinite loop
		do {
			// validate the buffer so we can check for surface loss
			int bufferStatus = VolatileImage.IMAGE_INCOMPATIBLE;
			if (buffer != null) {
				bufferStatus = buffer.validate(config);
			}

			// If the buffer status is incompatible or restored, then we need to
			// re-render to the volatile image
			if ((bufferStatus == VolatileImage.IMAGE_INCOMPATIBLE) || (bufferStatus == VolatileImage.IMAGE_RESTORED)) {
				// if the buffer is null (hasn't been created), or isn't the
				// right size, or has lost its contents,
				// then recreate the buffer
				if ((buffer == null) || (buffer.getWidth() != w) || (buffer.getHeight() != h) || (bufferStatus == VolatileImage.IMAGE_INCOMPATIBLE)) {
					// clear any resources related to the old back buffer
					if (buffer != null) {
						buffer.flush();
						buffer = null;
					}
					// recreate the buffer
					buffer = config.createCompatibleVolatileImage(w, h, Transparency.TRANSLUCENT);
					// put in cache for future
					imageCache.setImage(buffer, config, w, h, this, extendedCacheKeys);
				}
				// create the graphics context with which to paint to the buffer
				Graphics2D bg = buffer.createGraphics();
				// clear the background before configuring the graphics
				bg.setComposite(AlphaComposite.Clear);
				bg.fillRect(0, 0, w, h);
				bg.setComposite(AlphaComposite.SrcOver);
				this.configureGraphics(bg);
				// paint the painter into buffer
				this.paint0(bg, c, w, h, extendedCacheKeys);
				// close buffer graphics
				bg.dispose();
			}
		} while (buffer.contentsLost() && (renderCounter++ < 3));
		// check if we failed
		if (renderCounter == 3) {
			return null;
		}
		// return image
		return buffer;
	}

	// convenience method which creates a temporary graphics object by creating
	// a
	// clone of the passed in one, configuring it, drawing with it, disposing
	// it.
	// These steps have to be taken to ensure that any hints set on the graphics
	// are removed subsequent to painting.
	protected void paint0(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		this.prepare(width, height);
		g = (Graphics2D) g.create();
		this.configureGraphics(g);
		this.doPaint(g, c, width, height, extendedCacheKeys);
		g.dispose();
	}

	protected float clamp(float value) {
		if (value < 0) {
			value = 0;
		} else if (value > 1) {
			value = 1;
		}
		return value;
	}

	protected int clamp(int value) {
		if (value < 0) {
			value = 0;
		} else if (value > 255) {
			value = 255;
		}
		return value;
	}

	/**
	 * Decodes and returns a color, which is derived from an offset between two
	 * other colors.
	 *
	 * @param color1
	 *            The first color
	 * @param color2
	 *            The second color
	 * @param midPoint
	 *            The offset between color 1 and color 2, a value of 0.0 is
	 *            color 1 and 1.0 is color 2;
	 * @param uiResource
	 *            True if the derived color should be a UIResource
	 * @return The derived color
	 */
	protected Color getDerivedColor(Color color1, Color color2, float midPoint, boolean uiResource) {
		int argb = AbstractRegionPainter.deriveARGB(color1, color2, midPoint);
		if (uiResource) {
			return new ColorUIResource(argb);
		} else {
			return new Color(argb);
		}
	}

	/**
	 * Decodes and returns a color, which is derived from a offset between two
	 * other colors.
	 *
	 * @param color1
	 *            The first color
	 * @param color2
	 *            The second color
	 * @param midPoint
	 *            The offset between color 1 and color 2, a value of 0.0 is
	 *            color 1 and 1.0 is color 2;
	 * @return The derived color, which will be a UIResource
	 */
	protected Color getDerivedColor(Color color1, Color color2, float midPoint) {
		return this.getDerivedColor(color1, color2, midPoint, true);
	}

	/**
	 * Derives the ARGB value for a color based on an offset between two other
	 * colors.
	 *
	 * @param color1
	 *            The first color
	 * @param color2
	 *            The second color
	 * @param midPoint
	 *            The offset between color 1 and color 2, a value of 0.0 is
	 *            color 1 and 1.0 is color 2;
	 * @return the ARGB value for a new color based on this derivation
	 */
	public static int deriveARGB(Color color1, Color color2, float midPoint) {
		int r = color1.getRed() + (int) (((color2.getRed() - color1.getRed()) * midPoint) + 0.5f);
		int g = color1.getGreen() + (int) (((color2.getGreen() - color1.getGreen()) * midPoint) + 0.5f);
		int b = color1.getBlue() + (int) (((color2.getBlue() - color1.getBlue()) * midPoint) + 0.5f);
		int a = color1.getAlpha() + (int) (((color2.getAlpha() - color1.getAlpha()) * midPoint) + 0.5f);
		return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
	}

	public int intValue(double double_) {
		return ((Number) new Double(double_)).intValue();
	}
}
