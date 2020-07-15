package com.ontimize.plaf.utils;

import java.io.IOException;
import java.util.Properties;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.plaf.OntimizeLookAndFeel;


/**
 * Generic utilities for the Ontimize Look And Feel (from now L&F or OLAF)
 *
 * @author Imatia Innovation
 *
 */
public class OntimizeLAFUtils {

    private static final Logger logger = LoggerFactory.getLogger(OntimizeLAFUtils.class);

    public static Properties customConfProp = null;

    public static Properties defaultConfProp = null;


    /**
     * This method load the configuration properties for the Ontimize Look And Feel, which are in the
     * filed indicated by the String path. These L&F configuration properties are stored in the variable
     * OntimizeLAFUtils.customConfProp
     */
    public static void loadcustomConfProp(String path) {

        if ((path != null) && !"".equalsIgnoreCase(path)) {
            OntimizeLAFUtils.customConfProp = OntimizeLAFUtils.readProperties(path);
        } else {
            OntimizeLAFUtils.customConfProp = null;
        }
    }

    /**
     * This method load the default configuration properties for the Ontimize Look And Feel, which are
     * in the filed indicated by the String path. These L&F configuration properties are stored in the
     * variable OntimizeLAFUtils.defaultConfProp
     *
     */
    public static void loadDefaultConfProp(String path) {

        // TODO: blindar para que este método no pueda ser visible dentro de este proyecto (el de Ontimize
        // Look And Feel). Para evitar que otra persona
        // lo llame desde otro proyecto (al tener el jar de este) y nos cambie las props por defecto ¡¡ para
        // eso ya estan las custom (loadcustomConfProp) !!!

        OntimizeLAFUtils.defaultConfProp = OntimizeLAFUtils.readProperties(path);
    }


    /**
     * This method reads and returns the configuration properties file indicated by the String path.
     */
    public static Properties readProperties(String path) {

        // read the configuration properties file called webservice.properties:
        Properties p = new Properties();
        try {
            p.load(OntimizeLAFUtils.class.getClassLoader().getResourceAsStream(path));
        } catch (IOException e) {
            OntimizeLAFUtils.logger.error("", e);
        }
        return p;
    }


    /**
     * Fill the uidefaults Object (from OntimizeLookAndFeel class) with the property indicated by
     * property in the custom configuration properties (if it exists). If it does not not exist or it
     * does not contain that property, this method get the property in the default configuration
     * properties of the Ontimize Look And Feel, that is, get the default value for that property in the
     * Ontimize L&F.
     *
     * In case that the method obtains a not-null-value or a not-empty-value the uidefaults Object if
     * popullated with that value for the key named property.
     * @param property
     * @return
     */
    public static void putProperty(String property) {

        Object value = OntimizeLAFUtils.getProperty(property);
        OntimizeLAFUtils.logger.debug("Property change: {}", property);

        if ((value != null) && !"".equalsIgnoreCase(value.toString())) {

            UIManager.getLookAndFeel().getDefaults().put(property, value);
        }
    }


    /**
     * Fill the uidefaults Object (from OntimizeLookAndFeel class) with the property indicated by
     * property in the custom configuration properties (if it exists). If it does not not exist or it
     * does not contain that property, this method get the property in the default configuration
     * properties of the Ontimize Look And Feel, that is, get the default value for that property in the
     * Ontimize L&F.
     *
     * In case that the method obtains a not-null-value or a not-empty-value the uidefaults Object if
     * popullated with that value for the key named property.
     *
     * In other case, the method fills the uidefaults Object with the sent defaultValue, if it is not
     * null and not empty.
     * @param property
     * @param defaultValue
     * @return
     */
    public static void putProperty(String property, Object defaultValue) {

        Object value = OntimizeLAFUtils.getProperty(property);
        OntimizeLAFUtils.logger.debug("Property change: {}", property);
        if ((value != null) && !"".equalsIgnoreCase(value.toString())) {
            UIManager.getLookAndFeel().getDefaults().put(property, value);
        } else if ((defaultValue != null) && !"".equalsIgnoreCase(defaultValue.toString())) {
            UIManager.getLookAndFeel().getDefaults().put(property, defaultValue);
        }
    }


    /**
     * Get the property indicated by key in the custom configuration properties (if it exists). If it
     * does not not exist or it does not contain that property, this method get the property in the
     * default configuration properties of the Ontimize Look And Feel, that is, return the default value
     * for that property in the Ontimize L&F.
     *
     * In other case, it returns null.
     * @param property
     * @return
     */
    public static Object getProperty(String property) {

        Object value = null;
        if (OntimizeLAFUtils.customConfProp != null) {
            value = OntimizeLAFUtils.getProperty(property, OntimizeLAFUtils.customConfProp);
        }
        if ((value == null) || "".equalsIgnoreCase(value.toString())) {
            if (OntimizeLAFUtils.defaultConfProp != null) {
                value = OntimizeLAFUtils.getProperty(property, OntimizeLAFUtils.defaultConfProp);
            }
        }

        if ((value != null) && "".equalsIgnoreCase(value.toString())) {
            return null; // to avoid return empty values
        }

        return value;
    }


    /**
     * <pre>
     * Get the property indicated by key in the properties named p.
     * The method returns null when:
     *      - key does not exist in the properties
     *      - key exists but it contains a non-valid value for the Ontimize Look And Feel
     *
     * &#64;param key
     * &#64;param p
     * &#64;return
     *
     * </pre>
     */
    public static Object getProperty(String key, Properties p) {
        if (p != null) {
            if (p.containsKey(key)) {
                return OntimizeLAFUtils.validate(key, p.getProperty(key));
            }
        }

        return null;
    }


    /**
     * <pre>
     * Check if the value is a valid value in the Ontimize Look And Feel for the key type.
     * The method returns an Object with the value converted to the desired data type, only if the value is a well-formatted value for the key type.
     *
     * Null is returned in other cases.
     *
     * NOTE: Special running for painters. Painters can not be created until the component points (x, y, w, h or x1, y1, x2, y2) are known.
     * For that fact, if the key belongs to a painter it will returned the value as String in order to be created in the component.
     *
     *
     * &#64;param key
     * &#64;param value
     * &#64;return
     *
     * </pre>
     */
    public static Object validate(String key, Object value) {

        // TODO: Cambiar este método por otro que llame a los parses de modo genérico en función de un
        // segundo properties de conf, q diga el tipo de dato que es cada clave, o la clase a llamar para
        // que la desparsee.
        // Va a ser más ineficiente que esto, pero bueno.

        if ((key != null) && (value != null)) {
            String keyToLowerCase = key.toLowerCase();

            // Special running for painters. Painters can not be created until the component points (x, y, w, h
            // or x1, y1, x2, y2) are known.
            // For that fact, if the key belongs to a painter it will returned the value as String in order to
            // be created in the component
            if (keyToLowerCase.indexOf("painter") >= 0) {
                return value.toString();
            } else if (keyToLowerCase.indexOf("usetextureimage") >= 0) {
                return value.toString();
            }

            // Normal mode:
            if ((keyToLowerCase.indexOf("menuitem[disabled].textforeground") >= 0)
                    || (keyToLowerCase.indexOf("menuitem[enabled].textforeground") >= 0)
                    || (keyToLowerCase.indexOf("menuitem[mouseover].textforeground") >= 0)
                    || (keyToLowerCase.indexOf("menuitem:menuitemaccelerator[enabled].textforeground") >= 0)) {
                return OntimizeLAFParseUtils.parseColorUIResource(value.toString(), null);
            } else if (keyToLowerCase.indexOf("degradated") >= 0) { // TODO: ahora que hay painters en el properties,
                                                                    // ver de cambiar esto por un painter.
                return OntimizeLAFParseUtils.parseColorArray(value.toString(), null);
            } else if ((keyToLowerCase.indexOf("rendererfillbackground") >= 0)
                    || (keyToLowerCase.indexOf("opaque") >= 0)) {
                return OntimizeLAFParseUtils.parseBoolean(value.toString(), null);
            } else if (keyToLowerCase.indexOf("color") >= 0) {
                return OntimizeLAFParseUtils.parseColor(value.toString(), null);
            } else if (keyToLowerCase.indexOf("background") >= 0) {
                return OntimizeLAFParseUtils.parseColor(value.toString(), null);
            } else if (keyToLowerCase.indexOf("foreground") >= 0) {
                return OntimizeLAFParseUtils.parseColorUIResource(value.toString(), null);
            } else if (keyToLowerCase.indexOf("font") >= 0) {
                return OntimizeLAFParseUtils.parseFont(value.toString(), null);
            } else if ((keyToLowerCase.indexOf("margins") >= 0) || (keyToLowerCase.indexOf("cellnofocusborder") >= 0)
                    || (keyToLowerCase.indexOf("focuscellhighlightborder") >= 0)) {
                return OntimizeLAFParseUtils.parseInsets(value.toString(), null);
            } else if (keyToLowerCase.indexOf("icon") >= 0) {
                return OntimizeLAFParseUtils.parseIcon(value.toString(), null);
            } else if ((keyToLowerCase.indexOf("extendtabstobase") >= 0)
                    || (keyToLowerCase.indexOf("tabAreaStatesMatchSelectedTab") >= 0)
                    || (keyToLowerCase.indexOf("nudgeSelectedLabel") >= 0)
                    || (keyToLowerCase.indexOf("useBasicArrows") >= 0)
                    || (keyToLowerCase.indexOf("rightalignsortarrow") >= 0) || (keyToLowerCase.indexOf("showgrid") >= 0)
                    || (keyToLowerCase.indexOf("rendererusetablecolors") >= 0)
                    || (keyToLowerCase.indexOf("rendereruseuiborder") >= 0)) {
                return OntimizeLAFParseUtils.parseBoolean(value.toString(), null);
            } else if ((keyToLowerCase.indexOf("maximumthumbsize") >= 0)
                    || (keyToLowerCase.indexOf("minimumthumbsize") >= 0)
                    || (keyToLowerCase.indexOf("intercellspacing") >= 0)) {
                return OntimizeLAFParseUtils.parseDimension(value.toString(), null);
            } else if ((keyToLowerCase.indexOf("disabled") >= 0) || (keyToLowerCase.indexOf("disabledtext") >= 0)) {
                return OntimizeLAFParseUtils.parseColor(value.toString(), null);


                // default integer returned value is -1 (for those keys):
            } else if ((keyToLowerCase.indexOf("arcwidth") >= 0) || (keyToLowerCase.indexOf("archeight") >= 0)
                    || (keyToLowerCase.indexOf("thickness") >= 0) || (keyToLowerCase.indexOf("taboverlap") >= 0)) { // positive
                                                                                                                    // or
                                                                                                                    // negative
                                                                                                                    // values
                                                                                                                    // are
                                                                                                                    // valid
                                                                                                                    // (to
                                                                                                                    // be
                                                                                                                    // returned)
                return OntimizeLAFParseUtils.getInteger(value.toString(), -1);
                // Only positive values are valid to be returned for those keys. (If the result is not a positive
                // value or zero, null is returned):
            } else if ((keyToLowerCase.indexOf("width") >= 0) || (keyToLowerCase.indexOf("height") >= 0)
                    || (keyToLowerCase.indexOf("indent") >= 0) || (keyToLowerCase.indexOf("size") >= 0)) {
                int result = OntimizeLAFParseUtils.getInteger(value.toString(), -1);
                return (result >= 0 ? result : null);
                // null is returned when value is not a valid string (null values will not be inserted as a
                // uidefault property)
            } else if (keyToLowerCase.indexOf("buttongap") >= 0) {
                return OntimizeLAFParseUtils.getInteger(value.toString(), null);
            }

            else if (OntimizeLAFUtils.matched(OntimizeLookAndFeel.NIMBUS_COLORS_KEYS, key)) {
                return OntimizeLAFParseUtils.parseColor(value.toString(), null);
            }


        }

        return null;
    }


    /**
     * The method returns true whether the String target is included in the String array, named set. In
     * other case, false is returned;
     * @param set
     * @param target
     * @return
     */
    protected static boolean matched(String[] set, String target) {

        for (String key : set) {
            if (key.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }


    /**
     *
     * Returns true if the key indicated by property has value (and not-empty value) in the custom
     * configuration properties (if it exists), or in the default configuration properties of the
     * Ontimize Look And Feel
     * @param property
     * @return
     */
    public static Boolean hasValue(String property) {

        Object value = null;
        if ((OntimizeLAFUtils.customConfProp != null) && OntimizeLAFUtils.customConfProp.containsKey(property)) {
            value = OntimizeLAFUtils.customConfProp.get(property);
        }
        if ((value != null) && !"".equalsIgnoreCase(value.toString().trim())) {
            return true;
        } else if ((OntimizeLAFUtils.defaultConfProp != null)
                && OntimizeLAFUtils.defaultConfProp.containsKey(property)) {
            value = OntimizeLAFUtils.defaultConfProp.get(property);
        }
        if ((value != null) && !"".equalsIgnoreCase(value.toString().trim())) {
            return true;
        }


        return false;
    }

}
