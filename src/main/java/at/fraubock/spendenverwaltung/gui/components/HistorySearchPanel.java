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

		JLabel extended = new JLabel("Suche:");
//		extended.addMouseListener(new MouseListener() {
//
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent arg0) {
//				HistorySearchPanel.this
//						.setCursor(new Cursor(Cursor.HAND_CURSOR));
//			}
//
//			@Override
//			public void mouseExited(MouseEvent arg0) {
//				HistorySearchPanel.this.setCursor(new Cursor(
//						Cursor.DEFAULT_CURSOR));
//			}
//
//			@Override
//			public void mousePressed(MouseEvent arg0) {
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent arg0) {
//				view.showExtendedSearch(true);
//			}
//		});
//		extended.setFont(new Font("Search", Font.PLAIN, 11));
		add(extended, "wrap,gapbottom 10");

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
