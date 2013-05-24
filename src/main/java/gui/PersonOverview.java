package gui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import service.IAddressService;
import service.IDonationService;
import service.IPersonService;

public class PersonOverview extends JPanel{

	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private Overview overview;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private JButton create;
	private JButton edit;
	private JButton delete;
	private JButton show;
	private JButton search;
	private JPanel panel;
	private JLabel createLabel;
	private JLabel editLabel;
	private JLabel deleteLabel;
	private JLabel showLabel;
	private JLabel searchLabel;
	
	public PersonOverview(IPersonService personService, IAddressService addressService, IDonationService donationService, Overview overview){
		super(new MigLayout());
	
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.overview = overview;
		setUp();
	}
	
	public void setUp(){
		System.out.println("Hello");
		
		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		
		panel = builder.createPanel();
		panel.setLayout(new MigLayout());
		panel.setPreferredSize(new Dimension(800, 800));
		
		this.add(panel);
		create = builder.createImageButton("images/template.jpg", buttonListener, "create_person");
		panel.add(create);
		
		edit = builder.createImageButton("images/template.jpg", buttonListener, "edit_person");
		panel.add(edit, "gap 30");
		
		delete = builder.createImageButton("images/template.jpg", buttonListener, "delete_person");
		panel.add(delete, "gap 30");
		
		show = builder.createImageButton("images/template.jpg", buttonListener, "show_person");
		panel.add(show, "gap 30");
		
		search = builder.createImageButton("images/template.jpg", buttonListener, "search_person");
		panel.add(search, "gap 30, wrap");
		
		createLabel = builder.createLabel("Person anlegen");
		panel.add(createLabel, "gap 5");
		
		editLabel = builder.createLabel("Person bearbeiten");
		panel.add(editLabel, "gap 25");
		
		deleteLabel = builder.createLabel("Person loeschen");
		panel.add(deleteLabel, "gap 30");
		
		showLabel = builder.createLabel("Personen anzeigen");
		panel.add(showLabel, "gap 22");
		
		searchLabel = builder.createLabel("Personen suchen");
		panel.add(searchLabel, "gap 28");
	}
	
	public void goToCreate(){
		//TODO: Broke on refactoring
//		CreatePerson cp = new CreatePerson(personService, addressService, donationService, this);
		removeAll();
		revalidate();
		repaint();
//		add(cp);
	}
	
	public void goToDelete(){
		DeletePerson cp = new DeletePerson(personService, addressService, this);
		removeAll();
		revalidate();
		repaint();
		add(cp);
	}
	
}
