package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.HistorySearchPanel;
import at.fraubock.spendenverwaltung.gui.components.HistoryTableModel;
import at.fraubock.spendenverwaltung.gui.components.PageNavigator;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IActionService;
import at.fraubock.spendenverwaltung.util.ActionSearchVO;
import at.fraubock.spendenverwaltung.util.Pager;

public class HistoryView extends InitializableView {
	private static final long serialVersionUID = 4106122970891943776L;
	private static final Logger log = Logger.getLogger(HistoryView.class);

	private final int MAX_TABLE_SIZE = 20;

	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
	private JPanel panel;
	private JTable historyTable;
	private JScrollPane scrollPane;
	private HistoryTableModel historyModel;
	private IActionService actionService;
	private PageNavigator navigator;

	public HistoryView(ViewActionFactory viewActionFactory,
			IActionService actionService) {
		this.setLayout(new MigLayout());
		this.viewActionFactory = viewActionFactory;
		this.actionService = actionService;
	}

	@Override
	public void init() {
		componentFactory = new ComponentFactory();
		panel = componentFactory.createPanel(750, 800);
		this.add(panel);

		JButton backButton = new JButton();
		javax.swing.Action getBack = viewActionFactory.getMainMenuViewAction();
		getBack.putValue(javax.swing.Action.NAME, "Abbrechen");
		getBack.putValue(javax.swing.Action.SMALL_ICON, new ImageIcon(
				getClass().getResource("/images/backInButton.png")));
		backButton.setAction(getBack);
		backButton.setFont(new Font("Bigger", Font.PLAIN, 13));

		JLabel headline = componentFactory.createLabel("Verlauf aller Aktionen");
		headline.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(headline, "wrap, gapbottom 20");

		panel.add(new HistorySearchPanel(this), "wrap");

		historyTable = new JTable(historyModel = new HistoryTableModel());
		historyTable.setFillsViewportHeight(true);
		historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		historyTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2) {
					Action a = historyModel.getActionRow(historyTable
							.getSelectedRow());

					JOptionPane.showMessageDialog(HistoryView.this,
							a.getPayload(), "Details",
							JOptionPane.INFORMATION_MESSAGE);

				}

			}
		});

		historyTable.getColumnModel().getColumn(0).setMinWidth(200);
		historyTable.getColumnModel().getColumn(0).setMaxWidth(200);

		historyTable.getColumnModel().getColumn(1).setMinWidth(150);
		historyTable.getColumnModel().getColumn(1).setMaxWidth(150);

		historyTable.getColumnModel().getColumn(2).setMinWidth(210);
		historyTable.getColumnModel().getColumn(2).setMaxWidth(210);

		historyTable.getColumnModel().getColumn(3).setMinWidth(150);
		historyTable.getColumnModel().getColumn(3).setMaxWidth(150);

		scrollPane = new JScrollPane(historyTable);
		scrollPane.setPreferredSize(new Dimension(700,
				MAX_TABLE_SIZE * 18 - 17));
		panel.add(scrollPane, "wrap");

		panel.add(navigator = new PageNavigator(historyModel), "growx, wrap 20px");
		
		//backbutton
		panel.add(backButton);
		applyExtendedSearch(new ActionSearchVO());
	}

	public void applyExtendedSearch(ActionSearchVO searchVO) {
		Pager<Action> pager;
		try {
			pager = actionService.searchActions(searchVO, MAX_TABLE_SIZE);
			historyModel.setPager(pager);
			historyModel.refreshPage();
			navigator.modelRefreshed();
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this,
					"Ein unerwarteter Fehler ist aufgetreten.", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			log.error("Error when loading all actions as pager or refreshing page model: "
					+ e.getMessage());
		}
	}

	public void showActions(final List<Action> list) {

		Pager<Action> pager = new Pager<Action>() {

			private int currentPos = 0;

			@Override
			public List<Action> getPage(int index) throws ServiceException {
				currentPos = index;
				List<Action> result = new ArrayList<Action>();

				for (int i = index * MAX_TABLE_SIZE; i < index * MAX_TABLE_SIZE
						+ MAX_TABLE_SIZE
						&& i < list.size(); i++) {
					result.add(list.get(i));
				}

				return result;
			}

			@Override
			public int getCurrentPosition() {
				return currentPos;
			}

			@Override
			public long getNumberOfPages() throws ServiceException {
				long count = list.size();

				long mod = count % MAX_TABLE_SIZE;

				return mod == 0 ? (long) count / MAX_TABLE_SIZE
						: (((long) count / MAX_TABLE_SIZE) + 1);
			}

		};
		historyModel.setPager(pager);
		try {
			historyModel.refreshPage();
		} catch (ServiceException e) {
			// should never happen since this pager can't throw a service
			// exception
		}
		navigator.modelRefreshed();
	}
}