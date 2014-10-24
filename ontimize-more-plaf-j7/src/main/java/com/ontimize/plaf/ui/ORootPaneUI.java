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
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;

import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.border.OntimizeBorder;
import com.ontimize.plaf.component.OTitlePane;
import com.ontimize.plaf.utils.ContextUtils;
import com.ontimize.util.AWTUtilities;

public class ORootPaneUI extends BasicRootPaneUI implements SynthUI{

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
	
	protected SynthStyle style;

	
	
	public static ComponentUI createUI(JComponent jcomponent) {
		return new ORootPaneUI();
	}
	
	@Override
	protected void installDefaults(JRootPane c) {
		updateStyle(c);
	}
	
	/**
     * Update the control style.
     *
     * @param c the component.
     */
    protected void updateStyle(JComponent c) {
        SynthContext context  = getContext(c, ENABLED);
        SynthStyle      oldStyle = style;

        style = OntimizeLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            if (oldStyle != null) {
                uninstallKeyboardActions((JRootPane) c);
                installKeyboardActions((JRootPane) c);
            }
        }

        
    }
	

	@Override
	public void installUI(JComponent component) {
		super.installUI(component);
		rootPane = (JRootPane) component;

		int style = rootPane.getWindowDecorationStyle();
		if (style != JRootPane.NONE) {
			installClientDecorations(rootPane);
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
		installBorder(root);

		titlePane = createTitlePane(root);
		setTitlePane(root, titlePane);

		installWindowListeners(root, root.getParent());
		installLayout(root);
		if (window != null) {
			root.revalidate();
			root.repaint();
		}
	}

	/**
	 * Returns the <code>JComponent</code> to render the window decoration style.
	 */
	protected JComponent createTitlePane(JRootPane root) {
		return new OTitlePane(root, this);
//		return new OntimizeTitlePane(root, this);
	}

	protected void setTitlePane(JRootPane jrootpane, JComponent jcomponent) {
		JLayeredPane jlayeredpane = jrootpane.getLayeredPane();
		if (titlePane != null) {
			titlePane.setVisible(false);
			jlayeredpane.remove(titlePane);
		}
		if (jcomponent != null) {
			jlayeredpane.add(jcomponent, JLayeredPane.FRAME_CONTENT_LAYER);
			jcomponent.setVisible(true);
		}
		titlePane = jcomponent;
	}

	/**
	 * Installs the appropriate <code>Border</code> onto the
	 * <code>JRootPane</code>.
	 */
	public void installBorder(JRootPane root) {
		int style = root.getWindowDecorationStyle();
		if (style == JRootPane.NONE) {
            LookAndFeel.uninstallBorder(root);
        } else {
            root.setBorder(new OntimizeBorder(this, new Insets(0, 1, 1, 1)));
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
		if (layoutManager == null) {
			layoutManager = createLayoutManager();
		}
		savedOldLayout = root.getLayout();
		root.setLayout(layoutManager);
	}

	// TODO uninstallWindowListeners
	protected void uninstallWindowListeners(JRootPane jrootpane) {

	}

	protected void installWindowListeners(JRootPane jrootpane, Component component) {
		window = (component instanceof Window) ? (Window) component : SwingUtilities.getWindowAncestor(component);
		if (window != null) {
			if (mouseInputListener == null) mouseInputListener = new MouseInputHandler();
			window.addMouseListener(mouseInputListener);
			window.addMouseMotionListener(mouseInputListener);

			if (windowListener == null && !OntimizeLookAndFeel.isWindowOpacityEnabled(window)) {
				// if(OS.getCurrentOS() == OS.Mac)
				// SyntheticaLookAndFeel.setWindowOpaque(window, false);
				windowListener = new WindowAdapter() {
					final ORootPaneUI ontimizeRootPaneUI;

					public void windowOpened(WindowEvent windowevent) {
						Window window1 = windowevent.getWindow();
						if (!OntimizeLookAndFeel.isWindowOpacityEnabled(window)) {
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
						ontimizeRootPaneUI = ORootPaneUI.this;

					}
				};
				window.addWindowListener(windowListener);
			}

			if (windowResizeListener == null && OntimizeLookAndFeel.isWindowShapeEnabled(window))
			// if(OS.getCurrentOS() == OS.Mac){
			// OntimizeLookAndFeel.updateWindowShape(window);
			// } else{
			windowResizeListener = new ComponentAdapter() {
				ORootPaneUI ontimizeLookAndFeel;

				public void componentResized(ComponentEvent componentevent) {
					Window window1 = (Window) componentevent.getComponent();
					OntimizeLookAndFeel.updateWindowShape(window1);
				}

				{
					ontimizeLookAndFeel = ORootPaneUI.this;
				}
			};
			window.addComponentListener(windowResizeListener);
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
		return titlePane;
	}
	
	/**
     * Returns the <code>JRootPane</code> we're providing the look and
     * feel for.
     */
    protected JRootPane getRootPane() {
        return rootPane;
    }
	
	
	//*****************************************
	//Synth Methods....
	//*****************************************
	/**
     * @see sun.swing.plaf.synth.SynthUI#getContext(javax.swing.JComponent)
     */
    public SynthContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    /**
     * Get the SynthContext.
     *
     * @param  c     the component.
     * @param  state the state.
     *
     * @return the SynthContext.
     */
    protected SynthContext getContext(JComponent c, int state) {
    	if(this.style == null){
    		this.style = OntimizeLookAndFeel.getOntimizeStyle(c, OntimizeLookAndFeel.getRegion(c));
    	}
    	return new SynthContext( c, OntimizeLookAndFeel.getRegion(c), this.style, state);
    }

    /**
     * Get the component state.
     *
     * @param  c the component.
     *
     * @return the state.
     */
    protected int getComponentState(JComponent c) {
        return OntimizeLookAndFeel.getComponentState(c);
    }
	
	
	//*****************************************
	//Paint Methods....
	//*****************************************
    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    public void update(Graphics g, JComponent c) {
    	SynthContext context = getContext(c);

    	OntimizeLookAndFeel.update(context, g);
        if (((JRootPane) c).getWindowDecorationStyle() != JRootPane.NONE) {
            ContextUtils.getPainter(context).paintRootPaneBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        } 
//        else if (PlatformUtils.isMac()) {
//            // We may need to paint the rootpane on a Mac if the window is
//            // decorated.
//            boolean   shouldPaint       = false;
//            Container toplevelContainer = c.getParent();
//
//            if (toplevelContainer instanceof JFrame) {
//                shouldPaint = !((JFrame) toplevelContainer).isUndecorated();
//            }
//
//            if (shouldPaint) {
//                if (!paintTextured) {
//                    g.setColor(c.getBackground());
//                    g.fillRect(0, 0, c.getWidth(), c.getHeight());
//                } else if (isWindowFocused.isInState(c)) {
//                    contentActive.paint((Graphics2D) g, c, c.getWidth(), c.getHeight());
//                } else {
//                    contentInactive.paint((Graphics2D) g, c, c.getWidth(), c.getHeight());
//                }
//            }
//        }

        paint(context, g);
        
    }

    /**
     * @see javax.swing.plaf.ComponentUI#paint(java.awt.Graphics, javax.swing.JComponent)
     */
    public void paint(Graphics g, JComponent c) {
    	SynthContext context = getContext(c);

        paint(context, g);
        
    }

    /**
     * Paint the object.
     *
     * @param context the SynthContext.
     * @param g       the Graphics context.
     */
    protected void paint(SynthContext context, Graphics g) {
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ContextUtils.getPainter(context).paintRootPaneBorder(context, g, x, y, w, h);
    }
    
    
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		super.propertyChange(propertyChangeEvent);
		String s = propertyChangeEvent.getPropertyName();
		if (s == null) return;
		if (s.equals("menubar")) {
			System.out.println("Ok");
		}
		if (s.equals("windowDecorationStyle")) {
			uninstallClientDecorations(rootPane);
			if (rootPane.getWindowDecorationStyle() != JRootPane.NONE) installClientDecorations(rootPane);
		} else if (s.equals("ancestor")) {
			uninstallWindowListeners(rootPane);
			if (rootPane.getWindowDecorationStyle() != JRootPane.NONE) installWindowListeners(rootPane, rootPane.getParent());
		}
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

			if (root.getWindowDecorationStyle() != JRootPane.NONE && (root.getUI() instanceof ORootPaneUI)) {
				JComponent titlePane = ((ORootPaneUI) root.getUI()).getTitlePane();
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
			if (root.getWindowDecorationStyle() != JRootPane.NONE && (root.getUI() instanceof ORootPaneUI)) {
				JComponent titlePane = ((ORootPaneUI) root.getUI()).getTitlePane();
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

			if (root.getWindowDecorationStyle() != JRootPane.NONE && (root.getUI() instanceof ORootPaneUI)) {
				JComponent titlePane = ((ORootPaneUI) root.getUI()).getTitlePane();
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
			if (root.getWindowDecorationStyle() != JRootPane.NONE && (root.getUI() instanceof ORootPaneUI)) {
				JComponent titlePane = ((ORootPaneUI) root.getUI()).getTitlePane();
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

		public void addLayoutComponent(String name, Component comp) {
		}

		public void removeLayoutComponent(Component comp) {
		}

		public void addLayoutComponent(Component comp, Object constraints) {
		}

		public float getLayoutAlignmentX(Container target) {
			return 0.0f;
		}

		public float getLayoutAlignmentY(Container target) {
			return 0.0f;
		}

		public void invalidateLayout(Container target) {
		}
	}

    
    
	protected class MouseInputHandler_ implements MouseInputListener {

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

		final ORootPaneUI ontimizeRootPaneUI;

		Point start_drag = null;

		Point start_loc = null;

		Rectangle start_dimension = null;

		protected MouseInputHandler_() {
			ontimizeRootPaneUI = ORootPaneUI.this;
			frame = null;
			dialog = null;
			if (window instanceof Frame) {
				frame = (Frame) window;
			} else if (window instanceof Dialog) {
				dialog = (Dialog) window;
			}
		}

		MouseInputHandler_(MouseInputHandler mouseinputhandler) {
			this();
		}

		public void mousePressed(MouseEvent mouseevent) {
			if (rootPane.getWindowDecorationStyle() == JRootPane.NONE) return;
			window.toFront();
			minimumYPos = window.getGraphicsConfiguration().getBounds().y;
			Point point = mouseevent.getPoint();
			Point point1 = SwingUtilities.convertPoint(window, point, titlePane);
			int i = position2Cursor(window, mouseevent.getX(), mouseevent.getY());
			if (i == 0 && titlePane != null && titlePane.contains(point1) && (dialog != null || frame != null && frame.getExtendedState() != 6)) {
				windowAction = WINDOW_MOVE;

				Point point3 = mouseevent.getPoint();
				SwingUtilities.convertPointToScreen(point3, (Component) window);
				this.start_drag = point3;
				this.start_loc = window.getLocation();

				dragXOffset = point.x;
				dragYOffset = point.y;
			} else if (isWindowResizable()) {
				windowAction = WINDOW_RESIZE;
				dragXOffset = point.x;
				dragYOffset = point.y;

				this.start_dimension = window.getBounds();
				this.start_drag = mouseevent.getPoint();
				SwingUtilities.convertPointToScreen(this.start_drag, (Component) window);

				dragDimension = new Dimension(window.getWidth(), window.getHeight());
				resizeType = position2Cursor(window, point.x, point.y);
			}
		}

		public void mouseReleased(MouseEvent mouseevent) {
			if (windowAction == WINDOW_RESIZE && !window.isValid()) {
				window.validate();
				rootPane.repaint();
			}
			windowAction = -1;
			window.setCursor(Cursor.getDefaultCursor());
		}

		public void mouseMoved(MouseEvent mouseevent) {
			if (rootPane.getWindowDecorationStyle() == JRootPane.NONE) return;

			int i = position2Cursor(window, mouseevent.getX(), mouseevent.getY());
			if (i != 0 && isWindowResizable()) window.setCursor(Cursor.getPredefinedCursor(i));
			else
				window.setCursor(Cursor.getDefaultCursor());
		}

		public void mouseEntered(MouseEvent mouseevent) {
			mouseMoved(mouseevent);
		}

		public void mouseExited(MouseEvent mouseevent) {
			window.setCursor(Cursor.getDefaultCursor());
		}

		public void mouseDragged(MouseEvent mouseevent) {
			GraphicsConfiguration graphicsconfiguration = window.getGraphicsConfiguration();
			Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(graphicsconfiguration);
			minimumYPos = graphicsconfiguration.getBounds().y + insets.top;

			// boolean flag =
			// SyntheticaLookAndFeel.isSystemPropertySet("synthetica.window.respectMinimumYPos");
			boolean flag = false;
			if (windowAction == WINDOW_MOVE) {

				Point current = mouseevent.getLocationOnScreen();

				Point offset = new Point((int) current.getX() - (int) start_drag.getX(), (int) current.getY() - (int) start_drag.getY());

				Point new_location = new Point((int) (this.start_loc.getX() + offset.getX()), (int) (this.start_loc.getY() + offset.getY()));

				window.setLocation(new_location);

			} else if (windowAction == WINDOW_RESIZE) {
				Point point1 = mouseevent.getPoint();
				// Dimension dimension =
				// (Dimension)SyntheticaLookAndFeel.get("Synthetica.rootPane.minimumWindowSize",
				// window);
				// if(dimension == null)
				Dimension dimension = window.getMinimumSize();
				Rectangle rectangle = window.getBounds();

				Point current = mouseevent.getLocationOnScreen();
				Point offset = new Point((int) current.getX() - (int) start_drag.getX(), (int) current.getY() - (int) start_drag.getY());

				Rectangle rectangle1 = new Rectangle(rectangle);
				if (resizeType == RIGHT_RESIZE || resizeType == TOP_RIGHT_RESIZE || resizeType == DOWN_RIGHT_RESIZE) {
					// rectangle.width = Math.max(dimension.width,
					// (dragDimension.width + point1.x)- dragXOffset);
					rectangle.width = (int) (this.start_dimension.width + offset.getX());
				}

				if (resizeType == DOWN_RESIZE || resizeType == DOWN_LEFT_RESIZE || resizeType == DOWN_RIGHT_RESIZE) {
					// rectangle.height =
					// Math.max(dimension.height,(dragDimension.height + point1.y) -
					// dragYOffset);
					rectangle.height = (int) (this.start_dimension.height + offset.getY());
				}

				if (resizeType == TOP_RESIZE || resizeType == TOP_LEFT_RESIZE || resizeType == TOP_RIGHT_RESIZE) {
					
//					rectangle.height = Math.max(dimension.height, (rectangle.height - point1.y) + dragYOffset);
					rectangle.height = (int) (this.start_dimension.height - offset.getY());
					
//					if (rectangle.height != dimension.height) 
						rectangle.y = (int)(this.start_dimension.y + offset.getY());
				}

				if (resizeType == LEFT_RESIZE || resizeType == TOP_LEFT_RESIZE || resizeType == DOWN_LEFT_RESIZE) {
//					rectangle.width = Math.max(dimension.width, (rectangle.width - point1.x) + dragXOffset);
					
					rectangle.width = (int) (this.start_dimension.width - offset.getX());
					
//					if (rectangle.width != dimension.width) 
						rectangle.x =(int)(this.start_dimension.x + offset.getX());
				}

				if (flag && rectangle.y < minimumYPos) rectangle.y = minimumYPos;
				if (!rectangle.equals(rectangle1)) window.setBounds(rectangle);

				System.out.println("WINDOW_RESIZE: W:" + rectangle.width + " H:" + rectangle.height);
			}
		}

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
			if(rootPane.getBorder()!=null){
			Insets insets = rootPane.getBorder().getBorderInsets(rootPane);
			int width = window.getWidth();
			int height = window.getHeight();
			if (x < insets.left && y < insets.top) return TOP_LEFT_RESIZE;
			if (x > width - insets.right && y < insets.top) return TOP_RIGHT_RESIZE;
			if (x < insets.left && y > height - insets.bottom) return DOWN_LEFT_RESIZE;
			if (x > width - insets.right && y > height - insets.bottom) return DOWN_RIGHT_RESIZE;
			if (x < insets.left) return LEFT_RESIZE;
			if (x > width - insets.right) return RIGHT_RESIZE;
			if (y < insets.top) return TOP_RESIZE;
			return y <= height - insets.bottom ? 0 : DOWN_RESIZE;
			}
			return 0;
		}

		protected boolean isFrameResizable() {
			return frame != null && frame.isResizable() && (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0;
		}

		protected boolean isDialogResizable() {
			return dialog != null && dialog.isResizable();
		}

		protected boolean isWindowResizable() {
			return isFrameResizable() || isDialogResizable();
		}
	}

	
	
	
	
	
	 /**
     * The amount of space (in pixels) that the cursor is changed on.
     */
    protected static final int CORNER_DRAG_WIDTH = 16;

    /**
     * Region from edges that dragging is active from.
     */
    protected static final int BORDER_DRAG_THICKNESS = 5;
    
    /**
     * <code>Cursor</code> used to track the cursor set by the user.  
     * This is initially <code>Cursor.DEFAULT_CURSOR</code>.
     */
    protected Cursor lastCursor =
            Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    
    /**
     * Maps from positions to cursor type. Refer to calculateCorner and
     * calculatePosition for details of this.
     */
    protected static final int[] cursorMapping = new int[]
    { Cursor.NW_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR,
             Cursor.NE_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
      Cursor.NW_RESIZE_CURSOR, 0, 0, 0, Cursor.NE_RESIZE_CURSOR,
      Cursor.W_RESIZE_CURSOR, 0, 0, 0, Cursor.E_RESIZE_CURSOR,
      Cursor.SW_RESIZE_CURSOR, 0, 0, 0, Cursor.SE_RESIZE_CURSOR,
      Cursor.SW_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR,
             Cursor.SE_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR
    };

    
	/**
     * MouseInputHandler is responsible for handling resize/moving of
     * the Window. It sets the cursor directly on the Window when then
     * mouse moves over a hot spot.
     */
    protected class MouseInputHandler implements MouseInputListener {
        /**
         * Set to true if the drag operation is moving the window.
         */
        protected boolean isMovingWindow;

        /**
         * Used to determine the corner the resize is occuring from.
         */
        protected int dragCursor;

        /**
         * X location the mouse went down on for a drag operation.
         */
        protected int dragOffsetX;

        /**
         * Y location the mouse went down on for a drag operation.
         */
        protected int dragOffsetY;

        /**
         * Width of the window when the drag started.
         */
        protected int dragWidth;

        /**
         * Height of the window when the drag started.
         */
        protected int dragHeight;

        public void mousePressed(MouseEvent ev) {
            JRootPane rootPane = getRootPane();

            if (rootPane.getWindowDecorationStyle() == JRootPane.NONE) {
                return;
            }
            Point dragWindowOffset = ev.getPoint();
            Window w = (Window)ev.getSource();
            if (w != null) {
                w.toFront();
            }
            Point convertedDragWindowOffset = SwingUtilities.convertPoint(
                           w, dragWindowOffset, getTitlePane());

            Frame f = null;
            Dialog d = null;

            if (w instanceof Frame) {
                f = (Frame)w;
            } else if (w instanceof Dialog) {
                d = (Dialog)w;
            }

            int frameState = (f != null) ? f.getExtendedState() : 0;

            if (getTitlePane() != null &&
                        getTitlePane().contains(convertedDragWindowOffset)) {
                if ((f != null && ((frameState & Frame.MAXIMIZED_BOTH) == 0)
                        || (d != null))
                        && dragWindowOffset.y >= BORDER_DRAG_THICKNESS
                        && dragWindowOffset.x >= BORDER_DRAG_THICKNESS
                        && dragWindowOffset.x < w.getWidth()
                            - BORDER_DRAG_THICKNESS) {
                    isMovingWindow = true;
                    dragOffsetX = dragWindowOffset.x;
                    dragOffsetY = dragWindowOffset.y;
                }
            }
            else if (f != null && f.isResizable()
                    && ((frameState & Frame.MAXIMIZED_BOTH) == 0)
                    || (d != null && d.isResizable())) {
                dragOffsetX = dragWindowOffset.x;
                dragOffsetY = dragWindowOffset.y;
                dragWidth = w.getWidth();
                dragHeight = w.getHeight();
                dragCursor = getCursor(calculateCorner(
                             w, dragWindowOffset.x, dragWindowOffset.y));
            }
        }

        public void mouseReleased(MouseEvent ev) {
            if (dragCursor != 0 && window != null && !window.isValid()) {
                // Some Window systems validate as you resize, others won't,
                // thus the check for validity before repainting.
                window.validate();
                getRootPane().repaint();
            }
            isMovingWindow = false;
            dragCursor = 0;
        }

        public void mouseMoved(MouseEvent ev) {
            JRootPane root = getRootPane();

            if (root.getWindowDecorationStyle() == JRootPane.NONE) {
                return;
            }

            Window w = (Window)ev.getSource();

            Frame f = null;
            Dialog d = null;

            if (w instanceof Frame) {
                f = (Frame)w;
            } else if (w instanceof Dialog) {
                d = (Dialog)w;
            }

            // Update the cursor
            int cursor = getCursor(calculateCorner(w, ev.getX(), ev.getY()));

            if (cursor != 0 && ((f != null && (f.isResizable() &&
                    (f.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0))
                    || (d != null && d.isResizable()))) {
                w.setCursor(Cursor.getPredefinedCursor(cursor));
            }
            else {
                w.setCursor(lastCursor);
            }
        }

        protected void adjust(Rectangle bounds, Dimension min, int deltaX,
                            int deltaY, int deltaWidth, int deltaHeight) {
            bounds.x += deltaX;
            bounds.y += deltaY;
            bounds.width += deltaWidth;
            bounds.height += deltaHeight;
            if (min != null) {
                if (bounds.width < min.width) {
                    int correction = min.width - bounds.width;
                    if (deltaX != 0) {
                        bounds.x -= correction;
                    }
                    bounds.width = min.width;
                }
                if (bounds.height < min.height) {
                    int correction = min.height - bounds.height;
                    if (deltaY != 0) {
                        bounds.y -= correction;
                    }
                    bounds.height = min.height;
                }
            }
        }

        public void mouseDragged(MouseEvent ev) {
            Window w = (Window)ev.getSource();
            Point pt = ev.getPoint();

            if (isMovingWindow) {
                Point eventLocationOnScreen = ev.getLocationOnScreen();
                w.setLocation(eventLocationOnScreen.x - dragOffsetX,
                              eventLocationOnScreen.y - dragOffsetY); 
            }
            else if (dragCursor != 0) {
                Rectangle r = w.getBounds();
                Rectangle startBounds = new Rectangle(r);
                Dimension min = w.getMinimumSize();

                switch (dragCursor) {
                case Cursor.E_RESIZE_CURSOR:
                    adjust(r, min, 0, 0, pt.x + (dragWidth - dragOffsetX) -
                           r.width, 0);
                    break;
                case Cursor.S_RESIZE_CURSOR:
                    adjust(r, min, 0, 0, 0, pt.y + (dragHeight - dragOffsetY) -
                           r.height);
                    break;
                case Cursor.N_RESIZE_CURSOR:
                    adjust(r, min, 0, pt.y -dragOffsetY, 0,
                           -(pt.y - dragOffsetY));
                    break;
                case Cursor.W_RESIZE_CURSOR:
                    adjust(r, min, pt.x - dragOffsetX, 0,
                           -(pt.x - dragOffsetX), 0);
                    break;
                case Cursor.NE_RESIZE_CURSOR:
                    adjust(r, min, 0, pt.y - dragOffsetY,
                           pt.x + (dragWidth - dragOffsetX) - r.width,
                           -(pt.y - dragOffsetY));
                    break;
                case Cursor.SE_RESIZE_CURSOR:
                    adjust(r, min, 0, 0, 
                           pt.x + (dragWidth - dragOffsetX) - r.width,
                           pt.y + (dragHeight - dragOffsetY) -
                           r.height);
                    break;
                case Cursor.NW_RESIZE_CURSOR:
                    adjust(r, min, pt.x - dragOffsetX,
                           pt.y - dragOffsetY,
                           -(pt.x - dragOffsetX),
                           -(pt.y - dragOffsetY));
                    break;
                case Cursor.SW_RESIZE_CURSOR:
                    adjust(r, min, pt.x - dragOffsetX, 0,
                           -(pt.x - dragOffsetX),
                           pt.y + (dragHeight - dragOffsetY) - r.height);
                    break;
                default:
                    break;
                }
                if (!r.equals(startBounds)) {
                    w.setBounds(r);
                    // Defer repaint/validate on mouseReleased unless dynamic
                    // layout is active.
                    if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
                        w.validate();
                        getRootPane().repaint();
                    }
                }
            }
        }

        public void mouseEntered(MouseEvent ev) {
            Window w = (Window)ev.getSource();
            lastCursor = w.getCursor();
            mouseMoved(ev);
        }

        public void mouseExited(MouseEvent ev) {
            Window w = (Window)ev.getSource();
            w.setCursor(lastCursor);
        }

        public void mouseClicked(MouseEvent ev) {
            Window w = (Window)ev.getSource();
            Frame f = null;

            if (w instanceof Frame) {
                f = (Frame)w;
            } else {
                return;
            }

            Point convertedPoint = SwingUtilities.convertPoint(
                           w, ev.getPoint(), getTitlePane());

            int state = f.getExtendedState();
            if (getTitlePane() != null &&
                    getTitlePane().contains(convertedPoint)) {
                if ((ev.getClickCount() % 2) == 0 &&
                        ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0)) {
                    if (f.isResizable()) {
                        if ((state & Frame.MAXIMIZED_BOTH) != 0) {
                            f.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
                        }
                        else {
                            f.setExtendedState(state | Frame.MAXIMIZED_BOTH);
                        }
                        return;
                    }
                }
            }
        }

        /**
         * Returns the corner that contains the point <code>x</code>,
         * <code>y</code>, or -1 if the position doesn't match a corner.
         */
        protected int calculateCorner(Window w, int x, int y) {
            Insets insets = w.getInsets();
            int xPosition = calculatePosition(x - insets.left,
                    w.getWidth() - insets.left - insets.right);
            int yPosition = calculatePosition(y - insets.top,
                    w.getHeight() - insets.top - insets.bottom);

            if (xPosition == -1 || yPosition == -1) {
                return -1;
            }
            return yPosition * 5 + xPosition;
        }

        /**
         * Returns the Cursor to render for the specified corner. This returns
         * 0 if the corner doesn't map to a valid Cursor
         */
        protected int getCursor(int corner) {
            if (corner == -1) {
                return 0;
            }
            return cursorMapping[corner];
        }

        /**
         * Returns an integer indicating the position of <code>spot</code>
         * in <code>width</code>. The return value will be:
         * 0 if < BORDER_DRAG_THICKNESS
         * 1 if < CORNER_DRAG_WIDTH
         * 2 if >= CORNER_DRAG_WIDTH && < width - BORDER_DRAG_THICKNESS
         * 3 if >= width - CORNER_DRAG_WIDTH
         * 4 if >= width - BORDER_DRAG_THICKNESS
         * 5 otherwise
         */
        protected int calculatePosition(int spot, int width) {
            if (spot < BORDER_DRAG_THICKNESS) {
                return 0;
            }
            if (spot < CORNER_DRAG_WIDTH) {
                return 1;
            }
            if (spot >= (width - BORDER_DRAG_THICKNESS)) {
                return 4;
            }
            if (spot >= (width - CORNER_DRAG_WIDTH)) {
                return 3;
            }
            return 2;
        }
    }
    
}
