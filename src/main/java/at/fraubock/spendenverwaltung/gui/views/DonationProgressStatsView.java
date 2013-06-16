package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Color;
import java.awt.Dimension;
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
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.Pair;
import at.fraubock.spendenverwaltung.util.statistics.Classification;
import at.fraubock.spendenverwaltung.util.statistics.Operation;
import at.fraubock.spendenverwaltung.util.statistics.Statistic;

public class DonationProgressStatsView extends InitializableView {
	
	private static final Logger log = Logger.getLogger(DonationProgressStatsView.class);

	private static final long serialVersionUID = 1L;
	private IDonationService donationService;
	private IFilterService filterService;
	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
	private Statistic statistic;
	private JButton submit;
	private JButton cancel;
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
	private JLabel chooseClass;
	private JComboBox<String> classBox;
	private List<Filter> donationFilters;
	private JRadioButton chooseTwo;
	private JRadioButton chooseThree;
	private ButtonGroup group;
	private JFreeChart chart;
	private ChartPanel chartPanel;
	private List<Donation> donationList;
	
	public DonationProgressStatsView(ComponentFactory componentFactory, ViewActionFactory viewActionFactory, 
			IDonationService donationService, IFilterService filterService) {
		this.componentFactory = componentFactory;
		this.viewActionFactory = viewActionFactory;
		this.donationService = donationService;
		this.filterService = filterService;
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
		
		operationsPanel= componentFactory.createPanel(700, 800);
		this.add(operationsPanel, "wrap");
		doSingleStat = componentFactory.createLabel("Statistische Berechnungen durchf\u00FChren ");
		doSingleStat.setFont(new Font("Headline", Font.PLAIN, 14));
		operationsPanel.add(doSingleStat, "wrap 30px");
		
// choose number of filters for comparison
		chooseFilterCount = componentFactory
				.createLabel("Filteranzahl ausw\u00E4hlen: ");
		operationsPanel.add(chooseFilterCount, "split4");

		chooseTwo = new JRadioButton("2");
		chooseTwo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				}
		});
		chooseThree = new JRadioButton("3");
		chooseThree.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				}
		});
		group = new ButtonGroup();
		group.add(chooseTwo);
		group.add(chooseThree);
		operationsPanel.add(chooseTwo);
		operationsPanel.add(chooseThree, "wrap 10px");
		
		firstFilter = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
				donationFilters));
		firstFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getDonations((Filter) firstFilter.getModel().getSelectedItem());
			}
		});
		
		secondFilter = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
				donationFilters));
		secondFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getDonations((Filter) secondFilter.getModel().getSelectedItem());
			}
		});
		
		thirdFilter = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
				donationFilters));
		thirdFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getDonations((Filter) thirdFilter.getModel().getSelectedItem());
			}
		});
		
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
		operationsPanel.add(cancel, "wrap 20px");
		
		
		
	}
	
	private List<Donation> getDonations(Filter filter) {
		
		donationList = new ArrayList<Donation>();
		try {
			donationList = donationService.getByFilter(filter);
			log.info("List " + donationList.size() + " persons");
		} catch (ServiceException e) {
			JOptionPane
					.showMessageDialog(
							this,
							"Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
							"Fehler", JOptionPane.ERROR_MESSAGE);
			log.error(e);
			e.printStackTrace();
		}
		if (donationList == null) {
			JOptionPane.showMessageDialog(this, "GetAll() returns null.",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		return donationList;
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
		private List<Donation> donationList;
		private List<Pair<List<Donation>, String>> pairList;
		private Classification classification;
		private Operation operation;
		private DefaultCategoryDataset dataSet;
		private String name;
		Pair<List<Donation>, String> pair;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			donationList = new ArrayList<Donation>();
			pairList = new ArrayList<Pair<List<Donation>, String>>();
			operation = (Operation.values()[operationBox.getSelectedIndex()]);
			
			if (chooseTwo.isSelected() == true) {
				if (firstFilter.getSelectedIndex() != 0
						&& secondFilter.getSelectedIndex() != 0) {
					donationList = getDonations((Filter) firstFilter
							.getSelectedItem());
					name = ((Filter) firstFilter.getSelectedItem()).getName();
					pair = new Pair<List<Donation>, String>(donationList, name);
					pairList.add(pair);

					donationList = getDonations((Filter) secondFilter
							.getSelectedItem());
					name = ((Filter) secondFilter.getSelectedItem()).getName();
					pair = new Pair<List<Donation>, String>(donationList, name);
					pairList.add(pair);

					classification = (Classification.values()[classBox
							.getSelectedIndex()]);
					statistic = new Statistic(pairList, classification);
					dataSet = statistic.createDataset(operation);

					chart = createBarChart(dataSet);
					chartPanel = new ChartPanel(chart);
					chartPanel.setPreferredSize(new Dimension(700, 270));
					operationsPanel.add(chartPanel);
					operationsPanel.validate();
				}
				if (secondFilter.getSelectedIndex() != 0
						&& thirdFilter.getSelectedIndex() != 0) {
					donationList = getDonations((Filter) secondFilter
							.getSelectedItem());
					name = ((Filter) secondFilter.getSelectedItem()).getName();
					pair = new Pair<List<Donation>, String>(donationList, name);
					pairList.add(pair);

					donationList = getDonations((Filter) thirdFilter
							.getSelectedItem());
					name = ((Filter) thirdFilter.getSelectedItem()).getName();
					pair = new Pair<List<Donation>, String>(donationList, name);
					pairList.add(pair);

					classification = (Classification.values()[classBox
							.getSelectedIndex()]);
					statistic = new Statistic(pairList, classification);
					dataSet = statistic.createDataset(operation);

					chart = createBarChart(dataSet);
					chartPanel = new ChartPanel(chart);
					chartPanel.setPreferredSize(new Dimension(700, 270));
					operationsPanel.add(chartPanel);
					operationsPanel.validate();
				}
				if (firstFilter.getSelectedIndex() != 0
						&& thirdFilter.getSelectedIndex() != 0) {
					donationList = getDonations((Filter) firstFilter
							.getSelectedItem());
					name = ((Filter) firstFilter.getSelectedItem()).getName();
					pair = new Pair<List<Donation>, String>(donationList, name);
					pairList.add(pair);

					donationList = getDonations((Filter) thirdFilter
							.getSelectedItem());
					name = ((Filter) thirdFilter.getSelectedItem()).getName();
					pair = new Pair<List<Donation>, String>(donationList, name);
					pairList.add(pair);

					classification = (Classification.values()[classBox
							.getSelectedIndex()]);
					statistic = new Statistic(pairList, classification);
					dataSet = statistic.createDataset(operation);

					chart = createBarChart(dataSet);
					chartPanel = new ChartPanel(chart);
					chartPanel.setPreferredSize(new Dimension(700, 270));
					operationsPanel.add(chartPanel);
					operationsPanel.validate();
				}
			}
			else if(chooseThree.isSelected()==true){
				donationList = getDonations((Filter) firstFilter
						.getSelectedItem());
				name = ((Filter) firstFilter.getSelectedItem()).getName();
				pair = new Pair<List<Donation>, String>(donationList, name);
				pairList.add(pair);

				donationList = getDonations((Filter) secondFilter
						.getSelectedItem());
				name = ((Filter) secondFilter.getSelectedItem()).getName();
				pair = new Pair<List<Donation>, String>(donationList, name);
				pairList.add(pair);

				donationList = getDonations((Filter) thirdFilter
						.getSelectedItem());
				name = ((Filter) thirdFilter.getSelectedItem()).getName();
				pair = new Pair<List<Donation>, String>(donationList, name);
				pairList.add(pair);

				classification = (Classification.values()[classBox
						.getSelectedIndex()]);
				statistic = new Statistic(pairList, classification);
				dataSet = statistic.createDataset(operation);

				chart = createBarChart(dataSet);
				chartPanel = new ChartPanel(chart);
				chartPanel.setPreferredSize(new Dimension(700, 270));
				operationsPanel.add(chartPanel);
				operationsPanel.validate();
			}
			else{
				JOptionPane.showMessageDialog(operationsPanel,
						"Bitte Auswahl an Einstellungen treffen."); return;
			}
			}
	}
	
	private JFreeChart createLineChart(DefaultCategoryDataset dataSet) {
		//ChartFactory.createLineChart(title, categoryAxisLabel, valueAxisLabel, dataset, orientation, legend, tooltips, urls)
		chart = ChartFactory.createLineChart(null, null, null, dataSet, PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        return chart;
	}
	
	private JFreeChart createBarChart(DefaultCategoryDataset dataSet) {
		chart = ChartFactory.createBarChart(null, null, null, dataSet, PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        return chart;
	}
}
