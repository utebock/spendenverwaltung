package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.StringTextField;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing.Medium;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.MailingTemplateUtil;
import at.fraubock.spendenverwaltung.util.MailChimp;
import at.fraubock.spendenverwaltung.util.MailChimp.MailChimpListItem;

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
	private JXDatePicker emailDatePicker;
	private JXDatePicker postalDatePicker;

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
			cancelEMailingButton, cancelPostalMailingButton;
	JSeparator separator;
	
	StringTextField outputNameField;

	JComboBox<Filter> eMailingPersonFilterChooser, postalPersonFilterChooser;
	JComboBox<Mailing.MailingType> eMailingTypeChooser,
			postalMailingTypeChooser;
	JComboBox<MailChimpListItem> emailMailChimpListChooser;
	

	public CreateMailingsView(ViewActionFactory viewActionFactory,
			ComponentFactory componentFactory, IMailingService mailingService,
			IFilterService filterService, IPersonService personService) {
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.mailingService = mailingService;
		this.filterService = filterService;
		this.personService = personService;
		setUpLayout();
	}

	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(800, 800);
		createEMailingPanel = componentFactory.createPanel(800, 350);
		createPostalMailingPanel = componentFactory.createPanel(800, 350);

		this.add(contentPanel);

		contentPanel.add(createEMailingPanel, "wrap");
		// end of emailing panel, add separator
		contentPanel.add(componentFactory.createSeparator(), "wrap, growx");
		contentPanel.add(createPostalMailingPanel, "wrap");
		// end of mailing panel, add separator
		contentPanel.add(componentFactory.createSeparator(), "wrap, growx");

		// set title label
		emailTitle = componentFactory
				.createLabel("Neue Email Aussendung Erstellen");
		emailTitle.setFont(new Font("Headline", Font.PLAIN, 16));
		createEMailingPanel.add(emailTitle, "gapbottom 50, wrap");

		// filter combobox and label
		emailFilterLabel = componentFactory
				.createLabel("Personenfilter Auswählen");
		eMailingPersonFilterChooser = new JComboBox<Filter>();
		createEMailingPanel.add(emailFilterLabel);
		createEMailingPanel.add(eMailingPersonFilterChooser, "wrap");

		// type combobox and label
		emailTypeLabel = componentFactory
				.createLabel("Aussendungstyp Auswählen");
		eMailingTypeChooser = new JComboBox<Mailing.MailingType>(
				Mailing.MailingType.values());
		createEMailingPanel.add(emailTypeLabel);
		createEMailingPanel.add(eMailingTypeChooser, "wrap");

		emailDateLabel = componentFactory.createLabel("Datum");
		emailDatePicker = new JXDatePicker(new java.util.Date());
		createEMailingPanel.add(emailDateLabel);
		createEMailingPanel.add(emailDatePicker, "wrap");
		
		//MailChimp listChooser
		emailMailChimpLabel = componentFactory.createLabel("MailChimp List");
		emailMailChimpListChooser = new JComboBox<MailChimpListItem>();
		createEMailingPanel.add(emailMailChimpLabel);
		createEMailingPanel.add(emailMailChimpListChooser, "wrap");
		

		// buttons
		createEMailingButton = new JButton("Anlegen");
		cancelEMailingButton = new JButton("Abbrechen");
		createEMailingPanel.add(cancelEMailingButton, "split 2");
		createEMailingPanel.add(createEMailingButton, "wrap");

		// set up postal mailing panel layout
		postalTitle = componentFactory
				.createLabel("Neue Postalische Aussendung Erstellen");
		postalTitle.setFont(new Font("Headline", Font.PLAIN, 16));
		createPostalMailingPanel.add(postalTitle, "gapbottom 50,wrap");

		// filter combobox and label
		postalFilterLabel = componentFactory
				.createLabel("Personenfilter Auswählen");
		postalPersonFilterChooser = new JComboBox<Filter>();
		createPostalMailingPanel.add(postalFilterLabel);
		createPostalMailingPanel.add(postalPersonFilterChooser, "wrap");

		// type combobox and label
		postalTypeLabel = componentFactory
				.createLabel("Aussendungstyp Auswählen");
		postalMailingTypeChooser = new JComboBox<Mailing.MailingType>(
				Mailing.MailingType.values());
		createPostalMailingPanel.add(postalTypeLabel);
		createPostalMailingPanel.add(postalMailingTypeChooser, "wrap");

		postalDateLabel = componentFactory.createLabel("Datum");
		postalDatePicker = new JXDatePicker(new java.util.Date());
		createPostalMailingPanel.add(postalDateLabel);
		createPostalMailingPanel.add(postalDatePicker, "wrap");
		
		outputNameLabel = componentFactory.createLabel("PDF Name");
		outputNameField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		createPostalMailingPanel.add(outputNameLabel);
		createPostalMailingPanel.add(outputNameField, "wrap");
		
		fileChooserButton = new JButton("Auswählen");
		createPostalMailingPanel.add(fileChooserButton, "wrap");
		
		// buttons
		createPostalMailingButton = new JButton("Anlegen");
		cancelPostalMailingButton = new JButton("Abbrechen");
		createPostalMailingPanel.add(cancelPostalMailingButton, "split 2");
		createPostalMailingPanel.add(createPostalMailingButton, "wrap");
		
		feedbackLabel = componentFactory.createLabel("");
		feedbackLabel.setFont(new Font("Headline", Font.PLAIN, 16));
		contentPanel.add(feedbackLabel);
		



//		// reproduce mailed documents
//		reproduceTitle = componentFactory
//				.createLabel("Postalische Aussendungen wiederherstellen");
//		reproduceTitle.setFont(new Font("Headline", Font.PLAIN, 16));
//		createReproducePanel.add(reproduceTitle, "gapbottom 30,wrap");
//
//		List<Mailing> mailings = null;
//		try {
//			mailings = mailingService.getAll();
//		} catch (ServiceException e) {
//			JOptionPane.showMessageDialog(null,
//					"Ein Fehler trat beim Laden der Aussendungen auf.");
//			log.error("Error retrieving mailings: " + e.getMessage());
//		}
//
//		List<PostalMailingPicker> postalMailings = new ArrayList<PostalMailingPicker>();
//		for (Mailing m : mailings) {
//			if (m.getMedium() == Mailing.Medium.POSTAL) {
//				postalMailings.add(new PostalMailingPicker(m));
//			}
//		}
//
//		final JComboBox<PostalMailingPicker> sentMailingsDD = new JComboBox<PostalMailingPicker>(
//				new SimpleComboBoxModel<PostalMailingPicker>(postalMailings));
//
//		createReproducePanel.add(sentMailingsDD, "wrap");
//
//		JButton reproduceDocument = new JButton();
//		reproduceDocument.setAction(new AbstractAction(
//				"Aussendung wiederherstellen") {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				PostalMailingPicker mailingChoice = (PostalMailingPicker) sentMailingsDD
//						.getSelectedItem();
//
//				if (mailingChoice == null) {
//					JOptionPane.showMessageDialog(CreateMailingsView.this,
//							"Bitte w�hlen Sie eine Aussendung aus.", "Info",
//							JOptionPane.INFORMATION_MESSAGE);
//				}
//				
//				Mailing mailing = mailingChoice.getMailing();
//
//
//				try {
//					mailingService.reproduceDocument(mailing);
//				} catch (ServiceException e) {
//					JOptionPane
//							.showMessageDialog(
//									CreateMailingsView.this,
//									"Die Aussendung konnte nicht wiederhergestellt werden.",
//									"Error", JOptionPane.ERROR_MESSAGE);
//					log.error("Mailing could not be reproduced.mailing_id='"
//							+ mailing.getId() + "', error was: "
//							+ e.getMessage());
//				}
//			}
//
//		});
//		createReproducePanel.add(reproduceDocument, "wrap, gaptop 10");

	}

	// adds actions to the buttons
	public void init() {

		List<Filter> personFilters;
		try {
			personFilters = filterService.getAllByFilter(FilterType.PERSON);

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
			cancelEMailingButton.setAction(cancelAction);
			cancelPostalMailingButton.setAction(cancelAction);
			fileChooserButton.setAction(new ChoosePostalTemplateAction());
			
		} catch (ServiceException e) {
			JOptionPane
					.showMessageDialog(null,
							"Ein Fehler trat beim Initialisieren der Personenfilter auf.");
		}
		
		try{
			//Load lists from MailChimp
			emailMailChimpListChooser.setModel(new SimpleComboBoxModel<MailChimpListItem>(MailChimp.getLists()));
		}
		catch(ServiceException e){
			JOptionPane.showMessageDialog(null, "Ein Fehler trat bei der Kommunikation mit MailChimp auf");
		}
		
	}

	private final class CreateEMailingAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CreateEMailingAction() {
			super("EMail Aussendung anlegen");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Mailing mailing = new Mailing();
			int errors;
			if ((selectedEmailFilter = (Filter) eMailingPersonFilterChooser
					.getSelectedItem()) == null) {
				JOptionPane.showMessageDialog(null,
						"Es muss ein Personenfilter ausgewählt werden!");
				return;
			}
			else if(emailMailChimpListChooser.getSelectedItem()==null){
				JOptionPane.showMessageDialog(null,
						"Es muss eine MailChimp Liste ausgewählt werden!");
				return;
			}
			mailing.setFilter(selectedEmailFilter);
			mailing.setMedium(Medium.EMAIL);
			mailing.setDate(emailDatePicker.getDate());
			mailing.setType((Mailing.MailingType) eMailingTypeChooser
					.getSelectedItem());
			try {
				mailingService.insertOrUpdate(mailing);
				errors = mailingService.exportEMailsToMailChimp(mailing, 
						((MailChimpListItem)emailMailChimpListChooser.getSelectedItem()).getId());
				feedbackLabel.setText("Aussendung wurde erstellt.");
				if(errors==0){
					JOptionPane.showMessageDialog(null,
						"Es wurden alle ausgewählten Personen der MailChimp Liste hinzugefügt!");
				}
				else if(errors>0){
					JOptionPane.showMessageDialog(null,
							"Es gab "+errors+" fehlerhafte Datensätze beim Hinzufügen zu MailChimp!");
				}
			} catch (ServiceException e1) {
				log.error(e1.getMessage() + " occured in CreateMailings");
				feedbackLabel.setText("Ein Fehler ist während der Erstellung dieser Aussendung aufgetreten.");
			}
			
		}

	}

	private final class CreatePostalMailingAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CreatePostalMailingAction() {
			super("Postalische Aussendung anlegen");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Mailing mailing = new Mailing();
			if ((selectedPostalFilter = (Filter) postalPersonFilterChooser
					.getSelectedItem()) == null) {
				JOptionPane.showMessageDialog(null,
						"Es muss ein Personenfilter ausgewählt werden!");
				return;
			}
			mailing.setFilter(selectedPostalFilter);
			mailing.setMedium(Medium.POSTAL);
			mailing.setDate(postalDatePicker.getDate());
			mailing.setType((Mailing.MailingType) postalMailingTypeChooser
					.getSelectedItem());
			

			try {
				mailingService.insertOrUpdate(mailing);
				feedbackLabel.setText("Aussendung wurde erstellt.");
				
				if(templateFile != null) {
					String name;
					if(outputNameField.getText().isEmpty()) {
						name = "./vorlage"+(new Date())+".pdf";
					} else {
						name = "./"+outputNameField.getText()+".pdf";
					}
					
					List<Person> recipients = personService.getPersonsByMailing(mailing);
					
					if(recipients.isEmpty()) {
						feedbackLabel.setText("Der Personenfilter enthielt keine erreichbaren Personen.");
						mailingService.delete(mailing);
					} else {					
						MailingTemplateUtil.createMailingWithDocxTemplate(templateFile, personService.getPersonsByMailing(mailing), name);
					}
				}
			} catch (ServiceException e1) {
				log.error(e1 + " occured in CreateMailingsView");
				feedbackLabel.setText("Ein Fehler ist während der Erstellung dieser Aussendung aufgetreten.");
			} catch (IOException e1) {
				log.error(e1 + " occured in CreateMailingsView");
				feedbackLabel.setText("Ein Fehler ist während der Erstellung des PDFs aufgetreten.");
			}
		}

	}

	private final class ChoosePostalTemplateAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		
		public ChoosePostalTemplateAction() {
			super("Vorlage auswählen");
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
//	private class PostalMailingPicker {
//
//		private Mailing mailing;
//
//		public PostalMailingPicker(Mailing mailing) {
//			this.mailing = mailing;
//		}
//
//		@Override
//		public String toString() {
//			String res = "";
//			res += new SimpleDateFormat("dd.MM.yyyy").format(mailing.getDate())
//					+ " - ";
//			res += mailing.getType() + " - ";
//			res += mailing.getTemplate().getFileName();
//
//			return res;
//		}
//
//		public Mailing getMailing() {
//			return mailing;
//		}
//	}

}
