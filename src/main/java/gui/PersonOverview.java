package gui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import service.IAddressService;
import service.IPersonService;

public class PersonOverview extends JPanel{

	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private Overview overview;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private JButton create;
	private JButton edit;
	private JButton delete;
	private JButton show;
	private JButton search;
	private JPanel panel;
	
	public PersonOverview(IPersonService personService, IAddressService addressService, Overview overview){
		super(new MigLayout());
		
		this.overview = overview;
		this.personService = personService;
		this.addressService = addressService;
		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		panel = builder.createPanel();
		panel.setLayout(new MigLayout());
		panel.setPreferredSize(new Dimension(800, 800));
		
		this.add(panel);
		
		create = builder.createImageButton("images/template.jpg", buttonListener, "create_person");
		panel.add(create);
		
		edit = builder.createImageButton("images/template.jpg", buttonListener, "edit_person");
		panel.add(edit, "gap 25");
		
		delete = builder.createImageButton("images/template.jpg", buttonListener, "delete_person");
		panel.add(delete, "gap 25");
		
		show = builder.createImageButton("images/template.jpg", buttonListener, "show_person");
		panel.add(show, "gap 25");
		
		search = builder.createImageButton("images/template.jpg", buttonListener, "search_person");
		panel.add(search);
		
		
	}
	
}
