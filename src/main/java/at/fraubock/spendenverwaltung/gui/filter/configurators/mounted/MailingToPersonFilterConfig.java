package at.fraubock.spendenverwaltung.gui.filter.configurators.mounted;

import java.awt.Font;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.CustomTextField;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationalOperatorGuiWrapper;
import at.fraubock.spendenverwaltung.gui.filter.configurators.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.util.FilterType;

/**
 * implements {@link ICriterionConfigurator} for {@link MountedFilterCriterion}s
 * that mount Mailing filters and are added to person filters.
 * 
 * @author philipp muhoray
 * 
 */
public class MailingToPersonFilterConfig extends JPanel implements
		ICriterionConfigurator {
	private static final long serialVersionUID = -8507188088817762907L;

	private JComboBox<Filter> filterBox;
	private String display;
	private RelationalOperatorPicker picker;
	private CustomTextField amount;
	private JPanel firstLine = new JPanel();
	private JPanel secondLine = new JPanel();

	public MailingToPersonFilterConfig(String display, List<Filter> filters) {
		super(new MigLayout());
		this.display = display;

		JLabel descr = new JLabel("Die Person hat von diesen Aussendungen: ");
		descr.setFont(new Font("Headline", Font.PLAIN, 14));
		firstLine.add(descr);
		firstLine.add(filterBox = new JComboBox<Filter>(
				new SimpleComboBoxModel<Filter>(filters)), "wrap");
		add(firstLine, "wrap");
		secondLine.add(picker = new RelationalOperatorPicker(
				RelationType.FOR_NUMBER_AND_DATE));
		secondLine.add(amount = new CustomTextField(5));
		amount.setText("10");

		JLabel gespendet = new JLabel("erhalten");
		gespendet.setFont(new Font("Headline", Font.PLAIN, 14));
		secondLine.add(gespendet);
		add(secondLine);
	}

	@Override
	public Criterion createCriterion() throws InvalidInputException {
		return null;
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
			if (crit.getMount().getType() == FilterType.MAILING) {
				this.picker.setSelectedItem(RelationalOperatorGuiWrapper
						.getForOperator(crit.getRelationalOperator()));
				this.filterBox.setSelectedItem(crit.getMount());
				if (crit.getCount() != null) {
					this.amount.setText("" + crit.getCount());
				}

				return true;
			}
		}
		return false;
	}
}
