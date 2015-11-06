package com.ontimize.plaf.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Shape;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.ontimize.plaf.painter.AbstractRegionPainter;
import com.ontimize.plaf.painter.ORowPanelPainter;
import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.utils.GradientDirection;
import com.ontimize.plaf.utils.LinearGradient;

public class OLoweredBorder extends AbstractRegionPainter implements Border {

	public static int DEFAULT_IMG_SIZE = 30;
	public static int DEFAULT_RADIUS = 13;
	public static Insets DEFAULT_INSETS = new Insets(10, 10, 10, 10);

	protected PaintContext paintContext;
	protected int radius = DEFAULT_RADIUS;
	protected Insets insets = DEFAULT_INSETS;
	
	public OLoweredBorder(){
		paintContext = new PaintContext(DEFAULT_INSETS,
				new Dimension(DEFAULT_IMG_SIZE, DEFAULT_IMG_SIZE), false,
				PaintContext.CacheMode.NO_CACHING, Integer.MAX_VALUE,
				Integer.MAX_VALUE);
	}
	
	public OLoweredBorder(Insets insets, Integer titleSize, Integer radius){
		this.insets = insets!=null ? insets : DEFAULT_INSETS;
		int size = titleSize!=null ? ((Integer)titleSize).intValue() : DEFAULT_IMG_SIZE;
		this.radius = radius!=null ? ((Integer)radius).intValue() : DEFAULT_RADIUS;
		
		paintContext = new PaintContext( this.insets,
				new Dimension(size, size), false,
				PaintContext.CacheMode.NO_CACHING, Integer.MAX_VALUE,
				Integer.MAX_VALUE);
	}
	
	
	// =========================================================================
	// Painter Methods

	@Override
	protected Object[] getExtendedCacheKeys(JComponent c) {
		return new Object[] { c.getBackground() };
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
	 * <p/>
	 * <p>
	 * This method allows for subclasses to package the painting of different
	 * states with possibly different canvas sizes, etc, into one
	 * AbstractRegionPainter implementation.
	 * </p>
	 * 
	 * @return a PaintContext associated with this paint operation.
	 */
	protected PaintContext getPaintContext() {
		return paintContext;
	}

	// =========================================================================
	// Border Methods

	/**
	 * Returns the insets of the border.
	 * 
	 * @param c
	 *            the component for which this border insets value applies
	 */
	public Insets getBorderInsets(Component c) {
		return insets;
	}

	/**
	 * Returns whether or not the border is opaque. If the border is opaque, it
	 * is responsible for filling in it's own background when painting.
	 */
	public boolean isBorderOpaque() {
		return false;
	}

	/**
	 * Paints the border for the specified component with the specified position
	 * and size.
	 * 
	 * @param c
	 *            the component for which this border is being painted
	 * @param g
	 *            the paint graphics
	 * @param x
	 *            the x position of the painted border
	 * @param y
	 *            the y position of the painted border
	 * @param width
	 *            the width of the painted border
	 * @param height
	 *            the height of the painted border
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		JComponent comp = (c instanceof JComponent) ? (JComponent) c : null;
		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			paint(g2, comp, width, height);
		}
	}

	protected void doPaint(Graphics2D g, JComponent c, int width, int height,
			Object[] extendedCacheKeys) {

		if(hasBgPaint(c)){
			paintGradient_(g, c, width, height, extendedCacheKeys);
		}else{
			paint_(g, c, width, height, extendedCacheKeys);
		}
	}
	
	protected void paint_(Graphics2D g, JComponent c, int width, int height,
			Object[] extendedCacheKeys){
		width = c.getBounds().width;
		height = c.getBounds().height;
		Insets insets = ORowPanelPainter.getTitleBorderInsets(c.getBorder(), c);
		Color oldColor = g.getColor();

		g.setColor(c.getBackground());
		// TOP
		Shape shape = ShapeFactory.getInstace()
				.createSemiRoundRect(1, 1, width - 2, insets.top - 1, radius, radius, ShapeFactory.TOP_ORIENTATION);
		g.fill(shape);
		// LEFT
		//g.fillRect(1, insets.top , insets.left -1, height - insets.top - insets.bottom );
		shape = ShapeFactory.getInstace().createRectangle(1, insets.top , insets.left -1, height - insets.top - insets.bottom);
		g.fill(shape);
		// RIGHT
		//g.fillRect(width-insets.right , insets.top, insets.right - 1,height - insets.top - insets.bottom );
		shape = ShapeFactory.getInstace().createRectangle(width-insets.right , insets.top, insets.right - 1,height - insets.top - insets.bottom);
		g.fill(shape);
		// BOTTOM
		shape = ShapeFactory.getInstace().createSemiRoundRect(1, height - insets.bottom , width - 2, insets.bottom - 1, radius,
				radius, ShapeFactory.BOTTOM_ORIENTATION);
		g.fill(shape);
		g.setColor(oldColor);
	}
	
	
	protected void paintGradient_(Graphics2D g, JComponent c, int width, int height,
			Object[] extendedCacheKeys){
		width = c.getBounds().width;
		height = c.getBounds().height;
		Color oldColor = g.getColor();

		LinearGradient lg = (LinearGradient)UIManager.getLookAndFeelDefaults().get("\""+c.getName()+"\".bgpaint");
		
		// TOP
		Shape shape = getTopShape(c,lg);
		g.setPaint(getTopBackgroundPaint(lg, shape));
		g.fill(shape);
		// LEFT
		shape = getLeftShape(c, lg);
		g.setPaint(getLeftBackgroundPaint(lg, shape));
		g.fill(shape);
		// RIGHT
		shape = getRightShape(c, lg);
		g.setPaint(getRightBackgroundPaint(lg, shape));
		g.fill(shape);
		// BOTTOM
		shape = getBottomShape(c, lg);
		g.setPaint(getBottomBackgroundPaint(lg, shape));
		g.fill(shape);
		g.setColor(oldColor);
	}
	
	protected Shape getTopShape(JComponent c, LinearGradient lg){
		int width = c.getBounds().width;
		Insets insets = ORowPanelPainter.getTitleBorderInsets(c.getBorder(), c);
		
		Shape shape = null;
		if(lg!=null && GradientDirection.HORIZONTAL_DIRECTION == lg.getDirectionType()){
			shape = ShapeFactory.getInstace().createRectangle(insets.left, 1, width - insets.left -insets.right, insets.top - 1);
		}else{
			shape = ShapeFactory.getInstace()
			.createSemiRoundRect(1, 1, width - 2, insets.top - 1, radius, radius, ShapeFactory.TOP_ORIENTATION);
		}
		return shape;
	}
	
	protected Shape getBottomShape(JComponent c, LinearGradient lg){
		int width = c.getBounds().width;
		int height = c.getBounds().height;
		Insets insets = ORowPanelPainter.getTitleBorderInsets(c.getBorder(), c);
		
		Shape shape = null;
		if(lg!=null && GradientDirection.HORIZONTAL_DIRECTION == lg.getDirectionType()){
			shape = ShapeFactory.getInstace().createRectangle(insets.left, height - insets.bottom, width - insets.left -insets.right, insets.bottom - 1);
		}else{
			shape = ShapeFactory.getInstace().createSemiRoundRect(1, height - insets.bottom , width - 2, insets.bottom - 1, radius,
					radius, ShapeFactory.BOTTOM_ORIENTATION);
		}
		return shape;
	}
	
	protected Shape getLeftShape(JComponent c, LinearGradient lg){
		int height = c.getBounds().height;
		Insets insets = ORowPanelPainter.getTitleBorderInsets(c.getBorder(), c);
		
		Shape shape = null;
		if(lg!=null && GradientDirection.HORIZONTAL_DIRECTION == lg.getDirectionType()){
			shape = ShapeFactory.getInstace().createSemiRoundRect(1, 1, insets.left-1, height-2, 
					radius, radius, ShapeFactory.LEFT_ORIENTATION);
		}else{
			shape = ShapeFactory.getInstace().createRectangle(1, insets.top , insets.left -1, height - insets.top - insets.bottom);
		}
		return shape;
	}
	
	protected Shape getRightShape(JComponent c, LinearGradient lg){
		int width = c.getBounds().width;
		int height = c.getBounds().height;
		Insets insets = ORowPanelPainter.getTitleBorderInsets(c.getBorder(), c);
		
		Shape shape = null;
		if(lg!=null && GradientDirection.HORIZONTAL_DIRECTION == lg.getDirectionType()){
			shape = ShapeFactory.getInstace().createSemiRoundRect(width-insets.right, 1, insets.right-1, height-2, 
					radius, radius, ShapeFactory.RIGHT_ORIENTATION);
		}else{
			shape = ShapeFactory.getInstace().createRectangle(width-insets.right , insets.top, insets.right - 1,height - insets.top - insets.bottom);
		}
		return shape;
	}
	
	protected Paint getTopBackgroundPaint(LinearGradient lg, Shape topShape){
		if(lg!=null && topShape!=null){
			return lg.getTopPaint(topShape.getBounds());
		}
		return null;
	}
	
	protected Paint getBottomBackgroundPaint(LinearGradient lg, Shape bottomShape){
		if(lg!=null && bottomShape!=null){
			return lg.getBottomPaint(bottomShape.getBounds());
		}
		return null;
	}
	
	protected Paint getLeftBackgroundPaint(LinearGradient lg, Shape leftShape){
		if(lg!=null && leftShape!=null){
			return lg.getLeftPaint(leftShape.getBounds());
		}
		return null;
	}
	
	protected Paint getRightBackgroundPaint(LinearGradient lg, Shape rightShape){
		if(lg!=null && rightShape!=null){
			return lg.getRightPaint(rightShape.getBounds());
		}
		return null;
	}
	
	protected boolean hasBgPaint(JComponent c){
		if(c!=null){
			Object obj = UIManager.getLookAndFeelDefaults().get("\""+c.getName()+"\".bgpaint");
			if(obj instanceof LinearGradient){
				return true;
			}
		}
		return false;
	}
	
	
	
	public static void clearReferences(){
	}
}
