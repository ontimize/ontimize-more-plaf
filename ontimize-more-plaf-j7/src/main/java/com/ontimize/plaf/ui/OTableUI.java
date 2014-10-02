package com.ontimize.plaf.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.OntimizeLookAndFeel.TableCellEditorBorder;
import com.ontimize.plaf.OntimizeStyle;
import com.ontimize.plaf.border.OntimizeBorder;
import com.ontimize.plaf.painter.ViewportPainter;
import com.ontimize.plaf.utils.ContextUtils;
import com.ontimize.plaf.utils.OntimizeLAFColorUtils;

public class OTableUI extends BasicTableUI implements SynthUI, PropertyChangeListener, ViewportPainter {
	//
	// Instance Variables
	//

	protected SynthStyle style;

	protected boolean useTableColors;
	protected boolean useUIBorder;
	protected Color alternateColor; // the background color to use for cells for alternate cells
	
	protected boolean useOddEvenAlternateRowColor;
	protected Color alternateOddColor; // the background color to use for odd cells
	protected Color alternateEvenColor; // the background color to use for even cells
	
	protected boolean showGrid;
	
	protected Color             transparentColor;

	// TableCellRenderer installed on the JTable at the time we're installed,
	// cached so that we can reinstall them at uninstallUI time.
	protected TableCellRenderer dateRenderer;
	protected TableCellRenderer numberRenderer;
	protected TableCellRenderer doubleRender;
	protected TableCellRenderer floatRenderer;
	protected TableCellRenderer iconRenderer;
	protected TableCellRenderer imageIconRenderer;
	protected TableCellRenderer booleanRenderer;
	protected TableCellRenderer objectRenderer;

	//
	// The installation/uninstall procedures and support
	//

	public static ComponentUI createUI(JComponent c) {
		return new OTableUI();
	}

	/**
	 * Initialize JTable properties, e.g. font, foreground, and background. The
	 * font, foreground, and background properties are only set if their current
	 * value is either null or a UIResource, other properties are set if the
	 * current value is null.
	 * 
	 * @see #installUI
	 */
	protected void installDefaults() {
		dateRenderer = installRendererIfPossible(Date.class, null);
		numberRenderer = installRendererIfPossible(Number.class, null);
		doubleRender = installRendererIfPossible(Double.class, null);
		floatRenderer = installRendererIfPossible(Float.class, null);
		iconRenderer = installRendererIfPossible(Icon.class, null);
		imageIconRenderer = installRendererIfPossible(ImageIcon.class, null);
		booleanRenderer = installRendererIfPossible(Boolean.class, new OBooleanTableCellRenderer());
		objectRenderer = installRendererIfPossible(Object.class, new OTableCellRenderer());
		if (this.style == null) {
			this.style = OntimizeStyle.NULL_STYLE;
		}
		updateStyle(table);
	}

	protected TableCellRenderer installRendererIfPossible(Class objectClass, TableCellRenderer renderer) {
		TableCellRenderer currentRenderer = table.getDefaultRenderer(objectClass);
		if (currentRenderer instanceof UIResource) {
			table.setDefaultRenderer(objectClass, renderer);
		}
		return currentRenderer;
	}

	protected void updateStyle(JTable c) {
		SynthContext context = getContext(c, ENABLED);
		SynthStyle oldStyle = style;
		style = OntimizeLookAndFeel.updateStyle(context, this);
		
		transparentColor = OntimizeLAFColorUtils.setAlpha(Color.white, 0.0);
		
		if (style != oldStyle) {
			table.remove(rendererPane);
            rendererPane = createCustomCellRendererPane();
            table.add(rendererPane);
	            
			ContextUtils.setComponentState(context, ENABLED | SELECTED);

			Color sbg = table.getSelectionBackground();
			if (sbg == null || sbg instanceof UIResource) {
				table.setSelectionBackground(style.getColor(context, ColorType.TEXT_BACKGROUND));
			}

			Color sfg = table.getSelectionForeground();
			if (sfg == null || sfg instanceof UIResource) {
				table.setSelectionForeground(style.getColor(context, ColorType.TEXT_FOREGROUND));
			}

			ContextUtils.setComponentState(context, ENABLED);

			Color gridColor = table.getGridColor();
			if (gridColor == null || gridColor instanceof UIResource) {
				gridColor = (Color) style.get(context, "Table.gridColor");
				if (gridColor == null) {
					gridColor = style.getColor(context, ColorType.FOREGROUND);
				}
				table.setGridColor(gridColor);
			}

			useTableColors = style.getBoolean(context, "Table.rendererUseTableColors", true);
			useUIBorder = style.getBoolean(context, "Table.rendererUseUIBorder", true);

			Object rowHeight = style.get(context, "Table.rowHeight");
			if (rowHeight != null) {
				LookAndFeel.installProperty(table, "rowHeight", rowHeight);
			}
			showGrid = style.getBoolean(context, "Table.showGrid", true);
			if (!showGrid) {
				table.setShowGrid(false);
			}
			Dimension d = table.getIntercellSpacing();
			// if (d == null || d instanceof UIResource) {
			if (d != null) {
				d = (Dimension) style.get(context, "Table.intercellSpacing");
			}
			alternateColor = (Color) style.get(context, "Table.alternateRowColor");
			if (d != null) {
				table.setIntercellSpacing(d);
			}
			
			useOddEvenAlternateRowColor = style.getBoolean(context, "Table.useOddEvenAlternateRowColor", true);
			if(useOddEvenAlternateRowColor){
				alternateOddColor = (Color) style.get(context, "Table.alternateOddRowColor");
				alternateEvenColor = (Color) style.get(context, "Table.alternateEvenRowColor");
			}
			// install the scrollpane border
	        Container parent = table.getParent();  // should be viewport
	        if (parent != null) {
	            parent = parent.getParent();  // should be the scrollpane
	            if (parent != null && parent instanceof JScrollPane) {
	                LookAndFeel.installBorder((JScrollPane)parent, "Table.scrollPaneBorder");
	            }
	        }

			if (oldStyle != null) {
				uninstallKeyboardActions();
				installKeyboardActions();
			}
		}
		
	}

	/**
	 * Attaches listeners to the JTable.
	 */
	protected void installListeners() {
		super.installListeners();
		table.addPropertyChangeListener(this);
	}

	protected void uninstallDefaults() {
		table.setDefaultRenderer(Date.class, dateRenderer);
		table.setDefaultRenderer(Number.class, numberRenderer);
		table.setDefaultRenderer(Double.class, doubleRender);
		table.setDefaultRenderer(Float.class, floatRenderer);
		table.setDefaultRenderer(Icon.class, iconRenderer);
		table.setDefaultRenderer(ImageIcon.class, imageIconRenderer);
		table.setDefaultRenderer(Boolean.class, booleanRenderer);
		table.setDefaultRenderer(Object.class, objectRenderer);

		if (table.getTransferHandler() instanceof UIResource) {
			table.setTransferHandler(null);
		}
		SynthContext context = getContext(table, ENABLED);
		style.uninstallDefaults(context);
		
		style = null;
		
		super.uninstallDefaults();
	}

	protected void uninstallListeners() {
		table.removePropertyChangeListener(this);
		super.uninstallListeners();
	}

	//
	// SynthUI
	//
	public SynthContext getContext(JComponent c) {
		return getContext(c, getComponentState(c));
	}

	protected SynthContext getContext(JComponent c, int state) {
		return new SynthContext( c, OntimizeLookAndFeel.getRegion(c), this.style,
				state);
	}

	protected Region getRegion(JComponent c) {
		return OntimizeLookAndFeel.getRegion(c);
	}

	protected int getComponentState(JComponent c) {
		return OntimizeLookAndFeel.getComponentState(c);
	}

	//
	// Paint methods and support
	//

	public void update(Graphics g, JComponent c) {
		SynthContext context = getContext(c);

		OntimizeLookAndFeel.update(context, g);
		ContextUtils.getPainter(context).paintTableBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
		paint(context, g);
		
	}

	public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
		ContextUtils.getPainter(context).paintTableBorder(context, g, x, y, w, h);
	}

	public void paint(Graphics g, JComponent c) {
		SynthContext context = getContext(c);

		paint(context, g);
		
	}

	protected void paint(SynthContext context, Graphics g) {
		Rectangle clip = g.getClipBounds();

		Rectangle bounds = table.getBounds();
		// account for the fact that the graphics has already been translated
		// into the table's bounds
		bounds.x = bounds.y = 0;

		if (table.getRowCount() <= 0 || table.getColumnCount() <= 0 ||
		// this check prevents us from painting the entire table
		// when the clip doesn't intersect our bounds at all
				!bounds.intersects(clip)) {

			paintDropLines(context, g);
			return;
		}

		boolean ltr = table.getComponentOrientation().isLeftToRight();

		Point upperLeft = clip.getLocation();
		if (!ltr) {
			upperLeft.x++;
		}

		Point lowerRight = new Point(clip.x + clip.width - (ltr ? 1 : 0), clip.y + clip.height);

		int rMin = table.rowAtPoint(upperLeft);
		int rMax = table.rowAtPoint(lowerRight);
		// This should never happen (as long as our bounds intersect the clip,
		// which is why we bail above if that is the case).
		if (rMin == -1) {
			rMin = 0;
		}
		// If the table does not have enough rows to fill the view we'll get -1.
		// (We could also get -1 if our bounds don't intersect the clip,
		// which is why we bail above if that is the case).
		// Replace this with the index of the last row.
		if (rMax == -1) {
			rMax = table.getRowCount() - 1;
		}

		int cMin = table.columnAtPoint(ltr ? upperLeft : lowerRight);
		int cMax = table.columnAtPoint(ltr ? lowerRight : upperLeft);
		// This should never happen.
		if (cMin == -1) {
			cMin = 0;
		}
		// If the table does not have enough columns to fill the view we'll get
		// -1.
		// Replace this with the index of the last column.
		if (cMax == -1) {
			cMax = table.getColumnCount() - 1;
		}
		
		 if (!(table.getParent() instanceof JViewport)
	                || (table.getParent() != null && !(table.getParent().getParent() instanceof JScrollPane))) {
			 paintViewportBackground(context, g, table, table.getWidth(), table.getHeight(), table.getLocation().y);
		 }

		// Paint the cells.
		paintCells(context, g, rMin, rMax, cMin, cMax);

		// Paint the grid.
		// it is important to paint the grid after the cells, otherwise the grid
		// will be overpainted
		// because in Synth cell renderers are likely to be opaque
		paintGrid(context, g, rMin, rMax, cMin, cMax);

		paintDropLines(context, g);
		
		
	}
	
    public void paintViewport(SynthContext context, Graphics g, JViewport c) {
        paintViewportBackground(context, g, c, c.getWidth(), c.getHeight(), table.getLocation().y);
    }
    
    public void paintViewportBackground(SynthContext context, Graphics g, JComponent c, int width, int height, int top) {
    	 // Fill the viewport with the background color of the table
        g.setColor(table.getBackground());
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
        
    }

	protected void paintDropLines(SynthContext context, Graphics g) {
		JTable.DropLocation loc = table.getDropLocation();
		if (loc == null) {
			return;
		}

		Color color = (Color) style.get(context, "Table.dropLineColor");
		Color shortColor = (Color) style.get(context, "Table.dropLineShortColor");
		if (color == null && shortColor == null) {
			return;
		}

		Rectangle rect;

		rect = getHDropLineRect(loc);
		if (rect != null) {
			int x = rect.x;
			int w = rect.width;
			if (color != null) {
				extendRect(rect, true);
				g.setColor(color);
				g.fillRect(rect.x, rect.y, rect.width, rect.height);
			}
			if (!loc.isInsertColumn() && shortColor != null) {
				g.setColor(shortColor);
				g.fillRect(x, rect.y, w, rect.height);
			}
		}

		rect = getVDropLineRect(loc);
		if (rect != null) {
			int y = rect.y;
			int h = rect.height;
			if (color != null) {
				extendRect(rect, false);
				g.setColor(color);
				g.fillRect(rect.x, rect.y, rect.width, rect.height);
			}
			if (!loc.isInsertRow() && shortColor != null) {
				g.setColor(shortColor);
				g.fillRect(rect.x, y, rect.width, h);
			}
		}
	}

	protected Rectangle getHDropLineRect(JTable.DropLocation loc) {
		if (!loc.isInsertRow()) {
			return null;
		}

		int row = loc.getRow();
		int col = loc.getColumn();
		if (col >= table.getColumnCount()) {
			col--;
		}

		Rectangle rect = table.getCellRect(row, col, true);

		if (row >= table.getRowCount()) {
			row--;
			Rectangle prevRect = table.getCellRect(row, col, true);
			rect.y = prevRect.y + prevRect.height;
		}

		if (rect.y == 0) {
			rect.y = -1;
		} else {
			rect.y -= 2;
		}

		rect.height = 3;

		return rect;
	}

	protected Rectangle getVDropLineRect(JTable.DropLocation loc) {
		if (!loc.isInsertColumn()) {
			return null;
		}

		boolean ltr = table.getComponentOrientation().isLeftToRight();
		int col = loc.getColumn();
		Rectangle rect = table.getCellRect(loc.getRow(), col, true);

		if (col >= table.getColumnCount()) {
			col--;
			rect = table.getCellRect(loc.getRow(), col, true);
			if (ltr) {
				rect.x = rect.x + rect.width;
			}
		} else if (!ltr) {
			rect.x = rect.x + rect.width;
		}

		if (rect.x == 0) {
			rect.x = -1;
		} else {
			rect.x -= 2;
		}

		rect.width = 3;

		return rect;
	}

	protected Rectangle extendRect(Rectangle rect, boolean horizontal) {
		if (rect == null) {
			return rect;
		}

		if (horizontal) {
			rect.x = 0;
			rect.width = table.getWidth();
		} else {
			rect.y = 0;

			if (table.getRowCount() != 0) {
				Rectangle lastRect = table.getCellRect(table.getRowCount() - 1, 0, true);
				rect.height = lastRect.y + lastRect.height;
			} else {
				rect.height = table.getHeight();
			}
		}

		return rect;
	}

	/*
	 * Paints the grid lines within <I>aRect</I>, using the grid color set with
	 * <I>setGridColor</I>. Paints vertical lines if
	 * <code>getShowVerticalLines()</code> returns true and paints horizontal
	 * lines if <code>getShowHorizontalLines()</code> returns true.
	 */
	protected void paintGrid(SynthContext context, Graphics g, int rMin, int rMax, int cMin, int cMax) {
		g.setColor(table.getGridColor());

		Rectangle minCell = table.getCellRect(rMin, cMin, true);
		Rectangle maxCell = table.getCellRect(rMax, cMax, true);
		Rectangle damagedArea = minCell.union(maxCell);
		SynthGraphicsUtils synthG = context.getStyle().getGraphicsUtils(context);

		if (table.getShowHorizontalLines()) {
			int tableWidth = damagedArea.x + damagedArea.width;
			int y = damagedArea.y;
			for (int row = rMin; row <= rMax; row++) {
				y += table.getRowHeight(row);
				synthG.drawLine(context, "Table.grid", g, damagedArea.x, y - 1, tableWidth - 1, y - 1);
			}
		}
		if (table.getShowVerticalLines()) {
			TableColumnModel cm = table.getColumnModel();
			int tableHeight = damagedArea.y + damagedArea.height;
			int x;
			if (table.getComponentOrientation().isLeftToRight()) {
				x = damagedArea.x;
				for (int column = cMin; column <= cMax; column++) {
					int w = cm.getColumn(column).getWidth();
					x += w;
					synthG.drawLine(context, "Table.grid", g, x - 1, 0, x - 1, tableHeight - 1);
				}
			} else {
				x = damagedArea.x;
				for (int column = cMax; column >= cMin; column--) {
					int w = cm.getColumn(column).getWidth();
					x += w;
					synthG.drawLine(context, "Table.grid", g, x - 1, 0, x - 1, tableHeight - 1);
				}
			}
		}
	}

	protected int viewIndexForColumn(TableColumn aColumn) {
		TableColumnModel cm = table.getColumnModel();
		for (int column = 0; column < cm.getColumnCount(); column++) {
			if (cm.getColumn(column) == aColumn) {
				return column;
			}
		}
		return -1;
	}

	protected void paintCells(SynthContext context, Graphics g, int rMin, int rMax, int cMin, int cMax) {
		JTableHeader header = table.getTableHeader();
		TableColumn draggedColumn = (header == null) ? null : header.getDraggedColumn();

		TableColumnModel cm = table.getColumnModel();
		int columnMargin = cm.getColumnMargin();

		Rectangle cellRect;
		TableColumn aColumn;
		int columnWidth;
		if (table.getComponentOrientation().isLeftToRight()) {
			for (int row = rMin; row <= rMax; row++) {
				cellRect = table.getCellRect(row, cMin, false);
				for (int column = cMin; column <= cMax; column++) {
					aColumn = cm.getColumn(column);
					columnWidth = aColumn.getWidth();
					cellRect.width = columnWidth - columnMargin;
					if (aColumn != draggedColumn) {
						paintCell(context, g, cellRect, row, column);
					}
					cellRect.x += columnWidth;
				}
			}
		} else {
			for (int row = rMin; row <= rMax; row++) {
				cellRect = table.getCellRect(row, cMin, false);
				aColumn = cm.getColumn(cMin);
				if (aColumn != draggedColumn) {
					columnWidth = aColumn.getWidth();
					cellRect.width = columnWidth - columnMargin;
					paintCell(context, g, cellRect, row, cMin);
				}
				for (int column = cMin + 1; column <= cMax; column++) {
					aColumn = cm.getColumn(column);
					columnWidth = aColumn.getWidth();
					cellRect.width = columnWidth - columnMargin;
					cellRect.x -= columnWidth;
					if (aColumn != draggedColumn) {
						paintCell(context, g, cellRect, row, column);
					}
				}
			}
		}

		// Paint the dragged column if we are dragging.
		if (draggedColumn != null) {
			paintDraggedArea(context, g, rMin, rMax, draggedColumn, header.getDraggedDistance());
		}

		// Remove any renderers that may be left in the rendererPane.
		rendererPane.removeAll();
	}

	protected void paintDraggedArea(SynthContext context, Graphics g, int rMin, int rMax,
			TableColumn draggedColumn, int distance) {
		int draggedColumnIndex = viewIndexForColumn(draggedColumn);

		Rectangle minCell = table.getCellRect(rMin, draggedColumnIndex, true);
		Rectangle maxCell = table.getCellRect(rMax, draggedColumnIndex, true);

		Rectangle vacatedColumnRect = minCell.union(maxCell);

		// Paint a gray well in place of the moving column.
		g.setColor(table.getParent().getBackground());
		g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width,
				vacatedColumnRect.height);

		// Move to the where the cell has been dragged.
		vacatedColumnRect.x += distance;

		// Fill the background.
		g.setColor(context.getStyle().getColor(context, ColorType.BACKGROUND));
		g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width,
				vacatedColumnRect.height);

		SynthGraphicsUtils synthG = context.getStyle().getGraphicsUtils(context);

		// Paint the vertical grid lines if necessary.
		if (table.getShowVerticalLines()) {
			g.setColor(table.getGridColor());
			int x1 = vacatedColumnRect.x;
			int y1 = vacatedColumnRect.y;
			int x2 = x1 + vacatedColumnRect.width - 1;
			int y2 = y1 + vacatedColumnRect.height - 1;
			// Left
			synthG.drawLine(context, "Table.grid", g, x1 - 1, y1, x1 - 1, y2);
			// Right
			synthG.drawLine(context, "Table.grid", g, x2, y1, x2, y2);
		}

		for (int row = rMin; row <= rMax; row++) {
			// Render the cell value
			Rectangle r = table.getCellRect(row, draggedColumnIndex, false);
			r.x += distance;
			paintCell(context, g, r, row, draggedColumnIndex);

			// Paint the (lower) horizontal grid line if necessary.
			if (table.getShowHorizontalLines()) {
				g.setColor(table.getGridColor());
				Rectangle rcr = table.getCellRect(row, draggedColumnIndex, true);
				rcr.x += distance;
				int x1 = rcr.x;
				int y1 = rcr.y;
				int x2 = x1 + rcr.width - 1;
				int y2 = y1 + rcr.height - 1;
				synthG.drawLine(context, "Table.grid", g, x1, y2, x2, y2);
			}
		}
	}

	protected void paintCell(SynthContext context, Graphics g, Rectangle cellRect, int row, int column) {
		if (table.isEditing() && table.getEditingRow() == row && table.getEditingColumn() == column) {
			Component component = table.getEditorComponent();
			
			if(component instanceof JComponent && useUIBorder){
				BorderUIResource border = (BorderUIResource)style.get(context, "Table.cellEditorBorder");
				if(border!=null){
					if(component instanceof JComboBox){
						if(border instanceof TableCellEditorBorder && ((TableCellEditorBorder)border).getDelegateBorder() instanceof CompoundBorder){
							Border b = ((CompoundBorder)((TableCellEditorBorder)border).getDelegateBorder()).getOutsideBorder();
							((JComponent)component).setBorder(b);
						}else{
							((JComponent)component).setBorder(BorderFactory.createEmptyBorder());
						}
					}else{
						((JComponent)component).setBorder(border);
					}
				}
				
			}
			
			cellRect.width = table.getShowVerticalLines() ? cellRect.width-1 : cellRect.width;
			cellRect.height = table.getShowHorizontalLines() ? cellRect.height -1 : cellRect.height;
			component.setBounds(cellRect);
			component.validate();
		} else {
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			Component component = table.prepareRenderer(renderer, row, column);
			Color b = component.getBackground();
			if ((b == null || b instanceof UIResource )
					&& !table.isCellSelected(row, column)) {
				if (alternateColor != null && row % 2 == 0) {
					component.setBackground(alternateColor);
				}
			}
			
			rendererPane.paintComponent(g, component, table, cellRect.x, cellRect.y, table.getShowVerticalLines() ? cellRect.width-1 : cellRect.width,
					table.getShowHorizontalLines() ? cellRect.height -1 : cellRect.height, true);
		}
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (OntimizeLookAndFeel.shouldUpdateStyle(event)) {
			updateStyle((JTable) event.getSource());
		}
	}
	
    protected Border getCellBorder(JComponent component) {
        return (Border) style.get(getContext(component), "Table.cellNoFocusBorder");
    }

    protected Border getSelectedRowBorder(JComponent component) {
       Border b = (Border) style.get(getContext(component), "Table.focusRowHighlightBorder");
    	return b!=null ? b : getCellBorder(component);
    }
    
    protected Border getSelectedColumnBorder(JComponent component) {
        return (Border) style.get(getContext(component), "Table.focusCellHighlightBorder");
    }

	
    /**
     * Creates a custom {@link CellRendererPane} that sets the renderer
     * component to be non-opaque if the associated row isn't selected. This
     * custom {@code CellRendererPane} is needed because a table UI delegate has
     * no prepare renderer like {@link JTable} has.
     *
     * @return DOCUMENT ME!
     */
    protected CellRendererPane createCustomCellRendererPane() {
        return new CellRendererPane() {
            @Override
            public void paintComponent(Graphics graphics, Component component, Container container, int x, int y, int w, int h,
                    boolean shouldValidate) {
                int rowAtPoint = table.rowAtPoint(new Point(x, y));
                int columnAtPoint = table.columnAtPoint(new Point(x,y));
                boolean isRowSelected = table.isRowSelected(rowAtPoint);
                boolean isColumnSelected = table.isColumnSelected(columnAtPoint);
               

                if (component instanceof JComponent && component instanceof UIResource) {
                    JComponent jComponent = (JComponent) component;

                    jComponent.setOpaque(true);
                    jComponent.setBorder(isRowSelected ? getSelectedRowBorder(jComponent) : getCellBorder(jComponent));
                    jComponent.setBackground(isRowSelected ? jComponent.getBackground() : transparentColor);
                    
					if (isColumnSelected && isRowSelected) {
						jComponent.setBorder(getSelectedColumnBorder(jComponent));
						jComponent.setBackground(isRowSelected ? jComponent.getBackground(): transparentColor);
					}

                    if (isRowSelected) {
                        jComponent.setForeground(unwrap(table.getSelectionForeground()));
                        jComponent.setBackground(unwrap(table.getSelectionBackground()));
                    } else {
                        jComponent.setForeground(unwrap(table.getForeground()));
                        
                        if(useOddEvenAlternateRowColor && alternateEvenColor!=null && alternateOddColor!=null){
                        	if (rowAtPoint % 2 == 0) {
	                        	jComponent.setBackground(unwrap(alternateOddColor));
	        				}else{
	        					jComponent.setBackground(unwrap(alternateEvenColor));
	        				}
                        }else{
	                        if (alternateColor != null && rowAtPoint % 2 == 0) {
	                        	jComponent.setBackground(unwrap(alternateColor));
	        				}else{
	        					jComponent.setBackground(transparentColor);
	        				}
                        }
                    }
                }

                super.paintComponent(graphics, component, container, x, y, w, h, shouldValidate);
            }

            protected Color unwrap(Color c) {
                if (c instanceof UIResource) {
                    return new Color(c.getRGB());
                }

                return c;
            }

            /**
             * @see javax.swing.JComponent#isOpaque()
             */
            @SuppressWarnings("unused")
            public boolean isOpaque(int x, int y) {
                int rowAtPoint = table.rowAtPoint(new Point(x, y));

                return table.isRowSelected(rowAtPoint) ? true : super.isOpaque();
            }
        };
    }

	protected class OBooleanTableCellRenderer extends JCheckBox implements TableCellRenderer, UIResource {
		protected boolean isRowSelected;

		public OBooleanTableCellRenderer() {
			setHorizontalAlignment(JLabel.CENTER);
			setName("Table.cellRenderer");
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			isRowSelected = isSelected;

			if (isSelected) {
				setForeground(unwrap(table.getSelectionForeground()));
				setBackground(unwrap(table.getSelectionBackground()));
			} else {
				setForeground(unwrap(table.getForeground()));
				setBackground(unwrap(table.getBackground()));
			}

			setSelected((value != null && ((Boolean) value).booleanValue()));
			return this;
		}

		protected Color unwrap(Color c) {
			if (c instanceof UIResource) {
				return new Color(c.getRGB());
			}
			return c;
		}

		public boolean isOpaque() {
			return isRowSelected ? true : super.isOpaque();
		}
	}

	protected class OTableCellRenderer extends DefaultTableCellRenderer implements UIResource {
		protected Object numberFormat;
		protected Object dateFormat;
		protected boolean opaque;

		public void setOpaque(boolean isOpaque) {
			opaque = isOpaque;
		}

		public boolean isOpaque() {
			return opaque;
		}

		public String getName() {
			String name = super.getName();
			if (name == null) {
				return "Table.cellRenderer";
			}
			return name;
		}

		public void setBorder(Border b) {
			if (useUIBorder || b instanceof OntimizeBorder) {
				super.setBorder(b);
			}
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			if (!useTableColors && (isSelected || hasFocus)) {
				OntimizeLookAndFeel.setSelectedUI(
						(OLabelUI) OntimizeLookAndFeel.getUIOfType(getUI(), OLabelUI.class), isSelected,
						hasFocus, table.isEnabled(), false);
			} else {
				OntimizeLookAndFeel.resetSelectedUI();
			}
			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (row % 2 == 0) {
				comp.setBackground(alternateColor);
				setBackground(alternateColor);
			} else {
				comp.setBackground(table.getBackground());
				setBackground(table.getBackground());
			}

			setIcon(null);
			Class columnClass = table.getColumnClass(column);
			configureValue(value, columnClass);

			return this;
		}

		protected void configureValue(Object value, Class columnClass) {
			if (columnClass == Object.class || columnClass == null) {
				setHorizontalAlignment(JLabel.LEADING);
			} else if (columnClass == Float.class || columnClass == Double.class) {
				if (numberFormat == null) {
					numberFormat = NumberFormat.getInstance();
				}
				setHorizontalAlignment(JLabel.TRAILING);
				setText((value == null) ? "" : ((NumberFormat) numberFormat).format(value));
			} else if (columnClass == Number.class) {
				setHorizontalAlignment(JLabel.TRAILING);
				// Super will have set value.
			} else if (columnClass == Icon.class || columnClass == ImageIcon.class) {
				setHorizontalAlignment(JLabel.CENTER);
				setIcon((value instanceof Icon) ? (Icon) value : null);
				setText("");
			} else if (columnClass == Date.class) {
				if (dateFormat == null) {
					dateFormat = DateFormat.getDateInstance();
				}
				setHorizontalAlignment(JLabel.LEADING);
				setText((value == null) ? "" : ((Format) dateFormat).format(value));
			} else {
				configureValue(value, columnClass.getSuperclass());
			}
		}

		public void paint(Graphics g) {
			super.paint(g);
			OntimizeLookAndFeel.resetSelectedUI();
		}
	}
}
