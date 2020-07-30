package com.ontimize.plaf.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;
import javax.swing.text.View;

import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.OntimizeRegion;
import com.ontimize.plaf.OntimizeSynthPainterImpl;
import com.ontimize.plaf.component.OTabbedPaneTabPopup;
import com.ontimize.plaf.component.OntimizeArrowButton;
import com.ontimize.plaf.component.TabbedPanePopup;
import com.ontimize.plaf.utils.ContextUtils;
import com.ontimize.plaf.utils.ControlOrientation;
import com.ontimize.plaf.utils.OTabCloseListener;

import sun.swing.SwingUtilities2;
import sun.swing.plaf.synth.DefaultSynthStyle;

/**
 * TabbedPane UI delegate.
 *
 * <p>
 * Based on SynthTabbedPaneUI.
 * </p>
 */
public class OTabbedPaneUI extends BasicTabbedPaneUI implements SynthUI, PropertyChangeListener, TabbedPanePopup {

    protected SynthStyle style;

    protected SynthStyle tabStyle;

    protected SynthStyle tabCloseStyle;

    protected SynthStyle tabAreaStyle;

    protected SynthStyle tabContentStyle;

    protected Rectangle textRect;

    protected Rectangle iconRect;

    protected Rectangle tabAreaRect;

    protected Rectangle contentRect;

    protected boolean selectedTabIsPressed = false;

    /**
     * Actions to be performed when tab close button is pressed and when tab is actually closed.
     */
    protected OTabCloseListener tabCloseListener;

    /** The scroll forward button. This may or may not be visible. */
    protected SynthScrollableTabButton scrollForwardButton;

    /** The popup open button. This may or may not be visible */
    protected SynthScrollableTabButton arrowButton;

    /** The scroll backward button. This may or may not be visible. */
    protected SynthScrollableTabButton scrollBackwardButton;

    /** Margin for the close button. */
    protected Insets closeButtonInsets;

    /** The size of the close button. */
    protected int closeButtonSize;

    /** Index of initial displayed tab. */
    protected int leadingTabIndex = 0;

    /** Index of final displayed tab. */
    protected int trailingTabIndex = 0;

    /** Side the tabs are on. */
    protected int tabPlacement;

    /** Horizontal/vertical abstraction. */
    protected ControlOrientation orientation;

    /**
     * Placement of the tab close button: LEFT, RIGHT, or CENTER (the default) for none.
     */
    protected int tabCloseButtonPlacement;

    /** Index of the armed close button, or -1 if none is armed. */
    protected int closeButtonArmedIndex;

    /**
     * Index of the close button the mouse is hovering over, or -1 if none is being hovered over.
     */
    protected int closeButtonHoverIndex;

    // This list is for drawing the current item in the combo box.
    protected JList listBox;

    // The implementation of ComboPopup that is used to show the popup.
    protected OTabbedPaneTabPopup popup;

    // Listeners that the ComboPopup produces.
    protected MouseListener popupMouseListener;

    protected MouseMotionListener popupMouseMotionListener;

    protected KeyListener popupKeyListener;

    /**
     * Creates a new OntimizeTabbedPaneUI object.
     */
    protected OTabbedPaneUI() {
        this.textRect = new Rectangle();
        this.iconRect = new Rectangle();
        this.tabAreaRect = new Rectangle();
        this.contentRect = new Rectangle();
    }

    /**
     * Create the UI delegate.
     * @param c the component (not used).
     * @return the UI delegate.
     */
    public static ComponentUI createUI(JComponent c) {
        return new OTabbedPaneUI();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
        this.popup = this.createPopup(c);
        this.listBox = this.popup.getList();
        super.installUI(c);
    }

    protected OTabbedPaneTabPopup createPopup(JComponent c) {
        OTabbedPaneTabPopup p = new OTabbedPaneTabPopup((JTabbedPane) c);
        return p;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installComponents()
     */
    @Override
    protected void installComponents() {
        super.installComponents();

        // Adding scroll buttons to tabPane...
        this.scrollBackwardButton = (SynthScrollableTabButton) this.createScrollButton(SwingConstants.WEST);
        this.scrollForwardButton = (SynthScrollableTabButton) this.createScrollButton(SwingConstants.EAST);
        this.arrowButton = (SynthScrollableTabButton) this.createScrollButton(SwingConstants.SOUTH);
        this.tabPane.add(this.scrollBackwardButton);
        this.tabPane.add(this.scrollForwardButton);
        this.tabPane.add(this.arrowButton);
        this.scrollBackwardButton.setVisible(false);
        this.scrollForwardButton.setVisible(false);
        this.arrowButton.setVisible(false);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#uninstallComponents()
     */
    @Override
    protected void uninstallComponents() {
        super.uninstallComponents();

        if (this.scrollBackwardButton != null) {
            this.tabPane.remove(this.scrollBackwardButton);
            this.scrollBackwardButton = null;
        }

        if (this.scrollForwardButton != null) {
            this.tabPane.remove(this.scrollForwardButton);
            this.scrollForwardButton = null;
        }

        if (this.arrowButton != null) {
            this.tabPane.remove(this.arrowButton);
            this.arrowButton.removeMouseListener(this.popup.getMouseListener());
            this.arrowButton.removeMouseMotionListener(this.popup.getMouseMotionListener());
            this.arrowButton = null;
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installDefaults()
     */
    @Override
    protected void installDefaults() {
        this.leadingTabIndex = 0;
        this.trailingTabIndex = 0;
        this.updateStyle(this.tabPane);
    }

    /**
     * Update the Synth styles when something changes.
     * @param c the component.
     */
    protected void updateStyle(JTabbedPane c) {
        SynthContext context = this.getContext(c, SynthConstants.ENABLED);
        SynthStyle oldStyle = this.style;

        this.style = OntimizeLookAndFeel.updateStyle(context, this);

        this.tabPlacement = this.tabPane.getTabPlacement();
        this.orientation = ControlOrientation
            .getOrientation((this.tabPlacement == SwingConstants.LEFT) || (this.tabPlacement == SwingConstants.RIGHT)
                    ? SwingConstants.VERTICAL
                    : SwingConstants.HORIZONTAL);

        this.closeButtonArmedIndex = -1;
        Object o = c.getClientProperty("JTabbedPane.closeButton");

        if ((o != null) && "left".equals(o)) {
            this.tabCloseButtonPlacement = SwingConstants.LEFT;
        } else if ((o != null) && "right".equals(o)) {
            this.tabCloseButtonPlacement = SwingConstants.RIGHT;
        } else {
            this.tabCloseButtonPlacement = SwingConstants.CENTER;
        }

        this.closeButtonSize = this.style.getInt(context, "closeButtonSize", 6);
        this.closeButtonInsets = (Insets) this.style.get(context, "closeButtonInsets");
        if (this.closeButtonInsets == null) {
            this.closeButtonInsets = new Insets(2, 2, 2, 2);
        }

        o = c.getClientProperty("JTabbedPane.closeListener");
        if ((o != null) && (o instanceof OTabCloseListener)) {
            if (this.tabCloseListener == null) {
                this.tabCloseListener = (OTabCloseListener) o;
            }
        }

        Object opaque = UIManager.get("TabbedPane.opaque");
        if (opaque == null) {
            opaque = Boolean.FALSE;
        }
        LookAndFeel.installProperty(this.tabPane, "opaque", opaque);

        // Add properties other than JComponent colors, Borders and
        // opacity settings here:
        if (this.style != oldStyle) {

            this.tabRunOverlay = 0;
            this.textIconGap = this.style.getInt(context, "TabbedPane.textIconGap", 0);
            this.selectedTabPadInsets = (Insets) this.style.get(context, "TabbedPane.selectedTabPadInsets");

            if (this.selectedTabPadInsets == null) {
                this.selectedTabPadInsets = new Insets(0, 0, 0, 0);
            }

            if (oldStyle != null) {
                this.uninstallKeyboardActions();
                this.installKeyboardActions();
            }
        }

        SynthContext tabContext = this.getContext(c, Region.TABBED_PANE_TAB, SynthConstants.ENABLED);
        this.tabStyle = OntimizeLookAndFeel.updateStyle(tabContext, this);
        this.tabInsets = this.tabStyle.getInsets(tabContext, null);

        SynthContext tabCloseContext = this.getContext(c, OntimizeRegion.TABBED_PANE_TAB_CLOSE_BUTTON,
                SynthConstants.ENABLED);
        this.tabCloseStyle = OntimizeLookAndFeel.updateStyle(tabCloseContext, this);

        SynthContext tabAreaContext = this.getContext(c, Region.TABBED_PANE_TAB_AREA, SynthConstants.ENABLED);
        this.tabAreaStyle = OntimizeLookAndFeel.updateStyle(tabAreaContext, this);
        this.tabAreaInsets = this.tabAreaStyle.getInsets(tabAreaContext, null);

        SynthContext tabContentContext = this.getContext(c, Region.TABBED_PANE_CONTENT, SynthConstants.ENABLED);
        this.tabContentStyle = OntimizeLookAndFeel.updateStyle(tabContentContext, this);
        this.contentBorderInsets = this.tabContentStyle.getInsets(tabContentContext, null);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installListeners()
     */
    @Override
    protected void installListeners() {
        super.installListeners();
        this.tabPane.addMouseMotionListener((MouseAdapter) this.mouseListener);
        this.tabPane.addPropertyChangeListener(this);

        this.scrollBackwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OTabbedPaneUI.this.scrollBackward();
            }
        });

        this.scrollForwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OTabbedPaneUI.this.scrollForward();
            }
        });

        if (this.arrowButton != null) {
            this.arrowButton.addMouseListener(this.popup.getMouseListener());
            this.arrowButton.addMouseMotionListener(this.popup.getMouseMotionListener());
            this.arrowButton.resetKeyboardActions();
            this.arrowButton.setInheritsPopupMenu(true);
        }

    }

    /**
     * Scroll the tab buttons backwards.
     */
    protected void scrollBackward() {
        int selectedIndex = this.tabPane.getSelectedIndex();

        if (--selectedIndex < 0) {
            this.tabPane.setSelectedIndex(this.tabPane.getTabCount() == 0 ? -1 : 0);
        } else {
            this.tabPane.setSelectedIndex(selectedIndex);
        }

        this.tabPane.repaint();
    }

    /**
     * Scroll the tab buttons forwards.
     */
    protected void scrollForward() {
        int selectedIndex = this.tabPane.getSelectedIndex();

        if (++selectedIndex >= this.tabPane.getTabCount()) {
            this.tabPane.setSelectedIndex(this.tabPane.getTabCount() - 1);
        } else {
            this.tabPane.setSelectedIndex(selectedIndex);
        }

        this.tabPane.repaint();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#uninstallListeners()
     */
    @Override
    protected void uninstallListeners() {
        if (this.mouseListener != null) {
            this.tabPane.removeMouseMotionListener((MouseAdapter) this.mouseListener);
        }

        this.tabPane.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#uninstallDefaults()
     */
    @Override
    protected void uninstallDefaults() {
        SynthContext context = this.getContext(this.tabPane, SynthConstants.ENABLED);

        this.style.uninstallDefaults(context);

        this.style = null;
        this.tabStyle = null;
        this.tabAreaStyle = null;
        this.tabContentStyle = null;
    }

    // ================================
    //

    /**
     * Tells if the popup is visible or not.
     */
    @Override
    public boolean isPopupVisible() {
        return this.popup.isVisible();
    }

    /**
     * Hides the popup.
     */
    @Override
    public void setPopupVisible(boolean v) {
        if (v) {
            this.popup.show();
        } else {
            this.popup.hide();
        }
    }

    //
    // ==============================

    /**
     * @see sun.swing.plaf.synth.SynthUI#getContext(javax.swing.JComponent)
     */
    @Override
    public SynthContext getContext(JComponent c) {
        return this.getContext(c, this.getComponentState(c));
    }

    /**
     * Create a SynthContext for the component and state. Use the default region.
     * @param c the component.
     * @param state the state.
     * @return the newly created SynthContext.
     */
    public SynthContext getContext(JComponent c, int state) {
        if (this.style == null) {
            this.style = OntimizeLookAndFeel.getOntimizeStyle(c, OntimizeLookAndFeel.getRegion(c));
        }
        return new SynthContext(c, OntimizeLookAndFeel.getRegion(c), this.style, state);
    }

    /**
     * Create a SynthContext for the component and subregion. Use the current state.
     * @param c the component.
     * @param subregion the subregion.
     * @return the newly created SynthContext.
     */
    public SynthContext getContext(JComponent c, Region subregion) {
        return this.getContext(c, subregion, this.getComponentState(c));
    }

    /**
     * Create a SynthContext for the component, subregion, and state.
     * @param c the component.
     * @param subregion the subregion.
     * @param state the state.
     * @return the newly created SynthContext.
     */
    protected SynthContext getContext(JComponent c, Region subregion, int state) {
        SynthStyle style = this.style;

        if (subregion == Region.TABBED_PANE_TAB) {
            if (this.tabStyle == null) {
                this.tabStyle = new DefaultSynthStyle();
            }
            style = this.tabStyle;
        } else if (subregion == Region.TABBED_PANE_TAB_AREA) {
            if (this.tabAreaStyle == null) {
                this.tabAreaStyle = new DefaultSynthStyle();
            }
            style = this.tabAreaStyle;
        } else if (subregion == Region.TABBED_PANE_CONTENT) {
            if (this.tabContentStyle == null) {
                this.tabContentStyle = new DefaultSynthStyle();
            }
            style = this.tabContentStyle;
        } else if (subregion == OntimizeRegion.TABBED_PANE_TAB_CLOSE_BUTTON) {
            if (this.tabCloseStyle == null) {
                this.tabCloseStyle = new DefaultSynthStyle();
            }
            style = this.tabCloseStyle;
        }

        return new SynthContext(c, subregion, style, state);
    }

    /**
     * Get the current state for the component.
     * @param c the component.
     * @return the component's state.
     */
    protected int getComponentState(JComponent c) {
        return OntimizeLookAndFeel.getComponentState(c);
    }

    /**
     * Get the state for the specified tab's close button.
     * @param c the tabbed pane.
     * @param tabIndex the index of the tab.
     * @param tabIsMousedOver TODO
     * @return the close button state.
     */
    public int getCloseButtonState(JComponent c, int tabIndex, boolean tabIsMousedOver) {
        if (!c.isEnabled()) {
            return SynthConstants.DISABLED;
        } else if (tabIndex == this.closeButtonArmedIndex) {
            return SynthConstants.PRESSED;
        } else if (tabIndex == this.closeButtonHoverIndex) {
            return SynthConstants.FOCUSED;
        } else if (tabIsMousedOver) {
            return SynthConstants.MOUSE_OVER;
        }

        return SynthConstants.ENABLED;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#createScrollButton(int)
     */
    @Override
    protected JButton createScrollButton(int direction) {

        SynthScrollableTabButton b = new SynthScrollableTabButton(direction);

        b.setName("TabbedPaneTabArea.button");

        return b;
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (OntimizeLookAndFeel.shouldUpdateStyle(e)) {
            this.updateStyle(this.tabPane);
        } else if (e.getPropertyName() == "tabPlacement") {
            this.updateStyle(this.tabPane);
        }
    }

    /**
     * Create the mouse listener.
     *
     * <p>
     * Overridden to keep track of whether the selected tab is also pressed.
     * </p>
     * @return the mouse listener.
     *
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#createMouseListener()
     */
    @Override
    protected MouseListener createMouseListener() {
        return new OntimizeTabbedPaneMouseHandler(super.createMouseListener());
    }

    @Override
    protected ChangeListener createChangeListener() {
        return new OntimizeTabbedPaneChangeHandler(super.createChangeListener());
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#createLayoutManager()
     */
    @Override
    protected LayoutManager createLayoutManager() {
        return new OntimizeTabbedPaneLayout();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getTabLabelShiftX(int, int, boolean)
     */
    @Override
    protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getTabLabelShiftY(int, int, boolean)
     */
    @Override
    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
        SynthContext context = this.getContext(c);

        OntimizeLookAndFeel.update(context, g);
        ContextUtils.getPainter(context)
            .paintTabbedPaneBackground(context, g, this.tabAreaRect.x, this.tabAreaRect.y, this.tabAreaRect.width,
                    this.tabAreaRect.height);
        this.paint(context, g);

    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getBaseline(int)
     */
    @Override
    protected int getBaseline(int tab) {
        if ((this.tabPane.getTabComponentAt(tab) != null) || (this.getTextViewForTab(tab) != null)) {
            return super.getBaseline(tab);
        }
        SynthContext tabContext = this.getContext(this.tabPane, Region.TABBED_PANE_TAB, SynthConstants.ENABLED);
        String title = this.tabPane.getTitleAt(tab);
        Font font = tabContext.getStyle().getFont(tabContext);
        FontMetrics metrics = this.getFontMetrics(font);
        Icon icon = this.getIconForTab(tab);

        this.textRect.setBounds(0, 0, 0, 0);
        this.iconRect.setBounds(0, 0, 0, 0);
        this.calcRect.setBounds(0, 0, Short.MAX_VALUE, this.maxTabHeight);
        tabContext
            .getStyle()
            .getGraphicsUtils(tabContext)
            .layoutText(tabContext, metrics, title, icon, SwingConstants.CENTER, SwingConstants.CENTER,
                    SwingConstants.LEADING, SwingConstants.TRAILING, this.calcRect,
                    this.iconRect, this.textRect, this.textIconGap);

        return this.textRect.y + metrics.getAscent() + this.getBaselineOffset();
    }

    /**
     * @see sun.swing.plaf.synth.SynthUI#paintBorder(javax.swing.plaf.synth.SynthContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    @Override
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {

        SynthPainter painter = ContextUtils.getPainter(context);
        if (painter instanceof OntimizeSynthPainterImpl) {
            ((OntimizeSynthPainterImpl) painter).paintTabbedPaneBorder(context, g, x, y, w, h, this.tabPlacement);
        } else {
            painter.paintTabbedPaneBorder(context, g, x, y, w, h);
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paint(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        SynthContext context = this.getContext(c);

        this.paint(context, g);

    }

    /**
     * Paint the tabbed pane.
     * @param context the SynthContext describing the control.
     * @param g the Graphics context to paint with.
     */
    protected void paint(SynthContext context, Graphics g) {
        int selectedIndex = this.tabPane.getSelectedIndex();

        this.ensureCurrentLayout();
        SynthContext tabContentContext = this.getContext(this.tabPane, Region.TABBED_PANE_CONTENT,
                SynthConstants.ENABLED);
        // Paint content border.
        this.paintContentBorder(tabContentContext, g, this.tabPlacement, selectedIndex);
        this.paintTabArea(g, this.tabPlacement, selectedIndex);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintTabArea(java.awt.Graphics, int, int)
     */
    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        SynthContext tabAreaContext = this.getContext(this.tabPane, Region.TABBED_PANE_TAB_AREA,
                SynthConstants.ENABLED);
        this.paintTabArea(tabAreaContext, g, tabPlacement, selectedIndex, this.tabAreaRect);
    }

    /**
     * Paint the tab area, including the tabs.
     * @param ss the SynthContext.
     * @param g the Graphics context.
     * @param tabPlacement the side the tabs are on.
     * @param selectedIndex the current selected tab index.
     * @param tabAreaBounds the bounds of the tab area.
     */
    protected void paintTabArea(SynthContext ss, Graphics g, int tabPlacement, int selectedIndex,
            Rectangle tabAreaBounds) {
        Rectangle clipRect = g.getClipBounds();

        ContextUtils.setComponentState(ss, SynthConstants.ENABLED);

        // Paint the tab area.
        OntimizeLookAndFeel.updateSubregion(ss, g, tabAreaBounds);
        ContextUtils.getPainter(ss)
            .paintTabbedPaneTabAreaBackground(ss, g, tabAreaBounds.x, tabAreaBounds.y, tabAreaBounds.width,
                    tabAreaBounds.height, tabPlacement);
        ContextUtils.getPainter(ss)
            .paintTabbedPaneTabAreaBorder(ss, g, tabAreaBounds.x, tabAreaBounds.y, tabAreaBounds.width,
                    tabAreaBounds.height, tabPlacement);

        this.iconRect.setBounds(0, 0, 0, 0);
        this.textRect.setBounds(0, 0, 0, 0);

        if (this.runCount == 0) {
            return;
        }

        if (this.scrollBackwardButton.isVisible()) {
            this.paintScrollButtonBackground(ss, g, this.scrollBackwardButton);
        }

        if (this.scrollForwardButton.isVisible()) {
            this.paintScrollButtonBackground(ss, g, this.scrollForwardButton);
        }

        if (this.arrowButton.isVisible()) {
            this.paintScrollButtonBackground(ss, g, this.arrowButton);
        }

        SynthContext tabContext = this.getContext(this.tabPane, Region.TABBED_PANE_TAB, SynthConstants.ENABLED);
        for (int i = this.leadingTabIndex; i <= this.trailingTabIndex; i++) {
            if (this.rects[i].intersects(clipRect) && (selectedIndex != i)) {
                this.paintTab(tabContext, g, this.rects, i, this.iconRect, this.textRect);
            }
        }

        if (selectedIndex >= 0) {
            if (this.rects[selectedIndex].intersects(clipRect)) {
                this.paintTab(tabContext, g, this.rects, selectedIndex, this.iconRect, this.textRect);
            }
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#setRolloverTab(int)
     */
    @Override
    protected void setRolloverTab(int index) {
        int oldRolloverTab = this.getRolloverTab();

        super.setRolloverTab(index);

        Rectangle r = null;

        if ((oldRolloverTab >= 0) && (oldRolloverTab < this.tabPane.getTabCount())) {
            r = this.getTabBounds(this.tabPane, oldRolloverTab);

            if (r != null) {
                this.tabPane.repaint(r);
            }
        }

        if (index >= 0) {
            r = this.getTabBounds(this.tabPane, index);

            if (r != null) {
                this.tabPane.repaint(r);
            }
        }
    }

    /**
     * Paint a tab.
     * @param tabContext the SynthContext.
     * @param g the Graphics context.
     * @param rects the array containing the bounds for the tabs.
     * @param tabIndex the tab index to paint.
     * @param iconRect the bounds in which to paint the tab icon, if any.
     * @param textRect the bounds in which to paint the tab text, if any.
     */
    protected void paintTab(SynthContext tabContext, Graphics g, Rectangle[] rects, int tabIndex, Rectangle iconRect,
            Rectangle textRect) {
        Rectangle tabRect = rects[tabIndex];
        int selectedIndex = this.tabPane.getSelectedIndex();
        boolean isSelected = selectedIndex == tabIndex;
        JComponent b = tabContext.getComponent();

        boolean flipSegments = ((this.orientation == ControlOrientation.HORIZONTAL)
                && !this.tabPane.getComponentOrientation().isLeftToRight());
        String segmentPosition = "only";

        if (this.tabPane.getTabCount() > 1) {
            if ((tabIndex == 0) && (tabIndex == this.leadingTabIndex)) {
                segmentPosition = flipSegments ? "last" : "first";
            } else if ((tabIndex == (this.tabPane.getTabCount() - 1)) && (tabIndex == this.trailingTabIndex)) {
                segmentPosition = flipSegments ? "first" : "last";
            } else {
                segmentPosition = "middle";
            }
        }

        b.putClientProperty("JTabbedPane.Tab.segmentPosition", segmentPosition);
        tabContext = this.getTabContext(tabIndex, isSelected, isSelected && this.selectedTabIsPressed,
                this.getRolloverTab() == tabIndex, this.getFocusIndex() == tabIndex);

        OntimizeLookAndFeel.updateSubregion(tabContext, g, tabRect);

        int x = tabRect.x;
        int y = tabRect.y;
        int height = tabRect.height;
        int width = tabRect.width;

        ContextUtils.getPainter(tabContext)
            .paintTabbedPaneTabBackground(tabContext, g, x, y, width, height, tabIndex, this.tabPlacement);
        ContextUtils.getPainter(tabContext)
            .paintTabbedPaneTabBorder(tabContext, g, x, y, width, height, tabIndex, this.tabPlacement);

        if (this.tabCloseButtonPlacement != SwingConstants.CENTER) {
            tabRect = this.paintCloseButton(g, tabContext, tabIndex);
        }

        if (this.tabPane.getTabComponentAt(tabIndex) == null) {
            String title = this.tabPane.getTitleAt(tabIndex);
            Font font = tabContext.getStyle().getFont(tabContext);
            FontMetrics metrics = SwingUtilities2.getFontMetrics(this.tabPane, g, font);
            Icon icon = this.getIconForTab(tabIndex);

            this.layoutLabel(tabContext, this.tabPlacement, metrics, tabIndex, title, icon, tabRect, iconRect, textRect,
                    isSelected);
            this.paintText(tabContext, g, this.tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
            this.paintIcon(g, this.tabPlacement, tabIndex, icon, iconRect, isSelected);
        }
    }

    /**
     * Paint the close button for a tab.
     * @param g the Graphics context.
     * @param tabContext the SynthContext for the tab itself.
     * @param tabIndex the tab index to paint.
     * @return the new tab bounds.
     */
    protected Rectangle paintCloseButton(Graphics g, SynthContext tabContext, int tabIndex) {
        Rectangle tabRect = new Rectangle(this.rects[tabIndex]);

        Rectangle bounds = this.getCloseButtonBounds(tabIndex);
        int offset = bounds.width + this.textIconGap;
        boolean onLeft = this.isCloseButtonOnLeft();

        if (onLeft) {
            tabRect.x += offset;
            tabRect.width -= offset;
        } else {
            tabRect.width -= offset;
        }

        SynthContext subcontext = this.getContext(this.tabPane, OntimizeRegion.TABBED_PANE_TAB_CLOSE_BUTTON,
                this.getCloseButtonState(this.tabPane, tabIndex,
                        (tabContext.getComponentState() & SynthConstants.MOUSE_OVER) != 0));

        OntimizeLookAndFeel.updateSubregion(subcontext, g, bounds);

        OntimizeSynthPainterImpl painter = (OntimizeSynthPainterImpl) ContextUtils.getPainter(subcontext);

        painter.paintSearchButtonForeground(subcontext, g, bounds.x, bounds.y, bounds.width, bounds.height);

        return tabRect;
    }

    /**
     * Paint the background for a tab scroll button.
     * @param ss the tab subregion SynthContext.
     * @param g the Graphics context.
     * @param scrollButton the button to paint.
     */
    protected void paintScrollButtonBackground(SynthContext ss, Graphics g, JButton scrollButton) {
        Rectangle tabRect = scrollButton.getBounds();
        int x = tabRect.x;
        int y = tabRect.y;
        int height = tabRect.height;
        int width = tabRect.width;

        boolean flipSegments = ((this.orientation == ControlOrientation.HORIZONTAL)
                && !this.tabPane.getComponentOrientation().isLeftToRight());

        OntimizeLookAndFeel.updateSubregion(ss, g, tabRect);

        String segmentPosition = "first";
        if (scrollButton == this.scrollBackwardButton) {
            segmentPosition = flipSegments ? "last" : "first";
        } else if (scrollButton == this.scrollForwardButton) {
            segmentPosition = this.arrowButton.isVisible() ? "middle" : flipSegments ? "first" : "last";
        } else if (scrollButton == this.arrowButton) {
            segmentPosition = flipSegments ? "first" : "last";
        }

        this.tabPane.putClientProperty("JTabbedPane.Tab.segmentPosition", segmentPosition);

        SynthContext tabContext = this.getContext(this.tabPane, Region.TABBED_PANE_TAB, SynthConstants.ENABLED);
        int oldState = tabContext.getComponentState();
        ButtonModel model = scrollButton.getModel();
        int isPressed = model.isPressed() && model.isArmed() ? SynthConstants.PRESSED : 0;
        int buttonState = OntimizeLookAndFeel.getComponentState(scrollButton) | isPressed;
        if ((buttonState == SynthConstants.ENABLED) && model.isRollover()) {
            buttonState |= (SynthConstants.ENABLED | SynthConstants.MOUSE_OVER);
        }

        ContextUtils.setComponentState(tabContext, buttonState);
        ContextUtils.getPainter(tabContext)
            .paintTabbedPaneTabBackground(tabContext, g, x, y, width, height, -1, this.tabPlacement);
        ContextUtils.getPainter(tabContext)
            .paintTabbedPaneTabBorder(tabContext, g, x, y, width, height, -1, this.tabPlacement);
        ContextUtils.setComponentState(tabContext, oldState);
    }

    /**
     * Layout label text for a tab.
     * @param ss the SynthContext.
     * @param tabPlacement the side the tabs are on.
     * @param metrics the font metrics.
     * @param tabIndex the index of the tab to lay out.
     * @param title the text for the label, if any.
     * @param icon the icon for the label, if any.
     * @param tabRect Rectangle to layout text and icon in.
     * @param iconRect Rectangle to place icon bounds in
     * @param textRect Rectangle to place text in
     * @param isSelected is the tab selected?
     */
    protected void layoutLabel(SynthContext ss, int tabPlacement, FontMetrics metrics, int tabIndex, String title,
            Icon icon, Rectangle tabRect, Rectangle iconRect,
            Rectangle textRect, boolean isSelected) {
        View v = this.getTextViewForTab(tabIndex);

        if (v != null) {
            this.tabPane.putClientProperty("html", v);
        }

        textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

        ss.getStyle()
            .getGraphicsUtils(ss)
            .layoutText(ss, metrics, title, icon, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEADING,
                    SwingConstants.TRAILING, tabRect, iconRect, textRect,
                    this.textIconGap);

        this.tabPane.putClientProperty("html", null);

        int xNudge = this.getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
        int yNudge = this.getTabLabelShiftY(tabPlacement, tabIndex, isSelected);

        iconRect.x += xNudge;
        iconRect.y += yNudge;
        textRect.x += xNudge;
        textRect.y += yNudge;

        // Changing position between text and icon...
        if (iconRect.width > 0) {
            int xx = iconRect.x;
            iconRect.x = textRect.x;
            textRect.x = (xx - textRect.width) + iconRect.width + this.textIconGap;
        }
    }

    /**
     * Paint the label text for a tab.
     * @param ss the SynthContext.
     * @param g the Graphics context.
     * @param tabPlacement the side the tabs are on.
     * @param font the font to use.
     * @param metrics the font metrics.
     * @param tabIndex the index of the tab to lay out.
     * @param title the text for the label, if any.
     * @param textRect Rectangle to place text in
     * @param isSelected is the tab selected?
     */
    protected void paintText(SynthContext ss, Graphics g, int tabPlacement, Font font, FontMetrics metrics,
            int tabIndex, String title, Rectangle textRect, boolean isSelected) {
        g.setFont(font);

        View v = this.getTextViewForTab(tabIndex);

        if (v != null) {
            // html
            v.paint(g, textRect);
        } else {
            // plain text
            int mnemIndex = this.tabPane.getDisplayedMnemonicIndexAt(tabIndex);
            FontMetrics fm = SwingUtilities2.getFontMetrics(this.tabPane, g);
            title = SwingUtilities2.clipStringIfNecessary(this.tabPane, fm, title, textRect.width);
            // ORIGINAL g.setColor(ss.getStyle().getColor(ss,
            // ColorType.TEXT_FOREGROUND));
            Color foregroundColor = this.tabPane.getForegroundAt(tabIndex);
            if ((foregroundColor != null) && !(foregroundColor instanceof UIResource)) {
                g.setColor(foregroundColor);
            } else {
                g.setColor(ss.getStyle().getColor(ss, ColorType.TEXT_FOREGROUND));
            }
            ss.getStyle().getGraphicsUtils(ss).paintText(ss, g, title, textRect, mnemIndex);
        }
    }

    /**
     * Paint the content pane's border.
     * @param ss the SynthContext.
     * @param g the Graphics context.
     * @param tabPlacement the side the tabs are on.
     * @param selectedIndex the current selected tab index.
     */
    protected void paintContentBorder(SynthContext ss, Graphics g, int tabPlacement, int selectedIndex) {
        int width = this.tabPane.getWidth();
        int height = this.tabPane.getHeight();
        Insets insets = this.tabPane.getInsets();

        int x = insets.left;
        int y = insets.top;
        int w = width - insets.right - insets.left;
        int h = height - insets.top - insets.bottom;

        switch (tabPlacement) {

            case LEFT:
                x += this.calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
                w -= (x - insets.left);
                break;

            case RIGHT:
                w -= this.calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
                break;

            case BOTTOM:
                h -= this.calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
                break;

            case TOP:
            default:
                y += this.calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
                h -= (y - insets.top);
        }

        OntimizeLookAndFeel.updateSubregion(ss, g, new Rectangle(x, y, w, h));
        ContextUtils.getPainter(ss).paintTabbedPaneContentBackground(ss, g, x, y, w, h);
        ContextUtils.getPainter(ss).paintTabbedPaneContentBorder(ss, g, x, y, w, h);
    }

    /**
     * Make sure we have laid out the pane with the current layout.
     */
    protected void ensureCurrentLayout() {
        if (!this.tabPane.isValid()) {
            this.tabPane.validate();
        }

        /*
         * If tabPane doesn't have a peer yet, the validate() call will silently fail. We handle that by
         * forcing a layout if tabPane is still invalid. See bug 4237677.
         */
        if (!this.tabPane.isValid()) {
            TabbedPaneLayout layout = (TabbedPaneLayout) this.tabPane.getLayout();

            layout.calculateLayoutInfo();
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateMaxTabHeight(int)
     */
    @Override
    public int calculateMaxTabHeight(int tabPlacement) {
        SynthContext tabContext = this.getContext(this.tabPane, Region.TABBED_PANE_TAB, SynthConstants.ENABLED);
        FontMetrics metrics = this.getFontMetrics(tabContext.getStyle().getFont(tabContext));
        int tabCount = this.tabPane.getTabCount();
        int result = 0;
        int fontHeight = metrics.getHeight();

        for (int i = 0; i < tabCount; i++) {
            result = Math.max(this.calculateTabHeight(tabPlacement, i, fontHeight), result);
        }

        result = result + 2;
        return result;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabWidth(int, int, java.awt.FontMetrics)
     */
    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        Icon icon = this.getIconForTab(tabIndex);
        Insets tabInsets = this.getTabInsets(tabPlacement, tabIndex);
        int width = tabInsets.left + tabInsets.right + 3;
        Component tabComponent = this.tabPane.getTabComponentAt(tabIndex);

        if (tabComponent != null) {
            width += tabComponent.getPreferredSize().width;
            if ((tabIndex < this.rects.length) && (this.tabCloseButtonPlacement != SwingConstants.CENTER)) {
                width += this.getCloseButtonBounds(tabIndex).width + this.textIconGap;
            }
        } else {
            if (icon != null) {
                width += icon.getIconWidth() + this.textIconGap;
            }

            // Hack to prevent array index out of bounds before the size has
            // been set and the rectangles created.
            if ((tabIndex < this.rects.length) && (this.tabCloseButtonPlacement != SwingConstants.CENTER)) {
                width += this.getCloseButtonBounds(tabIndex).width + this.textIconGap;
            }

            View v = this.getTextViewForTab(tabIndex);

            if (v != null) {
                // html
                width += (int) v.getPreferredSpan(View.X_AXIS);
            } else {
                SynthContext tabContext = this.getContext(this.tabPane, Region.TABBED_PANE_TAB, SynthConstants.ENABLED);
                // plain text
                String title = this.tabPane.getTitleAt(tabIndex);

                width += tabContext.getStyle()
                    .getGraphicsUtils(tabContext)
                    .computeStringWidth(tabContext, metrics.getFont(), metrics, title);
            }
        }

        // Adding 20 px (10 per each side) to set a margin between the text and
        // the end of the tab.
        width = width + 20;
        return width;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateMaxTabWidth(int)
     */
    @Override
    public int calculateMaxTabWidth(int tabPlacement) {
        SynthContext tabContext = this.getContext(this.tabPane, Region.TABBED_PANE_TAB, SynthConstants.ENABLED);
        FontMetrics metrics = this.getFontMetrics(tabContext.getStyle().getFont(tabContext));
        int tabCount = this.tabPane.getTabCount();
        int result = 0;

        for (int i = 0; i < tabCount; i++) {
            result = Math.max(this.calculateTabWidth(tabPlacement, i, metrics), result);
        }

        return result;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getTabInsets(int, int)
     */
    @Override
    protected Insets getTabInsets(int tabPlacement, int tabIndex) {
        return this.tabInsets;
    }

    @Override
    public Rectangle getTabBounds(JTabbedPane pane, int i) {
        Rectangle rect = super.getTabBounds(pane, i);
        return rect;
    }

    public Rectangle getMaxTabBounds(JTabbedPane pane) {
        Rectangle dimension = null;
        boolean correctDimension = false;
        int index = 0;
        int size = this.rects.length;
        for (int i = 0; i < size; i++) {
            Rectangle rect = super.getTabBounds(pane, index);
            /*
             * Note: Because of scroll buttons bounds does not update properly and depending on the position of
             * them and if they are displaying or not, when requesting dimension of tab bounds sometimes it can
             * be wrong.
             */
            if ((rect.x != -1) && (rect.y != -1) && (rect.width != 0) && (rect.height != 0)) {
                dimension = rect;
                break;
            }
            index++;
        }
        return dimension;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getFontMetrics()
     */
    @Override
    protected FontMetrics getFontMetrics() {
        SynthContext tabContext = this.getContext(this.tabPane, Region.TABBED_PANE_TAB, SynthConstants.ENABLED);
        return this.getFontMetrics(tabContext.getStyle().getFont(tabContext));
    }

    /**
     * Get the font metrics for the font.
     * @param font the font.
     * @return the metrics for the font.
     */
    protected FontMetrics getFontMetrics(Font font) {
        return this.tabPane.getFontMetrics(font);
    }

    /**
     * Update the SynthContext for the tab area for a specified tab.
     * @param index the tab to update for.
     * @param selected is the tab selected?
     * @param isMouseDown is the mouse down?
     * @param isMouseOver is the mouse over the tab?
     * @param hasFocus do we have focus?
     */
    protected SynthContext getTabContext(int index, boolean selected, boolean isMouseDown, boolean isMouseOver,
            boolean hasFocus) {
        int state = 0;

        if (!this.tabPane.isEnabled() || !this.tabPane.isEnabledAt(index)) {
            state |= SynthConstants.DISABLED;

            if (selected) {
                state |= SynthConstants.SELECTED;
            }
        } else if (selected) {
            state |= (SynthConstants.ENABLED | SynthConstants.SELECTED);

            if (isMouseOver && UIManager.getBoolean("TabbedPane.isTabRollover")) {
                state |= SynthConstants.MOUSE_OVER;
            }
        } else if (isMouseOver) {
            state |= (SynthConstants.ENABLED | SynthConstants.MOUSE_OVER);
        } else {
            state = OntimizeLookAndFeel.getComponentState(this.tabPane);
            state &= ~SynthConstants.FOCUSED; // Don't use tabbedpane focus
            // state.
        }

        if (hasFocus && this.tabPane.hasFocus()) {
            state |= SynthConstants.FOCUSED; // individual tab has focus
        }

        if (isMouseDown) {
            state |= SynthConstants.PRESSED;
        }

        return this.getContext(this.tabPane, Region.TABBED_PANE_TAB, state);
    }

    /**
     * Determine whether the mouse is over a tab close button, and if so, set the hover index.
     * @param x the current mouse x coordinate.
     * @param y the current mouse y coordinate.
     * @return {@code true} if the mouse is over a close button, {@code false} otherwise.
     */
    protected boolean isOverCloseButton(int x, int y) {
        if (this.tabCloseButtonPlacement != SwingConstants.CENTER) {
            int tabCount = this.tabPane.getTabCount();

            for (int i = 0; i < tabCount; i++) {
                if (this.getCloseButtonBounds(i).contains(x, y)) {
                    this.closeButtonHoverIndex = i;
                    return true;
                }
            }
        }

        this.closeButtonHoverIndex = -1;
        return false;
    }

    /**
     * Get the bounds for a tab close button.
     * @param tabIndex the tab index.
     * @return the bounds.
     */
    protected Rectangle getCloseButtonBounds(int tabIndex) {
        Rectangle bounds = new Rectangle(this.rects[tabIndex]);

        bounds.width = this.closeButtonSize;
        bounds.height = this.closeButtonSize;

        bounds.y += ((this.rects[tabIndex].height - this.closeButtonSize - this.closeButtonInsets.top
                - this.closeButtonInsets.bottom) / 2) + this.closeButtonInsets.top;

        boolean onLeft = this.isCloseButtonOnLeft();

        if (onLeft) {
            int offset = (this.orientation == ControlOrientation.VERTICAL) || (tabIndex == 0) ? 6 : 3;

            bounds.x += offset;
        } else {
            int offset = (this.orientation == ControlOrientation.VERTICAL)
                    || (tabIndex == (this.tabPane.getTabCount() - 1)) ? 6 : 4;

            bounds.x += this.rects[tabIndex].width - bounds.width - offset;
        }

        return bounds;
    }

    /**
     * Return {@code true} if the tab close button should be placed on the left, {@code false}
     * otherwise.
     * @return {@code true} if the tab close button should be placed on the left, {@code false}
     *         otherwise.
     */
    protected boolean isCloseButtonOnLeft() {
        return (this.tabCloseButtonPlacement == SwingConstants.LEFT) == this.tabPane.getComponentOrientation()
            .isLeftToRight();
    }

    /**
     * Called when a close tab button is pressed.
     * @param tabIndex TODO
     */
    protected void doClose(int tabIndex) {
        if ((this.tabCloseListener == null) || this.tabCloseListener.tabAboutToBeClosed(tabIndex)) {
            String title = this.tabPane.getTitleAt(tabIndex);
            Component component = this.tabPane.getComponentAt(tabIndex);

            this.tabPane.removeTabAt(tabIndex);

            if (this.tabCloseListener != null) {
                this.tabCloseListener.tabClosed(title, component);
            }
        }
    }

    /**
     * The scrollable tab button.
     */
    protected class SynthScrollableTabButton extends OntimizeArrowButton {

        protected static final long serialVersionUID = -3983149584304630486L;

        /**
         * Creates a new SynthScrollableTabButton object.
         * @param direction the arrow direction.
         */
        public SynthScrollableTabButton(int direction) {
            super(direction);
            this.putClientProperty("__arrow_scale__", 0.6);
        }

        /**
         * @see javax.swing.JComponent#getPreferredSize()
         */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(26, 20);
        }

    }

    /**
     * The layout manager.
     */
    protected class OntimizeTabbedPaneLayout extends TabbedPaneLayout {

        @Override
        protected Dimension calculateSize(boolean minimum) {
            if (OTabbedPaneUI.this.tabPane.getTabCount() > 0) {
                return super.calculateSize(minimum);
            }
            return new Dimension(0, 0);
        }

        @Override
        protected int preferredTabAreaHeight(int tabPlacement, int width) {
            return OTabbedPaneUI.this.calculateMaxTabHeight(tabPlacement);
        }

        @Override
        protected int preferredTabAreaWidth(int tabPlacement, int height) {
            return OTabbedPaneUI.this.calculateMaxTabWidth(tabPlacement);
        }

        /**
         * @see com.apple.laf.AquaTabbedPaneCopyFromBasicUI$TabbedPaneLayout#layoutContainer(java.awt.Container)
         */
        @Override
        public void layoutContainer(Container parent) {
            OTabbedPaneUI.this.setRolloverTab(-1);

            this.setScrollButtonDirections();
            this.calculateLayoutInfo();

            boolean shouldChangeFocus = this.verifyFocus(OTabbedPaneUI.this.tabPane.getSelectedIndex());

            if (OTabbedPaneUI.this.tabPane.getTabCount() <= 0) {
                return;
            }

            this.calcContentRect();

            for (int i = 0; i < OTabbedPaneUI.this.tabPane.getComponentCount(); i++) {
                Component child = OTabbedPaneUI.this.tabPane.getComponent(i);

                // Ignore the scroll buttons. They have already been positioned
                // in
                // calculateTabRects, which will have been called by
                // calculateLayoutInfo,
                // which is called above.
                if ((child != OTabbedPaneUI.this.scrollBackwardButton)
                        && (child != OTabbedPaneUI.this.scrollForwardButton)
                        && (child != OTabbedPaneUI.this.arrowButton)) {
                    child.setBounds(OTabbedPaneUI.this.contentRect);
                }
            }
            this.setTabContainerBounds();
            this.layoutTabComponents();

            if (shouldChangeFocus
                    && !SwingUtilities2.tabbedPaneChangeFocusTo(OTabbedPaneUI.this.getVisibleComponent())) {
                OTabbedPaneUI.this.tabPane.requestFocus();
            }
        }

        /**
         * Check if we have a custom tab component container.
         * @return the container used to hold custom tab components.
         */
        protected Component getTabContainer() {
            int tabCount = OTabbedPaneUI.this.tabPane.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                Component tabComponent = OTabbedPaneUI.this.tabPane.getTabComponentAt(i);
                if (tabComponent != null) {
                    return tabComponent.getParent();
                }
            }
            return null;
        }

        /**
         * Set the position of the tab container that is used to layout custom tab components.
         */
        protected void setTabContainerBounds() {
            Component tabContainer = this.getTabContainer();
            if (tabContainer != null) {
                int tabContainerX = OTabbedPaneUI.this.tabPlacement == SwingConstants.RIGHT
                        ? OTabbedPaneUI.this.contentRect.width : 0;
                int tabContainerY = OTabbedPaneUI.this.tabPlacement == SwingConstants.BOTTOM
                        ? OTabbedPaneUI.this.contentRect.height : 0;
                int tabContainerWidth = (OTabbedPaneUI.this.tabPlacement == SwingConstants.LEFT)
                        || (OTabbedPaneUI.this.tabPlacement == SwingConstants.RIGHT)
                                ? OTabbedPaneUI.this.tabPane
                                    .getWidth() - OTabbedPaneUI.this.contentRect.width
                                : OTabbedPaneUI.this.contentRect.width;
                int tabContainerHeight = (OTabbedPaneUI.this.tabPlacement == SwingConstants.TOP)
                        || (OTabbedPaneUI.this.tabPlacement == SwingConstants.BOTTOM)
                                ? OTabbedPaneUI.this.tabPane
                                    .getHeight() - OTabbedPaneUI.this.contentRect.height
                                : OTabbedPaneUI.this.contentRect.height;
                tabContainer.setBounds(tabContainerX, tabContainerY, tabContainerWidth, tabContainerHeight);
            }
        }

        /**
         * Set the directions of the arrows in the scroll buttons if necessary.
         */
        protected void setScrollButtonDirections() {
            if ((OTabbedPaneUI.this.tabPlacement == SwingConstants.LEFT)
                    || (OTabbedPaneUI.this.tabPlacement == SwingConstants.RIGHT)) {
                if (OTabbedPaneUI.this.scrollForwardButton.getDirection() != SwingConstants.SOUTH) {
                    OTabbedPaneUI.this.scrollForwardButton.setDirection(SwingConstants.SOUTH);
                }

                if (OTabbedPaneUI.this.scrollBackwardButton.getDirection() != SwingConstants.NORTH) {
                    OTabbedPaneUI.this.scrollBackwardButton.setDirection(SwingConstants.NORTH);
                }

                if (OTabbedPaneUI.this.arrowButton.getDirection() != SwingConstants.WEST) {
                    OTabbedPaneUI.this.arrowButton.setDirection(SwingConstants.WEST);
                }
            } else if (OTabbedPaneUI.this.tabPane.getComponentOrientation().isLeftToRight()) {
                if (OTabbedPaneUI.this.scrollForwardButton.getDirection() != SwingConstants.EAST) {
                    OTabbedPaneUI.this.scrollForwardButton.setDirection(SwingConstants.EAST);
                }

                if (OTabbedPaneUI.this.scrollBackwardButton.getDirection() != SwingConstants.WEST) {
                    OTabbedPaneUI.this.scrollBackwardButton.setDirection(SwingConstants.WEST);
                }

                if (OTabbedPaneUI.this.arrowButton.getDirection() != SwingConstants.SOUTH) {
                    OTabbedPaneUI.this.arrowButton.setDirection(SwingConstants.SOUTH);
                }
            } else {
                if (OTabbedPaneUI.this.scrollForwardButton.getDirection() != SwingConstants.WEST) {
                    OTabbedPaneUI.this.scrollForwardButton.setDirection(SwingConstants.WEST);
                }

                if (OTabbedPaneUI.this.scrollBackwardButton.getDirection() != SwingConstants.EAST) {
                    OTabbedPaneUI.this.scrollBackwardButton.setDirection(SwingConstants.EAST);
                }

                if (OTabbedPaneUI.this.arrowButton.getDirection() != SwingConstants.SOUTH) {
                    OTabbedPaneUI.this.arrowButton.setDirection(SwingConstants.SOUTH);
                }
            }
        }

        /**
         * Verify that the currently focused element exists. Reset the focus to none if it doesn't. Return
         * whether focus needs to be changed.
         * @param selectedIndex the current selected index.
         * @return {@code true} if the focus needs to be changed, {@code false} otherwise.
         */
        protected boolean verifyFocus(int selectedIndex) {
            Component visibleComponent = OTabbedPaneUI.this.getVisibleComponent();
            Component selectedComponent = null;

            if (selectedIndex < 0) {
                if (visibleComponent != null) {
                    // The last tab was removed, so remove the component.
                    OTabbedPaneUI.this.setVisibleComponent(null);
                }
            } else {
                selectedComponent = OTabbedPaneUI.this.tabPane.getComponentAt(selectedIndex);
            }

            if (OTabbedPaneUI.this.tabPane.getTabCount() == 0) {
                return false;
            }

            boolean shouldChangeFocus = false;

            // In order to allow programs to use a single component
            // as the display for multiple tabs, we will not change
            // the visible compnent if the currently selected tab
            // has a null component. This is a bit dicey, as we don't
            // explicitly state we support this in the spec, but since
            // programs are now depending on this, we're making it work.
            if (selectedComponent != null) {
                if ((selectedComponent != visibleComponent) && (visibleComponent != null)) {
                    if (this.findFocusOwner(visibleComponent) != null) {
                        shouldChangeFocus = true;
                    }
                }

                OTabbedPaneUI.this.setVisibleComponent(selectedComponent);
            }

            return shouldChangeFocus;
        }

        /**
         * Calculate the bounds Rectangle for the content panes.
         */
        protected void calcContentRect() {
            Insets contentInsets = OTabbedPaneUI.this.getContentBorderInsets(OTabbedPaneUI.this.tabPlacement);
            Rectangle bounds = OTabbedPaneUI.this.tabPane.getBounds();
            Insets insets = OTabbedPaneUI.this.tabPane.getInsets();

            int cx;
            int cy;
            int cw;
            int ch;

            switch (OTabbedPaneUI.this.tabPlacement) {

                case LEFT:
                    cx = OTabbedPaneUI.this.tabAreaRect.x + OTabbedPaneUI.this.tabAreaRect.width + contentInsets.left;
                    cy = OTabbedPaneUI.this.tabAreaRect.y + contentInsets.top;
                    cw = bounds.width - insets.left - insets.right - OTabbedPaneUI.this.tabAreaRect.width
                            - contentInsets.left - contentInsets.right;
                    ch = bounds.height - insets.top - insets.bottom - contentInsets.top - contentInsets.bottom;
                    break;

                case RIGHT:
                    cx = insets.left + contentInsets.left;
                    cy = insets.top + contentInsets.top;
                    cw = bounds.width - insets.left - insets.right - OTabbedPaneUI.this.tabAreaRect.width
                            - contentInsets.left - contentInsets.right;
                    ch = bounds.height - insets.top - insets.bottom - contentInsets.top - contentInsets.bottom;
                    break;

                case BOTTOM:
                    cx = insets.left + contentInsets.left;
                    cy = insets.top + contentInsets.top;
                    cw = bounds.width - insets.left - insets.right - contentInsets.left - contentInsets.right;
                    ch = bounds.height - insets.top - insets.bottom - OTabbedPaneUI.this.tabAreaRect.height
                            - contentInsets.top - contentInsets.bottom;
                    break;

                case TOP:
                default:
                    cx = OTabbedPaneUI.this.tabAreaRect.x + contentInsets.left;
                    cy = OTabbedPaneUI.this.tabAreaRect.y + OTabbedPaneUI.this.tabAreaRect.height + contentInsets.top;
                    cw = bounds.width - insets.left - insets.right - contentInsets.left - contentInsets.right;
                    ch = bounds.height - insets.top - insets.bottom - OTabbedPaneUI.this.tabAreaRect.height
                            - contentInsets.top - contentInsets.bottom;
            }

            OTabbedPaneUI.this.contentRect.setBounds(cx, cy, cw, ch);
        }

        /**
         * Layout the tab components.
         */
        protected void layoutTabComponents() {
            Rectangle rect = new Rectangle();
            for (int i = 0; i < OTabbedPaneUI.this.tabPane.getTabCount(); i++) {
                Component c = OTabbedPaneUI.this.tabPane.getTabComponentAt(i);

                if (c == null) {
                    continue;
                }

                OTabbedPaneUI.this.getTabBounds(i, rect);

                Dimension preferredSize = c.getPreferredSize();
                Insets insets = OTabbedPaneUI.this.getTabInsets(OTabbedPaneUI.this.tabPlacement, i);
                int outerX = rect.x + insets.left;
                int outerY = rect.y + insets.top;
                int outerWidth = rect.width - insets.left - insets.right;
                int outerHeight = rect.height - insets.top - insets.bottom;

                // centralize component
                int x = outerX + ((outerWidth - preferredSize.width) / 2);
                int y = outerY + ((outerHeight - preferredSize.height) / 2);
                boolean isSelected = i == OTabbedPaneUI.this.tabPane.getSelectedIndex();
                c.setBounds(x + OTabbedPaneUI.this.getTabLabelShiftX(OTabbedPaneUI.this.tabPlacement, i, isSelected),
                        y + OTabbedPaneUI.this.getTabLabelShiftY(OTabbedPaneUI.this.tabPlacement, i, isSelected),
                        preferredSize.width, preferredSize.height);
            }
        }

        /**
         * @see com.apple.laf.AquaTabbedPaneCopyFromBasicUI$TabbedPaneLayout#calculateTabRects(int, int)
         */
        @Override
        protected void calculateTabRects(int tabPlacement, int tabCount) {
            if (OTabbedPaneUI.this.orientation == ControlOrientation.HORIZONTAL) {
                OTabbedPaneUI.this.maxTabHeight = OTabbedPaneUI.this.calculateMaxTabHeight(tabPlacement);
            } else {
                OTabbedPaneUI.this.maxTabWidth = OTabbedPaneUI.this.calculateMaxTabWidth(tabPlacement);
            }

            // Calculate the tab area itself.
            this.calcTabAreaRect();

            if (tabCount == 0) {
                OTabbedPaneUI.this.scrollBackwardButton.setVisible(false);
                OTabbedPaneUI.this.scrollForwardButton.setVisible(false);
                OTabbedPaneUI.this.arrowButton.setVisible(false);
                OTabbedPaneUI.this.runCount = 0;
                OTabbedPaneUI.this.selectedRun = -1;
                return;
            }

            OTabbedPaneUI.this.selectedRun = 0;
            OTabbedPaneUI.this.runCount = 1;

            int selectedIndex = OTabbedPaneUI.this.tabPane.getSelectedIndex();

            if (OTabbedPaneUI.this.leadingTabIndex > selectedIndex) {
                OTabbedPaneUI.this.leadingTabIndex = selectedIndex;
            }

            Insets tabAreaInsets = OTabbedPaneUI.this.getTabAreaInsets(tabPlacement);
            Dimension size = new Dimension(
                    OTabbedPaneUI.this.tabAreaRect.width - tabAreaInsets.left - tabAreaInsets.right,
                    OTabbedPaneUI.this.tabAreaRect.height
                            - tabAreaInsets.top - tabAreaInsets.bottom);
            int tabAreaLength = OTabbedPaneUI.this.orientation.getLength(size);
            int buttonLength = OTabbedPaneUI.this.orientation
                .getLength(OTabbedPaneUI.this.scrollForwardButton.getPreferredSize());

            this.determineVisibleTabIndices(tabCount, selectedIndex, tabAreaLength, buttonLength);
            this.resetTabPositionsToLeadingTabIndex(tabCount);
            int totalLength = OTabbedPaneUI.this.orientation.getPosition(
                    OTabbedPaneUI.this.rects[OTabbedPaneUI.this.trailingTabIndex].x
                            + OTabbedPaneUI.this.rects[OTabbedPaneUI.this.trailingTabIndex].width,
                    OTabbedPaneUI.this.rects[OTabbedPaneUI.this.trailingTabIndex].y
                            + OTabbedPaneUI.this.rects[OTabbedPaneUI.this.trailingTabIndex].height);

            if ((OTabbedPaneUI.this.leadingTabIndex > 0) || (OTabbedPaneUI.this.trailingTabIndex < (tabCount - 1))) {
                this.resizeTabs(tabCount, totalLength, buttonLength, tabAreaLength);
            } else {
                this.centerTabs(tabCount, totalLength, tabAreaLength);
            }

            // Set the positions and visibility of the scroll buttons.
            this.setScrollButtonPositions(OTabbedPaneUI.this.scrollBackwardButton,
                    (OTabbedPaneUI.this.leadingTabIndex > 0),
                    OTabbedPaneUI.this.orientation
                        .getPosition(OTabbedPaneUI.this.rects[OTabbedPaneUI.this.leadingTabIndex]) - buttonLength);
            this.setScrollButtonPositions(
                    OTabbedPaneUI.this.scrollForwardButton,
                    (OTabbedPaneUI.this.trailingTabIndex < (tabCount - 1)),
                    OTabbedPaneUI.this.orientation
                        .getPosition(OTabbedPaneUI.this.rects[OTabbedPaneUI.this.trailingTabIndex])
                            + OTabbedPaneUI.this.orientation
                                .getLength(OTabbedPaneUI.this.rects[OTabbedPaneUI.this.trailingTabIndex]));
            this.setScrollButtonPositions(
                    OTabbedPaneUI.this.arrowButton,
                    (OTabbedPaneUI.this.leadingTabIndex > 0) || (OTabbedPaneUI.this.trailingTabIndex < (tabCount - 1)),
                    OTabbedPaneUI.this.orientation
                        .getPosition(OTabbedPaneUI.this.rects[OTabbedPaneUI.this.trailingTabIndex])
                            + OTabbedPaneUI.this.orientation
                                .getLength(OTabbedPaneUI.this.rects[OTabbedPaneUI.this.trailingTabIndex])
                            + (OTabbedPaneUI.this.scrollForwardButton.isVisible()
                                    ? OTabbedPaneUI.this.scrollForwardButton.getWidth() : 0));

            // If component orientation right to left and tab placement is on
            // the top or the bottom,
            // flip x positions and adjust by widths.
            if (!OTabbedPaneUI.this.tabPane.getComponentOrientation().isLeftToRight()
                    && (OTabbedPaneUI.this.orientation == ControlOrientation.HORIZONTAL)) {
                this.flipRightToLeft(tabCount, OTabbedPaneUI.this.tabPane.getSize());
            }
        }

        /**
         * Calculate the rectangle into which the tabs will be drawn. This does not include the tab area
         * insets, but does include the tab pane insets.
         *
         * <p>
         * This is used for painting the background as well as for laying out the tabs.
         * </p>
         */
        protected void calcTabAreaRect() {
            Insets insets = OTabbedPaneUI.this.tabPane.getInsets();
            Insets tabAreaInsets = OTabbedPaneUI.this.getTabAreaInsets(OTabbedPaneUI.this.tabPlacement);
            Rectangle bounds = OTabbedPaneUI.this.tabPane.getBounds();

            if (OTabbedPaneUI.this.tabPane.getTabCount() == 0) {
                OTabbedPaneUI.this.tabAreaRect.setBounds(0, 0, 0, 0);
                return;
            }

            // Calculate bounds within which a tab run must fit.
            int position;
            int offset;
            int length;
            int thickness;

            if (OTabbedPaneUI.this.orientation == ControlOrientation.HORIZONTAL) {
                length = bounds.width - insets.left - insets.right;
                position = insets.left;
                thickness = OTabbedPaneUI.this.maxTabHeight + tabAreaInsets.top + tabAreaInsets.bottom;
                offset = (OTabbedPaneUI.this.tabPlacement == SwingConstants.BOTTOM)
                        ? bounds.height - insets.bottom - thickness : insets.top;
            } else {
                length = bounds.height - insets.top - insets.bottom;
                position = insets.top;
                thickness = OTabbedPaneUI.this.maxTabWidth + tabAreaInsets.left + tabAreaInsets.right;
                offset = (OTabbedPaneUI.this.tabPlacement == SwingConstants.RIGHT)
                        ? bounds.width - insets.right - thickness : insets.left;
            }

            OTabbedPaneUI.this.tabAreaRect
                .setBounds(OTabbedPaneUI.this.orientation.createBounds(position, offset, length, thickness));
        }

        /**
         * Calculate the leading and trailing tab indices that will fit in the length, keeping the selected
         * index visible.
         * @param tabCount the number of tabs.
         * @param selectedIndex the current tab.
         * @param tabAreaLength the length of the tab area. This takes the tab area insets into account.
         * @param buttonLength the length of a scroll button. They are both the same length.
         */
        protected void determineVisibleTabIndices(int tabCount, int selectedIndex, int tabAreaLength,
                int buttonLength) {
            int desiredMaximumLength = this.calcDesiredMaximumLength(tabCount);
            int leadingTabOffset = OTabbedPaneUI.this.orientation
                .getPosition(OTabbedPaneUI.this.rects[OTabbedPaneUI.this.leadingTabIndex]);
            int selectedTabEndOffset = OTabbedPaneUI.this.orientation.getPosition(
                    OTabbedPaneUI.this.rects[selectedIndex].x + OTabbedPaneUI.this.rects[selectedIndex].width,
                    OTabbedPaneUI.this.rects[selectedIndex].y + OTabbedPaneUI.this.rects[selectedIndex].height);

            if (desiredMaximumLength <= tabAreaLength) {
                // Fits with no scroll buttons.
                OTabbedPaneUI.this.leadingTabIndex = 0;
                OTabbedPaneUI.this.trailingTabIndex = tabCount - 1;
            } else if (((desiredMaximumLength - leadingTabOffset) + buttonLength) <= tabAreaLength) {
                // Fits from current leading tab index, with scroll backward
                // button. Leave leadingTabIndex alone.
                OTabbedPaneUI.this.trailingTabIndex = tabCount - 1;
            } else if (((OTabbedPaneUI.this.leadingTabIndex == 0)
                    && (((selectedTabEndOffset - leadingTabOffset) + buttonLength) <= tabAreaLength))
                    || (((selectedTabEndOffset - leadingTabOffset) + (2 * buttonLength)) <= tabAreaLength)) {
                // Selected index fits with current leading tab index and one or
                // two scroll buttons. Leave leadingTabIndex alone.
                OTabbedPaneUI.this.trailingTabIndex = -1;

                for (int i = tabCount - 1; i > selectedIndex; i--) {
                    int end = OTabbedPaneUI.this.orientation.getPosition(
                            OTabbedPaneUI.this.rects[i].x + OTabbedPaneUI.this.rects[i].width,
                            OTabbedPaneUI.this.rects[i].y
                                    + OTabbedPaneUI.this.rects[i].height);

                    if (((end - leadingTabOffset) + (3 * buttonLength)) <= tabAreaLength) {
                        OTabbedPaneUI.this.trailingTabIndex = i;
                        break;
                    }
                }

                if (OTabbedPaneUI.this.trailingTabIndex == -1) {
                    OTabbedPaneUI.this.trailingTabIndex = selectedIndex;
                }
            } else {
                // Selected index does not fit with current leading index and
                // two scroll buttons.
                // Make selected index the trailing index and find the leading
                // index that will fit.
                OTabbedPaneUI.this.trailingTabIndex = selectedIndex;
                OTabbedPaneUI.this.leadingTabIndex = -1;

                for (int i = 0; i < selectedIndex; i++) {
                    int start = OTabbedPaneUI.this.orientation.getPosition(OTabbedPaneUI.this.rects[i]);

                    if (((selectedTabEndOffset - start) + (2 * buttonLength)) <= tabAreaLength) {
                        OTabbedPaneUI.this.leadingTabIndex = i;
                        break;
                    }
                }

                if (OTabbedPaneUI.this.leadingTabIndex == -1) {
                    OTabbedPaneUI.this.leadingTabIndex = selectedIndex;
                }
            }

            OTabbedPaneUI.this.tabRuns[0] = OTabbedPaneUI.this.leadingTabIndex;
        }

        /**
         * Run through tabs and lay them all out in a single run, assigning maxTabWidth and maxTabHeight.
         * The offset and thickness are set to zero in this method. They will be assigned good values later.
         * @param tabCount the number of tabs.
         * @return the maximum width, if tabs run horizontall, otherwise the maximum height.
         */
        protected int calcDesiredMaximumLength(int tabCount) {
            FontMetrics metrics = OTabbedPaneUI.this.getFontMetrics();
            int fontHeight = metrics.getHeight();
            Insets tabAreaInsets = OTabbedPaneUI.this.getTabAreaInsets(fontHeight);
            Point corner = new Point(OTabbedPaneUI.this.tabAreaRect.x + tabAreaInsets.left,
                    OTabbedPaneUI.this.tabAreaRect.y + tabAreaInsets.top);
            int offset = OTabbedPaneUI.this.orientation.getOrthogonalOffset(corner);
            int thickness = (OTabbedPaneUI.this.orientation == ControlOrientation.HORIZONTAL)
                    ? OTabbedPaneUI.this.maxTabWidth : OTabbedPaneUI.this.maxTabHeight;
            int position = 0;
            int maxTabLength = 0;

            // Run through tabs and lay them out in a single long run.
            for (int i = 0; i < tabCount; i++) {
                int length = (OTabbedPaneUI.this.orientation == ControlOrientation.HORIZONTAL)
                        ? OTabbedPaneUI.this.calculateTabWidth(SwingConstants.TOP, i, metrics)
                        : OTabbedPaneUI.this.calculateTabHeight(SwingConstants.LEFT, i, fontHeight);

                OTabbedPaneUI.this.rects[i]
                    .setBounds(OTabbedPaneUI.this.orientation.createBounds(position, offset, length, thickness));

                // Update the maximum length and the next tab position.
                maxTabLength = Math.max(maxTabLength, length);
                position += length;
            }

            // Update the BasicTabbedPaneUI length variable.
            if (OTabbedPaneUI.this.orientation == ControlOrientation.HORIZONTAL) {
                OTabbedPaneUI.this.maxTabWidth = maxTabLength;
            } else {
                OTabbedPaneUI.this.maxTabHeight = maxTabLength;
            }

            return position;
        }

        /**
         * Reset the positions of the tabs between leadingTabIndex and trailingTabIndex, inclusive, such
         * that the leadingTabIndex is at position zero.
         * @param tabCount the number of tabs.
         */
        protected void resetTabPositionsToLeadingTabIndex(int tabCount) {
            // Rebalance the layout such that the leading tab is at position 0.
            int leadingTabPosition = OTabbedPaneUI.this.orientation
                .getPosition(OTabbedPaneUI.this.rects[OTabbedPaneUI.this.leadingTabIndex]);

            for (int i = 0; i < tabCount; i++) {
                if ((i < OTabbedPaneUI.this.leadingTabIndex) || (i > OTabbedPaneUI.this.trailingTabIndex)) {
                    OTabbedPaneUI.this.rects[i].setBounds(-1000, -1000, 0, 0);
                } else {
                    OTabbedPaneUI.this.orientation.updateBoundsPosition(OTabbedPaneUI.this.rects[i],
                            OTabbedPaneUI.this.orientation.getPosition(OTabbedPaneUI.this.rects[i])
                                    - leadingTabPosition);
                }
            }
        }

        /**
         * Center the tabs in the tab area.
         * @param tabCount the number of tabs.
         * @param totalLength the total length available of the tabs.
         * @param tabAreaLength the total length available.
         */
        protected void centerTabs(int tabCount, int totalLength, int tabAreaLength) {
            Insets tabAreaInsets = OTabbedPaneUI.this.getTabAreaInsets(OTabbedPaneUI.this.tabPlacement);
            Point corner = new Point(OTabbedPaneUI.this.tabAreaRect.x + tabAreaInsets.left,
                    OTabbedPaneUI.this.tabAreaRect.y + tabAreaInsets.top);
            int startPosition = OTabbedPaneUI.this.orientation.getPosition(corner);
            int offset = OTabbedPaneUI.this.orientation.getOrthogonalOffset(corner);
            int thickness = (OTabbedPaneUI.this.orientation == ControlOrientation.HORIZONTAL)
                    ? OTabbedPaneUI.this.maxTabHeight : OTabbedPaneUI.this.maxTabWidth;
            int delta = (-(tabAreaLength - totalLength) / 2) - startPosition;

            for (int i = OTabbedPaneUI.this.leadingTabIndex; i <= OTabbedPaneUI.this.trailingTabIndex; i++) {
                int position = OTabbedPaneUI.this.orientation.getPosition(OTabbedPaneUI.this.rects[i]) - delta;
                int length = OTabbedPaneUI.this.orientation.getLength(OTabbedPaneUI.this.rects[i]);

                OTabbedPaneUI.this.rects[i]
                    .setBounds(OTabbedPaneUI.this.orientation.createBounds(position, offset, length, thickness));
            }
        }

        /**
         * Fill out the visible tabs and scroll buttons to fit the available length.
         * @param tabCount the number of tabs.
         * @param totalLength the total length available of the tabs.
         * @param buttonLength the size of a scroll button.
         * @param tabAreaLength the total length available.
         */
        protected void resizeTabs(int tabCount, int totalLength, int buttonLength, int tabAreaLength) {
            // Subtract off the button length from the available length.
            if (OTabbedPaneUI.this.leadingTabIndex > 0) {
                tabAreaLength -= buttonLength;
            }

            if (OTabbedPaneUI.this.trailingTabIndex < (tabCount - 1)) {
                tabAreaLength -= buttonLength;
            }

            if ((OTabbedPaneUI.this.leadingTabIndex > 0) || (OTabbedPaneUI.this.trailingTabIndex < (tabCount - 1))) {
                tabAreaLength -= buttonLength;
            }

            Insets tabAreaInsets = OTabbedPaneUI.this.getTabAreaInsets(OTabbedPaneUI.this.tabPlacement);
            Point corner = new Point(OTabbedPaneUI.this.tabAreaRect.x + tabAreaInsets.left,
                    OTabbedPaneUI.this.tabAreaRect.y + tabAreaInsets.top);
            int startPosition = OTabbedPaneUI.this.orientation.getPosition(corner);
            int offset = OTabbedPaneUI.this.orientation.getOrthogonalOffset(corner);
            int thickness = (OTabbedPaneUI.this.orientation == ControlOrientation.HORIZONTAL)
                    ? OTabbedPaneUI.this.maxTabHeight : OTabbedPaneUI.this.maxTabWidth;

            // Fill the tabs to the available width.
            float multiplier = ((float) tabAreaLength / totalLength);

            for (int i = OTabbedPaneUI.this.leadingTabIndex; i <= OTabbedPaneUI.this.trailingTabIndex; i++) {
                int position = (i == OTabbedPaneUI.this.leadingTabIndex)
                        ? startPosition + (OTabbedPaneUI.this.leadingTabIndex > 0 ? buttonLength : 0)
                        : OTabbedPaneUI.this.orientation.getPosition(OTabbedPaneUI.this.rects[i - 1])
                                + OTabbedPaneUI.this.orientation.getLength(OTabbedPaneUI.this.rects[i - 1]);
                int length = (int) (OTabbedPaneUI.this.orientation.getLength(OTabbedPaneUI.this.rects[i]) * multiplier);

                OTabbedPaneUI.this.rects[i]
                    .setBounds(OTabbedPaneUI.this.orientation.createBounds(position, offset, length, thickness));
            }
        }

        /**
         * Set the bounds Rectangle for a scroll button.
         * @param child the scroll button.
         * @param visible whether the button is visible or not.
         * @param position the position from the start of the tab run.
         */
        protected void setScrollButtonPositions(Component child, boolean visible, int position) {
            if (visible) {
                child.setBounds(OTabbedPaneUI.this.orientation.createBounds(position,
                        OTabbedPaneUI.this.orientation
                            .getOrthogonalOffset(OTabbedPaneUI.this.rects[OTabbedPaneUI.this.leadingTabIndex]),
                        OTabbedPaneUI.this.orientation.getLength(child.getPreferredSize()),
                        OTabbedPaneUI.this.orientation
                            .getThickness(OTabbedPaneUI.this.rects[OTabbedPaneUI.this.leadingTabIndex])));
            }

            child.setEnabled(OTabbedPaneUI.this.tabPane.isEnabled());
            child.setVisible(visible);
        }

        /**
         * Flip the buttons right to left.
         * @param tabCount the number of tabs.
         * @param size the rectangle to fit them in.
         */
        protected void flipRightToLeft(int tabCount, Dimension size) {
            int rightMargin = size.width;

            for (int i = 0; i < tabCount; i++) {
                OTabbedPaneUI.this.rects[i].x = rightMargin - OTabbedPaneUI.this.rects[i].x
                        - OTabbedPaneUI.this.rects[i].width;
            }

            if (OTabbedPaneUI.this.scrollBackwardButton.isVisible()) {
                Rectangle b = OTabbedPaneUI.this.scrollBackwardButton.getBounds();

                OTabbedPaneUI.this.scrollBackwardButton.setLocation(rightMargin - b.x - b.width, b.y);
            }

            if (OTabbedPaneUI.this.scrollForwardButton.isVisible()) {
                Rectangle b = OTabbedPaneUI.this.scrollForwardButton.getBounds();

                OTabbedPaneUI.this.scrollForwardButton.setLocation(rightMargin - b.x - b.width, b.y);
            }
        }

        /**
         * Find the focus owner of the component.
         * @param visibleComponent the component.
         * @return the focus owner of the component.
         */
        protected Component findFocusOwner(Component visibleComponent) {
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

            // Verify that focusOwner is a descendant of visibleComponent.
            for (Component temp = focusOwner; temp != null; temp = (temp instanceof Window) ? null : temp.getParent()) {
                if (temp == visibleComponent) {
                    return focusOwner;
                }
            }

            return null;
        }

    }

    /**
     * Our Mouse Handler. Delegates to the basic UI's mouse handler if we don't need to handle an event.
     */
    public class OntimizeTabbedPaneMouseHandler extends MouseAdapter {

        /** Current mouse x coordinate. */
        protected transient int currentMouseX;

        /** Current mouse y coordinate. */
        protected transient int currentMouseY;

        protected MouseListener delegate;

        protected MouseMotionListener delegate2;

        /**
         * Creates a new OntimizeTabbedPaneMouseHandler object.
         * @param originalMouseListener the original mouse handler.
         */
        public OntimizeTabbedPaneMouseHandler(MouseListener originalMouseListener) {
            this.delegate = originalMouseListener;
            this.delegate2 = (MouseMotionListener) originalMouseListener;

            OTabbedPaneUI.this.closeButtonHoverIndex = -1;
            OTabbedPaneUI.this.closeButtonArmedIndex = -1;
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            this.delegate.mouseClicked(e);
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            this.delegate.mouseEntered(e);
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseExited(MouseEvent e) {
            this.delegate.mouseExited(e);
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            int oldHoverIndex = OTabbedPaneUI.this.closeButtonHoverIndex;

            // Test for mouse position and set hover index.
            this.currentMouseX = e.getX();
            this.currentMouseY = e.getY();

            OTabbedPaneUI.this.isOverCloseButton(this.currentMouseX, this.currentMouseY);

            if (oldHoverIndex != OTabbedPaneUI.this.closeButtonHoverIndex) {
                OTabbedPaneUI.this.tabPane.repaint();
                return;
            }

            this.delegate2.mouseMoved(e);
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            this.currentMouseX = e.getX();
            this.currentMouseY = e.getY();

            if ((OTabbedPaneUI.this.closeButtonArmedIndex != -1)
                    && !OTabbedPaneUI.this.isOverCloseButton(this.currentMouseX, this.currentMouseY)) {
                // isOverCloseButton resets closeButtonArmedIndex.
                OTabbedPaneUI.this.tabPane.repaint();
            }
        }

        /**
         * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
         */
        @Override
        public void mousePressed(MouseEvent e) {
            if (!OTabbedPaneUI.this.tabPane.isEnabled()) {
                return;
            }

            if (!SwingUtilities.isLeftMouseButton(e) || !OTabbedPaneUI.this.tabPane.isEnabled()) {
                return;
            }

            int tabIndex = OTabbedPaneUI.this.tabForCoordinate(OTabbedPaneUI.this.tabPane, e.getX(), e.getY());

            this.currentMouseX = e.getX();
            this.currentMouseY = e.getY();

            if (OTabbedPaneUI.this.isOverCloseButton(this.currentMouseX, this.currentMouseY)) {
                OTabbedPaneUI.this.closeButtonArmedIndex = tabIndex;
                OTabbedPaneUI.this.tabPane.repaint();
                return;
            } else if (OTabbedPaneUI.this.closeButtonArmedIndex != -1) {
                // isOverCloseButton resets closeButtonArmedIndex.
                OTabbedPaneUI.this.tabPane.repaint();
                return;
            }

            if ((tabIndex >= 0) && OTabbedPaneUI.this.tabPane.isEnabledAt(tabIndex)) {
                if (tabIndex == OTabbedPaneUI.this.tabPane.getSelectedIndex()) {
                    // Clicking on selected tab
                    OTabbedPaneUI.this.selectedTabIsPressed = true;

                    // TODO need to just repaint the tab area!
                    OTabbedPaneUI.this.tabPane.repaint();
                }
            }

            // forward the event (this will set the selected index, or none
            // at all
            this.delegate.mousePressed(e);
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            if (OTabbedPaneUI.this.closeButtonArmedIndex != -1) {
                if (OTabbedPaneUI.this.isOverCloseButton(this.currentMouseX, this.currentMouseY)) {
                    OTabbedPaneUI.this.doClose(OTabbedPaneUI.this.closeButtonArmedIndex);
                }

                OTabbedPaneUI.this.closeButtonArmedIndex = -1;

                OTabbedPaneUI.this.tabPane.repaint();
            } else if (OTabbedPaneUI.this.selectedTabIsPressed) {
                OTabbedPaneUI.this.selectedTabIsPressed = false;

                // TODO need to just repaint the tab area!
                OTabbedPaneUI.this.tabPane.repaint();
            }

            // forward the event
            this.delegate.mouseReleased(e);

            // hack: The super method *should* be setting the mouse-over
            // property correctly here, but it doesn't. That is, when the
            // mouse is released, whatever tab is below the released mouse
            // should be in rollover state. But, if you select a tab and
            // don't move the mouse, this doesn't happen. Hence, forwarding
            // the event.
            this.delegate2.mouseMoved(e);
        }

    }

    public class OntimizeTabbedPaneChangeHandler implements ChangeListener {

        protected ChangeListener delegate;

        public OntimizeTabbedPaneChangeHandler(ChangeListener listener) {
            this.delegate = listener;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            this.delegate.stateChanged(e);
        }

    }

}
