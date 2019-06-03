package com.ontimize.plaf.painter;

import java.awt.Graphics2D;
import java.awt.Insets;
import java.net.URL;

import javax.swing.JComponent;

public class ButtonPainter extends ImagePainter {
	  //package protected integers representing the available states that
   //this painter will paint. These are used when creating a new instance
   //of ButtonPainter to determine which region/state is being painted
   //by that instance.
   public static final int BACKGROUND_DEFAULT = 1;
   public static final int BACKGROUND_DEFAULT_FOCUSED = 2;
   public static final int BACKGROUND_MOUSEOVER_DEFAULT = 3;
   public static final int BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED = 4;
   public static final int BACKGROUND_PRESSED_DEFAULT = 5;
   public static final int BACKGROUND_PRESSED_DEFAULT_FOCUSED = 6;
   public static final int BACKGROUND_DISABLED = 7;
   public static final int BACKGROUND_ENABLED = 8;
   public static final int BACKGROUND_FOCUSED = 9;
   public static final int BACKGROUND_MOUSEOVER = 10;
   public static final int BACKGROUND_MOUSEOVER_FOCUSED = 11;
   public static final int BACKGROUND_PRESSED = 12;
   public static final int BACKGROUND_PRESSED_FOCUSED = 13;

	public ButtonPainter(int which,boolean tiles, boolean paintCenter, Insets sourceInsets, Insets destinationInsets, URL path, boolean center) {
		super(which,tiles, paintCenter, sourceInsets, destinationInsets, path, center);
	}

	public ButtonPainter(int which, URL path) {
		super(which,true, true, new Insets(9,8,10,8), null, path, false);
	}


	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
      componentColors = extendedCacheKeys;
      //generate this entire method. Each state/bg/fg/border combo that has
      //been painted gets its own KEY and paint method.
      switch(state) {
//          case BACKGROUND_DEFAULT: paintBackgroundDefault(g); break;
//          case BACKGROUND_DEFAULT_FOCUSED: paintBackgroundDefaultAndFocused(g); break;
//          case BACKGROUND_MOUSEOVER_DEFAULT: paintBackgroundMouseOverAndDefault(g); break;
//          case BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED: paintBackgroundMouseOverAndDefaultAndFocused(g); break;
//          case BACKGROUND_PRESSED_DEFAULT: paintBackgroundPressedAndDefault(g); break;
//          case BACKGROUND_PRESSED_DEFAULT_FOCUSED: paintBackgroundPressedAndDefaultAndFocused(g); break;
//          case BACKGROUND_DISABLED: paintBackgroundDisabled(g); break;
//          case BACKGROUND_ENABLED: paintBackgroundEnabled(g); break;
//          case BACKGROUND_FOCUSED: paintBackgroundFocused(g); break;
//          case BACKGROUND_MOUSEOVER: paintBackgroundMouseOver(g); break;
//          case BACKGROUND_MOUSEOVER_FOCUSED: paintBackgroundMouseOverAndFocused(g); break;
//          case BACKGROUND_PRESSED: paintBackgroundPressed(g); break;
//          case BACKGROUND_PRESSED_FOCUSED: paintBackgroundPressedAndFocused(g); break;
      default: paint(c,g,0,0,width,height);
      }
//      c.setBackground(oldbg);
	}
}
