package at.fraubock.spendenverwaltung.gui.filter.configurators.mounted;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
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
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;

/**
 * implements {@link ICriterionConfigurator} for {@link MountedFilterCriterion}s
 * that mount Donation filters and are added to person filters.
 * 
 * @author philipp muhoray
 * 
 */
public class DonationToPersonFilterConfig extends JPanel implements
		ICriterionConfigurator {
	private static final long serialVersionUID = -7505945596725892100L;

	private JComboBox<Filter> filterBox;
	private String display;
	private RelationalOperatorPicker picker;
	private CustomTextField amount;
	private boolean euro = true;
	private JPanel firstLine = new JPanel();
	private JPanel secondLine = new JPanel();
	private JLabel euroLabel;

	public DonationToPersonFilterConfig(String display, List<Filter> filters) {
		super(new MigLayout());
		this.display = display;

		if (filters.isEmpty()) {
			add(new JLabel("Bitte legen Sie zuerst einen Spendenfilter an."));
		} else {
			JLabel descr = new JLabel("Die Person hat in diesen Spenden: ");
			descr.setFont(new Font("Headline", Font.PLAIN, 14));
			firstLine.add(descr);
			firstLine.add(filterBox = new JComboBox<Filter>(
					new SimpleComboBoxModel<Filter>(filters)), "wrap");
			add(firstLine, "wrap");
			secondLine.add(picker = new RelationalOperatorPicker(
					RelationType.FOR_NUMBER_AND_DATE));
			secondLine.add(amount = new CustomTextField(5));
			amount.setText("10");

			euroLabel = new JLabel("\u20AC", JLabel.CENTER);
			euroLabel.setPreferredSize(new Dimension(45, 22));
			euroLabel.setFont(new Font("Headline", Font.PLAIN, 18));
			euroLabel.setBorder(BorderFactory.createRaisedBevelBorder());
			euroLabel.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
				}

				@Override
				public void mouseClicked(MouseEvent arg0) {
					if (euro) {
						euroLabel.setText("mal");
						euro = false;
					} else {
						euroLabel.setText("\u20AC");
						euro = true;
					}
					euroLabel.revalidate();
					euroLabel.repaint();

				}
			});
			secondLine.add(euroLabel, "w 200!");

			JLabel gespendet = new JLabel("gespendet");
			gespendet.setFont(new Font("Headline", Font.PLAIN, 14));
			secondLine.add(gespendet);
			add(secondLine);
		}
	}

	private Double getNumber() throws InvalidInputException {
		try {
			return Double.valueOf(amount.getText());
		} catch (NumberFormatException e) {
			amount.invalidateInput();
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

		if (euro) {
			crit.setProperty(FilterProperty.DONATION_AMOUNT);
			crit.setSum(number);
		} else {
			if ((int) ((double) number) == number) {
				crit.setCount(number.intValue());
			} else {
				amount.invalidateInput();
				throw new InvalidInputException(
						"Bitte geben Sie eine ganze Zahl ein!");
			}
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
			if (crit.getMount().getType() == FilterType.DONATION) {
				this.picker.setSelectedItem(RelationalOperatorGuiWrapper
						.getForOperator(crit.getRelationalOperator()));
				this.filterBox.setSelectedItem(crit.getMount());
				if (crit.getCount() != null) {
					this.amount.setText("" + crit.getCount());
					euro = false;
					euroLabel.setText("mal");
				} else {
					this.amount.setText("" + crit.getSum());
					euro = true;
					euroLabel.setText("\u20AC");
				}

				return true;
			}
		}
		return false;
	}
}
