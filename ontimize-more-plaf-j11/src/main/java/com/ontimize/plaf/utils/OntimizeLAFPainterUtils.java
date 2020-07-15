package com.ontimize.plaf.utils;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class contains Painter utils for the Ontimize Look And Feel.
 *
 * @author Imatia Innovation
 *
 */
public class OntimizeLAFPainterUtils {

    private static final Logger logger = LoggerFactory.getLogger(OntimizeLAFPainterUtils.class);

    public static final String HORIZONTAL_DEGRADATED = "HORIZONTAL_DEGRADATED";

    public static final String VERTICAL_DEGRADATED = "VERTICAL_DEGRADATED";

    public static final String ASCENT_TRANSVERSAL_DEGRADATED = "ASCENT_TRANSVERSAL_DEGRADATED";

    public static final String DESCENT_TRANSVERSAL_DEGRADATED = "DESCENT_TRANSVERSAL_DEGRADATED";

    /**
     * evaluates when the property is an image (that is, a texture) or when a gradientPainter or a
     * single color in rgb or hexadecimal with or without alpha, returning a paint according to that.
     * (TexturePainter, gradientPainter or Color)
     * @param s
     * @param prop
     * @return
     */
    public static Paint getPaint(Shape s, String prop) {

        Paint paint = null;
        if ((s != null) && (prop != null)) {

            // if the prop is a texture:
            if (prop.contains("/") || prop.contains("\\")) {
                paint = OntimizeLAFPainterUtils.decodeTexturePaint(s, prop, null);
            } else if (prop.contains("¬")) {
                // if the prop is painter:
                paint = OntimizeLAFPainterUtils.decodeGradient(s, prop);
            } else {
                // if the prop is a single Color
                paint = OntimizeLAFParseUtils.parseColor(prop, null);
            }

            if (paint == null) {
                throw new IllegalArgumentException(
                        "  OntimizeLAFPainterUtils  -> A texture, color or painter is required");
            }
        } else {
            throw new NullPointerException(
                    "  OntimizeLAFPainterUtils  -> Shape and prop must be not null to configure a painter");
        }

        return paint;
    }

    /**
     * This method converts the values of these painters in the Ontimize Default Configuration syntax
     * for painters (that is, in the way that the Ontimize L&F objects are both cached and configured in
     * the OLAFDefaultConfiguration.properties)
     *
     * That is, float[] fractions (splitted by ~) ¬ Color[] Color (splitted by ~ and valid with alpha) ¬
     * CycleMethod NO_CYCLE, REFLECT o REPEAT + Direction for the degradate ¬ Points
     *
     * See manual or configuration file for more information
     * @param fractions
     * @param colors
     */
    public static String painterToString(float[] fractions, Color[] colors) {

        return OntimizeLAFPainterUtils.painterToString(fractions, colors, null, null, -1.0f, -1.0f, -1.0f, -1.0f);

    }

    /**
     * This method converts the values of these painters in the Ontimize Default Configuration syntax
     * for painters (that is, in the way that the Ontimize L&F objects are both cached and configured in
     * the OLAFDefaultConfiguration.properties)
     *
     * That is, float[] fractions (splitted by ~) ¬ Color[] Color (splitted by ~ and valid with alpha) ¬
     * CycleMethod NO_CYCLE, REFLECT o REPEAT + Direction for the degradate ¬ Points
     *
     * See manual or configuration file for more information
     * @param fractions
     * @param colors
     */
    public static String painterToString(float[] fractions, Color[] colors, String direction) {

        return OntimizeLAFPainterUtils.painterToString(fractions, colors, null, direction, -1.0f, -1.0f, -1.0f, -1.0f);

    }

    /**
     * This method converts the values of these painters in the Ontimize Default Configuration syntax
     * for painters (that is, in the way that the Ontimize L&F objects are both cached and configured in
     * the OLAFDefaultConfiguration.properties)
     *
     * That is, float[] fractions (splitted by ~) ¬ Color[] Color (splitted by ~ and valid with alpha) ¬
     * CycleMethod NO_CYCLE, REFLECT o REPEAT + Direction for the degradate ¬ Points
     *
     * See manual or configuration file for more information
     * @param fractions
     * @param colors
     */
    public static String painterToString(float[] fractions, Color[] colors, CycleMethod cycleMethod, String direction,
            float x, float y, float w, float h) {

        String result = "";

        // to convert a single Color to an String:
        if ((fractions == null) || ((fractions != null) && (fractions.length < 1))) {
            if ((colors != null) && (colors.length == 1)) {
                return OntimizeLAFColorUtils.colorToString(colors[0], null);
            }
        }

        // to convert a whole painter to an String:
        if ((fractions != null) && (fractions.length > 0) && (colors != null) && (colors.length > 0)) {

            if (fractions.length != colors.length) {
                throw new IllegalArgumentException(
                        "  OntimizeLAFPainterUtils  ->  Colors and fractions must have equal size");
            }

            for (int i = 0; i < fractions.length; i++) {
                if (i > 0) {
                    result = result.concat("~");
                }
                result = result.concat(String.valueOf(fractions[i]));
            }
            result = result.concat("¬");
            boolean filled = false;
            for (int i = 0; i < colors.length; i++) {
                if (filled) {
                    result = result.concat("~");
                }
                String sColor = OntimizeLAFColorUtils.colorToString(colors[i], null);
                if (sColor != null) {
                    result = result.concat(sColor);
                    filled = true;
                }
            }
            if (cycleMethod != null) {
                result = result.concat("¬");
                result = result.concat(cycleMethod.name());
            }
            if (direction != null) {
                result = result.concat("¬");
                result = result.concat(direction);
            }
            if ((x != -1.0f) && (y != -1.0f) && (w != -1.0f) && (h != -1.0f)) {
                result = result.concat("¬");
                result = result.concat(OntimizeLAFPainterUtils.pointsToString(x, y, w, h));
            }
        } else {
            throw new IllegalArgumentException(
                    "  OntimizeLAFPainterUtils  ->  Invalid number of parameters to call painterToString method");
        }

        return result;
    }

    /**
     * This method converts the received float values in the Ontimize Default Configuration syntax (that
     * is, in the way that the Ontimize L&F objects are both cached and configured in the
     * OLAFDefaultConfiguration.properties)
     *
     * That is, x~y~w~h
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public static String pointsToString(float x, float y, float w, float h) {

        String result = "";
        result = result.concat(String.valueOf(x));
        result = result.concat("~");
        result = result.concat(String.valueOf(y));
        result = result.concat("~");
        result = result.concat(String.valueOf(w));
        result = result.concat("~");
        result = result.concat(String.valueOf(h));

        return result;
    }

    /**
     * Decode the variable painter, in order to be able to call the same-named method with values in all
     * the calling-parameters .
     * @param s
     * @param painter
     * @return
     */
    public static Paint decodeGradient(Shape s, String painter) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        painter = OntimizeLAFPainterUtils.painterRemovePoints(painter);
        String painterWithPoints = painter + "¬" + OntimizeLAFPainterUtils.pointsToString(x, y, w, h);
        return OntimizeLAFPainterUtils.getGradientPaint(painterWithPoints, null);

    }

    /**
     * Parse the encoded string named painter in order to create a new painter.
     *
     * The syntax of that string is based on several string parameters splitted by ¬. Those parameters
     * are: fractions¬colors¬CycleMethod¬Direction¬Points (optionally)
     *
     * FRACTIONS: They indicates the percentage of the area which is filled by each color. Float numbers
     * (in the range 0 to 1) splitted by ~ are required. For instance: 0.0f~0.5f~1.0f COLOURS: They
     * indicate the colors to fill the areas between fractions. Colors in hexadecimal or R;G;B (with our
     * without alpha) splitted by ~ are required. For instance:
     * 158;191;230;50~111;161;217;50~99;132;169;50 CycleMethod: Has one of the next values: By default,
     * CycleMethod.NO_CYCLE. NO_CYCLE: Use the terminal colors to fill the remaining area. REFLECT:
     * Cycle the gradient colors start-to-end, end-to-start to fill the remaining area. REPEAT: Cycle
     * the gradient colors start-to-end, start-to-end to fill the remaining area. DIRECTION: Indicates
     * the direction in which run the parallel bands of color that determines the gradient: By default,
     * HORIZONTAL_DEGRADATED. HORIZONTAL_DEGRADATED: For horizontal parallel bands (0 degrees).
     * VERTICAL_DEGRADATED: For vertical parallel bands (90 degrees). ASCENT_TRANSVERSAL_DEGRADATED: For
     * transversal parallel bands in ascent way (45 degrees). DESCENT_TRANSVERSAL_DEGRADATED: For
     * transversal parallel bands in descent way (135 degrees). POINTS: They indicate the start point,
     * end point, width and heigt. Float numbers splitted by ~. Optional. If they are not indicated the
     * L&F fill them.
     * @param painter
     * @param defaultValue
     * @return
     */
    public static MultipleGradientPaint getGradientPaint(String painter, MultipleGradientPaint defaultValue) {

        // checking if the received painter String has value:
        if ((painter == null) || "".equals(painter)) {
            return defaultValue;
        }

        // checking if the received painter String is cached:
        if (OntimizeLAFParseUtils.olafCache.containsKey(painter)) {
            return (MultipleGradientPaint) OntimizeLAFParseUtils.olafCache.get(painter);
        }

        // Parsing the received painter String to get a painter Object and
        // caching and returning it:
        MultipleGradientPaint result = null;
        String pointsParam = null;
        StringTokenizer st = new StringTokenizer(painter, "¬");
        if (st != null) {
            if ((st.countTokens() < 2) || (st.countTokens() > 5)) {
                throw new IllegalArgumentException("Invalid number of parameters to configure a painter");
            }
            String sFractions = st.nextToken();
            String sColors = st.nextToken();
            String thirdParam = null, fourthParam = null, fifthParam = null;
            if (st.countTokens() >= 1) {
                thirdParam = st.nextToken(); // each time a token is taken, the
                // count is decrease one unit.
            }
            if (st.countTokens() >= 1) {
                fourthParam = st.nextToken();
            }
            if (st.countTokens() >= 1) {
                fifthParam = st.nextToken();
            }

            float[] fractions = OntimizeLAFParseUtils.parseFloatsArray(sFractions, null);
            Color[] colors = OntimizeLAFParseUtils.parseColorArray(sColors, null);
            // OJO CON ESTA SUPOSCIÓN SI EN UN FUTURO ALGUIÉN AÑADE MÁS
            // PARÁMETROS SEPARADOS CON ~ :
            pointsParam = (((fifthParam != null) && fifthParam.contains("~")) ? fifthParam
                    : ((fourthParam != null) && fourthParam.contains("~")) ? fourthParam
                            : ((thirdParam != null) && thirdParam.contains("~")) ? thirdParam : null);
            String[] otherParams = new String[] { thirdParam, fourthParam, fifthParam };

            // CycleMethod cycleMethod = null;
            // if (sCycleMethod!=null) {
            // String sCycleMethodToUpperCase = sCycleMethod.toUpperCase();
            // if (sCycleMethodToUpperCase.indexOf("NO_CYCLE")>=0) cycleMethod =
            // cycleMethod.NO_CYCLE;
            // else if (sCycleMethodToUpperCase.indexOf("REFLECT")>=0)
            // cycleMethod = cycleMethod.REFLECT;
            // else if (sCycleMethodToUpperCase.indexOf("REPEAT")>=0)
            // cycleMethod = cycleMethod.REPEAT;
            // else cycleMethod = cycleMethod.NO_CYCLE;
            // } else cycleMethod = cycleMethod.NO_CYCLE;
            // String direction = null;
            // if (sDirection!=null) {
            // String sDirectionToUpperCase = sDirection.toUpperCase();
            // if (sDirectionToUpperCase.indexOf("HORIZONTAL_DEGRADATED")>=0)
            // direction = OTabbedPaneTabPainter.HORIZONTAL_DEGRADATED;
            // else if (sDirectionToUpperCase.indexOf("VERTICAL_DEGRADATED")>=0)
            // direction = OTabbedPaneTabPainter.VERTICAL_DEGRADATED;
            // else if
            // (sDirectionToUpperCase.indexOf("ASCENT_TRANSVERSAL_DEGRADATED")>=0)
            // direction = OTabbedPaneTabPainter.ASCENT_TRANSVERSAL_DEGRADATED;
            // else if
            // (sDirectionToUpperCase.indexOf("DESCENT_TRANSVERSAL_DEGRADATED")>=0)
            // direction = OTabbedPaneTabPainter.DESCENT_TRANSVERSAL_DEGRADATED;
            // else direction = OTabbedPaneTabPainter.HORIZONTAL_DEGRADATED;
            // } else direction = OTabbedPaneTabPainter.HORIZONTAL_DEGRADATED;
            // Float start, end, width, heigh;

            CycleMethod cycleMethod = OntimizeLAFPainterUtils.getCycleMethod(otherParams);
            String direction = OntimizeLAFPainterUtils.getDirection(otherParams);

            float x = 0.0f, y = 0.0f, w = 10.0f, h = 10.0f; // default values if
            // they are not
            // indicated.
            if (pointsParam != null) {
                StringTokenizer stPoints = new StringTokenizer(pointsParam, "~");
                if (stPoints != null) {
                    if (stPoints.countTokens() != 4) {
                        throw new IllegalArgumentException("Invalid number of points to configure a painter");
                    }
                    try {
                        x = Float.valueOf(stPoints.nextToken().trim()).floatValue();
                    } catch (NumberFormatException nfe) {
                        x = 0.0f;
                    }
                    try {
                        y = Float.valueOf(stPoints.nextToken().trim()).floatValue();
                    } catch (NumberFormatException nfe) {
                        y = 0.0f;
                    }
                    try {
                        w = Float.valueOf(stPoints.nextToken().trim()).floatValue();
                    } catch (NumberFormatException nfe) {
                        w = 10.0f;
                    }
                    try {
                        h = Float.valueOf(stPoints.nextToken().trim()).floatValue();
                    } catch (NumberFormatException nfe) {
                        h = 10.0f;
                    }
                }
            }

            result = OntimizeLAFPainterUtils.decodeGradient(x, y, w, h, fractions, colors, direction);
        }

        if ((pointsParam != null) && (result != null)) {
            OntimizeLAFParseUtils.olafCache.put(painter, result); // stored only
            // if x, y, w
            // and h are
            // indicated
            // in the
            // cache key
            // (that is,
            // in the
            // painter
            // variable)
        }
        return result;
    }

    protected static CycleMethod getCycleMethod(String[] params) {

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                String param = params[i];
                if (param != null) {
                    String paramToUpperCase = param.toUpperCase();
                    if (paramToUpperCase.indexOf("NO_CYCLE") >= 0) {
                        return CycleMethod.NO_CYCLE;
                    } else if (paramToUpperCase.indexOf("REFLECT") >= 0) {
                        return CycleMethod.REFLECT;
                    } else if (paramToUpperCase.indexOf("REPEAT") >= 0) {
                        return CycleMethod.REPEAT;
                    }
                }
            }
        }

        // by default NO_CYCLE:
        return CycleMethod.NO_CYCLE;
    }

    protected static String getDirection(String[] params) {

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                String param = params[i];
                if (param != null) {
                    String paramToUpperCase = param.toUpperCase();
                    if (paramToUpperCase.indexOf("HORIZONTAL_DEGRADATED") >= 0) {
                        return OntimizeLAFPainterUtils.HORIZONTAL_DEGRADATED;
                    } else if (paramToUpperCase.indexOf("VERTICAL_DEGRADATED") >= 0) {
                        return OntimizeLAFPainterUtils.VERTICAL_DEGRADATED;
                    } else if (paramToUpperCase.indexOf("ASCENT_TRANSVERSAL_DEGRADATED") >= 0) {
                        return OntimizeLAFPainterUtils.ASCENT_TRANSVERSAL_DEGRADATED;
                    } else if (paramToUpperCase.indexOf("DESCENT_TRANSVERSAL_DEGRADATED") >= 0) {
                        return OntimizeLAFPainterUtils.DESCENT_TRANSVERSAL_DEGRADATED;
                    } else if ((param.indexOf("*") != -1) || (param.indexOf("(") != -1) || (param.indexOf(")") != -1)) {
                        return param; // for directions coded as forms
                    }
                }
            }
        }

        // by default HORIZONTAL_DEGRADATED:
        return OntimizeLAFPainterUtils.HORIZONTAL_DEGRADATED;
    }

    /**
     * Fill defaults CycleMethod value and calls the same-named method (see that javadoc for details)
     * @param x
     * @param y
     * @param w
     * @param h
     * @param fractions
     * @param colors
     * @param direction
     * @return
     */
    public static LinearGradientPaint decodeGradient(float x, float y, float w, float h, float[] fractions,
            Color[] colors, String direction) {
        return OntimizeLAFPainterUtils.decodeGradient(x, y, w, h, fractions, colors, direction, CycleMethod.NO_CYCLE);

    }

    /**
     * Create a LinearGradientPaint, indicating diferent "gradient lines" (line between x1,y1 and
     * x2,y2). The gradient smoothly shifts from one color to the other as you move along the line that
     * connects those two points (the gradient line). The GradientPaint creates parallel bands of color,
     * perpendicular to the gradient line.
     *
     * Several directions are allowed: HORIZONTAL_DEGRADATED: For horizontal parallel bands (0 degrees).
     * VERTICAL_DEGRADATED: For vertical parallel bands (90 degrees). ASCENT_TRANSVERSAL_DEGRADATED: For
     * transversal parallel bands in ascent way (45 degrees). DESCENT_TRANSVERSAL_DEGRADATED: For
     * transversal parallel bands in descent way (135 degrees). Custom directions: Custom gradient
     * direction can be specified by four functions or constants splitted by , and without whites
     * (spaces). Each function must indicate the coordinate x1, y1, x2 and y2(start and end points) as a
     * float constant value or a function depending on the start point and the width and height (coded
     * as x, y, w, and h). For example: (0.925f*w)+x,(0.9285714f*h)+y,(0.925f*w)+x,(0.004201681f*h)+y By
     * default, the HORIZONTAL_DEGRADATED gradient line is selected, that is, parallel horizontal bands
     * of colors.
     *
     * Several cycles method to paint are also permited: NO_CYCLE: Use the terminal colors to fill the
     * remaining area. REFLECT: Cycle the gradient colors start-to-end, end-to-start to fill the
     * remaining area. REPEAT: Cycle the gradient colors start-to-end, start-to-end to fill the
     * remaining area.
     * @param x : start horizontal point
     * @param y : start vertical point
     * @param w : width (horizontal)
     * @param h : height (vertical)
     * @param fractions : float array that indicates the percentage in which filling using each color
     * @param colors : color array that indicates the color used to fill the area between two
     *        consecutive fractions
     * @param direction : see above
     * @param cycleMethod : see above
     * @return
     */
    public static LinearGradientPaint decodeGradient(float x, float y, float w, float h, float[] fractions,
            Color[] colors, String direction, CycleMethod cycleMethod) {

        // by default CycleMethod.NO_CYCLE
        if (cycleMethod == null) {
            cycleMethod = CycleMethod.NO_CYCLE;
        }

        // by default return HORIZONTAL_DEGRADATED
        if ((direction == null) || "".equalsIgnoreCase(direction)) {
            direction = OntimizeLAFPainterUtils.HORIZONTAL_DEGRADATED;
        }

        if (OntimizeLAFPainterUtils.HORIZONTAL_DEGRADATED.equalsIgnoreCase(direction)) {
            return OntimizeLAFPainterUtils.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h)
                    + y, fractions, colors, cycleMethod);
        } else if (OntimizeLAFPainterUtils.VERTICAL_DEGRADATED.equalsIgnoreCase(direction)) {
            return OntimizeLAFPainterUtils.decodeGradient((0.0f * w) + x, (0.5f * h) + y, (1.0f * w) + x, (0.5f * h)
                    + y, fractions, colors, cycleMethod);
        } else if (OntimizeLAFPainterUtils.ASCENT_TRANSVERSAL_DEGRADATED.equalsIgnoreCase(direction)) {
            return OntimizeLAFPainterUtils.decodeGradient((0.0f * w) + x, (1.0f * h) + y, (1.0f * w) + x, (0.0f * h)
                    + y, fractions, colors, cycleMethod);
        } else if (OntimizeLAFPainterUtils.DESCENT_TRANSVERSAL_DEGRADATED.equalsIgnoreCase(direction)) {
            return OntimizeLAFPainterUtils.decodeGradient((0.0f * w) + x, (0.0f * h) + y, (1.0f * w) + x, (1.0f * h)
                    + y, fractions, colors, cycleMethod);
        } else {

            // also custom directions are allowed. (seldom, rarely used):
            // TODO: Improvements for more complex syntaxis.
            // Now this methos is coded to perform operations such as
            // (0.925f*w)+x,(0.9285714f*h)+y,(0.925f*w)+x,(0.004201681f*h)+y.
            // PErform more complex operations using mathematical libraries
            // (such us, the mesp, which are free) like in the
            // getCalculatedColumns() in the
            // ExtendedTableMode.java
            direction = direction.trim();
            StringTokenizer st = new StringTokenizer(direction, ",");
            if (st != null) {
                if (st.countTokens() != 4) {
                    throw new IllegalArgumentException("Invalid number of parameters to configure a painter");
                }
                String sx1 = st.nextToken().trim();
                String sy1 = st.nextToken().trim();
                String sx2 = st.nextToken().trim();
                String sy2 = st.nextToken().trim();
                float x1 = OntimizeLAFPainterUtils.calculate(sx1, x, y, w, h);
                float y1 = OntimizeLAFPainterUtils.calculate(sy1, x, y, w, h);
                float x2 = OntimizeLAFPainterUtils.calculate(sx2, x, y, w, h);
                float y2 = OntimizeLAFPainterUtils.calculate(sy2, x, y, w, h);

                return OntimizeLAFPainterUtils.decodeGradient(x1, y1, x2, y2, fractions, colors, cycleMethod);
            }
        }

        // by default return HORIZONTAL_DEGRADATED
        return OntimizeLAFPainterUtils.decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                fractions, colors, cycleMethod);
    }

    /**
     * This method performs the operation indicated by the input string (named operation), swapping the
     * constants indicated by x, y, w, h by the input values. A float value with the operation result is
     * returned.
     *
     * TODO: Improvements for more complex syntaxis. Now this methos is coded to perform operations such
     * as (0.925f*w)+x,(0.9285714f*h)+y,(0.925f*w)+x,(0.004201681f*h)+y. PErform more complex operations
     * using mathematical libraries (such us, the mesp, which are free) like in the
     * getCalculatedColumns() in the ExtendedTableMode.java
     * @param operation
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    protected static float calculate(String operation, float x, float y, float w, float h) {

        StringTokenizer st = new StringTokenizer(operation, "+");
        if (st != null) {
            if (st.countTokens() != 2) {
                throw new IllegalArgumentException(
                        "OntimizeLAFPainterUtils -> Invalid parameters to configure a direction");
            }
            String firstOperator = st.nextToken();
            String secondOperator = st.nextToken();

            float resultFirstOperator = 0;
            if (firstOperator != null) {
                firstOperator = OntimizeLAFPainterUtils.removeSubstring(firstOperator, "(");
                firstOperator = OntimizeLAFPainterUtils.removeSubstring(firstOperator, ")");
                firstOperator.trim();
                StringTokenizer stFirstOperator = new StringTokenizer(firstOperator, "*");
                if (stFirstOperator != null) {
                    if (stFirstOperator.countTokens() != 2) {
                        throw new IllegalArgumentException(
                                "OntimizeLAFPainterUtils -> Invalid parameters to configure a direction");
                    }
                    String a = stFirstOperator.nextToken().trim();
                    String b = stFirstOperator.nextToken().trim();
                    float numberA = OntimizeLAFPainterUtils.getNumericValue(a, x, y, w, h);
                    float numberB = OntimizeLAFPainterUtils.getNumericValue(b, x, y, w, h);
                    resultFirstOperator = numberA * numberB;
                }
            }

            float resultSecondOperator = OntimizeLAFPainterUtils.getNumericValue(secondOperator, x, y, w, h);

            return resultFirstOperator + resultSecondOperator;
        }

        return new Float(0.0f);
    }

    /**
     * This method converts the input string (named srcValue) to a float number (also if the string is a
     * variable x, y , w or h)
     * @param srcValue
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    protected static float getNumericValue(String srcValue, float x, float y, float w, float h) {

        if (srcValue != null) {
            if (srcValue.indexOf("f") != -1) {
                return new Float(srcValue);
            } else if (srcValue.indexOf("x") != -1) {
                return x;
            } else if (srcValue.indexOf("y") != -1) {
                return y;
            } else if (srcValue.indexOf("w") != -1) {
                return w;
            } else if (srcValue.indexOf("h") != -1) {
                return h;
            }
        }
        return 1.0f;
    }

    public static String removeSubstring(String s, String c) {

        int pos = s.indexOf(c);
        if (pos != -1) {
            return s.substring(0, pos) + s.substring(pos + 1);
        }

        return s;
    }

    /**
     * Given parameters for creating a LinearGradientPaint, this method will create and return a linear
     * gradient paint. One primary purpose for this method is to avoid creating a LinearGradientPaint
     * where the start and end points are equal. In such a case, the end y point is slightly increased
     * to avoid the overlap.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param midpoints
     * @param colors
     * @param cycleMethod
     * @return a valid LinearGradientPaint. This method never returns null.
     */
    public static LinearGradientPaint decodeGradient(float x1, float y1, float x2, float y2, float[] midpoints,
            Color[] colors, CycleMethod cycleMethod) {
        if ((x1 == x2) && (y1 == y2)) {
            y2 += .00001f;
        }
        return new LinearGradientPaint(x1, y1, x2, y2, midpoints, colors, cycleMethod);
    }

    /**
     * Given parameters for creating a RadialGradientPaint, this method will create and return a radial
     * gradient paint. One primary purpose for this method is to avoid creating a RadialGradientPaint
     * where the radius is non-positive. In such a case, the radius is just slightly increased to avoid
     * 0.
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

    // // Other method syntax to call the previous same-named method with other
    // parameters:
    // public static Paint decodeGradient(Shape s, float[] fractions, Color[]
    // colors) {
    // Rectangle2D bounds = s.getBounds2D();
    // float x = (float)bounds.getX();
    // float y = (float)bounds.getY();
    // float w = (float)bounds.getWidth();
    // float h = (float)bounds.getHeight();
    // return decodeGradient(x, y, w, h, fractions, colors,
    // HORIZONTAL_DEGRADATED);
    //
    // }
    // public static Paint decodeGradient(Shape s, float[] fractions, Color[]
    // colors, String direction) {
    // Rectangle2D bounds = s.getBounds2D();
    // float x = (float)bounds.getX();
    // float y = (float)bounds.getY();
    // float w = (float)bounds.getWidth();
    // float h = (float)bounds.getHeight();
    // return decodeGradient(x, y, w, h, fractions, colors, direction);
    // }

    /**
     * This method checks whether a painter indicated by a String contains the points that configure the
     * painter start, end, width and height points. If it is exists, this method returns the same
     * painter String without those points (which are one of the last three token in the string and
     * contains ~)
     * @return
     */
    public static String painterRemovePoints(String painter) {

        String result = "";
        StringTokenizer st = new StringTokenizer(painter, "¬");
        if (st != null) {
            String sFractions = st.nextToken();
            result = result.concat(sFractions);
            String sColors = st.nextToken();
            result = result.concat("¬" + sColors);
            if (st.countTokens() >= 1) { // because we have just "removed" two
                // tokens from the string tokenizer
                // (3-2=1, 4-2=2, 5-2=3)
                String thirdParam = st.nextToken();
                if ((thirdParam != null) && !thirdParam.contains("~")) {
                    result = result.concat("¬" + thirdParam);
                } ;
            }
            if (st.countTokens() >= 2) {
                String fourthParam = st.nextToken();
                if ((fourthParam != null) && !fourthParam.contains("~")) {
                    result = result.concat("¬" + fourthParam);
                }
            }
            if (st.countTokens() >= 3) {
                String fifthParam = st.nextToken();
                if ((fifthParam != null) && !fifthParam.contains("~")) {
                    result = result.concat("¬" + fifthParam);
                }
            }
        }
        return result;
    }

    /**
     * Return a painter which paints an image indicated in prop (file *.jpg, *.JPEG, *.JPG, *.png,
     * *.PNG)
     * @param s
     * @param prop
     * @param defaultValue
     * @return
     */
    public static TexturePaint decodeTexturePaint(Shape s, String prop, TexturePaint defaultValue) {

        // checking if the received painter String has value:
        if ((prop == null) || "".equals(prop)) {
            return defaultValue;
        }

        // checking if the received painter String is cached:
        if (OntimizeLAFParseUtils.olafCache.containsKey(prop)) {
            return (TexturePaint) OntimizeLAFParseUtils.olafCache.get(prop);
        }

        // Get point values:
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();

        // Parsing the received painter String to get a painter Object and
        // caching and returning it:
        try {
            InputStream in = OntimizeLAFPainterUtils.class.getClassLoader().getResourceAsStream(prop);
            BufferedImage mImage = null;
            if (prop.endsWith(".jpeg") || prop.endsWith(".jpg") || prop.endsWith(".JPEG") || prop.endsWith("JPG")) {
                mImage = ImageIO.read(in);
            } else if (prop.endsWith(".png") || prop.endsWith(".PNG")) {
                mImage = ImageIO.read(in);
            }
            in.close();
            Rectangle2D tr = new Rectangle2D.Double(x, y, w, h); // Create a
            // texture
            // rectangle
            // the same
            // size as the
            // shape to
            // paint.
            // Create the TexturePaint.
            TexturePaint tp = new TexturePaint(mImage, tr);

            // caching the result for future uses:
            OntimizeLAFParseUtils.olafCache.put(prop, tp);
            return tp;

        } catch (IOException e) {
            OntimizeLAFPainterUtils.logger.error("", e);
        }

        return null;
    }

}
