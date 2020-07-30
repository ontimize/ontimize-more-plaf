package com.ontimize.plaf.utils;

import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.util.ArrayList;

/**
 *
 *
 * <linear-gradient> = linear-gradient( [ [ <angle> | to <side-or-corner> ] ,]? <color-stop>[,
 * <color-stop>]+ )
 *
 * <side-or-corner> = [left | right] || [top | bottom]
 *
 * <angle> = xdeg
 *
 * @author Imatia S.L.
 *
 */

public class LinearGradient implements Paint {

    protected String direction;

    protected GradientDirection gDirection;

    protected ArrayList<Color> colors = new ArrayList<Color>();

    protected ArrayList<Float> stops = new ArrayList<Float>();

    public LinearGradient() {

    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public GradientDirection getgDirection() {
        return gDirection;
    }

    public void setgDirection(GradientDirection gDirection) {
        this.gDirection = gDirection;
    }

    public int getDirectionType() {
        return this.gDirection.getDirectionType();
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    public Color[] getColorsArray() {
        Color[] colorArray = new Color[colors.size()];

        for (int i = 0; i < colors.size(); i++) {
            Color c = colors.get(i);
            colorArray[i] = (c != null ? c : null);
        }
        return colorArray;
    }

    public void setColors(ArrayList<Color> colors) {
        this.colors = colors;
    }

    public ArrayList<Float> getStops() {
        return stops;
    }

    public float[] getStopsArray() {
        float[] floatArray = new float[stops.size()];

        for (int i = 0; i < stops.size(); i++) {
            Float f = stops.get(i);
            floatArray[i] = (f != null ? f.floatValue() : Float.NaN);
        }
        return floatArray;
    }

    public void setStops(ArrayList<Float> stops) {
        this.stops = stops;
    }

    public void addColor(Color color) {
        this.colors.add(color);
    }

    public void addStop(Float stop) {
        this.stops.add(stop);
    }

    public Paint getTopPaint(Rectangle bounds) {
        if (this.gDirection != null) {
            return this.gDirection.getTopPaint(getColorsArray(), getStopsArray(), bounds);
        }
        return null;
    }

    public Paint getBottomPaint(Rectangle bounds) {
        if (this.gDirection != null) {
            return this.gDirection.getBottomPaint(getColorsArray(), getStopsArray(), bounds);
        }
        return null;
    }

    public Paint getLeftPaint(Rectangle bounds) {
        if (this.gDirection != null) {
            return this.gDirection.getLeftPaint(getColorsArray(), getStopsArray(), bounds);
        }
        return null;
    }

    public Paint getRightPaint(Rectangle bounds) {
        if (this.gDirection != null) {
            return this.gDirection.getRightPaint(getColorsArray(), getStopsArray(), bounds);
        }
        return null;
    }


    @Override
    public int getTransparency() {
        return 0;
    }

    @Override
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds,
            AffineTransform xform, RenderingHints hints) {
        return null;
    }

}
