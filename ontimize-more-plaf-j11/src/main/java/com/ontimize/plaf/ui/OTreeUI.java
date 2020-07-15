package com.ontimize.plaf.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.ontimize.gui.tree.BasicTreeCellRenderer;

public class OTreeUI extends BasicTreeUI {

    protected int leadRow;

    protected boolean lineTypeCurved;

    protected Color lineColor, lineSelectionColor;

    public static ComponentUI createUI(JComponent x) {
        return new OTreeUI();
    }

    public OTreeUI() {
    }

    @Override
    protected void setSelectionModel(TreeSelectionModel newLSM) {
        super.setSelectionModel(newLSM);
        if (this.treeSelectionModel != null) {
            this.treeSelectionModel.addTreeSelectionListener(new TreeSelectionListener() {

                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    if (OTreeUI.this.tree != null) {
                        OTreeUI.this.tree.repaint();
                    }
                }
            });
        }
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        this.lineTypeCurved = UIManager.getBoolean("Tree.lineTypeCurved");
        this.lineColor = UIManager.getColor("Tree.lineColor");
        this.lineSelectionColor = UIManager.getColor("Tree.lineSelectionColor");

    }

    @Override
    protected TreeCellRenderer createDefaultCellRenderer() {
        return new BasicTreeCellRenderer();
    }

    protected TreePath getLeadSelectionPath() {
        return this.tree.getLeadSelectionPath();
    }

    protected void updateLeadRow() {
        this.leadRow = this.getRowForPath(this.tree, this.getLeadSelectionPath());
    }

    @Override
    protected int getLeadSelectionRow() {
        return this.leadRow;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        this.updateLeadRow();
        super.paint(g, c);

        // Paint selected lines
        Rectangle paintBounds = g.getClipBounds();
        Insets insets = this.tree.getInsets();

        TreePath initialPath = this.tree.getSelectionPath();// getClosestPathForLocation(tree,
        // 0,
        // paintBounds.y);

        if (initialPath != null) {
            TreePath parentPath = initialPath;
            // parentPath = parentPath.getParentPath();
            while (parentPath != null) {
                this.paintVerticalSelectedPart(g, paintBounds, insets, parentPath);
                this.drawingCache.put(parentPath, Boolean.TRUE);
                parentPath = parentPath.getParentPath();
            }
        }
    }

    protected int getRowForY(int y) {
        TreePath treePath = this.getClosestPathForLocation(this.tree, 0, y);
        return this.getRowForPath(this.tree, treePath);
    }

    protected boolean isLastTreePath(int x, int y) {
        /*
         * Old code y = y - 6; x = x + this.getRightChildIndent();
         *
         * TreePath treePath = this.getClosestPathForLocation(this.tree, x, y); TreeNode treeNode =
         * (TreeNode) treePath.getLastPathComponent();
         *
         * if (treeNode.getParent() == null) { return false; } TreeNode last =
         * treeNode.getParent().getChildAt(treeNode.getParent().getChildCount() - 1); TreePath lastPath =
         * treePath.getParentPath().pathByAddingChild(last);
         *
         * Rectangle r = this.getPathBounds(this.tree, lastPath);
         *
         * if (r.contains(x, y)) { return true; }
         *
         * return false;
         */

        y = y - 6;
        x = x + this.getRightChildIndent();
        TreePath treePath = this.getClosestPathForLocation(this.tree, x, y);
        if (treePath.getParentPath() == null) {
            return false;
        }
        Object last = this.treeModel.getChild(treePath.getParentPath().getLastPathComponent(),
                this.treeModel.getChildCount(treePath.getParentPath().getLastPathComponent()) - 1);
        TreePath lastPath = treePath.getParentPath().pathByAddingChild(last);
        Rectangle r = this.getPathBounds(this.tree, lastPath);
        if (r.contains(x, y)) {
            return true;
        }
        return false;

    }

    protected boolean isSelectedPath(TreePath currentPath) {
        if (this.tree != null) {
            TreePath[] paths = this.tree.getSelectionPaths();
            if ((paths != null) && (paths.length > 0) && (currentPath != null)) {
                for (int i = 0; i < paths.length; i++) {
                    TreePath path = paths[i];
                    if (currentPath.isDescendant(path)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void paintHorizontalPartOfLeg(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds,
            TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded,
            boolean isLeaf) {

        Color oldHashColor = this.getHashColor();

        boolean seleted = false;
        if (this.isSelectedPath(path)) {
            seleted = true;
            this.setHashColor(this.lineSelectionColor);
        } else {
            this.setHashColor(this.lineColor);
        }

        super.paintHorizontalPartOfLeg(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);

        if ((path.getParentPath() != null) && seleted) {
            // paintVerticalSelectedPart(g, clipBounds, insets, path);
        }

        this.setHashColor(oldHashColor);
    }

    protected void paintVerticalSelectedPart(Graphics g, Rectangle clipBounds, Insets insets, TreePath selectedPath) {
        TreePath path = selectedPath.getParentPath();

        if (path == null) {
            return;
        }
        int depth = path.getPathCount() - 1;
        if ((depth == 0) && !this.getShowsRootHandles() && !this.isRootVisible()) {
            return;
        }
        int lineX = this.getRowX(-1, depth + 1);
        if (this.tree.getComponentOrientation().isLeftToRight()) {
            lineX = (lineX - this.getRightChildIndent()) + insets.left;
        } else {
            lineX = ((this.tree.getWidth() - lineX - insets.right) + this.getRightChildIndent()) - 1;
        }

        int clipLeft = clipBounds.x;
        int clipRight = clipBounds.x + (clipBounds.width - 1);

        if ((lineX >= clipLeft) && (lineX <= clipRight)) {
            int clipTop = clipBounds.y;
            int clipBottom = clipBounds.y + clipBounds.height;
            Rectangle parentBounds = this.getPathBounds(this.tree, path);

            Rectangle lastChildBounds = this.getPathBounds(this.tree, selectedPath);
            // getLastChildPath(selectedPath));

            if (lastChildBounds == null) {
                // This shouldn't happen, but if the model is modified
                // in another thread it is possible for this to happen.
                // Swing isn't multithreaded, but I'll add this check in
                // anyway.
                return;
            }

            int top;

            if (parentBounds == null) {
                top = Math.max(insets.top + this.getVerticalLegBuffer(), clipTop);
            } else {
                top = Math.max(parentBounds.y + parentBounds.height + this.getVerticalLegBuffer(), clipTop);
            }
            if ((depth == 0) && !this.isRootVisible()) {
                TreeModel model = this.getModel();

                if (model != null) {
                    Object root = model.getRoot();

                    if (model.getChildCount(root) > 0) {
                        parentBounds = this.getPathBounds(this.tree, path.pathByAddingChild(model.getChild(root, 0)));
                        if (parentBounds != null) {
                            top = Math.max(insets.top + this.getVerticalLegBuffer(),
                                    parentBounds.y + (parentBounds.height / 2));
                        }
                    }
                }
            }

            int bottom = Math.min(lastChildBounds.y + 2, clipBottom);

            if (top <= bottom) {
                this.paintVerticalLine(g, this.tree, lineX, top, bottom, true);
            }
        }
    }

    @Override
    protected void paintVerticalLine(Graphics g, JComponent c, int x, int top, int bottom) {
        this.paintVerticalLine(g, c, x, top, bottom, false);
    }

    /**
     * Paints a vertical line.
     */
    protected void paintVerticalLine(Graphics g, JComponent c, int x, int top, int bottom, boolean selected) {
        boolean end = false;

        if (this.lineTypeCurved) {
            Color old = g.getColor();

            if (this.isLastTreePath(x, bottom)) {
                end = true;
            }

            if (selected) {
                g.setColor(this.lineSelectionColor);
            } else {
                g.setColor(this.lineColor);
            }
            if (end) {
                g.fillRect(x - 6, top, 2, bottom - top - 6);
            } else {
                g.fillRect(x - 6, top, 2, bottom - top);
            }
            g.setColor(old);
        } else {
            super.paintVerticalLine(g, c, x, top, bottom);
        }
    }

    /**
     * Paints a horizontal line.
     */
    @Override
    protected void paintHorizontalLine(Graphics g, JComponent c, int y, int left, int right) {
        if (this.lineTypeCurved) {
            // Color old = g.getColor();
            Paint previousPaint = ((Graphics2D) g).getPaint();
            RenderingHints rh = ((Graphics2D) g).getRenderingHints();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // int currentRow = getRowForY(y);
            // int currentRow = -1;
            // if(lastSelectedRow>=currentRow){
            // g.setColor(lineSelectionColor);
            // }else{
            // g.setColor(lineColor);
            // }
            g.fillRect(left, y, right - left, 2);

            Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);

            path.moveTo(left, y);
            path.curveTo(left - 3, y, left - 6, y - 4, left - 5, y - 9);
            path.moveTo(left - 6, y - 9);
            path.curveTo(left - 6, y - 3, left - 3, y + 1, left, y + 1);
            ((Graphics2D) g).draw(path);

            // g.setColor(old);
            ((Graphics2D) g).setPaint(previousPaint);
            ((Graphics2D) g).setRenderingHints(rh);
        } else {
            super.paintHorizontalLine(g, c, y, left, right);
        }
    }

    /**
     * Paints the renderer part of a row. The receiver should NOT modify <code>clipBounds</code>, or
     * <code>insets</code>.
     */
    @Override
    protected void paintRow(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row,
            boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
        // Don't paint the renderer if editing this row.
        if ((this.editingComponent != null) && (this.editingRow == row)) {
            return;
        }

        int leadIndex;

        if (this.tree.hasFocus()) {
            leadIndex = this.getLeadSelectionRow();
        } else {
            leadIndex = -1;
        }

        Component component;

        component = this.currentCellRenderer.getTreeCellRendererComponent(this.tree, path.getLastPathComponent(),
                this.tree.isRowSelected(row), isExpanded, isLeaf, row,
                (leadIndex == row));

        try {
            /*
             * Due to DefaultTreeCellRenderer constructor (the component class) modification into Java7, it is
             * necessary to call updateUI to reinitialize the value of 'Tree.rendererFillBackground' property to
             * make that background label was not painted.
             */
            // if(UIManager.getLookAndFeel() instanceof
            // javax.swing.plaf.nimbus.NimbusLookAndFeel) {
            String s = System.getProperty("java.version");
            if ((s != null) && s.startsWith("1.7")) {
                ((JComponent) component).updateUI();
            }
        } catch (Throwable e) {
        }
        this.rendererPane.paintComponent(g, component, this.tree, bounds.x, bounds.y, this.tree.getWidth() - bounds.x,
                bounds.height, true);
    }

    @Override
    public void uninstallUI(JComponent c) {
        if (this.tree != null) {
            this.tree.setCellRenderer(null);
        }
        if (this.tree != null) {
            this.tree.setCellEditor(null);
        }
        super.uninstallUI(c);

    }

}
