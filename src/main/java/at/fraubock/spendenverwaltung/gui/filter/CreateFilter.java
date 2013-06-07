package at.fraubock.spendenverwaltung.gui.filter;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.ButtonListener;
import at.fraubock.spendenverwaltung.gui.ComponentBuilder;
import at.fraubock.spendenverwaltung.gui.CustomTextField;
import at.fraubock.spendenverwaltung.gui.FilterOverview;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.to.FilterTO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class CreateFilter extends JPanel {

	private static final long serialVersionUID = 1L;

	private int MAXIMUM_CRITERIONS = 4;

	private FilterOverview filterOverview;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;

	private IFilterService filterService;
	private FilterType type;
	private ConfiguratorFactory factory;
	private boolean andOperator = true;

	private CustomTextField nameField;
	private JButton plusButton;

	private JLabel headline;
	private JPanel filterNamePanel;
	private JPanel operatorPicker;
	private JPanel criterionSelectorPanel;
	private JPanel controlButtonPanel;
	private List<CriterionSelector> selectors = new ArrayList<CriterionSelector>();
	private List<JButton> minusButtons = new ArrayList<JButton>();

	public CreateFilter(FilterType type, IFilterService filterService,
			FilterOverview filterOverview) {
		super(new MigLayout());

		// set attributes
		this.filterService = filterService;
		this.filterOverview = filterOverview;
		this.buttonListener = new ButtonListener(this);
		this.builder = new ComponentBuilder();
		this.type = type;
		this.factory = new ConfiguratorFactory(type);
		this.criterionSelectorPanel = builder.createPanel(500, 0);

		// set up GUI
		setUpCreate();

		this.add(criterionSelectorPanel, "wrap, h 800");

		// add plus button
		plusButton = builder.createImageButton("/images/plusButton.gif",
				buttonListener, "plusButton_create_filter");
		criterionSelectorPanel.add(plusButton, "wrap, gapleft 7, gaptop 10");

		// add controls
		this.controlButtonPanel = new JPanel();
		controlButtonPanel.add(builder.createButton("Anlegen", buttonListener,
				"create_filter_in_db"));

		controlButtonPanel.add(builder.createButton("Abbrechen",
				buttonListener, "cancel_filter_in_db"));

		add(controlButtonPanel);
	}

	private void setUpCreate() {

		// create headline
		headline = builder.createLabel("Neuen Filter f\u00FCr "
				+ type.getDisplayName() + " anlegen:");
		headline.setFont(new Font("Headline", Font.PLAIN, 14));
		add(headline, "wrap, gapbottom 10");

		// create filter name panel
		this.filterNamePanel = new JPanel();
		nameField = new CustomTextField(100);
		filterNamePanel.add(builder.createLabel("Filtername: "));
		filterNamePanel.add(nameField, "growx");
		add(filterNamePanel, "wrap, gapbottom 10");

		// create operator picker panel
		this.operatorPicker = new JPanel();
		final JLabel operatorLabel = new JLabel(" Kriterien erfüllt sind:");
		operatorPicker.add(new JLabel("Gültig, wenn"));
		final JComboBox<String> operatorCombo = new JComboBox<String>(
				new SimpleComboBoxModel<String>("alle", "ein"));
		operatorCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String operator = (String) operatorCombo.getModel()
						.getSelectedItem();
				if ("alle".equals(operator)) {
					andOperator = true;
					operatorLabel.setText(" Kriterien erfüllt sind:");
				} else {
					andOperator = false;
					operatorLabel.setText(" Kriterium erfüllt ist:");
				}
				operatorLabel.repaint();
				operatorLabel.revalidate();
			}
		});
		operatorPicker.add(operatorCombo);
		operatorPicker.add(operatorLabel);
		add(operatorPicker, "wrap, gapbottom 10");

		// add default criterion selector
		gainMore();

	}

	public void gainMore() {
		// create selector
		JButton minusButton = builder.createImageButton(
				"/images/minusButton.gif", buttonListener,
				"minusButton_create_filter");
		CriterionSelector selectorComp = new CriterionSelector(
				factory.getConfigurators());

		// add selector to panel and list
		selectors.add(selectorComp);
		minusButtons.add(minusButton);
		criterionSelectorPanel.add(selectorComp, "w 550!");
		criterionSelectorPanel.add(minusButton, "wrap");
		
		if(plusButton!=null) {
			criterionSelectorPanel.remove(plusButton);
			criterionSelectorPanel.add(plusButton, "wrap, gapleft 7, gaptop 10");
		}

		if (selectors.size() == MAXIMUM_CRITERIONS) {
			plusButton.setEnabled(false);
		}

		repaint();
		revalidate();
	}

	public void removeSelector(Object button) {
		// see to which selector panel the button belongs
		int indexMinusButton = minusButtons.indexOf(button);

		criterionSelectorPanel.remove(selectors.get(indexMinusButton));
		selectors.remove(selectors.get(indexMinusButton));
		criterionSelectorPanel.remove(minusButtons.get(indexMinusButton));
		minusButtons.remove(indexMinusButton);
		plusButton.setEnabled(true);
		repaint();
		revalidate();
	}

	public void createFilter() {
		// get filter name
		String name = nameField.getText();
		if (name.equals("") || name == null) {
			JOptionPane.showMessageDialog(this,
					"Bitte geben Sie einen Namen für den Filter an.", "Warn",
					JOptionPane.WARNING_MESSAGE);
			nameField.invalidateInput();
			return;
		}

		// create filter
		FilterTO filter = new FilterTO();
		filter.setType(type);
		filter.setOperator(andOperator ? LogicalOperator.AND
				: LogicalOperator.OR);
		filter.setName(nameField.getText());

		try {
			List<Criterion> crits = new ArrayList<Criterion>();
			for (CriterionSelector sel : selectors) {
				crits.add(sel.toCriterionTO());
			}

			filter.setCriterions(crits);

			filterService.create(filter);

			JOptionPane.showMessageDialog(this, "Filter erfolgreich angelegt!",
					"Info", JOptionPane.INFORMATION_MESSAGE);
			returnTo();
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this,
					"Ein unerwarteter Fehler ist aufgetreten.", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (InvalidInputException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Warn",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public void returnTo() {
		this.removeAll();
		this.revalidate();
		this.repaint();
		filterOverview.removeAll();
		filterOverview.revalidate();
		filterOverview.repaint();
		filterOverview.setUp();
	}
}
