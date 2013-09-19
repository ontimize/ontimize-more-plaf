package com.ontimize.plaf.painter.util;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

/**
 * Return various shapes used by the Painter classes.
 *
 * @author Imatia Innovation
 */
public class ShapeFactory {

	protected static ShapeFactory factory=new ShapeFactory();
	
	static public ShapeFactory getInstace(){
		return factory;
	}
	
    /** Used for generic shapes. */
    protected Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);

    /** Used for simple elliptical or circular shapes. */
    protected Ellipse2D ellipse = new Ellipse2D.Float();

    /**
     * Return a path for a rectangle with square corners.
     *
     * @param  x the X coordinate of the upper-left corner of the rectangle
     * @param  y the Y coordinate of the upper-left corner of the rectangle
     * @param  w the width of the rectangle
     * @param  h the height of the rectangle
     *
     * @return a path representing the shape.
     */
    public Shape createRectangle(final double x, final double y, final double w, final double h) {
    	path.reset();
		
		//left top corner.
		path.moveTo(x , y);
		//left bottom corner
		path.lineTo(x, y+h);

		//right top corner
		path.lineTo(x+w, y+h);
		
		//right bottom corner
		path.lineTo(x+w, y);
		
		path.lineTo(x, y);
		
		return path;
	}
    

    
    public static final int TOP_LEFT_CORNER = 0;
    public static final int TOP_RIGHT_CORNER = 1;
    public static final int BOTTOM_LEFT_CORNER = 2;
    public static final int BOTTOM_RIGHT_CORNER = 3;
    
    
    public Shape createRoundCorner( double x, double y, double w, double h, int cornerLocation, boolean open) {
		
		path.reset();
		switch (cornerLocation) {
		case TOP_LEFT_CORNER:
			path.moveTo(x + w, y);
			path.curveTo(x + w / 2.0, y, x, y + h / 2.0, x, y+ h);
			if (!open) {
				path.lineTo(x + w, y + h);
				path.closePath();
			}
			break;
		case TOP_RIGHT_CORNER:
			path.moveTo(x-w, y);
			path.curveTo(x-w/2.0,y, x, y+h/2.0, x, y+h);
			if (!open) {
				path.lineTo(x - w, y + h);
				path.closePath();
			}
			break;
		case BOTTOM_LEFT_CORNER:
			path.moveTo(x, y-h);
			path.curveTo(x,y- h/2.0, x+w/2.0, y, x+w, y);
			if (!open) {
				path.lineTo(x + w, y - h);
				path.closePath();
			}
			break;
		case BOTTOM_RIGHT_CORNER:
			path.moveTo(x-w,y);
			path.curveTo(x-w/2.0,y, x, y-h/2.0, x, y-h);
			if (!open) {
				path.lineTo(x - w, y - h);
				path.closePath();
			}
			break;
		default:
			break;
		}
		
		return path;
	}
    
    
    public static final int CLOCKWISE = 0;
	public static final int ANTICLOCKWISE = 1;
	
	/**
	 * 
	 * @param x X coordinate of semicircle init.
	 * @param y Y coordinate of semicircle init.
	 * @param h height Height of the field.
	 * @param wise Clockwise or anticlockwise
	 * @param open determines whether the path is closed or not.
	 * @return
	 */
	public Shape createSemiCircle(double x, double y, double h,
			int wise, boolean open) {
		path.reset();
		
		double radius = h/2.0;
		
		switch (wise) {
		case ANTICLOCKWISE:
			path.moveTo(x , y);
			path.curveTo(x - radius/2.0, y, x -radius, y + radius/ 2.0, x -radius, y+ radius);
			path.curveTo(x - radius,y+ h- radius/2.0, x-radius/2.0, y+h, x, y+h);
			break;
		case CLOCKWISE:
			path.moveTo(x, y);
			path.curveTo(x+radius/2.0,y, x+radius, y+radius/2.0, x+radius, y+radius);
			path.curveTo(x+radius,y+h-radius/2.0, x+radius/2.0, y+h, x, y+h);
			break;

		}
		
		if(!open){
			path.closePath();
		}
		
		return path;
	}
	
	/**
	 * 
	 * @param x X coordinate of semicircle init.
	 * @param y Y coordinate of semicircle init.
	 * @param w height Height of the field.
	 * @param wise Clockwise or anticlockwise
	 * @param open determines whether the path is closed or not.
	 * @return
	 */
	public Shape createSemiCircleVertical(double x, double y, double w,
			int wise, boolean open) {
		path.reset();
		
		double radius = w/2.0;
		
		switch (wise) {
		case CLOCKWISE:
			path.moveTo(x , y);
			path.curveTo(x , y- radius/2.0, x + radius/ 2.0, y -radius , x + radius, y- radius);
			path.curveTo(x + w - radius/2.0 ,y - radius, x + w, y - radius/2.0, x+w, y);
			break;
		case ANTICLOCKWISE:
			path.moveTo(x, y);
			path.curveTo(x,y + radius/2.0, x+radius/2.0, y+radius, x+radius, y+radius);
			path.curveTo(x+w-radius/2.0,y+radius, x+w, y+radius/2.0, x+w, y+w);
			break;

		}
		
		if(!open){
			path.closePath();
		}
		return path;
	}
	
	public static final int TOP_ORIENTATION = 0;
	public static final int BOTTOM_ORIENTATION = 1;
	public static final int LEFT_ORIENTATION = 2;
	public static final int RIGHT_ORIENTATION = 3;
	
	public Shape createSemiRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight, int orientation){
		path.reset();
		switch (orientation) {
		case TOP_ORIENTATION:
			path.moveTo(x, y+h);
			path.lineTo(x, y+ arcHeight);
			path.quadTo(x, y, x + arcWidth, y);
			
			path.lineTo(x + w - arcWidth , y);
			path.quadTo(x + w, y, x + w, y + arcHeight);
			path.lineTo(x+w, y+h);
			path.closePath();
			break;
		case BOTTOM_ORIENTATION:
			path.moveTo(x, y);
			path.lineTo(x, y + h - arcHeight);
			path.quadTo(x, y + h, x + arcWidth, y + h);
			
			path.lineTo(x + w - arcWidth , y + h);
			path.quadTo(x + w, y + h , x + w, y + h - arcHeight);
			path.lineTo(x+w, y);
			path.closePath();
			break;
		case LEFT_ORIENTATION:
			path.moveTo(x + w, y);
			path.lineTo(x + arcWidth, y);
			path.quadTo(x, y, x , y + arcHeight);
			
			path.lineTo(x , y + h - arcHeight);
			path.quadTo(x, y + h , x + arcWidth, y + h);
			path.lineTo(x+w, y + h);
			path.closePath();
			break;
		case RIGHT_ORIENTATION:
			path.moveTo(x , y);
			path.lineTo(x +w - arcWidth, y);
			path.quadTo(x + w, y, x + w , y + arcHeight);
			
			path.lineTo(x +w , y + h - arcHeight);
			path.quadTo(x +w, y + h , x +w - arcWidth, y + h);
			path.lineTo(x, y + h);
			path.closePath();
			break;
		default:
			break;
		}
		return path;
		
	}
    
	public static void clearReferences(){
		factory = null;
	}
	
}
