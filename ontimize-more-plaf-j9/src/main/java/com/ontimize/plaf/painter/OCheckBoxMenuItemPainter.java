package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * Class that paints the check box menu item background and the check icons
 * depending on the element status (enable, disable, mouse over, selected and
 * combinations). Paint way can be indicated in the Ontimize L&F configuration
 * properties files or default values can be used when no custom configuration
 * is included.
 *
 * @author Imatia Innovation
 *
 */
public class OCheckBoxMenuItemPainter extends AbstractRegionPainter {
	// package protected integers representing the available states that
	// this painter will paint. These are used when creating a new instance
	// of CheckBoxMenuItemPainter to determine which region/state is being
	// painted
	// by that instance.
	public static final int BACKGROUND_DISABLED = 1;
	public static final int BACKGROUND_ENABLED = 2;
	public static final int BACKGROUND_MOUSEOVER = 3;
	public static final int BACKGROUND_SELECTED = 5;// 8
	public static final int BACKGROUND_SELECTED_MOUSEOVER = 4;

	public static final int CHECKICON_ICON_DISABLED = 6;
	public static final int CHECKICON_ICON_ENABLED = 7;
	public static final int CHECKICON_ICON_FOCUSED = 8;
	public static final int CHECKICON_ICON_MOUSEOVER = 9;
	public static final int CHECKICON_ICON_MOUSEOVER_FOCUSED = 10;
	public static final int CHECKICON_ICON_PRESSED = 11;
	public static final int CHECKICON_ICON_PRESSED_FOCUSED = 12;
	public static final int CHECKICON_ICON_SELECTED = 13;
	public static final int CHECKICON_ICON_SELECTED_FOCUSED = 14;
	public static final int CHECKICON_ICON_PRESSED_SELECTED = 15;
	public static final int CHECKICON_ICON_PRESSED_SELECTED_FOCUSED = 16;
	public static final int CHECKICON_ICON_MOUSEOVER_SELECTED = 17;
	public static final int CHECKICON_ICON_MOUSEOVER_SELECTED_FOCUSED = 18;
	public static final int CHECKICON_ICON_DISABLED_SELECTED = 19;

	protected URL url;

	// the following 4 variables are reused during the painting code of the
	// layers
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

	// All Colors used for painting are stored here. Ideally, only those colors
	// being used
	// by a particular instance of CheckBoxMenuItemPainter would be created. For
	// the moment at least,
	// however, all are created for each instance.
	protected final Color color1 = this.decodeColor("nimbusSelection", 0.0f, 0.0f, 0.0f, 0);
	protected final Color color2 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.08983666f, -0.17647058f, 0);
	protected final Color color4 = this.decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);

	protected Paint backgroundColorDisabled;
	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorSelected;
	protected Paint backgroundColorMouseOver;
	protected Paint backgroundColorMouseOverSelected;

	// Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	protected Image image;

	public OCheckBoxMenuItemPainter(int state, PaintContext ctx) {
		super(state, ctx);
	}

	public OCheckBoxMenuItemPainter(int state, PaintContext ctx, URL url) {
		super(state, ctx);
		this.url = url;
	}

	public Image getImage() {
		if (this.image == null) {
			if (this.url != null) {
				this.image = new ImageIcon(this.url).getImage();
			}
		}
		return this.image;
	}

	@Override
	protected String getComponentKeyName() {
		return "CheckBoxMenuItem";
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		// populate componentColors array with colors calculated in
		// getExtendedCacheKeys call
		this.componentColors = extendedCacheKeys;

		switch (this.state) {
		case BACKGROUND_MOUSEOVER:
			this.paintBackgroundMouseOver(g);
			break;
		case BACKGROUND_SELECTED_MOUSEOVER:
			this.paintBackgroundSelectedAndMouseOver(g);
			break;
		case BACKGROUND_ENABLED:
			this.paintBackgroundEnabled(g);
			break;
		case BACKGROUND_DISABLED:
			this.paintBackgroundDisabled(g);
			break;
		case BACKGROUND_SELECTED:
			this.paintBackgroundSelected(g);
			break;

		case CHECKICON_ICON_DISABLED:
		case CHECKICON_ICON_ENABLED:
		case CHECKICON_ICON_FOCUSED:
		case CHECKICON_ICON_MOUSEOVER:
		case CHECKICON_ICON_MOUSEOVER_FOCUSED:
		case CHECKICON_ICON_PRESSED:
		case CHECKICON_ICON_PRESSED_FOCUSED:
		case CHECKICON_ICON_SELECTED:
		case CHECKICON_ICON_SELECTED_FOCUSED:
		case CHECKICON_ICON_PRESSED_SELECTED:
		case CHECKICON_ICON_PRESSED_SELECTED_FOCUSED:
		case CHECKICON_ICON_MOUSEOVER_SELECTED:
		case CHECKICON_ICON_MOUSEOVER_SELECTED_FOCUSED:
		case CHECKICON_ICON_DISABLED_SELECTED:
			this.paintImage(g);
			break;
		}
	}

	@Override
	protected PaintContext getPaintContext() {
		return this.ctx;
	}

	/**
	 * Get configuration properties to be used in this painter (such as:
	 * *BorderPainter and *BgPainter). As usual, it is checked the
	 * OLAFCustomConfig.properties, and then in OLAFDefaultConfig.properties.
	 *
	 * Moreover, if there are not values for that properties in both, the
	 * default Nimbus configuration values are set, due to, those properties are
	 * needed to paint and used by the Ontimize L&F, so, there are not Nimbus
	 * values for that, so, default values are always configured based on Nimbus
	 * values.
	 *
	 */
	@Override
	protected void init() {

		// disable:
		Object obj = UIManager.getLookAndFeelDefaults().get("CheckBoxMenuItem[Disabled].background");
		if (obj instanceof Paint) {
			this.backgroundColorDisabled = (Paint) obj;
		} else {
			this.backgroundColorDisabled = this.color4;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get("CheckBoxMenuItem[Enabled].background");
		if (obj instanceof Paint) {
			this.backgroundColorEnabled = (Paint) obj;
		} else {
			this.backgroundColorEnabled = this.color2;
		}

		// selected:
		obj = UIManager.getLookAndFeelDefaults().get("CheckBoxMenuItem[Selected].background");
		if (obj instanceof Paint) {
			this.backgroundColorSelected = (Paint) obj;
		} else {
			this.backgroundColorSelected = this.color1;
		}

		// mouseover:
		obj = UIManager.getLookAndFeelDefaults().get("CheckBoxMenuItem[MouseOver].background");
		if (obj instanceof Paint) {
			this.backgroundColorMouseOver = (Paint) obj;
		} else {
			this.backgroundColorMouseOver = this.color1;
		}

		// selected+mouseover:
		obj = UIManager.getLookAndFeelDefaults().get("CheckBoxMenuItem[MouseOver+Selected].background");
		if (obj instanceof Paint) {
			this.backgroundColorMouseOverSelected = (Paint) obj;
		} else {
			this.backgroundColorMouseOverSelected = this.color1;
		}

	}

	protected void paintBackgroundMouseOver(Graphics2D g) {
		this.rect = this.decodeRect1();
		g.setPaint(this.backgroundColorMouseOver);
		g.fill(this.rect);

	}

	protected void paintBackgroundEnabled(Graphics2D g) {
		this.rect = this.decodeRect1();
		g.setPaint(this.backgroundColorEnabled);
		g.fill(this.rect);

	}

	protected void paintBackgroundDisabled(Graphics2D g) {
		this.rect = this.decodeRect1();
		g.setPaint(this.backgroundColorDisabled);
		g.fill(this.rect);

	}

	protected void paintBackgroundSelected(Graphics2D g) {
		this.rect = this.decodeRect1();
		g.setPaint(this.backgroundColorSelected);
		g.fill(this.rect);

	}

	protected void paintBackgroundSelectedAndMouseOver(Graphics2D g) {
		this.rect = this.decodeRect1();
		g.setPaint(this.backgroundColorMouseOverSelected);
		g.fill(this.rect);

	}

	protected void paintImage(Graphics2D g) {
		g.drawImage(this.getImage(), (int) this.decodeX(0f), // x
				(int) this.decodeY(0f), // y
				(int) (this.decodeX(3f) - this.decodeX(0f)), // width
				(int) (this.decodeY(3f) - this.decodeY(0f)), null);
	}

	protected Rectangle2D decodeRect1() {
		this.rect.setRect(this.decodeX(0.0f), // x
				this.decodeY(1.0f), // y
				this.decodeX(3.0f), // width
				this.decodeY(2.0f) - this.decodeY(1.0f)); // height
		return this.rect;
	}

}