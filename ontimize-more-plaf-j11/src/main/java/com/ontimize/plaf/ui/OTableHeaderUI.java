package com.ontimize.plaf.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import sun.swing.DefaultLookup;

import com.ontimize.gui.table.Table;
import com.ontimize.plaf.OntimizeLookAndFeel;
import com.ontimize.plaf.border.OntimizeBorder;
import com.ontimize.plaf.utils.ContextUtils;

public class OTableHeaderUI extends BasicTableHeaderUI implements PropertyChangeListener, SynthUI {

    protected TableCellRenderer prevRenderer = null;

    protected SynthStyle style;

    /**
     * DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static ComponentUI createUI(JComponent h) {
        return new OTableHeaderUI();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableHeaderUI#installDefaults()
     */
    protected void installDefaults() {
        prevRenderer = header.getDefaultRenderer();
        if (prevRenderer instanceof UIResource) {
            header.setDefaultRenderer(new HeaderRenderer());
        }
        updateStyle(header);
    }

    /**
     * DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    protected void updateStyle(JTableHeader c) {
        SynthContext context = getContext(c, ENABLED);
        SynthStyle oldStyle = style;

        style = OntimizeLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }


    }

    /**
     * @see javax.swing.plaf.basic.BasicTableHeaderUI#installListeners()
     */
    protected void installListeners() {
        super.installListeners();
        header.addPropertyChangeListener(this);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableHeaderUI#uninstallDefaults()
     */
    protected void uninstallDefaults() {
        if (header.getDefaultRenderer() instanceof HeaderRenderer) {
            header.setDefaultRenderer(prevRenderer);
        }

        SynthContext context = getContext(header, ENABLED);

        style.uninstallDefaults(context);

        style = null;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableHeaderUI#uninstallListeners()
     */
    protected void uninstallListeners() {
        header.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    public void update(Graphics g, JComponent c) {
        SynthContext context = getContext(c);

        OntimizeLookAndFeel.update(context, g);
        ContextUtils.getPainter(context).paintTableHeaderBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);

    }

    /**
     * @see javax.swing.plaf.basic.BasicTableHeaderUI#paint(java.awt.Graphics, javax.swing.JComponent)
     */
    public void paint(Graphics g, JComponent c) {
        SynthContext context = getContext(c);

        paint(context, g);

    }

    /**
     * DOCUMENT ME!
     * @param context DOCUMENT ME!
     * @param g DOCUMENT ME!
     */
    protected void paint(SynthContext context, Graphics g) {
        super.paint(g, context.getComponent());
    }

    /**
     * @see sun.swing.plaf.synth.SynthUI#paintBorder(javax.swing.plaf.synth.SynthContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ContextUtils.getPainter(context).paintTableHeaderBorder(context, g, x, y, w, h);
    }

    //
    // SynthUI
    //
    /**
     * @see sun.swing.plaf.synth.SynthUI#getContext(javax.swing.JComponent)
     */
    public SynthContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    /**
     * DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param state DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    protected SynthContext getContext(JComponent c, int state) {
        if (this.style == null) {
            this.style = OntimizeLookAndFeel.getOntimizeStyle(c, OntimizeLookAndFeel.getRegion(c));
        }
        return new SynthContext(c, SynthLookAndFeel.getRegion(c), this.style, state);
    }

    /**
     * DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    protected int getComponentState(JComponent c) {
        return OntimizeLookAndFeel.getComponentState(c);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (OntimizeLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle((JTableHeader) evt.getSource());
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableHeaderUI#rolloverColumnUpdated(int, int)
     */
    @Override
    protected void rolloverColumnUpdated(int oldColumn, int newColumn) {
        header.repaint(header.getHeaderRect(oldColumn));
        header.repaint(header.getHeaderRect(newColumn));
    }


    // Size methods.
    @Override
    public Dimension getMinimumSize(JComponent c) {
        Dimension dim = super.getMinimumSize(c);
        if (dim != null && SwingUtilities.getAncestorOfClass(Table.class, c) != null) {
            dim.height = dim.height + c.getInsets().top + c.getInsets().bottom;
        }
        return dim;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension dim = super.getPreferredSize(c);
        if (dim != null && SwingUtilities.getAncestorOfClass(Table.class, c) != null) {
            dim.height = dim.height + c.getInsets().top + c.getInsets().bottom;
        }
        return dim;
    }


    public static class HeaderRenderer extends DefaultTableCellHeaderRenderer {

        protected static final long serialVersionUID = 3595483618538272322L;

        /**
         * Creates a new HeaderRenderer object.
         */
        public HeaderRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setName("TableHeader.renderer");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row,
                int column) {
            boolean hasRollover = false; // (column == getRolloverColumn());

            if (isSelected || hasRollover || hasFocus) {
                OntimizeLookAndFeel.setSelectedUI((OLabelUI) OntimizeLookAndFeel.getUIOfType(getUI(), OLabelUI.class),
                        isSelected, hasFocus, table.isEnabled(), hasRollover);
            } else {
                OntimizeLookAndFeel.resetSelectedUI();
            }

            // Stuff a variable into the client property of this renderer
            // indicating the sort order, so that different rendering can be
            // done for the header based on sorted state.
            RowSorter rs = table == null ? null : table.getRowSorter();
            java.util.List<? extends RowSorter.SortKey> sortKeys = rs == null ? null : rs.getSortKeys();

            if (sortKeys != null && sortKeys.size() > 0
                    && sortKeys.get(0).getColumn() == table.convertColumnIndexToModel(column)) {
                switch (sortKeys.get(0).getSortOrder()) {

                    case ASCENDING:
                        putClientProperty("Table.sortOrder", "ASCENDING");
                        break;

                    case DESCENDING:
                        putClientProperty("Table.sortOrder", "DESCENDING");
                        break;

                    case UNSORTED:
                        putClientProperty("Table.sortOrder", "UNSORTED");
                        break;

                    default:
                        throw new AssertionError("Cannot happen");
                }
            } else {
                putClientProperty("Table.sortOrder", "UNSORTED");
            }

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            return this;
        }

        /**
         * @see javax.swing.JComponent#setBorder(javax.swing.border.Border)
         */
        @Override
        public void setBorder(Border border) {
            if (border instanceof OntimizeBorder) {
                super.setBorder(border);
            }
        }

    }

    public static class DefaultTableCellHeaderRenderer extends DefaultTableCellRenderer implements UIResource {

        protected static final long serialVersionUID = -4466195868054511962L;

        protected Icon sortArrow;

        protected EmptyIcon emptyIcon = new EmptyIcon();

        /**
         * Creates a new DefaultTableCellHeaderRenderer object.
         */
        public DefaultTableCellHeaderRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        /**
         * @see javax.swing.JLabel#setHorizontalTextPosition(int)
         */
        public void setHorizontalTextPosition(int textPosition) {
            super.setHorizontalTextPosition(textPosition);
        }

        /**
         * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row,
                int column) {
            Icon sortIcon = null;

            boolean isPaintingForPrint = false;

            if (table != null) {
                JTableHeader header = table.getTableHeader();

                if (header != null) {
                    Color fgColor = null;
                    Color bgColor = null;

                    if (hasFocus) {
                        fgColor = DefaultLookup.getColor(this, ui, "TableHeader.focusCellForeground");
                        bgColor = DefaultLookup.getColor(this, ui, "TableHeader.focusCellBackground");
                    }

                    if (fgColor == null) {
                        fgColor = header.getForeground();
                    }

                    if (bgColor == null) {
                        bgColor = header.getBackground();
                    }

                    setForeground(fgColor);
                    setBackground(bgColor);

                    setFont(header.getFont());

                    isPaintingForPrint = header.isPaintingForPrint();
                }

                if (!isPaintingForPrint && table.getRowSorter() != null) {
                    SortOrder sortOrder = getColumnSortOrder(table, column);

                    if (sortOrder != null) {
                        switch (sortOrder) {

                            case ASCENDING:
                                sortIcon = DefaultLookup.getIcon(this, ui, "Table.ascendingSortIcon");
                                setFont(header.getFont().deriveFont(Font.BOLD));
                                break;

                            case DESCENDING:
                                sortIcon = DefaultLookup.getIcon(this, ui, "Table.descendingSortIcon");
                                setFont(header.getFont().deriveFont(Font.BOLD));
                                break;

                            case UNSORTED:
                                sortIcon = DefaultLookup.getIcon(this, ui, "Table.naturalSortIcon");
                                break;
                        }
                    }
                }
            }

            if (value instanceof Icon) {
                setText("");
                setIcon((Icon) value);
            } else if (value instanceof JLabel) {
                JLabel label = (JLabel) value;

                setText(label.getText());
                setIcon(label.getIcon());
                setHorizontalAlignment(label.getHorizontalAlignment());
                setHorizontalTextPosition(label.getHorizontalTextPosition());
            } else {
                setText(value == null ? "" : value.toString());
                setIcon(null);
            }

            sortArrow = sortIcon;

            Border border = null;

            if (hasFocus) {
                border = DefaultLookup.getBorder(this, ui, "TableHeader.focusCellBorder");
            }

            if (border == null) {
                border = DefaultLookup.getBorder(this, ui, "TableHeader.cellBorder");
            }

            setBorder(border);

            return this;
        }

        public static SortOrder getColumnSortOrder(JTable table, int column) {
            SortOrder rv = null;

            if (table == null || table.getRowSorter() == null) {
                return rv;
            }

            java.util.List<? extends RowSorter.SortKey> sortKeys = table.getRowSorter().getSortKeys();

            if (sortKeys.size() > 0 && sortKeys.get(0).getColumn() == table.convertColumnIndexToModel(column)) {
                rv = sortKeys.get(0).getSortOrder();
            }

            return rv;
        }

        /**
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        @Override
        public void paintComponent(Graphics g) {
            if (sortArrow != null) {
                // emptyIcon is used so that if the text in the header is right
                // aligned, or if the column is too narrow, then the text will
                // be sized appropriately to make room for the icon that is
                // about to be painted manually here.
                emptyIcon.width = sortArrow.getIconWidth();
                emptyIcon.height = sortArrow.getIconHeight();
                Point position = computeIconPosition(g);

                super.paintComponent(g);
                sortArrow.paintIcon(this, g, position.x, position.y);
            } else {
                super.paintComponent(g);
            }
        }

        protected Point computeIconPosition(Graphics g) {
            FontMetrics fontMetrics = g.getFontMetrics();
            Rectangle viewR = new Rectangle();
            Rectangle textR = new Rectangle();
            Rectangle iconR = new Rectangle();
            Insets i = getInsets();

            viewR.x = i.left;
            viewR.y = i.top;
            viewR.width = getWidth() - (i.left + i.right);
            viewR.height = getHeight() - (i.top + i.bottom);
            SwingUtilities.layoutCompoundLabel(this, fontMetrics, getText(), sortArrow, getVerticalAlignment(),
                    getHorizontalAlignment(),
                    getVerticalTextPosition(), getHorizontalTextPosition(), viewR, iconR, textR,
                    getIconTextGap());
            int x = getComponentOrientation().isLeftToRight() ? getWidth() - i.right - sortArrow.getIconWidth()
                    : i.left;
            int y = iconR.y;

            return new Point(x, y);
        }

        protected class EmptyIcon implements Icon, Serializable {

            protected static final long serialVersionUID = -821523476678771032L;

            int width = 0;

            int height = 0;

            /**
             * @see javax.swing.Icon#paintIcon(java.awt.Component,java.awt.Graphics, int, int)
             */
            public void paintIcon(Component c, Graphics g, int x, int y) {
            }

            /**
             * @see javax.swing.Icon#getIconWidth()
             */
            public int getIconWidth() {
                return width;
            }

            /**
             * @see javax.swing.Icon#getIconHeight()
             */
            public int getIconHeight() {
                return height;
            }

        }

    }

}
