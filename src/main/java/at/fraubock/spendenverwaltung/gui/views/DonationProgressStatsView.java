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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.util.Pair;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.statistics.DonationDatasetGenerator;
import at.fraubock.spendenverwaltung.util.statistics.Operation;

public class DonationProgressStatsView extends InitializableView {

	private static final Logger log = Logger
			.getLogger(DonationProgressStatsView.class);

	private static final long serialVersionUID = 1L;
	private IDonationService donationService;
	private IFilterService filterService;
	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
	private JButton submit;
	private JButton cancel;
	private JPanel operationsPanel;
	private JLabel doSingleStat;
	private Filter showAllFilter;
	private JComboBox<Filter> firstFilter;
	private JLabel chooseFilter;
	private JLabel chooseOperation;
	private JComboBox<Operation> operationBox;
	private JComboBox<Filter> secondFilter;
	private JComboBox<Filter> thirdFilter;
	private JLabel chooseClass;
	private JComboBox<String> classBox;
	private List<Filter> donationFilters;
	private JFreeChart chart;
	private ChartPanel chartPanel;
	private List<Donation> donationList;

	private JPanel plotPanel;

	public DonationProgressStatsView(ComponentFactory componentFactory,
			ViewActionFactory viewActionFactory,
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

		try {
			donationFilters.addAll(filterService
					.getAllByFilter(FilterType.DONATION).a);
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this,
					"Ein unerwarteter Fehler ist aufgetreten.", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
	}

	private void setUpCreate() {

		operationsPanel = componentFactory.createPanel(750, 800);
		this.add(operationsPanel, "wrap");
		plotPanel = componentFactory.createPanel(700, 400);
		doSingleStat = componentFactory
				.createLabel("Statistische Berechnungen durchf\u00FChren ");
		doSingleStat.setFont(new Font("Headline", Font.PLAIN, 14));
		operationsPanel.add(doSingleStat, "wrap 30px");

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
				"Quartal", "Jahr", "Bundesland" };
		classBox = new JComboBox<String>(classification);
		classBox.setSelectedItem("Monat");
		operationsPanel.add(classBox, "gap 35, wrap 10px, growx");

		// choose operation then
		chooseOperation = componentFactory
				.createLabel("Operation ausw\u00E4hlen: ");
		operationsPanel.add(chooseOperation, "split 2");
		operationBox = new JComboBox<Operation>(Operation.values());
		operationBox.setSelectedItem(Operation.SUM);
		operationsPanel.add(operationBox, "gap 10, growx, wrap 10px");

		submit = new JButton();
		cancel = new JButton();
		operationsPanel.add(submit, "split 2");
		operationsPanel.add(cancel, "wrap 20px");

		operationsPanel.add(plotPanel);
	}

	private List<Donation> getDonations(Filter filter) {

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
		}
		return donationList;
	}

	public void init() {
		PlotAction calculateAction = new PlotAction();
		calculateAction.putValue(Action.NAME, "Berechnen");
		submit.setAction(calculateAction);

		Action cancelAction = viewActionFactory.getMainMenuViewAction();
		cancelAction.putValue(Action.NAME, "Abbrechen");
		cancelAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass()
				.getResource("/images/backInButton.png")));
		cancel.setAction(cancelAction);
		cancel.setFont(new Font("bigger", Font.PLAIN, 13));

	}

	private final class PlotAction extends AbstractAction {
		private static final long serialVersionUID = 2003305342391375273L;
		private List<Donation> donationList;
		private String name;
		Pair<List<Donation>, String> pair;
		private double result;

		@Override
		public void actionPerformed(ActionEvent e) {
			// first check if any filters are selected
			if (firstFilter.getSelectedIndex() == 0
					&& secondFilter.getSelectedIndex() == 0
					&& thirdFilter.getSelectedIndex() == 0) {
				JOptionPane.showMessageDialog(operationsPanel,
						"Bitte Auswahl an Einstellungen treffen.");
				return;
			}

			// now, create the datasets
			List<Pair<List<Donation>, String>> pairList = new ArrayList<Pair<List<Donation>, String>>();
			Operation operation = (Operation) operationBox.getSelectedItem();
			Class<? extends RegularTimePeriod> periodClass;
			switch ((String) classBox.getSelectedItem()) {
			case "Jahr":
				periodClass = Year.class;
				break;
			case "Tag":
				periodClass = Day.class;
				break;
			case "Woche":
				periodClass = Week.class;
				break;
			case "Monat":
				periodClass = Month.class;
				break;
			case "Quartal":
				periodClass = Quarter.class;
				break;
			case "Bundesland":
				periodClass = null; // flag to use provinces
				break;
			default:
				periodClass = Year.class;
				break;
			}

			if (firstFilter.getSelectedIndex() != 0) {
				donationList = getDonations((Filter) firstFilter
						.getSelectedItem());
				name = ((Filter) firstFilter.getSelectedItem()).getName();
				pair = new Pair<List<Donation>, String>(donationList, name);
				pairList.add(pair);
			}
			if (secondFilter.getSelectedIndex() != 0) {
				donationList = getDonations((Filter) secondFilter
						.getSelectedItem());
				name = ((Filter) secondFilter.getSelectedItem()).getName();
				pair = new Pair<List<Donation>, String>(donationList, name);
				pairList.add(pair);
			}
			if (thirdFilter.getSelectedIndex() != 0) {
				donationList = getDonations((Filter) thirdFilter
						.getSelectedItem());
				name = ((Filter) thirdFilter.getSelectedItem()).getName();
				pair = new Pair<List<Donation>, String>(donationList, name);
				pairList.add(pair);
			}

			if (periodClass != null) {
				// time series chart
				TimeSeriesCollection dataSets = DonationDatasetGenerator
						.createDataset(pairList, operation, periodClass);

				chart = createTimeSeriesChart(dataSets);
			} else {
				// "Bundesland" chosen
				CategoryDataset dataSet = DonationDatasetGenerator
						.createDatasetProvinciallyCategorized(pairList,
								operation);
				chart = createBarChart(dataSet);
			}
			chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new Dimension(700, 270));
			plotPanel.removeAll();
			plotPanel.repaint();
			plotPanel.add(chartPanel,"wrap");
			plotPanel.add(createExportPanel());
			plotPanel.validate();

		}
	}
	
	private JPanel createExportPanel() {
		JPanel panel = new JPanel();
		
		JButton exportButton = new JButton("Statistik exportieren");
		exportButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		final JFileChooser fileChooser = new JFileChooser();
		
		List<Filter> selected = new ArrayList<Filter>();
		if (firstFilter.getSelectedIndex() != 0) {
			selected.add((Filter) firstFilter
					.getSelectedItem());
		}
		if (secondFilter.getSelectedIndex() != 0) {
			selected.add((Filter) secondFilter
					.getSelectedItem());
		}
		if (thirdFilter.getSelectedIndex() != 0) {
			selected.add((Filter) thirdFilter
					.getSelectedItem());
		}
		
		JComboBox<Filter> cb = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(selected));
		panel.add(cb);
		exportButton.addActionListener(new ExportActionListener(fileChooser,cb));
		panel.add(exportButton);
		
		return panel;
		
	}

	private static JFreeChart createTimeSeriesChart(XYDataset dataset) {
		final JFreeChart chart = ChartFactory.createTimeSeriesChart(null,
				"Datum", "Betrag", dataset, true, true, true);
		final XYItemRenderer renderer = chart.getXYPlot().getRenderer();
		final StandardXYToolTipGenerator g = new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				DateFormat.getDateInstance(DateFormat.LONG),
				DecimalFormat.getCurrencyInstance(Locale.GERMANY));
		renderer.setBaseToolTipGenerator(g);

		return chart;
	}

	private static JFreeChart createBarChart(CategoryDataset dataset) {
		final JFreeChart chart = ChartFactory.createBarChart(null,
				"Bundesland", "Betrag", dataset, PlotOrientation.VERTICAL,
				true, true, false);
		return chart;
	}	
	
	private final class ExportActionListener implements ActionListener {
		
		private final JFileChooser fileChooser;
		private final JComboBox<Filter> cb;
		
		private ExportActionListener(JFileChooser fileChooser, JComboBox<Filter> cb) {
			this.fileChooser = fileChooser;
			this.cb = cb;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Filter filter = (Filter)cb.getSelectedItem();
			String csv = null;
			try {
				csv = filterService.convertResultsToCSVById(filter.getId());
			} catch (ServiceException e1) {
				JOptionPane.showMessageDialog(DonationProgressStatsView.this,
						"Der Filter konnte nicht exportiert werden.",
						"Error", JOptionPane.ERROR_MESSAGE);
				log.error("convertResultsToCSVById failed. filter_id='" + filter.getId() + "'");
			}

			fileChooser.setSelectedFile(new File("spendenstatistik.csv"));
			fileChooser.setFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".csv")
							|| f.isDirectory();
				}

				@Override
				public String getDescription() {
					return "CSV Dateien(*.csv)";
				}

			});

			if (fileChooser.showSaveDialog(DonationProgressStatsView.this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				FileWriter writer = null;
				try {
					writer = new FileWriter(file);
					writer.write(csv);
					writer.flush();
					writer.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(DonationProgressStatsView.this,
							"Die Datei konnte nicht beschrieben werden.",
							"Error", JOptionPane.ERROR_MESSAGE);
					log.error("File could not be written to. path='"
							+ file.getAbsolutePath() + "', text='" + csv + "'");
				}
			}
		}
	}
}
