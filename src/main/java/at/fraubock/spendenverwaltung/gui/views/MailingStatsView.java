package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
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
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing.MailingType;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;
import at.fraubock.spendenverwaltung.util.Pair;
import at.fraubock.spendenverwaltung.util.RelationalOperator;
import at.fraubock.spendenverwaltung.util.statistics.DonationTimeStatisticDatasetGenerator;
import at.fraubock.spendenverwaltung.util.statistics.Operation;

public class MailingStatsView extends InitializableView {

	private static final Logger log = Logger
			.getLogger(MailingStatsView.class);

	private static final long serialVersionUID = 1L;
	private IMailingService mailingService;
	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
	private JButton submit;
	private JButton cancel;
	private JPanel operationsPanel;
	private JLabel doSingleStat;
	private JLabel mailingTypeLabel;
	private JComboBox<String> mailingTypes;
	private ChartPanel chartPanel;
	private List<Mailing> mailingList;

	private JPanel plotPanel;

	public MailingStatsView(ComponentFactory componentFactory,
			ViewActionFactory viewActionFactory,
			IMailingService mailingService, IFilterService filterService) {
		this.componentFactory = componentFactory;
		this.viewActionFactory = viewActionFactory;
		this.mailingService = mailingService;
		setUpCreate();
	}

	public void setMailingService(IMailingService mailingService) {
		this.mailingService = mailingService;
	}

	public void setComponentFactory(ComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
	}

	public void setViewActionFactory(ViewActionFactory viewActionFactory) {
		this.viewActionFactory = viewActionFactory;
	}

	private void setUpCreate() {

		operationsPanel = componentFactory.createPanel(700, 800);
		this.add(operationsPanel, "wrap");
		plotPanel = componentFactory.createPanel(700, 400);
		doSingleStat = componentFactory
				.createLabel("Statistische Berechnungen durchf\u00FChren ");
		doSingleStat.setFont(new Font("Headline", Font.PLAIN, 14));
		operationsPanel.add(doSingleStat, "wrap 30px");

		mailingTypeLabel = componentFactory.createLabel("Aussendungstyp ausw\u00E4hlen:");
		
		mailingTypes = new JComboBox<String>(MailingType.toStringArray());
		operationsPanel.add(mailingTypeLabel, "split 2");
		operationsPanel.add(mailingTypes, "wrap");
		

		submit = new JButton();
		cancel = new JButton();
		operationsPanel.add(submit, "split 2");
		operationsPanel.add(cancel, "wrap 20px");

		operationsPanel.add(plotPanel);
	}

	private List<Mailing> getMailings() {
		Filter filter = new Filter();
		filter.setType(FilterType.MAILING);
		
		PropertyCriterion criterionType = new PropertyCriterion();
		criterionType.compare(FilterProperty.MAILING_TYPE, RelationalOperator.EQUALS, mailingTypes.getSelectedItem().toString());
		filter.setCriterion(criterionType);
		
		try {
			mailingList = mailingService.getByFilter(filter);
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this, "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
					  "Fehler", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
		
		log.info("List " + mailingList.size() + " Mailings");
		return mailingList;
	}

	public void init() {
		PlotAction calculateAction = new PlotAction();
		calculateAction.putValue(Action.NAME, "Berechnen");
		submit.setAction(calculateAction);

		Action cancelAction = viewActionFactory.getMainMenuViewAction();
		cancelAction.putValue(Action.NAME, "Abbrechen");
		cancelAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass()
				.getResource("/images/backButton.jpg")));
		cancel.setAction(cancelAction);
		cancel.setFont(new Font("bigger", Font.PLAIN, 13));

	}

	private final class PlotAction extends AbstractAction {
		private static final long serialVersionUID = 2003305342391375273L;
		private List<Mailing> mailingList;

		@Override
		public void actionPerformed(ActionEvent e) {
			mailingList = getMailings();
	        DefaultKeyedValues data = new DefaultKeyedValues();
	        int maxY = 0;
	        
	        for(Mailing m : mailingList){
	        	try {
	        		int mailingSize = mailingService.getSize(m);
	        		
	        		if(mailingSize > maxY)
	        			maxY = mailingSize;
	        		
					data.addValue(m.getDate(), mailingSize);
				} catch (ServiceException e1) {
					e1.printStackTrace();
					return;
				}
	        }

	        CategoryDataset dataset = DatasetUtilities.createCategoryDataset("Anzahl Aussendungen", data);
	        JFreeChart chart = ChartFactory.createBarChart("Anzahl Aussendungen","Date","Anzahl Aussendungen",dataset,PlotOrientation.VERTICAL,true,true,false);
	        
	        //Switch from a Bar Rendered to a LineAndShapeRenderer so the chart looks like an XYChart
	        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
	        renderer.setBaseLinesVisible(true); //TUrn of the lines
	        CategoryPlot plot = (CategoryPlot) chart.getPlot();
	        plot.setRenderer(0, renderer);

	        NumberAxis numberAxis = (NumberAxis)plot.getRangeAxis();        
	        numberAxis.setRange(new Range(0,maxY)); 

			chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new Dimension(700, 270));
			plotPanel.removeAll();
			plotPanel.repaint();
			plotPanel.add(chartPanel);
			plotPanel.validate();
		}
	}
}
