/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the Ontimize Pluggable Look and Feel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.ontimize.plaf.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.Painter;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;
import javax.swing.text.JTextComponent;

import com.ontimize.gui.table.EJTable;
import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.utils.ContextUtils;

/**
 * OntimizeScrollPaneUI implementation.
 *<p>
 * Minimum necessary copied from BasicScrollPaneUI with appropriate change to
 * support Apple-style horizontal wheel scrolling.
 * 
 * @see javax.swing.plaf.basic.BasicScrollPaneUI
 */
public class OScrollPaneUI extends BasicScrollPaneUI implements PropertyChangeListener, ScrollPaneConstants, SynthUI {
	protected MouseWheelListener       mouseScrollListener;
	protected SynthStyle               style;
	protected boolean                  viewportViewHasFocus = false;
	protected ViewportViewFocusHandler viewportViewFocusHandler;

	/**
	 * PropertyChangeListener installed on the vertical scrollbar.
	 */
	protected PropertyChangeListener   vsbPropertyChangeListener;

	/**
	 * PropertyChangeListener installed on the horizontal scrollbar.
	 */
	protected PropertyChangeListener   hsbPropertyChangeListener;

	protected Handler                  handler;

	protected Painter                  cornerPainter;

	/**
	 * State flag that shows whether setValue() was called from a user program
	 * before the value of "extent" was set in right-to-left component
	 * orientation.
	 */
	protected boolean                  setValueCalled       = false;

	public static ComponentUI createUI(JComponent x) {
		return new OScrollPaneUI();
	}

	@Override
	protected void installDefaults(JScrollPane scrollpane) {
		LookAndFeel.installBorder(scrollpane, "ScrollPane.border");
		LookAndFeel.installColorsAndFont(scrollpane, "ScrollPane.background", "ScrollPane.foreground", "ScrollPane.font");

		Border vpBorder = scrollpane.getViewportBorder();
		if ((vpBorder == null) || (vpBorder instanceof UIResource)) {
			vpBorder = UIManager.getBorder("ScrollPane.viewportBorder");
			scrollpane.setViewportBorder(vpBorder);
		}

		Object obj = UIManager.get("ScrollPane.cornerPainter");
		if ((obj != null) && (obj instanceof Painter)) {
			this.cornerPainter = (Painter) obj;
		}

		LookAndFeel.installProperty(scrollpane, "opaque", Boolean.FALSE);
		this.updateStyle(scrollpane);
	}

	@Override
	protected void uninstallDefaults(JScrollPane c) {
		SynthContext context = this.getContext(c, SynthConstants.ENABLED);

		this.style.uninstallDefaults(context);

		this.style = null;
		if (this.scrollpane.getViewportBorder() instanceof UIResource) {
			this.scrollpane.setViewportBorder(null);
		}
	}

	protected void updateStyle(JScrollPane c) {
		SynthContext context = this.getContext(c, SynthConstants.ENABLED);
		SynthStyle oldStyle = this.style;

		this.style = OntimizeLookAndFeel.updateStyle(context, this);
		if (this.style != oldStyle) {
			Border vpBorder = this.scrollpane.getViewportBorder();
			if ((vpBorder == null) || (vpBorder instanceof UIResource)) {
				this.scrollpane.setViewportBorder(new ViewportBorder(context));
			}
			if (oldStyle != null) {
				this.uninstallKeyboardActions(c);
				this.installKeyboardActions(c);
			}
		}

	}

	@Override
	protected void installListeners(JScrollPane c) {
		this.vsbChangeListener = this.createVSBChangeListener();
		this.vsbPropertyChangeListener = this.createVSBPropertyChangeListener();
		this.hsbChangeListener = this.createHSBChangeListener();
		this.hsbPropertyChangeListener = this.createHSBPropertyChangeListener();
		this.viewportChangeListener = this.createViewportChangeListener();
		this.spPropertyChangeListener = this.createPropertyChangeListener();

		JViewport viewport = this.scrollpane.getViewport();
		JScrollBar vsb = this.scrollpane.getVerticalScrollBar();
		JScrollBar hsb = this.scrollpane.getHorizontalScrollBar();

		if (viewport != null) {
			viewport.addChangeListener(this.viewportChangeListener);
		}
		if (vsb != null) {
			vsb.getModel().addChangeListener(this.vsbChangeListener);
			vsb.addPropertyChangeListener(this.vsbPropertyChangeListener);
		}
		if (hsb != null) {
			hsb.getModel().addChangeListener(this.hsbChangeListener);
			hsb.addPropertyChangeListener(this.hsbPropertyChangeListener);
		}

		this.scrollpane.addPropertyChangeListener(this.spPropertyChangeListener);

		this.mouseScrollListener = this.createMouseWheelListener();
		this.scrollpane.addMouseWheelListener(this.mouseScrollListener);

		// From SynthScrollPaneUI.
		c.addPropertyChangeListener(this);
		if (UIManager.getBoolean("ScrollPane.useChildTextComponentFocus")) {
			this.viewportViewFocusHandler = new ViewportViewFocusHandler();
			c.getViewport().addContainerListener(this.viewportViewFocusHandler);
			Component view = c.getViewport().getView();
			if (view instanceof JTextComponent) {
				view.addFocusListener(this.viewportViewFocusHandler);
			}
		}
	}

	@Override
	protected void uninstallListeners(JComponent c) {
		JViewport viewport = this.scrollpane.getViewport();
		JScrollBar vsb = this.scrollpane.getVerticalScrollBar();
		JScrollBar hsb = this.scrollpane.getHorizontalScrollBar();

		if (viewport != null) {
			viewport.removeChangeListener(this.viewportChangeListener);
		}
		if (vsb != null) {
			vsb.getModel().removeChangeListener(this.vsbChangeListener);
			vsb.removePropertyChangeListener(this.vsbPropertyChangeListener);
		}
		if (hsb != null) {
			hsb.getModel().removeChangeListener(this.hsbChangeListener);
			hsb.removePropertyChangeListener(this.hsbPropertyChangeListener);
		}

		this.scrollpane.removePropertyChangeListener(this.spPropertyChangeListener);

		if (this.mouseScrollListener != null) {
			this.scrollpane.removeMouseWheelListener(this.mouseScrollListener);
		}

		this.vsbChangeListener = null;
		this.hsbChangeListener = null;
		this.viewportChangeListener = null;
		this.spPropertyChangeListener = null;
		this.mouseScrollListener = null;
		this.handler = null;

		// From SynthScrollPaneUI.
		c.removePropertyChangeListener(this);
		if (this.viewportViewFocusHandler != null) {
			viewport.removeContainerListener(this.viewportViewFocusHandler);
			if (viewport.getView() != null) {
				viewport.getView().removeFocusListener(this.viewportViewFocusHandler);
			}
			this.viewportViewFocusHandler = null;
		}
	}

	@Override
	public SynthContext getContext(JComponent c) {
		return this.getContext(c, this.getComponentState(c));
	}

	protected SynthContext getContext(JComponent c, int state) {
		if(this.style == null){
			this.style = OntimizeLookAndFeel.getOntimizeStyle(c, OntimizeLookAndFeel.getRegion(c));
		}
		return new SynthContext( c, SynthLookAndFeel.getRegion(c), this.style, state);
	}

	protected int getComponentState(JComponent c) {
		if ((c instanceof JScrollPane) && (((JScrollPane) c).getViewport().getView() instanceof JTextComponent)) {
			JTextComponent tComp = (JTextComponent) ((JScrollPane) c).getViewport().getView();
			int baseState = OntimizeLookAndFeel.getComponentState(tComp);
			return baseState;
		} else {
			int baseState = OntimizeLookAndFeel.getComponentState(c);
			if ((this.viewportViewFocusHandler != null) && this.viewportViewHasFocus) {
				baseState = baseState | SynthConstants.FOCUSED;
			}
			return baseState;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (OntimizeLookAndFeel.shouldUpdateStyle(e)) {
			this.updateStyle(this.scrollpane);
		}
	}

	@Override
	public void update(Graphics g, JComponent c) {
		SynthContext context = this.getContext(c);

		OntimizeLookAndFeel.update(context, g);
		if(this.containsTableComponent(context.getComponent())){
			c.setBorder((Border) UIManager.getDefaults().get("Table.scrollPaneBorder"));
		}

		ContextUtils.getPainter(context).paintScrollPaneBackground(context, g, 0, 0, context.getComponent().getWidth(), context.getComponent().getHeight());
		this.paintScrollPaneCorner(g, c);
		this.paint(context, g);

	}

	/**
	 * @param g
	 * @param c
	 */
	protected void paintScrollPaneCorner(Graphics g, JComponent c) {
		if (this.scrollpane == null) {
			return;
		}
		if(this.cornerPainter==null) {
			return;
		}
		if ((this.scrollpane.getHorizontalScrollBar() == null) || !this.scrollpane.getHorizontalScrollBar().isVisible()) {
			return;
		}
		if ((this.scrollpane.getVerticalScrollBar() == null) || !this.scrollpane.getVerticalScrollBar().isVisible()) {
			return;
		}

		int vBarWidth = this.scrollpane.getVerticalScrollBar().getWidth();
		int hBarHeight = this.scrollpane.getHorizontalScrollBar().getHeight();

		Insets insets = c.getInsets();

		Graphics2D g2 = (Graphics2D) g.create();
		if (this.scrollpane.getComponentOrientation().isLeftToRight()) {
			g2.translate(c.getWidth() - insets.right - vBarWidth, c.getHeight() - insets.bottom - hBarHeight);
			g2.setClip(0, 0, vBarWidth, hBarHeight);
		} else {
			g2.translate(15 + insets.right, c.getHeight() - insets.bottom - hBarHeight);
			g2.scale(-1, 1);
			g2.setClip(0, 0, vBarWidth, hBarHeight);
		}
		this.cornerPainter.paint(g2, c, 24, 24);
	}


	@Override
	public void paint(Graphics g, JComponent c) {
		SynthContext context = this.getContext(c);

		this.paint(context, g);

	}

	protected void paint(SynthContext context, Graphics g) {
	}

	@Override
	public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
		JComponent c = context.getComponent();
		ContextUtils.getPainter(context).paintScrollPaneBorder(context, g, x, y, w, h);
	}

	protected boolean containsTableComponent(JComponent c) {
		if (c instanceof JScrollPane) {
			Component cView = ((JScrollPane) c).getViewport().getView();
			if (cView instanceof EJTable) {
				return true;
			}
		}

		return false;
	}

	protected Handler getHandler() {
		if (this.handler == null) {
			this.handler = new Handler();
		}
		return this.handler;
	}

	@Override
	protected void syncScrollPaneWithViewport() {
		JViewport viewport = this.scrollpane.getViewport();
		JScrollBar vsb = this.scrollpane.getVerticalScrollBar();
		JScrollBar hsb = this.scrollpane.getHorizontalScrollBar();
		JViewport rowHead = this.scrollpane.getRowHeader();
		JViewport colHead = this.scrollpane.getColumnHeader();
		boolean ltr = this.scrollpane.getComponentOrientation().isLeftToRight();

		if (viewport != null) {
			Dimension extentSize = viewport.getExtentSize();
			Dimension viewSize = viewport.getViewSize();
			Point viewPosition = viewport.getViewPosition();

			if (vsb != null) {
				int extent = extentSize.height;
				int max = viewSize.height;
				int value = Math.max(0, Math.min(viewPosition.y, max - extent));
				vsb.setValues(value, extent, 0, max);
			}

			if (hsb != null) {
				int extent = extentSize.width;
				int max = viewSize.width;
				int value;

				if (ltr) {
					value = Math.max(0, Math.min(viewPosition.x, max - extent));
				} else {
					int currentValue = hsb.getValue();

					/*
					 * Use a particular formula to calculate "value" until
					 * effective x coordinate is calculated.
					 */
					if (this.setValueCalled && ((max - currentValue) == viewPosition.x)) {
						value = Math.max(0, Math.min(max - extent, currentValue));
						/*
						 * After "extent" is set, turn setValueCalled flag off.
						 */
						if (extent != 0) {
							this.setValueCalled = false;
						}
					} else {
						if (extent > max) {
							viewPosition.x = max - extent;
							viewport.setViewPosition(viewPosition);
							value = 0;
						} else {
							/*
							 * The following line can't handle a small value of
							 * viewPosition.x like Integer.MIN_VALUE correctly
							 * because (max - extent - viewPositoiin.x) causes
							 * an overflow. As a result, value becomes zero.
							 * (e.g. setViewPosition(Integer.MAX_VALUE, ...) in
							 * a user program causes a overflow. Its expected
							 * value is (max - extent).) However, this seems a
							 * trivial bug and adding a fix makes this
							 * often-called method slow, so I'll leave it until
							 * someone claims.
							 */
							value = Math.max(0, Math.min(max - extent, max - extent - viewPosition.x));
						}
					}
				}
				hsb.setValues(value, extent, 0, max);
			}

			if (rowHead != null) {
				Point p = rowHead.getViewPosition();
				p.y = viewport.getViewPosition().y;
				p.x = 0;
				rowHead.setViewPosition(p);
			}

			if (colHead != null) {
				Point p = colHead.getViewPosition();
				if (ltr) {
					p.x = viewport.getViewPosition().x;
				} else {
					p.x = Math.max(0, viewport.getViewPosition().x);
				}
				p.y = 0;
				colHead.setViewPosition(p);
			}
			this.scrollpane.repaint();
		}
	}

	/**
	 * Listener for viewport events.
	 */
	public class ViewportChangeHandler implements ChangeListener {

		// NOTE: This class exists only for backward compatability. All
		// its functionality has been moved into Handler. If you need to add
		// new functionality add it to the Handler, but make sure this
		// class calls into the Handler.

		@Override
		public void stateChanged(ChangeEvent e) {
			OScrollPaneUI.this.getHandler().stateChanged(e);
		}
	}

	@Override
	protected ChangeListener createViewportChangeListener() {
		return this.getHandler();
	}

	/**
	 * Horizontal scrollbar listener.
	 */
	public class HSBChangeListener implements ChangeListener {

		// NOTE: This class exists only for backward compatability. All
		// its functionality has been moved into Handler. If you need to add
		// new functionality add it to the Handler, but make sure this
		// class calls into the Handler.

		@Override
		public void stateChanged(ChangeEvent e) {
			OScrollPaneUI.this.getHandler().stateChanged(e);
		}
	}

	/**
	 * Returns a <code>PropertyChangeListener</code> that will be installed on
	 * the horizontal <code>JScrollBar</code>.
	 */
	protected PropertyChangeListener createHSBPropertyChangeListener() {
		return this.getHandler();
	}

	@Override
	protected ChangeListener createHSBChangeListener() {
		return this.getHandler();
	}

	/**
	 * Vertical scrollbar listener.
	 */
	public class VSBChangeListener implements ChangeListener {

		// NOTE: This class exists only for backward compatability. All
		// its functionality has been moved into Handler. If you need to add
		// new functionality add it to the Handler, but make sure this
		// class calls into the Handler.

		@Override
		public void stateChanged(ChangeEvent e) {
			OScrollPaneUI.this.getHandler().stateChanged(e);
		}
	}

	/**
	 * Returns a <code>PropertyChangeListener</code> that will be installed on
	 * the vertical <code>JScrollBar</code>.
	 */
	protected PropertyChangeListener createVSBPropertyChangeListener() {
		return this.getHandler();
	}

	@Override
	protected ChangeListener createVSBChangeListener() {
		return this.getHandler();
	}

	/**
	 * MouseWheelHandler is an inner class which implements the
	 * MouseWheelListener interface. MouseWheelHandler responds to
	 * MouseWheelEvents by scrolling the JScrollPane appropriately. If the
	 * scroll pane's <code>isWheelScrollingEnabled</code> method returns false,
	 * no scrolling occurs.
	 * 
	 * @see javax.swing.JScrollPane#isWheelScrollingEnabled
	 * @see #createMouseWheelListener
	 * @see java.awt.event.MouseWheelListener
	 * @see java.awt.event.MouseWheelEvent
	 * @since 1.4
	 */
	protected class MouseWheelHandler implements MouseWheelListener {

		// NOTE: This class exists only for backward compatability. All
		// its functionality has been moved into Handler. If you need to add
		// new functionality add it to the Handler, but make sure this
		// class calls into the Handler.

		/**
		 * Called when the mouse wheel is rotated while over a JScrollPane.
		 * 
		 * @param e
		 *            MouseWheelEvent to be handled
		 * @since 1.4
		 */
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			OScrollPaneUI.this.getHandler().mouseWheelMoved(e);
		}
	}

	/**
	 * Creates an instance of MouseWheelListener, which is added to the
	 * JScrollPane by installUI(). The returned MouseWheelListener is used to
	 * handle mouse wheel-driven scrolling.
	 * 
	 * @return MouseWheelListener which implements wheel-driven scrolling
	 * @see #installUI
	 * @see MouseWheelHandler
	 * @since 1.4
	 */
	@Override
	protected MouseWheelListener createMouseWheelListener() {
		return this.getHandler();
	}

	protected void updateHorizontalScrollBar(PropertyChangeEvent pce) {
		this.updateScrollBar(pce, this.hsbChangeListener, this.hsbPropertyChangeListener);
	}

	protected void updateVerticalScrollBar(PropertyChangeEvent pce) {
		this.updateScrollBar(pce, this.vsbChangeListener, this.vsbPropertyChangeListener);
	}

	protected void updateScrollBar(PropertyChangeEvent pce, ChangeListener cl, PropertyChangeListener pcl) {
		JScrollBar sb = (JScrollBar) pce.getOldValue();
		if (sb != null) {
			if (cl != null) {
				sb.getModel().removeChangeListener(cl);
			}
			if (pcl != null) {
				sb.removePropertyChangeListener(pcl);
			}
		}
		sb = (JScrollBar) pce.getNewValue();
		if (sb != null) {
			if (cl != null) {
				sb.getModel().addChangeListener(cl);
			}
			if (pcl != null) {
				sb.addPropertyChangeListener(pcl);
			}
		}
	}

	public class PropertyChangeHandler implements PropertyChangeListener {

		// NOTE: This class exists only for backward compatability. All
		// its functionality has been moved into Handler. If you need to add
		// new functionality add it to the Handler, but make sure this
		// class calls into the Handler.

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			OScrollPaneUI.this.getHandler().propertyChange(e);
		}
	}

	/**
	 * Creates an instance of PropertyChangeListener that's added to the
	 * JScrollPane by installUI(). Subclasses can override this method to return
	 * a custom PropertyChangeListener, e.g.
	 * 
	 * <pre>
	 * class MyScrollPaneUI extends BasicScrollPaneUI {
	 *    protected PropertyChangeListener &lt;b&gt;createPropertyChangeListener&lt;/b&gt;() {
	 *        return new MyPropertyChangeListener();
	 *    }
	 *    public class MyPropertyChangeListener extends PropertyChangeListener {
	 *        public void propertyChange(PropertyChangeEvent e) {
	 *            if (e.getPropertyName().equals(&quot;viewport&quot;)) {
	 *                // do some extra work when the viewport changes
	 *            }
	 *            super.propertyChange(e);
	 *        }
	 *    }
	 * }
	 * </pre>
	 * 
	 * @see java.beans.PropertyChangeListener
	 * @see #installUI
	 */
	@Override
	protected PropertyChangeListener createPropertyChangeListener() {
		return this.getHandler();
	}

	class Handler implements ChangeListener, PropertyChangeListener, MouseWheelListener {
		//
		// MouseWheelListener
		//
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (OScrollPaneUI.this.scrollpane.isWheelScrollingEnabled() && (e.getWheelRotation() != 0)) {
				boolean isHorizontal = (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0;
				JScrollBar toScroll = OScrollPaneUI.this.scrollpane.getVerticalScrollBar();
				int direction = e.getWheelRotation() < 0 ? -1 : 1;
				int orientation = SwingConstants.VERTICAL;

				// find which scrollbar to scroll, or return if none
				if ((toScroll == null) || !toScroll.isVisible() || isHorizontal) {
					toScroll = OScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
					if ((toScroll == null) || !toScroll.isVisible()) {
						return;
					}
					orientation = SwingConstants.HORIZONTAL;
				}

				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					JViewport vp = OScrollPaneUI.this.scrollpane.getViewport();
					if (vp == null) {
						return;
					}
					Component comp = vp.getView();
					int units = Math.abs(e.getUnitsToScroll());

					// When the scrolling speed is set to maximum, it's possible
					// for a single wheel click to scroll by more units than
					// will fit in the visible area. This makes it
					// hard/impossible to get to certain parts of the scrolling
					// Component with the wheel. To make for more accurate
					// low-speed scrolling, we limit scrolling to the block
					// increment if the wheel was only rotated one click.
					boolean limitScroll = Math.abs(e.getWheelRotation()) == 1;

					// Check if we should use the visibleRect trick
					Object fastWheelScroll = toScroll.getClientProperty("JScrollBar.fastWheelScrolling");
					if ((Boolean.TRUE == fastWheelScroll) && (comp instanceof Scrollable)) {
						// 5078454: Under maximum acceleration, we may scroll
						// by many 100s of units in ~1 second.
						//
						// BasicScrollBarUI.scrollByUnits() can bog down the EDT
						// with repaints in this situation. However, the
						// Scrollable interface allows us to pass in an
						// arbitrary visibleRect. This allows us to accurately
						// calculate the total scroll amount, and then update
						// the GUI once. This technique provides much faster
						// accelerated wheel scrolling.
						Scrollable scrollComp = (Scrollable) comp;
						Rectangle viewRect = vp.getViewRect();
						int startingX = viewRect.x;
						boolean leftToRight = comp.getComponentOrientation().isLeftToRight();
						int scrollMin = toScroll.getMinimum();
						int scrollMax = toScroll.getMaximum() - toScroll.getModel().getExtent();

						if (limitScroll) {
							int blockIncr = scrollComp.getScrollableBlockIncrement(viewRect, orientation, direction);
							if (direction < 0) {
								scrollMin = Math.max(scrollMin, toScroll.getValue() - blockIncr);
							} else {
								scrollMax = Math.min(scrollMax, toScroll.getValue() + blockIncr);
							}
						}

						for (int i = 0; i < units; i++) {
							int unitIncr = scrollComp.getScrollableUnitIncrement(viewRect, orientation, direction);
							// Modify the visible rect for the next unit, and
							// check to see if we're at the end already.
							if (orientation == SwingConstants.VERTICAL) {
								if (direction < 0) {
									viewRect.y -= unitIncr;
									if (viewRect.y <= scrollMin) {
										viewRect.y = scrollMin;
										break;
									}
								} else { // (direction > 0
									viewRect.y += unitIncr;
									if (viewRect.y >= scrollMax) {
										viewRect.y = scrollMax;
										break;
									}
								}
							} else {
								// Scroll left
								if ((leftToRight && (direction < 0)) || (!leftToRight && (direction > 0))) {
									viewRect.x -= unitIncr;
									if (leftToRight) {
										if (viewRect.x < scrollMin) {
											viewRect.x = scrollMin;
											break;
										}
									}
								}
								// Scroll right
								else if ((leftToRight && (direction > 0)) || (!leftToRight && (direction < 0))) {
									viewRect.x += unitIncr;
									if (leftToRight) {
										if (viewRect.x > scrollMax) {
											viewRect.x = scrollMax;
											break;
										}
									}
								} else {
									assert false : "Non-sensical ComponentOrientation / scroll direction";
								}
							}
						}
						// Set the final view position on the ScrollBar
						if (orientation == SwingConstants.VERTICAL) {
							toScroll.setValue(viewRect.y);
						} else {
							if (leftToRight) {
								toScroll.setValue(viewRect.x);
							} else {
								// rightToLeft scrollbars are oriented with
								// minValue on the right and maxValue on the
								// left.
								int newPos = toScroll.getValue() - (viewRect.x - startingX);
								if (newPos < scrollMin) {
									newPos = scrollMin;
								} else if (newPos > scrollMax) {
									newPos = scrollMax;
								}
								toScroll.setValue(newPos);
							}
						}
					} else {
						// Viewport's view is not a Scrollable, or fast wheel
						// scrolling is not enabled.
						this.scrollByUnits(toScroll, direction, units, limitScroll);
					}
				} else if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
					this.scrollByBlock(toScroll, direction);
				}
				e.consume();
			}
		}

		/*
		 * Method for scrolling by a block increment. Added for mouse wheel
		 * scrolling support, RFE 4202656.
		 */
		void scrollByBlock(JScrollBar scrollbar, int direction) {
			// This method is called from BasicScrollPaneUI to implement wheel
			// scrolling, and also from scrollByBlock().
			int oldValue = scrollbar.getValue();
			int blockIncrement = scrollbar.getBlockIncrement(direction);
			int delta = blockIncrement * ((direction > 0) ? +1 : -1);
			int newValue = oldValue + delta;

			// Check for overflow.
			if ((delta > 0) && (newValue < oldValue)) {
				newValue = scrollbar.getMaximum();
			} else if ((delta < 0) && (newValue > oldValue)) {
				newValue = scrollbar.getMinimum();
			}

			scrollbar.setValue(newValue);
		}

		/*
		 * Method for scrolling by a unit increment. Added for mouse wheel
		 * scrolling support, RFE 4202656.
		 * 
		 * If limitByBlock is set to true, the scrollbar will scroll at least 1
		 * unit increment, but will not scroll farther than the block increment.
		 * See BasicScrollPaneUI.Handler.mouseWheelMoved().
		 */
		void scrollByUnits(JScrollBar scrollbar, int direction, int units, boolean limitToBlock) {
			// This method is called from BasicScrollPaneUI to implement wheel
			// scrolling, as well as from scrollByUnit().
			int delta;
			int limit = -1;

			if (limitToBlock) {
				if (direction < 0) {
					limit = scrollbar.getValue() - scrollbar.getBlockIncrement(direction);
				} else {
					limit = scrollbar.getValue() + scrollbar.getBlockIncrement(direction);
				}
			}

			for (int i = 0; i < units; i++) {
				if (direction > 0) {
					delta = scrollbar.getUnitIncrement(direction);
				} else {
					delta = -scrollbar.getUnitIncrement(direction);
				}

				int oldValue = scrollbar.getValue();
				int newValue = oldValue + delta;

				// Check for overflow.
				if ((delta > 0) && (newValue < oldValue)) {
					newValue = scrollbar.getMaximum();
				} else if ((delta < 0) && (newValue > oldValue)) {
					newValue = scrollbar.getMinimum();
				}
				if (oldValue == newValue) {
					break;
				}

				if (limitToBlock && (i > 0)) {
					assert limit != -1;
					if (((direction < 0) && (newValue < limit)) || ((direction > 0) && (newValue > limit))) {
						break;
					}
				}
				scrollbar.setValue(newValue);
			}
		}

		//
		// ChangeListener: This is added to the vieport, and hsb/vsb models.
		//
		@Override
		public void stateChanged(ChangeEvent e) {
			JViewport viewport = OScrollPaneUI.this.scrollpane.getViewport();

			if (viewport != null) {
				if (e.getSource() == viewport) {
					this.viewportStateChanged(e);
				} else {
					JScrollBar hsb = OScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
					if ((hsb != null) && (e.getSource() == hsb.getModel())) {
						this.hsbStateChanged(viewport, e);
					} else {
						JScrollBar vsb = OScrollPaneUI.this.scrollpane.getVerticalScrollBar();
						if ((vsb != null) && (e.getSource() == vsb.getModel())) {
							this.vsbStateChanged(viewport, e);
						}
					}
				}
			}
		}

		protected void vsbStateChanged(JViewport viewport, ChangeEvent e) {
			BoundedRangeModel model = (BoundedRangeModel) (e.getSource());
			Point p = viewport.getViewPosition();
			p.y = model.getValue();
			viewport.setViewPosition(p);
		}

		protected void hsbStateChanged(JViewport viewport, ChangeEvent e) {
			BoundedRangeModel model = (BoundedRangeModel) (e.getSource());
			Point p = viewport.getViewPosition();
			int value = model.getValue();
			if (OScrollPaneUI.this.scrollpane.getComponentOrientation().isLeftToRight()) {
				p.x = value;
			} else {
				int max = viewport.getViewSize().width;
				int extent = viewport.getExtentSize().width;
				int oldX = p.x;

				/*
				 * Set new X coordinate based on "value".
				 */
				p.x = max - extent - value;

				/*
				 * If setValue() was called before "extent" was fixed, turn
				 * setValueCalled flag on.
				 */
				if ((extent == 0) && (value != 0) && (oldX == max)) {
					OScrollPaneUI.this.setValueCalled = true;
				} else {
					/*
					 * When a pane without a horizontal scroll bar was reduced
					 * and the bar appeared, the viewport should show the right
					 * side of the view.
					 */
					if ((extent != 0) && (oldX < 0) && (p.x == 0)) {
						p.x += value;
					}
				}
			}
			viewport.setViewPosition(p);
		}

		protected void viewportStateChanged(ChangeEvent e) {
			OScrollPaneUI.this.syncScrollPaneWithViewport();
		}

		//
		// PropertyChangeListener: This is installed on both the JScrollPane
		// and the horizontal/vertical scrollbars.
		//

		// Listens for changes in the model property and reinstalls the
		// horizontal/vertical PropertyChangeListeners.
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			if (e.getSource() == OScrollPaneUI.this.scrollpane) {
				this.scrollPanePropertyChange(e);
			} else {
				this.sbPropertyChange(e);
			}
		}

		protected void scrollPanePropertyChange(PropertyChangeEvent e) {
			String propertyName = e.getPropertyName();

			if (propertyName == "verticalScrollBarDisplayPolicy") {
				OScrollPaneUI.this.updateScrollBarDisplayPolicy(e);
			} else if (propertyName == "horizontalScrollBarDisplayPolicy") {
				OScrollPaneUI.this.updateScrollBarDisplayPolicy(e);
			} else if (propertyName == "viewport") {
				OScrollPaneUI.this.updateViewport(e);
			} else if (propertyName == "rowHeader") {
				OScrollPaneUI.this.updateRowHeader(e);
			} else if (propertyName == "columnHeader") {
				OScrollPaneUI.this.updateColumnHeader(e);
			} else if (propertyName == "verticalScrollBar") {
				OScrollPaneUI.this.updateVerticalScrollBar(e);
			} else if (propertyName == "horizontalScrollBar") {
				OScrollPaneUI.this.updateHorizontalScrollBar(e);
			} else if (propertyName == "componentOrientation") {
				OScrollPaneUI.this.scrollpane.revalidate();
				OScrollPaneUI.this.scrollpane.repaint();
			}
		}

		// PropertyChangeListener for the horizontal and vertical scrollbars.
		protected void sbPropertyChange(PropertyChangeEvent e) {
			String propertyName = e.getPropertyName();
			Object source = e.getSource();

			if ("model" == propertyName) {
				JScrollBar sb = OScrollPaneUI.this.scrollpane.getVerticalScrollBar();
				BoundedRangeModel oldModel = (BoundedRangeModel) e.getOldValue();
				ChangeListener cl = null;

				if (source == sb) {
					cl = OScrollPaneUI.this.vsbChangeListener;
				} else if (source == OScrollPaneUI.this.scrollpane.getHorizontalScrollBar()) {
					sb = OScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
					cl = OScrollPaneUI.this.hsbChangeListener;
				}
				if (cl != null) {
					if (oldModel != null) {
						oldModel.removeChangeListener(cl);
					}
					if (sb.getModel() != null) {
						sb.getModel().addChangeListener(cl);
					}
				}
			} else if ("componentOrientation" == propertyName) {
				if (source == OScrollPaneUI.this.scrollpane.getHorizontalScrollBar()) {
					JScrollBar hsb = OScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
					JViewport viewport = OScrollPaneUI.this.scrollpane.getViewport();
					Point p = viewport.getViewPosition();
					if (OScrollPaneUI.this.scrollpane.getComponentOrientation().isLeftToRight()) {
						p.x = hsb.getValue();
					} else {
						p.x = viewport.getViewSize().width - viewport.getExtentSize().width - hsb.getValue();
					}
					viewport.setViewPosition(p);
				}
			}
		}
	}

	protected class ViewportBorder extends AbstractBorder implements UIResource {
		protected Insets insets;

		ViewportBorder(SynthContext context) {
			this.insets = (Insets) context.getStyle().get(context, "ScrollPane.viewportBorderInsets");
			if (this.insets == null) {
				this.insets = OntimizeLookAndFeel.EMPTY_UIRESOURCE_INSETS;
			}
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			JComponent jc = (JComponent) c;
			SynthContext context = OScrollPaneUI.this.getContext(jc);
			SynthStyle style = context.getStyle();
			if (style == null) {
				assert false : "SynthBorder is being used outside after the " + " UI has been uninstalled";
			return;
			}
			ContextUtils.getPainter(context).paintViewportBorder(context, g, x, y, width, height);

		}

		@Override
		public Insets getBorderInsets(Component c) {
			return this.getBorderInsets(c, null);
		}

		@Override
		public Insets getBorderInsets(Component c, Insets insets) {
			if (insets == null) {
				return new Insets(this.insets.top, this.insets.left, this.insets.bottom, this.insets.right);
			}
			insets.top = this.insets.top;
			insets.bottom = this.insets.bottom;
			insets.left = this.insets.left;
			insets.right = this.insets.left;
			return insets;
		}

		@Override
		public boolean isBorderOpaque() {
			return false;
		}
	}

	/**
	 * Handle keeping track of the viewport's view's focus
	 */
	protected class ViewportViewFocusHandler implements ContainerListener, FocusListener {
		@Override
		public void componentAdded(ContainerEvent e) {
			if (e.getChild() instanceof JTextComponent) {
				e.getChild().addFocusListener(this);
				OScrollPaneUI.this.viewportViewHasFocus = e.getChild().isFocusOwner();
				OScrollPaneUI.this.scrollpane.repaint();
			}
		}

		@Override
		public void componentRemoved(ContainerEvent e) {
			if (e.getChild() instanceof JTextComponent) {
				e.getChild().removeFocusListener(this);
			}
		}

		@Override
		public void focusGained(FocusEvent e) {
			OScrollPaneUI.this.viewportViewHasFocus = true;
			OScrollPaneUI.this.scrollpane.repaint();
		}

		@Override
		public void focusLost(FocusEvent e) {
			OScrollPaneUI.this.viewportViewHasFocus = false;
			OScrollPaneUI.this.scrollpane.repaint();
		}
	}
}
