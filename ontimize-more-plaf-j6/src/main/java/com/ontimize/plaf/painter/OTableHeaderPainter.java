package com.ontimize.plaf.painter;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import com.ontimize.gui.table.Sortable;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;


/**
 * Painter for the table header arrows (to configure border and foreground colors, painters, images, ...)
 *
 * @author Imatia Innovation
 *
 */
public class OTableHeaderPainter extends AbstractRegionPainter {
	//package protected integers representing the available states that
	//this painter will paint. These are used when creating a new instance
	//of OTableHeaderPainter to determine which region/state is being painted
	//by that instance.
	public static final int ASCENDINGSORTICON_ENABLED = 1;
	public static final int DESCENDINGSORTICON_ENABLED = 2;
	
	public static Color defaultShadowColor = OntimizeLAFColorUtils.setAlpha(Color.black, 0.3);
	public static Color defaultArrowColor = Color.white;
	public static Font defaultNumberFont = new Font("Arial",Font.PLAIN,9);


	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;

	//the following 4 variables are reused during the painting code of the layers
	protected Path2D path = new Path2D.Float();
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
	protected RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);
	protected Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);
	
	protected Paint backgroundColorDescending;
	protected Paint backgroundColorAscending;

	//All Colors used for painting are stored here. Ideally, only those colors being used
	//by a particular instance of OTableHeaderPainter would be created. For the moment at least,
	//however, all are created for each instance.
	protected final Color color1 = new Color(0xe64718);
	protected final Color color2 = new Color(0x80b721);


	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;
	
	protected Font numberFont;

	public OTableHeaderPainter(int state, PaintContext ctx) {
		super();
		this.state = state;
		this.ctx = ctx;
		this.numberFont = new Font("Arial",Font.PLAIN,9);
		
		init();
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		//populate componentColors array with colors calculated in getExtendedCacheKeys call
		componentColors = extendedCacheKeys;
		//generate this entire method. Each state/bg/fg/border combo that has
		//been painted gets its own KEY and paint method.
		
		switch(state) {
		case ASCENDINGSORTICON_ENABLED: paintascendingSortIconEnabled(g, c); break;
		case DESCENDINGSORTICON_ENABLED: paintdescendingSortIconEnabled(g, c); break;

		}
	}

	protected void init (){

		// Ascending:
		Object obj = UIManager.getLookAndFeelDefaults().get("TableHeader.ascendingSortIconBackground");
		if(obj instanceof Paint){
			backgroundColorAscending = (Paint)obj;
		}else{
			backgroundColorAscending = color2;
		}

		// Descending:
		obj = UIManager.getLookAndFeelDefaults().get("TableHeader.descendingSortIconBackground");
		if(obj instanceof Paint){
			backgroundColorDescending = (Paint)obj;
		}else{
			backgroundColorDescending = color1;
		}
	}


	@Override
	protected PaintContext getPaintContext() {
		return ctx;
	}
	
	protected Font getNumberFont(){
		if(this.numberFont==null){
			this.numberFont = defaultNumberFont;
		}
		return this.numberFont;
	}
	
	protected int getSortIndex(JComponent c) {
		int sortIndex = -1;

		JTable table = getTable(c);
		if (table != null ) {
			int selectedColumn = table.getColumnModel().getColumnIndexAtX(c.getX());

			TableModel model = table.getModel();
			if (model instanceof Sortable) {
				Sortable sorter = (Sortable) model;

				for (int i = 0; i < sorter.getSortingColumns().length; i++) {
					int cInd = sorter.getSortingColumns()[i];
					if (selectedColumn == table.convertColumnIndexToView(cInd)) {
						sortIndex = i;
						break;
					}
				}
			}
		}
		return sortIndex;
	}
	
	protected JTable getTable(JComponent c){
		JTable table = null;
		try {
			Object obj = SwingUtilities.getAncestorOfClass(JTableHeader.class, c);
			if(obj instanceof JTableHeader){
				table = ((JTableHeader)obj).getTable();
			}
		} catch (Exception e) {
		}
		return table;
	}

	protected void paintascendingSortIconEnabled(Graphics2D g, JComponent c) {
		path = decodeBackgroundShape();
		g.setPaint( backgroundColorAscending);
		g.fill(path);
		
		ellipse = new Ellipse2D.Float(decodeX(1.5161290322f), decodeY(1.1176470588f), decodeX(1.3870967741f), decodeY(1.7058823529f));
		g.setPaint(defaultShadowColor);
		g.fill(ellipse);
		
		path = decodeUpArrowShape();
		g.setPaint(defaultArrowColor); 
		g.fill(path);
		
		Font oldFont = g.getFont();
		g.setFont(getNumberFont());
		int index = getSortIndex(c);
		g.drawString(""+(index+1), decodeX(1.6451612903f),decodeY(1.6470588235f));
		g.setFont(oldFont);

	}

	protected void paintdescendingSortIconEnabled(Graphics2D g, JComponent c) {
		path = decodeBackgroundShape();
		g.setPaint(backgroundColorDescending);
		g.fill(path);
		
		
		ellipse = new Ellipse2D.Float(decodeX(1.5161290322f), decodeY(1.1176470588f), decodeX(1.3870967741f), decodeY(1.7058823529f));
		g.setPaint(defaultShadowColor);
		g.fill(ellipse);
		
		path = decodeDownArrowShape();
		g.setPaint(defaultArrowColor); 
		g.fill(path);
		
		Font oldFont = g.getFont();
		g.setFont(getNumberFont());
		int index = getSortIndex(c);
		g.drawString(""+(index+1), decodeX(1.6451612903f),decodeY(1.6470588235f));
		g.setFont(oldFont);
		
	}


	protected Path2D decodeBackgroundShape(){
		
		path.reset();
		path.moveTo(decodeX(1.2580645161f), decodeY(1.0f));
		path.lineTo(decodeX(1.7419354838f), decodeY(1.0f));
		path.curveTo(decodeX(1.8709677419f), decodeY(1.0f), decodeX(2.0f), decodeY(1.2352941176f), decodeX(2.0f), decodeY(1.4705882352f));
		path.curveTo(decodeX(2.0f), decodeY(1.7058823529f), decodeX(1.8709677419f), decodeY(1.9411764705f), decodeX(1.7419354838f), decodeY(1.9411764705f));
		path.lineTo(decodeX(1.2580645161f), decodeY(1.9411764705f));
		path.curveTo(decodeX(1.1290322580f), decodeY(1.9411764705f), decodeX(1.0f), decodeY(1.7058823529f), decodeX(1.0f), decodeY(1.4705882352f));
		path.curveTo(decodeX(1.0f), decodeY(1.2352941176f), decodeX(1.1290322580f), decodeY(1.0f), decodeX(1.2580645161f), decodeY(1.0f));
		path.closePath();
		
		
		return path;
	}
	
	protected Path2D decodeUpArrowShape(){
		path.reset();
		
		path.moveTo(decodeX(1.1612903225f), decodeY(1.5882352941f));
		path.lineTo(decodeX(1.3870967741f), decodeY(1.5882352941f));
		path.lineTo(decodeX(1.2741935483f), decodeY(1.3529411764f));
		path.closePath();
		
		return path;
	}
	
	protected Path2D decodeDownArrowShape(){
		path.reset();
		
		path.moveTo(decodeX(1.1612903225f), decodeY(1.3529411764f));
		path.lineTo(decodeX(1.3870967741f), decodeY(1.3529411764f));
		path.lineTo(decodeX(1.2741935483f), decodeY(1.5882352941f));
		path.closePath();
		
		return path;
	}

}
