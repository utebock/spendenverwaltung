package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Color;
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
import org.jdesktop.swingx.JXDatePicker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
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
import at.fraubock.spendenverwaltung.util.Pair;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.LogicalOperator;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;
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
	private JXDatePicker fromDatePicker;
	private JXDatePicker toDatePicker;
	private JLabel fromLabel;
	private JLabel toLabel;
	private JLabel infoLabel;

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
		operationsPanel.add(mailingTypeLabel, "split2");
		operationsPanel.add(mailingTypes, "gap 30, wrap, growx");
		
		fromLabel = componentFactory.createLabel("Datum von:");
		fromDatePicker = new JXDatePicker(new java.util.Date());
		operationsPanel.add(fromLabel, "split2");
		operationsPanel.add(fromDatePicker, "gap 140, wrap, growx");
		
		toLabel = componentFactory.createLabel("Datum bis:");
		toDatePicker = new JXDatePicker(new java.util.Date());
		operationsPanel.add(toLabel, "split2");
		operationsPanel.add(toDatePicker, "gap 143, wrap 30px, growx");
		
		submit = new JButton();
		cancel = new JButton();
		operationsPanel.add(submit, "split2");
		operationsPanel.add(cancel, "wrap 20px");
		
		infoLabel = componentFactory.createLabel("F\u00FCr diesen Aussendungstyp existieren noch keine bestaetigten Aussendungen.");
		infoLabel.setFont(new Font("Dialog", Font.PLAIN, 16));

		operationsPanel.add(plotPanel, "span 2");
	}

	private List<Mailing> getMailings() {
		Filter filter = new Filter();
		filter.setType(FilterType.MAILING);
		
		PropertyCriterion criterionType = new PropertyCriterion();
		PropertyCriterion criterionFrom = new PropertyCriterion();
		PropertyCriterion criterionTo = new PropertyCriterion();
		
		criterionType.compare(FilterProperty.MAILING_TYPE, RelationalOperator.EQUALS, mailingTypes.getSelectedItem().toString());
		criterionFrom.compare(FilterProperty.MAILING_DATE, RelationalOperator.GREATER_EQ, fromDatePicker.getDate());
		criterionTo.compare(FilterProperty.MAILING_DATE, RelationalOperator.LESS_EQ, toDatePicker.getDate());
		
		ConnectedCriterion connected1 = new ConnectedCriterion();
		connected1.connect(criterionFrom, LogicalOperator.AND, criterionTo);
		
		ConnectedCriterion connected2 = new ConnectedCriterion();
		connected2.connect(connected1, LogicalOperator.AND, criterionType);
		
		filter.setCriterion(connected2);
		
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
				.getResource("/images/backInButton.png")));
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
	        
	        //get size of each mailing and put it with the date of the mailing in the collection
	        for(Mailing m : mailingList){
	        	try {
	        		int mailingSize = mailingService.getSize(m);
	        		if(data.getKeys().contains(m.getDate()))
	        			mailingSize = data.getValue(m.getDate()).intValue() + mailingSize;
	        		
	        		if(mailingSize > maxY)
	        			maxY = mailingSize;
	        		
	        		if(mailingSize > 0)
	        			data.addValue(m.getDate(), mailingSize);
				} catch (ServiceException e1) {
					e1.printStackTrace();
					return;
				}
	        }

	        CategoryDataset dataset = DatasetUtilities.createCategoryDataset("Anzahl Aussendungen", data);
	        JFreeChart chart = ChartFactory.createBarChart("Aussendungsentwicklung","Datum","Anzahl Aussendungen",dataset,PlotOrientation.VERTICAL,false,true,false);
	        chart.setBackgroundPaint(new Color(238, 238, 238));
	        
	        //Switch from a Bar Rendered to a LineAndShapeRenderer so the chart looks like an XYChart
	        CategoryPlot plot = (CategoryPlot) chart.getPlot();
	        plot.setBackgroundAlpha((float) 0.2);
	       
	        
	        BarRenderer renderer = (BarRenderer) plot.getRenderer();
	        renderer.setMaximumBarWidth(.1);
	        renderer.setSeriesPaint(0, new Color(229,222,107));
	        renderer.setDrawBarOutline(false);

	        NumberAxis numberAxis = (NumberAxis)plot.getRangeAxis();        
	        numberAxis.setRange(new Range(0,maxY)); 
	        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	        
	        CategoryAxis domainAxis = plot.getDomainAxis();
	        
	        double rotation = 0;
	        if(data.getKeys().size() > 7)
	        	rotation = Math.PI / 6.0;
	        else if(data.getKeys().size() > 20)
	        	rotation = Math.PI/2;
	        
	        if(rotation != 0){
		        domainAxis.setCategoryLabelPositions(
		            CategoryLabelPositions.createUpRotationLabelPositions(rotation)
		        );
	        }
	        
			chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new Dimension(700, 270));
			plotPanel.removeAll();
			plotPanel.repaint();
			
			if(mailingList.isEmpty())
				//plotPanel.add(infoLabel);
				JOptionPane.showMessageDialog(plotPanel, "F\u00FCr diesen Aussendungstyp existieren noch keine best\u00E4tigten Aussendungen.", "Warnung", JOptionPane.WARNING_MESSAGE);
			else
				plotPanel.add(chartPanel);
			plotPanel.validate();
		}
	}
}
