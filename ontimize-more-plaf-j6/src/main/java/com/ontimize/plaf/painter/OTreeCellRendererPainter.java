package com.ontimize.plaf.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;

import com.ontimize.gui.tree.BasicTreeCellRenderer;
import com.ontimize.plaf.painter.util.ShapeFactory;
import com.ontimize.plaf.utils.OntimizeLAFParseUtils;


public class OTreeCellRendererPainter extends AbstractRegionPainter {

	public static final int BACKGROUND_ENABLED = 1;

	protected int state; //refers to one of the static ints above
	protected PaintContext ctx;


	//the following 4 variables are reused during the painting code of the layers
	protected Path2D path = new Path2D.Float();
	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);

	protected  Paint background;
	protected  Paint backgroundSelectionParent;
	 
	protected  Paint backgroundSelection;
	protected  Paint topBackgroundSelection;
	protected  Paint bottomBackgroundSelection;
	


	//Array of current component colors, updated in each paint call
	protected Object[] componentColors;

	public OTreeCellRendererPainter(int state, PaintContext ctx) {
		super();
		this.state = state;
		this.ctx = ctx;
		init();
	}



	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		//populate componentColors array with colors calculated in getExtendedCacheKeys call
		componentColors = extendedCacheKeys;
		//generate this entire method. Each state/bg/fg/border combo that has
		//been painted gets its own KEY and paint method.
		switch(state) {
		case BACKGROUND_ENABLED: paintBackgroundEnabled(g, c, width, height);break;
		}
	}



	@Override
	protected PaintContext getPaintContext() {
		return ctx;
	}

	protected void init (){

		Object obj = UIManager.getLookAndFeelDefaults().get( "Tree:\"Tree.cellRenderer\".background");
		if(obj instanceof Paint){
			background = (Paint)obj;
		}else{
			background = OntimizeLAFParseUtils.parseColor("#E4EDF0", Color.white);
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "Tree:\"Tree.cellRenderer\".backgroundSelectionParent");
		if(obj instanceof Paint){
			backgroundSelectionParent = (Paint)obj;
		}else{
			backgroundSelectionParent = OntimizeLAFParseUtils.parseColor("#ABC7D8",Color.white);
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "Tree:\"Tree.cellRenderer\".backgroundSelection");
		if(obj instanceof Paint){
			backgroundSelection = (Paint)obj;
		}else{
			backgroundSelection = OntimizeLAFParseUtils.parseColor("#517286", Color.blue);
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "Tree:\"Tree.cellRenderer\".topBackgroundSelection");
		if(obj instanceof Paint){
			topBackgroundSelection = (Paint)obj;
		}else{
			topBackgroundSelection = OntimizeLAFParseUtils.parseColor("#638092", Color.blue);
		}
		
		obj = UIManager.getLookAndFeelDefaults().get( "Tree:\"Tree.cellRenderer\".bottomBackgroundSelection");
		if(obj instanceof Paint){
			bottomBackgroundSelection = (Paint)obj;
		}else{
			bottomBackgroundSelection = OntimizeLAFParseUtils.parseColor("#496678", Color.blue);
		}

	}
	
	protected void paintBackgroundEnabled(Graphics2D g, JComponent c, int width , int height){
		Paint previousPaint = g.getPaint();
		RenderingHints rh = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color ant = g.getColor();
		g.setPaint(background);

		boolean selected = false;
		boolean leaf = true;
		int childCount = -1;
		
		boolean isRoot = false;
		
		if (c instanceof BasicTreeCellRenderer){
			BasicTreeCellRenderer basic = (BasicTreeCellRenderer)c;
			
			if (basic.isParentSelected()){
				g.setPaint(backgroundSelectionParent);
			}
			
			if (basic.isSelected()){
				 g.setColor(basic.getBackgroundSelectionColor());
				 selected = true;
			}
			
			if (!basic.isLeaf()){
				leaf = false; 
			}
			
			childCount = basic.getChildCount();
			
			isRoot = basic.isRoot();
		}

		if (!isRoot){
			paintBackground(g, c, width, height, selected, leaf, childCount);
		}
		
		g.setColor(ant);
		g.setPaint(previousPaint);
		g.setRenderingHints(rh);
	}
	
	 protected int getLabelStart(JLabel label) {
		 Icon currentI = label.getIcon();
		 if( currentI != null ) {
			 return currentI.getIconWidth()+ 2;
		 }
		 return 0;
	 }
	
	 protected void paintBackground(Graphics2D g, JComponent c, int width , int height, boolean selected, boolean leaf, int childCount){
		 int offset = height/2 - 1;
		 int labelstart = getLabelStart((JLabel)c);
		 if (width < labelstart +offset + offset){
			 return;
		 }
		 //left
		 Shape s = ShapeFactory.getInstace().createSemiCircle(getLabelStart((JLabel)c)+ offset, 1, height-2, ShapeFactory.ANTICLOCKWISE, false);
		 ((Graphics2D)g).fill(s);

		 //center
		 ((Graphics2D)g).fillRect(getLabelStart((JLabel)c)+ offset, 1, width-((offset*2)+getLabelStart((JLabel)c))-1,height-2);

		 //right
		 s = ShapeFactory.getInstace().createSemiCircle(getLabelStart((JLabel)c)+ offset+(width-((offset*2)+getLabelStart((JLabel)c)))-1, 1, 
				 										height-2, ShapeFactory.CLOCKWISE, false);
		 ((Graphics2D)g).fill(s);

		 if (selected){
			 //TOP
			 int x1 = getLabelStart((JLabel)c) + 3;
			 int y1 = (height/2) - 2;
			 int h = (height/2) - 4;
			 int w = width- 7 - getLabelStart((JLabel)c);

			 path.reset();
			 path.moveTo(x1 , y1);
			 path.curveTo(x1 , y1-h/2.0, x1+h/2.0, y1-h , x1+h, y1- h);

			 path.lineTo(x1+w-h, y1- h);

			 path.curveTo(x1+w-h/2.0,y1- h, x1+w, y1-h/2.0, x1+w, y1);
			 path.closePath();

			 g.setPaint(topBackgroundSelection);
			 g.fill(path);

			 path.reset();

			 g.setPaint(bottomBackgroundSelection);

			 //BOTTOM
			 x1 = getLabelStart((JLabel)c) + 3;
			 y1 = (height/2) +3;
			 h = (height/2) - 4;

			 path.moveTo(x1 , y1);
			 path.curveTo(x1 , y1+h/2.0, x1+h/2.0, y1+h , x1+h, y1+ h);

			 path.lineTo(x1+w-h, y1+ h);
			 path.curveTo(x1+w-h/2.0,y1+ h, x1+w, y1+h/2.0, x1+w, y1);
			 path.closePath();
			 g.fill(path);
		 }
		 
	 }

}