package com.ontimize.plaf.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.synth.Region;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.component.OntimizeTitlePane;
import com.ontimize.plaf.painter.OntimizePainter;
import com.ontimize.util.AWTUtilities;

public class OntimizeRootPaneUI extends BasicRootPaneUI {

	private static final Logger logger = LoggerFactory.getLogger(OntimizeRootPaneUI.class);

	protected JRootPane rootPane;

	protected Window window;

	protected MouseInputListener mouseInputListener;

	protected WindowListener windowListener;

	protected JComponent titlePane;

	protected ComponentListener windowResizeListener;

	/**
	 * The <code>LayoutManager</code> that is set on the <code>JRootPane</code>.
	 */
	protected LayoutManager layoutManager;

	/**
	 * <code>LayoutManager</code> of the <code>JRootPane</code> before we
	 * replaced it.
	 */
	protected LayoutManager savedOldLayout;

	protected class MouseInputHandler implements MouseInputListener {

		protected static final int WINDOW_MOVE = 1;

		protected static final int WINDOW_RESIZE = 2;

		protected static final int TOP_LEFT_RESIZE = 6;

		protected static final int TOP_RIGHT_RESIZE = 7;

		protected static final int DOWN_LEFT_RESIZE = 4;

		protected static final int DOWN_RIGHT_RESIZE = 5;

		protected static final int LEFT_RESIZE = 10;

		protected static final int RIGHT_RESIZE = 11;

		protected static final int DOWN_RESIZE = 9;

		protected static final int TOP_RESIZE = 8;

		protected int windowAction;

		protected int dragXOffset;

		protected int dragYOffset;

		protected Dimension dragDimension;

		protected int resizeType;

		protected int minimumYPos;

		protected Frame frame;

		protected Dialog dialog;

		final OntimizeRootPaneUI ontimizeRootPaneUI;

		Point start_drag = null;

		Point start_loc = null;

		Rectangle start_dimension = null;

		protected MouseInputHandler() {
			this.ontimizeRootPaneUI = OntimizeRootPaneUI.this;
			this.frame = null;
			this.dialog = null;
			if (OntimizeRootPaneUI.this.window instanceof Frame) {
				this.frame = (Frame) OntimizeRootPaneUI.this.window;
			} else if (OntimizeRootPaneUI.this.window instanceof Dialog) {
				this.dialog = (Dialog) OntimizeRootPaneUI.this.window;
			}
		}

		MouseInputHandler(MouseInputHandler mouseinputhandler) {
			this();
		}

		@Override
		public void mousePressed(MouseEvent mouseevent) {
			if (OntimizeRootPaneUI.this.rootPane.getWindowDecorationStyle() == JRootPane.NONE) {
				return;
			}
			OntimizeRootPaneUI.this.window.toFront();
			this.minimumYPos = OntimizeRootPaneUI.this.window.getGraphicsConfiguration().getBounds().y;
			Point point = mouseevent.getPoint();
			Point point1 = SwingUtilities.convertPoint(OntimizeRootPaneUI.this.window, point, OntimizeRootPaneUI.this.titlePane);
			int i = this.position2Cursor(OntimizeRootPaneUI.this.window, mouseevent.getX(), mouseevent.getY());
			if ((i == 0) && (OntimizeRootPaneUI.this.titlePane != null) && OntimizeRootPaneUI.this.titlePane.contains(point1) && ((this.dialog != null) || ((this.frame != null) && (this.frame.getExtendedState() != 6)))) {
				this.windowAction = MouseInputHandler.WINDOW_MOVE;

				Point point3 = mouseevent.getPoint();
				SwingUtilities.convertPointToScreen(point3, OntimizeRootPaneUI.this.window);
				this.start_drag = point3;
				this.start_loc = OntimizeRootPaneUI.this.window.getLocation();

				this.dragXOffset = point.x;
				this.dragYOffset = point.y;
			} else if (this.isWindowResizable()) {
				this.windowAction = MouseInputHandler.WINDOW_RESIZE;
				this.dragXOffset = point.x;
				this.dragYOffset = point.y;

				this.start_dimension = OntimizeRootPaneUI.this.window.getBounds();
				this.start_drag = mouseevent.getPoint();
				SwingUtilities.convertPointToScreen(this.start_drag, OntimizeRootPaneUI.this.window);

				this.dragDimension = new Dimension(OntimizeRootPaneUI.this.window.getWidth(), OntimizeRootPaneUI.this.window.getHeight());
				this.resizeType = this.position2Cursor(OntimizeRootPaneUI.this.window, point.x, point.y);
			}
		}

		@Override
		public void mouseReleased(MouseEvent mouseevent) {
			if ((this.windowAction == MouseInputHandler.WINDOW_RESIZE) && !OntimizeRootPaneUI.this.window.isValid()) {
				OntimizeRootPaneUI.this.window.validate();
				OntimizeRootPaneUI.this.rootPane.repaint();
			}
			this.windowAction = -1;
			OntimizeRootPaneUI.this.window.setCursor(Cursor.getDefaultCursor());
		}

		@Override
		public void mouseMoved(MouseEvent mouseevent) {
			if (OntimizeRootPaneUI.this.rootPane.getWindowDecorationStyle() == JRootPane.NONE) {
				return;
			}

			int i = this.position2Cursor(OntimizeRootPaneUI.this.window, mouseevent.getX(), mouseevent.getY());
			if ((i != 0) && this.isWindowResizable()) {
				OntimizeRootPaneUI.this.window.setCursor(Cursor.getPredefinedCursor(i));
			} else {
				OntimizeRootPaneUI.this.window.setCursor(Cursor.getDefaultCursor());
			}
		}

		@Override
		public void mouseEntered(MouseEvent mouseevent) {
			this.mouseMoved(mouseevent);
		}

		@Override
		public void mouseExited(MouseEvent mouseevent) {
			OntimizeRootPaneUI.this.window.setCursor(Cursor.getDefaultCursor());
		}

		@Override
		public void mouseDragged(MouseEvent mouseevent) {
			GraphicsConfiguration graphicsconfiguration = OntimizeRootPaneUI.this.window.getGraphicsConfiguration();
			Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(graphicsconfiguration);
			this.minimumYPos = graphicsconfiguration.getBounds().y + insets.top;

			// boolean flag =
			// SyntheticaLookAndFeel.isSystemPropertySet("synthetica.window.respectMinimumYPos");
			boolean flag = false;
			if (this.windowAction == MouseInputHandler.WINDOW_MOVE) {

				Point current = mouseevent.getLocationOnScreen();

				Point offset = new Point((int) current.getX() - (int) this.start_drag.getX(), (int) current.getY() - (int) this.start_drag.getY());

				Point new_location = new Point((int) (this.start_loc.getX() + offset.getX()), (int) (this.start_loc.getY() + offset.getY()));

				OntimizeRootPaneUI.this.window.setLocation(new_location);

			} else if (this.windowAction == MouseInputHandler.WINDOW_RESIZE) {
				Point point1 = mouseevent.getPoint();
				// Dimension dimension =
				// (Dimension)SyntheticaLookAndFeel.get("Synthetica.rootPane.minimumWindowSize",
				// window);
				// if(dimension == null)
				Dimension dimension = OntimizeRootPaneUI.this.window.getMinimumSize();
				Rectangle rectangle = OntimizeRootPaneUI.this.window.getBounds();

				Point current = mouseevent.getLocationOnScreen();
				Point offset = new Point((int) current.getX() - (int) this.start_drag.getX(), (int) current.getY() - (int) this.start_drag.getY());

				Rectangle rectangle1 = new Rectangle(rectangle);
				if ((this.resizeType == MouseInputHandler.RIGHT_RESIZE) || (this.resizeType == MouseInputHandler.TOP_RIGHT_RESIZE) || (this.resizeType == MouseInputHandler.DOWN_RIGHT_RESIZE)) {
					// rectangle.width = Math.max(dimension.width,
					// (dragDimension.width + point1.x)- dragXOffset);
					rectangle.width = (int) (this.start_dimension.width + offset.getX());
				}

				if ((this.resizeType == MouseInputHandler.DOWN_RESIZE) || (this.resizeType == MouseInputHandler.DOWN_LEFT_RESIZE) || (this.resizeType == MouseInputHandler.DOWN_RIGHT_RESIZE)) {
					// rectangle.height =
					// Math.max(dimension.height,(dragDimension.height + point1.y) -
					// dragYOffset);
					rectangle.height = (int) (this.start_dimension.height + offset.getY());
				}

				if ((this.resizeType == MouseInputHandler.TOP_RESIZE) || (this.resizeType == MouseInputHandler.TOP_LEFT_RESIZE) || (this.resizeType == MouseInputHandler.TOP_RIGHT_RESIZE)) {

					//					rectangle.height = Math.max(dimension.height, (rectangle.height - point1.y) + dragYOffset);
					rectangle.height = (int) (this.start_dimension.height - offset.getY());

					//					if (rectangle.height != dimension.height)
					rectangle.y = (int)(this.start_dimension.y + offset.getY());
				}

				if ((this.resizeType == MouseInputHandler.LEFT_RESIZE) || (this.resizeType == MouseInputHandler.TOP_LEFT_RESIZE) || (this.resizeType == MouseInputHandler.DOWN_LEFT_RESIZE)) {
					//					rectangle.width = Math.max(dimension.width, (rectangle.width - point1.x) + dragXOffset);

					rectangle.width = (int) (this.start_dimension.width - offset.getX());

					//					if (rectangle.width != dimension.width)
					rectangle.x =(int)(this.start_dimension.x + offset.getX());
				}

				if (flag && (rectangle.y < this.minimumYPos)) {
					rectangle.y = this.minimumYPos;
				}
				if (!rectangle.equals(rectangle1)) {
					OntimizeRootPaneUI.this.window.setBounds(rectangle);
				}

				OntimizeRootPaneUI.logger.debug("WINDOW_RESIZE: W:{} H: {}",rectangle.width, rectangle.height);
			}
		}

		@Override
		public void mouseClicked(MouseEvent mouseevent) {
			// if(frame == null)
			// return;
			// Point point = SwingUtilities.convertPoint(window,
			// mouseevent.getPoint(), titlePane);
			// if(titlePane != null && titlePane.contains(point) &&
			// mouseevent.getClickCount() == 2 && (mouseevent.getModifiers() &
			// 0x10) == 16)
			// if(frame.isResizable() && isFrameResizable())
			// ((SyntheticaTitlePane)titlePane).maximize();
			// else
			// if(frame.isResizable() && !isFrameResizable())
			// ((SyntheticaTitlePane)titlePane).restore();
		}

		protected int position2Cursor(Window window, int x, int y) {
			Insets insets = OntimizeRootPaneUI.this.rootPane.getBorder().getBorderInsets(OntimizeRootPaneUI.this.rootPane);
			int width = window.getWidth();
			int height = window.getHeight();
			if ((x < insets.left) && (y < insets.top)) {
				return MouseInputHandler.TOP_LEFT_RESIZE;
			}
			if ((x > (width - insets.right)) && (y < insets.top)) {
				return MouseInputHandler.TOP_RIGHT_RESIZE;
			}
			if ((x < insets.left) && (y > (height - insets.bottom))) {
				return MouseInputHandler.DOWN_LEFT_RESIZE;
			}
			if ((x > (width - insets.right)) && (y > (height - insets.bottom))) {
				return MouseInputHandler.DOWN_RIGHT_RESIZE;
			}
			if (x < insets.left) {
				return MouseInputHandler.LEFT_RESIZE;
			}
			if (x > (width - insets.right)) {
				return MouseInputHandler.RIGHT_RESIZE;
			}
			if (y < insets.top) {
				return MouseInputHandler.TOP_RESIZE;
			}
			return y <= (height - insets.bottom) ? 0 : MouseInputHandler.DOWN_RESIZE;
		}

		protected boolean isFrameResizable() {
			return (this.frame != null) && this.frame.isResizable() && ((this.frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0);
		}

		protected boolean isDialogResizable() {
			return (this.dialog != null) && this.dialog.isResizable();
		}

		protected boolean isWindowResizable() {
			return this.isFrameResizable() || this.isDialogResizable();
		}
	}

	public static ComponentUI createUI(JComponent jcomponent) {
		return new OntimizeRootPaneUI();
	}

	@Override
	public void installUI(JComponent component) {
		super.installUI(component);
		this.rootPane = (JRootPane) component;

		int style = this.rootPane.getWindowDecorationStyle();
		if (style != JRootPane.NONE) {
			this.installClientDecorations(this.rootPane);
		}
	}

	// TODO uninstallClienteDecorations
	protected void uninstallClientDecorations(JRootPane jrootpane) {
		// if(titlePane != null && (titlePane instanceof SyntheticaTitlePane))
		// ((SyntheticaTitlePane)titlePane).uninstallListeners(jrootpane);
		// setTitlePane(jrootpane, null);
		// uninstallBorder(jrootpane);
		// uninstallWindowListeners(jrootpane);
		// uninstallLayout(jrootpane);
	}

	/**
	 * Installs the necessary state onto the JRootPane to render client
	 * decorations. This is ONLY invoked if the <code>JRootPane</code> has a
	 * decoration style other than <code>JRootPane.NONE</code>.
	 */
	protected void installClientDecorations(JRootPane root) {
		this.installBorder(root);

		this.titlePane = this.createTitlePane(root);
		this.setTitlePane(root, this.titlePane);

		this.installWindowListeners(root, root.getParent());
		this.installLayout(root);
		if (this.window != null) {
			root.revalidate();
			root.repaint();
		}
	}

	/**
	 * Returns the <code>JComponent</code> to render the window decoration style.
	 */
	protected JComponent createTitlePane(JRootPane root) {
		return new OntimizeTitlePane(root, this);
	}

	protected void setTitlePane(JRootPane jrootpane, JComponent jcomponent) {
		JLayeredPane jlayeredpane = jrootpane.getLayeredPane();
		if (this.titlePane != null) {
			this.titlePane.setVisible(false);
			jlayeredpane.remove(this.titlePane);
		}
		if (jcomponent != null) {
			jlayeredpane.add(jcomponent, JLayeredPane.FRAME_CONTENT_LAYER);
			jcomponent.setVisible(true);
		}
		this.titlePane = jcomponent;
	}

	/**
	 * Installs the appropriate <code>Border</code> onto the
	 * <code>JRootPane</code>.
	 */
	public void installBorder(JRootPane root) {
		int style = root.getWindowDecorationStyle();

		if (style != JRootPane.NONE) {
			root.setBorder(new Border() {
				@Override
				public void paintBorder(Component component, Graphics g, int x, int y, int width, int height) {
					javax.swing.plaf.synth.SynthContext synthcontext = OntimizeLookAndFeel.createContext((JComponent) component, Region.ROOT_PANE, 0);
					OntimizePainter.getInstance().paintRootPaneBorder(synthcontext, g, x, y, width, height);
				}

				@Override
				public boolean isBorderOpaque() {
					return false;
				}

				@Override
				public Insets getBorderInsets(Component c) {
					if ((OntimizeRootPaneUI.this.window instanceof Frame) && ((((Frame) OntimizeRootPaneUI.this.window).getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH)) {
						return new Insets(0, 0, 0, 0);
					}

					Object insets = OntimizeLookAndFeel.lookup("RootPane.border.insets");
					if (insets instanceof Insets){
						return (Insets)insets;
					}
					return new Insets(5, 5, 5, 5);
				}
			});
		}
	}

	/**
	 * Returns a <code>LayoutManager</code> that will be set on the
	 * <code>JRootPane</code>.
	 */
	protected LayoutManager createLayoutManager() {
		return new OntimizeRootLayout();
	}

	/**
	 * Installs the appropriate LayoutManager on the <code>JRootPane</code> to
	 * render the window decorations.
	 */
	protected void installLayout(JRootPane root) {
		if (this.layoutManager == null) {
			this.layoutManager = this.createLayoutManager();
		}
		this.savedOldLayout = root.getLayout();
		root.setLayout(this.layoutManager);
	}

	// TODO uninstallWindowListeners
	protected void uninstallWindowListeners(JRootPane jrootpane) {

	}

	protected void installWindowListeners(JRootPane jrootpane, Component component) {
		this.window = (component instanceof Window) ? (Window) component : SwingUtilities.getWindowAncestor(component);
		if (this.window != null) {
			if (this.mouseInputListener == null) {
				this.mouseInputListener = new MouseInputHandler(null);
			}
			this.window.addMouseListener(this.mouseInputListener);
			this.window.addMouseMotionListener(this.mouseInputListener);

			if ((this.windowListener == null) && !OntimizeLookAndFeel.isWindowOpacityEnabled(this.window)) {
				// if(OS.getCurrentOS() == OS.Mac)
				// SyntheticaLookAndFeel.setWindowOpaque(window, false);
				this.windowListener = new WindowAdapter() {
					final OntimizeRootPaneUI ontimizeRootPaneUI;

					@Override
					public void windowOpened(WindowEvent windowevent) {
						Window window1 = windowevent.getWindow();
						if (!OntimizeLookAndFeel.isWindowOpacityEnabled(OntimizeRootPaneUI.this.window)) {
							AWTUtilities.setWindowOpaque(window1, false);
							// if(SyntheticaLookAndFeel.getBoolean("Synthetica.window.contentPane.opaque",
							// window, true))
							// if((window1 instanceof JDialog) &&
							// (((JDialog)window1).getContentPane() instanceof
							// JComponent))
							// ((JComponent)((JDialog)window1).getContentPane()).setOpaque(true);
							// else
							// if((window1 instanceof JFrame) &&
							// (((JFrame)window1).getContentPane() instanceof
							// JComponent))
							// ((JComponent)((JFrame)window1).getContentPane()).setOpaque(true);
						} else {
							AWTUtilities.setWindowOpaque(window1, true);
						}
					}

					{
						this.ontimizeRootPaneUI = OntimizeRootPaneUI.this;

					}
				};
				this.window.addWindowListener(this.windowListener);
			}

			if ((this.windowResizeListener == null) && OntimizeLookAndFeel.isWindowShapeEnabled(this.window)) {
				// if(OS.getCurrentOS() == OS.Mac){
				// OntimizeLookAndFeel.updateWindowShape(window);
				// } else{
				this.windowResizeListener = new ComponentAdapter() {
					OntimizeRootPaneUI ontimizeLookAndFeel;

					@Override
					public void componentResized(ComponentEvent componentevent) {
						Window window1 = (Window) componentevent.getComponent();
						OntimizeLookAndFeel.updateWindowShape(window1);
					}

					{
						this.ontimizeLookAndFeel = OntimizeRootPaneUI.this;
					}
				};
			}
			this.window.addComponentListener(this.windowResizeListener);
		}
		// }
	}

	/**
	 * Returns the <code>JComponent</code> rendering the title pane. If this
	 * returns null, it implies there is no need to render window decorations.
	 *
	 * @return the current window title pane, or null
	 * @see #setTitlePane
	 */
	public JComponent getTitlePane() {
		return this.titlePane;
	}

	/**
	 * A custom layout manager that is responsible for the layout of layeredPane,
	 * glassPane, menuBar and titlePane, if one has been installed.
	 */
	// NOTE: Ideally this would extends JRootPane.RootLayout, but that
	// would force this to be non-static.
	protected static class OntimizeRootLayout implements LayoutManager2 {
		/**
		 * Returns the amount of space the layout would like to have.
		 *
		 * @param the
		 *           Container for which this layout manager is being used
		 * @return a Dimension object containing the layout's preferred size
		 */
		@Override
		public Dimension preferredLayoutSize(Container container) {
			Dimension cpd, mbd, tpd;
			int cpWidth = 0;
			int cpHeight = 0;
			int mbWidth = 0;
			int mbHeight = 0;
			int tpWidth = 0;
			int tpHeight = 0;

			Insets insets = container.getInsets();
			JRootPane root = (JRootPane) container;

			if (root.getContentPane() != null) {
				cpd = root.getContentPane().getPreferredSize();
			} else {
				cpd = root.getSize();
			}
			if (cpd != null) {
				cpWidth = cpd.width;
				cpHeight = cpd.height;
			}

			if (root.getMenuBar() != null) {
				mbd = root.getMenuBar().getPreferredSize();
				if (mbd != null) {
					mbWidth = mbd.width;
					mbHeight = mbd.height;
				}
			}

			if ((root.getWindowDecorationStyle() != JRootPane.NONE) && (root.getUI() instanceof OntimizeRootPaneUI)) {
				JComponent titlePane = ((OntimizeRootPaneUI) root.getUI()).getTitlePane();
				if (titlePane != null) {
					tpd = titlePane.getPreferredSize();
					if (tpd != null) {
						tpWidth = tpd.width;
						tpHeight = tpd.height;
					}
				}
			}

			return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth) + insets.left + insets.right, cpHeight + mbHeight + tpWidth + insets.top
					+ insets.bottom);
		}

		/**
		 * Returns the minimum amount of space the layout needs.
		 *
		 * @param the
		 *           Container for which this layout manager is being used
		 * @return a Dimension object containing the layout's minimum size
		 */
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			Dimension cpd, mbd, tpd;
			int cpWidth = 0;
			int cpHeight = 0;
			int mbWidth = 0;
			int mbHeight = 0;
			int tpWidth = 0;
			int tpHeight = 0;
			Insets i = parent.getInsets();
			JRootPane root = (JRootPane) parent;

			if (root.getContentPane() != null) {
				cpd = root.getContentPane().getMinimumSize();
			} else {
				cpd = root.getSize();
			}
			if (cpd != null) {
				cpWidth = cpd.width;
				cpHeight = cpd.height;
			}

			if (root.getMenuBar() != null) {
				mbd = root.getMenuBar().getMinimumSize();
				if (mbd != null) {
					mbWidth = mbd.width;
					mbHeight = mbd.height;
				}
			}
			if ((root.getWindowDecorationStyle() != JRootPane.NONE) && (root.getUI() instanceof OntimizeRootPaneUI)) {
				JComponent titlePane = ((OntimizeRootPaneUI) root.getUI()).getTitlePane();
				if (titlePane != null) {
					tpd = titlePane.getMinimumSize();
					if (tpd != null) {
						tpWidth = tpd.width;
						tpHeight = tpd.height;
					}
				}
			}

			return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth) + i.left + i.right, cpHeight + mbHeight + tpWidth + i.top + i.bottom);
		}

		/**
		 * Returns the maximum amount of space the layout can use.
		 *
		 * @param the
		 *           Container for which this layout manager is being used
		 * @return a Dimension object containing the layout's maximum size
		 */
		@Override
		public Dimension maximumLayoutSize(Container target) {
			Dimension cpd, mbd, tpd;
			int cpWidth = Integer.MAX_VALUE;
			int cpHeight = Integer.MAX_VALUE;
			int mbWidth = Integer.MAX_VALUE;
			int mbHeight = Integer.MAX_VALUE;
			int tpWidth = Integer.MAX_VALUE;
			int tpHeight = Integer.MAX_VALUE;
			Insets i = target.getInsets();
			JRootPane root = (JRootPane) target;

			if (root.getContentPane() != null) {
				cpd = root.getContentPane().getMaximumSize();
				if (cpd != null) {
					cpWidth = cpd.width;
					cpHeight = cpd.height;
				}
			}

			if (root.getMenuBar() != null) {
				mbd = root.getMenuBar().getMaximumSize();
				if (mbd != null) {
					mbWidth = mbd.width;
					mbHeight = mbd.height;
				}
			}

			if ((root.getWindowDecorationStyle() != JRootPane.NONE) && (root.getUI() instanceof OntimizeRootPaneUI)) {
				JComponent titlePane = ((OntimizeRootPaneUI) root.getUI()).getTitlePane();
				if (titlePane != null) {
					tpd = titlePane.getMaximumSize();
					if (tpd != null) {
						tpWidth = tpd.width;
						tpHeight = tpd.height;
					}
				}
			}

			int maxHeight = Math.max(Math.max(cpHeight, mbHeight), tpHeight);
			// Only overflows if 3 real non-MAX_VALUE heights, sum to > MAX_VALUE
			// Only will happen if sums to more than 2 billion units. Not likely.
			if (maxHeight != Integer.MAX_VALUE) {
				maxHeight = cpHeight + mbHeight + tpHeight + i.top + i.bottom;
			}

			int maxWidth = Math.max(Math.max(cpWidth, mbWidth), tpWidth);
			// Similar overflow comment as above
			if (maxWidth != Integer.MAX_VALUE) {
				maxWidth += i.left + i.right;
			}

			return new Dimension(maxWidth, maxHeight);
		}

		/**
		 * Instructs the layout manager to perform the layout for the specified
		 * container.
		 *
		 * @param the
		 *           Container for which this layout manager is being used
		 */
		@Override
		public void layoutContainer(Container parent) {
			JRootPane root = (JRootPane) parent;
			Rectangle b = root.getBounds();
			Insets i = root.getInsets();
			int nextY = 0;
			int w = b.width - i.right - i.left;
			int h = b.height - i.top - i.bottom;

			if (root.getLayeredPane() != null) {
				root.getLayeredPane().setBounds(i.left, i.top, w, h);
			}
			if (root.getGlassPane() != null) {
				root.getGlassPane().setBounds(i.left, i.top, w, h);
			}
			// Note: This is laying out the children in the layeredPane,
			// technically, these are not our children.
			if ((root.getWindowDecorationStyle() != JRootPane.NONE) && (root.getUI() instanceof OntimizeRootPaneUI)) {
				JComponent titlePane = ((OntimizeRootPaneUI) root.getUI()).getTitlePane();
				if (titlePane != null) {
					Dimension tpd = titlePane.getPreferredSize();
					if (tpd != null) {
						int tpHeight = tpd.height;
						titlePane.setBounds(0, 0, w, tpHeight);
						nextY += tpHeight;
					}
				}
			}
			if (root.getMenuBar() != null) {
				Dimension mbd = root.getMenuBar().getPreferredSize();
				root.getMenuBar().setBounds(0, nextY, w, mbd.height);
				nextY += mbd.height;
			}
			if (root.getContentPane() != null) {
				Dimension cpd = root.getContentPane().getPreferredSize();
				root.getContentPane().setBounds(0, nextY, w, h < nextY ? 0 : h - nextY);
			}
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

		@Override
		public void addLayoutComponent(Component comp, Object constraints) {
		}

		@Override
		public float getLayoutAlignmentX(Container target) {
			return 0.0f;
		}

		@Override
		public float getLayoutAlignmentY(Container target) {
			return 0.0f;
		}

		@Override
		public void invalidateLayout(Container target) {
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		super.propertyChange(propertyChangeEvent);
		String s = propertyChangeEvent.getPropertyName();
		if (s == null) {
			return;
		}
		if (s.equals("windowDecorationStyle")) {
			this.uninstallClientDecorations(this.rootPane);
			if (this.rootPane.getWindowDecorationStyle() != JRootPane.NONE) {
				this.installClientDecorations(this.rootPane);
			}
		} else if (s.equals("ancestor")) {
			this.uninstallWindowListeners(this.rootPane);
			if (this.rootPane.getWindowDecorationStyle() != JRootPane.NONE) {
				this.installWindowListeners(this.rootPane, this.rootPane.getParent());
			}
		}
	}
}
