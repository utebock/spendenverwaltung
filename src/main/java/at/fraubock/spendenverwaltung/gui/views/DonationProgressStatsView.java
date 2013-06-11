package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Font;
import java.awt.event.ActionEvent;
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
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.statistics.DonationStats;

public class DonationProgressStatsView extends InitializableView {
	
	private static final Logger log = Logger.getLogger(DonationProgressStatsView.class);

	private static final long serialVersionUID = 1L;
	private IDonationService donationService;
	private IFilterService filterService;
	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
	private DonationStats donationStats;
	private JButton submit;
	private JButton cancel;
	private Donation donation;
	private JSeparator separator;
	private JPanel overviewPanel;
	private JLabel doSingleStat;
	private Filter showAllFilter;
	private JComboBox<Filter> choooseSingleFilter;
	private JLabel chooseFilter;
	private JLabel chooseOperation;
	private JComboBox<String> operationBox;
	private JLabel resultLabel;
	private JTextField result;

	public DonationProgressStatsView(ComponentFactory componentFactory, ViewActionFactory viewActionFactory, 
			IDonationService donationService, IFilterService filterService) {
		this.componentFactory = componentFactory;
		this.viewActionFactory = viewActionFactory;
		this.donationService = donationService;
		this.filterService = filterService;
		donationStats = new DonationStats();
		setUpCreate();
	}
	
	public void setDonationService(IDonationService donationService) {
		this.donationService = donationService;
	}
	public void setFilterService(IFilterService filterService) {
		this.filterService = filterService;
	}

	public void setComponentFactory(ComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
	}
	
	public void setViewActionFactory(ViewActionFactory viewActionFactory) {
		this.viewActionFactory = viewActionFactory;
	}

	private void setUpCreate() {
		overviewPanel = componentFactory.createPanel(700, 800);
		this.add(overviewPanel);
		doSingleStat = componentFactory.createLabel("Statistische Berechnungen durchf\u00FChren ");
		doSingleStat.setFont(new Font("Headline", Font.PLAIN, 14));
		overviewPanel.add(doSingleStat, "wrap 20px");
		
		showAllFilter = new Filter(FilterType.DONATION);
		List<Filter> donationFilters = new ArrayList<Filter>();
		donationFilters.add(showAllFilter);
		
		try{
			donationFilters.addAll(filterService.getAllByFilter(FilterType.DONATION));
		}	catch (ServiceException e) {
			JOptionPane
			.showMessageDialog(
					this,
					"Ein unerwarteter Fehler ist aufgetreten.",
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		chooseFilter = componentFactory.createLabel("Filter ausw\u00E4hlen: ");
		overviewPanel.add(chooseFilter, "split2");
		choooseSingleFilter = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(donationFilters));
		overviewPanel.add(choooseSingleFilter, "gap 33, wrap, growx");
		
		chooseOperation = componentFactory.createLabel("Operation ausw\u00E4hlen: ");
		overviewPanel.add(chooseOperation, "split2");
		
		String[] operations = new String[]{"Betragsmaximum", "Betragsmedian", "Betragsminimum", 
				"Betragsmittelwert", "Gesamtanzahl der Spenden", "Gesamtsumme der Spenden"};
		operationBox = new JComboBox<String>(operations);
		overviewPanel.add(operationBox, "growx, wrap");
		
		resultLabel = componentFactory.createLabel("Ergebnis: ");
		result = componentFactory.createTextField(150); //get calculated result here
		overviewPanel.add(resultLabel, "split2");
		overviewPanel.add(result, "gap 82, growx, wrap 30px");
		
		submit = new JButton();
		cancel = new JButton();
		overviewPanel.add(submit, "split 2");
		overviewPanel.add(cancel, "wrap");
		
		separator = componentFactory.createSeparator();
		overviewPanel.add(separator, "wrap, growx");
	}

	public void init() {
		CalculateAction calculateAction = new CalculateAction();
		calculateAction.putValue(Action.NAME, "Berechnen");
		submit.setAction(calculateAction);
		
		Action cancelAction = viewActionFactory.getMainMenuViewAction();
		cancelAction.putValue(Action.NAME, "Abbrechen");
		cancel.setAction(cancelAction);
		
	}
	
	private final class CalculateAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			
		}
	}
}
