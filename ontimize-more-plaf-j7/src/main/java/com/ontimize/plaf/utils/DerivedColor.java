package com.ontimize.plaf.utils;

import java.awt.Color;

public class DerivedColor extends Color {

	protected final Color parentColor;
	protected final float hOffset, sOffset, bOffset;
	protected final int aOffset;
	protected int argbValue;

	DerivedColor(Color parent, float hOffset, float sOffset, float bOffset, int aOffset) {
		super(0);
		this.parentColor = parent;
		this.hOffset = hOffset;
		this.sOffset = sOffset;
		this.bOffset = bOffset;
		this.aOffset = aOffset;
	}

	public Color getParentColor() {
		return parentColor;
	}

	public float getHueOffset() {
		return hOffset;
	}

	public float getSaturationOffset() {
		return sOffset;
	}

	public float getBrightnessOffset() {
		return bOffset;
	}

	public int getAlphaOffset() {
		return aOffset;
	}

	/**
	 * Recalculate the derived color from the UIManager parent color and offsets
	 */
	 public void rederiveColor() {
		 Color src = getParentColor();
		 if (src != null) {
			 float[] tmp = Color.RGBtoHSB(src.getRed(), src.getGreen(), src.getBlue(), null);
			 // apply offsets
			 tmp[0] = clamp(tmp[0] + hOffset);
			 tmp[1] = clamp(tmp[1] + sOffset);
			 tmp[2] = clamp(tmp[2] + bOffset);
			 int alpha = clamp(src.getAlpha() + aOffset);
			 argbValue = (Color.HSBtoRGB(tmp[0], tmp[1], tmp[2]) & 0xFFFFFF) | (alpha << 24);
		 } else {
			 float[] tmp = new float[3];
			 tmp[0] = clamp(hOffset);
			 tmp[1] = clamp(sOffset);
			 tmp[2] = clamp(bOffset);
			 int alpha = clamp(aOffset);
			 argbValue = (Color.HSBtoRGB(tmp[0], tmp[1], tmp[2]) & 0xFFFFFF) | (alpha << 24);
		 }
	 }

	 /**
	  * Returns the RGB value representing the color in the default sRGB {@link java.awt.image.ColorModel}. (Bits 24-31
	  * are alpha, 16-23 are red, 8-15 are green, 0-7 are blue).
	  *
	  * @return the RGB value of the color in the default sRGB <code>ColorModel</code>.
	  * @see java.awt.image.ColorModel#getRGBdefault
	  * @see #getRed
	  * @see #getGreen
	  * @see #getBlue
	  * @since JDK1.0
	  */
	 @Override public int getRGB() {
		 return argbValue;
	 }

	 @Override
	 public boolean equals(Object o) {
		 if (this == o) return true;
		 if (!(o instanceof DerivedColor)) return false;
		 DerivedColor that = (DerivedColor) o;
		 if (aOffset != that.aOffset) return false;
		 if (Float.compare(that.bOffset, bOffset) != 0) return false;
		 if (Float.compare(that.hOffset, hOffset) != 0) return false;
		 if (Float.compare(that.sOffset, sOffset) != 0) return false;
		 if (!parentColor.equals(that.parentColor)) return false;
		 return true;
	 }

	 @Override
	 public int hashCode() {
		 int result = parentColor.hashCode();
		 result = 31 * result + hOffset != +0.0f ?
				 Float.floatToIntBits(hOffset) : 0;
				 result = 31 * result + sOffset != +0.0f ?
						 Float.floatToIntBits(sOffset) : 0;
						 result = 31 * result + bOffset != +0.0f ?
								 Float.floatToIntBits(bOffset) : 0;
								 result = 31 * result + aOffset;
								 return result;
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

	 public static class UIResource extends DerivedColor implements javax.swing.plaf.UIResource {
		 public UIResource(Color parentColor, float hOffset, float sOffset,
				 float bOffset, int aOffset) {
			 super(parentColor, hOffset, sOffset, bOffset, aOffset);
		 }
	 }
}
