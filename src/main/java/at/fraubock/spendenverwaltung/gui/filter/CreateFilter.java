package at.fraubock.spendenverwaltung.gui.filter;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.ComponentBuilder;
import at.fraubock.spendenverwaltung.gui.CustomTextField;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.MainFilterView;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.views.ViewActionFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter.FilterPrivacyStatus;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.to.FilterTO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class CreateFilter extends JPanel {

	private static final long serialVersionUID = 1L;

	private int MAXIMUM_CRITERIONS = 4;

	private MainFilterView filterOverview;
	private ComponentBuilder builder;

	private IFilterService filterService;
	private FilterType type;
	private ConfiguratorFactory factory;
	private Filter editFilter;
	private boolean andOperator = true;

	private CustomTextField nameField;
	private JButton plusButton;

	private ViewActionFactory viewActionFactory;

	private JLabel headline;
	private JPanel operatorPicker;
	private JPanel criterionSelectorPanel;
	private JPanel controlButtonPanel;
	private List<CriterionSelector> selectors = new ArrayList<CriterionSelector>();
	private List<JButton> minusButtons = new ArrayList<JButton>();

	private JComboBox<String> privacyComboBox;

	public CreateFilter(FilterType type, IFilterService filterService,
			MainFilterView overview, ViewActionFactory viewActionFactory) {
		this(type, filterService, overview, null, viewActionFactory);
	}

	public CreateFilter(FilterType type, IFilterService filterService,
			MainFilterView filterOverview, Filter editFilter,
			ViewActionFactory viewActionFactory) {
		super(new MigLayout());

		// set attributes
		this.viewActionFactory = viewActionFactory;
		this.editFilter = editFilter;
		this.filterService = filterService;
		this.filterOverview = filterOverview;
		this.builder = new ComponentBuilder();
		this.type = type;
		this.factory = new ConfiguratorFactory(type, editFilter);
		this.criterionSelectorPanel = builder.createPanel(700, 500);

		this.plusButton = new JButton();
		AbstractAction plusAct = new AbstractAction() {
			private static final long serialVersionUID = 5503627241676586871L;

			@Override
			public void actionPerformed(ActionEvent e) {
				gainMore();
			}

		};
		plusAct.putValue(Action.SMALL_ICON, new ImageIcon(getClass()
				.getResource("/images/plusButton.gif")));
		plusButton.setAction(plusAct);

		// set up GUI
		setUpCreate();

		this.add(criterionSelectorPanel, "wrap, h 400");

		// add plus button
		criterionSelectorPanel.add(plusButton, "wrap, gapleft 7, gaptop 10");

		// add controls
		JButton saveButton = new JButton();
		saveButton.setAction(new AbstractAction("Speichern") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				createFilter();
			}

		});

		JButton cancelButton = new JButton();
		Action cancelAction = this.viewActionFactory.getMainFilterViewAction();
		cancelAction.putValue(Action.NAME, "<html>Abbrechen</html>");
		cancelAction.putValue(Action.SMALL_ICON, null);
		cancelButton.setAction(cancelAction);
		this.controlButtonPanel = new JPanel();
		controlButtonPanel.add(saveButton);
		controlButtonPanel.add(cancelButton);

		add(controlButtonPanel);
	}

	private void setUpCreate() {

		// create headline
		if (editFilter == null) {
			headline = builder.createLabel("Neuen Filter f\u00FCr "
					+ type.getDisplayName() + " anlegen");
		} else {
			headline = builder.createLabel("Filter " + editFilter.getName()
					+ " bearbeiten");
		}
		headline.setFont(new Font("Headline", Font.PLAIN, 14));
		this.add(headline, "wrap, gapbottom 20");

		nameField = new CustomTextField(100);
		if (editFilter != null) {
			nameField.setText(editFilter.getName());
		}
		this.add(builder.createLabel("Filtername: "), "split 2");
		this.add(nameField, "wrap, gapbottom 20");
		// add(filterNamePanel, "wrap");

		// Combobox for filter privacy setting
		if ((editFilter != null) || editFilter == null) {
			privacyComboBox = new JComboBox<String>(
					FilterPrivacyStatus.toStringArray());
			this.add(privacyComboBox, "wrap");

			if (editFilter != null)
				privacyComboBox.setSelectedItem(editFilter.getPrivacyStatus()
						.toString());
		}

		// create operator picker panel
		this.operatorPicker = new JPanel();
		final JLabel operatorLabel = new JLabel(" Kriterien erf\u00FCllt sind:");
		operatorPicker.add(new JLabel("G\u00FCltig, wenn"));
		final JComboBox<String> operatorCombo = new JComboBox<String>(
				new SimpleComboBoxModel<String>("alle", "ein"));

		if (editFilter != null) {
			Criterion crit = editFilter.getCriterion();
			if (crit instanceof ConnectedCriterion) {
				ConnectedCriterion con = (ConnectedCriterion) crit;
				if (con.getLogicalOperator() == LogicalOperator.AND) {
					operatorCombo.setSelectedItem("alle");
					andOperator = true;
				} else if (con.getLogicalOperator() == LogicalOperator.OR) {
					operatorCombo.setSelectedItem("ein");
					andOperator = false;
					operatorLabel.setText(" Kriterium erf\u00FCllt ist:");
				}
			}
		}

		operatorCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String operator = (String) operatorCombo.getModel()
						.getSelectedItem();
				if ("alle".equals(operator)) {
					andOperator = true;
					operatorLabel.setText(" Kriterien erf\u00FCllt sind:");
				} else {
					andOperator = false;
					operatorLabel.setText(" Kriterium erf\u00FCllt ist:");
				}
				operatorLabel.repaint();
				operatorLabel.revalidate();
			}
		});
		operatorPicker.add(operatorCombo);
		operatorPicker.add(operatorLabel);
		add(operatorPicker, "wrap, gapbottom 10");

		if (editFilter == null) {
			// add default criterion selector
			gainMore();
		} else {
			renderCriterion(editFilter.getCriterion());
		}

	}

	public void gainMore() {
		newSelector(new CriterionSelector(factory.getConfigurators()));
	}

	private void newSelector(CriterionSelector selectorComp) {
		// add selector to panel and list
		selectors.add(selectorComp);

		JButton minusButton = new JButton();
		AbstractAction minusAct = new AbstractAction() {
			private static final long serialVersionUID = 5503627241676586871L;

			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelector(e.getSource());
			}

		};
		minusAct.putValue(Action.SMALL_ICON, new ImageIcon(getClass()
				.getResource("/images/minusButton.gif")));
		minusButton.setAction(minusAct);

		minusButtons.add(minusButton);
		criterionSelectorPanel.add(selectorComp, "w 700");
		criterionSelectorPanel.add(minusButton, "wrap");

		if (plusButton != null) {
			criterionSelectorPanel.remove(plusButton);
			criterionSelectorPanel
					.add(plusButton, "wrap, gapleft 7, gaptop 10");
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
					"Bitte geben Sie einen Namen f\u00FCr den Filter an.",
					"Warn", JOptionPane.WARNING_MESSAGE);
			nameField.invalidateInput();
			return;
		}

		// create filter
		FilterTO filter = new FilterTO();
		filter.setType(type);
		filter.setOperator(andOperator ? LogicalOperator.AND
				: LogicalOperator.OR);
		filter.setName(nameField.getText());
		filter.setPrivacyStatus(FilterPrivacyStatus.getByName(privacyComboBox
				.getSelectedItem().toString()));

		try {
			List<Criterion> crits = new ArrayList<Criterion>();
			for (CriterionSelector sel : selectors) {
				crits.add(sel.toCriterionTO());
			}

			filter.setCriterions(crits);

			if (editFilter != null) {
				filterService.update(editFilter, filter);
			} else {
				filterService.create(filter);
			}

			JOptionPane.showMessageDialog(this,
					"Filter erfolgreich gespeichert!", "Info",
					JOptionPane.INFORMATION_MESSAGE);
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

	private void returnTo() {
		this.removeAll();
		this.revalidate();
		this.repaint();
		filterOverview.removeAll();
		filterOverview.revalidate();
		filterOverview.repaint();
		filterOverview.init();
	}

	private void renderCriterion(Criterion crit) {
		if (crit == null) {
			return;
		}
		if (crit instanceof ConnectedCriterion) {
			ConnectedCriterion con = (ConnectedCriterion) crit;
			renderCriterion(con.getOperand1());
			renderCriterion(con.getOperand2());
		} else {
			newSelector(new CriterionSelector(factory.getConfigurators(), crit));
		}
	}
}
