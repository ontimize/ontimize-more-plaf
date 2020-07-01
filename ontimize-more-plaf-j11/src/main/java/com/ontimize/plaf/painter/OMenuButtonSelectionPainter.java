package com.ontimize.plaf.painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class OMenuButtonSelectionPainter extends AbstractOButtonPainter {

	public static Color defaultFocusBgColor = new Color(0x366581);
	public static Color defaultMouseOverBgColor = new Color(0x366581);
	public static Color defaultFocusedMouseOverBgColor = new Color(0x366581);
	protected Color focusBgColor;
	protected Color mouseOverBgColor;
	protected Color focusedMouseOverBgColor;

	protected Paint foregroundColorDisabled;
	protected Paint foregroundColorEnabled;
	protected Paint foregroundColorMouseOver;
	protected Paint foregroundColorPressed;
	protected Paint foregroundColorFocused;

	protected Path2D path = new Path2D.Float();

	public OMenuButtonSelectionPainter(int state, PaintContext ctx) {
		super(state, ctx);
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		this.componentColors = extendedCacheKeys;
		super.doPaint(g, c, width, height, extendedCacheKeys);
	}

	@Override
	protected String getComponentKeyName() {
		return "\"MenuButtonSelection\"";
	}

	@Override
	protected void init() {
		super.init();

		Object obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Focused].background");
		if (obj instanceof Color) {
			this.focusBgColor = (Color) obj;
		} else {
			this.focusBgColor = OMenuButtonSelectionPainter.defaultFocusBgColor;
		}

		obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[MouseOver].background");
		if (obj instanceof Color) {
			this.mouseOverBgColor = (Color) obj;
		} else {
			this.mouseOverBgColor = OMenuButtonSelectionPainter.defaultMouseOverBgColor;
		}

		obj = UIManager.getDefaults().get(this.getComponentKeyName() + "[Focused+MouseOver].background");
		if (obj instanceof Color) {
			this.focusedMouseOverBgColor = (Color) obj;
		} else {
			this.focusedMouseOverBgColor = OMenuButtonSelectionPainter.defaultFocusedMouseOverBgColor;
		}

		// disabled:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Disabled].foreground");
		if (obj instanceof Paint) {
			this.foregroundColorDisabled = (Paint) obj;
		} else {
			this.foregroundColorDisabled = Color.black;
		}

		// enabled:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Enabled].foreground");
		if (obj instanceof Paint) {
			this.foregroundColorEnabled = (Paint) obj;
		} else {
			this.foregroundColorEnabled = Color.black;
		}

		// selected:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Focused].foreground");
		if (obj instanceof Paint) {
			this.foregroundColorFocused = (Paint) obj;
		} else {
			this.foregroundColorFocused = Color.black;
		}

		// mouseover:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[MouseOver].foreground");
		if (obj instanceof Paint) {
			this.foregroundColorMouseOver = (Paint) obj;
		} else {
			this.foregroundColorMouseOver = Color.black;
		}

		// pressed:
		obj = UIManager.getLookAndFeelDefaults().get(this.getComponentKeyName() + "[Pressed].foreground");
		if (obj instanceof Paint) {
			this.foregroundColorPressed = (Paint) obj;
		} else {
			this.foregroundColorPressed = Color.black;
		}

	}

	@Override
	protected void paintBackgroundEnabled(Graphics2D g) {
		g.setPaint(this.foregroundColorEnabled);
		Shape arrow = this.decodeArrow();
		g.fill(arrow);
	}

	@Override
	protected void paintBackgroundDisabled(Graphics2D g) {
		g.setPaint(this.foregroundColorDisabled);
		Shape arrow = this.decodeArrow();
		g.fill(arrow);
	}

	@Override
	protected void paintBackgroundMouseOverAndDefault(Graphics2D g) {

		AlphaComposite old = (AlphaComposite) g.getComposite();
		g.setComposite(this.getDerivedAlphaComposite());
		this.roundRect = this.decodeRoundRect4();
		g.setPaint(this.mouseOverBgColor != null ? this.mouseOverBgColor : OMenuButtonSelectionPainter.defaultMouseOverBgColor);
		g.fill(this.roundRect);
		g.setComposite(old);

		g.setPaint(this.foregroundColorMouseOver);
		Shape arrow = this.decodeArrow();
		g.fill(arrow);
	}

	@Override
	protected void paintBackgroundMouseOver(Graphics2D g) {

		AlphaComposite old = (AlphaComposite) g.getComposite();
		g.setComposite(this.getDerivedAlphaComposite());
		this.roundRect = this.decodeRoundRect4();
		g.setPaint(this.mouseOverBgColor != null ? this.mouseOverBgColor : OMenuButtonSelectionPainter.defaultMouseOverBgColor);
		g.fill(this.roundRect);
		g.setComposite(old);

		g.setPaint(this.foregroundColorMouseOver);
		Shape arrow = this.decodeArrow();
		g.fill(arrow);
	}

	@Override
	protected void paintBackgroundDefaultAndFocused(Graphics2D g) {
		AlphaComposite old = (AlphaComposite) g.getComposite();
		g.setComposite(this.getDerivedAlphaComposite());
		this.roundRect = this.decodeRoundRect4();
		g.setPaint(this.focusBgColor != null ? this.focusBgColor : OMenuButtonSelectionPainter.defaultFocusBgColor);
		g.fill(this.roundRect);
		g.setComposite(old);

		g.setPaint(this.foregroundColorFocused);
		Shape arrow = this.decodeArrow();
		g.fill(arrow);
	}

	@Override
	protected void paintBackgroundFocused(Graphics2D g) {
		AlphaComposite old = (AlphaComposite) g.getComposite();
		g.setComposite(this.getDerivedAlphaComposite());
		this.roundRect = this.decodeRoundRect4();
		g.setPaint(this.focusBgColor != null ? this.focusBgColor : OMenuButtonSelectionPainter.defaultFocusBgColor);
		g.fill(this.roundRect);
		g.setComposite(old);

		g.setPaint(this.foregroundColorFocused);
		Shape arrow = this.decodeArrow();
		g.fill(arrow);
	}

	@Override
	protected void paintBackgroundPressed(Graphics2D g) {
		g.setPaint(this.foregroundColorPressed);
		Shape arrow = this.decodeArrow();
		g.fill(arrow);
	}

	@Override
	protected void paintBackgroundMouseOverAndFocused(Graphics2D g) {
		AlphaComposite old = (AlphaComposite) g.getComposite();
		g.setComposite(this.getDerivedAlphaComposite());
		this.roundRect = this.decodeRoundRect4();
		g.setPaint(this.focusedMouseOverBgColor != null ? this.focusedMouseOverBgColor : OMenuButtonSelectionPainter.defaultFocusedMouseOverBgColor);
		g.fill(this.roundRect);
		g.setComposite(old);

		g.setPaint(this.foregroundColorFocused);
		Shape arrow = this.decodeArrow();
		g.fill(arrow);
	}

	@Override
	protected void paintBackgroundMouseOverAndDefaultAndFocused(Graphics2D g) {
		AlphaComposite old = (AlphaComposite) g.getComposite();
		g.setComposite(this.getDerivedAlphaComposite());
		this.roundRect = this.decodeRoundRect4();
		g.setPaint(this.focusedMouseOverBgColor != null ? this.focusedMouseOverBgColor : OMenuButtonSelectionPainter.defaultFocusedMouseOverBgColor);
		g.fill(this.roundRect);
		g.setComposite(old);

		g.setPaint(this.foregroundColorFocused);
		Shape arrow = this.decodeArrow();
		g.fill(arrow);
	}

	@Override
	protected void paintBackgroundPressedAndFocused(Graphics2D g) {
		g.setPaint(this.foregroundColorFocused);
		Shape arrow = this.decodeArrow();
		g.fill(arrow);
	}

	protected Path2D decodeArrow() {
		this.path.reset();

		double xcenter = (this.decodeX(3.0f)) / 2.0;
		double ycenter = (this.decodeY(3.0f)) / 2.0;

		double arrowHeight = 4;
		double arrowWidth = 6;

		this.path.moveTo(xcenter - (arrowWidth / 2.0), ycenter - (arrowHeight / 2.0));
		this.path.lineTo(xcenter + (arrowWidth / 2.0), ycenter - (arrowHeight / 2.0));
		this.path.lineTo(xcenter, ycenter + (arrowHeight / 2.0));
		this.path.lineTo(xcenter - (arrowWidth / 2.0), ycenter - (arrowHeight / 2.0));

		this.path.closePath();
		return this.path;
	}

	@Override
	protected RoundRectangle2D decodeRoundRect1() {
		this.roundRect.setRoundRect(this.decodeX(0.2857143f), // x
				this.decodeY(0.42857143f), // y
				this.decodeX(2.7142859f) - this.decodeX(0.2857143f), // width
				this.decodeY(2.857143f) - this.decodeY(0.42857143f), // height
				9.0f, 9.0f); // rounding
		return this.roundRect;
	}

	@Override
	protected RoundRectangle2D decodeRoundRect2() {
		this.roundRect.setRoundRect(this.decodeX(0.2857143f), // x
				this.decodeY(0.2857143f), // y
				this.decodeX(2.7142859f) - this.decodeX(0.2857143f), // width
				this.decodeY(2.7142859f) - this.decodeY(0.2857143f), // height
				6.0f, 6.0f); // rounding
		return this.roundRect;
	}

	@Override
	protected RoundRectangle2D decodeRoundRect3() {
		this.roundRect.setRoundRect(this.decodeX(0.42857143f), // x
				this.decodeY(0.42857143f), // y
				this.decodeX(2.5714285f) - this.decodeX(0.42857143f), // width
				this.decodeY(2.5714285f) - this.decodeY(0.42857143f), // height
				6.0f, 6.0f); // rounding
		return this.roundRect;
	}

	@Override
	protected RoundRectangle2D decodeRoundRect4() {
		this.roundRect.setRoundRect(this.decodeX(0.08571429f), // x
				this.decodeY(0.08571429f), // y
				this.decodeX(2.914286f) - this.decodeX(0.08571429f), // width
				this.decodeY(2.914286f) - this.decodeY(0.08571429f), // height
				8.0f, 8.0f); // rounding
		return this.roundRect;
	}

}
