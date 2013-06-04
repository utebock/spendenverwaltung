package at.fraubock.spendenverwaltung.gui;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
		panel = builder.createPanel(800, 300);
		
		pathField = builder.createTextField("");
		panel.add(pathField, "growx");
		openFileBtn = builder.createButton("Choose file", buttonListener, "open_file_import_data");
		panel.add(openFileBtn, "wrap, growx");

		importTypes = new String[]{"native import", "Hypo import", "SMS import"};
		importType = builder.createComboBox(importTypes, actionHandler);
		panel.add(importType, "wrap, growx");
		
		importBtn = builder.createButton("Import", buttonListener, "import_import_data");
		panel.add(importBtn, "wrap, growx");
		
		backBtn = builder.createButton("Abbrechen", buttonListener, "return_from_import_data_to_overview");
		panel.add(backBtn);
		
		this.add(panel, "wrap, growx");
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
