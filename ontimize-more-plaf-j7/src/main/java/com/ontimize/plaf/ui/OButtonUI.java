package com.ontimize.plaf.ui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;
import javax.swing.text.View;

import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.utils.ContextUtils;

/**
 * OntimizeButtonUI implementation.
 *
 * <p>
 * Based on SynthButtonUI by Scott Violet.
 * </p>
 *
 * @see javax.swing.plaf.synth.SynthButtonUI
 */
public class OButtonUI extends BasicButtonUI implements PropertyChangeListener,
		SynthUI {
	protected static final String APPLE_PREFIX = "JButton.";
	protected SynthStyle style;

	/**
	 * Create a new UI delegate.
	 *
	 * @param c
	 *            the component.
	 *
	 * @return the UI delegate.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new OButtonUI();
	}

	/**
	 * @see javax.swing.plaf.basic.BasicButtonUI#installDefaults(javax.swing.AbstractButton)
	 */
	protected void installDefaults(AbstractButton b) {
		updateStyle(b);

		LookAndFeel.installProperty(b, "rolloverEnabled", Boolean.TRUE);
	}

	/**
	 * @see javax.swing.plaf.basic.BasicButtonUI#installListeners(javax.swing.AbstractButton)
	 */
	protected void installListeners(AbstractButton b) {
		super.installListeners(b);
		b.addPropertyChangeListener(this);
	}

	/**
	 * Update the style of the button.
	 *
	 * @param b
	 *            the button.
	 */
	public void updateStyle(AbstractButton b) {
		SynthContext context = getContext(b, SynthConstants.ENABLED);
		SynthStyle oldStyle = style;

		style = OntimizeLookAndFeel.updateStyle(context, this);
		if (style != oldStyle) {
			if (b.getMargin() == null || (b.getMargin() instanceof UIResource)) {
				Insets margin = (Insets) style.get(context, getPropertyPrefix()
						+ "margin");

				if (margin == null) {
					// Some places assume margins are non-null.
					margin = OntimizeLookAndFeel.EMPTY_UIRESOURCE_INSETS;
				}

				b.setMargin(margin);
			}

			Object value = style.get(context, getPropertyPrefix()
					+ "iconTextGap");

			if (value != null) {
				LookAndFeel.installProperty(b, "iconTextGap", value);
			}

			value = style.get(context, getPropertyPrefix()
					+ "contentAreaFilled");
			LookAndFeel.installProperty(b, "contentAreaFilled",
					value != null ? value : Boolean.TRUE);

			value = b.getClientProperty(APPLE_PREFIX + "buttonType");
			if (value != null) {
				if ("segmented".equals(value)) {
					b.setMargin(OntimizeLookAndFeel.EMPTY_UIRESOURCE_INSETS);
				}
			}

			if (oldStyle != null) {
				uninstallKeyboardActions(b);
				installKeyboardActions(b);
			}
		}

	}

	/**
	 * @see javax.swing.plaf.basic.BasicButtonUI#uninstallListeners(javax.swing.AbstractButton)
	 */
	protected void uninstallListeners(AbstractButton b) {
		super.uninstallListeners(b);
		b.removePropertyChangeListener(this);
	}

	/**
	 * @see javax.swing.plaf.basic.BasicButtonUI#uninstallDefaults(javax.swing.AbstractButton)
	 */
	protected void uninstallDefaults(AbstractButton b) {
		SynthContext context = getContext(b, ENABLED);

		style.uninstallDefaults(context);

		style = null;
	}

	/**
	 * @see sun.swing.plaf.synth.SynthUI#getContext(javax.swing.JComponent)
	 */
	public SynthContext getContext(JComponent c) {
		return getContext(c, getComponentState(c));
	}

	/**
	 * Get the Synth context.
	 *
	 * @param c
	 *            the component.
	 * @param state
	 *            the state.
	 *
	 * @return the Synth context.
	 */
	protected SynthContext getContext(JComponent c, int state) {
		Region region = getRegion(c);
		if (this.style == null) {
			this.style = OntimizeLookAndFeel.getOntimizeStyle(c,
					OntimizeLookAndFeel.getRegion(c));
		}
		return new SynthContext(c, region, this.style, state);
	}

	/**
	 * Get the region.
	 *
	 * @param c
	 *            the component.
	 *
	 * @return the region.
	 */
	protected Region getRegion(JComponent c) {
		return SynthLookAndFeel.getRegion(c);
	}

	/**
	 * Returns the current state of the passed in <code>AbstractButton</code>.
	 *
	 * @param c
	 *            the button component.
	 *
	 * @return the button's state.
	 */
	protected int getComponentState(JComponent c) {
		int state = ENABLED;

		if (!c.isEnabled()) {
			state = DISABLED;
		}

		if (OntimizeLookAndFeel.selectedUI == this) {
			return OntimizeLookAndFeel.selectedUIState | SynthConstants.ENABLED;
		}

		AbstractButton button = (AbstractButton) c;
		ButtonModel model = button.getModel();

		if (model.isPressed()) {
			if (model.isArmed()) {
				state = PRESSED;
			} else {
				state = MOUSE_OVER;
			}
		}

		if (model.isRollover()) {
			state |= MOUSE_OVER;
		}

		if (model.isSelected()) {
			state |= SELECTED;
		}

		if (c.isFocusOwner() && button.isFocusPainted()) {
			state |= FOCUSED;
		}

		if ((c instanceof JButton) && ((JButton) c).isDefaultButton()) {
			state |= DEFAULT;
		}

		return state;
	}

	/**
	 * @see javax.swing.plaf.basic.BasicButtonUI#getBaseline(javax.swing.JComponent,
	 *      int, int)
	 */
	public int getBaseline(JComponent c, int width, int height) {
		if (c == null) {
			throw new NullPointerException("Component must be non-null");
		}

		if (width < 0 || height < 0) {
			throw new IllegalArgumentException("Width and height must be >= 0");
		}

		AbstractButton b = (AbstractButton) c;
		String text = b.getText();

		if (text == null || "".equals(text)) {
			return -1;
		}

		Insets i = b.getInsets();
		Rectangle viewRect = new Rectangle();
		Rectangle textRect = new Rectangle();
		Rectangle iconRect = new Rectangle();

		viewRect.x = i.left;
		viewRect.y = i.top;
		viewRect.width = width - (i.right + viewRect.x);
		viewRect.height = height - (i.bottom + viewRect.y);

		// layout the text and icon
		SynthContext context = getContext(b);
		FontMetrics fm = context.getComponent().getFontMetrics(
				context.getStyle().getFont(context));

		context.getStyle()
				.getGraphicsUtils(context)
				.layoutText(context, fm, b.getText(), b.getIcon(),
						b.getHorizontalAlignment(), b.getVerticalAlignment(),
						b.getHorizontalTextPosition(),
						b.getVerticalTextPosition(), viewRect, iconRect,
						textRect, b.getIconTextGap());
		View view = (View) b.getClientProperty(BasicHTML.propertyKey);
		int baseline;

		if (view != null) {
			baseline = BasicHTML.getHTMLBaseline(view, textRect.width,
					textRect.height);
			if (baseline >= 0) {
				baseline += textRect.y;
			}
		} else {
			baseline = textRect.y + fm.getAscent();
		}

		return baseline;
	}

	// ********************************
	// Paint Methods
	// ********************************

	/**
	 * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics,
	 *      javax.swing.JComponent)
	 */
	public void update(Graphics g, JComponent c) {
		SynthContext context = getContext(c);

		OntimizeLookAndFeel.update(context, g);
		paintBackground(context, g, c);
		paint(context, g);

	}

	/**
	 * @see javax.swing.plaf.basic.BasicButtonUI#paint(java.awt.Graphics,
	 *      javax.swing.JComponent)
	 */
	public void paint(Graphics g, JComponent c) {
		SynthContext context = getContext(c);

		paint(context, g);

	}

	/**
	 * Paint the button.
	 *
	 * @param context
	 *            the Synth context.
	 * @param g
	 *            the Graphics context.
	 */
	protected void paint(SynthContext context, Graphics g) {
		AbstractButton b = (AbstractButton) context.getComponent();

		g.setColor(context.getStyle().getColor(context,
				ColorType.TEXT_FOREGROUND));
		g.setFont(style.getFont(context));
		context.getStyle()
				.getGraphicsUtils(context)
				.paintText(context, g, b.getText(), getIcon(b),
						b.getHorizontalAlignment(), b.getVerticalAlignment(),
						b.getHorizontalTextPosition(),
						b.getVerticalTextPosition(), b.getIconTextGap(),
						b.getDisplayedMnemonicIndex(),
						getTextShiftOffset(context));
	}

	/**
	 * Paint the button background.
	 *
	 * @param context
	 *            the Synth context.
	 * @param g
	 *            the Graphics context.
	 * @param c
	 *            the button component.
	 */
	void paintBackground(SynthContext context, Graphics g, JComponent c) {
		if (((AbstractButton) c).isContentAreaFilled()) {
			ContextUtils.getPainter(context).paintButtonBackground(context, g,
					0, 0, c.getWidth(), c.getHeight());
		}
	}

	/**
	 * @see sun.swing.plaf.synth.SynthUI#paintBorder(javax.swing.plaf.synth.SynthContext,
	 *      java.awt.Graphics, int, int, int, int)
	 */
	public void paintBorder(SynthContext context, Graphics g, int x, int y,
			int w, int h) {
		ContextUtils.getPainter(context).paintButtonBorder(context, g, x, y, w,
				h);
	}

	/**
	 * Returns the default icon. This should NOT callback to the JComponent.
	 *
	 * @param b
	 *            AbstractButton the iocn is associated with.
	 *
	 * @return the default icon.
	 */

	protected Icon getDefaultIcon(AbstractButton b) {
		SynthContext context = getContext(b);
		Icon icon = context.getStyle().getIcon(context,
				getPropertyPrefix() + "icon");

		return icon;
	}

	/**
	 * Returns the Icon to use in painting the button.
	 *
	 * @param b
	 *            the button.
	 *
	 * @return the icon.
	 */
	protected Icon getIcon(AbstractButton b) {
		Icon icon = b.getIcon();
		ButtonModel model = b.getModel();

		if (!model.isEnabled()) {
			icon = getSynthDisabledIcon(b, icon);
		} else if (model.isPressed() && model.isArmed()) {
			icon = getPressedIcon(b, getSelectedIcon(b, icon));
		} else if (b.isRolloverEnabled() && model.isRollover()) {
			icon = getRolloverIcon(b, getSelectedIcon(b, icon));
		} else if (model.isSelected()) {
			icon = getSelectedIcon(b, icon);
		} else {
			icon = getEnabledIcon(b, icon);
		}

		if (icon == null) {
			return getDefaultIcon(b);
		}

		return icon;
	}

	/**
	 * This method will return the icon that should be used for a button. We
	 * only want to use the synth icon defined by the style if the specific icon
	 * has not been defined for the button state and the backup icon is a
	 * UIResource (we set it, not the developer).
	 *
	 * @param b
	 *            button
	 * @param specificIcon
	 *            icon returned from the button for the specific state
	 * @param defaultIcon
	 *            fallback icon
	 * @param state
	 *            the synth state of the button
	 *
	 * @return the icon.
	 */
	protected Icon getIcon(AbstractButton b, Icon specificIcon,
			Icon defaultIcon, int state) {
		Icon icon = specificIcon;

		if (icon == null) {
			if (defaultIcon instanceof UIResource) {
				icon = getSynthIcon(b, state);
				if (icon == null) {
					icon = defaultIcon;
				}
			} else {
				icon = defaultIcon;
			}
		}

		return icon;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param b
	 *            DOCUMENT ME!
	 * @param synthConstant
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected Icon getSynthIcon(AbstractButton b, int synthConstant) {
		return style.getIcon(getContext(b, synthConstant), getPropertyPrefix()
				+ "icon");
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param b
	 *            DOCUMENT ME!
	 * @param defaultIcon
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected Icon getEnabledIcon(AbstractButton b, Icon defaultIcon) {
		if (defaultIcon == null) {
			defaultIcon = getSynthIcon(b, SynthConstants.ENABLED);
		}

		return defaultIcon;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param b
	 *            DOCUMENT ME!
	 * @param defaultIcon
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected Icon getSelectedIcon(AbstractButton b, Icon defaultIcon) {
		return getIcon(b, b.getSelectedIcon(), defaultIcon,
				SynthConstants.SELECTED);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param b
	 *            DOCUMENT ME!
	 * @param defaultIcon
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected Icon getRolloverIcon(AbstractButton b, Icon defaultIcon) {
		ButtonModel model = b.getModel();
		Icon icon;

		if (model.isSelected()) {
			icon = getIcon(b, b.getRolloverSelectedIcon(), defaultIcon,
					SynthConstants.MOUSE_OVER | SynthConstants.SELECTED);
		} else {
			icon = getIcon(b, b.getRolloverIcon(), defaultIcon,
					SynthConstants.MOUSE_OVER);
		}

		return icon;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param b
	 *            DOCUMENT ME!
	 * @param defaultIcon
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected Icon getPressedIcon(AbstractButton b, Icon defaultIcon) {
		return getIcon(b, b.getPressedIcon(), defaultIcon,
				SynthConstants.PRESSED);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param b
	 *            DOCUMENT ME!
	 * @param defaultIcon
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected Icon getSynthDisabledIcon(AbstractButton b, Icon defaultIcon) {
		ButtonModel model = b.getModel();
		Icon icon;

		if (model.isSelected()) {
			icon = getIcon(b, b.getDisabledSelectedIcon(), defaultIcon,
					SynthConstants.DISABLED | SynthConstants.SELECTED);
		} else {
			icon = getIcon(b, b.getDisabledIcon(), defaultIcon,
					SynthConstants.DISABLED);
		}

		return icon;
	}

	/**
	 * Returns the amount to shift the text/icon when painting.
	 *
	 * @param state
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected int getTextShiftOffset(SynthContext state) {
		AbstractButton button = (AbstractButton) state.getComponent();
		ButtonModel model = button.getModel();

		if (model.isArmed() && model.isPressed()
				&& button.getPressedIcon() == null) {
			return state.getStyle().getInt(state,
					getPropertyPrefix() + "textShiftOffset", 0);
		}

		return 0;
	}

	// ********************************
	// Layout Methods
	// ********************************
	/**
	 * @see javax.swing.plaf.basic.BasicButtonUI#getMinimumSize(javax.swing.JComponent)
	 */
	public Dimension getMinimumSize(JComponent c) {
		if (c.getComponentCount() > 0 && c.getLayout() != null) {
			return null;
		}

		AbstractButton b = (AbstractButton) c;
		SynthContext ss = getContext(c);
		final SynthStyle style2 = ss.getStyle();
		Dimension size = style2.getGraphicsUtils(ss).getMinimumSize(ss,
				style2.getFont(ss), b.getText(), getSizingIcon(b),
				b.getHorizontalAlignment(), b.getVerticalAlignment(),
				b.getHorizontalTextPosition(), b.getVerticalTextPosition(),
				b.getIconTextGap(), b.getDisplayedMnemonicIndex());

		return size;
	}

	/**
	 * @see javax.swing.plaf.basic.BasicButtonUI#getPreferredSize(javax.swing.JComponent)
	 */
	public Dimension getPreferredSize(JComponent c) {
		if (c.getComponentCount() > 0 && c.getLayout() != null) {
			return null;
		}

		AbstractButton b = (AbstractButton) c;
		SynthContext ss = getContext(c);
		SynthStyle style2 = ss.getStyle();
		SynthGraphicsUtils graphicsUtils = style2.getGraphicsUtils(ss);
		Dimension size = graphicsUtils.getPreferredSize(ss, style2.getFont(ss),
				b.getText(), getSizingIcon(b), b.getHorizontalAlignment(),
				b.getVerticalAlignment(), b.getHorizontalTextPosition(),
				b.getVerticalTextPosition(), b.getIconTextGap(),
				b.getDisplayedMnemonicIndex());

		// Make height odd.
		size.height &= ~1;
		return size;
	}

	/**
	 * @see javax.swing.plaf.basic.BasicButtonUI#getMaximumSize(javax.swing.JComponent)
	 */
	public Dimension getMaximumSize(JComponent c) {
		if (c.getComponentCount() > 0 && c.getLayout() != null) {
			return null;
		}

		AbstractButton b = (AbstractButton) c;
		SynthContext ss = getContext(c);
		final SynthStyle style2 = ss.getStyle();
		Dimension size = style2.getGraphicsUtils(ss).getMaximumSize(ss,
				style2.getFont(ss), b.getText(), getSizingIcon(b),
				b.getHorizontalAlignment(), b.getVerticalAlignment(),
				b.getHorizontalTextPosition(), b.getVerticalTextPosition(),
				b.getIconTextGap(), b.getDisplayedMnemonicIndex());

		return size;
	}

	/**
	 * Returns the Icon used in calculating the pref/min/max size.
	 *
	 * @param b
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected Icon getSizingIcon(AbstractButton b) {
		// NOTE: this is slightly different than BasicButtonUI, where it
		// would just use getIcon, but this should be ok.
		Icon icon = (b.isEnabled() || b.getDisabledIcon() == null) ? b
				.getIcon() : b.getDisabledIcon();

		if (icon == null) {
			icon = getDefaultIcon(b);
		}

		return icon;
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent e) {
		if (OntimizeLookAndFeel.shouldUpdateStyle(e)) {
			updateStyle((AbstractButton) e.getSource());
		}
	}
}
