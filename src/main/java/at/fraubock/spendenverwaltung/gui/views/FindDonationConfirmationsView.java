package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.ConfirmationTableModel;
import at.fraubock.spendenverwaltung.interfaces.domain.Confirmation;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IConfirmationService;

public class FindDonationConfirmationsView extends InitializableView {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FindDonationConfirmationsView.class);
	private IConfirmationService confirmationService;
	private ConfirmationTableModel ConfirmationModel;
	private JTable showTable;
	private JScrollPane scrollPane;
	private JToolBar toolbar;
	private JButton reproduceButton;
	private JButton backButton;
	private JPanel overviewPanel;
	private JLabel feedbackLabel;
	private JLabel quickSearchLabel;
	private JTextField quickSearchField;
	private ComponentFactory componentFactory;
	private ViewActionFactory viewActionFactory;

	public FindDonationConfirmationsView(IConfirmationService confirmationService, ComponentFactory componentFactory,
			ViewActionFactory viewActionFactory) {

		this.componentFactory = componentFactory;
		this.viewActionFactory = viewActionFactory;
		this.confirmationService = confirmationService;
	}

	public void init() {
		this.setLayout(new MigLayout());
		overviewPanel = componentFactory.createPanel(800, 800);
		this.add(overviewPanel);

		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		addComponentsToToolbar(toolbar);
		overviewPanel.add(toolbar, "growx, span, wrap");
		initTable();

		quickSearchLabel = componentFactory.createLabel("Schnellsuche: ");
		quickSearchField = new JTextField(30);
		quickSearchField.addActionListener(new QuickSearchAction());

		overviewPanel.add(quickSearchLabel, "split 2");
		overviewPanel.add(quickSearchField, "gap 25, wrap 20px, growx");

		overviewPanel.add(scrollPane, "wrap");

		feedbackLabel = componentFactory.createLabel("");
		feedbackLabel.setFont(new Font("Bigger", Font.PLAIN, 13));

		overviewPanel.add(feedbackLabel, "wrap");
	}

	public void initTable() {
		if (ConfirmationModel == null) {
			ConfirmationModel = new ConfirmationTableModel();
		}
		
		try {
			ConfirmationModel.addAll(confirmationService.getByPersonNameLike(""));
		} catch (ServiceException e) {
			log.error(e);
			JOptionPane
			.showMessageDialog(
					overviewPanel,
					"Ein Fehler ist w\u00E4hrend dem Laden der Daten aufgetreten.",
					"Fehler", JOptionPane.ERROR_MESSAGE);
		}

		showTable = new JTable(ConfirmationModel);
		showTable.setFillsViewportHeight(true);
		showTable.setAutoCreateRowSorter(true);
		showTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(showTable);
		scrollPane.setPreferredSize(new Dimension(700, 550));
	}

	private void addComponentsToToolbar(JToolBar toolbar) {

		backButton = new JButton();
		Action getBack = viewActionFactory.getMainMenuViewAction();
		getBack.putValue(Action.SMALL_ICON, new ImageIcon(getClass()
				.getResource("/images/backButton.png")));
		backButton.setAction(getBack);

		reproduceButton = new JButton();
		reproduceButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		ReproduceAction reproduceAction = new ReproduceAction();
		reproduceAction.putValue(Action.NAME,
				"<html>&nbsp;Spendenbestätigung wiederherstellen</html>");
		reproduceButton.setAction(reproduceAction);

		toolbar.add(backButton, "split 2, growx");
		toolbar.add(reproduceButton);

	}

	public ConfirmationTableModel getConfirmationModel() {
		return this.ConfirmationModel;
	}
	
	private final class ReproduceAction extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public ReproduceAction(){
			super("Spendenbestätigung wiederherstellen");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(ConfirmationModel.getConfirmationRow(showTable.getSelectedRow())==null){
				JOptionPane
				.showMessageDialog(
						overviewPanel,
						"Es muss eine Spendenbest\u00E4tigung ausgew\u00E4hlt sein.",
						"Fehler", JOptionPane.ERROR_MESSAGE);
			}
			else{
				String outputName = showSaveDialog();
				if(!outputName.equals("")){
					try {
						File f = confirmationService.reproduceDocument(ConfirmationModel.getConfirmationRow(showTable.getSelectedRow()), outputName);
						Desktop.getDesktop().open(f);
					} catch (ServiceException e1) {
						log.error(e1);
						JOptionPane
						.showMessageDialog(
								overviewPanel,
								"Beim Wiederherstellen der Spendenbest\u00E4tigung ist ein Fehler aufgetreten",
								"Fehler", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						log.error(e1);
						JOptionPane
						.showMessageDialog(
								overviewPanel,
								"Beim öffnen der Spendenbest\u00E4tigung ist ein Fehler aufgetreten",
								"Fehler", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
			
		}
		
	}

	private final class QuickSearchAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (quickSearchField.getText().equals("")) {
				return;
			} else {
				try {
					List<Confirmation> results = confirmationService.getByPersonNameLike(quickSearchField.getText());
					ConfirmationModel.clear();
					ConfirmationModel.addAll(results);
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					// feedbackLabel
					// .setText("Ein Fehler passierte waehrend der Schnellsuche.");
					JOptionPane
							.showMessageDialog(
									overviewPanel,
									"Ein Fehler ist w\u00E4hrend der Schnellsuche aufgetreten. Bitte kontaktieren Sie Ihren Administrator.",
									"Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	private String showSaveDialog(){

        JFileChooser chooser; 
        String path = System.getProperty("user.home");

        chooser = new JFileChooser(path); 
        chooser.setDialogType(JFileChooser.SAVE_DIALOG); 
        
        FileFilter filterPdf = new FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory())
                    return true;
                return f.getName().toLowerCase().endsWith(".pdf");
            }

            public String getDescription() {
                return "pdf-File (*.pdf)";
            }
        };
        
        FileFilter filterDocx = new FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory())
                    return false;
                return f.getName().toLowerCase().endsWith(".docx");
            }

            public String getDescription() {
                return "docx-File (*.docx)";
            }
        };
        
        chooser.addChoosableFileFilter(filterPdf);
        chooser.addChoosableFileFilter(filterDocx);
        
        chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
        chooser.setDialogTitle("Speichern unter..."); 
        chooser.setFileFilter(filterPdf);
        chooser.setVisible(true); 

        int result = chooser.showSaveDialog(this); 

        if (result == JFileChooser.APPROVE_OPTION) { 

        	path = chooser.getSelectedFile().toString();
            
            if(chooser.getFileFilter().getDescription().contains("docx")){
            	if(path.endsWith(".docx"))
            		return path;
            	else
            		return path + ".docx";
            } else if(chooser.getFileFilter().getDescription().contains("pdf")){
            	if(path.endsWith(".pdf"))
            		return path;
            	else
            		return path + ".pdf";
            }
            chooser.setVisible(false);
        } 
        chooser.setVisible(false); 
        return ""; 
	}
}