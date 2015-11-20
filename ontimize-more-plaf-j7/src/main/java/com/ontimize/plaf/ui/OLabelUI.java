package com.ontimize.plaf.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.beans.PropertyChangeEvent;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;
import javax.swing.text.View;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.ontimize.gui.calendar.VisualCalendarComponent;
import com.ontimize.gui.manager.TabbedFormManager.ButtonTabComponent;
import com.ontimize.gui.tree.BasicTreeCellRenderer;
import com.ontimize.gui.tree.Tree;
import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.ui.OComboBoxUI.SynthComboBoxRenderer;
import com.ontimize.plaf.utils.ContextUtils;
import com.ontimize.util.swing.popuplist.PopupItem;

/**
 * OntimizeLabelUI.
 *
 * Based on SynthLabelUI by Scott Violet.
 *
 * @see javax.swing.plaf.synth.SynthLabelUI
 */
public class OLabelUI extends BasicLabelUI implements SynthUI {
	protected SynthStyle style;

	/**
	 * Returns the LabelUI implementation used for the skins look and feel.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new OLabelUI();
	}

	@Override
	protected void installDefaults(JLabel c) {
		String compName = c.getName();
		if ("ELabel".equals(compName)) {
			OntimizeLookAndFeel.installColorsAndFont(c, "\"ELabel\".background", "\"ELabel\".foreground", "\"ELabel\".font");
			LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
		} else if (c instanceof BasicTreeCellRenderer) {
			OntimizeLookAndFeel.installColorsAndFont(c, "Tree:\"Tree.cellRenderer\".background", "Tree:\"Tree.cellRenderer\".foreground", "Tree:\"Tree.cellRenderer\".font");
			LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
		} else if ("PageFetcher.Label".equals(compName)) {
			OntimizeLookAndFeel.installColorsAndFont(c, "\"PageFetcher.Label\".background", "\"PageFetcher.Label\".foreground", "\"PageFetcher.Label\".font");
			LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
		} else if ("TableHeader.renderer".equals(compName)) {
			OntimizeLookAndFeel.installColorsAndFont(c, "TableHeader:\"TableHeader.renderer\".background", "TableHeader:\"TableHeader.renderer\".foreground",
					"TableHeader:\"TableHeader.renderer\".font");
			LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
		} else if ((compName != null) && (compName.length() > 0)) {
			OntimizeLookAndFeel.installColorsAndFont(c, compName + ".background", compName + ".foreground", compName + ".font");
			LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
		} else {
			super.installDefaults(c);
		}
		this.updateStyle(c);
	}

	void updateStyle(JLabel c) {

		SynthContext context = this.getContext(c, SynthConstants.ENABLED);

		this.style = OntimizeLookAndFeel.updateStyle(context, this);

		Object iconTextGap = this.style.get(context, ".iconTextGap");
		if (iconTextGap instanceof Integer) {
			c.setIconTextGap((Integer) iconTextGap);
		}

	}

	@Override
	protected void uninstallDefaults(JLabel c) {
		SynthContext context = this.getContext(c, SynthConstants.ENABLED);
		this.style.uninstallDefaults(context);

		this.style = null;
	}

	@Override
	public SynthContext getContext(JComponent c) {
		return this.getContext(c, this.getComponentState(c));
	}

	protected SynthContext getContext(JComponent c, int state) {
		if (this.style == null) {
			this.style = OntimizeLookAndFeel.getOntimizeStyle(c, OntimizeLookAndFeel.getRegion(c));
		}
		return new SynthContext(c, OntimizeLookAndFeel.getRegion(c), this.style, state);
	}

	protected int getComponentState(JComponent c) {
		int state = OntimizeLookAndFeel.getComponentState(c);
		if ((OntimizeLookAndFeel.getSelectedUI() == this) && (state == SynthConstants.ENABLED)) {
			state = OntimizeLookAndFeel.getSelectedUIState() | SynthConstants.ENABLED;
		}

		if (c instanceof PopupItem) {
			if (((PopupItem) c).isMouseOver()) {
				return SynthConstants.MOUSE_OVER;
			}
		}

		if (c instanceof SynthComboBoxRenderer) {
			state = SynthConstants.ENABLED;
		}

		if (c.getParent() instanceof ButtonTabComponent) {
			JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, c);
			if ((ButtonTabComponent) c.getParent() == tabbedPane.getTabComponentAt(tabbedPane.getSelectedIndex())) {
				state = SynthConstants.SELECTED;
				if (c.getParent().hasFocus()) {
					state |= SynthConstants.FOCUSED;
				}
			}
		}

		return state;
	}

	@Override
	public int getBaseline(JComponent c, int width, int height) {
		if (c == null) {
			throw new NullPointerException("Component must be non-null");
		}
		if ((width < 0) || (height < 0)) {
			throw new IllegalArgumentException("Width and height must be >= 0");
		}
		JLabel label = (JLabel) c;
		String text = label.getText();
		if ((text == null) || "".equals(text)) {
			return -1;
		}
		Insets i = label.getInsets();
		Rectangle viewRect = new Rectangle();
		Rectangle textRect = new Rectangle();
		Rectangle iconRect = new Rectangle();
		viewRect.x = i.left;
		viewRect.y = i.top;
		viewRect.width = width - (i.right + viewRect.x);
		viewRect.height = height - (i.bottom + viewRect.y);

		// layout the text and icon
		SynthContext context = this.getContext(label);
		FontMetrics fm = context.getComponent().getFontMetrics(context.getStyle().getFont(context));
		context.getStyle()
		.getGraphicsUtils(context)
		.layoutText(context, fm, label.getText(), label.getIcon(), label.getHorizontalAlignment(), label.getVerticalAlignment(), label.getHorizontalTextPosition(),
				label.getVerticalTextPosition(), viewRect, iconRect, textRect, label.getIconTextGap());
		View view = (View) label.getClientProperty(BasicHTML.propertyKey);
		int baseline;
		if (view != null) {
			baseline = BasicHTML.getHTMLBaseline(view, textRect.width, textRect.height);
			if (baseline >= 0) {
				baseline += textRect.y;
			}
		} else {
			baseline = textRect.y + fm.getAscent();
		}

		return baseline;
	}

	/**
	 * Notifies this UI delegate that it's time to paint the specified
	 * component. This method is invoked by <code>JComponent</code> when the
	 * specified component is being painted.
	 */
	@Override
	public void update(Graphics g, JComponent c) {
		SynthContext context = this.getContext(c);

		OntimizeLookAndFeel.update(context, g);
		ContextUtils.getPainter(context).paintLabelBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
		this.paint(context, g);

	}

	@Override
	public void paint(Graphics g, JComponent c) {
		SynthContext context = this.getContext(c);

		this.paint(context, g);

	}

	protected void paint(SynthContext context, Graphics g) {
		JLabel label = (JLabel) context.getComponent();
		Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

		g.setColor(context.getStyle().getColor(context, ColorType.TEXT_FOREGROUND));
		g.setFont(this.style.getFont(context));

		Border oldBorder = label.getBorder();
		if (label instanceof BasicTreeCellRenderer) {

			BasicTreeCellRenderer renderer = (BasicTreeCellRenderer) label;

			if (!(renderer.isLeaf() || renderer.isRoot())) {
				Insets insets = label.getInsets();
				int size = this.drawBasicTreeCellRendererChildCount((Graphics2D) g, (BasicTreeCellRenderer) label);
				insets.right = insets.right + size;
				EmptyBorder border = new EmptyBorder(insets);
				label.setBorder(border);
			}
		}

		if (label instanceof VisualCalendarComponent.HeaderRenderer) {
			Color oldColor = g.getColor();
			Object oColor = OntimizeLookAndFeel.lookup("TableHeader:\"VisualCalendar:TableHeader.renderer\".foregroundShadow");
			if (oColor instanceof Color) {
				g.setColor((Color) oColor);
			} else {
				// Default color...
				g.setColor(new Color(0x00000033));
			}
			context.getStyle()
			.getGraphicsUtils(context)
			.paintText(context, g, label.getText(), null, label.getHorizontalAlignment(), label.getVerticalAlignment(), label.getHorizontalTextPosition(),
					label.getVerticalTextPosition(), label.getIconTextGap(), label.getDisplayedMnemonicIndex(), 1);
			oColor = OntimizeLookAndFeel.lookup("TableHeader:\"VisualCalendar:TableHeader.renderer\".foreground");
			if (oColor instanceof Color) {
				g.setColor((Color) oColor);
			} else {
				g.setColor(oldColor);
			}
		}

		context.getStyle()
		.getGraphicsUtils(context)
		.paintText(context, g, label.getText(), icon, label.getHorizontalAlignment(), label.getVerticalAlignment(), label.getHorizontalTextPosition(),
				label.getVerticalTextPosition(), label.getIconTextGap(), label.getDisplayedMnemonicIndex(), 0);

		if (label instanceof BasicTreeCellRenderer) {
			label.setBorder(oldBorder);
		}

	}

	protected int getLabelStar(JLabel label) {
		Icon current = label.getIcon();
		if (current == null) {
			return 0;
		}
		return current.getIconWidth();
	}

	protected int drawBasicTreeCellRendererChildCount(Graphics2D g, BasicTreeCellRenderer renderer) {

		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int childCount = renderer.getChildCount();
		if (Tree.enabledRowCount == false) {
			return 0;
		}

		if (childCount == -1) {
			return 0;
		}
		int h = renderer.getHeight();
		int w = renderer.getWidth();
		int height = h - 6;

		Font oldFont = renderer.getFont();
		Font font = oldFont.deriveFont(oldFont.getSize2D() - 1.0f);

		// FontMetrics fm = SwingUtilities2.getFontMetrics(renderer, g);
		FontMetrics fm = renderer.getFontMetrics(font);
		g.setFont(font);
		int textWidth = Math.max(fm.stringWidth(String.valueOf(childCount)) - (height / 2), 0);

		int width = height + textWidth;
		if (w < (width + this.getLabelStar(renderer))) {
			return 0;
		}

		int x1 = w - width - 5;
		int y1 = 3;

		g.translate(x1, y1);
		Color ant = g.getColor();

		if (renderer.isSelected()) {
			Object obj = UIManager.getLookAndFeelDefaults().get("Tree:\"Tree.cellRenderer\".backgroundSelectionCount");
			if (obj instanceof Color) {
				g.setColor((Color) obj);
			} else {
				g.setColor(renderer.getBackgroundNonSelectionColor());
			}
		} else {
			Object obj = UIManager.getLookAndFeelDefaults().get("Tree:\"Tree.cellRenderer\".backgroundChildCount");
			if (obj instanceof Paint) {
				g.setPaint((Paint) obj);
			} else {
				g.setColor(renderer.getBackgroundSelectionColor());
			}
		}

		Shape circle = ShapeFactory.getInstace().createSemiCircle(height / 2, 0, height, ShapeFactory.ANTICLOCKWISE, false);
		g.fill(circle);

		if (textWidth > 0) {
			g.fillRect(height / 2, 0, textWidth, height);
		}

		circle = ShapeFactory.getInstace().createSemiCircle((height / 2) + textWidth, 0, height, ShapeFactory.CLOCKWISE, false);
		g.fill(circle);

		if (renderer.isSelected()) {
			Object obj = UIManager.getLookAndFeelDefaults().get("Tree:\"Tree.cellRenderer\".foregroundSelectionChildCount");
			if (obj instanceof Paint) {
				g.setPaint((Paint) obj);
			} else {
				g.setColor(renderer.getBackgroundSelectionColor());
			}
		} else {
			Object obj = UIManager.getLookAndFeelDefaults().get("Tree:\"Tree.cellRenderer\".foregroundChildCount");
			if (obj instanceof Paint) {
				g.setPaint((Paint) obj);
			} else {
				g.setColor(renderer.getTextSelectionColor());
			}
		}

		int letterHeight = fm.getAscent();
		int letterY = (height / 2) + (letterHeight / 2);
		int letterX = (height / 2) - (fm.stringWidth("8") / 2);

		g.drawString("" + childCount, letterX, letterY);
		g.setColor(ant);
		g.translate(-x1, -y1);

		g.setPaint(previousPaint);
		g.setRenderingHints(rh);

		g.setFont(oldFont);
		return height;
	}

	@Override
	public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
		ContextUtils.getPainter(context).paintLabelBorder(context, g, x, y, w, h);
	}

	@Override
	public Dimension getPreferredSize(JComponent c) {
		JLabel label = (JLabel) c;
		Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();
		SynthContext context = this.getContext(c);
		Dimension size = context
				.getStyle()
				.getGraphicsUtils(context)
				.getPreferredSize(context, context.getStyle().getFont(context), label.getText(), icon, label.getHorizontalAlignment(), label.getVerticalAlignment(),
						label.getHorizontalTextPosition(), label.getVerticalTextPosition(), label.getIconTextGap(), label.getDisplayedMnemonicIndex());

		if (c instanceof DefaultTreeCellRenderer) {
			size.height = size.height;
			return size;
		}
		if (size != null) {
			size.height = size.height + 4;
		}
		return size;
	}

	@Override
	public Dimension getMinimumSize(JComponent c) {
		JLabel label = (JLabel) c;
		Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();
		SynthContext context = this.getContext(c);
		Dimension size = context
				.getStyle()
				.getGraphicsUtils(context)
				.getMinimumSize(context, context.getStyle().getFont(context), label.getText(), icon, label.getHorizontalAlignment(), label.getVerticalAlignment(),
						label.getHorizontalTextPosition(), label.getVerticalTextPosition(), label.getIconTextGap(), label.getDisplayedMnemonicIndex());

		if (c instanceof DefaultTreeCellRenderer) {
			return size;
		}

		if (size != null) {
			size.height = size.height + 4;
		}

		return size;
	}

	@Override
	public Dimension getMaximumSize(JComponent c) {
		JLabel label = (JLabel) c;
		Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();
		SynthContext context = this.getContext(c);
		Dimension size = context
				.getStyle()
				.getGraphicsUtils(context)
				.getMaximumSize(context, context.getStyle().getFont(context), label.getText(), icon, label.getHorizontalAlignment(), label.getVerticalAlignment(),
						label.getHorizontalTextPosition(), label.getVerticalTextPosition(), label.getIconTextGap(), label.getDisplayedMnemonicIndex());

		return size;
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);
		if (OntimizeLookAndFeel.shouldUpdateStyle(e)) {
			this.updateStyle((JLabel) e.getSource());
		}
	}
}
