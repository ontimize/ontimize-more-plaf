package com.ontimize.plaf.utils;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;

public class GradientDirection {

    public static final String LEFT_STR = "left";

    public static final String RIGHT_STR = "right";

    public static final String TOP_STR = "top";

    public static final String BOTTOM_STR = "bottom";


    public static final int VERTICAL_DIRECTION = 1;

    public static final int HORIZONTAL_DIRECTION = 2;


    public static String defaultDirection = "to bottom";

    protected String strDirection;

    protected int directionType;

    protected Direction direction;

    public GradientDirection(String direction) {
        this.strDirection = direction;
        if (direction == null || "".equals(direction))
            this.strDirection = defaultDirection;

        parseDirection(strDirection);

    }

    protected void parseDirection(String direction) {
        if ("to right".equalsIgnoreCase(direction)) {
            this.direction = new ToRight();
            this.directionType = HORIZONTAL_DIRECTION;
        } else if ("to left".equalsIgnoreCase(direction)) {
            this.direction = new ToLeft();
            this.directionType = HORIZONTAL_DIRECTION;
        } else if ("to top".equalsIgnoreCase(direction)) {
            this.direction = new ToTop();
            this.directionType = VERTICAL_DIRECTION;
        } else if ("to bottom".equalsIgnoreCase(direction)) {
            this.direction = new ToBottom();
            this.directionType = VERTICAL_DIRECTION;
        } else {
            this.direction = new ToBottom();
        }
    }


    public int getStartX(Rectangle bounds) {
        return ((Number) this.direction.getStartX(bounds)).intValue();
    }

    public int getStartY(Rectangle bounds) {
        return ((Number) this.direction.getStartY(bounds)).intValue();
    }

    public int getEndX(Rectangle bounds) {
        return ((Number) this.direction.getEndX(bounds)).intValue();
    }

    public int getEndY(Rectangle bounds) {
        return ((Number) this.direction.getEndY(bounds)).intValue();
    }

    public Paint getTopPaint(Color[] colors, float[] stops, Rectangle bounds) {
        return this.direction.getTopPaint(colors, stops, bounds);
    }

    public Paint getBottomPaint(Color[] colors, float[] stops, Rectangle bounds) {
        return this.direction.getBottomPaint(colors, stops, bounds);
    }

    public Paint getLeftPaint(Color[] colors, float[] stops, Rectangle bounds) {
        return this.direction.getLeftPaint(colors, stops, bounds);
    }

    public Paint getRightPaint(Color[] colors, float[] stops, Rectangle bounds) {
        return this.direction.getRightPaint(colors, stops, bounds);
    }


    public int getDirectionType() {
        return directionType;
    }


    public abstract class Direction {

        public Direction() {
        }

        public abstract int getStartX(Rectangle bounds);

        public abstract int getStartY(Rectangle bounds);

        public abstract int getEndX(Rectangle bounds);

        public abstract int getEndY(Rectangle bounds);

        public abstract Paint getTopPaint(Color[] colors, float[] stops, Rectangle bounds);

        public abstract Paint getBottomPaint(Color[] colors, float[] stops, Rectangle bounds);

        public abstract Paint getLeftPaint(Color[] colors, float[] stops, Rectangle bounds);

        public abstract Paint getRightPaint(Color[] colors, float[] stops, Rectangle bounds);

    }

    public class ToRight extends Direction {

        public ToRight() {
        }

        @Override
        public int getStartX(Rectangle bounds) {
            return ((Number) bounds.getX()).intValue();
        }

        @Override
        public int getStartY(Rectangle bounds) {
            return ((Number) bounds.getY()).intValue();
        }

        @Override
        public int getEndX(Rectangle bounds) {
            double endX = bounds.getX() + bounds.getWidth();
            return ((Number) endX).intValue();
        }

        @Override
        public int getEndY(Rectangle bounds) {
            return ((Number) bounds.getY()).intValue();
        }

        @Override
        public Paint getTopPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return new LinearGradientPaint(getStartX(bounds), getStartY(bounds),
                    getEndX(bounds), getEndY(bounds),
                    stops, colors);
        }

        @Override
        public Paint getBottomPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return new LinearGradientPaint(getStartX(bounds), getStartY(bounds),
                    getEndX(bounds), getEndY(bounds),
                    stops, colors);
        }

        @Override
        public Paint getLeftPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return colors[0];
        }

        @Override
        public Paint getRightPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return colors[colors.length - 1];
        }

    }

    public class ToLeft extends Direction {

        public ToLeft() {
        }

        @Override
        public int getStartX(Rectangle bounds) {
            double startX = bounds.getX() + bounds.getWidth();
            return ((Number) startX).intValue();
        }

        @Override
        public int getStartY(Rectangle bounds) {
            return ((Number) bounds.getY()).intValue();
        }

        @Override
        public int getEndX(Rectangle bounds) {
            return ((Number) bounds.getX()).intValue();
        }

        @Override
        public int getEndY(Rectangle bounds) {
            return ((Number) bounds.getY()).intValue();
        }

        @Override
        public Paint getTopPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return new LinearGradientPaint(getStartX(bounds), getStartY(bounds),
                    getEndX(bounds), getEndY(bounds),
                    stops, colors);
        }

        @Override
        public Paint getBottomPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return new LinearGradientPaint(getStartX(bounds), getStartY(bounds),
                    getEndX(bounds), getEndY(bounds),
                    stops, colors);
        }

        @Override
        public Paint getLeftPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return colors[colors.length - 1];
        }

        @Override
        public Paint getRightPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return colors[0];
        }

    }

    public class ToTop extends Direction {

        public ToTop() {
        }

        @Override
        public int getStartX(Rectangle bounds) {
            return ((Number) bounds.getX()).intValue();
        }

        @Override
        public int getStartY(Rectangle bounds) {
            double startY = bounds.getY() + bounds.getHeight();
            return ((Number) startY).intValue();
        }

        @Override
        public int getEndX(Rectangle bounds) {
            return ((Number) bounds.getX()).intValue();
        }

        @Override
        public int getEndY(Rectangle bounds) {
            return ((Number) bounds.getY()).intValue();
        }

        @Override
        public Paint getTopPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return colors[colors.length - 1];
        }

        @Override
        public Paint getBottomPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return colors[0];
        }

        @Override
        public Paint getLeftPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return new LinearGradientPaint(getStartX(bounds), getStartY(bounds),
                    getEndX(bounds), getEndY(bounds),
                    stops, colors);
        }

        @Override
        public Paint getRightPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return new LinearGradientPaint(getStartX(bounds), getStartY(bounds),
                    getEndX(bounds), getEndY(bounds),
                    stops, colors);
        }

    }

    public class ToBottom extends Direction {

        public ToBottom() {
        }

        @Override
        public int getStartX(Rectangle bounds) {
            return ((Number) bounds.getX()).intValue();
        }

        @Override
        public int getStartY(Rectangle bounds) {
            return ((Number) bounds.getY()).intValue();
        }

        @Override
        public int getEndX(Rectangle bounds) {
            return ((Number) bounds.getX()).intValue();
        }

        @Override
        public int getEndY(Rectangle bounds) {
            double endY = bounds.getY() + bounds.getHeight();
            return ((Number) endY).intValue();
        }

        @Override
        public Paint getTopPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return colors[0];
        }

        @Override
        public Paint getBottomPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return colors[colors.length - 1];
        }

        @Override
        public Paint getLeftPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return new LinearGradientPaint(getStartX(bounds), getStartY(bounds),
                    getEndX(bounds), getEndY(bounds),
                    stops, colors);
        }

        @Override
        public Paint getRightPaint(Color[] colors, float[] stops, Rectangle bounds) {
            return new LinearGradientPaint(getStartX(bounds), getStartY(bounds),
                    getEndX(bounds), getEndY(bounds),
                    stops, colors);
        }

    }

}
