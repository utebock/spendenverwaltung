package at.fraubock.spendenverwaltung.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.views.HistoryView;
import at.fraubock.spendenverwaltung.util.ActionAttribute;

public class HistorySearchPanel extends JPanel {
	private static final long serialVersionUID = 5351279341286482636L;

	private StringTextField textField;
	private JComboBox<ActionAttribute> attributes;
	private HistoryView view;

	public HistorySearchPanel(HistoryView viewParam) {
		setLayout(new MigLayout());
		this.view = viewParam;
		add(new JLabel("Suche: "));

		add(textField = new StringTextField(ComponentConstants.SHORT_TEXT));
		textField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				view.applySearch(
						(ActionAttribute) attributes.getSelectedItem(),
						textField.getText());
			}

		});

		add(new JLabel(" enthalten in "));

		add(attributes = new JComboBox<ActionAttribute>(
				new SimpleComboBoxModel<ActionAttribute>(
						ActionAttribute.values())));
		
		attributes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				view.applySearch(
						(ActionAttribute) attributes.getSelectedItem(),
						textField.getText());
			}
		});
	}

}
