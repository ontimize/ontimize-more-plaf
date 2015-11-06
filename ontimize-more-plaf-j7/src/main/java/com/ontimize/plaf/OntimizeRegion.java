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
    public static Region TABBED_PANE_TAB_CLOSE_BUTTON = new OntimizeRegion("TabbedPaneTabClaseButton", null, true);


	protected OntimizeRegion(String name, String ui, boolean subregion) {
		super(name, ui, subregion);
	}

	/**
     * Returns the Region for the JComponent <code>c</code>.
     *
     * @param c JComponent to fetch the Region for
     * @return Region corresponding to <code>c</code>
     */
    public static Region getRegion(JComponent c) {
    	return getUItoRegionMap().get(c.getUIClassID());
    }
    
    protected static Map<String, Region> getUItoRegionMap() {
        AppContext context = AppContext.getAppContext();
        Map<String, Region> map = (Map<String, Region>) context.get(UI_TO_REGION_MAP_KEY);
        if (map == null) {
            map = new HashMap<String, Region>();
            map.put("ArrowButtonUI", ARROW_BUTTON);
            map.put("ButtonUI", BUTTON);
            map.put("CheckBoxUI", CHECK_BOX);
            map.put("CheckBoxMenuItemUI", CHECK_BOX_MENU_ITEM);
            map.put("ColorChooserUI", COLOR_CHOOSER);
            map.put("ComboBoxUI", COMBO_BOX);
            map.put("DesktopPaneUI", DESKTOP_PANE);
            map.put("DesktopIconUI", DESKTOP_ICON);
            map.put("EditorPaneUI", EDITOR_PANE);
            map.put("FileChooserUI", FILE_CHOOSER);
            map.put("FormattedTextFieldUI", FORMATTED_TEXT_FIELD);
            map.put("InternalFrameUI", INTERNAL_FRAME);
            map.put("InternalFrameTitlePaneUI", INTERNAL_FRAME_TITLE_PANE);
            map.put("LabelUI", LABEL);
            map.put("ListUI", LIST);
            map.put("MenuUI", MENU);
            map.put("MenuBarUI", MENU_BAR);
            map.put("MenuItemUI", MENU_ITEM);
            map.put("OptionPaneUI", OPTION_PANE);
            map.put("PanelUI", PANEL);
            map.put("PasswordFieldUI", PASSWORD_FIELD);
            map.put("PopupMenuUI", POPUP_MENU);
            map.put("PopupMenuSeparatorUI", POPUP_MENU_SEPARATOR);
            map.put("ProgressBarUI", PROGRESS_BAR);
            map.put("RadioButtonUI", RADIO_BUTTON);
            map.put("RadioButtonMenuItemUI", RADIO_BUTTON_MENU_ITEM);
            map.put("RootPaneUI", ROOT_PANE);
            map.put("ScrollBarUI", SCROLL_BAR);
            map.put("ScrollPaneUI", SCROLL_PANE);
            map.put("SeparatorUI", SEPARATOR);
            map.put("SliderUI", SLIDER);
            map.put("SpinnerUI", SPINNER);
            map.put("SplitPaneUI", SPLIT_PANE);
            map.put("TabbedPaneUI", TABBED_PANE);
            map.put("TableUI", TABLE);
            map.put("TableHeaderUI", TABLE_HEADER);
            map.put("TextAreaUI", TEXT_AREA);
            map.put("TextFieldUI", TEXT_FIELD);
            map.put("TextPaneUI", TEXT_PANE);
            map.put("ToggleButtonUI", TOGGLE_BUTTON);
            map.put("ToolBarUI", TOOL_BAR);
            map.put("ToolTipUI", TOOL_TIP);
            map.put("ToolBarSeparatorUI", TOOL_BAR_SEPARATOR);
            map.put("TreeUI", TREE);
            map.put("ViewportUI", VIEWPORT);
            context.put(UI_TO_REGION_MAP_KEY, map);
        }
        return map;
    }
    
    public static void clearReferences(){
    	TABBED_PANE_TAB_CLOSE_BUTTON = null;
    }
}
