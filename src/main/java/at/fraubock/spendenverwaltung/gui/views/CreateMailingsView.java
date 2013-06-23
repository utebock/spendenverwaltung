package at.fraubock.spendenverwaltung.gui.views;

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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.StringTextField;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing.Medium;
import at.fraubock.spendenverwaltung.interfaces.domain.MailingTemplate;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailChimpService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailChimpService.MailChimpListItem;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.MailingTemplateUtil;
import at.fraubock.spendenverwaltung.util.filter.FilterType;

/**
 * 
 * @author Chris Steele
 * 
 */

public class CreateMailingsView extends InitializableView {

	private static final Logger log = Logger
			.getLogger(CreateMailingsView.class);

	private static final long serialVersionUID = 1L;

	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
	private IMailingService mailingService;
	private IFilterService filterService;
	private IPersonService personService;
	private IMailChimpService mailChimpService;
	private JXDatePicker emailDatePicker;
	private JXDatePicker postalDatePicker;
	private JComboBox<SupportedFileFormat> fileFormatBox;
	private JLabel fileFormatLabel;
	
	public enum SupportedFileFormat{
		PDF, DOCX
	}

	// personfilters
	private Filter selectedEmailFilter;
	private Filter selectedPostalFilter;
	
	private File templateFile;

	JPanel contentPanel, createEMailingPanel, createPostalMailingPanel,
			createReproducePanel;

	JLabel emailTitle, postalTitle, reproduceTitle, emailFilterLabel,
			postalFilterLabel, emailTypeLabel, postalTypeLabel, emailDateLabel,
			postalDateLabel, feedbackLabel, outputNameLabel, emailMailChimpLabel;
	JButton createEMailingButton, createPostalMailingButton, fileChooserButton,
			cancelEMailingButton, cancelPostalMailingButton, emailMailChimpApiButton;
	JSeparator separator;
	
	StringTextField outputNameField;

	JComboBox<Filter> eMailingPersonFilterChooser, postalPersonFilterChooser;
	JComboBox<Mailing.MailingType> eMailingTypeChooser,
			postalMailingTypeChooser;
	JComboBox<MailChimpListItem> emailMailChimpListChooser;

	private JLabel fileChooserLabel;

	private JLabel apiKeyLabel;
	

	public CreateMailingsView(ViewActionFactory viewActionFactory,
			ComponentFactory componentFactory, IMailingService mailingService,
			IFilterService filterService, IPersonService personService, IMailChimpService mailChimpService) {
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.mailingService = mailingService;
		this.filterService = filterService;
		this.personService = personService;
		this.mailChimpService = mailChimpService;
		setUpLayout();
	}

	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(750, 800);
		createEMailingPanel = componentFactory.createPanel(750, 350);
		createPostalMailingPanel = componentFactory.createPanel(750, 350);

		this.add(contentPanel);

		contentPanel.add(createEMailingPanel, "wrap");
		// end of emailing panel, add separator
		contentPanel.add(componentFactory.createSeparator(), "wrap, growx");
		contentPanel.add(createPostalMailingPanel, "wrap");
		// end of mailing panel, add separator
		//contentPanel.add(componentFactory.createSeparator(), "wrap, growx");

		// set title label
		emailTitle = componentFactory
				.createLabel("Neue E-Mail-Aussendung erstellen");
		emailTitle.setFont(new Font("Headline", Font.PLAIN, 14));
		createEMailingPanel.add(emailTitle, "wrap 20px");

		// filter combobox and label
		emailFilterLabel = componentFactory
				.createLabel("Personenfilter ausw\u00E4hlen: ");
		eMailingPersonFilterChooser = new JComboBox<Filter>();
		createEMailingPanel.add(emailFilterLabel, "split2");
		createEMailingPanel.add(eMailingPersonFilterChooser, "gap 108, wrap, growx");

		// type combobox and label
		emailTypeLabel = componentFactory
				.createLabel("Aussendungstyp ausw\u00E4hlen: ");
		eMailingTypeChooser = new JComboBox<Mailing.MailingType>(
				Mailing.MailingType.values());
		createEMailingPanel.add(emailTypeLabel, "split 2");
		createEMailingPanel.add(eMailingTypeChooser, "gap 90, wrap, growx");

		emailDateLabel = componentFactory.createLabel("Datum ausw\u00E4hlen: ");
		//eMailingPersonFilterChooser = new JComboBox<Filter>();
		emailDatePicker = new JXDatePicker(new java.util.Date());
		createEMailingPanel.add(emailDateLabel, "split2");
		createEMailingPanel.add(emailDatePicker, "gap 210, wrap, growx");
		
		//MailChimp listChooser
		emailMailChimpLabel = componentFactory.createLabel("MailChimp-Liste ausw\u00E4hlen: ");
		emailMailChimpListChooser = new JComboBox<MailChimpListItem>();
		createEMailingPanel.add(emailMailChimpLabel, "split 2");
		createEMailingPanel.add(emailMailChimpListChooser, "gap 90, wrap, growx");
		
		apiKeyLabel = componentFactory.createLabel("MailChimp-API-Schl\u00FCssel \u00E4ndern: ");
		createEMailingPanel.add(apiKeyLabel, "split2");
		emailMailChimpApiButton = new JButton("MailChimp API Key");
		createEMailingPanel.add(emailMailChimpApiButton, "gap 160, wrap 20px, growx");
		
		// buttons
		createEMailingButton = new JButton("Anlegen");
		cancelEMailingButton = new JButton("Abbrechen");
		
		createEMailingPanel.add(createEMailingButton, "split2");
		createEMailingPanel.add(cancelEMailingButton, "wrap");
		

		// set up postal mailing panel layout
		postalTitle = componentFactory
				.createLabel("Neue Brief-Aussendung erstellen");
		postalTitle.setFont(new Font("Headline", Font.PLAIN, 14));
		createPostalMailingPanel.add(postalTitle, "wrap 20px");

		// filter combobox and label
		postalFilterLabel = componentFactory
				.createLabel("Personenfilter ausw\u00E4hlen: ");
		postalPersonFilterChooser = new JComboBox<Filter>();
		createPostalMailingPanel.add(postalFilterLabel, "split2");
		createPostalMailingPanel.add(postalPersonFilterChooser, "gap 108, wrap, growx");

		// type combobox and label
		postalTypeLabel = componentFactory
				.createLabel("Aussendungstyp ausw\u00E4hlen: ");
		postalMailingTypeChooser = new JComboBox<Mailing.MailingType>(
				Mailing.MailingType.values());
		createPostalMailingPanel.add(postalTypeLabel, "split2");
		createPostalMailingPanel.add(postalMailingTypeChooser, "gap 90, wrap, growx");

		postalDateLabel = componentFactory.createLabel("Datum ausw\u00E4hlen: ");
		postalDatePicker = new JXDatePicker(new java.util.Date());
		createPostalMailingPanel.add(postalDateLabel, "split2");
		createPostalMailingPanel.add(postalDatePicker, "gap 210, wrap, growx");
		
		fileChooserLabel = componentFactory.createLabel("Vorlage ausw\u00E4hlen: ");
		fileChooserButton = new JButton("Auswaehlen");
		createPostalMailingPanel.add(fileChooserLabel, "split2");
		createPostalMailingPanel.add(fileChooserButton, "gap 250, growx, wrap 20px");
		
		// buttons
		createPostalMailingButton = new JButton("Anlegen");
		cancelPostalMailingButton = new JButton("Abbrechen");
		createPostalMailingPanel.add(createPostalMailingButton, "split2");
		createPostalMailingPanel.add(cancelPostalMailingButton, "wrap");
		
		feedbackLabel = componentFactory.createLabel("");
		feedbackLabel.setFont(new Font("Headline", Font.PLAIN, 16));
		contentPanel.add(feedbackLabel);
	}

	// adds actions to the buttons
	public void init() {

		List<Filter> personFilters;
		try {
			personFilters = filterService.getAllByFilter(FilterType.PERSON).a;

			eMailingPersonFilterChooser
					.setModel(new SimpleComboBoxModel<Filter>(personFilters));
			eMailingPersonFilterChooser.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					selectedEmailFilter = (Filter) eMailingPersonFilterChooser
							.getModel().getSelectedItem();
				}
			});
			postalPersonFilterChooser.setModel(new SimpleComboBoxModel<Filter>(
					personFilters));
			postalPersonFilterChooser.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					selectedPostalFilter = (Filter) postalPersonFilterChooser
							.getModel().getSelectedItem();
				}
			});

			createEMailingButton.setAction(new CreateEMailingAction());
			createPostalMailingButton
					.setAction(new CreatePostalMailingAction());

			Action cancelAction = viewActionFactory.getMainMenuViewAction();
			cancelAction.putValue(Action.NAME, "Abbrechen");
			cancelAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backInButton.png")));
			cancelEMailingButton.setAction(cancelAction);
			cancelEMailingButton.setFont(new Font("bigger", Font.PLAIN, 13));
			cancelPostalMailingButton.setAction(cancelAction);
			cancelPostalMailingButton.setFont(new Font("bigger", Font.PLAIN, 13));
			fileChooserButton.setAction(new ChoosePostalTemplateAction());
			emailMailChimpApiButton.setAction(new ChangeMailChimpApiKeyAction());
			
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
			
		}
		
		try{
			//Load lists from MailChimp
			emailMailChimpListChooser.setModel(new SimpleComboBoxModel<MailChimpListItem>(mailChimpService.getLists()));
		}
		catch(ServiceException e){
			JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
			
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

	private final class CreateEMailingAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CreateEMailingAction() {
			super("Speichern");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Mailing mailing = new Mailing();
			int errors;
			if ((selectedEmailFilter = (Filter) eMailingPersonFilterChooser
					.getSelectedItem()) == null) {
				//JOptionPane.showMessageDialog(null,
				//		"Es muss ein Personenfilter ausgewaehlt werden!");
				JOptionPane.showMessageDialog(contentPanel, "Bitte Personenfilter ausw\u00E4hlen.", "Warnung", JOptionPane.WARNING_MESSAGE);
				
				return;
			}
			else if(emailMailChimpListChooser.getSelectedItem()==null){
			//	JOptionPane.showMessageDialog(null,
				//		"Es muss eine MailChimp Liste ausgewaehlt werden!");
				JOptionPane.showMessageDialog(contentPanel, "Bitte MailChimp-Liste ausw\u00E4hlen.", "Warnung", JOptionPane.WARNING_MESSAGE);
				return;
			}
			mailing.setFilter(selectedEmailFilter);
			mailing.setMedium(Medium.EMAIL);
			mailing.setDate(emailDatePicker.getDate());
			mailing.setType((Mailing.MailingType) eMailingTypeChooser
					.getSelectedItem());
			try {
				mailingService.insertOrUpdate(mailing);
				
				List<Person> recipients = personService.getPersonsByMailing(mailing);
				
				errors = mailChimpService.addPersonsToList(
						((MailChimpListItem)emailMailChimpListChooser.getSelectedItem()).getId(),
						personService.getPersonsByMailing(mailing));
						
				//feedbackLabel.setText("Aussendung wurde erstellt.");
				JOptionPane.showMessageDialog(contentPanel, "Aussendung erfolgreich erstellt", "Information", JOptionPane.INFORMATION_MESSAGE);
				if(errors==0){
					JOptionPane.showMessageDialog(null,
						"Es wurden "+recipients.size()+" Personen der MailChimp-Liste hinzugef\u00FCgt.");
				}
				else if(errors>0){
					JOptionPane.showMessageDialog(null,
							+errors+" fehlerhafte Datens\u00E4tze beim Hinzuf\u00FCgen zu MailChimp!");
				}
			} catch (ServiceException e1) {
				log.error(e1.getMessage() + " occured in CreateMailings");
				JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
				
			}
			
		}

	}

	private final class CreatePostalMailingAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CreatePostalMailingAction() {
			super("Speichern");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Mailing mailing = new Mailing();
			if ((selectedPostalFilter = (Filter) postalPersonFilterChooser
					.getSelectedItem()) == null) {
				//JOptionPane.showMessageDialog(null,
					//	"Es muss ein Personenfilter ausgewaehlt werden!");
				JOptionPane.showMessageDialog(contentPanel, "Bitte Personenfilter ausw\u00E4hlen.", "Warnung", JOptionPane.WARNING_MESSAGE);
				return;
			}
			mailing.setFilter(selectedPostalFilter);
			mailing.setMedium(Medium.POSTAL);
			mailing.setDate(postalDatePicker.getDate());
			mailing.setType((Mailing.MailingType) postalMailingTypeChooser
					.getSelectedItem());

			String fileName = showSaveDialog();
			
			if(fileName.equals(""))
				return;
			if(templateFile != null) {
				log.debug("Template file size "+templateFile.length());
				MailingTemplate template = new MailingTemplate();
				template.setFile(templateFile);
				template.setFileName(templateFile.getName());
				mailing.setTemplate(template);
			} else {
				log.debug("Template file was null");
			}

			try {
				mailingService.insertOrUpdate(mailing);
				//feedbackLabel.setText("Aussendung wurde erstellt.");
				JOptionPane.showMessageDialog(contentPanel, "Aussendung erfolgreich erstellt", "Information", JOptionPane.INFORMATION_MESSAGE);
				if(templateFile != null) {
					String name = "";
//					String fileName = outputNameField.getText();
//					SupportedFileFormat selectedFileFormat = (SupportedFileFormat) fileFormatBox.getSelectedItem();
					
//					if(outputNameField.getText().isEmpty()) {
//						if(selectedFileFormat == SupportedFileFormat.DOCX)
//							name = "./vorlage"+(new Date())+".docx";
//						else if(selectedFileFormat == SupportedFileFormat.PDF)
//							name = "./vorlage"+(new Date())+".pdf";
//					} else {
//						if(selectedFileFormat == SupportedFileFormat.DOCX){
//							if(!fileName.endsWith(".docx"))
//								name = "./" + fileName.concat(".docx");
//							else
//								name = "./" + fileName;
//						} else if(selectedFileFormat == SupportedFileFormat.PDF){
//							if(!fileName.endsWith(".pdf"))
//								name = "./" + fileName.concat(".pdf");
//							else
//								name = "./" + fileName;
//						}
//					}
					
					List<Person> recipients = personService.getPersonsByMailing(mailing);
					
					if(recipients.isEmpty()) {
					//	feedbackLabel.setText("Der Personenfilter enthielt keine erreichbaren Personen.");
						JOptionPane.showMessageDialog(contentPanel, "Der Personenfilter ent\u00E4lt keine adressierbaren Personen.", "Warnung", JOptionPane.WARNING_MESSAGE);
						mailingService.delete(mailing);
					} else {					
						MailingTemplateUtil.createMailingWithDocxTemplate(templateFile, personService.getPersonsByMailing(mailing), fileName);
					}
				}
			} catch (ServiceException e1) {
				log.error(e1 + " occured in CreateMailingsView");
			//	feedbackLabel.setText("Ein Fehler ist waehrend der Erstellung dieser Aussendung aufgetreten.");
				JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e1) {
				log.error(e1 + " occured in CreateMailingsView");
				//feedbackLabel.setText("Ein Fehler ist waehrend der Erstellung des PDFs aufgetreten.");
				JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	private final class ChoosePostalTemplateAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		
		public ChoosePostalTemplateAction() {
			super("Vorlage ausw\u00E4hlen");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "docx", "docx");
			
			fileChooser.setFileFilter(filter);
			
			int returnval = fileChooser.showOpenDialog(contentPanel);
			
			if(returnval == JFileChooser.APPROVE_OPTION) {
				templateFile = fileChooser.getSelectedFile();
				log.debug("Set template file");
			}	
		}
	}
	
	private final class ChangeMailChimpApiKeyAction extends AbstractAction{

		private static final long serialVersionUID = 1L;
		
		public ChangeMailChimpApiKeyAction(){
			super("Schl\u00FCssel \u00E4ndern");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String newKey = JOptionPane.showInputDialog("Bitte geben Sie einen g\u00FCltigen Schl\u00FCssel ein");
			if(newKey!=null&&!newKey.isEmpty()){
				try {
					mailChimpService.setAPIKey(newKey);
					//feedbackLabel.setText("Der eingegebene MailChimp API Key wurde gespeichert");
					JOptionPane.showMessageDialog(contentPanel, "Schl\u00FCssel gespeichert.", "Information", JOptionPane.INFORMATION_MESSAGE);
					//Reaload lists from MailChimp
					emailMailChimpListChooser.setModel(new SimpleComboBoxModel<MailChimpListItem>(mailChimpService.getLists()));
				} catch (ServiceException e1) {
					log.debug("MailChimp API Key was invalid");
				//	feedbackLabel.setText("Der eingegebene MailChimp API Key ist ungueltig");
					JOptionPane.showMessageDialog(contentPanel, "Der angegebene Schl\u00FCssel ist ung\u00FCltig.", "Warnung", JOptionPane.WARNING_MESSAGE);
				}
			}
			
		}
		
	}
}