package de.ceruti.curcuma.appkit.widgets.swing;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.table.NSTableView;
import de.ceruti.curcuma.appkit.view.cells.WidgetPlugin;

class NSCellRenderer implements TableCellRenderer {
	private static Category logger = Logger
			.getInstance(NSCellRenderer.class);
	
	private final NSTableView view;
	public NSCellRenderer(NSTableView view) {
		this.view = view;
	}
	
	public NSTableView getView(){
		return view;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		NSCell cell = getView().preparedCell(value, isSelected, hasFocus, row, column);

		value = cell.getCellValue();
		
		Class<? extends Object> valueClass = value==null?Object.class : value.getClass();
		
		WidgetPlugin plugIn = (WidgetPlugin)cell.getImplementation();
		
		if (plugIn == null || plugIn.isDummy())
		{
			try {
				// throw new IllegalStateException("missing WidgetPlugIn");
				logger.warn("missing WidgetPlugIn");
				WidgetPlugin newPlug = PlugInFactory.get()
						.createPlugInForComponent(
								getDefaultJTableRenderComponent(table, value,
										isSelected, hasFocus, row, column,
										valueClass));
				plugIn.setWidget(newPlug.getWidget());
				cell.updateDisplayValue();
				return (Component) newPlug.getWidget();
			} finally {
				plugIn.setWidget(null);
			}
		}
		
		//let native Cell-Renderer win:
		if(plugIn.getNativeCellRenderer()!=null && plugIn.getNativeCellRenderer() instanceof TableCellRenderer)
		{
			return ((TableCellRenderer) plugIn.getNativeCellRenderer())
					.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);
		}
		
		Component renderComponent = (Component) plugIn.getWidget();
		if(renderComponent==null){
			try {
				Component defaultRenderComponent = getDefaultJTableRenderComponent(table, value,
						isSelected, hasFocus, row, column, valueClass);
				plugIn.setWidget(defaultRenderComponent);
				cell.updateDisplayValue();
				return defaultRenderComponent;
			} finally {
				plugIn.setWidget(null);
			}
		}
		else if (renderComponent instanceof TableCellRenderer) {
			return ((TableCellRenderer) renderComponent)
					.getTableCellRendererComponent(table,
							plugIn.getPlugInValue(), isSelected, hasFocus, row,
							column);
		}
		
		drawComponent(renderComponent, table, plugIn.getPlugInValue(),
					isSelected, hasFocus, row, column);
		return renderComponent;
	}

	private Component getDefaultJTableRenderComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Class<? extends Object> valueClass) {
		return table.getDefaultRenderer(valueClass)
				.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);
	}

	private Component getDefaultRenderComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column,
			NSCell cell) {
		TableCellRenderer defaultRenderer = table.getDefaultRenderer(value==null?Object.class : value.getClass());
		return defaultRenderer.getTableCellRendererComponent(table, cell.getCellValue(), isSelected, hasFocus, row, column);
	}
	
	private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	
	private void drawComponent(Component c, JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		if (isSelected) {
			c.setBackground(table.getSelectionBackground());
			c.setForeground(table.getSelectionForeground());
		} else {
			c.setBackground(table.getBackground());
			c.setForeground(table.getForeground());
		}
		if (c instanceof JComponent) {
			Border b = null;
			if (hasFocus) {
				if (isSelected)
					b = UIManager
							.getBorder("Table.focusSelectedCellHighlightBorder");
				if (b == null)
					b = UIManager
							.getBorder("Table.focusCellHighlightBorder");
			} else
				b = noFocusBorder;
			((JComponent) c).setBorder(b);
		}
		c.setFont(table.getFont());
	}
	
	
}