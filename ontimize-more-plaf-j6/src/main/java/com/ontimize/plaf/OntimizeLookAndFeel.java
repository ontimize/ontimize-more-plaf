package com.ontimize.plaf;

import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.PopupFactory;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;

import sun.swing.plaf.synth.DefaultSynthStyle;
import sun.swing.plaf.synth.SynthUI;

import com.ontimize.gui.ApToolBarNavigator;
import com.ontimize.gui.ApToolBarPopupButton;
import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.ApplicationToolBar;
import com.ontimize.gui.BorderManager;
import com.ontimize.gui.Form;
import com.ontimize.gui.Form.FormButton;
import com.ontimize.gui.attachment.AttachmentComponent;
import com.ontimize.gui.attachment.AttachmentListPopup;
import com.ontimize.gui.button.FormHeaderButton;
import com.ontimize.gui.button.FormHeaderPopupButton;
import com.ontimize.gui.button.RolloverButton;
import com.ontimize.gui.calendar.VisualCalendarComponent;
import com.ontimize.gui.container.CollapsiblePanel;
import com.ontimize.gui.container.CurveMattedDeployableBorder;
import com.ontimize.gui.container.Grid;
import com.ontimize.gui.container.Row;
import com.ontimize.gui.field.DataField;
import com.ontimize.gui.field.DataField.FieldButton;
import com.ontimize.gui.field.HTMLDataField;
import com.ontimize.gui.field.ListDataField;
import com.ontimize.gui.images.ImageManager;
import com.ontimize.gui.table.BooleanCellRenderer;
import com.ontimize.gui.table.CellEditor;
import com.ontimize.gui.table.CellRenderer;
import com.ontimize.gui.table.RowHeadCellRenderer;
import com.ontimize.gui.table.SortTableCellRenderer;
import com.ontimize.gui.table.SumCellRenderer;
import com.ontimize.gui.table.SumRowSetupDialog;
import com.ontimize.gui.table.Table;
import com.ontimize.gui.table.Table.QuickFieldText;
import com.ontimize.gui.table.TableButton;
import com.ontimize.gui.tree.BasicTreeCellRenderer;
import com.ontimize.gui.tree.Tree;
import com.ontimize.plaf.OntimizeDefaults.LazyPainter;
import com.ontimize.plaf.border.OLoweredBorder;
import com.ontimize.plaf.component.OntimizePopupFactory;
import com.ontimize.plaf.component.OntimizeSynthIcon;
import com.ontimize.plaf.painter.AbstractOButtonPainter;
import com.ontimize.plaf.painter.AbstractOToolBarPainter;
import com.ontimize.plaf.painter.AbstractRegionPainter;
import com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext;
import com.ontimize.plaf.painter.ButtonPainter;
import com.ontimize.plaf.painter.OCardPanelPainter;
import com.ontimize.plaf.painter.OCheckBoxMenuItemPainter;
import com.ontimize.plaf.painter.OCheckBoxPainter;
import com.ontimize.plaf.painter.OCollapsibleButtonPanelPainter;
import com.ontimize.plaf.painter.OColumnPanelPainter;
import com.ontimize.plaf.painter.OComboBoxArrowButtonPainter;
import com.ontimize.plaf.painter.OComboBoxPainter;
import com.ontimize.plaf.painter.OComboBoxTextFieldPainter;
import com.ontimize.plaf.painter.OEditorPanePainter;
import com.ontimize.plaf.painter.OFormBodyPanelPainter;
import com.ontimize.plaf.painter.OFormTitlePainter;
import com.ontimize.plaf.painter.OGridPanelPainter;
import com.ontimize.plaf.painter.OMenuBarMenuPainter;
import com.ontimize.plaf.painter.OMenuBarPainter;
import com.ontimize.plaf.painter.OMenuItemPainter;
import com.ontimize.plaf.painter.OMenuPainter;
import com.ontimize.plaf.painter.OPopupItemPainter;
import com.ontimize.plaf.painter.OPopupMenuPainter;
import com.ontimize.plaf.painter.OPopupMenuSeparatorPainter;
import com.ontimize.plaf.painter.OQuickFilterPainter;
import com.ontimize.plaf.painter.ORadioButtonMenuItemPainter;
import com.ontimize.plaf.painter.ORadioButtonPainter;
import com.ontimize.plaf.painter.ORootPainter;
import com.ontimize.plaf.painter.ORowPanelPainter;
import com.ontimize.plaf.painter.OScrollBarButtonPainter;
import com.ontimize.plaf.painter.OScrollBarPainter;
import com.ontimize.plaf.painter.OScrollBarThumbPainter;
import com.ontimize.plaf.painter.OScrollBarTrackPainter;
import com.ontimize.plaf.painter.OScrollPanePainter;
import com.ontimize.plaf.painter.OSeparatorPainter;
import com.ontimize.plaf.painter.OSplitPaneDividerPainter;
import com.ontimize.plaf.painter.OTabbedPaneArrowButtonPainter;
import com.ontimize.plaf.painter.OTabbedPaneContentPainter;
import com.ontimize.plaf.painter.OTabbedPanePainter;
import com.ontimize.plaf.painter.OTabbedPaneTabAreaPainter;
import com.ontimize.plaf.painter.OTabbedPaneTabPainter;
import com.ontimize.plaf.painter.OTableButtonFooterPanelPainter;
import com.ontimize.plaf.painter.OTableButtonPanelPainter;
import com.ontimize.plaf.painter.OTableCellEditorPainter;
import com.ontimize.plaf.painter.OTableHeaderPainter;
import com.ontimize.plaf.painter.OTableHeaderRendererPainter;
import com.ontimize.plaf.painter.OTextAreaPainter;
import com.ontimize.plaf.painter.OTextFieldPainter;
import com.ontimize.plaf.painter.OToggleButtonPainter;
import com.ontimize.plaf.painter.OToolBarButtonPainter;
import com.ontimize.plaf.painter.OToolBarPainter;
import com.ontimize.plaf.painter.OToolBarSeparatorPainter;
import com.ontimize.plaf.painter.OToolBarToggleButtonPainter;
import com.ontimize.plaf.painter.OToolTipPainter;
import com.ontimize.plaf.painter.OTreeCellEditorPainter;
import com.ontimize.plaf.painter.OTreeCellRendererPainter;
import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.state.OComboBoxArrowButtonEditableState;
import com.ontimize.plaf.state.OComboBoxEditableState;
import com.ontimize.plaf.state.OProgressBarFinishedState;
import com.ontimize.plaf.state.OProgressBarIndeterminateState;
import com.ontimize.plaf.state.ORootPaneNoFrameState;
import com.ontimize.plaf.state.ORootPaneWindowFocusedState;
import com.ontimize.plaf.state.OSliderThumbArrowShapeState;
import com.ontimize.plaf.state.OSplitPaneDividerVerticalState;
import com.ontimize.plaf.state.OSplitPaneVerticalState;
import com.ontimize.plaf.state.OTableHeaderTableHeaderRendererSortedState;
import com.ontimize.plaf.state.OTextAreaNotInScrollPaneState;
import com.ontimize.plaf.state.OToolBarEastState;
import com.ontimize.plaf.state.OToolBarNorthState;
import com.ontimize.plaf.state.OToolBarSouthState;
import com.ontimize.plaf.state.OToolBarWestState;
import com.ontimize.plaf.state.RequiredState;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;
import com.ontimize.plaf.utils.OntimizeLAFParseUtils;
import com.ontimize.plaf.utils.ReflectionUtils;
import com.ontimize.plaf.utils.StyleUtil;
import com.ontimize.util.swing.ButtonSelection;
import com.ontimize.util.swing.CollapsibleButtonPanel;
import com.sun.java.swing.Painter;

/**
 * Ontimize Look And Feel development.
 * 
 * @author Imatia Innovation
 */
@SuppressWarnings("serial")
public class OntimizeLookAndFeel extends com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel {

	/**
	 * Used in a handful of places where we need an empty Insets.
	 */
	public static final Insets EMPTY_UIRESOURCE_INSETS = new InsetsUIResource(0, 0, 0, 0);

	public static final String UI_PACKAGE_PREFIX = "com.ontimize.plaf.ui.O";

	protected boolean decorated = false;

	protected boolean initialized = false;

	/** Refer to setSelectedUI */
	public static ComponentUI selectedUI;

	/** Refer to setSelectedUI */
	public static int selectedUIState;
	
	/**
	 * The number of pixels that compounds the border width of the component.
	 */
	public static int defaultNumBorders = 4;
	
	/**
	 * The radius of component corners.
	 */
	public static double defaultRadius = new Double(7);// Double.MAX_VALUE;

	/**
	 * Our fallback style to avoid NPEs if the proper style cannot be found in
	 * this class. Not sure if relying on DefaultSynthStyle is the best choice.
	 */
	protected DefaultSynthStyle defaultStyle;

	/**
	 * The default font that will be used. I store this value so that it can be
	 * set in the UIDefaults when requested.
	 */
	protected Font defaultFont;

	public static final String[] NIMBUS_COLORS_KEYS = new String[] { "nimbusSelectionBackground", "text", "nimbusSelectedText", "nimbusDisabledText", "nimbusLightBackground", "control", "info", "nimbusInfoBlue", "nimbusAlertYellow", "nimbusBase",
			"nimbusFocus", "nimbusGreen", "nimbusRed", "nimbusOrange", "activeCaption", "background", "controlDkShadow", "controlHighlight", "controlLHighlight", "controlShadow", "controlText", "desktop", "inactiveCaption", "infoText", "menu",
			"menuText", "nimbusBlueGrey", "nimbusBorder", "nimbusSelection", "scrollbar", "textBackground", "textForeground", "textHighlight", "textHighlightText", "textInactiveText" };

	/**
	 * Constructor method. Here it is indicated: - initialize the map of styles
	 * (for Ontimize components that must be configured regardless of other JAVA
	 * components. For instance, to allow users to configurate the Rows and
	 * Columns in other way that the other Panels in the look and feel, but
	 * using the same keys) 
	 */
	public OntimizeLookAndFeel() {
		super();

		defaultFont = getDefaultFont();
		defaultStyle = new DefaultSynthStyle();
		defaultStyle.setFont(defaultFont);

		/*
		 * Register all of the regions and their states that this class will use
		 * for later lookup. Additional regions can be registered later by 3rd
		 * party components. These are simply the default registrations.
		 */
		registerStyles();

	}

	@Override
	public String getName() {
		return "Ontimize L&F";
	}

	@Override
	public String getID() {
		return "Ontimize L&F";
	}

	@Override
	public String getDescription() {
		return "Ontimize Look and Feel";
	}

	@Override
	public boolean getSupportsWindowDecorations() {
		return decorated;
	}

	public static Object lookup(String s) {
		if (UIManager.getDefaults() != null)
			return UIManager.getDefaults().get(s);
		else
			return UIManager.get(s);
	}

	public static boolean isWindowOpacityEnabled(Window window) {
		// boolean flag = !getBoolean("Synthetica.window.opaque", window, true);
		boolean flag = false;
		// return !isJava6u10OrAbove() && OS.getCurrentOS() != OS.Mac || !flag;
		return !isJava6u10OrAbove() && !flag;
	}

	public static boolean isWindowShapeEnabled(Window window) {
		// String s = getString("Synthetica.window.shape", window);
		// return (isJava6u10OrAbove() || OS.getCurrentOS() == OS.Mac) &&
		// "ROUND_RECT".equals(s);
		return isJava6u10OrAbove();
	}

	public static void updateWindowShape(Window window) {
		// if(OS.getCurrentOS() == OS.Mac) {
		// setWindowOpaque(window, false);
		// return;
		// }
		// if(getJVMCompatibilityMode() != JVMCompatibilityMode.SUN)
		// return;
		// boolean flag = (window instanceof Frame) &&
		// (((Frame)window).getExtendedState() & 6) == 6;
		// try
		// {
		// Class class1 = Class.forName("com.sun.awt.AWTUtilities");
		// Method method = class1.getMethod("setWindowShape", new Class[] {
		// java/awt/Window, java/awt/Shape
		// });
		// String s = getString("Synthetica.window.shape", window);
		// java.awt.geom.RoundRectangle2D.Float float1 = null;
		// if(flag || !isWindowShapeEnabled(window))
		// float1 = null;
		// else
		// if("ROUND_RECT".equals(s))
		// {
		// int i = getInt("Synthetica.window.arcW", window, 18);
		// int j = getInt("Synthetica.window.arcH", window, 18);
		// float1 = new java.awt.geom.RoundRectangle2D.Float(0.0F, 0.0F,
		// window.getWidth(), window.getHeight(), i, j);
		// }
		// method.invoke(null, new Object[] {
		// window, float1
		// });
		// method = class1.getMethod("getWindowShape", new Class[] {
		// java/awt/Window
		// });
		// }
		// catch(Exception exception)
		// {
		// exception.printStackTrace();
		// }
	}

	protected static boolean isJava6u10OrAbove() {
		String s = System.getProperty("java.version");
		if (s.startsWith("1.5."))
			return false;
		if (s.startsWith("1.6.0_09"))
			return false;
		if (s.startsWith("1.6.0_08"))
			return false;
		if (s.startsWith("1.6.0_07"))
			return false;
		if (s.startsWith("1.6.0_06"))
			return false;
		if (s.startsWith("1.6.0_05"))
			return false;
		if (s.startsWith("1.6.0_04"))
			return false;
		if (s.startsWith("1.6.0_03"))
			return false;
		if (s.startsWith("1.6.0_02"))
			return false;
		if (s.startsWith("1.6.0_01"))
			return false;
		return !s.equals("1.6.0");
	}

	public static SynthContext createContext(JComponent component, Region region, int state) {
		SynthStyle synthstyle = getStyle(component, region);
		return new SynthContext(component, region, synthstyle, state);
	}

	protected Icon frameCloseIcon = null;
	protected Icon frameIconifyIcon = null;
	protected Icon frameMaximizeIcon = null;

	protected Icon createFrameCloseIcon() {
		if (frameCloseIcon == null) {
			URL url = getClass().getClassLoader().getResource("com/ontimize/plaf/images/closeIcon.png");
			frameCloseIcon = new ImageIcon(url);
		}
		return frameCloseIcon;
	}

	protected Icon createFrameIconifyIcon() {
		if (frameIconifyIcon == null) {
			URL url = getClass().getClassLoader().getResource("com/ontimize/plaf/images/iconifyIcon.png");
			frameIconifyIcon = new ImageIcon(url);
		}
		return frameIconifyIcon;
	}

	protected Icon createFrameMaximizeIcon() {
		if (frameMaximizeIcon == null) {
			URL url = getClass().getClassLoader().getResource("com/ontimize/plaf/images/maximizeIcon.png");
			frameMaximizeIcon = new ImageIcon(url);
		}
		return frameMaximizeIcon;
	}

	// public static Icon createFrameMinimizeIcon() {
	// if (frame_minIcon == null) {
	// frame_minIcon = new FrameButtonIcon(Part.WP_RESTOREBUTTON);
	// }
	// return frame_minIcon;
	// }
	
	 /**
     * Convenience method for setting a component's foreground
     * and background color properties with values from the
     * defaults.  The properties are only set if the current
     * value is either {@code null} or a {@code UIResource}. 
     * 
     * @param c component to set the colors on
     * @param defaultBgName key for the background
     * @param defaultFgName key for the foreground
     * 
     * @see #installColorsAndFont
     * @see UIManager#getColor
     * @throws NullPointerException as described in
     *         <a href="#exceptions">exceptions</a>
     */
	public static void installColors(JComponent c, String defaultBgName, String defaultFgName) {
		if (UIManager.getColor(defaultBgName) != null) { // bg == null || bg instanceof UIResource
			c.setBackground(UIManager.getColor(defaultBgName));
		}

		if (UIManager.getColor(defaultFgName) != null) {// fg == null || fg instanceof UIResource
			c.setForeground(UIManager.getColor(defaultFgName));
		}
	}


    /**
     * Convenience method for setting a component's foreground,
     * background and font properties with values from the
     * defaults.  The properties are only set if the current
     * value is either {@code null} or a {@code UIResource}.
     * 
     * @param c component set to the colors and font on
     * @param defaultBgName key for the background
     * @param defaultFgName key for the foreground
     * @param defaultFontName key for the font
     * @throws NullPointerException as described in
     *         <a href="#exceptions">exceptions</a>
     * 
     * @see #installColors
     * @see UIManager#getColor
     * @see UIManager#getFont
     */
	public static void installColorsAndFont(JComponent c, String defaultBgName, String defaultFgName,
			String defaultFontName) {
		if (UIManager.getFont(defaultFontName) != null) { // original -> f == null || f instanceof UIResource
			c.setFont(UIManager.getFont(defaultFontName));
		}
		installColors(c, defaultBgName, defaultFgName);
	}

	
	public static Font defaultAppFont = null;

	protected Font getDefaultFont() {
		
		if (defaultFont == null) {
			defaultFont = StyleUtil.getFont("Application", "font", null);
			
			if(defaultAppFont instanceof Font && defaultFont == null){
				defaultFont = defaultAppFont;
			}
			
			if (defaultFont == null) {
				try {
//					defaultFont = FontManager.getFontConfigFUIR("Arial", Font.PLAIN, 11);
					defaultFont = new FontUIResource(new Font("Arial", Font.PLAIN, 11));
				} catch (Throwable e) {
				}
			}
			
			if (defaultFont == null) {
				defaultFont = new Font("Arial", Font.PLAIN, 11);
			}
		}

		return defaultFont;
	}
	
	protected void defineDefaultFont(UIDefaults d) {
		if (d != null) {
			d.put("defaultFont", getDefaultFont());
		}
	}
	
	protected void defineTextPane(UIDefaults d){
		//Initialize TextPane
		String compName = "TextPane";
		
		setBoolean(d, compName, "opaque", "true");
		setInsetsUIResource(d, compName, "contentMargins", "4 6 4 6");
		
		setColor(d, compName, "selectionForeground", "#FFFFFF");
		setColor(d, compName, "selectionBackground", "#39698a");
		
		setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#243b4aCC");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		
		d.put(compName + "[Enabled].background", StyleUtil.getColorUI(compName, "[Enabled].background", "#FFFFFF"));
		d.put(compName + "[Disabled].background", StyleUtil.getColorUI(compName, "[Disabled].background", "#FFFFFF7D"));
		d.put(compName + "[Selected].background", StyleUtil.getColorUI(compName, "[Selected].background", "#FFFFFF"));
		
	}

	protected void defineTextFields(UIDefaults d) {

		// TextField: 
		String compName = "TextField";
		d.put(compName+".States", "Enabled,Disabled,Focused,Selected,Required");
		d.put(compName+".Required", new RequiredState());
		
		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		setInsetsUIResource(d, compName, "contentMargins", "4 14 4 14");
		
		setDouble(d, "Application", "radius", ((Double)defaultRadius).toString());
		
		setColorUIResource(d, compName, "textBackground", "#39698a");//Background of the selected text

		setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#243b4aCC");//	//oldvalue 3359718F
		setColorUIResource(d, compName, "[Focused].textForeground", "#61BEE8");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		ColorUIResource requiredFgColor = StyleUtil.getColorUI(compName, "[Required].textForeground", "#FFFFFF");
		DataField.requiredFieldForegroundColor = requiredFgColor;
		setColorUIResource(d, compName, "[Required].textForeground", "#FFFFFF");
		
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF");
		ColorUIResource disabledColor = StyleUtil.getColorUI(compName, "[Disabled].background", "#FFFFFF7D");
		d.put(compName + "[Disabled].background", disabledColor);
		DataField.defaultDisableBackgroundColor = disabledColor;
		ColorUIResource focusedColor = StyleUtil.getColorUI(compName, "[Focused].background", "#FFFFFF");
		d.put(compName + "[Focused].background", focusedColor);
		DataField.FOCUS_BACKGROUNDCOLOR = focusedColor;

		ColorUIResource requiredColor = StyleUtil.getColorUI(compName, "[Required].background", "#89A5B9");
		d.put(compName + "[Required].background", requiredColor);
		d.put(compName + "[Focused+Required].background", requiredColor);
		DataField.requiredFieldBackgroundColor = requiredColor;
		
		d.put(compName + "[Enabled].border", StyleUtil.getArrayColorUI(compName, "[Enabled].border", "#E5E5E57D"));
		d.put(compName + "[Required].border", StyleUtil.getArrayColorUI(compName, "[Required].border", "#E5E5E57D"));
		d.put(compName + "[Disabled].border", StyleUtil.getArrayColorUI(compName, "[Disabled].border", "#A5B6C0"));
		d.put(compName + "[Focused].border", StyleUtil.getArrayColorUI(compName, "[Focused].border", "#61BEE8FF #61BEE8B3 #61BEE866 #61BEE819"));
		d.put(compName + "[Focused].innerShadow", StyleUtil.getColor(compName, "[Focused].innerShadow", "#CACACA"));
		
		//Editor inner border...
		d.put(compName + ".editorInnerBorder", StyleUtil.getArrayColorUI(compName, "editorInnerBorder", "#BAf1FE #F3FdFF"));

		setColorUIResource(d, compName, "background", null);
		setColorUIResource(d, compName, "foreground", null);
		setColorUIResource(d, compName, "disabled", null);
		setColorUIResource(d, compName, "disabledText", null);

		String pClass = "com.ontimize.plaf.painter.OTextFieldPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "4 14 4 14"), new Dimension(122, 26), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[Required].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_REQUIRED, ctx));
		d.put(compName + "[Focused].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_FOCUSED, ctx));
		d.put(compName + "[Enabled].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_ENABLED, ctx));
		d.put(compName + "[Required].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_REQUIRED, ctx));
		d.put(compName + "[Focused+Required].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_FOCUSED_REQUIRED, ctx));
		d.put(compName + "[Disabled].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_DISABLED, ctx));

		// Initialize TextArea
		compName = "TextArea";

		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		setInsetsUIResource(d, compName, "contentMargins", "6 6 6 6");
		
		setColorUIResource(d, compName, "textBackground", "#39698a");//Background of the selected text
		
		setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		setColorUIResource(d, compName, "[Focused].textForeground", "#61BEE8");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		
		
		d.put(compName + "[Enabled].background", StyleUtil.getColorUI(compName, "[Enabled].background", "#FFFFFF"));
		d.put(compName + "[Required].background", StyleUtil.getColorUI(compName, "[Required].background", "#89A5B9"));
		d.put(compName + "[Disabled].background", StyleUtil.getColorUI(compName, "[Disabled].background", "#FFFFFF7D"));
		d.put(compName + "[Selected].background", StyleUtil.getColorUI(compName, "[Selected].background", "#FFFFFF"));

		d.put(compName + "[Enabled].border", StyleUtil.getArrayColorUI(compName, "[Enabled].border", "#E5E5E5"));
		d.put(compName + "[Disabled].border", StyleUtil.getArrayColorUI(compName, "[Disabled].border", "#A5B6C0"));
		d.put(compName + "[Focused].border", StyleUtil.getArrayColorUI(compName, "[Focused].border", "#61BEE8FF #61BEE8B3 #61BEE866 #61BEE819"));

		setColorUIResource(d, compName, "background", null);
		setColorUIResource(d, compName, "foreground", null);
		setColorUIResource(d, compName, "disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		
		// TextArea in scroll pane
		pClass = "com.ontimize.plaf.painter.OTextAreaPainter";
		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "6 6 6 6"), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + ".States", "Enabled,MouseOver,Pressed,Selected,Disabled,Focused,NotInScrollPane");
		d.put(compName + ".NotInScrollPane", new OTextAreaNotInScrollPaneState());
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OTextAreaPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OTextAreaPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Disabled].borderPainter", new LazyPainter(pClass, OTextAreaPainter.BORDER_DISABLED, ctx));
		d.put(compName + "[Focused].borderPainter", new LazyPainter(pClass, OTextAreaPainter.BORDER_FOCUSED, ctx));
		d.put(compName + "[Enabled].borderPainter", new LazyPainter(pClass, OTextAreaPainter.BORDER_ENABLED, ctx));

		// TextArea not in scroll pane
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OTextAreaPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[Disabled+NotInScrollPane].backgroundPainter", new LazyPainter(pClass, OTextAreaPainter.BACKGROUND_DISABLED_NOTINSCROLLPANE, ctx));
		d.put(compName + "[Enabled+NotInScrollPane].backgroundPainter", new LazyPainter(pClass, OTextAreaPainter.BACKGROUND_ENABLED_NOTINSCROLLPANE, ctx));
		d.put(compName + "[Disabled+NotInScrollPane].borderPainter", new LazyPainter(pClass, OTextAreaPainter.BORDER_DISABLED_NOTINSCROLLPANE, ctx));
		d.put(compName + "[Focused+NotInScrollPane].borderPainter", new LazyPainter(pClass, OTextAreaPainter.BORDER_FOCUSED_NOTINSCROLLPANE, ctx));
		d.put(compName + "[Enabled+NotInScrollPane].borderPainter", new LazyPainter(pClass, OTextAreaPainter.BORDER_ENABLED_NOTINSCROLLPANE, ctx));
	}
	
	protected void definePassword(UIDefaults d){
		String compName = "PasswordField";
        
        setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		setInsetsUIResource(d, compName, "contentMargins", "4 14 4 14");
		
		setDouble(d, "Application", "radius", ((Double)defaultRadius).toString());

		setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#243b4aCC");
		setColorUIResource(d, compName, "[Focused].textForeground", "#61BEE8");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[Required].textForeground", "#FFFFFF");
		
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF");
		ColorUIResource requiredColor = StyleUtil.getColorUI(compName, "[Required].background", "#89A5B9");
		d.put(compName + "[Required].background", requiredColor);
		
		d.put(compName + "[Enabled].border", StyleUtil.getArrayColorUI(compName, "[Enabled].border", "#E5E5E57D"));
		d.put(compName + "[Required].border", StyleUtil.getArrayColorUI(compName, "[Required].border", "#E5E5E57D"));
		d.put(compName + "[Disabled].border", StyleUtil.getArrayColorUI(compName, "[Disabled].border", "#A5B6C0"));
		d.put(compName + "[Focused].border", StyleUtil.getArrayColorUI(compName, "[Focused].border", "#61BEE8FF #61BEE8B3 #61BEE866 #61BEE819"));
		d.put(compName + "[Focused].innerShadow", StyleUtil.getColor(compName, "[Focused].innerShadow", "#CACACA"));
		
		//Editor inner border...
		d.put(compName + ".editorInnerBorder", StyleUtil.getArrayColorUI(compName, "editorInnerBorder", "#BAf1FE #F3FdFF"));

		setColorUIResource(d, compName, "background", null);
		setColorUIResource(d, compName, "foreground", null);
		setColorUIResource(d, compName, "disabled", null);
		setColorUIResource(d, compName, "disabledText", null);

		String pClass = "com.ontimize.plaf.painter.OTextFieldPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "4 14 4 14"), new Dimension(122, 26), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[Required].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_REQUIRED, ctx));
		d.put(compName + "[Focused].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_FOCUSED, ctx));
		d.put(compName + "[Enabled].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_ENABLED, ctx));
		d.put(compName + "[Disabled].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_DISABLED, ctx));
		d.put(compName + "[Required].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_REQUIRED, ctx));

	}
	
	protected void defineReferenceExtComponent(UIDefaults d){
		String compName = "TextField:\"TextField.ReferenceExt\"";
		
		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		setInsetsUIResource(d, compName, "contentMargins", "4 14 4 14");
		
		setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#243b4aCC");
		setColorUIResource(d, compName, "[Focused].textForeground", "#61BEE8");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[Required].textForeground", "#FFFFFF");
		
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF");
		ColorUIResource requiredColor = StyleUtil.getColorUI(compName, "[Required].background", "#89A5B9");
		d.put(compName + "[Required].background", requiredColor);
		
		ColorUIResource disabledColor = StyleUtil.getColorUI(compName, "[Disabled].background", "#FFFFFF7D");
		d.put(compName + "[Disabled].background", disabledColor);
		ColorUIResource focusedColor = StyleUtil.getColorUI(compName, "[Focused].background", "#FFFFFF");
		d.put(compName + "[Focused].background", focusedColor);
		d.put(compName + "[Enabled].border", StyleUtil.getArrayColorUI(compName, "[Enabled].border", "#E5E5E57D"));
		d.put(compName + "[Required].border", StyleUtil.getArrayColorUI(compName, "[Required].border", "#E5E5E57D"));
		d.put(compName + "[Disabled].border", StyleUtil.getArrayColorUI(compName, "[Disabled].border", "#A5B6C0"));
		d.put(compName + "[Focused].border", StyleUtil.getArrayColorUI(compName, "[Focused].border", "#61BEE8FF #61BEE8B3 #61BEE866 #61BEE819"));
		d.put(compName + "[Focused].innerShadow", StyleUtil.getColor(compName, "[Focused].innerShadow", "#CACACA"));
		

		String pClass = "com.ontimize.plaf.painter.OReferenceExtFieldPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "4 14 4 14"), new Dimension(122, 26), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[Required].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_REQUIRED, ctx));
		d.put(compName + "[Focused].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_FOCUSED, ctx));
		d.put(compName + "[Enabled].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_ENABLED, ctx));
		d.put(compName + "[Disabled].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_DISABLED, ctx));
		d.put(compName + "[Required].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_REQUIRED, ctx));
		d.put(compName + "[Focused+Required].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_FOCUSED_REQUIRED, ctx));
		
		
		compName = "TextField:\"TextField.ReferenceExtCode\"";
		
		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		setInsetsUIResource(d, compName, "contentMargins", "4 14 4 14");
		
		setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#243b4aCC");
		setColorUIResource(d, compName, "[Focused].textForeground", "#61BEE8");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[Required].textForeground", "#FFFFFF");
		
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF");
		requiredColor = StyleUtil.getColorUI(compName, "[Required].background", "#89A5B9");
		d.put(compName + "[Required].background", requiredColor);
		
		disabledColor = StyleUtil.getColorUI(compName, "[Disabled].background", "#FFFFFF7D");
		d.put(compName + "[Disabled].background", disabledColor);
		focusedColor = StyleUtil.getColorUI(compName, "[Focused].background", "#FFFFFF");
		d.put(compName + "[Focused].background", focusedColor);
		d.put(compName + "[Enabled].border", StyleUtil.getArrayColorUI(compName, "[Enabled].border", "#E5E5E57D"));
		d.put(compName + "[Required].border", StyleUtil.getArrayColorUI(compName, "[Required].border", "#E5E5E57D"));
		d.put(compName + "[Disabled].border", StyleUtil.getArrayColorUI(compName, "[Disabled].border", "#A5B6C0"));
		d.put(compName + "[Focused].border", StyleUtil.getArrayColorUI(compName, "[Focused].border", "#61BEE8FF #61BEE8B3 #61BEE866 #61BEE819"));
		d.put(compName + "[Focused].innerShadow", StyleUtil.getColor(compName, "[Focused].innerShadow", "#CACACA"));
		

		pClass = "com.ontimize.plaf.painter.OReferenceExtCodeFieldPainter";
		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "4 14 4 14"), new Dimension(122, 26), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[Required].backgroundPainter", new LazyPainter(pClass, OTextFieldPainter.BACKGROUND_REQUIRED, ctx));
		d.put(compName + "[Focused].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_FOCUSED, ctx));
		d.put(compName + "[Enabled].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_ENABLED, ctx));
		d.put(compName + "[Disabled].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_DISABLED, ctx));
		d.put(compName + "[Required].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_REQUIRED, ctx));
		d.put(compName + "[Focused+Required].borderPainter", new LazyPainter(pClass, OTextFieldPainter.BORDER_FOCUSED_REQUIRED, ctx));
		
	}

	protected void defineQuickFilter(UIDefaults d) {

		// QuickFilter...
		String compName = "Table:\"Table.QuickFilter\"";
		String pClass = "com.ontimize.plaf.painter.OQuickFilterPainter";
		
		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		setInsetsUIResource(d, compName, "contentMargins", "4 14 4 30");

		setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		setColorUIResource(d, compName, "[Focused].textForeground", "#61BEE8");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF");
		
		ColorUIResource disabledColor = StyleUtil.getColorUI(compName, "[Disabled].background", "#FFFFFF7D");
		d.put(compName + "[Disabled].background", disabledColor);
		ColorUIResource focusedColor = StyleUtil.getColorUI(compName, "[Focused].background", "#FFFFFF");
		d.put(compName + "[Focused].background", focusedColor);

		d.put(compName + "[Enabled].border", StyleUtil.getArrayColorUI(compName, "[Enabled].border", "#E5E5E57D"));
		d.put(compName + "[Disabled].border", StyleUtil.getArrayColorUI(compName, "[Disabled].border", "#A5B6C0"));
		d.put(compName + "[Focused].border", StyleUtil.getArrayColorUI(compName, "[Focused].border", "#61BEE8FF #61BEE8B3 #61BEE866 #61BEE819"));
		d.put(compName + "[Focused].innerShadow", StyleUtil.getColor(compName, "[Focused].innerShadow", "#CACACA"));
		
		d.put(compName + ".icon", StyleUtil.getProperty(compName, "icon", "com/ontimize/plaf/images/queryfilter.png"));
		
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(4, 14, 4, 30), new Dimension(122, 26), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OQuickFilterPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OQuickFilterPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OQuickFilterPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[Focused].borderPainter", new LazyPainter(pClass, OQuickFilterPainter.BORDER_FOCUSED, ctx));
		d.put(compName + "[Enabled].borderPainter", new LazyPainter(pClass, OQuickFilterPainter.BORDER_ENABLED, ctx));
		d.put(compName + "[Disabled].borderPainter", new LazyPainter(pClass, OQuickFilterPainter.BORDER_DISABLED, ctx));

		QuickFieldText.paintFindText = false;
	}

	protected void defineEditorPane(UIDefaults d) {
		// Initialize EditorPane
		String compName = "EditorPane";
		d.put(compName + ".contentMargins", new InsetsUIResource(10, 10, 10, 10));
		d.put(compName + ".opaque", Boolean.TRUE);

		d.put(compName + "[Enabled].textForeground", StyleUtil.getColorUI(compName, "[Enabled].textForeground", "#335971"));
		d.put(compName + "[Disabled].textForeground", StyleUtil.getColorUI(compName, "[Disabled].textForeground", "#335971"));
		d.put(compName + "[Focused].textForeground", StyleUtil.getColorUI(compName, "[Focused].textForeground", "#61BEE8"));
		d.put(compName + "[Selected].textForeground", StyleUtil.getColorUI(compName, "[Selected].textForeground", "#FFFFFF"));

		d.put(compName + "[Enabled].background", StyleUtil.getColorUI(compName, "[Enabled].background", "#FFFFFF"));
		d.put(compName + "[Disabled].background", StyleUtil.getColorUI(compName, "[Disabled].background", "#FFFFFF7D"));
		d.put(compName + "[Selected].background", StyleUtil.getColorUI(compName, "[Selected].background", "#FFFFFF"));

		d.put(compName + "[Enabled].border", StyleUtil.getArrayColorUI(compName, "[Enabled].border", "#E5E5E5"));
		d.put(compName + "[Disabled].border", StyleUtil.getArrayColorUI(compName, "[Disabled].border", "#A5B6C0"));
		d.put(compName + "[Focused].border", StyleUtil.getArrayColorUI(compName, "[Focused].border", "#61BEE8FF #61BEE8B3 #61BEE866 #61BEE819"));

		String pClass = "com.ontimize.plaf.painter.OEditorPanePainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(10, 10, 10, 10), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OEditorPanePainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OEditorPanePainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OEditorPanePainter.BACKGROUND_SELECTED, ctx));
		d.put(compName + "[Disabled].borderPainter", new LazyPainter(pClass, OTextAreaPainter.BORDER_DISABLED, ctx));
		d.put(compName + "[Focused].borderPainter", new LazyPainter(pClass, OTextAreaPainter.BORDER_FOCUSED, ctx));
		d.put(compName + "[Enabled].borderPainter", new LazyPainter(pClass, OTextAreaPainter.BORDER_ENABLED, ctx));

	}
	
	protected void defineFileChooser(UIDefaults d){
		String compName = "FileChooser";
		
		setInsetsUIResource(d, compName, "contentMargins", "10 10 10 10");
		setBoolean(d, compName, "opaque", "true");
		setBoolean(d, compName, "usesSingleFilePane", "true");
		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		
		setColorUIResource(d, compName, "[Enabled].background", "#366581");
		setColor(d, compName, "background", "#366581");
		
		String pClass = "com.ontimize.plaf.painter.OFileChooserPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(10, 10, 10, 10), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OEditorPanePainter.BACKGROUND_ENABLED, ctx));
		
	}
	
	protected void defineOptionPane(UIDefaults d){
		String compName = "OptionPane";
		
		// Initialize OptionPane
		setColorUIResource(d, compName, "background", "#366581");
		//MessageForeground must be Color not ColorUIResource, with UI color is not fixed.
		setColor(d, compName, "messageForeground", "#ffffff");
		setInteger(d, compName, "buttonMinimumWidth", "60");
		
	}

	protected void defineScrollPane(UIDefaults d) {

		// Initialize ScrollPane
		String compName = "ScrollPane";
		String painterClass = "com.ontimize.plaf.painter.OScrollPanePainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(4, 4, 4, 4), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + ".contentMargins", new InsetsUIResource(4, 4, 4, 4));
		setBoolean(d, compName, "opaque", "true");
		setBoolean(d, compName, "useChildTextComponentFocus", "true");

		d.put(compName + "[Enabled+Focused].borderPainter", new LazyPainter(painterClass, OScrollPanePainter.BORDER_ENABLED_FOCUSED, ctx));
		d.put(compName + "[Enabled].borderPainter", new LazyPainter(painterClass, OScrollPanePainter.BORDER_ENABLED, ctx));
		d.put(compName + "[Disabled].borderPainter", new LazyPainter(painterClass, OScrollPanePainter.BORDER_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(painterClass, OScrollPanePainter.BACKGROUND_ENABLED, ctx));

		// Store ScrollPane Corner Component
		d.put(compName + ".cornerPainter", new LazyPainter(painterClass, OScrollPanePainter.CORNER_ENABLED, ctx));
	}

	protected void defineViewport(UIDefaults d) {

//		// Initialize Viewport
		d.put("Viewport.contentMargins", new InsetsUIResource(0, 0, 0, 0));
		d.put("Viewport.opaque", Boolean.FALSE);
	}

	protected void defineScrollBar(UIDefaults d) {

		// ScrollBar:
		String compName = "ScrollBar";
		
		setInsetsUIResource(d, compName, "contentMargins", "6 6 6 6");
		setFontUIResource(d, compName, "font", null);
		setBoolean(d, compName, "opaque", "true");
		setInteger(d, compName, "decrementButtonGap", "0");//0
		setInteger(d, compName, "incrementButtonGap", "0");//0
		setInteger(d, compName, "thumbHeight", "12");
		d.put(compName + ".minimumThumbSize", new DimensionUIResource(29, 29));
		d.put(compName + ".maximumThumbSize", new DimensionUIResource(1000, 1000));
		
		setColorUIResource(d, compName, "foreground", null);
		setColorUIResource(d, compName, "background", "#ffffff01");
		setColorUIResource(d, compName, "disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		
		setColorUIResource(d, compName, "[Enabled].border", "#FFFFFF");
		setColorUIResource(d, compName, "[Disabled].border", "#FFFFFF");
		
		String pClass = "com.ontimize.plaf.painter.OScrollBarPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "6 6 6 6"), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + ".States", "Enabled,Disabled");
		d.put(compName + "[Enabled].borderPainter", new LazyPainter(pClass, OScrollBarPainter.BORDER_ENABLED, ctx));
		d.put(compName + "[Disabled].borderPainter", new LazyPainter(pClass, OScrollBarPainter.BORDER_DISABLED, ctx));

		// ScrollBar button:
		compName="ScrollBar:\"ScrollBar.button\"";
		pClass = "com.ontimize.plaf.painter.OScrollBarButtonPainter";
		
		setInsetsUIResource(d, compName, "contentMargins", "0 0 0 0");
		setInteger(d, compName, "size", "25");
		
		setColorUIResource(d, compName, "[Enabled].background", "#BDBDBD");
		setColorUIResource(d, compName, "[Disabled].background", "#D0DAE2");
		setColorUIResource(d, compName, "[MouseOver].background", "#BDBDBD");
		setColorUIResource(d, compName, "[Pressed].background", "#9D9D9D");
		
		setColorUIResource(d, compName, "[Enabled].arrowBackground", "#FFFFFF");
		setColorUIResource(d, compName, "[Disabled].arrowBackground", "#FFFFFF");
		setColorUIResource(d, compName, "[MouseOver].arrowBackground", "#FFFFFF");
		setColorUIResource(d, compName, "[Pressed].arrowBackground", "#FFFFFF");
		
		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "0 0 0 0"), new Dimension(25, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);
		d.put(compName + "[Enabled].foregroundPainter", new LazyPainter(pClass, OScrollBarButtonPainter.FOREGROUND_ENABLED, ctx));
		d.put(compName + "[Disabled].foregroundPainter", new LazyPainter(pClass, OScrollBarButtonPainter.FOREGROUND_DISABLED, ctx));
		d.put(compName + "[MouseOver].foregroundPainter", new LazyPainter(pClass, OScrollBarButtonPainter.FOREGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Pressed].foregroundPainter", new LazyPainter(pClass, OScrollBarButtonPainter.FOREGROUND_PRESSED, ctx));

		// ScrollBar thumb :
		compName = "ScrollBar:ScrollBarThumb";
		pClass = "com.ontimize.plaf.painter.OScrollBarThumbPainter";
		setInsetsUIResource(d, compName, "contentMargins", "2 2 2 2");
		
		setColorUIResource(d, compName, "[Enabled].background", "#36627F");
		setColorUIResource(d, compName, "[MouseOver].background", "#36627F");
		setColorUIResource(d, compName, "[Pressed].background", "#36627F");
		setColorUIResource(d, compName, "[Enabled].border", "#36627F");
		setColorUIResource(d, compName, "[MouseOver].border", "#36627F");
		setColorUIResource(d, compName, "[Pressed].border", "#36627F");
		setColorUIResource(d, compName, "[Enabled].backgroundShadow", "#FFFFFF25");
		setColorUIResource(d, compName, "[MouseOver].backgroundShadow", "#FFFFFF25");
		setColorUIResource(d, compName, "[Pressed].backgroundShadow", "#FFFFFF25");
		
		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "2 2 2 2"), new Dimension(38, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, 2.0);
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OScrollBarThumbPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, OScrollBarThumbPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, OScrollBarThumbPainter.BACKGROUND_PRESSED, ctx));

		// ScrollBar track :
		compName = "ScrollBar:ScrollBarTrack";
		pClass = "com.ontimize.plaf.painter.OScrollBarTrackPainter";
		
		setInsetsUIResource(d, compName, "contentMargins", "5 5 5 5");
		
		setColorUIResource(d, compName, "[Enabled].background", "#E6E6E6");
		setColorUIResource(d, compName, "[Disabled].background", "#EEF1F4");
		
		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "5 5 5 5"), new Dimension(18, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, 2.0);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OScrollBarTrackPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OScrollBarTrackPainter.BACKGROUND_ENABLED, ctx));

	}

	protected void defineTable(UIDefaults d) {

		// Table:
		String compName = "Table";
		
		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(this.defaultFont));
		setInsetsUIResource(d, compName, "contentMargins", "2 2 2 2");
		setInteger(d, compName, "rowHeight", "22");
		setBoolean(d, compName, "opaque", "true");
		setBoolean(d, compName, "showGrid", "true");
		setColorUIResource(d, compName, "gridColor", "#ffffff");
		setColorUIResource(d, compName, "disabledText", null);
		setColorUIResource(d, compName, "disabled", null);
		
		setColorUIResource(d, compName, "foreground", "#3A5F77");
		setColorUIResource(d, compName, "textForeground", "#3A5F77");
		setColorUIResource(d, compName, "TextBackground", "#9cb2c1");
		setColorUIResource(d, compName, "background", "#c3d5dd");
		setColor(d, compName, "alternateRowColor", "#Dae7Ed");
		setColorUIResource(d, compName, "alternateOddRowColor", "#B6CAD6");
		setColorUIResource(d, compName, "alternateEvenRowColor", "#A6BDCB");
		setBoolean(d, compName, "useOddEvenAlternateRowColor", "true");
		setBoolean(d, compName, "rendererUseTableColors", "true");
		setBoolean(d, compName, "rendererUseUIBorder", "true");
		
		setColorUIResource(d, compName, "selectionBackground", "#F2F8F9");
		setColorUIResource(d, compName, "selectionForeground", "#2E8ECB");
		
		d.put("Table.cellNoFocusBorder", new BorderUIResource(BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        Color borderColor = StyleUtil.getColor(compName, "[Selected].border", "#2E8ECB");
		Border outsideBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor);
		Border insideBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		d.put("Table.focusCellHighlightBorder", new BorderUIResource(BorderFactory.createCompoundBorder(outsideBorder, insideBorder)));
		
		setColorUIResource(d, compName, "[Enabled+Selected].textForeground", "#2E8ECB");	//Color used for default TableCellRenderes of swing
		setColorUIResource(d, compName, "[Enabled+Selected].textBackground", "#F2F8F9");	//Color used for default TableCellRenderes of swing
		setColorUIResource(d, compName, "[Disabled+Selected].textBackground", "#F2F8F9");
		

		// ************Ontimize Table Configuration***********
		Table.defaultTableOpaque = Boolean.TRUE;
		Table.defaultTableBackgroundColor = StyleUtil.getColor("Table", "background", "#9CB2C1");
		Table.MIN_ROW_HEIGHT = 22;
		borderColor = StyleUtil.getColorUI("Table", "border", "#ADC0CE");
		BorderUIResource tBorder = new BorderUIResource(BorderFactory.createLineBorder(borderColor, 2));
		BorderManager.putBorder(BorderManager.DEFAULT_TABLE_BORDER_KEY, tBorder);
		d.put("Table.scrollPaneBorder", tBorder);
		
		
		compName = "\"PageFetcher.Label\"";
		setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		setColorUIResource(d, compName, "[Focused].textForeground", "#61BEE8");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		
		setColor(d, compName, "foreground", "#335971");
		setColor(d, compName, "disabled", "#243b4aCC");
		setColor(d, compName, "disabledText", "#243b4aCC");

		// Buttons configuration...
		ImageManager.TABLE_SUMROWSETUP = "com/ontimize/plaf/images/table/calc.png";
		ImageManager.TABLE_CALCULATEDCOLS = "com/ontimize/plaf/images/table/calculate.png";
		ImageManager.TABLE_CHART = "com/ontimize/plaf/images/table/chart.png";
		ImageManager.TABLE_DEFAULT_CHARTS = "com/ontimize/plaf/images/table/chart.png";
		ImageManager.TABLE_CONF_VISIBLE_COLS = "com/ontimize/plaf/images/table/confvisiblecols.png";
		ImageManager.TABLE_CONF_VISIBLE_COLS_RED = "com/ontimize/plaf/images/table/confvisiblecolsred.png";
		ImageManager.TABLE_COPY = "com/ontimize/plaf/images/table/copy.png";
		ImageManager.TABLE_EXCEL = "com/ontimize/plaf/images/table/excel.png";
		ImageManager.TABLE_INSERT = "com/ontimize/plaf/images/table/insert.png";
		ImageManager.TABLE_HTML = "com/ontimize/plaf/images/table/internet.png";
		ImageManager.TABLE_REPORTS = "com/ontimize/plaf/images/table/page.png";
		ImageManager.TABLE_PIVOT = "com/ontimize/plaf/images/table/pivot.png";
		ImageManager.TABLE_PRINT = "com/ontimize/plaf/images/table/print.png";
		ImageManager.TABLE_REMOVE = "com/ontimize/plaf/images/table/recycler.png";
		ImageManager.TABLE_REFRESH = "com/ontimize/plaf/images/table/refresh.png";
		ImageManager.TABLE_SAVE_TABLE_FILTER = "com/ontimize/plaf/images/table/savetablefilter.png";
		ImageManager.TABLE_GROUP = "com/ontimize/plaf/images/table/groupicons.png";

	}

	protected void defineTableCellRenderer(UIDefaults d) {
		// Table:"Table.cellRenderer
		String compName = "Table:\"Table.cellRenderer\"";
//		d.put("Table:\"Table.cellRenderer\".contentMargins", new InsetsUIResource(1, 6, 1, 6));
//		d.put("Table:\"Table.cellRenderer\".opaque", Boolean.FALSE);
//		d.put("Table:\"Table.cellRenderer\".background", OntimizeLAFColorUtils.setAlpha(Color.red, 0.5));
		
		// ************Ontimize Table Configuration***********
		CellRenderer.font = StyleUtil.getFont(compName, "font", OntimizeLAFParseUtils.fontToString(this.defaultFont));
		CellRenderer.fontColor = StyleUtil.getColor(compName, "foreground", "#3A5F77");
		CellRenderer.evenRowBackgroundColor = StyleUtil.getColor(compName, "evenRowBackground", "#A6BDCB");
		CellRenderer.oddRowBackgroundColor = StyleUtil.getColor(compName, "oddRowBackground", "#B6CAD6");
		CellRenderer.emptyBorder = BorderFactory.createEmptyBorder(0, 6, 0, 6);

		CellRenderer.editableFontColor = StyleUtil.getColor(compName, "[Editable].foreground", "#3A5F77");
		CellRenderer.evenEditableBackgroundColor = StyleUtil.getColor(compName, "[Editable].evenEditableBackground", "#c3d5dd");
		CellRenderer.oddEditableBackgroundColor = StyleUtil.getColor(compName, "[Editable].oddEditableBackground", "#dae7ed");
		
		CellRenderer.requiredInsertColumns = StyleUtil.getColor(compName, "[Insertable].requiredBackground", "#b8bacb");
		CellRenderer.noRequiredInsertColumns = StyleUtil.getColor(compName, "[Insertable].norequiredBackground", "#cdced9");

		CellRenderer.selectedFontColor = StyleUtil.getColor(compName, "[Selected].foreground", "#2e8ecb");
		CellRenderer.selectedEditableBackgroundColor = StyleUtil.getColor(compName, "[Selected+Editable].background", "#ffffff");
		CellRenderer.selectedBackgroundColor = StyleUtil.getColor(compName, "[Selected].background", "#ffffff");
		Color borderColor = StyleUtil.getColor(compName, "[Selected].border", "#2E8ECB");
		Border outsideBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor);
		Border insideBorder = BorderFactory.createEmptyBorder(0, 6, 0, 6);
		CellRenderer.focusBorder = BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
		CellRenderer.focusBorderColor = borderColor;

		// Row number column...
		compName = "Table:\"Table.rowHeadCellRenderer\"";
		RowHeadCellRenderer.rowNumberForeground = StyleUtil.getColor(compName, "foreground", "#dae7ed");
		Font rNFont = getDefaultFont();
		rNFont = rNFont.deriveFont((float)rNFont.getSize()+3);
		RowHeadCellRenderer.rowNumberFont = StyleUtil.getFont(compName, "font", OntimizeLAFParseUtils.fontToString(rNFont));
		RowHeadCellRenderer.rowNumberBackground = StyleUtil.getColor(compName, "[Enabled].background", "#6387a0");
		borderColor = StyleUtil.getColor(compName, "border", "#FFFFFF");
		RowHeadCellRenderer.rowNumberBorder = BorderFactory.createEmptyBorder();

		// SumCell Renderer
		compName = "Table:\"Table.sumCellRenderer\"";
		SumCellRenderer.backgroundColor = StyleUtil.getColor(compName, "[Enabled].background", "#80B721");
		SumCellRenderer.foregroundColor = StyleUtil.getColor(compName, "foreground", "#FFFFFF");
		SumCellRenderer.disabledBackgroundColor = StyleUtil.getColor(compName, "[Disabled].background", "#9CB2C1");

		// VisualCalendar Component...
		compName = "Table:\"VisualCalendar:Table.cellRenderer\"";
		String pClass = "com.ontimize.plaf.painter.OTableVisualCalendarCellRendererPainter";
		d.put(compName + ".States", "Enabled");
		d.put(compName + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
		d.put(compName + ".opaque", Boolean.TRUE);
		Color fgColor = StyleUtil.getColor(compName, "foreground", "#6b7e75");
		d.put(compName + "[Enabled].textForeground", fgColor);
		d.put(compName + ".foreground", fgColor);
		d.put(compName + ".TextForeground", fgColor);

		VisualCalendarComponent.DayRenderer.dayOfWeekFgColor = StyleUtil.getColor(compName, "dayOfWeekFgColor", "#6b7e75");
		VisualCalendarComponent.DayRenderer.dayOfWeekEndFgColor = StyleUtil.getColor(compName, "dayOfWeekEndFgColor", "#a8b7c0");
		VisualCalendarComponent.DayRenderer.daySelectedFgColor = StyleUtil.getColor(compName, "daySelectedFgColor", "#ffffff");
		
		setColor(d, compName, "[Selected].background", "#5cb1ea");
		setColor(d, compName, "[Selected].dayColor", "#bcd3e3");
		setColor(d, compName, "evenColumnBackground", "#ffffff");
		setColor(d, compName, "oddColumnBackground", "#F3F3F0");
		
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OTableHeaderRendererPainter.BACKGROUND_ENABLED, ctx));

	}

	protected void defineTableCellEditor(UIDefaults d) {

		String compName = "\"Table.editor\"";
		String pClass = "com.ontimize.plaf.painter.OTableCellEditorPainter";
		d.put(compName + ".contentMargins", new InsetsUIResource(0, 6, 0, 6));
		d.put(compName + ".opaque", Boolean.TRUE);
		d.put(compName + "[Enabled].textForeground", StyleUtil.getColorUI(compName, "[Enabled].textForeground", "#31c7fc"));
		d.put(compName + "[Selected].textForeground", StyleUtil.getColorUI(compName, "[Selected].textForeground", "#FFFFFF"));
		
		PaintContext ctx = new PaintContext(new Insets(1, 1, 1, 1), new Dimension(31, 17), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass,	OTableCellEditorPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Enabled+Focused].backgroundPainter", new LazyPainter(pClass,	OTableCellEditorPainter.BACKGROUND_ENABLED_FOCUSED, ctx));

		// ************Ontimize Table Configuration***********
		compName = "Table:\"Table.cellEditor\"";
		Color borderColor = StyleUtil.getColor(compName, ".border", "#4cb5d7");
		Border insideBorder = BorderFactory.createEmptyBorder(0, 6, 0, 6);
		Border outsideBorder = BorderFactory.createLineBorder(borderColor);
		CellEditor.focusBorder = BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
		CellEditor.fontColor = StyleUtil.getColor(compName, "foreground", "#31c7fc");
		CellEditor.backgroundColor = StyleUtil.getColor(compName, "background", "#FFFFFF");
		CellEditor.font = StyleUtil.getFont(compName, "font", OntimizeLAFParseUtils.fontToString(this.defaultFont));
		
		d.put("Table.cellEditorBorder", new TableCellEditorBorder(CellEditor.focusBorder));

	}
	
	public class TableCellEditorBorder extends BorderUIResource{
		protected Border delegate_;
		
		public TableCellEditorBorder(Border delegate){
			super(delegate);
			this.delegate_ = delegate;
		}
		
		public Border getDelegateBorder(){
			return this.delegate_;
		}
	}

	protected void defineTableHeader(UIDefaults d) {
		// TableHeader:
		String compName = "TableHeader";

		Font tHFont = getDefaultFont();
		tHFont = tHFont.deriveFont((float)tHFont.getSize()+1);
		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(tHFont));
		/*
		 * NOTE:
		 * //Insets fixed, they can't be changed by the user. If user wants to change insets,
		 * he could change them into table header renderer.
		 */
		d.put(compName + ".contentMargins", new InsetsUIResource(0, 0, 0, 0)); 
		setBoolean(d, compName, "opaque", "true");
		setBoolean(d, compName, "rightAlignSortArrow", "false");
		
		setColorUIResource(d, compName, "foreground", "#647b8b");
		setColorUIResource(d, compName, "background", "#e5edf0");
		setColorUIResource(d, compName, "disabledText", null);
		setColorUIResource(d, compName, "disabled", null);

		SortTableCellRenderer.defaultForegroundFilterColor = StyleUtil.getColor(compName, "foregroundFilter", "#2E8ECb");
		SortTableCellRenderer.paintSortIcon = true;

		d.put("Table.ascendingSortIcon", new OntimizeSynthIcon("TableHeader", "ascendingSortIconPainter", 31, 17));
		d.put("Table.descendingSortIcon", new OntimizeSynthIcon("TableHeader", "descendingSortIconPainter", 31, 17));
		
		setColorUIResource(d, compName, "ascendingSortIconBackground", "#80b721");
		setColorUIResource(d, compName, "descendingSortIconBackground", "#e64718");

		String pClass = "com.ontimize.plaf.painter.OTableHeaderPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(0,0,0,0), new Dimension(31, 17), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);
		d.put(compName + "[Enabled].ascendingSortIconPainter", new LazyPainter(pClass, OTableHeaderPainter.ASCENDINGSORTICON_ENABLED, ctx));
		d.put(compName + "[Enabled].descendingSortIconPainter", new LazyPainter(pClass, OTableHeaderPainter.DESCENDINGSORTICON_ENABLED, ctx));

	}

	protected void defineTableHeaderRenderer(UIDefaults d) {

		// TableHeader: renderer :
		String compName = "TableHeader:\"TableHeader.renderer\"";
		
		d.put(compName + ".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Sorted");
		d.put(compName + ".Sorted", new OTableHeaderTableHeaderRendererSortedState());
		
		setInsetsUIResource(d, compName, "contentMargins", "2 2 2 2");
		Insets insets = StyleUtil.getInsets(compName, "contentMargins", "2 2 2 2");
		SortTableCellRenderer.defaultVerticalHeaderMargin = 4 + insets.top+insets.bottom;//4 is defaultVerticalHeaderMargin in Ontimize
		
		Font tHRFont = getDefaultFont();
		tHRFont = tHRFont.deriveFont((float)tHRFont.getSize()+1);
		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(tHRFont));
		setBoolean(d, compName, "opaque", "true");

		d.put(compName + ".backgroundDegraded", StyleUtil.getArrayColor(compName, "backgroundDegraded", "#aab7bf #e5edf0"));
		setColorUIResource(d, compName, "textForeground", "#647b8b");
		setColorUIResource(d, compName, "outsideRightBorder", "#858d92");
		setColorUIResource(d, compName, "insideRightBorder", "#FFFFFF");
		setColorUIResource(d, compName, "bottomBorder", "#858d92");
		
		String pClass = "com.ontimize.plaf.painter.OTableHeaderRendererPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(insets, new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OTableHeaderRendererPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OTableHeaderRendererPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Enabled+Focused].backgroundPainter", new LazyPainter(pClass, OTableHeaderRendererPainter.BACKGROUND_ENABLED_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, OTableHeaderRendererPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, OTableHeaderRendererPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Enabled+Sorted].backgroundPainter", new LazyPainter(pClass, OTableHeaderRendererPainter.BACKGROUND_ENABLED_SORTED, ctx));
		d.put(compName + "[Enabled+Focused+Sorted].backgroundPainter", new LazyPainter(pClass, OTableHeaderRendererPainter.BACKGROUND_ENABLED_FOCUSED_SORTED, ctx));
		d.put(compName + "[Disabled+Sorted].backgroundPainter", new LazyPainter(pClass, OTableHeaderRendererPainter.BACKGROUND_DISABLED_SORTED, ctx));

		// VisualCalendar Component...
		compName = "TableHeader:\"VisualCalendar:TableHeader.renderer\"";
		pClass = "com.ontimize.plaf.painter.OTableHeaderVisualCalendarRendererPainter";
		d.put(compName + ".States", "Enabled");
		
		setInsetsUIResource(d, compName, "contentMargins", "5 5 5 5");
		setColorUIResource(d, compName, "foreground", "#FFFFFF");
		setColorUIResource(d, compName, "foregroundShadow", "00000033");
		
		setColorUIResource(d, compName, "topBorder", "#959ea3");
		setColorUIResource(d, compName, "bottomBorder", "#b6bdbf");

		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "5 5 5 5"), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OTableHeaderRendererPainter.BACKGROUND_ENABLED, ctx));
	}

	protected void defineLabel(UIDefaults d) {
		// Label
		String compName = "Label";
		setFont(d, compName, "font", OntimizeLAFParseUtils.fontToString(this.defaultFont));
		d.put(compName + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
		
		/*
		 * Note:
		 * Colors with state ([Enables]...) are used on renderers of Combos, Lists, etc.
		 * Due to, background of this components is white, the color of the font is #335971
		 */
		setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#243b4aCC");
		setColorUIResource(d, compName, "[Focused].textForeground", "#61BEE8");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		/*
		 * Generic colors of the Label when used alone, that is, when label background is not set
		 * and then, white color is used.
		 */
		setColor(d, compName, "background", "#FFFFFF");
		setColor(d, compName, "foreground", "#ffffff");
		setColor(d, compName, "disabled", "#FFFFFF7D");
		setColor(d, compName, "disabledText", "#243b4aCC");
		
	}
	
	protected void defineELabel(UIDefaults d) {
		String prefix = "\"ELabel\"";
		// Label
//		d.put(prefix + "font", StyleUtil.getFont(prefix, "font", OntimizeLAFParseUtils.fontToString(this.defaultFont) ));
//		d.put(prefix + "contentMargins", new InsetsUIResource(0, 0, 0, 0));
		
		setFont(d, prefix, "font", OntimizeLAFParseUtils.fontToString(this.defaultFont));
		setInsetsUIResource(d, prefix, "contentMargins", "0 0 0 0");

		setColorUIResource(d, prefix, "foreground", "#FFFFFF");
		setColorUIResource(d, prefix, "[Enabled].textForeground", "#FFFFFF");
		setColorUIResource(d, prefix, "[Disabled].textForeground", "#FFFFFF");
		setColorUIResource(d, prefix, "disabledText", "#FFFFFF");

	}

	protected void defineButton(UIDefaults d) {

		// Button
		String compName = "Button";

		setInsetsUIResource(d, compName, "contentMargins", "7 7 7 7");
		setBoolean(d, compName, "defaultButtonFollowsFocus", "false");
		setFontUIResource(d, compName, "font", null);
		setColorUIResource(d, compName,"disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		setColorUIResource(d, compName, "[Enabled].textForeground", null);
		setColorUIResource(d, compName, "[Focused].textForeground", null);
		setColorUIResource(d, compName, "[MouseOver].textForeground", null);
		setColorUIResource(d, compName, "[Focused+MouseOver].textForeground", null);
		setColorUIResource(d, compName, "[Pressed].textForeground", null);
		setColorUIResource(d, compName, "[Focused+Pressed].textForeground", null);
		
		setColorUIResource(d, compName, "[Default].textForeground", null);
		setColorUIResource(d, compName, "[Default+Focused].textForeground", null);
		setColorUIResource(d, compName, "[Default+MouseOver].textForeground", null);
		setColorUIResource(d, compName, "[Default+Focused+MouseOver].textForeground", null);
		setColorUIResource(d, compName, "[Default+Pressed].textForeground", null);
		setColorUIResource(d, compName, "[Default+Focused+Pressed].textForeground", null);
		
		setColorUIResource(d, compName, "background", "#FFFFFF");
		
		setFloat(d, compName, "[Default].alphaTransparency", "0.5");
		setFloat(d, compName, "[Default+Focused].alphaTransparency", "0.5");
		setFloat(d, compName, "[Default+MouseOver].alphaTransparency", "0.5");
		setFloat(d, compName, "[Default+Focused+MouseOver].alphaTransparency", "0.5");
		setFloat(d, compName, "[Default+Pressed].alphaTransparency", "0.5");
		setFloat(d, compName, "[Default+Focused+Pressed].alphaTransparency", "0.5");
		
		setFloat(d, compName, "[Disabled].alphaTransparency", "0.5");
		setFloat(d, compName, "[Enabled].alphaTransparency", "0.3");
		setFloat(d, compName, "[Focused].alphaTransparency", "0.3");
		setFloat(d, compName, "[MouseOver].alphaTransparency", "0.5");
		setFloat(d, compName, "[Focused+MouseOver].alphaTransparency", "0.5");
		setFloat(d, compName, "[Pressed].alphaTransparency", "0.5");
		setFloat(d, compName, "[Focused+Pressed].alphaTransparency", "0.5");
		
		
		String pClass = "com.ontimize.plaf.painter.OButtonPainter";
		PaintContext ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "7 7 7 7"), new Dimension(104, 33), false, PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

		d.put(compName + "[Default].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT, ctx));
		d.put(compName + "[Default+Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT_FOCUSED, ctx));

		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
	}
	
	protected void defineToggleButton(UIDefaults d){
		String compName = "ToggleButton";
		
		setInsetsUIResource(d, compName, "contentMargins", "7 7 7 7");
		setFontUIResource(d, compName, "font", null);
		setBoolean(d, compName, "defaultButtonFollowsFocus", "false");
		setColorUIResource(d, compName,"disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		setColorUIResource(d, compName, "[Enabled].textForeground", null);
		setColorUIResource(d, compName, "[Focused].textForeground", null);
		setColorUIResource(d, compName, "[MouseOver].textForeground", null);
		setColorUIResource(d, compName, "[Focused+MouseOver].textForeground", null);
		setColorUIResource(d, compName, "[Pressed].textForeground", null);
		setColorUIResource(d, compName, "[Focused+Pressed].textForeground", null);
		
		setColorUIResource(d, compName, "[Selected].textForeground", null);
		setColorUIResource(d, compName, "[Disabled+Selected].textForeground", null);
		setColorUIResource(d, compName, "[Focused+Selected].textForeground", null);
		setColorUIResource(d, compName, "[MouseOver+Selected].textForeground", null);
		setColorUIResource(d, compName, "[Focused+MouseOver+Selected].textForeground", null);
		setColorUIResource(d, compName, "[Pressed+Selected].textForeground", null);
		setColorUIResource(d, compName, "[Focused+Pressed+Selected].textForeground", null);
		
		setColor(d, compName, "background", "#FFFFFF");
		
		setFloat(d, compName, "[Disabled].alphaTransparency", "0.5");
		setFloat(d, compName, "[Enabled].alphaTransparency", "0.3");
		setFloat(d, compName, "[Focused].alphaTransparency", "0.3");
		setFloat(d, compName, "[MouseOver].alphaTransparency", "0.5");
		setFloat(d, compName, "[Focused+MouseOver].alphaTransparency", "0.5");
		setFloat(d, compName, "[Pressed].alphaTransparency", "0.5");
		setFloat(d, compName, "[Focused+Pressed].alphaTransparency", "0.5");
		
		setFloat(d, compName, "[Selected].alphaTransparency", "0.5");
		setFloat(d, compName, "[Disabled+Selected].alphaTransparency", "0.5");
		setFloat(d, compName, "[Focused+Selected].alphaTransparency", "0.5");
		setFloat(d, compName, "[MouseOver+Selected].alphaTransparency", "0.5");
		setFloat(d, compName, "[Focused+MouseOver+Selected].alphaTransparency", "0.5");
		setFloat(d, compName, "[Pressed+Selected].alphaTransparency", "0.5");
		setFloat(d, compName, "[Focused+Pressed+Selected].alphaTransparency", "0.5");
		
		String pClass = "com.ontimize.plaf.painter.OToggleButtonPainter";
		PaintContext ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "7 7 7 7"), new Dimension(104, 33), false, PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
		
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_SELECTED, ctx));
		d.put(compName + "[Disabled+Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_DISABLED_SELECTED, ctx));
		d.put(compName + "[Focused+Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_SELECTED_FOCUSED, ctx));
		d.put(compName + "[MouseOver+Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_MOUSEOVER_SELECTED, ctx));
		d.put(compName + "[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_MOUSEOVER_SELECTED_FOCUSED, ctx));
		d.put(compName + "[Pressed+Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_PRESSED_SELECTED, ctx));
		d.put(compName + "[Focused+Pressed+Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_PRESSED_SELECTED_FOCUSED, ctx));
		
	}
	
	protected void defineSelectableItem(UIDefaults d){
		String compName = "\"SelectableItem\"";
		String pClass = "com.ontimize.plaf.painter.OCheckBoxPainter";
		String iconBasePath = "com/ontimize/plaf/images/check/";
		
		setColorUIResource(d, compName, "background", "#ffffff01");
//		setColorUIResource(d, compName, "foreground", null);
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "foreground", "#335971");
		setColorUIResource(d, compName, "disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		
		setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#243b4aCC");
		setColorUIResource(d, compName, "[Focused].textForeground", "#335971");
		setColorUIResource(d, compName, "[Selected].textForeground", "#335971");
		
		PaintContext ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "0 0 0 0"), new Dimension(180, 180), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);

		d.put(compName + ".contentMargins", StyleUtil.getInsetsUI(compName, "contentMargins", "0 0 0 0"));
		// // addColor(d, "CheckBox[Disabled].textForeground",
		// "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
		String path = StyleUtil.getIconPath(compName, "[Disabled].icon", iconBasePath + "18x18_ico_checkbox_disabled.png");
		d.put(compName + "[Disabled].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_DISABLED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Enabled].icon", iconBasePath + "18x18_ico_checkbox_enabled.png");
		d.put(compName + "[Enabled].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_ENABLED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused].icon", iconBasePath + "18x18_ico_checkbox_focused.png");
		d.put(compName + "[Focused].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[MouseOver].icon", iconBasePath + "18x18_ico_checkbox_mouseover.png");
		d.put(compName + "[MouseOver].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_MOUSEOVER, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+MouseOver].icon", iconBasePath + "18x18_ico_checkbox_mouseover-focused.png");
		d.put(compName + "[Focused+MouseOver].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_MOUSEOVER_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Pressed].icon", iconBasePath + "18x18_ico_checkbox_pressed.png");
		d.put(compName + "[Pressed].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_PRESSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Pressed].icon", iconBasePath + "18x18_ico_checkbox_pressed-focused.png");
		d.put(compName + "[Focused+Pressed].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_PRESSED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Selected].icon", iconBasePath + "18x18_ico_checkbox_selected.png");
		d.put(compName + "[Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-focused.png");
		d.put(compName + "[Focused+Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_SELECTED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Pressed+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-pressed.png");
		d.put(compName + "[Pressed+Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_PRESSED_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Pressed+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-pressed-focused.png");
		d.put(compName + "[Focused+Pressed+Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_PRESSED_SELECTED_FOCUSED, ctx, path));
		
		path = StyleUtil.getIconPath(compName, "[MouseOver+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-mouseover.png");
		d.put(compName + "[MouseOver+Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_MOUSEOVER_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+MouseOver+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-mouseover-focussed.png");
		d.put(compName + "[Focused+MouseOver+Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_MOUSEOVER_SELECTED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Disabled+Selected].icon", iconBasePath + "18x18_ico_checkbox_disabled-selected.png");
		d.put(compName + "[Disabled+Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_DISABLED_SELECTED, ctx, path));

	}

	protected void defineCheckBox(UIDefaults d) {
		// //Initialize CheckBox
		String compName = "CheckBox";
		String pClass = "com.ontimize.plaf.painter.OCheckBoxPainter";
		String iconBasePath = "com/ontimize/plaf/images/check/";
		
		
		setColorUIResource(d, compName, "background", "#ffffff01");
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "foreground", "#335971");
		setColorUIResource(d, compName, "disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		
		setColorUIResource(d, compName, "[Enabled].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#FFFFFF7D");
		setColorUIResource(d, compName, "[Focused].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");

		PaintContext ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "0 0 0 0"), new Dimension(180, 180), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);

		d.put(compName + ".contentMargins", StyleUtil.getInsetsUI(compName, "contentMargins", "0 0 0 0"));
		// // addColor(d, "CheckBox[Disabled].textForeground",
		// "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
		String path = StyleUtil.getIconPath(compName, "[Disabled].icon", iconBasePath + "18x18_ico_checkbox_disabled.png");
		d.put(compName + "[Disabled].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_DISABLED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Enabled].icon", iconBasePath + "18x18_ico_checkbox_enabled.png");
		d.put(compName + "[Enabled].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_ENABLED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused].icon", iconBasePath + "18x18_ico_checkbox_focused.png");
		d.put(compName + "[Focused].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[MouseOver].icon", iconBasePath + "18x18_ico_checkbox_mouseover.png");
		d.put(compName + "[MouseOver].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_MOUSEOVER, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+MouseOver].icon", iconBasePath + "18x18_ico_checkbox_mouseover-focused.png");
		d.put(compName + "[Focused+MouseOver].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_MOUSEOVER_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Pressed].icon", iconBasePath + "18x18_ico_checkbox_pressed.png");
		d.put(compName + "[Pressed].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_PRESSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Pressed].icon", iconBasePath + "18x18_ico_checkbox_pressed-focused.png");
		d.put(compName + "[Focused+Pressed].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_PRESSED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Selected].icon", iconBasePath + "18x18_ico_checkbox_selected.png");
		d.put(compName + "[Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-focused.png");
		d.put(compName + "[Focused+Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_SELECTED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Pressed+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-pressed.png");
		d.put(compName + "[Pressed+Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_PRESSED_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Pressed+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-pressed-focused.png");
		d.put(compName + "[Focused+Pressed+Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_PRESSED_SELECTED_FOCUSED, ctx, path));
		
		path = StyleUtil.getIconPath(compName, "[MouseOver+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-mouseover.png");
		d.put(compName + "[MouseOver+Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_MOUSEOVER_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+MouseOver+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-mouseover-focussed.png");
		d.put(compName + "[Focused+MouseOver+Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_MOUSEOVER_SELECTED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Disabled+Selected].icon", iconBasePath + "18x18_ico_checkbox_disabled-selected.png");
		d.put(compName + "[Disabled+Selected].iconPainter", new LazyPainter(pClass, OCheckBoxPainter.ICON_DISABLED_SELECTED, ctx, path));

	}

	protected void defineComboBox(UIDefaults d) {

		// Initialize ComboBox
		String compName = "ComboBox";
		setFontUIResource(d, compName, "font", null);
		setInsetsUIResource(d, compName, "contentMargins", "2 13 2 4");//0 5 0 2
		
		d.put(compName + ".States", "Enabled,MouseOver,Pressed,Selected,Disabled,Focused,Editable");
		d.put(compName + ".Editable", new OComboBoxEditableState());
		d.put(compName + ".forceOpaque", Boolean.TRUE);
		d.put(compName + ".buttonWhenNotEditable", Boolean.TRUE);
		d.put(compName + ".rendererUseListColors", Boolean.FALSE);
		d.put(compName + ".pressedWhenPopupVisible", Boolean.TRUE);
		d.put(compName + ".squareButton", Boolean.FALSE);
		d.put(compName + ".popupInsets", new InsetsUIResource(-2, 2, 0, 2));
		d.put(compName + ".padding", new InsetsUIResource(0, 0, 0, 0));
		
		d.put(compName + ".numBorders", new Integer(4));
		
		setColor(d, compName, "background", "#FFFFFF");
		setColorUIResource(d, compName, "foreground", "#335971");
		setColorUIResource(d, compName, "disabled", "#FFFFFF7D");
		setColorUIResource(d, compName, "disabledText", "#8F9CA4");
		setColor(d, compName, "selectionForeground", "#FFFFFF");
		setColor(d, compName, "selectionBackground", "#36627F");

		setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		setColorUIResource(d, compName, "[Focused].textForeground", "#61BEE8");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[Required].textForeground", "#FFFFFF");
		
		setColorUIResource(d, compName, "[Disabled].background", "#FFFFFF7D");
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF");
		setColorUIResource(d, compName, "[Focused].background", "#FFFFFF");
		setColorUIResource(d, compName, "[Required].background", "#89A5B9");
		setColorUIResource(d, compName, "[Focused+MouseOver].background", "#FFFFFF");
		setColorUIResource(d, compName, "[MouseOver].background", "#FFFFFF");
		setColorUIResource(d, compName, "[Pressed].background", "#FFFFFF");
		setColorUIResource(d, compName, "[Focused+Pressed].background", "#FFFFFF");
		
		d.put(compName + "[Enabled].border", StyleUtil.getArrayColorUI(compName, "[Enabled].border", "#E5E5E57D"));
		d.put(compName + "[Required].border", StyleUtil.getArrayColorUI(compName, "[Required].border", "#E5E5E57D"));
		d.put(compName + "[Disabled].border", StyleUtil.getArrayColorUI(compName, "[Disabled].border", "#A5B6C0"));
		d.put(compName + "[Focused].border", StyleUtil.getArrayColorUI(compName, "[Focused].border", "#61BEE8FF #61BEE8B3 #61BEE866 #61BEE819"));

		String pClass = "com.ontimize.plaf.painter.OComboBoxPainter";
		PaintContext ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "2 13 2 4"), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, 2.0);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Disabled+Pressed].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_DISABLED_PRESSED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Enabled+Selected].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_ENABLED_SELECTED, ctx));
		d.put(compName + "[Disabled+Editable].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_DISABLED_EDITABLE, ctx));
		d.put(compName + "[Editable+Enabled].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_ENABLED_EDITABLE, ctx));
		d.put(compName + "[Editable+Focused].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_FOCUSED_EDITABLE, ctx));
		d.put(compName + "[Editable+MouseOver].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_MOUSEOVER_EDITABLE, ctx));
		d.put(compName + "[Editable+Pressed].backgroundPainter", new LazyPainter(pClass, OComboBoxPainter.BACKGROUND_PRESSED_EDITABLE, ctx));

		// ************ComboBox.TextField*******************************
		compName = "ComboBox:\"ComboBox.textField\"";
		pClass = "com.ontimize.plaf.painter.OComboBoxTextFieldPainter";
		
		setInsetsUIResource(d, compName, "contentMargins", "0 0 0 0");
//		d.put(compName + "[Selected].textForeground", new Color(0xFFFFFF));
		
		setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[Required].textForeground", "#FFFFFF");
		
		setColorUIResource(d, compName, "[Disabled].background", "#FFFFFF7D");
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF");
		setColorUIResource(d, compName, "[Selected].background", "#FFFFFF");

		ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "0 0 0 0"), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, 2.0);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OComboBoxTextFieldPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OComboBoxTextFieldPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OComboBoxTextFieldPainter.BACKGROUND_SELECTED, ctx));

		// *************ComboBox.Arrow******************************
		compName = "ComboBox:\"ComboBox.arrowButton\"";
		d.put(compName + ".contentMargins", new InsetsUIResource(1, 1, 1, 1));
		d.put(compName + ".States", "Enabled,MouseOver,Pressed,Disabled,Editable");
		d.put(compName + ".Editable", new OComboBoxArrowButtonEditableState());
		d.put(compName + ".size", new Integer(21));
		
		/*
		 *Note: background arrow button is painted into OComboBoxPainter. 
		 */
		setColorUIResource(d, compName, "[Disabled].background", "#263945");
		setColorUIResource(d, compName, "[Enabled].background", "#263945");
		setColorUIResource(d, compName, "[Focused].background", "#263945");
		setColorUIResource(d, compName, "[MouseOver].background", "#263945");
		setColorUIResource(d, compName, "[Pressed].background", "#263945");
		
		setColorUIResource(d, compName, "[Disabled+Editable].background", "#263945");
		setColorUIResource(d, compName, "[Editable+Enabled].background", "#263945");
		setColorUIResource(d, compName, "[Editable+Focused].background", "#263945");
		setColorUIResource(d, compName, "[Editable+MouseOver].background", "#263945");
		setColorUIResource(d, compName, "[Editable+Pressed].background", "#263945");
		
		setColor(d, compName, "[Disabled].foreground", "#FFFFFF");
		setColor(d, compName, "[Enabled].foreground", "#FFFFFF");
		setColor(d, compName, "[Selected].foreground", "#000000");
		setColor(d, compName, "[MouseOver].foreground", "#000000");
		setColor(d, compName, "[Pressed].foreground", "#000000");

		pClass = "com.ontimize.plaf.painter.OComboBoxArrowButtonPainter";
		ctx = new PaintContext(new Insets(1, 1, 1, 1), new Dimension(20, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, 2.0);
		//because it is registered into NimbusDefaults
		d.remove(compName + "[Disabled+Editable].backgroundPainter");
		d.remove(compName + "[Editable+Enabled].backgroundPainter");
		d.remove(compName + "[Editable+MouseOver].backgroundPainter");
		d.remove(compName + "[Editable+Pressed].backgroundPainter");
		d.remove(compName + "[Editable+Selected].backgroundPainter");
		
		d.put(compName + "[Enabled].foregroundPainter", new LazyPainter(pClass, OComboBoxArrowButtonPainter.FOREGROUND_ENABLED, ctx));
		d.put(compName + "[MouseOver].foregroundPainter", new LazyPainter(pClass, OComboBoxArrowButtonPainter.FOREGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Disabled].foregroundPainter", new LazyPainter(pClass, OComboBoxArrowButtonPainter.FOREGROUND_DISABLED, ctx));
		d.put(compName + "[Pressed].foregroundPainter", new LazyPainter(pClass, OComboBoxArrowButtonPainter.FOREGROUND_PRESSED, ctx));
		d.put(compName + "[Selected].foregroundPainter", new LazyPainter(pClass, OComboBoxArrowButtonPainter.FOREGROUND_SELECTED, ctx));

		// **************ComboBox.ListRenderer****************
		compName = "ComboBox:\"ComboBox.listRenderer\"";
		setInsetsUIResource(d, compName, "contentMargins", "2 10 2 6");
		setBoolean(d, compName, "opaque", "true");
		setColor(d, compName, "background", "#ffffff");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8e8f91");
		setColor(d, compName, "[Selected].textForeground", "#ffffff");
		setColorUIResource(d, compName, "[Selected].background", "#36627F");

		// *************ComboBox.Renderer*************************
		compName = "ComboBox:\"ComboBox.renderer\"";
		setInsetsUIResource(d, compName, "contentMargins", "2 4 2 4");
		setColorUIResource(d, compName, "background", "#ffffff");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8e8f91");
		setColor(d, compName, "[Selected].textForeground", "#ffffff");
		setColorUIResource(d, compName, "[Selected].background", "#36627F");

		// *************ComboBox.scrollPane*************************
		d.put("\"ComboBox.scrollPane\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
		ColorUIResource borderColor = StyleUtil.getColorUI( "\"ComboBox.scrollPane\"" , "border", "#8CA0AD");
		Border cBorder = BorderFactory.createLineBorder(borderColor, 2);
		d.put("\"ComboBox.scrollPane\".border", cBorder); 
		
	}

	protected void defineMenu(UIDefaults d) {

		// Menu:
		String compName = "Menu";

		d.put(compName + "[Disabled].textForeground", StyleUtil.getColorUI(compName, "[Disabled].textForeground", "#FFFFFF7D"));
		d.put(compName + "[Enabled].textForeground", StyleUtil.getColorUI(compName, "[Enabled].textForeground", "#FFFFFF"));
		d.put(compName + "[Enabled+Selected].textForeground", StyleUtil.getColorUI(compName, "[Enabled+Selected].textForeground", "#426a84"));

		String pClass = "com.ontimize.plaf.painter.OMenuPainter";
		// Background...
		d.put(compName + ".contentMargins", StyleUtil.getInsetsUI(compName, "contentMargins", "3 10 3 10"));
		d.put(compName + "[Enabled+Selected].background", StyleUtil.getColor(compName, "[Enabled+Selected].background", "#86adbf"));
		d.put(compName + "[Enabled+Selected].border", StyleUtil.getColor(compName, "[Enabled+Selected].border", "#c6dfe3"));

		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "3 10 3 10"), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);
		d.put(compName + "[Enabled+Selected].backgroundPainter", new LazyPainter(pClass, OMenuPainter.BACKGROUND_ENABLED_SELECTED, ctx));

		// Arrow...
		d.put(compName + "[Enabled].arrowBackground", StyleUtil.getColor(compName, "[Enabled].arrowBackground", "#FFFFFF7D"));
		d.put(compName + "[Disabled].arrowBackground", StyleUtil.getColor(compName, "[Disabled].arrowBackground", "#0000007D"));
		d.put(compName + "[Enabled+Selected].arrowBackground", StyleUtil.getColor(compName, "[Enabled+Selected].arrowBackground", "#0000004C"));

		d.put(compName + ".arrowBackground_upperShadow", StyleUtil.getColor(compName, "arrowBackground_upperShadow", "#00000033"));
		d.put(compName + ".arrowBackground_bottomShadow", StyleUtil.getColor(compName, "arrowBackground_bottomShadow", "#ffffff7d"));
		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(1, 1, 1, 1), new Dimension(6, 13), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);
		d.put(compName + "[Disabled].arrowIconPainter", new LazyPainter(pClass, OMenuPainter.ARROWICON_DISABLED, ctx));
		d.put(compName + "[Enabled].arrowIconPainter", new LazyPainter(pClass, OMenuPainter.ARROWICON_ENABLED, ctx));
		d.put(compName + "[Enabled+Selected].arrowIconPainter", new LazyPainter(pClass, OMenuPainter.ARROWICON_ENABLED_SELECTED, ctx));
	}

	protected void defineMenuBar(UIDefaults d) {

		// MenuBar:
		String compName = "MenuBar";
		String pClass = "com.ontimize.plaf.painter.OMenuBarPainter";

		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		setInsetsUIResource(d, compName, "contentMargins", "1 5 0 5");
		
		setColorUIResource(d, compName, "background", "#1C304B");
		setColorUIResource(d, compName, "[Enabled].background", "#1C304B");
		setColorUIResource(d, compName, "[Enabled].border", "#1C304B");
		
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "1 5 0 5"), new Dimension(18, 22), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 2.0, 2.0);
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OMenuBarPainter.BACKGROUND_ENABLED, ctx));

		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "1 5 0 5"), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 2.0, 2.0);
		d.put(compName + "[Enabled].borderPainter", new LazyPainter(pClass, OMenuBarPainter.BORDER_ENABLED, ctx));
		
		
		//MenuBar:Menu
		compName = "MenuBar:Menu";
		pClass = "com.ontimize.plaf.painter.OMenuBarMenuPainter";
		
		setColorUIResource(d, compName, "[Enabled].textForeground", "#B3CAd8");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#B3CAd87D");
		setColorUIResource(d, compName, "[Selected].textForeground", "#1C304B");
		
		setColorUIResource(d, compName, "[Selected].background", "#86ADBF");
		setColorUIResource(d, compName, "[Selected].border", "#C6DFE3");
		
		setFontUIResource(d, compName, "font", null);
		setInsetsUIResource(d, compName, "contentMargins", "3 15 0 15");
		
		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "3 15 0 15"), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OMenuBarMenuPainter.BACKGROUND_SELECTED, ctx));
	}

	protected void defineMenuItem(UIDefaults d) {

		// MenuItem:
		String compName = "MenuItem";
		d.put(compName + ".States", "Enabled,MouseOver,Disabled");
		setInteger(d, compName, "textIconGap", "5");
		setFontUIResource(d, compName, "font", null);

		setColorUIResource(d, compName, "[Disabled].textForeground", "#FFFFFF7F");
		setColorUIResource(d, compName, "[Enabled].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[MouseOver].textForeground", "#426A84");

		setColorUIResource(d, compName, "[Disabled].background", "#FFFFFF01");
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF01");
		setColorUIResource(d, compName, "[MouseOver].background", "#FFFFFF");

		String pClass = "com.ontimize.plaf.painter.OMenuItemPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "2 10 2 10"), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);
		d.put(compName + ".contentMargins", StyleUtil.getInsetsUI(compName, "contentMargins", "2 10 2 10"));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, OMenuItemPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OMenuItemPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OMenuItemPainter.BACKGROUND_DISABLED, ctx));
		
		//MenuItemAccelerator
		setColorUIResource(d, compName, ":MenuItemAccelerator[Disabled].textForeground", "#FFFFFF7F");
		setColorUIResource(d, compName, ":MenuItemAccelerator[Enabled].textForeground", "#FFFFFF7F");
		setColorUIResource(d, compName, ":MenuItemAccelerator[MouseOver].textForeground", "#426A84");

	}

	protected void definePopupItem(UIDefaults d) {
		String compName = "\"PopupItem\"";
		d.put(compName + ".States", "Enabled,MouseOver,Disabled");
		d.put(compName + ".textIconGap", new Integer(5));

		setColorUIResource(d, compName, "[Disabled].textForeground", "#FFFFFF7F");
		setColorUIResource(d, compName, "[Enabled].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[MouseOver].textForeground", "#426A84");

		setColorUIResource(d, compName, "[Disabled].background", "#FFFFFF01");
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF01");
		setColorUIResource(d, compName, "[MouseOver].background", "#FFFFFF");

		String pClass = "com.ontimize.plaf.painter.OPopupItemPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "1 10 1 10"), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);
		d.put(compName + ".contentMargins", StyleUtil.getInsetsUI(compName, "contentMargins", "1 10 1 10"));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, OPopupItemPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OPopupItemPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OPopupItemPainter.BACKGROUND_DISABLED, ctx));

	}

	protected void defineRadioButtonMenuItem(UIDefaults d) {
		// RadioButtonMenuItem:
		String compName = "RadioButtonMenuItem";
		String pClass = "com.ontimize.plaf.painter.ORadioButtonMenuItemPainter";
		String iconBasePath = "com/ontimize/plaf/images/radio/";

		setFontUIResource(d, compName, "font", null);
		setInsetsUIResource(d, compName, "contentMargins", "2 10 2 10");
		setInteger(d, compName, "textIconGap", null);
		
		setColorUIResource(d, compName, "foreground", null);
		setColorUIResource(d, compName, "[Disabled].textForeground", "#FFFFFF7D");
		setColorUIResource(d, compName, "[Enabled].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[MouseOver].textForeground", "#426A84");
		setColorUIResource(d, compName, "[MouseOver+Selected].textForeground", "#426A84");
		
		setColorUIResource(d, compName, "background", "#FFFFFF");
		setColorUIResource(d, compName, "[Disabled].background", "#FFFFFF01");
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF01");
		setColorUIResource(d, compName, "[Selected].background", "#FFFFFF01");
		setColorUIResource(d, compName, "[MouseOver].background", "#FFFFFF");
		setColorUIResource(d, compName, "[MouseOver+Selected].background", "#FFFFFF");
		
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "2 10 2 10"), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[MouseOver+Selected].backgroundPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.BACKGROUND_SELECTED_MOUSEOVER, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.BACKGROUND_DISABLED, ctx));

		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "1 10 1 10"), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);

		String path = StyleUtil.getIconPath(compName, "[Disabled].icon", iconBasePath + "18x18_ico_radiobutton_disabled.png");
		d.put(compName + "[Disabled].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_DISABLED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Enabled].icon", iconBasePath + "18x18_ico_radiobutton_enabled.png");
		d.put(compName + "[Enabled].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_ENABLED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused].icon", iconBasePath + "18x18_ico_radiobutton_focused.png");
		d.put(compName + "[Focused].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[MouseOver].icon", iconBasePath + "18x18_ico_radiobutton_mouseover.png");
		d.put(compName + "[MouseOver].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_MOUSEOVER, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+MouseOver].icon", iconBasePath + "18x18_ico_radiobutton_mouseover-focused.png");
		d.put(compName + "[Focused+MouseOver].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_MOUSEOVER_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Pressed].icon", iconBasePath + "18x18_ico_radiobutton_pressed.png");
		d.put(compName + "[Pressed].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_PRESSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Pressed].icon", iconBasePath + "18x18_ico_radiobutton_pressed-focused.png");
		d.put(compName + "[Focused+Pressed].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_PRESSED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Enabled+Selected].icon", iconBasePath + "18x18_ico_radiobutton_selected.png");
		d.put(compName + "[Enabled+Selected].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Selected].icon", iconBasePath + "18x18_ico_radiobutton_selected-focused.png");
		d.put(compName + "[Focused+Selected].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_SELECTED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Pressed+Selected].icon", iconBasePath + "18x18_ico_radiobutton_selected-pressed.png");
		d.put(compName + "[Pressed+Selected].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_PRESSED_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Pressed+Selected].icon", iconBasePath + "18x18_ico_radiobutton_selected-pressed-focused.png");
		d.put(compName + "[Focused+Pressed+Selected].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_PRESSED_SELECTED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[MouseOver+Selected].icon", iconBasePath + "18x18_ico_radiobutton_selected-mouseover.png");
		d.put(compName + "[MouseOver+Selected].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_MOUSEOVER_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+MouseOver+Selected].icon", iconBasePath + "18x18_ico_radiobutton_selected-mouseover-focused.png");
		d.put(compName + "[Focused+MouseOver+Selected].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_MOUSEOVER_SELECTED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Disabled+Selected].icon", iconBasePath + "18x18_ico_radiobutton_disabled-selected.png");
		d.put(compName + "[Disabled+Selected].checkIconPainter", new LazyPainter(pClass, ORadioButtonMenuItemPainter.CHECKICON_ICON_DISABLED_SELECTED, ctx, path));
		
	}

	protected void defineCheckBoxMenuItem(UIDefaults d) {

		// CheckBoxMenuItem :
		String compName = "CheckBoxMenuItem";
		String pClass = "com.ontimize.plaf.painter.OCheckBoxMenuItemPainter";
		String iconBasePath = "com/ontimize/plaf/images/check/";

		setFontUIResource(d, compName, "font", null);
		setInsetsUIResource(d, compName, "contentMargins", "2 10 2 10");
		setInteger(d, compName, "textIconGap", null);
		
		setColorUIResource(d, compName, "foreground", null);
		setColorUIResource(d, compName, "[Disabled].textForeground", "#FFFFFF7D");
		setColorUIResource(d, compName, "[Enabled].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[MouseOver].textForeground", "#426A84");
		setColorUIResource(d, compName, "[MouseOver+Selected].textForeground", "#426A84");
		
		setColorUIResource(d, compName, "background", null);
		setColorUIResource(d, compName, "[Disabled].background", "#FFFFFF01");
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF01");
		setColorUIResource(d, compName, "[Selected].background", "#FFFFFF01");
		setColorUIResource(d, compName, "[MouseOver].background", "#FFFFFF");
		setColorUIResource(d, compName, "[MouseOver+Selected].background", "#FFFFFF");

		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "2 10 2 10"), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.BACKGROUND_SELECTED, ctx));
		d.put(compName + "[MouseOver+Selected].backgroundPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.BACKGROUND_SELECTED_MOUSEOVER, ctx));

		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "1 10 1 10"), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);
		String path = StyleUtil.getIconPath(compName, "[Disabled].icon", iconBasePath + "18x18_ico_checkbox_disabled.png");
		d.put(compName + "[Disabled].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_DISABLED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Enabled].icon", iconBasePath + "18x18_ico_checkbox_enabled.png");
		d.put(compName + "[Enabled].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_ENABLED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused].icon", iconBasePath + "18x18_ico_checkbox_focused.png");
		d.put(compName + "[Focused].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[MouseOver].icon", iconBasePath + "18x18_ico_checkbox_mouseover.png");
		d.put(compName + "[MouseOver].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_MOUSEOVER, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+MouseOver].icon", iconBasePath + "18x18_ico_checkbox_mouseover-focused.png");
		d.put(compName + "[Focused+MouseOver].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_MOUSEOVER_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Pressed].icon", iconBasePath + "18x18_ico_checkbox_pressed.png");
		d.put(compName + "[Pressed].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_PRESSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Pressed].icon", iconBasePath + "18x18_ico_checkbox_pressed-focused.png");
		d.put(compName + "[Focused+Pressed].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_PRESSED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Enabled+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected.png");
		d.put(compName + "[Enabled+Selected].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-focused.png");
		d.put(compName + "[Focused+Selected].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_SELECTED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Pressed+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-pressed.png");
		d.put(compName + "[Pressed+Selected].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_PRESSED_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Pressed+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-pressed-focused.png");
		d.put(compName + "[Focused+Pressed+Selected].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_PRESSED_SELECTED_FOCUSED, ctx, path));
		
		path = StyleUtil.getIconPath(compName, "[MouseOver+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-mouseover.png");
		d.put(compName + "[MouseOver+Selected].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_MOUSEOVER_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+MouseOver+Selected].icon", iconBasePath + "18x18_ico_checkbox_selected-mouseover-focussed.png");
		d.put(compName + "[Focused+MouseOver+Selected].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_MOUSEOVER_SELECTED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Disabled+Selected].icon", iconBasePath + "18x18_ico_checkbox_disabled-selected.png");
		d.put(compName + "[Disabled+Selected].checkIconPainter", new LazyPainter(pClass, OCheckBoxMenuItemPainter.CHECKICON_ICON_DISABLED_SELECTED, ctx, path));

	}

	protected void defineList(UIDefaults d) {
		// Initialize List
		String compName = "List";
		setInsetsUIResource(d, compName, "contentMargins", "0 0 0 0");

		setBoolean(d, compName, "opaque", "true");
		setBoolean(d, compName, "rendererUseListColors", "true");
		setBoolean(d, compName, "rendererUseUIBorder", "true");
		
		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		setColor(d, compName, "background", "#FFFFFF");//setColorUIResource
		setColor(d, compName, "foreground", "#335971");//setColorUIResource
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "dropLineColor", "#73a4d1");
		setColorUIResource(d, compName, "disabled", "#39698a");
		setColorUIResource(d, compName, "disabledText", "#8e8f91");
		
		setColor(d, compName, "[Selected].textForeground", "#FFFFFF");
		setColor(d, compName, "[Selected].textBackground", "#36627F");
		setColor(d, compName, "[Disabled+Selected].textBackground", "#36627F");
		setColor(d, compName, "[Disabled].textForeground", "#8e8f91");
		
		//List.cellRenderer...
		compName = "List:\"List.cellRenderer\"";
		setInsetsUIResource(d, compName, "contentMargins", "2 6 2 6");
		
		Insets in = StyleUtil.getInsets(compName, "contentMargins", "2 6 2 6");
		d.put(compName + ".cellNoFocusBorder",	new BorderUIResource(BorderFactory.createEmptyBorder(in.top, in.left, in.bottom, in.right)));
		ListDataField.defaultNoFocusBorder = BorderFactory.createEmptyBorder(in.top, in.left, in.bottom, in.right);
		d.put(compName + ".focusCellHighlightBorder", new BorderUIResource(new PainterBorder(
				"Tree:TreeCell[Enabled+Focused].backgroundPainter", in)));
		
		setBoolean(d, compName, "opaque", "true");
		setColorUIResource(d, compName, "background", "#FFFFFF");
		setColorUIResource(d, compName, "textForeground", "#335971");
		
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8e8f91");
		setColor(d, compName, "[Disabled].background", "#39698a");
	}
	
	protected void definePanel(UIDefaults d){
		
		// Panel
		String compName = "Panel";
		
		setInsetsUIResource(d, compName, "contentMargins", "0 0 0 0");
		setBoolean(d, compName, "opaque", "true");
		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		
		setColorUIResource(d, compName, "background", "#366581");
		setColor(d, compName, "disabled", null);
		setColor(d, compName, "foreground", null);
		setColor(d, compName, "disabledText", null);
		
	}

	protected void definePopupMenu(UIDefaults d) {

		// Popup Menu:
		String compName = "PopupMenu";

		setInsetsUIResource(d, compName, "contentMargins", "18 9 16 9");
		setBoolean(d, compName, "opaque", "false");
		setBoolean(d, compName, "consumeEventOnClose", "true");
		
		setFontUIResource(d, compName, "font", null);
		setColorUIResource(d, compName, "foreground", "#ffffff");
		setColorUIResource(d, compName, "background", "#8cbec6");
		
		setColorUIResource(d, compName, "[Disabled].background", "#8cbec6");
		setColorUIResource(d, compName, "[Enabled].background", "#86adbf");
		setColorUIResource(d, compName, "[Disabled].border", "#c6dfe3");
		setColorUIResource(d, compName, "[Enabled].border", "#c6dfe3");

		String pClass = "com.ontimize.plaf.painter.OPopupMenuPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "18 9 16 9"), new Dimension(220, 313), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OPopupMenuPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OPopupMenuPainter.BACKGROUND_ENABLED, ctx));

	}

	protected void definePopupMenuSeparator(UIDefaults d) {
		// Popup Menu Separator:
		String compName = "PopupMenuSeparator";
		
		setInsetsUIResource(d, compName, "contentMargins", "5 0 5 0");
		setFontUIResource(d, compName, "font", null);
		setColorUIResource(d, compName, "foreground", null);
		setColorUIResource(d, compName, "background", null);
		setColorUIResource(d, compName, "disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		
		setColorUIResource(d, compName, "bottomshadowcolor", "#FFFFFF33");
		setColorUIResource(d, compName, "uppershadowcolor", "#00000033");

		String pClass = "com.ontimize.plaf.painter.OPopupMenuSeparatorPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "5 0 5 0"), new Dimension(100, 2), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OPopupMenuSeparatorPainter.BACKGROUND_ENABLED, ctx));
	}

	protected void defineSeparator(UIDefaults d) {

		// Separator:
		String compName = "Separator";
		
		setInsetsUIResource(d, compName, "contentMargins", "5 5 5 5");
		setColorUIResource(d, compName, "[Enabled].background", "00000033");
		setColorUIResource(d, compName, "[Enabled].backgroundShadow", "FFFFFF33");

		String pClass = "com.ontimize.plaf.painter.OSeparatorPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "5 5 5 5"), new Dimension(100, 2), true, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);
		d.put("Separator[Enabled].backgroundPainter", new LazyPainter(pClass, OSeparatorPainter.BACKGROUND_ENABLED, ctx));

	}
	
	protected void defineProgressBar(UIDefaults d){
		// JProgressBar...
		d.put("ProgressBar.Indeterminate", new OProgressBarIndeterminateState());
		d.put("ProgressBar.Finished", new OProgressBarFinishedState());
		
	}

	/**
	 * Initialize the slider settings.
	 * 
	 * @param d
	 *            the UI defaults map.
	 */
	protected void defineSliders(UIDefaults d) {
		// d.put("sliderTrackBorderBase", new Color(0x898989));
		// d.put("sliderTrackInteriorBase", new Color(0xd8d8d8));
		//
		// String p = "Slider";
		//
		// d.put(p + ".ArrowShape", new SliderArrowShapeState());
		// d.put(p + ":SliderThumb.ArrowShape", new SliderArrowShapeState());
		// d.put(p + ":SliderTrack.ArrowShape", new SliderArrowShapeState());
		// d.put(p + ".thumbWidth", new Integer(17));
		// d.put(p + ".thumbHeight", new Integer(20));
		// d.put(p + ".trackBorder", new Integer(0));
		// d.put(p + ".trackHeight", new Integer(5));
		// d.put(p + ".font", defaultFont.deriveFont(11.0f));
		//
		// p = "Slider:SliderThumb";
		// String c = PAINTER_PREFIX + "SliderThumbPainter";
		//
		// d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_DISABLED));
		// d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_ENABLED));
		// d.put(p + "[Focused].backgroundPainter", new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_FOCUSED));
		// d.put(p + "[Focused+MouseOver].backgroundPainter",
		// new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_FOCUSED_MOUSEOVER));
		// d.put(p + "[Focused+Pressed].backgroundPainter",
		// new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_FOCUSED_PRESSED));
		// d.put(p + "[MouseOver].backgroundPainter",
		// new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_MOUSEOVER));
		// d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_PRESSED));
		// d.put(p + "[ArrowShape+Enabled].backgroundPainter",
		// new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_ENABLED_ARROWSHAPE));
		// d.put(p + "[ArrowShape+Disabled].backgroundPainter",
		// new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_DISABLED_ARROWSHAPE));
		// d.put(p + "[ArrowShape+MouseOver].backgroundPainter",
		// new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_MOUSEOVER_ARROWSHAPE));
		// d.put(p + "[ArrowShape+Pressed].backgroundPainter",
		// new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_PRESSED_ARROWSHAPE));
		// d.put(p + "[ArrowShape+Focused].backgroundPainter",
		// new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_FOCUSED_ARROWSHAPE));
		// d.put(p + "[ArrowShape+Focused+MouseOver].backgroundPainter",
		// new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_FOCUSED_MOUSEOVER_ARROWSHAPE));
		// d.put(p + "[ArrowShape+Focused+Pressed].backgroundPainter",
		// new LazyPainter(c,
		// SliderThumbPainter.Which.BACKGROUND_FOCUSED_PRESSED_ARROWSHAPE));
		//
		// p = "Slider:SliderTrack";
		// c = PAINTER_PREFIX + "SliderTrackPainter";
		// d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c,
		// SliderTrackPainter.Which.BACKGROUND_DISABLED));
		// d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c,
		// SliderTrackPainter.Which.BACKGROUND_ENABLED));

		String compName = "Slider";
		d.put(compName + ".ArrowShape", new OSliderThumbArrowShapeState());
		d.put(compName + ":SliderThumb.ArrowShape", new OSliderThumbArrowShapeState());
		d.put(compName + ":SliderTrack.ArrowShape", new OSliderThumbArrowShapeState());
	}

	protected void defineSplitPane(UIDefaults d) {

		// SplitPane:
		String compName = "SplitPane";
		
		setInsetsUIResource(d, compName, "contentMargins", "0 0 0 0");
		setInteger(d, compName, "size", "12");
		setInteger(d, compName, "dividerSize", "12");
		
		setBoolean(d, compName, "centerOneTouchButtons", "true");
		setBoolean(d, compName, "oneTouchExpandable", "false");
		setBoolean(d, compName, "continuousLayout", "true");
		setInteger(d, compName, "oneTouchButtonOffset", "30");
		
		setFontUIResource(d, compName, "font", null);
		setColorUIResource(d, compName, "foreground", null);
		setColorUIResource(d, compName, "background", null);
		setColorUIResource(d, compName, "disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		
		d.put("SplitPane.States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Vertical");
		d.put("SplitPane.Vertical", new OSplitPaneVerticalState());
		
		
		//SplitPane: SplitPaneDivider :
		compName = "SplitPane:SplitPaneDivider";
		String pClass = "com.ontimize.plaf.painter.OSplitPaneDividerPainter";
		
		setInsetsUIResource(d, compName, "contentMargins", "3 0 3 0");
		d.put(compName+".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Vertical");
		d.put(compName+".Vertical", new OSplitPaneDividerVerticalState());
		
		setColorUIResource(d, compName, "[Enabled].background", "#E6E6E6");
		setColorUIResource(d, compName, "[Focused].background", "#E6E6E6");
		
		setColorUIResource(d, compName, "[Enabled].foreground", "#AAAAAA7D");
		setColorUIResource(d, compName, "[Enabled+Vertical].foreground", "#AAAAAA7D");
		setColorUIResource(d, compName, "[Enabled].foregroundBorder", "#FFFFFF");
		setColorUIResource(d, compName, "[Enabled+Vertical].foregroundBorder", "#FFFFFF");
		
		Insets in = StyleUtil.getInsets(compName, "contentMargins", "3 0 3 0");
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(in, new Dimension(68, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName+"[Enabled].backgroundPainter", new LazyPainter(pClass, OSplitPaneDividerPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName+"[Focused].backgroundPainter", new LazyPainter(pClass, OSplitPaneDividerPainter.BACKGROUND_FOCUSED, ctx));

		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(0, 1, 0, 1), new Dimension(92, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 2.0, 2.0);
		d.put(compName+"[Enabled].foregroundPainter", new LazyPainter(pClass, OSplitPaneDividerPainter.FOREGROUND_ENABLED, ctx));
		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(1, 0, 1, 0), new Dimension(12, 90), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 2.0, 2.0);
		d.put(compName+"[Enabled+Vertical].foregroundPainter", new LazyPainter(pClass, OSplitPaneDividerPainter.FOREGROUND_ENABLED_VERTICAL, ctx));

	}

	protected void defineTree(UIDefaults d) {

		String compName = "Tree";
		
		setBoolean(d, compName, "rendererFillBackground", "false");
		setBoolean(d, compName, "drawHorizontalLines", "true");
		setBoolean(d, compName, "drawVerticalLines", "true");
		d.put("Tree.linesStyle", "dashed");
		setBoolean(d, compName, "lineTypeCurved", "true");
		setBoolean(d, compName, "paintLines", "true");
		setColorUIResource(d, compName, "lineColor", "#C5D1DA");
		setColorUIResource(d, compName, "lineSelectionColor", "#517286");
		setInteger(d, compName, "rightChildIndent", "10");
		setIcon(d, compName, "expandedIcon", "com/ontimize/plaf/images/11x11_tree_open.png");
		setIcon(d, compName, "collapsedIcon", "com/ontimize/plaf/images/11x11_tree_close.png");
		setColorUIResource(d, compName, "selectionBackground", "#517286");
		setColor(d, compName, "selectionForeground", "#E4EDF0");
		setColor(d, compName, "textForeground", "#94A4B0");
		

		// OTreeCellRendererPainter
		compName = "Tree:\"Tree.cellRenderer\"";

		setBoolean(d, compName, "opaque", "true");
		setInsetsUIResource(d, compName, "contentMargins", "0 2 0 30");
		setInteger(d, compName, "iconTextGap", "10");
		Font tCRFont = getDefaultFont();
		tCRFont = tCRFont.deriveFont((float)tCRFont.getSize()-1);
		setFont(d, compName, "font", OntimizeLAFParseUtils.fontToString(tCRFont));
		setColorUIResource(d, compName, "textForeground", "#517286");
		
		setColorUIResource(d, compName, "background", "#E4EDF0");
		setColorUIResource(d, compName, "backgroundSelectionParent", "#ABC7D8");
		setColorUIResource(d, compName, "backgroundSelection", "#517286");
		setColorUIResource(d, compName, "topBackgroundSelection", "#638092");
		setColorUIResource(d, compName, "bottomBackgroundSelection", "#496678");
		setColorUIResource(d, compName, "backgroundChildCount", "#36627F");
		setColorUIResource(d, compName, "foregroundChildCount", "#E4EDF0");
		setColorUIResource(d, compName, "backgroundSelectionCount", "#E4EDF0");
		setColorUIResource(d, compName, "foregroundSelectionChildCount", "#517286");
		
		String pClass = "com.ontimize.plaf.painter.OTreeCellRendererPainter";
		PaintContext ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "0 2 0 30"), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);
		d.put(compName + ".backgroundPainter", new OntimizeDefaults.LazyPainter(pClass, OTreeCellRendererPainter.BACKGROUND_ENABLED, ctx));
		
		
		//Initialize \"Tree.cellEditor\"
		compName = "\"Tree.cellEditor\"";
        d.put(compName + ".contentMargins", new InsetsUIResource(2, 5, 2, 5));
        d.put(compName + ".opaque", Boolean.TRUE);
        tCRFont = getDefaultFont();
		tCRFont = tCRFont.deriveFont((float)tCRFont.getSize()-1);
		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(tCRFont));
        setColorUIResource(d, compName, "background", "#ffffff");
        setColorUIResource(d, compName, "[Enabled].textForeground", "#335971");
        setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
        setColorUIResource(d, compName, "[Selected].textForeground", "#ffffff");
        
        pClass = "com.ontimize.plaf.painter.OTreeCellEditorPainter";
		ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "2 5 2 5"), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + "[Enabled].backgroundPainter", new OntimizeDefaults.LazyPainter(pClass, OTreeCellEditorPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Enabled+Focused].backgroundPainter", new OntimizeDefaults.LazyPainter(pClass, OTreeCellEditorPainter.BACKGROUND_ENABLED_FOCUSED, ctx));
		

	}

	protected void defineDiagramToolBar(UIDefaults d) {
		String compName = "\"DiagramToolBar\"";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(1, 1, 1, 1), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 3.0, 3.0);
		d.put(compName + "[East].backgroundPainter", new LazyPainter("com.ontimize.plaf.painter.EastOToolBarPainter", AbstractOToolBarPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[North].backgroundPainter", new LazyPainter("com.ontimize.plaf.painter.NorthODiagramToolBarPainter", AbstractOToolBarPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[South].backgroundPainter", new LazyPainter("com.ontimize.plaf.painter.SouthOToolBarPainter", AbstractOToolBarPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[West].backgroundPainter", new LazyPainter("com.ontimize.plaf.painter.WestOToolBarPainter", AbstractOToolBarPainter.BACKGROUND_ENABLED, ctx));
	}

	protected void defineToolBar(UIDefaults d) {

		defineDiagramToolBar(d);
		String compName = "ToolBar";
		// Toolbar:
		d.put(compName + ".States", "North,East,West,South");
		d.put(compName + ".North", new OToolBarNorthState());
		d.put(compName + ".East", new OToolBarEastState());
		d.put(compName + ".West", new OToolBarWestState());
		d.put(compName + ".South", new OToolBarSouthState());
		
		setInsetsUIResource(d, compName, "contentMargins", "0 0 0 0");
		setBoolean(d, compName, "opaque", "true");
		setBoolean(d, compName, "useTextureImage", "true");
		d.put(compName + ".backgroundImage", StyleUtil.getProperty(compName, "backgroundImage", "com/ontimize/plaf/images/toolbarbackground.png"));
		setPaint(d, compName, "bgpaint", null);

		setFontUIResource(d, compName, "font", null);
		setColorUIResource(d,compName,"foreground",null);
		setColorUIResource(d,compName,"disabledText",null);
		setColorUIResource(d,compName,"background",null);
		setColorUIResource(d,compName,"disabled",null);

		// WARNING: There is an important BUG in class NimbusDefaults in Nimbus.
		// The TOOLBAR (not other components inside the toolbar such as
		// ToolBar:Button or ToolBar:ToggleButton)
		// has not normal states (enable, disable, mouseover, ...). The toolbar
		// has the states stored in the variable ToolBar.States, which
		// (by default in Nimbus) are: North,East,West,South.
		// For that reason, keys like ToolBar[Enabled].borderPainter or
		// ToolBar[Enabled].handleIconPainter has no sense. Two ways for fixing
		// them:
		// Solution A) override those keys changing the status Enabled for the
		// others (North,East,West,South).
		// Solution B) override the property ToolBar.States in order to add the
		// Enabled value to the existing ones (North,East,West,South).
		// In Ontimize L&F we choose the first one, because the status for the
		// toolbar is always enabled.
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "0 0 0 0"), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);
		String pClass = "com.ontimize.plaf.painter.OToolBarPainter";
		d.put(compName + "[East].borderPainter", new LazyPainter(pClass, OToolBarPainter.BORDER_EAST, ctx));
		d.put(compName + "[North].borderPainter", new LazyPainter(pClass, OToolBarPainter.BORDER_NORTH, ctx));
		d.put(compName + "[South].borderPainter", new LazyPainter(pClass, OToolBarPainter.BORDER_SOUTH, ctx));
		d.put(compName + "[West].borderPainter", new LazyPainter(pClass, OToolBarPainter.BORDER_WEST, ctx));

		// WARNING: Here, there is also other BUG in class NimbusDefaults in
		// Nimbus. Only the values for keys that contains backgroundPainter,
		// foregroundPainter or borderPainter
		// are processed as a painter (See class NimbusStyle, method
		// init(values, defaults)) => Method createValue() in LazyPainter never
		// was called for the key handleIconPainter.
		// Fixing: as we use the same painter for the whole Toolbar
		// (OToolBarPainter), we change the not-valid key
		// (ToolBar[Enabled].handleIconPainter) for a valid key
		// (ToolBar[East].backgroundPainter).
		ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(1, 1, 1, 1), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 3.0, 3.0);
		d.put(compName + "[East].backgroundPainter", new LazyPainter("com.ontimize.plaf.painter.EastOToolBarPainter", AbstractOToolBarPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[North].backgroundPainter", new LazyPainter("com.ontimize.plaf.painter.NorthOToolBarPainter", AbstractOToolBarPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[South].backgroundPainter", new LazyPainter("com.ontimize.plaf.painter.SouthOToolBarPainter", AbstractOToolBarPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[West].backgroundPainter", new LazyPainter("com.ontimize.plaf.painter.WestOToolBarPainter", AbstractOToolBarPainter.BACKGROUND_ENABLED, ctx));

		Integer height = StyleUtil.getInteger(compName, "height", "50");
		ApplicationToolBar.DEFAULT_TOOLBAR_HEIGHT = height;
		
	}
	
	protected void defineToolBarButton(UIDefaults d){
		// ToolBar: Button:
		String compName = "ToolBar:Button";
		
		setFont(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		setInsetsUIResource(d, compName, "contentMargins", "0 8 0 8");
		setInteger(d, compName, "size", "32");
		setColorUIResource(d,compName,"textForeground","#FFFFFF");
		
		setColorUIResource(d, compName, "background", "#817F7F7F");
		
		setFloat(d, compName, "[Default].alphaTransparency", "1.0");
		setFloat(d, compName, "[Default+Focused].alphaTransparency", "1.0");
		setFloat(d, compName, "[Default+MouseOver].alphaTransparency", "1.0");
		setFloat(d, compName, "[Default+Focused+MouseOver].alphaTransparency", "1.0");
		setFloat(d, compName, "[Default+Pressed].alphaTransparency", "1.0");
		setFloat(d, compName, "[Default+Focused+Pressed].alphaTransparency", "1.0");
		
		setFloat(d, compName, "[Disabled].alphaTransparency", "1.0");
		setFloat(d, compName, "[Enabled].alphaTransparency", "1.0");
		setFloat(d, compName, "[Focused].alphaTransparency", "1.0");
		setFloat(d, compName, "[MouseOver].alphaTransparency", "1.0");
		setFloat(d, compName, "[Focused+MouseOver].alphaTransparency", "1.0");
		setFloat(d, compName, "[Pressed].alphaTransparency", "1.0");
		setFloat(d, compName, "[Focused+Pressed].alphaTransparency", "1.0");
		
		String pClass = "com.ontimize.plaf.painter.OToolBarButtonPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "0 8 0 8"), new Dimension(32, 32), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 2.0, Double.POSITIVE_INFINITY);
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, OToolBarButtonPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, OToolBarButtonPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, OToolBarButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, OToolBarButtonPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, OToolBarButtonPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OToolBarButtonPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OToolBarButtonPainter.BACKGROUND_DISABLED, ctx));

		Integer size = StyleUtil.getInteger(compName, "size", "32");
		ApplicationToolBar.DEFAULT_BUTTON_SIZE = size;
		ApToolBarNavigator.defaultButtonsBorder = BorderFactory.createEmptyBorder();
	}
	
	protected void defineToolBarToggleButton(UIDefaults d){
		// ToolBar: ToggleButton:
		String compName = "ToolBar:ToggleButton";
		setInsetsUIResource(d, compName, "contentMargins", "0 8 0 8");
		setFont(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		setColorUIResource(d,compName,"textForeground","#FFFFFF");
		
		setColorUIResource(d, compName, "background", "#817F7F7F");
		
		setFloat(d, compName, "[Disabled].alphaTransparency", "1.0");
		setFloat(d, compName, "[Enabled].alphaTransparency", "1.0");
		setFloat(d, compName, "[Focused].alphaTransparency", "1.0");
		setFloat(d, compName, "[MouseOver].alphaTransparency", "1.0");
		setFloat(d, compName, "[Focused+MouseOver].alphaTransparency", "1.0");
		setFloat(d, compName, "[Pressed].alphaTransparency", "1.0");
		setFloat(d, compName, "[Focused+Pressed].alphaTransparency", "1.0");
		
		setFloat(d, compName, "[Selected].alphaTransparency", "1.0");
		setFloat(d, compName, "[Disabled+Selected].alphaTransparency", "1.0");
		setFloat(d, compName, "[Focused+Selected].alphaTransparency", "1.0");
		setFloat(d, compName, "[MouseOver+Selected].alphaTransparency", "1.0");
		setFloat(d, compName, "[Focused+MouseOver+Selected].alphaTransparency", "1.0");
		setFloat(d, compName, "[Pressed+Selected].alphaTransparency", "1.0");
		setFloat(d, compName, "[Focused+Pressed+Selected].alphaTransparency", "1.0");
		
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "0 8 0 8"), new Dimension(32, 32), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 2.0, Double.POSITIVE_INFINITY);
		String pClass = "com.ontimize.plaf.painter.OToolBarToggleButtonPainter";

		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
		
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_SELECTED, ctx));
		d.put(compName + "[Focused+Selected].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_SELECTED_FOCUSED, ctx));
		d.put(compName + "[Pressed+Selected].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_PRESSED_SELECTED, ctx));
		d.put(compName + "[Focused+Pressed+Selected].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_PRESSED_SELECTED_FOCUSED, ctx));
		d.put(compName + "[MouseOver+Selected].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_MOUSEOVER_SELECTED, ctx));
		d.put(compName + "[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_MOUSEOVER_SELECTED_FOCUSED, ctx));
		d.put(compName + "[Disabled+Selected].backgroundPainter", new LazyPainter(pClass, OToolBarToggleButtonPainter.BACKGROUND_DISABLED_SELECTED, ctx));

	}
	
	protected void defineToolBarSeparator(UIDefaults d){
		// ToolBarSeparator:
		String compName = "ToolBarSeparator";
		String pClass = "com.ontimize.plaf.painter.OToolBarSeparatorPainter";
		
		setInsetsUIResource(d, compName, "contentMargins", "0 0 0 0");
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF4C");

		Insets insets = StyleUtil.getInsets(compName, "contentMargins", "0 0 0 0");
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(insets, new Dimension(38, 7), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OToolBarSeparatorPainter.BACKGROUND_ENABLED, ctx));
	}
	
	protected void defineToolTip(UIDefaults d){
		// ToolTip
		String compName = "ToolTip";
		
		setFontUIResource(d, compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		setInsetsUIResource(d, compName, "contentMargins", "5 9 6 9");
		setColorUIResource(d, compName, "disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		
		setColorUIResource(d, compName, "background", "#86ADBF");
		setColorUIResource(d, compName, "[Enabled].background", "#FFFFFF01");
		
		setColorUIResource(d, compName, "textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "border", "#C6DFE3");
		
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "5 9 6 9"), new Dimension(10, 10), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter("com.ontimize.plaf.painter.OToolTipPainter", OToolTipPainter.BACKGROUND_ENABLED, ctx));

	}

	protected void defineRadioButton(UIDefaults d) {
		// Initialize RadioButton
		String compName = "RadioButton";
		String pClass = "com.ontimize.plaf.painter.ORadioButtonPainter";
		String iconBasePath = "com/ontimize/plaf/images/radio/";
		
		setColorUIResource(d, compName, "background", "#ffffff01");
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "foreground", "#335971");
		setColorUIResource(d, compName, "disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		
		setColorUIResource(d, compName, "[Enabled].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#FFFFFF7D");
		setColorUIResource(d, compName, "[Focused].textForeground", "#FFFFFF");
		setColorUIResource(d, compName, "[Selected].textForeground", "#FFFFFF");
		
		PaintContext ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "0 0 0 0"), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);

		d.put(compName + ".contentMargins", StyleUtil.getInsetsUI(compName, "contentMargins", "0 0 0 0"));

		String path = StyleUtil.getIconPath(compName, "[Disabled].icon", iconBasePath + "18x18_ico_radiobutton_disabled.png");
		d.put(compName + "[Disabled].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_DISABLED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Enabled].icon", iconBasePath + "18x18_ico_radiobutton_enabled.png");
		d.put(compName + "[Enabled].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_ENABLED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused].icon", iconBasePath + "18x18_ico_radiobutton_focused.png");
		d.put(compName + "[Focused].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[MouseOver].icon", iconBasePath + "18x18_ico_radiobutton_mouseover.png");
		d.put(compName + "[MouseOver].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_MOUSEOVER, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+MouseOver].icon", iconBasePath + "18x18_ico_radiobutton_mouseover-focused.png");
		d.put(compName + "[Focused+MouseOver].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_MOUSEOVER_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Pressed].icon", iconBasePath + "18x18_ico_radiobutton_pressed.png");
		d.put(compName + "[Pressed].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_PRESSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Pressed].icon", iconBasePath + "18x18_ico_radiobutton_pressed-focused.png");
		d.put(compName + "[Focused+Pressed].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_PRESSED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Selected].icon", iconBasePath + "18x18_ico_radiobutton_selected.png");
		d.put(compName + "[Selected].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Selected].icon", iconBasePath + "18x18_ico_radiobutton_selected-focused.png");
		d.put(compName + "[Focused+Selected].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_SELECTED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Pressed+Selected].icon", iconBasePath + "18x18_ico_radiobutton_selected-pressed.png");
		d.put(compName + "[Pressed+Selected].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_PRESSED_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+Pressed+Selected].icon", iconBasePath + "18x18_ico_radiobutton_selected-pressed-focused.png");
		d.put(compName + "[Focused+Pressed+Selected].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_PRESSED_SELECTED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[MouseOver+Selected].icon", iconBasePath + "18x18_ico_radiobutton_selected-mouseover.png");
		d.put(compName + "[MouseOver+Selected].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_MOUSEOVER_SELECTED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Focused+MouseOver+Selected].icon", iconBasePath + "18x18_ico_radiobutton_selected-mouseover-focused.png");
		d.put(compName + "[Focused+MouseOver+Selected].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_MOUSEOVER_SELECTED_FOCUSED, ctx, path));

		path = StyleUtil.getIconPath(compName, "[Disabled+Selected].icon", iconBasePath + "18x18_ico_radiobutton_disabled-selected.png");
		d.put(compName + "[Disabled+Selected].iconPainter", new LazyPainter(pClass, ORadioButtonPainter.ICON_DISABLED_SELECTED, ctx, path));
	}

	protected void defineRow(UIDefaults d) {
		String prefix = "\"Row\"";

		setColorUIResource(d, prefix, "background", "#FFFFFF14");
		setPaint(d, prefix, "bgpaint", null);
		setBoolean(d, prefix, "opaque", "false");
		setInsetsUIResource(d, prefix, "contentMargins", "0 0 0 0");

		Insets insets = StyleUtil.getInsets(prefix, "contentMargins", "0 0 0 0");
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(insets, new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);

		d.put(prefix + ".backgroundPainter", new OntimizeDefaults.LazyPainter("com.ontimize.plaf.painter.ORowPanelPainter", ORowPanelPainter.BACKGROUND_ENABLED, ctx));
	}

	protected void defineColumn(UIDefaults d) {
		String prefix = "\"Column\"";

		setColorUIResource(d, prefix, "background", "#FFFFFF14");
		setPaint(d, prefix, "bgpaint", null);
		setBoolean(d, prefix, "opaque", "false");
		setInsetsUIResource(d, prefix, "contentMargins", "0 0 0 0");

		Insets insets = StyleUtil.getInsets(prefix, "contentMargins", "0 0 0 0");
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(insets, new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);

		d.put(prefix + ".backgroundPainter", new OntimizeDefaults.LazyPainter("com.ontimize.plaf.painter.OColumnPanelPainter", OColumnPanelPainter.BACKGROUND_ENABLED, ctx));
	}
	
	protected void defineCardPanel(UIDefaults d) {
		String prefix = "\"CardPanel\"";

		setColorUIResource(d, prefix, "background", "#FFFFFF14");
		setBoolean(d, prefix, "opaque", "false");
		setInsetsUIResource(d, prefix, "contentMargins", "0 0 0 0");

		Insets insets = StyleUtil.getInsets(prefix, "contentMargins", "0 0 0 0");
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(insets, new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);

		d.put(prefix + ".backgroundPainter", new OntimizeDefaults.LazyPainter("com.ontimize.plaf.painter.OCardPanelPainter", OCardPanelPainter.BACKGROUND_ENABLED, ctx));
	}

	protected void defineGrid(UIDefaults d) {
		String prefix = "\"Grid\"";

		setColorUIResource(d, prefix, "background", "#FFFFFF14");
		setPaint(d, prefix, "bgpaint", null);
		setBoolean(d, prefix, "opaque", "false");
		Grid.defaultOpaque = StyleUtil.getBoolean(prefix, "opaque", "false");
		setInsetsUIResource(d, prefix, "contentMargins", "5 5 5 5");

		Insets insets = StyleUtil.getInsets(prefix, "contentMargins", "5 5 5 5");
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(insets, new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);

		d.put(prefix + ".backgroundPainter", new OntimizeDefaults.LazyPainter("com.ontimize.plaf.painter.OGridPanelPainter", OGridPanelPainter.BACKGROUND_ENABLED, ctx));
	}

	protected void defineTitleBorder(UIDefaults d) {
		String compName = "TitledBorder";
		d.put(compName+".position", "BELOW_TOP");
		Insets insets = StyleUtil.getInsets(compName, "contentMargins", "10 10 10 10");
		Integer radius = StyleUtil.getInteger(compName, "radius", null);
		Integer titleSize = StyleUtil.getInteger(compName, "titleSize", null);
		
		d.put(compName+".border", new BorderUIResource(new OLoweredBorder(insets, titleSize, radius)));
		Font tBFont = this.getDefaultFont();
		tBFont = tBFont.deriveFont(Font.BOLD, (float)tBFont.getSize()+2);
		setFont(d, compName, "font", OntimizeLAFParseUtils.fontToString(tBFont));
		
		setColor(d, compName, "titleColor", "#3b3b3b");
	}

	protected void defineTableButton(UIDefaults d) {
		String compName = "\"TableButton\"";

		TableButton.defaultPaintFocus = true;
		TableButton.defaultContentAreaFilled = true;
		TableButton.defaultCapable = true;
		
		setInsetsUIResource(d, compName, "contentMargins", "0 0 0 0");
		setFontUIResource(d, compName, "font", null);
		setBoolean(d, compName, "defaultButtonFollowsFocus", "false");
		setColorUIResource(d, compName,"disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		setColor(d, compName, "background", "#FFFFFF");
		
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		
		setColorUIResource(d, compName, "[Focused].background", "#366581");
		setFloat(d, compName, "[Focused].alphaTransparency", "0.5");

		String pClass = "com.ontimize.plaf.painter.OTableButtonPainter";
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(compName, "contentMargins", "0 0 0 0"), new Dimension(33, 33), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);

		d.put(compName + "[Default].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT, ctx));
		d.put(compName + "[Default+Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
	}

	protected void defineTableButtonPanel(UIDefaults d) {
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(0, 0, 0, 0), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);

		String prefix = "\"TableButtonPanel\"";

		setColor(d, prefix, "topBackgroundColor", "#DAE7ED");
		setColor(d, prefix, "bottomBackgroundColor", "#A2ABB0");
		d.put(prefix + ".backgroundPainter", new OntimizeDefaults.LazyPainter("com.ontimize.plaf.painter.OTableButtonPanelPainter", OTableButtonPanelPainter.BACKGROUND_ENABLED, ctx));
	}
	
	protected void defineTableButtonFooterPanel(UIDefaults d) {
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(0, 0, 0, 0), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);

		String prefix = "\"TableButtonFooterPanel\"";

		setColor(d, prefix, "topBackgroundColor", "#A2ABB0");
		setColor(d, prefix, "bottomBackgroundColor", "#DAE7ED");
		d.put(prefix + ".backgroundPainter", new OntimizeDefaults.LazyPainter("com.ontimize.plaf.painter.OTableButtonFooterPanelPainter", OTableButtonFooterPanelPainter.BACKGROUND_ENABLED, ctx));
	}

	protected void defineFormButtonPanel(UIDefaults d) {
		String prefix = "\"FormButtonPanel\"";

		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);

		setColor(d, prefix, "topBackgroundColor", "#AAB8BF");
		setColor(d, prefix, "bottomBackgroundColor", "#E6EFF2");
		d.put(prefix + ".backgroundPainter", new OntimizeDefaults.LazyPainter("com.ontimize.plaf.painter.OFormButtonPanelPainter", OFormBodyPanelPainter.BACKGROUND_ENABLED, ctx));
	}

	protected void defineFormBodyPanel(UIDefaults d) {
		String prefix = "\"FormBodyPanel\"";
		
		setPaint(d, prefix, "bgpaint", null);

		OFormBodyPanelPainter.BG_IMAGE = StyleUtil.getProperty(prefix, "backgroundImage", "com/ontimize/plaf/images/backgroundDarkBlue.jpg");
		Form.DEFAULT_FORM_MARGIN = StyleUtil.getInsets(prefix, "contentMargins", "5 5 5 5");

		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(StyleUtil.getInsets(prefix, "contentMargins", "5 5 5 5"), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);

		d.put(prefix + ".backgroundPainter", new OntimizeDefaults.LazyPainter("com.ontimize.plaf.painter.OFormBodyPanelPainter", OFormBodyPanelPainter.BACKGROUND_ENABLED, ctx));
	}

	protected void defineFieldButton(UIDefaults d) {
		String compName = "\"FieldButton\"";
		String pClass = "com.ontimize.plaf.painter.OFieldButtonPainter";

		FieldButton.defaultContentAreaFilled = true;
		FieldButton.defaultPaintFocus = true;
		FieldButton.defaultCapable = true;
		
		setFontUIResource(d, compName, "font", null);
		setBoolean(d, compName, "defaultButtonFollowsFocus", "false");
		setColorUIResource(d, compName,"disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		setColor(d, compName, "background", "#FFFFFF");
		
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		
		setColorUIResource(d, compName, "[Focused].background", "#366581");
		setFloat(d, compName, "[Focused].alphaTransparency", "0.5");

		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(16, 16, 16, 16), new Dimension(33, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		d.put(compName + "[Default].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT, ctx));
		d.put(compName + "[Default+Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
	}

	protected void defineFormButton(UIDefaults d) {
		String compName = "\"FormButton\"";
		String pClass = "com.ontimize.plaf.painter.OFormButtonPainter";

		Form.defaultFormButtonSize = 26;
		FormButton.defaultContentAreaFilled = true;
		FormButton.defaultPaintFocus = true;
		FormButton.defaultCapable = true;
		
		setFontUIResource(d, compName, "font", null);
		setBoolean(d, compName, "defaultButtonFollowsFocus", "false");
		setColorUIResource(d, compName,"disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		setColor(d, compName, "background", "#FFFFFF");
		
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		
		setColorUIResource(d, compName, "[Focused].background", "#366581");
		setFloat(d, compName, "[Focused].alphaTransparency", "0.5");
		
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(16, 16, 16, 16), new Dimension(33, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		d.put(compName + "[Default].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT, ctx));
		d.put(compName + "[Default+Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
	}

	protected void defineFormHeader(UIDefaults d) {
		
		ImageManager.INSERT = "com/ontimize/plaf/images/formheader/insert.png";
		ImageManager.CONFIRM_INSERT = "com/ontimize/plaf/images/formheader/confirminsert.png";
		ImageManager.UPDATE = "com/ontimize/plaf/images/formheader/update.png";
		ImageManager.SEARCH = "com/ontimize/plaf/images/formheader/search.png";
		ImageManager.CONFIRM_QUERY = "com/ontimize/plaf/images/formheader/confirmquery.png";
		ImageManager.DELETE_DOCUMENT = "com/ontimize/plaf/images/formheader/delete_document.png";
		ImageManager.ADVANCE_SEARCH = "com/ontimize/plaf/images/formheader/advancesearch.png";
		ImageManager.NEXT_2 = "com/ontimize/plaf/images/formheader/next.png";
		ImageManager.PREVIOUS_2 = "com/ontimize/plaf/images/formheader/previous.png";
		ImageManager.END_2 = "com/ontimize/plaf/images/formheader/end.png";
		ImageManager.START_2 = "com/ontimize/plaf/images/formheader/start.png";
		ImageManager.PRINT = "com/ontimize/plaf/images/formheader/print.png";
		ImageManager.REFRESH_2 = "com/ontimize/plaf/images/formheader/refresh.png";
		ImageManager.ATTACH_FILE = "com/ontimize/plaf/images/formheader/attachfile.png";
		ImageManager.BUNDLE = "com/ontimize/plaf/images/formheader/bundle.png";
		ImageManager.EDIT = "com/ontimize/plaf/images/formheader/edit.png";
		ImageManager.TABLE_VIEW = "com/ontimize/plaf/images/formheader/tableview.png";
		ImageManager.CHECK = "com/ontimize/plaf/images/formheader/tableviewOK.png";
		ImageManager.DELETE_FIELDS = "com/ontimize/plaf/images/formheader/deletefields.png";
		ImageManager.FUNNEL_NEW = "com/ontimize/plaf/images/formheader/filter.png";
	}

	protected void defineFormHeaderButton(UIDefaults d) {
		String compName = "\"FormHeaderButton\"";
		String pClass = "com.ontimize.plaf.painter.OFormHeaderButtonPainter";

		FormHeaderButton.defaultCapable = true;
		FormHeaderButton.defaultContentAreaFilled = true;
		FormHeaderButton.defaultPaintFocus = true;
		
//		setInsetsUIResource(d, compName, "contentMargins", "16 16 16 16");
		setFontUIResource(d, compName, "font", null);
		setBoolean(d, compName, "defaultButtonFollowsFocus", "false");
		setColorUIResource(d, compName,"disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		setColor(d, compName, "background", "#FFFFFF");
		
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		
		setColorUIResource(d, compName, "[Focused].background", "#366581");
		setFloat(d, compName, "[Focused].alphaTransparency", "0.5");
		

		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(16, 16, 16, 16), new Dimension(33, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);

		d.put(compName + "[Default].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT, ctx));
		d.put(compName + "[Default+Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
	}
	
	protected void defineFormHeaderPopupButton(UIDefaults d) {
		
		String compName = "\"FormHeaderPopupButton\"";
		
		FormHeaderPopupButton.defaultCapable = true;
		FormHeaderPopupButton.defaultContentAreaFilled = true;
		FormHeaderPopupButton.defaultPaintFocus = true;
		
//		setInsetsUIResource(d, compName, "contentMargins", "16 16 16 16");
		setFontUIResource(d, compName, "font", null);
		setBoolean(d, compName, "defaultButtonFollowsFocus", "false");
		setColorUIResource(d, compName,"disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		setColor(d, compName, "background", "#FFFFFF");
		
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		
		setColorUIResource(d, compName, "[Focused].background", "#366581");
		setFloat(d, compName, "[Focused].alphaTransparency", "0.5");
		
		setFloat(d, compName, "[Disabled].alphaTransparency", "0.5");
		setFloat(d, compName, "[Enabled].alphaTransparency", "0.3");
//		setFloat(d, compName, "[Focused].alphaTransparency", "0.3");
		setFloat(d, compName, "[MouseOver].alphaTransparency", "0.5");
		setFloat(d, compName, "[Focused+MouseOver].alphaTransparency", "0.5");
		setFloat(d, compName, "[Pressed].alphaTransparency", "0.5");
		setFloat(d, compName, "[Focused+Pressed].alphaTransparency", "0.5");
		
		setFloat(d, compName, "[Selected].alphaTransparency", "0.5");
		setFloat(d, compName, "[Disabled+Selected].alphaTransparency", "0.5");
		setFloat(d, compName, "[Focused+Selected].alphaTransparency", "0.5");
		setFloat(d, compName, "[MouseOver+Selected].alphaTransparency", "0.5");
		setFloat(d, compName, "[Focused+MouseOver+Selected].alphaTransparency", "0.5");
		setFloat(d, compName, "[Pressed+Selected].alphaTransparency", "0.5");
		setFloat(d, compName, "[Focused+Pressed+Selected].alphaTransparency", "0.5");
		
		String pClass = "com.ontimize.plaf.painter.OFormHeaderPopupButtonPainter";
		PaintContext ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "7 7 7 7"), new Dimension(104, 33), false, PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
		
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_SELECTED, ctx));
		d.put(compName + "[Disabled+Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_DISABLED_SELECTED, ctx));
		d.put(compName + "[Focused+Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_SELECTED_FOCUSED, ctx));
		d.put(compName + "[MouseOver+Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_MOUSEOVER_SELECTED, ctx));
		d.put(compName + "[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_MOUSEOVER_SELECTED_FOCUSED, ctx));
		d.put(compName + "[Pressed+Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_PRESSED_SELECTED, ctx));
		d.put(compName + "[Focused+Pressed+Selected].backgroundPainter", new LazyPainter(pClass, OToggleButtonPainter.BACKGROUND_PRESSED_SELECTED_FOCUSED, ctx));
		
	}

	protected void defineButtonSelection(UIDefaults d) {
		// ButtonSelection...
		String compName = "\"ButtonSelection\"";

		ButtonSelection.defaultButtonSelectionCapable = true;
		ButtonSelection.defaultButtonSelectionPaintFocus = true;

		setInsetsUIResource(d, compName, "contentMargins", "16 16 16 16");
		setFontUIResource(d, compName, "font", null);
		setBoolean(d, compName, "defaultButtonFollowsFocus", "false");
		setColorUIResource(d, compName,"disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		setColor(d, compName, "background", "#FFFFFF");
		
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		
		setColorUIResource(d, compName, "[Focused].background", "#366581");
		setFloat(d, compName, "[Focused].alphaTransparency", "0.5");


		String pClass = "com.ontimize.plaf.painter.OButtonSelectionPainter";
		PaintContext ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "16 16 16 16"), new Dimension(33, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

		d.put(compName + "[Default].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT, ctx));
		d.put(compName + "[Default+Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
	}
	
	protected void defineMenuButtonSelection(UIDefaults d) {
		// MenuButtonSelection...
		String compName = "\"MenuButtonSelection\"";
		
		ButtonSelection.defaultMenuButtonSelectionCapable = true;
		ButtonSelection.defaultMenuButtonSelectionPaintFocus = true;
		ButtonSelection.defaultMenuButtonSelectionContentAreaFilled = true;

		setInsetsUIResource(d, compName, "contentMargins", "10 0 10 0");
		setFontUIResource(d, compName, "font", null);
		setBoolean(d, compName, "defaultButtonFollowsFocus", "false");
		setColorUIResource(d, compName,"disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		setColor(d, compName, "background", "#FFFFFF");
		
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		
		setColorUIResource(d, compName, "[Focused].background", "#366581");
		setFloat(d, compName, "[Focused].alphaTransparency", "0.5");
		
		setColor(d, compName, "[Disabled].foreground", "#000000");
		setColor(d, compName, "[Enabled].foreground", "#000000");
		setColor(d, compName, "[Focused].foreground", "#000000");
		setColor(d, compName, "[MouseOver].foreground", "#000000");
		setColor(d, compName, "[Pressed].foreground", "#000000");

		String pClass = "com.ontimize.plaf.painter.OMenuButtonSelectionPainter";
		PaintContext ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "10 0 10 0"), new Dimension(33, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

		d.put(compName + "[Default].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT, ctx));
		d.put(compName + "[Default+Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
	}
	
	protected void defineToolbarNavigatorMenuButtonSelection(UIDefaults d) {
		// MenuButtonSelection...
		String compName = "\"ToolbarNavigatorMenuButtonSelection\"";
		
		ButtonSelection.defaultMenuButtonSelectionCapable = true;
		ButtonSelection.defaultMenuButtonSelectionPaintFocus = true;
		ButtonSelection.defaultMenuButtonSelectionContentAreaFilled = true;

		setInsetsUIResource(d, compName, "contentMargins", "8 8 8 0");
		setFontUIResource(d, compName, "font", null);
		setBoolean(d, compName, "defaultButtonFollowsFocus", "false");
		setColorUIResource(d, compName,"disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		setColor(d, compName, "background", "#FFFFFF");
		
		setColorUIResource(d, compName, "textForeground", "#335971");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#8F9CA4");
		
		setColorUIResource(d, compName, "[Focused].background", "#366581");
		setFloat(d, compName, "[Focused].alphaTransparency", "0.5");
		
		setColor(d, compName, "[Disabled].foreground", "#929292");
		setColor(d, compName, "[Enabled].foreground", "#000000");
		setColor(d, compName, "[Focused].foreground", "#000000");
		setColor(d, compName, "[MouseOver].foreground", "#000000");
		setColor(d, compName, "[Pressed].foreground", "#000000");

		String pClass = "com.ontimize.plaf.painter.OToolbarNavigatorMenuButtonSelectionPainter";
		PaintContext ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "8 0 8 0"), new Dimension(33, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

		d.put(compName + "[Default].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT, ctx));
		d.put(compName + "[Default+Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Default+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT, ctx));
		d.put(compName + "[Default+Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_DEFAULT_FOCUSED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Focused].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_FOCUSED, ctx));
		d.put(compName + "[MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER, ctx));
		d.put(compName + "[Focused+MouseOver].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED, ctx));
		d.put(compName + "[Focused+Pressed].backgroundPainter", new LazyPainter(pClass, AbstractOButtonPainter.BACKGROUND_PRESSED_FOCUSED, ctx));
	}

	protected void defineCollapsibleButtonPanel(UIDefaults d) {
		String prefix = "\"CollapsibleButtonPanel\"";

		setBoolean(d, prefix, "opaque", "false");
		setInsetsUIResource(d, prefix, "contentMargins", "2 3 2 6");
		
		
		setColor(d, prefix, "lineBorderColor", "#90999E");
		setColor(d, prefix, "backgroundColor", "#FFFFFF");
		CollapsibleButtonPanel.backgroundColor = null;
		CollapsibleButtonPanel.lineBorderColor = null;
		CollapsibleButtonPanel.leftIconPath = StyleUtil.getIconPath(prefix, "arrowLeftIcon", "com/ontimize/plaf/images/allleftarrow.png");
		CollapsibleButtonPanel.rightIconPath = StyleUtil.getIconPath(prefix, "arrowRightIcon", "com/ontimize/plaf/images/allrightarrow.png");
		
		Insets in = StyleUtil.getInsets(prefix, "contentMargins", "2 3 1 4");
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(in, new Dimension(20, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);
		d.put(prefix + ".backgroundPainter", new OntimizeDefaults.LazyPainter("com.ontimize.plaf.painter.OCollapsibleButtonPanelPainter", OCollapsibleButtonPanelPainter.BACKGROUND_ENABLED, ctx));

	}

	protected void defineCollapsiblePanel(UIDefaults d) {
		// Assigning CurveMattedDeployableBorder to collapsible panel.
		String prefix = "CollapsiblePanel";
		Font cPFont = this.getDefaultFont();
		cPFont = cPFont.deriveFont(Font.BOLD, (float)cPFont.getSize()+1);
		CurveMattedDeployableBorder.defaultFont = StyleUtil.getFont(prefix, "font", OntimizeLAFParseUtils.fontToString(cPFont) );
		CollapsiblePanel.borderClass = "com.ontimize.gui.container.CurveMattedDeployableBorder";
		CurveMattedDeployableBorder.defaultRectTitleColor = StyleUtil.getColor(prefix, "titleBackground", "#2a3c48");
		CurveMattedDeployableBorder.defaultFontColor = StyleUtil.getColor(prefix, "foreground", "#e1e1e1");
		CurveMattedDeployableBorder.defaultFontShadowColor = StyleUtil.getColor(prefix, "foregroundShadow", "#22303a");
		CurveMattedDeployableBorder.defaultSolidBandColor = StyleUtil.getColor(prefix, "background", "#eef4f6");
		CurveMattedDeployableBorder.defaultGradientBandInitColor = StyleUtil.getColor(prefix, "backgroundGradientInit", "#dae8ec");
		CurveMattedDeployableBorder.defaultGradientBandEndColor = StyleUtil.getColor(prefix, "backgroundGradientEnd", "#e3ecf0");

		CurveMattedDeployableBorder.ARROW_DOWN_ICON_PATH = StyleUtil.getIconPath(prefix, "arrowDownIcon", "com/ontimize/plaf/images/border/curvematted/arrow_down.png");
		CurveMattedDeployableBorder.ARROW_LEFT_ICON_PATH = StyleUtil.getIconPath(prefix, "arrowLeftIcon", "com/ontimize/plaf/images/border/curvematted/arrow_left.png");
		CurveMattedDeployableBorder.ARROW_RIGHT_ICON_PATH = StyleUtil.getIconPath(prefix, "arrowRightIcon", "com/ontimize/plaf/images/border/curvematted/arrow_right.png");
		CurveMattedDeployableBorder.ARROW_UP_ICON_PATH = StyleUtil.getIconPath(prefix, "arrowUpIcon", "com/ontimize/plaf/images/border/curvematted/arrow_up.png");
		
	}


	protected void defineComponentToolBar(UIDefaults d) {
		PaintContext ctx = new com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext(new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);

		String prefix = "\"ComponentToolBar\"";

		d.put(prefix + ".topBackgroundColor", OntimizeLAFParseUtils.parseColor("#DAE7ED", Color.black));
		d.put(prefix + ".downBackgroundColor", OntimizeLAFParseUtils.parseColor("#A2ABB0", Color.black));
		d.put(prefix + ".backgroundPainter", new OntimizeDefaults.LazyPainter("com.ontimize.plaf.painter.OComponentToolBarPainter", OTableButtonPanelPainter.BACKGROUND_ENABLED, ctx));
	}

	protected void defineFormTitle(UIDefaults d) {

		String prefix = "\"FormTitle\"";
		String pClass = "com.ontimize.plaf.painter.OFormTitlePainter";

		d.put(prefix + ".contentMargins", new InsetsUIResource(1, 60, 2, 60));
		
		Font fTFont = this.getDefaultFont();
		fTFont = fTFont.deriveFont(Font.BOLD, (float)fTFont.getSize()+5);
		d.put(prefix + ".font", StyleUtil.getFont(prefix, "font", OntimizeLAFParseUtils.fontToString(fTFont)));
		setColorUIResource(d, prefix, "textForeground", "#E9ECEE");
		setColorUIResource(d, prefix, "textForegroundShadow", "#616C73");
		setColorUIResource(d, prefix, "background", "#91A2AC");
		setPaint(d, prefix, "bgpaint", null);
		setColorUIResource(d, prefix, "shadow", "#00000033");

		PaintContext ctx = new PaintContext(new Insets(5, 60, 5, 60), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);
		d.put(prefix + ".backgroundPainter", new OntimizeDefaults.LazyPainter(pClass, OFormTitlePainter.BACKGROUND_ENABLED, ctx));
		d.put(prefix + ".borderPainter", new OntimizeDefaults.LazyPainter(pClass, OFormTitlePainter.BORDER_ENABLED, ctx));

	}

	protected void defineTabbedPane(UIDefaults d) {

		// TabbedPane:
		String compName = "TabbedPane";

		d.put(compName + ".States", "Enabled,Disabled");
		setInsetsUIResource(d, compName, "contentMargins", "8 12 12 12");
		FontUIResource font = StyleUtil.getFontUIResource(compName, "font", OntimizeLAFParseUtils.fontToString(getDefaultFont()));
		d.put("TabbedPane.font", font);
		setBoolean(d, compName, "opaque", "false");
		setInteger(d, compName, "textIconGap", "3");
		
		setColorUIResource(d, compName, "foreground", null);
		setColorUIResource(d, compName, "textForeground", "#263945");
		setColorUIResource(d, compName, "background", "#517286");
		setColorUIResource(d, compName, "darkShadow", null);
		setColorUIResource(d, compName, "disabled", null);
		setColorUIResource(d, compName, "disabledText", null);
		setColorUIResource(d, compName, "highlight", null);
		setColorUIResource(d, compName, "shadow", null);
		setBoolean(d, compName, "isTabRollover", null);
		setInteger(d, compName, "tabRunOverlay", null);
		setInteger(d, compName, "tabOverlap", null);
		setBoolean(d, compName, "extendTabsToBase", null);
		setBoolean(d, compName, "tabAreaStatesMatchSelectedTab", null);
		setBoolean(d, compName, "nudgeSelectedLabel", null);
		
		
		setColorUIResource(d, compName, "[Enabled].border", "#000000");
		setColorUIResource(d, compName, "[Disabled].border", "#000000");

		String pClass = "com.ontimize.plaf.painter.OTabbedPanePainter";
		PaintContext ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "8 12 12 12"), new Dimension(68, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put("TabbedPane[Enabled].borderPainter", new LazyPainter(pClass, OTabbedPanePainter.BORDER_ENABLED, ctx));
		d.put("TabbedPane[Disabled].borderPainter", new LazyPainter(pClass, OTabbedPanePainter.BORDER_DISABLED, ctx));

		// Tab...
		compName = "TabbedPane:TabbedPaneTab";
		pClass = "com.ontimize.plaf.painter.OTabbedPaneTabPainter";
		
		setInsetsUIResource(d, compName, "contentMargins", "1 1 1 1");
		d.put("TabbedPane:TabbedPaneTab.font", font);//It must be the same font that TabbedPane!
		
		setColorUIResource(d, compName, "[Enabled].textForeground", "#263945");
		setColorUIResource(d, compName, "[Disabled].textForeground", "#263945");
		setColorUIResource(d, compName, "[Selected].textForeground", "#263945");
		setColorUIResource(d, compName, "[Focused].textForeground", "#263945");
		
		setColorUIResource(d, compName, "[Enabled].background", "#E4E4E4");
		setColorUIResource(d, compName, "[Enabled+MouseOver].background", "#CCCCCC");
		setColorUIResource(d, compName, "[Enabled+Pressed].background", "#77ACD0");
		setColorUIResource(d, compName, "[Disabled].background", "#969396");
		setColorUIResource(d, compName, "[Disabled+Selected].background", "#969396");
		setColorUIResource(d, compName, "[Selected].background", "#77acd0");
		setColorUIResource(d, compName, "[MouseOver+Selected].background", "#77acd0");
		setColorUIResource(d, compName, "[Pressed+Selected].background", "#77acd0");
		setColorUIResource(d, compName, "[Focused+Selected].background", "#628ca9");
		setColorUIResource(d, compName, "[Focused+MouseOver+Selected].background", "#77acd0");
		setColorUIResource(d, compName, "[Focused+Pressed+Selected].background", "#77acd0");
		
		ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "1 1 1 1"), new Dimension(44, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Enabled+MouseOver].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabPainter.BACKGROUND_ENABLED_MOUSEOVER, ctx));
		d.put(compName + "[Enabled+Pressed].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabPainter.BACKGROUND_ENABLED_PRESSED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Disabled+Selected].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabPainter.BACKGROUND_SELECTED_DISABLED, ctx));
		d.put(compName + "[Selected].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabPainter.BACKGROUND_SELECTED, ctx));
		d.put(compName + "[MouseOver+Selected].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabPainter.BACKGROUND_SELECTED_MOUSEOVER, ctx));
		d.put(compName + "[Pressed+Selected].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabPainter.BACKGROUND_SELECTED_PRESSED, ctx));
		d.put(compName + "[Focused+Selected].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabPainter.BACKGROUND_SELECTED_FOCUSED, ctx));
		d.put(compName + "[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabPainter.BACKGROUND_SELECTED_MOUSEOVER_FOCUSED, ctx));
		d.put(compName + "[Focused+Pressed+Selected].backgroundPainter", new OntimizeDefaults.LazyPainter(pClass, OTabbedPaneTabPainter.BACKGROUND_SELECTED_PRESSED_FOCUSED, ctx));

		// *********************************************************************
		// TabArea...
		compName = "TabbedPane:TabbedPaneTabArea";
		pClass = "com.ontimize.plaf.painter.OTabbedPaneTabAreaPainter";

		setInsetsUIResource(d, compName, "contentMargins", "0 0 0 0");
		
		ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "0 0 0 0"), new Dimension(5, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabAreaPainter.BACKGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled+MouseOver].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabAreaPainter.BACKGROUND_ENABLED_MOUSEOVER, ctx));
		d.put(compName + "[Enabled+Pressed].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabAreaPainter.BACKGROUND_ENABLED_PRESSED, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OTabbedPaneTabAreaPainter.BACKGROUND_ENABLED, ctx));

		// *********************************************************************
		// Content...
		compName = "TabbedPane:TabbedPaneContent";
		pClass = "com.ontimize.plaf.painter.OTabbedPaneContentPainter";

		d.put(compName + ".States", "Enabled,Disabled");
		setInsetsUIResource(d, compName, "contentMargins", "5 2 5 2");
		
		setColorUIResource(d, compName, "[Enabled].background", "#517286");
		setColorUIResource(d, compName, "[Disabled].background", "#517286");
		
		ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "5 2 5 2"), new Dimension(68, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, OTabbedPaneContentPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Disabled].backgroundPainter", new LazyPainter(pClass, OTabbedPaneContentPainter.BACKGROUND_DISABLED, ctx));
		// *********************************************************************

		// Buttons...
		String painterClass = "com.ontimize.plaf.painter.OTabbedPaneArrowButtonPainter";
		compName = "TabbedPane:TabbedPaneTabArea:\"TabbedPaneTabArea.button\"";
		
		d.put(compName + ".States", "Enabled,Pressed,Disabled,MouseOver");
		setColorUIResource(d, compName, "[Enabled].foreground", "#BDBDBD");
		setColorUIResource(d, compName, "[Disabled].foreground", "#D0DAE2");
		setColorUIResource(d, compName, "[Pressed].foreground", "#9D9D9D");
		setColorUIResource(d, compName, "[MouseOver].foreground", "#517286");
		
		ctx = new PaintContext(StyleUtil.getInsets(compName, "contentMargins", "1 1 1 1"), new Dimension(68, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		d.put(compName + "[Disabled].foregroundPainter", new LazyPainter(painterClass, OTabbedPaneArrowButtonPainter.FOREGROUND_DISABLED, ctx));
		d.put(compName + "[Enabled].foregroundPainter", new LazyPainter(painterClass, OTabbedPaneArrowButtonPainter.FOREGROUND_ENABLED, ctx));
		d.put(compName + "[Pressed].foregroundPainter", new LazyPainter(painterClass, OTabbedPaneArrowButtonPainter.FOREGROUND_PRESSED, ctx));
		d.put(compName + "[MouseOver].foregroundPainter", new LazyPainter(painterClass, OTabbedPaneArrowButtonPainter.FOREGROUND_MOUSEOVER, ctx));

	}

	protected void defineOntimizeComponents(UIDefaults d) {
		ApplicationManager.useOntimizePlaf = true;
		
		DataField.DEFAULT_BOTTOM_MARGIN = 0;
		DataField.DEFAULT_TOP_MARGIN = 0;
		DataField.DEFAULT_PARENT_MARGIN = 0;
		DataField.DEFAULT_PARENT_MARGIN_FOR_SCROLL = 0;
		DataField.DEFAULT_FIELD_LEFT_MARGIN = 0;
		DataField.DEFAULT_FIELD_RIGHT_MARGIN = 0;
		
		Row.defaultFLayoutHGap = 1;
		Row.defaultFLayoutVGap = 0;

		Form.toUppercaseTitle = true;
		Form.defaultBorderButtons = false;
		
		//Set the height in ReferenceExt table y Form Table View.
		Form.defaultTableViewMinRowHeight = 22;
		
		Tree.enabledRowCount = true;

		BasicTreeCellRenderer.firstNodeConfiguration = true;
		BasicTreeCellRenderer.organizationalForegroundColor = d.getColor("\"Tree.cellRenderer\".textForeground");
		BasicTreeCellRenderer.includeChildCount = false;

		BasicTreeCellRenderer.rootNodeForegroundColor = OntimizeLAFParseUtils.parseColor("#4AAEE3", null);
		BasicTreeCellRenderer.rootNodeSelectionForegroundColor = OntimizeLAFParseUtils.parseColor("#396F8A", null);

		BooleanCellRenderer.USE_CHECKBOX = true;

		FormHeaderButton.createRolloverIcon = true;
		RolloverButton.createRolloverIcon = true;
		FormHeaderPopupButton.createRolloverIcon = true;
		
		//ImageDataField
		Color borderColor = StyleUtil.getColorUI("Image", "border", "#ADC0CE");
		BorderUIResource iBorder = new BorderUIResource(BorderFactory.createLineBorder(borderColor, 2));
		BorderManager.putBorder(BorderManager.DEFAULT_IMAGE_BORDER_KEY, iBorder);

		// VisualCalendar component...
		ImageManager.CALENDAR_PREV = ImageManager.ARROW_LEFT;
		ImageManager.CALENDAR_NEXT = ImageManager.ARROW_RIGHT;
		VisualCalendarComponent.fieldsBorder = null;
		VisualCalendarComponent.HeaderRenderer.headerFont = new Font("Arial", Font.BOLD, 17);
		VisualCalendarComponent.DayRenderer.headerFont = new Font("Arial", Font.PLAIN, 13);

		// PopupList
		Border border = new EmptyBorder(StyleUtil.getInsets("MenuItem", "contentMargins", "0 10 0 10"));
		AttachmentListPopup.itemsBorder = border;
		AttachmentComponent.componentBorder = border;
		
		// ApToolbarNavigator
		ApToolBarNavigator.defaultPrevIconPath = "com/ontimize/plaf/images/toolbar/prev.png";
		ApToolBarNavigator.defaultNextIconPath = "com/ontimize/plaf/images/toolbar/next.png";
		ApToolBarNavigator.defaultListIconPath = "com/ontimize/plaf/images/toolbar/home.png";
		ApToolBarNavigator.defaultOpaque = false;
		ApToolBarNavigator.defaultBorderButtons = false;

		try {
			com.ontimize.login.ShapeLoginDialog.opaque = false;
		} catch (Throwable e) {

		}
		
		//SumRowSetupDialog
		SumRowSetupDialog.defaultSelectedItemBgColor = new Color(0x36627F);
		SumRowSetupDialog.defaultItemFgColor = new Color(0x335971);
		
		//Popup_arrow
		ApToolBarPopupButton.popupArrowIcon = "com/ontimize/plaf/images/popuparrow_white.png";
	}

	protected void defineHTMLDataField(UIDefaults d) {
		HTMLDataField.toolBarFiller = true;
		HTMLDataField.DEFAULT_PARENT_MARGIN_FOR_SCROLL = 0;
		HTMLDataField.DEFAULT_PARENT_MARGIN = 0;
		ImageManager.BOLD_FONT = "com/ontimize/plaf/images/html/text_bold.png";
		ImageManager.ITALIC_FONT = "com/ontimize/plaf/images/html/text_italic.png";
		ImageManager.UNDERLINE_FONT = "com/ontimize/plaf/images/html/text_underline.png";
		ImageManager.LEFT_ALIGN = "com/ontimize/plaf/images/html/text_align_left.png";
		ImageManager.CENTER_ALIGN = "com/ontimize/plaf/images/html/text_align_center.png";
		ImageManager.RIGHT_ALIGN = "com/ontimize/plaf/images/html/text_align_right.png";
		ImageManager.JUSTIFY_ALIGN = "com/ontimize/plaf/images/html/text_align_justify.png";
		ImageManager.CHOOSE_COLOR = "com/ontimize/plaf/images/html/color.png";
		ImageManager.LIST_ORDERED = "com/ontimize/plaf/images/html/listordered.png";
		ImageManager.LIST_UNORDERED = "com/ontimize/plaf/images/html/listunordered.png";
		ImageManager.IMAGE = "com/ontimize/plaf/images/html/html_image.png";
		ImageManager.HTML_TABLE = "com/ontimize/plaf/images/html/html_table.png";
		
		try {
			com.ontimize.gui.field.HTMLShefDataField.defaultHTMLToolbarButtonHeight = 32;
		} catch (Throwable e) {
		}
	}

	protected void defineODMSComponents(UIDefaults d) {
		try {
			System.setProperty("com.ontimize.dms.client.gui.COLOR_CONFIGURATION_FILE", "com/ontimize/plaf/odms/color.properties");
		} catch (Throwable e) {
		}
	}

	protected void defineGanttComponent(UIDefaults d) {
		try {
			com.ontimize.gantt.treetable.JTreeTable.defaultSelectedColor = StyleUtil.getColor("", "", "#517286");
			com.ontimize.gantt.treetable.JTreeTable.fillRowSelectedColor = false;
			com.ontimize.gantt.treetable.JTreeTable.defaultTextSelectionColor = Color.white;
			com.ontimize.gantt.gui.GanttChartGUI.MIN_ROW_HEIGHT = 22;
		} catch (Throwable e) {
		}
	}

	protected void defineAgendaComponent(UIDefaults d) {
		try {
			com.ontimize.agenda.CenterPanel.DAY_ICON = "com/ontimize/plaf/images/agenda/24x24_table_agenda_dia.png";
			com.ontimize.agenda.CenterPanel.WEEK_ICON = "com/ontimize/plaf/images/agenda/24x24_table_agenda_semana.png";
			com.ontimize.agenda.CenterPanel.MONTH_ICON = "com/ontimize/plaf/images/agenda/24x24_table_agenda_mes.png";
			com.ontimize.agenda.CenterPanel.REFRESH_ICON = "com/ontimize/plaf/images/agenda/24x24_table_actualizar.png";
			com.ontimize.agenda.CenterPanel.PREFERENCE_ICON = "com/ontimize/plaf/images/agenda/24x24_table_agenda_conf.png";
			com.ontimize.agenda.CenterPanel.PRINT_ICON = "com/ontimize/plaf/images/table/print.png";
			com.ontimize.agenda.gui.AgendaGUI.toolBarFiller = false;
			com.ontimize.agenda.gui.AgendaGUI.defaultToolBarHeight = 40;
		} catch (Throwable e) {
		}
	}

	protected void defineDiagramComponent(UIDefaults d) {
		try {
			com.ontimize.diagram.ui.DiagramComponent.defaultToolBarHeight = 40;
			Color borderColor = StyleUtil.getColorUI("Diagram", "border", "#ADC0CE");
			com.ontimize.diagram.ui.DiagramComponent.defaultDiagramBorder = BorderFactory.createLineBorder(borderColor, 2);
			com.ontimize.diagram.ui.BasicDiagram.defaultBackgroundColor = StyleUtil.getColor("Diagram", "background", "#FFFFFF");
		} catch (Throwable ex) {

		}

	}
	
	protected void defineGISComponent(UIDefaults d) {
      try {
         com.ontimize.util.gis.client.gui.panels.DraggableToolbar.toolbarHeight = 40;
         com.ontimize.util.gis.client.gui.panels.DraggableToolbar.paintToolbarBorder = false;
         
         com.ontimize.util.gis.client.gui.panels.DraggableToolbar.defaultBorderButtons=true;
         com.ontimize.util.gis.client.gui.panels.DraggableToolbar.defaultcontentAreaFilled=true;
         com.ontimize.util.gis.client.gui.panels.DraggableToolbar.defaultOpaqueButtons=false;
         
         com.ontimize.util.gis.client.gui.panels.components.BasicToolbarButton.buttonHeight = 28;
      } catch (Throwable ex) {

      }

   }
	

	/**
	 * Initialize the root pane settings.
	 * 
	 * @param d
	 *            the UI defaults map.
	 */
	protected void defineRootPanes(UIDefaults d) {

		decorated = true;

		// ***********************
		// FROM Ontimize
		// ***********************
		String compName = "RootPane";
		String pClass = "com.ontimize.plaf.painter.ORootPainter";

		d.put(compName + ".States", "Enabled,WindowFocused,NoFrame");
		d.put(compName + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
		d.put(compName + ".opaque", Boolean.FALSE);
		d.put(compName + ".NoFrame", new ORootPaneNoFrameState());
		d.put(compName + ".WindowFocused", new ORootPaneWindowFocusedState());

		PaintContext ctx = new PaintContext(new Insets(0, 0, 0, 0), new Dimension(100, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);
		d.put(compName + "[Enabled+NoFrame].backgroundPainter", new LazyPainter(pClass, ORootPainter.BACKGROUND_ENABLED_NOFRAME, ctx));
		d.put(compName + "[Enabled].backgroundPainter", new LazyPainter(pClass, ORootPainter.BACKGROUND_ENABLED, ctx));
		d.put(compName + "[Enabled+WindowFocused].backgroundPainter", new LazyPainter(pClass, ORootPainter.BACKGROUND_ENABLED_WINDOWFOCUSED, ctx));

		// ***********************
		// FROM ONTIMIZE OntimizeRootPaneUI
		// ***********************
		// if(UIManager.getBoolean("Ontimize.window.decoration")==true)
		// if (decorated)
		// uidefaults
		// .put("RootPaneUI", "com.ontimize.plaf.OntimizeRootPaneUI");
		// else
		// decorated = false;

		// uidefaults.put("RootPane.border.insets", new InsetsUIResource(2, 2,
		// 2, 2));
		// uidefaults.put("RootPane.border.background",
		// OntimizeLAFColorUtils.colorHexToColor("#E7EFF1"));
		// uidefaults.put("RootPane.border.arc", new Integer(8));
		//
		d.put("RootPane.titlePane.background", OntimizeLAFColorUtils.colorHexToColor("#05A9C4"));
		// uidefaults.put("RootPane.titlePane.height", new Integer(25));
		// uidefaults.put("RootPane.titlePane.line.background",
		// OntimizeLAFColorUtils.colorHexToColor("#FFFFFF", 102));
		// uidefaults.put("RootPane.titlePane.arc", new Integer(4));

	}

	/**
	 * This method is override in order to populate the uidefaults object with
	 * the configuration information provided by the CSS file or the default one.
	 * For that fact, information is put in that
	 * UIDefaults Object by using the put(key, value) method.
	 * 
	 */
	@SuppressWarnings("restriction")
	@Override
	public UIDefaults getDefaults() {
		if (!initialized) {
			initialized = true;
			UIDefaults uidefaults = super.getDefaults();

			// Define customized UI's...
			defineUI(uidefaults, "ComboBox");
			
			defineUI(uidefaults, "Label");

			defineUI(uidefaults, "List");
			
//			defineUI(uidefaults, "Panel");
			
			defineUI(uidefaults, "PasswordField");

			// defineUI(uidefaults, "RootPane");

			defineUI(uidefaults, "ScrollBar");
			
			defineUI(uidefaults, "ScrollPane");

			defineUI(uidefaults, "TabbedPane");
			
			defineUI(uidefaults, "Table");

			defineUI(uidefaults, "TableHeader");

			defineUI(uidefaults, "TextField");
			
			defineUI(uidefaults, "TextArea");

			defineUI(uidefaults, "ToolBar");
			
			defineUI(uidefaults, "ToolTip");
			
			defineUI(uidefaults, "Tree");

			defineUI(uidefaults, "Viewport");

			// Set the default font.
			defineDefaultFont(uidefaults);

			// Define painter components configuration...
			defineButton(uidefaults);

			defineCheckBox(uidefaults);

			defineCheckBoxMenuItem(uidefaults);

			defineComboBox(uidefaults);

			defineEditorPane(uidefaults);
			
			defineFileChooser(uidefaults);

			defineLabel(uidefaults);

			defineList(uidefaults);

			defineMenu(uidefaults);

			defineMenuBar(uidefaults);

			defineMenuItem(uidefaults);
			
			defineOptionPane(uidefaults);
			
			definePanel(uidefaults);
			
			definePassword(uidefaults);

			definePopupMenu(uidefaults);

			definePopupItem(uidefaults);

			definePopupMenuSeparator(uidefaults);
			
			defineProgressBar(uidefaults);

			defineRadioButtonMenuItem(uidefaults);

			// defineRootPanes(uidefaults);

			defineScrollBar(uidefaults);

			defineScrollPane(uidefaults);
			
			defineSeparator(uidefaults);

			defineSliders(uidefaults);

			defineSplitPane(uidefaults);

			defineTabbedPane(uidefaults);

			defineTextFields(uidefaults);
			
			defineTextPane(uidefaults);
			
			defineToggleButton(uidefaults);

			defineToolBar(uidefaults);
			
			defineToolBarButton(uidefaults);
			
			defineToolBarToggleButton(uidefaults);
			
			defineToolBarSeparator(uidefaults);
			
			defineToolTip(uidefaults);

			defineTree(uidefaults);

			// defineViewport(uidefaults);

			// Ontimize components...
			defineCardPanel(uidefaults);
			defineCollapsibleButtonPanel(uidefaults);
			defineCollapsiblePanel(uidefaults);
			defineColumn(uidefaults);
			defineELabel(uidefaults);
			defineFieldButton(uidefaults);
			defineFormTitle(uidefaults);
			defineFormButton(uidefaults);
			defineFormButtonPanel(uidefaults);
			defineFormBodyPanel(uidefaults);
			defineFormHeader(uidefaults);
			defineFormHeaderButton(uidefaults);
			defineFormHeaderPopupButton(uidefaults);
			defineHTMLDataField(uidefaults);
			defineButtonSelection(uidefaults);
			defineMenuButtonSelection(uidefaults);
			defineToolbarNavigatorMenuButtonSelection(uidefaults);
			defineRadioButton(uidefaults);
			defineQuickFilter(uidefaults);
			defineRow(uidefaults);
			defineSelectableItem(uidefaults);
			defineTable(uidefaults);
			defineTableButton(uidefaults);
			defineTableButtonPanel(uidefaults);
			defineTableButtonFooterPanel(uidefaults);
			defineTableCellRenderer(uidefaults);
			defineTableCellEditor(uidefaults);
			defineTableHeader(uidefaults);
			defineTableHeaderRenderer(uidefaults);
			defineTitleBorder(uidefaults);
			defineOntimizeComponents(uidefaults);
			defineODMSComponents(uidefaults);
			defineGanttComponent(uidefaults);
			defineGrid(uidefaults);
			defineAgendaComponent(uidefaults);
			defineComponentToolBar(uidefaults);
			defineDiagramComponent(uidefaults);
			defineGISComponent(uidefaults);
			defineReferenceExtComponent(uidefaults);


			uidefaults.put("\"FrameButton\".backgroundPainter", new OntimizeDefaults.LazyPainter("com.ontimize.plaf.painter.ButtonPainter", ButtonPainter.BACKGROUND_DEFAULT, "com/ontimize/plaf/images/closeIcon.png"));

			uidefaults.put("InternalFrame.maximizeIcon", createFrameMaximizeIcon());
			uidefaults.put("InternalFrame.minimizeIcon", createFrameIconifyIcon());
			uidefaults.put("InternalFrame.iconifyIcon", createFrameIconifyIcon());
			uidefaults.put("InternalFrame.closeIcon", createFrameCloseIcon());

			JFrame.setDefaultLookAndFeelDecorated(decorated);
			JDialog.setDefaultLookAndFeelDecorated(decorated);

		}
		return super.getDefaults();
	}

	protected void defineUI(UIDefaults d, String uiName) {
		uiName = uiName + "UI";
		d.put(uiName, UI_PACKAGE_PREFIX + uiName);
	}

	/**
	 * Registers the given region and prefix. The prefix, if it contains quoted
	 * sections, refers to certain named components. If there are not quoted
	 * sections, then the prefix refers to a generic component type.
	 * 
	 * <p>
	 * If the given region/prefix combo has already been registered, then it
	 * will not be registered twice. The second registration attempt will fail
	 * silently.
	 * </p>
	 * 
	 * @param region
	 *            The Synth Region that is being registered. Such as Button, or
	 *            ScrollBarThumb.
	 * @param prefix
	 *            The UIDefault prefix. For example, could be ComboBox, or if a
	 *            named components, "MyComboBox", or even something like
	 *            ToolBar:"MyComboBox":"ComboBox.arrowButton"
	 */
	public void register(Region region, String prefix) {
		super.register(region, prefix);
	}

	/**
	 * Locate the style associated with the given region and component. This is
	 * called from OntimizeLookAndFeel in the SynthStyleFactory implementation.
	 * 
	 * <p>
	 * Lookup occurs as follows:<br/>
	 * Check the map of styles <code>styleMap</code>. If the map contains no
	 * styles at all, then simply return the defaultStyle. If the map contains
	 * styles, then iterate over all of the styles for the Region <code>r</code>
	 * looking for the best match, based on prefix. If a match was made, then
	 * return that SynthStyle. Otherwise, return the defaultStyle.
	 * </p>
	 * 
	 * @param c
	 *            The component associated with this region. For example, if the
	 *            Region is Region.Button then the component will be a JButton.
	 *            If the Region is a subregion, such as ScrollBarThumb, then the
	 *            associated component will be the component that subregion
	 *            belongs to, such as JScrollBar. The JComponent may be named.
	 *            It may not be null.
	 * @param r
	 *            The region we are looking for a style for. May not be null.
	 * 
	 * @return the style associated with the given region and component.
	 */
	public static SynthStyle getOntimizeStyle(JComponent c, Region r) {
		return  SynthLookAndFeel.getStyle(c, r);
	}
	

	/**
	 * A convience method that will reset the Style of StyleContext if
	 * necessary.
	 * 
	 * @param context
	 *            the SynthContext corresponding to the current state.
	 * @param ui
	 *            the UI delegate.
	 * 
	 * @return the new, updated style.
	 */
	public static SynthStyle updateStyle(SynthContext context, SynthUI ui) {
		SynthStyle newStyle = SynthLookAndFeel.getStyle(context.getComponent(), context.getRegion());
		SynthStyle oldStyle = context.getStyle();

		if (newStyle != oldStyle) {

			if (oldStyle != null) {
				oldStyle.uninstallDefaults(context);
			}

			ReflectionUtils.invoke(context, "setStyle", newStyle);
			if (newStyle instanceof OntimizeStyle) {
				((OntimizeStyle) newStyle).installDefaults(context, ui);
			} else {
				newStyle.installDefaults(context);
			}
		}

		return newStyle;
	}

	/**
	 * Returns the component state for the specified component. This should only
	 * be used for Components that don't have any special state beyond that of
	 * ENABLED, DISABLED or FOCUSED. For example, buttons shouldn't call into
	 * this method.
	 */
	public static int getComponentState(Component c) {
		if (c.isEnabled()) {
			if (c.isFocusOwner()) {
				return SynthUI.ENABLED | SynthUI.FOCUSED;
			}
			return SynthUI.ENABLED;
		}
		return SynthUI.DISABLED;
	}

	/**
	 * Returns the Region for the JComponent <code>c</code>.
	 * 
	 * @param c
	 *            JComponent to fetch the Region for
	 * @return Region corresponding to <code>c</code>
	 */
	public static Region getRegion(JComponent c) {
		return OntimizeRegion.getRegion(c);
	}

	/**
	 * A convenience method that handles painting of the background. All SynthUI
	 * implementations should override update and invoke this method.
	 * 
	 * @param state
	 *            the SynthContext describing the current component and state.
	 * @param g
	 *            the Graphics context to use to paint the component.
	 */
	public static void update(SynthContext state, Graphics g) {
		paintRegion(state, g, null);
	}

	/**
	 * A convenience method that handles painting of the background for
	 * subregions. All SynthUI's that have subregions should invoke this method,
	 * than paint the foreground.
	 * 
	 * @param state
	 *            the SynthContext describing the component, region, and state.
	 * @param g
	 *            the Graphics context used to paint the subregion.
	 * @param bounds
	 *            the bounds to paint in.
	 */
	public static void updateSubregion(SynthContext state, Graphics g, Rectangle bounds) {
		paintRegion(state, g, bounds);
	}

	/**
	 * Paint a region.
	 * 
	 * @param state
	 *            the SynthContext describing the current component, region, and
	 *            state.
	 * @param g
	 *            the Graphics context used to paint the subregion.
	 * @param bounds
	 *            the bounds to paint in.
	 */
	protected static void paintRegion(SynthContext state, Graphics g, Rectangle bounds) {
		JComponent c = state.getComponent();
		SynthStyle style = state.getStyle();
		int x;
		int y;
		int width;
		int height;

		if (bounds == null) {
			x = 0;
			y = 0;
			width = c.getWidth();
			height = c.getHeight();
		} else {
			x = bounds.x;
			y = bounds.y;
			width = bounds.width;
			height = bounds.height;
		}

		// Fill in the background, if necessary.
		boolean subregion = state.getRegion().isSubregion();

		// TODO Review if fillRect is necessary.
		if ((subregion && style.isOpaque(state)) || (!subregion && c.isOpaque())) {
			
			if (!(c instanceof JTextField)) {
				g.setColor(style.getColor(state, ColorType.BACKGROUND));
				g.fillRect(x, y, width, height);
			}
			if(c instanceof JTextField && "Tree.cellEditor".equals(c.getName())){
				g.setColor(style.getColor(state, ColorType.BACKGROUND));
				g.fillRect(x, y, width, height);
			}
		}
	}

	/**
	 * Returns true if the Style should be updated in response to the specified
	 * PropertyChangeEvent. This forwards to <code>
	 * shouldUpdateStyleOnAncestorChanged</code> as necessary.
	 * 
	 * @param event
	 *            the property change event.
	 * 
	 * @return {@code true} if the style should be updated as a result of this
	 *         property change, {@code false} otherwise.
	 */
	public static boolean shouldUpdateStyle(PropertyChangeEvent event) {
		String eName = event.getPropertyName();

		if ("name" == eName) {

			// Always update on a name change
			return true;
		} else if ("componentOrientation" == eName) {

			// Always update on a component orientation change
			return true;
		} else if ("ancestor" == eName && event.getNewValue() != null) {

			// Only update on an ancestor change when getting a valid
			// parent and the LookAndFeel wants this.
			LookAndFeel laf = UIManager.getLookAndFeel();

			return (laf instanceof SynthLookAndFeel && ((SynthLookAndFeel) laf).shouldUpdateStyleOnAncestorChanged());
		}
		/*
		 * Note: The following two Ontimize based overrides should be
		 * refactored to be in the Ontimize LAF. Due to constraints in an update
		 * release, we couldn't actually provide the public API necessary to
		 * allow OntimizeLookAndFeel (a subclass of SynthLookAndFeel) to provide
		 * its own rules for shouldUpdateStyle.
		 */
		else if ("Ontimize.Overrides" == eName) {

			// Always update when the Ontimize.Overrides client property has
			// been changed
			return true;
		} else if ("Ontimize.Overrides.InheritDefaults" == eName) {

			// Always update when the Ontimize.Overrides.InheritDefaults
			// client property has changed
			return true;
		} else if ("JComponent.sizeVariant" == eName) {

			// Always update when the JComponent.sizeVariant
			// client property has changed
			return true;
		} else if (eName != null && (eName.startsWith("JButton.") || eName.startsWith("JTextField."))) {

			// Always update when an Apple-style variant client property has
			// changed.
			return true;
		} 

		return false;
	}

	/**
	 * Called by UIManager when this look and feel is installed.
	 */
	@Override
	public void initialize() {
		super.initialize();
		final SynthStyleFactory styleFactory = SynthLookAndFeel.getStyleFactory();
		// create synth style factory
		setStyleFactory(new SynthStyleFactory() {
			@Override
			public SynthStyle getStyle(JComponent c, Region r) {
				SynthStyle style = styleFactory.getStyle(c, r);

				if (!(style instanceof OntimizeStyle)) {
					style = new OntimizeStyleWrapper(style);
				}

				return style;
			}
		});
		
		//Create popup Factory.
		PopupFactory.setSharedInstance(new OntimizePopupFactory());
	}

	/**
	 * Called by UIManager when this look and feel is uninstalled.
	 */
	@Override
	public void uninitialize() {
		super.uninitialize();
//		uidefaults.clear();
//		OntimizeStyle.uninitialize();
		com.ontimize.plaf.utils.ImageCache.getInstance().flush();
//		styleMap.clear();
//		registeredRegions.clear();
		setStyleFactory(null);
	}

	/**
	 * Initialize the map of styles.
	 */
	protected void registerStyles() {
		register(Region.ARROW_BUTTON, "ArrowButton");
		register(Region.BUTTON, "Button");
		register(Region.TOGGLE_BUTTON, "ToggleButton");
		register(Region.RADIO_BUTTON, "RadioButton");
		register(Region.CHECK_BOX, "CheckBox");
		register(Region.COLOR_CHOOSER, "ColorChooser");
		register(Region.PANEL, "ColorChooser:\"ColorChooser.previewPanelHolder\"");
		register(Region.LABEL, "ColorChooser:\"ColorChooser.previewPanelHolder\":\"OptionPane.label\"");
		register(Region.COMBO_BOX, "ComboBox");
		register(Region.TEXT_FIELD, "ComboBox:\"ComboBox.textField\"");
		register(Region.ARROW_BUTTON, "ComboBox:\"ComboBox.arrowButton\"");
		register(Region.LABEL, "ComboBox:\"ComboBox.listRenderer\"");
		register(Region.LABEL, "ComboBox:\"ComboBox.renderer\"");
		register(Region.SCROLL_PANE, "\"ComboBox.scrollPane\"");
		register(Region.FILE_CHOOSER, "FileChooser");
		register(Region.INTERNAL_FRAME_TITLE_PANE, "InternalFrameTitlePane");
		register(Region.INTERNAL_FRAME, "InternalFrame");
		register(Region.INTERNAL_FRAME_TITLE_PANE, "InternalFrame:InternalFrameTitlePane");
		register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"");
		register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"");
		register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"");
		register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"");
		register(Region.DESKTOP_ICON, "DesktopIcon");
		register(Region.DESKTOP_PANE, "DesktopPane");
		register(Region.LABEL, "Label");
		register(Region.LIST, "List");
		register(Region.LABEL, "List:\"List.cellRenderer\"");
		register(Region.MENU_BAR, "MenuBar");
		register(Region.MENU, "MenuBar:Menu");
		register(Region.MENU_ITEM_ACCELERATOR, "MenuBar:Menu:MenuItemAccelerator");
		register(Region.MENU_ITEM, "MenuItem");
		register(Region.MENU_ITEM_ACCELERATOR, "MenuItem:MenuItemAccelerator");
		register(Region.RADIO_BUTTON_MENU_ITEM, "RadioButtonMenuItem");
		register(Region.MENU_ITEM_ACCELERATOR, "RadioButtonMenuItem:MenuItemAccelerator");
		register(Region.CHECK_BOX_MENU_ITEM, "CheckBoxMenuItem");
		register(Region.MENU_ITEM_ACCELERATOR, "CheckBoxMenuItem:MenuItemAccelerator");
		register(Region.MENU, "Menu");
		register(Region.MENU_ITEM_ACCELERATOR, "Menu:MenuItemAccelerator");
		register(Region.POPUP_MENU, "PopupMenu");
		register(Region.POPUP_MENU_SEPARATOR, "PopupMenuSeparator");
		register(Region.OPTION_PANE, "OptionPane");
		register(Region.SEPARATOR, "OptionPane:\"OptionPane.separator\"");
		register(Region.PANEL, "OptionPane:\"OptionPane.messageArea\"");
		register(Region.LABEL, "OptionPane:\"OptionPane.messageArea\":\"OptionPane.label\"");
		register(Region.PANEL, "Panel");
		register(Region.PROGRESS_BAR, "ProgressBar");
		register(Region.SEPARATOR, "Separator");
		register(Region.SCROLL_BAR, "ScrollBar");
		register(Region.ARROW_BUTTON, "ScrollBar:\"ScrollBar.button\"");
		register(Region.SCROLL_BAR_THUMB, "ScrollBar:ScrollBarThumb");
		register(Region.SCROLL_BAR_TRACK, "ScrollBar:ScrollBarTrack");
		register(Region.SCROLL_PANE, "ScrollPane");
		register(Region.VIEWPORT, "Viewport");
		register(Region.SLIDER, "Slider");
		register(Region.SLIDER_THUMB, "Slider:SliderThumb");
		register(Region.SLIDER_TRACK, "Slider:SliderTrack");
		register(Region.SPINNER, "Spinner");
		register(Region.PANEL, "Spinner:\"Spinner.editor\"");
		register(Region.FORMATTED_TEXT_FIELD, "Spinner:Panel:\"Spinner.formattedTextField\"");
		register(Region.ARROW_BUTTON, "Spinner:\"Spinner.previousButton\"");
		register(Region.ARROW_BUTTON, "Spinner:\"Spinner.nextButton\"");
		register(Region.SPLIT_PANE, "SplitPane");
		register(Region.SPLIT_PANE_DIVIDER, "SplitPane:SplitPaneDivider");
		register(Region.TABBED_PANE, "TabbedPane");
		register(Region.TABBED_PANE_TAB, "TabbedPane:TabbedPaneTab");
		register(Region.TABBED_PANE_TAB_AREA, "TabbedPane:TabbedPaneTabArea");
		register(Region.TABBED_PANE_CONTENT, "TabbedPane:TabbedPaneContent");

		register(Region.ARROW_BUTTON, "TabbedPane:TabbedPaneTabArea:\"TabbedPaneTabArea.button\"");

		register(Region.TABLE, "Table");
		register(Region.LABEL, "Table:\"Table.cellRenderer\"");
		register(Region.LABEL, "Table:\"VisualCalendar:Table.cellRenderer\"");
		register(Region.TABLE_HEADER, "TableHeader");
		register(Region.LABEL, "TableHeader:\"TableHeader.renderer\"");
		register(Region.LABEL, "TableHeader:\"VisualCalendar:TableHeader.renderer\"");
		register(Region.TEXT_FIELD, "\"Table.editor\"");
		register(Region.TEXT_FIELD, "\"Tree.cellEditor\"");
		register(Region.TEXT_FIELD, "TextField");
		register(Region.FORMATTED_TEXT_FIELD, "FormattedTextField");
		register(Region.PASSWORD_FIELD, "PasswordField");
		register(Region.TEXT_AREA, "TextArea");
		register(Region.TEXT_PANE, "TextPane");
		register(Region.EDITOR_PANE, "EditorPane");
		register(Region.TOOL_BAR, "ToolBar");
		register(Region.BUTTON, "ToolBar:Button");
		register(Region.TOGGLE_BUTTON, "ToolBar:ToggleButton");
		register(Region.TOOL_BAR_SEPARATOR, "ToolBarSeparator");
		register(Region.TOOL_TIP, "ToolTip");
		register(Region.TREE, "Tree");
		register(Region.TREE_CELL, "Tree:TreeCell");
		register(Region.ROOT_PANE, "RootPane");

		// ********************************************************************
		// Ontimize Components.
		// ***************************************************************
		register(Region.BUTTON, "\"FormButton\"");
		register(Region.BUTTON, "\"FrameButton\"");
		register(Region.BUTTON, "\"FormHeaderButton\"");
		register(Region.BUTTON, "\"TableButton\"");
		register(Region.BUTTON, "\"ButtonSelection\"");
		register(Region.BUTTON, "\"MenuButtonSelection\"");
		register(Region.BUTTON, "\"ToolbarNavigatorMenuButtonSelection\"");
		register(Region.BUTTON, "\"QueryFilterButton\"");
		register(Region.BUTTON, "\"FieldButton\"");
		
		register(Region.CHECK_BOX, "\"SelectableItem\"");
		
		register(Region.TOGGLE_BUTTON, "\"FormHeaderPopupButton\"");
		register(Region.TOGGLE_BUTTON, "\"TableButton\"");

		register(Region.PANEL, "\"Form\"");
		register(Region.PANEL, "\"FormExt\"");
		register(Region.PANEL, "\"Row\"");
		register(Region.PANEL, "\"Column\"");
		register(Region.PANEL, "\"CardPanel\"");
		register(Region.PANEL, "\"Row\"");
		register(Region.PANEL, "\"Grid\"");
		register(Region.PANEL, "\"TableButtonPanel\"");
		register(Region.PANEL, "\"TableButtonFooterPanel\"");
		register(Region.PANEL, "\"FormButtonPanel\"");
		register(Region.PANEL, "\"FormBodyPanel\"");

		register(Region.LABEL, "\"CollapsibleButtonPanel\"");
		register(Region.LABEL, "\"ELabel\"");
		register(Region.LABEL, "\"Tree.cellRenderer\"");
		register(Region.LABEL, "\"PopupItem\"");
		register(Region.LABEL, "\"PageFetcher.Label\"");

		register(Region.TEXT_FIELD, "\"FormTitle\"");
		register(Region.TEXT_FIELD, "Table:\"Table.QuickFilter\"");
		register(Region.TEXT_FIELD, "TextField:\"TextField.ReferenceExt\"");
		register(Region.TEXT_FIELD, "TextField:\"TextField.ReferenceExtCode\"");

		register(Region.TOOL_BAR, "\"ComponentToolBar\"");
	}

	/**
	 * Returns the ui that is of type <code>klass</code>, or null if one can not
	 * be found.
	 * 
	 * @param ui
	 *            the UI delegate to be tested.
	 * @param klass
	 *            the class to test against.
	 * 
	 * @return {@code ui} if {@code klass} is an instance of {@code ui},
	 *         {@code null} otherwise.
	 */
	public static Object getUIOfType(ComponentUI ui, Class klass) {
		if (klass.isInstance(ui)) {
			return ui;
		}

		return null;
	}

	/**
	 * Package protected method which returns either BorderLayout.NORTH,
	 * BorderLayout.SOUTH, BorderLayout.EAST, or BorderLayout.WEST depending on
	 * the location of the toolbar in its parent. The toolbar might be in
	 * PAGE_START, PAGE_END, CENTER, or some other position, but will be
	 * resolved to either NORTH,SOUTH,EAST, or WEST based on where the toolbar
	 * actually IS, with CENTER being NORTH.
	 * 
	 * <p/>
	 * This code is used to determine where the border line should be drawn by
	 * the custom toolbar states, and also used by OntimizeIcon to determine
	 * whether the handle icon needs to be shifted to look correct.
	 * </p>
	 * 
	 * <p>
	 * Toollbars are unfortunately odd in the way these things are handled, and
	 * so this code exists to unify the logic related to toolbars so it can be
	 * shared among the static files such as OntimizeIcon and generated files
	 * such as the ToolBar state classes.
	 * </p>
	 * 
	 * @param toolbar
	 *            a toolbar in the Swing hierarchy.
	 * 
	 * @return the {@code BorderLayout} orientation of the toolbar, or
	 *         {@code BorderLayout.NORTH} if none can be determined.
	 */
	public static Object resolveToolbarConstraint(JToolBar toolbar) {
		/*
		 * NOTE: we don't worry about component orientation or PAGE_END etc
		 * because the BasicToolBarUI always uses an absolute position of
		 * NORTH/SOUTH/EAST/WEST.
		 */
		if (toolbar != null) {
			Container parent = toolbar.getParent();

			if (parent != null) {
				LayoutManager m = parent.getLayout();

				if (m instanceof BorderLayout) {
					BorderLayout b = (BorderLayout) m;
					Object con = b.getConstraints(toolbar);

					if (con == SOUTH || con == EAST || con == WEST) {
						return con;
					}

					return NORTH;
				}
			}
		}

		return NORTH;
	}

	/**
	 * Used by the renderers. For the most part the renderers are implemented as
	 * Labels, which is problematic in so far as they are never selected. To
	 * accomodate this OLabelUI checks if the current UI matches that of
	 * <code>selectedUI</code> (which this methods sets), if it does, then a
	 * state as set by this method is set in the field {@code selectedUIState}.
	 * This provides a way for labels to have a state other than selected.
	 * 
	 * @param uix
	 *            a UI delegate.
	 * @param selected
	 *            is the component selected?
	 * @param focused
	 *            is the component focused?
	 * @param enabled
	 *            is the component enabled?
	 * @param rollover
	 *            is the component's rollover state enabled?
	 */
	public static void setSelectedUI(ComponentUI uix, boolean selected, boolean focused, boolean enabled, boolean rollover) {
		selectedUI = uix;
		selectedUIState = 0;

		if (selected) {
			selectedUIState = SynthConstants.SELECTED;

			if (focused) {
				selectedUIState |= SynthConstants.FOCUSED;
			}
		} else if (rollover && enabled) {
			selectedUIState |= SynthConstants.MOUSE_OVER | SynthConstants.ENABLED;

			if (focused) {
				selectedUIState |= SynthConstants.FOCUSED;
			}
		} else {

			if (enabled) {
				selectedUIState |= SynthConstants.ENABLED;
				selectedUIState = SynthConstants.FOCUSED;
			} else {
				selectedUIState |= SynthConstants.DISABLED;
			}
		}
	}

	/**
	 * Clears out the selected UI that was last set in setSelectedUI.
	 */
	public static void resetSelectedUI() {
		selectedUI = null;
	}

	public Image getImage(String key) {
		String path = (String) super.getDefaults().get(key);

		if (path != null) {
			URL url = OntimizeLookAndFeel.class.getClassLoader().getResource(path);
			return new ImageIcon(url).getImage();
		}
		return null;
	}

	public static void clearReferences() {
//		SynthContext.clearReferences();
		OntimizeRegion.clearReferences();
		OLoweredBorder.clearReferences();
		ShapeFactory.clearReferences();
		try {
			Field map = javax.swing.plaf.synth.SynthContext.class.getDeclaredField("contextMap");
			map.setAccessible(true);
			Object value = map.get(null);
			Method m = Map.class.getDeclaredMethod("clear", null);
			m.invoke(value, null);
		} catch (Throwable t) {
		}
	}

	public static class PainterBorder implements Border, UIResource {
		protected Insets insets;
		protected Painter painter;
		protected String painterKey;

		PainterBorder(String painterKey, Insets insets) {
			this.insets = insets;
			this.painterKey = painterKey;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
			if (painter == null) {
				painter = (Painter) UIManager.get(painterKey);
				if (painter == null)
					return;
			}

			g.translate(x, y);
			if (g instanceof Graphics2D)
				painter.paint((Graphics2D) g, c, w, h);
			else {
				BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
				Graphics2D gfx = img.createGraphics();
				painter.paint(gfx, c, w, h);
				gfx.dispose();
				g.drawImage(img, x, y, null);
				img = null;
			}
			g.translate(-x, -y);
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return (Insets) insets.clone();
		}

		@Override
		public boolean isBorderOpaque() {
			return false;
		}
	}
	
	public static void setColorUIResource(UIDefaults defaults, String name, String key,String defaultValue){
		ColorUIResource color = StyleUtil.getColorUI(name, key, defaultValue);
		if (color!=null){
			if(key.startsWith("[") || key.startsWith(":")){
				defaults.put(name+key, color);
			}else{
				defaults.put(name+"."+key, color);
			}
		}
	}
	
	public static void setColor(UIDefaults defaults, String name, String key,String defaultValue){
		Color color = StyleUtil.getColor(name, key, defaultValue);
		if (color!=null){
			if(key.startsWith("[") || key.startsWith(":")){
				defaults.put(name+key, color);
			}else{
				defaults.put(name+"."+key, color);
			}
		}
	}
	
	public static void setPaint(UIDefaults defaults, String name, String key,String defaultValue){
		Paint paint = StyleUtil.getPaint(name, key, defaultValue);
		if (paint!=null){
			if(key.startsWith("[") || key.startsWith(":")){
				defaults.put(name+key, paint);
			}else{
				defaults.put(name+"."+key, paint);
			}
		}
	}
	
	public static void setBoolean(UIDefaults defaults, String name, String key,String defaultValue){
		Boolean boolValue = StyleUtil.getBoolean(name, key, defaultValue);
		if (boolValue!=null){
			if(key.startsWith("[") || key.startsWith(":")){
				defaults.put(name+key, boolValue);
			}else{
				defaults.put(name+"."+key, boolValue);
			}
		}
	}
	
	public static void setInteger(UIDefaults defaults, String name, String key,String defaultValue){
		Integer intValue = StyleUtil.getInteger(name, key, defaultValue);
		if (intValue!=null){
			if(key.startsWith("[") || key.startsWith(":")){
				defaults.put(name+key, intValue);
			}else{
				defaults.put(name+"."+key, intValue);
			}
		}
	}
	
	public static void setFloat(UIDefaults defaults, String name, String key,String defaultValue){
		Float floatValue = StyleUtil.getFloat(name, key, defaultValue);
		if (floatValue!=null){
			if(key.startsWith("[") || key.startsWith(":")){
				defaults.put(name+key, floatValue);
			}else{
				defaults.put(name+"."+key, floatValue);
			}
		}
	}
	
	public static void setDouble(UIDefaults defaults, String name, String key,String defaultValue){
		Double doubleValue = StyleUtil.getDouble(name, key, defaultValue);
		if (doubleValue!=null){
			if(key.startsWith("[") || key.startsWith(":")){
				defaults.put(name+key, doubleValue);
			}else{
				defaults.put(name+"."+key, doubleValue);
			}
		}
	}
	
	public static void setInsetsUIResource(UIDefaults defaults, String name, String key,String defaultValue){
		InsetsUIResource insets = StyleUtil.getInsetsUI(name, key, defaultValue);
		if (insets!=null){
			defaults.put(name+"."+key, insets);
		}
	}

	
	public static void setFontUIResource(UIDefaults defaults, String name, String key,String defaultValue){
		FontUIResource font = StyleUtil.getFontUIResource(name, key, defaultValue);
		if (font!=null){
			defaults.put(name+"."+key, font);
		}
	}
	
	public static void setFont(UIDefaults defaults, String name, String key,String defaultValue){
		Font font = StyleUtil.getFont(name, key, defaultValue);
		if (font!=null){
			defaults.put(name+"."+key, font);
		}
	}
	
	public static void setIcon(UIDefaults defaults, String name, String key,String defaultValue){
		Icon icon = StyleUtil.getIcon(name, key, defaultValue);
		if (icon!=null){
			defaults.put(name+"."+key, icon);
		}
	}
}
