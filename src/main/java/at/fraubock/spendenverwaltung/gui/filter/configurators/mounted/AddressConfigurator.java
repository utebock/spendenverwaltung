package at.fraubock.spendenverwaltung.gui.filter.configurators.mounted;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.configurators.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.gui.filter.configurators.property.StringComparator;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.LogicalOperator;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

/**
 * implements {@link ICriterionConfigurator} for {@link MountedFilterCriterion}s
 * that mount Donation filters and are added to person filters.
 * 
 * @author philipp muhoray
 * 
 */
public class AddressConfigurator extends JPanel implements
		ICriterionConfigurator {
	private static final long serialVersionUID = -7505945596725892100L;

	private String display;
	private JCheckBox hws;
	private JComboBox<StringComparator> property;
	private List<StringComparator> displayProp = new ArrayList<StringComparator>();

	public AddressConfigurator(String display) {
		super(new MigLayout());
		this.display = display;
		hws = new JCheckBox();
		add(new JLabel("Nur Hauptwohnsitz"));
		add(hws, "wrap");

		displayProp.add(new StringComparator(FilterProperty.ADDRESS_COUNTRY,
				"Land"));
		displayProp.add(new StringComparator(FilterProperty.ADDRESS_CITY,
				"Stadt"));
		displayProp.add(new StringComparator(FilterProperty.ADDRESS_POSTCODE,
				"PLZ"));
		displayProp.add(new StringComparator(FilterProperty.ADDRESS_STREET,
				"Strasse"));

		add(property = new JComboBox<StringComparator>(
				new SimpleComboBoxModel<StringComparator>(displayProp)));

		property.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringComparator model = (StringComparator) property
						.getSelectedItem();
				remove(3);
				add(model.getConfigComponent(), 3);
				repaint();
				revalidate();
			}
		});

		add(displayProp.get(0).getConfigComponent());
	}

	@Override
	public Criterion createCriterion() throws InvalidInputException {
		StringComparator comp = (StringComparator) property.getSelectedItem();
		Criterion crit = comp.createCriterion();
		if (hws.isSelected()) {
			ConnectedCriterion con = new ConnectedCriterion();
			PropertyCriterion prop = new PropertyCriterion();
			prop.compare(FilterProperty.ADDRESS_IS_MAIN, true);
			con.connect(crit, LogicalOperator.AND, prop);
			crit = con;
		}

		Filter filter = new Filter(FilterType.ADDRESS, crit);
		filter.setAnonymous(true);

		MountedFilterCriterion mCrit = new MountedFilterCriterion();
		mCrit.setMount(filter);
		mCrit.setRelationalOperator(RelationalOperator.GREATER_EQ);
		mCrit.setCount(1);
		return mCrit;
	}

	@Override
	public JComponent getConfigComponent() {
		return this;
	}

	@Override
	public String toString() {
		return display;
	}

	@Override
	public boolean applyCriterion(Criterion criterion) {
		if (criterion instanceof MountedFilterCriterion) {
			MountedFilterCriterion crit = (MountedFilterCriterion) criterion;
			Filter filter = crit.getMount();
			if (filter.getType() == FilterType.ADDRESS) {
				PropertyCriterion selected = null;
				Criterion head = filter.getCriterion();
				if(head instanceof ConnectedCriterion) {
					selected = (PropertyCriterion) ((ConnectedCriterion) head).getOperand1();
					hws.setSelected(true);
				} else {
					selected = (PropertyCriterion)head;
				}
				
				for(StringComparator comp: displayProp) {
					if(comp.applyCriterion(selected)) {
						property.setSelectedItem(comp);
					}
				}
				
				return true;
			}
		}
		return false;
	}
}
