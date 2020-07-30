package com.ontimize.plaf;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.plaf.synth.Region;

import sun.awt.AppContext;

public class OntimizeRegion extends Region {

    protected static final Object UI_TO_REGION_MAP_KEY = new Object();

    protected static final Object LOWER_CASE_NAME_MAP_KEY = new Object();

    /** A Close button for a tab in a tabbed pane. */
    public static Region TABBED_PANE_TAB_CLOSE_BUTTON = new OntimizeRegion("TabbedPaneTabCloseButton", null, true);

    public static Region FORM_TABBED_PANE = new OntimizeRegion("FormTabbedPane", "FormTabbedPaneUI", false);

    public static Region FORM_TABBED_PANE_TAB = new OntimizeRegion("FormTabbedPaneTab", null, true);

    public static Region FORM_TABBED_PANE_TAB_AREA = new OntimizeRegion("FormTabbedPaneTabArea", null, true);

    public static Region FORM_TABBED_PANE_CONTENT = new OntimizeRegion("FormTabbedPaneConent", null, true);

    protected OntimizeRegion(String name, String ui, boolean subregion) {
        super(name, ui, subregion);
    }

    /**
     * Returns the Region for the JComponent <code>c</code>.
     * @param c JComponent to fetch the Region for
     * @return Region corresponding to <code>c</code>
     */
    public static Region getRegion(JComponent c) {
        Map<String, Region> map = OntimizeRegion.getUItoRegionMap();
        return map.get(c.getUIClassID());
    }

    protected static Map<String, Region> getUItoRegionMap() {
        AppContext context = AppContext.getAppContext();
        Map<String, Region> map = (Map<String, Region>) context.get(OntimizeRegion.UI_TO_REGION_MAP_KEY);
        if (map == null) {
            map = new HashMap<String, Region>();
            // Registering standard Regions...
            map.put("ArrowButtonUI", Region.ARROW_BUTTON);
            map.put("ButtonUI", Region.BUTTON);
            map.put("CheckBoxUI", Region.CHECK_BOX);
            map.put("CheckBoxMenuItemUI", Region.CHECK_BOX_MENU_ITEM);
            map.put("ColorChooserUI", Region.COLOR_CHOOSER);
            map.put("ComboBoxUI", Region.COMBO_BOX);
            map.put("DesktopPaneUI", Region.DESKTOP_PANE);
            map.put("DesktopIconUI", Region.DESKTOP_ICON);
            map.put("EditorPaneUI", Region.EDITOR_PANE);
            map.put("FileChooserUI", Region.FILE_CHOOSER);
            map.put("FormattedTextFieldUI", Region.FORMATTED_TEXT_FIELD);
            map.put("InternalFrameUI", Region.INTERNAL_FRAME);
            map.put("InternalFrameTitlePaneUI", Region.INTERNAL_FRAME_TITLE_PANE);
            map.put("LabelUI", Region.LABEL);
            map.put("ListUI", Region.LIST);
            map.put("MenuUI", Region.MENU);
            map.put("MenuBarUI", Region.MENU_BAR);
            map.put("MenuItemUI", Region.MENU_ITEM);
            map.put("OptionPaneUI", Region.OPTION_PANE);
            map.put("PanelUI", Region.PANEL);
            map.put("PasswordFieldUI", Region.PASSWORD_FIELD);
            map.put("PopupMenuUI", Region.POPUP_MENU);
            map.put("PopupMenuSeparatorUI", Region.POPUP_MENU_SEPARATOR);
            map.put("ProgressBarUI", Region.PROGRESS_BAR);
            map.put("RadioButtonUI", Region.RADIO_BUTTON);
            map.put("RadioButtonMenuItemUI", Region.RADIO_BUTTON_MENU_ITEM);
            map.put("RootPaneUI", Region.ROOT_PANE);
            map.put("ScrollBarUI", Region.SCROLL_BAR);
            map.put("ScrollPaneUI", Region.SCROLL_PANE);
            map.put("SeparatorUI", Region.SEPARATOR);
            map.put("SliderUI", Region.SLIDER);
            map.put("SpinnerUI", Region.SPINNER);
            map.put("SplitPaneUI", Region.SPLIT_PANE);
            map.put("TabbedPaneUI", Region.TABBED_PANE);
            map.put("TableUI", Region.TABLE);
            map.put("TableHeaderUI", Region.TABLE_HEADER);
            map.put("TextAreaUI", Region.TEXT_AREA);
            map.put("TextFieldUI", Region.TEXT_FIELD);
            map.put("TextPaneUI", Region.TEXT_PANE);
            map.put("ToggleButtonUI", Region.TOGGLE_BUTTON);
            map.put("ToolBarUI", Region.TOOL_BAR);
            map.put("ToolTipUI", Region.TOOL_TIP);
            map.put("ToolBarSeparatorUI", Region.TOOL_BAR_SEPARATOR);
            map.put("TreeUI", Region.TREE);
            map.put("ViewportUI", Region.VIEWPORT);
            // Registering customized Regions...
            OntimizeRegion.registerCustomRegions(map);
            context.put(OntimizeRegion.UI_TO_REGION_MAP_KEY, map);
        }
        return map;
    }

    protected static void registerCustomRegions(Map<String, Region> map) {
        map.put("FormTabbedPaneUI", OntimizeRegion.FORM_TABBED_PANE);
    }

    public static void clearReferences() {
        OntimizeRegion.TABBED_PANE_TAB_CLOSE_BUTTON = null;
    }

}
