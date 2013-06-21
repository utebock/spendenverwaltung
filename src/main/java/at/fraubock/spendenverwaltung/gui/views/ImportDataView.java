package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;

/**
 * 
 * @author romanvoglhuber
 *
 */
public class ImportDataView extends InitializableView {

	private static final long serialVersionUID = -8149156595292128784L;
	private IImportService importService;
	private ComponentFactory componentFactory;
	private ViewActionFactory viewActionFactory;
	private JPanel panel;
	private JFileChooser chooser;
	private JButton openFileBtn;
	private JTextField pathField;
	private JComboBox<String> importType;
	private String[] importTypes;
	private JButton importBtn;
	private JButton backBtn;
	private JLabel headline;
	
	public ImportDataView(ComponentFactory componentFactory, ViewActionFactory viewActionFactory, IImportService importService){
		this.setLayout(new MigLayout());
		this.componentFactory = componentFactory;
		this.viewActionFactory = viewActionFactory;
		this.importService = importService;
		
		this.chooser = new JFileChooser();
		setUpLayout();
	}
	
	@Override
	public void init() {
			openFileBtn.setAction(new ChooseFileAction());
			importBtn.setAction(new ImportDataAction());
			Action cancelAction = viewActionFactory.getMainMenuViewAction();
			cancelAction.putValue(Action.NAME, "Abbrechen");
			cancelAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backInButton.png")));
			backBtn.setAction(cancelAction);
			backBtn.setFont(new Font("bigger", Font.PLAIN, 13));
	}
	
	private void setUpLayout(){
		panel = componentFactory.createPanel(700, 800);
		
		headline = componentFactory.createLabel("CSV-Files importieren ");
		headline.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(headline, "wrap");
		JLabel empty = componentFactory.createLabel("		");
		panel.add(empty, "wrap");
		
		
		importTypes = new String[]{"Nativer Import", "Hypo-Import", "SMS-Import"};
		importType = new JComboBox<String>(importTypes);
		panel.add(importType, "wrap, growx");
		
		pathField = componentFactory.createTextField("");
		panel.add(pathField, "growx");
		openFileBtn = new JButton("Datei ausw\u00E4hlen");
		panel.add(openFileBtn, "wrap 20px");
		
		importBtn = new JButton("Import");
		panel.add(importBtn, "split2");
		
		backBtn = new JButton("Abbrechen");
		panel.add(backBtn);
		
		this.add(panel, "wrap");
	}

	private final class ChooseFileAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;

		public ChooseFileAction(){
			super("Datei ausw\u00E4hlen");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int chooserReturn = chooser.showOpenDialog(null);
			
			if(chooserReturn == JFileChooser.APPROVE_OPTION){
				pathField.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

	private final class ImportDataAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;

		public ImportDataAction(){
			super("Importieren");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (pathField.getText().length() > 0) {
				File selectedFile = new File(pathField.getText());
				try {
					switch (importType.getSelectedItem().toString()) {
					case "Nativer Import":
						importService.nativeImport(selectedFile);
						break;
					case "Hypo-Import":
						importService.hypoImport(selectedFile);
						break;
					case "SMS-Import":
						importService.smsImport(selectedFile);
						break;
					default:
						break;
					}
					JOptionPane.showMessageDialog(null, "Es wurden alle Daten importiert",
							"Import", JOptionPane.INFORMATION_MESSAGE);
				} catch (ServiceException ex) {
					JOptionPane.showMessageDialog(null,	ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null,	ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}


}
