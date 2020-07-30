package com.ontimize.plaf.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.plaf.ColorUIResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains Parse utils for the Ontimize Look And Feel.
 *
 * @author Imatia Innovation
 *
 */
public class OntimizeLAFParseUtils {

    private static final Logger logger = LoggerFactory.getLogger(OntimizeLAFParseUtils.class);

    /**
     * Cache that stores previous decoded variables like colors, fonts, etc.
     */
    public static Hashtable<Object, Object> olafCache = new Hashtable<Object, Object>();


    public static Font parseFont(String font, Font defaultFont) {

        // checking if the received font String has value:
        if ((font == null) || "".equals(font)) {
            return defaultFont;
        }

        // checking if the received font String is cached:
        if (OntimizeLAFParseUtils.olafCache.containsKey(font)) {
            return (Font) OntimizeLAFParseUtils.olafCache.get(font);
        }

        // Parsing the received font String to get an Font Object and caching and returning it:
        Font result = null;
        result = Font.decode(font);

        if (result != null) {
            OntimizeLAFParseUtils.olafCache.put(font, result);
            return result;
        } else {
            return defaultFont;
        }
    }

    public static String fontToString(Font font) {
        if (font == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(font.getFamily()).append("-");
        String strStyle;
        if (font.isBold()) {
            strStyle = font.isItalic() ? "bolditalic" : "bold";
        } else {
            strStyle = font.isItalic() ? "italic" : "plain";
        }
        sb.append(strStyle).append("-");
        sb.append(font.getSize());
        return sb.toString();
    }


    public static Integer getInteger(String s, Integer defaultValue) {
        if ((s != null) && !"".equals(s)) {
            return Integer.parseInt(s);
        }
        return defaultValue;
    }


    /**
     * Parses a String to convert it to a boolean. If the String value is 'yes' or 'true' the boolean
     * response will be true, and if the String value is 'no' or 'false' the result will be false. If
     * the String is not one of the previous one specified previously, the defaultValue will be
     * returned.
     * @param string the string to parse
     * @param defaultValue the default value to return if no coincidence found
     * @return
     */
    public static Boolean parseBoolean(String string, Boolean defaultValue) {
        if (string == null) {
            return defaultValue;
        }
        if (string.equalsIgnoreCase("yes") || string.equalsIgnoreCase("true")) {
            return true;
        } else if (string.equalsIgnoreCase("no") || string.equalsIgnoreCase("false")) {
            return false;
        }
        return defaultValue;
    }


    /**
     * Receives an string (named color) with the codification of several colours (in both RGB and
     * Hexadecimal) splitted by ~ and returns an Color array with those colours.
     * @param color
     * @param defaultColor
     * @return
     */
    public static Color[] parseColorArray(String color, Color[] defaultColor) {

        // checking if the received color String is valid:
        if ((color == null) || "".equals(color)) {
            return defaultColor;
        }

        // checking if the received color String is cached (we indicate "Color[]" before the key, to
        // distinguish the array from a single color cached in method parseColor):
        if (OntimizeLAFParseUtils.olafCache.containsKey("Color[]" + color)) {
            return (Color[]) OntimizeLAFParseUtils.olafCache.get("Color[]" + color);
        }

        // Parsing the received color String to get an Color[] Object and caching and returning it:
        StringTokenizer st = new StringTokenizer(color, "~");
        Color[] result = new Color[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            result[i] = OntimizeLAFParseUtils.parseColor(st.nextToken(), defaultColor != null ? defaultColor[i] : null);
            i++;
        }
        OntimizeLAFParseUtils.olafCache.put("Color[]" + color, result);
        return result;
    }


    /**
     * Parse the sent string (named color), checking the cache or converting the string to return a
     * color for that encoded string. If the value is wrong enconded and also it does not exist in the
     * cache, this method returns defaultColor.
     * @param color
     * @param defaultColor
     * @return
     */
    public static Color parseColor(String color, Color defaultColor) {

        // checking if the received color String is valid:
        if ((color == null) || "".equals(color)) {
            return defaultColor;
        }

        // checking if the received color String is cached:
        if (OntimizeLAFParseUtils.olafCache.containsKey(color)) {
            return (Color) OntimizeLAFParseUtils.olafCache.get(color);
        }

        // Parsing the received color String to get an Color Object and caching and returning it:
        if (color.startsWith("#")) {
            Color c = OntimizeLAFColorUtils.colorHexToColor(color);
            if (c != null) {
                OntimizeLAFParseUtils.olafCache.put(color, c);
            }
            return c;
        } else if (color.indexOf(";") >= 0) {
            Color c = OntimizeLAFColorUtils.colorRGBToColor(color);
            if (c != null) {
                OntimizeLAFParseUtils.olafCache.put(color, c);
            }
            return c;
        }
        return defaultColor;
    }


    /**
     * Parse the sent string (named color), checking the cache or converting the string to return a
     * color for that encoded string. If the value is wrong enconded and also it does not exist in the
     * cache, this method returns defaultColor.
     * @param color
     * @param defaultColor
     * @return
     */
    public static ColorUIResource parseColorUIResource(String color, ColorUIResource defaultColor) {

        // checking if the received color String is valid:
        if ((color == null) || "".equals(color)) {
            return defaultColor;
        }

        Color oColor = OntimizeLAFParseUtils.parseColor(color, defaultColor);
        if (color != null) {
            return new ColorUIResource(oColor);
        }

        return defaultColor;
    }


    public static Insets parseInsets(String insets, Insets defaultValue) throws IllegalArgumentException {

        // checking if the received font String has value:
        if ((insets == null) || "".equals(insets)) {
            return defaultValue;
        }

        // checking if the received font String is cached:
        if (OntimizeLAFParseUtils.olafCache.containsKey(insets)) {
            return (Insets) OntimizeLAFParseUtils.olafCache.get(insets);
        }

        // Parsing the received insets String to get an InsetsUIResource Object and caching and returning
        // it:
        StringTokenizer st = new StringTokenizer(insets, ";");
        if (st.countTokens() != 4) {
            throw new IllegalArgumentException("Insets must have 4 tokens");
        }
        int top = Integer.parseInt(st.nextToken());
        int left = Integer.parseInt(st.nextToken());
        int bottom = Integer.parseInt(st.nextToken());
        int right = Integer.parseInt(st.nextToken());
        Insets result = new Insets(top, left, bottom, right);

        OntimizeLAFParseUtils.olafCache.put(insets, result);
        return result;
    }


    public static ImageIcon parseIcon(String path, ImageIcon defaultValue) {

        // checking if the received font String has value:
        if ((path == null) || "".equals(path)) {
            return defaultValue;
        }

        // checking if the received font String is cached:
        if (OntimizeLAFParseUtils.olafCache.containsKey(path)) {
            return (ImageIcon) OntimizeLAFParseUtils.olafCache.get(path);
        }

        // Parsing the received path String to get an ImageIcon Object and caching and returning it:
        ImageIcon result = null;
        try {
            InputStream iS = OntimizeLAFParseUtils.class.getClassLoader().getResourceAsStream(path);
            if (iS != null) {
                ;
                result = new ImageIcon(ImageIO.read(iS));
            } else {
                result = defaultValue;
            }
        } catch (IOException e) {
            OntimizeLAFParseUtils.logger.error("", e);
        }

        if (result != null) {
            OntimizeLAFParseUtils.olafCache.put(path, result);
        }
        return result;
    }


    /**
     * Receives an string (named floats) with the codification of several floats splitted by ~ and
     * returns a float array with those floats.
     * @param floats
     * @param defaultFloatArray
     * @return
     */
    public static float[] parseFloatsArray(String floats, float[] defaultFloatArray) {

        // checking if the received color String is valid:
        if ((floats == null) || "".equals(floats)) {
            return defaultFloatArray;
        }

        // checking if the received color String is cached:
        if (OntimizeLAFParseUtils.olafCache.containsKey(floats)) {
            return (float[]) OntimizeLAFParseUtils.olafCache.get(floats);
        }

        // Parsing the received color String to get an Color[] Object and caching and returning it:
        StringTokenizer st = new StringTokenizer(floats, "~");
        float[] result = new float[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            try {
                result[i] = Float.valueOf(st.nextToken().trim()).floatValue();
            } catch (NumberFormatException nfe) {
                if (defaultFloatArray != null) {
                    result[i] = defaultFloatArray[i];
                }
            }
            i++;
        }
        OntimizeLAFParseUtils.olafCache.put(floats, result);
        return result;
    }


    /**
     * Parse the string dim to get a dimension value. The syntax of the dim variable must be two integer
     * splitted by ; A;B = new Dimension (A, B) By default, defaultValue is returned
     * @param dim
     * @param defaultValue
     * @return
     */
    public static Dimension parseDimension(String dim, Dimension defaultValue) {

        // checking if the received dimension String has value:
        if ((dim == null) || "".equals(dim)) {
            return defaultValue;
        }

        // checking if the received dimension String is cached:
        if (OntimizeLAFParseUtils.olafCache.containsKey(dim)) {
            return (Dimension) OntimizeLAFParseUtils.olafCache.get(dim);
        }

        // Parsing the received dimension String to get a Dimension Object and caching and returning it:
        StringTokenizer st = new StringTokenizer(dim, ";");
        if (st.countTokens() != 2) {
            throw new IllegalArgumentException("  OntimizeLAFParseUtils -> Dimensions must have 2 tokens");
        }
        int w = Integer.parseInt(st.nextToken());
        int h = Integer.parseInt(st.nextToken());
        Dimension result = new Dimension(w, h);


        OntimizeLAFParseUtils.olafCache.put(dim, result);
        return result;
    }

    // linear-gradient(to right, #1e5799 0%,#2989d8 25%,#1fc124 76%,#b8f785 100%)
    public static LinearGradient parseLinearGradient(String gradient, LinearGradient lgradient) {

        if ((gradient == null) || "".equals(gradient) || !gradient.startsWith("linear-gradient(")
                || !gradient.endsWith(")")) {
            return lgradient;
        }


        int startIndex = gradient.indexOf("(");
        int endIndex = gradient.indexOf(")");
        if ((startIndex > 0) && (endIndex > 0) && (startIndex < endIndex)) {
            LinearGradient lg = new LinearGradient();

            String gradientValues = gradient.substring(startIndex + 1, endIndex);

            StringTokenizer fragments = new StringTokenizer(gradientValues, ",");
            int tokenCount = fragments.countTokens();
            if (tokenCount < 3) {
                throw new IllegalArgumentException(
                        "  OntimizeLAFParseUtils -> Gradients must have 3 tokens (direction, color-stop, color-stop)");
            }

            String direction = fragments.nextToken();
            lg.setDirection(direction);
            lg.setgDirection(new GradientDirection(direction));

            int length = fragments.countTokens();
            if (length < 2) {
                throw new IllegalArgumentException(
                        "  OntimizeLAFParseUtils -> Color stops must have 2 tokens at least");
            }
            while (fragments.hasMoreElements()) {
                String fragment = fragments.nextToken();
                try {
                    OntimizeLAFParseUtils.parseColorStop(fragment, lg);
                } catch (Exception e) {
                    OntimizeLAFParseUtils.logger.error("Error parsing color stop : {}", fragment, e);
                    return null;
                }
            }
            return lg;
        }
        return null;
    }

    // #1e5799 0%
    protected static void parseColorStop(String colorStop, LinearGradient lg) {
        if ((colorStop == null) || "".equals(colorStop)) {
            return;
        }

        StringTokenizer st = new StringTokenizer(colorStop, " ");
        if (st.countTokens() != 2) {
            throw new IllegalArgumentException(
                    "  OntimizeLAFParseUtils -> color-stop must have 2 tokens (color stop%)");
        }

        String color = st.nextToken();
        String stop = st.nextToken();

        Float fStop = OntimizeLAFParseUtils.parseStop(stop);
        if (fStop == null) {
            throw new IllegalArgumentException("  OntimizeLAFParseUtils -> impossible to parse color stop: " + stop);
        }
        lg.addStop(fStop.floatValue());

        Color cColor = OntimizeLAFParseUtils.parseColor(color, null);
        if (cColor == null) {
            throw new IllegalArgumentException("  OntimizeLAFParseUtils -> impossible to parse color: " + color);
        }
        lg.addColor(cColor);

    }

    // 0.15%
    protected static Float parseStop(String stop) {
        if ((stop == null) || "".equals(stop)) {
            return null;
        }

        if (!stop.contains("%")) {
            return null;
        }
        String aux = stop.substring(0, stop.indexOf("%"));
        try {
            boolean rangeError = false;
            Float f_ = Float.parseFloat(aux);
            if ((f_ < 0.0f) || (f_ > 100.0)) {
                throw new IllegalArgumentException("stop parameter outside of expected range: 0 - 100 %");
            }

            return f_ / 100.0f;

        } catch (Exception e) {
            OntimizeLAFParseUtils.logger.error("Error parsing stop : {}", stop, e);
        }
        return null;
    }

}
