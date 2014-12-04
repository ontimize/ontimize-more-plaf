package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class OFormTabbedPaneTabAreaPainter extends AbstractRegionPainter {
	// package private integers representing the available states that
	// this painter will paint. These are used when creating a new instance
	// of TabbedPaneTabAreaPainter to determine which region/state is being
	// painted
	// by that instance.
	public static final int BACKGROUND_ENABLED = 1;
	public static final int BACKGROUND_DISABLED = 2;

	// refers to one of the static final ints above
	private final int state;
	private final PaintContext ctx;

	// the following 4 variables are reused during the painting code of the
	// layers
	private Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

	// All Colors used for painting are stored here. Ideally, only those colors
	// being used
	// by a particular instance of TabbedPaneTabAreaPainter would be created.
	// For the moment at least,
	// however, all are created for each instance.
	private final Color color2 = this.decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.4784314f, 0);
	private final Color color10 = this.decodeColor("nimbusBase", 0.0f, -0.09303135f, 0.09411764f, 0);

	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorDisabled;

	public OFormTabbedPaneTabAreaPainter(int state, PaintContext ctx) {
		super();
		this.state = state;
		this.ctx = ctx;
	}

	@Override
	protected String getComponentKeyName() {
		return "FormTabbedPane:TabbedPaneTabArea";
	}

	@Override
	protected void init() {
		// disable:
		this.backgroundColorDisabled = this.getConfiguredPaint(this.getComponentKeyName() + "[Disabled].background", this.color10);
		this.backgroundColorEnabled = this.getConfiguredPaint(this.getComponentKeyName() + "[Enabled].background", this.color2);
	}

	private Paint getConfiguredPaint(String name, Paint defaultPaint) {
		Object object = UIManager.getLookAndFeelDefaults().get(name);
		return (Paint) (object == null ? defaultPaint : object);
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		switch (this.state) {
		case BACKGROUND_DISABLED:
			this.paintBackgroundDisabled(g);
			break;
		default:
			this.paintBackgroundEnabled(g);
			break;
		}
	}

	@Override
	protected final PaintContext getPaintContext() {
		return this.ctx;
	}

	private void paintBackgroundEnabled(Graphics2D g) {
		this.rect = this.decodeRect1();
		if (this.rect.getHeight() > 0) {
			g.setPaint(this.backgroundColorEnabled);
			g.fill(this.rect);
		}
	}

	private void paintBackgroundDisabled(Graphics2D g) {
		this.rect = this.decodeRect1();
		if (this.rect.getHeight() > 0) {
			g.setPaint(this.backgroundColorDisabled);
			g.fill(this.rect);
		}
	}

	private Rectangle2D decodeRect1() {
		this.rect.setRect(this.decodeX(0.0f), // x
				this.decodeY(0.0f), // y
				this.decodeX(2.0f) - this.decodeX(1.0f), // width
				this.decodeY(2.0f) - this.decodeY(1.0f)); // height
		return this.rect;
	}

}