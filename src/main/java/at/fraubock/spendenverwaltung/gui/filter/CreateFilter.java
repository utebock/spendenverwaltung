package at.fraubock.spendenverwaltung.gui.filter;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.ActionHandler;
import at.fraubock.spendenverwaltung.gui.ButtonListener;
import at.fraubock.spendenverwaltung.gui.ComponentBuilder;
import at.fraubock.spendenverwaltung.gui.FilterOverview;
import at.fraubock.spendenverwaltung.gui.FilterTableModel;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.service.to.CriterionTO;
import at.fraubock.spendenverwaltung.service.to.FilterTO;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class CreateFilter extends JPanel {

	private static final long serialVersionUID = 1L;
	private IFilterService filterService;
	private FilterOverview filterOverview;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private ActionHandler actionHandler;
	private FilterTableModel filterModel;
	private JPanel panel;
	private JLabel headline;
	private JLabel empty;
	private JLabel nameLabel;
	private JTextField nameField;
	private JButton ok;
	private JButton cancel;
	private JPanel panel2;
	private String pluralName;
	private IPropertySelectorProvider modelReceiver;
	private List<SelectorGuiComponent> selectorComponents;
	private FilterType type;

	public CreateFilter(FilterType type, String pluralName,
			IPropertySelectorProvider modelReceiver,
			IFilterService filterService, FilterOverview filterOverview) {

		super(new MigLayout());
		this.filterService = filterService;

		this.filterOverview = filterOverview;
		this.filterModel = this.filterOverview.getFilterModel();

		this.selectorComponents = new ArrayList<SelectorGuiComponent>();

		this.type = type;
		this.pluralName = pluralName;
		this.modelReceiver = modelReceiver;

		buttonListener = new ButtonListener(this);
		actionHandler = new ActionHandler(this);
		builder = new ComponentBuilder();

		panel = builder.createPanel(800, 300);
		panel2 = builder.createPanel(800, 300);

		this.add(panel, "wrap, span 2");
		setUpCreate();

		ok = builder.createButton("Anlegen", buttonListener,
				"create_filter_in_db");
		add(ok);

		cancel = builder.createButton("Abbrechen", buttonListener,
				"cancel_filter_in_db");
		add(cancel, "wrap");
	}

	private void setUpCreate() {
		headline = builder.createLabel("Neuen Filter f\u00FCr " + pluralName
				+ " anlegen:");
		headline.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(headline, "wrap");
		empty = builder.createLabel("		");
		panel.add(empty, "wrap");

		nameLabel = builder.createLabel("Filtername: ");
		nameField = builder.createTextField(150);
		panel.add(nameLabel, "split 2");
		panel.add(nameField, "growx, wrap");
		panel.add(panel2);

		addPropertySelector(0);

	}

	public void gainMore(Object button) {
		int index = 0;
		for (SelectorGuiComponent sel : selectorComponents) {
			if (sel.getPlusButton() == (JButton) button) {
				index = selectorComponents.indexOf(sel);
			}
		}

		SelectorGuiComponent selectorComp = addPropertySelector(index + 1);
		selectorComp.setPicker(new LogicalOperatorPicker());
		repaint();
		revalidate();
	}

	public void removeSelector(Object button) {
		int index = 0;
		for (SelectorGuiComponent sel : selectorComponents) {
			if (sel.getMinusButton() == (JButton) button) {
				index = selectorComponents.indexOf(sel);
				break;
			}
		}
		if (index == 0 && selectorComponents.size() > 1) {
			// this is the first selector and there follows another one. remove
			// his operator picker
			selectorComponents.get(index + 1).setPicker(null);
		}
		selectorComponents.remove(index);
		panel2.remove(index);

		repaint();
		revalidate();
	}

	/*
	 * index is the position at which the selector will be placed
	 */
	private SelectorGuiComponent addPropertySelector(int index) {
		SelectorGuiComponent selectorComp = new SelectorGuiComponent(
				new PropertySelector(modelReceiver.receiveModels()),
				builder.createImageButton("/images/plusButton.gif",
						buttonListener, "plusButton_create_filter"),
				builder.createImageButton("/images/minusButton.gif",
						buttonListener, "minusButton_create_filter"));

		selectorComponents.add(index, selectorComp);
		panel2.add(selectorComp, "wrap", index);
		return selectorComp;
	}

	public void createFilter() {
		List<CriterionTO> crits = new ArrayList<CriterionTO>();
		List<LogicalOperator> ops = new ArrayList<LogicalOperator>();

		for (SelectorGuiComponent sel : selectorComponents) {
			crits.add(sel.getSelector().toCriterionTO());
			ops.add(sel.getPicker().getPickedOperator());
		}

		FilterTO filter = new FilterTO();
		filter.setType(type);
		filter.setCriterions(crits);
		filter.setOperators(ops);
		filter.setName(nameField.getText());

		try {
			filterService.create(filter);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	/*
	 * component that holds all the components for a property selector (that is
	 * the selector itself, plus- and minusbutton, and the logical operator
	 * dropdown
	 */
	public class SelectorGuiComponent extends JPanel {
		private static final long serialVersionUID = -2885373338561255928L;

		/* might be null for the very first property selector */
		private LogicalOperatorPicker picker;
		private PropertySelector selector;
		private JButton plusButton;
		private JButton minusButton;

		public SelectorGuiComponent(PropertySelector selector,
				JButton plusButton, JButton minusButton) {
			super(new MigLayout());
			this.selector = selector;
			this.plusButton = plusButton;
			this.minusButton = minusButton;
			add(selector);
			add(plusButton);
			add(minusButton);
		}

		public PropertySelector getSelector() {
			return selector;
		}

		public JButton getPlusButton() {
			return plusButton;
		}

		public JButton getMinusButton() {
			return minusButton;
		}

		public LogicalOperatorPicker getPicker() {
			return picker;
		}

		public void setPicker(LogicalOperatorPicker picker) {
			if (picker == null) {
				remove(this.picker);
				this.picker = null;
			} else {
				this.picker = picker;
				add(this.picker, "wrap", 0);
			}
		}
	}
}
