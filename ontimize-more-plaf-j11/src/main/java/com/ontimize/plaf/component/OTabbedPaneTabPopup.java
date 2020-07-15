package com.ontimize.plaf.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.accessibility.AccessibleContext;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.TabbedPaneUI;

/**
 * This is a basic implementation of the <code>TabbedPanePopup</code> interface.
 *
 * This class represents the ui for the popup portion of the tabbed pane.
 * <p>
 */
public class OTabbedPaneTabPopup extends JPopupMenu implements TabbedPaneTabPopup {

    // An empty ListMode, this is used when the UI changes to allow
    // the JList to be gc'ed.
    private static class EmptyListModelClass<String> implements ListModel<String>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public String getElementAt(int index) {
            return null;
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }

    };

    static final ListModel<String> EmptyListModel = new EmptyListModelClass<String>();

    public static Border O_LIST_BORDER = new LineBorder(new Color(0x8ca0ad), 2);

    protected JTabbedPane tabbedPane;

    /**
     * This protected field is implementation specific. Do not access directly or override. Use the
     * accessor methods instead.
     *
     * @see #getList
     * @see #createList
     */
    protected JList list;

    /**
     * This protected field is implementation specific. Do not access directly or override. Use the
     * create method instead
     *
     * @see #createScroller
     */
    protected JScrollPane scroller;

    /**
     * As of Java 2 platform v1.4 this previously undocumented field is no longer used.
     */
    protected boolean valueIsAdjusting = false;

    // Listeners that are required by the TabbedPanePopup interface

    /**
     * Implementation of all the listener classes.
     */
    private Handler handler;

    /**
     * This protected field is implementation specific. Do not access directly or override. Use the
     * accessor or create methods instead.
     *
     * @see #getMouseMotionListener
     * @see #createMouseMotionListener
     */
    protected MouseMotionListener mouseMotionListener;

    /**
     * This protected field is implementation specific. Do not access directly or override. Use the
     * accessor or create methods instead.
     *
     * @see #getMouseListener
     * @see #createMouseListener
     */
    protected MouseListener mouseListener;

    /**
     * This protected field is implementation specific. Do not access directly or override. Use the
     * accessor or create methods instead.
     *
     * @see #getKeyListener
     * @see #createKeyListener
     */
    protected KeyListener keyListener;

    /**
     * This protected field is implementation specific. Do not access directly or override. Use the
     * create method instead.
     *
     * @see #createListSelectionListener
     */
    protected ListSelectionListener listSelectionListener;

    // Listeners that are attached to the list
    /**
     * This protected field is implementation specific. Do not access directly or override. Use the
     * create method instead.
     *
     * @see #createListMouseListener
     */
    protected MouseListener listMouseListener;

    /**
     * This protected field is implementation specific. Do not access directly or override. Use the
     * create method instead
     *
     * @see #createListMouseMotionListener
     */
    protected MouseMotionListener listMouseMotionListener;

    // Added to the tabbed pane for bound properties
    /**
     * This protected field is implementation specific. Do not access directly or override. Use the
     * create method instead
     *
     * @see #createPropertyChangeListener
     */
    protected PropertyChangeListener propertyChangeListener;

    // Added to the tabbed pane model
    /**
     * This protected field is implementation specific. Do not access directly or override. Use the
     * create method instead
     *
     * @see #createListDataListener
     */
    protected ListDataListener listDataListener;

    /**
     * This protected field is implementation specific. Do not access directly or override. Use the
     * create method instead
     *
     * @see #createItemListener
     */
    protected ItemListener itemListener;

    protected ContainerListener containerListener;

    /**
     * This protected field is implementation specific. Do not access directly or override.
     */
    protected Timer autoscrollTimer;

    protected boolean hasEntered = false;

    protected boolean isAutoScrolling = false;

    protected int scrollDirection = OTabbedPaneTabPopup.SCROLL_UP;

    protected static final int SCROLL_UP = 0;

    protected static final int SCROLL_DOWN = 1;

    // ========================================
    // begin TabbedPanePopup method implementations
    //

    /**
     * Implementation of TabbedPanePopup.show().
     */
    @Override
    public void show() {
        this.setListSelection(this.tabbedPane.getSelectedIndex());
        Point location = this.getPopupLocation();
        this.show(this.tabbedPane, location.x, location.y);
    }

    /**
     * Implementation of TabbedPanePopup.hide().
     */
    @Override
    public void hide() {
        MenuSelectionManager manager = MenuSelectionManager.defaultManager();
        MenuElement[] selection = manager.getSelectedPath();
        for (int i = 0; i < selection.length; i++) {
            if (selection[i] == this) {
                manager.clearSelectedPath();
                break;
            }
        }
        if (selection.length > 0) {
            this.tabbedPane.repaint();
        }
    }

    /**
     * Implementation of TabbedPaneTabPopup.getList().
     */
    @Override
    public JList getList() {
        return this.list;
    }

    /**
     * Implementation of TabbedPanePopup.getMouseListener().
     * @return a <code>MouseListener</code> or null
     * @see TabbedPaneTabPopup#getMouseListener
     */
    @Override
    public MouseListener getMouseListener() {
        if (this.mouseListener == null) {
            this.mouseListener = this.createMouseListener();
        }
        return this.mouseListener;
    }

    /**
     * Implementation of TabbedPanePopup.getMouseMotionListener().
     * @return a <code>MouseMotionListener</code> or null
     * @see TabbedPaneTabPopup#getMouseMotionListener
     */
    @Override
    public MouseMotionListener getMouseMotionListener() {
        if (this.mouseMotionListener == null) {
            this.mouseMotionListener = this.createMouseMotionListener();
        }
        return this.mouseMotionListener;
    }

    public ContainerListener getContainerListener() {
        if (this.containerListener == null) {
            this.containerListener = this.createContainerListener();
        }
        return this.containerListener;
    }

    /**
     * Called when the UI is uninstalling. Since this popup isn't in the component tree, it won't get
     * it's uninstallUI() called. It removes the listeners that were added in addTabbedPaneListeners().
     */
    @Override
    public void uninstallingUI() {
        if (this.propertyChangeListener != null) {
            this.tabbedPane.removePropertyChangeListener(this.propertyChangeListener);
        }
        this.uninstallKeyboardActions();
        this.uninstallListListeners();
        // We do this, otherwise the listener the ui installs on
        // the model will keep a reference to the list, causing the list (and
        // us) to never get gced.
        this.list.setModel(OTabbedPaneTabPopup.EmptyListModel);
    }

    //
    // end TabbedPanePopup method implementations
    // ======================================

    protected void uninstallKeyboardActions() {
    }

    // ===================================================================
    // begin Initialization routines
    //
    public OTabbedPaneTabPopup(JTabbedPane tabbedPane) {
        super();
        this.setName("ComboPopup.popup");
        this.tabbedPane = tabbedPane;

        this.setLightWeightPopupEnabled(false);

        // UI construction of the popup.
        this.list = this.createList();
        this.list.setName("ComboBox.list");
        this.configureList();
        this.scroller = this.createScroller();
        this.scroller.setName("ComboBox.scrollPane");
        this.configureScroller();
        this.configurePopup();

        this.installTabbedPaneListeners();
    }

    // Overriden PopupMenuListener notification methods to inform tabbed pane
    // PopupMenuListeners.

    @Override
    protected void firePopupMenuWillBecomeVisible() {
        super.firePopupMenuWillBecomeVisible();
    }

    @Override
    protected void firePopupMenuWillBecomeInvisible() {
        super.firePopupMenuWillBecomeInvisible();
    }

    @Override
    protected void firePopupMenuCanceled() {
        super.firePopupMenuCanceled();
    }

    /**
     * Creates a listener that will watch for mouse-press and release events on the tabbed pane.
     *
     * <strong>Warning:</strong> When overriding this method, make sure to maintain the existing
     * behavior.
     * @return a <code>MouseListener</code> which will be added to the tabbed pane or null
     */
    protected MouseListener createMouseListener() {
        return this.getHandler();
    }

    /**
     * Creates the mouse motion listener which will be added to the tabbed pane.
     *
     * <strong>Warning:</strong> When overriding this method, make sure to maintain the existing
     * behavior.
     * @return a <code>MouseMotionListener</code> which will be added to the tabbed pane or null
     */
    protected MouseMotionListener createMouseMotionListener() {
        return this.getHandler();
    }

    /**
     * Creates a list selection listener that watches for selection changes in the popup's list. If this
     * method returns null then it will not be added to the popup list.
     * @return an instance of a <code>ListSelectionListener</code> or null
     */
    protected ListSelectionListener createListSelectionListener() {
        return null;
    }

    /**
     * Creates a list data listener which will be added to the <code>ComboBoxModel</code>. If this
     * method returns null then it will not be added to the tabbed pane model.
     * @return an instance of a <code>ListDataListener</code> or null
     */
    protected ListDataListener createListDataListener() {
        return null;
    }

    /**
     * Creates a mouse listener that watches for mouse events in the popup's list. If this method
     * returns null then it will not be added to the combo box.
     * @return an instance of a <code>MouseListener</code> or null
     */
    protected MouseListener createListMouseListener() {
        return this.getHandler();
    }

    /**
     * Creates a mouse motion listener that watches for mouse motion events in the popup's list. If this
     * method returns null then it will not be added to the tabbed pane.
     * @return an instance of a <code>MouseMotionListener</code> or null
     */
    protected MouseMotionListener createListMouseMotionListener() {
        return this.getHandler();
    }

    /**
     * Creates a <code>PropertyChangeListener</code> which will be added to the tabbed pane. If this
     * method returns null then it will not be added to the tabbed pane.
     * @return an instance of a <code>PropertyChangeListener</code> or null
     */
    protected PropertyChangeListener createPropertyChangeListener() {
        return this.getHandler();
    }

    /**
     * Creates an <code>ContainerListener</code> which will be added to the tabbed pane. If this method
     * returns null then it will not be added to the tabbed pane.
     * <p>
     * Subclasses may override this method to return instances of their own ContainerEvent handlers.
     * @return an instance of an <code>ContainerListener</code> or null
     */
    protected ContainerListener createContainerListener() {
        return this.getHandler();
    }

    private Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    /**
     * Creates the JList used in the popup to display the items in the tabbed pane model. This method is
     * called when the UI class is created.
     * @return a <code>JList</code> used to display the tabbed pane items
     */
    protected JList createList() {
        DefaultListModel<String> model = new javax.swing.DefaultListModel<String>();
        this.populateListModel(model);
        return new JList(model);
    }

    protected void populateListModel(DefaultListModel<String> model) {
        int count = this.tabbedPane.getTabCount();
        for (int i = 0; i < count; i++) {
            model.addElement(this.tabbedPane.getTitleAt(i));
        }
    }

    /**
     * Configures the list which is used to hold the tabbed pane items in the popup. This method is
     * called when the UI class is created.
     *
     * @see #createList
     */
    protected void configureList() {
        this.list.setFont(this.tabbedPane.getFont());
        this.list.setForeground(this.tabbedPane.getForeground());
        this.list.setBackground(this.tabbedPane.getBackground());
        this.list.setSelectionForeground(UIManager.getColor("ComboBox.selectionForeground"));
        this.list.setSelectionBackground(UIManager.getColor("ComboBox.selectionBackground"));
        this.list.setBorder(null);
        // TODO this.list.setCellRenderer(this.tabbedPane.getRenderer());
        this.list.setFocusable(false);
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setListSelection(this.tabbedPane.getSelectedIndex());
        this.installListListeners();
    }

    /**
     * Adds the listeners to the list control.
     */
    protected void installListListeners() {
        if ((this.listMouseListener = this.createListMouseListener()) != null) {
            this.list.addMouseListener(this.listMouseListener);
        }
        if ((this.listMouseMotionListener = this.createListMouseMotionListener()) != null) {
            this.list.addMouseMotionListener(this.listMouseMotionListener);
        }
        if ((this.listSelectionListener = this.createListSelectionListener()) != null) {
            this.list.addListSelectionListener(this.listSelectionListener);
        }
    }

    void uninstallListListeners() {
        if (this.listMouseListener != null) {
            this.list.removeMouseListener(this.listMouseListener);
            this.listMouseListener = null;
        }
        if (this.listMouseMotionListener != null) {
            this.list.removeMouseMotionListener(this.listMouseMotionListener);
            this.listMouseMotionListener = null;
        }
        if (this.listSelectionListener != null) {
            this.list.removeListSelectionListener(this.listSelectionListener);
            this.listSelectionListener = null;
        }
        this.handler = null;
    }

    /**
     * Creates the scroll pane which houses the scrollable list.
     */
    protected JScrollPane createScroller() {
        JScrollPane sp = new JScrollPane(this.list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setHorizontalScrollBar(null);
        return sp;
    }

    /**
     * Configures the scrollable portion which holds the list within the combo box popup. This method is
     * called when the UI class is created.
     */
    protected void configureScroller() {
        this.scroller.setFocusable(false);
        this.scroller.getVerticalScrollBar().setFocusable(false);
        this.scroller.setBorder(null);
    }

    /**
     * Configures the popup portion of the tabbed pane. This method is called when the UI class is
     * created.
     */
    protected void configurePopup() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorderPainted(true);
        this.setBorder(OTabbedPaneTabPopup.O_LIST_BORDER);
        this.setOpaque(false);
        this.add(this.scroller);
        this.setDoubleBuffered(true);
        this.setFocusable(false);
    }

    /**
     * This method adds the necessary listeners to the JComboBox.
     */
    protected void installTabbedPaneListeners() {
        if ((this.propertyChangeListener = this.createPropertyChangeListener()) != null) {
            this.tabbedPane.addPropertyChangeListener(this.propertyChangeListener);
        }
        if ((this.containerListener = this.createContainerListener()) != null) {
            this.tabbedPane.addContainerListener(this.containerListener);
        }
    }

    //
    // end Initialization routines
    // =================================================================

    // ===================================================================
    // begin Event Listenters
    //

    /**
     * A listener to be registered upon the tabbed pane (<em>not</em> its popup menu) to handle mouse
     * events that affect the state of the popup menu. The main purpose of this listener is to make the
     * popup menu appear and disappear. This listener also helps with click-and-drag scenarios by
     * setting the selection if the mouse was released over the list during a drag.
     *
     * <p>
     * <strong>Warning:</strong> We recommend that you <em>not</em> create subclasses of this class. If
     * you absolutely must create a subclass, be sure to invoke the superclass version of each method.
     *
     * @see OTabbedPaneTabPopup#createMouseListener
     */
    protected class InvocationMouseHandler extends MouseAdapter {

        /**
         * Responds to mouse-pressed events on the tabbed pane.
         * @param e the mouse-press event to be handled
         */
        @Override
        public void mousePressed(MouseEvent e) {
            OTabbedPaneTabPopup.this.getHandler().mousePressed(e);
        }

        /**
         * Responds to the user terminating a click or drag that began on the tabbed pane.
         * @param e the mouse-release event to be handled
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            OTabbedPaneTabPopup.this.getHandler().mouseReleased(e);
        }

    }

    /**
     * This listener watches for dragging and updates the current selection in the list if it is
     * dragging over the list.
     */
    protected class InvocationMouseMotionHandler extends MouseMotionAdapter {

        @Override
        public void mouseDragged(MouseEvent e) {
            OTabbedPaneTabPopup.this.getHandler().mouseDragged(e);
        }

    }

    /**
     * As of Java 2 platform v 1.4, this class is now obsolete and is only included for backwards API
     * compatibility. Do not instantiate or subclass.
     * <p>
     * All the functionality of this class has been included in BasicComboBoxUI ActionMap/InputMap
     * methods.
     */
    public class InvocationKeyHandler extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
        }

    }

    /**
     * As of Java 2 platform v 1.4, this class is now obsolete, doesn't do anything, and is only
     * included for backwards API compatibility. Do not call or override.
     */
    protected class ListSelectionHandler implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
        }

    }

    /**
     * This listener hides the popup when the mouse is released in the list.
     */
    protected class ListMouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent anEvent) {
            OTabbedPaneTabPopup.this.getHandler().mouseReleased(anEvent);
        }

    }

    /**
     * This listener changes the selected item as you move the mouse over the list. The selection change
     * is not committed to the model, this is for user feedback only.
     */
    protected class ListMouseMotionHandler extends MouseMotionAdapter {

        @Override
        public void mouseMoved(MouseEvent anEvent) {
            OTabbedPaneTabPopup.this.getHandler().mouseMoved(anEvent);
        }

    }

    /**
     * This listener watches for components added to the tabbedPane.
     *
     */
    protected class ContainerHandler implements ContainerListener {

        @Override
        public void componentAdded(ContainerEvent e) {
            OTabbedPaneTabPopup.this.getHandler().componentAdded(e);
        }

        @Override
        public void componentRemoved(ContainerEvent e) {
            OTabbedPaneTabPopup.this.getHandler().componentRemoved(e);
        }

    }

    /**
     * This listener watches for bound properties that have changed in the combo box.
     * <p>
     * Subclasses which wish to listen to tabbed pane property changes should call the superclass
     * methods to ensure that the combo popup correctly handles property changes.
     *
     * @see #createPropertyChangeListener
     */
    protected class PropertyChangeHandler implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            OTabbedPaneTabPopup.this.getHandler().propertyChange(e);
        }

    }

    private class AutoScrollActionHandler implements ActionListener {

        private final int direction;

        AutoScrollActionHandler(int direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (this.direction == OTabbedPaneTabPopup.SCROLL_UP) {
                OTabbedPaneTabPopup.this.autoScrollUp();
            } else {
                OTabbedPaneTabPopup.this.autoScrollDown();
            }
        }

    }

    private class Handler
            implements ContainerListener, MouseListener, MouseMotionListener, PropertyChangeListener, Serializable {

        private static final long serialVersionUID = 1L;

        //
        // MouseListener
        // NOTE: this is added to both the JList and JTabbedPane
        //
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == OTabbedPaneTabPopup.this.list) {
                return;
            }
            if (!SwingUtilities.isLeftMouseButton(e) || !OTabbedPaneTabPopup.this.tabbedPane.isEnabled()) {
                return;
            }

            if (OTabbedPaneTabPopup.this.tabbedPane.isRequestFocusEnabled()) {
                OTabbedPaneTabPopup.this.tabbedPane.requestFocus();
            }
            OTabbedPaneTabPopup.this.togglePopup();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getSource() == OTabbedPaneTabPopup.this.list) {
                if (OTabbedPaneTabPopup.this.list.getModel().getSize() > 0) {
                    // JList mouse listener
                    OTabbedPaneTabPopup.this.tabbedPane
                        .setSelectedIndex(OTabbedPaneTabPopup.this.list.getSelectedIndex());
                }
                TabbedPaneUI ui = OTabbedPaneTabPopup.this.tabbedPane.getUI();
                if (ui instanceof TabbedPanePopup) {
                    ((TabbedPanePopup) ui).setPopupVisible(false);
                }
                return;
            }
            // JComboBox mouse listener
            Component source = (Component) e.getSource();
            Dimension size = source.getSize();
            Rectangle bounds = new Rectangle(0, 0, size.width - 1, size.height - 1);
            if (!bounds.contains(e.getPoint())) {
                MouseEvent newEvent = OTabbedPaneTabPopup.this.convertMouseEvent(e);
                Point location = newEvent.getPoint();
                Rectangle r = new Rectangle();
                OTabbedPaneTabPopup.this.list.computeVisibleRect(r);
                if (r.contains(location)) {
                    OTabbedPaneTabPopup.this.tabbedPane
                        .setSelectedIndex(OTabbedPaneTabPopup.this.list.getSelectedIndex());
                }
                TabbedPaneUI ui = OTabbedPaneTabPopup.this.tabbedPane.getUI();
                if (ui instanceof TabbedPanePopup) {
                    ((TabbedPanePopup) ui).setPopupVisible(false);
                }
            }
            OTabbedPaneTabPopup.this.hasEntered = false;
            OTabbedPaneTabPopup.this.stopAutoScrolling();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        //
        // MouseMotionListener:
        // NOTE: this is added to both the List and TabbedPane
        //
        @Override
        public void mouseMoved(MouseEvent anEvent) {
            if (anEvent.getSource() == OTabbedPaneTabPopup.this.list) {
                Point location = anEvent.getPoint();
                Rectangle r = new Rectangle();
                OTabbedPaneTabPopup.this.list.computeVisibleRect(r);
                if (r.contains(location)) {
                    OTabbedPaneTabPopup.this.updateListBoxSelectionForEvent(anEvent, false);
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (e.getSource() == OTabbedPaneTabPopup.this.list) {
                return;
            }
            if (OTabbedPaneTabPopup.this.isVisible()) {
                MouseEvent newEvent = OTabbedPaneTabPopup.this.convertMouseEvent(e);
                Rectangle r = new Rectangle();
                OTabbedPaneTabPopup.this.list.computeVisibleRect(r);

                if ((newEvent.getPoint().y >= r.y) && (newEvent.getPoint().y <= ((r.y + r.height) - 1))) {
                    OTabbedPaneTabPopup.this.hasEntered = true;
                    if (OTabbedPaneTabPopup.this.isAutoScrolling) {
                        OTabbedPaneTabPopup.this.stopAutoScrolling();
                    }
                    Point location = newEvent.getPoint();
                    if (r.contains(location)) {
                        OTabbedPaneTabPopup.this.updateListBoxSelectionForEvent(newEvent, false);
                    }
                } else {
                    if (OTabbedPaneTabPopup.this.hasEntered) {
                        int directionToScroll = newEvent.getPoint().y < r.y ? OTabbedPaneTabPopup.SCROLL_UP
                                : OTabbedPaneTabPopup.SCROLL_DOWN;
                        if (OTabbedPaneTabPopup.this.isAutoScrolling
                                && (OTabbedPaneTabPopup.this.scrollDirection != directionToScroll)) {
                            OTabbedPaneTabPopup.this.stopAutoScrolling();
                            OTabbedPaneTabPopup.this.startAutoScrolling(directionToScroll);
                        } else if (!OTabbedPaneTabPopup.this.isAutoScrolling) {
                            OTabbedPaneTabPopup.this.startAutoScrolling(directionToScroll);
                        }
                    } else {
                        if (e.getPoint().y < 0) {
                            OTabbedPaneTabPopup.this.hasEntered = true;
                            OTabbedPaneTabPopup.this.startAutoScrolling(OTabbedPaneTabPopup.SCROLL_UP);
                        }
                    }
                }
            }
        }

        //
        // PropertyChangeListener
        //
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if ("indexForTitle".equals(e.getPropertyName())) {
                Object oIndex = e.getNewValue();
                if (oIndex instanceof Integer) {
                    int index = ((Integer) oIndex).intValue();
                    String element = OTabbedPaneTabPopup.this.tabbedPane.getTitleAt(index);
                    ((DefaultListModel) OTabbedPaneTabPopup.this.getList().getModel()).setElementAt(element, index);
                }
            }
        }

        //
        // ContainerListener
        //
        @Override
        public void componentAdded(ContainerEvent e) {

            if (e.getChild() instanceof OntimizeArrowButton) {
                return;
            }

            JTabbedPane tabPane = (JTabbedPane) e.getContainer();
            int index = tabPane.indexOfComponent(e.getChild());
            if (index >= 0) {
                String title = tabPane.getTitleAt(index);
                if (title != null) {
                    ((DefaultListModel) OTabbedPaneTabPopup.this.list.getModel()).add(index, title);
                }
            }

        }

        @Override
        public void componentRemoved(ContainerEvent e) {
            if (e.getChild() instanceof OntimizeArrowButton) {
                return;
            }

            JTabbedPane tabPane = (JTabbedPane) e.getContainer();
            if (OTabbedPaneTabPopup.this.getList().getModel() instanceof DefaultListModel) {
                ((DefaultListModel) OTabbedPaneTabPopup.this.getList().getModel()).clear();
                OTabbedPaneTabPopup.this
                    .populateListModel(((DefaultListModel) OTabbedPaneTabPopup.this.getList().getModel()));
            }
        }

    }

    //
    // end Event Listeners
    // =================================================================

    /**
     * Overridden to unconditionally return false.
     */
    @Override
    public boolean isFocusTraversable() {
        return false;
    }

    // ===================================================================
    // begin Autoscroll methods
    //

    /**
     * This protected method is implementation specific and should be private. do not call or override.
     */
    protected void startAutoScrolling(int direction) {
        // XXX - should be a private method within InvocationMouseMotionHandler
        // if possible.
        if (this.isAutoScrolling) {
            this.autoscrollTimer.stop();
        }

        this.isAutoScrolling = true;

        if (direction == OTabbedPaneTabPopup.SCROLL_UP) {
            this.scrollDirection = OTabbedPaneTabPopup.SCROLL_UP;
            Point convertedPoint = SwingUtilities.convertPoint(this.scroller, new Point(1, 1), this.list);
            int top = this.list.locationToIndex(convertedPoint);
            this.list.setSelectedIndex(top);

            this.autoscrollTimer = new Timer(100, new AutoScrollActionHandler(OTabbedPaneTabPopup.SCROLL_UP));
        } else if (direction == OTabbedPaneTabPopup.SCROLL_DOWN) {
            this.scrollDirection = OTabbedPaneTabPopup.SCROLL_DOWN;
            Dimension size = this.scroller.getSize();
            Point convertedPoint = SwingUtilities.convertPoint(this.scroller, new Point(1, (size.height - 1) - 2),
                    this.list);
            int bottom = this.list.locationToIndex(convertedPoint);
            this.list.setSelectedIndex(bottom);

            this.autoscrollTimer = new Timer(100, new AutoScrollActionHandler(OTabbedPaneTabPopup.SCROLL_DOWN));
        }
        this.autoscrollTimer.start();
    }

    /**
     * This protected method is implementation specific and should be private. do not call or override.
     */
    protected void stopAutoScrolling() {
        this.isAutoScrolling = false;

        if (this.autoscrollTimer != null) {
            this.autoscrollTimer.stop();
            this.autoscrollTimer = null;
        }
    }

    /**
     * This protected method is implementation specific and should be private. do not call or override.
     */
    protected void autoScrollUp() {
        int index = this.list.getSelectedIndex();
        if (index > 0) {
            this.list.setSelectedIndex(index - 1);
            this.list.ensureIndexIsVisible(index - 1);
        }
    }

    /**
     * This protected method is implementation specific and should be private. do not call or override.
     */
    protected void autoScrollDown() {
        int index = this.list.getSelectedIndex();
        int lastItem = this.list.getModel().getSize() - 1;
        if (index < lastItem) {
            this.list.setSelectedIndex(index + 1);
            this.list.ensureIndexIsVisible(index + 1);
        }
    }

    //
    // end Autoscroll methods
    // =================================================================

    // ===================================================================
    // begin Utility methods
    //

    /**
     * Gets the AccessibleContext associated with this OTabbedPaneTabPopup. The AccessibleContext will
     * have its parent set to the TabbedPane.
     * @return an AccessibleContext for the OTabbedPaneTabPopup
     * @since 1.5
     */
    @Override
    public AccessibleContext getAccessibleContext() {
        AccessibleContext context = super.getAccessibleContext();
        context.setAccessibleParent(this.tabbedPane);
        return context;
    }

    /**
     * This is is a utility method that helps event handlers figure out where to send the focus when the
     * popup is brought up. The standard implementation delegates the focus to the editor (if the tabbed
     * pane is editable) or to the JComboBox if it is not editable.
     */
    protected void delegateFocus(MouseEvent e) {
        if (this.tabbedPane.isRequestFocusEnabled()) {
            this.tabbedPane.requestFocus();
        }
    }

    /**
     * Makes the popup visible if it is hidden and makes it hidden if it is visible.
     */
    protected void togglePopup() {
        if (this.isVisible()) {
            this.hide();
        } else {
            this.show();
        }
    }

    /**
     * Sets the list selection index to the selectedIndex. This method is used to synchronize the list
     * selection with the tabbed pane selection.
     * @param selectedIndex the index to set the list
     */
    private void setListSelection(int selectedIndex) {
        if (selectedIndex == -1) {
            this.list.clearSelection();
        } else {
            this.list.setSelectedIndex(selectedIndex);
            this.list.ensureIndexIsVisible(selectedIndex);
        }
    }

    protected MouseEvent convertMouseEvent(MouseEvent e) {
        Point convertedPoint = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), this.list);
        MouseEvent newEvent = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(),
                convertedPoint.x, convertedPoint.y, e.getXOnScreen(),
                e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), MouseEvent.NOBUTTON);
        return newEvent;
    }

    /**
     * Retrieves the height of the popup based on the current ListCellRenderer and the maximum row
     * count.
     */
    protected int getPopupHeightForRowCount(int maxRowCount) {
        // Set the cached value of the minimum row count
        int minRowCount = Math.min(maxRowCount, this.tabbedPane.getTabCount());
        int height = 0;
        ListCellRenderer renderer = this.list.getCellRenderer();
        Object value = null;

        for (int i = 0; i < minRowCount; ++i) {
            value = this.list.getModel().getElementAt(i);
            Component c = renderer.getListCellRendererComponent(this.list, value, i, false, false);
            height += c.getPreferredSize().height;
        }

        if (height == 0) {
            height = OTabbedPaneTabPopup.this.list.getPreferredSize().height;
        }

        Border border = this.scroller.getViewportBorder();
        if (border != null) {
            Insets insets = border.getBorderInsets(null);
            height += insets.top + insets.bottom;
        }

        border = this.scroller.getBorder();
        if (border != null) {
            Insets insets = border.getBorderInsets(null);
            height += insets.top + insets.bottom;
        }

        return height;
    }

    /**
     * Calculate the placement and size of the popup portion of the tabbed pane based on the tabbed pane
     * location and the enclosing screen bounds. If no transformations are required, then the returned
     * rectangle will have the same values as the parameters.
     * @param px starting x location
     * @param py starting y location
     * @param pw starting width
     * @param ph starting height
     * @return a rectangle which represents the placement and size of the popup
     */
    protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Rectangle screenBounds;

        // Calculate the desktop dimensions relative to the tabbed pane.
        GraphicsConfiguration gc = this.tabbedPane.getGraphicsConfiguration();
        Point p = new Point();
        SwingUtilities.convertPointFromScreen(p, this.tabbedPane);
        if (gc != null) {
            Insets screenInsets = toolkit.getScreenInsets(gc);
            screenBounds = gc.getBounds();
            screenBounds.width -= (screenInsets.left + screenInsets.right);
            screenBounds.height -= (screenInsets.top + screenInsets.bottom);
            screenBounds.x += (p.x + screenInsets.left);
            screenBounds.y += (p.y + screenInsets.top);
        } else {
            screenBounds = new Rectangle(p, toolkit.getScreenSize());
        }

        Rectangle rect = new Rectangle(px, py, pw, ph);
        if (((py + ph) > (screenBounds.y + screenBounds.height)) && (ph < screenBounds.height)) {
            rect.y = -rect.height;
        }
        return rect;
    }

    /**
     * Calculates the upper left location of the Popup.
     */
    private Point getPopupLocation() {
        Dimension popupSize = OTabbedPaneTabPopup.this.list.getPreferredSize();
        Insets insets = this.getInsets();

        // reduce the width of the scrollpane by the insets so that the popup
        // is the same width as the tabbed pane.
        popupSize.setSize(popupSize.width - (insets.right + insets.left),
                this.getPopupHeightForRowCount(this.tabbedPane.getTabCount()));
        Rectangle btnBounds = this.tabbedPane.getComponentAt(this.tabbedPane.getTabCount() - 1).getBounds();
        Rectangle tabPaneBounds = this.tabbedPane.getBounds();
        Rectangle popupBounds = this.computePopupBounds(
                tabPaneBounds.width - insets.left - insets.right - popupSize.width, btnBounds.y, popupSize.width,
                popupSize.height);
        Dimension scrollSize = popupBounds.getSize();
        Point popupLocation = popupBounds.getLocation();

        this.scroller.setMaximumSize(scrollSize);
        this.scroller.setPreferredSize(scrollSize);
        this.scroller.setMinimumSize(scrollSize);

        this.list.revalidate();

        return popupLocation;
    }

    /**
     * A utility method used by the event listeners. Given a mouse event, it changes the list selection
     * to the list item below the mouse.
     */
    protected void updateListBoxSelectionForEvent(MouseEvent anEvent, boolean shouldScroll) {
        // XXX - only seems to be called from this class. shouldScroll flag is
        // never true
        Point location = anEvent.getPoint();
        if (this.list == null) {
            return;
        }
        int index = this.list.locationToIndex(location);
        if (index == -1) {
            if (location.y < 0) {
                index = 0;
            } else {
                index = this.tabbedPane.getTabCount() - 1;
            }
        }
        if (this.list.getSelectedIndex() != index) {
            this.list.setSelectedIndex(index);
            if (shouldScroll) {
                this.list.ensureIndexIsVisible(index);
            }
        }
    }

    //
    // end Utility methods
    // =================================================================

}
