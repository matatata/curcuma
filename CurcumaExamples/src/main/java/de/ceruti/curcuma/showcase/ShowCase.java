package de.ceruti.curcuma.showcase;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Category;

import de.ceruti.curcuma.api.appkit.NSSelectionMarker;
import de.ceruti.curcuma.api.appkit.controller.NSArrayFilter;
import de.ceruti.curcuma.api.appkit.view.NSControl;
import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSComboCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.appkit.view.table.NSTableColumn;
import de.ceruti.curcuma.api.appkit.view.table.NSTableView;
import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;
import de.ceruti.curcuma.appkit.controllers.NSArrayControllerImpl;
import de.ceruti.curcuma.appkit.view.NSComboBoxImpl;
import de.ceruti.curcuma.appkit.view.NSTextFieldImpl;
import de.ceruti.curcuma.appkit.view.cells.NSComboBoxCell;
import de.ceruti.curcuma.appkit.view.cells.NSTextFieldCell;
import de.ceruti.curcuma.appkit.view.cells.SimpleCell;
import de.ceruti.curcuma.appkit.view.table.NSTable;
import de.ceruti.curcuma.appkit.view.table.NSTableCol;
import de.ceruti.curcuma.appkit.view.table.TablePlugin;
import de.ceruti.curcuma.appkit.widgets.swing.PlugInFactory;
import de.ceruti.curcuma.appkit.widgets.swing.SwingComboBoxWidgetPlugIn;
import de.ceruti.curcuma.appkit.widgets.swing.SwingLabelWidgetPlugIn;
import de.ceruti.curcuma.appkit.widgets.swing.SwingTextWidgetPlugIn;
import de.ceruti.curcuma.foundation.Company;
import de.ceruti.curcuma.foundation.Employee;
import de.ceruti.curcuma.keyvaluebinding.DefaultBindingOptions;

@SuppressWarnings("rawtypes")
public class ShowCase extends JFrame {

	private final static Category logger = Category.getInstance(ShowCase.class);
	
	JTable table;
	JButton addButton;
	JButton removeButton;
	JButton aButton;
	JTextField filter;
	JTextComponent selectionNameJTextComponent;
	JTextComponent selectionSalaryJTextComponent;
	JTextComponent selectionBossSalaryJTextComponent;
	JComboBox selectionBossComboBox;
	
	JTextField salaryCellEditorJTextField;
	JLabel salaryCellJLabel;
	
	JLabel descriptionLabel;
	
	JComboBox bossCellEditorComboBox;
	JComboBox bossCellRendererComboBox;
	
	java.util.List<Employee> employees;

	NSArrayControllerImpl arrayController;
	NSTableView nsTable;

	private void initSalaryColumn() {
		//Editor Cell:
		NSTextFieldCell editorCell = new NSTextFieldCell();
		editorCell.setFormat(NumberFormat.getNumberInstance());
		SwingTextWidgetPlugIn p = new SwingTextWidgetPlugIn();
		editorCell.setWidgetPlugIn(p);
		editorCell.setEditBackground(Color.yellow);
		editorCell.setInvalidForeground(Color.red);

		//Display Cell
		NSTextFieldCell lcell = new NSTextFieldCell();
		lcell.setFormat(NumberFormat.getCurrencyInstance());
		SwingLabelWidgetPlugIn l = new SwingLabelWidgetPlugIn();
		lcell.setWidgetPlugIn(l);
		salaryCellJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//Column
		NSTableCol salaryCol = new NSTableCol();
		salaryCol.setIdentifier("salary");
		salaryCol.setDataCell(lcell);
		salaryCol.setEditorCell(editorCell);
		nsTable.addTableColumn(salaryCol);
		
		salaryCol.bind(NSCell.CellValueBinding,arrayController,"arrangedObjects.salary",new DefaultBindingOptions());
		
	}
	
	private void initSelectionDesc(){
		SwingLabelWidgetPlugIn plug = new SwingLabelWidgetPlugIn();
		plug.setWidget(descriptionLabel);
		NSTextFieldCell cell = new NSTextFieldCell();
		cell.setWidgetPlugIn(plug);
		NSTextFieldImpl tf = new NSTextFieldImpl();
		tf.setCell(cell);
		tf.setViewPlugIn(plug);
		
		tf.bind(NSControl.ControlValueBinding,arrayController,"selection.description",new DefaultBindingOptions());
	}
	
	private void initSelectionSalary(){
		SwingTextWidgetPlugIn plug = new SwingTextWidgetPlugIn();
		plug.setWidget(selectionSalaryJTextComponent);
		NSTextFieldCell salaryCell = new NSTextFieldCell();
		salaryCell.setFormat(NumberFormat.getCurrencyInstance(Locale.GERMANY));
		salaryCell.setFormatWhenFocused(NumberFormat
				.getNumberInstance());
		salaryCell.setWidgetPlugIn(plug);
		NSTextFieldImpl salaryTextField = new NSTextFieldImpl();
		salaryTextField.setCell(salaryCell);
		salaryTextField.setViewPlugIn(plug);

		salaryCell.setUncommitedChangeBackground(Color.cyan);
		salaryCell.setInvalidForeground(Color.red);
		
		DefaultBindingOptions opts = new DefaultBindingOptions();
		salaryTextField.setDefaultPlaceholderForMarkerWithBinding("<NoSelection>",
				NSSelectionMarker.NSNoSelectionMarker,
				NSControl.ControlValueBinding);
		opts.setCustomBindingOptionForKey("<MultipleValues>",
				NSSelectionMarker.NSMultipleValuesMarker.getBindingOptionKey());
		
		salaryTextField.bind(NSControl.ControlValueBinding, arrayController,
				"selection.salary", opts);
		
		
		salaryTextField.setAutoCommit(false);
//		salaryTextField.setContinuouslyCommit(true);
	}
	
	private void initSelectionBossSalary(){
		SwingTextWidgetPlugIn plug = new SwingTextWidgetPlugIn();
		plug.setWidget(selectionBossSalaryJTextComponent);
		NSTextFieldCell salaryCell = new NSTextFieldCell();
		salaryCell.setFormat(NumberFormat.getCurrencyInstance(Locale.US));
		salaryCell.setFormatWhenFocused(NumberFormat
				.getNumberInstance());
		salaryCell.setWidgetPlugIn(plug);
		NSTextFieldImpl salaryTextField = new NSTextFieldImpl();
		salaryTextField.setCell(salaryCell);
		salaryTextField.setViewPlugIn(plug);
		
		salaryCell.setUncommitedChangeBackground(Color.cyan);
		salaryCell.setInvalidForeground(Color.red);

		DefaultBindingOptions opts = new DefaultBindingOptions();
		salaryTextField.setDefaultPlaceholderForMarkerWithBinding("<NoSelection>",
				NSSelectionMarker.NSNoSelectionMarker,
				NSControl.ControlValueBinding);
		opts.setCustomBindingOptionForKey("<MultipleValues>",
				NSSelectionMarker.NSMultipleValuesMarker.getBindingOptionKey());
		opts.setCustomBindingOptionForKey("<Null>",
				NSSelectionMarker.NSNullValueMarker.getBindingOptionKey());
		
		
		salaryTextField.bind(NSControl.ControlValueBinding, arrayController,
				"selection.boss.salary", opts);
		
		
	}
	
	private void initSelectionBossCombo() {
		SwingComboBoxWidgetPlugIn comboPlug = new SwingComboBoxWidgetPlugIn();
		NSComboBoxCell comboCell = new NSComboBoxCell();
		comboCell.setWidgetPlugIn(comboPlug);
		
		

		NSTextFieldCell editorTf = new NSTextFieldCell();
		SwingTextWidgetPlugIn pll = new SwingTextWidgetPlugIn();
		editorTf.setWidgetPlugIn(pll);
		
		comboCell.setEditorCell(editorTf);
		editorTf.setEditBackground(Color.red);
		editorTf.setInvalidForeground(Color.red);
		comboPlug.setWidget(selectionBossComboBox);
		
		
		NSComboBoxImpl comboText = new NSComboBoxImpl();
		comboText.setCell(comboCell);
		comboText.setViewPlugIn(comboPlug);
		comboCell.bind(NSComboCell.ContentBinding, arrayController,
				"arrangedObjects", new DefaultBindingOptions());
		comboCell.bind(NSComboCell.ContentValuesBinding, arrayController,
				"arrangedObjects.name", new DefaultBindingOptions());
	
		comboText.bind(NSControl.ControlValueBinding, arrayController,
				"selection.boss", new DefaultBindingOptions());
		
		
		
	}
	
	private void initSelectionNameField() {
		// selection field name
		SwingTextWidgetPlugIn textPlugIn = new SwingTextWidgetPlugIn();
		textPlugIn.setWidget(selectionNameJTextComponent);
		NSTextFieldCell textCell = new NSTextFieldCell();
		textCell.setWidgetPlugIn(textPlugIn);
		NSTextFieldImpl nsText = new NSTextFieldImpl();
		nsText.setCell(textCell);
		nsText.setViewPlugIn(textPlugIn);
		
		nsText.setDefaultPlaceholderForMarkerWithBinding("<Ohne Name>",
				NSSelectionMarker.NSNullValueMarker,
				NSControl.ControlValueBinding);
		
		nsText.bind(NSControl.ControlValueBinding, arrayController,
				"selection.name", new DefaultBindingOptions());
		
		
		textCell.setUncommitedChangeBackground(Color.cyan);
		textCell.setInvalidForeground(Color.red);
		
		
		nsText.setAutoCommit(false);
//		nsText.setContinuouslyCommit(true);
		
	}
	
	private void initMainComponents(){
		table = new JTable();
		addButton = new JButton("add");
		removeButton = new JButton("remove");
		aButton = new JButton("addSomeRows");
		filter = new JTextField();
		descriptionLabel = new JLabel();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout());
		
		
		//Table
		table.setPreferredScrollableViewportSize(new Dimension(460,340)); 
		
		pack();
		setBounds(100, 100, 500, 550);
		setVisible(true);

		addButton.addActionListener(new ActionListener() {
			int johnCount = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				arrayController.addObject(new Employee("John Doe"
						+ (++johnCount)));
			}
		});

		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				arrayController.remove();

			}
		});

		aButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Employee> em = new ArrayList<Employee>();
				for (int i = 0; i < 400; i++) {
					em.add(new Employee("Employee " + i));
				}
				
				arrayController.addObjects(em);
			}
		});

		filter.setPreferredSize(new Dimension(100, 19));
		filter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				arrayController.setFilterPredicate(new NSArrayFilter() {

					@Override
					public boolean filter(Object o) {
						Employee e = (Employee) o;
						if (e != null
								&& e.getName().indexOf(filter.getText()) != -1)
							return true;
						return false;
					}
				});
			}
		});
		
		
		selectionNameJTextComponent = new JTextField();
		selectionBossComboBox = new JComboBox();
		selectionBossComboBox.setEditable(true);
		//  In JDK1.4 this prevents action events from being fired when the
		//  up/down arrow keys are used on the dropdown menu
 		selectionBossComboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
		
		selectionSalaryJTextComponent = new JTextField();
		((JTextField)selectionSalaryJTextComponent).setHorizontalAlignment(SwingConstants.RIGHT);
		selectionBossSalaryJTextComponent = new JTextField();
		((JTextField)selectionBossSalaryJTextComponent).setHorizontalAlignment(SwingConstants.RIGHT);
		
		
		JPanel controlsPanel = new JPanel(new GridLayout(0,4));
		JPanel mainPanel = new JPanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				controlsPanel, mainPanel);
		

		controlsPanel.add(selectionSalaryJTextComponent);
		controlsPanel.add(selectionNameJTextComponent);
		controlsPanel.add(selectionBossComboBox);
		controlsPanel.add(selectionBossSalaryJTextComponent);
		controlsPanel.add(addButton);
		controlsPanel.add(removeButton);
		controlsPanel.add(aButton);
		controlsPanel.add(filter);
		
		mainPanel.add(new JScrollPane(table));
		mainPanel.add(descriptionLabel);
	
		

		add(splitPane);
		
		
	}
	
	private void initCellComponents() {
		salaryCellJLabel = new DefaultTableCellRenderer();
		salaryCellJLabel.setIcon(new ImageIcon(getClass().getResource("icon_info.gif")));
		salaryCellJLabel.setBorder(new EmptyBorder(1, 1, 1, 1));
		salaryCellJLabel.setOpaque(true);
		
		salaryCellEditorJTextField = new JTextField();

		
		bossCellRendererComboBox = new JComboBox();
		bossCellEditorComboBox = new JComboBox();
		bossCellEditorComboBox.setEditable(true);

	}
	
	private void initModelsAndControllers() {
		employees = new ArrayList<Employee>();

		arrayController = new NSArrayControllerImpl();
		TablePlugin tablePlugin = PlugInFactory.get().createPlugInForComponent(table);
		nsTable = new NSTable();
		((NSTable) nsTable).setViewPlugIn(tablePlugin);
	}

	private void initTable(){
		
		initSalaryColumn();
		initNameColumn();
		initBossColumns();
		
		nsTable.setDelegate(new NSTableView.Delegate() {
			
			@Override
			public boolean isCellEditable(int row, NSTableColumn col) {
				return true;
			}

			@Override
			public boolean shouldChangeSelectionIndexes(IndexSet newSelection) {
				return true;
			}

			@Override
			public NSEditorCell getEditorCell(int row, int col) {
				return null;
			}
		});
		
		nsTable.bind(NSTableView.ContentArrayBinding, arrayController,
				"arrangedObjects", new DefaultBindingOptions());
		nsTable.bind("selectionIndexes", arrayController, "selectionIndexes",
				new DefaultBindingOptions());
		
	}
	
	private void initNameColumn(){
		// name Column. Nothing special
		NSTableCol nameCol = new NSTableCol();
		nameCol.setIdentifier("name");
		nameCol.setDataCell(new SimpleCell());
		nameCol.bind(NSCell.CellValueBinding,arrayController,"arrangedObjects.name",new DefaultBindingOptions());

		nsTable.addTableColumn(nameCol);
	}
	
	private void initBossColumns() {
		
		
		// Boss Cell Editor
		NSTableCol bossCol = new NSTableCol();

		
		NSComboBoxCell funCell = new NSComboBoxCell();
		SwingComboBoxWidgetPlugIn simplePlug = new SwingComboBoxWidgetPlugIn();
		funCell.setWidgetPlugIn(simplePlug);
		simplePlug.setWidget(bossCellEditorComboBox);
		
		
		funCell.bind(NSComboCell.ContentBinding, arrayController,
				"arrangedObjects", new DefaultBindingOptions());
		funCell.bind(NSComboCell.ContentValuesBinding, arrayController,
				"arrangedObjects.name", new DefaultBindingOptions());

		
		bossCol.setEditorCell(funCell);
		bossCol.setDataCell(new SimpleCell());
		nsTable.addTableColumn(bossCol);
		
		bossCol.bind(NSComboCell.ContentBinding,arrayController,"arrangedObjects.boss",new DefaultBindingOptions());
		bossCol.bind(NSComboCell.ContentValuesBinding,arrayController,"arrangedObjects.boss.name",new DefaultBindingOptions());
		
		
		//A Column displaying the bosse's salary
		NSTableColumn bossesSalary=new NSTableCol();
		nsTable.addTableColumn(bossesSalary);
		bossesSalary.setDataCell(new SimpleCell());
		bossesSalary.bind(NSCell.CellValueBinding,arrayController,"arrangedObjects.boss.salary",new DefaultBindingOptions());
	}
	
	public ShowCase() {
		
		initMainComponents();
		initModelsAndControllers();
		initCellComponents();
		

		initTable();
		
		initSelectionBossCombo();
		initSelectionNameField();
		initSelectionSalary();
		initSelectionBossSalary();
		
		
		initSelectionDesc();


		
		table.setRowHeight(bossCellEditorComboBox.getPreferredSize().height);

		

		Company company = Company.createSampleCompany();
		arrayController.setObjectClass(Employee.class);

		arrayController.setContentArray(employees);
		arrayController.setSelectsInsertedObjects(false);
		arrayController.setPreservesSelection(true);
		arrayController.setAlwaysUsesMultipleValuesMarker(false);
		arrayController.addObjects(company.getEmployees());

		arrayController.addObserver(new KVObserver() {
			@Override
			public void observeValue(String keypath, KeyValueObserving object,
					KVOEvent change, Object context) {
				removeButton.setEnabled(arrayController.canRemove());
			}
		},"canRemove",null,KVOOption.KeyValueObservingOptionNew);
				
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {

					UIManager.setLookAndFeel(UIManager
							.getCrossPlatformLookAndFeelClassName());
				} catch (Exception e) {
				}
				new ShowCase();

			}
		});

	}

}
