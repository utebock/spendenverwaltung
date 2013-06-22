package at.fraubock.spendenverwaltung.gui.filter.configurators.mounted;

import java.awt.Font;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.components.NumericTextField;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationalOperatorGuiWrapper;
import at.fraubock.spendenverwaltung.gui.filter.configurators.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.util.filter.FilterType;

/**
 * implements {@link ICriterionConfigurator} for {@link MountedFilterCriterion}s
 * that mount person filters and are added to mailing filters.
 * 
 * @author philipp muhoray
 * 
 */
public class PersonToMailingFilterConfig extends JPanel implements
		ICriterionConfigurator {
	private static final long serialVersionUID = -8507188088817762907L;

	private JComboBox<Filter> filterBox;
	private String display;
	private RelationalOperatorPicker picker;
	private NumericTextField amount;
	private JPanel firstLine = new JPanel();
	private JPanel secondLine = new JPanel();

	public PersonToMailingFilterConfig(String display, List<Filter> filters) {
		super(new MigLayout());
		this.display = display;

		JLabel descr = new JLabel("Die Aussendung ist an diese Personen: ");
		descr.setFont(new Font("Headline", Font.PLAIN, 14));
		firstLine.add(descr);
		firstLine.add(filterBox = new JComboBox<Filter>(
				new SimpleComboBoxModel<Filter>(filters)), "wrap");
		add(firstLine, "wrap");
		secondLine.add(picker = new RelationalOperatorPicker(
				RelationType.FOR_NUMBER));
		secondLine.add(amount = new NumericTextField());
		amount.setText("10");

		JLabel gesendet = new JLabel("mal verschickt worden");
		gesendet.setFont(new Font("Headline", Font.PLAIN, 14));
		secondLine.add(gesendet);
		add(secondLine);
	}

	private Double getNumber() throws InvalidInputException {
		if (amount.validateContents()) {
			return amount.getNumericValue();
		} else {
			throw new InvalidInputException(
					"Bitte geben Sie eine g\u00FCltige Zahl ein!");
		}
	}

	@Override
	public Criterion createCriterion() throws InvalidInputException {
		Filter filter = (Filter) filterBox.getModel().getSelectedItem();

		MountedFilterCriterion crit = new MountedFilterCriterion();
		crit.setMount(filter);
		crit.setRelationalOperator(picker.getPickedOperator());
		Double number = getNumber();

		if ((int) ((double) number) == number) {
			crit.setCount(number.intValue());
		} else {
			amount.invalidateInput();
			throw new InvalidInputException(
					"Bitte geben Sie eine ganze Zahl ein!");
		}

		return crit;
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
			if (crit.getMount().getType() == FilterType.PERSON) {
				this.picker.setSelectedItem(RelationalOperatorGuiWrapper
						.getForOperator(crit.getRelationalOperator()));
				this.filterBox.setSelectedItem(crit.getMount());
				if (crit.getCount() != null) {
					this.amount.setNumericValue((double) crit.getCount());
				}

				return true;
			}
		}
		return false;
	}
}
