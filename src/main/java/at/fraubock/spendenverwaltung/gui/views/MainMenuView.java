/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;

public class MainMenuView extends InitializableView {
//	private static final Logger log = Logger.getLogger(MainMenuView.class);
	private static final long serialVersionUID = 1L;
	
	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
	
	private JButton person;
	private JLabel general;
	private JPanel importPanel;
	private JButton filter;
	private JButton history;
	private JLabel persons;
	private JLabel filterLabel;
	private JLabel importLabel;
	private JLabel validationLabel;
	private JSeparator personSeparator;
	private JSeparator importSeparator;
	private JLabel donationImport;
	private JButton imports;
	private JButton validation;
	private JButton createConfirm;
	private JButton obtainConfirm;
	private JLabel createConfirmLabel;
	private JLabel obtainConfirmLabel;
	private JPanel sendPanel;
	private JButton mailings;
	private JLabel sending;
	private JLabel eSendingLabel;
	private JSeparator sendSeparator;
	private JButton showSendings;
	private JButton confirmSendings;
	private JLabel showSendingsLabel;
	private JLabel confirmSendingsLabel;
	private JPanel statsPanel;
	private JLabel stats;
	private JButton progress;
	private JButton statsSendings;
	private JLabel progressLabel;
	private JLabel statsSendingsLabel;
	private JButton search;
	private JLabel searchLabel;
	private JPanel overviewPanel;

	private JLabel historyLabel;
	
	public MainMenuView(ViewActionFactory viewActionFactory, ComponentFactory componentFactory) {
		/**
		 * good to know: basic font is 13pt
		 */
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		setUp();
	}
	
	public void setViewActionFactory(ViewActionFactory viewActionFactory) {
		this.viewActionFactory = viewActionFactory;
	}

	public void setComponentFactory(ComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
	}

	public void setUp() {
		this.setLayout(new MigLayout());
		overviewPanel = componentFactory.createPanel(800, 850);
		this.add(overviewPanel);
		/**
		 * "Allgemein"-panel contains persons and filter
		 */
		
		JPanel personsPanel = componentFactory.createPanel(800,160);
		
		overviewPanel.add(personsPanel, "wrap 0px");
		general = componentFactory.createLabel("Allgemein");
		general.setFont(new Font("Headline", Font.PLAIN, 14));

		//CreatePersonView
		personsPanel.add(general, "wrap");
		person = new JButton();
		personsPanel.add(person);
		search = new JButton();
		personsPanel.add(search, "gap 35");
		filter = new JButton();
		personsPanel.add(filter, "gap 35");
		history = new JButton();
		personsPanel.add(history, "wrap 0px, gap 45");
		//button labels
		persons = componentFactory.createLabel("Person anlegen");
		personsPanel.add(persons);
		
		//FindPersonsView
		searchLabel = componentFactory.createLabel("Personen suchen");
		personsPanel.add(searchLabel, "gap 25");
		
		//MainFilterView
		filterLabel = componentFactory.createLabel("Filter");
		personsPanel.add(filterLabel, "gap 59");
		
		historyLabel = componentFactory.createLabel("Verlauf");
		personsPanel.add(historyLabel, "gap 62, wrap 0px");
		
		//separator for next section
		personSeparator = componentFactory.createSeparator();
		overviewPanel.add(personSeparator, "wrap 0px, growx");
		
		/**
		 * "Spenden"-panel contains import, validation, confirmations
		 */
		
		//DonationImport
		importPanel = componentFactory.createPanel(800,160);
		overviewPanel.add(importPanel, "wrap 0px");
		donationImport = componentFactory.createLabel("Spenden");
		donationImport.setFont(new Font("Headline", Font.PLAIN, 14));
		importPanel.add(donationImport, "wrap");
		
		imports = new JButton();
		importPanel.add(imports, "split 3");
		validation = new JButton();
		importPanel.add(validation, "gap 55");
		createConfirm = new JButton();
		//importPanel.add(createConfirm, "gap 55");
		obtainConfirm = new JButton();
		importPanel.add(obtainConfirm, "wrap 0px, gap 55");
		
		importLabel = componentFactory.createLabel("Importe");
		importPanel.add(importLabel, "gap 15, split3");
		validationLabel = componentFactory.createLabel("Validierung");
		importPanel.add(validationLabel, "gap 77");
		createConfirmLabel = componentFactory.createLabel("<html><center>Best\u00E4tigung <br> erstellen</html>");
	//	importPanel.add(createConfirmLabel, "gap 63"); accessible through find persons
		obtainConfirmLabel = componentFactory.createLabel("<html><center>Best\u00E4tigung <br>abrufen</html>");
		importPanel.add(obtainConfirmLabel, "gap 63");
		
		importSeparator = componentFactory.createSeparator();
		overviewPanel.add(importSeparator, "wrap 0px, growx");
	
		/**
		 * "Aussendungen"-panel contains e-aussendung erstellen, briefaussendung erstellen, aussendungen anzeigen, 
		 * aussendungen bestaetigen, aussendung loeschen
		 */
		
		sendPanel = componentFactory.createPanel(800,160);
		overviewPanel.add(sendPanel, "wrap");
		sending = componentFactory.createLabel("Aussendungen");
		sending.setFont(new Font("Headline", Font.PLAIN, 14));
		sendPanel.add(sending, "wrap 0px");
		//importPanel.add(empty, "wrap");
		mailings = new JButton();
		sendPanel.add(mailings, "split 3");
		showSendings = new JButton();
		sendPanel.add(showSendings, "gap 55");
		confirmSendings = new JButton();
		sendPanel.add(confirmSendings, "gap 55, wrap 0px");
		
		eSendingLabel = componentFactory.createLabel("<html><center>Aussendung<br>erstellen");
		sendPanel.add(eSendingLabel, "split 3");
		
		showSendingsLabel = componentFactory.createLabel("<html><center>Aussendungen<br>anzeigen");
		sendPanel.add(showSendingsLabel, "gap 53");
		
		confirmSendingsLabel = componentFactory.createLabel("<html><center>Aussendungen<br>best\u00E4tigen</html>");
		sendPanel.add(confirmSendingsLabel, "gap 43");
		
		sendSeparator = componentFactory.createSeparator();
		overviewPanel.add(sendSeparator, "wrap 0px, growx");
		
		/**
		 * "Statistiken"-panel contains spendenentwicklung, aussendungen
		 */
				
		statsPanel = componentFactory.createPanel(800,160);
		overviewPanel.add(statsPanel, "wrap 0px");
		stats = componentFactory.createLabel("Statistiken");
		stats.setFont(new Font("Headline", Font.PLAIN, 14));
		statsPanel.add(stats, "wrap");
		progress = new JButton();
		statsPanel.add(progress, "split 2");
		statsSendings = new JButton();;
		statsPanel.add(statsSendings, "gap 55, wrap 0px");
		
		progressLabel = componentFactory.createLabel("<html><center>Spenden-<br>entwicklung</html>");
		statsPanel.add(progressLabel, "split 2");
		statsSendingsLabel = componentFactory.createLabel("Aussendungen");
		statsPanel.add(statsSendingsLabel, "gap 55");

			}
	
	//call when viewActionFactory is fully populated
	public void init() {
		
		person.setAction(viewActionFactory.getCreatePersonsViewAction());
		search.setAction(viewActionFactory.getFindPersonsViewAction());
		filter.setAction(viewActionFactory.getMainFilterViewAction());
		history.setAction(viewActionFactory.getHistoryViewAction());
		imports.setAction(viewActionFactory.getDonationImportViewAction());
		validation.setAction(viewActionFactory.getImportValidationViewAction());
		//createConfirm.setAction(viewActionFactory.getCreateDonationConfirmationViewAction()); accessible through find persons
		obtainConfirm.setAction( viewActionFactory.getFindDonationConfirmationViewAction());
		mailings.setAction(viewActionFactory.getCreateMailingsViewAction());
		showSendings.setAction(viewActionFactory.getFindMailingsViewAction(null)); 
		confirmSendings.setAction(viewActionFactory.getConfirmMailingsViewAction(null));
		progress.setAction(viewActionFactory.getDonationProgressStatsViewAction());
		statsSendings.setAction(viewActionFactory.getShowMailingStatsViewAction());
	}
	
//	public static void main(String[] args) {
//		ViewDisplayer displayer = new ViewDisplayer();
//		displayer.changeView(new MainMenuView(new ViewActionFactory(), new ComponentFactory()));
//	}
}
