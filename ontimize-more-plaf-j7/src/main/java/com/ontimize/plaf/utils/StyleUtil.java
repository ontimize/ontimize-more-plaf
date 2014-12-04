package com.ontimize.plaf.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Paint;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import com.ontimize.plaf.utils.CSSParser.CSSDocument;
import com.ontimize.plaf.utils.CSSParser.CSSStyle;

public class StyleUtil {

    public static final String STYLE_PROPERTY = "com.ontimize.gui.lafstyle";

    protected CSSDocument document;

    protected static StyleUtil instance;

    public static final String STYLE_TAG = "style";
    public static final String CLASS_ATTR = "class";
    public static final String PROPERTY_ATTR = "property";
    public static final String STATE_ATTR = "state";
    public static final String NAME_ATTR = "name";
    public static final String ITEM_TAG = "item";
    public static final String CONSTANTS_TAG = "Constants";
    static {
        String definition = System.getProperty(StyleUtil.STYLE_PROPERTY);
        if (definition != null) {
            StyleUtil.instance = new StyleUtil(definition);
        } else {
            StyleUtil.instance = new StyleUtil();
        }
    }

    public static StyleUtil getInstance() {
        return StyleUtil.instance;
    }

    public static void newInstance(String definition) {
        if (StyleUtil.instance != null) {
            // Revisar para eliminar los recursos...
            StyleUtil.instance = null;
        }

        if (definition != null) {
            StyleUtil.instance = new StyleUtil(definition);
        } else {
            StyleUtil.instance = new StyleUtil();
        }
    }

    protected StyleUtil() {
    }

    protected StyleUtil(String definition) {
        InputStream input = StyleUtil.class.getClassLoader().getResourceAsStream(definition);
        if (input != null) {
            this.document = CSSParser.getInstance().parse(input);
        }
    }

    protected CSSStyle getCSSStyle(String name) {

        CSSStyle cssStyle = this.getCSSStyleInner(name);
        cssStyle = this.translateStyle(cssStyle);
        return cssStyle;
    }

    protected CSSStyle getCSSStyleInner(String name) {

        CSSStyle cssStyle = null;
        if (this.document == null) {
            return cssStyle;
        }
        int index = this.document.indexOf(name);
        if (index >= 0) {
            cssStyle = this.document.get(index);
        }
        return cssStyle;
    }

    /**
     * Translate style.
     *
     * @param cssStyle
     *            the css style
     * @return the CSS style
     */
    protected CSSStyle translateStyle(CSSStyle cssStyle) {
        Map<String, String> constants = this.getCSSStyleInner(StyleUtil.CONSTANTS_TAG);
        if ((constants != null) && (cssStyle != null)) {
            for (Entry<String, String> entry : cssStyle.entrySet()) {
                String newValue = constants.get(entry.getValue());
                if (newValue != null) {
                    entry.setValue(newValue);
                }
            }
        }
        return cssStyle;
    }

    public static String getProperty(String name, String key, String defaultValue) {
        Map<String, String> styles;
        styles = StyleUtil.getInstance().getCSSStyle(name);

        if (styles != null && styles.containsKey(key)) {
            return styles.get(key);
        }
        return defaultValue;
    }

    public static ColorUIResource getColorUI(String name, String key, String defaultValue) {
        Color color = StyleUtil.getColor(name, key, defaultValue);
        if (color != null) {
            return new ColorUIResource(color);
        }
        return null;
    }

    public static Color getColor(String name, String key, String defaultValue) {
        String value = StyleUtil.getProperty(name, key, defaultValue);
        if (value != null) {
            if (value.startsWith("#")) {
                return OntimizeLAFColorUtils.colorHexToColor(value);
            } else if (value.startsWith("[")) {
                // TODO
            }
        }
        return null;
    }

    public static Paint getPaint(String name, String key, String defaultValue) {
        String value = StyleUtil.getProperty(name, key, defaultValue);
        if (value != null) {
            if (value.startsWith("linear-gradient")) {
                return OntimizeLAFParseUtils.parseLinearGradient(value, null);// TODO
                                                                              // create
                                                                              // a
                                                                              // default
                                                                              // gradient
            } else {
                return StyleUtil.getColor(name, key, defaultValue);
            }
        }
        return null;
    }

    public static ColorUIResource[] getArrayColorUI(String name, String key, String defaultValue) {

        StringTokenizer st = new StringTokenizer(StyleUtil.getProperty(name, key, defaultValue), " ");
        if (st.countTokens() == 0) {
            st = new StringTokenizer(defaultValue, " ");
        }
        ColorUIResource[] colors = new ColorUIResource[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            colors[i] = new ColorUIResource(OntimizeLAFColorUtils.colorHexToColor(token));
            i++;
        }
        return colors;

    }

    public static Color[] getArrayColor(String name, String key, String defaultValue) {

        StringTokenizer st = new StringTokenizer(StyleUtil.getProperty(name, key, defaultValue), " ");
        if (st.countTokens() == 0) {
            st = new StringTokenizer(defaultValue, " ");
        }
        Color[] colors = new Color[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            colors[i] = OntimizeLAFColorUtils.colorHexToColor(token);
            i++;
        }
        return colors;

    }

    public static FontUIResource getFontUIResource(String name, String key, String defaultValue) {
        try {
            String value = StyleUtil.getProperty(name, key, defaultValue);
            Font font = StyleUtil.getFont(value);
            if (font == null) {
                return null;
            }
            return new FontUIResource(font);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Font getFont(String name, String key, String defaultValue) {
        try {
            String value = StyleUtil.getProperty(name, key, defaultValue);
            Font font = StyleUtil.getFont(value);
            if (font == null) {
                return null;
            }
            return font;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Icon getIcon(String name, String key, String defaultValue) {
        try {
            String iconPath = StyleUtil.getIconPath(name, key, defaultValue);
            Icon icon = OntimizeLAFParseUtils.parseIcon(iconPath, null);
            return icon;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InsetsUIResource getInsetsUI(String name, String key, String defaultValue) {
        try {
            String value = StyleUtil.getProperty(name, key, defaultValue);
            if (value == null) {
                return null;
            }
            Insets insets = StyleUtil.getInsets(value);
            return new InsetsUIResource(insets.top, insets.left, insets.bottom, insets.right);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Insets getInsets(String name, String key, String defaultValue) {
        try {
            String value = StyleUtil.getProperty(name, key, defaultValue);
            return StyleUtil.getInsets(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean getBoolean(String name, String key, String defaultValue) {
        try {
            String value = StyleUtil.getProperty(name, key, defaultValue);
            return StyleUtil.getBoolean(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getInteger(String name, String key, String defaultValue) {
        try {
            String value = StyleUtil.getProperty(name, key, defaultValue);
            if (value == null) {
                return null;
            }
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Float getFloat(String name, String key, String defaultValue) {
        try {
            String value = StyleUtil.getProperty(name, key, defaultValue);
            if (value == null) {
                return null;
            }
            return Float.parseFloat(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Double getDouble(String name, String key, String defaultValue) {
        try {
            String value = StyleUtil.getProperty(name, key, defaultValue);
            if (value == null) {
                return null;
            }
            return Double.parseDouble(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static Insets getInsets(String value) throws IllegalArgumentException {
        StringTokenizer st = new StringTokenizer(value, " ");
        if (st.countTokens() != 4) {
            throw new IllegalArgumentException("Insets must have 4 tokens");
        }
        int top = Integer.parseInt(st.nextToken());
        int left = Integer.parseInt(st.nextToken());
        int bottom = Integer.parseInt(st.nextToken());
        int right = Integer.parseInt(st.nextToken());
        return new Insets(top, left, bottom, right);
    }

    public static boolean getBoolean(String s) {
        if ((s != null)) {
            if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("true")) {
                return true;
            } else if (s.equalsIgnoreCase("no") || s.equalsIgnoreCase("false")) {
                return false;
            }
        }
        return false;
    }

    public static Font getFont(String string) {
        if ((string == null) || "".equals(string)) {
            return null;
        }
        return Font.decode(string);
    }

    public static String getIconPath(String name, String key, String defaultValue) {

        try {
            String path = StyleUtil.getProperty(name, key, defaultValue);
            if ((path == null) || "".equals(path)) {
                return null;
            }
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL getIconURL(String name, String key, String defaultValue) {

        try {
            String path = StyleUtil.getProperty(name, key, defaultValue);
            if ((path == null) || "".equals(path)) {
                return null;
            }
            return StyleUtil.class.getClassLoader().getResource(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
