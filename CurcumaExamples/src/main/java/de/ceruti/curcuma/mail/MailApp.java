package de.ceruti.curcuma.mail;

import java.awt.Color;
import java.awt.Font;
import java.awt.ItemSelectable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.ceruti.curcuma.api.appkit.controller.NSArrayController;
import de.ceruti.curcuma.api.appkit.controller.NSObjectController;
import de.ceruti.curcuma.api.appkit.view.NSControl;
import de.ceruti.curcuma.api.appkit.view.NSTextField;
import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSTextCell;
import de.ceruti.curcuma.api.appkit.view.table.NSTableView;
import de.ceruti.curcuma.appkit.controllers.NSArrayControllerImpl;
import de.ceruti.curcuma.appkit.view.NSTextFieldImpl;
import de.ceruti.curcuma.appkit.view.cells.NSTextFieldCell;
import de.ceruti.curcuma.appkit.view.cells.SimpleCell;
import de.ceruti.curcuma.appkit.view.table.NSTable;
import de.ceruti.curcuma.appkit.view.table.NSTableCol;
import de.ceruti.curcuma.appkit.view.table.TablePlugin;
import de.ceruti.curcuma.appkit.widgets.swing.NSCellFactory;
import de.ceruti.curcuma.appkit.widgets.swing.PlugInFactory;
import de.ceruti.curcuma.keyvaluebinding.DefaultBindingOptions;
import net.miginfocom.swing.MigLayout;

public class MailApp {
	private static JFrame frame;
	private static JTable resourceTable;
	private static JTable privsTable;
	private static NSArrayController resourcesArrayController;
	private static NSArrayController mailArrayController;
	private static JTextArea textArea;
	private static NSCellFactory cellFactory = NSCellFactory.create(PlugInFactory.get());

	private static NSTextField textField;

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());
//		UIManager.setLookAndFeel(UIManager
//				.getSystemLookAndFeelClassName());
		
		
		initSwing();
		
		final List<Mailbox> resources=new ArrayList<Mailbox>();

		for (int j = 0; j < 2; j++) {
			Mailbox res = new Mailbox();
			res.setName("mbox" + j);
			for (int i = 0; i < 10; i++) {
				Mail mail = new Mail();
				mail.setBetreff("Subject" + i);
				mail.setBody("Body" + i);
				mail.setRcptTo("johndoe" + i + "@doe.com");
				mail.setDate(new Date());
				res.getMails().add(mail);
			}

			resources.add(res);
		}
		initResourcesArrayController(resources);
		initPrivsArrayController();
		
		initResourceTable();
		initMailsTable();
		initBodyField();
	}

	private static void initBodyField() {
		NSTextCell textCell = cellFactory.createCellForComponent(textArea);
		
		textField = new NSTextFieldImpl();
		textField.setCell(textCell);
		((NSTextFieldImpl)textField).setViewPlugIn(((NSTextFieldCell)textCell).getWidgetPlugIn());
		textField.bind(NSControl.ControlValueBinding, mailArrayController, "selection.body", new DefaultBindingOptions());
		
		
		
	}

	private static void initResourceTable() {
		TablePlugin tablePlugin = PlugInFactory.get().createPlugInForComponent(resourceTable);
		NSTableView resourcesTableView = new NSTable();
		((NSTable)resourcesTableView).setViewPlugIn(tablePlugin);
		
		NSTableCol nameCol = new NSTableCol();
		nameCol.setIdentifier("Mailbox");
		SimpleCell dataCell = new SimpleCell();
		nameCol.setDataCell(dataCell);
		nameCol.bind(NSCell.CellValueBinding,resourcesArrayController,"arrangedObjects.name",new DefaultBindingOptions());
		resourcesTableView.addTableColumn(nameCol);
		
		resourcesTableView.bind(NSTableView.ContentArrayBinding, resourcesArrayController,
				"arrangedObjects", new DefaultBindingOptions());
		resourcesTableView.bind("selectionIndexes", resourcesArrayController, "selectionIndexes",
				new DefaultBindingOptions());
	}
	
	private static void initMailsTable() {
		TablePlugin tablePlugin = PlugInFactory.get().createPlugInForComponent(privsTable);
		NSTableView privsTableView = new NSTable();
		((NSTable)privsTableView).setViewPlugIn(tablePlugin);
		initDateColumn(privsTableView);
		
		NSTableCol col;
		initFlagColumn(privsTableView);
		
		
		col = new NSTableCol();
		col.setDataCell(new SimpleCell());
		col.setIdentifier("Subject");
		col.bind(NSCell.CellValueBinding,mailArrayController,"arrangedObjects.betreff",new DefaultBindingOptions());
		privsTableView.addTableColumn(col);
		
		
		col = new NSTableCol();
		col.setDataCell(new SimpleCell());
		col.setIdentifier("RcptTo");
		col.bind(NSCell.CellValueBinding,mailArrayController,"arrangedObjects.rcptTo",new DefaultBindingOptions());
		privsTableView.addTableColumn(col);
		
		
		
		privsTableView.bind(NSTableView.ContentArrayBinding, mailArrayController,
				"arrangedObjects", new DefaultBindingOptions());
		privsTableView.bind("selectionIndexes", mailArrayController, "selectionIndexes",
				new DefaultBindingOptions());
	}

	private static void initFlagColumn(NSTableView privsTableView) {
		NSTableCol col = new NSTableCol();
		col.setDataCell(cellFactory.createCellForComponent(new JCheckBox()));
		NSEditorCell editorCell = cellFactory.createCellForComponent(new JCheckBox());
		col.setEditorCell(editorCell);
		col.setIdentifier("Flag");
		col.bind(NSCell.CellValueBinding,mailArrayController,"arrangedObjects.flagged",new DefaultBindingOptions());
		privsTableView.addTableColumn(col);
	}

	private static void initDateColumn(NSTableView privsTableView) {
		NSTableCol col = new NSTableCol();
		NSTextFieldCell dateCell = (NSTextFieldCell) cellFactory.createCellForComponent(new JLabel());
		dateCell.setFormat(DateFormat.getDateInstance(DateFormat.LONG,Locale.getDefault()));
		col.setDataCell(dateCell);
		NSTextFieldCell editorCell = new NSTextFieldCell();
		editorCell.setFormat(DateFormat.getDateInstance(DateFormat.SHORT,Locale.getDefault()));
		JTextField ed = new JTextField();
		ed.setFont(new Font("Serif", Font.BOLD, 12));
		ed.setForeground(Color.BLUE);
		
		editorCell.setWidgetPlugIn(PlugInFactory.get().createPlugInForComponent(ed));
		col.setEditorCell(editorCell);
		col.setIdentifier("Date");
		col.bind(NSCell.CellValueBinding,mailArrayController,"arrangedObjects.date",new DefaultBindingOptions());
		privsTableView.addTableColumn(col);
	}

	private static void initResourcesArrayController(final List<Mailbox> resources) {
		resourcesArrayController = new NSArrayControllerImpl();
		resourcesArrayController.setContent(resources);
	}

	private static void initPrivsArrayController() {
		mailArrayController = new NSArrayControllerImpl();
		mailArrayController.bind(NSObjectController.ContentBinding,
				resourcesArrayController, "selection.mails",
				new DefaultBindingOptions());
	}

	private static void initSwing() {
		frame = new JFrame("Resources");
		frame.getContentPane().setLayout(new MigLayout("fill,insets 0"));
		resourceTable = new JTable();
		privsTable = new JTable();
		textArea = new JTextArea();
		frame.setSize(600, 400);
		frame.getContentPane().add(new JScrollPane(resourceTable),"width 100:100:120,grow,spany");
		frame.getContentPane().add(new JScrollPane(privsTable),"width :100:,height 80:100:150, grow, wrap");
		frame.getContentPane().add(new JScrollPane(textArea),"grow, push");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
