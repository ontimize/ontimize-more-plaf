package com.ontimize.plaf.ui;

import java.awt.Graphics;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;
import javax.swing.text.PasswordView;
import javax.swing.text.View;

import com.ontimize.plaf.utils.ContextUtils;

/**
 * Sea Glass PasswordField UI delegate.
 *
 * <p>
 * Based on SynthPasswordTextFieldUI, but needs to extend OTextFieldUI instead.
 * </p>
 */
public class OPasswordFieldUI extends OTextFieldUI {

    /**
     * Creates a UI for a JPasswordField.
     * @param c the JPasswordField
     * @return the UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new OPasswordFieldUI();
    }

    /**
     * Fetches the name used as a key to look up properties through the UIManager. This is used as a
     * prefix to all the standard text properties.
     * @return the name ("PasswordField")
     */
    protected String getPropertyPrefix() {
        return "PasswordField";
    }

    /**
     * Creates a view (PasswordView) for an element.
     * @param elem the element
     * @return the view
     */
    public View create(Element elem) {
        return new PasswordView(elem);
    }


    void paintBackground(SynthContext context, Graphics g, JComponent c) {
        ContextUtils.getPainter(context).paintPasswordFieldBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ContextUtils.getPainter(context).paintPasswordFieldBorder(context, g, x, y, w, h);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTextUI#installKeyboardActions()
     */
    protected void installKeyboardActions() {
        super.installKeyboardActions();
        ActionMap map = SwingUtilities.getUIActionMap(getComponent());

        if (map != null && map.get(DefaultEditorKit.selectWordAction) != null) {
            Action a = map.get(DefaultEditorKit.selectLineAction);

            if (a != null) {
                map.put(DefaultEditorKit.selectWordAction, a);
            }
        }
    }

}
