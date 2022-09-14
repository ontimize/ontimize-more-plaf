package com.ontimize.plaf.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

import sun.awt.SunToolkit;

import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.ui.ORootPaneUI;

public class OTitlePane extends JComponent {

    protected static final Border handyEmptyBorder = new EmptyBorder(0, 0, 0, 0);

    protected static final int IMAGE_HEIGHT = 16;

    protected static final int IMAGE_WIDTH = 16;

    /**
     * PropertyChangeListener added to the JRootPane.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * JMenuBar, typically renders the system menu items.
     */
    protected JMenuBar menuBar;

    /**
     * Action used to close the Window.
     */
    protected Action closeAction;

    /**
     * Action used to iconify the Frame.
     */
    protected Action iconifyAction;

    /**
     * Action to restore the Frame size.
     */
    protected Action restoreAction;

    /**
     * Action to restore the Frame size.
     */
    protected Action maximizeAction;

    /**
     * Button used to maximize or restore the Frame.
     */
    protected JButton toggleButton;

    /**
     * Button used to maximize or restore the Frame.
     */
    protected JButton iconifyButton;

    /**
     * Button used to maximize or restore the Frame.
     */
    protected JButton closeButton;

    /**
     * Icon used for toggleButton when window is normal size.
     */
    protected Icon maximizeIcon;

    /**
     * Icon used for toggleButton when window is maximized.
     */
    protected Icon minimizeIcon;

    /**
     * Image used for the system menu icon
     */
    protected Image systemIcon;

    protected int roundArc = 8;

    /**
     * Listens for changes in the state of the Window listener to update the state of the widgets.
     */
    protected WindowListener windowListener;

    /**
     * Window we're currently in.
     */
    protected Window window;

    /**
     * JRootPane rendering for.
     */
    protected final JRootPane rootPane;

    /**
     * Room remaining in title for bumps.
     */
    protected int buttonsWidth;

    /**
     * Buffered Frame.state property. As state isn't bound, this is kept to determine when to avoid
     * updating widgets.
     */
    protected int state;

    /**
     * OntimizeRootPaneUI that created us.
     */
    protected final ORootPaneUI rootPaneUI;

    protected JSeparator buttonSeparator;

    // Colors
    protected final Color inactiveBackground = UIManager.getColor("inactiveCaption");

    protected final Color inactiveForeground = UIManager.getColor("inactiveCaptionText");

    protected final Color inactiveLine = UIManager.getColor("RootPane.titlePane.line.background");

    protected final Color activeBumpsHighlight = MetalLookAndFeel.getPrimaryControlHighlight();

    protected final Color activeBumpsShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();

    protected Color activeBackground = null;

    protected Color activeForeground = null;

    protected Color activeLine = null;

    // // Bumps
    // protected MetalBumps activeBumps
    // = new MetalBumps( 0, 0,
    // activeBumpsHighlight,
    // activeBumpsShadow,
    // MetalLookAndFeel.getPrimaryControl() );
    // protected MetalBumps inactiveBumps
    // = new MetalBumps( 0, 0,
    // MetalLookAndFeel.getControlHighlight(),
    // MetalLookAndFeel.getControlDarkShadow(),
    // MetalLookAndFeel.getControl() );

    public OTitlePane(JRootPane root, ORootPaneUI ui) {
        this.rootPane = root;
        this.rootPaneUI = ui;

        this.setOpaque(false);
        this.state = -1;

        this.installSubcomponents();
        this.determineColors();
        this.determineArc();
        this.installDefaults();

        this.setLayout(this.createLayout());
    }

    /**
     * Uninstalls the necessary state.
     */
    protected void uninstall() {
        this.uninstallListeners();
        this.window = null;
        this.removeAll();
    }

    /**
     * Installs the necessary listeners.
     */
    protected void installListeners() {
        if (this.window != null) {
            this.windowListener = this.createWindowListener();
            this.window.addWindowListener(this.windowListener);
            this.propertyChangeListener = this.createWindowPropertyChangeListener();
            this.window.addPropertyChangeListener(this.propertyChangeListener);
        }
    }

    /**
     * Uninstalls the necessary listeners.
     */
    protected void uninstallListeners() {
        if (this.window != null) {
            this.window.removeWindowListener(this.windowListener);
            this.window.removePropertyChangeListener(this.propertyChangeListener);
        }
    }

    /**
     * Returns the <code>WindowListener</code> to add to the <code>Window</code> .
     */
    protected WindowListener createWindowListener() {
        return new WindowHandler();
    }

    /**
     * Returns the <code>PropertyChangeListener</code> to install on the <code>Window</code>.
     */
    protected PropertyChangeListener createWindowPropertyChangeListener() {
        return new PropertyChangeHandler();
    }

    /**
     * Returns the <code>JRootPane</code> this was created for.
     */
    @Override
    public JRootPane getRootPane() {
        return this.rootPane;
    }

    /**
     * Returns the decoration style of the <code>JRootPane</code>.
     */
    protected int getWindowDecorationStyle() {
        return this.getRootPane().getWindowDecorationStyle();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        this.uninstallListeners();

        this.window = SwingUtilities.getWindowAncestor(this);
        if (this.window != null) {
            if (this.window instanceof Frame) {
                this.setState(((Frame) this.window).getExtendedState());
            } else {
                this.setState(0);
            }
            this.setActive(this.window.isActive());
            this.installListeners();
            this.updateSystemIcon();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();

        this.uninstallListeners();
        this.window = null;
    }

    protected void insertMenu() {
        int decorationStyle = this.getWindowDecorationStyle();
        if (decorationStyle == JRootPane.FRAME) {
            this.remove(this.menuBar);
            this.remove(this.iconifyButton);
            this.remove(this.toggleButton);
            this.remove(this.closeButton);

            this.menuBar = this.rootPane.getJMenuBar();
            this.add(this.menuBar);
            this.add(this.iconifyButton);
            this.add(this.toggleButton);
            this.add(this.closeButton);
            this.revalidate();
        }
    }

    /**
     * Adds any sub-Components contained in the <code>MetalTitlePane</code>.
     */
    protected void installSubcomponents() {
        int decorationStyle = this.getWindowDecorationStyle();
        if (decorationStyle == JRootPane.FRAME) {
            this.createActions();
            this.menuBar = this.createMenuBar();
            if (this.menuBar != null) {
                this.add(this.menuBar);
            }
            this.createSeparator();
            this.add(this.buttonSeparator);
            this.createButtons();
            this.add(this.iconifyButton);
            this.add(this.toggleButton);
            this.add(this.closeButton);
        } else if ((decorationStyle == JRootPane.PLAIN_DIALOG) || (decorationStyle == JRootPane.INFORMATION_DIALOG)
                || (decorationStyle == JRootPane.ERROR_DIALOG)
                || (decorationStyle == JRootPane.COLOR_CHOOSER_DIALOG)
                || (decorationStyle == JRootPane.FILE_CHOOSER_DIALOG) || (decorationStyle == JRootPane.QUESTION_DIALOG)
                || (decorationStyle == JRootPane.WARNING_DIALOG)) {
            this.createActions();
            this.createSeparator();
            this.add(this.buttonSeparator);
            this.createButtons();
            this.add(this.closeButton);
        }
    }

    protected void determineArc() {
        Object arc = OntimizeLookAndFeel.lookup("RootPane.titlePane.arc");
        if (arc instanceof Integer) {
            this.roundArc = ((Integer) arc).intValue();
        }
    }

    /**
     * Determines the Colors to draw with.
     */
    protected void determineColors() {
        switch (this.getWindowDecorationStyle()) {
            case JRootPane.FRAME:
                this.activeBackground = UIManager.getColor("RootPane.titlePane.background");

                this.activeForeground = UIManager.getColor("activeCaptionText");

                this.activeLine = UIManager.getColor("RootPane.titlePane.line.background");

                break;
            case JRootPane.ERROR_DIALOG:
                this.activeBackground = UIManager.getColor("RootPane.titlePane.background");
                this.activeForeground = UIManager.getColor("OptionPane.errorDialog.titlePane.foreground");
                this.activeLine = UIManager.getColor("RootPane.titlePane.line.background");
                break;
            case JRootPane.QUESTION_DIALOG:
            case JRootPane.COLOR_CHOOSER_DIALOG:
            case JRootPane.FILE_CHOOSER_DIALOG:
                this.activeBackground = UIManager.getColor("RootPane.titlePane.background");
                this.activeForeground = UIManager.getColor("OptionPane.questionDialog.titlePane.foreground");
                this.activeLine = UIManager.getColor("RootPane.titlePane.line.background");
                break;
            case JRootPane.WARNING_DIALOG:
                this.activeBackground = UIManager.getColor("RootPane.titlePane.background");
                this.activeForeground = UIManager.getColor("OptionPane.warningDialog.titlePane.foreground");
                this.activeLine = UIManager.getColor("RootPane.titlePane.line.background");
                break;
            case JRootPane.PLAIN_DIALOG:
            case JRootPane.INFORMATION_DIALOG:
            default:
                this.activeBackground = UIManager.getColor("RootPane.titlePane.background");
                this.activeForeground = UIManager.getColor("activeCaptionText");
                this.activeLine = UIManager.getColor("RootPane.titlePane.line.background");
                break;
        }
        // activeBumps.setBumpColors(activeBumpsHighlight, activeBumpsShadow,
        // activeBackground);
    }

    /**
     * Installs the fonts and necessary properties on the MetalTitlePane.
     */
    protected void installDefaults() {
        this.setFont(UIManager.getFont("InternalFrame.titleFont", this.getLocale()));
    }

    /**
     * Uninstalls any previously installed UI values.
     */
    protected void uninstallDefaults() {
    }

    /**
     * Returns the <code>JMenuBar</code> displaying the appropriate system menu items.
     */
    protected JMenuBar createMenuBar() {
        // menuBar = new SystemMenuBar();
        // menuBar.setFocusable(false);
        // menuBar.setBorderPainted(true);
        // menuBar.add(createMenu());
        return this.menuBar;
    }

    /**
     * Closes the Window.
     */
    protected void close() {
        Window window = this.getWindow();

        if (window != null) {
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * Iconifies the Frame.
     */
    protected void iconify() {
        Frame frame = this.getFrame();
        if (frame != null) {
            frame.setExtendedState(this.state | Frame.ICONIFIED);
        }
    }

    /**
     * Maximizes the Frame.
     */
    protected void maximize() {
        Frame frame = this.getFrame();
        if (frame != null) {
            frame.setExtendedState(this.state | Frame.MAXIMIZED_BOTH);
        }
    }

    /**
     * Restores the Frame size.
     */
    protected void restore() {
        Frame frame = this.getFrame();

        if (frame == null) {
            return;
        }

        if ((this.state & Frame.ICONIFIED) != 0) {
            frame.setExtendedState(this.state & ~Frame.ICONIFIED);
        } else {
            frame.setExtendedState(this.state & ~Frame.MAXIMIZED_BOTH);
        }
    }

    /**
     * Create the <code>Action</code>s that get associated with the buttons and menu items.
     */
    protected void createActions() {
        this.closeAction = new CloseAction();
        if (this.getWindowDecorationStyle() == JRootPane.FRAME) {
            this.iconifyAction = new IconifyAction();
            this.restoreAction = new RestoreAction();
            this.maximizeAction = new MaximizeAction();
        }
    }

    /**
     * Returns the <code>JMenu</code> displaying the appropriate menu items for manipulating the Frame.
     */
    protected JMenu createMenu() {
        JMenu menu = new JMenu("");
        if (this.getWindowDecorationStyle() == JRootPane.FRAME) {
            this.addMenuItems(menu);
        }
        return menu;
    }

    /**
     * Adds the necessary <code>JMenuItem</code>s to the passed in menu.
     */
    protected void addMenuItems(JMenu menu) {
        // Locale locale = getRootPane().getLocale();
        // JMenuItem mi = menu.add(restoreAction);
        // // int mnemonic = MetalUtils.getInt("MetalTitlePane.restoreMnemonic",
        // -1);
        //
        // if (mnemonic != -1) {
        // mi.setMnemonic(mnemonic);
        // }
        //
        // mi = menu.add(iconifyAction);
        // // mnemonic = MetalUtils.getInt("MetalTitlePane.iconifyMnemonic",
        // -1);
        // if (mnemonic != -1) {
        // mi.setMnemonic(mnemonic);
        // }
        //
        // if (Toolkit.getDefaultToolkit().isFrameStateSupported(
        // Frame.MAXIMIZED_BOTH)) {
        // mi = menu.add(maximizeAction);
        // mnemonic =
        // MetalUtils.getInt("MetalTitlePane.maximizeMnemonic", -1);
        // if (mnemonic != -1) {
        // mi.setMnemonic(mnemonic);
        // }
        // }
        //
        // menu.add(new JSeparator());
        //
        // mi = menu.add(closeAction);
        // mnemonic = MetalUtils.getInt("MetalTitlePane.closeMnemonic", -1);
        // if (mnemonic != -1) {
        // mi.setMnemonic(mnemonic);
        // }
    }

    /**
     * Returns a <code>JButton</code> appropriate for placement on the TitlePane.
     */
    protected JButton createTitleButton() {
        JButton button = new JButton() {
            @Override
            public String getName() {
                return "FrameButton";
            }
        };

        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder());
        return button;
    }

    protected void createSeparator() {
        this.buttonSeparator = new JSeparator(SwingConstants.VERTICAL);
    }

    /**
     * Creates the Buttons that will be placed on the TitlePane.
     */
    protected void createButtons() {
        this.closeButton = this.createTitleButton();
        this.closeButton.setAction(this.closeAction);
        this.closeButton.setText(null);
        this.closeButton.putClientProperty("paintActive", Boolean.TRUE);
        this.closeButton.setBorder(OTitlePane.handyEmptyBorder);
        this.closeButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Close");
        this.closeButton.setIcon(UIManager.getIcon("InternalFrame.closeIcon"));

        if (this.getWindowDecorationStyle() == JRootPane.FRAME) {
            this.maximizeIcon = UIManager.getIcon("InternalFrame.maximizeIcon");
            this.minimizeIcon = UIManager.getIcon("InternalFrame.minimizeIcon");

            this.iconifyButton = this.createTitleButton();
            this.iconifyButton.setAction(this.iconifyAction);
            this.iconifyButton.setText(null);
            this.iconifyButton.putClientProperty("paintActive", Boolean.TRUE);
            this.iconifyButton.setBorder(OTitlePane.handyEmptyBorder);
            this.iconifyButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Iconify");
            this.iconifyButton.setIcon(UIManager.getIcon("InternalFrame.iconifyIcon"));

            this.toggleButton = this.createTitleButton();
            this.toggleButton.setAction(this.restoreAction);
            this.toggleButton.putClientProperty("paintActive", Boolean.TRUE);
            this.toggleButton.setBorder(OTitlePane.handyEmptyBorder);
            this.toggleButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Maximize");
            this.toggleButton.setIcon(this.maximizeIcon);
        }
    }

    /**
     * Returns the <code>LayoutManager</code> that should be installed on the
     * <code>MetalTitlePane</code>.
     */
    protected LayoutManager createLayout() {
        return new TitlePaneLayout();
    }

    /**
     * Updates state dependant upon the Window's active state.
     */
    protected void setActive(boolean isActive) {
        Boolean activeB = isActive ? Boolean.TRUE : Boolean.FALSE;

        this.closeButton.putClientProperty("paintActive", activeB);
        if (this.getWindowDecorationStyle() == JRootPane.FRAME) {
            this.iconifyButton.putClientProperty("paintActive", activeB);
            this.toggleButton.putClientProperty("paintActive", activeB);
        }
        // Repaint the whole thing as the Borders that are used have
        // different colors for active vs inactive
        this.getRootPane().repaint();
    }

    /**
     * Sets the state of the Window.
     */
    protected void setState(int state) {
        this.setState(state, false);
    }

    /**
     * Sets the state of the window. If <code>updateRegardless</code> is true and the state has not
     * changed, this will update anyway.
     */
    protected void setState(int state, boolean updateRegardless) {
        Window w = this.getWindow();

        if ((w != null) && (this.getWindowDecorationStyle() == JRootPane.FRAME)) {
            if ((this.state == state) && !updateRegardless) {
                return;
            }
            Frame frame = this.getFrame();

            if (frame != null) {
                JRootPane rootPane = this.getRootPane();

                if (((state & Frame.MAXIMIZED_BOTH) != 0)
                        && ((rootPane.getBorder() == null) || (rootPane.getBorder() instanceof UIResource))
                        && frame.isShowing()) {
                    rootPane.setBorder(null);
                } else if ((state & Frame.MAXIMIZED_BOTH) == 0) {
                    // This is a croak, if state becomes bound, this can
                    // be nuked.
                    this.rootPaneUI.installBorder(rootPane);
                }
                if (frame.isResizable()) {
                    if ((state & Frame.MAXIMIZED_BOTH) != 0) {
                        this.updateToggleButton(this.restoreAction, this.minimizeIcon);
                        this.maximizeAction.setEnabled(false);
                        this.restoreAction.setEnabled(true);
                    } else {
                        this.updateToggleButton(this.maximizeAction, this.maximizeIcon);
                        this.maximizeAction.setEnabled(true);
                        this.restoreAction.setEnabled(false);
                    }
                    if ((this.toggleButton.getParent() == null) || (this.iconifyButton.getParent() == null)) {
                        this.add(this.toggleButton);
                        this.add(this.iconifyButton);
                        this.revalidate();
                        this.repaint();
                    }
                    this.toggleButton.setText(null);
                } else {
                    this.maximizeAction.setEnabled(false);
                    this.restoreAction.setEnabled(false);
                    if (this.toggleButton.getParent() != null) {
                        this.remove(this.toggleButton);
                        this.revalidate();
                        this.repaint();
                    }
                }
            } else {
                // Not contained in a Frame
                this.maximizeAction.setEnabled(false);
                this.restoreAction.setEnabled(false);
                this.iconifyAction.setEnabled(false);
                this.remove(this.toggleButton);
                this.remove(this.iconifyButton);
                this.revalidate();
                this.repaint();
            }
            this.closeAction.setEnabled(true);
            this.state = state;
        }
    }

    /**
     * Updates the toggle button to contain the Icon <code>icon</code>, and Action <code>action</code>.
     */
    protected void updateToggleButton(Action action, Icon icon) {
        this.toggleButton.setAction(action);
        this.toggleButton.setIcon(icon);
        this.toggleButton.setText(null);
    }

    /**
     * Returns the Frame rendering in. This will return null if the <code>JRootPane</code> is not
     * contained in a <code>Frame</code>.
     */
    protected Frame getFrame() {
        Window window = this.getWindow();

        if (window instanceof Frame) {
            return (Frame) window;
        }
        return null;
    }

    /**
     * Returns the <code>Window</code> the <code>JRootPane</code> is contained in. This will return null
     * if there is no parent ancestor of the <code>JRootPane</code>.
     */
    protected Window getWindow() {
        return this.window;
    }

    /**
     * Returns the String to display as the title.
     */
    protected String getTitle() {
        Window w = this.getWindow();

        if (w instanceof Frame) {
            return ((Frame) w).getTitle();
        } else if (w instanceof Dialog) {
            return ((Dialog) w).getTitle();
        }
        return null;
    }

    /**
     * Renders the TitlePane.
     */
    @Override
    public void paintComponent(Graphics g) {
        // As state isn't bound, we need a convenience place to check
        // if it has changed. Changing the state typically changes the
        if (this.getFrame() != null) {
            this.setState(this.getFrame().getExtendedState());
        }
        JRootPane rootPane = this.getRootPane();
        Window window = this.getWindow();
        boolean leftToRight = (window == null) ? rootPane.getComponentOrientation().isLeftToRight()
                : window.getComponentOrientation().isLeftToRight();
        boolean isSelected = (window == null) ? true : window.isActive();
        int width = this.getWidth();
        int height = this.getHeight();

        Color background;
        Color foreground;
        Color lineColor;

        // MetalBumps bumps;

        if (isSelected) {
            background = this.activeBackground;
            foreground = this.activeForeground;
            lineColor = this.activeLine;
            // bumps = activeBumps;
        } else {
            background = this.inactiveBackground;
            foreground = this.inactiveForeground;
            lineColor = this.activeLine;
            // bumps = inactiveBumps;
        }

        g.setColor(background);

        // g.fillRoundRect(0, 0, width, height,this.roundArc,this.roundArc);
        // g.fillRect(0, height/2, width, height);

        // g.setColor( darkShadow );
        // g.drawLine ( 0, height - 1, width, height -1);
        // g.drawLine ( 0, 0, 0 ,0);
        // g.drawLine ( width - 1, 0 , width -1, 0);

        // g.setColor(lineColor);
        // g.drawLine(0, height-2, width, height-2);

        int xOffset = leftToRight ? 5 : width - 5;

        if (this.getWindowDecorationStyle() == JRootPane.FRAME) {
            xOffset += leftToRight ? OTitlePane.IMAGE_WIDTH + 5 : -OTitlePane.IMAGE_WIDTH - 5;
        }

        String theTitle = this.getTitle();
        // if (theTitle != null) {
        // FontMetrics fm = SwingUtilities2.getFontMetrics(rootPane, g);
        //
        // g.setColor(foreground);
        //
        // int yOffset = ( (height - fm.getHeight() ) / 2 ) + fm.getAscent();
        //
        // Rectangle rect = new Rectangle(0, 0, 0, 0);
        // if (iconifyButton != null && iconifyButton.getParent() != null) {
        // rect = iconifyButton.getBounds();
        // }
        // int titleW;
        //
        // if( leftToRight ) {
        // if (rect.x == 0) {
        // rect.x = window.getWidth() - window.getInsets().right-2;
        // }
        // titleW = rect.x - xOffset - 4;
        // theTitle = SwingUtilities2.clipStringIfNecessary(
        // rootPane, fm, theTitle, titleW);
        // } else {
        // titleW = xOffset - rect.x - rect.width - 4;
        // theTitle = SwingUtilities2.clipStringIfNecessary(
        // rootPane, fm, theTitle, titleW);
        // xOffset -= SwingUtilities2.stringWidth(rootPane, fm,
        // theTitle);
        // }
        // int titleLength = SwingUtilities2.stringWidth(rootPane, fm,
        // theTitle);
        // SwingUtilities2.drawString(rootPane, g, theTitle, xOffset,yOffset );
        // xOffset += leftToRight ? titleLength + 5 : -5;
        // }

        int bumpXOffset;
        int bumpLength;
        if (leftToRight) {
            bumpLength = width - this.buttonsWidth - xOffset - 5;
            bumpXOffset = xOffset;
        } else {
            bumpLength = xOffset - this.buttonsWidth - 5;
            bumpXOffset = this.buttonsWidth + 5;
        }
        int bumpYOffset = 3;
        int bumpHeight = this.getHeight() - (2 * bumpYOffset);
        // bumps.setBumpArea( bumpLength, bumpHeight );
        // bumps.paintIcon(this, g, bumpXOffset, bumpYOffset);
    }

    /**
     * Actions used to <code>close</code> the <code>Window</code>.
     */
    protected class CloseAction extends AbstractAction {

        public CloseAction() {
            super(UIManager.getString("OntimizeTitlePane.closeTitle", OTitlePane.this.getLocale()));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            OTitlePane.this.close();
        }

    }

    /**
     * Actions used to <code>iconfiy</code> the <code>Frame</code>.
     */
    protected class IconifyAction extends AbstractAction {

        public IconifyAction() {
            super(UIManager.getString("OntimizeTitlePane.iconifyTitle", OTitlePane.this.getLocale()));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            OTitlePane.this.iconify();
        }

    }

    /**
     * Actions used to <code>restore</code> the <code>Frame</code>.
     */
    protected class RestoreAction extends AbstractAction {

        public RestoreAction() {
            super(UIManager.getString("OntimizeTitlePane.restoreTitle", OTitlePane.this.getLocale()));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            OTitlePane.this.restore();
        }

    }

    /**
     * Actions used to <code>restore</code> the <code>Frame</code>.
     */
    protected class MaximizeAction extends AbstractAction {

        public MaximizeAction() {
            super(UIManager.getString("OntimizeTitlePane.maximizeTitle", OTitlePane.this.getLocale()));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            OTitlePane.this.maximize();
        }

    }

    /**
     * Class responsible for drawing the system menu. Looks up the image to draw from the Frame
     * associated with the <code>JRootPane</code>.
     */
    protected class SystemMenuBar extends JMenuBar {

        @Override
        public void paint(Graphics g) {
            if (this.isOpaque()) {
                g.setColor(this.getBackground());
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
            }

            if (OTitlePane.this.systemIcon != null) {
                g.drawImage(OTitlePane.this.systemIcon, 0, 0, OTitlePane.IMAGE_WIDTH, OTitlePane.IMAGE_HEIGHT, null);
            } else {
                Icon icon = UIManager.getIcon("InternalFrame.icon");

                if (icon != null) {
                    icon.paintIcon(this, g, 0, 0);
                }
            }

            g.drawLine(0, 0, this.getWidth(), this.getHeight());
        }

        @Override
        public Dimension getMinimumSize() {
            return this.getPreferredSize();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();

            return new Dimension(Math.max(OTitlePane.IMAGE_WIDTH, size.width),
                    Math.max(size.height, OTitlePane.IMAGE_HEIGHT));
        }

    }

    protected class TitlePaneLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component c) {
        }

        @Override
        public void removeLayoutComponent(Component c) {
        }

        @Override
        public Dimension preferredLayoutSize(Container c) {
            int height = this.computeHeight();
            return new Dimension(height, height);
        }

        @Override
        public Dimension minimumLayoutSize(Container c) {
            return this.preferredLayoutSize(c);
        }

        protected int computeHeight() {
            FontMetrics fm = OTitlePane.this.rootPane.getFontMetrics(OTitlePane.this.getFont());
            int fontHeight = fm.getHeight();
            fontHeight += 7;
            int iconHeight = 0;
            if (OTitlePane.this.getWindowDecorationStyle() == JRootPane.FRAME) {
                iconHeight = OTitlePane.IMAGE_HEIGHT;
            }

            int finalHeight = Math.max(fontHeight, iconHeight);
            return finalHeight;
        }

        @Override
        public void layoutContainer(Container c) {
            boolean leftToRight = (OTitlePane.this.window == null)
                    ? OTitlePane.this.getRootPane().getComponentOrientation().isLeftToRight() : OTitlePane.this.window
                        .getComponentOrientation()
                        .isLeftToRight();

            int w = OTitlePane.this.getWidth();
            int x;
            int y = 3;
            int spacing;
            int buttonHeight;
            int buttonWidth;

            if ((OTitlePane.this.closeButton != null) && (OTitlePane.this.closeButton.getIcon() != null)) {
                buttonHeight = OTitlePane.this.closeButton.getIcon().getIconHeight();
                buttonWidth = OTitlePane.this.closeButton.getIcon().getIconWidth();
            } else {
                buttonHeight = OTitlePane.IMAGE_HEIGHT;
                buttonWidth = OTitlePane.IMAGE_WIDTH;
            }

            // assumes all buttons have the same dimensions
            // these dimensions include the borders

            x = leftToRight ? w : 0;

            spacing = 5;
            x = leftToRight ? spacing : w - buttonWidth - spacing;

            if (OTitlePane.this.menuBar != null) {
                OTitlePane.this.menuBar.setBounds(x, y, buttonWidth, buttonHeight);
            }

            x = leftToRight ? w : 0;
            spacing = 10;
            x += leftToRight ? -spacing - buttonWidth : spacing;
            if (OTitlePane.this.closeButton != null) {
                OTitlePane.this.closeButton.setBounds(x, y, buttonWidth, buttonHeight);
            }

            if (!leftToRight) {
                x += buttonWidth;
            }

            if (OTitlePane.this.getWindowDecorationStyle() == JRootPane.FRAME) {
                if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
                    if (OTitlePane.this.toggleButton.getParent() != null) {
                        spacing = 2;
                        x += leftToRight ? -spacing - buttonWidth : spacing;
                        OTitlePane.this.toggleButton.setBounds(x, y, buttonWidth, buttonHeight);
                        if (!leftToRight) {
                            x += buttonWidth;
                        }
                    }
                }

                if ((OTitlePane.this.iconifyButton != null) && (OTitlePane.this.iconifyButton.getParent() != null)) {
                    spacing = 2;
                    x += leftToRight ? -spacing - buttonWidth : spacing;
                    OTitlePane.this.iconifyButton.setBounds(x, y, buttonWidth, buttonHeight);
                    if (!leftToRight) {
                        x += buttonWidth;
                    }
                }
            }
            OTitlePane.this.buttonsWidth = leftToRight ? w - x : x;

            if (OTitlePane.this.buttonSeparator != null) {
                Dimension dimension = OTitlePane.this.buttonSeparator.getPreferredSize();
                x += -(spacing * 2) - dimension.width;
                OTitlePane.this.buttonSeparator.setBounds(x, y, dimension.width, buttonHeight);
            }
        }

    }

    /**
     * PropertyChangeListener installed on the Window. Updates the necessary state as the state of the
     * Window changes.
     */
    protected class PropertyChangeHandler implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            String name = pce.getPropertyName();

            // Frame.state isn't currently bound.
            if ("resizable".equals(name) || "state".equals(name)) {
                Frame frame = OTitlePane.this.getFrame();

                if (frame != null) {
                    OTitlePane.this.setState(frame.getExtendedState(), true);
                }
                if ("resizable".equals(name)) {
                    OTitlePane.this.getRootPane().repaint();
                }
            } else if ("title".equals(name)) {
                OTitlePane.this.repaint();
            } else if ("componentOrientation" == name) {
                OTitlePane.this.revalidate();
                OTitlePane.this.repaint();
            } else if ("iconImage" == name) {
                OTitlePane.this.updateSystemIcon();
                OTitlePane.this.revalidate();
                OTitlePane.this.repaint();
            } else if ("menubar" == name) {
                // TODO
                // insertMenu();
                OTitlePane.this.repaint();
            }
        }

    }

    /**
     * Update the image used for the system icon
     */
    protected void updateSystemIcon() {
        Window window = this.getWindow();
        if (window == null) {
            this.systemIcon = null;
            return;
        }
        java.util.List<Image> icons = window.getIconImages();
        assert icons != null;

        if (icons.size() == 0) {
            this.systemIcon = null;
        } else if (icons.size() == 1) {
            this.systemIcon = icons.get(0);
        } else {
            this.systemIcon = SunToolkit.getScaledIconImage(icons, OTitlePane.IMAGE_WIDTH, OTitlePane.IMAGE_HEIGHT);
        }
    }

    /**
     * WindowListener installed on the Window, updates the state as necessary.
     */
    protected class WindowHandler extends WindowAdapter {

        @Override
        public void windowActivated(WindowEvent ev) {
            OTitlePane.this.setActive(true);
        }

        @Override
        public void windowDeactivated(WindowEvent ev) {
            OTitlePane.this.setActive(false);
        }

    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();

        int currentHeight = size.height;

        Object uiHeight = OntimizeLookAndFeel.lookup("RootPane.titlePane.height");
        if (uiHeight instanceof Integer) {
            currentHeight = Math.max(size.height, ((Integer) uiHeight).intValue());
        }
        size.height = currentHeight;
        return size;
    }

}
