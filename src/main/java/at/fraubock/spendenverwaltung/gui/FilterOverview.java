package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

import net.miginfocom.swing.MigLayout;

public class FilterOverview extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FilterOverview.class);
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private IFilterService filterService;
	private Overview overview;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	
	private FilterTableModel filterModel;
	private JTable showTable;
	private JScrollPane scrollPane;
	private List<Filter> filterList;
	private JPanel panel;
	private JToolBar toolbar;
	
	@SuppressWarnings("unused")
	private JComboBox<String[]> filterCombo;
	private JButton backButton;
	private JButton create;
	private JButton edit;
	private JButton delete;
	private JButton share;
	private JButton join;
	
	public FilterOverview(IFilterService filterService, IPersonService personService, IAddressService addressService, IDonationService donationService, Overview overview){
		super(new MigLayout());
		
		this.filterService = filterService;
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.overview = overview;
		//initTable();
		setUp();
	}
	
	public void initTable(){
		filterModel = new FilterTableModel();
		getFilter();
		showTable = new JTable(filterModel);
		showTable.setFillsViewportHeight(true);
		showTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(showTable);
		scrollPane.setPreferredSize(new Dimension(800, 800));
		
	}
	
	public void setUp(){
		new ActionHandler(this);
		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		panel = builder.createPanel(800, 800);
		this.add(panel);
		
		toolbar = builder.createToolbar();
		addComponentsToToolbar(toolbar);
		panel.add(toolbar, "wrap");
		//panel.add(scrollPane, "span 5, gaptop 25");
	}
	
	private void addComponentsToToolbar(JToolBar toolbar) {
		create = builder.createButton("Filter anlegen", buttonListener, "create_filter");
		edit = builder.createButton("Filter bearbeiten", buttonListener, "edit_filter");
		delete = builder.createButton("Filter l\u00F6schen", buttonListener, "delete_filter");
		share = builder.createButton("Filter freigeben", buttonListener, "share_filter");
		join = builder.createButton("Filter verkn\u00FCpfen", buttonListener, "join_filter");
		backButton = builder.createButton("Zur\u00FCck", buttonListener, "return_to_overview");
		toolbar.add(create);
		toolbar.add(edit);
		toolbar.add(delete);
		toolbar.add(share);
		toolbar.add(join);
		toolbar.add(backButton);
	}

	public FilterTableModel getFilterModel(){
		return this.filterModel;
	}
	
	private void getFilter(){
		filterList = new ArrayList<Filter>();
		
		try{
			filterList = filterService.getAll();
			log.info("List " + filterList.size() + " filter");
		}
		catch(ServiceException e){
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
		if(filterList == null){
			JOptionPane.showMessageDialog(this, "GetAll() returns null.", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		for(Filter f : filterList){
			filterModel.addFilter(f);
		}	
	}
	
	public void createFilter(){
		CreateFilter cf = new CreateFilter(filterService, personService, addressService, donationService, this);
		removeAll();
		revalidate();
		repaint();
		add(cf);
	}
	
	public void returnTo(){
		this.removeAll();
		this.revalidate();
		this.repaint();
		overview.removeAll();
		overview.revalidate();
		overview.repaint();
		overview.setUp();
	}

	
}