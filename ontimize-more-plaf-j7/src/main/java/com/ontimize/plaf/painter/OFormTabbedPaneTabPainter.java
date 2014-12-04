package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class OFormTabbedPaneTabPainter extends AbstractRegionPainter {
	// package private integers representing the available states that
	// this painter will paint. These are used when creating a new instance
	// of TabbedPaneTabPainter to determine which region/state is being painted
	// by that instance.
	public static final int BACKGROUND_ENABLED = 1;
	public static final int BACKGROUND_ENABLED_MOUSEOVER = 2;
	public static final int BACKGROUND_ENABLED_PRESSED = 3;
	public static final int BACKGROUND_DISABLED = 4;
	public static final int BACKGROUND_SELECTED_DISABLED = 5;
	public static final int BACKGROUND_SELECTED = 6;
	public static final int BACKGROUND_SELECTED_MOUSEOVER = 7;
	public static final int BACKGROUND_SELECTED_PRESSED = 8;
	public static final int BACKGROUND_SELECTED_FOCUSED = 9;
	public static final int BACKGROUND_SELECTED_MOUSEOVER_FOCUSED = 10;
	public static final int BACKGROUND_SELECTED_PRESSED_FOCUSED = 11;

	private final int state; // refers to one of the static final ints above
	private final PaintContext ctx;

	// the following 4 variables are reused during the painting code of the
	// layers
	private Path2D path = new Path2D.Float();

	// All Colors used for painting are stored here. Ideally, only those colors
	// being used
	// by a particular instance of TabbedPaneTabPainter would be created. For
	// the moment at least,
	// however, all are created for each instance.
	private final Color color1 = this.decodeColor("nimbusBase", 0.032459438f, -0.55535716f, -0.109803945f, 0);
	private final Color color2 = this.decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.4784314f, 0);
	private final Color color3 = this.decodeColor("nimbusBase", 0.08801502f, -0.63174605f, 0.43921566f, 0);
	private final Color color4 = this.decodeColor("nimbusBase", 0.05468172f, -0.6145278f, 0.37647057f, 0);
	private final Color color5 = this.decodeColor("nimbusBase", 0.032459438f, -0.5953556f, 0.32549018f, 0);
	private final Color color6 = this.decodeColor("nimbusBase", 0.032459438f, -0.54616207f, -0.02352941f, 0);
	private final Color color7 = this.decodeColor("nimbusBase", 0.08801502f, -0.6317773f, 0.4470588f, 0);
	private final Color color8 = this.decodeColor("nimbusBase", 0.021348298f, -0.61547136f, 0.41960782f, 0);
	private final Color color9 = this.decodeColor("nimbusBase", 0.032459438f, -0.5985242f, 0.39999998f, 0);
	private final Color color10 = this.decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.52156866f, 0);
	private final Color color11 = this.decodeColor("nimbusBase", 0.027408898f, -0.5847884f, 0.2980392f, 0);

	private Paint backgroundEnabled;
	private Paint backgroundEnabledMouseOver;
	private Paint backgroundEnabledPressed;
	private Paint backgroundDisabled;
	private Paint backgroundSelected;
	private Paint backgroundSelectedDisabled;
	private Paint backgroundSelectedMouseOver;
	private Paint backgroundSelectedPressed;
	private Paint backgroundSelectedFocused;
	private Paint backgroundSelectedMouseOverFocused;
	private Paint backgroundSelectedPressedFoscused;

	public OFormTabbedPaneTabPainter(int state, PaintContext ctx) {
		super();
		this.state = state;
		this.ctx = ctx;
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		// generate this entire method. Each state/bg/fg/border combo that has
		// been painted gets its own KEY and paint method.
		switch (this.state) {
		case BACKGROUND_ENABLED:
			this.paintBackgroundEnabled(g);
			break;
		case BACKGROUND_ENABLED_MOUSEOVER:
			this.paintBackgroundEnabledAndMouseOver(g);
			break;
		case BACKGROUND_ENABLED_PRESSED:
			this.paintBackgroundEnabledAndPressed(g);
			break;
		case BACKGROUND_DISABLED:
			this.paintBackgroundDisabled(g);
			break;
		case BACKGROUND_SELECTED_DISABLED:
			this.paintBackgroundSelectedAndDisabled(g);
			break;
		case BACKGROUND_SELECTED:
			this.paintBackgroundSelected(g);
			break;
		case BACKGROUND_SELECTED_MOUSEOVER:
			this.paintBackgroundSelectedAndMouseOver(g);
			break;
		case BACKGROUND_SELECTED_PRESSED:
			this.paintBackgroundSelectedAndPressed(g);
			break;
		case BACKGROUND_SELECTED_FOCUSED:
			this.paintBackgroundSelectedAndFocused(g);
			break;
		case BACKGROUND_SELECTED_MOUSEOVER_FOCUSED:
			this.paintBackgroundSelectedAndMouseOverAndFocused(g);
			break;
		case BACKGROUND_SELECTED_PRESSED_FOCUSED:
			this.paintBackgroundSelectedAndPressedAndFocused(g);
			break;
		}
	}

	@Override
	protected String getComponentKeyName() {
		return "FormTabbedPane:TabbedPaneTab";
	}

	@Override
	protected void init() {
		this.backgroundEnabled = this.getConfiguredPaint(this.getComponentKeyName() + "[Enabled].background", this.color1);
		this.backgroundEnabledMouseOver = this.getConfiguredPaint(this.getComponentKeyName() + "[Enabled+MouseOver].background", this.color2);
		this.backgroundEnabledPressed = this.getConfiguredPaint(this.getComponentKeyName() + "[Enabled+Pressed].background", this.color3);
		this.backgroundDisabled = this.getConfiguredPaint(this.getComponentKeyName() + "[Disabled].background", this.color4);
		this.backgroundSelected = this.getConfiguredPaint(this.getComponentKeyName() + "[Selected].background", this.color5);
		this.backgroundSelectedDisabled = this.getConfiguredPaint(this.getComponentKeyName() + "[Disabled+Selected].background", this.color6);
		this.backgroundSelectedMouseOver = this.getConfiguredPaint(this.getComponentKeyName() + "[MouseOver+Selected].background", this.color7);
		this.backgroundSelectedPressed = this.getConfiguredPaint(this.getComponentKeyName() + "[Pressed+Selected].background", this.color8);
		this.backgroundSelectedFocused = this.getConfiguredPaint(this.getComponentKeyName() + "[Focused+Selected].background", this.color9);
		this.backgroundSelectedMouseOverFocused = this.getConfiguredPaint(this.getComponentKeyName() + "[Focused+MouseOver+Selected].background", this.color10);
		this.backgroundSelectedPressedFoscused = this.getConfiguredPaint(this.getComponentKeyName() + "[Focused+Pressed+Selected].background", this.color11);
	}

	private Paint getConfiguredPaint(String name, Paint defaultPaint) {
		Object object = UIManager.getLookAndFeelDefaults().get(name);
		return (Paint) (object == null ? defaultPaint : object);
	}

	@Override
	protected final PaintContext getPaintContext() {
		return this.ctx;
	}

	private void paintBackgroundEnabled(Graphics2D g) {
		this.path = this.decodePath1();
		g.setPaint(new Color(0x737373));
		g.fill(this.path);
		this.path = this.decodePath2();
		g.setPaint(this.backgroundEnabled);
		g.fill(this.path);
	}

	private void paintBackgroundEnabledAndMouseOver(Graphics2D g) {
		this.path = this.decodePath1();
		g.setPaint(new Color(0x737373));
		g.fill(this.path);
		this.path = this.decodePath2();
		g.setPaint(this.backgroundEnabledMouseOver);
		g.fill(this.path);

	}

	private void paintBackgroundEnabledAndPressed(Graphics2D g) {
		this.path = this.decodePath3();
		g.setPaint(this.backgroundEnabledPressed);
		g.fill(this.path);

	}

	private void paintBackgroundDisabled(Graphics2D g) {
		this.path = this.decodePath5();
		g.setPaint(this.backgroundDisabled);
		g.fill(this.path);
	}

	private void paintBackgroundSelectedAndDisabled(Graphics2D g) {
		this.path = this.decodePath7();
		g.setPaint(this.backgroundSelectedDisabled);
		g.fill(this.path);
	}

	private void paintBackgroundSelected(Graphics2D g) {
		this.path = this.decodePath1();
		g.setPaint(new Color(0x21465e));
		g.fill(this.path);
		this.path = this.decodePath2();
		g.setPaint(this.backgroundSelected);
		g.fill(this.path);
	}

	private void paintBackgroundSelectedAndMouseOver(Graphics2D g) {
		this.path = this.decodePath8();
		g.setPaint(this.backgroundSelectedMouseOver);
		g.fill(this.path);
	}

	private void paintBackgroundSelectedAndPressed(Graphics2D g) {
		this.path = this.decodePath8();
		g.setPaint(this.backgroundSelectedPressed);
		g.fill(this.path);
	}

	private void paintBackgroundSelectedAndFocused(Graphics2D g) {
		this.path = this.decodePath1();
		g.setPaint(new Color(0x21465e));
		g.fill(this.path);
		this.path = this.decodePath2();
		g.setPaint(this.backgroundSelectedFocused);
		g.fill(this.path);
	}

	private void paintBackgroundSelectedAndMouseOverAndFocused(Graphics2D g) {
		this.path = this.decodePath12();
		g.setPaint(this.backgroundSelectedMouseOverFocused);
		g.fill(this.path);
	}

	private void paintBackgroundSelectedAndPressedAndFocused(Graphics2D g) {
		this.path = this.decodePath12();
		g.setPaint(this.backgroundSelectedPressedFoscused);
		g.fill(this.path);
	}

	private Path2D decodePath1() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.71428573f));
		this.path.curveTo(this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(0.7142857313156128f, -3.0f), this.decodeAnchorX(0.7142857313156128f, -3.0f),
				this.decodeAnchorY(0.0f, 0.0f), this.decodeX(0.71428573f), this.decodeY(0.0f));
		this.path.curveTo(this.decodeAnchorX(0.7142857313156128f, 3.0f), this.decodeAnchorY(0.0f, 0.0f), this.decodeAnchorX(2.2857143878936768f, -3.0f),
				this.decodeAnchorY(0.0f, 0.0f), this.decodeX(2.2857144f), this.decodeY(0.0f));
		this.path.curveTo(this.decodeAnchorX(2.2857143878936768f, 3.0f), this.decodeAnchorY(0.0f, 0.0f), this.decodeAnchorX(3.0f, 0.0f),
				this.decodeAnchorY(0.7142857313156128f, -3.0f), this.decodeX(3.0f), this.decodeY(0.71428573f));
		this.path.curveTo(this.decodeAnchorX(3.0f, 0.0f), this.decodeAnchorY(0.7142857313156128f, 3.0f), this.decodeAnchorX(3.0f, 0.0f), this.decodeAnchorY(3.0f, 0.0f),
				this.decodeX(3.0f), this.decodeY(3.0f));
		this.path.lineTo(this.decodeX(0.0f), this.decodeY(3.0f));
		this.path.curveTo(this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(3.0f, 0.0f), this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(0.7142857313156128f, 3.0f),
				this.decodeX(0.0f), this.decodeY(0.71428573f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath2() {

		this.path.reset();
		this.path.moveTo(this.decodeX(0.14285715f), this.decodeY(3.0f));
		this.path.curveTo(this.decodeAnchorX(0.1428571492433548f, 0.0f), this.decodeAnchorY(3.0f, 0.0f), this.decodeAnchorX(0.1428571492433548f, 0.0f),
				this.decodeAnchorY(0.8571428656578064f, 3.5555555555555536f), this.decodeX(0.14285715f), this.decodeY(0.85714287f));
		this.path.curveTo(this.decodeAnchorX(0.1428571492433548f, 0.0f), this.decodeAnchorY(0.8571428656578064f, -3.5555555555555536f),
				this.decodeAnchorX(0.8571428656578064f, -3.444444444444443f), this.decodeAnchorY(0.1428571492433548f, 0.0f), this.decodeX(0.85714287f), this.decodeY(0.14285715f));
		this.path.curveTo(this.decodeAnchorX(0.8571428656578064f, 3.444444444444443f), this.decodeAnchorY(0.1428571492433548f, 0.0f),
				this.decodeAnchorX(2.142857074737549f, -3.333333333333343f), this.decodeAnchorY(0.1428571492433548f, 0.0f), this.decodeX(2.142857f), this.decodeY(0.14285715f));
		this.path.curveTo(this.decodeAnchorX(2.142857074737549f, 3.333333333333343f), this.decodeAnchorY(0.1428571492433548f, 0.0f), this.decodeAnchorX(2.857142925262451f, 0.0f),
				this.decodeAnchorY(0.8571428656578064f, -3.277777777777777f), this.decodeX(2.857143f), this.decodeY(0.85714287f));
		this.path.curveTo(this.decodeAnchorX(2.857142925262451f, 0.0f), this.decodeAnchorY(0.8571428656578064f, 3.277777777777777f), this.decodeAnchorX(2.857142925262451f, 0.0f),
				this.decodeAnchorY(3.0f, 0.0f), this.decodeX(2.857143f), this.decodeY(3.0f));
		this.path.lineTo(this.decodeX(0.14285715f), this.decodeY(3.0f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath3() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.71428573f));
		this.path.curveTo(this.decodeAnchorX(0.0f, 0.05555555555555555f), this.decodeAnchorY(0.7142857313156128f, 2.6111111111111125f),
				this.decodeAnchorX(0.8333333134651184f, -2.5000000000000018f), this.decodeAnchorY(0.0f, 0.0f), this.decodeX(0.8333333f), this.decodeY(0.0f));
		this.path.curveTo(this.decodeAnchorX(0.8333333134651184f, 2.5000000000000018f), this.decodeAnchorY(0.0f, 0.0f),
				this.decodeAnchorX(2.2857143878936768f, -2.7222222222222143f), this.decodeAnchorY(0.0f, 0.0f), this.decodeX(2.2857144f), this.decodeY(0.0f));
		this.path.curveTo(this.decodeAnchorX(2.2857143878936768f, 2.7222222222222143f), this.decodeAnchorY(0.0f, 0.0f), this.decodeAnchorX(3.0f, -0.055555555555557135f),
				this.decodeAnchorY(0.7142857313156128f, -2.722222222222223f), this.decodeX(3.0f), this.decodeY(0.71428573f));
		this.path.curveTo(this.decodeAnchorX(3.0f, 0.055555555555557135f), this.decodeAnchorY(0.7142857313156128f, 2.722222222222223f), this.decodeAnchorX(3.0f, 0.0f),
				this.decodeAnchorY(3.0f, 0.0f), this.decodeX(3.0f), this.decodeY(3.0f));
		this.path.lineTo(this.decodeX(0.0f), this.decodeY(3.0f));
		this.path.curveTo(this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(3.0f, 0.0f), this.decodeAnchorX(0.0f, -0.05555555555555555f),
				this.decodeAnchorY(0.7142857313156128f, -2.6111111111111125f), this.decodeX(0.0f), this.decodeY(0.71428573f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath4() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.16666667f), this.decodeY(2.0f));
		this.path.curveTo(this.decodeAnchorX(0.1666666716337204f, 0.0f), this.decodeAnchorY(2.0f, 0.0f), this.decodeAnchorX(0.1666666716337204f, 0.0f),
				this.decodeAnchorY(0.8571428656578064f, 3.6666666666666643f), this.decodeX(0.16666667f), this.decodeY(0.85714287f));
		this.path.curveTo(this.decodeAnchorX(0.1666666716337204f, 0.0f), this.decodeAnchorY(0.8571428656578064f, -3.6666666666666643f),
				this.decodeAnchorX(1.0f, -3.5555555555555536f), this.decodeAnchorY(0.1428571492433548f, 0.0f), this.decodeX(1.0f), this.decodeY(0.14285715f));
		this.path.curveTo(this.decodeAnchorX(1.0f, 3.5555555555555536f), this.decodeAnchorY(0.1428571492433548f, 0.0f),
				this.decodeAnchorX(2.142857074737549f, -3.500000000000014f), this.decodeAnchorY(0.1428571492433548f, 0.05555555555555558f), this.decodeX(2.142857f),
				this.decodeY(0.14285715f));
		this.path.curveTo(this.decodeAnchorX(2.142857074737549f, 3.500000000000014f), this.decodeAnchorY(0.1428571492433548f, -0.05555555555555558f),
				this.decodeAnchorX(2.857142925262451f, 0.055555555555557135f), this.decodeAnchorY(0.8571428656578064f, -3.6666666666666643f), this.decodeX(2.857143f),
				this.decodeY(0.85714287f));
		this.path.curveTo(this.decodeAnchorX(2.857142925262451f, -0.055555555555557135f), this.decodeAnchorY(0.8571428656578064f, 3.6666666666666643f),
				this.decodeAnchorX(2.857142925262451f, 0.0f), this.decodeAnchorY(2.0f, 0.0f), this.decodeX(2.857143f), this.decodeY(2.0f));
		this.path.lineTo(this.decodeX(0.16666667f), this.decodeY(2.0f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath5() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.8333333f));
		this.path.curveTo(this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(0.8333333134651184f, -3.0f), this.decodeAnchorX(0.7142857313156128f, -3.0f),
				this.decodeAnchorY(0.0f, 0.0f), this.decodeX(0.71428573f), this.decodeY(0.0f));
		this.path.curveTo(this.decodeAnchorX(0.7142857313156128f, 3.0f), this.decodeAnchorY(0.0f, 0.0f), this.decodeAnchorX(2.2857143878936768f, -3.0f),
				this.decodeAnchorY(0.0f, 0.0f), this.decodeX(2.2857144f), this.decodeY(0.0f));
		this.path.curveTo(this.decodeAnchorX(2.2857143878936768f, 3.0f), this.decodeAnchorY(0.0f, 0.0f), this.decodeAnchorX(3.0f, 0.0f),
				this.decodeAnchorY(0.8333333134651184f, -3.0f), this.decodeX(3.0f), this.decodeY(0.8333333f));
		this.path.curveTo(this.decodeAnchorX(3.0f, 0.0f), this.decodeAnchorY(0.8333333134651184f, 3.0f), this.decodeAnchorX(3.0f, 0.0f), this.decodeAnchorY(3.0f, 0.0f),
				this.decodeX(3.0f), this.decodeY(3.0f));
		this.path.lineTo(this.decodeX(0.0f), this.decodeY(3.0f));
		this.path.curveTo(this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(3.0f, 0.0f), this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(0.8333333134651184f, 3.0f),
				this.decodeX(0.0f), this.decodeY(0.8333333f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath6() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.14285715f), this.decodeY(2.0f));
		this.path.curveTo(this.decodeAnchorX(0.1428571492433548f, 0.0f), this.decodeAnchorY(2.0f, 0.0f), this.decodeAnchorX(0.1428571492433548f, 0.0f),
				this.decodeAnchorY(1.0f, 3.5555555555555536f), this.decodeX(0.14285715f), this.decodeY(1.0f));
		this.path.curveTo(this.decodeAnchorX(0.1428571492433548f, 0.0f), this.decodeAnchorY(1.0f, -3.5555555555555536f),
				this.decodeAnchorX(0.8571428656578064f, -3.444444444444443f), this.decodeAnchorY(0.1666666716337204f, 0.0f), this.decodeX(0.85714287f), this.decodeY(0.16666667f));
		this.path.curveTo(this.decodeAnchorX(0.8571428656578064f, 3.444444444444443f), this.decodeAnchorY(0.1666666716337204f, 0.0f),
				this.decodeAnchorX(2.142857074737549f, -3.333333333333343f), this.decodeAnchorY(0.1666666716337204f, 0.0f), this.decodeX(2.142857f), this.decodeY(0.16666667f));
		this.path.curveTo(this.decodeAnchorX(2.142857074737549f, 3.333333333333343f), this.decodeAnchorY(0.1666666716337204f, 0.0f), this.decodeAnchorX(2.857142925262451f, 0.0f),
				this.decodeAnchorY(1.0f, -3.277777777777777f), this.decodeX(2.857143f), this.decodeY(1.0f));
		this.path.curveTo(this.decodeAnchorX(2.857142925262451f, 0.0f), this.decodeAnchorY(1.0f, 3.277777777777777f), this.decodeAnchorX(2.857142925262451f, 0.0f),
				this.decodeAnchorY(2.0f, 0.0f), this.decodeX(2.857143f), this.decodeY(2.0f));
		this.path.lineTo(this.decodeX(0.14285715f), this.decodeY(2.0f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath7() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.71428573f));
		this.path.curveTo(this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(0.7142857313156128f, -3.0f), this.decodeAnchorX(0.7142857313156128f, -3.0f),
				this.decodeAnchorY(0.0f, 0.0f), this.decodeX(0.71428573f), this.decodeY(0.0f));
		this.path.curveTo(this.decodeAnchorX(0.7142857313156128f, 3.0f), this.decodeAnchorY(0.0f, 0.0f), this.decodeAnchorX(2.2857143878936768f, -3.0f),
				this.decodeAnchorY(0.0f, 0.0f), this.decodeX(2.2857144f), this.decodeY(0.0f));
		this.path.curveTo(this.decodeAnchorX(2.2857143878936768f, 3.0f), this.decodeAnchorY(0.0f, 0.0f), this.decodeAnchorX(3.0f, 0.0f),
				this.decodeAnchorY(0.7142857313156128f, -3.0f), this.decodeX(3.0f), this.decodeY(0.71428573f));
		this.path.curveTo(this.decodeAnchorX(3.0f, 0.0f), this.decodeAnchorY(0.7142857313156128f, 3.0f), this.decodeAnchorX(3.0f, 0.0f), this.decodeAnchorY(2.0f, 0.0f),
				this.decodeX(3.0f), this.decodeY(2.0f));
		this.path.lineTo(this.decodeX(0.0f), this.decodeY(2.0f));
		this.path.curveTo(this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(2.0f, 0.0f), this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(0.7142857313156128f, 3.0f),
				this.decodeX(0.0f), this.decodeY(0.71428573f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath8() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.71428573f));
		this.path.curveTo(this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(0.7142857313156128f, -3.0f), this.decodeAnchorX(0.5555555820465088f, -3.0f),
				this.decodeAnchorY(0.0f, 0.0f), this.decodeX(0.5555556f), this.decodeY(0.0f));
		this.path.curveTo(this.decodeAnchorX(0.5555555820465088f, 3.0f), this.decodeAnchorY(0.0f, 0.0f), this.decodeAnchorX(2.444444417953491f, -3.0f),
				this.decodeAnchorY(0.0f, 0.0f), this.decodeX(2.4444444f), this.decodeY(0.0f));
		this.path.curveTo(this.decodeAnchorX(2.444444417953491f, 3.0f), this.decodeAnchorY(0.0f, 0.0f), this.decodeAnchorX(3.0f, 0.0f),
				this.decodeAnchorY(0.7142857313156128f, -3.0f), this.decodeX(3.0f), this.decodeY(0.71428573f));
		this.path.curveTo(this.decodeAnchorX(3.0f, 0.0f), this.decodeAnchorY(0.7142857313156128f, 3.0f), this.decodeAnchorX(3.0f, 0.0f), this.decodeAnchorY(2.0f, 0.0f),
				this.decodeX(3.0f), this.decodeY(2.0f));
		this.path.lineTo(this.decodeX(0.0f), this.decodeY(2.0f));
		this.path.curveTo(this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(2.0f, 0.0f), this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(0.7142857313156128f, 3.0f),
				this.decodeX(0.0f), this.decodeY(0.71428573f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath9() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.11111111f), this.decodeY(2.0f));
		this.path.curveTo(this.decodeAnchorX(0.1111111119389534f, 0.0f), this.decodeAnchorY(2.0f, 0.0f), this.decodeAnchorX(0.1111111119389534f, 0.0f),
				this.decodeAnchorY(0.8571428656578064f, 3.5555555555555536f), this.decodeX(0.11111111f), this.decodeY(0.85714287f));
		this.path.curveTo(this.decodeAnchorX(0.1111111119389534f, 0.0f), this.decodeAnchorY(0.8571428656578064f, -3.5555555555555536f),
				this.decodeAnchorX(0.6666666865348816f, -3.444444444444443f), this.decodeAnchorY(0.1428571492433548f, 0.0f), this.decodeX(0.6666667f), this.decodeY(0.14285715f));
		this.path.curveTo(this.decodeAnchorX(0.6666666865348816f, 3.444444444444443f), this.decodeAnchorY(0.1428571492433548f, 0.0f),
				this.decodeAnchorX(2.3333332538604736f, -3.333333333333343f), this.decodeAnchorY(0.1428571492433548f, 0.0f), this.decodeX(2.3333333f), this.decodeY(0.14285715f));
		this.path.curveTo(this.decodeAnchorX(2.3333332538604736f, 3.333333333333343f), this.decodeAnchorY(0.1428571492433548f, 0.0f),
				this.decodeAnchorX(2.8888888359069824f, 0.0f), this.decodeAnchorY(0.8571428656578064f, -3.277777777777777f), this.decodeX(2.8888888f), this.decodeY(0.85714287f));
		this.path.curveTo(this.decodeAnchorX(2.8888888359069824f, 0.0f), this.decodeAnchorY(0.8571428656578064f, 3.277777777777777f),
				this.decodeAnchorX(2.8888888359069824f, 0.0f), this.decodeAnchorY(2.0f, 0.0f), this.decodeX(2.8888888f), this.decodeY(2.0f));
		this.path.lineTo(this.decodeX(0.11111111f), this.decodeY(2.0f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath10() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.14285715f), this.decodeY(3.0f));
		this.path.curveTo(this.decodeAnchorX(0.1428571492433548f, 0.0f), this.decodeAnchorY(3.0f, 0.0f), this.decodeAnchorX(0.1428571492433548f, 0.0f),
				this.decodeAnchorY(0.8571428656578064f, 3.5555555555555536f), this.decodeX(0.14285715f), this.decodeY(0.85714287f));
		this.path.curveTo(this.decodeAnchorX(0.1428571492433548f, 0.0f), this.decodeAnchorY(0.8571428656578064f, -3.5555555555555536f),
				this.decodeAnchorX(0.8571428656578064f, -3.444444444444443f), this.decodeAnchorY(0.1428571492433548f, 0.0f), this.decodeX(0.85714287f), this.decodeY(0.14285715f));
		this.path.curveTo(this.decodeAnchorX(0.8571428656578064f, 3.444444444444443f), this.decodeAnchorY(0.1428571492433548f, 0.0f),
				this.decodeAnchorX(2.142857074737549f, -3.333333333333343f), this.decodeAnchorY(0.1428571492433548f, 0.0f), this.decodeX(2.142857f), this.decodeY(0.14285715f));
		this.path.curveTo(this.decodeAnchorX(2.142857074737549f, 3.333333333333343f), this.decodeAnchorY(0.1428571492433548f, 0.0f), this.decodeAnchorX(2.857142925262451f, 0.0f),
				this.decodeAnchorY(0.8571428656578064f, -3.277777777777777f), this.decodeX(2.857143f), this.decodeY(0.85714287f));
		this.path.curveTo(this.decodeAnchorX(2.857142925262451f, 0.0f), this.decodeAnchorY(0.8571428656578064f, 3.277777777777777f), this.decodeAnchorX(2.857142925262451f, 0.0f),
				this.decodeAnchorY(3.0f, 0.0f), this.decodeX(2.857143f), this.decodeY(3.0f));
		this.path.lineTo(this.decodeX(0.14285715f), this.decodeY(3.0f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath11() {
		this.path.reset();
		this.path.moveTo(this.decodeX(1.4638889f), this.decodeY(2.25f));
		this.path.lineTo(this.decodeX(1.4652778f), this.decodeY(2.777778f));
		this.path.lineTo(this.decodeX(0.3809524f), this.decodeY(2.777778f));
		this.path.lineTo(this.decodeX(0.375f), this.decodeY(0.88095236f));
		this.path.curveTo(this.decodeAnchorX(0.375f, 0.0f), this.decodeAnchorY(0.8809523582458496f, -2.2500000000000004f),
				this.decodeAnchorX(0.8452380895614624f, -1.9166666666666647f), this.decodeAnchorY(0.380952388048172f, 0.0f), this.decodeX(0.8452381f), this.decodeY(0.3809524f));
		this.path.lineTo(this.decodeX(2.1011903f), this.decodeY(0.3809524f));
		this.path.curveTo(this.decodeAnchorX(2.1011903285980225f, 2.124999999999986f), this.decodeAnchorY(0.380952388048172f, 0.0f), this.decodeAnchorX(2.6309525966644287f, 0.0f),
				this.decodeAnchorY(0.863095223903656f, -2.5833333333333317f), this.decodeX(2.6309526f), this.decodeY(0.8630952f));
		this.path.lineTo(this.decodeX(2.625f), this.decodeY(2.7638886f));
		this.path.lineTo(this.decodeX(1.4666667f), this.decodeY(2.777778f));
		this.path.lineTo(this.decodeX(1.4638889f), this.decodeY(2.2361114f));
		this.path.lineTo(this.decodeX(2.3869045f), this.decodeY(2.222222f));
		this.path.lineTo(this.decodeX(2.375f), this.decodeY(0.86904764f));
		this.path.curveTo(this.decodeAnchorX(2.375f, -7.105427357601002E-15f), this.decodeAnchorY(0.8690476417541504f, -0.9166666666666679f),
				this.decodeAnchorX(2.095238208770752f, 1.0833333333333357f), this.decodeAnchorY(0.6071428656578064f, -1.7763568394002505E-15f), this.decodeX(2.0952382f),
				this.decodeY(0.60714287f));
		this.path.lineTo(this.decodeX(0.8333334f), this.decodeY(0.6130952f));
		this.path.curveTo(this.decodeAnchorX(0.8333333730697632f, -1.0f), this.decodeAnchorY(0.613095223903656f, 0.0f), this.decodeAnchorX(0.625f, 0.04166666666666696f),
				this.decodeAnchorY(0.8690476417541504f, -0.9583333333333339f), this.decodeX(0.625f), this.decodeY(0.86904764f));
		this.path.lineTo(this.decodeX(0.6130952f), this.decodeY(2.2361114f));
		this.path.lineTo(this.decodeX(1.4638889f), this.decodeY(2.25f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath12() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.0f), this.decodeY(0.71428573f));
		this.path.curveTo(this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(0.7142857313156128f, -3.0f), this.decodeAnchorX(0.5555555820465088f, -3.0f),
				this.decodeAnchorY(0.0f, 0.0f), this.decodeX(0.5555556f), this.decodeY(0.0f));
		this.path.curveTo(this.decodeAnchorX(0.5555555820465088f, 3.0f), this.decodeAnchorY(0.0f, 0.0f), this.decodeAnchorX(2.444444417953491f, -3.0f),
				this.decodeAnchorY(0.0f, 0.0f), this.decodeX(2.4444444f), this.decodeY(0.0f));
		this.path.curveTo(this.decodeAnchorX(2.444444417953491f, 3.0f), this.decodeAnchorY(0.0f, 0.0f), this.decodeAnchorX(3.0f, 0.0f),
				this.decodeAnchorY(0.7142857313156128f, -3.0f), this.decodeX(3.0f), this.decodeY(0.71428573f));
		this.path.curveTo(this.decodeAnchorX(3.0f, 0.0f), this.decodeAnchorY(0.7142857313156128f, 3.0f), this.decodeAnchorX(3.0f, 0.0f), this.decodeAnchorY(3.0f, 0.0f),
				this.decodeX(3.0f), this.decodeY(3.0f));
		this.path.lineTo(this.decodeX(0.0f), this.decodeY(3.0f));
		this.path.curveTo(this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(3.0f, 0.0f), this.decodeAnchorX(0.0f, 0.0f), this.decodeAnchorY(0.7142857313156128f, 3.0f),
				this.decodeX(0.0f), this.decodeY(0.71428573f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath13() {
		this.path.reset();
		this.path.moveTo(this.decodeX(0.11111111f), this.decodeY(3.0f));
		this.path.curveTo(this.decodeAnchorX(0.1111111119389534f, 0.0f), this.decodeAnchorY(3.0f, 0.0f), this.decodeAnchorX(0.1111111119389534f, 0.0f),
				this.decodeAnchorY(0.8571428656578064f, 3.5555555555555536f), this.decodeX(0.11111111f), this.decodeY(0.85714287f));
		this.path.curveTo(this.decodeAnchorX(0.1111111119389534f, 0.0f), this.decodeAnchorY(0.8571428656578064f, -3.5555555555555536f),
				this.decodeAnchorX(0.6666666865348816f, -3.444444444444443f), this.decodeAnchorY(0.1428571492433548f, 0.0f), this.decodeX(0.6666667f), this.decodeY(0.14285715f));
		this.path.curveTo(this.decodeAnchorX(0.6666666865348816f, 3.444444444444443f), this.decodeAnchorY(0.1428571492433548f, 0.0f),
				this.decodeAnchorX(2.3333332538604736f, -3.333333333333343f), this.decodeAnchorY(0.1428571492433548f, 0.0f), this.decodeX(2.3333333f), this.decodeY(0.14285715f));
		this.path.curveTo(this.decodeAnchorX(2.3333332538604736f, 3.333333333333343f), this.decodeAnchorY(0.1428571492433548f, 0.0f),
				this.decodeAnchorX(2.8888888359069824f, 0.0f), this.decodeAnchorY(0.8571428656578064f, -3.277777777777777f), this.decodeX(2.8888888f), this.decodeY(0.85714287f));
		this.path.curveTo(this.decodeAnchorX(2.8888888359069824f, 0.0f), this.decodeAnchorY(0.8571428656578064f, 3.277777777777777f),
				this.decodeAnchorX(2.8888888359069824f, 0.0f), this.decodeAnchorY(3.0f, 0.0f), this.decodeX(2.8888888f), this.decodeY(3.0f));
		this.path.lineTo(this.decodeX(0.11111111f), this.decodeY(3.0f));
		this.path.closePath();
		return this.path;
	}

	private Path2D decodePath14() {
		this.path.reset();
		this.path.moveTo(this.decodeX(1.4583333f), this.decodeY(2.25f));
		this.path.lineTo(this.decodeX(1.4599359f), this.decodeY(2.777778f));
		this.path.lineTo(this.decodeX(0.2962963f), this.decodeY(2.777778f));
		this.path.lineTo(this.decodeX(0.29166666f), this.decodeY(0.88095236f));
		this.path.curveTo(this.decodeAnchorX(0.2916666567325592f, 0.0f), this.decodeAnchorY(0.8809523582458496f, -2.2500000000000004f),
				this.decodeAnchorX(0.6574074029922485f, -1.9166666666666647f), this.decodeAnchorY(0.380952388048172f, 0.0f), this.decodeX(0.6574074f), this.decodeY(0.3809524f));
		this.path.lineTo(this.decodeX(2.3009257f), this.decodeY(0.3809524f));
		this.path.curveTo(this.decodeAnchorX(2.3009257316589355f, 2.124999999999986f), this.decodeAnchorY(0.380952388048172f, 0.0f), this.decodeAnchorX(2.712963104248047f, 0.0f),
				this.decodeAnchorY(0.863095223903656f, -2.5833333333333317f), this.decodeX(2.712963f), this.decodeY(0.8630952f));
		this.path.lineTo(this.decodeX(2.7083333f), this.decodeY(2.7638886f));
		this.path.lineTo(this.decodeX(1.4615384f), this.decodeY(2.777778f));
		this.path.lineTo(this.decodeX(1.4583333f), this.decodeY(2.2361114f));
		this.path.lineTo(this.decodeX(2.523148f), this.decodeY(2.222222f));
		this.path.lineTo(this.decodeX(2.5138888f), this.decodeY(0.86904764f));
		this.path.curveTo(this.decodeAnchorX(2.5138888359069824f, -7.105427357601002E-15f), this.decodeAnchorY(0.8690476417541504f, -0.9166666666666679f),
				this.decodeAnchorX(2.2962963581085205f, 1.0833333333333357f), this.decodeAnchorY(0.6071428656578064f, -1.7763568394002505E-15f), this.decodeX(2.2962964f),
				this.decodeY(0.60714287f));
		this.path.lineTo(this.decodeX(0.6481482f), this.decodeY(0.6130952f));
		this.path.curveTo(this.decodeAnchorX(0.6481481790542603f, -1.0f), this.decodeAnchorY(0.613095223903656f, 0.0f),
				this.decodeAnchorX(0.4861111044883728f, 0.04166666666666696f), this.decodeAnchorY(0.8690476417541504f, -0.9583333333333339f), this.decodeX(0.4861111f),
				this.decodeY(0.86904764f));
		this.path.lineTo(this.decodeX(0.47685182f), this.decodeY(2.2361114f));
		this.path.lineTo(this.decodeX(1.4583333f), this.decodeY(2.25f));
		this.path.closePath();
		return this.path;
	}
}
