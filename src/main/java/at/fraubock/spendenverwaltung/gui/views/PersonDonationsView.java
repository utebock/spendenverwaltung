package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

import at.fraubock.spendenverwaltung.gui.DonationTableModel;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.NumericTextField;
import at.fraubock.spendenverwaltung.gui.components.PersonTableModel;
import at.fraubock.spendenverwaltung.gui.components.StringTextField;
import at.fraubock.spendenverwaltung.interfaces.domain.Confirmation;
import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing.Medium;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IConfirmationService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.util.ConfirmationTemplateUtil;
import at.fraubock.spendenverwaltung.util.MailingTemplateUtil;

public class PersonDonationsView extends InitializableView {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(PersonDonationsView.class);
	
	private ComponentFactory componentFactory;
	private ViewActionFactory viewActionFactory;
	
	private JPanel contentPanel;
	
	//show feedback messages on the bottom of the view with this label
	private JLabel feedbackLabel, donationTotalLabel;
	
	private JToolBar toolbar;
	private JTable donationsTable;
	private JButton addDonation, editDonation, confirmDonation, confirmAllDonations, deleteDonation,
		backButton;
	
	private IDonationService donationService;
	
	//needed for confirmation document creation
	private IAddressService addressService;
	
	private IConfirmationService confirmationService;
	
	//datepicker for quick search
	private JXDatePicker start, end;
	
	//used for total field
	private long currentTotal = 0;
	
	//selected in parent view
	private Person selectedPerson;
	//keep state of parent component tablemodel until view switch
	private PersonTableModel personTableModel;
	
	private DonationTableModel donationTableModel;
	
	private File confirmationTemplateFile;
	JXDatePicker confirmationPicker;
	private JLabel dateLabel;
	
	public PersonDonationsView (ViewActionFactory viewActionFactory, ComponentFactory componentFactory,
			IDonationService donationService, IConfirmationService confirmationService, IAddressService addressService, Person selectedPerson, PersonTableModel personTableModel) {
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.addressService = addressService;
		this.donationService = donationService;
		this.confirmationService = confirmationService;
		this.selectedPerson = selectedPerson;
		this.personTableModel = personTableModel;
		
		setUpLayout();
	}
	
	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(800, 800);
		
		this.add(contentPanel);
		start = new JXDatePicker();
		start.addActionListener(new StartDateSelectedListener());
		end = new JXDatePicker();
		end.addActionListener(new EndDateSelectedListener());
		
		toolbar = new JToolBar();
		contentPanel.add(toolbar, "spanx, wrap, growx");
		
		dateLabel = componentFactory.createLabel("Datum: ");
		contentPanel.add(dateLabel, "split3");
		
		contentPanel.add(start, "split 2");
		contentPanel.add(end, "wrap 20px");
		
		donationTotalLabel = componentFactory.createLabel("");
		donationTotalLabel.setFont(new Font("Headline", Font.PLAIN, 14));
		contentPanel.add(donationTotalLabel, "wrap 20px");
		
		donationTableModel = new DonationTableModel();
		donationsTable = new JTable(donationTableModel);
		donationsTable.setAutoCreateRowSorter(true);
		donationsTable.setFillsViewportHeight(true);
		donationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(donationsTable);
		scrollPane.setPreferredSize(new Dimension(750, 450));
		
		contentPanel.add(scrollPane, "wrap");
		
		feedbackLabel = componentFactory.createLabel("");
		feedbackLabel.setFont(new Font("Headline", Font.PLAIN, 13));
		contentPanel.add(feedbackLabel, "wrap");
	}
	
	@Override
	public void init() {
		initTable();
		addComponentsToToolbar(toolbar);
	}
	
	private void setTotalLabel() {
		double sum = currentTotal/100;
		donationTotalLabel.setText("Spendensumme: "+sum+" \u20ac");
	}
	
	public void initTable() {
		if(selectedPerson != null) {
			if(selectedPerson.getMainAddress() != null) {
				donationTotalLabel.setText(selectedPerson.getMainAddress().toString());
			}
		} else {
			//feedbackLabel.setText("Es wurde keine Person ausgew\u00E4hlt");
			JOptionPane.showMessageDialog(contentPanel, "Es wurde keine Person ausgew\u00E4hlt.", "Information", JOptionPane.INFORMATION_MESSAGE);
		}
		
		try {
			List<Donation> donationList = donationService.getByPerson(selectedPerson);
								
			for(Donation entry : donationList) {
				currentTotal += entry.getAmount();
				donationTableModel.addDonation(entry);
			}

			setTotalLabel();
		} catch (ServiceException e) {
			log.warn(e.getLocalizedMessage());
			//feedbackLabel.setText("Es passierte ein Fehler beim Laden der Spenden.");
			JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void addComponentsToToolbar(JToolBar toolbar) {
		
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		
		backButton = new JButton();
		
		Action getBack = viewActionFactory.getFindPersonsViewAction(personTableModel);
		getBack.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backButton.png")));
		backButton.setAction(getBack);
		
		addDonation = new JButton();
		addDonation.setFont(new Font("Bigger", Font.PLAIN, 13));
		AddAction addAction = new AddAction();
		addDonation.setAction(addAction);

		editDonation = new JButton();
		editDonation.setFont(new Font("Bigger", Font.PLAIN, 13));
		EditAction editAction = new EditAction();
		editDonation.setAction(editAction);

		deleteDonation = new JButton();
		deleteDonation.setFont(new Font("Bigger", Font.PLAIN, 13));
		DeleteAction deleteAction = new DeleteAction();
		deleteDonation.setAction(deleteAction);
		
		confirmDonation = new JButton();
		confirmDonation.setFont(new Font("Bigger", Font.PLAIN, 13));
		ConfirmAction confirmAction = new ConfirmAction();
		confirmDonation.setAction(confirmAction);
		
		confirmAllDonations = new JButton();
		confirmAllDonations.setFont(new Font("Bigger", Font.PLAIN, 13));
		ConfirmAllAction confirmAllAction = new ConfirmAllAction();
		confirmAllDonations.setAction(confirmAllAction);
		
		toolbar.add(backButton, "split 6");
		toolbar.add(addDonation);
		toolbar.add(editDonation);
		toolbar.add(deleteDonation);
		toolbar.add(confirmDonation);
		toolbar.add(confirmAllDonations);
			
	}
	
	private final class StartDateSelectedListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			feedbackLabel.setText("");

			Date startDate = start.getDate();
			Date endDate = end.getDate();
			
			try {
				List<Donation> donations = donationService.getByPerson(selectedPerson);
			
				if(startDate != null && endDate != null) {
					if(startDate.after(endDate)) {
						//feedbackLabel.setText("Ungueltiger Datumsbereich");
						JOptionPane.showMessageDialog(contentPanel, "Ung\u00FCltiger Datumsbereich.", "Warnung", JOptionPane.WARNING_MESSAGE);
					} else {
						donationTableModel.clear();
						for(Donation entry : donations) {
							if(entry.getDate().before(endDate) && entry.getDate().after(startDate)) {
								donationTableModel.addDonation(entry);
							}
						}
					}
				} else if (startDate != null) {
					
					donationTableModel.clear();
					for(Donation entry : donations) {
						if(entry.getDate().after(startDate)) {
							donationTableModel.addDonation(entry);
						}
					}
				} else {
					//feedbackLabel.setText("Es wurde kein Datum ausgewaehlt");
					JOptionPane.showMessageDialog(contentPanel, "Es wurde kein Datum ausgewaehlt.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (ServiceException e2) {
				log.warn(e2.getLocalizedMessage());
			//	feedbackLabel.setText("Es passierte ein Fehler beim Initialisieren der Tabelle.");
				JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private final class EndDateSelectedListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			feedbackLabel.setText("");

			Date startDate = start.getDate();
			Date endDate = end.getDate();
			
			try {
				List<Donation> donations = donationService.getByPerson(selectedPerson);
			
				if(startDate != null && endDate != null) {
					if(startDate.after(endDate)) {
						//feedbackLabel.setText("Ungueltiger Datumsbereich");
						JOptionPane.showMessageDialog(contentPanel, "Ung\u00FCltiger Datumsbereich", "Warnung", JOptionPane.WARNING_MESSAGE);
					} else {
						donationTableModel.clear();
						for(Donation entry : donations) {
							if(entry.getDate().before(endDate) && entry.getDate().after(startDate)) {
								donationTableModel.addDonation(entry);
							}
						}
					}
				} else if (endDate != null) {
					donationTableModel.clear();
					for(Donation entry : donations) {
						if(entry.getDate().before(endDate)) {
							donationTableModel.addDonation(entry);
						}
					}
				}
			} catch (ServiceException e2) {
				log.warn(e2.getLocalizedMessage());
				//feedbackLabel.setText("Es passierte ein Fehler beim Initialisieren der Tabelle.");
				JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private final class AddAction extends AbstractAction {
		
		private JDialog addDonationDialog;
		private JPanel addDonationPanel;
		
		private JLabel donationTypeLabel;
		private JComboBox<Donation.DonationType> donationTypeCombo;
		
		private JLabel donationAmountLabel;
		private NumericTextField donationAmountField;

		private JLabel dedicationLabel;
		private StringTextField dedicationField;
		
		private JLabel noteLabel;
		private StringTextField noteField;
		
		private JLabel dateLabel;
		private JXDatePicker donationDatePicker;
		
		private JLabel validationFeedbackLabel;
		
		private JButton submitButton;
		private JButton cancelButton;
		
		private static final long serialVersionUID = 1L;
		
		public AddAction() {
			super("Neue Spende anlegen");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			feedbackLabel.setText("");

			addDonationDialog = new JDialog(SwingUtilities.getWindowAncestor(contentPanel), Dialog.ModalityType.APPLICATION_MODAL);
			addDonationDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			addDonationPanel = componentFactory.createPanel(350, 250);

			//add combobox + label
			donationTypeLabel = componentFactory.createLabel("Spendentyp: ");
			donationTypeCombo = new JComboBox<Donation.DonationType>(new SimpleComboBoxModel<>(Donation.DonationType.values()));
			
			addDonationPanel.add(donationTypeLabel, "split 2");
			addDonationPanel.add(donationTypeCombo, "wrap");
			
			dateLabel = componentFactory.createLabel("Datum: ");
			donationDatePicker = new JXDatePicker(new Date());
			addDonationPanel.add(dateLabel, "split 2");
			addDonationPanel.add(donationDatePicker, "wrap");
			
			//add amount field + label
			donationAmountLabel = componentFactory.createLabel("Spendenbetrag: ");
			donationAmountField = new NumericTextField(ComponentConstants.SHORT_TEXT, false);
			addDonationPanel.add(donationAmountLabel, "split 2");
			addDonationPanel.add(donationAmountField, "wrap");
			
			dedicationLabel = componentFactory.createLabel("Widmung: ");
			dedicationField = new StringTextField(ComponentConstants.LONG_TEXT);
			addDonationPanel.add(dedicationLabel, "split 2");
			addDonationPanel.add(dedicationField, "wrap");
			
			noteLabel = componentFactory.createLabel("Notiz: ");
			noteField = new StringTextField(ComponentConstants.LONG_TEXT);
			addDonationPanel.add(noteLabel, "split 2");
			addDonationPanel.add(noteField, "wrap 20px");
			
			submitButton = new JButton(new SubmitAddAction());
			cancelButton = new JButton(new CancelAddAction());		
			
			addDonationPanel.add(submitButton, "split 2");
			addDonationPanel.add(cancelButton, "wrap");
			
			validationFeedbackLabel = componentFactory.createLabel("");
			addDonationPanel.add(validationFeedbackLabel, "wrap");
			addDonationDialog.add(addDonationPanel);
			addDonationDialog.pack();
			addDonationDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(contentPanel));
			addDonationDialog.setVisible(true);
		}	
		
		private final class SubmitAddAction extends AbstractAction {

			private static final long serialVersionUID = 1L;

			public SubmitAddAction() {
				super("Speichern");
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Donation donation = new Donation();
				donation.setDonator(selectedPerson);
				
				boolean validation = true;
				
				if(donationTypeCombo.getModel().getSelectedItem() != null) { 
					donation.setType((Donation.DonationType)donationTypeCombo.getModel().getSelectedItem());
				} else {
					validation = false;
					//validationFeedbackLabel.setText("Spendenart muss ausgewaehlt werden!");
					JOptionPane.showMessageDialog(contentPanel, "Bitte Spendentyp w\u00E4hlen.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				
				if(donationAmountField.validateContents()) {
					donation.setAmount(donationAmountField.getHundredths());
				} else {
					validation = false;
					//validationFeedbackLabel.setText("Spendenhoehe muss ausgefuellt werden!");
					JOptionPane.showMessageDialog(contentPanel, "Bitte Betrag angeben.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				
				if(dedicationField.validateContents()) {
					if(!dedicationField.getText().equals(""))
						donation.setDedication(dedicationField.getText());
				} else {
					validation = false;
					//validationFeedbackLabel.setText("Spendenwidmung konnte nicht validiert werden.");
					JOptionPane.showMessageDialog(contentPanel, "Widmung konnte nicht validiert werden.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				
				if(noteField.validateContents()) {
					if(!noteField.getText().equals(""))
						donation.setNote(noteField.getText());
				} else {
					validation = false;
					//validationFeedbackLabel.setText("Spendennotiz konnte nicht validiert werden.");
					JOptionPane.showMessageDialog(contentPanel, "Notiz konnte nicht validiert werden.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				
				donation.setDate(donationDatePicker.getDate());
				
				if(validation) {
					try {
						donationService.create(donation);
						donationTableModel.addDonation(donation);
						//feedbackLabel.setText("Spende wurde erstellt.");
						JOptionPane.showMessageDialog(contentPanel, "Spende erfolgreich hinzugef\u00FCgt.", "Information", JOptionPane.INFORMATION_MESSAGE);
						//update total label
						currentTotal += donation.getAmount();
						setTotalLabel();
						addDonationDialog.dispose();						
					} catch (ServiceException e1) {
						log.warn(e1.getLocalizedMessage());
						//feedbackLabel.setText("Es passierte ein Fehler beim erstellen der Spende.");
						JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		
		private final class	CancelAddAction extends AbstractAction {

			private static final long serialVersionUID = 1L;

			public CancelAddAction() {
				super("Abbrechen");
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addDonationDialog.dispose();
			}
			
		}
	}
	
	private final class EditAction extends AbstractAction {
		
		private JDialog editDonationDialog;
		private JPanel editDonationPanel;
		
		private JLabel donationTypeLabel;
		private JComboBox<Donation.DonationType> donationTypeCombo;
		
		private JLabel donationAmountLabel;
		private NumericTextField donationAmountField;

		private JLabel dedicationLabel;
		private StringTextField dedicationField;
		
		private JLabel noteLabel;
		private StringTextField noteField;
		
		private JLabel dateLabel;
		private JXDatePicker donationDatePicker;
		
		private JLabel validationFeedbackLabel;
		
		private JButton submitButton;
		private JButton cancelButton;
		
		private int selectedRow;
		
		private Donation selectedDonation;
		
		private static final long serialVersionUID = 1L;
		
		public EditAction() {
			super("Spende bearbeiten");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			feedbackLabel.setText("");
			
			selectedRow = donationsTable.getSelectedRow();
			if(selectedRow == -1) {
				//feedbackLabel.setText("Eine Spende muss zum bearbeiten ausgewaehlt werden.");
				JOptionPane.showMessageDialog(contentPanel, "Bitte Spende ausw\u00E4hlen.", "Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			selectedDonation = donationTableModel.getDonationRow(selectedRow);
					
			editDonationDialog = new JDialog(SwingUtilities.getWindowAncestor(contentPanel), Dialog.ModalityType.APPLICATION_MODAL);
			editDonationDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			editDonationPanel = componentFactory.createPanel(350, 250);
			
			//add combobox + label
			donationTypeLabel = componentFactory.createLabel("Spendentyp: ");
			donationTypeCombo = new JComboBox<Donation.DonationType>(new SimpleComboBoxModel<>(Donation.DonationType.values()));
			donationTypeCombo.setSelectedItem(selectedDonation.getType());
			
			editDonationPanel.add(donationTypeLabel, "split 2");
			editDonationPanel.add(donationTypeCombo, "wrap, growx");
			
			dateLabel = componentFactory.createLabel("Datum: ");
			donationDatePicker = new JXDatePicker();
			donationDatePicker.setDate(selectedDonation.getDate());
			editDonationPanel.add(dateLabel, "split 2");
			editDonationPanel.add(donationDatePicker, "wrap, growx");
			
			//add amount field + label
			donationAmountLabel = componentFactory.createLabel("Spendenbetrag: ");
			donationAmountField = new NumericTextField(ComponentConstants.SHORT_TEXT, false);
			currentTotal -= selectedDonation.getAmount();
			donationAmountField.setNumericValue(selectedDonation.getAmount());
			editDonationPanel.add(donationAmountLabel, "split 2");
			editDonationPanel.add(donationAmountField, "wrap, growx");
			
			dedicationLabel = componentFactory.createLabel("Widmung: ");
			dedicationField = new StringTextField(ComponentConstants.LONG_TEXT);
			if(selectedDonation.getDedication() != null) {
				dedicationField.setText(selectedDonation.getDedication());
			}
			editDonationPanel.add(dedicationLabel, "split 2");
			editDonationPanel.add(dedicationField, "wrap, growx");
			
			noteLabel = componentFactory.createLabel("Notiz: ");
			noteField = new StringTextField(ComponentConstants.LONG_TEXT);
			if(selectedDonation.getNote() != null) {
				noteField.setText(selectedDonation.getNote());
			}
			editDonationPanel.add(noteLabel, "split 2");
			editDonationPanel.add(noteField, "wrap, growx");
			
			submitButton = new JButton(new SubmitEditAction());
			cancelButton = new JButton(new CancelEditAction());		
			
			editDonationPanel.add(submitButton, "split2");
			editDonationPanel.add(cancelButton, "wrap");
				
			validationFeedbackLabel = componentFactory.createLabel("");

			editDonationPanel.add(validationFeedbackLabel, "wrap");
			editDonationDialog.add(editDonationPanel);
			editDonationDialog.pack();
			editDonationDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(contentPanel));
			editDonationDialog.setVisible(true);
		}	
		
		private final class SubmitEditAction extends AbstractAction {

			private static final long serialVersionUID = 1L;
			
			public SubmitEditAction() {
				super("Speichern");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				Donation donation = selectedDonation;
				donation.setDonator(selectedPerson);
				
				boolean validation = true;
				
				if(donationTypeCombo.getModel().getSelectedItem() != null) { 
					donation.setType((Donation.DonationType)donationTypeCombo.getModel().getSelectedItem());
				} else {
					validation = false;
					//validationFeedbackLabel.setText("Spendenart muss ausgewaehlt werden!");
					JOptionPane.showMessageDialog(contentPanel, "Bitte Spendentyp w\u00E4hlen.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				
				if(donationAmountField.validateContents()) {
					donation.setAmount(donationAmountField.getHundredths());
				} else {
					validation = false;
					//validationFeedbackLabel.setText("Spendenhoehe muss ausgefuellt werden!");
					JOptionPane.showMessageDialog(contentPanel, "Bitte Betrag angebe.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				
				if(dedicationField.validateContents()) {
					if(!dedicationField.getText().equals(""))
						donation.setDedication(dedicationField.getText());
				} else {
					validation = false;
					//validationFeedbackLabel.setText("Spendenwidmung konnte nicht validiert werden.");
					JOptionPane.showMessageDialog(contentPanel, "Widmung konnte nicht validiert werden.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				
				if(noteField.validateContents()) {
					if(!noteField.getText().equals(""))
						donation.setNote(noteField.getText());
				} else {
					validation = false;
					//validationFeedbackLabel.setText("Spendennotiz konnte nicht validiert werden.");
					JOptionPane.showMessageDialog(contentPanel, "Notiz konnte nicht validiert werden.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				
				donation.setDate(donationDatePicker.getDate());
				
				if(validation) {
					try {
						donationService.update(donation);
						donationTableModel.removeDonation(selectedRow);
						donationTableModel.addDonation(donation);
						//feedbackLabel.setText("Spende wurde geaendert.");
						JOptionPane.showMessageDialog(contentPanel, "Spende erfolgreich ge\u00E4ndert.", "Information", JOptionPane.INFORMATION_MESSAGE);
						//update total label
						currentTotal += donation.getAmount();
						setTotalLabel();
						editDonationDialog.dispose();						
					} catch (ServiceException e1) {
						log.warn(e1.getLocalizedMessage());
						//feedbackLabel.setText("Es passierte ein Fehler beim bearbeiten der Spende.");
						JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		
		private final class	CancelEditAction extends AbstractAction {

			private static final long serialVersionUID = 1L;
			
			public CancelEditAction() {
				super("Abbrechen");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				//was subtracted earlier
				currentTotal += selectedDonation.getAmount();
				editDonationDialog.dispose();
			}
			
		}
	}
	
	private final class DeleteAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;
		
		public DeleteAction() {
			super("Spende l\u00F6schen");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			feedbackLabel.setText("");
			
			int selectedRow = donationsTable.getSelectedRow();
			if(selectedRow == -1) {
			//	feedbackLabel.setText("Eine Spende muss zum bearbeiten ausgewaehlt werden.");
				JOptionPane.showMessageDialog(contentPanel, "Bitte Spende ausw\u00E4hlen.", "Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			Donation donation = donationTableModel.getDonationRow(selectedRow);

			int dialogResult = JOptionPane.showConfirmDialog (contentPanel, "Wollen Sie diese Spende wirklich l\u00F6schen?", "L\u00F6schen", JOptionPane.YES_NO_OPTION);
		
			if(dialogResult == JOptionPane.YES_OPTION) {
				try {
					donationService.delete(donation);
					currentTotal -= donation.getAmount();
					donationTableModel.removeDonation(selectedRow);
					//feedbackLabel.setText("Spende wurde geloescht.");
					JOptionPane.showMessageDialog(contentPanel, "Spende erfolgreich gel\u00F6scht.", "Information", JOptionPane.INFORMATION_MESSAGE);
					setTotalLabel();
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					//feedbackLabel.setText("Spende konnte nicht geloescht werden.");
					JOptionPane.showMessageDialog(contentPanel, "Spende konnte nicht gel\u00F6scht werden.", "Warnung", JOptionPane.WARNING_MESSAGE);
				}
			}
		}	
	}
	
	private final class ConfirmAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;

		JDialog donationConfirmationDialog;
		JPanel donationConfirmationPanel;
		JLabel donationHeadLabel;
		JLabel templateLabel;
		JButton templateButton;
		JButton saveButton;
		JButton cancelButton;
		Donation donation;
		JLabel confirmationLabel;
				
		public ConfirmAction() {
			super("Spendenbest\u00E4tigung");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			feedbackLabel.setText("");
			
			int selectedRow = donationsTable.getSelectedRow();
			if(selectedRow == -1) {
				//feedbackLabel.setText("Eine Spende muss zum bearbeiten ausgewaehlt werden.");
				JOptionPane.showMessageDialog(contentPanel, "Bitte Spende ausw\u00E4hlen.", "Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			donation = donationTableModel.getDonationRow(selectedRow);
			
			donationConfirmationDialog = new JDialog(SwingUtilities.getWindowAncestor(contentPanel), Dialog.ModalityType.APPLICATION_MODAL);
			donationConfirmationDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			donationConfirmationPanel = componentFactory.createPanel(300, 250);

			//add headline
			donationHeadLabel = componentFactory.createLabel("Spendenbest\u00E4tigung erstellen");
			donationConfirmationPanel.add(donationHeadLabel, "wrap, span 2");
			
			//add template
			templateLabel = componentFactory.createLabel("Vorlage");
			donationConfirmationPanel.add(templateLabel);
			
			templateButton = new JButton(new ChooseDonationConfirmationTemplate());
			donationConfirmationPanel.add(templateButton, "wrap");
			
			//add date
			confirmationLabel = componentFactory.createLabel("Datum");
			donationConfirmationPanel.add(confirmationLabel);
			
			confirmationPicker = new JXDatePicker(new java.util.Date());
			donationConfirmationPanel.add(confirmationPicker, "wrap");
			
			//add save + cancel
			saveButton = new JButton(new CreateConfirmationAction(donation));
			donationConfirmationPanel.add(saveButton, "wrap");
			
			cancelButton = new JButton(new CancelDonationConfirmation(donationConfirmationDialog));
			donationConfirmationPanel.add(cancelButton, "wrap");
			
			donationConfirmationDialog.add(donationConfirmationPanel);
			donationConfirmationDialog.pack();
			donationConfirmationDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(contentPanel));
			donationConfirmationDialog.setVisible(true);
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

	private final class CreateConfirmationAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private Donation donation;

		public CreateConfirmationAction(Donation donation) {
			super("Speichern");
			this.donation = donation;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			List<Donation> donationList = new ArrayList<Donation>();
			donationList.add(donation);
			Confirmation confirmation = new Confirmation();
			
			confirmation.setDonation(donation);
			confirmation.setDate(confirmationPicker.getDate());
			confirmation.setPerson(donation.getDonator());
			
			String fileName = showSaveDialog();
			
			if(fileName.equals(""))
				return;

			try {
				
				if(confirmationTemplateFile != null) {
					ConfirmationTemplate confirmationTemplate = new ConfirmationTemplate();
					confirmationTemplate.setFile(confirmationTemplateFile);
					confirmationTemplate.setName("abc");
					
					confirmationTemplate = confirmationService.insertOrUpdateConfirmationTemplate(confirmationTemplate);
					
					confirmation.setTemplate(confirmationTemplate);

					confirmationService.insertOrUpdate(confirmation);
					//feedbackLabel.setText("Best\u00E4tigung wurde erstellt.");
					JOptionPane.showMessageDialog(contentPanel, "Spendenbest\u00E4tigung erfolgreich erstellt.", "Information", JOptionPane.INFORMATION_MESSAGE);
					
					ConfirmationTemplateUtil.createMailingWithDocxTemplate(confirmationTemplateFile,donationList, fileName);
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

	private final class ChooseDonationConfirmationTemplate extends AbstractAction {

		private static final long serialVersionUID = 1L;
		
		public ChooseDonationConfirmationTemplate() {
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
				confirmationTemplateFile = fileChooser.getSelectedFile();
				log.debug("Set template file");
			}	
		}
	}
	
	private final class CancelDonationConfirmation extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private JDialog parent;
		
		public CancelDonationConfirmation(JDialog parent) {
			super("Abbrechen");
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			parent.dispose();
		}	
	}
	
	private final class ConfirmAllAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;
		
		public ConfirmAllAction() {
			super("Spendenbest\u00E4tigung f\u00FCr alle");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO alldonationconfirmation logic
		}	
	}
	
	
}
