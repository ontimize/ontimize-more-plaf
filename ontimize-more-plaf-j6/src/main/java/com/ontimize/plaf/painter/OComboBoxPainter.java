package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.utils.DerivedColor;

public class OComboBoxPainter extends AbstractRegionPainter {
    //package protected integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of ComboBoxPainter to determine which region/state is being painted
    //by that instance.
	public static final int BACKGROUND_DISABLED = 1;
	public static final int BACKGROUND_DISABLED_PRESSED = 2;
	public static final int BACKGROUND_ENABLED = 3;
	public static final int BACKGROUND_FOCUSED = 4;
	public static final int BACKGROUND_MOUSEOVER_FOCUSED = 5;
	public static final int BACKGROUND_MOUSEOVER = 6;
	public static final int BACKGROUND_PRESSED_FOCUSED = 7;
	public static final int BACKGROUND_PRESSED = 8;
    public static final int BACKGROUND_ENABLED_SELECTED = 9;
    public static final int BACKGROUND_DISABLED_EDITABLE = 10;
    public static final int BACKGROUND_ENABLED_EDITABLE = 11;
    public static final int BACKGROUND_FOCUSED_EDITABLE = 12;
    public static final int BACKGROUND_MOUSEOVER_EDITABLE = 13;
    public static final int BACKGROUND_PRESSED_EDITABLE = 14;


    protected int state; //refers to one of the static ints above
    protected PaintContext ctx;
    
    // painters to fill the component
	protected Paint backgroundColorDisabled;
	protected Paint backgroundColorEnabled;
	protected Paint backgroundColorFocused;
	protected Paint backgroundColorFocusedMouseOver;
	protected Paint backgroundColorMouseOver;
	protected Paint backgroundColorFocusedPressed;
	protected Paint backgroundColorPressed;
	
	protected Paint backgroundArrowButtonColorDisabled;
	protected Paint backgroundArrowButtonColorEnabled;
	protected Paint backgroundArrowButtonColorFocused;
	protected Paint backgroundArrowButtonColorFocusedMouseOver;
	protected Paint backgroundArrowButtonColorMouseOver;
	protected Paint backgroundArrowButtonColorFocusedPressed;
	protected Paint backgroundArrowButtonColorPressed;
	
	protected Paint backgroundArrowButtonColorEditableDisabled;
	protected Paint backgroundArrowButtonColorEditableEnabled;
	protected Paint backgroundArrowButtonColorEditableFocused;
	protected Paint backgroundArrowButtonColorEditableFocusedMouseOver;
	protected Paint backgroundArrowButtonColorEditableMouseOver;
	protected Paint backgroundArrowButtonColorEditableFocusedPressed;
	protected Paint backgroundArrowButtonColorEditablePressed;
	
	// arrays to round the component (several rounded borders with degradation):
	protected Paint[] degradatedBorderColorEnabled;
	protected Paint[] degradatedBorderColorDisabled;
	protected Paint[] degradatedBorderColorFocused;
	
	// painters to fill the editor
	protected Paint backgroundEditorColorDisabled;
	protected Paint backgroundEditorColorEnabled;
	protected Paint backgroundEditorColorSelected;

    //the following 4 variables are reused during the painting code of the layers
    protected Path2D path = new Path2D.Float();
    protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
    protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);
    protected Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    //All Colors used for painting are stored here. Ideally, only those colors being used
    //by a particular instance of ComboBoxPainter would be created. For the moment at least,
    //however, all are created for each instance.
    protected Color color1 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.74509805f, -247);
    protected Color color2 = decodeColor("nimbusBase", 0.032459438f, -0.5928571f, 0.2745098f, 0);
    protected Color color3 = decodeColor("nimbusBase", 0.032459438f, -0.590029f, 0.2235294f, 0);
    protected Color color4 = decodeColor("nimbusBase", 0.032459438f, -0.60996324f, 0.36470586f, 0);
    protected Color color5 = decodeColor("nimbusBase", 0.040395975f, -0.60474086f, 0.33725488f, 0);
    protected Color color6 = decodeColor("nimbusBase", 0.032459438f, -0.5953556f, 0.32549018f, 0);
    protected Color color7 = decodeColor("nimbusBase", 0.032459438f, -0.5957143f, 0.3333333f, 0);
    protected Color color8 = decodeColor("nimbusBase", 0.021348298f, -0.56289876f, 0.2588235f, 0);
    protected Color color9 = decodeColor("nimbusBase", 0.010237217f, -0.55799407f, 0.20784312f, 0);
    protected Color color10 = decodeColor("nimbusBase", 0.021348298f, -0.59223604f, 0.35294116f, 0);
    protected Color color11 = decodeColor("nimbusBase", 0.02391243f, -0.5774183f, 0.32549018f, 0);
    protected Color color12 = decodeColor("nimbusBase", 0.021348298f, -0.56722116f, 0.3098039f, 0);
    protected Color color13 = decodeColor("nimbusBase", 0.021348298f, -0.567841f, 0.31764704f, 0);
    protected Color color14 = decodeColor("nimbusBlueGrey", 0.0f, 0.0f, -0.22f, -176);
    protected Color color15 = decodeColor("nimbusBase", 0.032459438f, -0.5787523f, 0.07058823f, 0);
    protected Color color16 = decodeColor("nimbusBase", 0.032459438f, -0.5399696f, -0.18039218f, 0);
    protected Color color17 = decodeColor("nimbusBase", 0.08801502f, -0.63174605f, 0.43921566f, 0);
    protected Color color18 = decodeColor("nimbusBase", 0.040395975f, -0.6054113f, 0.35686272f, 0);
    protected Color color19 = decodeColor("nimbusBase", 0.032459438f, -0.5998577f, 0.4352941f, 0);
    protected Color color20 = decodeColor("nimbusBase", 5.1498413E-4f, -0.34585923f, -0.007843137f, 0);
    protected Color color21 = decodeColor("nimbusBase", 5.1498413E-4f, -0.095173776f, -0.25882354f, 0);
    protected Color color22 = decodeColor("nimbusBase", 0.004681647f, -0.6197143f, 0.43137252f, 0);
    protected Color color23 = decodeColor("nimbusBase", -0.0028941035f, -0.4800539f, 0.28235292f, 0);
    protected Color color24 = decodeColor("nimbusBase", 5.1498413E-4f, -0.43866998f, 0.24705881f, 0);
    protected Color color25 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4625541f, 0.35686272f, 0);
    protected Color color26 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    protected Color color27 = decodeColor("nimbusBase", 0.032459438f, -0.54616207f, -0.02352941f, 0);
    protected Color color28 = decodeColor("nimbusBase", 0.032459438f, -0.41349208f, -0.33725494f, 0);
    protected Color color29 = decodeColor("nimbusBase", 0.08801502f, -0.6317773f, 0.4470588f, 0);
    protected Color color30 = decodeColor("nimbusBase", 0.032459438f, -0.6113241f, 0.41568625f, 0);
    protected Color color31 = decodeColor("nimbusBase", 0.032459438f, -0.5985242f, 0.39999998f, 0);
    protected Color color32 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);
    protected Color color33 = decodeColor("nimbusBase", 0.0013483167f, -0.1769987f, -0.12156865f, 0);
    protected Color color34 = decodeColor("nimbusBase", 0.059279382f, 0.3642857f, -0.43529415f, 0);
    protected Color color35 = decodeColor("nimbusBase", 0.004681647f, -0.6198413f, 0.43921566f, 0);
    protected Color color36 = decodeColor("nimbusBase", -8.738637E-4f, -0.50527954f, 0.35294116f, 0);
    protected Color color37 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4555341f, 0.3215686f, 0);
    protected Color color38 = decodeColor("nimbusBase", 5.1498413E-4f, -0.4757143f, 0.43137252f, 0);
    protected Color color39 = decodeColor("nimbusBase", 0.08801502f, 0.3642857f, -0.52156866f, 0);
    protected Color color40 = decodeColor("nimbusBase", 0.032459438f, -0.5246032f, -0.12549022f, 0);
    protected Color color41 = decodeColor("nimbusBase", 0.027408898f, -0.5847884f, 0.2980392f, 0);
    protected Color color42 = decodeColor("nimbusBase", 0.026611507f, -0.53623784f, 0.19999999f, 0);
    protected Color color43 = decodeColor("nimbusBase", 0.029681683f, -0.52701867f, 0.17254901f, 0);
    protected Color color44 = decodeColor("nimbusBase", 0.03801495f, -0.5456242f, 0.3215686f, 0);
    protected Color color45 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    protected Color color46 = decodeColor("nimbusBase", -3.528595E-5f, 0.018606722f, -0.23137257f, 0);
    protected Color color47 = decodeColor("nimbusBase", -4.2033195E-4f, -0.38050595f, 0.20392156f, 0);
    protected Color color48 = decodeColor("nimbusBase", 4.081726E-4f, -0.12922078f, 0.054901958f, 0);
    protected Color color49 = decodeColor("nimbusBase", 0.0f, -0.00895375f, 0.007843137f, 0);
    protected Color color50 = decodeColor("nimbusBase", -0.0015907288f, -0.1436508f, 0.19215685f, 0);
    protected Color color51 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -83);
    protected Color color52 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -88);
    protected Color color53 = new Color(0x263945);


    //Array of current component colors, updated in each paint call
    protected Object[] componentColors;
    
    protected java.awt.Image paddLock = null;
    
    protected double radius;
    
    public static String imgUrl = "com/ontimize/plaf/images/paddlock.png";
    
	protected int numBorders;
	
	protected JComponent component;

	public OComboBoxPainter(int state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx = ctx;
        
        init();

		if (imgUrl != null) {
			URL url = getClass().getClassLoader().getResource(imgUrl);
			paddLock = Toolkit.getDefaultToolkit().getImage(url);
		}
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        //populate componentColors array with colors calculated in getExtendedCacheKeys call
        componentColors = extendedCacheKeys;
        setComponent(c);
        //generate this entire method. Each state/bg/fg/border combo that has
        //been painted gets its own KEY and paint method.
        
        switch(state) {
            case BACKGROUND_DISABLED: paintBackgroundDisabled(g); break;
            case BACKGROUND_DISABLED_PRESSED: paintBackgroundDisabledAndPressed(g); break;
            case BACKGROUND_ENABLED: paintBackgroundEnabled(g); break;
            case BACKGROUND_FOCUSED: paintBackgroundFocused(g); break;
            case BACKGROUND_MOUSEOVER_FOCUSED: paintBackgroundMouseOverAndFocused(g); break;
            case BACKGROUND_MOUSEOVER: paintBackgroundMouseOver(g); break;
            case BACKGROUND_PRESSED_FOCUSED: paintBackgroundPressedAndFocused(g); break;
            case BACKGROUND_PRESSED: paintBackgroundPressed(g); break;
            case BACKGROUND_ENABLED_SELECTED: paintBackgroundEnabledAndSelected(g); break;
            case BACKGROUND_DISABLED_EDITABLE: paintBackgroundDisabledAndEditable(g); break;
            case BACKGROUND_ENABLED_EDITABLE: paintBackgroundEnabledAndEditable(g); break;
            case BACKGROUND_FOCUSED_EDITABLE: paintBackgroundFocusedAndEditable(g); break;
            case BACKGROUND_MOUSEOVER_EDITABLE: paintBackgroundMouseOverAndEditable(g); break;
            case BACKGROUND_PRESSED_EDITABLE: paintBackgroundPressedAndEditable(g); break;
        }
    }
        
    protected Object[] getExtendedCacheKeys(JComponent c) {
        Object[] extendedCacheKeys = null;
        switch(state) {
            case BACKGROUND_ENABLED:
                extendedCacheKeys = new Object[] {
                     getComponentColor(c, "background", color17, -0.63174605f, 0.43921566f, 0),
                     getComponentColor(c, "background", color18, -0.6054113f, 0.35686272f, 0),
                     getComponentColor(c, "background", color6, -0.5953556f, 0.32549018f, 0),
                     getComponentColor(c, "background", color19, -0.5998577f, 0.4352941f, 0),
                     getComponentColor(c, "background", color22, -0.6197143f, 0.43137252f, 0),
                     getComponentColor(c, "background", color23, -0.4800539f, 0.28235292f, 0),
                     getComponentColor(c, "background", color24, -0.43866998f, 0.24705881f, 0),
                     getComponentColor(c, "background", color25, -0.4625541f, 0.35686272f, 0)};
                break;
            case BACKGROUND_FOCUSED:
                extendedCacheKeys = new Object[] {
                     getComponentColor(c, "background", color17, -0.63174605f, 0.43921566f, 0),
                     getComponentColor(c, "background", color18, -0.6054113f, 0.35686272f, 0),
                     getComponentColor(c, "background", color6, -0.5953556f, 0.32549018f, 0),
                     getComponentColor(c, "background", color19, -0.5998577f, 0.4352941f, 0),
                     getComponentColor(c, "background", color22, -0.6197143f, 0.43137252f, 0),
                     getComponentColor(c, "background", color23, -0.4800539f, 0.28235292f, 0),
                     getComponentColor(c, "background", color24, -0.43866998f, 0.24705881f, 0),
                     getComponentColor(c, "background", color25, -0.4625541f, 0.35686272f, 0)};
                break;
            case BACKGROUND_MOUSEOVER_FOCUSED:
                extendedCacheKeys = new Object[] {
                     getComponentColor(c, "background", color29, -0.6317773f, 0.4470588f, 0),
                     getComponentColor(c, "background", color30, -0.6113241f, 0.41568625f, 0),
                     getComponentColor(c, "background", color31, -0.5985242f, 0.39999998f, 0),
                     getComponentColor(c, "background", color32, -0.6357143f, 0.45098037f, 0),
                     getComponentColor(c, "background", color35, -0.6198413f, 0.43921566f, 0),
                     getComponentColor(c, "background", color36, -0.50527954f, 0.35294116f, 0),
                     getComponentColor(c, "background", color37, -0.4555341f, 0.3215686f, 0),
                     getComponentColor(c, "background", color25, -0.4625541f, 0.35686272f, 0),
                     getComponentColor(c, "background", color38, -0.4757143f, 0.43137252f, 0)};
                break;
            case BACKGROUND_MOUSEOVER:
                extendedCacheKeys = new Object[] {
                     getComponentColor(c, "background", color29, -0.6317773f, 0.4470588f, 0),
                     getComponentColor(c, "background", color30, -0.6113241f, 0.41568625f, 0),
                     getComponentColor(c, "background", color31, -0.5985242f, 0.39999998f, 0),
                     getComponentColor(c, "background", color32, -0.6357143f, 0.45098037f, 0),
                     getComponentColor(c, "background", color35, -0.6198413f, 0.43921566f, 0),
                     getComponentColor(c, "background", color36, -0.50527954f, 0.35294116f, 0),
                     getComponentColor(c, "background", color37, -0.4555341f, 0.3215686f, 0),
                     getComponentColor(c, "background", color25, -0.4625541f, 0.35686272f, 0),
                     getComponentColor(c, "background", color38, -0.4757143f, 0.43137252f, 0)};
                break;
            case BACKGROUND_PRESSED_FOCUSED:
                extendedCacheKeys = new Object[] {
                     getComponentColor(c, "background", color41, -0.5847884f, 0.2980392f, 0),
                     getComponentColor(c, "background", color42, -0.53623784f, 0.19999999f, 0),
                     getComponentColor(c, "background", color43, -0.52701867f, 0.17254901f, 0),
                     getComponentColor(c, "background", color44, -0.5456242f, 0.3215686f, 0),
                     getComponentColor(c, "background", color47, -0.38050595f, 0.20392156f, 0),
                     getComponentColor(c, "background", color48, -0.12922078f, 0.054901958f, 0),
                     getComponentColor(c, "background", color49, -0.00895375f, 0.007843137f, 0),
                     getComponentColor(c, "background", color50, -0.1436508f, 0.19215685f, 0)};
                break;
            case BACKGROUND_PRESSED:
                extendedCacheKeys = new Object[] {
                     getComponentColor(c, "background", color41, -0.5847884f, 0.2980392f, 0),
                     getComponentColor(c, "background", color42, -0.53623784f, 0.19999999f, 0),
                     getComponentColor(c, "background", color43, -0.52701867f, 0.17254901f, 0),
                     getComponentColor(c, "background", color44, -0.5456242f, 0.3215686f, 0),
                     getComponentColor(c, "background", color47, -0.38050595f, 0.20392156f, 0),
                     getComponentColor(c, "background", color48, -0.12922078f, 0.054901958f, 0),
                     getComponentColor(c, "background", color49, -0.00895375f, 0.007843137f, 0),
                     getComponentColor(c, "background", color50, -0.1436508f, 0.19215685f, 0)};
                break;
            case BACKGROUND_ENABLED_SELECTED:
                extendedCacheKeys = new Object[] {
                     getComponentColor(c, "background", color41, -0.5847884f, 0.2980392f, 0),
                     getComponentColor(c, "background", color42, -0.53623784f, 0.19999999f, 0),
                     getComponentColor(c, "background", color43, -0.52701867f, 0.17254901f, 0),
                     getComponentColor(c, "background", color44, -0.5456242f, 0.3215686f, 0),
                     getComponentColor(c, "background", color47, -0.38050595f, 0.20392156f, 0),
                     getComponentColor(c, "background", color48, -0.12922078f, 0.054901958f, 0),
                     getComponentColor(c, "background", color49, -0.00895375f, 0.007843137f, 0),
                     getComponentColor(c, "background", color50, -0.1436508f, 0.19215685f, 0)};
                break;
        }
        return extendedCacheKeys;
    }
    
	protected String getComponentKeyName() {
		return "ComboBox";
	}
    
    /**
	 * Get configuration properties to be used in this painter (such as: *BorderPainter,  *upperCornerPainter,  *lowerCornerPainter and *BgPainter).
	 * As usual, it is checked the OLAFCustomConfig.properties, and then in OLAFDefaultConfig.properties.
	 *
	 * Moreover, if there are not values for that properties in both, the default Nimbus configuration values are set, due to, those properties are needed to
	 * paint and used by the Ontimize L&F, so, there are not Nimbus values for that, so, default values are always configured based on Nimbus values.
	 *
	 */
	protected void init (){
		
		Object oRadius = UIManager.getLookAndFeelDefaults().get("Application.radius");
		if(oRadius instanceof Double){
			radius = ((Double)oRadius).doubleValue();
		}else{
			radius = OntimizeLookAndFeel.defaultRadius;
		}

		Object obj = UIManager.getDefaults().get("ComboBox.numBorders");
		if(obj instanceof Number){
			this.numBorders = ((Number)obj).intValue();
		}else{
			this.numBorders = OntimizeLookAndFeel.defaultNumBorders;
		}
			
		
		// disable:
		obj = UIManager.getLookAndFeelDefaults().get(getComponentKeyName() + "[Disabled].background");
		if(obj instanceof Paint){
			backgroundColorDisabled = (Paint)obj;
		}else{
			backgroundColorDisabled = color1;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Enabled].background");
		if(obj instanceof Paint){
			backgroundColorEnabled = (Paint)obj;
		}else{
			backgroundColorEnabled = color2;
		}

		// focused:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Focused].background");
		if(obj instanceof Paint){
			backgroundColorFocused = (Paint)obj;
		}else{
			backgroundColorFocused = color2;
		}
		
		// focused + mouseover:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Focused+MouseOver].background");
		if(obj instanceof Paint){
			backgroundColorFocusedMouseOver = (Paint)obj;
		}else{
			backgroundColorFocusedMouseOver = color2;
		}
		
		// mouseover:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[MouseOver].background");
		if(obj instanceof Paint){
			backgroundColorMouseOver = (Paint)obj;
		}else{
			backgroundColorMouseOver = color2;
		}

		// focused + pressed:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Focused+Pressed].background");
		if(obj instanceof Paint){
			backgroundColorFocusedPressed = (Paint)obj;
		}else{
			backgroundColorFocusedPressed = color2;
		}

		// pressed:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Pressed].background");
		if(obj instanceof Paint){
			backgroundColorPressed = (Paint)obj;
		}else{
			backgroundColorPressed = color2;
		}



		// BORDER COLORS FOR ROUNDED SHAPES (<=> ARCWIDTH>=0 AND ARCHEIGHT>=0):
		// enable:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Enabled].border");
		if(obj instanceof Paint){
			degradatedBorderColorEnabled = new Paint[]{(Paint)obj};
		}else if(obj instanceof Paint[]){
			degradatedBorderColorEnabled = (Paint[])obj;
		}else{
			degradatedBorderColorEnabled = new Color[] { color7, decodeColor(color7,color8,0.5f), color8};
		}
		
		// disable:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Disabled].border");
		if(obj instanceof Paint){
			degradatedBorderColorDisabled = new Paint[]{(Paint)obj};
		}else if(obj instanceof Paint[]){
			degradatedBorderColorDisabled = (Paint[])obj;
		}else{
			degradatedBorderColorDisabled = new Color[] { color3, decodeColor(color3,color4,0.5f), color4};
		}

		//Focused:
		obj = UIManager.getLookAndFeelDefaults().get( getComponentKeyName() + "[Focused].border");
		if(obj instanceof Paint){
			degradatedBorderColorFocused = new Paint[]{(Paint)obj};
		}else if(obj instanceof Paint[]){
			degradatedBorderColorFocused = (Paint[])obj;
		}else{
			degradatedBorderColorFocused = new Color[] { color7, decodeColor(color7,color8,0.5f), color8};
		}


		//EDITOR:
		// editor disable:
		obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.textField\"[Disabled].background");
		if(obj instanceof Paint){
			backgroundEditorColorDisabled = (Paint)obj;
		}else{
			backgroundEditorColorDisabled = color1;
		}

		// editor enable:
		obj = UIManager.getLookAndFeelDefaults().get( "ComboBox:\"ComboBox.textField\"[Enabled].background");
		if(obj instanceof Paint){
			backgroundEditorColorEnabled = (Paint)obj;
		}else{
			backgroundEditorColorEnabled = color2;
		}

		// editor selected:
		obj = UIManager.getLookAndFeelDefaults().get( "ComboBox:\"ComboBox.textField\"[Selected].background");
		if(obj instanceof Paint){
			backgroundEditorColorSelected = (Paint)obj;
		}else{
			backgroundEditorColorSelected = color2;
		}
		
		//Arrow Button.
		// disable:
		obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Disabled].background");
		if(obj instanceof Paint){
			backgroundArrowButtonColorDisabled = (Paint)obj;
		}else{
			backgroundArrowButtonColorDisabled = color53;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get( "ComboBox:\"ComboBox.arrowButton\"[Enabled].background");
		if(obj instanceof Paint){
			backgroundArrowButtonColorEnabled = (Paint)obj;
		}else{
			backgroundArrowButtonColorEnabled = color53;
		}
		
		// mouseover:
		obj = UIManager.getLookAndFeelDefaults().get( "ComboBox:\"ComboBox.arrowButton\"[MouseOver].background");
		if(obj instanceof Paint){
			backgroundArrowButtonColorMouseOver = (Paint)obj;
		}else{
			backgroundArrowButtonColorMouseOver = color53;
		}

		// pressed:
		obj = UIManager.getLookAndFeelDefaults().get( "ComboBox:\"ComboBox.arrowButton\"[Pressed].background");
		if(obj instanceof Paint){
			backgroundArrowButtonColorPressed = (Paint)obj;
		}else{
			backgroundArrowButtonColorPressed = color53;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ComboBox:\"ComboBox.arrowButton\"[Focused].background");
		if(obj instanceof Paint){
			backgroundArrowButtonColorFocused = (Paint)obj;
		}else{
			backgroundArrowButtonColorFocused = color53;
		}
		
		//Editable
		// disable:
		obj = UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.arrowButton\"[Disabled+Editable].background");
		if(obj instanceof Paint){
			backgroundArrowButtonColorEditableDisabled = (Paint)obj;
		}else{
			backgroundArrowButtonColorEditableDisabled = color53;
		}

		// enable:
		obj = UIManager.getLookAndFeelDefaults().get( "ComboBox:\"ComboBox.arrowButton\"[Editable+Enabled].background");
		if(obj instanceof Paint){
			backgroundArrowButtonColorEditableEnabled = (Paint)obj;
		}else{
			backgroundArrowButtonColorEditableEnabled = color53;
		}

		// mouseover:
		obj = UIManager.getLookAndFeelDefaults().get( "ComboBox:\"ComboBox.arrowButton\"[Editable+MouseOver].background");
		if(obj instanceof Paint){
			backgroundArrowButtonColorEditableMouseOver = (Paint)obj;
		}else{
			backgroundArrowButtonColorEditableMouseOver = color53;
		}

		// pressed:
		obj = UIManager.getLookAndFeelDefaults().get( "ComboBox:\"ComboBox.arrowButton\"[Editable+Pressed].background");
		if(obj instanceof Paint){
			backgroundArrowButtonColorEditablePressed = (Paint)obj;
		}else{
			backgroundArrowButtonColorEditablePressed = color53;
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "ComboBox:\"ComboBox.arrowButton\"[Editable+Focused].background");
		if(obj instanceof Paint){
			backgroundArrowButtonColorEditableFocused = (Paint)obj;
		}else{
			backgroundArrowButtonColorEditableFocused = color53;
		}
		
	}
	
	public JComponent getComponent() {
		return component;
	}

	public void setComponent(JComponent component) {
		this.component = component;
	}
	
	public boolean isTableEditor(){
		JComponent c = getComponent();
    	if(SwingUtilities.getAncestorOfClass(JTable.class, c) != null){
    		return true;
    	}
    	return false;
	}

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    protected void paintBackgroundDisabled(Graphics2D g) {
        
    	if(isTableEditor()){
    		drawBackgroundTableEditor(g, getBackgroundColor(getComponent(), backgroundColorDisabled));
		} else {
	       drawBackground(g, getBackgroundColor(getComponent(), backgroundColorDisabled));
	       drawDegradatedBorders(g, degradatedBorderColorDisabled);
	       g.drawImage(paddLock, 1, (int) (decodeY(2.0f)-10), 10, 10, null);
		}

    }

    protected void paintBackgroundDisabledAndPressed(Graphics2D g) {
    	
    	if(isTableEditor()){
    		drawBackgroundTableEditor(g, getBackgroundColor(getComponent(), backgroundColorDisabled));
		} else {
	    	drawBackground(g, getBackgroundColor(getComponent(), backgroundColorDisabled));
	    	drawDegradatedBorders(g, degradatedBorderColorDisabled);
	        g.drawImage(paddLock, 1, (int) (decodeY(2.0f)-10), 10, 10, null);
		}
    }

    protected void paintBackgroundEnabled(Graphics2D g) {
        
    	if(isTableEditor()){
    		drawBackgroundTableEditor(g, getBackgroundColor(getComponent(), backgroundColorEnabled));
		} else {
	        drawBackground(g, getBackgroundColor(getComponent(), backgroundColorEnabled));
	        drawDegradatedBorders(g, degradatedBorderColorEnabled);
		}
    }

    protected void paintBackgroundFocused(Graphics2D g) {
    	
    	if(isTableEditor()){
    		drawBackgroundTableEditor(g, getBackgroundColor(getComponent(), backgroundColorFocused));
		} else {
			drawBackground(g, getBackgroundColor(getComponent(), backgroundColorFocused));
			drawDegradatedBorders(g, degradatedBorderColorFocused);
		}
    	
    }

    protected void paintBackgroundMouseOverAndFocused(Graphics2D g) {
    	
    	if(isTableEditor()){
    		drawBackgroundTableEditor(g, getBackgroundColor(getComponent(), backgroundColorFocusedMouseOver));
		} else {
	    	drawBackground(g, getBackgroundColor(getComponent(), backgroundColorFocusedMouseOver));
	    	drawDegradatedBorders(g, degradatedBorderColorFocused);
		}
    	
    }

    protected void paintBackgroundMouseOver(Graphics2D g) {
    	
    	if(isTableEditor()){
    		drawBackgroundTableEditor(g, getBackgroundColor(getComponent(), backgroundColorMouseOver));
		} else {
	    	drawBackground(g, getBackgroundColor(getComponent(), backgroundColorMouseOver));
	    	drawDegradatedBorders(g, degradatedBorderColorEnabled);
		}
    	
    }

    protected void paintBackgroundPressedAndFocused(Graphics2D g) {
    	
    	if(isTableEditor()){
    		drawBackgroundTableEditor(g, getBackgroundColor(getComponent(), backgroundColorFocusedPressed));
		} else {
	    	drawBackground(g, getBackgroundColor(getComponent(), backgroundColorFocusedPressed));
	    	drawDegradatedBorders(g, degradatedBorderColorFocused);
		}

    }

    protected void paintBackgroundPressed(Graphics2D g) {
    	
    	if(isTableEditor()){
    		drawBackgroundTableEditor(g, getBackgroundColor(getComponent(), backgroundColorPressed));
		} else {
	    	drawBackground(g, getBackgroundColor(getComponent(), backgroundColorPressed));
	    	drawDegradatedBorders(g, degradatedBorderColorEnabled);
		}

    }

    protected void paintBackgroundEnabledAndSelected(Graphics2D g) {
    
    	if(isTableEditor()){
    		drawBackgroundTableEditor(g, getBackgroundColor(getComponent(), backgroundColorEnabled));
		} else {
    	 drawBackground(g, getBackgroundColor(getComponent(), backgroundColorEnabled));
    	 drawDegradatedBorders(g, degradatedBorderColorEnabled);
		}

    }

    protected void paintBackgroundDisabledAndEditable(Graphics2D g) {
        
        drawBackgroundEditable(g, getBackgroundColor(getComponent(), backgroundEditorColorDisabled));
        drawDegradatedBorders(g, degradatedBorderColorDisabled);
        g.drawImage(paddLock, 1, (int) (decodeY(2.0f)-10), 10, 10, null);

    }

    protected void paintBackgroundEnabledAndEditable(Graphics2D g) {

    	drawBackgroundEditable(g, getBackgroundColor(getComponent(), backgroundEditorColorEnabled));
    	drawDegradatedBorders(g, degradatedBorderColorEnabled);

    }

    protected void paintBackgroundFocusedAndEditable(Graphics2D g) {
    	
    	if(isTableEditor()){
    		drawBackgroundTableEditor(g, getBackgroundColor(getComponent(), backgroundColorEnabled));
		} else {
			drawBackgroundEditable(g, getBackgroundColor(getComponent(), backgroundEditorColorEnabled));
	    	drawDegradatedBorders(g, degradatedBorderColorFocused);
		}
    	//TODO At the moment editor textfield does not change to background selected state. When it changes, it is
    	//necessary to change the color to backgroundEditorColorSelected.

    }

    protected void paintBackgroundMouseOverAndEditable(Graphics2D g) {
    	
    	drawBackground(g, getBackgroundColor(getComponent(), backgroundColorMouseOver));
    	drawDegradatedBorders(g, degradatedBorderColorEnabled);

    }

    protected void paintBackgroundPressedAndEditable(Graphics2D g) {

    	drawBackground(g, getBackgroundColor(getComponent(), backgroundColorPressed));
    	drawDegradatedBorders(g, degradatedBorderColorEnabled);

    }
    
    
    protected Paint getBackgroundColor(JComponent c, Paint defaultColor){
		if(c!=null){
			if(c.getBackground()!= null && c.isEnabled()){
				return c.getBackground();
			}
		}
		return defaultColor;
	}
    
    protected com.ontimize.plaf.utils.DerivedColor getDerivedColor(Color parent,
            float hOffset, float sOffset,
            float bOffset, int aOffset) {
    	DerivedColor color = new DerivedColor.UIResource(parent, hOffset, sOffset, bOffset, aOffset);
    	color.rederiveColor();
    	return color;
    }

    protected Paint decodeGradientButton(Shape s, Color bgBaseColor) {
    	
    	Color derived = getDerivedColor(bgBaseColor, 0.0036656857f, -0.271856f, 0.21568626f, 0);
    	
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float h = (float)bounds.getHeight();
        return decodeGradient( x, y, x, y+h,
                new float[] { 0.0f,0.495f,0.505f,1.0f },
                new Color[] { bgBaseColor,
        					  derived,
        					  derived,
        					  bgBaseColor});
    }

    
    protected void drawBackground(Graphics2D g, Paint color){
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		int x_arc = intValue(decodeX(0.0f) + getMaximumRadius() + numBorders);
		if(x_arc > intValue(decodeX(1.0f))){
			x_arc = intValue(decodeX(1.0f));
		}
		int y = intValue(decodeY(0.0f)+numBorders);
		int w = intValue(decodeX(2.0f)-decodeX(1.0f));
		int h = intValue(decodeY(3.0f)- 2*numBorders);
        
        JButton button = getArrowButton();
    	if(button!=null){
    		Rectangle bounds = button.getBounds();
    		w = bounds.x - x_arc; 
    	}

    	//The rectangle of the component...
    	Shape s = ShapeFactory.getInstace().createRectangle(x_arc, y, w, h);
		g.setPaint( color );
		g.fill(s);
		
		//The left rounded filler
		s = decodeLeftBorderFillerPath(h);
		g.fill(s);
		
		//The button, just the background...
		drawButton(g);
		
		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
	}
    
    protected Shape decodeLeftBorderFillerPath(int h){
		
		double x = decodeX(0.0f) + getMaximumRadius() + numBorders;
		double radius = getRadius();
		double x_arc = decodeX(0.0f)+numBorders + radius;
		if(x_arc>x){
			x_arc = x;
			radius = getMaximumRadius();
		}
		double y = decodeY(0.0f)+numBorders;
		
		path.reset();
		path.moveTo(x, y);
		path.lineTo(x_arc, y);
		path.curveTo(x_arc - radius/2.0, y, x_arc -radius, y + radius/ 2.0, x_arc -radius, y+ radius);
		path.lineTo(x_arc-radius, y + h-radius);
		path.curveTo(x_arc - radius,y+ h- radius/2.0, x_arc-radius/2.0, y+h, x_arc, y+h);
		path.lineTo(x, y+h);
		path.closePath();
		
		return path;
	}
    
    protected void drawBackgroundTableEditor(Graphics2D g, Paint color){
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		int x = intValue(decodeX(0.0f));
		int y = intValue(decodeY(0.0f));
		int w = intValue(decodeX(2.0f)-decodeX(1.0f));
		int h = intValue(decodeY(3.0f)- 1);
        
        JButton button = getArrowButton();
    	if(button!=null){
    		Rectangle bounds = button.getBounds();
    		w = bounds.x;
    	}

    	Shape s = ShapeFactory.getInstace().createRectangle(x, y, w, h);
		g.setPaint( color );
		g.fill(s);
		
		drawButtonTableEditor(g);
		
		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
	}
    
    protected void drawBackgroundEditable(Graphics2D g, Paint color){
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
        int x_arc = intValue(decodeX(0.0f) + getMaximumRadius() + numBorders);
        if(x_arc > intValue(decodeX(1.0f))){
			x_arc = intValue(decodeX(1.0f));
		}
        int y = intValue(decodeY(0.0f)+numBorders);
        int h = intValue(decodeY(3.0f)- 2*numBorders);
        
		Shape s = ShapeFactory.getInstace().createSemiCircle(x_arc, y, h, ShapeFactory.ANTICLOCKWISE, false);
		g.setPaint( color );
		g.fill(s);
		
		drawButton(g);
		
		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
	}
    
    protected void drawButton(Graphics2D g){
    	
    	JButton button = getArrowButton();
    	if(button!=null){
    		Rectangle bounds = button.getBounds();
    		int xBut = bounds.x;
    		int yBut = intValue(decodeY(0.0f)+numBorders);
    		int wBut = bounds.width - intValue(bounds.width*0.4);
    		int hBut = intValue(decodeY(3.0f)- 2*numBorders);
    		
        	Shape s = ShapeFactory.getInstace().createRectangle(xBut, yBut, wBut, hBut);
        	g.setPaint(decodeGradientButton(s, getArrowButtonBackgroundColor()));
    		g.fill(s);
    		
    		double x = xBut + wBut;
    		double h = decodeY(3.0f) - 2*numBorders;
    		s = decodeRightBorderFillerPath(x,h);
    		g.fill(s);
    		
    	}
    	
    }
    
    protected Shape decodeRightBorderFillerPath(double x, double h){
		
		double radius = getRadius();
		double x_arc = decodeX(3.0f)-numBorders-radius;
		if(x_arc<x){
			x_arc = x;
			radius = getMaximumRadius();
		}
		double y = decodeY(0.0f)+numBorders;
		
		path.reset();
		path.moveTo(x, y);
		path.lineTo(x_arc, y);
		path.curveTo(x_arc+radius/2.0,y, x_arc+radius, y+radius/2.0, x_arc+radius, y+radius);
		path.lineTo(x_arc+radius, y+h-radius);
		path.curveTo(x_arc+radius,y+h-radius/2.0, x_arc+radius/2.0, y+h, x_arc, y+h);
		path.lineTo(x, y+h);
		path.closePath();
		
		return path;
	}
    
    protected void drawButtonTableEditor(Graphics2D g){
    	
    	JButton button = getArrowButton();
    	if(button!=null){
    		Rectangle bounds = button.getBounds();
    		int xBut = bounds.x;
    		int yBut = intValue(decodeY(0.0f));
    		int wBut = bounds.width ;
    		int hBut = intValue(decodeY(3.0f)- 1);
    		
        	Shape s = ShapeFactory.getInstace().createRectangle(xBut, yBut, wBut, hBut);
        	g.setPaint(decodeGradientButton(s, getArrowButtonBackgroundColor()));
    		g.fill(s);
    	}
    	
    }
    
    protected JButton getArrowButton(){
    	JComponent c = getComponent();
    	for (int i=0;i<c.getComponentCount();i++){
    		if( c.getComponent(i) instanceof JButton){
    			return (JButton)c.getComponent(i);
        	}
    	}
    	return null;
    }
    
    protected Color getArrowButtonBackgroundColor(){
    	Color c = null;
    	 switch(state) {
    	 case BACKGROUND_DISABLED_PRESSED: c = (Color) backgroundArrowButtonColorDisabled; break;
         case BACKGROUND_DISABLED: c = (Color) backgroundArrowButtonColorDisabled; break;
         case BACKGROUND_ENABLED:  c =(Color) backgroundArrowButtonColorEnabled; break;
         case BACKGROUND_MOUSEOVER:  c = (Color) backgroundArrowButtonColorMouseOver; break;
         case BACKGROUND_MOUSEOVER_FOCUSED:  c = (Color) backgroundArrowButtonColorMouseOver; break;
         case BACKGROUND_PRESSED:  c = (Color) backgroundArrowButtonColorPressed; break;
         case BACKGROUND_PRESSED_FOCUSED: c = (Color) backgroundArrowButtonColorPressed; break;
         case BACKGROUND_FOCUSED: c = (Color) backgroundArrowButtonColorFocused; break;
         
         case BACKGROUND_DISABLED_EDITABLE:  c = (Color) backgroundArrowButtonColorEditableDisabled; break;
         case BACKGROUND_ENABLED_EDITABLE:  c = (Color) backgroundArrowButtonColorEditableEnabled; break;
         case BACKGROUND_ENABLED_SELECTED: c = (Color) backgroundArrowButtonColorEditableEnabled; break;
         case BACKGROUND_MOUSEOVER_EDITABLE:  c = (Color) backgroundArrowButtonColorEditableMouseOver; break;
         case BACKGROUND_PRESSED_EDITABLE:  c = (Color) backgroundArrowButtonColorEditablePressed; break;
         case BACKGROUND_FOCUSED_EDITABLE: c = (Color) backgroundArrowButtonColorEditableFocused; break;
     }
    	 
    	 return c;
    }
    
    protected void drawDegradatedBorders(Graphics2D g, Paint[] colors){
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		
		int xx = intValue(decodeX(0.0f)+numBorders + getMaximumRadius());
		if(xx > intValue(decodeX(1.0f))){
			xx = intValue(decodeX(1.0f));
		}
		int yy = intValue(decodeY(0.0f)+numBorders);
		int w = intValue(decodeX(3.0f) - xx-1);
		int h = intValue(decodeY(3.0f) - 2*numBorders);
		
		JButton button = getArrowButton();
    	if(button!=null){
    		Rectangle bounds = button.getBounds();
    		w = bounds.x + bounds.width - intValue(bounds.width*0.4)-1; 
    	}
		
		for (int i = 1; i <= colors.length ; i++) {
			int yyy = yy - i;
			int hh = h + 2*i-1;
			Shape s = decodeBorderPath(xx, yyy, w, hh, i-1);
			g.setPaint(colors[i-1]);
			g.draw(s);
		}

		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
		
	}
    
    protected Shape decodeBorderPath(double x, double y, double w, double h, int borderIndex){
    	//w is just the width without curved zones.
		double radius = getRadius();
		double x_arc = decodeX(0.0f)+ numBorders + radius;
		if(x_arc>x){
			x_arc = x;
			radius = getMaximumRadius();
		}
		
		path.reset();
		path.moveTo(x, y);
		path.lineTo(x_arc, y);
		path.curveTo(x_arc - radius/2.0, y, x_arc -radius, y + radius/ 2.0, x_arc -radius-borderIndex, y+ radius);
		path.lineTo(x_arc-radius-borderIndex, y + h-radius);
		path.curveTo(x_arc - radius -borderIndex,y+ h- radius/2.0, x_arc-radius/2.0, y+h, x_arc, y+h);
		path.lineTo(x, y+h);
		
		path.lineTo(w, y+h);
		
		x_arc = decodeX(3.0f)-1-numBorders-radius;
		if(x_arc<w){
			x_arc = w;
			radius = getMaximumRadius();
		}
		
		path.lineTo(x_arc, y+h);
		path.curveTo(x_arc+radius/2.0,y+h, x_arc+radius, y+h-radius/2.0, x_arc+radius+borderIndex, y+h-radius);
		path.lineTo(x_arc+radius+borderIndex, y+radius);
		path.curveTo(x_arc+radius+borderIndex,y+radius/2.0, x_arc+radius/2.0, y, x_arc, y);
		
		path.lineTo(x, y);
		path.closePath();

		return path;
    }
    
    protected double getRadius(){
    	return radius;
    }
    
    protected double getMaximumRadius(){
		return  (decodeY(3.0f)-1 - 2*numBorders)/2.0;
	}

}
