package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

import net.miginfocom.swing.MigLayout;

public class ShowPerson extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private PersonOverview personOverview;
	private Person person;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private JPanel panel;
	private JTable addressTable;
	private JTable donationTable;
	private DonationTableModel donationModel;
	private AddressTableModel addressModel;
	private JScrollPane donationPane;
	private JScrollPane addressPane;
	private List<Address> addressList;
	private List<Donation> donationList;
	private JButton ok;
	private JButton cancel;
	
	private JLabel salutLabel;
	private JLabel salut;
	
	private JLabel titleLabel;
	private JLabel title;

	private JLabel companyLabel;
	private JLabel company;

	private JLabel given_nameLabel;
	private JLabel given_name;
	
	private JLabel surnameLabel;
	private JLabel surname;
	
	private JLabel telephoneLabel;
	private JLabel telephone;
	
	private JLabel mailLabel;
	private JLabel mail;
	
	private JLabel streetLabel;
	private JLabel street;
	
	private JLabel postalLabel;
	private JLabel postal;
	
	private JLabel cityLabel;
	private JLabel city;
	
	private JLabel countryLabel;
	private JLabel country;
	
	private JLabel notifyTypeLabel;
	private JLabel notifyType;
	
	private JLabel noteLabel;
	private JLabel note;
	
	private JLabel donationsLabel;
	
	private JLabel addressesLabel;
	
	public ShowPerson(Person person, IPersonService personService, IAddressService addressService, IDonationService donationService, PersonOverview personOverview){
		super(new MigLayout());
		
		this.person = person;
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.personOverview = personOverview;
		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		donationModel = new DonationTableModel();
		addressModel = new AddressTableModel();
		setUp();
	}
	
	public void setUp(){

		panel = builder.createPanel(700, 700);
		this.add(panel);
		
		
		titleLabel = builder.createLabel("Titel: ");
		title = builder.createLabel(person.getTitle());
		panel.add(titleLabel, "gap 150");
		panel.add(title, "wrap");

		companyLabel = builder.createLabel("Firma: ");
		company = builder.createLabel(person.getCompany());
		panel.add(companyLabel, "gap 150");
		panel.add(company, "wrap");

		given_nameLabel = builder.createLabel("Vorname: ");
		given_name = builder.createLabel(person.getGivenName());
		panel.add(given_nameLabel, "gap 150");
		panel.add(given_name, "wrap");
		
		surnameLabel = builder.createLabel("Nachname: ");
		surname = builder.createLabel(person.getSurname());
		panel.add(surnameLabel, "gap 150");
		panel.add(surname, "wrap");
		
		telephoneLabel = builder.createLabel("Telefonnummer: ");
		telephone = builder.createLabel(person.getTelephone());
		panel.add(telephoneLabel, "gap 150");
		panel.add(telephone, "wrap");
		
		mailLabel = builder.createLabel("E-Mail: ");
		mail = builder.createLabel(person.getEmail());
		panel.add(mailLabel, "gap 150");
		panel.add(mail, "wrap");
		
		streetLabel = builder.createLabel("Stra§e: ");
		street = builder.createLabel(person.getMainAddress().getStreet());
		panel.add(streetLabel, "gap 150");
		panel.add(street, "wrap");
		
		postalLabel = builder.createLabel("PLZ: ");
		postal = builder.createLabel(person.getMainAddress().getPostalCode());
		panel.add(postalLabel, "gap 150");
		panel.add(postal, "wrap");
		
		cityLabel = builder.createLabel("Ort: ");
		city = builder.createLabel(person.getMainAddress().getCity());
		panel.add(cityLabel, "gap 150");
		panel.add(city, "wrap");
		
		countryLabel = builder.createLabel("Land: ");
		country = builder.createLabel(person.getMainAddress().getCountry());
		panel.add(cityLabel, "gap 150");
		panel.add(city, "wrap");
		
		noteLabel = builder.createLabel("Notiz: ");
		note = builder.createLabel(person.getNote());
		panel.add(noteLabel, "gap 150");
		panel.add(note, "wrap");
		
		
		donationTable = new JTable(donationModel);
		donationTable.setFillsViewportHeight(true);
		donationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		donationPane = new JScrollPane(donationTable);
		donationPane.setPreferredSize(new Dimension(800,800));
		panel.add(donationPane, "wrap");
		
		addressTable = new JTable(addressModel);
		addressTable.setFillsViewportHeight(true);
		addressTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addressPane = new JScrollPane(addressTable);
		addressPane.setPreferredSize(new Dimension(800,800));
		panel.add(addressPane, "wrap");
		
		ok = builder.createButton("Loeschen", buttonListener, "delete_person_from_db");
		panel.add(ok);
		
		cancel = builder.createButton("Abbrechen", buttonListener, "cancel_delete_person_from_db");
		panel.add(cancel, "split 2");
		
		getAddresses();	
	}
	
	private void getAddresses(){
		addressList = new ArrayList<Address>();

		addressList = person.getAddresses();
		if(addressList == null){
			JOptionPane.showMessageDialog(this, "Addresslist returns null.", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		for(Address a : addressList){
			addressModel.addAddress(a);
			addressTable.revalidate();
		}	
	}
	
	private void getDonations(){
		donationList = new ArrayList<Donation>();

		try {
			donationList = donationService.getByPerson(person);
		} catch(ServiceException e){
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
		if(donationList == null){
			JOptionPane.showMessageDialog(this, "Donationlist returns null.", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		for(Donation d : donationList){
			donationModel.addDonation(d);
			donationTable.revalidate();
		}	
	}

	public void returnTo(){
		this.removeAll();
		this.revalidate();
		this.repaint();
		personOverview.removeAll();
		personOverview.revalidate();
		personOverview.repaint();
		personOverview.setUp();
	}
}
