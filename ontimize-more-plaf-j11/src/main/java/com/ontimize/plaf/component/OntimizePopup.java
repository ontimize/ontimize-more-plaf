package com.ontimize.plaf.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToolTip;
import javax.swing.JWindow;
import javax.swing.MenuElement;
import javax.swing.Popup;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.ontimize.gui.ExtendedJPopupMenu;

import sun.awt.AppContext;
import sun.awt.ModalExclude;

public class OntimizePopup extends Popup {

    /**
     * Max number of items to store in any one particular cache.
     */
    protected static final int MAX_CACHE_SIZE = 5;

    protected static final Object ontimizePopupCacheKey = new Object();

    protected JWindow popupWindow;

    protected Component component;

    /**
     * Variable to set Fade effect.
     */
    protected boolean toFade = false;

    protected int currOpacity;

    protected Timer fadeInTimer;

    protected Timer fadeOutTimer;

    protected OntimizePopup() {
    }

    /**
     * The Component representing the Popup.
     */
    // protected Component component;

    /**
     * Returns either a new or recycled <code>Popup</code> containing the specified children.
     */
    public static Popup getHeavyWeightPopup(Component owner, Component contents, int ownerX, int ownerY) {
        Window window = (owner != null) ? SwingUtilities.getWindowAncestor(owner) : null;
        OntimizePopup popup = null;

        if (window != null) {
            // popup = new OntimizePopup();
            // popup = getRecycledOntmizePopup(window);
        }

        boolean focusPopup = false;
        if ((contents != null) && contents.isFocusable()) {
            if (contents instanceof JPopupMenu) {
                JPopupMenu jpm = (JPopupMenu) contents;
                Component popComps[] = jpm.getComponents();
                for (int i = 0; i < popComps.length; i++) {
                    if (!(popComps[i] instanceof MenuElement) && !(popComps[i] instanceof JSeparator)) {
                        focusPopup = true;
                        break;
                    }
                }
            }
        }

        if ((popup == null) || (((JWindow) popup.getComponent()).getFocusableWindowState() != focusPopup)) {
            if (popup != null) {
                // The recycled popup can't serve us well
                // dispose it and create new one
                popup._dispose();
            }
            popup = new OntimizePopup();
        }
        popup.reset(owner, contents, ownerX, ownerY);

        if (focusPopup) {
            JWindow wnd = (JWindow) popup.getComponent();
            wnd.setFocusableWindowState(true);
            // Set window name. We need this in BasicPopupMenuUI
            // to identify focusable popup window.
            wnd.setName("###focusableSwingPopup###");
        }

        return popup;
    }

    void _dispose() {
        Component component = this.getComponent();
        Window window = SwingUtilities.getWindowAncestor(component);

        if (component instanceof JWindow) {
            ((Window) component).dispose();
            component = null;
        }
    }

    protected Component getComponent() {
        return this.component;
    }

    /**
     * Creates the Component to use as the parent of the <code>Popup</code>. The default implementation
     * creates a <code>Window</code>, subclasses should override.
     */
    protected Component createComponent(Component owner) {
        if (GraphicsEnvironment.isHeadless()) {
            // Generally not useful, bail.
            return null;
        }
        return new HeavyWeightWindow(this.getParentWindow(owner));
    }

    /**
     * Returns the <code>Window</code> to use as the parent of the <code>Window</code> created for the
     * <code>Popup</code>. This creates a new <code>DefaultFrame</code>, if necessary.
     */
    protected Window getParentWindow(Component owner) {
        Window window = null;

        if (owner instanceof Window) {
            window = (Window) owner;
        } else if (owner != null) {
            window = SwingUtilities.getWindowAncestor(owner);
        }
        if (window == null) {
            window = new JWindow();
        }
        return window;
    }

    public OntimizePopup(Component owner, Component contents, int ownerX, int ownerY) {

        // create a new heavyweight window
        this.popupWindow = new JWindow();

        if (contents instanceof JToolTip) {
            this.toFade = true;
        } else if (contents instanceof ExtendedJPopupMenu) {
            this.toFade = true;
        } else {
            this.toFade = false;
        }
        // determine the popup location
        this.popupWindow.setLocation(ownerX, ownerY);
        // add the contents to the popup
        this.popupWindow.getContentPane().add(contents, BorderLayout.CENTER);
        contents.invalidate();
    }

    /**
     * Resets the <code>Popup</code> to an initial state.
     */
    protected void reset(Component owner, Component contents, int ownerX, int ownerY) {
        if (this.getComponent() == null) {
            this.component = this.createComponent(owner);
        }

        Component c = this.getComponent();

        if (c instanceof JWindow) {
            JWindow component = (JWindow) this.getComponent();

            if (contents instanceof JToolTip) {
                this.toFade = true;
            } else if (contents instanceof ExtendedJPopupMenu) {
                this.toFade = true;
            } else {
                this.toFade = false;
            }

            component.setLocation(ownerX, ownerY);
            component.getContentPane().add(contents, BorderLayout.CENTER);
            contents.invalidate();
            if (component.isVisible()) {
                // Do not call pack() if window is not visible to
                // avoid early native peer creation
                this._pack();
            }
        }
    }

    protected void _pack() {
        if (this.getComponent() instanceof Window) {
            ((Window) this.getComponent()).pack();
        }
    }

    @Override
    public void show() {

        if (this.getComponent() instanceof JWindow) {
            this.popupWindow = (JWindow) this.getComponent();
        }
        if (this.popupWindow == null) {
            return;
        }

        if (this.toFade) {
            // mark the popup with 0% opacity
            this.currOpacity = 0;
            // AWTUtilities.setWindowOpacity(popupWindow, 0.0f);
            try {
                this.popupWindow.setOpacity(0.0f);
            } catch (Exception e) {
            }
        }

        this.component.setVisible(true);
        this._pack();

        // AWTUtilities.setWindowOpaque(popupWindow, false);
        this.popupWindow.setBackground(new Color(0, 0, 0, 1));
        if (this.popupWindow.getContentPane() instanceof JPanel) {
            this.popupWindow.getRootPane().setBackground(new Color(0, 0, 0, 1));
            ((JPanel) this.popupWindow.getContentPane()).setOpaque(false);
            this.popupWindow.getContentPane().repaint();
        }

        if (this.toFade) {
            // start fading in
            this.fadeInTimer = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    OntimizePopup.this.currOpacity += 20;
                    if (OntimizePopup.this.currOpacity <= 100) {
                        // AWTUtilities.setWindowOpacity(popupWindow, currOpacity / 100.0f);
                        try {
                            OntimizePopup.this.popupWindow.setOpacity(OntimizePopup.this.currOpacity / 100.0f);
                        } catch (Exception ex) {
                        }
                        OntimizePopup.this.popupWindow.getContentPane().repaint();
                    } else {
                        OntimizePopup.this.currOpacity = 100;
                        OntimizePopup.this.fadeInTimer.stop();
                    }
                }
            });
            this.fadeInTimer.setRepeats(true);
            this.fadeInTimer.start();
        } else {
            this.popupWindow.setBackground(new Color(0, 0, 0, 1));
        }

    }

    @Override
    public void hide() {
        if (this.toFade && (this.popupWindow != null)) {
            // cancel fade-in if it's running.
            if ((this.fadeInTimer != null) && this.fadeInTimer.isRunning()) {
                this.fadeInTimer.stop();
            }

            // start fading out
            this.fadeOutTimer = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    OntimizePopup.this.currOpacity -= 10;
                    if (OntimizePopup.this.currOpacity >= 0) {
                        // AWTUtilities.setWindowOpacity(popupWindow, currOpacity / 100.0f);
                        try {
                            OntimizePopup.this.popupWindow.setOpacity(OntimizePopup.this.currOpacity / 100.0f);
                        } catch (Exception ex) {
                        }
                        // workaround bug 6670649 - should call
                        // popupWindow.repaint() but that will not repaint the
                        // panel
                        OntimizePopup.this.popupWindow.getContentPane().repaint();
                    } else {
                        OntimizePopup.this.fadeOutTimer.stop();
                        OntimizePopup.this.popupWindow.setVisible(false);
                        OntimizePopup.this.popupWindow.removeAll();
                        OntimizePopup.this.popupWindow.dispose();
                        OntimizePopup.this.currOpacity = 0;
                    }
                }
            });
            this.fadeOutTimer.setRepeats(true);
            this.fadeOutTimer.start();
        } else {
            // popupWindow.setVisible(false);
            // popupWindow.removeAll();
            // popupWindow.dispose();
            Component component = this.getComponent();

            if (component instanceof JWindow) {
                component.hide();
                ((JWindow) component).getContentPane().removeAll();
            }
            this._dispose();
        }
        // recycleOntmizePopup(this);
    }


    /**
     * Returns a previously disposed heavy weight <code>Popup</code> associated with
     * <code>window</code>. This will return null if there is no <code>OntimizePopup</code> associated
     * with <code>window</code>.
     */
    protected static OntimizePopup getRecycledOntmizePopup(Window w) {
        synchronized (OntimizePopup.class) {
            List cache;
            Map heavyPopupCache = OntimizePopup.getOntimizePopupCache();

            if (heavyPopupCache.containsKey(w)) {
                cache = (List) heavyPopupCache.get(w);
            } else {
                return null;
            }
            int c;
            if ((c = cache.size()) > 0) {
                OntimizePopup r = (OntimizePopup) cache.get(0);
                cache.remove(0);
                return r;
            }
            return null;
        }
    }

    /**
     * Returns the cache to use for ontmize popups. Maps from <code>Window</code> to a <code>List</code>
     * of <code>OntimizePopup</code>s.
     */
    protected static Map getOntimizePopupCache() {
        synchronized (OntimizePopup.class) {
            Map cache = (Map) AppContext.getAppContext().get(OntimizePopup.ontimizePopupCacheKey);

            if (cache == null) {
                cache = new HashMap(2);
                AppContext.getAppContext().put(OntimizePopup.ontimizePopupCacheKey, cache);
            }
            return cache;
        }
    }

    /**
     * Recycles the passed in <code>OntmizePopup</code>.
     */
    protected static void recycleOntmizePopup(OntimizePopup popup) {
        synchronized (OntimizePopup.class) {
            List cache;
            Object window = SwingUtilities.getWindowAncestor(popup.getComponent());
            Map heavyPopupCache = OntimizePopup.getOntimizePopupCache();

            if (!((Window) window).isVisible()) {
                // If the Window isn't visible, we don't cache it as we
                // likely won't ever get a windowClosed event to clean up.
                popup._dispose();
                return;
            } else if (heavyPopupCache.containsKey(window)) {
                cache = (List) heavyPopupCache.get(window);
            } else {
                cache = new ArrayList();
                heavyPopupCache.put(window, cache);
                // Clean up if the Window is closed
                final Window w = (Window) window;

                w.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        List popups;

                        synchronized (OntimizePopup.class) {
                            Map heavyPopupCache2 = OntimizePopup.getOntimizePopupCache();
                            popups = (List) heavyPopupCache2.remove(w);
                        }
                        if (popups != null) {
                            for (int counter = popups.size() - 1; counter >= 0; counter--) {
                                ((OntimizePopup) popups.get(counter))._dispose();
                            }
                        }
                    }
                });
            }

            if (cache.size() < OntimizePopup.MAX_CACHE_SIZE) {
                cache.add(popup);
            } else {
                popup._dispose();
            }
        }
    }


    /**
     * Component used to house window.
     */
    static class HeavyWeightWindow extends JWindow implements ModalExclude {

        HeavyWeightWindow(Window parent) {
            super(parent);
            this.setFocusableWindowState(false);
            this.setName("###overrideRedirect###");
            this.setType(Window.Type.POPUP);
            // Popups are typically transient and most likely won't benefit
            // from true double buffering. Turn it off here.
            // getRootPane().setUseTrueDoubleBuffering(false);
            // Try to set "always-on-top" for the popup window.
            // Applets usually don't have sufficient permissions to do it.
            // In this case simply ignore the exception.
            try {
                this.setAlwaysOnTop(true);
            } catch (SecurityException se) {
                // setAlwaysOnTop is restricted,
                // the exception is ignored
            }
        }

        @Override
        public void update(Graphics g) {
            this.paint(g);
        }

        @Override
        public void show() {
            this.pack();
            super.show();
        }

    }

}
