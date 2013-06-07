package at.fraubock.spendenverwaltung.gui;

import java.awt.Font;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;


public class ImportData extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8149156595292128784L;
	private IImportService importService;
	private ActionHandler actionHandler;
	private Overview overview;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private JPanel panel;
	private JFileChooser chooser;
	private JButton openFileBtn;
	private JTextField pathField;
	private JComboBox<String[]> importType;
	private String[] importTypes;
	private JButton importBtn;
	private JButton backBtn;
	private JLabel headline;

	
	public ImportData(IImportService importService, Overview overview){
		super(new MigLayout());
		
		this.importService = importService;
		this.overview = overview;
		this.actionHandler = new ActionHandler(this);
		this.builder = new ComponentBuilder();
		this.buttonListener = new ButtonListener(this);
		this.chooser = new JFileChooser();
		
		setUp();
	}
	
	private void setUp(){
		panel = builder.createPanel(800, 800);
		
		headline = builder.createLabel("CSV-Files importieren ");
		headline.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(headline, "wrap");
		JLabel empty = builder.createLabel("		");
		panel.add(empty, "wrap");
		
		
		importTypes = new String[]{"native import", "Hypo import", "SMS import"};
		importType = builder.createComboBox(importTypes, actionHandler);
		panel.add(importType, "wrap, growx");
		
		pathField = builder.createTextField("");
		panel.add(pathField, "growx");
		openFileBtn = builder.createButton("Choose file", buttonListener, "open_file_import_data");
		panel.add(openFileBtn, "wrap, growx");
		
		JLabel empty2 = builder.createLabel("		");
		panel.add(empty2, "wrap");
		
		importBtn = builder.createButton("Import", buttonListener, "import_import_data");
		panel.add(importBtn, "split2");
		
		backBtn = builder.createButton("Abbrechen", buttonListener, "return_from_import_data_to_overview");
		panel.add(backBtn);
		
		this.add(panel, "wrap");
	}

	public void returnTo() {
		this.removeAll();
		this.revalidate();
		this.repaint();
		overview.removeAll();
		overview.revalidate();
		overview.repaint();
		overview.setUp();
		
	}

	public void chooseFile() {
		int chooserReturn = chooser.showOpenDialog(this);
		
		if(chooserReturn == JFileChooser.APPROVE_OPTION){
			pathField.setText(chooser.getSelectedFile().getAbsolutePath());
		}
	}

	public void importData() {
		if(pathField.getText().length()>0){
			switch(importType.getSelectedItem().toString()){
			case "native import":
				try {
					importService.nativeImport(new File(pathField.getText()));
				} catch (ServiceException e) {
					JOptionPane.showMessageDialog(this, "An error occured during import", "Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
				break;
			default:
			}
		}
		
	}
	
}
