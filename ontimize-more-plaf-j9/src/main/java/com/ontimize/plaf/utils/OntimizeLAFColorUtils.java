package com.ontimize.plaf.utils;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains Color utils for the Ontimize Look And Feel.
 *
 * @author Imatia Innovation
 *
 */
public class OntimizeLAFColorUtils {

	private static final Logger logger = LoggerFactory.getLogger(OntimizeLAFColorUtils.class);


	/**
	 * This method convert a color to the Ontimize Syntax used both in the cached values and in the OntimizeLAFDefaultConfig.properties,
	 * that is RR;GG;BB;alpha where each value (between ;) is an int between 0 and 255 (also the alpha value)
	 *
	 * @param color
	 * @param defaultValue
	 * @return
	 */
	public static String colorToString(Color color, String defaultValue) {

		if ((color == null) || "".equals(color)) {
			return defaultValue;
		}

		return color.getRed() + ";" + color.getGreen() + ";" + color.getBlue() + ";" + color.getAlpha();
	}



	/**
	 * This method convert the received String to a color. This string must be a three-values(#rrggbb) or four-values(#rrggbbaa) string, where each one
	 * of these values must be a hexadecimal number between 00 and ff. In case that the string was a four-values the forth is used to indicate the color alpha
	 *
	 * @param hex
	 * @return
	 * @throws Exception
	 */
	public static Color colorHexToColor(String hex) {

		// when no alpha is indicated:
		if (hex.length() == 7) {
			return OntimizeLAFColorUtils.colorHexToColor(hex,255);
		} else if ( (hex.length()==9) && hex.startsWith("#")){

			String rH = hex.substring(1, 3);
			String gH = hex.substring(3, 5);
			String bH = hex.substring(5, 7);
			String alphaH = hex.substring(7, 9);

			int r = Integer.parseInt(rH, 16);
			int g = Integer.parseInt(gH, 16);
			int b = Integer.parseInt(bH, 16);
			int alpha = Integer.parseInt(alphaH, 16);
			return new Color(r, g, b, alpha);

			// in other case, return null:
		} else {
			try {
				throw new Exception("Invalid values " + hex + " not hexadecimal color");
			} catch (Exception e) {
				OntimizeLAFColorUtils.logger.error("", e);
				return null;
			}
		}
	}




	/**
	 * This method convert the received String to a color. This string must be a three-values(#rrggbb) string, where each one
	 * of these values must be a hexadecimal number between 00 and ff. Also an alpha can be indicated
	 *
	 * @param hex
	 * @param alpha
	 * @return
	 */
	public static Color colorHexToColor(String hex,int alpha) {
		try {
			if ((hex.length() != 7) || (hex.startsWith("#") == false)) {
				throw new Exception("Invalid values " + hex + " not hexadecimal color");
			}
			String rH = hex.substring(1, 3);
			String gH = hex.substring(3, 5);
			String bH = hex.substring(5, 7);

			int r = Integer.parseInt(rH, 16);
			int g = Integer.parseInt(gH, 16);
			int b = Integer.parseInt(bH, 16);
			return new Color(r, g, b, alpha);
		} catch (Exception e) {
			OntimizeLAFColorUtils.logger.error("", e);
			return null;
		}
	}



	/**
	 * This method convert the received String to a color. This string must be a three-values(R;G;B) or four-values(R;G;B;alpha) string, where each one
	 * of these values must be splitted by ; and they must be a positive int between 0 and 255
	 *
	 * @param rgb String with RGB values separated by ';' (For example 124;100;100 or 255;255;255;255)
	 * @return
	 * @throws Exception
	 */
	public static Color colorRGBToColor(String rgb) {
		StringTokenizer st = new StringTokenizer(rgb, ";");
		try {
			if ((st.countTokens() < 3) || (st.countTokens() > 4)) {
				throw new Exception("Invalid values");
			}
			int r = Integer.parseInt(st.nextToken());
			int g = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			if (st.hasMoreTokens()) {
				int alpha = Integer.parseInt(st.nextToken());
				return new Color(r, g, b, alpha);
			} else {
				return new Color(r, g, b);
			}
		} catch (Exception e) {
			OntimizeLAFColorUtils.logger.error("", e);
			return null;
		}
	}




	/**
	 * This method convert the received String to a color. This string must be a three-values(R;G;B) string, where each one
	 * of these values must be splitted by ; and they must be a positive int between 0 and 255
	 *
	 * @param rgb String with RGB values separated by ';' (For example 124;100;100)
	 * @return
	 * @throws Exception
	 */
	public static Color colorRGBToColor(String rgb, int alpha) {
		StringTokenizer st = new StringTokenizer(rgb, ";");
		try {
			if (st.countTokens() != 3 ) {
				throw new Exception("Invalid values");
			}
			int r = Integer.parseInt(st.nextToken());
			int g = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());

			return new Color(r, g, b);
		} catch (Exception e) {
			OntimizeLAFColorUtils.logger.error("", e);
			return null;
		}
	}


	/**
	 * Returns a new color equal to the old one, except alpha (transparency)
	 * channel is set to the new value.
	 *
	 * @param color
	 *            the color to modify
	 * @param alpha
	 *            the new alpha (transparency) level. Must be an int between 0
	 *            and 255
	 * @return a new alpha-applied {@code Color}
	 * @throws IllegalArgumentException
	 *             if {@code alpha} is not between 0 and 255 inclusive
	 * @throws NullPointerException
	 *             if {@code color} is {@code null}
	 */
	public static Color setAlpha(Color color, int alpha) {
		if ((alpha < 0) || (alpha > 255)) {
			throw new IllegalArgumentException("invalid alpha value");
		}

		return new Color(
				color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	public static Color setAlpha(Color color, double alpha){
		if ((alpha < 0.0) || (alpha > 1.0)) {
			throw new IllegalArgumentException("invalid alpha value");
		}
		int i_alpha = (int)((alpha*255)+0.5);
		return OntimizeLAFColorUtils.setAlpha(color, i_alpha);
	}

	public static LinearGradientPaint decodeGradient(Rectangle bounds, LinearGradient gradient){
		if((bounds!=null) && (gradient != null)){
			GradientDirection gd = gradient.getgDirection();
			try {
				return new LinearGradientPaint(gd.getStartX(bounds), gd.getStartY(bounds),
						gd.getEndX(bounds), gd.getEndY(bounds),
						gradient.getStopsArray(),
						gradient.getColorsArray());
			} catch (Exception e) {
				OntimizeLAFColorUtils.logger.error("", e);
			}

		}
		return null;
	}

}
