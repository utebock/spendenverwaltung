package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.NumericTextField;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing.Medium;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.util.FilterType;

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
	private JXDatePicker emailDatePicker;
	private JXDatePicker postalDatePicker;

	// personfilters
	private Filter selectedEmailFilter;
	private Filter selectedPostalFilter;

	JPanel contentPanel, createEMailingPanel, createPostalMailingPanel, createReproducePanel;

	JLabel emailTitle, postalTitle, reproduceTitle, emailFilterLabel, postalFilterLabel,
			emailTypeLabel, postalTypeLabel, emailDateLabel, postalDateLabel;
	JButton createEMailingButton, createPostalMailingButton,
			cancelEMailingButton, cancelPostalMailingButton
			;
	JSeparator separator;

	JComboBox<Filter> eMailingPersonFilterChooser, postalPersonFilterChooser;
	JComboBox<Mailing.MailingType> eMailingTypeChooser,
			postalMailingTypeChooser;

	public CreateMailingsView(ViewActionFactory viewActionFactory,
			ComponentFactory componentFactory, IMailingService mailingService,
			IFilterService filterService) {
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.mailingService = mailingService;
		this.filterService = filterService;
		setUpLayout();
	}

	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(800, 800);
		createEMailingPanel = componentFactory.createPanel(800, 350);
		createPostalMailingPanel = componentFactory.createPanel(800, 350);
		createReproducePanel = componentFactory.createPanel(800, 350);
		
		this.add(contentPanel);

		contentPanel.add(createEMailingPanel, "wrap");
		// end of emailing panel, add separator
		contentPanel.add(componentFactory.createSeparator(), "wrap, growx");
		contentPanel.add(createPostalMailingPanel, "wrap");
		// end of mailing panel, add separator
		contentPanel.add(componentFactory.createSeparator(), "wrap, growx");
		contentPanel.add(createReproducePanel);

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

		// buttons
		createPostalMailingButton = new JButton("Anlegen");
		cancelPostalMailingButton = new JButton("Abbrechen");
		createPostalMailingPanel.add(cancelPostalMailingButton, "split 2");
		createPostalMailingPanel.add(createPostalMailingButton,"wrap");

		// reproduce mailed documents
		reproduceTitle = componentFactory
				.createLabel("Postalische Aussendungen wiederherstellen");
		reproduceTitle.setFont(new Font("Headline", Font.PLAIN, 16));
		createReproducePanel.add(reproduceTitle, "gapbottom 30,wrap");
		
		List<Mailing> mailings = null;
		try {
			mailings = mailingService.getAll();
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(null,
					"Ein Fehler trat beim Laden der Aussendungen auf.");
			log.error("Error retrieving mailings: " + e.getMessage());
		}

		List<PostalMailingPicker> postalMailings = new ArrayList<PostalMailingPicker>();
		for (Mailing m : mailings) {
			if (m.getMedium() == Mailing.Medium.POSTAL) {
				postalMailings.add(new PostalMailingPicker(m));
			}
		}

		final JComboBox<PostalMailingPicker> sentMailingsDD = new JComboBox<PostalMailingPicker>(
				new SimpleComboBoxModel<PostalMailingPicker>(postalMailings));

		createReproducePanel.add(sentMailingsDD, "wrap");

		JButton reproduceDocument = new JButton();
		reproduceDocument.setAction(new AbstractAction(
				"Aussendung wiederherstellen") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PostalMailingPicker mailingChoice = (PostalMailingPicker) sentMailingsDD
						.getSelectedItem();
				Mailing mailing = mailingChoice.getMailing();

				try {
					mailingService.reproduceDocument(mailing);
				} catch (ServiceException e) {
					JOptionPane
							.showMessageDialog(
									CreateMailingsView.this,
									"Die Aussendung konnte nicht wiederhergestellt werden.",
									"Error", JOptionPane.ERROR_MESSAGE);
					log.error("Mailing could not be reproduced.mailing_id='"
							+ mailing.getId() + "', error was: "
							+ e.getMessage());
				}
			}

		});
		createReproducePanel.add(reproduceDocument,"wrap, gaptop 10");

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
			NumericTextField test = new NumericTextField();
			test.validateContents();
		} catch (ServiceException e) {
			JOptionPane
					.showMessageDialog(null,
							"Ein Fehler trat beim Initialisieren der Personenfilter auf.");
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
			if ((selectedEmailFilter = (Filter) eMailingPersonFilterChooser
					.getSelectedItem()) == null) {
				JOptionPane.showMessageDialog(null,
						"Es muss ein Personenfilter ausgewählt werden!");
				return;
			}
			mailing.setFilter(selectedEmailFilter);
			mailing.setMedium(Medium.EMAIL);
			mailing.setDate(emailDatePicker.getDate());
			mailing.setType((Mailing.MailingType) eMailingTypeChooser
					.getSelectedItem());
			try {
				mailingService.insertOrUpdate(mailing);
			} catch (ServiceException e1) {
				log.error(e1.getMessage() + " occured in CreateMailings");
				JOptionPane
						.showMessageDialog(null,
								"Ein Fehler ist während der Erstellung dieser Aussendung aufgetreten.");
			}
			// TODO create email mailing with service layer, add mailchimp
			// logic, create
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
			} catch (ServiceException e1) {
				log.error(e1 + " occured in CreateMailingsView");
				JOptionPane
						.showMessageDialog(null,
								"Ein Fehler ist während der Erstellung dieser Aussendung aufgetreten");
			}
		}

	}

	private class PostalMailingPicker {

		private Mailing mailing;

		public PostalMailingPicker(Mailing mailing) {
			this.mailing = mailing;
		}

		@Override
		public String toString() {
			String res = "";
			res += new SimpleDateFormat("dd.MM.yyyy").format(mailing.getDate())
					+ " - ";
			res += mailing.getType() + " - ";
			// res += mailing.getTemplate();TODO

			return res;
		}

		public Mailing getMailing() {
			return mailing;
		}
	}

}