package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
import at.fraubock.spendenverwaltung.util.statistics.Classification;
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
	private JPanel operationsPanel;
	private JLabel doSingleStat;
	private Filter showAllFilter;
	private JComboBox<Filter> firstFilter;
	private JLabel chooseFilter;
	private JLabel chooseOperation;
	private JComboBox<String> operationBox;
	private JComboBox<String> filterCountCombo;
	private JLabel chooseFilterCount;
	private JComboBox<Filter> secondFilter;
	private JComboBox<Filter> thirdFilter;
	private JComboBox<Filter> fourthFilter;
	private JLabel chooseClass;
	private JComboBox<String> classBox;
	private JPanel buttonPanel;
	private List<Filter> donationFilters;

	private JRadioButton chooseOne;

	private JRadioButton chooseTwo;

	private JRadioButton chooseThree;

	private ButtonGroup group;
	
	public DonationProgressStatsView(ComponentFactory componentFactory, ViewActionFactory viewActionFactory, 
			IDonationService donationService, IFilterService filterService) {
		this.componentFactory = componentFactory;
		this.viewActionFactory = viewActionFactory;
		this.donationService = donationService;
		this.filterService = filterService;
		donationStats = new DonationStats();
		fillFilters();
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
	
	private void fillFilters() {
		showAllFilter = new Filter(FilterType.DONATION);
		donationFilters = new ArrayList<Filter>();
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
	}
	
	private void setUpCreate() {
		
		operationsPanel= componentFactory.createPanel(700, 500);
		this.add(operationsPanel, "wrap");
		doSingleStat = componentFactory.createLabel("Statistische Berechnungen durchf\u00FChren ");
		doSingleStat.setFont(new Font("Headline", Font.PLAIN, 14));
		operationsPanel.add(doSingleStat, "wrap 30px");
		
// choose number of filters for comparison
		chooseFilterCount = componentFactory
				.createLabel("Filteranzahl ausw\u00E4hlen: ");
		operationsPanel.add(chooseFilterCount, "split4");
		chooseOne = new JRadioButton("1");
		chooseTwo = new JRadioButton("2");
		chooseThree = new JRadioButton("3");
		group = new ButtonGroup();
		group.add(chooseOne);
		group.add(chooseTwo);
		group.add(chooseThree);
		operationsPanel.add(chooseOne);
		operationsPanel.add(chooseTwo);
		operationsPanel.add(chooseThree, "wrap 10px");
		
		
		firstFilter = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
				donationFilters));
		secondFilter = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
				donationFilters));
		thirdFilter = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
				donationFilters));

		chooseFilter = componentFactory.createLabel("Filter ausw\u00E4hlen: ");
		operationsPanel.add(chooseFilter, "split4");
		operationsPanel.add(firstFilter, "gap 40");
		operationsPanel.add(secondFilter);
		operationsPanel.add(thirdFilter, "wrap 10px");
		
// then choose class 
		chooseClass = componentFactory.createLabel("Darstellung nach: ");
		operationsPanel.add(chooseClass, "split 2");
		String[] classification = new String[] { "Tag", "Woche", "Monat",
				"Quartal", "Jahr" };
		classBox = new JComboBox<String>(classification);
		operationsPanel.add(classBox, "gap 35, wrap 10px, growx");
				
//	choose operation then
		chooseOperation = componentFactory.createLabel("Operation ausw\u00E4hlen: ");
		operationsPanel.add(chooseOperation, "split 2");
		String[] operations = new String[]{"Betragsmaximum", "Betragsmedian", "Betragsminimum", 
				"Betragsmittelwert", "Gesamtanzahl der Spenden", "Gesamtsumme der Spenden"};
		operationBox = new JComboBox<String>(operations);
		operationsPanel.add(operationBox, "gap 10, growx, wrap 30px");
	
		submit = new JButton();
		cancel = new JButton();
		operationsPanel.add(submit, "split 2");
		operationsPanel.add(cancel, "wrap");
	}
	
	public void init() {
		PlotAction calculateAction = new PlotAction();
		calculateAction.putValue(Action.NAME, "Berechnen");
		submit.setAction(calculateAction);
		
		Action cancelAction = viewActionFactory.getMainMenuViewAction();
		cancelAction.putValue(Action.NAME, "Abbrechen");
		cancel.setAction(cancelAction);
		
	}
	
	private final class PlotAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
				
		}
	}
}
