package at.fraubock.spendenverwaltung.gui;

import java.awt.Component;
import java.awt.Font;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

import net.miginfocom.swing.MigLayout;

public class CreateFilter extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private IFilterService filterService;
	private IPersonService personService;
	private IAddressService addressService;
	private FilterOverview filterOverview;
	private IDonationService donationService;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private ActionHandler actionHandler;
	private FilterTableModel filterModel;
	private JPanel panel;
	private JLabel headline;
	private JLabel empty;
	private JLabel nameLabel;
	private JTextField nameField;
	private JComboBox comboPersonAttributes;
	private JComboBox comboOperators;
	private JButton plusButton;
	private JTextField inputField;
	private JButton minusButton;
	private JButton ok;
	private JButton cancel;
	private Component comboPersonAttributes2;
	private JComboBox comboOperators2;
	private JPanel panel2;
	private JTextField inputField2;
	private JButton plusButton2;
	private JButton minusButton2;
	private JPanel panel3;
	private JLabel labelTest;
	

	public CreateFilter(IFilterService filterService, IPersonService personService, IAddressService addressService, IDonationService donationService, FilterOverview filterOverview){
		super(new MigLayout());
		
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.filterOverview = filterOverview;
		this.filterModel = this.filterOverview.getFilterModel();
		buttonListener = new ButtonListener(this);
		actionHandler = new ActionHandler(this);
		builder = new ComponentBuilder();
		
		panel = builder.createPanel(800,300);
		this.add(panel, "wrap");
		panel2 = builder.createPanel(800, 300);
		this.add(panel2, "wrap");
		panel3 = builder.createPanel(800, 200);
		this.add(panel3);
		setUpCreate();
	}

	private void setUpCreate() {
		headline = builder.createLabel("Neuen Filter anlegen");
		headline.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(headline, "wrap");
		empty = builder.createLabel("		");
		panel.add(empty, "wrap");
		
		nameLabel = builder.createLabel("Filtername: ");
		nameField = builder.createTextField(150);
		panel.add(nameLabel, "split 2");
		panel.add(nameField, "growx, wrap");
		
		String[] personAttributes = new String[]{"Anrede", "Titel", "Firma", "Vorname", "Nachname",
				"Telefon", "E-Mail", "Stra\u00DFe", "PLZ", "Ort", "Land", "Notification Type", "Notiz"};
		
		comboPersonAttributes = builder.createComboBox(personAttributes, actionHandler);
		panel.add(comboPersonAttributes, "split 3");
		String[] operators = new String[]{"ist gleich", "ist gr\u00F6\u00DFer", "ist gr\u00F6\u00DFer gleich", 
				"ist kleiner", "ist kleiner gleich", "ist \u00E4hnlich"};
		comboOperators = builder.createComboBox(operators, actionHandler);
		panel.add(comboOperators);
		inputField = builder.createTextField(150);
		panel.add(inputField);
		plusButton = builder.createImageButton("/images/plusButton.gif", buttonListener, "plusButton_create_filter");
		minusButton = builder.createImageButton("/images/minusButton.gif", buttonListener, "minusButton_create_filter");
		panel.add(plusButton, "split 2");
		panel.add(minusButton, "wrap");
		
		/*
		 
		panel.add(ok, "split 2");
		cancel = builder.createButton("Abbrechen", buttonListener, "cancel_filter_in_db");
		panel.add(cancel, "wrap");
		*/
	}
	
	public void showButtons(){
		ok = builder.createButton("Anlegen", buttonListener, "create_filter_in_db");
		panel3.add(ok, "split 2");
		cancel = builder.createButton("Abbrechen", buttonListener, "cancel_filter_in_db");
		panel3.add(cancel, "wrap");

	}
	public void gainMore(){
	
		String[] personAttributes2 = new String[]{"Anrede", "Titel", "Firma", "Vorname", "Nachname",
				"Telefon", "E-Mail", "Stra\u00DFe", "PLZ", "Ort", "Land", "Notification Type", "Notiz"};
		
		comboPersonAttributes2 = builder.createComboBox(personAttributes2, actionHandler);
		panel2.add(comboPersonAttributes2, "split 3");
		String[] operators2 = new String[]{"ist gleich", "ist gr\u00F6\u00DFer", "ist gr\u00F6\u00DFer gleich", 
				"ist kleiner", "ist kleiner gleich", "ist \u00E4hnlich"};
		comboOperators2 = builder.createComboBox(operators2, actionHandler);
		panel2.add(comboOperators2);
		inputField2 = builder.createTextField(150);
		panel2.add(inputField2);
		plusButton2 = builder.createImageButton("/images/plusButton.gif", buttonListener, "plusButton_create_filter");
		minusButton2 = builder.createImageButton("/images/minusButton.gif", buttonListener, "minusButton_create_filter");
		panel2.add(plusButton2, "split 2");
		panel2.add(minusButton2, "wrap");
	}
	
	public void createFilter(){
		
	}
	
	public void returnTo(){
		this.removeAll();
		this.revalidate();
		this.repaint();
		filterOverview.removeAll();
		filterOverview.revalidate();
		filterOverview.repaint();
		filterOverview.setUp();
	}
}
