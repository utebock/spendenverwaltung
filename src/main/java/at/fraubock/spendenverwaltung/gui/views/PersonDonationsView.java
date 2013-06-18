package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

import at.fraubock.spendenverwaltung.gui.DonationTableModel;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.NumericTextField;
import at.fraubock.spendenverwaltung.gui.components.PersonTableModel;
import at.fraubock.spendenverwaltung.gui.components.StringTextField;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;

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
	
	//datepicker for quick search
	private JXDatePicker start, end;
	
	//used for total field
	private long currentTotal = 0;
	
	//selected in parent view
	private Person selectedPerson;
	//keep state of parent component tablemodel until view switch
	private PersonTableModel personTableModel;
	
	private DonationTableModel donationTableModel;
	
	public PersonDonationsView (ViewActionFactory viewActionFactory, ComponentFactory componentFactory,
			IDonationService donationService, IAddressService addressService, Person selectedPerson, PersonTableModel personTableModel) {
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.addressService = addressService;
		this.donationService = donationService;
		this.selectedPerson = selectedPerson;
		this.personTableModel = personTableModel;
		
		setUpLayout();
	}
	
	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(800, 800);
		
		this.add(contentPanel);
		
		toolbar = new JToolBar();
		contentPanel.add(toolbar, "spanx, wrap, growx");

		donationTotalLabel = componentFactory.createLabel("");
		donationTotalLabel.setFont(new Font("Headline", Font.PLAIN, 13));
		contentPanel.add(donationTotalLabel, "wrap");
		
		donationTableModel = new DonationTableModel();
		donationsTable = new JTable(donationTableModel);
		donationsTable.setFillsViewportHeight(true);
		donationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(donationsTable);
		scrollPane.setPreferredSize(new Dimension(700, 550));
		
		contentPanel.add(scrollPane, "wrap");
		
		start = new JXDatePicker();
		start.addActionListener(new StartDateSelectedListener());
		end = new JXDatePicker();
		end.addActionListener(new EndDateSelectedListener());
		
		contentPanel.add(start, "split 2");
		contentPanel.add(end, "wrap");
		
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
			feedbackLabel.setText("Es wurde keine Person ausgewählt");
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
			feedbackLabel.setText("Es passierte ein Fehler beim Laden der Spenden.");
		}
	}
	
	private void addComponentsToToolbar(JToolBar toolbar) {
		
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		
		backButton = new JButton();
		
		Action getBack = viewActionFactory.getFindPersonsViewAction(personTableModel);
		getBack.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backButton.jpg")));
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
		
		toolbar.add(backButton, "split 6, growx");
		toolbar.add(addDonation, "growx");
		toolbar.add(editDonation, "growx");
		toolbar.add(confirmDonation, "growx");
		toolbar.add(confirmAllDonations, "growx");
		toolbar.add(deleteDonation, "growx");	
	}
	
	private final class AddAction extends AbstractAction {
		
		private JDialog addDonationFrame;
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
			super("Neue Spende");
			setUpLayout();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			addDonationFrame = new JDialog();
			addDonationFrame.setLocation(300, 300);
			addDonationPanel = componentFactory.createPanel(300, 250);
			
			//add combobox + label
			donationTypeLabel = componentFactory.createLabel("Spendenart");
			donationTypeCombo = new JComboBox<Donation.DonationType>(new SimpleComboBoxModel<>(Donation.DonationType.values()));
			
			addDonationPanel.add(donationTypeLabel, "split 2");
			addDonationPanel.add(donationTypeCombo, "wrap");
			
			dateLabel = componentFactory.createLabel("Datum");
			donationDatePicker = new JXDatePicker(new Date());
			addDonationPanel.add(dateLabel, "split 2");
			addDonationPanel.add(donationDatePicker, "wrap");
			
			//add amount field + label
			donationAmountLabel = componentFactory.createLabel("Spendenhöhe");
			donationAmountField = new NumericTextField(ComponentConstants.SHORT_TEXT, false);
			addDonationPanel.add(donationAmountLabel, "split 2");
			addDonationPanel.add(donationAmountField, "wrap");
			
			dedicationLabel = componentFactory.createLabel("Widmung");
			dedicationField = new StringTextField(ComponentConstants.LONG_TEXT);
			addDonationPanel.add(dedicationLabel, "split 2");
			addDonationPanel.add(dedicationField, "wrap");
			
			noteLabel = componentFactory.createLabel("Notiz");
			noteField = new StringTextField(ComponentConstants.LONG_TEXT);
			addDonationPanel.add(noteLabel, "split 2");
			addDonationPanel.add(noteField, "wrap");
			
			submitButton = new JButton(new SubmitAddAction());
			cancelButton = new JButton(new AddCancelAction());		
			addDonationPanel.add(cancelButton, "split 2");
			addDonationPanel.add(submitButton, "wrap");
			
			validationFeedbackLabel = componentFactory.createLabel("");
			addDonationPanel.add(validationFeedbackLabel, "wrap");
			addDonationFrame.add(addDonationPanel);
			addDonationFrame.pack();
			addDonationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			addDonationFrame.setVisible(true);
		}	
		
		private final class SubmitAddAction extends AbstractAction {

			private static final long serialVersionUID = 1L;

			public SubmitAddAction() {
				super("Anlegen");
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
					validationFeedbackLabel.setText("Spendenart muss ausgewählt werden!");
				}
				
				if(donationAmountField.validateContents()) {
					donation.setAmount(donationAmountField.getHundredths());
				} else {
					validation = false;
					validationFeedbackLabel.setText("Spendenhöhe muss ausgefüllt werden!");
				}
				
				if(dedicationField.validateContents()) {
					if(!dedicationField.getText().equals(""))
						donation.setDedication(dedicationField.getText());
				} else {
					validation = false;
					validationFeedbackLabel.setText("Spendenwidmung konnte nicht validiert werden.");
				}
				
				if(noteField.validateContents()) {
					if(!noteField.getText().equals(""))
						donation.setNote(noteField.getText());
				} else {
					validation = false;
					validationFeedbackLabel.setText("Spendennotiz konnte nicht validiert werden.");
				}
				
				donation.setDate(donationDatePicker.getDate());
				
				if(validation) {
					try {
						donationService.create(donation);
						donationTableModel.addDonation(donation);
						feedbackLabel.setText("Spende wurde erstellt.");
						//update total label
						currentTotal += donation.getAmount();
						setTotalLabel();
						addDonationFrame.dispose();						
					} catch (ServiceException e1) {
						log.warn(e1.getLocalizedMessage());
						feedbackLabel.setText("Es passierte ein Fehler beim erstellen der Spende.");
					}
				}
			}
		}
		
		private final class	AddCancelAction extends AbstractAction {

			private static final long serialVersionUID = 1L;

			public AddCancelAction() {
				super("Abbrechen");
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addDonationFrame.dispose();
			}
			
		}
	}
	
	private final class EditAction extends AbstractAction {
		
		private JDialog editDonationFrame;
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
			setUpLayout();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
					
			selectedRow = donationsTable.getSelectedRow();
			if(selectedRow == -1) {
				feedbackLabel.setText("Eine Spende muss zum bearbeiten ausgewählt werden.");
				return;
			}
			
			selectedDonation = donationTableModel.getDonationRow(selectedRow);
			
			editDonationFrame = new JDialog();
			editDonationPanel = componentFactory.createPanel(250, 200);
			
			//add combobox + label
			donationTypeLabel = componentFactory.createLabel("Spendenart");
			donationTypeCombo = new JComboBox<Donation.DonationType>(new SimpleComboBoxModel<>(Donation.DonationType.values()));
			donationTypeCombo.setSelectedItem(selectedDonation.getType());
			
			editDonationPanel.add(donationTypeLabel, "split 2");
			editDonationPanel.add(donationTypeCombo, "wrap");
			
			dateLabel = componentFactory.createLabel("Datum");
			donationDatePicker = new JXDatePicker();
			donationDatePicker.setDate(selectedDonation.getDate());
			editDonationPanel.add(dateLabel, "split 2");
			editDonationPanel.add(donationDatePicker, "wrap");
			
			//add amount field + label
			donationAmountLabel = componentFactory.createLabel("Spendenhöhe");
			donationAmountField = new NumericTextField(ComponentConstants.SHORT_TEXT, false);
			currentTotal -= selectedDonation.getAmount();
			donationAmountField.setNumericValue(selectedDonation.getAmount());
			editDonationPanel.add(donationAmountLabel, "split 2");
			editDonationPanel.add(donationAmountField, "wrap");
			
			dedicationLabel = componentFactory.createLabel("Widmung");
			dedicationField = new StringTextField(ComponentConstants.LONG_TEXT);
			if(selectedDonation.getDedication() != null) {
				dedicationField.setText(selectedDonation.getDedication());
			}
			editDonationPanel.add(dedicationLabel, "split 2");
			editDonationPanel.add(dedicationField, "wrap");
			
			noteLabel = componentFactory.createLabel("Notiz");
			noteField = new StringTextField(ComponentConstants.LONG_TEXT);
			if(selectedDonation.getNote() != null) {
				noteField.setText(selectedDonation.getNote());
			}
			editDonationPanel.add(noteLabel, "split 2");
			editDonationPanel.add(noteField, "wrap");
			
			submitButton = new JButton(new SubmitEditAction());
			cancelButton = new JButton(new CancelEditAction());		
			editDonationPanel.add(cancelButton, "split 2");
			editDonationPanel.add(submitButton, "wrap");
			
			editDonationPanel.add(validationFeedbackLabel, "wrap");
			editDonationFrame.add(editDonationPanel);
			editDonationFrame.pack();
			editDonationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			editDonationFrame.setVisible(true);
		}	
		
		private final class SubmitEditAction extends AbstractAction {

			private static final long serialVersionUID = 1L;
			
			public SubmitEditAction() {
				super("Anlegen");
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
					validationFeedbackLabel.setText("Spendenart muss ausgewählt werden!");
				}
				
				if(donationAmountField.validateContents()) {
					donation.setAmount(donationAmountField.getHundredths());
				} else {
					validation = false;
					validationFeedbackLabel.setText("Spendenhöhe muss ausgefüllt werden!");
				}
				
				if(dedicationField.validateContents()) {
					if(!dedicationField.getText().equals(""))
						donation.setDedication(dedicationField.getText());
				} else {
					validation = false;
					validationFeedbackLabel.setText("Spendenwidmung konnte nicht validiert werden.");
				}
				
				if(noteField.validateContents()) {
					if(!noteField.getText().equals(""))
						donation.setNote(noteField.getText());
				} else {
					validation = false;
					validationFeedbackLabel.setText("Spendennotiz konnte nicht validiert werden.");
				}
				
				donation.setDate(donationDatePicker.getDate());
				
				if(validation) {
					try {
						donationService.update(donation);
						donationTableModel.removeDonation(selectedRow);
						donationTableModel.addDonation(donation);
						feedbackLabel.setText("Spende wurde erstellt.");
						//update total label
						currentTotal += donation.getAmount();
						setTotalLabel();
						editDonationFrame.dispose();						
					} catch (ServiceException e1) {
						log.warn(e1.getLocalizedMessage());
						feedbackLabel.setText("Es passierte ein Fehler beim bearbeiten der Spende.");
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
				editDonationFrame.dispose();
			}
			
		}
	}
	
	private final class DeleteAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;
		
		public DeleteAction() {
			super("Spende Löschen");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = donationsTable.getSelectedRow();
			if(selectedRow == -1) {
				feedbackLabel.setText("Eine Spende muss zum bearbeiten ausgewählt werden.");
				return;
			}
			
			Donation donation = donationTableModel.getDonationRow(selectedRow);

			int dialogResult = JOptionPane.showConfirmDialog (contentPanel, "Wollen sie diese Spende wirklich löschen?", "Löschen", JOptionPane.YES_NO_OPTION);
		
			if(dialogResult == JOptionPane.YES_OPTION) {
				try {
					donationService.delete(donation);
					donationTableModel.removeDonation(selectedRow);
					feedbackLabel.setText("Spende wurde gelöscht.");
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					feedbackLabel.setText("Spende konnte nicht gelöscht werden.");
				}
			}
		}	
	}
	
	private final class ConfirmAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;
		
		public ConfirmAction() {
			super("Spende bestätigen");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO donationconfirmation logic
		}	
	}
	
	private final class ConfirmAllAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;
		
		public ConfirmAllAction() {
			super("Alle Spenden bestätigen");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO alldonationconfirmation logic
		}	
	}
	
	private final class StartDateSelectedListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Date startDate = start.getDate();
			Date endDate = end.getDate();
			
			try {
				List<Donation> donations = donationService.getByPerson(selectedPerson);
			
				if(startDate != null && endDate != null) {
					if(startDate.after(endDate)) {
						feedbackLabel.setText("Ungültiger Datumsbereich");
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
				}
			} catch (ServiceException e2) {
				log.warn(e2.getLocalizedMessage());
				feedbackLabel.setText("Es passierte ein Fehler beim Initialisieren der Tabelle.");
			}
		}
	}
	
	private final class EndDateSelectedListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Date startDate = start.getDate();
			Date endDate = end.getDate();
			
			try {
				List<Donation> donations = donationService.getByPerson(selectedPerson);
			
				if(startDate != null && endDate != null) {
					if(startDate.after(endDate)) {
						feedbackLabel.setText("Ungültiger Datumsbereich");
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
				feedbackLabel.setText("Es passierte ein Fehler beim Initialisieren der Tabelle.");
			}
		}
	}
	
	
	
}
